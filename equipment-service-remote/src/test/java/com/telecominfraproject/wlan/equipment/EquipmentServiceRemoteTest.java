package com.telecominfraproject.wlan.equipment;


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

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.Lists;
import com.telecominfraproject.wlan.core.model.equipment.DeploymentType;
import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pagination.SortOrder;
import com.telecominfraproject.wlan.core.model.pair.PairLongLong;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;
import com.telecominfraproject.wlan.equipment.models.AntennaType;
import com.telecominfraproject.wlan.equipment.models.ApElementConfiguration;
import com.telecominfraproject.wlan.equipment.models.ElementRadioConfiguration;
import com.telecominfraproject.wlan.equipment.models.Equipment;
import com.telecominfraproject.wlan.equipment.models.EquipmentDetails;
import com.telecominfraproject.wlan.equipment.models.bulkupdate.rrm.EquipmentRrmBulkUpdateItem;
import com.telecominfraproject.wlan.equipment.models.bulkupdate.rrm.EquipmentRrmBulkUpdateRequest;
import com.telecominfraproject.wlan.remote.tests.BaseRemoteTest;

/**
 * @author dtoptygin
 *
 */
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = {
        "integration_test",
        "no_ssl","http_digest_auth","rest-template-single-user-per-service-digest-auth",
        }) //NOTE: these profiles will be ADDED to the list of active profiles  
public class EquipmentServiceRemoteTest extends BaseRemoteTest {

    
    @Autowired EquipmentServiceRemote remoteInterface; 
    
    @Before public void urlSetup(){
        configureBaseUrl("tip.wlan.equipmentServiceBaseUrl");
    }
    
    
    @Test
    public void testEquipmentCRUD() throws Exception {
        
        //Create new Equipment - success
        Equipment equipment = new Equipment();
        equipment.setName("testName-"+getNextEquipmentId());
        equipment.setInventoryId("test-inv-"+getNextEquipmentId());
        equipment.setEquipmentType(EquipmentType.AP);

        Equipment ret = remoteInterface.create(equipment);
        assertNotNull(ret);
        

        ret = remoteInterface.get(ret.getId());
        assertEqualEquipments(equipment, ret);

        ret = remoteInterface.getOrNull(ret.getId());
        assertEqualEquipments(equipment, ret);

        ret = remoteInterface.getByInventoryIdOrNull(ret.getInventoryId());
        assertEqualEquipments(equipment, ret);
        
        assertNull(remoteInterface.getOrNull(-1));
        
        assertNull(remoteInterface.getByInventoryIdOrNull("inventoryId-non-existent"));

        //Update success
        ret.setName(ret.getName()+"_modified");
        //TODO: add more Equipment fields to modify here
        
        Equipment updatedEquipment = remoteInterface.update(ret);
        assertEqualEquipments(ret, updatedEquipment);

        //Update - failure because of concurrent modification
        try{
            remoteInterface.update(ret);
            fail("failed to detect concurrent modification");
        }catch(RuntimeException e){
            //expected it
        }

        //Update - failure because of non-existent record
        try{
            updatedEquipment.setId(-1L);
            remoteInterface.update(updatedEquipment);
            fail("updated non-existent Equipment");
        }catch(RuntimeException e){
            //expected it
        }


        //Delete - failure because of non-existent record
        try{
            remoteInterface.delete(-1);
            fail("deleted non-existent Equipment");
        }catch(RuntimeException e){
            //expected it
        }
        
        //Delete - success
        remoteInterface.delete(ret.getId());
        
        try{
            remoteInterface.get(ret.getId());
            fail("Equipment was not deleted");
        }catch(RuntimeException e){
            //expected it
        }
        
    }
    
    
    @Test
    public void testGetAllInSet() {
        Set<Equipment> createdSet = new HashSet<>();
        Set<Equipment> createdTestSet = new HashSet<>();

        //Create test Equipments
        Equipment equipment = new Equipment();

        int customerId = getNextCustomerId();
        
        for (int i = 0; i < 10; i++) {
            equipment.setName("test_" + i);
            equipment.setCustomerId(customerId);
            equipment.setEquipmentType(EquipmentType.AP);

            Equipment ret = remoteInterface.create(equipment);

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

        List<Equipment> equipmentsRetrievedByIdSet = remoteInterface.get(testSetIds);
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
        	remoteInterface.delete(c.getId());
        }
        for (Equipment c : createdTestSet) {
        	remoteInterface.delete(c.getId());
        }

    }

