package com.telecominfraproject.wlan.location.service;

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
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import com.telecominfraproject.wlan.core.model.entity.CountryCode;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pagination.SortOrder;
import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.location.models.Location;
import com.telecominfraproject.wlan.location.models.LocationDetails;
import com.telecominfraproject.wlan.location.models.LocationType;
import com.telecominfraproject.wlan.remote.tests.BaseRemoteTest;

//NOTE: these profiles will be ADDED to the list of active profiles  
@ActiveProfiles(profiles = {
      "integration_test",
      "no_ssl","http_digest_auth","rest-template-single-user-per-service-digest-auth",
      }) 
public class LocationServiceRemoteTest extends BaseRemoteTest {

    @Autowired LocationServiceInterface testInterface;

    @Before public void urlSetup(){
        configureBaseUrl("tip.wlan.locationServiceBaseUrl");
    }
    
    @Test
    public void testCreateUpdateDelete() {
        
        //CREATE test
        Location location = new Location();
        location.setLocationType(LocationType.SITE);
        location.setCustomerId(getNextCustomerId());
        location.setName("testName");
        location.setDetails(generateDetails(CountryCode.ca));
        
        Location ret = testInterface.create(location);
        //check that id got auto-assigned
        assertTrue(ret.getId()>0);
                
        //GET by Id test
        ret = testInterface.get(ret.getId());     
        assertFieldEquals(location, ret);

        //UPDATE test - success
        location = ret;
        location.setLocationType(LocationType.FLOOR);
        location.setCustomerId(getNextCustomerId());
        location.setName("testName_updated");
        location.setDetails(generateDetails(CountryCode.usa));

        Location retFromUpdate = testInterface.update(location);
        assertFieldEquals(location, retFromUpdate);
        
        //UPDATE test - fail because of concurrent modification exception
        try{
        	Location customerAddressConcurrentUpdate = ret.clone();
        	customerAddressConcurrentUpdate.setLocationType(LocationType.BUILDING);
        	testInterface.update(customerAddressConcurrentUpdate);
        	fail("failed to detect concurrent modification");
        }catch (DsConcurrentModificationException e) {
        	// expected it
        }

        
        //DELETE Test
        testInterface.delete(ret.getId());
        
        try{
        	testInterface.get(ret.getId());
            fail("failed to delete Location");
        }catch (Exception e) {
            // expected it
        }
                
    }
    
    @Test
    public void testGetAllForCustomer() {
        
    	int customerId_1 = getNextCustomerId();
    	int customerId_2 = getNextCustomerId();
    	
        //create 10 records for customer 2
        for(int i=0; i<10; i++){
            Location equipmentLocationRecord = new Location();
            equipmentLocationRecord.setLocationType(LocationType.SITE);
            equipmentLocationRecord.setCustomerId(customerId_1);
            equipmentLocationRecord.setName("testName_"+i);
            equipmentLocationRecord.setDetails(generateDetails(CountryCode.usa));
            
            Location ret = testInterface.create(equipmentLocationRecord);
            //check that id got auto-assigned
            assertTrue(ret.getId()>0);
        }
                
        //create 5 records for customer 3
        for(int i=0; i<5; i++){
            Location equipmentLocationRecord = new Location();
            equipmentLocationRecord.setLocationType(LocationType.SITE);
            equipmentLocationRecord.setCustomerId(customerId_2);
            equipmentLocationRecord.setName("testName_"+i);
            
            Location ret = testInterface.create(equipmentLocationRecord);
            //check that id got auto-assigned
            assertTrue(ret.getId()>0);
        }
        
        assertEquals(10, testInterface.getAllForCustomer(customerId_1).size());
        assertEquals(5, testInterface.getAllForCustomer(customerId_2).size());
                
    }

