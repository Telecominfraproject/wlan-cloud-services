package com.telecominfraproject.wlan.client.datastore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.telecominfraproject.wlan.client.info.models.ClientInfoDetails;
import com.telecominfraproject.wlan.client.models.Client;
import com.telecominfraproject.wlan.client.session.models.ClientSession;
import com.telecominfraproject.wlan.client.session.models.ClientSessionDetails;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pagination.SortOrder;
import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;

/**
 * @author dtoptygin
 *
 */
public abstract class BaseClientDatastoreTest {
    @Autowired
    protected ClientDatastore testInterface;

    private static final AtomicLong testSequence = new AtomicLong(2);

    @Test
    public void testCRUD() {
    	Client client = createClientObject();

        //create
    	Client created = testInterface.create(client);
        assertNotNull(created);
        assertEquals(client.getCustomerId(), created.getCustomerId());
        assertNotNull(created.getDetails());
        assertEquals(client.getDetails(), created.getDetails());
                
        // update
        ((ClientInfoDetails)created.getDetails()).setAlias(((ClientInfoDetails)created.getDetails()).getAlias()+"_updated");
        Client updated = testInterface.update(created);
        assertNotNull(updated);
        assertEquals(created.getDetails(), updated.getDetails());
        
        while(updated.getLastModifiedTimestamp() == created.getLastModifiedTimestamp()) {
        	//update again to make the timestamps different      	
        	((ClientInfoDetails)created.getDetails()).setAlias(((ClientInfoDetails)created.getDetails()).getAlias()+"_updated_1");
            updated = testInterface.update(created);
        }

        //UPDATE test - fail because of concurrent modification exception
        try{
        	Client modelConcurrentUpdate = created.clone();
        	((ClientInfoDetails)modelConcurrentUpdate.getDetails()).setAlias("not important");
        	testInterface.update(modelConcurrentUpdate);
        	fail("failed to detect concurrent modification");
        }catch (DsConcurrentModificationException e) {
        	// expected it
        }
        
        //retrieve
        Client retrieved = testInterface.getOrNull(created.getCustomerId(), created.getMacAddress());
        assertNotNull(retrieved);
        assertEquals(retrieved.getLastModifiedTimestamp(), updated.getLastModifiedTimestamp());
        
        //retrieve non-existent
        assertNull(testInterface.getOrNull(-1, new MacAddress(0L)));
        
        //delete
        retrieved = testInterface.delete(created.getCustomerId(), created.getMacAddress());
        assertNotNull(retrieved);
        
        //delete non-existent
        try {
        	testInterface.delete(-1, new MacAddress(0L));
        	fail("delete non-existing record");
        }catch(DsEntityNotFoundException e ){
        	//expected it
        }

        //update non-existent
        try {
        	testInterface.update(retrieved);
        	fail("update non-existing record");
        }catch(DsEntityNotFoundException e ){
        	//expected it
        }

    }
    
    @Test
    public void testGetAllInSet() {
        Set<Client> createdSet = new HashSet<>();
        Set<Client> createdTestSet = new HashSet<>();

        //Create test Clients
        Client client = new Client();
        long nextId = testSequence.getAndIncrement();
        client.setCustomerId((int) nextId);
        ClientInfoDetails details = new ClientInfoDetails();
        details.setAlias("test-details-" + nextId);
        client.setDetails(details );

        for (int i = 0; i < 10; i++) {
            ((ClientInfoDetails)client.getDetails()).setAlias("test_" + i);
            client.setMacAddress(new MacAddress((long)i)); 
            Client ret = testInterface.create(client);

            // Only keep track of half of the created ones for testing
            if (i % 2 == 0) {
                createdTestSet.add(ret.clone());
            } else {
                createdSet.add(ret.clone());
            }
        }

        // Use only the IDs from the test set to retrieve records.
        Set<MacAddress> testSetIds = new HashSet<>();
        for (Client c : createdTestSet) {
            testSetIds.add(c.getMacAddress());
        }
        assertEquals(5, testSetIds.size());

        List<Client> clientsRetrievedByIdSet = testInterface.get(client.getCustomerId(), testSetIds);
        assertEquals(5, clientsRetrievedByIdSet.size());
        for (Client c : clientsRetrievedByIdSet) {
            assertTrue(createdTestSet.contains(c));
        }

        // Make sure the clients from the non-test set are not in the list
        for (Client c : clientsRetrievedByIdSet) {
            assertTrue(!createdSet.contains(c));
        }
		
        // Clean up after test
        for (Client c : createdSet) {
        	testInterface.delete(c.getCustomerId(), c.getMacAddress());
        }
        for (Client c : createdTestSet) {
        	testInterface.delete(c.getCustomerId(), c.getMacAddress());
        }

    }
    
