package com.telecominfraproject.wlan.alarm.datastore;

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

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pagination.SortOrder;
import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;

import com.telecominfraproject.wlan.alarm.models.Alarm;
import com.telecominfraproject.wlan.alarm.models.AlarmCode;
import com.telecominfraproject.wlan.alarm.models.AlarmCounts;
import com.telecominfraproject.wlan.alarm.models.AlarmDetails;

/**
 * @author dtoptygin
 *
 */
public abstract class BaseAlarmDatastoreTest {
    @Autowired
    protected AlarmDatastore testInterface;

    private static final AtomicLong testSequence = new AtomicLong(1);

    @Test
    public void testCRUD() {
    	Alarm alarm = createAlarmObject();

        //create
    	Alarm created = testInterface.create(alarm);
        assertNotNull(created);
        assertEquals(alarm.getCustomerId(), created.getCustomerId());
        assertEquals(alarm.getEquipmentId(), created.getEquipmentId());
        assertEquals(alarm.getAlarmCode(), created.getAlarmCode());
        assertEquals(alarm.getCreatedTimestamp(), created.getCreatedTimestamp());
        assertNotNull(created.getDetails());
        assertEquals(alarm.getDetails(), created.getDetails());
                
        // update
        created.setScopeId("scope_updated");
        Alarm updated = testInterface.update(created);
        assertNotNull(updated);
        assertEquals(created.getScopeId(), updated.getScopeId());
        
        while(updated.getLastModifiedTimestamp() == created.getLastModifiedTimestamp()) {
        	//update again to make the timestamps different      	
            created.setScopeId(created.getScopeId()+"_updated_1");
            updated = testInterface.update(created);
        }

        //UPDATE test - fail because of concurrent modification exception
        try{
        	Alarm modelConcurrentUpdate = created.clone();
        	modelConcurrentUpdate.setScopeId("not important");
        	testInterface.update(modelConcurrentUpdate);
        	fail("failed to detect concurrent modification");
        }catch (DsConcurrentModificationException e) {
        	// expected it
        }
        
        //retrieve
        Alarm retrieved = testInterface.getOrNull(created.getCustomerId(), created.getEquipmentId(), created.getAlarmCode(), created.getCreatedTimestamp());
        assertNotNull(retrieved);
        assertEquals(retrieved.getLastModifiedTimestamp(), updated.getLastModifiedTimestamp());
        
        //retrieve non-existent
        
        assertNull(testInterface.getOrNull(-1, -1, AlarmCode.AssocFailure, -1));
        
        //delete
        retrieved = testInterface.delete(created.getCustomerId(), created.getEquipmentId(), created.getAlarmCode(), created.getCreatedTimestamp());
        assertNotNull(retrieved);
        retrieved = testInterface.getOrNull(created.getCustomerId(), created.getEquipmentId(), created.getAlarmCode(), created.getCreatedTimestamp());
        assertNull(retrieved);
        
        //delete non-existent
        try {
        	testInterface.delete(created.getCustomerId(), created.getEquipmentId(), created.getAlarmCode(), created.getCreatedTimestamp());
        	fail("delete non-existing record");
        }catch(DsEntityNotFoundException e ){
        	//expected it
        }

        //update non-existent
        try {
        	testInterface.update(updated);
        	fail("update non-existing record");
        }catch(DsEntityNotFoundException e ){
        	//expected it
        }

    }
    
