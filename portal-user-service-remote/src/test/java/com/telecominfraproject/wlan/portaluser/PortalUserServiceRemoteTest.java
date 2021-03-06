package com.telecominfraproject.wlan.portaluser;


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

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pagination.SortOrder;
import com.telecominfraproject.wlan.core.model.role.PortalUserRole;
import com.telecominfraproject.wlan.remote.tests.BaseRemoteTest;

import com.telecominfraproject.wlan.portaluser.models.PortalUser;

/**
 * @author dtoptygin
 *
 */
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = {
        "integration_test",
        "no_ssl","http_digest_auth","rest-template-single-user-per-service-digest-auth",
        }) //NOTE: these profiles will be ADDED to the list of active profiles  
public class PortalUserServiceRemoteTest extends BaseRemoteTest {

    
    @Autowired PortalUserServiceRemote remoteInterface; 
    
    @Before public void urlSetup(){
        configureBaseUrl("tip.wlan.portalUserServiceBaseUrl");
    }
    
    
    @Test
    public void testPortalUserCRUD() throws Exception {
        
        //Create new PortalUser - success
        PortalUser portalUser = new PortalUser();
        portalUser.setUsername("test");
        portalUser.setCustomerId(getNextCustomerId());
        portalUser.setPassword("blah");
        portalUser.setRoles(Arrays.asList(PortalUserRole.TechSupport));

        PortalUser ret = remoteInterface.create(portalUser);
        assertNotNull(ret);        

        ret = remoteInterface.get(ret.getId());
        assertEqualPortalUsers(portalUser, ret);

        ret = remoteInterface.getOrNull(ret.getId());
        assertEqualPortalUsers(portalUser, ret);
        
        ret = remoteInterface.getByUsernameOrNull(portalUser.getCustomerId(), portalUser.getUsername());
        assertNotNull(ret);
        assertEqualPortalUsers(portalUser, ret);

        assertNull(remoteInterface.getOrNull(-1));
        assertNull(remoteInterface.getByUsernameOrNull(-1, "non-existent"));
        assertNull(remoteInterface.getByUsernameOrNull(portalUser.getCustomerId(), "non-existent"));


        //Update success
        ret.setUsername(ret.getUsername()+"_modified");
        //TODO: add more PortalUser fields to modify here
        
        PortalUser updatedPortalUser = remoteInterface.update(ret);
        assertEqualPortalUsers(ret, updatedPortalUser);

        //Update - failure because of concurrent modification
        try{
            remoteInterface.update(ret);
            fail("failed to detect concurrent modification");
        }catch(RuntimeException e){
            //expected it
        }

        //Update - failure because of non-existent record
        try{
            updatedPortalUser.setId(-1L);
            remoteInterface.update(updatedPortalUser);
            fail("updated non-existent PortalUser");
        }catch(RuntimeException e){
            //expected it
        }


        //Delete - failure because of non-existent record
        try{
            remoteInterface.delete(-1);
            fail("deleted non-existent PortalUser");
        }catch(RuntimeException e){
            //expected it
        }
        
        //Delete - success
        remoteInterface.delete(ret.getId());
        
        try{
            remoteInterface.get(ret.getId());
            fail("PortalUser was not deleted");
        }catch(RuntimeException e){
            //expected it
        }
        
    }
    
    
    @Test
    public void testGetAllInSet() {
        Set<PortalUser> createdSet = new HashSet<>();
        Set<PortalUser> createdTestSet = new HashSet<>();

        //Create test PortalUsers
        PortalUser portalUser = new PortalUser();

        int customerId = getNextCustomerId();
        
        for (int i = 0; i < 10; i++) {
            portalUser.setUsername("test_" + i);
            portalUser.setCustomerId(customerId);
            portalUser.setPassword("blah");
            portalUser.setRoles(Arrays.asList(PortalUserRole.TechSupport));

            PortalUser ret = remoteInterface.create(portalUser);

            // Only keep track of half of the created ones for testing
            if (i % 2 == 0) {
                createdTestSet.add(ret.clone());
            } else {
                createdSet.add(ret.clone());
            }
        }

        // Use only the IDs from the test set to retrieve records.
        Set<Long> testSetIds = new HashSet<>();
        for (PortalUser c : createdTestSet) {
            testSetIds.add(c.getId());
        }
        assertEquals(5, testSetIds.size());

        List<PortalUser> portalUsersRetrievedByIdSet = remoteInterface.get(testSetIds);
        assertEquals(5, portalUsersRetrievedByIdSet.size());
        for (PortalUser c : portalUsersRetrievedByIdSet) {
            assertTrue(createdTestSet.contains(c));
        }

        // Make sure the portalUsers from the non-test set are not in the list
        for (PortalUser c : portalUsersRetrievedByIdSet) {
            assertTrue(!createdSet.contains(c));
        }
		
        // Clean up after test
        for (PortalUser c : createdSet) {
        	remoteInterface.delete(c.getId());
        }
        for (PortalUser c : createdTestSet) {
        	remoteInterface.delete(c.getId());
        }

    }
    
    @Test
    public void testGetUsersForUsername() {
        String testUsername = "testSameUsername";

        //Create test PortalUsers
        PortalUser portalUser = new PortalUser();
        portalUser.setUsername(testUsername);

        for (int i = 0; i < 10; i++) {
        	if (i >= 5) {
        		portalUser.setUsername("badUsername");
        	}
            portalUser.setCustomerId(getNextCustomerId());
            portalUser.setPassword("blah");
            portalUser.setRoles(Arrays.asList(PortalUserRole.TechSupport));

            remoteInterface.create(portalUser);
            
        }
        
        List<PortalUser> listOfPortalUsers = remoteInterface.getUsersForUsername(testUsername);
        assertEquals(5, listOfPortalUsers.size());
        for (PortalUser pUser : listOfPortalUsers) {
        	assertEquals(testUsername, pUser.getUsername());
        }

    }

