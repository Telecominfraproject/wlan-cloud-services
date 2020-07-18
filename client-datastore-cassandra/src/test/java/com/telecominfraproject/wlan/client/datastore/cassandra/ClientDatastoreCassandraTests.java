package com.telecominfraproject.wlan.client.datastore.cassandra;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;

import com.telecominfraproject.wlan.client.datastore.BaseClientDatastoreTest;
import com.telecominfraproject.wlan.client.info.models.ClientInfoDetails;
import com.telecominfraproject.wlan.client.models.Client;
import com.telecominfraproject.wlan.client.session.models.ClientSession;
import com.telecominfraproject.wlan.client.session.models.ClientSessionDetails;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pagination.SortOrder;
import com.telecominfraproject.wlan.core.server.cassandra.BaseCassandraDataSource;
import com.telecominfraproject.wlan.core.server.cassandra.test.BaseCassandraTest;

/**
 * @author dtoptygin
 * NOTE: these tests require an cluster of Cassandra nodes to be up. Need to find a solution for small in-memory cluster for unit tests.
 * We'll not run these tests during the CI/CD build for now, they need to be manually executed.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = BaseCassandraTest.Config.class)
@Import(value = { ClientDatastoreCassandra.class, ClientDAO.class, ClientSessionDAO.class, BaseCassandraTest.Config.class, BaseCassandraDataSource.class })
@Ignore("Ignore Cassandra Tests until we can set up a cassandra cluster for the integration testing")
public class ClientDatastoreCassandraTests extends BaseClientDatastoreTest {
	
    @Test
    public void testClientPagination()
    {
       //create 100 Clients
       Client mdl;
       int customerId_1 = (int) testSequence.incrementAndGet();
       int customerId_2 = (int) testSequence.incrementAndGet();
       
       List<Client> created_models = new ArrayList<>();
       
       int apNameIdx = 0;
       
       for(int i = 0; i< 50; i++){
           mdl = new Client();
           mdl.setCustomerId(customerId_1);
           ClientInfoDetails details = new ClientInfoDetails();
           details.setAlias("qr_"+apNameIdx);
           mdl.setDetails(details );
           mdl.setMacAddress(new MacAddress((long)i));

           apNameIdx++;
           mdl = testInterface.create(mdl);
           created_models.add(mdl);
       }

       for(int i = 0; i< 50; i++){
           mdl = new Client();
           mdl.setCustomerId(customerId_2);
           ClientInfoDetails details = new ClientInfoDetails();
           details.setAlias("qr_"+apNameIdx);
           mdl.setDetails(details );
           mdl.setMacAddress(new MacAddress((long)i));
           
           apNameIdx++;
           mdl = testInterface.create(mdl);
           created_models.add(mdl);
       }

       //paginate over Clients
       
       List<ColumnAndSort> sortBy = new ArrayList<>();
       //Cassandra has limited support for sorting. Not going to sort the results
       //sortBy.addAll(Arrays.asList(new ColumnAndSort("macAddress")));
       
       PaginationContext<Client> context = new PaginationContext<>(10);
       PaginationResponse<Client> page1 = testInterface.getForCustomer(customerId_1, sortBy, context);
       PaginationResponse<Client> page2 = testInterface.getForCustomer(customerId_1, sortBy, page1.getContext());
       PaginationResponse<Client> page3 = testInterface.getForCustomer(customerId_1, sortBy, page2.getContext());
       PaginationResponse<Client> page4 = testInterface.getForCustomer(customerId_1, sortBy, page3.getContext());
       PaginationResponse<Client> page5 = testInterface.getForCustomer(customerId_1, sortBy, page4.getContext());
       PaginationResponse<Client> page6 = testInterface.getForCustomer(customerId_1, sortBy, page5.getContext());
       PaginationResponse<Client> page7 = testInterface.getForCustomer(customerId_1, sortBy, page6.getContext());
       
       //verify returned pages
       assertEquals(10, page1.getItems().size());
       assertEquals(10, page2.getItems().size());
       assertEquals(10, page3.getItems().size());
       assertEquals(10, page4.getItems().size());
       assertEquals(10, page5.getItems().size());
       
       page1.getItems().forEach(e -> assertEquals(customerId_1, e.getCustomerId()) );
       page2.getItems().forEach(e -> assertEquals(customerId_1, e.getCustomerId()) );
       page3.getItems().forEach(e -> assertEquals(customerId_1, e.getCustomerId()) );
       page4.getItems().forEach(e -> assertEquals(customerId_1, e.getCustomerId()) );
       page5.getItems().forEach(e -> assertEquals(customerId_1, e.getCustomerId()) );
       
       assertEquals(0, page6.getItems().size());
       assertEquals(0, page7.getItems().size());
       
       assertFalse(page1.getContext().isLastPage());
       assertFalse(page2.getContext().isLastPage());
       assertFalse(page3.getContext().isLastPage());
       assertFalse(page4.getContext().isLastPage());
       assertFalse(page5.getContext().isLastPage());
       
       assertTrue(page6.getContext().isLastPage());
       assertTrue(page7.getContext().isLastPage());
       
       //sort order is weird but consistent
       List<String> expectedPage3Strings = new ArrayList<	>(Arrays.asList(new String[]{"qr_48", "qr_30", "qr_31", "qr_12", "qr_8", "qr_9", "qr_17", "qr_49", "qr_14", "qr_43" }));
       List<String> actualPage3Strings = new ArrayList<>();
       page3.getItems().stream().forEach( ce -> actualPage3Strings.add(((ClientInfoDetails)ce.getDetails()).getAlias()) );
       
       assertEquals(expectedPage3Strings, actualPage3Strings);
       
       //test first page of the results with empty sort order -> sort order is weird but consistent
       PaginationResponse<Client> page1EmptySort = testInterface.getForCustomer(customerId_1, Collections.emptyList(), context);
       assertEquals(10, page1EmptySort.getItems().size());

       List<String> expectedPage1EmptySortStrings = new ArrayList<>(Arrays.asList(new String[]{"qr_29", "qr_44", "qr_13", "qr_23", "qr_47", "qr_3", "qr_36", "qr_33", "qr_11", "qr_45" }));
       List<String> actualPage1EmptySortStrings = new ArrayList<>();
       page1EmptySort.getItems().stream().forEach( ce -> actualPage1EmptySortStrings.add(((ClientInfoDetails)ce.getDetails()).getAlias()) );

       assertEquals(expectedPage1EmptySortStrings, actualPage1EmptySortStrings);

       //test first page of the results with null sort order -> sort order is weird but consistent
       PaginationResponse<Client> page1NullSort = testInterface.getForCustomer(customerId_1, null, context);
       assertEquals(10, page1NullSort.getItems().size());

       List<String> expectedPage1NullSortStrings = new ArrayList<>(expectedPage1EmptySortStrings);
       List<String> actualPage1NullSortStrings = new ArrayList<>();
       page1NullSort.getItems().stream().forEach( ce -> actualPage1NullSortStrings.add(((ClientInfoDetails)ce.getDetails()).getAlias()) );

       assertEquals(expectedPage1NullSortStrings, actualPage1NullSortStrings);

       
       //test first page of the results with sort descending order by a macAddress property -> sort order is weird but consistent
       PaginationResponse<Client> page1SingleSortDesc = testInterface.getForCustomer(customerId_1, Collections.singletonList(new ColumnAndSort("macAddress", SortOrder.desc)), context);
       assertEquals(10, page1SingleSortDesc.getItems().size());

       List<String> expectedPage1SingleSortDescStrings = new ArrayList<>(expectedPage1EmptySortStrings);
       List<String> actualPage1SingleSortDescStrings = new ArrayList<>();
       page1SingleSortDesc.getItems().stream().forEach( ce -> actualPage1SingleSortDescStrings.add(((ClientInfoDetails)ce.getDetails()).getAlias()) );
       
       assertEquals(expectedPage1SingleSortDescStrings, actualPage1SingleSortDescStrings);

       created_models.forEach(m -> testInterface.delete(m.getCustomerId(), m.getMacAddress()));
       
    }
    
    
    @Test
    public void testClientSessionPagination()
    {
       //create 100 Client sessions
       ClientSession mdl;
       int customerId_1 = (int) testSequence.incrementAndGet();
       int customerId_2 = (int) testSequence.incrementAndGet();
       long equipmentId_1 = testSequence.incrementAndGet();
       long equipmentId_2 = testSequence.incrementAndGet();
       long locationId_1 = testSequence.incrementAndGet();
       long locationId_2 = testSequence.incrementAndGet();
       
       int apNameIdx = 0;
       
       List<ClientSession> sessionsToCreate = new ArrayList<>();
       
       for(int i = 0; i< 50; i++){
           mdl = new ClientSession();
           mdl.setCustomerId(customerId_1);
           if(i<10) {
        	   mdl.setEquipmentId(equipmentId_1);
        	   mdl.setLocationId(locationId_1);
           } else if(i<20) {
        	   mdl.setEquipmentId(equipmentId_2);
        	   mdl.setLocationId(locationId_2);
           } else {
        	   mdl.setEquipmentId(testSequence.incrementAndGet());
           }
           
           ClientSessionDetails details = new ClientSessionDetails();
           details.setApFingerprint("qr_"+apNameIdx);
           mdl.setDetails(details );
           mdl.setMacAddress(new MacAddress((long)i));

           apNameIdx++;
           
           sessionsToCreate.add(mdl);
       }
       
       List<ClientSession> createdList = testInterface.updateSessions(sessionsToCreate);
       List<ClientSessionDetails> createdDetailsList = new ArrayList<>();
       List<ClientSessionDetails> detailsToCreateList = new ArrayList<>();
       
       sessionsToCreate.forEach(s -> detailsToCreateList.add(s.getDetails()));
       createdList.forEach(s -> createdDetailsList.add(s.getDetails()));       
       assertEquals(detailsToCreateList, createdDetailsList);
       

       List<ClientSession> createdListExtra = new ArrayList<>(); 
       
       for(int i = 0; i< 50; i++){
           mdl = new ClientSession();
           mdl.setCustomerId(customerId_2);
    	   mdl.setEquipmentId(testSequence.incrementAndGet());
    	   mdl.setLocationId(testSequence.incrementAndGet());
           ClientSessionDetails details = new ClientSessionDetails();
           details.setApFingerprint("qr_"+apNameIdx);
           mdl.setDetails(details );
           mdl.setMacAddress(new MacAddress((long)i));
           
           apNameIdx++;
           mdl = testInterface.updateSession(mdl);
           createdListExtra.add(mdl);
       }

       //paginate over Clients
       
       List<ColumnAndSort> sortBy = new ArrayList<>();
       //Cassandra has limited support for sorting, we're not using that feature
       //sortBy.addAll(Arrays.asList(new ColumnAndSort("macAddress")));
       
       PaginationContext<ClientSession> context = new PaginationContext<>(10);
       PaginationResponse<ClientSession> page1 = testInterface.getSessionsForCustomer(customerId_1, null, null, sortBy, context);
       PaginationResponse<ClientSession> page2 = testInterface.getSessionsForCustomer(customerId_1, null, null, sortBy, page1.getContext());
       PaginationResponse<ClientSession> page3 = testInterface.getSessionsForCustomer(customerId_1, null, null, sortBy, page2.getContext());
       PaginationResponse<ClientSession> page4 = testInterface.getSessionsForCustomer(customerId_1, null, null, sortBy, page3.getContext());
       PaginationResponse<ClientSession> page5 = testInterface.getSessionsForCustomer(customerId_1, null, null, sortBy, page4.getContext());
       PaginationResponse<ClientSession> page6 = testInterface.getSessionsForCustomer(customerId_1, null, null, sortBy, page5.getContext());
       PaginationResponse<ClientSession> page7 = testInterface.getSessionsForCustomer(customerId_1, null, null, sortBy, page6.getContext());
       
       //verify returned pages
       assertEquals(10, page1.getItems().size());
       assertEquals(10, page2.getItems().size());
       assertEquals(10, page3.getItems().size());
       assertEquals(10, page4.getItems().size());
       assertEquals(10, page5.getItems().size());
       
       page1.getItems().forEach(e -> assertEquals(customerId_1, e.getCustomerId()) );
       page2.getItems().forEach(e -> assertEquals(customerId_1, e.getCustomerId()) );
       page3.getItems().forEach(e -> assertEquals(customerId_1, e.getCustomerId()) );
       page4.getItems().forEach(e -> assertEquals(customerId_1, e.getCustomerId()) );
       page5.getItems().forEach(e -> assertEquals(customerId_1, e.getCustomerId()) );
       
       assertEquals(0, page6.getItems().size());
       assertEquals(0, page7.getItems().size());
       
       assertFalse(page1.getContext().isLastPage());
       assertFalse(page2.getContext().isLastPage());
       assertFalse(page3.getContext().isLastPage());
       assertFalse(page4.getContext().isLastPage());
       assertFalse(page5.getContext().isLastPage());
       
       assertTrue(page6.getContext().isLastPage());
       assertTrue(page7.getContext().isLastPage());
       
       Set<String> expectedAllPagesStrings = new HashSet<>();
       for(int i=0; i<50; i++) {
    	   expectedAllPagesStrings.add("qr_"+i);
       }
       Set<String> actualPage3Strings = new HashSet<>();
       
       page1.getItems().stream().forEach( ce -> actualPage3Strings.add(((ClientSessionDetails)ce.getDetails()).getApFingerprint()) );
       page2.getItems().stream().forEach( ce -> actualPage3Strings.add(((ClientSessionDetails)ce.getDetails()).getApFingerprint()) );
       page3.getItems().stream().forEach( ce -> actualPage3Strings.add(((ClientSessionDetails)ce.getDetails()).getApFingerprint()) );
       page4.getItems().stream().forEach( ce -> actualPage3Strings.add(((ClientSessionDetails)ce.getDetails()).getApFingerprint()) );
       page5.getItems().stream().forEach( ce -> actualPage3Strings.add(((ClientSessionDetails)ce.getDetails()).getApFingerprint()) );
       
       assertEquals(expectedAllPagesStrings, actualPage3Strings);

       
       PaginationContext<ClientSession> context100 = new PaginationContext<>(100);
       //test first page of the results with empty sort order -> sort order is weird but consistent
       PaginationResponse<ClientSession> page1EmptySort = testInterface.getSessionsForCustomer(customerId_1, null, null, Collections.emptyList(), context100);
       assertEquals(50, page1EmptySort.getItems().size());

       Set<String> expectedPage1EmptySortStrings = new HashSet<>(expectedAllPagesStrings);
       
       Set<String> actualPage1EmptySortStrings = new HashSet<>();
       page1EmptySort.getItems().stream().forEach( ce -> actualPage1EmptySortStrings.add(((ClientSessionDetails)ce.getDetails()).getApFingerprint()) );

       assertEquals(expectedPage1EmptySortStrings, actualPage1EmptySortStrings);

       //test first page of the results with null sort order -> sort order is weird but consistent
       PaginationResponse<ClientSession> page1NullSort = testInterface.getSessionsForCustomer(customerId_1, null, null, null, context100);
       assertEquals(50, page1NullSort.getItems().size());

       Set<String> expectedPage1NullSortStrings = new HashSet<>(expectedPage1EmptySortStrings);
       Set<String> actualPage1NullSortStrings = new HashSet<>();
       page1NullSort.getItems().stream().forEach( ce -> actualPage1NullSortStrings.add(((ClientSessionDetails)ce.getDetails()).getApFingerprint()) );

       assertEquals(expectedPage1NullSortStrings, actualPage1NullSortStrings);

       
       //test first page of the results with sort descending order by a macAddress property -> sort order is weird but consistent
       PaginationResponse<ClientSession> page1SingleSortDesc = testInterface.getSessionsForCustomer(customerId_1, null, null, Collections.singletonList(new ColumnAndSort("macAddress", SortOrder.desc)), context100);
       assertEquals(50, page1SingleSortDesc.getItems().size());

       Set<String> expectedPage1SingleSortDescStrings = new HashSet<>(expectedPage1EmptySortStrings);
       Set<String> actualPage1SingleSortDescStrings = new HashSet<>();
       page1SingleSortDesc.getItems().stream().forEach( ce -> actualPage1SingleSortDescStrings.add(((ClientSessionDetails)ce.getDetails()).getApFingerprint()) );
       
       assertEquals(expectedPage1SingleSortDescStrings, actualPage1SingleSortDescStrings);

       //test the results for equipment_1 only
       PaginationResponse<ClientSession> page1Eq_1 = testInterface.getSessionsForCustomer(customerId_1, new HashSet<Long>(Arrays.asList(equipmentId_1)), null, Collections.emptyList(), context);
       PaginationResponse<ClientSession> page2Eq_1 = testInterface.getSessionsForCustomer(customerId_1, new HashSet<Long>(Arrays.asList(equipmentId_1)), null, Collections.emptyList(), page1Eq_1.getContext());

       assertEquals(10, page1Eq_1.getItems().size());
       assertEquals(0, page2Eq_1.getItems().size());
       assertFalse(page1Eq_1.getContext().isLastPage());
       assertTrue(page2Eq_1.getContext().isLastPage());
       
       List<String> expectedPage1Eq_1Strings = new ArrayList<>(Arrays.asList(new String[]{"qr_0", "qr_1", "qr_2", "qr_3", "qr_4", "qr_5", "qr_6", "qr_7", "qr_8", "qr_9" }));
       List<String> actualPage1Eq_1Strings = new ArrayList<>();
       page1Eq_1.getItems().stream().forEach( ce -> actualPage1Eq_1Strings.add(((ClientSessionDetails)ce.getDetails()).getApFingerprint()) );

       assertEquals(expectedPage1Eq_1Strings, actualPage1Eq_1Strings);

       //test the results for equipment_2 only
       PaginationResponse<ClientSession> page1Eq_2 = testInterface.getSessionsForCustomer(customerId_1, new HashSet<Long>(Arrays.asList(equipmentId_2)), null, Collections.emptyList(), context);
       PaginationResponse<ClientSession> page2Eq_2 = testInterface.getSessionsForCustomer(customerId_1, new HashSet<Long>(Arrays.asList(equipmentId_2)), null, Collections.emptyList(), page1Eq_2.getContext());

       assertEquals(10, page1Eq_2.getItems().size());
       assertEquals(0, page2Eq_2.getItems().size());
       assertFalse(page1Eq_2.getContext().isLastPage());
       assertTrue(page2Eq_2.getContext().isLastPage());
       
       List<String> expectedPage1Eq_2Strings = new ArrayList<>(Arrays.asList(new String[]{"qr_10", "qr_11", "qr_12", "qr_13", "qr_14", "qr_15", "qr_16", "qr_17", "qr_18", "qr_19"}));
       List<String> actualPage1Eq_2Strings = new ArrayList<>();
       page1Eq_2.getItems().stream().forEach( ce -> actualPage1Eq_2Strings.add(((ClientSessionDetails)ce.getDetails()).getApFingerprint()) );

       assertEquals(expectedPage1Eq_2Strings, actualPage1Eq_2Strings);

        
       //test the results for equipment_1 or  equipment_2 only
       PaginationResponse<ClientSession> page1Eq_1or2 = testInterface.getSessionsForCustomer(customerId_1, new HashSet<Long>(Arrays.asList(equipmentId_1, equipmentId_2)), null, Collections.emptyList(), context);
       PaginationResponse<ClientSession> page2Eq_1or2 = testInterface.getSessionsForCustomer(customerId_1, new HashSet<Long>(Arrays.asList(equipmentId_1, equipmentId_2)), null, Collections.emptyList(), page1Eq_1or2.getContext());
       PaginationResponse<ClientSession> page3Eq_1or2 = testInterface.getSessionsForCustomer(customerId_1, new HashSet<Long>(Arrays.asList(equipmentId_1, equipmentId_2)), null, Collections.emptyList(), page2Eq_1or2.getContext());

       assertEquals(10, page1Eq_1or2.getItems().size());
       assertEquals(10, page2Eq_1or2.getItems().size());
       assertEquals(0, page3Eq_1or2.getItems().size());
       assertFalse(page1Eq_1or2.getContext().isLastPage());
       assertFalse(page2Eq_1or2.getContext().isLastPage());
       assertTrue(page3Eq_1or2.getContext().isLastPage());
       
       List<String> expectedPage1Eq_1or2Strings = new ArrayList<>(Arrays.asList(new String[]{"qr_0", "qr_1", "qr_2", "qr_3", "qr_4", "qr_5", "qr_6", "qr_7", "qr_8", "qr_9" }));
       List<String> actualPage1Eq_1or2Strings = new ArrayList<>();
       page1Eq_1or2.getItems().stream().forEach( ce -> actualPage1Eq_1or2Strings.add(((ClientSessionDetails)ce.getDetails()).getApFingerprint()) );

       assertEquals(expectedPage1Eq_1or2Strings, actualPage1Eq_1or2Strings);

       List<String> expectedPage2Eq_1or2Strings = new ArrayList<>(Arrays.asList(new String[]{"qr_10", "qr_11", "qr_12", "qr_13", "qr_14", "qr_15", "qr_16", "qr_17", "qr_18", "qr_19" }));
       List<String> actualPage2Eq_1or2Strings = new ArrayList<>();
       page2Eq_1or2.getItems().stream().forEach( ce -> actualPage2Eq_1or2Strings.add(((ClientSessionDetails)ce.getDetails()).getApFingerprint()) );

       assertEquals(expectedPage2Eq_1or2Strings, actualPage2Eq_1or2Strings);

       //
       // test filters on locationIds
       //
       
       //test the results for location_1 only
       page1Eq_1 = testInterface.getSessionsForCustomer(customerId_1, null, new HashSet<Long>(Arrays.asList(locationId_1)), Collections.emptyList(), context);
       page2Eq_1 = testInterface.getSessionsForCustomer(customerId_1, null, new HashSet<Long>(Arrays.asList(locationId_1)), Collections.emptyList(), page1Eq_1.getContext());

       assertEquals(10, page1Eq_1.getItems().size());
       assertEquals(0, page2Eq_1.getItems().size());
       assertFalse(page1Eq_1.getContext().isLastPage());
       assertTrue(page2Eq_1.getContext().isLastPage());
       
       expectedPage1Eq_1Strings = new ArrayList<>(Arrays.asList(new String[]{"qr_0", "qr_1", "qr_2", "qr_3", "qr_4", "qr_5", "qr_6", "qr_7", "qr_8", "qr_9" }));
       List<String> actualPage1Loc_1Strings = new ArrayList<>();
       page1Eq_1.getItems().stream().forEach( ce -> actualPage1Loc_1Strings.add(((ClientSessionDetails)ce.getDetails()).getApFingerprint()) );

       assertEquals(expectedPage1Eq_1Strings, actualPage1Loc_1Strings);

       //test the results for location_2 only
       page1Eq_2 = testInterface.getSessionsForCustomer(customerId_1, null, new HashSet<Long>(Arrays.asList(locationId_2)), Collections.emptyList(), context);
       page2Eq_2 = testInterface.getSessionsForCustomer(customerId_1, null, new HashSet<Long>(Arrays.asList(locationId_2)), Collections.emptyList(), page1Eq_2.getContext());

       assertEquals(10, page1Eq_2.getItems().size());
       assertEquals(0, page2Eq_2.getItems().size());
       assertFalse(page1Eq_2.getContext().isLastPage());
       assertTrue(page2Eq_2.getContext().isLastPage());
       
       expectedPage1Eq_2Strings = new ArrayList<>(Arrays.asList(new String[]{"qr_10", "qr_11", "qr_12", "qr_13", "qr_14", "qr_15", "qr_16", "qr_17", "qr_18", "qr_19"}));
       List<String> actualPage1Loc_2Strings = new ArrayList<>();
       page1Eq_2.getItems().stream().forEach( ce -> actualPage1Loc_2Strings.add(((ClientSessionDetails)ce.getDetails()).getApFingerprint()) );

       assertEquals(expectedPage1Eq_2Strings, actualPage1Loc_2Strings);

        
       //test the results for location_1 or  location_2 only
       page1Eq_1or2 = testInterface.getSessionsForCustomer(customerId_1, null, new HashSet<Long>(Arrays.asList(locationId_1, locationId_2)), Collections.emptyList(), context);
       page2Eq_1or2 = testInterface.getSessionsForCustomer(customerId_1, null, new HashSet<Long>(Arrays.asList(locationId_1, locationId_2)), Collections.emptyList(), page1Eq_1or2.getContext());
       page3Eq_1or2 = testInterface.getSessionsForCustomer(customerId_1, null, new HashSet<Long>(Arrays.asList(locationId_1, locationId_2)), Collections.emptyList(), page2Eq_1or2.getContext());

       assertEquals(10, page1Eq_1or2.getItems().size());
       assertEquals(10, page2Eq_1or2.getItems().size());
       assertEquals(0, page3Eq_1or2.getItems().size());
       assertFalse(page1Eq_1or2.getContext().isLastPage());
       assertFalse(page2Eq_1or2.getContext().isLastPage());
       assertTrue(page3Eq_1or2.getContext().isLastPage());
       
       expectedPage1Eq_1or2Strings = new ArrayList<>(Arrays.asList(new String[]{"qr_0", "qr_1", "qr_2", "qr_3", "qr_4", "qr_5", "qr_6", "qr_7", "qr_8", "qr_9" }));
       List<String> actualPage1Loc_1or2Strings = new ArrayList<>();
       page1Eq_1or2.getItems().stream().forEach( ce -> actualPage1Loc_1or2Strings.add(((ClientSessionDetails)ce.getDetails()).getApFingerprint()) );

       assertEquals(expectedPage1Eq_1or2Strings, actualPage1Loc_1or2Strings);

       expectedPage2Eq_1or2Strings = new ArrayList<>(Arrays.asList(new String[]{"qr_10", "qr_11", "qr_12", "qr_13", "qr_14", "qr_15", "qr_16", "qr_17", "qr_18", "qr_19" }));
       List<String> actualPage2Loc_1or2Strings = new ArrayList<>();
       page2Eq_1or2.getItems().stream().forEach( ce -> actualPage2Loc_1or2Strings.add(((ClientSessionDetails)ce.getDetails()).getApFingerprint()) );

       assertEquals(expectedPage2Eq_1or2Strings, actualPage2Loc_1or2Strings);       

       //test the results for ( location_1 or  location_2 ) and equipment_1only
       page1Eq_1 = testInterface.getSessionsForCustomer(customerId_1, new HashSet<Long>(Arrays.asList(equipmentId_1)), new HashSet<Long>(Arrays.asList(locationId_1, locationId_2)), Collections.emptyList(), context);
       page2Eq_1 = testInterface.getSessionsForCustomer(customerId_1, new HashSet<Long>(Arrays.asList(equipmentId_1)), new HashSet<Long>(Arrays.asList(locationId_1, locationId_2)), Collections.emptyList(), page1Eq_1.getContext());

       assertEquals(10, page1Eq_1.getItems().size());
       assertEquals(0, page2Eq_1.getItems().size());
       assertFalse(page1Eq_1.getContext().isLastPage());
       assertTrue(page2Eq_1.getContext().isLastPage());
       
       expectedPage1Eq_1Strings = new ArrayList<>(expectedPage1Eq_1or2Strings);
       List<String> actualPage1Loc_121Strings = new ArrayList<>();
       page1Eq_1.getItems().stream().forEach( ce -> actualPage1Loc_121Strings.add(((ClientSessionDetails)ce.getDetails()).getApFingerprint()) );

       assertEquals(expectedPage1Eq_1Strings, actualPage1Loc_121Strings);

       createdList.forEach(c -> testInterface.deleteSession(c.getCustomerId(), c.getEquipmentId(), c.getMacAddress()));
       createdListExtra.forEach(c -> testInterface.deleteSession(c.getCustomerId(), c.getEquipmentId(), c.getMacAddress()));
    }
}
