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
import com.telecominfraproject.wlan.status.datastore.StatusDatastore;
import com.telecominfraproject.wlan.status.models.Status;
import com.telecominfraproject.wlan.status.models.StatusDataType;
import com.telecominfraproject.wlan.status.models.events.StatusChangedEvent;
import com.telecominfraproject.wlan.status.models.events.StatusRemovedEvent;
import com.telecominfraproject.wlan.systemevent.models.SystemEvent;


/**
 * @author dtoptygin
 *
 */
@RestController
@Transactional
@RequestMapping(value="/api/status")
public class StatusController {

    private static final Logger LOG = LoggerFactory.getLogger(StatusController.class);

    public static class ListOfStatuses extends ArrayList<Status> {
        private static final long serialVersionUID = 3070319062835500930L;
    }

    @Autowired private StatusDatastore statusDatastore;
    @Autowired private CloudEventDispatcherInterface cloudEventDispatcher;

    /**
     * Retrieves Status by id
     * @param statusId
     * @return Status for the supplied id
     */
    @RequestMapping(value = "/orNull", method=RequestMethod.GET)
    public Status getOrNull(@RequestParam int customerId, @RequestParam long equipmentId, @RequestParam StatusDataType statusDataType ) {
        
        LOG.debug("Retrieving Status {}:{}:{}", customerId, equipmentId, statusDataType);
        
        Status ret = statusDatastore.getOrNull(customerId, equipmentId, statusDataType);

        LOG.debug("Retrieved Status {}:{}:{} {}", customerId, equipmentId, statusDataType, ret);

        return ret;
    }

    @RequestMapping(value = "/forEquipment", method = RequestMethod.GET)
    public ListOfStatuses getForEquipment(@RequestParam int customerId, @RequestParam long equipmentId) {
        LOG.debug("getAllForEquipment({},{})", customerId, equipmentId);
        try {
            List<Status> result = statusDatastore.get(customerId, equipmentId);
            LOG.debug("getAllForEquipment({},{}) return {} entries", customerId, equipmentId, result.size());
            ListOfStatuses ret = new ListOfStatuses();
            ret.addAll(result);
            return ret;
        } catch (Exception exp) {
             LOG.error("getAllForEquipment({},{}) exception ", customerId, equipmentId, exp);
             throw exp;
        }
    }

    @RequestMapping(value = "/forCustomer", method = RequestMethod.GET)
    public PaginationResponse<Status> getForCustomer(@RequestParam int customerId,
            @RequestParam List<ColumnAndSort> sortBy,
            @RequestParam(required = false) PaginationContext<Status> paginationContext) {

    	if(paginationContext == null) {
    		paginationContext = new PaginationContext<>();
    	}

        LOG.debug("Looking up Statuses for customer {} with last returned page number {}", 
                customerId, paginationContext.getLastReturnedPageNumber());

        PaginationResponse<Status> ret = new PaginationResponse<>();

        if (paginationContext.isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up Statuses for customer {} with last returned page number {}",
                    customerId, paginationContext.getLastReturnedPageNumber());
            ret.setContext(paginationContext);
            return ret;
        }

        PaginationResponse<Status> onePage = this.statusDatastore
                .getForCustomer(customerId,  sortBy, paginationContext);
        ret.setContext(onePage.getContext());
        ret.getItems().addAll(onePage.getItems());

        LOG.debug("Retrieved {} Statuses for customer {} ", onePage.getItems().size(), 
                customerId);

