package com.telecominfraproject.wlan.customer.datastore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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
    public void testFind() {
        String testEmail = "makingatestemailissomuchfun@neverseenthisdomain.com";
        String testPassword = "whoopsie";  
        String companyName = "allofthewaysthiscompanyCanMakeYourWorldbetter";

        Set<Integer> createdSet = new HashSet<>();

        //Create test customers
        Customer customerRequest = new Customer();
        customerRequest.setEmail(testEmail);
        customerRequest.setName(companyName);

        final String emailPrefix = "testingprefix";

        for (int i = 0; i < 10; i++) {
            customerRequest.setEmail(emailPrefix + testEmail + "_" + i);
            customerRequest.setName(companyName + "_" + i);

            Customer ret = testInterface.create(customerRequest);

            // Keep track of created Customers for validation
            createdSet.add(ret.getId());
        }

        // Test getting all the created Customers
        List<Customer> customersRetrievedByEmailLike = testInterface.find(emailPrefix, 10);
        assertEquals(10, customersRetrievedByEmailLike.size());
        for (Customer c : customersRetrievedByEmailLike) {
            assertTrue(createdSet.contains(c.getId()));
        }

        // Test maxResults limitation
        customersRetrievedByEmailLike = testInterface.find(emailPrefix, 5);
        assertEquals(5, customersRetrievedByEmailLike.size());
        for (Customer c : customersRetrievedByEmailLike) {
            assertTrue(createdSet.contains(c.getId()));
        }

        // Test case insensitivity
        customersRetrievedByEmailLike = testInterface.find(emailPrefix.toUpperCase(), 10);
        assertEquals(10, customersRetrievedByEmailLike.size());
        for (Customer c : customersRetrievedByEmailLike) {
            assertTrue(createdSet.contains(c.getId()));
        }

        // Test using an non-matching criteria:
        customersRetrievedByEmailLike = testInterface.find("there is no way that any email is in there like this...right?", 10);
        assertEquals(0, customersRetrievedByEmailLike.size());

        // Test match on just 1 Customer
        Integer singleCustomerId = createdSet.iterator().next();
        Customer singleCustomer = testInterface.get(singleCustomerId);
        customersRetrievedByEmailLike = testInterface.find(singleCustomer.getEmail(), 10);
        assertEquals(1, customersRetrievedByEmailLike.size());
        assertEquals(singleCustomer, customersRetrievedByEmailLike.get(0));


        // Clean up after test
        for (Integer cid : createdSet) {
            testInterface.delete(cid);
        }
    }

    
    private Customer createCustomerObject() {
        Customer result = new Customer();
        int custNo = customerSequence.getAndIncrement();
        result.setEmail("customer-" + custNo);
        result.setName("Test Compmany " + custNo);
        return result;
    }
}
