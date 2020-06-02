package com.telecominfraproject.wlan.routing.datastore.inmemory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
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
import com.telecominfraproject.wlan.core.model.service.GatewayType;
import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;
import com.telecominfraproject.wlan.datastore.inmemory.BaseInMemoryDatastore;
import com.telecominfraproject.wlan.routing.datastore.RoutingDatastore;
import com.telecominfraproject.wlan.routing.models.EquipmentGatewayRecord;
import com.telecominfraproject.wlan.routing.models.EquipmentRoutingRecord;

/**
 * @author dtoptygin
 *
 */
@Configuration
public class RoutingDatastoreInMemory extends BaseInMemoryDatastore implements RoutingDatastore {

    private static final Logger LOG = LoggerFactory.getLogger(RoutingDatastoreInMemory.class);

    private static final Map<Long, EquipmentRoutingRecord> idToRoutingMap = new ConcurrentHashMap<Long, EquipmentRoutingRecord>();

    private static final Map<Long, EquipmentGatewayRecord> idToGatewayMap = new ConcurrentHashMap<Long, EquipmentGatewayRecord>();

    private static final AtomicLong routingIdCounter = new AtomicLong();    
    private static final AtomicLong gatewayIdCounter = new AtomicLong();    

    @Override
    public EquipmentRoutingRecord create(EquipmentRoutingRecord routing) {
        
        EquipmentRoutingRecord routingCopy = routing.clone();
        
        long id = routingIdCounter.incrementAndGet();
        routingCopy.setId(id);
        routingCopy.setCreatedTimestamp(System.currentTimeMillis());
        routingCopy.setLastModifiedTimestamp(routingCopy.getCreatedTimestamp());
        idToRoutingMap.put(id, routingCopy);
        
        LOG.debug("Stored EquipmentRoutingRecord {}", routingCopy);
        
        return routingCopy.clone();
    }


    @Override
    public EquipmentRoutingRecord get(long routingId) {
        LOG.debug("Looking up EquipmentRoutingRecord for id {}", routingId);
        
        EquipmentRoutingRecord routing = idToRoutingMap.get(routingId);
        
        if(routing==null){
            LOG.debug("Cannot find EquipmentRoutingRecord for id {}", routingId);
            throw new DsEntityNotFoundException("Cannot find EquipmentRoutingRecord for id " + routingId);
        } else {
            LOG.debug("Found EquipmentRoutingRecord {}", routing);
        }

        return routing.clone();
    }

    @Override
    public EquipmentRoutingRecord getOrNull(long routingId) {
        LOG.debug("Looking up EquipmentRoutingRecord for id {}", routingId);
        
        EquipmentRoutingRecord routing = idToRoutingMap.get(routingId);
        
        if(routing==null){
            LOG.debug("Cannot find EquipmentRoutingRecord for id {}", routingId);
            return null;
        } else {
            LOG.debug("Found EquipmentRoutingRecord {}", routing);
        }

        return routing.clone();
    }
    
    @Override
    public EquipmentRoutingRecord update(EquipmentRoutingRecord routing) {
        EquipmentRoutingRecord existingRouting = get(routing.getId());
        
        if(existingRouting.getLastModifiedTimestamp()!=routing.getLastModifiedTimestamp()){
            LOG.debug("Concurrent modification detected for EquipmentRoutingRecord with id {} expected version is {} but version in db was {}", 
                    routing.getId(),
                    routing.getLastModifiedTimestamp(),
                    existingRouting.getLastModifiedTimestamp()
                    );
            throw new DsConcurrentModificationException("Concurrent modification detected for EquipmentRoutingRecord with id " + routing.getId()
                    +" expected version is " + routing.getLastModifiedTimestamp()
                    +" but version in db was " + existingRouting.getLastModifiedTimestamp()
                    );
            
        }
        
        EquipmentRoutingRecord routingCopy = routing.clone();
        routingCopy.setLastModifiedTimestamp(getNewLastModTs(routing.getLastModifiedTimestamp()));

        idToRoutingMap.put(routingCopy.getId(), routingCopy);
        
        LOG.debug("Updated EquipmentRoutingRecord {}", routingCopy);
        
        return routingCopy.clone();
    }

