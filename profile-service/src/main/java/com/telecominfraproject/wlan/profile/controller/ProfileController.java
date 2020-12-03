package com.telecominfraproject.wlan.profile.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.telecominfraproject.wlan.cloudeventdispatcher.CloudEventDispatcherInterface;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pair.PairLongLong;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;
import com.telecominfraproject.wlan.systemevent.models.SystemEvent;

import com.telecominfraproject.wlan.profile.datastore.ProfileDatastore;
import com.telecominfraproject.wlan.profile.models.Profile;
import com.telecominfraproject.wlan.profile.models.ProfileByCustomerRequest;
import com.telecominfraproject.wlan.profile.models.ProfileByCustomerRequestFactory;
import com.telecominfraproject.wlan.profile.models.ProfileType;
import com.telecominfraproject.wlan.profile.models.events.ProfileAddedEvent;
import com.telecominfraproject.wlan.profile.models.events.ProfileChangedEvent;
import com.telecominfraproject.wlan.profile.models.events.ProfileRemovedEvent;
import com.telecominfraproject.wlan.server.exceptions.GenericErrorException;


/**
 * @author dtoptygin
 *
 */
@RestController
@Transactional
@RequestMapping(value="/api/profile")
public class ProfileController {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileController.class);

    public static class ListOfProfiles extends ArrayList<Profile> {
        private static final long serialVersionUID = 3070319062835500930L;
    }

    public static class ListOfPairLongLong extends ArrayList<PairLongLong> {
        private static final long serialVersionUID = 3070319062835500931L;
    }

    

    @Autowired private ProfileDatastore profileDatastore;
    @Autowired private CloudEventDispatcherInterface cloudEventDispatcher;
    @Autowired private ProfileByCustomerRequestFactory profileByCustomerRequestFactory;

    
    /**
     * Creates new Profile.
     *  
     * @param Profile
     * @return stored Profile object
     * @throws RuntimeException if Profile record already exists
     */
    @RequestMapping(method=RequestMethod.POST)
    public Profile create(@RequestBody Profile profile ) {

        LOG.debug("Creating Profile {}", profile);

        if (BaseJsonModel.hasUnsupportedValue(profile)) {
            LOG.error("Failed to create Profile, request contains unsupported value: {}", profile);
            throw new DsDataValidationException("Profile contains unsupported value");
        }        

        long ts = System.currentTimeMillis();
        if (profile.getCreatedTimestamp() == 0) {
        	profile.setCreatedTimestamp(ts);
        }
        profile.setLastModifiedTimestamp(ts);

        Profile ret = profileDatastore.create(profile);

        LOG.debug("Created Profile {}", ret);

        ProfileAddedEvent event = new ProfileAddedEvent(ret);
        publishEvent(event);


        return ret;
    }
    
    /**
     * Retrieves Profile by id
     * @param profileId
     * @return Profile for the supplied id
     */
    @RequestMapping(method=RequestMethod.GET)
    public Profile get(@RequestParam long profileId ) {
        
        LOG.debug("Retrieving Profile {}", profileId);
        
        Profile ret = profileDatastore.get(profileId);

        LOG.debug("Retrieved Profile {}", ret);

        return ret;
    }

    /**
     * Retrieves Profile by id
     * @param profileId
     * @return Profile for the supplied id
     */
    @RequestMapping(value = "/orNull", method=RequestMethod.GET)
    public Profile getOrNull(@RequestParam long profileId ) {
        
        LOG.debug("Retrieving Profile {}", profileId);
        
        Profile ret = profileDatastore.getOrNull(profileId);

        LOG.debug("Retrieved Profile {}", ret);

        return ret;
    }

    @RequestMapping(value = "/inSet", method = RequestMethod.GET)
    public ListOfProfiles getAllInSet(@RequestParam Set<Long> profileIdSet) {
        LOG.debug("getAllInSet({})", profileIdSet);
        try {
            List<Profile> result = profileDatastore.get(profileIdSet);
            LOG.debug("getAllInSet({}) return {} entries", profileIdSet, result.size());
            ListOfProfiles ret = new ListOfProfiles();
            ret.addAll(result);
            return ret;
        } catch (Exception exp) {
             LOG.error("getAllInSet({}) exception ", profileIdSet, exp);
             throw exp;
        }
    }

    @RequestMapping(value = "/forCustomer", method = RequestMethod.GET)
    public PaginationResponse<Profile> getForCustomer(
    		@RequestParam int customerId,
    		@RequestParam(required = false) ProfileType profileType,
    		@RequestParam(required = false) String nameSubstring,
    		@RequestParam List<ColumnAndSort> sortBy,
            @RequestParam(required = false) PaginationContext<Profile> paginationContext) {
    	
    	ProfileByCustomerRequest profileByCustomerRequest = profileByCustomerRequestFactory.create(customerId, profileType, nameSubstring, sortBy, paginationContext);

        LOG.debug("Looking up Profiles for customer {} with last returned page number {}", 
        		profileByCustomerRequest.getCustomerId(), profileByCustomerRequest.getPaginationContext().getLastReturnedPageNumber());

        PaginationResponse<Profile> ret = new PaginationResponse<>();

        if (profileByCustomerRequest.getPaginationContext().isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up Profiles for customer {} with last returned page number {}",
                    profileByCustomerRequest.getCustomerId(), profileByCustomerRequest.getPaginationContext().getLastReturnedPageNumber());
            ret.setContext(paginationContext);
            return ret;
        }

        PaginationResponse<Profile> onePage = this.profileDatastore
                .getForCustomer(profileByCustomerRequest);
        ret.setContext(onePage.getContext());
        ret.getItems().addAll(onePage.getItems());

        LOG.debug("Retrieved {} Profiles for customer {} ", onePage.getItems().size(), 
                customerId);

        return ret;
    }
    
    @RequestMapping(value = "/withChildren", method = RequestMethod.GET)
    public ListOfProfiles getWithChildren(@RequestParam long profileId) {
        LOG.debug("getWithChildren({})", profileId);
        try {
            List<Profile> result = profileDatastore.getProfileWithChildren(profileId);
            LOG.debug("getWithChildren({}) return {} entries", profileId, result.size());
            ListOfProfiles ret = new ListOfProfiles();
            ret.addAll(result);
            return ret;
        } catch (Exception exp) {
             LOG.error("getWithChildren({}) exception ", profileId, exp);
             throw exp;
        }
    }

    
    /**
     * Updates Profile record
     * 
     * @param Profile
     * @return updated Profile object
     * @throws RuntimeException if Profile record does not exist or if it was modified concurrently
     */
    @RequestMapping(method=RequestMethod.PUT)
    public Profile update(@RequestBody Profile profile){
        
        LOG.debug("Updating Profile {}", profile);
        
        if (BaseJsonModel.hasUnsupportedValue(profile)) {
            LOG.error("Failed to update Profile, request contains unsupported value: {}", profile);
            throw new DsDataValidationException("Profile contains unsupported value");
        }

        Profile ret = profileDatastore.update(profile);

        LOG.debug("Updated Profile {}", ret);

        ProfileChangedEvent event = new ProfileChangedEvent(ret);
        publishEvent(event);

        return ret;
    }
    
    /**
     * Deletes Profile record
     * 
     * @param profileId
     * @return deleted Profile object
     */
    @RequestMapping(method=RequestMethod.DELETE)
    public Profile delete(@RequestParam long profileId ) {
        
        LOG.debug("Deleting Profile {}", profileId);
        
        Profile ret = profileDatastore.delete(profileId);

        LOG.debug("Deleted Profile {}", ret);
        
        ProfileRemovedEvent event = new ProfileRemovedEvent(ret);
        publishEvent(event);

        return ret;
    }

    private void publishEvent(SystemEvent event) {
        if (event == null) {
            return;
        }
        
        try {
            cloudEventDispatcher.publishEvent(event);
        } catch (Exception e) {
            LOG.error("Failed to publish event : {}", event, e);
        }
    }

    @RequestMapping(value = "/topLevelProfiles", method = RequestMethod.GET)
	public ListOfPairLongLong getTopLevelProfiles(@RequestParam Set<Long> profileIdSet) {
    	ListOfPairLongLong ret = new ListOfPairLongLong();
    	ret.addAll(profileDatastore.getTopLevelProfiles(profileIdSet));
		return ret;
	}

    
}
