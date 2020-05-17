package com.telecominfraproject.wlan.profile.datastore;

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
import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;

import com.telecominfraproject.wlan.profile.models.Profile;
import com.telecominfraproject.wlan.profile.models.ProfileDetails;
import com.telecominfraproject.wlan.profile.models.ProfileType;
import com.telecominfraproject.wlan.profile.ssid.models.SsidConfiguration;

/**
 * @author dtoptygin
 *
 */
public abstract class BaseProfileDatastoreTest {
    @Autowired
    protected ProfileDatastore testInterface;

    private static final AtomicLong testSequence = new AtomicLong(1);

    @Test
    public void testCRUD() {
    	int nextId = (int) testSequence.getAndIncrement();

    	Profile profile = createProfileObject(nextId);

        //create
    	Profile created = testInterface.create(profile);
        assertNotNull(created);
        assertTrue(created.getId() > 0);
        assertEquals(profile.getName(), created.getName());
        assertEquals(profile.getCustomerId(), created.getCustomerId());
        assertEquals(profile.getProfileType(), created.getProfileType());
        assertNotNull(created.getDetails());
        assertEquals(profile.getDetails(), created.getDetails());
                
        // update
        created.setName(created.getName()+"_updated");
        Profile updated = testInterface.update(created);
        assertNotNull(updated);
        assertEquals(created.getName(), updated.getName());
        
        while(updated.getLastModifiedTimestamp() == created.getLastModifiedTimestamp()) {
        	//update again to make the timestamps different      	
            created.setName(created.getName()+"_updated_1");
            updated = testInterface.update(created);
        }
        
        //UPDATE test - fail because of concurrent modification exception
        try{
        	Profile modelConcurrentUpdate = created.clone();
        	modelConcurrentUpdate.setName("not important");
        	testInterface.update(modelConcurrentUpdate);
        	fail("failed to detect concurrent modification");
        }catch (DsConcurrentModificationException e) {
        	// expected it
        }
        
        //retrieve
        Profile retrieved = testInterface.get(created.getId());
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
        Set<Profile> createdSet = new HashSet<>();
        Set<Profile> createdTestSet = new HashSet<>();

        //Create test Profiles
        Profile profile = new Profile();

        for (int i = 0; i < 10; i++) {
            profile.setName("test_" + i);
            profile.setCustomerId(i);
            profile.setProfileType(ProfileType.equipment_ap);

            Profile ret = testInterface.create(profile);

            // Only keep track of half of the created ones for testing
            if (i % 2 == 0) {
                createdTestSet.add(ret.clone());
            } else {
                createdSet.add(ret.clone());
            }
        }

        // Use only the IDs from the test set to retrieve records.
        Set<Long> testSetIds = new HashSet<>();
        for (Profile c : createdTestSet) {
            testSetIds.add(c.getId());
        }
        assertEquals(5, testSetIds.size());

        List<Profile> profilesRetrievedByIdSet = testInterface.get(testSetIds);
        assertEquals(5, profilesRetrievedByIdSet.size());
        for (Profile c : profilesRetrievedByIdSet) {
            assertTrue(createdTestSet.contains(c));
        }

        // Make sure the profiles from the non-test set are not in the list
        for (Profile c : profilesRetrievedByIdSet) {
            assertTrue(!createdSet.contains(c));
        }
		
        // Clean up after test
        for (Profile c : createdSet) {
        	testInterface.delete(c.getId());
        }
        for (Profile c : createdTestSet) {
        	testInterface.delete(c.getId());
        }

    }
    
