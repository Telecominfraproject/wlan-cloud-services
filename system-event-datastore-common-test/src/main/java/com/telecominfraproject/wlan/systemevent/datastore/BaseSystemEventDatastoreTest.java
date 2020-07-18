package com.telecominfraproject.wlan.systemevent.datastore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pagination.SortOrder;
import com.telecominfraproject.wlan.systemevent.models.AnotherTestSystemEvent;
import com.telecominfraproject.wlan.systemevent.models.SystemEventRecord;
import com.telecominfraproject.wlan.systemevent.models.TestSystemEvent;
import com.telecominfraproject.wlan.systemevent.models.UnserializableSystemEvent;

/**
 * @author dtoptygin
 *
 */
public abstract class BaseSystemEventDatastoreTest {
    @Autowired
    protected SystemEventDatastore testInterface;

    protected static final AtomicLong testSequence = new AtomicLong(1);

    @Test
    public void testCRD() {
    	long baseTimestamp = System.currentTimeMillis();
		TestSystemEvent tse = new TestSystemEvent(
				(int) testSequence.incrementAndGet(), 
				testSequence.incrementAndGet(),
				baseTimestamp, "testStr");
    	SystemEventRecord systemEventRecord = new SystemEventRecord(tse);
        
        //create single
        testInterface.create(systemEventRecord);        
        PaginationResponse<SystemEventRecord> resp = testInterface.getForCustomer(0, baseTimestamp, systemEventRecord.getCustomerId(), Collections.singleton(systemEventRecord.getEquipmentId()), null, null, null);
        assertEquals(1, resp.getItems().size());
        assertEquals(systemEventRecord, resp.getItems().get(0)); 
            
        List<SystemEventRecord> metricsToCreate = new ArrayList<>();
        for(int i = 0; i< 10; i++) {
    		tse = new TestSystemEvent(
    				systemEventRecord.getCustomerId(), 
    				systemEventRecord.getEquipmentId(),
    				baseTimestamp - (i+1) * 10, 
    				"testStr");
    		SystemEventRecord sm = new SystemEventRecord(tse);

            metricsToCreate.add(sm);
        }

        //create bulk
        testInterface.create(metricsToCreate);
        PaginationResponse<SystemEventRecord> respBulk = testInterface.getForCustomer(0, baseTimestamp, systemEventRecord.getCustomerId(), Collections.singleton(systemEventRecord.getEquipmentId()), null, null, null);
        assertEquals(11, respBulk.getItems().size());
        metricsToCreate.forEach(m -> assertTrue(respBulk.getItems().contains(m)));
        assertTrue(respBulk.getItems().contains(systemEventRecord));

                
        //delete
        testInterface.delete(systemEventRecord.getCustomerId(), systemEventRecord.getEquipmentId(), baseTimestamp + 1);
        resp = testInterface.getForCustomer(0, baseTimestamp, systemEventRecord.getCustomerId(), Collections.singleton(systemEventRecord.getEquipmentId()), null, null, null);
        assertTrue(resp.getItems().isEmpty());
        
    }
    
