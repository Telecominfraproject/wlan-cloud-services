package com.telecominfraproject.wlan.client.datastore.inmemory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import com.telecominfraproject.wlan.client.datastore.ClientDatastore;
import com.telecominfraproject.wlan.client.info.models.ClientInfoDetails;
import com.telecominfraproject.wlan.client.models.Client;
import com.telecominfraproject.wlan.client.session.models.ClientSession;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.core.model.pagination.SortOrder;
import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;
import com.telecominfraproject.wlan.datastore.inmemory.BaseInMemoryDatastore;

/**
 * @author dtoptygin
 *
 */
@Configuration
public class ClientDatastoreInMemory extends BaseInMemoryDatastore implements ClientDatastore {

    private static final Logger LOG = LoggerFactory.getLogger(ClientDatastoreInMemory.class);

    private static final Map<CustomerMacKey, Client> idToClientMap = new ConcurrentHashMap<>();
    private static final Map<CustomerEquipmentMacKey, ClientSession> idToClientSessionMap = new ConcurrentHashMap<>();
    
    @Override
    public Client create(Client client) {
        
        Client clientCopy = client.clone();
        CustomerMacKey key = new CustomerMacKey(client);
        clientCopy.setCreatedTimestamp(System.currentTimeMillis());
        clientCopy.setLastModifiedTimestamp(clientCopy.getCreatedTimestamp());
        idToClientMap.put(key, clientCopy);
        
        LOG.debug("Stored Client {}", clientCopy);

        //clone it again to set the setNeedToUpdateBlocklist flag (we do not want to store that flag)
		clientCopy = clientCopy.clone();

        //update blocked_client table, if needed
		if((client.getDetails() instanceof ClientInfoDetails) 
				&& ((ClientInfoDetails)client.getDetails()).getBlocklistDetails()!=null 
				&& ((ClientInfoDetails)client.getDetails()).getBlocklistDetails().isEnabled() 
				) {
			
			clientCopy.setNeedToUpdateBlocklist(true);
		}
		
        return clientCopy;
    }


    @Override
    public Client getOrNull(int customerId, MacAddress clientMac) {
        LOG.debug("Looking up Client for id {} {}", customerId, clientMac);
        
        Client client = idToClientMap.get(new CustomerMacKey(customerId, clientMac));
        
        if(client==null){
            LOG.debug("Cannot find Client for id {} {}", customerId, clientMac);
            return null;
        } else {
            LOG.debug("Found Client {}", client);
        }

        return client.clone();
    }
    
    @Override
    public Client update(Client client) {
        Client existingClient = getOrNull(client.getCustomerId(), client.getMacAddress());
        
        if(existingClient == null) {
            throw new DsEntityNotFoundException("Cannot find Client for id " + client.getCustomerId() + " " + client.getMacAddress());
        }
        
        if(existingClient.getLastModifiedTimestamp()!=client.getLastModifiedTimestamp()){
            LOG.debug("Concurrent modification detected for Client with id {} {} expected version is {} but version in db was {}", 
                    client.getCustomerId(), client.getMacAddress(),
                    client.getLastModifiedTimestamp(),
                    existingClient.getLastModifiedTimestamp()
                    );
            throw new DsConcurrentModificationException("Concurrent modification detected for Client with id "  
                    + client.getCustomerId() + " " + client.getMacAddress()
                    +" expected version is " + client.getLastModifiedTimestamp()
                    +" but version in db was " + existingClient.getLastModifiedTimestamp()
                    );
            
        }

        Client clientCopy = client.clone();
        
        clientCopy.setLastModifiedTimestamp(getNewLastModTs(client.getLastModifiedTimestamp()));

        idToClientMap.put(new CustomerMacKey(clientCopy), clientCopy);
        
        LOG.debug("Updated Client {}", clientCopy);

        //clone it again to set the setNeedToUpdateBlocklist flag (we do not want to store that flag)
        clientCopy = clientCopy.clone();
        
        //update client_blocklist table, if the blocking state of the client has changed
        boolean existingClientBlocked = (existingClient.getDetails() instanceof ClientInfoDetails) 
				&& ((ClientInfoDetails)existingClient.getDetails()).getBlocklistDetails()!=null 
				&& ((ClientInfoDetails)existingClient.getDetails()).getBlocklistDetails().isEnabled();

        boolean updatedClientBlocked = (client.getDetails() instanceof ClientInfoDetails) 
				&& ((ClientInfoDetails)client.getDetails()).getBlocklistDetails()!=null 
				&& ((ClientInfoDetails)client.getDetails()).getBlocklistDetails().isEnabled();
        
        if(existingClientBlocked != updatedClientBlocked) {
        	
        	//notify the caller that block list needs to be updated
        	clientCopy.setNeedToUpdateBlocklist(true);

        }

        return clientCopy;
    }

