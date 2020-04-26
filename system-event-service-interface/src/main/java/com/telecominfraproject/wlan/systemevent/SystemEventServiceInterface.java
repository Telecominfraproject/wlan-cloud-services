package com.telecominfraproject.wlan.systemevent;

import java.util.List;
import java.util.Set;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;

import com.telecominfraproject.wlan.systemevent.models.SystemEventContainer;


/**
 * @author dtoptygin
 *
 */
public interface SystemEventServiceInterface {
    
    /**
     * Creates new SystemEventContainer
     *  
     * @param SystemEventContainer
     * @return stored SystemEventContainer object
     * @throws RuntimeException if SystemEventContainer record already exists
     */
    SystemEventContainer create(SystemEventContainer systemEventRecord );
    
    /**
     * Retrieves SystemEventContainer by id
     * @param systemEventRecordId
     * @return SystemEventContainer for the supplied id
     * @throws RuntimeException if SystemEventContainer record does not exist
     */
    SystemEventContainer get(long systemEventRecordId );

    /**
     * Retrieves SystemEventContainer by id
     * @param systemEventRecordId
     * @return SystemEventContainer for the supplied id
     */
    SystemEventContainer getOrNull(long systemEventRecordId );

    /**
     * Retrieves a list of SystemEventContainer records that which have their Id in the provided set.
     * 
     * @param systemEventRecordIdSet
     * @return list of matching SystemEventContainer objects.
     */
    List<SystemEventContainer> get(Set<Long> systemEventRecordIdSet);

    /**
     * Updates SystemEventContainer 
     * 
     * @param SystemEventContainer
     * @return updated SystemEventContainer object
     * @throws RuntimeException if SystemEventContainer record does not exist or if it was modified concurrently
     */
    SystemEventContainer update(SystemEventContainer systemEventRecord);
    
    /**
     * Deletes SystemEventContainer
     * 
     * @param systemEventRecordId
     * @return deleted SystemEventContainer object
     */
    SystemEventContainer delete(long systemEventRecordId );
    
    /**
     * <br>Retrieves all of the SystemEventContainer records that are mapped to the provided customerId.
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
     * @return next page of matching SystemEventContainer objects.
     */
    PaginationResponse<SystemEventContainer> getForCustomer(int customerId, List<ColumnAndSort> sortBy, PaginationContext<SystemEventContainer> context);

}