    @Test
    public void testChildrenCase()
    {
       /* Grand parent */
       Location grandpa = testInterface.create(generateLocation(0));

       Location homer = testInterface.create(generateLocation(grandpa.getId()));
       Location bart = testInterface.create(generateLocation(homer.getId()));
       Location lisa = testInterface.create(generateLocation(homer.getId()));

       Location bartJr = testInterface.create(generateLocation(bart.getId()));

       // Just to see if Flanders ends up in the mix
       @SuppressWarnings("unused")
       Location flanders = testInterface.create(generateLocation(0));

       List<Location> children = testInterface.getAllDescendants(grandpa.getId());

       assertNotNull(children);
       assertEquals(4, children.size());

       assertTrue(children.contains(homer));
       assertTrue(children.contains(bart));
       assertTrue(children.contains(bartJr));
       assertTrue(children.contains(lisa));
    }

    @Test
    public void testTopLevelQuery()
    {
          /* Grand parent */
          Location grandpa = testInterface.create(generateLocation(0));

          Location homer = testInterface.create(generateLocation(grandpa.getId()));
          Location bart = testInterface.create(generateLocation(homer.getId()));

          @SuppressWarnings("unused")
          Location lisa = testInterface.create(generateLocation(homer.getId()));
          @SuppressWarnings("unused")
          Location bartJr = testInterface.create(generateLocation(bart.getId()));

          // Just to see if Flanders ends up in the mix
          Location orphan = testInterface.create(generateLocation(999));

          Location topParent = testInterface.getTopLevelLocation(bart.getId());
          assertNotNull(topParent);
          assertFieldEquals(grandpa, topParent);
          
          /* We'll test the case also where there's no proper parent */
          Location orphanParent = testInterface.getTopLevelLocation(orphan.getId());
          assertNull(orphanParent);
          
    }

    @Test
    public void testGetAllInSet() {
        Set<Location> createdSet = new HashSet<>();
        Set<Location> createdTestSet = new HashSet<>();

        //Create test Locations
        Location location = new Location();

        int customerId = getNextCustomerId();
        
        for (int i = 0; i < 10; i++) {
            location.setName("test_" + i);
            location.setCustomerId(customerId);

            Location ret = testInterface.create(location);

            // Only keep track of half of the created ones for testing
            if (i % 2 == 0) {
                createdTestSet.add(ret.clone());
            } else {
                createdSet.add(ret.clone());
            }
        }

        // Use only the IDs from the test set to retrieve records.
        Set<Long> testSetIds = new HashSet<>();
        for (Location c : createdTestSet) {
            testSetIds.add(c.getId());
        }
        assertEquals(5, testSetIds.size());

        List<Location> locationsRetrievedByIdSet = testInterface.get(testSetIds);
        assertEquals(5, locationsRetrievedByIdSet.size());
        for (Location c : locationsRetrievedByIdSet) {
            assertTrue(createdTestSet.contains(c));
        }

        // Make sure the locations from the non-test set are not in the list
        for (Location c : locationsRetrievedByIdSet) {
            assertTrue(!createdSet.contains(c));
        }
		
        // Clean up after test
        for (Location c : createdSet) {
        	testInterface.delete(c.getId());
        }
        for (Location c : createdTestSet) {
        	testInterface.delete(c.getId());
        }

    }

