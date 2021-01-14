package com.telecominfraproject.wlan.status.datastore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;

import com.telecominfraproject.wlan.status.models.Status;
import com.telecominfraproject.wlan.status.models.StatusDataType;

/**
 * @author dtoptygin
 *
 */
public interface StatusDatastore {

    Status getOrNull(int customerId, long equipmentId, StatusDataType statusDataType);

    /**
     * Update of the status - if the record was not there, it will be created. LastModifiedTs checks are not enforced in this method, it will overwrite any previously stored status object.
     * @param status
     * @return updated version of the status object
     */
    Status update(Status status);
    
    /**
     * Deletes all Status records for a given customer equipment.
     * 
     * @param customerId
     * @param equipmentId
     * @return list of deleted Status objects.
     */
    default public List<Status> delete(int customerId, long equipmentId){
        return delete(customerId, equipmentId, null);
    }

    /**
     * Deletes Status records for a given customer equipment.
     * 
     * @param customerId
     * @param equipmentId
     * @param statusDataTypes - null or empty means delete all
     * @return list of deleted Status objects.
     */
    List<Status> delete(int customerId, long equipmentId, Set<StatusDataType> statusDataTypes);

    /**
     * Force bulk update of the status objects. If a particular record was not there, it will be created. LastModifiedTs checks are not enforced in this method, it will overwrite any previously stored status object.
     * @param status
     * @return list of updated versions of the status objects
     */
    default List<Status> update(List<Status> statusList) {
    	if(statusList == null || statusList.isEmpty()) {
    		return Collections.emptyList();
    	}
    	
    	List<Status> ret = new ArrayList<>(statusList.size());
    	statusList.forEach(s -> ret.add(update(s)));
    	
    	return ret;
    }

    /**
     * Retrieves a list of Status records for a given customer equipment.
     * 
     * @param customerId
     * @param equipmentId
     * @return list of matching Status objects.
     */
    List<Status> get(int customerId, long equipmentId);

    /**
     * <br>Retrieves all of the Status records that are mapped to the provided customerId.
     * Results are returned in pages.
     * 
     * <br>When changing sort order or filters, pagination should be restarted again from the first page. 
     * Old pagination contexts would be invalid and should not be used in that case. 
     * <br>The only time when a caller should be interacting with the properties of the paginationContext is during the 
     * call to the first page by setting its property maxItemsPerPage. 
     * <br>If initial context is not provided, then the maxItemsPerPage will be set to 20.
     * <br>If sortBy is not provided, then the data will be ordered by id.
     * <ul>Allowed columns for sorting are: 
	 *<li>  "id"
	 *<li> "customerId"
	 *<li> "equipmentId"
     *<br> 
     * @param customerId
     * @param sortBy - sorting columns. Allowed values: "id", "customerId", "equipmentId". If null or empty, then sorting is done by "id" column.
     * @param context - pagination context. The only time when a caller should be interacting with the properties of the paginationContext is during the 
     * call to the first page by setting its property maxItemsPerPage. For the subsequent calls of this method the next instance of the pagination context should be taken from the pagination response as is.
     * @return next page of matching Status objects.
     */
	default PaginationResponse<Status> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
			PaginationContext<Status> context) {
		return getForCustomer(customerId, null, null, sortBy, context);
	}    

    /**
     * @param customerId - customer for which to retrieve status objects
     * @param equipmentIds - set of equipment ids for which to retrieve status objects. Empty set or null means retrieve for all customer's equipment. Some status objects are not equipment specific (apply to the whole network), in which case the equipmentId is set to 0.   
     * @param statusDataTypes - set of status data types to retrieve. Empty set or null means retrieve all status data types.
     * @param sortBy - sorting columns. Allowed values: "id", "customerId", "equipmentId". If null or empty, then sorting is done by "id" column.
     * @param context - pagination context. The only time when a caller should be interacting with the properties of the paginationContext is during the 
     * call to the first page by setting its property maxItemsPerPage. For the subsequent calls of this method the next instance of the pagination context should be taken from the pagination response as is.
     * @return next page of matching Status objects.
     */
    PaginationResponse<Status> getForCustomer(int customerId, Set<Long> equipmentIds, Set<StatusDataType> statusDataTypes, List<ColumnAndSort> sortBy, PaginationContext<Status> context);

    /**
	 * Retrieve a list of Status object for supplied equipmentIds and status data
	 * types. Note: this method is not paginated because the results are naturally
	 * limited by the supplied equipment ids, that's why the equipment ids parameter
	 * must not be null or empty when calling this method.
	 * 
	 * @param customerId      - customer for which to retrieve status objects
	 * @param equipmentIds    - set of equipment ids for which to retrieve status
	 *                        objects. Empty set or null is not allowed. Some status
	 *                        objects are not equipment specific (apply to the whole
	 *                        network), in which case their equipmentId is set to 0.
	 * @param statusDataTypes - set of status data types to retrieve. Empty set or
	 *                        null means retrieve all status data types.
	 * @return list of Status object for supplied filters
	 */
    List<Status> getForEquipment(int customerId, Set<Long> equipmentIds, Set<StatusDataType> statusDataTypes);

}
