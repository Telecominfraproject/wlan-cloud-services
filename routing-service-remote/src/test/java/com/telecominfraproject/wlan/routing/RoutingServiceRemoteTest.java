package com.telecominfraproject.wlan.routing;


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
import com.telecominfraproject.wlan.remote.tests.BaseRemoteTest;

import com.telecominfraproject.wlan.routing.models.Routing;

/**
 * @author dtoptygin
 *
 */
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = {
        "integration_test",
        "no_ssl","http_digest_auth","rest-template-single-user-per-service-digest-auth",
        }) //NOTE: these profiles will be ADDED to the list of active profiles  
public class RoutingServiceRemoteTest extends BaseRemoteTest {

    
    @Autowired RoutingServiceRemote remoteInterface; 
    
    @Before public void urlSetup(){
        configureBaseUrl("tip.wlan.routingServiceBaseUrl");
    }
    
    
    @Test
    public void testRoutingCRUD() throws Exception {
        
        //Create new Routing - success
        Routing routing = new Routing();
        routing.setSampleStr("test");

        Routing ret = remoteInterface.create(routing);
        assertNotNull(ret);
        

        ret = remoteInterface.get(ret.getId());
        assertEqualRoutings(routing, ret);

        ret = remoteInterface.getOrNull(ret.getId());
        assertEqualRoutings(routing, ret);
        
        assertNull(remoteInterface.getOrNull(-1));

        //Update success
        ret.setSampleStr(ret.getSampleStr()+"_modified");
        //TODO: add more Routing fields to modify here
        
        Routing updatedRouting = remoteInterface.update(ret);
        assertEqualRoutings(ret, updatedRouting);

        //Update - failure because of concurrent modification
        try{
            remoteInterface.update(ret);
            fail("failed to detect concurrent modification");
        }catch(RuntimeException e){
            //expected it
        }

        //Update - failure because of non-existent record
        try{
            updatedRouting.setId(-1L);
            remoteInterface.update(updatedRouting);
            fail("updated non-existent Routing");
        }catch(RuntimeException e){
            //expected it
        }


        //Delete - failure because of non-existent record
        try{
            remoteInterface.delete(-1);
            fail("deleted non-existent Routing");
        }catch(RuntimeException e){
            //expected it
        }
        
        //Delete - success
        remoteInterface.delete(ret.getId());
        
        try{
            remoteInterface.get(ret.getId());
            fail("Routing was not deleted");
        }catch(RuntimeException e){
            //expected it
        }
        
    }
    
    
    @Test
    public void testGetAllInSet() {
        Set<Routing> createdSet = new HashSet<>();
        Set<Routing> createdTestSet = new HashSet<>();

        //Create test Routings
        Routing routing = new Routing();

        int customerId = getNextCustomerId();
        
        for (int i = 0; i < 10; i++) {
            routing.setSampleStr("test_" + i);
            routing.setCustomerId(customerId);

            Routing ret = remoteInterface.create(routing);

            // Only keep track of half of the created ones for testing
            if (i % 2 == 0) {
                createdTestSet.add(ret.clone());
            } else {
                createdSet.add(ret.clone());
            }
        }

        // Use only the IDs from the test set to retrieve records.
        Set<Long> testSetIds = new HashSet<>();
        for (Routing c : createdTestSet) {
            testSetIds.add(c.getId());
        }
        assertEquals(5, testSetIds.size());

        List<Routing> routingsRetrievedByIdSet = remoteInterface.get(testSetIds);
        assertEquals(5, routingsRetrievedByIdSet.size());
        for (Routing c : routingsRetrievedByIdSet) {
            assertTrue(createdTestSet.contains(c));
        }

        // Make sure the routings from the non-test set are not in the list
        for (Routing c : routingsRetrievedByIdSet) {
            assertTrue(!createdSet.contains(c));
        }
		
        // Clean up after test
        for (Routing c : createdSet) {
        	remoteInterface.delete(c.getId());
        }
        for (Routing c : createdTestSet) {
        	remoteInterface.delete(c.getId());
        }

    }