    @Test
    public void testLocationPagination()
    {
       //create 100 Locations
       Location mdl;
       int customerId_1 = getNextCustomerId();
       int customerId_2 = getNextCustomerId();
       
       int apNameIdx = 0;
       
       for(int i = 0; i< 50; i++){
           mdl = new Location();
           mdl.setCustomerId(customerId_1);
           mdl.setName("qr_"+apNameIdx);
           mdl.setLocationType(LocationType.BUILDING);
           apNameIdx++;
           testInterface.create(mdl);
       }

       for(int i = 0; i< 50; i++){
           mdl = new Location();
           mdl.setCustomerId(customerId_2);
           mdl.setName("qr_"+apNameIdx);
           mdl.setLocationType(LocationType.BUILDING);           
           apNameIdx++;
           testInterface.create(mdl);
       }

       //paginate over Locations
       
       List<ColumnAndSort> sortBy = new ArrayList<>();
       sortBy.addAll(Arrays.asList(new ColumnAndSort("name")));
       
       PaginationContext<Location> context = new PaginationContext<>(10);
       PaginationResponse<Location> page1 = testInterface.getForCustomer(customerId_1, sortBy, context);
       PaginationResponse<Location> page2 = testInterface.getForCustomer(customerId_1, sortBy, page1.getContext());
       PaginationResponse<Location> page3 = testInterface.getForCustomer(customerId_1, sortBy, page2.getContext());
       PaginationResponse<Location> page4 = testInterface.getForCustomer(customerId_1, sortBy, page3.getContext());
       PaginationResponse<Location> page5 = testInterface.getForCustomer(customerId_1, sortBy, page4.getContext());
       PaginationResponse<Location> page6 = testInterface.getForCustomer(customerId_1, sortBy, page5.getContext());
       PaginationResponse<Location> page7 = testInterface.getForCustomer(customerId_1, sortBy, page6.getContext());
       
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
//       for(Location pmdl: page3.getItems()){
//           System.out.println(pmdl);
//       }
//       System.out.println("================================");
//       System.out.println("Context: "+ page3.getContext());
//       System.out.println("================================");
       
       //test first page of the results with empty sort order -> default sort order (by Id ascending)
       PaginationResponse<Location> page1EmptySort = testInterface.getForCustomer(customerId_1, Collections.emptyList(), context);
       assertEquals(10, page1EmptySort.getItems().size());

       List<String> expectedPage1EmptySortStrings = new ArrayList<>(Arrays.asList(new String[]{"qr_0", "qr_1", "qr_2", "qr_3", "qr_4", "qr_5", "qr_6", "qr_7", "qr_8", "qr_9" }));
       List<String> actualPage1EmptySortStrings = new ArrayList<>();
       page1EmptySort.getItems().stream().forEach( ce -> actualPage1EmptySortStrings.add(ce.getName()) );

       assertEquals(expectedPage1EmptySortStrings, actualPage1EmptySortStrings);

       //test first page of the results with null sort order -> default sort order (by Id ascending)
       PaginationResponse<Location> page1NullSort = testInterface.getForCustomer(customerId_1, null, context);
       assertEquals(10, page1NullSort.getItems().size());

       List<String> expectedPage1NullSortStrings = new ArrayList<>(Arrays.asList(new String[]{"qr_0", "qr_1", "qr_2", "qr_3", "qr_4", "qr_5", "qr_6", "qr_7", "qr_8", "qr_9" }));
       List<String> actualPage1NullSortStrings = new ArrayList<>();
       page1NullSort.getItems().stream().forEach( ce -> actualPage1NullSortStrings.add(ce.getName()) );

       assertEquals(expectedPage1NullSortStrings, actualPage1NullSortStrings);

       
       //test first page of the results with sort descending order by a name property 
       PaginationResponse<Location> page1SingleSortDesc = testInterface.getForCustomer(customerId_1, Collections.singletonList(new ColumnAndSort("name", SortOrder.desc)), context);
       assertEquals(10, page1SingleSortDesc.getItems().size());

       List<String> expectedPage1SingleSortDescStrings = new ArrayList<	>(Arrays.asList(new String[]{"qr_9", "qr_8", "qr_7", "qr_6", "qr_5", "qr_49", "qr_48", "qr_47", "qr_46", "qr_45" }));
       List<String> actualPage1SingleSortDescStrings = new ArrayList<>();
       page1SingleSortDesc.getItems().stream().forEach( ce -> actualPage1SingleSortDescStrings.add(ce.getName()) );
       
       assertEquals(expectedPage1SingleSortDescStrings, actualPage1SingleSortDescStrings);

    }

    private Location generateLocation(long parentId)
    {
       Location location = new Location();
       location.setParentId(parentId);
       location.setCustomerId(10);
       location.setLocationType(LocationType.BUILDING);
       location.setName(UUID.randomUUID().toString());
       location.setDetails(generateDetails(CountryCode.usa));

       return location;
    }

    private LocationDetails generateDetails(CountryCode code) {
       LocationDetails details = LocationDetails.createWithDefaults();
       details.setCountryCode(code);
       
       return details;
    }

    private void assertFieldEquals(Location expected, Location actual) {
        assertEquals(expected.getParentId(), actual.getParentId());
        assertEquals(expected.getCustomerId(), actual.getCustomerId());
        assertEquals(expected.getLocationType(), actual.getLocationType());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDetails(), actual.getDetails());

    }

}
