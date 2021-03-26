package com.telecominfraproject.wlan.servicemetric.datastore.inmemory;

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

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pagination.SortOrder;
import com.telecominfraproject.wlan.datastore.inmemory.BaseInMemoryDatastore;
import com.telecominfraproject.wlan.servicemetric.datastore.ServiceMetricDatastore;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetric;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDataType;

/**
 * @author dtoptygin
 *
 */
@Configuration
public class ServiceMetricDatastoreInMemory extends BaseInMemoryDatastore implements ServiceMetricDatastore {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceMetricDatastoreInMemory.class);

    private static final Map<ServiceMetricKey, ServiceMetric> idToServiceMetricMap = new ConcurrentHashMap<ServiceMetricKey, ServiceMetric>();
    
    private static class ServiceMetricKey{
        final int customerId;
        final long locationId;
        final long equipmentId;
        final long clientMac;
        final ServiceMetricDataType dataType;
        final long createdTimestamp;
        
		public ServiceMetricKey(ServiceMetric serviceMetric) {
			this.customerId = serviceMetric.getCustomerId();
            this.locationId = serviceMetric.getLocationId();
			this.equipmentId = serviceMetric.getEquipmentId();
			this.clientMac = serviceMetric.getClientMac();
			this.dataType = serviceMetric.getDataType();
			this.createdTimestamp = serviceMetric.getCreatedTimestamp();
		}

		@Override
		public int hashCode() {
			return Objects.hash(clientMac, createdTimestamp, customerId, dataType, equipmentId, locationId);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (!(obj instanceof ServiceMetricKey)) {
				return false;
			}
			ServiceMetricKey other = (ServiceMetricKey) obj;
			return clientMac == other.clientMac && createdTimestamp == other.createdTimestamp
					&& customerId == other.customerId && dataType == other.dataType && equipmentId == other.equipmentId && locationId == other.locationId;
		}    
        
    }
    
    @Override
    public void create(ServiceMetric serviceMetric) {
        
        ServiceMetric serviceMetricCopy = serviceMetric.clone();
        
        if(serviceMetricCopy.getCreatedTimestamp()==0) {
        	serviceMetricCopy.setCreatedTimestamp(System.currentTimeMillis());
        }
        
        idToServiceMetricMap.put(new ServiceMetricKey(serviceMetricCopy), serviceMetricCopy);
        
        LOG.debug("Stored ServiceMetric {}", serviceMetricCopy);
        
    }

    @Override
    public void create(List<ServiceMetric> serviceMetrics) {
    	if(serviceMetrics==null || serviceMetrics.isEmpty()) {
    		return;
    	}
    	
    	serviceMetrics.forEach(m -> create(m));
    	
    }
    

    @Override
    public void delete(int customerId, long equipmentId, long createdBeforeTimestamp) {
    	List<ServiceMetricKey> keysToRemove = new ArrayList<>();
    	
        idToServiceMetricMap.keySet().forEach( k -> {
        	if(k.customerId == customerId && k.equipmentId == equipmentId && k.createdTimestamp < createdBeforeTimestamp) {
        		keysToRemove.add(k);
        	}
        });
        
        keysToRemove.forEach(k -> idToServiceMetricMap.remove(k) );
        
        LOG.debug("Deleted ServiceMetric s for customer {} equipment {} createdBefore {}", customerId, equipmentId, createdBeforeTimestamp);
    }

    @Override
    public void delete(long createdBeforeTimestamp) {
    	List<ServiceMetricKey> keysToRemove = new ArrayList<>();
    	
        idToServiceMetricMap.keySet().forEach( k -> {
        	if(k.createdTimestamp < createdBeforeTimestamp) {
        		keysToRemove.add(k);
        	}
        });
        
        keysToRemove.forEach(k -> idToServiceMetricMap.remove(k) );
        
        LOG.debug("Deleted {} ServiceMetrics createdBefore {}", keysToRemove.size(), createdBeforeTimestamp);
    }

    @Override
    public PaginationResponse<ServiceMetric> getForCustomer(long fromTime, long toTime, int customerId,
            Set<Long> locationIds, Set<Long> equipmentIds, Set<MacAddress> clientMacAddresses, Set<ServiceMetricDataType> dataTypes,
    		List<ColumnAndSort> sortBy, PaginationContext<ServiceMetric> context) {

    	if(context == null) {
    		context = new PaginationContext<>();
    	}
    	
        PaginationResponse<ServiceMetric> ret = new PaginationResponse<>();
        ret.setContext(context.clone());

        if (ret.getContext().isLastPage()) {
            // no more pages available according to the context
            return ret;
        }

        List<ServiceMetric> items = new LinkedList<>();

        // apply filters and build the full result list first - inefficient, but ok for testing
        for (ServiceMetric mdl : idToServiceMetricMap.values()) {

            if (mdl.getCustomerId() == customerId &&
                    (locationIds==null || locationIds.isEmpty() || locationIds.contains(mdl.getLocationId())) &&
            		(equipmentIds==null || equipmentIds.isEmpty() || equipmentIds.contains(mdl.getEquipmentId())) &&
            		(clientMacAddresses==null || clientMacAddresses.isEmpty() ||
            			clientMacAddressesContains(clientMacAddresses, mdl.getClientMacAddress())) &&
            		(dataTypes==null || dataTypes.isEmpty() || dataTypes.contains(mdl.getDataType())) &&
            		mdl.getCreatedTimestamp() >= fromTime &&
            		mdl.getCreatedTimestamp() <= toTime 
            	) {
            	items.add(mdl);
            }
        }

        // apply sortBy columns
        Collections.sort(items, new Comparator<ServiceMetric>() {
            @Override
            public int compare(ServiceMetric o1, ServiceMetric o2) {
                if (sortBy == null || sortBy.isEmpty()) {
                    // sort ascending by created time stamp by default
                    return Long.compare(o1.getCreatedTimestamp(), o2.getCreatedTimestamp());
                } else {
                    int cmp;
                    for (ColumnAndSort column : sortBy) {
                        switch (column.getColumnName()) {
                        case "createdTimestamp":
                            cmp = Long.compare(o1.getCreatedTimestamp(), o2.getCreatedTimestamp());
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
                            cmp = Integer.compare(o1.getDataType().getId(), o2.getDataType().getId());
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
        	ServiceMetricKey startAfterKey = new ServiceMetricKey(context.getStartAfterItem());
            for (ServiceMetric mdl : items) {
                fromIndex++;
                if (new ServiceMetricKey(mdl).equals(startAfterKey)) {
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
        	ServiceMetric oldStartAfterItem = ret.getContext().getStartAfterItem();
        	
        	newStartAfterItem.setCustomerId(oldStartAfterItem.getCustomerId());
        	newStartAfterItem.setLocationId(oldStartAfterItem.getLocationId());
        	newStartAfterItem.setEquipmentId(oldStartAfterItem.getEquipmentId());
        	newStartAfterItem.setClientMac(oldStartAfterItem.getClientMac());
        	newStartAfterItem.setDataType(oldStartAfterItem.getDataType());
        	newStartAfterItem.setCreatedTimestamp(oldStartAfterItem.getCreatedTimestamp());
        	
        	ret.getContext().setStartAfterItem(newStartAfterItem);
        }

        return ret;
    }
    
    private boolean clientMacAddressesContains(Set<MacAddress> clientMacAddresses, MacAddress macAddress) {
        if (macAddress == null) {
    	    for (MacAddress clientMacAdress : clientMacAddresses) {
                if (clientMacAdress.getAddressAsLong() == 0) {
    	            return true;
    	        }
    	    }
        }
        return clientMacAddresses.contains(macAddress);
    }
}
