package com.telecominfraproject.wlan.alarm.datastore.inmemory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import com.telecominfraproject.wlan.alarm.datastore.AlarmDatastore;
import com.telecominfraproject.wlan.alarm.models.Alarm;
import com.telecominfraproject.wlan.alarm.models.AlarmCode;
import com.telecominfraproject.wlan.alarm.models.AlarmCounts;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pagination.SortOrder;
import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;
import com.telecominfraproject.wlan.datastore.inmemory.BaseInMemoryDatastore;

/**
 * @author dtoptygin
 *
 */
@Configuration
public class AlarmDatastoreInMemory extends BaseInMemoryDatastore implements AlarmDatastore {

    private static final Logger LOG = LoggerFactory.getLogger(AlarmDatastoreInMemory.class);

    private static final Map<AlarmKey, Alarm> idToAlarmMap = new ConcurrentHashMap<>();
    
    private static class AlarmKey {
        public int customerId;
        public long equipmentId;
        public AlarmCode alarmCode;
        public long createdTimestamp;
        
        public AlarmKey(int customerId, long equipmentId, AlarmCode alarmCode, long createdTimestamp) {
        	this.customerId = customerId;
        	this.equipmentId = equipmentId;
        	this.alarmCode = alarmCode;
        	this.createdTimestamp = createdTimestamp;
		}

        public AlarmKey(Alarm alarm) {
        	this.customerId = alarm.getCustomerId();
        	this.equipmentId = alarm.getEquipmentId();
        	this.alarmCode = alarm.getAlarmCode();
        	this.createdTimestamp = alarm.getCreatedTimestamp();
		}

		@Override
		public int hashCode() {
			return Objects.hash(alarmCode, createdTimestamp, customerId, equipmentId);
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (!(obj instanceof AlarmKey)) {
				return false;
			}
			AlarmKey other = (AlarmKey) obj;
			return alarmCode == other.alarmCode && createdTimestamp == other.createdTimestamp
					&& customerId == other.customerId && equipmentId == other.equipmentId;
		}
        
    }
    
    @Override
    public Alarm create(Alarm alarm) {
        
        Alarm alarmCopy = alarm.clone();
        
        if(alarmCopy.getCreatedTimestamp()<=0) {
        	alarmCopy.setCreatedTimestamp(System.currentTimeMillis());
        }
        alarmCopy.setLastModifiedTimestamp(alarmCopy.getCreatedTimestamp());
        idToAlarmMap.put(new AlarmKey(alarmCopy), alarmCopy);
        
        LOG.debug("Stored Alarm {}", alarmCopy);
        
        return alarmCopy.clone();
    }


    public Alarm get(int customerId, long equipmentId, AlarmCode alarmCode, long createdTimestamp) {
        LOG.debug("Looking up Alarm for id {} {} {} {}", customerId, equipmentId, alarmCode, createdTimestamp);
        
        Alarm alarm = idToAlarmMap.get(new AlarmKey(customerId, equipmentId, alarmCode, createdTimestamp));
        
        if(alarm==null){
            LOG.debug("Cannot find Alarm for id {} {} {} {}", customerId, equipmentId, alarmCode, createdTimestamp);
            throw new DsEntityNotFoundException("Cannot find Alarm for id " + customerId + " " + equipmentId + " " + alarmCode + " " + createdTimestamp);
        } else {
            LOG.debug("Found Alarm {}", alarm);
        }

        return alarm.clone();
    }

    @Override
    public Alarm getOrNull(int customerId, long equipmentId, AlarmCode alarmCode, long createdTimestamp) {
        LOG.debug("Looking up Alarm for id {} {} {} {}", customerId, equipmentId, alarmCode, createdTimestamp);
        
        Alarm alarm = idToAlarmMap.get(new AlarmKey(customerId, equipmentId, alarmCode, createdTimestamp));
        
        if(alarm==null){
            LOG.debug("Cannot find Alarm for id {} {} {} {}", customerId, equipmentId, alarmCode, createdTimestamp);
            return null;
        } else {
            LOG.debug("Found Alarm {}", alarm);
        }

        return alarm.clone();
    }
    
