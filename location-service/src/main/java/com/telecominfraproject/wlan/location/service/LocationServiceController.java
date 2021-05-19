package com.telecominfraproject.wlan.location.service;

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
import com.telecominfraproject.wlan.core.model.pair.PairIntString;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;
import com.telecominfraproject.wlan.location.datastore.LocationDatastore;
import com.telecominfraproject.wlan.location.models.Location;
import com.telecominfraproject.wlan.location.models.events.LocationAddedEvent;
import com.telecominfraproject.wlan.location.models.events.LocationChangedApImpactingEvent;
import com.telecominfraproject.wlan.location.models.events.LocationChangedEvent;
import com.telecominfraproject.wlan.location.models.events.LocationRemovedEvent;
import com.telecominfraproject.wlan.systemevent.models.SystemEvent;

@RestController
@Transactional
@RequestMapping(value="/api/location")
public class LocationServiceController {

    private static final Logger LOG = LoggerFactory.getLogger(LocationServiceController.class);

    public static class ListOfLocations extends ArrayList<Location> {
        private static final long serialVersionUID = 3070319062835500930L;
    }

    public static class ListOfPairIntStrings extends ArrayList<PairIntString> {
        private static final long serialVersionUID = -2741465769595978625L;
    }

    @Autowired private LocationDatastore locationDatastore;
    @Autowired private CloudEventDispatcherInterface cloudEventDispatcher;

    /**
     * Creates new location record.
     * 
     * @param location object
     * @return created location object
     * @throws RuntimeException
     *             if location record already exists
     */
    @RequestMapping(method = RequestMethod.POST)
    public Location create(@RequestBody final Location location) {
        if (BaseJsonModel.hasUnsupportedValue(location)) {
            LOG.error("Failed to create location, request contains unsupported value: {}", location);
            throw new DsDataValidationException("Location contains unsupported value");
        }

        Location dbRequest = location.clone();
        long ts = System.currentTimeMillis();
        if (dbRequest.getCreatedTimestamp() == 0) {
            dbRequest.setCreatedTimestamp(ts);
        }
        dbRequest.setLastModifiedTimestamp(ts);

        Location result = locationDatastore.create(dbRequest);

        LOG.debug("Created customer {}", dbRequest);

        LocationAddedEvent event = new LocationAddedEvent(result);
        publishEvent(event);

        return result;
    }
    
    /**
     * Updates location record
     * 
     * @param location
     * @return location object
     * @throws RuntimeException
     *             if location record does not exist
     */
    @RequestMapping(method = RequestMethod.PUT)
    public Location update(@RequestBody Location location) {

        if (BaseJsonModel.hasUnsupportedValue(location)) {
            LOG.error("Failed to update location, request contains unsupported value: {}", location);
            throw new DsDataValidationException("Location contains unsupported value");
        }
        
        Location existingLocation = locationDatastore.get(location.getId());

        Location ret = locationDatastore.update(location);
        
        List<SystemEvent> events = new ArrayList<>();
        
        if (ret.needsToBeUpdatedOnDevice(existingLocation)) {
        	events.add(new LocationChangedApImpactingEvent(ret));
        }
        events.add(new LocationChangedEvent(ret));
        
        publishEvents(events);

        return ret;
    }
    
    /**
     * Removes location record
     * 
     * @param email
     * @return deleted location object
     * @throws RuntimeException
     *             if location record does not exist
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public Location delete(@RequestParam long locationId) {

    	Location location = locationDatastore.delete(locationId);

    	LocationRemovedEvent event = new LocationRemovedEvent(location);
        publishEvent(event);

        return location;
    }

    /**
     * Retrieves location record by id
     * 
     * @param locationId
     * @return location object
     * @throws RuntimeException
     *             if location record does not exist
     */
    @RequestMapping(method = RequestMethod.GET)
    public Location get(@RequestParam long locationId) {
    	Location location = locationDatastore.get(locationId);
        return location;
    }
    
    /**
     * Retrieves location record by id
     * 
     * @param locationId
     * @return location object or null if location record does not exist
     */
    @RequestMapping(value="/orNull", method = RequestMethod.GET)
    public Location getOrNull(long locationId) {
        try {
            return locationDatastore.get(locationId);
        } catch (DsEntityNotFoundException e)
        {
            LOG.debug("No location found for id {}, returning null.", locationId);
            return null;
        }
    }

