package com.telecominfraproject.wlan.location.datastore.inmemory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pagination.SortOrder;
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
    
    @Override
    public List<Location> get(Set<Long> locationIdSet) {

    	List<Location> ret = new ArrayList<>();
    	
    	if(locationIdSet!=null && !locationIdSet.isEmpty()) {	    	
	    	idToLocationMap.forEach(
	        		(id, c) -> {
	        			if(locationIdSet.contains(id)) {
				        	ret.add(c.clone());
				        } }
	        		);
    	}

        LOG.debug("Found Locations by ids {}", ret);

        return ret;
    
    }

    @Override
    public PaginationResponse<Location> getForCustomer(int customerId, 
    		final List<ColumnAndSort> sortBy, PaginationContext<Location> context) {

    	if(context == null) {
    		context = new PaginationContext<>();
    	}

        PaginationResponse<Location> ret = new PaginationResponse<>();
        ret.setContext(context.clone());

        if (ret.getContext().isLastPage()) {
            // no more pages available according to the context
            return ret;
        }

        List<Location> items = new LinkedList<>();

        // apply filters and build the full result list first - inefficient, but ok for testing
        for (Location mdl : idToLocationMap.values()) {

            if (mdl.getCustomerId() != customerId) {
                continue;
            }

            items.add(mdl);
        }

        // apply sortBy columns
        Collections.sort(items, new Comparator<Location>() {
            @Override
            public int compare(Location o1, Location o2) {
                if (sortBy == null || sortBy.isEmpty()) {
                    // sort ascending by id by default
                    return Long.compare(o1.getId(), o2.getId());
                } else {
                    int cmp;
                    for (ColumnAndSort column : sortBy) {
                        switch (column.getColumnName()) {
                        case "id":
                            cmp = Long.compare(o1.getId(), o2.getId());
                            break;
                        case "name":
                            cmp = o1.getName().compareTo(o2.getName());
                            break;
                        default:
                            // skip unknown column
                            continue;
                        }

                        if (cmp != 0) {
                            return (column.getSortOrder() == SortOrder.asc) ? cmp : (-cmp);
                        }

                    }
                }
                return 0;
            }
        });

        // now select only items for the requested page
        // find first item to add
        int fromIndex = 0;
        if (context.getStartAfterItem() != null) {
            for (Location mdl : items) {
                fromIndex++;
                if (mdl.getId() == context.getStartAfterItem().getId()) {
                    break;
                }
            }
        }

        // find last item to add
        int toIndexExclusive = fromIndex + context.getMaxItemsPerPage();
        if (toIndexExclusive > items.size()) {
            toIndexExclusive = items.size();
        }

        // copy page items into result
        List<Location> selectedItems = new ArrayList<>(context.getMaxItemsPerPage());
        for (Location mdl : items.subList(fromIndex, toIndexExclusive)) {
            selectedItems.add(mdl.clone());
        }

        ret.setItems(selectedItems);

        // adjust context for the next page
        ret.prepareForNextPage();

        if(ret.getContext().getStartAfterItem()!=null) {
        	//this datastore is only interested in the last item's id, so we'll clear all other fields on the startAfterItem in the pagination context
        	Location newStartAfterItem = new Location();
        	newStartAfterItem.setId(ret.getContext().getStartAfterItem().getId());
        	ret.getContext().setStartAfterItem(newStartAfterItem);
        }
        
        return ret;
    }
    
}
