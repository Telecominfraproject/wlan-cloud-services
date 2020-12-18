package com.telecominfraproject.wlan.customer.datastore;

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
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pagination.SortOrder;
import com.telecominfraproject.wlan.core.model.pair.PairIntString;
import com.telecominfraproject.wlan.customer.models.Customer;
import com.telecominfraproject.wlan.datastore.exceptions.DsDuplicateEntityException;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;

/**
 * @author dtop
 *
 */
public abstract class BaseCustomerDatastoreTest {
    @Autowired
    protected CustomerDatastore testInterface;

    private static final AtomicInteger customerSequence = new AtomicInteger(1);

    @Test
    public void testCRUD() {
        Customer customer = createCustomerObject();

        //create
        Customer created = testInterface.create(customer);
        assertNotNull(created);
        assertTrue(created.getId() > 0);
        assertEquals(customer.getName(), created.getName());
        assertEquals(customer.getEmail(), created.getEmail());
        
        try {
        	testInterface.create(customer);
        	fail("created duplicate customer");
        }catch(DsDuplicateEntityException e ){
        	//expected it
        }
        
        // update
        created.setName(created.getName()+"_updated");
        Customer updated = testInterface.update(created);
        assertNotNull(updated);
        assertEquals(created.getName(), updated.getName());
        
        //retrieve
        Customer retrieved = testInterface.get(created.getId());
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
        String testEmail = "thisemailisreallysilly";
        String companyName = "thisdoesnotmatter";
        Set<Customer> createdSet = new HashSet<>();
        Set<Customer> createdTestSet = new HashSet<>();

        //Create test customers
        Customer customer = new Customer();
        customer.setEmail(testEmail);
        customer.setName(companyName);

        for (int i = 0; i < 10; i++) {
            customer.setEmail(testEmail + "_" + i);
            customer.setName(companyName + "_" + i);

            Customer ret = testInterface.create(customer);

            // Only keep track of half of the created ones for testing
            if (i % 2 == 0) {
                createdTestSet.add(ret.clone());
            } else {
                createdSet.add(ret.clone());
            }
        }

        // Use only the IDs from the test set to retrieve records.
        Set<Integer> testSetIds = new HashSet<>();
        for (Customer c : createdTestSet) {
            testSetIds.add(c.getId());
        }
        assertEquals(5, testSetIds.size());

        List<Customer> customersRetrievedByIdSet = testInterface.get(testSetIds);
        assertEquals(5, customersRetrievedByIdSet.size());
        for (Customer c : customersRetrievedByIdSet) {
            assertTrue(createdTestSet.contains(c));
        }

        // Make sure the customers from the non-test set are not in the list
        for (Customer c : customersRetrievedByIdSet) {
            assertTrue(!createdSet.contains(c));
        }

        //retrieve all ids and names in batches
        int batchSize = 5;
		int continueAfterCustomerId = 0;
		List<PairIntString> batch = testInterface.getAll(batchSize , continueAfterCustomerId );
        Set<Integer> allIds = new HashSet<>();
		while(!batch.isEmpty()) {
			assertTrue(batch.size() <= batchSize);
			batch.forEach(p -> allIds.add(p.getIntVal()));
			batch = testInterface.getAll(batchSize , batch.get(batch.size()-1).getIntVal() );
		}

        for (Customer c : createdSet) {
            assertTrue(allIds.contains(c.getId()));
        }
        for (Customer c : createdTestSet) {
            assertTrue(allIds.contains(c.getId()));
        }

        
		
        // Clean up after test
        for (Customer c : createdSet) {
            testInterface.delete(c.getId());
        }
        for (Customer c : createdTestSet) {
            testInterface.delete(c.getId());
        }

    }
    
