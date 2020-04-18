package com.telecominfraproject.wlan.location.datastore.inmemory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;
import com.telecominfraproject.wlan.datastore.inmemory.BaseInMemoryDatastore;
import com.telecominfraproject.wlan.location.datastore.LocationDatastore;
import com.telecominfraproject.wlan.location.models.Location;

@Component
public class LocationDatastoreInMemory extends BaseInMemoryDatastore implements LocationDatastore {

    private static final Logger LOG = LoggerFactory.getLogger(LocationDatastoreInMemory.class);

    private final Map<Long, Location> idToLocationMap = new ConcurrentHashMap<>();
    
    private static final AtomicLong locationIdCounter = new AtomicLong(1);

    @Override
    public Location create(Location location) {
        Location locationCopy = location.clone();

        long id = locationIdCounter.incrementAndGet();
        locationCopy.setId(id);
        idToLocationMap.put(id, locationCopy);

        LOG.debug("Stored {}", locationCopy);

        return locationCopy.clone();
    }
    
    @Override
    public Location get(long locationId) {
        LOG.debug("Looking up Location {}", locationId);

        Location location = idToLocationMap.get(locationId);

        if (location == null) {
            LOG.debug("Cannot find Location {}", locationId);
            throw new DsEntityNotFoundException(
                    "Cannot find Location " + locationId);
        } else {
            LOG.debug("Found {}", location);
        }

        return location.clone();
    }
    
    @Override
    public Location update(Location location) {
        Location locationCopy = location.clone();

        Location existingLocation = get(locationCopy.getId());

        // Make sure there is a location record to update.
        if (existingLocation == null) {
            LOG.warn("Unable to update location {} as it is not in the datastore",
                    locationCopy.getId());
            return null;
        }

        // Check for concurrent modification
        if (existingLocation.getLastModifiedTimestamp() != location.getLastModifiedTimestamp()) {
            LOG.debug(
                    "Concurrent modification detected for Location {} expected version is {} but version in db was {}",
                    locationCopy.getId(), locationCopy.getLastModifiedTimestamp(),
                    existingLocation.getLastModifiedTimestamp());
            throw new DsConcurrentModificationException(
                    "Concurrent modification detected for Location "
                            + locationCopy.getId() + " expected version is "
                            + location.getLastModifiedTimestamp() + " but version in db was "
                            + existingLocation.getLastModifiedTimestamp());

        }

        // Change is acceptable, so make it.
        Location existingLocationCopy = existingLocation.clone();
        existingLocationCopy.setCustomerId(locationCopy.getCustomerId());
        existingLocationCopy.setName(locationCopy.getName());
        existingLocationCopy.setLocationType(locationCopy.getLocationType());
        existingLocationCopy.setDetails(locationCopy.getDetails()!=null?locationCopy.getDetails().clone():null);
        existingLocationCopy
                .setLastModifiedTimestamp(getNewLastModTs(existingLocation.getLastModifiedTimestamp()));
        
        idToLocationMap.put(existingLocationCopy.getId(), existingLocationCopy);

        LOG.debug("Updated {}", existingLocationCopy);

        return existingLocationCopy.clone();
    }
    
    @Override
    public Location delete(long locationId) {
        Location location = get(locationId);
        idToLocationMap.remove(location.getId());

        LOG.debug("Deleted Location {}", location);

        return location.clone();
    }
    
    @Override
    public List<Location> getAllForCustomer(int customerId) {
		List<Location> ret = new ArrayList<>();
		idToLocationMap.values().forEach(loc -> {
			if (loc.getCustomerId() == customerId) {
				ret.add(loc.clone());
			}
		});
		
		return ret;
    }
    
    @Override
    public Location getTopLevelLocation(long locationId) {       
        Location location = idToLocationMap.get(locationId);

        while (location!=null && location.getParentId() != 0) {
        	location = idToLocationMap.get(location.getParentId());
        }
        
        return location;
    }

    @Override
    public List<Location> getAllDescendants(long locationParentId) {
    	List<Location> descendents = new ArrayList<>();
    	getAllDescendants(locationParentId, descendents);
    	
    	return descendents;
    }
    
    private void getAllDescendants(long locationParentId, List<Location> collectedChildren) {

    	idToLocationMap.values().forEach(loc -> {
			if (loc.getParentId() == locationParentId) {
				collectedChildren.add(loc.clone());
				getAllDescendants(loc.getId(), collectedChildren);
			}
		});

    }
}