    @Test
    public void testEquipmentPagination()
    {
       //create 100 Equipments
       Equipment mdl;
       int customerId_1 = getNextCustomerId();
       int customerId_2 = getNextCustomerId();
       long locationId_1 = getNextLocationId();
       long locationId_3 = getNextLocationId();

       
       int apNameIdx = 0;
       
       for(int i = 0; i< 50; i++){
           mdl = new Equipment();
           mdl.setCustomerId(customerId_1);
           mdl.setName("qr_"+apNameIdx);
           mdl.setInventoryId("inv-testEquipmentPagination-" + mdl.getName());
           mdl.setLocationId(locationId_1);
           mdl.setEquipmentType(EquipmentType.AP);

           apNameIdx++;
           remoteInterface.create(mdl);
       }

       for(int i = 0; i< 50; i++){
           mdl = new Equipment();
           mdl.setCustomerId(customerId_2);
           mdl.setName("qr_"+apNameIdx);
           mdl.setInventoryId("inv-testEquipmentPagination-" + mdl.getName());
           mdl.setLocationId(locationId_3);
           mdl.setEquipmentType(EquipmentType.AP);
           
           apNameIdx++;
           remoteInterface.create(mdl);
       }

       //paginate over Equipments
       
       List<ColumnAndSort> sortBy = new ArrayList<>();
       sortBy.addAll(Arrays.asList(new ColumnAndSort("name")));
       
       PaginationContext<Equipment> context = new PaginationContext<>(10);
       PaginationResponse<Equipment> page1 = remoteInterface.getForCustomer(customerId_1, sortBy, context);
       PaginationResponse<Equipment> page2 = remoteInterface.getForCustomer(customerId_1, sortBy, page1.getContext());
       PaginationResponse<Equipment> page3 = remoteInterface.getForCustomer(customerId_1, sortBy, page2.getContext());
       PaginationResponse<Equipment> page4 = remoteInterface.getForCustomer(customerId_1, sortBy, page3.getContext());
       PaginationResponse<Equipment> page5 = remoteInterface.getForCustomer(customerId_1, sortBy, page4.getContext());
       PaginationResponse<Equipment> page6 = remoteInterface.getForCustomer(customerId_1, sortBy, page5.getContext());
       PaginationResponse<Equipment> page7 = remoteInterface.getForCustomer(customerId_1, sortBy, page6.getContext());
       
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
       PaginationResponse<Equipment> page1EmptySort = remoteInterface.getForCustomer(customerId_1, Collections.emptyList(), context);
       assertEquals(10, page1EmptySort.getItems().size());

       List<String> expectedPage1EmptySortStrings = new ArrayList<>(Arrays.asList(new String[]{"qr_0", "qr_1", "qr_2", "qr_3", "qr_4", "qr_5", "qr_6", "qr_7", "qr_8", "qr_9" }));
       List<String> actualPage1EmptySortStrings = new ArrayList<>();
       page1EmptySort.getItems().stream().forEach( ce -> actualPage1EmptySortStrings.add(ce.getName()) );

       assertEquals(expectedPage1EmptySortStrings, actualPage1EmptySortStrings);

       //test first page of the results with null sort order -> default sort order (by Id ascending)
       PaginationResponse<Equipment> page1NullSort = remoteInterface.getForCustomer(customerId_1, null, context);
       assertEquals(10, page1NullSort.getItems().size());

       List<String> expectedPage1NullSortStrings = new ArrayList<>(Arrays.asList(new String[]{"qr_0", "qr_1", "qr_2", "qr_3", "qr_4", "qr_5", "qr_6", "qr_7", "qr_8", "qr_9" }));
       List<String> actualPage1NullSortStrings = new ArrayList<>();
       page1NullSort.getItems().stream().forEach( ce -> actualPage1NullSortStrings.add(ce.getName()) );

       assertEquals(expectedPage1NullSortStrings, actualPage1NullSortStrings);

       
       //test first page of the results with sort descending order by a name property 
       PaginationResponse<Equipment> page1SingleSortDesc = remoteInterface.getForCustomer(customerId_1, Collections.singletonList(new ColumnAndSort("name", SortOrder.desc)), context);
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
       int customerId_1 = getNextCustomerId();
       int customerId_2 = getNextCustomerId();
       long locationId_1 = getNextLocationId();
       long locationId_2 = getNextLocationId();
       long locationId_3 = getNextLocationId();

       int apNameIdx = 0;
       
       for(int i = 0; i< 50; i++){
           mdl = new Equipment();
           mdl.setCustomerId(customerId_1);
           mdl.setName("qr_"+apNameIdx);
           mdl.setProfileId(1);
           mdl.setLocationId(locationId_1);
           mdl.setEquipmentType(EquipmentType.AP);
           mdl.setInventoryId("inv-" + mdl.getName());
           
           apNameIdx++;
           remoteInterface.create(mdl);
       }

       for(int i = 0; i< 50; i++){
           mdl = new Equipment();
           mdl.setCustomerId(customerId_1);
           mdl.setName("qr_"+apNameIdx);
           mdl.setProfileId(1);
           mdl.setLocationId(locationId_2);
           mdl.setEquipmentType(EquipmentType.AP);
           mdl.setInventoryId("inv-" + mdl.getName());
           
           apNameIdx++;
           remoteInterface.create(mdl);
       }


       for(int i = 0; i< 50; i++){
           mdl = new Equipment();
           mdl.setCustomerId(customerId_2);
           mdl.setName("qr_"+apNameIdx);
           mdl.setProfileId(1);
           mdl.setLocationId(locationId_3);
           mdl.setEquipmentType(EquipmentType.AP);
           mdl.setInventoryId("inv-" + mdl.getName());
           apNameIdx++;
           remoteInterface.create(mdl);
       }

       //paginate over Equipments
       
       Set<Long> locationIds = new HashSet<>();
       locationIds.add(locationId_1);

       List<ColumnAndSort> sortBy = new ArrayList<>();
       sortBy.addAll(Arrays.asList(new ColumnAndSort("name")));
       
       PaginationContext<Equipment> context = new PaginationContext<>(10);
       PaginationResponse<Equipment> page1 = remoteInterface.getForCustomer(customerId_1, EquipmentType.AP, locationIds, sortBy, context);
       PaginationResponse<Equipment> page2 = remoteInterface.getForCustomer(customerId_1, EquipmentType.AP, locationIds, sortBy, page1.getContext());
       PaginationResponse<Equipment> page3 = remoteInterface.getForCustomer(customerId_1, EquipmentType.AP, locationIds, sortBy, page2.getContext());
       PaginationResponse<Equipment> page4 = remoteInterface.getForCustomer(customerId_1, EquipmentType.AP, locationIds, sortBy, page3.getContext());
       PaginationResponse<Equipment> page5 = remoteInterface.getForCustomer(customerId_1, EquipmentType.AP, locationIds, sortBy, page4.getContext());
       PaginationResponse<Equipment> page6 = remoteInterface.getForCustomer(customerId_1, EquipmentType.AP, locationIds, sortBy, page5.getContext());
       PaginationResponse<Equipment> page7 = remoteInterface.getForCustomer(customerId_1, EquipmentType.AP, locationIds, sortBy, page6.getContext());
       
       //verify returned pages
       assertEquals(10, page1.getItems().size());
       assertEquals(10, page2.getItems().size());
       assertEquals(10, page3.getItems().size());
       assertEquals(10, page4.getItems().size());
       assertEquals(10, page5.getItems().size());
       
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

       
       //test first page of the results with empty sort order -> default sort order (by Id ascending)
       PaginationResponse<Equipment> page1EmptySort = remoteInterface.getForCustomer(customerId_1, EquipmentType.AP, locationIds, Collections.emptyList(), context);
       assertEquals(10, page1EmptySort.getItems().size());

       List<String> expectedPage1EmptySortStrings = new ArrayList<>(Arrays.asList(new String[]{"qr_0", "qr_1", "qr_2", "qr_3", "qr_4", "qr_5", "qr_6", "qr_7", "qr_8", "qr_9" }));
       List<String> actualPage1EmptySortStrings = new ArrayList<>();
       page1EmptySort.getItems().stream().forEach( ce -> actualPage1EmptySortStrings.add(ce.getName()) );

       assertEquals(expectedPage1EmptySortStrings, actualPage1EmptySortStrings);

       //test first page of the results with null sort order -> default sort order (by Id ascending)
       PaginationResponse<Equipment> page1NullSort = remoteInterface.getForCustomer(customerId_1, EquipmentType.AP, locationIds, null, context);
       assertEquals(10, page1NullSort.getItems().size());

       List<String> expectedPage1NullSortStrings = new ArrayList<>(Arrays.asList(new String[]{"qr_0", "qr_1", "qr_2", "qr_3", "qr_4", "qr_5", "qr_6", "qr_7", "qr_8", "qr_9" }));
       List<String> actualPage1NullSortStrings = new ArrayList<>();
       page1NullSort.getItems().stream().forEach( ce -> actualPage1NullSortStrings.add(ce.getName()) );

       assertEquals(expectedPage1NullSortStrings, actualPage1NullSortStrings);

       
       //test first page of the results with sort descending order by a name property 
       PaginationResponse<Equipment> page1SingleSortDesc = remoteInterface.getForCustomer(customerId_1, EquipmentType.AP, locationIds, Collections.singletonList(new ColumnAndSort("name", SortOrder.desc)), context);
       assertEquals(10, page1SingleSortDesc.getItems().size());

       List<String> expectedPage1SingleSortDescStrings = new ArrayList<	>(Arrays.asList(new String[]{"qr_9", "qr_8", "qr_7", "qr_6", "qr_5", "qr_49", "qr_48", "qr_47", "qr_46", "qr_45" }));
       List<String> actualPage1SingleSortDescStrings = new ArrayList<>();
       page1SingleSortDesc.getItems().stream().forEach( ce -> actualPage1SingleSortDescStrings.add(ce.getName()) );
       
       assertEquals(expectedPage1SingleSortDescStrings, actualPage1SingleSortDescStrings);


    }
    
