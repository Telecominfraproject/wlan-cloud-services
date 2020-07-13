package com.telecominfraproject.wlan.status.datastore;

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

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pagination.SortOrder;
import com.telecominfraproject.wlan.status.equipment.models.EquipmentAdminStatusData;
import com.telecominfraproject.wlan.status.equipment.models.EquipmentProtocolStatusData;
import com.telecominfraproject.wlan.status.equipment.report.models.OperatingSystemPerformance;
import com.telecominfraproject.wlan.status.models.Status;
import com.telecominfraproject.wlan.status.models.StatusCode;
import com.telecominfraproject.wlan.status.models.StatusDataType;
import com.telecominfraproject.wlan.status.models.StatusDetails;

/**
 * @author dtoptygin
 *
 */
public abstract class BaseStatusDatastoreTest {
    @Autowired
    protected StatusDatastore testInterface;

    protected static final AtomicLong testSequence = new AtomicLong(1);

    @Test
    public void testCRUD() {
    	Status status = createStatusObject();

        //create
    	Status created = testInterface.update(status);
        assertNotNull(created);
        assertEquals(status.getEquipmentId(), created.getEquipmentId());
        assertEquals(status.getCustomerId(), created.getCustomerId());
        assertNotNull(created.getDetails());
        assertEquals(status.getDetails(), created.getDetails());
                
        // update
        ((EquipmentAdminStatusData) created.getDetails()).setStatusMessage("updated");
        Status updated = testInterface.update(created);
        assertNotNull(updated);
        assertEquals(created.getDetails(), updated.getDetails());
               
        //retrieve
        Status retrieved = testInterface.getOrNull(created.getCustomerId(), created.getEquipmentId(), created.getStatusDataType());
        assertNotNull(retrieved);
        assertEquals(retrieved.getLastModifiedTimestamp(), updated.getLastModifiedTimestamp());

        retrieved = testInterface.getOrNull(created.getCustomerId(), created.getEquipmentId(), created.getStatusDataType());
        assertNotNull(retrieved);
        assertEquals(retrieved.getLastModifiedTimestamp(), updated.getLastModifiedTimestamp());
        
        //retrieve non-existent    
        assertNull(testInterface.getOrNull(-1, -1, null));
        
        //delete
        List<Status> retrievedList = testInterface.delete(created.getCustomerId(), created.getEquipmentId());
        assertNotNull(retrievedList);
        assertEquals(1, retrievedList.size());
        
        //delete non-existent
    	retrievedList = testInterface.delete(-1, -1);
    	assertTrue(retrievedList.isEmpty());

    }
    
    @Test
    public void testForceUpdate() {
    	Status status = createStatusObject();

        //create
    	Status created = testInterface.update(status);
        assertNotNull(created);
                
        // update
        ((EquipmentAdminStatusData) created.getDetails()).setStatusMessage("updated");
        Status updated = testInterface.update(created);
        assertNotNull(updated);
        assertEquals(created.getDetails(), updated.getDetails());
        
        while(updated.getLastModifiedTimestamp() == created.getLastModifiedTimestamp()) {
        	//update again to make the timestamps different      	
            ((EquipmentAdminStatusData) created.getDetails()).setStatusMessage("updated_1");
            updated = testInterface.update(created);
        }

        //force UPDATE test - will not fail because of concurrent modification exception
    	Status modelConcurrentUpdate = created.clone();
        ((EquipmentAdminStatusData) modelConcurrentUpdate.getDetails()).setStatusMessage("updated concurrently");
        Status updatedConcurrently = testInterface.update(modelConcurrentUpdate);
    	assertEquals(modelConcurrentUpdate.getDetails(), updatedConcurrently.getDetails());
              
    	List<Status> retrievedList = testInterface.get(created.getCustomerId(), created.getEquipmentId());
    	assertEquals(1, retrievedList.size());
    	assertEquals(updatedConcurrently, retrievedList.get(0));
    	
        //delete
    	testInterface.delete(created.getCustomerId(), created.getEquipmentId());
    	
    }
    
