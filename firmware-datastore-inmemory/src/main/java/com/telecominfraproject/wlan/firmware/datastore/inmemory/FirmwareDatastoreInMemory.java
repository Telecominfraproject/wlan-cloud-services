package com.telecominfraproject.wlan.firmware.datastore.inmemory;

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

import com.telecominfraproject.wlan.firmware.datastore.FirmwareDatastore;
import com.telecominfraproject.wlan.firmware.models.Firmware;

/**
 * @author dtoptygin
 *
 */
@Configuration
public class FirmwareDatastoreInMemory extends BaseInMemoryDatastore implements FirmwareDatastore {

    private static final Logger LOG = LoggerFactory.getLogger(FirmwareDatastoreInMemory.class);

    private static final Map<Long, Firmware> idToFirmwareMap = new ConcurrentHashMap<Long, Firmware>();
    
    private static final AtomicLong firmwareIdCounter = new AtomicLong();    

    @Override
    public Firmware create(Firmware firmware) {
        
        Firmware firmwareCopy = firmware.clone();
        
        long id = firmwareIdCounter.incrementAndGet();
        firmwareCopy.setId(id);
        firmwareCopy.setCreatedTimestamp(System.currentTimeMillis());
        firmwareCopy.setLastModifiedTimestamp(firmwareCopy.getCreatedTimestamp());
        idToFirmwareMap.put(id, firmwareCopy);
        
        LOG.debug("Stored Firmware {}", firmwareCopy);
        
        return firmwareCopy.clone();
    }


    @Override
    public Firmware get(long firmwareId) {
        LOG.debug("Looking up Firmware for id {}", firmwareId);
        
        Firmware firmware = idToFirmwareMap.get(firmwareId);
        
        if(firmware==null){
            LOG.debug("Cannot find Firmware for id {}", firmwareId);
            throw new DsEntityNotFoundException("Cannot find Firmware for id " + firmwareId);
        } else {
            LOG.debug("Found Firmware {}", firmware);
        }

        return firmware.clone();
    }

    @Override
    public Firmware getOrNull(long firmwareId) {
        LOG.debug("Looking up Firmware for id {}", firmwareId);
        
        Firmware firmware = idToFirmwareMap.get(firmwareId);
        
        if(firmware==null){
            LOG.debug("Cannot find Firmware for id {}", firmwareId);
            return null;
        } else {
            LOG.debug("Found Firmware {}", firmware);
        }

        return firmware.clone();
    }
    
    @Override
    public Firmware update(Firmware firmware) {
        Firmware existingFirmware = get(firmware.getId());
        
        if(existingFirmware.getLastModifiedTimestamp()!=firmware.getLastModifiedTimestamp()){
            LOG.debug("Concurrent modification detected for Firmware with id {} expected version is {} but version in db was {}", 
                    firmware.getId(),
                    firmware.getLastModifiedTimestamp(),
                    existingFirmware.getLastModifiedTimestamp()
                    );
            throw new DsConcurrentModificationException("Concurrent modification detected for Firmware with id " + firmware.getId()
                    +" expected version is " + firmware.getLastModifiedTimestamp()
                    +" but version in db was " + existingFirmware.getLastModifiedTimestamp()
                    );
            
        }
        
        Firmware firmwareCopy = firmware.clone();
        firmwareCopy.setLastModifiedTimestamp(getNewLastModTs(firmware.getLastModifiedTimestamp()));

        idToFirmwareMap.put(firmwareCopy.getId(), firmwareCopy);
        
        LOG.debug("Updated Firmware {}", firmwareCopy);
        
        return firmwareCopy.clone();
    }

    @Override
    public Firmware delete(long firmwareId) {
        Firmware firmware = get(firmwareId);
        idToFirmwareMap.remove(firmware.getId());
        
        LOG.debug("Deleted Firmware {}", firmware);
        
        return firmware.clone();
    }

    @Override
    public List<Firmware> get(Set<Long> firmwareIdSet) {

    	List<Firmware> ret = new ArrayList<>();
    	
    	if(firmwareIdSet!=null && !firmwareIdSet.isEmpty()) {	    	
	    	idToFirmwareMap.forEach(
	        		(id, c) -> {
	        			if(firmwareIdSet.contains(id)) {
				        	ret.add(c.clone());
				        } }
	        		);
    	}

        LOG.debug("Found Firmwares by ids {}", ret);

        return ret;
    
    }

    @Override
    public PaginationResponse<Firmware> getForCustomer(int customerId, 
    		final List<ColumnAndSort> sortBy, PaginationContext<Firmware> context) {

        PaginationResponse<Firmware> ret = new PaginationResponse<>();
        ret.setContext(context.clone());

        if (ret.getContext().isLastPage()) {
            // no more pages available according to the context
            return ret;
        }

        List<Firmware> items = new LinkedList<>();

        // apply filters and build the full result list first - inefficient, but ok for testing
        for (Firmware mdl : idToFirmwareMap.values()) {

            if (mdl.getCustomerId() != customerId) {
                continue;
            }

            items.add(mdl);
        }

        // apply sortBy columns
        Collections.sort(items, new Comparator<Firmware>() {
            @Override
            public int compare(Firmware o1, Firmware o2) {
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
            for (Firmware mdl : items) {
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
        List<Firmware> selectedItems = new ArrayList<>(context.getMaxItemsPerPage());
        for (Firmware mdl : items.subList(fromIndex, toIndexExclusive)) {
            selectedItems.add(mdl.clone());
        }

        ret.setItems(selectedItems);

        // adjust context for the next page
        ret.prepareForNextPage();

        if(ret.getContext().getStartAfterItem()!=null) {
        	//this datastore is only interested in the last item's id, so we'll clear all other fields on the startAfterItem in the pagination context
        	Firmware newStartAfterItem = new Firmware();
        	newStartAfterItem.setId(ret.getContext().getStartAfterItem().getId());
        	ret.getContext().setStartAfterItem(newStartAfterItem);
        }

        return ret;
    }    
}
