package com.telecominfraproject.wlan.portaluser;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;

import com.telecominfraproject.wlan.portaluser.controller.PortalUserController;
import com.telecominfraproject.wlan.portaluser.models.PortalUser;

/**
 * @author dtoptygin
 *
 */
@Component
public class PortalUserServiceLocal implements PortalUserServiceInterface {

    @Autowired private PortalUserController portalUserController;
    private static final Logger LOG = LoggerFactory.getLogger(PortalUserServiceLocal.class);

    
    @Override
    public PortalUser create(PortalUser portalUser) {
        LOG.debug("calling portalUserController.create {} ", portalUser);
        return portalUserController.create(portalUser);
    }

    @Override
    public PortalUser get(long portalUserId) {
        LOG.debug("calling portalUserController.get {} ", portalUserId);
        return portalUserController.get(portalUserId);
    }
    
    @Override
    public PortalUser getOrNull(long portalUserId) {
        LOG.debug("calling portalUserController.getOrNull {} ", portalUserId);
        return portalUserController.getOrNull(portalUserId);
    }
    
    @Override
    public PortalUser getByUsernameOrNull(int customerId, String username) {
        LOG.debug("calling portalUserController.getByUsernameOrNull {} {}", customerId, username);
        return portalUserController.getByUsernameOrNull(customerId, username);
    }
    
    @Override
    public List<PortalUser> get(Set<Long> portalUserIdSet) {
        LOG.debug("calling portalUserController.getAllInSet {} ", portalUserIdSet);
        return portalUserController.getAllInSet(portalUserIdSet);
    }
    
    @Override
    public PaginationResponse<PortalUser> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
    		PaginationContext<PortalUser> context) {
        LOG.debug("calling portalUserController.getForCustomer {} ", customerId);
        return portalUserController.getForCustomer(customerId, sortBy, context);
    }

    @Override
    public PortalUser update(PortalUser portalUser) {
        LOG.debug("calling portalUserController.update {} ", portalUser);
        return portalUserController.update(portalUser);
    }

    @Override
    public PortalUser delete(long portalUserId) {
        LOG.debug("calling portalUserController.delete {} ", portalUserId);
        return portalUserController.delete(portalUserId);
    }

}
