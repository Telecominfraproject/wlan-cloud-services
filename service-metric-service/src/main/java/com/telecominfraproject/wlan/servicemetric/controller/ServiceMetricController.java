package com.telecominfraproject.wlan.servicemetric.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.json.GenericResponse;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;
import com.telecominfraproject.wlan.servicemetric.datastore.ServiceMetricDatastore;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetric;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDataType;


/**
 * @author dtoptygin
 *
 */
@RestController
@Transactional
@RequestMapping(value="/api/serviceMetric")
public class ServiceMetricController {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceMetricController.class);

    public static class ListOfServiceMetrics extends ArrayList<ServiceMetric> {
        private static final long serialVersionUID = 3070319062835500930L;
    }

    @Autowired private ServiceMetricDatastore serviceMetricDatastore;

    
    /**
     * Creates new ServiceMetric.
     *  
     * @param ServiceMetric
     */
    @RequestMapping(method=RequestMethod.POST)
    public GenericResponse create(@RequestBody ServiceMetric serviceMetric ) {

        LOG.debug("Creating ServiceMetric {}", serviceMetric);

        if (BaseJsonModel.hasUnsupportedValue(serviceMetric)) {
            LOG.error("Failed to create ServiceMetric, request contains unsupported value: {}", serviceMetric);
            throw new DsDataValidationException("ServiceMetric contains unsupported value");
        }

        long ts = System.currentTimeMillis();
        if (serviceMetric.getCreatedTimestamp() == 0) {
        	serviceMetric.setCreatedTimestamp(ts);
        }

        serviceMetricDatastore.create(serviceMetric);

        GenericResponse ret = new GenericResponse(true, "");
        
        LOG.debug("Created ServiceMetric {}", serviceMetric);

        return ret;
    }

    
    /**
     * Creates batch of new ServiceMetrics.
     *  
     * @param ServiceMetric
     */
    @RequestMapping(value = "/bulk",method=RequestMethod.POST)
    public GenericResponse createBulk(@RequestBody List<ServiceMetric> serviceMetrics ) {

    	if(serviceMetrics==null || serviceMetrics.isEmpty()) {
    		return new GenericResponse(true, "empty service_metrics_collection_config list");
    	}
    	
        LOG.debug("Creating ServiceMetrics {}", serviceMetrics.size());
                

        if (BaseJsonModel.hasUnsupportedValue(serviceMetrics)) {
            LOG.error("Failed to create ServiceMetric, request contains unsupported value: {}", serviceMetrics);
            throw new DsDataValidationException("ServiceMetric contains unsupported value");
        }

        long ts = System.currentTimeMillis();
		serviceMetrics.forEach(m -> {
			if (m.getCreatedTimestamp() == 0) {
				m.setCreatedTimestamp(ts);
			}
		});

        serviceMetricDatastore.create(serviceMetrics);

        GenericResponse ret = new GenericResponse(true, "");
        
        LOG.debug("Created ServiceMetric {}", serviceMetrics.size());

        return ret;
    }


    @RequestMapping(value = "/forCustomer", method = RequestMethod.GET)
    public PaginationResponse<ServiceMetric> getForCustomer(@RequestParam long fromTime, @RequestParam long toTime, 
    		@RequestParam int customerId,
            @RequestParam(required = false) Set<Long> locationIds, 
    		@RequestParam(required = false) Set<Long> equipmentIds, 
    		@RequestParam(required = false) Set<MacAddress> clientMacAdresses, 
    		@RequestParam(required = false) Set<ServiceMetricDataType> dataTypes,
    		@RequestParam(required = false) List<ColumnAndSort> sortBy, 
    		@RequestParam(required = false) PaginationContext<ServiceMetric> paginationContext) {    

    	if(paginationContext==null) {
    		paginationContext = new PaginationContext<>();
    	}
    	
        LOG.debug("Looking up ServiceMetrics for customer {} location {} equipment {} clientMacs {} from {} to {} with last returned page number {}", 
                customerId, locationIds, equipmentIds, clientMacAdresses, fromTime, toTime, paginationContext.getLastReturnedPageNumber());

        PaginationResponse<ServiceMetric> ret = new PaginationResponse<>();

        if (paginationContext.isLastPage()) {
            // no more pages available according to the context
            LOG.debug("No more pages available when looking up ServiceMetrics");
            ret.setContext(paginationContext);
            return ret;
        }

        PaginationResponse<ServiceMetric> onePage = this.serviceMetricDatastore
                .getForCustomer(fromTime, toTime, customerId,
                		locationIds, equipmentIds, clientMacAdresses, dataTypes, sortBy, paginationContext);
        ret.setContext(onePage.getContext());
        ret.getItems().addAll(onePage.getItems());

        LOG.debug("Retrieved {} ServiceMetrics", onePage.getItems().size());

        return ret;
    }
    
    
    /**
     * Deletes ServiceMetric record
     * 
     * @param serviceMetricId
     * @return deleted ServiceMetric object
     */
    @RequestMapping(method=RequestMethod.DELETE)
    public GenericResponse delete(@RequestParam int customerId, @RequestParam long equipmentId, @RequestParam long createdBeforeTimestamp) {
        
        LOG.debug("Deleting ServiceMetrics for  {} {} {}", customerId, equipmentId, createdBeforeTimestamp);
        
        serviceMetricDatastore.delete(customerId, equipmentId, createdBeforeTimestamp);

        LOG.debug("Deleted ServiceMetrics for  {} {} {}", customerId, equipmentId, createdBeforeTimestamp);
        
        return new GenericResponse(true, "");
    }
    
}