    @Test
    public void testPortalUserPagination()
    {
       //create 100 PortalUsers
       PortalUser mdl;
       int customerId_1 = getNextCustomerId();
       int customerId_2 = getNextCustomerId();
       
       int apNameIdx = 0;
       
       for(int i = 0; i< 50; i++){
           mdl = new PortalUser();
           mdl.setCustomerId(customerId_1);
           mdl.setUsername("qr_"+apNameIdx);
           mdl.setPassword("blah");
           mdl.setRoles(Arrays.asList(PortalUserRole.TechSupport));

           apNameIdx++;
           remoteInterface.create(mdl);
       }

       for(int i = 0; i< 50; i++){
           mdl = new PortalUser();
           mdl.setCustomerId(customerId_2);
           mdl.setUsername("qr_"+apNameIdx);
           mdl.setPassword("blah");
           mdl.setRoles(Arrays.asList(PortalUserRole.TechSupport));
           
           apNameIdx++;
           remoteInterface.create(mdl);
       }

       //paginate over PortalUsers
       
       List<ColumnAndSort> sortBy = new ArrayList<>();
       sortBy.addAll(Arrays.asList(new ColumnAndSort("username")));
       
       PaginationContext<PortalUser> context = new PaginationContext<>(10);
       PaginationResponse<PortalUser> page1 = remoteInterface.getForCustomer(customerId_1, sortBy, context);
       PaginationResponse<PortalUser> page2 = remoteInterface.getForCustomer(customerId_1, sortBy, page1.getContext());
       PaginationResponse<PortalUser> page3 = remoteInterface.getForCustomer(customerId_1, sortBy, page2.getContext());
       PaginationResponse<PortalUser> page4 = remoteInterface.getForCustomer(customerId_1, sortBy, page3.getContext());
       PaginationResponse<PortalUser> page5 = remoteInterface.getForCustomer(customerId_1, sortBy, page4.getContext());
       PaginationResponse<PortalUser> page6 = remoteInterface.getForCustomer(customerId_1, sortBy, page5.getContext());
       PaginationResponse<PortalUser> page7 = remoteInterface.getForCustomer(customerId_1, sortBy, page6.getContext());
       
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
       page3.getItems().stream().forEach( ce -> actualPage3Strings.add(ce.getUsername()) );
       
       assertEquals(expectedPage3Strings, actualPage3Strings);

//       System.out.println("================================");
//       for(PortalUser pmdl: page3.getItems()){
//           System.out.println(pmdl);
//       }
//       System.out.println("================================");
//       System.out.println("Context: "+ page3.getContext());
//       System.out.println("================================");
       
       //test first page of the results with empty sort order -> default sort order (by Id ascending)
       PaginationResponse<PortalUser> page1EmptySort = remoteInterface.getForCustomer(customerId_1, Collections.emptyList(), context);
       assertEquals(10, page1EmptySort.getItems().size());

       List<String> expectedPage1EmptySortStrings = new ArrayList<>(Arrays.asList(new String[]{"qr_0", "qr_1", "qr_2", "qr_3", "qr_4", "qr_5", "qr_6", "qr_7", "qr_8", "qr_9" }));
       List<String> actualPage1EmptySortStrings = new ArrayList<>();
       page1EmptySort.getItems().stream().forEach( ce -> actualPage1EmptySortStrings.add(ce.getUsername()) );

       assertEquals(expectedPage1EmptySortStrings, actualPage1EmptySortStrings);

       //test first page of the results with null sort order -> default sort order (by Id ascending)
       PaginationResponse<PortalUser> page1NullSort = remoteInterface.getForCustomer(customerId_1, null, context);
       assertEquals(10, page1NullSort.getItems().size());

       List<String> expectedPage1NullSortStrings = new ArrayList<>(Arrays.asList(new String[]{"qr_0", "qr_1", "qr_2", "qr_3", "qr_4", "qr_5", "qr_6", "qr_7", "qr_8", "qr_9" }));
       List<String> actualPage1NullSortStrings = new ArrayList<>();
       page1NullSort.getItems().stream().forEach( ce -> actualPage1NullSortStrings.add(ce.getUsername()) );

       assertEquals(expectedPage1NullSortStrings, actualPage1NullSortStrings);

       
       //test first page of the results with sort descending order by a username property 
       PaginationResponse<PortalUser> page1SingleSortDesc = remoteInterface.getForCustomer(customerId_1, Collections.singletonList(new ColumnAndSort("username", SortOrder.desc)), context);
       assertEquals(10, page1SingleSortDesc.getItems().size());

       List<String> expectedPage1SingleSortDescStrings = new ArrayList<	>(Arrays.asList(new String[]{"qr_9", "qr_8", "qr_7", "qr_6", "qr_5", "qr_49", "qr_48", "qr_47", "qr_46", "qr_45" }));
       List<String> actualPage1SingleSortDescStrings = new ArrayList<>();
       page1SingleSortDesc.getItems().stream().forEach( ce -> actualPage1SingleSortDescStrings.add(ce.getUsername()) );
       
       assertEquals(expectedPage1SingleSortDescStrings, actualPage1SingleSortDescStrings);

    }

    
    private void assertEqualPortalUsers(
            PortalUser expected,
            PortalUser actual) {
        
        assertEquals(expected.getUsername(), actual.getUsername());
        assertEquals(expected.getCustomerId(), actual.getCustomerId());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getRoles(), actual.getRoles());
        
        //TODO: add more fields to check here
    }

}
