package com.telecominfraproject.wlan.portaluser;

import java.util.List;
import java.util.Set;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;

import com.telecominfraproject.wlan.portaluser.models.PortalUser;


/**
 * @author dtoptygin
 *
 */
public interface PortalUserServiceInterface {
    
    /**
     * Creates new PortalUser
     *  
     * @param PortalUser
     * @return stored PortalUser object
     * @throws RuntimeException if PortalUser record already exists
     */
    PortalUser create(PortalUser portalUser );
    
    /**
     * Retrieves PortalUser by id
     * @param portalUserId
     * @return PortalUser for the supplied id
     * @throws RuntimeException if PortalUser record does not exist
     */
    PortalUser get(long portalUserId );

    /**
     * Retrieves PortalUser by id
     * @param portalUserId
     * @return PortalUser for the supplied id
     */
    PortalUser getOrNull(long portalUserId );

    /**
     * Retrieves a list of PortalUser records that which have their Id in the provided set.
     * 
     * @param portalUserIdSet
     * @return list of matching PortalUser objects.
     */
    List<PortalUser> get(Set<Long> portalUserIdSet);

    /**
     * Updates PortalUser 
     * 
     * @param PortalUser
     * @return updated PortalUser object
     * @throws RuntimeException if PortalUser record does not exist or if it was modified concurrently
     */
    PortalUser update(PortalUser portalUser);
    
    /**
     * Deletes PortalUser
     * 
     * @param portalUserId
     * @return deleted PortalUser object
     */
    PortalUser delete(long portalUserId );
    
    /**
     * <br>Retrieves all of the PortalUser records that are mapped to the provided customerId.
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
     * @return next page of matching PortalUser objects.
     */
    PaginationResponse<PortalUser> getForCustomer(int customerId, List<ColumnAndSort> sortBy, PaginationContext<PortalUser> context);

}
