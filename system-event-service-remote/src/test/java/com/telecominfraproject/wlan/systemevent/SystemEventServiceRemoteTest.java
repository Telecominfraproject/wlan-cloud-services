package com.telecominfraproject.wlan.systemevent;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pagination.SortOrder;
import com.telecominfraproject.wlan.remote.tests.BaseRemoteTest;
import com.telecominfraproject.wlan.systemevent.models.AnotherTestSystemEvent;
import com.telecominfraproject.wlan.systemevent.models.SystemEventRecord;
import com.telecominfraproject.wlan.systemevent.models.TestSystemEvent;
import com.telecominfraproject.wlan.systemevent.models.UnserializableSystemEvent;

/**
 * @author dtoptygin
 *
 */
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = {
        "integration_test",
        "no_ssl","http_digest_auth","rest-template-single-user-per-service-digest-auth",
        }) //NOTE: these profiles will be ADDED to the list of active profiles  
public class SystemEventServiceRemoteTest extends BaseRemoteTest {

    
    @Autowired SystemEventServiceRemote remoteInterface; 
    
    @Before public void urlSetup(){
        configureBaseUrl("tip.wlan.systemEventServiceBaseUrl");
    }
    
    @Test
    public void testCRD() {
    	long baseTimestamp = System.currentTimeMillis();
		TestSystemEvent tse = new TestSystemEvent(
				getNextCustomerId(), 
				getNextEquipmentId(),
				baseTimestamp, "testStr");
    	SystemEventRecord systemEventRecord = new SystemEventRecord(tse);
        
        //create single
        remoteInterface.create(systemEventRecord);        
        PaginationResponse<SystemEventRecord> resp = remoteInterface.getForCustomer(0, baseTimestamp, systemEventRecord.getCustomerId(), null, Collections.singleton(systemEventRecord.getEquipmentId()), null, null, null, null);
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
        remoteInterface.create(metricsToCreate);
        PaginationResponse<SystemEventRecord> respBulk = remoteInterface.getForCustomer(0, baseTimestamp, systemEventRecord.getCustomerId(), null, Collections.singleton(systemEventRecord.getEquipmentId()), null, null, null, null);
        assertEquals(11, respBulk.getItems().size());
        metricsToCreate.forEach(m -> assertTrue(respBulk.getItems().contains(m)));
        assertTrue(respBulk.getItems().contains(systemEventRecord));

                
        //delete
        remoteInterface.delete(systemEventRecord.getCustomerId(), systemEventRecord.getEquipmentId(), baseTimestamp + 1);
        resp = remoteInterface.getForCustomer(0, baseTimestamp, systemEventRecord.getCustomerId(), null, Collections.singleton(systemEventRecord.getEquipmentId()), null, null, null, null);
        assertTrue(resp.getItems().isEmpty());
        
    }
    
