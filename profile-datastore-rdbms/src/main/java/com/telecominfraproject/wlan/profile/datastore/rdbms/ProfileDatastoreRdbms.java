package com.telecominfraproject.wlan.profile.datastore.rdbms;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;

import com.telecominfraproject.wlan.profile.datastore.ProfileDatastore;
import com.telecominfraproject.wlan.profile.models.Profile;

/**
 * @author dtoptygin
 *
 */
@Configuration
public class ProfileDatastoreRdbms implements ProfileDatastore {

    @Autowired ProfileDAO profileDAO;

    @Override
    public Profile create(Profile profile) {
        return profileDAO.create(profile);
    }

    @Override
    public Profile get(long profileId) {
        return profileDAO.get(profileId);
    }

    @Override
    public Profile getOrNull(long profileId) {
        return profileDAO.getOrNull(profileId);
    }
    
    @Override
    public Profile update(Profile profile) {
        return profileDAO.update(profile);
    }

    @Override
    public Profile delete(long profileId) {
        return profileDAO.delete(profileId);
    }
    
    @Override
    public List<Profile> get(Set<Long> profileIdSet) {
    	return profileDAO.get(profileIdSet);
    }
    
    @Override
    public PaginationResponse<Profile> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
    		PaginationContext<Profile> context) {
    	return profileDAO.getForCustomer( customerId, sortBy, context);
    }
    
    @Override
    public List<Profile> getProfileWithChildren(long profileId) {
    	return profileDAO.getProfileWithChildren(profileId);
    }
}
