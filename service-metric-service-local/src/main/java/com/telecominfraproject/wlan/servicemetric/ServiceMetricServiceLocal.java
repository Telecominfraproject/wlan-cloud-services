package com.telecominfraproject.wlan.servicemetric;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;

import com.telecominfraproject.wlan.servicemetric.controller.ServiceMetricController;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetric;

/**
 * @author dtoptygin
 *
 */
@Component
public class ServiceMetricServiceLocal implements ServiceMetricServiceInterface {

    @Autowired private ServiceMetricController serviceMetricController;
    private static final Logger LOG = LoggerFactory.getLogger(ServiceMetricServiceLocal.class);

    
    @Override
    public ServiceMetric create(ServiceMetric serviceMetric) {
        LOG.debug("calling serviceMetricController.create {} ", serviceMetric);
        return serviceMetricController.create(serviceMetric);
    }

    @Override
    public ServiceMetric get(long serviceMetricId) {
        LOG.debug("calling serviceMetricController.get {} ", serviceMetricId);
        return serviceMetricController.get(serviceMetricId);
    }
    
    @Override
    public ServiceMetric getOrNull(long serviceMetricId) {
        LOG.debug("calling serviceMetricController.getOrNull {} ", serviceMetricId);
        return serviceMetricController.getOrNull(serviceMetricId);
    }
    
    @Override
    public List<ServiceMetric> get(Set<Long> serviceMetricIdSet) {
        LOG.debug("calling serviceMetricController.getAllInSet {} ", serviceMetricIdSet);
        return serviceMetricController.getAllInSet(serviceMetricIdSet);
    }
    
    @Override
    public PaginationResponse<ServiceMetric> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
    		PaginationContext<ServiceMetric> context) {
        LOG.debug("calling serviceMetricController.getForCustomer {} ", customerId);
        return serviceMetricController.getForCustomer(customerId, sortBy, context);
    }

    @Override
    public ServiceMetric update(ServiceMetric serviceMetric) {
        LOG.debug("calling serviceMetricController.update {} ", serviceMetric);
        return serviceMetricController.update(serviceMetric);
    }

    @Override
    public ServiceMetric delete(long serviceMetricId) {
        LOG.debug("calling serviceMetricController.delete {} ", serviceMetricId);
        return serviceMetricController.delete(serviceMetricId);
    }

}
