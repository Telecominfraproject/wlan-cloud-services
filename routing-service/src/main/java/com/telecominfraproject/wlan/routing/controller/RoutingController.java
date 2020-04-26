package com.telecominfraproject.wlan.routing.controller;

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

import com.telecominfraproject.wlan.routing.datastore.RoutingDatastore;
import com.telecominfraproject.wlan.routing.models.Routing;
import com.telecominfraproject.wlan.routing.models.events.RoutingAddedEvent;
import com.telecominfraproject.wlan.routing.models.events.RoutingChangedEvent;
import com.telecominfraproject.wlan.routing.models.events.RoutingRemovedEvent;


/**
 * @author dtoptygin
 *
 */
@RestController
@Transactional
@RequestMapping(value="/api/routing")
public class RoutingController {

    private static final Logger LOG = LoggerFactory.getLogger(RoutingController.class);

    public static class ListOfRoutings extends ArrayList<Routing> {
        private static final long serialVersionUID = 3070319062835500930L;
    }

    @Autowired private RoutingDatastore routingDatastore;
    @Autowired private CloudEventDispatcherInterface cloudEventDispatcher;

    
    /**
     * Creates new Routing.
     *  
     * @param Routing
     * @return stored Routing object
     * @throws RuntimeException if Routing record already exists
     */
    @RequestMapping(method=RequestMethod.POST)
    public Routing create(@RequestBody Routing routing ) {

        LOG.debug("Creating Routing {}", routing);

        if (BaseJsonModel.hasUnsupportedValue(routing)) {
            LOG.error("Failed to create Routing, request contains unsupported value: {}", routing);
            throw new DsDataValidationException("Routing contains unsupported value");
        }

        long ts = System.currentTimeMillis();
        if (routing.getCreatedTimestamp() == 0) {
        	routing.setCreatedTimestamp(ts);
        }
        routing.setLastModifiedTimestamp(ts);

        Routing ret = routingDatastore.create(routing);

        LOG.debug("Created Routing {}", ret);

        RoutingAddedEvent event = new RoutingAddedEvent(ret);
        publishEvent(event);


        return ret;
    }
    
    /**
     * Retrieves Routing by id
     * @param routingId
     * @return Routing for the supplied id
     */
    @RequestMapping(method=RequestMethod.GET)
    public Routing get(@RequestParam long routingId ) {
        
        LOG.debug("Retrieving Routing {}", routingId);
        
        Routing ret = routingDatastore.get(routingId);

        LOG.debug("Retrieved Routing {}", ret);

        return ret;
    }

    /**
     * Retrieves Routing by id
     * @param routingId
     * @return Routing for the supplied id
     */
    @RequestMapping(value = "/orNull", method=RequestMethod.GET)
    public Routing getOrNull(@RequestParam long routingId ) {
        
        LOG.debug("Retrieving Routing {}", routingId);
        
        Routing ret = routingDatastore.getOrNull(routingId);

        LOG.debug("Retrieved Routing {}", ret);

        return ret;
    }

    @RequestMapping(value = "/inSet", method = RequestMethod.GET)
    public ListOfRoutings getAllInSet(@RequestParam Set<Long> routingIdSet) {
        LOG.debug("getAllInSet({})", routingIdSet);
        try {
            List<Routing> result = routingDatastore.get(routingIdSet);
            LOG.debug("getAllInSet({}) return {} entries", routingIdSet, result.size());
            ListOfRoutings ret = new ListOfRoutings();
            ret.addAll(result);
            return ret;
        } catch (Exception exp) {
             LOG.error("getAllInSet({}) exception ", routingIdSet, exp);
             throw exp;
        }
    }

    @RequestMapping(value = "/forCustomer", method = RequestMethod.GET)
    public PaginationResponse<Routing> getForCustomer(@RequestParam int customerId,
            @RequestParam List<ColumnAndSort> sortBy,
            @RequestParam PaginationContext<Routing> paginationContext) {

        LOG.debug("Looking up Routings for customer {} with last returned page number {}", 
                customerId, paginationContext.getLastReturnedPageNumber());

        PaginationResponse<Routing> ret = new PaginationResponse<>();

        if (paginationContext.isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up Routings for customer {} with last returned page number {}",
                    customerId, paginationContext.getLastReturnedPageNumber());
            ret.setContext(paginationContext);
            return ret;
        }

        PaginationResponse<Routing> onePage = this.routingDatastore
                .getForCustomer(customerId,  sortBy, paginationContext);
        ret.setContext(onePage.getContext());
        ret.getItems().addAll(onePage.getItems());

        LOG.debug("Retrieved {} Routings for customer {} ", onePage.getItems().size(), 
                customerId);

        return ret;
    }
    
    /**
     * Updates Routing record
     * 
     * @param Routing
     * @return updated Routing object
     * @throws RuntimeException if Routing record does not exist or if it was modified concurrently
     */
    @RequestMapping(method=RequestMethod.PUT)
    public Routing update(@RequestBody Routing routing){
        
        LOG.debug("Updating Routing {}", routing);
        
        if (BaseJsonModel.hasUnsupportedValue(routing)) {
            LOG.error("Failed to update Routing, request contains unsupported value: {}", routing);
            throw new DsDataValidationException("Routing contains unsupported value");
        }

        Routing ret = routingDatastore.update(routing);

        LOG.debug("Updated Routing {}", ret);

        RoutingChangedEvent event = new RoutingChangedEvent(ret);
        publishEvent(event);

        return ret;
    }
    
    /**
     * Deletes Routing record
     * 
     * @param routingId
     * @return deleted Routing object
     */
    @RequestMapping(method=RequestMethod.DELETE)
    public Routing delete(@RequestParam long routingId ) {
        
        LOG.debug("Deleting Routing {}", routingId);
        
        Routing ret = routingDatastore.delete(routingId);

        LOG.debug("Deleted Routing {}", ret);
        
        RoutingRemovedEvent event = new RoutingRemovedEvent(ret);
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
