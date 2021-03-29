package com.telecominfraproject.wlan.systemevent.datastore.inmemory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pagination.SortOrder;
import com.telecominfraproject.wlan.datastore.inmemory.BaseInMemoryDatastore;
import com.telecominfraproject.wlan.systemevent.datastore.SystemEventDatastore;
import com.telecominfraproject.wlan.systemevent.models.SystemEventRecord;
import com.telecominfraproject.wlan.systemevent.models.SystemEventRecordKey;

/**
 * @author dtoptygin
 *
 */
@Configuration
public class SystemEventDatastoreInMemory extends BaseInMemoryDatastore implements SystemEventDatastore {

    private static final Logger LOG = LoggerFactory.getLogger(SystemEventDatastoreInMemory.class);

    private static final Map<SystemEventRecordKey, SystemEventRecord> idToSystemEventRecordMap = new ConcurrentHashMap<SystemEventRecordKey, SystemEventRecord>();    
    
    @Override
    public void create(SystemEventRecord systemEvent) {
        
        SystemEventRecord systemEventCopy = systemEvent.clone();
        
        if(systemEventCopy.getEventTimestamp()==0) {
        	systemEventCopy.setEventTimestamp(System.currentTimeMillis());
        }
        
        idToSystemEventRecordMap.put(new SystemEventRecordKey(systemEventCopy), systemEventCopy);
        
        LOG.debug("Stored SystemEventRecord {}", systemEventCopy);
        
    }

    @Override
    public void create(List<SystemEventRecord> systemEvents) {
    	if(systemEvents==null || systemEvents.isEmpty()) {
    		return;
    	}
    	
    	systemEvents.forEach(m -> create(m));
    	
    }
    

    @Override
    public void delete(int customerId, long equipmentId, long createdBeforeTimestamp) {
    	List<SystemEventRecordKey> keysToRemove = new ArrayList<>();
    	
        idToSystemEventRecordMap.keySet().forEach( k -> {
        	if(k.getCustomerId() == customerId && k.getEquipmentId() == equipmentId && k.getEventTimestamp() < createdBeforeTimestamp) {
        		keysToRemove.add(k);
        	}
        });
        
        keysToRemove.forEach(k -> idToSystemEventRecordMap.remove(k) );
        
        LOG.debug("Deleted SystemEventRecord s for customer {} equipment {} createdBefore {}", customerId, equipmentId, createdBeforeTimestamp);
    }

    @Override
    public void delete(long createdBeforeTimestamp) {
    	List<SystemEventRecordKey> keysToRemove = new ArrayList<>();
    	
        idToSystemEventRecordMap.keySet().forEach( k -> {
        	if(k.getEventTimestamp() < createdBeforeTimestamp) {
        		keysToRemove.add(k);
        	}
        });
        
        keysToRemove.forEach(k -> idToSystemEventRecordMap.remove(k) );
        
        LOG.debug("Deleted {} SystemEventRecords createdBefore {}", keysToRemove.size(), createdBeforeTimestamp);
    }

