package com.telecominfraproject.wlan.servicemetric.datastore.inmemory;

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

import com.telecominfraproject.wlan.servicemetric.datastore.ServiceMetricDatastore;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetric;

/**
 * @author dtoptygin
 *
 */
@Configuration
public class ServiceMetricDatastoreInMemory extends BaseInMemoryDatastore implements ServiceMetricDatastore {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceMetricDatastoreInMemory.class);

    private static final Map<Long, ServiceMetric> idToServiceMetricMap = new ConcurrentHashMap<Long, ServiceMetric>();
    
    private static final AtomicLong serviceMetricIdCounter = new AtomicLong();    

    @Override
    public ServiceMetric create(ServiceMetric serviceMetric) {
        
        ServiceMetric serviceMetricCopy = serviceMetric.clone();
        
        long id = serviceMetricIdCounter.incrementAndGet();
        serviceMetricCopy.setId(id);
        serviceMetricCopy.setCreatedTimestamp(System.currentTimeMillis());
        serviceMetricCopy.setLastModifiedTimestamp(serviceMetricCopy.getCreatedTimestamp());
        idToServiceMetricMap.put(id, serviceMetricCopy);
        
        LOG.debug("Stored ServiceMetric {}", serviceMetricCopy);
        
        return serviceMetricCopy.clone();
    }


    @Override
    public ServiceMetric get(long serviceMetricId) {
        LOG.debug("Looking up ServiceMetric for id {}", serviceMetricId);
        
        ServiceMetric serviceMetric = idToServiceMetricMap.get(serviceMetricId);
        
        if(serviceMetric==null){
            LOG.debug("Cannot find ServiceMetric for id {}", serviceMetricId);
            throw new DsEntityNotFoundException("Cannot find ServiceMetric for id " + serviceMetricId);
        } else {
            LOG.debug("Found ServiceMetric {}", serviceMetric);
        }

        return serviceMetric.clone();
    }

    @Override
    public ServiceMetric getOrNull(long serviceMetricId) {
        LOG.debug("Looking up ServiceMetric for id {}", serviceMetricId);
        
        ServiceMetric serviceMetric = idToServiceMetricMap.get(serviceMetricId);
        
        if(serviceMetric==null){
            LOG.debug("Cannot find ServiceMetric for id {}", serviceMetricId);
            return null;
        } else {
            LOG.debug("Found ServiceMetric {}", serviceMetric);
        }

        return serviceMetric.clone();
    }
    
    @Override
    public ServiceMetric update(ServiceMetric serviceMetric) {
        ServiceMetric existingServiceMetric = get(serviceMetric.getId());
        
        if(existingServiceMetric.getLastModifiedTimestamp()!=serviceMetric.getLastModifiedTimestamp()){
            LOG.debug("Concurrent modification detected for ServiceMetric with id {} expected version is {} but version in db was {}", 
                    serviceMetric.getId(),
                    serviceMetric.getLastModifiedTimestamp(),
                    existingServiceMetric.getLastModifiedTimestamp()
                    );
            throw new DsConcurrentModificationException("Concurrent modification detected for ServiceMetric with id " + serviceMetric.getId()
                    +" expected version is " + serviceMetric.getLastModifiedTimestamp()
                    +" but version in db was " + existingServiceMetric.getLastModifiedTimestamp()
                    );
            
        }
        
        ServiceMetric serviceMetricCopy = serviceMetric.clone();
        serviceMetricCopy.setLastModifiedTimestamp(getNewLastModTs(serviceMetric.getLastModifiedTimestamp()));

        idToServiceMetricMap.put(serviceMetricCopy.getId(), serviceMetricCopy);
        
        LOG.debug("Updated ServiceMetric {}", serviceMetricCopy);
        
        return serviceMetricCopy.clone();
    }

    @Override
    public ServiceMetric delete(long serviceMetricId) {
        ServiceMetric serviceMetric = get(serviceMetricId);
        idToServiceMetricMap.remove(serviceMetric.getId());
        
        LOG.debug("Deleted ServiceMetric {}", serviceMetric);
        
        return serviceMetric.clone();
    }

    @Override
    public List<ServiceMetric> get(Set<Long> serviceMetricIdSet) {

    	List<ServiceMetric> ret = new ArrayList<>();
    	
    	if(serviceMetricIdSet!=null && !serviceMetricIdSet.isEmpty()) {	    	
	    	idToServiceMetricMap.forEach(
	        		(id, c) -> {
	        			if(serviceMetricIdSet.contains(id)) {
				        	ret.add(c.clone());
				        } }
	        		);
    	}

        LOG.debug("Found ServiceMetrics by ids {}", ret);

        return ret;
    
    }

    @Override
    public PaginationResponse<ServiceMetric> getForCustomer(int customerId, 
    		final List<ColumnAndSort> sortBy, PaginationContext<ServiceMetric> context) {

        PaginationResponse<ServiceMetric> ret = new PaginationResponse<>();
        ret.setContext(context.clone());

        if (ret.getContext().isLastPage()) {
            // no more pages available according to the context
            return ret;
        }

        List<ServiceMetric> items = new LinkedList<>();

        // apply filters and build the full result list first - inefficient, but ok for testing
        for (ServiceMetric mdl : idToServiceMetricMap.values()) {

            if (mdl.getCustomerId() != customerId) {
                continue;
            }

            items.add(mdl);
        }

        // apply sortBy columns
        Collections.sort(items, new Comparator<ServiceMetric>() {
            @Override
            public int compare(ServiceMetric o1, ServiceMetric o2) {
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
            for (ServiceMetric mdl : items) {
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
        List<ServiceMetric> selectedItems = new ArrayList<>(context.getMaxItemsPerPage());
        for (ServiceMetric mdl : items.subList(fromIndex, toIndexExclusive)) {
            selectedItems.add(mdl.clone());
        }

        ret.setItems(selectedItems);

        // adjust context for the next page
        ret.prepareForNextPage();

        if(ret.getContext().getStartAfterItem()!=null) {
        	//this datastore is only interested in the last item's id, so we'll clear all other fields on the startAfterItem in the pagination context
        	ServiceMetric newStartAfterItem = new ServiceMetric();
        	newStartAfterItem.setId(ret.getContext().getStartAfterItem().getId());
        	ret.getContext().setStartAfterItem(newStartAfterItem);
        }

        return ret;
    }    
}
