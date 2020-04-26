package com.telecominfraproject.wlan.client;

import java.util.List;
import java.util.Set;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;

import com.telecominfraproject.wlan.client.models.Client;


/**
 * @author dtoptygin
 *
 */
public interface ClientServiceInterface {
    
    /**
     * Creates new Client
     *  
     * @param Client
     * @return stored Client object
     * @throws RuntimeException if Client record already exists
     */
    Client create(Client client );
    
    /**
     * Retrieves Client by id
     * @param clientId
     * @return Client for the supplied id
     * @throws RuntimeException if Client record does not exist
     */
    Client get(long clientId );

    /**
     * Retrieves Client by id
     * @param clientId
     * @return Client for the supplied id
     */
    Client getOrNull(long clientId );

    /**
     * Retrieves a list of Client records that which have their Id in the provided set.
     * 
     * @param clientIdSet
     * @return list of matching Client objects.
     */
    List<Client> get(Set<Long> clientIdSet);

    /**
     * Updates Client 
     * 
     * @param Client
     * @return updated Client object
     * @throws RuntimeException if Client record does not exist or if it was modified concurrently
     */
    Client update(Client client);
    
    /**
     * Deletes Client
     * 
     * @param clientId
     * @return deleted Client object
     */
    Client delete(long clientId );
    
    /**
     * <br>Retrieves all of the Client records that are mapped to the provided customerId.
     * Results are returned in pages.
     * 
     * <br>When changing sort order or filters, pagination should be restarted again from the first page. 
     * Old pagination contexts would be invalid and should not be used in that case. 
     * <br>The only time when a caller should be interacting with the properties of the paginationContext is during the 
     * call to the first page by setting property maxItemsPerPage. 
     * <br>If initial context is not provided, then the maxItemsPerPage will be set to 20.
     * <br>If sortBy is not provided, then the data will be ordered by id.
     * <ul>Allowed columns for sorting are: 
	 *<li>  "id"
	 *<li> "sampleStr"
     *<br> 
     * @param customerId
     * @return next page of matching Client objects.
     */
    PaginationResponse<Client> getForCustomer(int customerId, List<ColumnAndSort> sortBy, PaginationContext<Client> context);

}
