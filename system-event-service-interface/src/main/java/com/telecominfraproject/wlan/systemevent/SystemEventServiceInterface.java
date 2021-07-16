package com.telecominfraproject.wlan.systemevent;

import java.util.List;
import java.util.Set;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.json.GenericResponse;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.systemevent.models.SystemEventRecord;
import com.telecominfraproject.wlan.systemevent.models.SystemEventStats;


/**
 * @author dtoptygin
 *
 */
public interface SystemEventServiceInterface {
    
	GenericResponse create(SystemEventRecord systemEventRecord);
	GenericResponse create(List<SystemEventRecord> systemEventRecords);
	GenericResponse delete(int customerId, long equipmentId, long createdBeforeTimestamp);
	GenericResponse delete(long createdBeforeTimestamp);

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
     * @param locationIds - null or empty means all locations for customer
     * @param equipmentIds - null or empty means all equipment for customer
     * @param clientMacAddresses - null or empty means all client MAC addresses
	 * @param dataTypes - null or empty means all event data types
	 * @param sortBy
	 * @param context
     * @return next page of matching SystemEventRecord objects.
	 */
	PaginationResponse<SystemEventRecord> getForCustomer(
			long fromTime,
			long toTime,
			int customerId, 
            Set<Long> locationIds,
            Set<Long> equipmentIds,
            Set<MacAddress> clientMacAddresses,
			Set<String> dataTypes, 
			List<ColumnAndSort> sortBy,
			PaginationContext<SystemEventRecord> context);

	default public  PaginationResponse<SystemEventRecord> getForCustomer(
            long fromTime,
            long toTime,
            int customerId, 
            Set<Long> equipmentIds,
            Set<String> dataTypes, 
            List<ColumnAndSort> sortBy,
            PaginationContext<SystemEventRecord> context){
	    return getForCustomer(fromTime, toTime, customerId, null, equipmentIds, null, dataTypes, sortBy, context);
	}

    /**
     * @param filterAttributeName
     * @param filterAttributeValue
     * @param fromTime
     * @param toTime
     * @return Returns system event statistics for the given time range.
     */
    SystemEventStats getSystemEventStats(String filterAttributeName, String filterAttributeValue, long fromTime, long toTime);

}
