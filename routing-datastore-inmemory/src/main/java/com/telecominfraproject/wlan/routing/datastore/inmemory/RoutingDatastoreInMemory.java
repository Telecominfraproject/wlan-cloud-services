package com.telecominfraproject.wlan.routing.datastore.inmemory;

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
import org.springframework.context.annotation.Configuration;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pagination.SortOrder;
import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;
import com.telecominfraproject.wlan.datastore.inmemory.BaseInMemoryDatastore;

import com.telecominfraproject.wlan.routing.datastore.RoutingDatastore;
import com.telecominfraproject.wlan.routing.models.Routing;

/**
 * @author dtoptygin
 *
 */
@Configuration
public class RoutingDatastoreInMemory extends BaseInMemoryDatastore implements RoutingDatastore {

    private static final Logger LOG = LoggerFactory.getLogger(RoutingDatastoreInMemory.class);

    private static final Map<Long, Routing> idToRoutingMap = new ConcurrentHashMap<Long, Routing>();
    
    private static final AtomicLong routingIdCounter = new AtomicLong();    

    @Override
    public Routing create(Routing routing) {
        
        Routing routingCopy = routing.clone();
        
        long id = routingIdCounter.incrementAndGet();
        routingCopy.setId(id);
        routingCopy.setCreatedTimestamp(System.currentTimeMillis());
        routingCopy.setLastModifiedTimestamp(routingCopy.getCreatedTimestamp());
        idToRoutingMap.put(id, routingCopy);
        
        LOG.debug("Stored Routing {}", routingCopy);
        
        return routingCopy.clone();
    }


    @Override
    public Routing get(long routingId) {
        LOG.debug("Looking up Routing for id {}", routingId);
        
        Routing routing = idToRoutingMap.get(routingId);
        
        if(routing==null){
            LOG.debug("Cannot find Routing for id {}", routingId);
            throw new DsEntityNotFoundException("Cannot find Routing for id " + routingId);
        } else {
            LOG.debug("Found Routing {}", routing);
        }

        return routing.clone();
    }

    @Override
    public Routing getOrNull(long routingId) {
        LOG.debug("Looking up Routing for id {}", routingId);
        
        Routing routing = idToRoutingMap.get(routingId);
        
        if(routing==null){
            LOG.debug("Cannot find Routing for id {}", routingId);
            return null;
        } else {
            LOG.debug("Found Routing {}", routing);
        }

        return routing.clone();
    }
    
    @Override
    public Routing update(Routing routing) {
        Routing existingRouting = get(routing.getId());
        
        if(existingRouting.getLastModifiedTimestamp()!=routing.getLastModifiedTimestamp()){
            LOG.debug("Concurrent modification detected for Routing with id {} expected version is {} but version in db was {}", 
                    routing.getId(),
                    routing.getLastModifiedTimestamp(),
                    existingRouting.getLastModifiedTimestamp()
                    );
            throw new DsConcurrentModificationException("Concurrent modification detected for Routing with id " + routing.getId()
                    +" expected version is " + routing.getLastModifiedTimestamp()
                    +" but version in db was " + existingRouting.getLastModifiedTimestamp()
                    );
            
        }
        
        Routing routingCopy = routing.clone();
        routingCopy.setLastModifiedTimestamp(getNewLastModTs(routing.getLastModifiedTimestamp()));

        idToRoutingMap.put(routingCopy.getId(), routingCopy);
        
        LOG.debug("Updated Routing {}", routingCopy);
        
        return routingCopy.clone();
    }

    @Override
    public Routing delete(long routingId) {
        Routing routing = get(routingId);
        idToRoutingMap.remove(routing.getId());
        
        LOG.debug("Deleted Routing {}", routing);
        
        return routing.clone();
    }

    @Override
    public List<Routing> get(Set<Long> routingIdSet) {

    	List<Routing> ret = new ArrayList<>();
    	
    	if(routingIdSet!=null && !routingIdSet.isEmpty()) {	    	
	    	idToRoutingMap.forEach(
	        		(id, c) -> {
	        			if(routingIdSet.contains(id)) {
				        	ret.add(c.clone());
				        } }
	        		);
    	}

        LOG.debug("Found Routings by ids {}", ret);

        return ret;
    
    }

    @Override
    public PaginationResponse<Routing> getForCustomer(int customerId, 
    		final List<ColumnAndSort> sortBy, PaginationContext<Routing> context) {

        PaginationResponse<Routing> ret = new PaginationResponse<>();
        ret.setContext(context.clone());

        if (ret.getContext().isLastPage()) {
            // no more pages available according to the context
            return ret;
        }

        List<Routing> items = new LinkedList<>();

        // apply filters and build the full result list first - inefficient, but ok for testing
        for (Routing mdl : idToRoutingMap.values()) {

            if (mdl.getCustomerId() != customerId) {
                continue;
            }

            items.add(mdl);
        }

        // apply sortBy columns
        Collections.sort(items, new Comparator<Routing>() {
            @Override
            public int compare(Routing o1, Routing o2) {
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
                        case "sampleStr":
                            cmp = o1.getSampleStr().compareTo(o2.getSampleStr());
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
            for (Routing mdl : items) {
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
        List<Routing> selectedItems = new ArrayList<>(context.getMaxItemsPerPage());
        for (Routing mdl : items.subList(fromIndex, toIndexExclusive)) {
            selectedItems.add(mdl.clone());
        }

        ret.setItems(selectedItems);

        // adjust context for the next page
        ret.prepareForNextPage();

        if(ret.getContext().getStartAfterItem()!=null) {
        	//this datastore is only interested in the last item's id, so we'll clear all other fields on the startAfterItem in the pagination context
        	Routing newStartAfterItem = new Routing();
        	newStartAfterItem.setId(ret.getContext().getStartAfterItem().getId());
        	ret.getContext().setStartAfterItem(newStartAfterItem);
        }

        return ret;
    }    
}
