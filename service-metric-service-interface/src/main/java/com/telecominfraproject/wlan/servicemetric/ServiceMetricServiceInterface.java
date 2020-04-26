package com.telecominfraproject.wlan.servicemetric;

import java.util.List;
import java.util.Set;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;

import com.telecominfraproject.wlan.servicemetric.models.ServiceMetric;


/**
 * @author dtoptygin
 *
 */
public interface ServiceMetricServiceInterface {
    
    /**
     * Creates new ServiceMetric
     *  
     * @param ServiceMetric
     * @return stored ServiceMetric object
     * @throws RuntimeException if ServiceMetric record already exists
     */
    ServiceMetric create(ServiceMetric serviceMetric );
    
    /**
     * Retrieves ServiceMetric by id
     * @param serviceMetricId
     * @return ServiceMetric for the supplied id
     * @throws RuntimeException if ServiceMetric record does not exist
     */
    ServiceMetric get(long serviceMetricId );

    /**
     * Retrieves ServiceMetric by id
     * @param serviceMetricId
     * @return ServiceMetric for the supplied id
     */
    ServiceMetric getOrNull(long serviceMetricId );

    /**
     * Retrieves a list of ServiceMetric records that which have their Id in the provided set.
     * 
     * @param serviceMetricIdSet
     * @return list of matching ServiceMetric objects.
     */
    List<ServiceMetric> get(Set<Long> serviceMetricIdSet);

    /**
     * Updates ServiceMetric 
     * 
     * @param ServiceMetric
     * @return updated ServiceMetric object
     * @throws RuntimeException if ServiceMetric record does not exist or if it was modified concurrently
     */
    ServiceMetric update(ServiceMetric serviceMetric);
    
    /**
     * Deletes ServiceMetric
     * 
     * @param serviceMetricId
     * @return deleted ServiceMetric object
     */
    ServiceMetric delete(long serviceMetricId );
    
    /**
     * <br>Retrieves all of the ServiceMetric records that are mapped to the provided customerId.
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
     * @return next page of matching ServiceMetric objects.
     */
    PaginationResponse<ServiceMetric> getForCustomer(int customerId, List<ColumnAndSort> sortBy, PaginationContext<ServiceMetric> context);

}
