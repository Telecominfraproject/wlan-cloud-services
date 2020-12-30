package com.telecominfraproject.wlan.customer.datastore.inmemory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pagination.SortOrder;
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
    
    @Override
    public PaginationResponse<Customer> searchAll(String criteria, String username,
    		final List<ColumnAndSort> sortBy, PaginationContext<Customer> context) {
    	
    	if (context == null) {
    		context = new PaginationContext<>();
    	}
    	
    	PaginationResponse<Customer> ret = new PaginationResponse<>();
    	ret.setContext(context.clone());
    	
    	if (ret.getContext().isLastPage()) {
    		// no more pages available according to the context
    		return ret;
    	}
    	
        List<Customer> items = new LinkedList<>();
        
        if (username == null) {
	        for (Customer mdl : idToCustomerMap.values()) {
	        	if (mdl.getName() != null && 
	        			mdl.getName().toLowerCase().contains(criteria.toLowerCase()) || 
	        			mdl.getEmail().toLowerCase().contains(criteria.toLowerCase())) {
	        		items.add(mdl);
	        	}
	        }
        } else {
        	for (Customer mdl : idToCustomerMap.values()) {
	        	if (mdl.getName() != null && 
	        			mdl.getName().toLowerCase().contains(criteria.toLowerCase()) && 
	        			mdl.getEmail().toLowerCase().equals(username)) {
	        		items.add(mdl);
	        	}
	        }
        }
        

        // apply sortBy columns
        Collections.sort(items, new Comparator<Customer>() {
            @Override
            public int compare(Customer o1, Customer o2) {
                if (sortBy == null || sortBy.isEmpty()) {
                    // sort ascending by id by default
                    return Long.compare(o1.getId(), o2.getId());
                } else {
                    int cmp;
                    for (ColumnAndSort column : sortBy) {
                        switch (column.getColumnName()) {
                        case "email":
                            cmp = o1.getEmail().compareTo(o2.getEmail());
                            break;
                        case "name":
                            cmp = o1.getName().compareTo(o2.getName());
                            break;
                        default:
                            // skip unknown column
                            continue;
                        }

                        if (cmp != 0) {
                            return (column.getSortOrder() == SortOrder.asc) ? cmp : (-cmp);
                        }

                    }
                }
                return 0;
            }
        });
        
        // now select only items for the requested page
        // find first item to add
        int fromIndex = 0;
        if (context.getStartAfterItem() != null) {
            for (Customer mdl : items) {
                fromIndex++;
                if (mdl.getId() == context.getStartAfterItem().getId()) {
                    break;
                }
            }
        }

        // find last item to add
        int toIndexExclusive = fromIndex + context.getMaxItemsPerPage();
        if (toIndexExclusive > items.size()) {
            toIndexExclusive = items.size();
        }

        // copy page items into result
        List<Customer> selectedItems = new ArrayList<>(context.getMaxItemsPerPage());
        for (Customer mdl : items.subList(fromIndex, toIndexExclusive)) {
            selectedItems.add(mdl.clone());
        }

        ret.setItems(selectedItems);

        // adjust context for the next page
        ret.prepareForNextPage();

        if(ret.getContext().getStartAfterItem()!=null) {
        	//this datastore is only interested in the last item's id, so we'll clear all other fields on the startAfterItem in the pagination context
        	Customer newStartAfterItem = new Customer();
        	newStartAfterItem.setId(ret.getContext().getStartAfterItem().getId());
        	ret.getContext().setStartAfterItem(newStartAfterItem);
        }

        return ret;
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

    /**
     * Provide access to internal idToCustomerMap so that other in-memory datastores can perform join operations efficiently.
     * One example of such a join operation can be retrieve all customers that have a portalUsername matching a criteria. 
     * 
     * @return internal map of id to Customer
     */
    public Map<Integer, Customer> getIdToCustomerMap() {
        return idToCustomerMap;
    }
    
}