    @Test
    public void testRoutingPagination()
    {
       //create 100 Routings
       Routing mdl;
       int customerId_1 = getNextCustomerId();
       int customerId_2 = getNextCustomerId();
       
       int apNameIdx = 0;
       
       for(int i = 0; i< 50; i++){
           mdl = new Routing();
           mdl.setCustomerId(customerId_1);
           mdl.setSampleStr("qr_"+apNameIdx);
           apNameIdx++;
           remoteInterface.create(mdl);
       }

       for(int i = 0; i< 50; i++){
           mdl = new Routing();
           mdl.setCustomerId(customerId_2);
           mdl.setSampleStr("qr_"+apNameIdx);
           apNameIdx++;
           remoteInterface.create(mdl);
       }

       //paginate over Routings
       
       List<ColumnAndSort> sortBy = new ArrayList<>();
       sortBy.addAll(Arrays.asList(new ColumnAndSort("sampleStr")));
       
       PaginationContext<Routing> context = new PaginationContext<>(10);
       PaginationResponse<Routing> page1 = remoteInterface.getForCustomer(customerId_1, sortBy, context);
       PaginationResponse<Routing> page2 = remoteInterface.getForCustomer(customerId_1, sortBy, page1.getContext());
       PaginationResponse<Routing> page3 = remoteInterface.getForCustomer(customerId_1, sortBy, page2.getContext());
       PaginationResponse<Routing> page4 = remoteInterface.getForCustomer(customerId_1, sortBy, page3.getContext());
       PaginationResponse<Routing> page5 = remoteInterface.getForCustomer(customerId_1, sortBy, page4.getContext());
       PaginationResponse<Routing> page6 = remoteInterface.getForCustomer(customerId_1, sortBy, page5.getContext());
       PaginationResponse<Routing> page7 = remoteInterface.getForCustomer(customerId_1, sortBy, page6.getContext());
       
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
       page3.getItems().stream().forEach( ce -> actualPage3Strings.add(ce.getSampleStr()) );
       
       assertEquals(expectedPage3Strings, actualPage3Strings);

//       System.out.println("================================");
//       for(Routing pmdl: page3.getItems()){
//           System.out.println(pmdl);
//       }
//       System.out.println("================================");
//       System.out.println("Context: "+ page3.getContext());
//       System.out.println("================================");
       
       //test first page of the results with empty sort order -> default sort order (by Id ascending)
       PaginationResponse<Routing> page1EmptySort = remoteInterface.getForCustomer(customerId_1, Collections.emptyList(), context);
       assertEquals(10, page1EmptySort.getItems().size());

       List<String> expectedPage1EmptySortStrings = new ArrayList<>(Arrays.asList(new String[]{"qr_0", "qr_1", "qr_2", "qr_3", "qr_4", "qr_5", "qr_6", "qr_7", "qr_8", "qr_9" }));
       List<String> actualPage1EmptySortStrings = new ArrayList<>();
       page1EmptySort.getItems().stream().forEach( ce -> actualPage1EmptySortStrings.add(ce.getSampleStr()) );

       assertEquals(expectedPage1EmptySortStrings, actualPage1EmptySortStrings);

       //test first page of the results with null sort order -> default sort order (by Id ascending)
       PaginationResponse<Routing> page1NullSort = remoteInterface.getForCustomer(customerId_1, null, context);
       assertEquals(10, page1NullSort.getItems().size());

       List<String> expectedPage1NullSortStrings = new ArrayList<>(Arrays.asList(new String[]{"qr_0", "qr_1", "qr_2", "qr_3", "qr_4", "qr_5", "qr_6", "qr_7", "qr_8", "qr_9" }));
       List<String> actualPage1NullSortStrings = new ArrayList<>();
       page1NullSort.getItems().stream().forEach( ce -> actualPage1NullSortStrings.add(ce.getSampleStr()) );

       assertEquals(expectedPage1NullSortStrings, actualPage1NullSortStrings);

       
       //test first page of the results with sort descending order by a sampleStr property 
       PaginationResponse<Routing> page1SingleSortDesc = remoteInterface.getForCustomer(customerId_1, Collections.singletonList(new ColumnAndSort("sampleStr", SortOrder.desc)), context);
       assertEquals(10, page1SingleSortDesc.getItems().size());

       List<String> expectedPage1SingleSortDescStrings = new ArrayList<	>(Arrays.asList(new String[]{"qr_9", "qr_8", "qr_7", "qr_6", "qr_5", "qr_49", "qr_48", "qr_47", "qr_46", "qr_45" }));
       List<String> actualPage1SingleSortDescStrings = new ArrayList<>();
       page1SingleSortDesc.getItems().stream().forEach( ce -> actualPage1SingleSortDescStrings.add(ce.getSampleStr()) );
       
       assertEquals(expectedPage1SingleSortDescStrings, actualPage1SingleSortDescStrings);

    }

    
    private void assertEqualRoutings(
            Routing expected,
            Routing actual) {
        
        assertEquals(expected.getSampleStr(), actual.getSampleStr());
        //TODO: add more fields to check here
    }

}