    //verify returned data and sort options
    @Test
    public void testPagination() {
    	
        int customerId_1 = (int) testSequence.incrementAndGet();
        int customerId_2 = (int) testSequence.incrementAndGet();

         long baseTimestamp = System.currentTimeMillis();

        long apNameIdx = 0;
                
        long fromTime = 0;
        long toTime = baseTimestamp;
        
        Set<Long> used_equipmentIds = new HashSet<>();

        //create System events
        
        //events to be tested
        for(int i = 0; i< 50; i++){
        	
        	TestSystemEvent tse = new TestSystemEvent(
        			customerId_1, 
        			testSequence.incrementAndGet(),
        			baseTimestamp - 100000 + testSequence.incrementAndGet() , 
        			"qr_"+apNameIdx);

        	SystemEventRecord systemEventRecord = new SystemEventRecord(tse);

            apNameIdx++;

            testInterface.create(systemEventRecord);
            used_equipmentIds.add(systemEventRecord.getEquipmentId());
        }

        //events outside the target time
        for(int i = 0; i< 50; i++){

        	TestSystemEvent tse = new TestSystemEvent(
        			customerId_1, 
        			testSequence.incrementAndGet(),
        			baseTimestamp + testSequence.incrementAndGet() , 
        			"qr_"+apNameIdx);

        	SystemEventRecord systemEventRecord = new SystemEventRecord(tse);

            apNameIdx++;

            testInterface.create(systemEventRecord);
            used_equipmentIds.add(systemEventRecord.getEquipmentId());
        }
        
        //events for another customer
        for(int i = 0; i< 50; i++){
        	
        	TestSystemEvent tse = new TestSystemEvent(
        			customerId_2, 
        			testSequence.incrementAndGet(),
        			baseTimestamp - 100000 + testSequence.incrementAndGet() , 
        			"qr_"+apNameIdx);

        	SystemEventRecord systemEventRecord = new SystemEventRecord(tse);

            apNameIdx++;

            testInterface.create(systemEventRecord);
            used_equipmentIds.add(systemEventRecord.getEquipmentId());
        }

        //paginate over events
        
        List<ColumnAndSort> sortBy = new ArrayList<>();
        sortBy.addAll(Arrays.asList(new ColumnAndSort("equipmentId")));
        
        PaginationContext<SystemEventRecord> context = new PaginationContext<>(10);
        PaginationResponse<SystemEventRecord> page1 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, sortBy, context);
        PaginationResponse<SystemEventRecord> page2 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, sortBy, page1.getContext());
        PaginationResponse<SystemEventRecord> page3 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, sortBy, page2.getContext());
        PaginationResponse<SystemEventRecord> page4 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, sortBy, page3.getContext());
        PaginationResponse<SystemEventRecord> page5 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, sortBy, page4.getContext());
        PaginationResponse<SystemEventRecord> page6 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, sortBy, page5.getContext());
        PaginationResponse<SystemEventRecord> page7 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, sortBy, page6.getContext());
        
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
        
        List<String> expectedPage3Strings = new ArrayList<>(Arrays.asList(new String[]{"qr_20", "qr_21", "qr_22", "qr_23", "qr_24", "qr_25", "qr_26", "qr_27", "qr_28", "qr_29" }));
        List<String> actualPage3Strings = new ArrayList<>();
        page3.getItems().stream().forEach( ce -> actualPage3Strings.add(((TestSystemEvent) ce.getDetails()).getPayload()) );
        
        assertEquals(expectedPage3Strings, actualPage3Strings);
       
        //test first page of the results with empty sort order -> default sort order (by createdTimestamp ascending)
        PaginationResponse<SystemEventRecord> page1EmptySort = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, Collections.emptyList(), context);
        assertEquals(10, page1EmptySort.getItems().size());

        List<String> expectedPage1EmptySortStrings = new ArrayList<>(Arrays.asList(new String[]{"qr_0", "qr_1", "qr_2", "qr_3", "qr_4", "qr_5", "qr_6", "qr_7", "qr_8", "qr_9" }));
        List<String> actualPage1EmptySortStrings = new ArrayList<>();
        page1EmptySort.getItems().stream().forEach( ce -> actualPage1EmptySortStrings.add(((TestSystemEvent) ce.getDetails()).getPayload()) );

        assertEquals(expectedPage1EmptySortStrings, actualPage1EmptySortStrings);

        //test first page of the results with null sort order -> default sort order (by createdTimestamp ascending)
        PaginationResponse<SystemEventRecord> page1NullSort = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, context);
        assertEquals(10, page1NullSort.getItems().size());

        List<String> expectedPage1NullSortStrings = new ArrayList<>(Arrays.asList(new String[]{"qr_0", "qr_1", "qr_2", "qr_3", "qr_4", "qr_5", "qr_6", "qr_7", "qr_8", "qr_9" }));
        List<String> actualPage1NullSortStrings = new ArrayList<>();
        page1NullSort.getItems().stream().forEach( ce -> actualPage1NullSortStrings.add(((TestSystemEvent) ce.getDetails()).getPayload()) );

        assertEquals(expectedPage1NullSortStrings, actualPage1NullSortStrings);

        
        //test first page of the results with sort descending order by a equipmentId property 
        PaginationResponse<SystemEventRecord> page1SingleSortDesc = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, Collections.singletonList(new ColumnAndSort("equipmentId", SortOrder.desc)), context);
        assertEquals(10, page1SingleSortDesc.getItems().size());

        List<String> expectedPage1SingleSortDescStrings = new ArrayList<	>(Arrays.asList(new String[]{"qr_49", "qr_48", "qr_47", "qr_46", "qr_45", "qr_44", "qr_43", "qr_42", "qr_41", "qr_40" }));
        List<String> actualPage1SingleSortDescStrings = new ArrayList<>();
        page1SingleSortDesc.getItems().stream().forEach( ce -> actualPage1SingleSortDescStrings.add(((TestSystemEvent) ce.getDetails()).getPayload()) );
        
        assertEquals(expectedPage1SingleSortDescStrings, actualPage1SingleSortDescStrings);        

        //testInterface.delete(System.currentTimeMillis());
        
        used_equipmentIds.forEach(eqId -> testInterface.delete(customerId_1, eqId, System.currentTimeMillis()));
        used_equipmentIds.forEach(eqId -> testInterface.delete(customerId_2, eqId, System.currentTimeMillis()));

     }
    
    
    @Test
    public void testPaginationWithFilters() {
       //create 30 events for our customer_1: 3 events per 10 equipment 
       int customerId_1 = (int) testSequence.incrementAndGet();
       int customerId_2 = (int) testSequence.incrementAndGet();
       
       long[] equipmentIds_1 = new long[10];
       for(int i=0; i<10; i++) {
    	   equipmentIds_1[i] =  testSequence.incrementAndGet(); 
       }

       String dataType_1 = "TestSystemEvent";
       String dataType_2 = "UnserializableSystemEvent";
       String dataType_3 = "AnotherTestSystemEvent";
       
       Set<Long> emptyEquipment = new HashSet<>();
       Set<Long> oneEquipment = new HashSet<>();
       oneEquipment.add(equipmentIds_1[0]);

       Set<Long> twoEquipment = new HashSet<>();
       twoEquipment.add(equipmentIds_1[0]);
       twoEquipment.add(equipmentIds_1[1]);

       Set<String> emptyDataTypes = new HashSet<>();
       Set<String> oneDataType = new HashSet<>();
       oneDataType.add(dataType_1);
       
       Set<String> twoDataTypes = new HashSet<>();
       twoDataTypes.add(dataType_1);
       twoDataTypes.add(dataType_2);

       Set<String> threeDataTypes = new HashSet<>();
       threeDataTypes.add(dataType_1);
       threeDataTypes.add(dataType_2);
       threeDataTypes.add(dataType_3);

       long baseTimestamp = System.currentTimeMillis();

       long apNameIdx = 0;
       
       for(int i = 0; i< equipmentIds_1.length; i++){
	       	TestSystemEvent tse = new TestSystemEvent(
	    			customerId_1, 
	    			equipmentIds_1[i],
	    			baseTimestamp - testSequence.incrementAndGet() , 
	    			"qr_"+apNameIdx);
	
	    	SystemEventRecord systemEventRecord = new SystemEventRecord(tse);

           testInterface.create(systemEventRecord);


	       	UnserializableSystemEvent use = new UnserializableSystemEvent(
	    			customerId_1, 
	    			equipmentIds_1[i],
	    			baseTimestamp - testSequence.incrementAndGet() , 
	    			"qr_"+apNameIdx);
	
	    	systemEventRecord = new SystemEventRecord(use);

           testInterface.create(systemEventRecord);

           
	       	AnotherTestSystemEvent atse = new AnotherTestSystemEvent(
	    			customerId_1, 
	    			equipmentIds_1[i],
	    			baseTimestamp - testSequence.incrementAndGet() , 
	    			"qr_"+apNameIdx);
	
	    	systemEventRecord = new SystemEventRecord(atse);

           testInterface.create(systemEventRecord);

           apNameIdx++;
       }

       //add some events for another customer into the mix
       for(int i = 0; i< 10; i++){
    	   
	       	TestSystemEvent tse = new TestSystemEvent(
	    			customerId_2, 
	    			equipmentIds_1[i],
	    			baseTimestamp - testSequence.incrementAndGet() , 
	    			"qr_"+apNameIdx);
	
	    	SystemEventRecord systemEventRecord = new SystemEventRecord(tse);

           testInterface.create(systemEventRecord);

           apNameIdx++;
       }

       //
       //Now paginate over SystemEvents with various filters:
       //
       
       List<ColumnAndSort> sortBy = new ArrayList<>();
       sortBy.addAll(Arrays.asList(new ColumnAndSort("equipmentId")));
       
       long fromTime = 0;
       long toTime = baseTimestamp;
       
       //Paginate over all equipment and all metrics
       PaginationContext<SystemEventRecord> context = new PaginationContext<>(10);
       PaginationResponse<SystemEventRecord> page1 = testInterface.getForCustomer(fromTime, toTime, customerId_1, emptyEquipment, emptyDataTypes, sortBy, context);
       PaginationResponse<SystemEventRecord> page2 = testInterface.getForCustomer(fromTime, toTime, customerId_1, emptyEquipment, emptyDataTypes, sortBy, page1.getContext());
       PaginationResponse<SystemEventRecord> page3 = testInterface.getForCustomer(fromTime, toTime, customerId_1, emptyEquipment, emptyDataTypes, sortBy, page2.getContext());
       PaginationResponse<SystemEventRecord> page4 = testInterface.getForCustomer(fromTime, toTime, customerId_1, emptyEquipment, emptyDataTypes, sortBy, page3.getContext());
       
       //verify returned pages
       assertEquals(10, page1.getItems().size());
       assertEquals(10, page2.getItems().size());
       assertEquals(10, page3.getItems().size());
       assertEquals(0, page4.getItems().size());
       
       page1.getItems().forEach(e -> assertEquals(customerId_1, e.getCustomerId()) );
       page2.getItems().forEach(e -> assertEquals(customerId_1, e.getCustomerId()) );
       page3.getItems().forEach(e -> assertEquals(customerId_1, e.getCustomerId()) );
       page4.getItems().forEach(e -> assertEquals(customerId_1, e.getCustomerId()) );
       
       assertFalse(page1.getContext().isLastPage());
       assertFalse(page2.getContext().isLastPage());
       assertFalse(page3.getContext().isLastPage());

       assertTrue(page4.getContext().isLastPage());
       
       //Paginate over all equipment and all statuses - with null parameters
       context = new PaginationContext<>(10);
       page1 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, sortBy, context);
       page2 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, sortBy, page1.getContext());
       page3 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, sortBy, page2.getContext());
       page4 = testInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, sortBy, page3.getContext());
       
       //verify returned pages
       assertEquals(10, page1.getItems().size());
       assertEquals(10, page2.getItems().size());
       assertEquals(10, page3.getItems().size());
       assertEquals(0, page4.getItems().size());
       
       page1.getItems().forEach(e -> assertEquals(customerId_1, e.getCustomerId()) );
       page2.getItems().forEach(e -> assertEquals(customerId_1, e.getCustomerId()) );
       page3.getItems().forEach(e -> assertEquals(customerId_1, e.getCustomerId()) );
       page4.getItems().forEach(e -> assertEquals(customerId_1, e.getCustomerId()) );
       
       assertFalse(page1.getContext().isLastPage());
       assertFalse(page2.getContext().isLastPage());
       assertFalse(page3.getContext().isLastPage());

       assertTrue(page4.getContext().isLastPage());

       Set<Long> returnedEquipmentIds = new HashSet<>();
       Set<String> returnedDataTypes = new HashSet<>();
       
       //Paginate over oneEquipment, all client macs and all statuses
       context = new PaginationContext<>(10);
       returnedEquipmentIds.clear();
       returnedDataTypes.clear();
       page1 = testInterface.getForCustomer(fromTime, toTime, customerId_1, oneEquipment, emptyDataTypes, sortBy, context);
       page2 = testInterface.getForCustomer(fromTime, toTime, customerId_1, oneEquipment, emptyDataTypes, sortBy, page1.getContext());
       
       //verify returned pages
       assertEquals(3, page1.getItems().size());
       assertEquals(0, page2.getItems().size());
       
		page1.getItems().forEach(e -> {
			assertEquals(customerId_1, e.getCustomerId());
			returnedEquipmentIds.add(e.getEquipmentId());
			returnedDataTypes.add(e.getDataType());
		});
		
		assertEquals(oneEquipment, returnedEquipmentIds);
		assertEquals(threeDataTypes, returnedDataTypes);
       
       assertTrue(page1.getContext().isLastPage());
       assertTrue(page2.getContext().isLastPage());
       
       //Paginate over twoEquipment, all client macs and all statuses
       context = new PaginationContext<>(5);
       returnedEquipmentIds.clear();
       returnedDataTypes.clear();
       page1 = testInterface.getForCustomer(fromTime, toTime, customerId_1, twoEquipment, emptyDataTypes, sortBy, context);
       page2 = testInterface.getForCustomer(fromTime, toTime, customerId_1, twoEquipment, emptyDataTypes, sortBy, page1.getContext());
       page3 = testInterface.getForCustomer(fromTime, toTime, customerId_1, twoEquipment, emptyDataTypes, sortBy, page2.getContext());
       
       //verify returned pages
       assertEquals(5, page1.getItems().size());
       assertEquals(1, page2.getItems().size());
       assertEquals(0, page3.getItems().size());
       
		page1.getItems().forEach(e -> {
			assertEquals(customerId_1, e.getCustomerId());
			returnedEquipmentIds.add(e.getEquipmentId());
			returnedDataTypes.add(e.getDataType());
		});

		page2.getItems().forEach(e -> {
			assertEquals(customerId_1, e.getCustomerId());
			returnedEquipmentIds.add(e.getEquipmentId());
			returnedDataTypes.add(e.getDataType());
		});

		assertEquals(twoEquipment, returnedEquipmentIds);
		assertEquals(threeDataTypes, returnedDataTypes);

       
       assertFalse(page1.getContext().isLastPage());
       assertTrue(page2.getContext().isLastPage());
       assertTrue(page3.getContext().isLastPage());

       //Paginate over oneEquipment, all client macs and oneStatus
       context = new PaginationContext<>(10);
       returnedEquipmentIds.clear();
       returnedDataTypes.clear();
       page1 = testInterface.getForCustomer(fromTime, toTime, customerId_1, oneEquipment, oneDataType, sortBy, context);
       page2 = testInterface.getForCustomer(fromTime, toTime, customerId_1, oneEquipment, oneDataType, sortBy, page1.getContext());
       
       //verify returned pages
       assertEquals(1, page1.getItems().size());
       assertEquals(0, page2.getItems().size());
       
		page1.getItems().forEach(e -> {
			assertEquals(customerId_1, e.getCustomerId());
			returnedEquipmentIds.add(e.getEquipmentId());
			returnedDataTypes.add(e.getDataType());
		});
		
		assertEquals(oneEquipment, returnedEquipmentIds);
		assertEquals(oneDataType, returnedDataTypes);

       assertTrue(page1.getContext().isLastPage());
       assertTrue(page2.getContext().isLastPage());
       
       //Paginate over oneEquipment, all client macs and twoStatuses
       context = new PaginationContext<>(10);
       returnedEquipmentIds.clear();
       returnedDataTypes.clear();
       page1 = testInterface.getForCustomer(fromTime, toTime, customerId_1, oneEquipment, twoDataTypes, sortBy, context);
       page2 = testInterface.getForCustomer(fromTime, toTime, customerId_1, oneEquipment, twoDataTypes, sortBy, page1.getContext());
       
       //verify returned pages
       assertEquals(2, page1.getItems().size());
       assertEquals(0, page2.getItems().size());
       
		page1.getItems().forEach(e -> {
			assertEquals(customerId_1, e.getCustomerId());
			returnedEquipmentIds.add(e.getEquipmentId());
			returnedDataTypes.add(e.getDataType());
		});
		
		assertEquals(oneEquipment, returnedEquipmentIds);
		assertEquals(twoDataTypes, returnedDataTypes);
       
       assertTrue(page1.getContext().isLastPage());
       assertTrue(page2.getContext().isLastPage());
       
       //Paginate over twoEquipment, all client macs and twoStatuses
       context = new PaginationContext<>(10);
       returnedEquipmentIds.clear();
       returnedDataTypes.clear();
       page1 = testInterface.getForCustomer(fromTime, toTime, customerId_1, twoEquipment, twoDataTypes, sortBy, context);
       page2 = testInterface.getForCustomer(fromTime, toTime, customerId_1, twoEquipment, twoDataTypes, sortBy, page1.getContext());
       
       //verify returned pages
       assertEquals(4, page1.getItems().size());
       assertEquals(0, page2.getItems().size());
       
		page1.getItems().forEach(e -> {
			assertEquals(customerId_1, e.getCustomerId());
			returnedEquipmentIds.add(e.getEquipmentId());
			returnedDataTypes.add(e.getDataType());
		});
		
		assertEquals(twoEquipment, returnedEquipmentIds);
		assertEquals(twoDataTypes, returnedDataTypes);
       
       assertTrue(page1.getContext().isLastPage());
       assertTrue(page2.getContext().isLastPage());

       Arrays.stream(equipmentIds_1).forEach(eqId -> testInterface.delete(customerId_1, eqId, System.currentTimeMillis()));
       Arrays.stream(equipmentIds_1).forEach(eqId -> testInterface.delete(customerId_2, eqId, System.currentTimeMillis()));

    }
    
}
