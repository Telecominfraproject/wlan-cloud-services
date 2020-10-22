package com.telecominfraproject.wlan.customer.datastore.inmemory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.core.model.pair.PairIntString;
import com.telecominfraproject.wlan.customer.datastore.CustomerDatastore;
import com.telecominfraproject.wlan.customer.models.Customer;
import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.datastore.exceptions.DsDuplicateEntityException;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;
import com.telecominfraproject.wlan.datastore.inmemory.BaseInMemoryDatastore;

/**
 * @author dtoptygin
 *
 */
@Component
public class CustomerDatastoreInMemory extends BaseInMemoryDatastore implements CustomerDatastore {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerDatastoreInMemory.class);

    private final Map<Integer, Customer> idToCustomerMap = new ConcurrentHashMap<>();
    private final static AtomicInteger customerId = new AtomicInteger(1);


    @Override
    public Customer create(Customer customer) {
        Customer customerCopy = customer.clone();

        idToCustomerMap.forEach(
        		(id, c) -> {
        			if(c.getEmail().equalsIgnoreCase(customerCopy.getEmail())) {
			        	throw new DsDuplicateEntityException("record already exists for " + customerCopy.getEmail());
			        } }
        		);
        

        int id = customerId.incrementAndGet();
        customerCopy.setId(id);
        if(customerCopy.getCreatedTimestamp()==0) {
        	customerCopy.setCreatedTimestamp(System.currentTimeMillis());
        }
        customerCopy.setLastModifiedTimestamp(getNewLastModTs(0));
        idToCustomerMap.put(id, customerCopy);
        
        LOG.debug("Stored {}", customerCopy);
        return customerCopy.clone();
    }

    @Override
    public Customer get(int id) {
        LOG.debug("Looking up customer {}", id);

        Customer customer = idToCustomerMap.get(id);
        if (customer == null) {
            LOG.debug("Cannot find customer for {}", id);
            throw new DsEntityNotFoundException("customer not found " + id);
        } else {
            LOG.debug("Found customer {}", customer);
        }

        return customer.clone();
    }

    @Override
    public Customer getOrNull(int id) {
        LOG.debug("Looking up customer for {}", id);

        Customer customer = idToCustomerMap.get(id);
        if (customer == null) {
            LOG.debug("Cannot find customer for {}", id);
            return null;
        } else {
            LOG.debug("Found customer {}", customer);
        }

        return customer.clone();
    }


    @Override
    public List<PairIntString> getAll(int batchSize, int continueAfterCustomerId) {
        List<Customer> allCustomers = new ArrayList<>(idToCustomerMap.values());
        Collections.sort(allCustomers, new Comparator<Customer>() {
            @Override
            public int compare(Customer o1, Customer o2) {
                return Integer.compare(o1.getId(), o2.getId());
            }
        });

        List<PairIntString> ret = new ArrayList<>();

        for (Customer c : allCustomers) {
            if (continueAfterCustomerId>0 && c.getId() <= continueAfterCustomerId) {
                // skip all until beginning of batch
                continue;
            }

            // batch starts here
            ret.add(new PairIntString(c.getId(), c.getName()));
            if (ret.size() >= batchSize) {
                // batch is full
                break;
            }
        }

        return ret;
    }

    @Override
    public Customer update(Customer customer) {
        Customer customerCopy = customer.clone();

        Customer existingCustomer = get(customerCopy.getId());

        if(existingCustomer.getLastModifiedTimestamp()!=customerCopy.getLastModifiedTimestamp()) {
            throw new DsConcurrentModificationException("Concurrent modification detected for customer with id "
                    + customer.getId() + " expected version is " + customerCopy.getLastModifiedTimestamp()
                    + " but version in db was " + existingCustomer.getLastModifiedTimestamp());
        }
        
        idToCustomerMap.forEach(
        		(id, c) -> {
        			if(id!= existingCustomer.getId() && c.getEmail().equalsIgnoreCase(customerCopy.getEmail())) {
			        	throw new DsDuplicateEntityException("record already exists for " + customerCopy.getEmail());
			        } }
        		);
        
        customerCopy.setCreatedTimestamp(existingCustomer.getCreatedTimestamp());
        customerCopy.setLastModifiedTimestamp(getNewLastModTs(existingCustomer.getLastModifiedTimestamp()));
        
        idToCustomerMap.put(existingCustomer.getId(), customerCopy);

        LOG.debug("Updated {}", customerCopy);

        return customerCopy.clone();
    }

    @Override
    public Customer delete(int id) {

        Customer customer = get(id);
        idToCustomerMap.remove(id);
        
        LOG.debug("Deleted {}", customer);

        return customer.clone();
    }

    @Override
    public List<Customer> find(String value, int maxRecords) {
        List<Customer> result = new ArrayList<>();

        for (Customer c : new ArrayList<>(idToCustomerMap.values())) {
            if ((c.getName()!=null && c.getName().toLowerCase().contains(value.toLowerCase()))
             || c.getEmail().toLowerCase().contains(value.toLowerCase())) {
                result.add(c);
            }
            
            if(result.size() == maxRecords) {
            	break;
            }
        }

        return result;
    }

    public void clearAll() {
        idToCustomerMap.clear();
        LOG.debug("Removed all customers");

    }

    @Override
    public List<Customer> get(Set<Integer> customerIdSet) {

    	List<Customer> ret = new ArrayList<>();
    	
        idToCustomerMap.forEach(
        		(id, c) -> {
        			if(customerIdSet.contains(id)) {
			        	ret.add(c.clone());
			        } }
        		);

        LOG.debug("Found customers by ids {}", ret);

        return ret;
    
    }
    
}
