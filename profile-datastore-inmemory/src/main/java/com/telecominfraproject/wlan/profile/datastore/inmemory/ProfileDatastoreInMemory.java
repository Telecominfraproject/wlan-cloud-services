package com.telecominfraproject.wlan.profile.datastore.inmemory;

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
import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;
import com.telecominfraproject.wlan.datastore.inmemory.BaseInMemoryDatastore;
import com.telecominfraproject.wlan.profile.datastore.ProfileDatastore;
import com.telecominfraproject.wlan.profile.models.Profile;

/**
 * @author dtoptygin
 *
 */
@Configuration
public class ProfileDatastoreInMemory extends BaseInMemoryDatastore implements ProfileDatastore {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileDatastoreInMemory.class);

    private static final Map<Long, Profile> idToProfileMap = new ConcurrentHashMap<Long, Profile>();
    
    private static final AtomicLong profileIdCounter = new AtomicLong();    

    @Override
    public Profile create(Profile profile) {
        
    	if(profile.hasUnsupportedValue()) {
    		throw new IllegalStateException("unsupported value in profile object");
    	}
    	
        Profile profileCopy = profile.clone();
        
        long id = profileIdCounter.incrementAndGet();
        profileCopy.setId(id);
        profileCopy.setCreatedTimestamp(System.currentTimeMillis());
        profileCopy.setLastModifiedTimestamp(profileCopy.getCreatedTimestamp());
        idToProfileMap.put(id, profileCopy);
        
        LOG.debug("Stored Profile {}", profileCopy);
        
        return profileCopy.clone();
    }


    @Override
    public Profile get(long profileId) {
        LOG.debug("Looking up Profile for id {}", profileId);
        
        Profile profile = idToProfileMap.get(profileId);
        
        if(profile==null){
            LOG.debug("Cannot find Profile for id {}", profileId);
            throw new DsEntityNotFoundException("Cannot find Profile for id " + profileId);
        } else {
            LOG.debug("Found Profile {}", profile);
        }

        return profile.clone();
    }

    @Override
    public Profile getOrNull(long profileId) {
        LOG.debug("Looking up Profile for id {}", profileId);
        
        Profile profile = idToProfileMap.get(profileId);
        
        if(profile==null){
            LOG.debug("Cannot find Profile for id {}", profileId);
            return null;
        } else {
            LOG.debug("Found Profile {}", profile);
        }

        return profile.clone();
    }
    
    @Override
    public Profile update(Profile profile) {
        Profile existingProfile = get(profile.getId());
        
        if(existingProfile.getLastModifiedTimestamp()!=profile.getLastModifiedTimestamp()){
            LOG.debug("Concurrent modification detected for Profile with id {} expected version is {} but version in db was {}", 
                    profile.getId(),
                    profile.getLastModifiedTimestamp(),
                    existingProfile.getLastModifiedTimestamp()
                    );
            throw new DsConcurrentModificationException("Concurrent modification detected for Profile with id " + profile.getId()
                    +" expected version is " + profile.getLastModifiedTimestamp()
                    +" but version in db was " + existingProfile.getLastModifiedTimestamp()
                    );
            
        }
        
        Profile profileCopy = profile.clone();
        profileCopy.setLastModifiedTimestamp(getNewLastModTs(profile.getLastModifiedTimestamp()));

        idToProfileMap.put(profileCopy.getId(), profileCopy);
        
        LOG.debug("Updated Profile {}", profileCopy);
        
        return profileCopy.clone();
    }

    @Override
    public Profile delete(long profileId) {
        Profile profile = get(profileId);
        idToProfileMap.remove(profile.getId());
        
        LOG.debug("Deleted Profile {}", profile);
        
        return profile.clone();
    }

    @Override
    public List<Profile> get(Set<Long> profileIdSet) {

    	List<Profile> ret = new ArrayList<>();
    	
    	if(profileIdSet!=null && !profileIdSet.isEmpty()) {	    	
	    	idToProfileMap.forEach(
	        		(id, c) -> {
	        			if(profileIdSet.contains(id)) {
				        	ret.add(c.clone());
				        } }
	        		);
    	}

        LOG.debug("Found Profiles by ids {}", ret);

        return ret;
    
    }

    @Override
    public PaginationResponse<Profile> getForCustomer(int customerId, 
    		final List<ColumnAndSort> sortBy, PaginationContext<Profile> context) {

    	if(context == null) {
    		context = new PaginationContext<>();
    	}

        PaginationResponse<Profile> ret = new PaginationResponse<>();
        ret.setContext(context.clone());

        if (ret.getContext().isLastPage()) {
            // no more pages available according to the context
            return ret;
        }

        List<Profile> items = new LinkedList<>();

        // apply filters and build the full result list first - inefficient, but ok for testing
        for (Profile mdl : idToProfileMap.values()) {

            if (mdl.getCustomerId() != customerId) {
                continue;
            }

            items.add(mdl);
        }

        // apply sortBy columns
        Collections.sort(items, new Comparator<Profile>() {
            @Override
            public int compare(Profile o1, Profile o2) {
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
            for (Profile mdl : items) {
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
        List<Profile> selectedItems = new ArrayList<>(context.getMaxItemsPerPage());
        for (Profile mdl : items.subList(fromIndex, toIndexExclusive)) {
            selectedItems.add(mdl.clone());
        }

        ret.setItems(selectedItems);

        // adjust context for the next page
        ret.prepareForNextPage();
        
        if(ret.getContext().getStartAfterItem()!=null) {
        	//this datastore is only interested in the last item's id, so we'll clear all other fields on the startAfterItem in the pagination context
        	Profile newStartAfterItem = new Profile();
        	newStartAfterItem.setId(ret.getContext().getStartAfterItem().getId());
        	ret.getContext().setStartAfterItem(newStartAfterItem);
        }

        return ret;
    }
    
    @Override
    public List<Profile> getProfileWithChildren(long profileId) {
    	Set<Profile> profileSet = new HashSet<>();
    	
    	getAllDescendants(get(profileId), profileSet);
    	
    	List<Profile> descendants = new ArrayList<>(profileSet);
    	
    	return descendants;
    }
    
    private void getAllDescendants(Profile profile, Set<Profile> collectedChildren) {

    	collectedChildren.add(profile.clone());
    	
    	if(profile.getChildProfileIds()!=null && !profile.getChildProfileIds().isEmpty()) {
    		profile.getChildProfileIds().forEach(pId -> getAllDescendants(get(pId), collectedChildren) );
    	}

    }
    
}
