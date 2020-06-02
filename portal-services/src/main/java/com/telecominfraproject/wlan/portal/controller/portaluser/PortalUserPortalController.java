package com.telecominfraproject.wlan.portal.controller.portaluser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.codec.digest.Crypt;
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
import com.telecominfraproject.wlan.portaluser.PortalUserServiceInterface;
import com.telecominfraproject.wlan.portaluser.models.PortalUser;

/**
 * @author dtoptygin
 *
 */
@RestController
@RequestMapping(value = "/portal")
public class PortalUserPortalController  {

    private static final Logger LOG = LoggerFactory.getLogger(PortalUserPortalController.class);

    // For serialization:
    // need these wrapper classes so that each element in returned container is
    // marked with "model_type" attribute
    // all callers of this class can deal with normal collections
    public static class ListOfPortalUsers extends ArrayList<PortalUser> {
        private static final long serialVersionUID = 1158560190003268713L;
    }

    /** For the format of the Salt @see Crypt.crypt()
    * SHA-512 salts start with {@code $6$} and are up to 16 chars long. 
    * The chars in the salt string are drawn from the set {@code [a-zA-Z0-9./]}.
    */
    private static final String saltForThePasswords = System.getProperty("tip.wlan.saltForThePasswords", "$6$V3ZcYJP/");

    @Autowired
    private PortalUserServiceInterface portalUserServiceInterface;

    @RequestMapping(value = "/portalUser", method = RequestMethod.GET)
    public PortalUser getPortalUser(@RequestParam long portalUserId) {
        LOG.debug("Getting portalUser {}", portalUserId);

        PortalUser portalUser = portalUserServiceInterface.get(portalUserId);

        return portalUser;
    }

    @RequestMapping(value = "/portalUser", method = RequestMethod.PUT)
    public PortalUser updatePortalUser(@RequestBody PortalUser portalUser) {
        LOG.debug("Updating portalUser {}", portalUser.getId());

        PortalUser origPortalUser = portalUserServiceInterface.get(portalUser.getId());
        
        if( ! origPortalUser.getPassword().equals(portalUser.getPassword()) ) {
	        //a new password has been supplied - need to re-encrypt it and store it
        	encryptPassword(portalUser);
        }

        PortalUser ret = portalUserServiceInterface.update(portalUser);

        return ret;
    }
    
    private void encryptPassword(PortalUser portalUser) {
        String incomingPassword = portalUser.getPassword();
        if(incomingPassword == null || incomingPassword.isEmpty()) {
        	throw new IllegalArgumentException("Invalid password - cannot be null or empty");	
        }
        
        String saltedPassword = Crypt.crypt(incomingPassword, saltForThePasswords);
        String pwdWithoutSalt = saltedPassword.substring(saltForThePasswords.length()+1);
        
        portalUser.setPassword(pwdWithoutSalt);    	
    }

    @RequestMapping(value = "/portalUser", method = RequestMethod.POST)
    public PortalUser createPortalUser(@RequestBody PortalUser portalUser) {
        LOG.debug("Creating portalUser {}", portalUser.getId());

    	encryptPassword(portalUser);

        PortalUser ret = portalUserServiceInterface.create(portalUser);

        return ret;
    }

    @RequestMapping(value = "/portalUser", method = RequestMethod.DELETE)
    public PortalUser deletePortalUser(@RequestParam long portalUserId) {
        LOG.debug("Deleting portalUser {}", portalUserId);

        PortalUser ret = portalUserServiceInterface.delete(portalUserId);

        return ret;
    }
    

    @RequestMapping(value = "/portalUser/inSet", method = RequestMethod.GET)
    public ListOfPortalUsers getAllInSet(@RequestParam Set<Long> portalUserIdSet) {
        LOG.debug("getAllInSet({})", portalUserIdSet);
        try {
            List<PortalUser> result = portalUserServiceInterface.get(portalUserIdSet);
            LOG.debug("getAllInSet({}) return {} entries", portalUserIdSet, result.size());
            ListOfPortalUsers ret = new ListOfPortalUsers();
            ret.addAll(result);
            return ret;
        } catch (Exception exp) {
             LOG.error("getAllInSet({}) exception ", portalUserIdSet, exp);
             throw exp;
        }
    }

    @RequestMapping(value = "/portalUser/forCustomer", method = RequestMethod.GET)
    public PaginationResponse<PortalUser> getForCustomer(@RequestParam int customerId,
            @RequestParam(required = false) List<ColumnAndSort> sortBy,
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

        PaginationResponse<PortalUser> onePage = this.portalUserServiceInterface
                .getForCustomer(customerId,  sortBy, paginationContext);
        ret.setContext(onePage.getContext());
        ret.getItems().addAll(onePage.getItems());

        LOG.debug("Retrieved {} PortalUsers for customer {} ", onePage.getItems().size(), 
                customerId);

        return ret;
    }
    
    /**
     * Retrieves PortalUser by user name
     * @param customerId
     * @param username
     * @return PortalUser for the supplied customerId and username
     */
    @RequestMapping(value = "/portalUser/byUsernameOrNull", method=RequestMethod.GET)
    public PortalUser getByUsernameOrNull(@RequestParam int customerId, @RequestParam String username) {
        
        LOG.debug("Retrieving PortalUser getByUsername {} {}", customerId, username);
        
        PortalUser ret = portalUserServiceInterface.getByUsernameOrNull(customerId, username);

        LOG.debug("Retrieved PortalUser getByUsername {} {} : {}", customerId, username, ret);

        return ret;
    }
   
}