    @Override
    public EquipmentRoutingRecord delete(long routingId) {
        EquipmentRoutingRecord routing = get(routingId);
        idToRoutingMap.remove(routing.getId());
        
        LOG.debug("Deleted EquipmentRoutingRecord {}", routing);
        
        return routing.clone();
    }

    @Override
    public List<EquipmentRoutingRecord> get(Set<Long> routingIdSet) {

    	List<EquipmentRoutingRecord> ret = new ArrayList<>();
    	
    	if(routingIdSet!=null && !routingIdSet.isEmpty()) {	    	
	    	idToRoutingMap.forEach(
	        		(id, c) -> {
	        			if(routingIdSet.contains(id)) {
				        	ret.add(c.clone());
				        } }
	        		);
    	}

        LOG.debug("Found Routings by ids {}", ret);

        return ret;
    
    }

    @Override
    public PaginationResponse<EquipmentRoutingRecord> getForCustomer(int customerId, 
    		final List<ColumnAndSort> sortBy, PaginationContext<EquipmentRoutingRecord> context) {

        PaginationResponse<EquipmentRoutingRecord> ret = new PaginationResponse<>();
        ret.setContext(context.clone());

        if (ret.getContext().isLastPage()) {
            // no more pages available according to the context
            return ret;
        }

        List<EquipmentRoutingRecord> items = new LinkedList<>();

        // apply filters and build the full result list first - inefficient, but ok for testing
        for (EquipmentRoutingRecord mdl : idToRoutingMap.values()) {

            if (mdl.getCustomerId() != customerId) {
                continue;
            }

            items.add(mdl);
        }

        // apply sortBy columns
        Collections.sort(items, new Comparator<EquipmentRoutingRecord>() {
            @Override
            public int compare(EquipmentRoutingRecord o1, EquipmentRoutingRecord o2) {
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
                        case "gatewayId":
                            cmp = Long.compare(o1.getGatewayId(), o2.getGatewayId());
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
        });

        // now select only items for the requested page
        // find first item to add
        int fromIndex = 0;
        if (context.getStartAfterItem() != null) {
            for (EquipmentRoutingRecord mdl : items) {
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
        List<EquipmentRoutingRecord> selectedItems = new ArrayList<>(context.getMaxItemsPerPage());
        for (EquipmentRoutingRecord mdl : items.subList(fromIndex, toIndexExclusive)) {
            selectedItems.add(mdl.clone());
        }

        ret.setItems(selectedItems);

        // adjust context for the next page
        ret.prepareForNextPage();

        if(ret.getContext().getStartAfterItem()!=null) {
        	//this datastore is only interested in the last item's id, so we'll clear all other fields on the startAfterItem in the pagination context
        	EquipmentRoutingRecord newStartAfterItem = new EquipmentRoutingRecord();
        	newStartAfterItem.setId(ret.getContext().getStartAfterItem().getId());
        	ret.getContext().setStartAfterItem(newStartAfterItem);
        }

        return ret;
    }


	@Override
	public EquipmentGatewayRecord registerGateway(EquipmentGatewayRecord equipmentGatewayRecord) {
        EquipmentGatewayRecord gatewayCopy = equipmentGatewayRecord.clone();
        
        long id = gatewayIdCounter.incrementAndGet();
        gatewayCopy.setId(id);
        gatewayCopy.setCreatedTimestamp(System.currentTimeMillis());
        gatewayCopy.setLastModifiedTimestamp(gatewayCopy.getCreatedTimestamp());
        idToGatewayMap.put(id, gatewayCopy);
        
        LOG.debug("Stored EquipmentGatewayRecord {}", gatewayCopy);
        
        return gatewayCopy.clone();
	}


	@Override
	public EquipmentGatewayRecord getGateway(long id) {
        LOG.debug("Looking up EquipmentGatewayRecord for id {}", id);
        
        EquipmentGatewayRecord gateway = idToGatewayMap.get(id);
        
        if(gateway==null){
            LOG.debug("Cannot find EquipmentGatewayRecord for id {}", id);
            throw new DsEntityNotFoundException("Cannot find EquipmentGatewayRecord for id " + id);
        } else {
            LOG.debug("Found EquipmentGatewayRecord {}", gateway);
        }

        return gateway.clone();
	}


	@Override
	public List<EquipmentGatewayRecord> getGateway(String hostname) {
		List<EquipmentGatewayRecord> ret = new ArrayList<>();
		idToGatewayMap.values().forEach(gw -> {
			if (gw.getHostname()!=null && gw.getHostname().equals(hostname)) {
				ret.add(gw.clone());
			}
		});
		
		return ret;
	}


	@Override
	public List<EquipmentGatewayRecord> getGateway(GatewayType gatewayType) {
		List<EquipmentGatewayRecord> ret = new ArrayList<>();
		idToGatewayMap.values().forEach(gw -> {
			if (gw.getGatewayType() == gatewayType) {
				ret.add(gw.clone());
			}
		});
		
		return ret;
	}


	@Override
	public List<EquipmentRoutingRecord> getRegisteredRouteList(long equipmentId) {
		List<EquipmentRoutingRecord> ret = new ArrayList<>();
		idToRoutingMap.values().forEach(route -> {
			if (route.getEquipmentId() == equipmentId) {
				ret.add(route.clone());
			}
		});
		
		return ret;
	}


	@Override
	public List<EquipmentGatewayRecord> getRegisteredGatewayRecordList(long equipmentId) {
		
		Set<Long> gwIds = new HashSet<>();
		idToRoutingMap.values().forEach(route -> {
			if (route.getEquipmentId() == equipmentId) {
				gwIds.add(route.getGatewayId());
			}
		});

		List<EquipmentGatewayRecord> ret = new ArrayList<>();
		
		gwIds.forEach(id -> {
			EquipmentGatewayRecord gw = idToGatewayMap.get(id);
			if(gw!=null) {
				ret.add(gw.clone());
			}
		});
		
		return ret;
	}


	@Override
	public EquipmentGatewayRecord updateGateway(EquipmentGatewayRecord equipmentGwRecord) {
		EquipmentGatewayRecord existingGwRecord = getGateway(equipmentGwRecord.getId());
        
        if(existingGwRecord.getLastModifiedTimestamp()!=equipmentGwRecord.getLastModifiedTimestamp()){
            LOG.debug("Concurrent modification detected for EquipmentGatewayRecord with id {} expected version is {} but version in db was {}", 
            		equipmentGwRecord.getId(),
            		equipmentGwRecord.getLastModifiedTimestamp(),
                    existingGwRecord.getLastModifiedTimestamp()
                    );
            throw new DsConcurrentModificationException("Concurrent modification detected for EquipmentGatewayRecord with id " + equipmentGwRecord.getId()
                    +" expected version is " + equipmentGwRecord.getLastModifiedTimestamp()
                    +" but version in db was " + existingGwRecord.getLastModifiedTimestamp()
                    );
            
        }
        
        EquipmentGatewayRecord gatewayCopy = equipmentGwRecord.clone();
        gatewayCopy.setLastModifiedTimestamp(getNewLastModTs(equipmentGwRecord.getLastModifiedTimestamp()));

        idToGatewayMap.put(gatewayCopy.getId(), gatewayCopy);
        
        LOG.debug("Updated EquipmentGatewayRecord {}", gatewayCopy);
        
        return gatewayCopy.clone();
	}


	@Override
	public EquipmentGatewayRecord deleteGateway(long id) {
        EquipmentGatewayRecord gateway = getGateway(id);
        idToGatewayMap.remove(gateway.getId());
        
        LOG.debug("Deleted EquipmentGatewayRecord {}", gateway);
        
        return gateway.clone();
	}


	@Override
	public List<EquipmentGatewayRecord> deleteGateway(String hostname) {
		List<EquipmentGatewayRecord> ret = getGateway(hostname);
		ret.forEach(gw -> idToGatewayMap.remove(gw.getId()));
		return ret;
	}
    
}