    @Test
    public void testClientPagination()
    {
       //create 100 Clients
       Client mdl;
       int customerId_1 = (int) testSequence.incrementAndGet();
       int customerId_2 = (int) testSequence.incrementAndGet();
       
       int apNameIdx = 0;
       
       for(int i = 0; i< 50; i++){
           mdl = new Client();
           mdl.setCustomerId(customerId_1);
           ClientInfoDetails details = new ClientInfoDetails();
           details.setAlias("qr_"+apNameIdx);
           mdl.setDetails(details );
           mdl.setMacAddress(new MacAddress((long)i));

           apNameIdx++;
           testInterface.create(mdl);
       }

       for(int i = 0; i< 50; i++){
           mdl = new Client();
           mdl.setCustomerId(customerId_2);
           ClientInfoDetails details = new ClientInfoDetails();
           details.setAlias("qr_"+apNameIdx);
           mdl.setDetails(details );
           mdl.setMacAddress(new MacAddress((long)i));
           
           apNameIdx++;
           testInterface.create(mdl);
       }

       //paginate over Clients
       
       List<ColumnAndSort> sortBy = new ArrayList<>();
       sortBy.addAll(Arrays.asList(new ColumnAndSort("macAddress")));
       
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
       
       List<String> expectedPage3Strings = new ArrayList<	>(Arrays.asList(new String[]{"qr_20", "qr_21", "qr_22", "qr_23", "qr_24", "qr_25", "qr_26", "qr_27", "qr_28", "qr_29" }));
       List<String> actualPage3Strings = new ArrayList<>();
       page3.getItems().stream().forEach( ce -> actualPage3Strings.add(((ClientInfoDetails)ce.getDetails()).getAlias()) );
       
       assertEquals(expectedPage3Strings, actualPage3Strings);

//       System.out.println("================================");
//       for(Client pmdl: page3.getItems()){
//           System.out.println(pmdl);
//       }
//       System.out.println("================================");
//       System.out.println("Context: "+ page3.getContext());
//       System.out.println("================================");
       
       //test first page of the results with empty sort order -> default sort order (by Id ascending)
       PaginationResponse<Client> page1EmptySort = testInterface.getForCustomer(customerId_1, Collections.emptyList(), context);
       assertEquals(10, page1EmptySort.getItems().size());

       List<String> expectedPage1EmptySortStrings = new ArrayList<>(Arrays.asList(new String[]{"qr_0", "qr_1", "qr_2", "qr_3", "qr_4", "qr_5", "qr_6", "qr_7", "qr_8", "qr_9" }));
       List<String> actualPage1EmptySortStrings = new ArrayList<>();
       page1EmptySort.getItems().stream().forEach( ce -> actualPage1EmptySortStrings.add(((ClientInfoDetails)ce.getDetails()).getAlias()) );

       assertEquals(expectedPage1EmptySortStrings, actualPage1EmptySortStrings);

       //test first page of the results with null sort order -> default sort order (by Id ascending)
       PaginationResponse<Client> page1NullSort = testInterface.getForCustomer(customerId_1, null, context);
       assertEquals(10, page1NullSort.getItems().size());

       List<String> expectedPage1NullSortStrings = new ArrayList<>(Arrays.asList(new String[]{"qr_0", "qr_1", "qr_2", "qr_3", "qr_4", "qr_5", "qr_6", "qr_7", "qr_8", "qr_9" }));
       List<String> actualPage1NullSortStrings = new ArrayList<>();
       page1NullSort.getItems().stream().forEach( ce -> actualPage1NullSortStrings.add(((ClientInfoDetails)ce.getDetails()).getAlias()) );

       assertEquals(expectedPage1NullSortStrings, actualPage1NullSortStrings);

       
       //test first page of the results with sort descending order by a macAddress property 
       PaginationResponse<Client> page1SingleSortDesc = testInterface.getForCustomer(customerId_1, Collections.singletonList(new ColumnAndSort("macAddress", SortOrder.desc)), context);
       assertEquals(10, page1SingleSortDesc.getItems().size());

       List<String> expectedPage1SingleSortDescStrings = new ArrayList<	>(Arrays.asList(new String[]{"qr_49", "qr_48", "qr_47", "qr_46", "qr_45", "qr_44", "qr_43", "qr_42", "qr_41", "qr_40" }));
       List<String> actualPage1SingleSortDescStrings = new ArrayList<>();
       page1SingleSortDesc.getItems().stream().forEach( ce -> actualPage1SingleSortDescStrings.add(((ClientInfoDetails)ce.getDetails()).getAlias()) );
       
       assertEquals(expectedPage1SingleSortDescStrings, actualPage1SingleSortDescStrings);

    }

    
    //
    // Client session tests
    //
    
