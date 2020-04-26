package com.telecominfraproject.wlan.client.datastore;

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
public interface ClientDatastore {

    Client create(Client client);
    Client get(long clientId);
    Client getOrNull(long clientId);
    Client update(Client client);
    Client delete(long clientId);
    
    /**
     * Retrieves a list of Client records that which have their Id in the provided set.
     * 
     * @param clientIdSet
     * @return list of matching Client objects.
     */
    List<Client> get(Set<Long> clientIdSet);

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
