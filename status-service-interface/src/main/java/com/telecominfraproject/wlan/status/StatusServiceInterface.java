package com.telecominfraproject.wlan.status;

import java.util.List;
import java.util.Set;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;

import com.telecominfraproject.wlan.status.models.Status;


/**
 * @author dtoptygin
 *
 */
public interface StatusServiceInterface {
    
    /**
     * Creates new Status
     *  
     * @param Status
     * @return stored Status object
     * @throws RuntimeException if Status record already exists
     */
    Status create(Status status );
    
    /**
     * Retrieves Status by id
     * @param statusId
     * @return Status for the supplied id
     * @throws RuntimeException if Status record does not exist
     */
    Status get(long statusId );

    /**
     * Retrieves Status by id
     * @param statusId
     * @return Status for the supplied id
     */
    Status getOrNull(long statusId );

    /**
     * Retrieves a list of Status records that which have their Id in the provided set.
     * 
     * @param statusIdSet
     * @return list of matching Status objects.
     */
    List<Status> get(Set<Long> statusIdSet);

    /**
     * Updates Status 
     * 
     * @param Status
     * @return updated Status object
     * @throws RuntimeException if Status record does not exist or if it was modified concurrently
     */
    Status update(Status status);
    
    /**
     * Deletes Status
     * 
     * @param statusId
     * @return deleted Status object
     */
    Status delete(long statusId );
    
    /**
     * <br>Retrieves all of the Status records that are mapped to the provided customerId.
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
     * @return next page of matching Status objects.
     */
    PaginationResponse<Status> getForCustomer(int customerId, List<ColumnAndSort> sortBy, PaginationContext<Status> context);

}
