package com.telecominfraproject.wlan.servicemetric.datastore.rdbms;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;

import com.telecominfraproject.wlan.servicemetric.datastore.ServiceMetricDatastore;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetric;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDataType;

/**
 * @author dtoptygin
 *
 */
@Configuration
public class ServiceMetricDatastoreRdbms implements ServiceMetricDatastore {

    @Autowired ServiceMetricDAO serviceMetricDAO;

    @Override
    public void create(ServiceMetric serviceMetric) {
        serviceMetricDAO.create(serviceMetric);
    }

    @Override
    public void create(List<ServiceMetric> serviceMetrics) {
    	 serviceMetricDAO.create(serviceMetrics);
    }
    
    @Override
    public void delete(int customerId, long equipmentId, long createdBeforeTimestamp) {
        serviceMetricDAO.delete(customerId, equipmentId, createdBeforeTimestamp);
    }
    
    @Override
    public PaginationResponse<ServiceMetric> getForCustomer(long fromTime, long toTime, int customerId,
    		Set<Long> equipmentIds, Set<MacAddress> clientMacAdresses, Set<ServiceMetricDataType> dataTypes,
    		List<ColumnAndSort> sortBy, PaginationContext<ServiceMetric> context) {
    	if(context ==null) {
    		context = new PaginationContext<>();
    	}
    	return serviceMetricDAO.getForCustomer( fromTime, toTime, customerId, equipmentIds, clientMacAdresses, dataTypes, sortBy, context);
    }
}