    @Test
    public void testGetAllForEquipment() {
        Set<Alarm> createdSet = new HashSet<>();
        Set<Alarm> createdTestSet = new HashSet<>();

        int customerId = (int) testSequence.incrementAndGet();
        long pastTimestamp = 0;
        
        //Create test Alarms
        for (int i = 0; i < 10; i++) {
        	Alarm alarm = createAlarmObject();
            alarm.setScopeId("test_" + i);
            alarm.setCustomerId(customerId);
            
            if(i == 8) {
            	//create one record for the time of 10 sec ago
            	alarm.setCreatedTimestamp(alarm.getCreatedTimestamp() - 10000);
            	pastTimestamp = alarm.getCreatedTimestamp();
            }

            Alarm ret = testInterface.create(alarm);

            // Only keep track of half of the created ones for testing
            if (i % 2 == 0) {
                createdTestSet.add(ret.clone());
            } else {
                createdSet.add(ret.clone());
            }
        }

        // Use only the IDs from the test set to retrieve records.
        Set<Long> testSetIds = new HashSet<>();
        for (Alarm c : createdTestSet) {
            testSetIds.add(c.getEquipmentId());
        }
        assertEquals(5, testSetIds.size());

        List<Alarm> alarmsRetrievedByIdSet = testInterface.get(customerId, testSetIds, Collections.singleton(AlarmCode.AccessPointIsUnreachable));
        assertEquals(5, alarmsRetrievedByIdSet.size());
        for (Alarm c : alarmsRetrievedByIdSet) {
            assertTrue(createdTestSet.contains(c));
        }

        // Make sure the alarms from the non-test set are not in the list
        for (Alarm c : alarmsRetrievedByIdSet) {
            assertTrue(!createdSet.contains(c));
        }

        List<Alarm> alarmsRetrievedByNullAlarmCodeSet = testInterface.get(customerId, testSetIds, null);
        Collections.sort(alarmsRetrievedByIdSet);
        Collections.sort(alarmsRetrievedByNullAlarmCodeSet);
        assertEquals(alarmsRetrievedByIdSet, alarmsRetrievedByNullAlarmCodeSet);

        List<Alarm> alarmsRetrievedByManyAlarmCodeSet = testInterface.get(customerId, testSetIds, new HashSet<AlarmCode>(Arrays.asList(AlarmCode.AccessPointIsUnreachable, AlarmCode.AssocFailure)));
        Collections.sort(alarmsRetrievedByManyAlarmCodeSet);
        assertEquals(alarmsRetrievedByIdSet, alarmsRetrievedByManyAlarmCodeSet);
        
        try {
        	testInterface.get(customerId, null, null);
        	fail("equipmentIds must not be null");
        } catch (IllegalArgumentException e) {
        	//expected it
        }

        try {
        	testInterface.get(customerId, Collections.emptySet(), null);
        	fail("equipmentIds must not be empty");
        } catch (IllegalArgumentException e) {
        	//expected it
        }

        //test retrieve after specified time stamp
        List<Alarm> alarmsRetrievedAfterTs = testInterface.get(customerId, testSetIds, null, pastTimestamp + 10 );
        assertEquals(4, alarmsRetrievedAfterTs.size());
        alarmsRetrievedAfterTs.forEach(c -> assertTrue(createdTestSet.contains(c)) );

        // Clean up after test
        for (Alarm c : createdSet) {
        	testInterface.delete(c.getCustomerId(), c.getEquipmentId());
        }
        for (Alarm c : createdTestSet) {
        	testInterface.delete(c.getCustomerId(), c.getEquipmentId());
        }

    }
    
    @Test
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

       for(int i = 0; i< 50; i++){
           mdl = createAlarmObject();
           mdl.setCustomerId(customerId_2);
           mdl.setScopeId("qr_"+apNameIdx);           
           apNameIdx++;
           testInterface.create(mdl);
       }

       //paginate over Alarms
       
       List<ColumnAndSort> sortBy = new ArrayList<>();
       sortBy.addAll(Arrays.asList(new ColumnAndSort("equipmentId")));
       
       //get active alarms for all equipment and all alarmCodes for the customer since the beginning of time
       PaginationContext<Alarm> context = new PaginationContext<>(10);
       PaginationResponse<Alarm> page1 = testInterface.getForCustomer(customerId_1, null, null, -1, sortBy, context);
       PaginationResponse<Alarm> page2 = testInterface.getForCustomer(customerId_1, null, null, -1, sortBy, page1.getContext());
       PaginationResponse<Alarm> page3 = testInterface.getForCustomer(customerId_1, null, null, -1, sortBy, page2.getContext());
       PaginationResponse<Alarm> page4 = testInterface.getForCustomer(customerId_1, null, null, -1, sortBy, page3.getContext());
       PaginationResponse<Alarm> page5 = testInterface.getForCustomer(customerId_1, null, null, -1, sortBy, page4.getContext());
       PaginationResponse<Alarm> page6 = testInterface.getForCustomer(customerId_1, null, null, -1, sortBy, page5.getContext());
       PaginationResponse<Alarm> page7 = testInterface.getForCustomer(customerId_1, null, null, -1, sortBy, page6.getContext());
       
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
       page3.getItems().stream().forEach( ce -> actualPage3Strings.add(ce.getScopeId()) );
       
