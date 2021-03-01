package com.telecominfraproject.wlan.client.controller;

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

import com.telecominfraproject.wlan.client.datastore.ClientDatastore;
import com.telecominfraproject.wlan.client.models.Client;
import com.telecominfraproject.wlan.client.models.events.ClientAddedEvent;
import com.telecominfraproject.wlan.client.models.events.ClientBlockListChangedEvent;
import com.telecominfraproject.wlan.client.models.events.ClientChangedEvent;
import com.telecominfraproject.wlan.client.models.events.ClientRemovedEvent;
import com.telecominfraproject.wlan.client.models.events.ClientSessionChangedEvent;
import com.telecominfraproject.wlan.client.models.events.ClientSessionRemovedEvent;
import com.telecominfraproject.wlan.client.session.models.ClientSession;
import com.telecominfraproject.wlan.client.validator.ClientSessionValidator;
import com.telecominfraproject.wlan.client.validator.ClientSessionValidatorException;
import com.telecominfraproject.wlan.cloudeventdispatcher.CloudEventDispatcherInterface;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;
import com.telecominfraproject.wlan.systemevent.models.SystemEvent;


/**
 * @author dtoptygin
 *
 */
@RestController
@Transactional
@RequestMapping(value="/api/client")
public class ClientController {

    private static final Logger LOG = LoggerFactory.getLogger(ClientController.class);

    public static class ListOfClients extends ArrayList<Client> {
        private static final long serialVersionUID = 3070319062835500930L;
    }

    public static class ListOfClientSessions extends ArrayList<ClientSession> {
        private static final long serialVersionUID = 3070319062835500930L;
    }

    @Autowired private ClientDatastore clientDatastore;
    @Autowired private CloudEventDispatcherInterface cloudEventDispatcher;
    @Autowired private ClientSessionValidator clientSessionValidator;
    
    /**
     * Creates new Client.
     *  
     * @param Client
     * @return stored Client object
     * @throws RuntimeException if Client record already exists
     */
    @RequestMapping(method=RequestMethod.POST)
    public Client create(@RequestBody Client client ) {

        LOG.debug("Creating Client {}", client);

        if (BaseJsonModel.hasUnsupportedValue(client)) {
            LOG.error("Failed to create Client, request contains unsupported value: {}", client);
            throw new DsDataValidationException("Client contains unsupported value");
        }

        long ts = System.currentTimeMillis();
        if (client.getCreatedTimestamp() == 0) {
        	client.setCreatedTimestamp(ts);
        }
        client.setLastModifiedTimestamp(ts);

        Client ret = clientDatastore.create(client);

        LOG.debug("Created Client {}", ret);

        List<SystemEvent> events = new ArrayList<>();
        events.add(new ClientAddedEvent(ret));
        
        if(ret.isNeedToUpdateBlocklist()) {
        	events.add(new ClientBlockListChangedEvent(ret));
        }
        
        publishEvents(events);


        return ret;
    }

    /**
     * Retrieves Client by id
     * @param clientId
     * @return Client for the supplied id
     */
    @RequestMapping(value = "/orNull", method=RequestMethod.GET)
    public Client getOrNull(@RequestParam int customerId,  @RequestParam MacAddress macAddress) {
        
        LOG.debug("Retrieving Client {} {}", customerId, macAddress);
        
        Client ret = clientDatastore.getOrNull(customerId, macAddress);

        LOG.debug("Retrieved Client {}", ret);

        return ret;
    }

    @RequestMapping(value = "/inSet", method = RequestMethod.GET)
    public ListOfClients getAllInSet(@RequestParam int customerId,  @RequestParam Set<MacAddress> clientMacSet) {
        LOG.debug("getAllInSet({}, {})", customerId, clientMacSet);
        try {
            List<Client> result = clientDatastore.get(customerId, clientMacSet);
            LOG.debug("getAllInSet({}, {}) return {} entries", customerId, clientMacSet, result.size());
            ListOfClients ret = new ListOfClients();
            ret.addAll(result);
            return ret;
        } catch (Exception exp) {
             LOG.error("getAllInSet({}, {}) exception ", customerId, clientMacSet, exp);
             throw exp;
        }
    }
    