    @Override
    public Alarm update(Alarm alarm) {
        Alarm existingAlarm = get(alarm.getCustomerId(), alarm.getEquipmentId(), alarm.getAlarmCode(), alarm.getCreatedTimestamp());
        
        if(existingAlarm.getLastModifiedTimestamp()!=alarm.getLastModifiedTimestamp()){
            LOG.debug("Concurrent modification detected for Alarm with id {} {} {} {} expected version is {} but version in db was {}", 
            		alarm.getCustomerId(), alarm.getEquipmentId(), alarm.getAlarmCode(), alarm.getCreatedTimestamp(),
                    alarm.getLastModifiedTimestamp(),
                    existingAlarm.getLastModifiedTimestamp()
                    );
            throw new DsConcurrentModificationException("Concurrent modification detected for Alarm with id "  
            		+ alarm.getCustomerId() + " " + alarm.getEquipmentId() + " " + alarm.getAlarmCode() + " " + alarm.getCreatedTimestamp()
                    +" expected version is " + alarm.getLastModifiedTimestamp()
                    +" but version in db was " + existingAlarm.getLastModifiedTimestamp()
                    );
            
        }
        
        Alarm alarmCopy = alarm.clone();
        alarmCopy.setLastModifiedTimestamp(getNewLastModTs(alarm.getLastModifiedTimestamp()));

        idToAlarmMap.put(new AlarmKey(alarmCopy), alarmCopy);
        
        LOG.debug("Updated Alarm {}", alarmCopy);
        
        return alarmCopy.clone();
    }

    @Override
    public Alarm delete(int customerId, long equipmentId, AlarmCode alarmCode, long createdTimestamp) {
        Alarm alarm = get(customerId, equipmentId, alarmCode, createdTimestamp);
        idToAlarmMap.remove(new AlarmKey(alarm));
        
        LOG.debug("Deleted Alarm {}", alarm);
        
        return alarm.clone();
    }

    @Override
    public List<Alarm> delete(int customerId, long equipmentId) {
        List<Alarm> ret = new ArrayList<>();
        idToAlarmMap.values().forEach(a -> {
        	if(a.getCustomerId() == customerId && a.getEquipmentId() == equipmentId) {
        		ret.add(a.clone());
        	}
        });
        
        ret.forEach(a -> idToAlarmMap.remove(new AlarmKey(a)));
        
        LOG.debug("Deleted Alarms {}", ret);
        
        return ret;
    }

    @Override
    public List<Alarm> get(int customerId, Set<Long> equipmentIdSet, Set<AlarmCode> alarmCodeSet, long createdAfterTimestamp) {
    	
    	if(equipmentIdSet == null || equipmentIdSet.isEmpty()) {
    		throw new IllegalArgumentException("equipmentIdSet must be provided");
    	}

    	List<Alarm> ret = new ArrayList<>();
    	
        idToAlarmMap.values().forEach(a -> {
        	if(a.getCustomerId() == customerId && equipmentIdSet.contains(a.getEquipmentId())
        			&& (alarmCodeSet ==null || alarmCodeSet.isEmpty() || alarmCodeSet.contains(a.getAlarmCode()))
        			&& a.getCreatedTimestamp() > createdAfterTimestamp
        			) {
        		ret.add(a.clone());
        	}
        });

        LOG.debug("Found Alarms {}", ret);

        return ret;
    
    }

