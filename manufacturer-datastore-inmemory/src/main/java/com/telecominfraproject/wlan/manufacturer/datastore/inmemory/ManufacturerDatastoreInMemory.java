package com.telecominfraproject.wlan.manufacturer.datastore.inmemory;

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

import com.telecominfraproject.wlan.manufacturer.datastore.ManufacturerDatastore;
import com.telecominfraproject.wlan.manufacturer.models.Manufacturer;

/**
 * @author dtoptygin
 *
 */
@Configuration
public class ManufacturerDatastoreInMemory extends BaseInMemoryDatastore implements ManufacturerDatastore {

    private static final Logger LOG = LoggerFactory.getLogger(ManufacturerDatastoreInMemory.class);

    private static final Map<Long, Manufacturer> idToManufacturerMap = new ConcurrentHashMap<Long, Manufacturer>();
    
    private static final AtomicLong manufacturerIdCounter = new AtomicLong();    

    @Override
    public Manufacturer create(Manufacturer manufacturer) {
        
        Manufacturer manufacturerCopy = manufacturer.clone();
        
        long id = manufacturerIdCounter.incrementAndGet();
        manufacturerCopy.setId(id);
        manufacturerCopy.setCreatedTimestamp(System.currentTimeMillis());
        manufacturerCopy.setLastModifiedTimestamp(manufacturerCopy.getCreatedTimestamp());
        idToManufacturerMap.put(id, manufacturerCopy);
        
        LOG.debug("Stored Manufacturer {}", manufacturerCopy);
        
        return manufacturerCopy.clone();
    }


    @Override
    public Manufacturer get(long manufacturerId) {
        LOG.debug("Looking up Manufacturer for id {}", manufacturerId);
        
        Manufacturer manufacturer = idToManufacturerMap.get(manufacturerId);
        
        if(manufacturer==null){
            LOG.debug("Cannot find Manufacturer for id {}", manufacturerId);
            throw new DsEntityNotFoundException("Cannot find Manufacturer for id " + manufacturerId);
        } else {
            LOG.debug("Found Manufacturer {}", manufacturer);
        }

        return manufacturer.clone();
    }

    @Override
    public Manufacturer getOrNull(long manufacturerId) {
        LOG.debug("Looking up Manufacturer for id {}", manufacturerId);
        
        Manufacturer manufacturer = idToManufacturerMap.get(manufacturerId);
        
        if(manufacturer==null){
            LOG.debug("Cannot find Manufacturer for id {}", manufacturerId);
            return null;
        } else {
            LOG.debug("Found Manufacturer {}", manufacturer);
        }

        return manufacturer.clone();
    }
    
    @Override
    public Manufacturer update(Manufacturer manufacturer) {
        Manufacturer existingManufacturer = get(manufacturer.getId());
        
        if(existingManufacturer.getLastModifiedTimestamp()!=manufacturer.getLastModifiedTimestamp()){
            LOG.debug("Concurrent modification detected for Manufacturer with id {} expected version is {} but version in db was {}", 
                    manufacturer.getId(),
                    manufacturer.getLastModifiedTimestamp(),
                    existingManufacturer.getLastModifiedTimestamp()
                    );
            throw new DsConcurrentModificationException("Concurrent modification detected for Manufacturer with id " + manufacturer.getId()
                    +" expected version is " + manufacturer.getLastModifiedTimestamp()
                    +" but version in db was " + existingManufacturer.getLastModifiedTimestamp()
                    );
            
        }
        
        Manufacturer manufacturerCopy = manufacturer.clone();
        manufacturerCopy.setLastModifiedTimestamp(getNewLastModTs(manufacturer.getLastModifiedTimestamp()));

        idToManufacturerMap.put(manufacturerCopy.getId(), manufacturerCopy);
        
        LOG.debug("Updated Manufacturer {}", manufacturerCopy);
        
        return manufacturerCopy.clone();
    }

    @Override
    public Manufacturer delete(long manufacturerId) {
        Manufacturer manufacturer = get(manufacturerId);
        idToManufacturerMap.remove(manufacturer.getId());
        
        LOG.debug("Deleted Manufacturer {}", manufacturer);
        
        return manufacturer.clone();
    }

    @Override
    public List<Manufacturer> get(Set<Long> manufacturerIdSet) {

    	List<Manufacturer> ret = new ArrayList<>();
    	
    	if(manufacturerIdSet!=null && !manufacturerIdSet.isEmpty()) {	    	
	    	idToManufacturerMap.forEach(
	        		(id, c) -> {
	        			if(manufacturerIdSet.contains(id)) {
				        	ret.add(c.clone());
				        } }
	        		);
    	}

        LOG.debug("Found Manufacturers by ids {}", ret);

        return ret;
    
    }

    @Override
    public PaginationResponse<Manufacturer> getForCustomer(int customerId, 
    		final List<ColumnAndSort> sortBy, PaginationContext<Manufacturer> context) {

        PaginationResponse<Manufacturer> ret = new PaginationResponse<>();
        ret.setContext(context.clone());

        if (ret.getContext().isLastPage()) {
            // no more pages available according to the context
            return ret;
        }

        List<Manufacturer> items = new LinkedList<>();

        // apply filters and build the full result list first - inefficient, but ok for testing
        for (Manufacturer mdl : idToManufacturerMap.values()) {

            if (mdl.getCustomerId() != customerId) {
                continue;
            }

            items.add(mdl);
        }

        // apply sortBy columns
        Collections.sort(items, new Comparator<Manufacturer>() {
            @Override
            public int compare(Manufacturer o1, Manufacturer o2) {
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
            for (Manufacturer mdl : items) {
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
        List<Manufacturer> selectedItems = new ArrayList<>(context.getMaxItemsPerPage());
        for (Manufacturer mdl : items.subList(fromIndex, toIndexExclusive)) {
            selectedItems.add(mdl.clone());
        }

        ret.setItems(selectedItems);

        // adjust context for the next page
        ret.prepareForNextPage();

        if(ret.getContext().getStartAfterItem()!=null) {
        	//this datastore is only interested in the last item's id, so we'll clear all other fields on the startAfterItem in the pagination context
        	Manufacturer newStartAfterItem = new Manufacturer();
        	newStartAfterItem.setId(ret.getContext().getStartAfterItem().getId());
        	ret.getContext().setStartAfterItem(newStartAfterItem);
        }

        return ret;
    }    
}