    public void testDefaultDetails() {
    	EquipmentDetails details = remoteInterface.getDefaultEquipmentDetails(EquipmentType.AP);
    	
    	assertTrue(details instanceof ApElementConfiguration);
    	
    	ApElementConfiguration apElementConfiguration = (ApElementConfiguration) details;
    	
    	assertEquals(AntennaType.OMNI, apElementConfiguration.getAntennaType());
    	assertEquals(DeploymentType.CEILING, apElementConfiguration.getDeploymentType());
    	
    }
    
    @Test(expected = DsDataValidationException.class)
    public void testEquipmentUpdateChannelNumValidation() {

        //Create new Equipment - success
        Equipment equipment = new Equipment();
        equipment.setName("testName-"+getNextEquipmentId());
        equipment.setInventoryId("test-inv-"+getNextEquipmentId());
        equipment.setEquipmentType(EquipmentType.AP);
        equipment.setDetails(ApElementConfiguration.createWithDefaults());

        ElementRadioConfiguration element2dot4RadioConfig = ((ApElementConfiguration)equipment.getDetails()).getRadioMap().get(RadioType.is2dot4GHz);
        element2dot4RadioConfig.setAllowedChannels(Lists.newArrayList(1, 6, 11));

//        System.out.println("================================");
//        System.out.println(equipment);

        Equipment ret = remoteInterface.create(equipment);
        assertNotNull(ret);

        ret = remoteInterface.get(ret.getId());
        assertEqualEquipments(equipment, ret);

        ElementRadioConfiguration retElement2dot4RadioConfig = ((ApElementConfiguration)ret.getDetails()).getRadioMap().get(RadioType.is2dot4GHz);
        assertEquals(retElement2dot4RadioConfig.getChannelNumber().intValue(), 6);

        //Update success
        retElement2dot4RadioConfig.setChannelNumber(1);

        Equipment updatedEquipment = remoteInterface.update(ret);

        Equipment retUpdate = remoteInterface.get(ret.getId());
        assertEqualEquipments(retUpdate, updatedEquipment);
        ElementRadioConfiguration ret2Element2dot4RadioConfig = ((ApElementConfiguration)retUpdate.getDetails()).getRadioMap().get(RadioType.is2dot4GHz);
        assertEquals(retElement2dot4RadioConfig.getChannelNumber().intValue(), 1);

        //Update failure
        ret2Element2dot4RadioConfig.setChannelNumber(7);
        remoteInterface.update(retUpdate);
    }

