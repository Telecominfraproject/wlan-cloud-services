package com.telecominfraproject.wlan.status.controller;

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

import com.telecominfraproject.wlan.status.datastore.StatusDatastore;
import com.telecominfraproject.wlan.status.models.Status;
import com.telecominfraproject.wlan.status.models.events.StatusAddedEvent;
import com.telecominfraproject.wlan.status.models.events.StatusChangedEvent;
import com.telecominfraproject.wlan.status.models.events.StatusRemovedEvent;


/**
 * @author dtoptygin
 *
 */
@RestController
@Transactional
@RequestMapping(value="/api/status")
public class StatusController {

    private static final Logger LOG = LoggerFactory.getLogger(StatusController.class);

    public static class ListOfStatuss extends ArrayList<Status> {
        private static final long serialVersionUID = 3070319062835500930L;
    }

    @Autowired private StatusDatastore statusDatastore;
    @Autowired private CloudEventDispatcherInterface cloudEventDispatcher;

    
    /**
     * Creates new Status.
     *  
     * @param Status
     * @return stored Status object
     * @throws RuntimeException if Status record already exists
     */
    @RequestMapping(method=RequestMethod.POST)
    public Status create(@RequestBody Status status ) {

        LOG.debug("Creating Status {}", status);

        if (BaseJsonModel.hasUnsupportedValue(status)) {
            LOG.error("Failed to create Status, request contains unsupported value: {}", status);
            throw new DsDataValidationException("Status contains unsupported value");
        }

        long ts = System.currentTimeMillis();
        if (status.getCreatedTimestamp() == 0) {
        	status.setCreatedTimestamp(ts);
        }
        status.setLastModifiedTimestamp(ts);

        Status ret = statusDatastore.create(status);

        LOG.debug("Created Status {}", ret);

        StatusAddedEvent event = new StatusAddedEvent(ret);
        publishEvent(event);


        return ret;
    }
    
    /**
     * Retrieves Status by id
     * @param statusId
     * @return Status for the supplied id
     */
    @RequestMapping(method=RequestMethod.GET)
    public Status get(@RequestParam long statusId ) {
        
        LOG.debug("Retrieving Status {}", statusId);
        
        Status ret = statusDatastore.get(statusId);

        LOG.debug("Retrieved Status {}", ret);

        return ret;
    }

    /**
     * Retrieves Status by id
     * @param statusId
     * @return Status for the supplied id
     */
    @RequestMapping(value = "/orNull", method=RequestMethod.GET)
    public Status getOrNull(@RequestParam long statusId ) {
        
        LOG.debug("Retrieving Status {}", statusId);
        
        Status ret = statusDatastore.getOrNull(statusId);

        LOG.debug("Retrieved Status {}", ret);

        return ret;
    }

    @RequestMapping(value = "/inSet", method = RequestMethod.GET)
    public ListOfStatuss getAllInSet(@RequestParam Set<Long> statusIdSet) {
        LOG.debug("getAllInSet({})", statusIdSet);
        try {
            List<Status> result = statusDatastore.get(statusIdSet);
            LOG.debug("getAllInSet({}) return {} entries", statusIdSet, result.size());
            ListOfStatuss ret = new ListOfStatuss();
            ret.addAll(result);
            return ret;
        } catch (Exception exp) {
             LOG.error("getAllInSet({}) exception ", statusIdSet, exp);
             throw exp;
        }
    }

    @RequestMapping(value = "/forCustomer", method = RequestMethod.GET)
    public PaginationResponse<Status> getForCustomer(@RequestParam int customerId,
            @RequestParam List<ColumnAndSort> sortBy,
            @RequestParam PaginationContext<Status> paginationContext) {

        LOG.debug("Looking up Statuss for customer {} with last returned page number {}", 
                customerId, paginationContext.getLastReturnedPageNumber());

        PaginationResponse<Status> ret = new PaginationResponse<>();

        if (paginationContext.isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up Statuss for customer {} with last returned page number {}",
                    customerId, paginationContext.getLastReturnedPageNumber());
            ret.setContext(paginationContext);
            return ret;
        }

        PaginationResponse<Status> onePage = this.statusDatastore
                .getForCustomer(customerId,  sortBy, paginationContext);
        ret.setContext(onePage.getContext());
        ret.getItems().addAll(onePage.getItems());

        LOG.debug("Retrieved {} Statuss for customer {} ", onePage.getItems().size(), 
                customerId);

        return ret;
    }
    
    /**
     * Updates Status record
     * 
     * @param Status
     * @return updated Status object
     * @throws RuntimeException if Status record does not exist or if it was modified concurrently
     */
    @RequestMapping(method=RequestMethod.PUT)
    public Status update(@RequestBody Status status){
        
        LOG.debug("Updating Status {}", status);
        
        if (BaseJsonModel.hasUnsupportedValue(status)) {
            LOG.error("Failed to update Status, request contains unsupported value: {}", status);
            throw new DsDataValidationException("Status contains unsupported value");
        }

        Status ret = statusDatastore.update(status);

        LOG.debug("Updated Status {}", ret);

        StatusChangedEvent event = new StatusChangedEvent(ret);
        publishEvent(event);

        return ret;
    }
    
    /**
     * Deletes Status record
     * 
     * @param statusId
     * @return deleted Status object
     */
    @RequestMapping(method=RequestMethod.DELETE)
    public Status delete(@RequestParam long statusId ) {
        
        LOG.debug("Deleting Status {}", statusId);
        
        Status ret = statusDatastore.delete(statusId);

        LOG.debug("Deleted Status {}", ret);
        
        StatusRemovedEvent event = new StatusRemovedEvent(ret);
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
