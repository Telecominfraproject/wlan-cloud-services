package com.telecominfraproject.wlan.status.datastore.inmemory;

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

import com.telecominfraproject.wlan.status.datastore.StatusDatastore;
import com.telecominfraproject.wlan.status.models.Status;

/**
 * @author dtoptygin
 *
 */
@Configuration
public class StatusDatastoreInMemory extends BaseInMemoryDatastore implements StatusDatastore {

    private static final Logger LOG = LoggerFactory.getLogger(StatusDatastoreInMemory.class);

    private static final Map<Long, Status> idToStatusMap = new ConcurrentHashMap<Long, Status>();
    
    private static final AtomicLong statusIdCounter = new AtomicLong();    

    @Override
    public Status create(Status status) {
        
        Status statusCopy = status.clone();
        
        long id = statusIdCounter.incrementAndGet();
        statusCopy.setId(id);
        statusCopy.setCreatedTimestamp(System.currentTimeMillis());
        statusCopy.setLastModifiedTimestamp(statusCopy.getCreatedTimestamp());
        idToStatusMap.put(id, statusCopy);
        
        LOG.debug("Stored Status {}", statusCopy);
        
        return statusCopy.clone();
    }


    @Override
    public Status get(long statusId) {
        LOG.debug("Looking up Status for id {}", statusId);
        
        Status status = idToStatusMap.get(statusId);
        
        if(status==null){
            LOG.debug("Cannot find Status for id {}", statusId);
            throw new DsEntityNotFoundException("Cannot find Status for id " + statusId);
        } else {
            LOG.debug("Found Status {}", status);
        }

        return status.clone();
    }

    @Override
    public Status getOrNull(long statusId) {
        LOG.debug("Looking up Status for id {}", statusId);
        
        Status status = idToStatusMap.get(statusId);
        
        if(status==null){
            LOG.debug("Cannot find Status for id {}", statusId);
            return null;
        } else {
            LOG.debug("Found Status {}", status);
        }

        return status.clone();
    }
    
    @Override
    public Status update(Status status) {
        Status existingStatus = get(status.getId());
        
        if(existingStatus.getLastModifiedTimestamp()!=status.getLastModifiedTimestamp()){
            LOG.debug("Concurrent modification detected for Status with id {} expected version is {} but version in db was {}", 
                    status.getId(),
                    status.getLastModifiedTimestamp(),
                    existingStatus.getLastModifiedTimestamp()
                    );
            throw new DsConcurrentModificationException("Concurrent modification detected for Status with id " + status.getId()
                    +" expected version is " + status.getLastModifiedTimestamp()
                    +" but version in db was " + existingStatus.getLastModifiedTimestamp()
                    );
            
        }
        
        Status statusCopy = status.clone();
        statusCopy.setLastModifiedTimestamp(getNewLastModTs(status.getLastModifiedTimestamp()));

        idToStatusMap.put(statusCopy.getId(), statusCopy);
        
        LOG.debug("Updated Status {}", statusCopy);
        
        return statusCopy.clone();
    }

    @Override
    public Status delete(long statusId) {
        Status status = get(statusId);
        idToStatusMap.remove(status.getId());
        
        LOG.debug("Deleted Status {}", status);
        
        return status.clone();
    }

    @Override
    public List<Status> get(Set<Long> statusIdSet) {

    	List<Status> ret = new ArrayList<>();
    	
    	if(statusIdSet!=null && !statusIdSet.isEmpty()) {	    	
	    	idToStatusMap.forEach(
	        		(id, c) -> {
	        			if(statusIdSet.contains(id)) {
				        	ret.add(c.clone());
				        } }
	        		);
    	}

        LOG.debug("Found Statuss by ids {}", ret);

        return ret;
    
    }

    @Override
    public PaginationResponse<Status> getForCustomer(int customerId, 
    		final List<ColumnAndSort> sortBy, PaginationContext<Status> context) {

        PaginationResponse<Status> ret = new PaginationResponse<>();
        ret.setContext(context.clone());

        if (ret.getContext().isLastPage()) {
            // no more pages available according to the context
            return ret;
        }

        List<Status> items = new LinkedList<>();

        // apply filters and build the full result list first - inefficient, but ok for testing
        for (Status mdl : idToStatusMap.values()) {

            if (mdl.getCustomerId() != customerId) {
                continue;
            }

            items.add(mdl);
        }

        // apply sortBy columns
        Collections.sort(items, new Comparator<Status>() {
            @Override
            public int compare(Status o1, Status o2) {
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
            for (Status mdl : items) {
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
        List<Status> selectedItems = new ArrayList<>(context.getMaxItemsPerPage());
        for (Status mdl : items.subList(fromIndex, toIndexExclusive)) {
            selectedItems.add(mdl.clone());
        }

        ret.setItems(selectedItems);

        // adjust context for the next page
        ret.prepareForNextPage();

        if(ret.getContext().getStartAfterItem()!=null) {
        	//this datastore is only interested in the last item's id, so we'll clear all other fields on the startAfterItem in the pagination context
        	Status newStartAfterItem = new Status();
        	newStartAfterItem.setId(ret.getContext().getStartAfterItem().getId());
        	ret.getContext().setStartAfterItem(newStartAfterItem);
        }

        return ret;
    }    
}