    @Test
    public void testBulkForceUpdate() {
    	List<Status> statusBatch = new ArrayList<>();
    	for(int i =0; i<10; i++) {
        	Status status = createStatusObject();
        	((EquipmentAdminStatusData) status.getDetails()).setStatusMessage("initial_" + i);
        	statusBatch.add(status);
    	}
    	
    	List<StatusDetails> statusBatchDetails = new ArrayList<>();
    	statusBatch.forEach(s -> statusBatchDetails.add(s.getDetails()));
    	
        //create batch
    	List<Status> createdBatch = testInterface.update(statusBatch);
        assertNotNull(createdBatch);

        List<StatusDetails> createdBatchDetails = new ArrayList<>();
    	createdBatch.forEach(s -> createdBatchDetails.add(s.getDetails()));
        
        assertEquals(statusBatchDetails, createdBatchDetails);
                
        // update batch 
        createdBatch.forEach(s ->  ((EquipmentAdminStatusData) s.getDetails()).setStatusMessage(
        		((EquipmentAdminStatusData) s.getDetails()).getStatusMessage() + "_updated")
        		);
    	
    	List<Status>  updatedBatch = testInterface.update(createdBatch);
    	assertEquals(createdBatch.size(), updatedBatch.size());
    	updatedBatch.forEach(s -> assertTrue(((EquipmentAdminStatusData) s.getDetails()).getStatusMessage().startsWith("initial_")) );
    	updatedBatch.forEach(s -> assertTrue(((EquipmentAdminStatusData) s.getDetails()).getStatusMessage().endsWith("_updated")) );

        //force update batch test - will not fail because of concurrent modification exception
        createdBatch.forEach(s ->  ((EquipmentAdminStatusData) s.getDetails()).setStatusMessage(
        		((EquipmentAdminStatusData) s.getDetails()).getStatusMessage() + "_1")
        		);
    	
    	updatedBatch = testInterface.update(createdBatch);
    	assertEquals(createdBatch.size(), updatedBatch.size());
    	updatedBatch.forEach(s -> assertTrue(((EquipmentAdminStatusData) s.getDetails()).getStatusMessage().startsWith("initial_")) );
    	updatedBatch.forEach(s -> assertTrue(((EquipmentAdminStatusData) s.getDetails()).getStatusMessage().endsWith("_updated_1")) );
              
        //delete
    	createdBatch.forEach(s ->  testInterface.delete(s.getCustomerId(), s.getEquipmentId()));
    	
    	//verify that delete went through
    	createdBatch.forEach(s ->  assertTrue(testInterface.get(s.getCustomerId(), s.getEquipmentId()).isEmpty()));
        
    }
    