    @Test
    public void testCustomerPagination()
    {
       //create 100 Customers
       Customer mdl;
       int customerId = (int) customerSequence.incrementAndGet();
       
       int customerNameIdx = 0;
              
       for(int i = 0; i< 50; i++){
           mdl = new Customer();
           mdl.setId(customerId);
           mdl.setName("customer_one_" + customerNameIdx);
           mdl.setEmail("someEmail_" + customerNameIdx + "@example.com");

           customerNameIdx++;
           testInterface.create(mdl);
       }

       for(int i = 0; i< 50; i++){
    	   mdl = new Customer();
           mdl.setId(customerId);
           mdl.setName("customer_two_" + customerNameIdx);
           mdl.setEmail("someEmail_" + customerNameIdx + "@example.com");

           customerNameIdx++;
           testInterface.create(mdl);
       }

       //paginate over Customers
       
       List<ColumnAndSort> sortBy = new ArrayList<>();
       sortBy.addAll(Arrays.asList(new ColumnAndSort("name")));
       
       PaginationContext<Customer> context = new PaginationContext<>(10);
       PaginationResponse<Customer> page1 = testInterface.searchAll("one", null, sortBy, context);
       PaginationResponse<Customer> page2 = testInterface.searchAll("one", null, sortBy, page1.getContext());
       PaginationResponse<Customer> page3 = testInterface.searchAll("one", null, sortBy, page2.getContext());
       PaginationResponse<Customer> page4 = testInterface.searchAll("one", null, sortBy, page3.getContext());
       PaginationResponse<Customer> page5 = testInterface.searchAll("one", null, sortBy, page4.getContext());
       PaginationResponse<Customer> page6 = testInterface.searchAll("one", null, sortBy, page5.getContext());
       PaginationResponse<Customer> page7 = testInterface.searchAll("one", null, sortBy, page6.getContext());
       
       //verify returned pages
       assertEquals(10, page1.getItems().size());
       assertEquals(10, page2.getItems().size());
       assertEquals(10, page3.getItems().size());
       assertEquals(10, page4.getItems().size());
       assertEquals(10, page5.getItems().size());
       
       page1.getItems().forEach(e -> assertTrue(e.getName().contains("one")));
       page2.getItems().forEach(e -> assertTrue(e.getName().contains("one")));
       page3.getItems().forEach(e -> assertTrue(e.getName().contains("one")));
       page4.getItems().forEach(e -> assertTrue(e.getName().contains("one")));
       page5.getItems().forEach(e -> assertTrue(e.getName().contains("one")));
       
       assertEquals(0, page6.getItems().size());
       assertEquals(0, page7.getItems().size());
       
       assertFalse(page1.getContext().isLastPage());
       assertFalse(page2.getContext().isLastPage());
       assertFalse(page3.getContext().isLastPage());
       assertFalse(page4.getContext().isLastPage());
       assertFalse(page5.getContext().isLastPage());
       
       assertTrue(page6.getContext().isLastPage());
       assertTrue(page7.getContext().isLastPage());
       
       
       List<String> expectedPage3Strings = new ArrayList<>(Arrays.asList(
    		   new String[]{"customer_one_27", "customer_one_28", "customer_one_29", "customer_one_3", "customer_one_30", "customer_one_31", 
    				   "customer_one_32", "customer_one_33", "customer_one_34", "customer_one_35" }));
       List<String> actualPage3Strings = new ArrayList<>();
       page3.getItems().stream().forEach(customer -> actualPage3Strings.add(customer.getName()) );
       
       assertEquals(expectedPage3Strings, actualPage3Strings);
       

       //test first page of the results with empty sort order -> default sort order (by id ascending)
       PaginationResponse<Customer> page1EmptySort = testInterface.searchAll("one", null, Collections.emptyList(), context);
       assertEquals(10, page1EmptySort.getItems().size());

       List<String> expectedPage1EmptySortStrings = new ArrayList<>(
    		   Arrays.asList(new String[]{"customer_one_0", "customer_one_1", "customer_one_2", "customer_one_3", "customer_one_4", "customer_one_5", 
			   "customer_one_6", "customer_one_7", "customer_one_8", "customer_one_9" }));
       List<String> actualPage1EmptySortStrings = new ArrayList<>();
       page1EmptySort.getItems().stream().forEach(customer -> actualPage1EmptySortStrings.add(customer.getName()) );

       assertEquals(expectedPage1EmptySortStrings, actualPage1EmptySortStrings);

       //test first page of the results with null sort order -> default sort order (by id ascending)
       PaginationResponse<Customer> page1NullSort = testInterface.searchAll("one", null, null, context);
       assertEquals(10, page1NullSort.getItems().size());

       List<String> expectedPage1NullSortStrings = new ArrayList<>(
    		   Arrays.asList(new String[]{"customer_one_0", "customer_one_1", "customer_one_2", "customer_one_3", "customer_one_4", "customer_one_5", 
			   "customer_one_6", "customer_one_7", "customer_one_8", "customer_one_9" }));       
       List<String> actualPage1NullSortStrings = new ArrayList<>();
       page1NullSort.getItems().stream().forEach(customer -> actualPage1NullSortStrings.add(customer.getName()) );

       assertEquals(expectedPage1NullSortStrings, actualPage1NullSortStrings);

       
       //test first page of the results with sort descending order by a name property 
       PaginationResponse<Customer> page1SingleSortDesc = testInterface.searchAll("one", null, Collections.singletonList(new ColumnAndSort("name", SortOrder.desc)), context);
       assertEquals(10, page1SingleSortDesc.getItems().size());

       List<String> expectedPage1SingleSortDescStrings = new ArrayList<	>(
    		   Arrays.asList(new String[]{"customer_one_9", "customer_one_8", "customer_one_7", "customer_one_6", "customer_one_5", "customer_one_49", 
			   "customer_one_48", "customer_one_47", "customer_one_46", "customer_one_45" }));
       List<String> actualPage1SingleSortDescStrings = new ArrayList<>();
       page1SingleSortDesc.getItems().stream().forEach(customer -> actualPage1SingleSortDescStrings.add(customer.getName()) );
       
       assertEquals(expectedPage1SingleSortDescStrings, actualPage1SingleSortDescStrings);

    }

    
    private Customer createCustomerObject() {
        Customer result = new Customer();
        int custNo = customerSequence.getAndIncrement();
        result.setEmail("customer-" + custNo);
        result.setName("Test Compmany " + custNo);
        return result;
    }
}