    @Override
    public PaginationResponse<SystemEventRecord> getForCustomer(long fromTime, long toTime, int customerId,
            Set<Long> locationIds, Set<Long> equipmentIds, Set<MacAddress> clientMacAddresses, Set<String> dataTypes,
    		List<ColumnAndSort> sortBy, PaginationContext<SystemEventRecord> context) {

    	if(context == null) {
    		context = new PaginationContext<>();
    	}
    	
        PaginationResponse<SystemEventRecord> ret = new PaginationResponse<>();
        ret.setContext(context.clone());

        if (ret.getContext().isLastPage()) {
            // no more pages available according to the context
            return ret;
        }

        List<SystemEventRecord> items = new LinkedList<>();

        // apply filters and build the full result list first - inefficient, but ok for testing
        for (SystemEventRecord mdl : idToSystemEventRecordMap.values()) {

            if (mdl.getCustomerId() == customerId &&
                    (locationIds==null || locationIds.isEmpty() || locationIds.contains(mdl.getLocationId())) &&
                    (equipmentIds==null || equipmentIds.isEmpty() || equipmentIds.contains(mdl.getEquipmentId())) &&
                    (clientMacAddresses==null || clientMacAddresses.isEmpty() ||
                    	clientMacAddressesContains(clientMacAddresses, mdl.getClientMacAddress())) &&
            		(dataTypes==null || dataTypes.isEmpty() || dataTypes.contains(mdl.getDataType())) &&
            		mdl.getEventTimestamp() >= fromTime &&
            		mdl.getEventTimestamp() <= toTime 
            	) {
            	items.add(mdl);
            }
        }

        // apply sortBy columns
        Collections.sort(items, new Comparator<SystemEventRecord>() {
            @Override
            public int compare(SystemEventRecord o1, SystemEventRecord o2) {
                if (sortBy == null || sortBy.isEmpty()) {
                    // sort ascending by event time stamp by default
                    return Long.compare(o1.getEventTimestamp(), o2.getEventTimestamp());
                } else {
                    int cmp;
                    for (ColumnAndSort column : sortBy) {
                        switch (column.getColumnName()) {
                        case "eventTimestamp":
                            cmp = Long.compare(o1.getEventTimestamp(), o2.getEventTimestamp());
                            break;
                        case "locationId":
                            cmp = Long.compare(o1.getLocationId(), o2.getLocationId());
                            break;
                        case "equipmentId":
                            cmp = Long.compare(o1.getEquipmentId(), o2.getEquipmentId());
                            break;
                        case "clientMac":
                            cmp = Long.compare(o1.getClientMac(), o2.getClientMac());
                            break;
                        case "dataType":
                            cmp = o1.getDataType().compareTo(o2.getDataType());
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
        	SystemEventRecordKey startAfterKey = new SystemEventRecordKey(context.getStartAfterItem());
            for (SystemEventRecord mdl : items) {
                fromIndex++;
                if (new SystemEventRecordKey(mdl).equals(startAfterKey)) {
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
        List<SystemEventRecord> selectedItems = new ArrayList<>(context.getMaxItemsPerPage());
        for (SystemEventRecord mdl : items.subList(fromIndex, toIndexExclusive)) {
            selectedItems.add(mdl.clone());
        }

        ret.setItems(selectedItems);

        // adjust context for the next page
        ret.prepareForNextPage();

        if(ret.getContext().getStartAfterItem()!=null) {
        	//this datastore is only interested in the last item's id, so we'll clear all other fields on the startAfterItem in the pagination context
        	SystemEventRecord newStartAfterItem = new SystemEventRecord();
        	SystemEventRecord oldStartAfterItem = ret.getContext().getStartAfterItem();
        	
        	newStartAfterItem.setCustomerId(oldStartAfterItem.getCustomerId());
        	newStartAfterItem.setLocationId(oldStartAfterItem.getLocationId());        	
        	newStartAfterItem.setEquipmentId(oldStartAfterItem.getEquipmentId());
        	newStartAfterItem.setClientMac(oldStartAfterItem.getClientMac());
        	newStartAfterItem.setDataType(oldStartAfterItem.getDataType());
        	newStartAfterItem.setEventTimestamp(oldStartAfterItem.getEventTimestamp());
        	
        	ret.getContext().setStartAfterItem(newStartAfterItem);
        }

        return ret;
    }
    
    private boolean clientMacAddressesContains(Set<MacAddress> clientMacAddresses, MacAddress macAddress) {
        if (clientMacAddresses == null) {
            return false;
        }
        if (macAddress == null) {
            for (MacAddress clientMacAddress : clientMacAddresses) {
                if (clientMacAddress.getAddressAsLong() == 0) {
    	            return true;
                }
            }
        }
        return clientMacAddresses.contains(macAddress);
    }

}
