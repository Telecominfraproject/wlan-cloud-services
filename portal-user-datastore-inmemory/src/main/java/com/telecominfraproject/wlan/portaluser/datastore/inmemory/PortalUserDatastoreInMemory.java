package com.telecominfraproject.wlan.portaluser.datastore.inmemory;

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
import com.telecominfraproject.wlan.portaluser.datastore.PortalUserDatastore;
import com.telecominfraproject.wlan.portaluser.models.PortalUser;

/**
 * @author dtoptygin
 *
 */
@Configuration
public class PortalUserDatastoreInMemory extends BaseInMemoryDatastore implements PortalUserDatastore {

    private static final Logger LOG = LoggerFactory.getLogger(PortalUserDatastoreInMemory.class);

    private static final Map<Long, PortalUser> idToPortalUserMap = new ConcurrentHashMap<Long, PortalUser>();
    
    private static final AtomicLong portalUserIdCounter = new AtomicLong();    

    @Override
    public PortalUser create(PortalUser portalUser) {
        
        PortalUser portalUserCopy = portalUser.clone();
        
        long id = portalUserIdCounter.incrementAndGet();
        portalUserCopy.setId(id);
        portalUserCopy.setCreatedTimestamp(System.currentTimeMillis());
        portalUserCopy.setLastModifiedTimestamp(portalUserCopy.getCreatedTimestamp());
        idToPortalUserMap.put(id, portalUserCopy);
        
        LOG.debug("Stored PortalUser {}", portalUserCopy);
        
        return portalUserCopy.clone();
    }


    @Override
    public PortalUser get(long portalUserId) {
        LOG.debug("Looking up PortalUser for id {}", portalUserId);
        
        PortalUser portalUser = idToPortalUserMap.get(portalUserId);
        
        if(portalUser==null){
            LOG.debug("Cannot find PortalUser for id {}", portalUserId);
            throw new DsEntityNotFoundException("Cannot find PortalUser for id " + portalUserId);
        } else {
            LOG.debug("Found PortalUser {}", portalUser);
        }

        return portalUser.clone();
    }

    @Override
    public PortalUser getOrNull(long portalUserId) {
        LOG.debug("Looking up PortalUser for id {}", portalUserId);
        
        PortalUser portalUser = idToPortalUserMap.get(portalUserId);
        
        if(portalUser==null){
            LOG.debug("Cannot find PortalUser for id {}", portalUserId);
            return null;
        } else {
            LOG.debug("Found PortalUser {}", portalUser);
        }

        return portalUser.clone();
    }
    
    @Override
    public PortalUser update(PortalUser portalUser) {
        PortalUser existingPortalUser = get(portalUser.getId());
        
        if(existingPortalUser.getLastModifiedTimestamp()!=portalUser.getLastModifiedTimestamp()){
            LOG.debug("Concurrent modification detected for PortalUser with id {} expected version is {} but version in db was {}", 
                    portalUser.getId(),
                    portalUser.getLastModifiedTimestamp(),
                    existingPortalUser.getLastModifiedTimestamp()
                    );
            throw new DsConcurrentModificationException("Concurrent modification detected for PortalUser with id " + portalUser.getId()
                    +" expected version is " + portalUser.getLastModifiedTimestamp()
                    +" but version in db was " + existingPortalUser.getLastModifiedTimestamp()
                    );
            
        }
        
        PortalUser portalUserCopy = portalUser.clone();
        portalUserCopy.setLastModifiedTimestamp(getNewLastModTs(portalUser.getLastModifiedTimestamp()));

        idToPortalUserMap.put(portalUserCopy.getId(), portalUserCopy);
        
        LOG.debug("Updated PortalUser {}", portalUserCopy);
        
        return portalUserCopy.clone();
    }

    @Override
    public PortalUser delete(long portalUserId) {
        PortalUser portalUser = get(portalUserId);
        idToPortalUserMap.remove(portalUser.getId());
        
        LOG.debug("Deleted PortalUser {}", portalUser);
        
        return portalUser.clone();
    }

    @Override
    public List<PortalUser> get(Set<Long> portalUserIdSet) {

    	List<PortalUser> ret = new ArrayList<>();
    	
    	if(portalUserIdSet!=null && !portalUserIdSet.isEmpty()) {	    	
	    	idToPortalUserMap.forEach(
	        		(id, c) -> {
	        			if(portalUserIdSet.contains(id)) {
				        	ret.add(c.clone());
				        } }
	        		);
    	}

        LOG.debug("Found PortalUsers by ids {}", ret);

        return ret;
    
    }

    @Override
    public PortalUser getByUsernameOrNull(int customerId, String username) {
    	PortalUser ret = null;
    	
        for (PortalUser mdl : idToPortalUserMap.values()) {
        	if(mdl.getCustomerId() == customerId && mdl.getUsername().equals(username)) {
        		ret = mdl.clone();
        	}
        }
        
    	return ret;
    }
    
    @Override
    public PaginationResponse<PortalUser> getForCustomer(int customerId, 
    		final List<ColumnAndSort> sortBy, PaginationContext<PortalUser> context) {

        PaginationResponse<PortalUser> ret = new PaginationResponse<>();
        ret.setContext(context.clone());

        if (ret.getContext().isLastPage()) {
            // no more pages available according to the context
            return ret;
        }

        List<PortalUser> items = new LinkedList<>();

        // apply filters and build the full result list first - inefficient, but ok for testing
        for (PortalUser mdl : idToPortalUserMap.values()) {

            if (mdl.getCustomerId() != customerId) {
                continue;
            }

            items.add(mdl);
        }

        // apply sortBy columns
        Collections.sort(items, new Comparator<PortalUser>() {
            @Override
            public int compare(PortalUser o1, PortalUser o2) {
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
                        case "username":
                            cmp = o1.getUsername().compareTo(o2.getUsername());
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
            for (PortalUser mdl : items) {
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
        List<PortalUser> selectedItems = new ArrayList<>(context.getMaxItemsPerPage());
        for (PortalUser mdl : items.subList(fromIndex, toIndexExclusive)) {
            selectedItems.add(mdl.clone());
        }

        ret.setItems(selectedItems);

        // adjust context for the next page
        ret.prepareForNextPage();

        if(ret.getContext().getStartAfterItem()!=null) {
        	//this datastore is only interested in the last item's id, so we'll clear all other fields on the startAfterItem in the pagination context
        	PortalUser newStartAfterItem = new PortalUser();
        	newStartAfterItem.setId(ret.getContext().getStartAfterItem().getId());
        	ret.getContext().setStartAfterItem(newStartAfterItem);
        }

        return ret;
    }    
}
