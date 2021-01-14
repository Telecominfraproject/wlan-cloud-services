package com.telecominfraproject.wlan.equipment.datastore.inmemory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pagination.SortOrder;
import com.telecominfraproject.wlan.core.model.pair.PairLongLong;
import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;
import com.telecominfraproject.wlan.datastore.inmemory.BaseInMemoryDatastore;
import com.telecominfraproject.wlan.equipment.datastore.EquipmentDatastore;
import com.telecominfraproject.wlan.equipment.models.CustomerEquipmentCounts;
import com.telecominfraproject.wlan.equipment.models.Equipment;
import com.telecominfraproject.wlan.equipment.models.bulkupdate.rrm.EquipmentRrmBulkUpdateItem;
import com.telecominfraproject.wlan.equipment.models.bulkupdate.rrm.EquipmentRrmBulkUpdateRequest;

/**
 * @author dtoptygin
 *
 */
@Configuration
public class EquipmentDatastoreInMemory extends BaseInMemoryDatastore implements EquipmentDatastore {

    private static final Logger LOG = LoggerFactory.getLogger(EquipmentDatastoreInMemory.class);

    private static final Map<Long, Equipment> idToEquipmentMap = new ConcurrentHashMap<Long, Equipment>();
    
    private static final AtomicLong equipmentIdCounter = new AtomicLong();    

    @Override
    public Equipment create(Equipment equipment) {
        
    	if(equipment.hasUnsupportedValue()) {
    		throw new IllegalStateException("unsupported value in equipment object");
    	}

        Equipment equipmentCopy = equipment.clone();
        
        long id = equipmentIdCounter.incrementAndGet();
        equipmentCopy.setId(id);
        equipmentCopy.setCreatedTimestamp(System.currentTimeMillis());
        equipmentCopy.setLastModifiedTimestamp(equipmentCopy.getCreatedTimestamp());
        idToEquipmentMap.put(id, equipmentCopy);
        
        LOG.debug("Stored Equipment {}", equipmentCopy);
        
        return equipmentCopy.clone();
    }


    @Override
    public Equipment get(long equipmentId) {
        LOG.debug("Looking up Equipment for id {}", equipmentId);
        
        Equipment equipment = idToEquipmentMap.get(equipmentId);
        
        if(equipment==null){
            LOG.debug("Cannot find Equipment for id {}", equipmentId);
            throw new DsEntityNotFoundException("Cannot find Equipment for id " + equipmentId);
        } else {
            LOG.debug("Found Equipment {}", equipment);
        }

        return equipment.clone();
    }

    @Override
    public Equipment getOrNull(long equipmentId) {
        LOG.debug("Looking up Equipment for id {}", equipmentId);
        
        Equipment equipment = idToEquipmentMap.get(equipmentId);
        
        if(equipment==null){
            LOG.debug("Cannot find Equipment for id {}", equipmentId);
            return null;
        } else {
            LOG.debug("Found Equipment {}", equipment);
        }

        return equipment.clone();
    }
    
    @Override
    public Equipment update(Equipment equipment) {
        Equipment existingEquipment = get(equipment.getId());
        
        if(existingEquipment.getLastModifiedTimestamp()!=equipment.getLastModifiedTimestamp()){
            LOG.debug("Concurrent modification detected for Equipment with id {} expected version is {} but version in db was {}", 
                    equipment.getId(),
                    equipment.getLastModifiedTimestamp(),
                    existingEquipment.getLastModifiedTimestamp()
                    );
            throw new DsConcurrentModificationException("Concurrent modification detected for Equipment with id " + equipment.getId()
                    +" expected version is " + equipment.getLastModifiedTimestamp()
                    +" but version in db was " + existingEquipment.getLastModifiedTimestamp()
                    );
            
        }
        
        Equipment equipmentCopy = equipment.clone();
        equipmentCopy.setLastModifiedTimestamp(getNewLastModTs(equipment.getLastModifiedTimestamp()));

        idToEquipmentMap.put(equipmentCopy.getId(), equipmentCopy);
        
        LOG.debug("Updated Equipment {}", equipmentCopy);
        
        return equipmentCopy.clone();
    }

