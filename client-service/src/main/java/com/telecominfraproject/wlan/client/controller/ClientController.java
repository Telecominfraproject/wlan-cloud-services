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

import com.telecominfraproject.wlan.cloudeventdispatcher.CloudEventDispatcherInterface;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;
import com.telecominfraproject.wlan.systemevent.models.SystemEvent;

import com.telecominfraproject.wlan.client.datastore.ClientDatastore;
import com.telecominfraproject.wlan.client.models.Client;
import com.telecominfraproject.wlan.client.models.events.ClientAddedEvent;
import com.telecominfraproject.wlan.client.models.events.ClientChangedEvent;
import com.telecominfraproject.wlan.client.models.events.ClientRemovedEvent;


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

    @Autowired private ClientDatastore clientDatastore;
    @Autowired private CloudEventDispatcherInterface cloudEventDispatcher;

    
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

        ClientAddedEvent event = new ClientAddedEvent(ret);
        publishEvent(event);


        return ret;
    }
    
    /**
     * Retrieves Client by id
     * @param clientId
     * @return Client for the supplied id
     */
    @RequestMapping(method=RequestMethod.GET)
    public Client get(@RequestParam long clientId ) {
        
        LOG.debug("Retrieving Client {}", clientId);
        
        Client ret = clientDatastore.get(clientId);

        LOG.debug("Retrieved Client {}", ret);

        return ret;
    }

    /**
     * Retrieves Client by id
     * @param clientId
     * @return Client for the supplied id
     */
    @RequestMapping(value = "/orNull", method=RequestMethod.GET)
    public Client getOrNull(@RequestParam long clientId ) {
        
        LOG.debug("Retrieving Client {}", clientId);
        
        Client ret = clientDatastore.getOrNull(clientId);

        LOG.debug("Retrieved Client {}", ret);

        return ret;
    }

    @RequestMapping(value = "/inSet", method = RequestMethod.GET)
    public ListOfClients getAllInSet(@RequestParam Set<Long> clientIdSet) {
        LOG.debug("getAllInSet({})", clientIdSet);
        try {
            List<Client> result = clientDatastore.get(clientIdSet);
            LOG.debug("getAllInSet({}) return {} entries", clientIdSet, result.size());
            ListOfClients ret = new ListOfClients();
            ret.addAll(result);
            return ret;
        } catch (Exception exp) {
             LOG.error("getAllInSet({}) exception ", clientIdSet, exp);
             throw exp;
        }
    }

    @RequestMapping(value = "/forCustomer", method = RequestMethod.GET)
    public PaginationResponse<Client> getForCustomer(@RequestParam int customerId,
            @RequestParam List<ColumnAndSort> sortBy,
            @RequestParam PaginationContext<Client> paginationContext) {

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
                .getForCustomer(customerId,  sortBy, paginationContext);
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

        ClientChangedEvent event = new ClientChangedEvent(ret);
        publishEvent(event);

        return ret;
    }
    
    /**
     * Deletes Client record
     * 
     * @param clientId
     * @return deleted Client object
     */
    @RequestMapping(method=RequestMethod.DELETE)
    public Client delete(@RequestParam long clientId ) {
        
        LOG.debug("Deleting Client {}", clientId);
        
        Client ret = clientDatastore.delete(clientId);

        LOG.debug("Deleted Client {}", ret);
        
        ClientRemovedEvent event = new ClientRemovedEvent(ret);
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
