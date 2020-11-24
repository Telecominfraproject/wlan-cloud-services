package com.telecominfraproject.wlan.portaluser.controller;

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
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;
import com.telecominfraproject.wlan.systemevent.models.SystemEvent;

import com.telecominfraproject.wlan.portaluser.datastore.PortalUserDatastore;
import com.telecominfraproject.wlan.portaluser.models.PortalUser;
import com.telecominfraproject.wlan.portaluser.models.events.PortalUserAddedEvent;
import com.telecominfraproject.wlan.portaluser.models.events.PortalUserChangedEvent;
import com.telecominfraproject.wlan.portaluser.models.events.PortalUserRemovedEvent;


/**
 * @author dtoptygin
 *
 */
@RestController
@Transactional
@RequestMapping(value="/api/portalUser")
public class PortalUserController {

    private static final Logger LOG = LoggerFactory.getLogger(PortalUserController.class);

    public static class ListOfPortalUsers extends ArrayList<PortalUser> {
        private static final long serialVersionUID = 3070319062835500930L;
    }

    @Autowired private PortalUserDatastore portalUserDatastore;
    @Autowired private CloudEventDispatcherInterface cloudEventDispatcher;

    
    /**
     * Creates new PortalUser.
     *  
     * @param PortalUser
     * @return stored PortalUser object
     * @throws RuntimeException if PortalUser record already exists
     */
    @RequestMapping(method=RequestMethod.POST)
    public PortalUser create(@RequestBody PortalUser portalUser ) {

        LOG.debug("Creating PortalUser {}", portalUser);

        if (BaseJsonModel.hasUnsupportedValue(portalUser)) {
            LOG.error("Failed to create PortalUser, request contains unsupported value: {}", portalUser);
            throw new DsDataValidationException("PortalUser contains unsupported value");
        }

        long ts = System.currentTimeMillis();
        if (portalUser.getCreatedTimestamp() == 0) {
        	portalUser.setCreatedTimestamp(ts);
        }
        portalUser.setLastModifiedTimestamp(ts);

        PortalUser ret = portalUserDatastore.create(portalUser);

        LOG.debug("Created PortalUser {}", ret);

        PortalUserAddedEvent event = new PortalUserAddedEvent(ret);
        publishEvent(event);


        return ret;
    }
    
    /**
     * Retrieves PortalUser by id
     * @param portalUserId
     * @return PortalUser for the supplied id
     */
    @RequestMapping(method=RequestMethod.GET)
    public PortalUser get(@RequestParam long portalUserId ) {
        
        LOG.debug("Retrieving PortalUser {}", portalUserId);
        
        PortalUser ret = portalUserDatastore.get(portalUserId);

        LOG.debug("Retrieved PortalUser {}", ret);

        return ret;
    }

    /**
     * Retrieves PortalUser by id
     * @param portalUserId
     * @return PortalUser for the supplied id
     */
    @RequestMapping(value = "/orNull", method=RequestMethod.GET)
    public PortalUser getOrNull(@RequestParam long portalUserId ) {
        
        LOG.debug("Retrieving PortalUser {}", portalUserId);
        
        PortalUser ret = portalUserDatastore.getOrNull(portalUserId);

        LOG.debug("Retrieved PortalUser {}", ret);

        return ret;
    }

    /**
     * Retrieves PortalUser by user name
     * @param customerId
     * @param username
     * @return PortalUser for the supplied customerId and username
     */
    @RequestMapping(value = "/byUsernameOrNull", method=RequestMethod.GET)
    public PortalUser getByUsernameOrNull(@RequestParam int customerId, @RequestParam String username) {
        
        LOG.debug("Retrieving PortalUser getByUsername {} {}", customerId, username);
        
        PortalUser ret = portalUserDatastore.getByUsernameOrNull(customerId, username);

        LOG.debug("Retrieved PortalUser getByUsername {} {} : {}", customerId, username, ret);

        return ret;
    }