    @Test
    public void testGetPaginatedEquipmentIds()
    {
       //create Equipment objects
       Equipment mdl;
       int customerId_1 = getNextCustomerId();
       int customerId_2 = getNextCustomerId();
       
       int apNameIdx = 0;
       
       for(int i = 0; i< 50; i++){
           mdl = new Equipment();
           mdl.setCustomerId(customerId_1);
           mdl.setName("qr_"+apNameIdx);
           mdl.setProfileId(i%5);
           mdl.setLocationId(100 + (i%5));
           mdl.setEquipmentType(EquipmentType.AP);
           mdl.setInventoryId("inv-testGetPaginatedEquipmentIds-" + mdl.getName());
           
           apNameIdx++;
           remoteInterface.create(mdl);
       }

       for(int i = 0; i< 10; i++){
           mdl = new Equipment();
           mdl.setCustomerId(customerId_2);
           mdl.setName("qr_"+apNameIdx);
           mdl.setProfileId(10000);
           mdl.setLocationId(20000);
           mdl.setEquipmentType(EquipmentType.AP);
           mdl.setInventoryId("inv-testGetPaginatedEquipmentIds-" + mdl.getName());
           apNameIdx++;
           remoteInterface.create(mdl);
       }

       //paginate over Equipment ids used by profileIds 1,2:
       
       PaginationContext<PairLongLong> context = new PaginationContext<>(5);
       Set<Long> profileIds = new HashSet<>(Arrays.asList(1L, 2L));
	   PaginationResponse<PairLongLong> page1 = remoteInterface.getEquipmentIdsByProfileIds(profileIds, context);
       PaginationResponse<PairLongLong> page2 = remoteInterface.getEquipmentIdsByProfileIds(profileIds, page1.getContext());
       PaginationResponse<PairLongLong> page3 = remoteInterface.getEquipmentIdsByProfileIds(profileIds, page2.getContext());
       PaginationResponse<PairLongLong> page4 = remoteInterface.getEquipmentIdsByProfileIds(profileIds, page3.getContext());
       PaginationResponse<PairLongLong> page5 = remoteInterface.getEquipmentIdsByProfileIds(profileIds, page4.getContext());
       
       assertEquals(5, page1.getItems().size());
       assertEquals(5, page2.getItems().size());
       assertEquals(5, page3.getItems().size());
       assertEquals(5, page4.getItems().size());
       assertEquals(0, page5.getItems().size());
       
       assertFalse(page1.getContext().isLastPage());
       assertFalse(page2.getContext().isLastPage());
       assertFalse(page3.getContext().isLastPage());
       assertFalse(page4.getContext().isLastPage());
       assertTrue(page5.getContext().isLastPage());
       
       page1.getItems().forEach(e -> assertEquals(1L, e.getValue1().longValue()) );
       page2.getItems().forEach(e -> assertEquals(1L, e.getValue1().longValue()) );
       page3.getItems().forEach(e -> assertEquals(2L, e.getValue1().longValue()) );
       page4.getItems().forEach(e -> assertEquals(2L, e.getValue1().longValue()) );

       //paginate over Equipment ids used by locationIds 101,102:
       
       context = new PaginationContext<>(5);
       Set<Long> locationIds = new HashSet<>(Arrays.asList(101L, 102L));
	   page1 = remoteInterface.getEquipmentIdsByLocationIds(locationIds, context);
       page2 = remoteInterface.getEquipmentIdsByLocationIds(locationIds, page1.getContext());
       page3 = remoteInterface.getEquipmentIdsByLocationIds(locationIds, page2.getContext());
       page4 = remoteInterface.getEquipmentIdsByLocationIds(locationIds, page3.getContext());
       page5 = remoteInterface.getEquipmentIdsByLocationIds(locationIds, page4.getContext());
       
       assertEquals(5, page1.getItems().size());
       assertEquals(5, page2.getItems().size());
       assertEquals(5, page3.getItems().size());
       assertEquals(5, page4.getItems().size());
       assertEquals(0, page5.getItems().size());
       
       assertFalse(page1.getContext().isLastPage());
       assertFalse(page2.getContext().isLastPage());
       assertFalse(page3.getContext().isLastPage());
       assertFalse(page4.getContext().isLastPage());
       assertTrue(page5.getContext().isLastPage());
       
       page1.getItems().forEach(e -> assertEquals(101L, e.getValue1().longValue()) );
       page2.getItems().forEach(e -> assertEquals(101L, e.getValue1().longValue()) );
       page3.getItems().forEach(e -> assertEquals(102L, e.getValue1().longValue()) );
       page4.getItems().forEach(e -> assertEquals(102L, e.getValue1().longValue()) );

    }
    