        return ret;
    }

    @RequestMapping(value = "/forCustomerWithFilter", method = RequestMethod.GET)
    public PaginationResponse<Status> getForCustomerWithFilter(@RequestParam int customerId,
    		@RequestParam Set<Long> equipmentIds,
    		@RequestParam Set<StatusDataType> statusDataTypes,
            @RequestParam List<ColumnAndSort> sortBy,
            @RequestParam(required = false) PaginationContext<Status> paginationContext) {

    	if(paginationContext == null) {
    		paginationContext = new PaginationContext<>();
    	}

        LOG.debug("Looking up Statuses for customer {} equipment {} types {} with last returned page number {}", 
                customerId, equipmentIds, statusDataTypes, paginationContext.getLastReturnedPageNumber());

        PaginationResponse<Status> ret = new PaginationResponse<>();

        if (paginationContext.isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up Statuses for customer {} equipment {} types {}  with last returned page number {}",
                    customerId, equipmentIds, statusDataTypes, paginationContext.getLastReturnedPageNumber());
            ret.setContext(paginationContext);
            return ret;
        }

        PaginationResponse<Status> onePage = this.statusDatastore
                .getForCustomer(customerId, equipmentIds, statusDataTypes, sortBy, paginationContext);
        ret.setContext(onePage.getContext());
        ret.getItems().addAll(onePage.getItems());

        LOG.debug("Retrieved {} Statuses for customer {} equipment {} types {}  ", onePage.getItems().size(), 
                customerId, equipmentIds, statusDataTypes);

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
     * Updates batch of Status records
     * 
     * @param statusList - batch of objects to update
     * @return list of updated Status objects
     * @throws RuntimeException if Status record does not exist or if it was modified concurrently
     */
    @RequestMapping(value = "/bulk", method=RequestMethod.PUT)
    public ListOfStatuses updateBulk(@RequestBody List<Status> statusList){
        
    	if(statusList == null || statusList.isEmpty()) {
    		//nothing to do here
    		return new ListOfStatuses();
    	}
    	
        LOG.debug("Updating Statuses {}", statusList);
                
        if (BaseJsonModel.hasUnsupportedValue(statusList)) {
            LOG.error("Failed to update Status, request contains unsupported value: {}", statusList);
            throw new DsDataValidationException("Status contains unsupported value");
        }

        List<Status> ret = statusDatastore.update(statusList);

        LOG.debug("Updated Statuses {}", ret);

        List<SystemEvent> events = new ArrayList<>();
        ret.forEach(e -> events.add(new StatusChangedEvent(e)));
        publishEvents(events);

        ListOfStatuses retList = new ListOfStatuses();
        retList.addAll(ret);
        
        return retList;
    }

    /**
     * Deletes Status record
     * 
     * @param statusId
     * @return deleted Status object
     */
    @RequestMapping(method=RequestMethod.DELETE)
    public ListOfStatuses delete(@RequestParam int customerId, @RequestParam long equipmentId) {
        LOG.debug("delete({},{})", customerId, equipmentId);
        
        List<Status> ret = statusDatastore.delete(customerId, equipmentId);

        LOG.debug("deleted statuses ({},{})", customerId, equipmentId);

        List<SystemEvent> events = new ArrayList<>();
        ret.forEach(s -> events.add(new StatusRemovedEvent(s)));
        
        publishEvents(events);
        
        ListOfStatuses retStatusList = new ListOfStatuses();
        retStatusList.addAll(ret);

        return retStatusList;
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

    private void publishEvents(List<SystemEvent> events) {
        try {
            cloudEventDispatcher.publishEventsBulk(events);
        } catch (Exception e) {
            LOG.error("Failed to publish events : {}", events, e);
        }
    }


    @RequestMapping(value = "/forEquipmentWithFilters", method = RequestMethod.GET)
    public ListOfStatuses getForEquipment(@RequestParam int customerId, 
    		@RequestParam Set<Long> equipmentIds, 
    		@RequestParam Set<StatusDataType> statusDataTypes) {
        LOG.debug("getForEquipment({},{},{})", customerId, equipmentIds, statusDataTypes);
        try {
            List<Status> result = statusDatastore.getForEquipment(customerId, equipmentIds, statusDataTypes);
            LOG.debug("getForEquipment({},{},{}) return {} entries", customerId, equipmentIds, statusDataTypes, result.size());
            ListOfStatuses ret = new ListOfStatuses();
            ret.addAll(result);
            return ret;
        } catch (Exception exp) {
             LOG.error("getForEquipment({},{},{}) exception ", customerId, equipmentIds, statusDataTypes, exp);
             throw exp;
        }
	}
        
}