    @Override
    public Client delete(int customerId, MacAddress clientMac) {
        Client client = getOrNull(customerId, clientMac);
        if(client!=null) {
	        idToClientMap.remove(new CustomerMacKey(client));
	        LOG.debug("Deleted Client {} {}", customerId, clientMac);
        } else {
        	throw new DsEntityNotFoundException("Cannot find Client for id " + customerId + " " + clientMac);
        }
        
        //update blocked_client table, if needed
		if((client.getDetails() instanceof ClientInfoDetails) 
				&& ((ClientInfoDetails)client.getDetails()).getBlocklistDetails()!=null 
				&& ((ClientInfoDetails)client.getDetails()).getBlocklistDetails().isEnabled() 
				) {
			
			client.setNeedToUpdateBlocklist(true);
		}


		//client is already cloned by the getOrNull method
        return client;
    }

    @Override
    public List<Client> get(int customerId, Set<MacAddress> clientMacSet) {

    	List<Client> ret = new ArrayList<>();
    	
    	if(clientMacSet!=null && !clientMacSet.isEmpty()) {	    	
	    	idToClientMap.forEach(
	        		(k, c) -> {
	        			if(k.customerId == customerId && clientMacSet.contains(k.mac)) {
				        	ret.add(c.clone());
				        } }
	        		);
    	}

        LOG.debug("Found Clients by ids {}", ret);

        return ret;
    
    }
    
