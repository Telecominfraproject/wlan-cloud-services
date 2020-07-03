package com.telecominfraproject.wlan.portal.controller.client;

import java.util.ArrayList;
import java.util.HashSet;
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

import com.telecominfraproject.wlan.client.ClientServiceInterface;
import com.telecominfraproject.wlan.client.models.Client;
import com.telecominfraproject.wlan.client.session.models.ClientSession;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.location.models.Location;

/**
 * @author dtoptygin
 *
 */
@RestController
@RequestMapping(value = "/portal")
public class ClientPortalController  {

    private static final Logger LOG = LoggerFactory.getLogger(ClientPortalController.class);

    // For serialization:
    // need these wrapper classes so that each element in returned container is
    // marked with "model_type" attribute
    // all callers of this class can deal with normal collections
    public static class ListOfClients extends ArrayList<Client> {
        private static final long serialVersionUID = 1158560190003268713L;
    }

    public static class ListOfClientSessions extends ArrayList<ClientSession> {
        private static final long serialVersionUID = 1158560190003268713L;
    }

    @Autowired
    private ClientServiceInterface clientServiceInterface;
    
    
    @RequestMapping(value = "/client/inSet", method = RequestMethod.GET)
    public ListOfClients getAllClientsInSet(@RequestParam int customerId, @RequestParam Set<String> clientMacs) {
        LOG.debug("getAllClientsInSet({} {})", customerId, clientMacs);
        ListOfClients ret = new ListOfClients();
        
        if(clientMacs==null || clientMacs.isEmpty()) {
        	return ret;
        }

        try {
        	Set<MacAddress> macSet = new HashSet<>();
        	clientMacs.forEach(m -> macSet.add(new MacAddress(m)));
        	
            List<Client> result = clientServiceInterface.get(customerId, macSet);
            LOG.debug("getAllClientsInSet({} {}) return {} entries", customerId, clientMacs, result.size());
            ret.addAll(result);
        } catch (Exception exp) {
             LOG.error("getAllClientsInSet({} {}) exception ", customerId, clientMacs, exp);
             throw exp;
        }

        return ret;

    }
    
    @RequestMapping(value = "/client", method = RequestMethod.PUT)
    public Client updateClient(@RequestBody Client client) {
        LOG.debug("Updating client {}", client);

        Client ret = clientServiceInterface.update(client);

        return ret;
    }



    @RequestMapping(value = "/client/session/inSet", method = RequestMethod.GET)
    public ListOfClientSessions getAllClientSessionsInSet(@RequestParam int customerId, @RequestParam Set<String> clientMacs) {
        LOG.debug("getAllClientSessionsInSet({} {})", customerId, clientMacs);
        ListOfClientSessions ret = new ListOfClientSessions();
        
        if(clientMacs==null || clientMacs.isEmpty()) {
        	return ret;
        }

        try {
        	Set<MacAddress> macSet = new HashSet<>();
        	clientMacs.forEach(m -> macSet.add(new MacAddress(m)));
        	
            List<ClientSession> result = clientServiceInterface.getSessions(customerId, macSet);
            LOG.debug("getAllClientSessionsInSet({} {}) return {} entries", customerId, clientMacs, result.size());
            ret.addAll(result);
        } catch (Exception exp) {
             LOG.error("getAllClientSessionsInSet({} {}) exception ", customerId, clientMacs, exp);
             throw exp;
        }

        return ret;

    }


    @RequestMapping(value = "/client/session/forCustomer", method = RequestMethod.GET)
    public PaginationResponse<ClientSession> getClientSessionsForCustomer(@RequestParam int customerId,
            @RequestParam(required = false) Set<Long> equipmentIds,    		
            @RequestParam(required = false) Set<Long> locationIds,    		
            @RequestParam(required = false) List<ColumnAndSort> sortBy,
            @RequestParam(required = false) PaginationContext<ClientSession> paginationContext) {

    	if(paginationContext == null) {
    		paginationContext = new PaginationContext<>();
    	}

        LOG.debug("Looking up Client Sessions for customer {} equipment {} locations {} with last returned page number {}", 
                customerId, equipmentIds, locationIds, paginationContext.getLastReturnedPageNumber());

        PaginationResponse<ClientSession> ret = new PaginationResponse<>();

        if (paginationContext.isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up Client Sessions for customer {} equipment {} with last returned page number {}",
                    customerId, equipmentIds, paginationContext.getLastReturnedPageNumber());
            ret.setContext(paginationContext);
            return ret;
        }

        PaginationResponse<ClientSession> onePage = this.clientServiceInterface
                .getSessionsForCustomer(customerId, equipmentIds, locationIds,  sortBy, paginationContext);
        ret.setContext(onePage.getContext());
        ret.getItems().addAll(onePage.getItems());

        LOG.debug("Retrieved {} Client Sessions for customer {} equipment {} locations {}", onePage.getItems().size(), 
                customerId, equipmentIds, locationIds);

        return ret;
    }

    
}
