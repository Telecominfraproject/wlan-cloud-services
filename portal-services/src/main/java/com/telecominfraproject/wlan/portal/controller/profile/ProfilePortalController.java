package com.telecominfraproject.wlan.portal.controller.profile;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

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

    @Autowired
    private ProfileServiceInterface profileServiceInterface;

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
            @RequestParam PaginationContext<Profile> paginationContext) {

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

    
}