    @Override
    public PaginationResponse<Client> searchByMacAddress(int customerId, String macSubstring,
    		final List<ColumnAndSort> sortBy, PaginationContext<Client> context) {
    	
    	if(context == null) {
    		context = new PaginationContext<>();
    	}

        PaginationResponse<Client> ret = new PaginationResponse<>();
        ret.setContext(context.clone());

        if (ret.getContext().isLastPage()) {
            // no more pages available according to the context
            return ret;
        }

        List<Client> items = new LinkedList<>();
        
    	if(macSubstring != null) {
	    	idToClientMap.forEach(
	    		(k, c) -> {
	    			if(k.customerId == customerId && k.mac.getAddressAsString().toLowerCase().contains(macSubstring.toLowerCase())) {
	    				items.add(c.clone());
			        } 
	    		}
	    	);
    	} else {
    		idToClientMap.forEach(
	    		(k, c) -> {
	    			if(k.customerId == customerId) {
	    				items.add(c.clone());
			        } 
	    		}
	    	);
    	}

        // apply sortBy columns
        Collections.sort(items, new Comparator<Client>() {
            @Override
            public int compare(Client o1, Client o2) {
                if (sortBy == null || sortBy.isEmpty()) {
                    // sort ascending by id by default
                    return o1.getMacAddress().compareTo(o2.getMacAddress());
                } else {
                    int cmp;
                    for (ColumnAndSort column : sortBy) {
                        switch (column.getColumnName()) {
                        case "macAddress":
                            cmp =  o1.getMacAddress().compareTo(o2.getMacAddress());
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
                if (mdl.getCustomerId() == context.getStartAfterItem().getCustomerId() 
                		&& mdl.getMacAddress().equals(context.getStartAfterItem().getMacAddress())) {
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
        	newStartAfterItem.setCustomerId(ret.getContext().getStartAfterItem().getCustomerId());
        	newStartAfterItem.setMacAddress(ret.getContext().getStartAfterItem().getMacAddress());
        	ret.getContext().setStartAfterItem(newStartAfterItem);
        }

        return ret;
    
    }

    @Override
    public List<Client> getBlockedClients(int customerId) {
    	List<Client> ret = new ArrayList<>();
    	
	    	idToClientMap.forEach(
	        		(k, c) -> {
	        			if(k.customerId == customerId 
	        					&& (c.getDetails() instanceof ClientInfoDetails) 
	        					&& ((ClientInfoDetails)c.getDetails()).getBlocklistDetails()!=null 
	        					&& ((ClientInfoDetails)c.getDetails()).getBlocklistDetails().isEnabled() 
	        					) {
				        	ret.add(c.clone());
				        } }
	        		);

        LOG.debug("Found blocked Clients {}", ret);

        return ret;
    }
    
    @Override
    public PaginationResponse<Client> getForCustomer(int customerId, 
    		final List<ColumnAndSort> sortBy, PaginationContext<Client> context) {

    	if(context == null) {
    		context = new PaginationContext<>();
    	}

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
                    return o1.getMacAddress().compareTo(o2.getMacAddress());
                } else {
                    int cmp;
                    for (ColumnAndSort column : sortBy) {
                        switch (column.getColumnName()) {
                        case "macAddress":
                            cmp =  o1.getMacAddress().compareTo(o2.getMacAddress());
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
                if (mdl.getCustomerId() == context.getStartAfterItem().getCustomerId() 
                		&& mdl.getMacAddress().equals(context.getStartAfterItem().getMacAddress())) {
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
        	newStartAfterItem.setCustomerId(ret.getContext().getStartAfterItem().getCustomerId());
        	newStartAfterItem.setMacAddress(ret.getContext().getStartAfterItem().getMacAddress());
        	ret.getContext().setStartAfterItem(newStartAfterItem);
        }

        return ret;
    }
    
    ///
    // Client session related methods
    ///
    
    public ClientSession createSession(ClientSession clientSession) {
        
        ClientSession clientSessionCopy = clientSession.clone();
        CustomerEquipmentMacKey key = new CustomerEquipmentMacKey(clientSession);
        clientSessionCopy.setLastModifiedTimestamp(System.currentTimeMillis());
        idToClientSessionMap.put(key, clientSessionCopy);
        
        LOG.debug("Stored Client {}", clientSessionCopy);
        
        return clientSessionCopy.clone();
    }


    @Override
    public ClientSession getSessionOrNull(int customerId, long equipmentId, MacAddress clientMac) {
        LOG.debug("Looking up Client session for id {} {} {}", customerId, equipmentId, clientMac);
        
        ClientSession clientSession = idToClientSessionMap.get(new CustomerEquipmentMacKey(customerId, equipmentId, clientMac));
        
        if(clientSession==null){
            LOG.debug("Cannot find Client session for id {} {} {}", customerId, equipmentId, clientMac);
            return null;
        } else {
            LOG.debug("Found Client session {}", clientSession);
        }

        return clientSession.clone();
    }
    
    @Override
    public ClientSession updateSession(ClientSession clientSession) {
        ClientSession existingSession = getSessionOrNull(clientSession.getCustomerId(), clientSession.getEquipmentId(), clientSession.getMacAddress());
        
        if(existingSession == null) {
        	return createSession(clientSession);
        }
        
        ClientSession sessionCopy = clientSession.clone();
        sessionCopy.setLastModifiedTimestamp(getNewLastModTs(clientSession.getLastModifiedTimestamp()));

        idToClientSessionMap.put(new CustomerEquipmentMacKey(sessionCopy), sessionCopy);
        
        LOG.debug("Updated Client session {}", sessionCopy);
        
        return sessionCopy.clone();
    }

    @Override
    public ClientSession deleteSession(int customerId, long equipmentId, MacAddress clientMac) {
        ClientSession clientSession = getSessionOrNull(customerId, equipmentId, clientMac);
        if(clientSession!=null) {
	        idToClientSessionMap.remove(new CustomerEquipmentMacKey(clientSession));
	        LOG.debug("Deleted Client session {} {} {}", customerId, equipmentId, clientMac);
        } else {
        	throw new DsEntityNotFoundException("Cannot find Client session for id " + customerId + " " + equipmentId + " " + clientMac);
        }
        
        return clientSession.clone();
    }    
    

    @Override
    public List<ClientSession> getSessions(int customerId, Set<MacAddress> clientMacSet) {

    	List<ClientSession> ret = new ArrayList<>();
    	
    	if(clientMacSet!=null && !clientMacSet.isEmpty()) {	    	
	    	idToClientSessionMap.forEach(
	        		(k, c) -> {
	        			if(k.customerId == customerId && clientMacSet.contains(k.mac)) {
				        	ret.add(c.clone());
				        } }
	        		);
    	}

        LOG.debug("Found Clients by ids {}", ret);

        return ret;
    
    }

    @Override
    public PaginationResponse<ClientSession> getSessionsForCustomer(int customerId,
    		Set<Long> equipmentIds,
    		Set<Long> locationIds,
    		final List<ColumnAndSort> sortBy, PaginationContext<ClientSession> context) {

    	if(context == null) {
    		context = new PaginationContext<>();
    	}

        PaginationResponse<ClientSession> ret = new PaginationResponse<>();
        ret.setContext(context.clone());

        if (ret.getContext().isLastPage()) {
            // no more pages available according to the context
            return ret;
        }

        List<ClientSession> items = new LinkedList<>();

        // apply filters and build the full result list first - inefficient, but ok for testing
        for (ClientSession mdl : idToClientSessionMap.values()) {

			if (mdl.getCustomerId() == customerId && 
					    (
							equipmentIds == null 
							|| equipmentIds.isEmpty()
							|| equipmentIds.contains(mdl.getEquipmentId()) 
						) &&
					    (
							locationIds == null 
							|| locationIds.isEmpty()
							|| locationIds.contains(mdl.getLocationId()) 
						)					    
					) {
	            items.add(mdl);
            }
        }

        // apply sortBy columns
        Collections.sort(items, new Comparator<ClientSession>() {
            @Override
            public int compare(ClientSession o1, ClientSession o2) {
                if (sortBy == null || sortBy.isEmpty()) {
                    // sort ascending by id by default
                    return o1.getMacAddress().compareTo(o2.getMacAddress());
                } else {
                    int cmp;
                    for (ColumnAndSort column : sortBy) {
                        switch (column.getColumnName()) {
                        case "macAddress":
                            cmp =  o1.getMacAddress().compareTo(o2.getMacAddress());
                            break;
                        case "equipmentId":
                            cmp =  Long.compare(o1.getEquipmentId(), o2.getEquipmentId());
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
            for (ClientSession mdl : items) {
                fromIndex++;
                if (mdl.getCustomerId() == context.getStartAfterItem().getCustomerId()
                		&& mdl.getEquipmentId() == context.getStartAfterItem().getEquipmentId()
                		&& mdl.getMacAddress().equals(context.getStartAfterItem().getMacAddress())) {
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
        List<ClientSession> selectedItems = new ArrayList<>(context.getMaxItemsPerPage());
        for (ClientSession mdl : items.subList(fromIndex, toIndexExclusive)) {
            selectedItems.add(mdl.clone());
        }

        ret.setItems(selectedItems);

        // adjust context for the next page
        ret.prepareForNextPage();

        if(ret.getContext().getStartAfterItem()!=null) {
        	//this datastore is only interested in the last item's id, so we'll clear all other fields on the startAfterItem in the pagination context
        	ClientSession newStartAfterItem = new ClientSession();
        	newStartAfterItem.setCustomerId(ret.getContext().getStartAfterItem().getCustomerId());
        	newStartAfterItem.setEquipmentId(ret.getContext().getStartAfterItem().getEquipmentId());
        	newStartAfterItem.setMacAddress(ret.getContext().getStartAfterItem().getMacAddress());
        	ret.getContext().setStartAfterItem(newStartAfterItem);
        }

        return ret;
    }

}
