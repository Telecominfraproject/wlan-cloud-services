package com.telecominfraproject.wlan.systemevent.datastore;

import java.util.List;
import java.util.Set;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.systemevent.models.SystemEventRecord;

/**
 * @author dtoptygin
 *
 */
public interface SystemEventDatastore {

    void create(SystemEventRecord systemEventRecord);
    void create(List<SystemEventRecord> systemEventRecords);
    void delete(int customerId, long equipmentId, long createdBeforeTimestamp);

    void delete(long createdBeforeTimestamp);

    /**
     * <br>Retrieves all of the SystemEvent records that are mapped to the provided customerId.
     * Results are returned in pages.
     * 
     * <br>When changing sort order or filters, pagination should be restarted again from the first page. 
     * Old pagination contexts would be invalid and should not be used in that case. 
     * <br>The only time when a caller should be interacting with the properties of the paginationContext is during the 
     * call to the first page by setting property maxItemsPerPage. 
     * <br>If initial context is not provided, then the maxItemsPerPage will be set to 20.
     * <br>If sortBy is not provided, then the data will be ordered by eventTimestamp.
     * <ul>Allowed columns for sorting are:
     *<li> "eventTimestamp"
	 *<li> "equipmentId"
	 *<li> "dataType"
     *<br> 
	 * @param fromTime
	 * @param toTime
	 * @param customerId
	 * @param equipmentIds - null or empty means all equipment for customer
	 * @param dataTypes - null or empty means all event data types
	 * @param sortBy
	 * @param context
     * @return next page of matching SystemEventRecord objects.
	 */
	PaginationResponse<SystemEventRecord> getForCustomer(
			long fromTime,
			long toTime,
			int customerId, 
			Set<Long> equipmentIds,
			Set<String> dataTypes, 
			List<ColumnAndSort> sortBy,
			PaginationContext<SystemEventRecord> context);

}