    @RequestMapping(value = "/searchByMac", method = RequestMethod.GET)
    public PaginationResponse<Client> searchByMacAddress(@RequestParam int customerId,  
    		@RequestParam(required = false) String macSubstring,      
	        @RequestParam(required = false) List<ColumnAndSort> sortBy,
	        @RequestParam(required = false) PaginationContext<Client> paginationContext) {
	        	LOG.debug("searchByMacAddress({}, {})", customerId, macSubstring);
	
		if(paginationContext == null) {
			paginationContext = new PaginationContext<>();
		}
	
	    LOG.debug("Looking up Clients for customer {} and macSubstring {} with last returned page number {}", 
	            customerId, macSubstring, paginationContext.getLastReturnedPageNumber());
	
	    PaginationResponse<Client> ret = new PaginationResponse<>();
	
	    if (paginationContext.isLastPage()) {
	        // no more pages available according to the context
	        LOG.debug(
	                "No more pages available when looking up Clients for customer {} and macSubstring {} with last returned page number {}",
	                customerId, macSubstring, paginationContext.getLastReturnedPageNumber());
	        ret.setContext(paginationContext);
	        return ret;
	    }
	
	    PaginationResponse<Client> onePage = this.clientDatastore
	            .searchByMacAddress(customerId, macSubstring, sortBy, paginationContext);
	    ret.setContext(onePage.getContext());
	    ret.getItems().addAll(onePage.getItems());
	
	    LOG.debug("Retrieved {} Clients for customer {} and macSubstring {} ", onePage.getItems().size(), 
	            customerId, macSubstring);
	
	    return ret;
        
    }
    
    /**
     * @param customerId
     * @return list of Clients for the customer that are marked as blocked. This per-customer list of blocked clients is pushed to every AP, so it has to be limited in size. 
     */
    @RequestMapping(value = "/blocked", method = RequestMethod.GET)
    public ListOfClients getBlockedClients(@RequestParam int customerId) {
        LOG.debug("getBlockedClients({})", customerId);
        try {
            List<Client> result = clientDatastore.getBlockedClients(customerId);
            LOG.debug("getBlockedClients({}) return {} entries", customerId, result.size());
            ListOfClients ret = new ListOfClients();
            ret.addAll(result);
            return ret;
        } catch (Exception exp) {
             LOG.error("getBlockedClients({}) exception ", customerId, exp);
             throw exp;
        }
    }


    @RequestMapping(value = "/forCustomer", method = RequestMethod.GET)
    public PaginationResponse<Client> getForCustomer(@RequestParam int customerId,
            @RequestParam List<ColumnAndSort> sortBy,
            @RequestParam(required = false) PaginationContext<Client> paginationContext) {

    	if(paginationContext == null) {
    		paginationContext = new PaginationContext<>();
    	}

        LOG.debug("Looking up Clients for customer {} with last returned page number {}", 
                customerId, paginationContext.getLastReturnedPageNumber());

        PaginationResponse<Client> ret = new PaginationResponse<>();

        if (paginationContext.isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up Clients for customer {} with last returned page number {}",
                    customerId, paginationContext.getLastReturnedPageNumber());
            ret.setContext(paginationContext);
            return ret;
        }

        PaginationResponse<Client> onePage = this.clientDatastore
                .getForCustomer(customerId, sortBy, paginationContext);
        ret.setContext(onePage.getContext());
        ret.getItems().addAll(onePage.getItems());

        LOG.debug("Retrieved {} Clients for customer {} ", onePage.getItems().size(), 
                customerId);

