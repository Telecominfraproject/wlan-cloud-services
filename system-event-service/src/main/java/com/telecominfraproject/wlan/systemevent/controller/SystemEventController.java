package com.telecominfraproject.wlan.systemevent.controller;

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
import com.telecominfraproject.wlan.systemevent.datastore.SystemEventDatastore;
import com.telecominfraproject.wlan.systemevent.models.SystemEvent;
import com.telecominfraproject.wlan.systemevent.models.SystemEventContainer;


/**
 * @author dtoptygin
 *
 */
@RestController
@Transactional
@RequestMapping(value="/api/systemEvent")
public class SystemEventController {

    private static final Logger LOG = LoggerFactory.getLogger(SystemEventController.class);

    public static class ListOfSystemEventRecords extends ArrayList<SystemEventContainer> {
        private static final long serialVersionUID = 3070319062835500930L;
    }

    @Autowired private SystemEventDatastore systemEventDatastore;
    @Autowired private CloudEventDispatcherInterface cloudEventDispatcher;

    
    /**
     * Creates new SystemEventContainer.
     *  
     * @param SystemEventContainer
     * @return stored SystemEventContainer object
     * @throws RuntimeException if SystemEventContainer record already exists
     */
    @RequestMapping(method=RequestMethod.POST)
    public SystemEventContainer create(@RequestBody SystemEventContainer systemEventRecord ) {

        LOG.debug("Creating SystemEventContainer {}", systemEventRecord);

        if (BaseJsonModel.hasUnsupportedValue(systemEventRecord)) {
            LOG.error("Failed to create SystemEventContainer, request contains unsupported value: {}", systemEventRecord);
            throw new DsDataValidationException("SystemEventContainer contains unsupported value");
        }

        long ts = System.currentTimeMillis();
        if (systemEventRecord.getCreatedTimestamp() == 0) {
        	systemEventRecord.setCreatedTimestamp(ts);
        }

        SystemEventContainer ret = systemEventDatastore.create(systemEventRecord);

        LOG.debug("Created SystemEventContainer {}", ret);

        return ret;
    }
    
    /**
     * Retrieves SystemEventContainer by id
     * @param systemEventRecordId
     * @return SystemEventContainer for the supplied id
     */
    @RequestMapping(method=RequestMethod.GET)
    public SystemEventContainer get(@RequestParam long systemEventRecordId ) {
        
        LOG.debug("Retrieving SystemEventContainer {}", systemEventRecordId);
        
        SystemEventContainer ret = systemEventDatastore.get(systemEventRecordId);

        LOG.debug("Retrieved SystemEventContainer {}", ret);

        return ret;
    }

    /**
     * Retrieves SystemEventContainer by id
     * @param systemEventRecordId
     * @return SystemEventContainer for the supplied id
     */
    @RequestMapping(value = "/orNull", method=RequestMethod.GET)
    public SystemEventContainer getOrNull(@RequestParam long systemEventRecordId ) {
        
        LOG.debug("Retrieving SystemEventContainer {}", systemEventRecordId);
        
        SystemEventContainer ret = systemEventDatastore.getOrNull(systemEventRecordId);

        LOG.debug("Retrieved SystemEventContainer {}", ret);

        return ret;
    }

    @RequestMapping(value = "/inSet", method = RequestMethod.GET)
    public ListOfSystemEventRecords getAllInSet(@RequestParam Set<Long> systemEventRecordIdSet) {
        LOG.debug("getAllInSet({})", systemEventRecordIdSet);
        try {
            List<SystemEventContainer> result = systemEventDatastore.get(systemEventRecordIdSet);
            LOG.debug("getAllInSet({}) return {} entries", systemEventRecordIdSet, result.size());
            ListOfSystemEventRecords ret = new ListOfSystemEventRecords();
            ret.addAll(result);
            return ret;
        } catch (Exception exp) {
             LOG.error("getAllInSet({}) exception ", systemEventRecordIdSet, exp);
             throw exp;
        }
    }

    @RequestMapping(value = "/forCustomer", method = RequestMethod.GET)
    public PaginationResponse<SystemEventContainer> getForCustomer(@RequestParam int customerId,
            @RequestParam List<ColumnAndSort> sortBy,
            @RequestParam PaginationContext<SystemEventContainer> paginationContext) {

        LOG.debug("Looking up SystemEventRecords for customer {} with last returned page number {}", 
                customerId, paginationContext.getLastReturnedPageNumber());

        PaginationResponse<SystemEventContainer> ret = new PaginationResponse<>();

        if (paginationContext.isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up SystemEventRecords for customer {} with last returned page number {}",
                    customerId, paginationContext.getLastReturnedPageNumber());
            ret.setContext(paginationContext);
            return ret;
        }

        PaginationResponse<SystemEventContainer> onePage = this.systemEventDatastore
                .getForCustomer(customerId,  sortBy, paginationContext);
        ret.setContext(onePage.getContext());
        ret.getItems().addAll(onePage.getItems());

        LOG.debug("Retrieved {} SystemEventRecords for customer {} ", onePage.getItems().size(), 
                customerId);

        return ret;
    }
    
    /**
     * Updates SystemEventContainer record
     * 
     * @param SystemEventContainer
     * @return updated SystemEventContainer object
     * @throws RuntimeException if SystemEventContainer record does not exist or if it was modified concurrently
     */
    @RequestMapping(method=RequestMethod.PUT)
    public SystemEventContainer update(@RequestBody SystemEventContainer systemEventRecord){
        
        LOG.debug("Updating SystemEventContainer {}", systemEventRecord);
        
        if (BaseJsonModel.hasUnsupportedValue(systemEventRecord)) {
            LOG.error("Failed to update SystemEventContainer, request contains unsupported value: {}", systemEventRecord);
            throw new DsDataValidationException("SystemEventContainer contains unsupported value");
        }

        SystemEventContainer ret = systemEventDatastore.update(systemEventRecord);

        LOG.debug("Updated SystemEventContainer {}", ret);

        return ret;
    }
    
    /**
     * Deletes SystemEventContainer record
     * 
     * @param systemEventRecordId
     * @return deleted SystemEventContainer object
     */
    @RequestMapping(method=RequestMethod.DELETE)
    public SystemEventContainer delete(@RequestParam long systemEventRecordId ) {
        
        LOG.debug("Deleting SystemEventContainer {}", systemEventRecordId);
        
        SystemEventContainer ret = systemEventDatastore.delete(systemEventRecordId);

        LOG.debug("Deleted SystemEventContainer {}", ret);
        
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