    @Test
    public void testProfilePagination()
    {
       //create 100 Profiles
       Profile mdl;
       int customerId_1 = (int) testSequence.incrementAndGet();
       int customerId_2 = (int) testSequence.incrementAndGet();
       
       int apNameIdx = 0;
       
       for(int i = 0; i< 50; i++){
           mdl = new Profile();
           mdl.setCustomerId(customerId_1);
           mdl.setName("qr_"+apNameIdx);
           mdl.setProfileType(ProfileType.equipment_ap);

           apNameIdx++;
           testInterface.create(mdl);
       }

       for(int i = 0; i< 50; i++){
           mdl = new Profile();
           mdl.setCustomerId(customerId_2);
           mdl.setName("qr_"+apNameIdx);
           mdl.setProfileType(ProfileType.equipment_ap);

           apNameIdx++;
           testInterface.create(mdl);
       }

       //paginate over Profiles
       
       List<ColumnAndSort> sortBy = new ArrayList<>();
       sortBy.addAll(Arrays.asList(new ColumnAndSort("name")));
       
       PaginationContext<Profile> context = new PaginationContext<>(10);
       PaginationResponse<Profile> page1 = testInterface.getForCustomer(customerId_1, sortBy, context);
       PaginationResponse<Profile> page2 = testInterface.getForCustomer(customerId_1, sortBy, page1.getContext());
       PaginationResponse<Profile> page3 = testInterface.getForCustomer(customerId_1, sortBy, page2.getContext());
       PaginationResponse<Profile> page4 = testInterface.getForCustomer(customerId_1, sortBy, page3.getContext());
       PaginationResponse<Profile> page5 = testInterface.getForCustomer(customerId_1, sortBy, page4.getContext());
       PaginationResponse<Profile> page6 = testInterface.getForCustomer(customerId_1, sortBy, page5.getContext());
       PaginationResponse<Profile> page7 = testInterface.getForCustomer(customerId_1, sortBy, page6.getContext());
       
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
//       for(Profile pmdl: page3.getItems()){
//           System.out.println(pmdl);
//       }
//       System.out.println("================================");
//       System.out.println("Context: "+ page3.getContext());
//       System.out.println("================================");
       
       //test first page of the results with empty sort order -> default sort order (by Id ascending)
       PaginationResponse<Profile> page1EmptySort = testInterface.getForCustomer(customerId_1, Collections.emptyList(), context);
       assertEquals(10, page1EmptySort.getItems().size());

       List<String> expectedPage1EmptySortStrings = new ArrayList<>(Arrays.asList(new String[]{"qr_0", "qr_1", "qr_2", "qr_3", "qr_4", "qr_5", "qr_6", "qr_7", "qr_8", "qr_9" }));
       List<String> actualPage1EmptySortStrings = new ArrayList<>();
       page1EmptySort.getItems().stream().forEach( ce -> actualPage1EmptySortStrings.add(ce.getName()) );

       assertEquals(expectedPage1EmptySortStrings, actualPage1EmptySortStrings);

       //test first page of the results with null sort order -> default sort order (by Id ascending)
       PaginationResponse<Profile> page1NullSort = testInterface.getForCustomer(customerId_1, null, context);
       assertEquals(10, page1NullSort.getItems().size());

       List<String> expectedPage1NullSortStrings = new ArrayList<>(Arrays.asList(new String[]{"qr_0", "qr_1", "qr_2", "qr_3", "qr_4", "qr_5", "qr_6", "qr_7", "qr_8", "qr_9" }));
       List<String> actualPage1NullSortStrings = new ArrayList<>();
       page1NullSort.getItems().stream().forEach( ce -> actualPage1NullSortStrings.add(ce.getName()) );

       assertEquals(expectedPage1NullSortStrings, actualPage1NullSortStrings);

       
       //test first page of the results with sort descending order by a sampleStr property 
       PaginationResponse<Profile> page1SingleSortDesc = testInterface.getForCustomer(customerId_1, Collections.singletonList(new ColumnAndSort("name", SortOrder.desc)), context);
       assertEquals(10, page1SingleSortDesc.getItems().size());

       List<String> expectedPage1SingleSortDescStrings = new ArrayList<	>(Arrays.asList(new String[]{"qr_9", "qr_8", "qr_7", "qr_6", "qr_5", "qr_49", "qr_48", "qr_47", "qr_46", "qr_45" }));
       List<String> actualPage1SingleSortDescStrings = new ArrayList<>();
       page1SingleSortDesc.getItems().stream().forEach( ce -> actualPage1SingleSortDescStrings.add(ce.getName()) );
       
       assertEquals(expectedPage1SingleSortDescStrings, actualPage1SingleSortDescStrings);

    }
    