    @Override
	public PaginationResponse<Alarm> getForCustomer(int customerId, Set<Long> equipmentIdSet, Set<AlarmCode> alarmCodeSet,
			long createdAfterTimestamp, List<ColumnAndSort> sortBy, PaginationContext<Alarm> context)  {

    	if(context == null) {
    		context = new PaginationContext<>();
    	}
    	
        PaginationResponse<Alarm> ret = new PaginationResponse<>();
        ret.setContext(context.clone());

        if (ret.getContext().isLastPage()) {
            // no more pages available according to the context
            return ret;
        }

        List<Alarm> items = new LinkedList<>();

        // apply filters and build the full result list first - inefficient, but ok for testing
        idToAlarmMap.values().forEach(a -> {
        	if(a.getCustomerId() == customerId 
        			&& ( equipmentIdSet == null || equipmentIdSet.isEmpty() || equipmentIdSet.contains(a.getEquipmentId()) )
        			&& ( alarmCodeSet ==null || alarmCodeSet.isEmpty() || alarmCodeSet.contains(a.getAlarmCode()) )
        			&& a.getCreatedTimestamp() > createdAfterTimestamp
        			) {
        		items.add(a.clone());
        	}
        });


        // apply sortBy columns
        Collections.sort(items, new Comparator<Alarm>() {
            @Override
            public int compare(Alarm o1, Alarm o2) {
                int cmp;
                if (sortBy == null || sortBy.isEmpty()) {
                    // sort ascending by equipmentId and createdTimestamp by default
                    cmp = Long.compare(o1.getEquipmentId(), o2.getEquipmentId());
                    if(cmp == 0 ) {
                    	cmp = Long.compare(o1.getCreatedTimestamp(), o2.getCreatedTimestamp());
                    }
                    
                    return cmp;
                    
                } else {
                    for (ColumnAndSort column : sortBy) {
                        switch (column.getColumnName()) {
                        case "customerId":
                            cmp = Long.compare(o1.getCustomerId(), o2.getCustomerId());
                            break;
                        case "equipmentId":
                            cmp = Long.compare(o1.getEquipmentId(), o2.getEquipmentId());
                            break;
                        case "createdTimestamp":
                            cmp = Long.compare(o1.getCreatedTimestamp(), o2.getCreatedTimestamp());
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
        	Alarm startAfter = context.getStartAfterItem(); 
            for (Alarm mdl : items) {
                fromIndex++;
                if ( mdl.getCustomerId() == startAfter.getCustomerId()
                		&& mdl.getEquipmentId() == startAfter.getEquipmentId()
                		&& mdl.getAlarmCode() == startAfter.getAlarmCode()
                		&& mdl.getCreatedTimestamp() == startAfter.getCreatedTimestamp() ) {
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
        	newStartAfterItem.setCustomerId(ret.getContext().getStartAfterItem().getCustomerId());
        	newStartAfterItem.setEquipmentId(ret.getContext().getStartAfterItem().getEquipmentId());
        	newStartAfterItem.setAlarmCode(ret.getContext().getStartAfterItem().getAlarmCode());
        	newStartAfterItem.setCreatedTimestamp(ret.getContext().getStartAfterItem().getCreatedTimestamp());

        	ret.getContext().setStartAfterItem(newStartAfterItem);
        }

        return ret;
    }
    
    @Override
    public AlarmCounts getAlarmCounts(int customerId, Set<Long> equipmentIdSet, Set<AlarmCode> alarmCodeSet) {
    	
    	AlarmCounts alarmCounts = new AlarmCounts();
    	alarmCounts.setCustomerId(customerId);
    	
        idToAlarmMap.values().forEach(a -> {
        	if(a.getCustomerId() == customerId) {
        		if( alarmCodeSet ==null || alarmCodeSet.isEmpty() || alarmCodeSet.contains(a.getAlarmCode()) ) {
		        	if( equipmentIdSet != null && !equipmentIdSet.isEmpty() && equipmentIdSet.contains(a.getEquipmentId()) ) {
		            	alarmCounts.addToCounter(a.getEquipmentId(), a.getAlarmCode(), 1);
		        	} else if( equipmentIdSet == null || equipmentIdSet.isEmpty()) {		        		 
		        		alarmCounts.addToCounter(0, a.getAlarmCode(), 1);		        		
		        	}
        		}
	        }
        });

        return alarmCounts;
    }
}