    @Test
    public void testSessionCRUD() {
    	ClientSession clientSession = createClientSessionObject();

        //create
    	ClientSession created = testInterface.updateSession(clientSession);
        assertNotNull(created);
        assertEquals(clientSession.getCustomerId(), created.getCustomerId());
        assertNotNull(created.getDetails());
        assertEquals(clientSession.getDetails(), created.getDetails());
                
        // update
        ((ClientSessionDetails)created.getDetails()).setApFingerprint(((ClientSessionDetails)created.getDetails()).getApFingerprint()+"_updated");
        ClientSession updated = testInterface.updateSession(created);
        assertNotNull(updated);
        assertEquals(created.getDetails(), updated.getDetails());
        
        while(updated.getLastModifiedTimestamp() == created.getLastModifiedTimestamp()) {
        	//update again to make the timestamps different      	
        	((ClientSessionDetails)created.getDetails()).setApFingerprint(((ClientSessionDetails)created.getDetails()).getApFingerprint()+"_updated_1");
            updated = testInterface.updateSession(created);
        }

        //UPDATE test - do NOT fail because of concurrent modification exception
    	ClientSession modelConcurrentUpdate = created.clone();
    	((ClientSessionDetails)modelConcurrentUpdate.getDetails()).setApFingerprint("not important");
    	updated = testInterface.updateSession(modelConcurrentUpdate);
        
        //retrieve
        ClientSession retrieved = testInterface.getSessionOrNull(created.getCustomerId(), created.getEquipmentId(), created.getMacAddress());
        assertNotNull(retrieved);
        assertEquals(retrieved.getLastModifiedTimestamp(), updated.getLastModifiedTimestamp());
        
        //retrieve non-existent
        assertNull(testInterface.getSessionOrNull(-1, -1, new MacAddress(0L)));
        
        //delete
        retrieved = testInterface.deleteSession(created.getCustomerId(), created.getEquipmentId(), created.getMacAddress());
        assertNotNull(retrieved);
        
        //delete non-existent
        try {
        	testInterface.deleteSession(-1, -1, new MacAddress(0L));
        	fail("delete non-existing record");
        }catch(DsEntityNotFoundException e ){
        	//expected it
        }

        //update non-existent
      	testInterface.updateSession(retrieved);

    }
    