    /**
     * Retrieves top-level location record for a supplied location id
     * 
     * @param locationId
     * @return location object
     * @throws RuntimeException
     *             if location record does not exist
     */
    @RequestMapping(value="/top", method = RequestMethod.GET)
    public Location getTopLevelLocation(@RequestParam long locationId) {
    	Location location = locationDatastore.getTopLevelLocation(locationId);
        return location;
    }

    @RequestMapping(value = "/allForCustomer", method = RequestMethod.GET)
    public ListOfLocations getAllForCustomer(@RequestParam int customerId) {
        LOG.debug("getAllForCustomer({})", customerId);
        try {
            List<Location> result = locationDatastore.getAllForCustomer(customerId);
            LOG.debug("getAllForCustomer({}) return {} entries", customerId, result.size());
            ListOfLocations ret = new ListOfLocations();
            ret.addAll(result);
            return ret;
        } catch (Exception exp) {
             LOG.error("getAllForCustomer({}) exception ", customerId, exp);
             throw exp;
        }
    }
    
    @RequestMapping(value = "/allDescendants", method = RequestMethod.GET)
    public ListOfLocations getAllDescendants(@RequestParam long locationId) {
        LOG.debug("getAllDescendants({})", locationId);
        try {
            List<Location> result = locationDatastore.getAllDescendants(locationId);
            LOG.debug("getAllDescendants({}) return {} entries", locationId, result.size());
            ListOfLocations ret = new ListOfLocations();
            ret.addAll(result);
            return ret;
        } catch (Exception exp) {
            LOG.error("getAllDescendants({}) exception ", locationId, exp);
            throw exp;
        }
    }
    
    @RequestMapping(value = "/allAncestors", method = RequestMethod.GET)
    public ListOfLocations getAllAncestors(@RequestParam long locationId) {
        LOG.debug("getAllAncestors({})", locationId);
        try {
            List<Location> result = locationDatastore.getAllAncestors(locationId);
            LOG.debug("getAllAncestors({}) return {} entries", locationId, result.size());
            ListOfLocations ret = new ListOfLocations();
            ret.addAll(result);
            return ret;
        } catch (Exception exp) {
            LOG.error("getAllAncestors({}) exception ", locationId, exp);
            throw exp;
        }
    }
    
    @RequestMapping(value = "/inSet", method = RequestMethod.GET)
    public ListOfLocations getAllInSet(@RequestParam Set<Long> locationIdSet) {
        LOG.debug("getAllInSet({})", locationIdSet);
        try {
            List<Location> result = locationDatastore.get(locationIdSet);
            LOG.debug("getAllInSet({}) return {} entries", locationIdSet, result.size());
            ListOfLocations ret = new ListOfLocations();
            ret.addAll(result);
            return ret;
        } catch (Exception exp) {
             LOG.error("getAllInSet({}) exception ", locationIdSet, exp);
             throw exp;
        }
    }

    @RequestMapping(value = "/forCustomer", method = RequestMethod.GET)
    public PaginationResponse<Location> getForCustomer(@RequestParam int customerId,
            @RequestParam List<ColumnAndSort> sortBy,
            @RequestParam(required = false) PaginationContext<Location> paginationContext) {

    	if(paginationContext == null) {
    		paginationContext = new PaginationContext<>();
    	}

        LOG.debug("Looking up Locations for customer {} with last returned page number {}", 
                customerId, paginationContext.getLastReturnedPageNumber());

        PaginationResponse<Location> ret = new PaginationResponse<>();

        if (paginationContext.isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up Locations for customer {} with last returned page number {}",
                    customerId, paginationContext.getLastReturnedPageNumber());
            ret.setContext(paginationContext);
            return ret;
        }

        PaginationResponse<Location> onePage = this.locationDatastore
                .getForCustomer(customerId,  sortBy, paginationContext);
        ret.setContext(onePage.getContext());
        ret.getItems().addAll(onePage.getItems());

        LOG.debug("Retrieved {} Locations for customer {} ", onePage.getItems().size(), 
                customerId);

        return ret;
    }
    
    private void publishEvent(SystemEvent event) {
        if (event == null) {
            return;
        }
        LOG.info("Publish event : {}", event);
        
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
}