    @Override
    public void updateRrmBulk(EquipmentRrmBulkUpdateRequest request) {
    	if(request == null || request.getItems() == null || request.getItems().isEmpty()) {
    		//nothing to do here
    		return;
    	}
    	
    	for(EquipmentRrmBulkUpdateItem item: request.getItems()) {
    		//get fresh equipment model
    		Equipment eq = getOrNull(item.getEquipmentId());
    		if(eq == null) {
    			//non-existing equipment, probably got removed, no need to terminate a batch update because of it
    			continue;
    		}
    		
    		//apply changes
    		boolean eqChanged = item.applyToEquipment(eq);
    		
    		//if resulting model changed - save it
    		if(eqChanged) {
    			//attempt to save the modified model
        		boolean modelSaved = false;
        		
    			for(int i = 0; i < 10; i++) {
	    			try {
	    				update(eq);
	    				modelSaved = true;
	    				break;
	    			} catch (DsConcurrentModificationException e) {
	    				//got concurrent update, sleep a bit and retry
						try {
							Thread.sleep(20);
						} catch (InterruptedException e1) {
							Thread.currentThread().interrupt();
						}
						
						//get fresh version of equipment from DB and re-apply our changes on it
	    				eq = getOrNull(item.getEquipmentId());
	    				if(eq==null) {
	    	    			//equipment is not there anymore, no need to terminate a batch update because of it
	    					break;
	    				}
	    				item.applyToEquipment(eq);

					}
    			}
    			
    			if(!modelSaved) {
    				LOG.warn("In bulk operation this equipment was NOT updated after 10 attempts: {}", eq);
    			}
    		}
    	}
    	
    }
    
    @Override
    public Equipment delete(long equipmentId) {
        Equipment equipment = get(equipmentId);
        idToEquipmentMap.remove(equipment.getId());
        
        LOG.debug("Deleted Equipment {}", equipment);
        
        return equipment.clone();
    }

    @Override
    public List<Equipment> get(Set<Long> equipmentIdSet) {

    	List<Equipment> ret = new ArrayList<>();
    	
    	if(equipmentIdSet!=null && !equipmentIdSet.isEmpty()) {	    	
	    	idToEquipmentMap.forEach(
	        		(id, c) -> {
	        			if(equipmentIdSet.contains(id)) {
				        	ret.add(c.clone());
				        } }
	        		);
    	}

        LOG.debug("Found Equipments by ids {}", ret);

        return ret;
    
    }
    
    @Override
    public CustomerEquipmentCounts getEquipmentCounts(int customerId) {
    	CustomerEquipmentCounts counts = new CustomerEquipmentCounts();
    	counts.setCustomerId(customerId);
    	AtomicInteger totalCount = new AtomicInteger();
    	Map<String, AtomicInteger> perOuiMap = new HashMap<>();
    	
    	idToEquipmentMap.values().forEach(
        		(eq) -> {
        			if(eq.getCustomerId() == customerId) {
			        	totalCount.incrementAndGet();
			        	
			        	if(eq.getBaseMacAddress()!=null) {
			        		AtomicInteger cnt = perOuiMap.get(eq.getBaseMacAddress().toOuiString());
			        		if(cnt == null) {
			        			cnt = new AtomicInteger();
			        			perOuiMap.put(eq.getBaseMacAddress().toOuiString(), cnt);
			        		}
			        		cnt.incrementAndGet();
			        	}
			        }        			
        		});

    	counts.setTotalCount(totalCount.get());
    	perOuiMap.forEach((oui, cnt) -> counts.getOuiCounts().put(oui, cnt.get()));
    	
    	return counts;
    }

    @Override
    public Equipment getByInventoryIdOrNull(String inventoryId) {
    	
    	for(Equipment eq: idToEquipmentMap.values()) {
    		if(eq.getInventoryId()!=null && eq.getInventoryId().equals(inventoryId)) {
    			return eq.clone();
    		}
    	}
    	
    	return null;
    }
    