    @Test
    public void testGetSessionAllInSet() {
        Set<ClientSession> createdSet = new HashSet<>();
        Set<ClientSession> createdTestSet = new HashSet<>();

        //Create test Client sessions
        ClientSession clientSession = new ClientSession();
        long nextId = testSequence.getAndIncrement();
        clientSession.setCustomerId((int) nextId);
        clientSession.setEquipmentId(testSequence.getAndIncrement());
        ClientSessionDetails details = new ClientSessionDetails();
        details.setApFingerprint("test-details-" + nextId);
        clientSession.setDetails(details );

        for (int i = 0; i < 10; i++) {
            ((ClientSessionDetails)clientSession.getDetails()).setApFingerprint("test_" + i);
            clientSession.setMacAddress(new MacAddress((long)i)); 
            ClientSession ret = testInterface.updateSession(clientSession);

            // Only keep track of half of the created ones for testing
            if (i % 2 == 0) {
                createdTestSet.add(ret.clone());
            } else {
                createdSet.add(ret.clone());
            }
        }

        // Use only the IDs from the test set to retrieve records.
        Set<MacAddress> testSetIds = new HashSet<>();
        for (ClientSession c : createdTestSet) {
            testSetIds.add(c.getMacAddress());
        }
        assertEquals(5, testSetIds.size());

        List<ClientSession> clientsRetrievedByIdSet = testInterface.getSessions(clientSession.getCustomerId(), testSetIds);
        assertEquals(5, clientsRetrievedByIdSet.size());
        for (ClientSession c : clientsRetrievedByIdSet) {
            assertTrue(createdTestSet.contains(c));
        }

        // Make sure the client sessions from the non-test set are not in the list
        for (ClientSession c : clientsRetrievedByIdSet) {
            assertTrue(!createdSet.contains(c));
        }
		
        // Clean up after test
        for (ClientSession c : createdSet) {
        	testInterface.deleteSession(c.getCustomerId(), c.getEquipmentId(), c.getMacAddress());
        }
        for (ClientSession c : createdTestSet) {
        	testInterface.deleteSession(c.getCustomerId(), c.getEquipmentId(), c.getMacAddress());
        }

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
       
       int apNameIdx = 0;
       
       List<ClientSession> sessionsToCreate = new ArrayList<>();
       
       for(int i = 0; i< 50; i++){
           mdl = new ClientSession();
           mdl.setCustomerId(customerId_1);
           if(i<10) {
        	   mdl.setEquipmentId(equipmentId_1);
           } else if(i<20) {
        	   mdl.setEquipmentId(equipmentId_2);
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
       

       for(int i = 0; i< 50; i++){
           mdl = new ClientSession();
           mdl.setCustomerId(customerId_2);
    	   mdl.setEquipmentId(testSequence.incrementAndGet());
           ClientSessionDetails details = new ClientSessionDetails();
           details.setApFingerprint("qr_"+apNameIdx);
           mdl.setDetails(details );
           mdl.setMacAddress(new MacAddress((long)i));
           
           apNameIdx++;
           testInterface.updateSession(mdl);
       }

       //paginate over Clients
       
       List<ColumnAndSort> sortBy = new ArrayList<>();
       sortBy.addAll(Arrays.asList(new ColumnAndSort("macAddress")));
       
       PaginationContext<ClientSession> context = new PaginationContext<>(10);
       PaginationResponse<ClientSession> page1 = testInterface.getSessionsForCustomer(customerId_1, null, sortBy, context);
       PaginationResponse<ClientSession> page2 = testInterface.getSessionsForCustomer(customerId_1, null, sortBy, page1.getContext());
       PaginationResponse<ClientSession> page3 = testInterface.getSessionsForCustomer(customerId_1, null, sortBy, page2.getContext());
       PaginationResponse<ClientSession> page4 = testInterface.getSessionsForCustomer(customerId_1, null, sortBy, page3.getContext());
       PaginationResponse<ClientSession> page5 = testInterface.getSessionsForCustomer(customerId_1, null, sortBy, page4.getContext());
       PaginationResponse<ClientSession> page6 = testInterface.getSessionsForCustomer(customerId_1, null, sortBy, page5.getContext());
       PaginationResponse<ClientSession> page7 = testInterface.getSessionsForCustomer(customerId_1, null, sortBy, page6.getContext());
       
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
       
       List<String> expectedPage3Strings = new ArrayList<	>(Arrays.asList(new String[]{"qr_20", "qr_21", "qr_22", "qr_23", "qr_24", "qr_25", "qr_26", "qr_27", "qr_28", "qr_29" }));
       List<String> actualPage3Strings = new ArrayList<>();
       page3.getItems().stream().forEach( ce -> actualPage3Strings.add(((ClientSessionDetails)ce.getDetails()).getApFingerprint()) );
       
       assertEquals(expectedPage3Strings, actualPage3Strings);

//       System.out.println("================================");
//       for(Client pmdl: page3.getItems()){
//           System.out.println(pmdl);
//       }
//       System.out.println("================================");
//       System.out.println("Context: "+ page3.getContext());
//       System.out.println("================================");
       
       //test first page of the results with empty sort order -> default sort order (by Id ascending)
       PaginationResponse<ClientSession> page1EmptySort = testInterface.getSessionsForCustomer(customerId_1, null, Collections.emptyList(), context);
       assertEquals(10, page1EmptySort.getItems().size());

       List<String> expectedPage1EmptySortStrings = new ArrayList<>(Arrays.asList(new String[]{"qr_0", "qr_1", "qr_2", "qr_3", "qr_4", "qr_5", "qr_6", "qr_7", "qr_8", "qr_9" }));
       List<String> actualPage1EmptySortStrings = new ArrayList<>();
       page1EmptySort.getItems().stream().forEach( ce -> actualPage1EmptySortStrings.add(((ClientSessionDetails)ce.getDetails()).getApFingerprint()) );

       assertEquals(expectedPage1EmptySortStrings, actualPage1EmptySortStrings);

       //test first page of the results with null sort order -> default sort order (by Id ascending)
       PaginationResponse<ClientSession> page1NullSort = testInterface.getSessionsForCustomer(customerId_1, null, null, context);
       assertEquals(10, page1NullSort.getItems().size());

       List<String> expectedPage1NullSortStrings = new ArrayList<>(Arrays.asList(new String[]{"qr_0", "qr_1", "qr_2", "qr_3", "qr_4", "qr_5", "qr_6", "qr_7", "qr_8", "qr_9" }));
       List<String> actualPage1NullSortStrings = new ArrayList<>();
       page1NullSort.getItems().stream().forEach( ce -> actualPage1NullSortStrings.add(((ClientSessionDetails)ce.getDetails()).getApFingerprint()) );

       assertEquals(expectedPage1NullSortStrings, actualPage1NullSortStrings);

       
       //test first page of the results with sort descending order by a macAddress property 
       PaginationResponse<ClientSession> page1SingleSortDesc = testInterface.getSessionsForCustomer(customerId_1, null, Collections.singletonList(new ColumnAndSort("macAddress", SortOrder.desc)), context);
       assertEquals(10, page1SingleSortDesc.getItems().size());

       List<String> expectedPage1SingleSortDescStrings = new ArrayList<	>(Arrays.asList(new String[]{"qr_49", "qr_48", "qr_47", "qr_46", "qr_45", "qr_44", "qr_43", "qr_42", "qr_41", "qr_40" }));
       List<String> actualPage1SingleSortDescStrings = new ArrayList<>();
       page1SingleSortDesc.getItems().stream().forEach( ce -> actualPage1SingleSortDescStrings.add(((ClientSessionDetails)ce.getDetails()).getApFingerprint()) );
       
       assertEquals(expectedPage1SingleSortDescStrings, actualPage1SingleSortDescStrings);

       //test the results for equipment_1 only
       PaginationResponse<ClientSession> page1Eq_1 = testInterface.getSessionsForCustomer(customerId_1, new HashSet<Long>(Arrays.asList(equipmentId_1)), Collections.emptyList(), context);
       PaginationResponse<ClientSession> page2Eq_1 = testInterface.getSessionsForCustomer(customerId_1, new HashSet<Long>(Arrays.asList(equipmentId_1)), Collections.emptyList(), page1Eq_1.getContext());

       assertEquals(10, page1Eq_1.getItems().size());
       assertEquals(0, page2Eq_1.getItems().size());
       assertFalse(page1Eq_1.getContext().isLastPage());
       assertTrue(page2Eq_1.getContext().isLastPage());
       
       List<String> expectedPage1Eq_1Strings = new ArrayList<>(Arrays.asList(new String[]{"qr_0", "qr_1", "qr_2", "qr_3", "qr_4", "qr_5", "qr_6", "qr_7", "qr_8", "qr_9" }));
       List<String> actualPage1Eq_1Strings = new ArrayList<>();
       page1Eq_1.getItems().stream().forEach( ce -> actualPage1Eq_1Strings.add(((ClientSessionDetails)ce.getDetails()).getApFingerprint()) );

       assertEquals(expectedPage1Eq_1Strings, actualPage1Eq_1Strings);

       //test the results for equipment_2 only
       PaginationResponse<ClientSession> page1Eq_2 = testInterface.getSessionsForCustomer(customerId_1, new HashSet<Long>(Arrays.asList(equipmentId_2)), Collections.emptyList(), context);
       PaginationResponse<ClientSession> page2Eq_2 = testInterface.getSessionsForCustomer(customerId_1, new HashSet<Long>(Arrays.asList(equipmentId_2)), Collections.emptyList(), page1Eq_2.getContext());

       assertEquals(10, page1Eq_2.getItems().size());
       assertEquals(0, page2Eq_2.getItems().size());
       assertFalse(page1Eq_2.getContext().isLastPage());
       assertTrue(page2Eq_2.getContext().isLastPage());
       
       List<String> expectedPage1Eq_2Strings = new ArrayList<>(Arrays.asList(new String[]{"qr_10", "qr_11", "qr_12", "qr_13", "qr_14", "qr_15", "qr_16", "qr_17", "qr_18", "qr_19"}));
       List<String> actualPage1Eq_2Strings = new ArrayList<>();
       page1Eq_2.getItems().stream().forEach( ce -> actualPage1Eq_2Strings.add(((ClientSessionDetails)ce.getDetails()).getApFingerprint()) );

       assertEquals(expectedPage1Eq_2Strings, actualPage1Eq_2Strings);

        
       //test the results for equipment_1 or  equipment_2 only
       PaginationResponse<ClientSession> page1Eq_1or2 = testInterface.getSessionsForCustomer(customerId_1, new HashSet<Long>(Arrays.asList(equipmentId_1, equipmentId_2)), Collections.emptyList(), context);
       PaginationResponse<ClientSession> page2Eq_1or2 = testInterface.getSessionsForCustomer(customerId_1, new HashSet<Long>(Arrays.asList(equipmentId_1, equipmentId_2)), Collections.emptyList(), page1Eq_1or2.getContext());
       PaginationResponse<ClientSession> page3Eq_1or2 = testInterface.getSessionsForCustomer(customerId_1, new HashSet<Long>(Arrays.asList(equipmentId_1, equipmentId_2)), Collections.emptyList(), page2Eq_1or2.getContext());

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

    }
    
    private ClientSession createClientSessionObject() {
    	ClientSession result = new ClientSession();
        long nextId = testSequence.getAndIncrement();
        result.setCustomerId((int) nextId);
        result.setMacAddress(new MacAddress(nextId));
        ClientSessionDetails details = new ClientSessionDetails();
        details.setApFingerprint("test-details-" + nextId);
		result.setDetails(details );
        return result;
    }

    
    private Client createClientObject() {
    	Client result = new Client();
        long nextId = testSequence.getAndIncrement();
        result.setCustomerId((int) nextId);
        result.setMacAddress(new MacAddress(nextId));
        ClientInfoDetails details = new ClientInfoDetails();
        details.setAlias("test-details-" + nextId);
		result.setDetails(details );
        return result;
    }
}