    @Test
    public void testStatusPagination()
    {
       //create 100 Statuses
       Status mdl;
       int customerId_1 = (int) testSequence.incrementAndGet();
       int customerId_2 = (int) testSequence.incrementAndGet();
       
       int apNameIdx = 0;
       
       List<Status> allCreatedStatuses = new ArrayList<>(); 
       
       for(int i = 0; i< 50; i++){
           mdl = createStatusObject();
           mdl.setCustomerId(customerId_1);
           ((EquipmentAdminStatusData) mdl.getDetails()).setStatusMessage("qr_"+apNameIdx);

           apNameIdx++;
           mdl = testInterface.update(mdl);
           allCreatedStatuses.add(mdl);
       }

       for(int i = 0; i< 50; i++){
           mdl = createStatusObject();
           mdl.setCustomerId(customerId_2);
           ((EquipmentAdminStatusData) mdl.getDetails()).setStatusMessage("qr_"+apNameIdx);

           apNameIdx++;
           mdl = testInterface.update(mdl);
           allCreatedStatuses.add(mdl);
       }

       //paginate over Statuses
       
       List<ColumnAndSort> sortBy = new ArrayList<>();
       sortBy.addAll(Arrays.asList(new ColumnAndSort("equipmentId")));
       
       PaginationContext<Status> context = new PaginationContext<>(10);
       PaginationResponse<Status> page1 = testInterface.getForCustomer(customerId_1, sortBy, context);
       PaginationResponse<Status> page2 = testInterface.getForCustomer(customerId_1, sortBy, page1.getContext());
       PaginationResponse<Status> page3 = testInterface.getForCustomer(customerId_1, sortBy, page2.getContext());
       PaginationResponse<Status> page4 = testInterface.getForCustomer(customerId_1, sortBy, page3.getContext());
       PaginationResponse<Status> page5 = testInterface.getForCustomer(customerId_1, sortBy, page4.getContext());
       PaginationResponse<Status> page6 = testInterface.getForCustomer(customerId_1, sortBy, page5.getContext());
       PaginationResponse<Status> page7 = testInterface.getForCustomer(customerId_1, sortBy, page6.getContext());
       
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
       page3.getItems().stream().forEach( ce -> actualPage3Strings.add(((EquipmentAdminStatusData) ce.getDetails()).getStatusMessage()) );
       
       assertEquals(expectedPage3Strings, actualPage3Strings);

//       System.out.println("================================");
//       for(Status pmdl: page3.getItems()){
//           System.out.println(pmdl);
//       }
//       System.out.println("================================");
//       System.out.println("Context: "+ page3.getContext());
//       System.out.println("================================");
       
       //test first page of the results with empty sort order -> default sort order (by Id ascending)
       PaginationResponse<Status> page1EmptySort = testInterface.getForCustomer(customerId_1, Collections.emptyList(), context);
       assertEquals(10, page1EmptySort.getItems().size());

       List<String> expectedPage1EmptySortStrings = new ArrayList<>(Arrays.asList(new String[]{"qr_0", "qr_1", "qr_2", "qr_3", "qr_4", "qr_5", "qr_6", "qr_7", "qr_8", "qr_9" }));
       List<String> actualPage1EmptySortStrings = new ArrayList<>();
       page1EmptySort.getItems().stream().forEach( ce -> actualPage1EmptySortStrings.add(((EquipmentAdminStatusData) ce.getDetails()).getStatusMessage()) );

       assertEquals(expectedPage1EmptySortStrings, actualPage1EmptySortStrings);

       //test first page of the results with null sort order -> default sort order (by Id ascending)
       PaginationResponse<Status> page1NullSort = testInterface.getForCustomer(customerId_1, null, context);
       assertEquals(10, page1NullSort.getItems().size());

       List<String> expectedPage1NullSortStrings = new ArrayList<>(Arrays.asList(new String[]{"qr_0", "qr_1", "qr_2", "qr_3", "qr_4", "qr_5", "qr_6", "qr_7", "qr_8", "qr_9" }));
       List<String> actualPage1NullSortStrings = new ArrayList<>();
       page1NullSort.getItems().stream().forEach( ce -> actualPage1NullSortStrings.add(((EquipmentAdminStatusData) ce.getDetails()).getStatusMessage()) );

       assertEquals(expectedPage1NullSortStrings, actualPage1NullSortStrings);

       
       //test first page of the results with sort descending order by a equipmentId property 
       PaginationResponse<Status> page1SingleSortDesc = testInterface.getForCustomer(customerId_1, Collections.singletonList(new ColumnAndSort("equipmentId", SortOrder.desc)), context);
       assertEquals(10, page1SingleSortDesc.getItems().size());

       List<String> expectedPage1SingleSortDescStrings = new ArrayList<	>(Arrays.asList(new String[]{"qr_49", "qr_48", "qr_47", "qr_46", "qr_45", "qr_44", "qr_43", "qr_42", "qr_41", "qr_40" }));
       List<String> actualPage1SingleSortDescStrings = new ArrayList<>();
       page1SingleSortDesc.getItems().stream().forEach( ce -> actualPage1SingleSortDescStrings.add(((EquipmentAdminStatusData) ce.getDetails()).getStatusMessage()) );
       
       assertEquals(expectedPage1SingleSortDescStrings, actualPage1SingleSortDescStrings);

       //delete
       allCreatedStatuses.forEach(s ->  testInterface.delete(s.getCustomerId(), s.getEquipmentId()));

    }
    