    @RequestMapping(value = "/inSet", method = RequestMethod.GET)
    public ListOfPortalUsers getAllInSet(@RequestParam Set<Long> portalUserIdSet) {
        LOG.debug("getAllInSet({})", portalUserIdSet);
        try {
            List<PortalUser> result = portalUserDatastore.get(portalUserIdSet);
            LOG.debug("getAllInSet({}) return {} entries", portalUserIdSet, result.size());
            ListOfPortalUsers ret = new ListOfPortalUsers();
            ret.addAll(result);
            return ret;
        } catch (Exception exp) {
             LOG.error("getAllInSet({}) exception ", portalUserIdSet, exp);
             throw exp;
        }
    }
    
    @RequestMapping(value = "/customerIdsForUsername", method = RequestMethod.GET)
    public List<Integer> getCustomerIdsForUsername(@RequestParam String username) {
        LOG.debug("getCustomerIdsForUsername({})", username);
        try {
            List<Integer> result = portalUserDatastore.getCustomerIdsForUsername(username);
            LOG.debug("getCustomerIdsForUsername({}) return {} entries", result, result.size());
            
            return result;
        } catch (Exception exp) {
             LOG.error("getCustomerIdsForUsername({}) exception ", username, exp);
             throw exp;
        }
    }

    @RequestMapping(value = "/forCustomer", method = RequestMethod.GET)
    public PaginationResponse<PortalUser> getForCustomer(@RequestParam int customerId,
            @RequestParam List<ColumnAndSort> sortBy,
            @RequestParam(required = false) PaginationContext<PortalUser> paginationContext) {

    	if(paginationContext == null) {
    		paginationContext = new PaginationContext<>();
    	}

        LOG.debug("Looking up PortalUsers for customer {} with last returned page number {}", 
                customerId, paginationContext.getLastReturnedPageNumber());

        PaginationResponse<PortalUser> ret = new PaginationResponse<>();

        if (paginationContext.isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up PortalUsers for customer {} with last returned page number {}",
                    customerId, paginationContext.getLastReturnedPageNumber());
            ret.setContext(paginationContext);
            return ret;
        }

        PaginationResponse<PortalUser> onePage = this.portalUserDatastore
                .getForCustomer(customerId,  sortBy, paginationContext);
        ret.setContext(onePage.getContext());
        ret.getItems().addAll(onePage.getItems());

        LOG.debug("Retrieved {} PortalUsers for customer {} ", onePage.getItems().size(), 
                customerId);

        return ret;
    }
    
    /**
     * Updates PortalUser record
     * 
     * @param PortalUser
     * @return updated PortalUser object
     * @throws RuntimeException if PortalUser record does not exist or if it was modified concurrently
     */
    @RequestMapping(method=RequestMethod.PUT)
    public PortalUser update(@RequestBody PortalUser portalUser){
        
        LOG.debug("Updating PortalUser {}", portalUser);
        
        if (BaseJsonModel.hasUnsupportedValue(portalUser)) {
            LOG.error("Failed to update PortalUser, request contains unsupported value: {}", portalUser);
            throw new DsDataValidationException("PortalUser contains unsupported value");
        }

        PortalUser ret = portalUserDatastore.update(portalUser);

        LOG.debug("Updated PortalUser {}", ret);

        PortalUserChangedEvent event = new PortalUserChangedEvent(ret);
        publishEvent(event);

        return ret;
    }
    
    /**
     * Deletes PortalUser record
     * 
     * @param portalUserId
     * @return deleted PortalUser object
     */
    @RequestMapping(method=RequestMethod.DELETE)
    public PortalUser delete(@RequestParam long portalUserId ) {
        
        LOG.debug("Deleting PortalUser {}", portalUserId);
        
        PortalUser ret = portalUserDatastore.delete(portalUserId);

        LOG.debug("Deleted PortalUser {}", ret);
        
        PortalUserRemovedEvent event = new PortalUserRemovedEvent(ret);
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

    
}
