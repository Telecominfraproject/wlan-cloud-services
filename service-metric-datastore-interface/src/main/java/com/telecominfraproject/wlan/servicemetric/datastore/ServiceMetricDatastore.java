package com.telecominfraproject.wlan.servicemetric.datastore;

import java.util.List;
import java.util.Set;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetric;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDataType;

/**
 * @author dtoptygin
 *
 */
public interface ServiceMetricDatastore {

    void create(ServiceMetric serviceMetric);
    void create(List<ServiceMetric> serviceMetrics);
    void delete(int customerId, long equipmentId, long createdBeforeTimestamp);

    void delete(long createdBeforeTimestamp);

    /**
     * <br>Retrieves all of the ServiceMetric records that are mapped to the provided customerId.
     * Results are returned in pages.
     * 
     * <br>When changing sort order or filters, pagination should be restarted again from the first page. 
     * Old pagination contexts would be invalid and should not be used in that case. 
     * <br>The only time when a caller should be interacting with the properties of the paginationContext is during the 
     * call to the first page by setting property maxItemsPerPage. 
     * <br>If initial context is not provided, then the maxItemsPerPage will be set to 20.
     * <br>If sortBy is not provided, then the data will be ordered by createdTimestamp.
     * <ul>Allowed columns for sorting are:
     *<li> "createdTimestamp"
     *<li> "locationId"
     *<li> "equipmentId"
	 *<li> "clientMac"
	 *<li> "dataType"
     *<br> 
	 * @param fromTime
	 * @param toTime
	 * @param customerId
     * @param locationIds - null or empty means all locations for customer. Note: Location hierarchy is NOT taken into account when filtering records.
	 * @param equipmentIds - null or empty means all equipment for customer
	 * @param clientMacAddresses - null or empty means all client MAC addresses
	 * @param dataTypes - null or empty means all metric data types
	 * @param sortBy
	 * @param context
     * @return next page of matching ServiceMetric objects.
	 */
	PaginationResponse<ServiceMetric> getForCustomer(
			long fromTime,
			long toTime,
			int customerId, 
            Set<Long> locationIds,
			Set<Long> equipmentIds,
			Set<MacAddress> clientMacAddresses,
			Set<ServiceMetricDataType> dataTypes, 
			List<ColumnAndSort> sortBy,
			PaginationContext<ServiceMetric> context);

}
