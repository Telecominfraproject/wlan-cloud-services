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

import com.telecominfraproject.wlan.cloudeventdispatcher.CloudEventDispatcherInterface;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;
import com.telecominfraproject.wlan.systemevent.models.SystemEvent;

import com.telecominfraproject.wlan.servicemetric.datastore.ServiceMetricDatastore;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetric;
import com.telecominfraproject.wlan.servicemetric.models.events.ServiceMetricAddedEvent;
import com.telecominfraproject.wlan.servicemetric.models.events.ServiceMetricChangedEvent;
import com.telecominfraproject.wlan.servicemetric.models.events.ServiceMetricRemovedEvent;


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
    @Autowired private CloudEventDispatcherInterface cloudEventDispatcher;

    
    /**
     * Creates new ServiceMetric.
     *  
     * @param ServiceMetric
     * @return stored ServiceMetric object
     * @throws RuntimeException if ServiceMetric record already exists
     */
    @RequestMapping(method=RequestMethod.POST)
    public ServiceMetric create(@RequestBody ServiceMetric serviceMetric ) {

        LOG.debug("Creating ServiceMetric {}", serviceMetric);

        if (BaseJsonModel.hasUnsupportedValue(serviceMetric)) {
            LOG.error("Failed to create ServiceMetric, request contains unsupported value: {}", serviceMetric);
            throw new DsDataValidationException("ServiceMetric contains unsupported value");
        }

        long ts = System.currentTimeMillis();
        if (serviceMetric.getCreatedTimestamp() == 0) {
        	serviceMetric.setCreatedTimestamp(ts);
        }
        serviceMetric.setLastModifiedTimestamp(ts);

        ServiceMetric ret = serviceMetricDatastore.create(serviceMetric);

        LOG.debug("Created ServiceMetric {}", ret);

        ServiceMetricAddedEvent event = new ServiceMetricAddedEvent(ret);
        publishEvent(event);


        return ret;
    }
    
    /**
     * Retrieves ServiceMetric by id
     * @param serviceMetricId
     * @return ServiceMetric for the supplied id
     */
    @RequestMapping(method=RequestMethod.GET)
    public ServiceMetric get(@RequestParam long serviceMetricId ) {
        
        LOG.debug("Retrieving ServiceMetric {}", serviceMetricId);
        
        ServiceMetric ret = serviceMetricDatastore.get(serviceMetricId);

        LOG.debug("Retrieved ServiceMetric {}", ret);

        return ret;
    }

    /**
     * Retrieves ServiceMetric by id
     * @param serviceMetricId
     * @return ServiceMetric for the supplied id
     */
    @RequestMapping(value = "/orNull", method=RequestMethod.GET)
    public ServiceMetric getOrNull(@RequestParam long serviceMetricId ) {
        
        LOG.debug("Retrieving ServiceMetric {}", serviceMetricId);
        
        ServiceMetric ret = serviceMetricDatastore.getOrNull(serviceMetricId);

        LOG.debug("Retrieved ServiceMetric {}", ret);

        return ret;
    }

    @RequestMapping(value = "/inSet", method = RequestMethod.GET)
    public ListOfServiceMetrics getAllInSet(@RequestParam Set<Long> serviceMetricIdSet) {
        LOG.debug("getAllInSet({})", serviceMetricIdSet);
        try {
            List<ServiceMetric> result = serviceMetricDatastore.get(serviceMetricIdSet);
            LOG.debug("getAllInSet({}) return {} entries", serviceMetricIdSet, result.size());
            ListOfServiceMetrics ret = new ListOfServiceMetrics();
            ret.addAll(result);
            return ret;
        } catch (Exception exp) {
             LOG.error("getAllInSet({}) exception ", serviceMetricIdSet, exp);
             throw exp;
        }
    }

    @RequestMapping(value = "/forCustomer", method = RequestMethod.GET)
    public PaginationResponse<ServiceMetric> getForCustomer(@RequestParam int customerId,
            @RequestParam List<ColumnAndSort> sortBy,
            @RequestParam PaginationContext<ServiceMetric> paginationContext) {

        LOG.debug("Looking up ServiceMetrics for customer {} with last returned page number {}", 
                customerId, paginationContext.getLastReturnedPageNumber());

        PaginationResponse<ServiceMetric> ret = new PaginationResponse<>();

        if (paginationContext.isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up ServiceMetrics for customer {} with last returned page number {}",
                    customerId, paginationContext.getLastReturnedPageNumber());
            ret.setContext(paginationContext);
            return ret;
        }

        PaginationResponse<ServiceMetric> onePage = this.serviceMetricDatastore
                .getForCustomer(customerId,  sortBy, paginationContext);
        ret.setContext(onePage.getContext());
        ret.getItems().addAll(onePage.getItems());

        LOG.debug("Retrieved {} ServiceMetrics for customer {} ", onePage.getItems().size(), 
                customerId);

        return ret;
    }
    
    /**
     * Updates ServiceMetric record
     * 
     * @param ServiceMetric
     * @return updated ServiceMetric object
     * @throws RuntimeException if ServiceMetric record does not exist or if it was modified concurrently
     */
    @RequestMapping(method=RequestMethod.PUT)
    public ServiceMetric update(@RequestBody ServiceMetric serviceMetric){
        
        LOG.debug("Updating ServiceMetric {}", serviceMetric);
        
        if (BaseJsonModel.hasUnsupportedValue(serviceMetric)) {
            LOG.error("Failed to update ServiceMetric, request contains unsupported value: {}", serviceMetric);
            throw new DsDataValidationException("ServiceMetric contains unsupported value");
        }

        ServiceMetric ret = serviceMetricDatastore.update(serviceMetric);

        LOG.debug("Updated ServiceMetric {}", ret);

        ServiceMetricChangedEvent event = new ServiceMetricChangedEvent(ret);
        publishEvent(event);

        return ret;
    }
    
    /**
     * Deletes ServiceMetric record
     * 
     * @param serviceMetricId
     * @return deleted ServiceMetric object
     */
    @RequestMapping(method=RequestMethod.DELETE)
    public ServiceMetric delete(@RequestParam long serviceMetricId ) {
        
        LOG.debug("Deleting ServiceMetric {}", serviceMetricId);
        
        ServiceMetric ret = serviceMetricDatastore.delete(serviceMetricId);

        LOG.debug("Deleted ServiceMetric {}", ret);
        
        ServiceMetricRemovedEvent event = new ServiceMetricRemovedEvent(ret);
        publishEvent(event);

        return ret;
    }

    private void publishEvent(SystemEvent event) {
        if (event == null) {
            return;
        }
        
        try {
            cloudEventDispatcher.publishEvent(event);
        } catch (Exception e) {
            LOG.error("Failed to publish event : {}", event, e);
        }
    }

    
}
