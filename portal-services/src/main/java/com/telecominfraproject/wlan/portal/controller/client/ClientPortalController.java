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
    
    
    /*
     * Client APIs
     */
    
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

    @RequestMapping(value = "/client/blocked", method = RequestMethod.GET)
    public ListOfClients getBlockedClients(@RequestParam int customerId) {
        LOG.debug("getBlockedClients({})", customerId);
        ListOfClients ret = new ListOfClients();
        
        try {
        	
            List<Client> result = clientServiceInterface.getBlockedClients(customerId);
            LOG.debug("getBlockedClients({}) return {} entries", customerId, result.size());
            ret.addAll(result);
        } catch (Exception exp) {
             LOG.error("getBlockedClients({}) exception ", customerId, exp);
             throw exp;
        }

        return ret;

    }

    @RequestMapping(value = "/client", method = RequestMethod.PUT)
    public Client updateClient(@RequestBody Client client) {
        LOG.debug("Updating client {}", client);
        Client ret = clientServiceInterface.getOrNull(client.getCustomerId(), client.getMacAddress());
        if(ret == null) {
        	ret = clientServiceInterface.create(client);
        } else {
        	//to match the OpenAPI docs: last update always wins for this operation
        	client.setLastModifiedTimestamp(ret.getLastModifiedTimestamp());
        	ret = clientServiceInterface.update(client);
        }

        return ret;
    }
    
    @RequestMapping(value = "/client/forCustomer", method = RequestMethod.GET)
    public PaginationResponse<Client> getForCustomer(@RequestParam int customerId,
            @RequestParam(required = false) Set<Long> equipmentIds,    		
            @RequestParam(required = false) String macSubstring,    		
            @RequestParam(required = false) List<ColumnAndSort> sortBy,
            @RequestParam(required = false) PaginationContext<Client> paginationContext) {

    	if(paginationContext == null) {
    		paginationContext = new PaginationContext<>();
    	}

        LOG.debug("Looking up Clients for customer {} equipment {} macSubstring {} with last returned page number {}", 
                customerId, equipmentIds, macSubstring, paginationContext.getLastReturnedPageNumber());
        
        @SuppressWarnings("unchecked")
		PaginationContext<ClientSession> clientSessionContext = (PaginationContext<ClientSession>) 
        		paginationContext.getChildren().getChildren().get("clientSessionChild");
        if (clientSessionContext == null) {
        	clientSessionContext = new PaginationContext<ClientSession>();
        	clientSessionContext.setMaxItemsPerPage(paginationContext.getMaxItemsPerPage());
        	paginationContext.getChildren().getChildren().put("clientSessionChild", clientSessionContext);
        }

        PaginationResponse<Client> clientResponse = new PaginationResponse<>();

        if (paginationContext.isLastPage() || clientSessionContext.isLastPage()) {
            // no more pages available according to the context or child's context
            LOG.debug("No more pages available when looking up Clients for customer {} equipment {} macSubstring {} with last returned page number {}",
                    customerId, equipmentIds, macSubstring, paginationContext.getLastReturnedPageNumber());
            clientResponse.setContext(paginationContext);
            return clientResponse;
        }
        
        clientResponse = processClientResponseWithContextChildren(customerId, equipmentIds, macSubstring, sortBy, paginationContext);

        LOG.debug("Retrieved {} Clients for customer {} equipment {} macSubstring {}", clientResponse.getItems().size(), 
                customerId, equipmentIds, macSubstring);

        return clientResponse;
    }


    /*
     * Client Session APIs
     */

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
            @RequestParam(required = false) String macSubstring,
            @RequestParam(required = false) List<ColumnAndSort> sortBy,
            @RequestParam(required = false) PaginationContext<ClientSession> paginationContext) {

    	if(paginationContext == null) {
    		paginationContext = new PaginationContext<>();
    	}

        LOG.debug("Looking up Client Sessions for customer {} equipment {} locations {} macSubstring {} with last returned page number {}", 
                customerId, equipmentIds, locationIds, macSubstring, paginationContext.getLastReturnedPageNumber());

        PaginationResponse<ClientSession> ret = new PaginationResponse<>();

        if (paginationContext.isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up Client Sessions for customer {} equipment {} locations {} macSubstring {} with last returned page number {}",
                    customerId, equipmentIds, locationIds, macSubstring, paginationContext.getLastReturnedPageNumber());
            ret.setContext(paginationContext);
            return ret;
        }

        PaginationResponse<ClientSession> onePage = this.clientServiceInterface
                .getSessionsForCustomer(customerId, equipmentIds, locationIds, macSubstring, sortBy, paginationContext);
        ret.setContext(onePage.getContext());
        ret.getItems().addAll(onePage.getItems());

        LOG.debug("Retrieved {} Client Sessions for customer {} equipment {} locations {} macSubstring {} ", onePage.getItems().size(), 
                customerId, equipmentIds, locationIds, macSubstring);

        return ret;
    }
    
    private PaginationResponse<Client> processClientResponseWithContextChildren(int customerId, Set<Long> equipmentIds, String macSubstring, 
    		List<ColumnAndSort> sortBy, PaginationContext<Client> context) {
    	PaginationResponse<Client> clientResponse = new PaginationResponse<>();
    	
    	@SuppressWarnings("unchecked")
		PaginationContext<ClientSession> clientSessionContext = (PaginationContext<ClientSession>) 
        		context.getChildren().getChildren().get("clientSessionChild");
    	
    	PaginationResponse<ClientSession> onePageSession = this.clientServiceInterface
        		.getSessionsForCustomer(customerId, equipmentIds, null, macSubstring, sortBy, clientSessionContext);
        
        // Get clients by MacAddress from the returned client sessions
        Set<MacAddress> macSet = new HashSet<>();
        onePageSession.getItems().forEach(y -> macSet.add(y.getMacAddress()));
        PaginationContext<ClientSession> returnedContextSession = onePageSession.getContext();
        List<Client> clientList = this.clientServiceInterface.get(customerId, macSet);
        clientList.sort((c1, c2) -> c1.getMacAddress().compareTo(c2.getMacAddress()));
        
        // Set final list of clients, sorted
        clientResponse.setItems(clientList);
        
        
        // Set returned session context as child
        context.getChildren().getChildren().put("clientSessionChild", returnedContextSession);
        
        // Set other applicable response parameters as per returned session context
        context.setLastReturnedPageNumber(returnedContextSession.getLastReturnedPageNumber());
        context.setLastPage(returnedContextSession.isLastPage());
        
        // Set total items returned based on actual list of client objects returning
        context.setTotalItemsReturned(context.getTotalItemsReturned() + clientList.size());
        
        // Set final context for PaginationResponse
        clientResponse.setContext(context);
        
        return clientResponse;
    }

}