    @Override
    public PaginationResponse<Equipment> getForCustomer(int customerId, 
    		final List<ColumnAndSort> sortBy, PaginationContext<Equipment> context) {

    	if(context == null) {
    		context = new PaginationContext<>();
    	}

        PaginationResponse<Equipment> ret = new PaginationResponse<>();
        ret.setContext(context.clone());

        if (ret.getContext().isLastPage()) {
            // no more pages available according to the context
            return ret;
        }

        List<Equipment> items = new LinkedList<>();

        // apply filters and build the full result list first - inefficient, but ok for testing
        for (Equipment mdl : idToEquipmentMap.values()) {

            if (mdl.getCustomerId() != customerId) {
                continue;
            }

            items.add(mdl);
        }

        // apply sortBy columns
        Collections.sort(items, new Comparator<Equipment>() {
            @Override
            public int compare(Equipment o1, Equipment o2) {
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
                        case "name":
                            cmp = o1.getName().compareTo(o2.getName());
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
            for (Equipment mdl : items) {
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
        List<Equipment> selectedItems = new ArrayList<>(context.getMaxItemsPerPage());
        for (Equipment mdl : items.subList(fromIndex, toIndexExclusive)) {
            selectedItems.add(mdl.clone());
        }

        ret.setItems(selectedItems);

        // adjust context for the next page
        ret.prepareForNextPage();
        
        if(ret.getContext().getStartAfterItem()!=null) {
        	//this datastore is only interested in the last item's id, so we'll clear all other fields on the startAfterItem in the pagination context
        	Equipment newStartAfterItem = new Equipment();
        	newStartAfterItem.setId(ret.getContext().getStartAfterItem().getId());
        	ret.getContext().setStartAfterItem(newStartAfterItem);
        }
        
        return ret;
    }
    
    @Override
    public PaginationResponse<Equipment> getForCustomer(int customerId, EquipmentType equipmentType,
            Set<Long> locationIds, String criteria, final List<ColumnAndSort> sortBy, PaginationContext<Equipment> context) {

    	if(context == null) {
    		context = new PaginationContext<>();
    	}

        PaginationResponse<Equipment> ret = new PaginationResponse<>();
        ret.setContext(context.clone());

        if (ret.getContext().isLastPage()) {
            // no more pages available according to the context
            return ret;
        }

        List<Equipment> items = new LinkedList<>();

        // build full result list first
        for (Equipment ce : idToEquipmentMap.values()) {

            if (ce.getCustomerId() != customerId) {
                continue;
            }

            if (equipmentType != null && ce.getEquipmentType() != equipmentType) {
                continue;
            }

            if (!(CollectionUtils.isEmpty(locationIds) || locationIds.contains(ce.getLocationId()))) {
                continue;
            }
            
            if (criteria != null) {
            	if (ce.getName().toLowerCase().contains(criteria.toLowerCase()) || 
                		ce.getBaseMacAddress().getAddressAsString().toLowerCase().contains(criteria.toLowerCase())) {
                    items.add(ce);
                }
            } else {
            	items.add(ce);
            }
        }

        // apply sortBy columns
        Collections.sort(items, new Comparator<Equipment>() {
            @Override
            public int compare(Equipment o1, Equipment o2) {
                if (sortBy == null || sortBy.isEmpty()) {
                    // sort ascending by equipmentId by default
                    return Long.compare(o1.getId(), o2.getId());
                } else {
                    int cmp;
                    for (ColumnAndSort column : sortBy) {
                        switch (column.getColumnName()) {
                        case "id":
                            cmp = Long.compare(o1.getId(), o2.getId());
                            break;
                        case "name":
                            cmp = o1.getName().compareTo(o2.getName());
                            break;
                        case "locationId":
                            cmp = Long.compare(o1.getLocationId(), o2.getLocationId());
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
            for (Equipment ce : items) {
                fromIndex++;
                if (ce.getId() == context.getStartAfterItem().getId()) {
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
        List<Equipment> selectedItems = new ArrayList<>(context.getMaxItemsPerPage());
        for (Equipment ce : items.subList(fromIndex, toIndexExclusive)) {
            selectedItems.add(ce.clone());
        }

        ret.setItems(selectedItems);

        // adjust context for the next page
        ret.prepareForNextPage();

        if(ret.getContext().getStartAfterItem()!=null) {
        	//this datastore is only interested in the last item's id, so we'll clear all other fields on the startAfterItem in the pagination context
        	Equipment newStartAfterItem = new Equipment();
        	newStartAfterItem.setId(ret.getContext().getStartAfterItem().getId());
        	ret.getContext().setStartAfterItem(newStartAfterItem);
        }
        
        return ret;
    }

    @Override
    public PaginationResponse<PairLongLong> getEquipmentIdsByLocationIds(Set<Long> locationIds,
    		PaginationContext<PairLongLong> context) {

    	if(context == null) {
    		context = new PaginationContext<>();
    	}

        PaginationResponse<PairLongLong> ret = new PaginationResponse<>();
        ret.setContext(context.clone());

        if (ret.getContext().isLastPage()) {
            // no more pages available according to the context
            return ret;
        }

        List<PairLongLong> items = new LinkedList<>();

        // build full result list first
        for (Equipment ce : idToEquipmentMap.values()) {
            if (CollectionUtils.isEmpty(locationIds) || locationIds.contains(ce.getLocationId())) {
                items.add(new PairLongLong(ce.getLocationId(), ce.getId()));
            }
        }

        Collections.sort(items, new Comparator<PairLongLong>() {
            @Override
            public int compare(PairLongLong o1, PairLongLong o2) {
                    return Long.compare(o1.getValue1(), o2.getValue1());
            }
        });

        // now select only items for the requested page
        // find first item to add
        int fromIndex = 0;
        if (context.getStartAfterItem() != null) {
            for (PairLongLong ce : items) {
                fromIndex++;
                if (ce.equals(context.getStartAfterItem())) {
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
        List<PairLongLong> selectedItems = new ArrayList<>(context.getMaxItemsPerPage());
        for (PairLongLong ce : items.subList(fromIndex, toIndexExclusive)) {
            selectedItems.add(ce);
        }

        ret.setItems(selectedItems);

        // adjust context for the next page
        ret.prepareForNextPage();
        
        return ret;
    }
    
    @Override
    public PaginationResponse<PairLongLong> getEquipmentIdsByProfileIds(Set<Long> profileIds,
    		PaginationContext<PairLongLong> context) {

    	if(context == null) {
    		context = new PaginationContext<>();
    	}

        PaginationResponse<PairLongLong> ret = new PaginationResponse<>();
        ret.setContext(context.clone());

        if (ret.getContext().isLastPage()) {
            // no more pages available according to the context
            return ret;
        }

        List<PairLongLong> items = new LinkedList<>();

        // build full result list first
        for (Equipment ce : idToEquipmentMap.values()) {
            if (CollectionUtils.isEmpty(profileIds) || profileIds.contains(ce.getProfileId())) {
                items.add(new PairLongLong(ce.getProfileId(), ce.getId()));
            }
        }

        Collections.sort(items, new Comparator<PairLongLong>() {
            @Override
            public int compare(PairLongLong o1, PairLongLong o2) {
                    return Long.compare(o1.getValue1(), o2.getValue1());
            }
        });

        // now select only items for the requested page
        // find first item to add
        int fromIndex = 0;
        if (context.getStartAfterItem() != null) {
            for (PairLongLong ce : items) {
                fromIndex++;
                if (ce.equals(context.getStartAfterItem())) {
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
        List<PairLongLong> selectedItems = new ArrayList<>(context.getMaxItemsPerPage());
        for (PairLongLong ce : items.subList(fromIndex, toIndexExclusive)) {
            selectedItems.add(ce);
        }

        ret.setItems(selectedItems);

        // adjust context for the next page
        ret.prepareForNextPage();
        
        return ret;
    }
    
 
    
}