    @Test
    public void testStatusPaginationWithFilters()
    {
       //create 30 Statuses for our customer_1: 3 statuses per 10 equipment 
       Status mdl;
       int customerId_1 = (int) testSequence.incrementAndGet();
       int customerId_2 = (int) testSequence.incrementAndGet();
       
       long[] equipmentIds_1 = new long[10];
       for(int i=0; i<10; i++) {
    	   equipmentIds_1[i] =  testSequence.incrementAndGet(); 
       }

       StatusDataType statusDataType_1 = StatusDataType.EQUIPMENT_ADMIN;
       StatusDataType statusDataType_2 = StatusDataType.PROTOCOL;
       StatusDataType statusDataType_3 = StatusDataType.OS_PERFORMANCE;
       
       Set<Long> emptyEquipment = new HashSet<>();
       Set<Long> oneEquipment = new HashSet<>();
       oneEquipment.add(equipmentIds_1[0]);

       Set<Long> twoEquipment = new HashSet<>();
       twoEquipment.add(equipmentIds_1[0]);
       twoEquipment.add(equipmentIds_1[1]);

       Set<StatusDataType> emptyStatusDataTypes = new HashSet<>();
       Set<StatusDataType> oneStatusDataType = new HashSet<>();
       oneStatusDataType.add(statusDataType_1);
       
       Set<StatusDataType> twoStatusDataTypes = new HashSet<>();
       twoStatusDataTypes.add(statusDataType_1);
       twoStatusDataTypes.add(statusDataType_2);

       Set<StatusDataType> threeStatusDataTypes = new HashSet<>();
       threeStatusDataTypes.add(statusDataType_1);
       threeStatusDataTypes.add(statusDataType_2);
       threeStatusDataTypes.add(statusDataType_3);

       List<Status> allCreatedStatuses = new ArrayList<>(); 

       int apNameIdx = 0;
       
       for(int i = 0; i< equipmentIds_1.length; i++){
           //first status
           mdl = new Status();
           mdl.setCustomerId(customerId_1);
           mdl.setEquipmentId(equipmentIds_1[i]);
           
           allCreatedStatuses.add(mdl);
           
           EquipmentAdminStatusData details1 = new EquipmentAdminStatusData();
           details1.setStatusCode(StatusCode.normal);
           details1.setStatusMessage("qr_"+apNameIdx);
           mdl.setDetails(details1 );

           testInterface.update(mdl);

           //second status
           mdl = new Status();
           mdl.setCustomerId(customerId_1);
           mdl.setEquipmentId(equipmentIds_1[i]);
           
           EquipmentProtocolStatusData details2 = new EquipmentProtocolStatusData();
           details2.setSerialNumber("qr_"+apNameIdx);
           mdl.setDetails(details2);

           testInterface.update(mdl);

           //third status
           mdl = new Status();
           mdl.setCustomerId(customerId_1);
           mdl.setEquipmentId(equipmentIds_1[i]);
           
           OperatingSystemPerformance details3 = new OperatingSystemPerformance();
           details3.setAvgFreeMemoryKb(apNameIdx);
           mdl.setDetails(details3);

           testInterface.update(mdl);

           apNameIdx++;
       }

       //add some statuses for another customer into the mix
       for(int i = 0; i< 10; i++){
           mdl = createStatusObject();
           mdl.setCustomerId(customerId_2);
           ((EquipmentAdminStatusData) mdl.getDetails()).setStatusMessage("qr_"+apNameIdx);

           apNameIdx++;
           mdl = testInterface.update(mdl);
           allCreatedStatuses.add(mdl);
       }

       //
       //Now paginate over Statuses with various filters:
       //
       
       List<ColumnAndSort> sortBy = new ArrayList<>();
       sortBy.addAll(Arrays.asList(new ColumnAndSort("equipmentId")));
       
       //Paginate over all equipment and all statuses
       PaginationContext<Status> context = new PaginationContext<>(10);
       PaginationResponse<Status> page1 = testInterface.getForCustomer(customerId_1, emptyEquipment, emptyStatusDataTypes, sortBy, context);
       PaginationResponse<Status> page2 = testInterface.getForCustomer(customerId_1, emptyEquipment, emptyStatusDataTypes, sortBy, page1.getContext());
       PaginationResponse<Status> page3 = testInterface.getForCustomer(customerId_1, emptyEquipment, emptyStatusDataTypes, sortBy, page2.getContext());
       PaginationResponse<Status> page4 = testInterface.getForCustomer(customerId_1, emptyEquipment, emptyStatusDataTypes, sortBy, page3.getContext());
       
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
       page1 = testInterface.getForCustomer(customerId_1, null, null, sortBy, context);
       page2 = testInterface.getForCustomer(customerId_1, null, null, sortBy, page1.getContext());
       page3 = testInterface.getForCustomer(customerId_1, null, null, sortBy, page2.getContext());
       page4 = testInterface.getForCustomer(customerId_1, null, null, sortBy, page3.getContext());
       
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
       Set<StatusDataType> returnedStatusDataTypes = new HashSet<>();
       
       //Paginate over oneEquipment and all statuses
       context = new PaginationContext<>(10);
       returnedEquipmentIds.clear();
       returnedStatusDataTypes.clear();
       page1 = testInterface.getForCustomer(customerId_1, oneEquipment, emptyStatusDataTypes, sortBy, context);
       page2 = testInterface.getForCustomer(customerId_1, oneEquipment, emptyStatusDataTypes, sortBy, page1.getContext());
       
       //verify returned pages
       assertEquals(3, page1.getItems().size());
       assertEquals(0, page2.getItems().size());
       
		page1.getItems().forEach(e -> {
			assertEquals(customerId_1, e.getCustomerId());
			returnedEquipmentIds.add(e.getEquipmentId());
			returnedStatusDataTypes.add(e.getStatusDataType());
		});
		
		assertEquals(oneEquipment, returnedEquipmentIds);
		assertEquals(threeStatusDataTypes, returnedStatusDataTypes);
       
       assertTrue(page1.getContext().isLastPage());
       assertTrue(page2.getContext().isLastPage());
       
       //Paginate over twoEquipment and all statuses
       context = new PaginationContext<>(5);
       returnedEquipmentIds.clear();
       returnedStatusDataTypes.clear();
       page1 = testInterface.getForCustomer(customerId_1, twoEquipment, emptyStatusDataTypes, sortBy, context);
       page2 = testInterface.getForCustomer(customerId_1, twoEquipment, emptyStatusDataTypes, sortBy, page1.getContext());
       page3 = testInterface.getForCustomer(customerId_1, twoEquipment, emptyStatusDataTypes, sortBy, page2.getContext());
       
       //verify returned pages
       assertEquals(5, page1.getItems().size());
       assertEquals(1, page2.getItems().size());
       assertEquals(0, page3.getItems().size());
       
		page1.getItems().forEach(e -> {
			assertEquals(customerId_1, e.getCustomerId());
			returnedEquipmentIds.add(e.getEquipmentId());
			returnedStatusDataTypes.add(e.getStatusDataType());
		});

		page2.getItems().forEach(e -> {
			assertEquals(customerId_1, e.getCustomerId());
			returnedEquipmentIds.add(e.getEquipmentId());
			returnedStatusDataTypes.add(e.getStatusDataType());
		});

		assertEquals(twoEquipment, returnedEquipmentIds);
		assertEquals(threeStatusDataTypes, returnedStatusDataTypes);

       
       assertFalse(page1.getContext().isLastPage());
       assertTrue(page2.getContext().isLastPage());
       assertTrue(page3.getContext().isLastPage());
       
       //Paginate over oneEquipment and oneStatus
       context = new PaginationContext<>(10);
       returnedEquipmentIds.clear();
       returnedStatusDataTypes.clear();
       page1 = testInterface.getForCustomer(customerId_1, oneEquipment, oneStatusDataType, sortBy, context);
       page2 = testInterface.getForCustomer(customerId_1, oneEquipment, oneStatusDataType, sortBy, page1.getContext());
       
       //verify returned pages
       assertEquals(1, page1.getItems().size());
       assertEquals(0, page2.getItems().size());
       
		page1.getItems().forEach(e -> {
			assertEquals(customerId_1, e.getCustomerId());
			returnedEquipmentIds.add(e.getEquipmentId());
			returnedStatusDataTypes.add(e.getStatusDataType());
		});
		
		assertEquals(oneEquipment, returnedEquipmentIds);
		assertEquals(oneStatusDataType, returnedStatusDataTypes);

       assertTrue(page1.getContext().isLastPage());
       assertTrue(page2.getContext().isLastPage());
       
       //Paginate over oneEquipment and twoStatuses
       context = new PaginationContext<>(10);
       returnedEquipmentIds.clear();
       returnedStatusDataTypes.clear();
       page1 = testInterface.getForCustomer(customerId_1, oneEquipment, twoStatusDataTypes, sortBy, context);
       page2 = testInterface.getForCustomer(customerId_1, oneEquipment, twoStatusDataTypes, sortBy, page1.getContext());
       
       //verify returned pages
       assertEquals(2, page1.getItems().size());
       assertEquals(0, page2.getItems().size());
       
		page1.getItems().forEach(e -> {
			assertEquals(customerId_1, e.getCustomerId());
			returnedEquipmentIds.add(e.getEquipmentId());
			returnedStatusDataTypes.add(e.getStatusDataType());
		});
		
		assertEquals(oneEquipment, returnedEquipmentIds);
		assertEquals(twoStatusDataTypes, returnedStatusDataTypes);
       
       assertTrue(page1.getContext().isLastPage());
       assertTrue(page2.getContext().isLastPage());
       
       //Paginate over twoEquipment and twoStatuses
       context = new PaginationContext<>(10);
       returnedEquipmentIds.clear();
       returnedStatusDataTypes.clear();
       page1 = testInterface.getForCustomer(customerId_1, twoEquipment, twoStatusDataTypes, sortBy, context);
       page2 = testInterface.getForCustomer(customerId_1, twoEquipment, twoStatusDataTypes, sortBy, page1.getContext());
       
       //verify returned pages
       assertEquals(4, page1.getItems().size());
       assertEquals(0, page2.getItems().size());
       
		page1.getItems().forEach(e -> {
			assertEquals(customerId_1, e.getCustomerId());
			returnedEquipmentIds.add(e.getEquipmentId());
			returnedStatusDataTypes.add(e.getStatusDataType());
		});
		
		assertEquals(twoEquipment, returnedEquipmentIds);
		assertEquals(twoStatusDataTypes, returnedStatusDataTypes);
       
       assertTrue(page1.getContext().isLastPage());
       assertTrue(page2.getContext().isLastPage());

       //delete
       allCreatedStatuses.forEach(s ->  testInterface.delete(s.getCustomerId(), s.getEquipmentId()));

    }