    //verify returned data and sort options
    @Test
    public void testPagination() {
    	
        int customerId_1 = getNextCustomerId();
        int customerId_2 = getNextCustomerId();

         long baseTimestamp = System.currentTimeMillis();

        long apNameIdx = 0;
                
        long fromTime = 0;
        long toTime = baseTimestamp;
        
        //create System events
        
        //events to be tested
        for(int i = 0; i< 50; i++){
        	
        	TestSystemEvent tse = new TestSystemEvent(
        			customerId_1, 
        			getNextEquipmentId(),
        			baseTimestamp - 100000 + getNextEquipmentId() , 
        			"qr_"+apNameIdx);

        	SystemEventRecord systemEventRecord = new SystemEventRecord(tse);

            apNameIdx++;

            remoteInterface.create(systemEventRecord);
        }

        //events outside the target time
        for(int i = 0; i< 50; i++){

        	TestSystemEvent tse = new TestSystemEvent(
        			customerId_1, 
        			getNextEquipmentId(),
        			baseTimestamp + getNextEquipmentId(), 
        			"qr_"+apNameIdx);

        	SystemEventRecord systemEventRecord = new SystemEventRecord(tse);

            apNameIdx++;

            remoteInterface.create(systemEventRecord);
        }
        
        //events for another customer
        for(int i = 0; i< 50; i++){
        	
        	TestSystemEvent tse = new TestSystemEvent(
        			customerId_2, 
        			getNextEquipmentId(),
        			baseTimestamp - 100000 + getNextEquipmentId() , 
        			"qr_"+apNameIdx);

        	SystemEventRecord systemEventRecord = new SystemEventRecord(tse);

            apNameIdx++;

            remoteInterface.create(systemEventRecord);
        }

        //paginate over events
        
        List<ColumnAndSort> sortBy = new ArrayList<>();
        sortBy.addAll(Arrays.asList(new ColumnAndSort("equipmentId")));
        
        PaginationContext<SystemEventRecord> context = new PaginationContext<>(10);
        PaginationResponse<SystemEventRecord> page1 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, sortBy, context);
        PaginationResponse<SystemEventRecord> page2 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, sortBy, page1.getContext());
        PaginationResponse<SystemEventRecord> page3 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, sortBy, page2.getContext());
        PaginationResponse<SystemEventRecord> page4 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, sortBy, page3.getContext());
        PaginationResponse<SystemEventRecord> page5 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, sortBy, page4.getContext());
        PaginationResponse<SystemEventRecord> page6 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, sortBy, page5.getContext());
        PaginationResponse<SystemEventRecord> page7 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, sortBy, page6.getContext());
        
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
        PaginationResponse<SystemEventRecord> page1EmptySort = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, Collections.emptyList(), context);
        assertEquals(10, page1EmptySort.getItems().size());

        List<String> expectedPage1EmptySortStrings = new ArrayList<>(Arrays.asList(new String[]{"qr_0", "qr_1", "qr_2", "qr_3", "qr_4", "qr_5", "qr_6", "qr_7", "qr_8", "qr_9" }));
        List<String> actualPage1EmptySortStrings = new ArrayList<>();
        page1EmptySort.getItems().stream().forEach( ce -> actualPage1EmptySortStrings.add(((TestSystemEvent) ce.getDetails()).getPayload()) );

        assertEquals(expectedPage1EmptySortStrings, actualPage1EmptySortStrings);

        //test first page of the results with null sort order -> default sort order (by createdTimestamp ascending)
        PaginationResponse<SystemEventRecord> page1NullSort = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, null, context);
        assertEquals(10, page1NullSort.getItems().size());

        List<String> expectedPage1NullSortStrings = new ArrayList<>(Arrays.asList(new String[]{"qr_0", "qr_1", "qr_2", "qr_3", "qr_4", "qr_5", "qr_6", "qr_7", "qr_8", "qr_9" }));
        List<String> actualPage1NullSortStrings = new ArrayList<>();
        page1NullSort.getItems().stream().forEach( ce -> actualPage1NullSortStrings.add(((TestSystemEvent) ce.getDetails()).getPayload()) );

        assertEquals(expectedPage1NullSortStrings, actualPage1NullSortStrings);

        
        //test first page of the results with sort descending order by a equipmentId property 
        PaginationResponse<SystemEventRecord> page1SingleSortDesc = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, Collections.singletonList(new ColumnAndSort("equipmentId", SortOrder.desc)), context);
        assertEquals(10, page1SingleSortDesc.getItems().size());

        List<String> expectedPage1SingleSortDescStrings = new ArrayList<	>(Arrays.asList(new String[]{"qr_49", "qr_48", "qr_47", "qr_46", "qr_45", "qr_44", "qr_43", "qr_42", "qr_41", "qr_40" }));
        List<String> actualPage1SingleSortDescStrings = new ArrayList<>();
        page1SingleSortDesc.getItems().stream().forEach( ce -> actualPage1SingleSortDescStrings.add(((TestSystemEvent) ce.getDetails()).getPayload()) );
        
        assertEquals(expectedPage1SingleSortDescStrings, actualPage1SingleSortDescStrings);        

     }
    
    
    @Test
    public void testPaginationWithFilters() {
       //create 30 events for our customer_1: 3 events per 10 equipment 
       int customerId_1 = getNextCustomerId();
       int customerId_2 = getNextCustomerId();
       
       long[] locationIds = new long[10];
       for(int i=0; i<10; i++) {
           locationIds[i] =  getNextLocationId(); 
       }

       long[] equipmentIds_1 = new long[10];
       for(int i=0; i<10; i++) {
    	   equipmentIds_1[i] =  getNextEquipmentId(); 
       }

       List<MacAddress> clientMacs = new ArrayList<>();
       for(int i=0; i<10; i++) {
           clientMacs.add(new MacAddress(getNextEquipmentId())); 
       }

       String dataType_1 = "TestSystemEvent";
       String dataType_2 = "UnserializableSystemEvent";
       String dataType_3 = "AnotherTestSystemEvent";
       
       Set<Long> emptyLocations = new HashSet<>();
       Set<Long> oneLocation = new HashSet<>();
       oneLocation.add(locationIds[0]);

       Set<Long> twoLocations = new HashSet<>();
       twoLocations.add(locationIds[0]);
       twoLocations.add(locationIds[1]);

       Set<Long> emptyEquipment = new HashSet<>();
       Set<Long> oneEquipment = new HashSet<>();
       oneEquipment.add(equipmentIds_1[0]);

       Set<Long> twoEquipment = new HashSet<>();
       twoEquipment.add(equipmentIds_1[0]);
       twoEquipment.add(equipmentIds_1[1]);

       Set<MacAddress> emptyMacs = new HashSet<>();
       Set<MacAddress> oneMac = new HashSet<>();
       oneMac.add(clientMacs.get(0));

       Set<MacAddress> twoMacs = new HashSet<>();
       twoMacs.add(clientMacs.get(0));
       twoMacs.add(clientMacs.get(1));

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
	    			baseTimestamp - getNextEquipmentId() , 
	    			"qr_"+apNameIdx);
	
	    	SystemEventRecord systemEventRecord = new SystemEventRecord(tse);
	    	systemEventRecord.setLocationId(locationIds[i]);
	        systemEventRecord.setClientMac(0);

           remoteInterface.create(systemEventRecord);


	       	UnserializableSystemEvent use = new UnserializableSystemEvent(
	    			customerId_1, 
	    			equipmentIds_1[i],
	    			baseTimestamp - getNextEquipmentId() , 
	    			"qr_"+apNameIdx);
	
	    	systemEventRecord = new SystemEventRecord(use);
            systemEventRecord.setLocationId(locationIds[i]);
            systemEventRecord.setClientMac(clientMacs.get(i).getAddressAsLong());

           remoteInterface.create(systemEventRecord);

           
	       	AnotherTestSystemEvent atse = new AnotherTestSystemEvent(
	    			customerId_1, 
	    			equipmentIds_1[i],
	    			baseTimestamp - getNextEquipmentId() , 
	    			"qr_"+apNameIdx);
	
	    	systemEventRecord = new SystemEventRecord(atse);
            systemEventRecord.setLocationId(locationIds[i]);
            systemEventRecord.setClientMac(0);

           remoteInterface.create(systemEventRecord);

           apNameIdx++;
       }

       //add some events for another customer into the mix
       for(int i = 0; i< 10; i++){
    	   
	       	TestSystemEvent tse = new TestSystemEvent(
	    			customerId_2, 
	    			equipmentIds_1[i],
	    			baseTimestamp - getNextEquipmentId() , 
	    			"qr_"+apNameIdx);
	
	    	SystemEventRecord systemEventRecord = new SystemEventRecord(tse);

           remoteInterface.create(systemEventRecord);

           apNameIdx++;
       }

       //
       //Now paginate over SystemEvents with various filters:
       //
       
       List<ColumnAndSort> sortBy = new ArrayList<>();
       sortBy.addAll(Arrays.asList(new ColumnAndSort("equipmentId")));
       
       long fromTime = 0;
       long toTime = baseTimestamp;
       
       //Paginate over all equipment and all service_metrics_collection_config
       PaginationContext<SystemEventRecord> context = new PaginationContext<>(10);
       PaginationResponse<SystemEventRecord> page1 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, emptyEquipment, null, emptyDataTypes, sortBy, context);
       PaginationResponse<SystemEventRecord> page2 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, emptyEquipment, null, emptyDataTypes, sortBy, page1.getContext());
       PaginationResponse<SystemEventRecord> page3 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, emptyEquipment, null, emptyDataTypes, sortBy, page2.getContext());
       PaginationResponse<SystemEventRecord> page4 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, emptyEquipment, null, emptyDataTypes, sortBy, page3.getContext());
       
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
       page1 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, sortBy, context);
       page2 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, sortBy, page1.getContext());
       page3 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, sortBy, page2.getContext());
       page4 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, null, null, null, sortBy, page3.getContext());
       
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
       page1 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, oneEquipment, null, emptyDataTypes, sortBy, context);
       page2 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, oneEquipment, null, emptyDataTypes, sortBy, page1.getContext());
       
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
       page1 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, twoEquipment, null, emptyDataTypes, sortBy, context);
       page2 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, twoEquipment, null, emptyDataTypes, sortBy, page1.getContext());
       page3 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, twoEquipment, null, emptyDataTypes, sortBy, page2.getContext());
       
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
       page1 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, oneEquipment, null, oneDataType, sortBy, context);
       page2 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, oneEquipment, null, oneDataType, sortBy, page1.getContext());
       
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
       page1 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, oneEquipment, null, twoDataTypes, sortBy, context);
       page2 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, oneEquipment, null, twoDataTypes, sortBy, page1.getContext());
       
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
       page1 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, twoEquipment, null, twoDataTypes, sortBy, context);
       page2 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, null, twoEquipment, null, twoDataTypes, sortBy, page1.getContext());
       
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

       //Paginate over twoEquipment, two locations, two client macs and two data types
       context = new PaginationContext<>(10);
       returnedEquipmentIds.clear();
       returnedDataTypes.clear();
       page1 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, twoLocations, twoEquipment, twoMacs, twoDataTypes, sortBy, context);
       page2 = remoteInterface.getForCustomer(fromTime, toTime, customerId_1, twoLocations, twoEquipment, twoMacs, twoDataTypes, sortBy, page1.getContext());
       
       //verify returned pages
       assertEquals(2, page1.getItems().size());
       assertEquals(0, page2.getItems().size());
       
        page1.getItems().forEach(e -> {
            assertEquals(customerId_1, e.getCustomerId());
            returnedEquipmentIds.add(e.getEquipmentId());
            returnedDataTypes.add(e.getDataType());
        });
        
        assertEquals(twoEquipment, returnedEquipmentIds);
        assertEquals(Set.of(dataType_2), returnedDataTypes);
       
       assertTrue(page1.getContext().isLastPage());
       assertTrue(page2.getContext().isLastPage());

    }
    
}
