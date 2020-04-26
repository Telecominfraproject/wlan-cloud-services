package com.telecominfraproject.wlan.servicemetric.datastore.rdbms;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;

import com.telecominfraproject.wlan.servicemetric.datastore.ServiceMetricDatastore;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetric;

/**
 * @author dtoptygin
 *
 */
@Configuration
public class ServiceMetricDatastoreRdbms implements ServiceMetricDatastore {

    @Autowired ServiceMetricDAO serviceMetricDAO;

    @Override
    public ServiceMetric create(ServiceMetric serviceMetric) {
        return serviceMetricDAO.create(serviceMetric);
    }

    @Override
    public ServiceMetric get(long serviceMetricId) {
        return serviceMetricDAO.get(serviceMetricId);
    }

    @Override
    public ServiceMetric getOrNull(long serviceMetricId) {
        return serviceMetricDAO.getOrNull(serviceMetricId);
    }
    
    @Override
    public ServiceMetric update(ServiceMetric serviceMetric) {
        return serviceMetricDAO.update(serviceMetric);
    }

    @Override
    public ServiceMetric delete(long serviceMetricId) {
        return serviceMetricDAO.delete(serviceMetricId);
    }
    
    @Override
    public List<ServiceMetric> get(Set<Long> serviceMetricIdSet) {
    	return serviceMetricDAO.get(serviceMetricIdSet);
    }
    
    @Override
    public PaginationResponse<ServiceMetric> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
    		PaginationContext<ServiceMetric> context) {
    	return serviceMetricDAO.getForCustomer( customerId, sortBy, context);
    }
}
