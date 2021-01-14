package com.telecominfraproject.wlan.status.datastore.inmemory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
import com.telecominfraproject.wlan.datastore.inmemory.BaseInMemoryDatastore;
import com.telecominfraproject.wlan.status.datastore.StatusDatastore;
import com.telecominfraproject.wlan.status.models.Status;
import com.telecominfraproject.wlan.status.models.StatusDataType;

/**
 * @author dtoptygin
 *
 */
@Configuration
public class StatusDatastoreInMemory extends BaseInMemoryDatastore implements StatusDatastore {

    private static final Logger LOG = LoggerFactory.getLogger(StatusDatastoreInMemory.class);

    private static class CustomerEquipmentStatusId {
    	final int customerId;
    	final long equipmentId;
    	final StatusDataType statusDataType;
    	
    	public CustomerEquipmentStatusId(int customerId, 	long equipmentId, StatusDataType statusDataType) {
    		this.customerId = customerId;
    		this.equipmentId = equipmentId;
    		this.statusDataType = statusDataType;
		}
    	
		@Override
		public int hashCode() {
			return Objects.hash(customerId, equipmentId, statusDataType);
		}
		
		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (!(obj instanceof CustomerEquipmentStatusId)) {
				return false;
			}
			CustomerEquipmentStatusId other = (CustomerEquipmentStatusId) obj;
			return customerId == other.customerId && equipmentId == other.equipmentId
					&& statusDataType == other.statusDataType;
		}
    	    	
    };
    
    private static final Map<CustomerEquipmentStatusId, Status> idToStatusMap = new ConcurrentHashMap<>();
    
    private static final AtomicLong statusIdCounter = new AtomicLong();    


    @Override
    public Status getOrNull(int customerId, long equipmentId, StatusDataType statusDataType) {
    	
    	CustomerEquipmentStatusId statusId = new CustomerEquipmentStatusId(customerId, equipmentId, statusDataType);
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
        Status statusCopy = status.clone();

        CustomerEquipmentStatusId statusId = new CustomerEquipmentStatusId(statusCopy.getCustomerId(), statusCopy.getEquipmentId(), statusCopy.getStatusDataType());
        if(statusCopy.getCreatedTimestamp()==0) {
        	statusCopy.setCreatedTimestamp(System.currentTimeMillis());
        }
        statusCopy.setLastModifiedTimestamp(System.currentTimeMillis());

        idToStatusMap.put(statusId, statusCopy);
        
        LOG.debug("Updated Status {}", statusCopy);
        
        return statusCopy.clone();

    }


    @Override
    public List<Status> delete(int customerId, long equipmentId, Set<StatusDataType> dataTypes) {
    	List<CustomerEquipmentStatusId> keysToRemove = new ArrayList<>();
		idToStatusMap.keySet().forEach(k -> {
			if (k.customerId == customerId && k.equipmentId == equipmentId && (dataTypes == null || dataTypes.isEmpty() || dataTypes.contains(k.statusDataType) ) ) {
				keysToRemove.add(k);
			}
		});
    	
    	List<Status> ret = new ArrayList<>();
		keysToRemove.forEach(k -> {
			Status s = idToStatusMap.remove(k);
			if (s != null) {
				ret.add(s);
			}
		});

    	return ret;
    }
    
    
	@Override
	public List<Status> get(int customerId, long equipmentId) {

    	List<Status> ret = new ArrayList<>();
    	
		idToStatusMap.entrySet().forEach( e -> {
			if (e.getKey().customerId == customerId && e.getKey().equipmentId == equipmentId) {
				ret.add(e.getValue().clone());
			}
		});

        LOG.debug("Found Statuses by customer {} equipment {} : {}", customerId, equipmentId, ret);

        return ret;
    
    }

    private static class StatusColumnComparator implements Comparator<Status> {
    	
    	private List<ColumnAndSort> sortBy;
    	
    	public StatusColumnComparator(List<ColumnAndSort> sortBy) {
    		this.sortBy = sortBy;
    	}
    	
        @Override
        public int compare(Status o1, Status o2) {
            if (sortBy == null || sortBy.isEmpty()) {
                // sort ascending by equipmentId by default
                return Long.compare(o1.getEquipmentId(), o2.getEquipmentId());
            } else {
                int cmp;
                for (ColumnAndSort column : sortBy) {
                    switch (column.getColumnName()) {
                    case "customerId":                        	
                        cmp = Integer.compare(o1.getCustomerId(), o2.getCustomerId());
                        break;
                    case "equipmentId":
                        cmp = Long.compare(o1.getEquipmentId(), o2.getEquipmentId());
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
    };
    
    @Override
    public PaginationResponse<Status> getForCustomer(int customerId, Set<Long> equipmentIds,
    		Set<StatusDataType> statusDataTypes, List<ColumnAndSort> sortBy, PaginationContext<Status> context) {
    
    	PaginationFilter<Status> filter = (mdl) -> {
            if (mdl.getCustomerId() != customerId) {
                return false;
            }
            
            if(equipmentIds!=null && !equipmentIds.isEmpty() && !equipmentIds.contains(mdl.getEquipmentId())) {
                return false;
            }

            if(statusDataTypes!=null && !statusDataTypes.isEmpty() && !statusDataTypes.contains(mdl.getStatusDataType())) {
                return false;
            }

			return true;
    	};
    	
    	return getNextPage(filter, sortBy, context);
    	
    }   

    private static interface PaginationFilter<T> {
    	boolean includeModel(T model);
    } 
    
    private PaginationResponse<Status> getNextPage(PaginationFilter<Status> filter, List<ColumnAndSort> sortBy, PaginationContext<Status> context) {
    
    	if(context == null) {
    		context = new PaginationContext<>();
    	}

        PaginationResponse<Status> ret = new PaginationResponse<>();
        ret.setContext(context.clone());

        if (ret.getContext().isLastPage()) {
            // no more pages available according to the context
            return ret;
        }

        List<Status> items = new LinkedList<>();

        // apply filters and build the full result list first - inefficient, but ok for testing
        idToStatusMap.values().stream().filter(mdl -> filter.includeModel(mdl)).forEach( mdl -> items.add(mdl));

        // apply sortBy columns
        Collections.sort(items, new StatusColumnComparator(sortBy));

        // now select only items for the requested page
        // find first item to add
        int fromIndex = 0;
        if (context.getStartAfterItem() != null) {
            for (Status mdl : items) {
                fromIndex++;
                if (mdl.getCustomerId() == context.getStartAfterItem().getCustomerId() &&
                		mdl.getEquipmentId() == context.getStartAfterItem().getEquipmentId() &&
                		mdl.getStatusDataType() == context.getStartAfterItem().getStatusDataType()
                		) {
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
        	newStartAfterItem.setCustomerId(ret.getContext().getStartAfterItem().getCustomerId());
        	newStartAfterItem.setEquipmentId(ret.getContext().getStartAfterItem().getEquipmentId());
        	newStartAfterItem.setStatusDataType(ret.getContext().getStartAfterItem().getStatusDataType());
        	ret.getContext().setStartAfterItem(newStartAfterItem);
        }

        return ret;
    }
    
    @Override
    public List<Status> getForEquipment(int customerId, Set<Long> equipmentIds, Set<StatusDataType> statusDataTypes) {

    	if(equipmentIds==null || equipmentIds.isEmpty()) {
    		throw new IllegalArgumentException("equipmentIds parameter must be provided");
    	}
    	
    	PaginationFilter<Status> filter = (mdl) -> {
            if (mdl.getCustomerId() != customerId) {
                return false;
            }
            
            if( !equipmentIds.contains(mdl.getEquipmentId()) ) {
                return false;
            }

            if(statusDataTypes!=null && !statusDataTypes.isEmpty() && !statusDataTypes.contains(mdl.getStatusDataType())) {
                return false;
            }

			return true;
    	};

        List<Status> ret = new LinkedList<>();

        // apply filters and build the result list
        idToStatusMap.values().stream().filter(mdl -> filter.includeModel(mdl)).forEach( mdl -> ret.add(mdl.clone()));

        //sort results by equipmentId, statusDataType
        Collections.sort(ret, new Comparator<Status>() {
        	@Override
        	public int compare(Status o1, Status o2) {
                int cmp = Long.compare(o1.getEquipmentId(), o2.getEquipmentId());
                if(cmp == 0) {
                	cmp = Integer.compare(o1.getStatusDataType().getId(), o2.getStatusDataType().getId());
                }
        		return cmp;
        	}
		});

        return ret;
    }
    
}
