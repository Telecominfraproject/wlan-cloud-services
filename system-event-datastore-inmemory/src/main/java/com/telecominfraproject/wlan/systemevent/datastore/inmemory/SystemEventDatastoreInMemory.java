package com.telecominfraproject.wlan.systemevent.datastore.inmemory;

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

import com.telecominfraproject.wlan.systemevent.datastore.SystemEventDatastore;
import com.telecominfraproject.wlan.systemevent.models.SystemEventContainer;

/**
 * @author dtoptygin
 *
 */
@Configuration
public class SystemEventDatastoreInMemory extends BaseInMemoryDatastore implements SystemEventDatastore {

    private static final Logger LOG = LoggerFactory.getLogger(SystemEventDatastoreInMemory.class);

    private static final Map<Long, SystemEventContainer> idToSystemEventRecordMap = new ConcurrentHashMap<Long, SystemEventContainer>();
    
    private static final AtomicLong systemEventRecordIdCounter = new AtomicLong();    

    @Override
    public SystemEventContainer create(SystemEventContainer systemEventRecord) {
        
        SystemEventContainer systemEventRecordCopy = systemEventRecord.clone();
        
        long id = systemEventRecordIdCounter.incrementAndGet();
        systemEventRecordCopy.setId(id);
        systemEventRecordCopy.setCreatedTimestamp(System.currentTimeMillis());
        systemEventRecordCopy.setLastModifiedTimestamp(systemEventRecordCopy.getCreatedTimestamp());
        idToSystemEventRecordMap.put(id, systemEventRecordCopy);
        
        LOG.debug("Stored SystemEventContainer {}", systemEventRecordCopy);
        
        return systemEventRecordCopy.clone();
    }


    @Override
    public SystemEventContainer get(long systemEventRecordId) {
        LOG.debug("Looking up SystemEventContainer for id {}", systemEventRecordId);
        
        SystemEventContainer systemEventRecord = idToSystemEventRecordMap.get(systemEventRecordId);
        
        if(systemEventRecord==null){
            LOG.debug("Cannot find SystemEventContainer for id {}", systemEventRecordId);
            throw new DsEntityNotFoundException("Cannot find SystemEventContainer for id " + systemEventRecordId);
        } else {
            LOG.debug("Found SystemEventContainer {}", systemEventRecord);
        }

        return systemEventRecord.clone();
    }

    @Override
    public SystemEventContainer getOrNull(long systemEventRecordId) {
        LOG.debug("Looking up SystemEventContainer for id {}", systemEventRecordId);
        
        SystemEventContainer systemEventRecord = idToSystemEventRecordMap.get(systemEventRecordId);
        
        if(systemEventRecord==null){
            LOG.debug("Cannot find SystemEventContainer for id {}", systemEventRecordId);
            return null;
        } else {
            LOG.debug("Found SystemEventContainer {}", systemEventRecord);
        }

        return systemEventRecord.clone();
    }
    
    @Override
    public SystemEventContainer update(SystemEventContainer systemEventRecord) {
        SystemEventContainer existingSystemEventRecord = get(systemEventRecord.getId());
        
        if(existingSystemEventRecord.getLastModifiedTimestamp()!=systemEventRecord.getLastModifiedTimestamp()){
            LOG.debug("Concurrent modification detected for SystemEventContainer with id {} expected version is {} but version in db was {}", 
                    systemEventRecord.getId(),
                    systemEventRecord.getLastModifiedTimestamp(),
                    existingSystemEventRecord.getLastModifiedTimestamp()
                    );
            throw new DsConcurrentModificationException("Concurrent modification detected for SystemEventContainer with id " + systemEventRecord.getId()
                    +" expected version is " + systemEventRecord.getLastModifiedTimestamp()
                    +" but version in db was " + existingSystemEventRecord.getLastModifiedTimestamp()
                    );
            
        }
        
        SystemEventContainer systemEventRecordCopy = systemEventRecord.clone();
        systemEventRecordCopy.setLastModifiedTimestamp(getNewLastModTs(systemEventRecord.getLastModifiedTimestamp()));

        idToSystemEventRecordMap.put(systemEventRecordCopy.getId(), systemEventRecordCopy);
        
        LOG.debug("Updated SystemEventContainer {}", systemEventRecordCopy);
        
        return systemEventRecordCopy.clone();
    }

    @Override
    public SystemEventContainer delete(long systemEventRecordId) {
        SystemEventContainer systemEventRecord = get(systemEventRecordId);
        idToSystemEventRecordMap.remove(systemEventRecord.getId());
        
        LOG.debug("Deleted SystemEventContainer {}", systemEventRecord);
        
        return systemEventRecord.clone();
    }

    @Override
    public List<SystemEventContainer> get(Set<Long> systemEventRecordIdSet) {

    	List<SystemEventContainer> ret = new ArrayList<>();
    	
    	if(systemEventRecordIdSet!=null && !systemEventRecordIdSet.isEmpty()) {	    	
	    	idToSystemEventRecordMap.forEach(
	        		(id, c) -> {
	        			if(systemEventRecordIdSet.contains(id)) {
				        	ret.add(c.clone());
				        } }
	        		);
    	}

        LOG.debug("Found SystemEventRecords by ids {}", ret);

        return ret;
    
    }

    @Override
    public PaginationResponse<SystemEventContainer> getForCustomer(int customerId, 
    		final List<ColumnAndSort> sortBy, PaginationContext<SystemEventContainer> context) {

    	if(context == null) {
    		context = new PaginationContext<>();
    	}

        PaginationResponse<SystemEventContainer> ret = new PaginationResponse<>();
        ret.setContext(context.clone());

        if (ret.getContext().isLastPage()) {
            // no more pages available according to the context
            return ret;
        }

        List<SystemEventContainer> items = new LinkedList<>();

        // apply filters and build the full result list first - inefficient, but ok for testing
        for (SystemEventContainer mdl : idToSystemEventRecordMap.values()) {

            if (mdl.getCustomerId() != customerId) {
                continue;
            }

            items.add(mdl);
        }

        // apply sortBy columns
        Collections.sort(items, new Comparator<SystemEventContainer>() {
            @Override
            public int compare(SystemEventContainer o1, SystemEventContainer o2) {
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
            for (SystemEventContainer mdl : items) {
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
        List<SystemEventContainer> selectedItems = new ArrayList<>(context.getMaxItemsPerPage());
        for (SystemEventContainer mdl : items.subList(fromIndex, toIndexExclusive)) {
            selectedItems.add(mdl.clone());
        }

        ret.setItems(selectedItems);

        // adjust context for the next page
        ret.prepareForNextPage();

        if(ret.getContext().getStartAfterItem()!=null) {
        	//this datastore is only interested in the last item's id, so we'll clear all other fields on the startAfterItem in the pagination context
        	SystemEventContainer newStartAfterItem = new SystemEventContainer();
        	newStartAfterItem.setId(ret.getContext().getStartAfterItem().getId());
        	ret.getContext().setStartAfterItem(newStartAfterItem);
        }

        return ret;
    }    
}