    @Test
    public void testChildProfiles() {
    	int nextId = (int) testSequence.getAndIncrement();

    	Profile profile_c1 = createProfileObject(nextId);
    	Profile profile_c2 = createProfileObject(nextId);

        //create with no children
    	profile_c1 = testInterface.create(profile_c1);
    	assertTrue(profile_c1.getChildProfileIds().isEmpty());

    	profile_c2 = testInterface.create(profile_c2);
    	
    	//create with 1 child
    	Profile profile_p1 = createProfileObject((int)nextId);
    	profile_p1.setChildProfileIds(new HashSet<>(Arrays.asList(profile_c1.getId())));

    	profile_p1 = testInterface.create(profile_p1);
    	assertEquals(1, profile_p1.getChildProfileIds().size());
    	assertEquals(new HashSet<>(Arrays.asList(profile_c1.getId())), profile_p1.getChildProfileIds());

    	//create with 2 children
    	Profile profile_p2 = createProfileObject((int)nextId);
    	profile_p2.setChildProfileIds(new HashSet<>(Arrays.asList(profile_c1.getId(), profile_c2.getId())));

    	profile_p2 = testInterface.create(profile_p2);
    	assertEquals(2, profile_p2.getChildProfileIds().size());
    	assertEquals(new HashSet<>(Arrays.asList(profile_c1.getId(), profile_c2.getId())), profile_p2.getChildProfileIds());

    	//update profile with 1 child to have 2 children
    	profile_p1.setChildProfileIds(new HashSet<>(Arrays.asList(profile_c1.getId(), profile_c2.getId())));
    	profile_p1 = testInterface.update(profile_p1);
    	assertEquals(2, profile_p1.getChildProfileIds().size());
    	assertEquals(new HashSet<>(Arrays.asList(profile_c1.getId(), profile_c2.getId())), profile_p1.getChildProfileIds());

    	//now update it back to have 1 child
    	profile_p1.setChildProfileIds(new HashSet<>(Arrays.asList(profile_c2.getId())));
    	profile_p1 = testInterface.update(profile_p1);
    	assertEquals(1, profile_p1.getChildProfileIds().size());
    	assertEquals(new HashSet<>(Arrays.asList(profile_c2.getId())), profile_p1.getChildProfileIds());

    	//now update it to have 0 children
    	profile_p1.getChildProfileIds().clear();
    	profile_p1 = testInterface.update(profile_p1);
    	assertEquals(0, profile_p1.getChildProfileIds().size());

    	//now delete the parent profiles and check that the former children are still there
    	testInterface.delete(profile_p1.getId());
    	testInterface.delete(profile_p2.getId());
    	
    	assertNotNull(testInterface.getOrNull(profile_c1.getId()));
    	assertNotNull(testInterface.getOrNull(profile_c2.getId()));
    	
    }
    
    @Test
    public void testGetProfileWithChildren(){
    	int nextId = (int) testSequence.getAndIncrement();

    	Profile profile_c1 = createProfileObject(nextId);
    	Profile profile_c2 = createProfileObject(nextId);

        //create with no children
    	profile_c1 = testInterface.create(profile_c1);
    	profile_c2 = testInterface.create(profile_c2);
    	
    	//create with 1 child
    	Profile profile_p1 = createProfileObject(nextId);
    	profile_p1.setChildProfileIds(new HashSet<>(Arrays.asList(profile_c1.getId())));
    	profile_p1 = testInterface.create(profile_p1);

    	//create with 2 children
    	Profile profile_p2 = createProfileObject(nextId);
    	profile_p2.setChildProfileIds(new HashSet<>(Arrays.asList(profile_c1.getId(), profile_c2.getId())));
    	profile_p2 = testInterface.create(profile_p2);
    	
    	//create with 2 children and 2 grand children
    	Profile profile_gp1 = createProfileObject(nextId);
    	profile_gp1.setChildProfileIds(new HashSet<>(Arrays.asList(profile_p1.getId(), profile_p2.getId())));
    	profile_gp1 = testInterface.create(profile_gp1);
    	
    	//test with no children
    	List<Profile> result = testInterface.getProfileWithChildren(profile_c1.getId());
    	assertEquals(1, result.size());
    	assertEquals(profile_c1, result.get(0));

    	//test with 1 child
    	result = testInterface.getProfileWithChildren(profile_p1.getId());
    	assertEquals(2, result.size());
    	assertEquals(new HashSet<>(Arrays.asList(profile_p1, profile_c1)), new HashSet<>(result) );

    	//test with 2 children
    	result = testInterface.getProfileWithChildren(profile_p2.getId());
    	assertEquals(3, result.size());
    	assertEquals(new HashSet<>(Arrays.asList(profile_p2, profile_c1, profile_c2)), new HashSet<>(result) );

    	//test with 2 children and 2 grand children
    	result = testInterface.getProfileWithChildren(profile_gp1.getId());
    	assertEquals(5, result.size());
    	assertEquals(new HashSet<>(Arrays.asList(profile_gp1, profile_p1, profile_p2, profile_c1, profile_c2)), new HashSet<>(result) );

    }
    
    private Profile createProfileObject(int customerId) {
    	Profile result = new Profile();        
        result.setCustomerId(customerId);
        result.setName("test-" + customerId); 
        SsidConfiguration details = SsidConfiguration.createWithDefaults();
        details.setSsid("test-details-" + customerId);
		result.setDetails(details );
        return result;
    }
}
