package com.telecominfraproject.wlan.portaluser.datastore.rdbms;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;

import com.telecominfraproject.wlan.portaluser.datastore.PortalUserDatastore;
import com.telecominfraproject.wlan.portaluser.models.PortalUser;

/**
 * @author dtoptygin
 *
 */
@Configuration
public class PortalUserDatastoreRdbms implements PortalUserDatastore {

    @Autowired PortalUserDAO portalUserDAO;

    @Override
    public PortalUser create(PortalUser portalUser) {
        return portalUserDAO.create(portalUser);
    }

    @Override
    public PortalUser get(long portalUserId) {
        return portalUserDAO.get(portalUserId);
    }

    @Override
    public PortalUser getOrNull(long portalUserId) {
        return portalUserDAO.getOrNull(portalUserId);
    }
    
    @Override
    public PortalUser getByUsernameOrNull(int customerId, String username) {
        return portalUserDAO.getByUsernameOrNull(customerId, username);
    }
    
    @Override
    public List<PortalUser> getUsersForUsername(String username) {
        return portalUserDAO.getUsersForUsername(username);
    }
    
    
    @Override
    public PortalUser update(PortalUser portalUser) {
        return portalUserDAO.update(portalUser);
    }

    @Override
    public PortalUser delete(long portalUserId) {
        return portalUserDAO.delete(portalUserId);
    }
    
    @Override
    public List<PortalUser> get(Set<Long> portalUserIdSet) {
    	return portalUserDAO.get(portalUserIdSet);
    }
    
    @Override
    public PaginationResponse<PortalUser> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
    		PaginationContext<PortalUser> context) {

    	if(context == null) {
    		context = new PaginationContext<>();
    	}

    	return portalUserDAO.getForCustomer( customerId, sortBy, context);
    }
}
