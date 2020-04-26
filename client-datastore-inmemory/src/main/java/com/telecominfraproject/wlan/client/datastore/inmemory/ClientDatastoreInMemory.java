package com.telecominfraproject.wlan.client.datastore.inmemory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pagination.SortOrder;
import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;
import com.telecominfraproject.wlan.datastore.inmemory.BaseInMemoryDatastore;

import com.telecominfraproject.wlan.client.datastore.ClientDatastore;
import com.telecominfraproject.wlan.client.models.Client;

/**
 * @author dtoptygin
 *
 */
@Configuration
public class ClientDatastoreInMemory extends BaseInMemoryDatastore implements ClientDatastore {

    private static final Logger LOG = LoggerFactory.getLogger(ClientDatastoreInMemory.class);

    private static final Map<Long, Client> idToClientMap = new ConcurrentHashMap<Long, Client>();
    
    private static final AtomicLong clientIdCounter = new AtomicLong();    

    @Override
    public Client create(Client client) {
        
        Client clientCopy = client.clone();
        
        long id = clientIdCounter.incrementAndGet();
        clientCopy.setId(id);
        clientCopy.setCreatedTimestamp(System.currentTimeMillis());
        clientCopy.setLastModifiedTimestamp(clientCopy.getCreatedTimestamp());
        idToClientMap.put(id, clientCopy);
        
        LOG.debug("Stored Client {}", clientCopy);
        
        return clientCopy.clone();
    }


    @Override
    public Client get(long clientId) {
        LOG.debug("Looking up Client for id {}", clientId);
        
        Client client = idToClientMap.get(clientId);
        
        if(client==null){
            LOG.debug("Cannot find Client for id {}", clientId);
            throw new DsEntityNotFoundException("Cannot find Client for id " + clientId);
        } else {
            LOG.debug("Found Client {}", client);
        }

        return client.clone();
    }

    @Override
    public Client getOrNull(long clientId) {
        LOG.debug("Looking up Client for id {}", clientId);
        
        Client client = idToClientMap.get(clientId);
        
        if(client==null){
            LOG.debug("Cannot find Client for id {}", clientId);
            return null;
        } else {
            LOG.debug("Found Client {}", client);
        }

        return client.clone();
    }
    
    @Override
    public Client update(Client client) {
        Client existingClient = get(client.getId());
        
        if(existingClient.getLastModifiedTimestamp()!=client.getLastModifiedTimestamp()){
            LOG.debug("Concurrent modification detected for Client with id {} expected version is {} but version in db was {}", 
                    client.getId(),
                    client.getLastModifiedTimestamp(),
                    existingClient.getLastModifiedTimestamp()
                    );
            throw new DsConcurrentModificationException("Concurrent modification detected for Client with id " + client.getId()
                    +" expected version is " + client.getLastModifiedTimestamp()
                    +" but version in db was " + existingClient.getLastModifiedTimestamp()
                    );
            
        }
        
        Client clientCopy = client.clone();
        clientCopy.setLastModifiedTimestamp(getNewLastModTs(client.getLastModifiedTimestamp()));

        idToClientMap.put(clientCopy.getId(), clientCopy);
        
        LOG.debug("Updated Client {}", clientCopy);
        
        return clientCopy.clone();
    }

    @Override
    public Client delete(long clientId) {
        Client client = get(clientId);
        idToClientMap.remove(client.getId());
        
        LOG.debug("Deleted Client {}", client);
        
        return client.clone();
    }

    @Override
    public List<Client> get(Set<Long> clientIdSet) {

    	List<Client> ret = new ArrayList<>();
    	
    	if(clientIdSet!=null && !clientIdSet.isEmpty()) {	    	
	    	idToClientMap.forEach(
	        		(id, c) -> {
	        			if(clientIdSet.contains(id)) {
				        	ret.add(c.clone());
				        } }
	        		);
    	}

        LOG.debug("Found Clients by ids {}", ret);

        return ret;
    
    }

    @Override
    public PaginationResponse<Client> getForCustomer(int customerId, 
    		final List<ColumnAndSort> sortBy, PaginationContext<Client> context) {

        PaginationResponse<Client> ret = new PaginationResponse<>();
        ret.setContext(context.clone());

        if (ret.getContext().isLastPage()) {
            // no more pages available according to the context
            return ret;
        }

        List<Client> items = new LinkedList<>();

        // apply filters and build the full result list first - inefficient, but ok for testing
        for (Client mdl : idToClientMap.values()) {

            if (mdl.getCustomerId() != customerId) {
                continue;
            }

            items.add(mdl);
        }

        // apply sortBy columns
        Collections.sort(items, new Comparator<Client>() {
            @Override
            public int compare(Client o1, Client o2) {
                if (sortBy == null || sortBy.isEmpty()) {
                    // sort ascending by id by default
                    return Long.compare(o1.getId(), o2.getId());
                } else {
                    int cmp;
                    for (ColumnAndSort column : sortBy) {
                        switch (column.getColumnName()) {
                        case "id":
                            cmp = Long.compare(o1.getId(), o2.getId());
                            break;
                        case "sampleStr":
                            cmp = o1.getSampleStr().compareTo(o2.getSampleStr());
                            break;
                        default:
                            // skip unknown column
                            continue;
                        }

                        if (cmp != 0) {
                            return (column.getSortOrder() == SortOrder.asc) ? cmp : (-cmp);
                        }

                    }
                }
                return 0;
            }
        });

        // now select only items for the requested page
        // find first item to add
        int fromIndex = 0;
        if (context.getStartAfterItem() != null) {
            for (Client mdl : items) {
                fromIndex++;
                if (mdl.getId() == context.getStartAfterItem().getId()) {
                    break;
                }
            }
        }

        // find last item to add
        int toIndexExclusive = fromIndex + context.getMaxItemsPerPage();
        if (toIndexExclusive > items.size()) {
            toIndexExclusive = items.size();
        }

        // copy page items into result
        List<Client> selectedItems = new ArrayList<>(context.getMaxItemsPerPage());
        for (Client mdl : items.subList(fromIndex, toIndexExclusive)) {
            selectedItems.add(mdl.clone());
        }

        ret.setItems(selectedItems);

        // adjust context for the next page
        ret.prepareForNextPage();

        if(ret.getContext().getStartAfterItem()!=null) {
        	//this datastore is only interested in the last item's id, so we'll clear all other fields on the startAfterItem in the pagination context
        	Client newStartAfterItem = new Client();
        	newStartAfterItem.setId(ret.getContext().getStartAfterItem().getId());
        	ret.getContext().setStartAfterItem(newStartAfterItem);
        }

        return ret;
    }    
}
