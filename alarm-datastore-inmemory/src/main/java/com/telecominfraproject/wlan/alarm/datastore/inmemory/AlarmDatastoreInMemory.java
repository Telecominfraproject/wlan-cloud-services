package com.telecominfraproject.wlan.alarm.datastore.inmemory;

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

import com.telecominfraproject.wlan.alarm.datastore.AlarmDatastore;
import com.telecominfraproject.wlan.alarm.models.Alarm;

/**
 * @author dtoptygin
 *
 */
@Configuration
public class AlarmDatastoreInMemory extends BaseInMemoryDatastore implements AlarmDatastore {

    private static final Logger LOG = LoggerFactory.getLogger(AlarmDatastoreInMemory.class);

    private static final Map<Long, Alarm> idToAlarmMap = new ConcurrentHashMap<Long, Alarm>();
    
    private static final AtomicLong alarmIdCounter = new AtomicLong();    

    @Override
    public Alarm create(Alarm alarm) {
        
        Alarm alarmCopy = alarm.clone();
        
        long id = alarmIdCounter.incrementAndGet();
        alarmCopy.setId(id);
        alarmCopy.setCreatedTimestamp(System.currentTimeMillis());
        alarmCopy.setLastModifiedTimestamp(alarmCopy.getCreatedTimestamp());
        idToAlarmMap.put(id, alarmCopy);
        
        LOG.debug("Stored Alarm {}", alarmCopy);
        
        return alarmCopy.clone();
    }


    @Override
    public Alarm get(long alarmId) {
        LOG.debug("Looking up Alarm for id {}", alarmId);
        
        Alarm alarm = idToAlarmMap.get(alarmId);
        
        if(alarm==null){
            LOG.debug("Cannot find Alarm for id {}", alarmId);
            throw new DsEntityNotFoundException("Cannot find Alarm for id " + alarmId);
        } else {
            LOG.debug("Found Alarm {}", alarm);
        }

        return alarm.clone();
    }

    @Override
    public Alarm getOrNull(long alarmId) {
        LOG.debug("Looking up Alarm for id {}", alarmId);
        
        Alarm alarm = idToAlarmMap.get(alarmId);
        
        if(alarm==null){
            LOG.debug("Cannot find Alarm for id {}", alarmId);
            return null;
        } else {
            LOG.debug("Found Alarm {}", alarm);
        }

        return alarm.clone();
    }
    
    @Override
    public Alarm update(Alarm alarm) {
        Alarm existingAlarm = get(alarm.getId());
        
        if(existingAlarm.getLastModifiedTimestamp()!=alarm.getLastModifiedTimestamp()){
            LOG.debug("Concurrent modification detected for Alarm with id {} expected version is {} but version in db was {}", 
                    alarm.getId(),
                    alarm.getLastModifiedTimestamp(),
                    existingAlarm.getLastModifiedTimestamp()
                    );
            throw new DsConcurrentModificationException("Concurrent modification detected for Alarm with id " + alarm.getId()
                    +" expected version is " + alarm.getLastModifiedTimestamp()
                    +" but version in db was " + existingAlarm.getLastModifiedTimestamp()
                    );
            
        }
        
        Alarm alarmCopy = alarm.clone();
        alarmCopy.setLastModifiedTimestamp(getNewLastModTs(alarm.getLastModifiedTimestamp()));

        idToAlarmMap.put(alarmCopy.getId(), alarmCopy);
        
        LOG.debug("Updated Alarm {}", alarmCopy);
        
        return alarmCopy.clone();
    }

    @Override
    public Alarm delete(long alarmId) {
        Alarm alarm = get(alarmId);
        idToAlarmMap.remove(alarm.getId());
        
        LOG.debug("Deleted Alarm {}", alarm);
        
        return alarm.clone();
    }

    @Override
    public List<Alarm> get(Set<Long> alarmIdSet) {

    	List<Alarm> ret = new ArrayList<>();
    	
    	if(alarmIdSet!=null && !alarmIdSet.isEmpty()) {	    	
	    	idToAlarmMap.forEach(
	        		(id, c) -> {
	        			if(alarmIdSet.contains(id)) {
				        	ret.add(c.clone());
				        } }
	        		);
    	}

        LOG.debug("Found Alarms by ids {}", ret);

        return ret;
    
    }

    @Override
    public PaginationResponse<Alarm> getForCustomer(int customerId, 
    		final List<ColumnAndSort> sortBy, PaginationContext<Alarm> context) {

        PaginationResponse<Alarm> ret = new PaginationResponse<>();
        ret.setContext(context.clone());

        if (ret.getContext().isLastPage()) {
            // no more pages available according to the context
            return ret;
        }

        List<Alarm> items = new LinkedList<>();

        // apply filters and build the full result list first - inefficient, but ok for testing
        for (Alarm mdl : idToAlarmMap.values()) {

            if (mdl.getCustomerId() != customerId) {
                continue;
            }

            items.add(mdl);
        }

        // apply sortBy columns
        Collections.sort(items, new Comparator<Alarm>() {
            @Override
            public int compare(Alarm o1, Alarm o2) {
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
            for (Alarm mdl : items) {
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
        List<Alarm> selectedItems = new ArrayList<>(context.getMaxItemsPerPage());
        for (Alarm mdl : items.subList(fromIndex, toIndexExclusive)) {
            selectedItems.add(mdl.clone());
        }

        ret.setItems(selectedItems);

        // adjust context for the next page
        ret.prepareForNextPage();

        if(ret.getContext().getStartAfterItem()!=null) {
        	//this datastore is only interested in the last item's id, so we'll clear all other fields on the startAfterItem in the pagination context
        	Alarm newStartAfterItem = new Alarm();
        	newStartAfterItem.setId(ret.getContext().getStartAfterItem().getId());
        	ret.getContext().setStartAfterItem(newStartAfterItem);
        }

        return ret;
    }    
}