    @Test
    public void testgetForEquipment()
    {
       //create 30 Statuses for our customer_1: 3 statuses per 10 equipment 
       Status mdl;
       int customerId_1 = (int) testSequence.incrementAndGet();
       int customerId_2 = (int) testSequence.incrementAndGet();
       
       long[] equipmentIds_1 = new long[10];
       for(int i=0; i<10; i++) {
    	   equipmentIds_1[i] =  testSequence.incrementAndGet(); 
       }

       StatusDataType statusDataType_1 = StatusDataType.EQUIPMENT_ADMIN;
       StatusDataType statusDataType_2 = StatusDataType.PROTOCOL;
       StatusDataType statusDataType_3 = StatusDataType.OS_PERFORMANCE;
       
       Set<Long> emptyEquipment = new HashSet<>();
       Set<Long> oneEquipment = new HashSet<>();
       oneEquipment.add(equipmentIds_1[0]);

       Set<Long> twoEquipment = new HashSet<>();
       twoEquipment.add(equipmentIds_1[0]);
       twoEquipment.add(equipmentIds_1[1]);

       Set<StatusDataType> emptyStatusDataTypes = new HashSet<>();
       Set<StatusDataType> oneStatusDataType = new HashSet<>();
       oneStatusDataType.add(statusDataType_1);
       
       Set<StatusDataType> twoStatusDataTypes = new HashSet<>();
       twoStatusDataTypes.add(statusDataType_1);
       twoStatusDataTypes.add(statusDataType_2);

       Set<StatusDataType> threeStatusDataTypes = new HashSet<>();
       threeStatusDataTypes.add(statusDataType_1);
       threeStatusDataTypes.add(statusDataType_2);
       threeStatusDataTypes.add(statusDataType_3);

       List<Status> allCreatedStatuses = new ArrayList<>(); 

       int apNameIdx = 0;
       
       for(int i = 0; i< equipmentIds_1.length; i++){
           //first status
           mdl = new Status();
           mdl.setCustomerId(customerId_1);
           mdl.setEquipmentId(equipmentIds_1[i]);
           
           allCreatedStatuses.add(mdl);
           
           EquipmentAdminStatusData details1 = new EquipmentAdminStatusData();
           details1.setStatusCode(StatusCode.normal);
           details1.setStatusMessage("qr_"+apNameIdx);
           mdl.setDetails(details1 );

           testInterface.update(mdl);

           //second status
           mdl = new Status();
           mdl.setCustomerId(customerId_1);
           mdl.setEquipmentId(equipmentIds_1[i]);
           
           EquipmentProtocolStatusData details2 = new EquipmentProtocolStatusData();
           details2.setSerialNumber("qr_"+apNameIdx);
           mdl.setDetails(details2);

           testInterface.update(mdl);

           //third status
           mdl = new Status();
           mdl.setCustomerId(customerId_1);
           mdl.setEquipmentId(equipmentIds_1[i]);
           
           OperatingSystemPerformance details3 = new OperatingSystemPerformance();
           details3.setAvgFreeMemoryKb(apNameIdx);
           mdl.setDetails(details3);

           testInterface.update(mdl);

           apNameIdx++;
       }

       //add some statuses for another customer into the mix
       for(int i = 0; i< 10; i++){
           mdl = createStatusObject();
           mdl.setCustomerId(customerId_2);
           ((EquipmentAdminStatusData) mdl.getDetails()).setStatusMessage("qr_"+apNameIdx);

           apNameIdx++;
           mdl = testInterface.update(mdl);
           allCreatedStatuses.add(mdl);

       }


       //
       // Now verify combinations of parameters for getForEquipment method 
       //
       
       List<Status> results;
       try {
    	   results = testInterface.getForEquipment(customerId_1, emptyEquipment, emptyStatusDataTypes);
    	   fail("equipmentIds mandatory check failed");
       }catch (IllegalArgumentException e) {
		// expected it
       }
       
       try {
    	   results = testInterface.getForEquipment(customerId_1, null, null);
		   fail("equipmentIds mandatory check failed");
	   }catch (IllegalArgumentException e) {
		// expected it
	   }
       
       Set<Long> returnedEquipmentIds = new HashSet<>();
       Set<StatusDataType> returnedStatusDataTypes = new HashSet<>();

       // verify one equipment, all data types
       returnedEquipmentIds.clear();
       returnedStatusDataTypes.clear();
       results = testInterface.getForEquipment(customerId_1, oneEquipment, emptyStatusDataTypes);
       assertEquals(3, results.size());
       results.forEach(e -> {
			assertEquals(customerId_1, e.getCustomerId());
			returnedEquipmentIds.add(e.getEquipmentId());
			returnedStatusDataTypes.add(e.getStatusDataType());
		});
		
		assertEquals(oneEquipment, returnedEquipmentIds);
		assertEquals(threeStatusDataTypes, returnedStatusDataTypes);

       
       // verify two equipment, all data types
       returnedEquipmentIds.clear();
       returnedStatusDataTypes.clear();
       results = testInterface.getForEquipment(customerId_1, twoEquipment, emptyStatusDataTypes);
       assertEquals(6, results.size());
       results.forEach(e -> {
			assertEquals(customerId_1, e.getCustomerId());
			returnedEquipmentIds.add(e.getEquipmentId());
			returnedStatusDataTypes.add(e.getStatusDataType());
		});
		
		assertEquals(twoEquipment, returnedEquipmentIds);
		assertEquals(threeStatusDataTypes, returnedStatusDataTypes);
       
       
       // verify one equipment, one data type
       returnedEquipmentIds.clear();
       returnedStatusDataTypes.clear();
       results = testInterface.getForEquipment(customerId_1, oneEquipment, oneStatusDataType);
       assertEquals(1, results.size());
       results.forEach(e -> {
			assertEquals(customerId_1, e.getCustomerId());
			returnedEquipmentIds.add(e.getEquipmentId());
			returnedStatusDataTypes.add(e.getStatusDataType());
		});
		
		assertEquals(oneEquipment, returnedEquipmentIds);
		assertEquals(oneStatusDataType, returnedStatusDataTypes);
       
       // verify one equipment, two data types
       returnedEquipmentIds.clear();
       returnedStatusDataTypes.clear();
       results = testInterface.getForEquipment(customerId_1, oneEquipment, twoStatusDataTypes);
       assertEquals(2, results.size());
       results.forEach(e -> {
			assertEquals(customerId_1, e.getCustomerId());
			returnedEquipmentIds.add(e.getEquipmentId());
			returnedStatusDataTypes.add(e.getStatusDataType());
		});
		
		assertEquals(oneEquipment, returnedEquipmentIds);
		assertEquals(twoStatusDataTypes, returnedStatusDataTypes);
       
       
       // verify two equipment, two data types
       returnedEquipmentIds.clear();
       returnedStatusDataTypes.clear();
       results = testInterface.getForEquipment(customerId_1, twoEquipment, twoStatusDataTypes);
       assertEquals(4, results.size());
       results.forEach(e -> {
			assertEquals(customerId_1, e.getCustomerId());
			returnedEquipmentIds.add(e.getEquipmentId());
			returnedStatusDataTypes.add(e.getStatusDataType());
		});
		
		assertEquals(twoEquipment, returnedEquipmentIds);
		assertEquals(twoStatusDataTypes, returnedStatusDataTypes);

       //delete
       allCreatedStatuses.forEach(s ->  testInterface.delete(s.getCustomerId(), s.getEquipmentId()));

	}
    
    protected Status createStatusObject() {
    	Status result = new Status();
        long nextId = testSequence.getAndIncrement();
        result.setCustomerId((int) nextId);
        result.setEquipmentId(testSequence.getAndIncrement()); 
        EquipmentAdminStatusData details = new EquipmentAdminStatusData();
        details.setStatusCode(StatusCode.normal);
		result.setDetails(details );
        return result;
    }
}
