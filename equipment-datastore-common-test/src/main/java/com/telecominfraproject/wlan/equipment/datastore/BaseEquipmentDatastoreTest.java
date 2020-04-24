package com.telecominfraproject.wlan.equipment.datastore;

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

import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pagination.SortOrder;
import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;

import com.telecominfraproject.wlan.equipment.models.Equipment;
import com.telecominfraproject.wlan.equipment.models.EquipmentDetails;

/**
 * @author dtoptygin
 *
 */
public abstract class BaseEquipmentDatastoreTest {
    @Autowired
    protected EquipmentDatastore testInterface;

    private static final AtomicLong testSequence = new AtomicLong(1);

    @Test
    public void testCRUD() {
    	Equipment equipment = createEquipmentObject();

        //create
    	Equipment created = testInterface.create(equipment);
        assertNotNull(created);
        assertTrue(created.getId() > 0);
        assertEquals(equipment.getName(), created.getName());
        assertEquals(equipment.getCustomerId(), created.getCustomerId());
        assertNotNull(created.getDetails());
        assertEquals(equipment.getDetails(), created.getDetails());
        
        assertEquals(equipment.getNetworkConfigId(), created.getNetworkConfigId());
        assertEquals(equipment.getLocationId(), created.getLocationId());
        assertEquals(equipment.getEquipmentType(), created.getEquipmentType());
        assertEquals(equipment.getInventoryId(), created.getInventoryId());
                
        // update
        created.setName(created.getName()+"_updated");
        Equipment updated = testInterface.update(created);
        assertNotNull(updated);
        assertEquals(created.getName(), updated.getName());
        
        //UPDATE test - fail because of concurrent modification exception
        try{
        	Equipment modelConcurrentUpdate = created.clone();
        	modelConcurrentUpdate.setName("not important");
        	testInterface.update(modelConcurrentUpdate);
        	fail("failed to detect concurrent modification");
        }catch (DsConcurrentModificationException e) {
        	// expected it
        }
        
        //retrieve
        Equipment retrieved = testInterface.get(created.getId());
        assertNotNull(retrieved);
        assertEquals(retrieved.getLastModifiedTimestamp(), updated.getLastModifiedTimestamp());

        retrieved = testInterface.getOrNull(created.getId());
        assertNotNull(retrieved);
        assertEquals(retrieved.getLastModifiedTimestamp(), updated.getLastModifiedTimestamp());
        
        //retrieve non-existent
        try {
        	testInterface.get(-1);
        	fail("retrieve non-existing record");
        }catch(DsEntityNotFoundException e ){
        	//expected it
        }
        
        assertNull(testInterface.getOrNull(-1));
        
        //delete
        retrieved = testInterface.delete(created.getId());
        assertNotNull(retrieved);
        
        //delete non-existent
        try {
        	testInterface.delete(-1);
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
        Set<Equipment> createdSet = new HashSet<>();
        Set<Equipment> createdTestSet = new HashSet<>();

        //Create test Equipments
        Equipment equipment = new Equipment();

        for (int i = 0; i < 10; i++) {
            equipment.setName("test_" + i);
            equipment.setCustomerId(i);
            equipment.setNetworkConfigId(1);
            equipment.setLocationId(2);
            equipment.setEquipmentType(EquipmentType.AP);
            equipment.setInventoryId("inv-" + equipment.getName());


            Equipment ret = testInterface.create(equipment);

            // Only keep track of half of the created ones for testing
            if (i % 2 == 0) {
                createdTestSet.add(ret.clone());
            } else {
                createdSet.add(ret.clone());
            }
        }

        // Use only the IDs from the test set to retrieve records.
        Set<Long> testSetIds = new HashSet<>();
        for (Equipment c : createdTestSet) {
            testSetIds.add(c.getId());
        }
        assertEquals(5, testSetIds.size());

        List<Equipment> equipmentsRetrievedByIdSet = testInterface.get(testSetIds);
        assertEquals(5, equipmentsRetrievedByIdSet.size());
        for (Equipment c : equipmentsRetrievedByIdSet) {
            assertTrue(createdTestSet.contains(c));
        }

        // Make sure the equipments from the non-test set are not in the list
        for (Equipment c : equipmentsRetrievedByIdSet) {
            assertTrue(!createdSet.contains(c));
        }
		
        // Clean up after test
        for (Equipment c : createdSet) {
        	testInterface.delete(c.getId());
        }
        for (Equipment c : createdTestSet) {
        	testInterface.delete(c.getId());
        }

    }
    
    @Test
    public void testEquipmentPagination()
    {
       //create 100 Equipments
       Equipment mdl;
       int customerId_1 = (int) testSequence.incrementAndGet();
       int customerId_2 = (int) testSequence.incrementAndGet();
       
       int apNameIdx = 0;
       
       for(int i = 0; i< 50; i++){
           mdl = new Equipment();
           mdl.setCustomerId(customerId_1);
           mdl.setName("qr_"+apNameIdx);
           mdl.setNetworkConfigId(1);
           mdl.setLocationId(2);
           mdl.setEquipmentType(EquipmentType.AP);
           mdl.setInventoryId("inv-testEquipmentPagination-" + mdl.getName());
           
           apNameIdx++;
           testInterface.create(mdl);
       }

       for(int i = 0; i< 50; i++){
           mdl = new Equipment();
           mdl.setCustomerId(customerId_2);
           mdl.setName("qr_"+apNameIdx);
           mdl.setNetworkConfigId(1);
           mdl.setLocationId(2);
           mdl.setEquipmentType(EquipmentType.AP);
           mdl.setInventoryId("inv-testEquipmentPagination-" + mdl.getName());
           apNameIdx++;
           testInterface.create(mdl);
       }

       //paginate over Equipments
       
       List<ColumnAndSort> sortBy = new ArrayList<>();
       sortBy.addAll(Arrays.asList(new ColumnAndSort("name")));
       
       PaginationContext<Equipment> context = new PaginationContext<>(10);
       PaginationResponse<Equipment> page1 = testInterface.getForCustomer(customerId_1, sortBy, context);
       PaginationResponse<Equipment> page2 = testInterface.getForCustomer(customerId_1, sortBy, page1.getContext());
       PaginationResponse<Equipment> page3 = testInterface.getForCustomer(customerId_1, sortBy, page2.getContext());
       PaginationResponse<Equipment> page4 = testInterface.getForCustomer(customerId_1, sortBy, page3.getContext());
       PaginationResponse<Equipment> page5 = testInterface.getForCustomer(customerId_1, sortBy, page4.getContext());
       PaginationResponse<Equipment> page6 = testInterface.getForCustomer(customerId_1, sortBy, page5.getContext());
       PaginationResponse<Equipment> page7 = testInterface.getForCustomer(customerId_1, sortBy, page6.getContext());
       
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
       
       List<String> expectedPage3Strings = new ArrayList<	>(Arrays.asList(new String[]{"qr_27", "qr_28", "qr_29", "qr_3", "qr_30", "qr_31", "qr_32", "qr_33", "qr_34", "qr_35" }));
       List<String> actualPage3Strings = new ArrayList<>();
       page3.getItems().stream().forEach( ce -> actualPage3Strings.add(ce.getName()) );
       
       assertEquals(expectedPage3Strings, actualPage3Strings);

//       System.out.println("================================");
//       for(Equipment pmdl: page3.getItems()){
//           System.out.println(pmdl);
//       }
//       System.out.println("================================");
//       System.out.println("Context: "+ page3.getContext());
//       System.out.println("================================");
       
       //test first page of the results with empty sort order -> default sort order (by Id ascending)
       PaginationResponse<Equipment> page1EmptySort = testInterface.getForCustomer(customerId_1, Collections.emptyList(), context);
       assertEquals(10, page1EmptySort.getItems().size());

       List<String> expectedPage1EmptySortStrings = new ArrayList<>(Arrays.asList(new String[]{"qr_0", "qr_1", "qr_2", "qr_3", "qr_4", "qr_5", "qr_6", "qr_7", "qr_8", "qr_9" }));
       List<String> actualPage1EmptySortStrings = new ArrayList<>();
       page1EmptySort.getItems().stream().forEach( ce -> actualPage1EmptySortStrings.add(ce.getName()) );

       assertEquals(expectedPage1EmptySortStrings, actualPage1EmptySortStrings);

       //test first page of the results with null sort order -> default sort order (by Id ascending)
       PaginationResponse<Equipment> page1NullSort = testInterface.getForCustomer(customerId_1, null, context);
       assertEquals(10, page1NullSort.getItems().size());

       List<String> expectedPage1NullSortStrings = new ArrayList<>(Arrays.asList(new String[]{"qr_0", "qr_1", "qr_2", "qr_3", "qr_4", "qr_5", "qr_6", "qr_7", "qr_8", "qr_9" }));
       List<String> actualPage1NullSortStrings = new ArrayList<>();
       page1NullSort.getItems().stream().forEach( ce -> actualPage1NullSortStrings.add(ce.getName()) );

       assertEquals(expectedPage1NullSortStrings, actualPage1NullSortStrings);

       
       //test first page of the results with sort descending order by a name property 
       PaginationResponse<Equipment> page1SingleSortDesc = testInterface.getForCustomer(customerId_1, Collections.singletonList(new ColumnAndSort("name", SortOrder.desc)), context);
       assertEquals(10, page1SingleSortDesc.getItems().size());

       List<String> expectedPage1SingleSortDescStrings = new ArrayList<	>(Arrays.asList(new String[]{"qr_9", "qr_8", "qr_7", "qr_6", "qr_5", "qr_49", "qr_48", "qr_47", "qr_46", "qr_45" }));
       List<String> actualPage1SingleSortDescStrings = new ArrayList<>();
       page1SingleSortDesc.getItems().stream().forEach( ce -> actualPage1SingleSortDescStrings.add(ce.getName()) );
       
       assertEquals(expectedPage1SingleSortDescStrings, actualPage1SingleSortDescStrings);

    }
    
    @Test
    public void testEquipmentPaginationWithFilters()
    {
       //create 150 Equipments
       Equipment mdl;
       int customerId_1 = (int) testSequence.incrementAndGet();
       int customerId_2 = (int) testSequence.incrementAndGet();
       long locationId_1 = testSequence.incrementAndGet();
       long locationId_2 = testSequence.incrementAndGet();
       long locationId_3 = testSequence.incrementAndGet();

       int apNameIdx = 0;
       
       for(int i = 0; i< 50; i++){
           mdl = new Equipment();
           mdl.setCustomerId(customerId_1);
           mdl.setName("qr_"+apNameIdx);
           mdl.setNetworkConfigId(1);
           mdl.setLocationId(locationId_1);
           mdl.setEquipmentType(EquipmentType.AP);
           mdl.setInventoryId("inv-" + mdl.getName());
           
           apNameIdx++;
           testInterface.create(mdl);
       }

       for(int i = 0; i< 50; i++){
           mdl = new Equipment();
           mdl.setCustomerId(customerId_1);
           mdl.setName("qr_"+apNameIdx);
           mdl.setNetworkConfigId(1);
           mdl.setLocationId(locationId_2);
           mdl.setEquipmentType(EquipmentType.AP);
           mdl.setInventoryId("inv-" + mdl.getName());
           
           apNameIdx++;
           testInterface.create(mdl);
       }


       for(int i = 0; i< 50; i++){
           mdl = new Equipment();
           mdl.setCustomerId(customerId_2);
           mdl.setName("qr_"+apNameIdx);
           mdl.setNetworkConfigId(1);
           mdl.setLocationId(locationId_3);
           mdl.setEquipmentType(EquipmentType.AP);
           mdl.setInventoryId("inv-" + mdl.getName());
           apNameIdx++;
           testInterface.create(mdl);
       }

       //paginate over Equipments
       
       Set<Long> locationIds = new HashSet<>();
       locationIds.add(locationId_1);

       List<ColumnAndSort> sortBy = new ArrayList<>();
       sortBy.addAll(Arrays.asList(new ColumnAndSort("name")));
       
       PaginationContext<Equipment> context = new PaginationContext<>(10);
       PaginationResponse<Equipment> page1 = testInterface.getForCustomer(customerId_1, EquipmentType.AP, locationIds, sortBy, context);
       PaginationResponse<Equipment> page2 = testInterface.getForCustomer(customerId_1, EquipmentType.AP, locationIds, sortBy, page1.getContext());
       PaginationResponse<Equipment> page3 = testInterface.getForCustomer(customerId_1, EquipmentType.AP, locationIds, sortBy, page2.getContext());
       PaginationResponse<Equipment> page4 = testInterface.getForCustomer(customerId_1, EquipmentType.AP, locationIds, sortBy, page3.getContext());
       PaginationResponse<Equipment> page5 = testInterface.getForCustomer(customerId_1, EquipmentType.AP, locationIds, sortBy, page4.getContext());
       PaginationResponse<Equipment> page6 = testInterface.getForCustomer(customerId_1, EquipmentType.AP, locationIds, sortBy, page5.getContext());
       PaginationResponse<Equipment> page7 = testInterface.getForCustomer(customerId_1, EquipmentType.AP, locationIds, sortBy, page6.getContext());
       
       //verify returned pages
       assertEquals(10, page1.getItems().size());
       assertEquals(10, page2.getItems().size());
       assertEquals(10, page3.getItems().size());
       assertEquals(10, page4.getItems().size());
       assertEquals(10, page5.getItems().size());
       
       page1.getItems().forEach(e -> {assertEquals(customerId_1, e.getCustomerId()); assertEquals(locationId_1, e.getLocationId()); } );
       page2.getItems().forEach(e -> {assertEquals(customerId_1, e.getCustomerId()); assertEquals(locationId_1, e.getLocationId()); } );
       page3.getItems().forEach(e -> {assertEquals(customerId_1, e.getCustomerId()); assertEquals(locationId_1, e.getLocationId()); } );
       page4.getItems().forEach(e -> {assertEquals(customerId_1, e.getCustomerId()); assertEquals(locationId_1, e.getLocationId()); } );
       page5.getItems().forEach(e -> {assertEquals(customerId_1, e.getCustomerId()); assertEquals(locationId_1, e.getLocationId()); } );
       
       assertEquals(0, page6.getItems().size());
       assertEquals(0, page7.getItems().size());
       
       assertFalse(page1.getContext().isLastPage());
       assertFalse(page2.getContext().isLastPage());
       assertFalse(page3.getContext().isLastPage());
       assertFalse(page4.getContext().isLastPage());
       assertFalse(page5.getContext().isLastPage());
       
       assertTrue(page6.getContext().isLastPage());
       assertTrue(page7.getContext().isLastPage());
       
       List<String> expectedPage3Strings = new ArrayList<	>(Arrays.asList(new String[]{"qr_27", "qr_28", "qr_29", "qr_3", "qr_30", "qr_31", "qr_32", "qr_33", "qr_34", "qr_35" }));
       List<String> actualPage3Strings = new ArrayList<>();
       page3.getItems().stream().forEach( ce -> actualPage3Strings.add(ce.getName()) );
       
       assertEquals(expectedPage3Strings, actualPage3Strings);

//       System.out.println("================================");
//       for(Equipment pmdl: page3.getItems()){
//           System.out.println(pmdl);
//       }
//       System.out.println("================================");
//       System.out.println("Context: "+ page3.getContext());
//       System.out.println("================================");
       
       //test first page of the results with empty sort order -> default sort order (by Id ascending)
       PaginationResponse<Equipment> page1EmptySort = testInterface.getForCustomer(customerId_1, EquipmentType.AP, locationIds, Collections.emptyList(), context);
       assertEquals(10, page1EmptySort.getItems().size());

       List<String> expectedPage1EmptySortStrings = new ArrayList<>(Arrays.asList(new String[]{"qr_0", "qr_1", "qr_2", "qr_3", "qr_4", "qr_5", "qr_6", "qr_7", "qr_8", "qr_9" }));
       List<String> actualPage1EmptySortStrings = new ArrayList<>();
       page1EmptySort.getItems().stream().forEach( ce -> actualPage1EmptySortStrings.add(ce.getName()) );

       assertEquals(expectedPage1EmptySortStrings, actualPage1EmptySortStrings);

       //test first page of the results with null sort order -> default sort order (by Id ascending)
       PaginationResponse<Equipment> page1NullSort = testInterface.getForCustomer(customerId_1, EquipmentType.AP, locationIds, null, context);
       assertEquals(10, page1NullSort.getItems().size());

       List<String> expectedPage1NullSortStrings = new ArrayList<>(Arrays.asList(new String[]{"qr_0", "qr_1", "qr_2", "qr_3", "qr_4", "qr_5", "qr_6", "qr_7", "qr_8", "qr_9" }));
       List<String> actualPage1NullSortStrings = new ArrayList<>();
       page1NullSort.getItems().stream().forEach( ce -> actualPage1NullSortStrings.add(ce.getName()) );

       assertEquals(expectedPage1NullSortStrings, actualPage1NullSortStrings);

       
       //test first page of the results with sort descending order by a name property 
       PaginationResponse<Equipment> page1SingleSortDesc = testInterface.getForCustomer(customerId_1, EquipmentType.AP, locationIds, Collections.singletonList(new ColumnAndSort("name", SortOrder.desc)), context);
       assertEquals(10, page1SingleSortDesc.getItems().size());

       List<String> expectedPage1SingleSortDescStrings = new ArrayList<	>(Arrays.asList(new String[]{"qr_9", "qr_8", "qr_7", "qr_6", "qr_5", "qr_49", "qr_48", "qr_47", "qr_46", "qr_45" }));
       List<String> actualPage1SingleSortDescStrings = new ArrayList<>();
       page1SingleSortDesc.getItems().stream().forEach( ce -> actualPage1SingleSortDescStrings.add(ce.getName()) );
       
       assertEquals(expectedPage1SingleSortDescStrings, actualPage1SingleSortDescStrings);

    }
    
    private Equipment createEquipmentObject() {
    	Equipment result = new Equipment();
        long nextId = testSequence.getAndIncrement();
        result.setCustomerId((int) nextId);
        result.setName("test-" + nextId); 
        result.setNetworkConfigId(1);
        result.setLocationId(2);
        result.setEquipmentType(EquipmentType.AP);
        result.setInventoryId("inv-" + result.getName());
        
        EquipmentDetails details = new EquipmentDetails();
        details.setSampleDetailsStr("test-details-" + nextId);
		result.setDetails(details );
        return result;
    }
}
