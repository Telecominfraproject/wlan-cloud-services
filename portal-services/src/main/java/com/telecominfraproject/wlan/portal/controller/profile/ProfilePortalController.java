package com.telecominfraproject.wlan.portal.controller.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pair.PairLongLong;
import com.telecominfraproject.wlan.equipment.EquipmentServiceInterface;
import com.telecominfraproject.wlan.profile.ProfileServiceInterface;
import com.telecominfraproject.wlan.profile.models.Profile;

/**
 * @author dtoptygin
 *
 */
@RestController
@RequestMapping(value = "/portal")
public class ProfilePortalController  {

    private static final Logger LOG = LoggerFactory.getLogger(ProfilePortalController.class);

    // For serialization:
    // need these wrapper classes so that each element in returned container is
    // marked with "model_type" attribute
    // all callers of this class can deal with normal collections
    public static class ListOfProfiles extends ArrayList<Profile> {
        private static final long serialVersionUID = 1158560190003268713L;
    }

    public static class ListOfPairLongLong extends ArrayList<PairLongLong> {
        private static final long serialVersionUID = 1158560190003268723L;
    }

    @Autowired
    private ProfileServiceInterface profileServiceInterface;

    @Autowired
    private EquipmentServiceInterface equipmentServiceInterface;

    @RequestMapping(value = "/profile", method = RequestMethod.GET)
    public Profile getProfile(@RequestParam long profileId) {
        LOG.debug("Getting profile {}", profileId);

        Profile profile = profileServiceInterface.get(profileId);

        return profile;
    }

    @RequestMapping(value = "/profile", method = RequestMethod.PUT)
    public Profile updateProfile(@RequestBody Profile profile) {
        LOG.debug("Updating profile {}", profile.getId());

        Profile ret = profileServiceInterface.update(profile);

        return ret;
    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public Profile createProfile(@RequestBody Profile profile) {
        LOG.debug("Creating profile {}", profile.getId());

        Profile ret = profileServiceInterface.create(profile);

        return ret;
    }

    @RequestMapping(value = "/profile", method = RequestMethod.DELETE)
    public Profile deleteProfile(@RequestParam long profileId) {
        LOG.debug("Deleting profile {}", profileId);

        Profile ret = profileServiceInterface.delete(profileId);

        return ret;
    }
    

    @RequestMapping(value = "/profile/inSet", method = RequestMethod.GET)
    public ListOfProfiles getAllInSet(@RequestParam Set<Long> profileIdSet) {
        LOG.debug("getAllInSet({})", profileIdSet);
        try {
            List<Profile> result = profileServiceInterface.get(profileIdSet);
            LOG.debug("getAllInSet({}) return {} entries", profileIdSet, result.size());
            ListOfProfiles ret = new ListOfProfiles();
            ret.addAll(result);
            return ret;
        } catch (Exception exp) {
             LOG.error("getAllInSet({}) exception ", profileIdSet, exp);
             throw exp;
        }
    }

    @RequestMapping(value = "/profile/forCustomer", method = RequestMethod.GET)
    public PaginationResponse<Profile> getForCustomer(@RequestParam int customerId,
            @RequestParam(required = false) List<ColumnAndSort> sortBy,
            @RequestParam(required = false) PaginationContext<Profile> paginationContext) {

    	if(paginationContext == null) {
    		paginationContext = new PaginationContext<>();
    	}

        LOG.debug("Looking up Profiles for customer {} with last returned page number {}", 
                customerId, paginationContext.getLastReturnedPageNumber());

        PaginationResponse<Profile> ret = new PaginationResponse<>();

        if (paginationContext.isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up Profiles for customer {} with last returned page number {}",
                    customerId, paginationContext.getLastReturnedPageNumber());
            ret.setContext(paginationContext);
            return ret;
        }

        PaginationResponse<Profile> onePage = this.profileServiceInterface
                .getForCustomer(customerId,  sortBy, paginationContext);
        ret.setContext(onePage.getContext());
        ret.getItems().addAll(onePage.getItems());

        LOG.debug("Retrieved {} Profiles for customer {} ", onePage.getItems().size(), 
                customerId);

        return ret;
    }
    
    @RequestMapping(value = "/profile/withChildren", method = RequestMethod.GET)
    public ListOfProfiles getWithChildren(@RequestParam long profileId) {
        LOG.debug("getWithChildren({})", profileId);
        try {
            List<Profile> result = this.profileServiceInterface.getProfileWithChildren(profileId);
            LOG.debug("getWithChildren({}) return {} entries", profileId, result.size());
            ListOfProfiles ret = new ListOfProfiles();
            ret.addAll(result);
            return ret;
        } catch (Exception exp) {
             LOG.error("getWithChildren({}) exception ", profileId, exp);
             throw exp;
        }
    }


    @RequestMapping(value = "/profile/equipmentCounts", method = RequestMethod.GET)
    public ListOfPairLongLong getCountsOfEquipmentThatUseProfiles(@RequestParam Set<Long> profileIdSet) {
        LOG.debug("getCountsOfEquipmentThatUseProfiles({})", profileIdSet);
        
        //first get top-level profiles for the supplied set - only top-level profiles are linked to equipment
        List<PairLongLong> topLevelProfiles = this.profileServiceInterface.getTopLevelProfiles(profileIdSet);
        ListOfPairLongLong ret = new ListOfPairLongLong();
        
        //map each supplied profile to its top-level parent
        Map<Long, Long> profileIdToTopProfileIdMap = new HashMap<>();
        topLevelProfiles.forEach(pair -> profileIdToTopProfileIdMap.put(pair.getValue1(), pair.getValue2()));
        
        //gather top-level profile ids
        Set<Long> topProfileIds = new HashSet<>();
        topLevelProfiles.forEach(pair -> topProfileIds.add(pair.getValue2()));

		
		//TODO: this may be more efficiently done by a specialized count method on equipment datastore

        //now get pages of equipmentIds that refer to the top-level profiles and count the equipmentIds
        PaginationContext<PairLongLong> context = new PaginationContext<>(500);        
		this.equipmentServiceInterface.getEquipmentIdsByProfileIds(topProfileIds, context );

		//prepare map of top-level profileId to the count of equipmentIds
        Map<Long, AtomicInteger> topProfileIdToEquipmentCountsMap = new HashMap<>();
        topProfileIds.forEach(p -> topProfileIdToEquipmentCountsMap.put(p, new AtomicInteger()));

		while(!context.isLastPage()) {
			PaginationResponse<PairLongLong> page = equipmentServiceInterface.getEquipmentIdsByProfileIds(topProfileIds, context );
			context = page.getContext();
			
			page.getItems().forEach(p -> {
				AtomicInteger cnt = topProfileIdToEquipmentCountsMap.get(p.getValue1());
				if(cnt!=null) {
					cnt.incrementAndGet();
				}
			});
			
			LOG.debug("Page {} - counted {} equipmentids", context.getLastReturnedPageNumber(), context.getTotalItemsReturned());
		}			

		
		//package results to get equipment counts for the original profile ids
		profileIdToTopProfileIdMap.forEach((p, tp) -> ret.add(new PairLongLong(p, topProfileIdToEquipmentCountsMap.get(tp).intValue())));
		
        LOG.debug("getCountsOfEquipmentThatUseProfiles({}) return {}", profileIdSet, ret);
        return ret;
    }


}
