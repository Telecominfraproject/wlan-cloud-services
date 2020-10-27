package com.telecominfraproject.wlan.profile;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pair.PairLongLong;
import com.telecominfraproject.wlan.profile.controller.ProfileController;
import com.telecominfraproject.wlan.profile.models.Profile;
import com.telecominfraproject.wlan.profile.models.ProfileType;

/**
 * @author dtoptygin
 *
 */
@Component
public class ProfileServiceLocal implements ProfileServiceInterface {

    @Autowired private ProfileController profileController;
    private static final Logger LOG = LoggerFactory.getLogger(ProfileServiceLocal.class);

    
    @Override
    public Profile create(Profile profile) {
        LOG.debug("calling profileController.create {} ", profile);
        return profileController.create(profile);
    }

    @Override
    public Profile get(long profileId) {
        LOG.debug("calling profileController.get {} ", profileId);
        return profileController.get(profileId);
    }
    
    @Override
    public Profile getOrNull(long profileId) {
        LOG.debug("calling profileController.getOrNull {} ", profileId);
        return profileController.getOrNull(profileId);
    }
    
    @Override
    public List<Profile> get(Set<Long> profileIdSet) {
        LOG.debug("calling profileController.getAllInSet {} ", profileIdSet);
        return profileController.getAllInSet(profileIdSet);
    }
    
    @Override
    public PaginationResponse<Profile> getForCustomer(int customerId, ProfileType profileType, List<ColumnAndSort> sortBy,
    		PaginationContext<Profile> context) {
        LOG.debug("calling profileController.getForCustomer {} ", customerId);
        return profileController.getForCustomer(customerId, profileType, sortBy, context);
    }

    @Override
    public List<Profile> getProfileWithChildren(long profileId) {
        LOG.debug("calling profileController.getProfileWithChildren {} ", profileId);
        return profileController.getWithChildren(profileId);
    }
    
    @Override
    public Profile update(Profile profile) {
        LOG.debug("calling profileController.update {} ", profile);
        return profileController.update(profile);
    }

    @Override
    public Profile delete(long profileId) {
        LOG.debug("calling profileController.delete {} ", profileId);
        return profileController.delete(profileId);
    }

    @Override
    public List<PairLongLong> getTopLevelProfiles(Set<Long> profileIds) {
        LOG.debug("calling profileController.getTopLevelProfiles {} ", profileIds);
        return profileController.getTopLevelProfiles(profileIds);
    }
}
