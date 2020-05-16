package com.telecominfraproject.wlan.status.datastore;

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
     * Deletes a list of Status records for a given customer equipment.
     * 
     * @param customerId
     * @param equipmentId
     * @return list of deleted Status objects.
     */
    List<Status> delete(int customerId, long equipmentId);

    /**
     * Force bulk update of the status objects. If a particular record was not there, it will be created. LastModifiedTs checks are not enforced in this method, it will overwrite any previously stored status object.
     * @param status
     * @return list of updated versions of the status objects
     */
    List<Status> update(List<Status> status);

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
    PaginationResponse<Status> getForCustomer(int customerId, List<ColumnAndSort> sortBy, PaginationContext<Status> context);

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

}