       assertEquals(expectedPage3Strings, actualPage3Strings);

       
       //test first page of the results with empty sort order -> default sort order (by Id ascending)
       PaginationResponse<Alarm> page1EmptySort = testInterface.getForCustomer(customerId_1, null, null, -1, Collections.emptyList(), context);
       assertEquals(10, page1EmptySort.getItems().size());

       List<String> expectedPage1EmptySortStrings = new ArrayList<>(Arrays.asList(new String[]{"qr_0", "qr_1", "qr_2", "qr_3", "qr_4", "qr_5", "qr_6", "qr_7", "qr_8", "qr_9" }));
       List<String> actualPage1EmptySortStrings = new ArrayList<>();
       page1EmptySort.getItems().stream().forEach( ce -> actualPage1EmptySortStrings.add(ce.getScopeId()) );

       assertEquals(expectedPage1EmptySortStrings, actualPage1EmptySortStrings);

       //test first page of the results with null sort order -> default sort order (by Id ascending)
       PaginationResponse<Alarm> page1NullSort = testInterface.getForCustomer(customerId_1, null, null, -1, null, context);
       assertEquals(10, page1NullSort.getItems().size());

       List<String> expectedPage1NullSortStrings = new ArrayList<>(Arrays.asList(new String[]{"qr_0", "qr_1", "qr_2", "qr_3", "qr_4", "qr_5", "qr_6", "qr_7", "qr_8", "qr_9" }));
       List<String> actualPage1NullSortStrings = new ArrayList<>();
       page1NullSort.getItems().stream().forEach( ce -> actualPage1NullSortStrings.add(ce.getScopeId()) );

       assertEquals(expectedPage1NullSortStrings, actualPage1NullSortStrings);

       
       //test first page of the results with sort descending order by a equipmentId property 
       PaginationResponse<Alarm> page1SingleSortDesc = testInterface.getForCustomer(customerId_1, null, null, -1, Collections.singletonList(new ColumnAndSort("equipmentId", SortOrder.desc)), context);
       assertEquals(10, page1SingleSortDesc.getItems().size());

