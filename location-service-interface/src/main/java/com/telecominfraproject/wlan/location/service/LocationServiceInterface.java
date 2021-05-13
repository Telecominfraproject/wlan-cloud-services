package com.telecominfraproject.wlan.location.service;

import java.util.List;
import java.util.Set;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.location.models.Location;

public interface LocationServiceInterface {

    List<Location> getAllForCustomer(int customerId);
    Location get(long locationId);
    Location create(Location location);
    Location update(Location location);
    Location delete(long locationId);
    Location getOrNull(long locationId);

    /**
     * Recursively return all the children of the specified location id.
     * ie: Grandpa -> Homer -> Bart. If you specify Grandpa, you'll receive both Homer and Bart.
     *     (but you will NOT receive Grandpa)
     */
    List<Location> getAllDescendants(long locationParentId);


    /**
     * @return the top-level location for a specified location
     */
    Location getTopLevelLocation(long locationId);
    
    /**
     * @return all the ancestor location for a specified location
     */
    List<Location> getAllAncestors(long locationId);
    
    /**
     * Retrieves a list of Location records that which have their Id in the provided set.
     * 
     * @param locationIdSet
     * @return list of matching Location objects.
     */
    List<Location> get(Set<Long> locationIdSet);

    /**
     * <br>Retrieves all of the Location records that are mapped to the provided customerId.
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
     * @return next page of matching Location objects.
     */
    PaginationResponse<Location> getForCustomer(int customerId, List<ColumnAndSort> sortBy, PaginationContext<Location> context);
    
}