        return ret;
    }
    
    /**
     * Updates Client record
     * 
     * @param Client
     * @return updated Client object
     * @throws RuntimeException if Client record does not exist or if it was modified concurrently
     */
    @RequestMapping(method=RequestMethod.PUT)
    public Client update(@RequestBody Client client){
        
        LOG.debug("Updating Client {}", client);
        
        if (BaseJsonModel.hasUnsupportedValue(client)) {
            LOG.error("Failed to update Client, request contains unsupported value: {}", client);
            throw new DsDataValidationException("Client contains unsupported value");
        }

        Client ret = clientDatastore.update(client);

        LOG.debug("Updated Client {}", ret);

        List<SystemEvent> events = new ArrayList<>();
        events.add(new ClientChangedEvent(ret));
        
        if(ret.isNeedToUpdateBlocklist()) {
        	events.add(new ClientBlockListChangedEvent(ret));
        }
        
        publishEvents(events);

        

        return ret;
    }
    
    /**
     * Deletes Client record
     * 
     * @param clientId
     * @return deleted Client object
     */
    @RequestMapping(method=RequestMethod.DELETE)
    public Client delete(@RequestParam int customerId,  @RequestParam MacAddress macAddress) {
        
        LOG.debug("Deleting Client {} {}", customerId, macAddress);
        
        Client ret = clientDatastore.delete(customerId, macAddress);

        LOG.debug("Deleted Client {}", ret);
        
        List<SystemEvent> events = new ArrayList<>();
        events.add(new ClientRemovedEvent(ret));
        
        if(ret.isNeedToUpdateBlocklist()) {
        	events.add(new ClientBlockListChangedEvent(ret));
        }
        
        publishEvents(events);


        return ret;
    }

    
    //
    // Client Session -related methods
    //

    /**
     * Retrieves ClientSession by id
     * @param customerId
     * @param equipmentId
     * @param macAddress
     * @return Client session for the supplied id
     */
    @RequestMapping(value = "/session/orNull", method=RequestMethod.GET)
    public ClientSession getSessionOrNull(@RequestParam int customerId, @RequestParam long equipmentId,  @RequestParam MacAddress macAddress) {
        
        LOG.debug("Retrieving Client session {} {} {}", customerId, equipmentId, macAddress);
        
        ClientSession ret = clientDatastore.getSessionOrNull(customerId, equipmentId, macAddress);

        LOG.debug("Retrieved Client {}", ret);

        return ret;
    }

    @RequestMapping(value = "/session/inSet", method = RequestMethod.GET)
    public ListOfClientSessions getAllSessionsInSet(@RequestParam int customerId,  @RequestParam Set<MacAddress> clientMacSet) {
        LOG.debug("getAllSessionsInSet({}, {})", customerId, clientMacSet);
        try {
            List<ClientSession> result = clientDatastore.getSessions(customerId, clientMacSet);
            LOG.debug("getAllSessionsInSet({}, {}) return {} entries", customerId, clientMacSet, result.size());
            ListOfClientSessions ret = new ListOfClientSessions();
            ret.addAll(result);
            return ret;
        } catch (Exception exp) {
             LOG.error("getAllSessionsInSet({}, {}) exception ", customerId, clientMacSet, exp);
             throw exp;
        }
    }

    @RequestMapping(value = "/session/forCustomer", method = RequestMethod.GET)
    public PaginationResponse<ClientSession> getSessionsForCustomer(@RequestParam int customerId,
    		@RequestParam Set<Long> equipmentIds,
    		@RequestParam Set<Long> locationIds,
            @RequestParam List<ColumnAndSort> sortBy,
            @RequestParam(required = false) PaginationContext<ClientSession> paginationContext) {

    	if(paginationContext == null) {
    		paginationContext = new PaginationContext<>();
    	}

        LOG.debug("Looking up Client sessions for customer {} with last returned page number {}", 
                customerId, paginationContext.getLastReturnedPageNumber());

        PaginationResponse<ClientSession> ret = new PaginationResponse<>();

        if (paginationContext.isLastPage()) {
            // no more pages available according to the context
            LOG.debug(
                    "No more pages available when looking up Client sessions for customer {} with last returned page number {}",
                    customerId, paginationContext.getLastReturnedPageNumber());
            ret.setContext(paginationContext);
            return ret;
        }

        PaginationResponse<ClientSession> onePage = this.clientDatastore
                .getSessionsForCustomer(customerId, equipmentIds, locationIds,  sortBy, paginationContext);
        ret.setContext(onePage.getContext());
        ret.getItems().addAll(onePage.getItems());

        LOG.debug("Retrieved {} Client sessions for customer {} ", onePage.getItems().size(), 
                customerId);

        return ret;
    }
    
    /**
     * Updates Client session record
     * 
     * @param Client session
     * @return updated or created Client session object
     */
    @RequestMapping(value="/session", method=RequestMethod.PUT)
    public ClientSession updateSession(@RequestBody ClientSession clientSession){
        
        LOG.debug("Updating Client session {}", clientSession);
        
        try {
            clientSessionValidator.validateClientSession(clientSession);
        } catch (ClientSessionValidatorException e) {
            LOG.error(e.getMessage());
            throw new DsDataValidationException(e.getMessage());
        }
        
        if (BaseJsonModel.hasUnsupportedValue(clientSession)) {
            LOG.error("Failed to update Client session, request contains unsupported value: {}", clientSession);
            throw new DsDataValidationException("Client session contains unsupported value");
        }

        ClientSession ret = clientDatastore.updateSession(clientSession);

        LOG.debug("Updated Client session {}", ret);

        //TODO: to reduce number of stored events - instead of generating many ClientSessionChangedEvents, we may want to create one ClientSessionClosedEvent once the session is closed
        ClientSessionChangedEvent event = new ClientSessionChangedEvent(ret);
        publishEvent(event);

        return ret;
    }
    
    /**
     * Updates a batch of Client sessions
     * 
     * @param list of Client sessions
     * @return List of updated or created Client sessions
     */
    @RequestMapping(value="/session/bulk", method=RequestMethod.PUT)
    public ListOfClientSessions updateSessionsBulk(@RequestBody List<ClientSession> clientSessions){
        
    	if(clientSessions == null || clientSessions.isEmpty()) {
    		//nothing to do here
    		return new ListOfClientSessions();
    	}

        LOG.debug("Updating Client sessions {}", clientSessions);
        
        try {
            for (ClientSession clientSession : clientSessions)
            {
                clientSessionValidator.validateClientSession(clientSession);
            }
        } catch (ClientSessionValidatorException e) {
            LOG.error(e.getMessage());
            throw new DsDataValidationException(e.getMessage());
        }
        
        if (BaseJsonModel.hasUnsupportedValue(clientSessions)) {
            LOG.error("Failed to update Client session, request contains unsupported value: {}", clientSessions);
            throw new DsDataValidationException("Client session contains unsupported value");
        }

        ListOfClientSessions ret = new ListOfClientSessions();
        
        ret.addAll(clientDatastore.updateSessions(clientSessions));

        LOG.debug("Updated {} Client sessions", ret.size());

        //TODO: to reduce number of stored events - instead of generating many ClientSessionChangedEvents, we may want to create one ClientSessionClosedEvent once the session is closed
        List<SystemEvent> events = new ArrayList<>();
        ret.forEach(s -> events.add(new ClientSessionChangedEvent(s)));
        
        publishEvents(events);

        return ret;
    }

    /**
     * Deletes Client session record
     * 
     * @param customerId
     * @param equipmentId
     * @param macAddress
     * @return deleted Client session object
     */
    @RequestMapping(value="/session", method=RequestMethod.DELETE)
    public ClientSession deleteSession(@RequestParam int customerId, @RequestParam long equipmentId,  @RequestParam MacAddress macAddress) {
        
        LOG.debug("Deleting Client session {} {}", customerId, equipmentId, macAddress);
        
        ClientSession ret = clientDatastore.deleteSession(customerId, equipmentId, macAddress);

        LOG.debug("Deleted Client session {}", ret);
        
        ClientSessionRemovedEvent event = new ClientSessionRemovedEvent(ret);
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

    private void publishEvents(List<SystemEvent> events) {
        try {
            cloudEventDispatcher.publishEventsBulk(events);
        } catch (Exception e) {
            LOG.error("Failed to publish events : {}", events, e);
        }
    }

    
}
