package com.telecominfraproject.wlan.alarm.datastore.cassandra;

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

import com.telecominfraproject.wlan.alarm.datastore.BaseAlarmDatastoreTest;
import com.telecominfraproject.wlan.alarm.models.Alarm;
import com.telecominfraproject.wlan.alarm.models.AlarmCode;
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
@Import(value = { AlarmDatastoreCassandra.class, BaseCassandraTest.Config.class, BaseCassandraDataSource.class })
@Ignore("Ignore Cassandra Tests until we can set up a cassandra cluster for the integration testing")
public class AlarmDatastoreCassandraTests extends BaseAlarmDatastoreTest {

    @Test
    @Override
    //Cassandra has some limitations for sorting and filtering vs other data store types, 
    //	that's why this method is adjusted for cassandra datastore specifically 
    public void testAlarmPagination()
    {
       //create 100 Alarms
       Alarm mdl;
       int customerId_1 = (int) testSequence.incrementAndGet();
       int customerId_2 = (int) testSequence.incrementAndGet();
       
       int apNameIdx = 0;
       Set<Long> equipmentIds = new HashSet<>();
       Set<AlarmCode> alarmCodes = new HashSet<>(Arrays.asList(AlarmCode.AccessPointIsUnreachable, AlarmCode.AssocFailure));
       long pastTimestamp = 0;
       Set<Long> equipmentIds_c2 = new HashSet<>();
       
       testInterface.resetAlarmCounters();
       
       for(int i = 0; i< 50; i++){
           mdl = createAlarmObject();
           mdl.setCustomerId(customerId_1);
           mdl.setScopeId("qr_"+apNameIdx);
           equipmentIds.add(mdl.getEquipmentId());
           
           if(i == 8) {
           	//create one record for the time of 10 sec ago
           	mdl.setCreatedTimestamp(mdl.getCreatedTimestamp() - 10000);
           	pastTimestamp = mdl.getCreatedTimestamp();
           }

           apNameIdx++;
           testInterface.create(mdl);
       }

       testInterface.resetAlarmCounters();

       for(int i = 0; i< 50; i++){
           mdl = createAlarmObject();
           mdl.setCustomerId(customerId_2);
           mdl.setScopeId("qr_"+apNameIdx);
           equipmentIds_c2.add(mdl.getEquipmentId());
           apNameIdx++;
           testInterface.create(mdl);
       }

       //paginate over Alarms
       
       List<ColumnAndSort> sortBy = new ArrayList<>();
       //In Cassandra the support for pagination order is very limited fixed at table creation, supplied sortBy options will be ignored.
       //sortBy.addAll(Arrays.asList(new ColumnAndSort("equipmentId")));
       
       //get active alarms for all equipment and all alarmCodes for the customer since the beginning of time
       PaginationContext<Alarm> context = new PaginationContext<>(10);
		PaginationResponse<Alarm> page1 = null;
		PaginationResponse<Alarm> page2 = null;
		PaginationResponse<Alarm> page3 = null;
		PaginationResponse<Alarm> page4 = null;
		PaginationResponse<Alarm> page5 = null;
		PaginationResponse<Alarm> page6 = null;
		PaginationResponse<Alarm> page7 = null;
       
       
       page1 = testInterface.getForCustomer(customerId_1, null, null, -1, sortBy, context);
       page2 = testInterface.getForCustomer(customerId_1, null, null, -1, sortBy, page1.getContext());
       page3 = testInterface.getForCustomer(customerId_1, null, null, -1, sortBy, page2.getContext());
       page4 = testInterface.getForCustomer(customerId_1, null, null, -1, sortBy, page3.getContext());
       page5 = testInterface.getForCustomer(customerId_1, null, null, -1, sortBy, page4.getContext());
       page6 = testInterface.getForCustomer(customerId_1, null, null, -1, sortBy, page5.getContext());
       page7 = testInterface.getForCustomer(customerId_1, null, null, -1, sortBy, page6.getContext());
       
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
       
       //the order is weird, but consistent
       List<String> expectedPage3Strings = new ArrayList<	>(Arrays.asList(new String[]{"qr_49", "qr_45", "qr_8", "qr_33", "qr_7", "qr_35", "qr_22", "qr_11", "qr_16", "qr_1" }));
       List<String> actualPage3Strings = new ArrayList<>();
       page3.getItems().stream().forEach( ce -> actualPage3Strings.add(ce.getScopeId()) );
       
       assertEquals(expectedPage3Strings, actualPage3Strings);


       List<String> expectedPage1Strings = Arrays.asList(new String[]{"qr_3", "qr_13", "qr_38", "qr_40", "qr_44", "qr_17", "qr_41", "qr_39", "qr_19", "qr_34" });
       
       //test first page of the results with empty sort order -> ignored by cassandra datastore - the order is weird, but consistent
       PaginationResponse<Alarm> page1EmptySort = testInterface.getForCustomer(customerId_1, null, null, -1, Collections.emptyList(), context);
       assertEquals(10, page1EmptySort.getItems().size());

       List<String> expectedPage1EmptySortStrings = new ArrayList<>(expectedPage1Strings);
       List<String> actualPage1EmptySortStrings = new ArrayList<>();
       page1EmptySort.getItems().stream().forEach( ce -> actualPage1EmptySortStrings.add(ce.getScopeId()) );

       assertEquals(expectedPage1EmptySortStrings, actualPage1EmptySortStrings);

       //test first page of the results with null sort order -> ignored by cassandra datastore - the order is weird, but consistent
       PaginationResponse<Alarm> page1NullSort = testInterface.getForCustomer(customerId_1, null, null, -1, null, context);
       assertEquals(10, page1NullSort.getItems().size());

       List<String> expectedPage1NullSortStrings = new ArrayList<>(expectedPage1Strings);
       List<String> actualPage1NullSortStrings = new ArrayList<>();
       page1NullSort.getItems().stream().forEach( ce -> actualPage1NullSortStrings.add(ce.getScopeId()) );

       assertEquals(expectedPage1NullSortStrings, actualPage1NullSortStrings);

       
       //test first page of the results with sort descending order by a equipmentId property -> ignored by cassandra datastore - the order is weird, but consistent
       PaginationResponse<Alarm> page1SingleSortDesc = testInterface.getForCustomer(customerId_1, null, null, -1, Collections.singletonList(new ColumnAndSort("equipmentId", SortOrder.desc)), context);
       assertEquals(10, page1SingleSortDesc.getItems().size());

       List<String> expectedPage1SingleSortDescStrings = new ArrayList<>(expectedPage1Strings);
       List<String> actualPage1SingleSortDescStrings = new ArrayList<>();
       page1SingleSortDesc.getItems().stream().forEach( ce -> actualPage1SingleSortDescStrings.add(ce.getScopeId()) );
       
       assertEquals(expectedPage1SingleSortDescStrings, actualPage1SingleSortDescStrings);
       
       //test with explicit list of equipmentIds and explicit list of AlarmCodes
       long createdAfterTs = pastTimestamp + 10;
       context = new PaginationContext<>(10);
       page1 = testInterface.getForCustomer(customerId_1, equipmentIds, alarmCodes, createdAfterTs, sortBy, context);
       page2 = testInterface.getForCustomer(customerId_1, equipmentIds, alarmCodes, createdAfterTs, sortBy, page1.getContext());
       page3 = testInterface.getForCustomer(customerId_1, equipmentIds, alarmCodes, createdAfterTs, sortBy, page2.getContext());
       page4 = testInterface.getForCustomer(customerId_1, equipmentIds, alarmCodes, createdAfterTs, sortBy, page3.getContext());
       page5 = testInterface.getForCustomer(customerId_1, equipmentIds, alarmCodes, createdAfterTs, sortBy, page4.getContext());
       page6 = testInterface.getForCustomer(customerId_1, equipmentIds, alarmCodes, createdAfterTs, sortBy, page5.getContext());
       page7 = testInterface.getForCustomer(customerId_1, equipmentIds, alarmCodes, createdAfterTs, sortBy, page6.getContext());
       
       //verify returned pages
       assertEquals(10, page1.getItems().size());
       assertEquals(10, page2.getItems().size());
       assertEquals(10, page3.getItems().size());
       assertEquals(10, page4.getItems().size());
       assertEquals(9, page5.getItems().size());
       assertEquals(0, page6.getItems().size());
       
       page1.getItems().forEach(e -> assertEquals(customerId_1, e.getCustomerId()) );
       page2.getItems().forEach(e -> assertEquals(customerId_1, e.getCustomerId()) );
       page3.getItems().forEach(e -> assertEquals(customerId_1, e.getCustomerId()) );
       page4.getItems().forEach(e -> assertEquals(customerId_1, e.getCustomerId()) );
       page5.getItems().forEach(e -> assertEquals(customerId_1, e.getCustomerId()) );
       
       //test with explicit list of equipmentIds of one element and explicit list of AlarmCodes of one element
       context = new PaginationContext<>(10);
       page1 = testInterface.getForCustomer(customerId_1, Collections.singleton(equipmentIds.iterator().next()), Collections.singleton(AlarmCode.AccessPointIsUnreachable), -1, sortBy, context);
       assertEquals(1, page1.getItems().size());

       testInterface.resetAlarmCounters();

       //clean up after the test
       equipmentIds.forEach(eqId -> testInterface.delete(customerId_1, eqId));
       equipmentIds_c2.forEach(eqId -> testInterface.delete(customerId_2, eqId));
       
       testInterface.resetAlarmCounters();

    }
    
}
