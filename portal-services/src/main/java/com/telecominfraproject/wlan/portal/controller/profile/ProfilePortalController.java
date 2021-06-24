package com.telecominfraproject.wlan.portal.controller.profile;

import java.util.ArrayList;
import java.util.Collections;
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
import com.telecominfraproject.wlan.profile.models.ProfileType;

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

        List<PairLongLong> countOfEquipmentThatUsesProfile = getCountsOfEquipmentThatUseProfiles(Collections.singleton(profileId));
        
        countOfEquipmentThatUsesProfile.forEach(pair -> {
            //value1 is profileId, value2 is count of equipment that uses it
            if(pair.getValue2()!=0) {
                throw new IllegalStateException("Profile with id "+ profileId + " is in use by " +pair.getValue2()+ " network equipment and cannot be deleted");
            }
        });
        
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
    		@RequestParam(required = false) ProfileType profileType,
    		@RequestParam(required = false) String nameSubstring,
            @RequestParam(required = false) List<ColumnAndSort> sortBy,
            @RequestParam(required = false) PaginationContext<Profile> paginationContext) {

        LOG.debug("Looking up Profiles for customer {}", customerId);

        PaginationResponse<Profile> ret = new PaginationResponse<>();

        PaginationResponse<Profile> onePage = this.profileServiceInterface
                .getForCustomer(customerId, 
                		profileType,
                		nameSubstring,
                		sortBy, 
                		paginationContext);
        
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
        
        Map<Long, AtomicInteger> profileIdToCountMap = new HashMap<Long, AtomicInteger>();
        profileIdSet.forEach(profileId -> profileIdToCountMap.put(profileId, new AtomicInteger(0)));
        
        //Maps child at all top level profiles that reference it. A top level profile references itself int this map
        Map<Long, List<Long>> childProfileToTopProfileMap = new HashMap<Long, List<Long>>();
        
        List<PairLongLong> topLevelProfileList = profileServiceInterface.getTopLevelProfiles(profileIdSet);
        Set<Long> topProfileIdSet= new HashSet<Long>();
        
        topLevelProfileList.forEach(pair ->
        {
            if (childProfileToTopProfileMap.putIfAbsent(
                    pair.getValue1(), 
                    new ArrayList<Long>() {{ add(pair.getValue2());}}
                    ) != null)
            {
                childProfileToTopProfileMap.compute(pair.getValue1(), (k,v) -> 
                new ArrayList<Long>() {{ 
                    addAll(v);
                    add(pair.getValue2());}});
            }
            topProfileIdSet.add(pair.getValue2());
        });
        
        Map<Long, AtomicInteger> topProfileToEquipmentCountMap = new HashMap<>();
        topProfileIdSet.forEach(p -> topProfileToEquipmentCountMap.put(p, new AtomicInteger()));
        
        PaginationContext<PairLongLong> context = new PaginationContext<>(500);   
        equipmentServiceInterface.getEquipmentIdsByProfileIds(topProfileIdSet, context);
        
        while(!context.isLastPage()) {
            PaginationResponse<PairLongLong> page = equipmentServiceInterface.getEquipmentIdsByProfileIds(topProfileIdSet, context );
            context = page.getContext();
            
            page.getItems().forEach(p -> {
                AtomicInteger cnt = topProfileToEquipmentCountMap.get(p.getValue1());
                if(cnt!=null) {
                    cnt.incrementAndGet();
                }
            });
            
            LOG.debug("Page {} - counted {} equipmentids", context.getLastReturnedPageNumber(), context.getTotalItemsReturned());
        }
        
        // assemble profile count for return, using child to top level profile id map
        childProfileToTopProfileMap.forEach((childKey,TopLevelIdList) ->
        {
            for (Long topProfileId : TopLevelIdList)
            {
                profileIdToCountMap.get(childKey).addAndGet(topProfileToEquipmentCountMap.get(topProfileId).get());
            }
        });
      //package results to get equipment counts for the original profile ids
        ListOfPairLongLong ret = new ListOfPairLongLong();
        profileIdToCountMap.forEach((id, count) -> ret.add(new PairLongLong(id, count.get())));
        
        return ret;
    }
}
