package com.telecominfraproject.wlan.customer.datastore;

import java.util.List;
import java.util.Set;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pair.PairIntString;
import com.telecominfraproject.wlan.customer.models.Customer;

/**
 * @author dtoptygin
 *
 */
public interface CustomerDatastore  {

    /**
     * Create customer object
     * 
     * @param customer
     * @return newly created customer object
     */
    Customer create(Customer customer);

    /**
     * Retrieve customer by id
     * 
     * @param customerId
     * @return customer for the specified id, throws unchecked DsEntityNotFoundException if customer is not provisioned
     */
    Customer get(int customerId);

    /**
     * Retrieve customer by id
     * 
     * @param customerId
     * @return customer for the specified id or null if the customer is not provisioned
     */
    Customer getOrNull(int customerId);

    /**
     * Update customer object
     * 
     * @param customer
     * @return updated customer object
     */
    Customer update(Customer customer);

    /**
     * Delete customer object 
     * 
     * @param id
     * @return deleted customer object
     */
    Customer delete(int customerId);

    /**
     * Retrieves a list of {Id,  Name} for all provisioned customers, sorted by customer id.
     * 
     * @param batchSize - number of records to return in this call
     * @param continueAfterCustomerId - continue after this customer id
     * @return list of {Id,  Name} objects for all provisioned customers
     */
    List<PairIntString> getAll(int batchSize, int continueAfterCustomerId);

    /**
     * Retrieves a list of Customers from the datastore that have any text attribute containing the passed in string value.
     * 
     * @param value
     *            - String value to be found in matching customer records.
     * @param maxRecords - maximum records to return.          
     * @return list of matching Customer objects.
     */
    List<Customer> find(String value, int maxRecords);
    
    /**
     * <br>Retrieves all of the Customers that matches with username and criteria.
     * Results are returned in pages.
     * 
     * <br>When changing sort order or filters, pagination should be restarted again from the first page. 
     * Old pagination contexts would be invalid and should not be used in that case. 
     * <br>The only time when a caller should be interacting with the properties of the paginationContext is during the 
     * call to the first page by setting property maxItemsPerPage. 
     * <br>If initial context is not provided, then the maxItemsPerPage will be set to 20.
     * <br>If sortBy is not provided, then the data will be ordered by id.
     * * <ul>Allowed columns for sorting are: 
	 *<li>  "id"
	 *<li>  "email"
	 *<li>  "name"
	 *
     *<br> 
     * @param criteria
     * @param username
     * @return next page of matching Client objects.
     */
    public PaginationResponse<Customer> searchAll(String criteria, String username, List<ColumnAndSort> sortBy, PaginationContext<Customer> paginationContext);
    
    /**
     * Retrieves a list of Customer records that which have their Id in the provided set.
     * 
     * @param customerIdSet
     * @return list of matching Customer objects.
     */
    List<Customer> get(Set<Integer> customerIdSet);
        
}