    @Test
    public void testRrmBulkUpdate() {
        Set<Long> createdSet = new HashSet<>();

        int customerId = getNextCustomerId();
        
        //Create test Equipment
        Equipment equipment = new Equipment();

        for (int i = 0; i < 10; i++) {
            equipment.setName("test_rrmBulkUpdate_" + i);
            equipment.setCustomerId(customerId);
            equipment.setProfileId(1);
            equipment.setLocationId(2);
            equipment.setEquipmentType(EquipmentType.AP);
            equipment.setInventoryId("inv-" + equipment.getName());

            equipment.setDetails(ApElementConfiguration.createWithDefaults());
            
            Equipment ret = remoteInterface.create(equipment);

            createdSet.add(ret.getId());
        }


        List<Equipment> equipmentList = remoteInterface.get(createdSet);
        assertEquals(10, equipmentList.size());

        //verify initial settings
        for (Equipment c : equipmentList) {
            assertEquals(50, ((ApElementConfiguration)c.getDetails()).getAdvancedRadioMap().get(RadioType.is2dot4GHz).getBestApSettings().getMinLoadFactor().intValue());
            assertEquals(6, ((ApElementConfiguration)c.getDetails()).getRadioMap().get(RadioType.is2dot4GHz).getChannelNumber().intValue());
        }

        //update all MinLoadFactor on 2.4Ghz radio to 42
        EquipmentRrmBulkUpdateRequest bulkRequest = new EquipmentRrmBulkUpdateRequest();
		equipmentList.forEach(eq -> {
			EquipmentRrmBulkUpdateItem bulkItem = new EquipmentRrmBulkUpdateItem(eq);
			bulkItem.setEquipmentId(eq.getId());
			bulkItem.getPerRadioDetails().get(RadioType.is2dot4GHz).setMinLoadFactor(42);
			bulkItem.getPerRadioDetails().get(RadioType.is2dot4GHz).setChannelNumber(11);
			bulkRequest.getItems().add(bulkItem);
		});
        
		remoteInterface.updateRrmBulk(bulkRequest);

		equipmentList = remoteInterface.get(createdSet);
        assertEquals(10, equipmentList.size());

        //verify updated settings
        for (Equipment c : equipmentList) {
            assertEquals(42, ((ApElementConfiguration)c.getDetails()).getAdvancedRadioMap().get(RadioType.is2dot4GHz).getBestApSettings().getMinLoadFactor().intValue());
            assertEquals(11, ((ApElementConfiguration)c.getDetails()).getRadioMap().get(RadioType.is2dot4GHz).getChannelNumber().intValue());            
        }

        // Clean up after test
        createdSet.forEach( c-> remoteInterface.delete(c));
        
    }
       
    private void assertEqualEquipments(
            Equipment expected,
            Equipment actual) {
        
        assertEquals(expected.getName(), actual.getName());
        //TODO: add more fields to check here
    }

}