       List<String> expectedPage1SingleSortDescStrings = new ArrayList<	>(Arrays.asList(new String[]{"qr_49", "qr_48", "qr_47", "qr_46", "qr_45", "qr_44", "qr_43", "qr_42", "qr_41", "qr_40" }));
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

    }
    
    @Test
    public void testAlarmCountsModel() {
		AlarmCounts alarmCounts = new AlarmCounts();
		alarmCounts.setCustomerId(42);
		alarmCounts.addToCounter(0, AlarmCode.AssocFailure, 5);
		alarmCounts.addToCounter(1, AlarmCode.ChannelsOutOfSync2g, 7);
		alarmCounts.addToCounter(2, AlarmCode.ChannelsOutOfSync2g, 9);
		alarmCounts.addToCounter(3, AlarmCode.ChannelsOutOfSync5g, 1);
		
		AlarmCounts deserialized = BaseJsonModel.fromString(alarmCounts.toString(), AlarmCounts.class);

		assertEquals(alarmCounts.toString(), deserialized.toString());
				
    }
    
    @Test
    public void testAlarmCountsRetrieval() {
        //create some Alarms
        Alarm mdl;
        int customerId_1 = (int) testSequence.incrementAndGet();
        int customerId_2 = (int) testSequence.incrementAndGet();
        
        int apNameIdx = 0;
        Set<Long> equipmentIds = new HashSet<>();
        Set<Long> equipmentIds_CPUUtilization = new HashSet<>();
        Set<Long> equipmentIds_AccessPointIsUnreachable = new HashSet<>();
        
        for(int i = 0; i< 50; i++){
            mdl = createAlarmObject();
            mdl.setCustomerId(customerId_1);
            mdl.setScopeId("qr_"+apNameIdx);
            if((i%2) == 0) {
            	mdl.setAlarmCode(AlarmCode.CPUUtilization);
            	equipmentIds_CPUUtilization.add(mdl.getEquipmentId());
            } else {
            	equipmentIds_AccessPointIsUnreachable.add(mdl.getEquipmentId());
            }
            equipmentIds.add(mdl.getEquipmentId());
            
            apNameIdx++;
            testInterface.create(mdl);
        }
        
        {
        	mdl = createAlarmObject();
            mdl.setCustomerId(customerId_1);
            mdl.setEquipmentId(0);
          	mdl.setAlarmCode(AlarmCode.GenericError);
            
            testInterface.create(mdl);        	
        }

        for(int i = 0; i< 50; i++){
            mdl = createAlarmObject();
            mdl.setCustomerId(customerId_2);
            mdl.setScopeId("qr_"+apNameIdx);           
            apNameIdx++;
            testInterface.create(mdl);
        }
        
        Set<AlarmCode> alarmCodes = new HashSet<>(Arrays.asList(AlarmCode.AccessPointIsUnreachable, AlarmCode.GenericError, AlarmCode.CPUUtilization));

        AlarmCounts alarmCounts = testInterface.getAlarmCounts(customerId_1, equipmentIds, alarmCodes);
        assertEquals(0, alarmCounts.getCounter(0, AlarmCode.GenericError));
        assertEquals(25, alarmCounts.getCounter(0, AlarmCode.CPUUtilization));
        assertEquals(25, alarmCounts.getCounter(0, AlarmCode.AccessPointIsUnreachable));
        
        equipmentIds_CPUUtilization.forEach(eqId -> assertEquals(1, alarmCounts.getCounter(eqId, AlarmCode.CPUUtilization)));
        equipmentIds_AccessPointIsUnreachable.forEach(eqId -> assertEquals(1, alarmCounts.getCounter(eqId, AlarmCode.AccessPointIsUnreachable)) );        

		AlarmCounts alarmCounts_noEq = testInterface.getAlarmCounts(customerId_1, null, alarmCodes);
        assertEquals(1, alarmCounts_noEq.getCounter(0, AlarmCode.GenericError));
        assertEquals(25, alarmCounts_noEq.getCounter(0, AlarmCode.CPUUtilization));
        assertEquals(25, alarmCounts_noEq.getCounter(0, AlarmCode.AccessPointIsUnreachable));
        assertTrue(alarmCounts_noEq.getCountsPerEquipmentIdMap().isEmpty());
        assertEquals(3, alarmCounts_noEq.getTotalCountsPerAlarmCodeMap().size());

		AlarmCounts alarmCounts_noEq_1code = testInterface.getAlarmCounts(customerId_1, null, Collections.singleton(AlarmCode.CPUUtilization));
        assertEquals(0, alarmCounts_noEq_1code.getCounter(0, AlarmCode.GenericError));
        assertEquals(25, alarmCounts_noEq_1code.getCounter(0, AlarmCode.CPUUtilization));
        assertEquals(0, alarmCounts_noEq_1code.getCounter(0, AlarmCode.AccessPointIsUnreachable));
        assertTrue(alarmCounts_noEq_1code.getCountsPerEquipmentIdMap().isEmpty());
        assertEquals(1, alarmCounts_noEq_1code.getTotalCountsPerAlarmCodeMap().size());
        
		AlarmCounts alarmCounts_1Eq_1code = testInterface.getAlarmCounts(customerId_1, Collections.singleton(equipmentIds.iterator().next()), Collections.singleton(AlarmCode.CPUUtilization));
        assertEquals(0, alarmCounts_1Eq_1code.getCounter(0, AlarmCode.GenericError));
        assertEquals(1, alarmCounts_1Eq_1code.getCounter(equipmentIds.iterator().next(), AlarmCode.CPUUtilization));
        assertEquals(1, alarmCounts_1Eq_1code.getCounter(0, AlarmCode.CPUUtilization));
        assertEquals(0, alarmCounts_1Eq_1code.getCounter(0, AlarmCode.AccessPointIsUnreachable));
        assertEquals(1, alarmCounts_1Eq_1code.getCountsPerEquipmentIdMap().size());
        assertEquals(1, alarmCounts_1Eq_1code.getTotalCountsPerAlarmCodeMap().size());

    }
    
    
    private Alarm createAlarmObject() {
    	Alarm result = new Alarm();
        result.setCustomerId((int) testSequence.getAndIncrement());
        result.setEquipmentId(testSequence.getAndIncrement());
        result.setAlarmCode(AlarmCode.AccessPointIsUnreachable);
        result.setCreatedTimestamp(System.currentTimeMillis());
        
        result.setScopeId("test-scope-"  + result.getEquipmentId());
        
        AlarmDetails details = new AlarmDetails();
        details.setMessage("test-details-" + result.getEquipmentId());
		result.setDetails(details );
        return result;
    }
}
