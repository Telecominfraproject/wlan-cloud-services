package com.telecominfraproject.wlan.servicemetric;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.json.GenericResponse;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;

import com.telecominfraproject.wlan.servicemetric.controller.ServiceMetricController;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetric;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDataType;

/**
 * @author dtoptygin
 *
 */
@Component
public class ServiceMetricServiceLocal implements ServiceMetricServiceInterface {

    @Autowired private ServiceMetricController serviceMetricController;
    private static final Logger LOG = LoggerFactory.getLogger(ServiceMetricServiceLocal.class);

    
    @Override
    public GenericResponse create(ServiceMetric serviceMetric) {
        LOG.debug("calling serviceMetricController.create {} ", serviceMetric);
        return serviceMetricController.create(serviceMetric);
    }

    @Override
	public GenericResponse create(List<ServiceMetric> serviceMetrics) {
        LOG.debug("calling serviceMetricController.create {} ", serviceMetrics.size());
        return serviceMetricController.createBulk(serviceMetrics);
	}

    @Override
	public GenericResponse delete(int customerId, long equipmentId, long createdBeforeTimestamp) {
        LOG.debug("calling serviceMetricController.delete {} {} {}", customerId, equipmentId, createdBeforeTimestamp);
        return serviceMetricController.delete(customerId, equipmentId, createdBeforeTimestamp);
    }

    @Override
    public PaginationResponse<ServiceMetric> getForCustomer(long fromTime, long toTime, int customerId,
    		Set<Long> equipmentIds, Set<MacAddress> clientMacAdresses, Set<ServiceMetricDataType> dataTypes,
    		List<ColumnAndSort> sortBy, PaginationContext<ServiceMetric> context) {
        LOG.debug("calling serviceMetricController.getForCustomer {} {} {} ", fromTime, toTime, customerId);
        return serviceMetricController.getForCustomer(fromTime, toTime, customerId,
        		equipmentIds, clientMacAdresses, dataTypes, sortBy, context);
    }


}
