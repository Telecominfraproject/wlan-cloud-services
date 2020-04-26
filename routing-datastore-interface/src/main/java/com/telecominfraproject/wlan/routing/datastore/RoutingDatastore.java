package com.telecominfraproject.wlan.routing.datastore;

import java.util.List;
import java.util.Set;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;

import com.telecominfraproject.wlan.routing.models.Routing;

/**
 * @author dtoptygin
 *
 */
public interface RoutingDatastore {

    Routing create(Routing routing);
    Routing get(long routingId);
    Routing getOrNull(long routingId);
    Routing update(Routing routing);
    Routing delete(long routingId);
    
    /**
     * Retrieves a list of Routing records that which have their Id in the provided set.
     * 
     * @param routingIdSet
     * @return list of matching Routing objects.
     */
    List<Routing> get(Set<Long> routingIdSet);

    /**
     * <br>Retrieves all of the Routing records that are mapped to the provided customerId.
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
     * @return next page of matching Routing objects.
     */
    PaginationResponse<Routing> getForCustomer(int customerId, List<ColumnAndSort> sortBy, PaginationContext<Routing> context);

}
