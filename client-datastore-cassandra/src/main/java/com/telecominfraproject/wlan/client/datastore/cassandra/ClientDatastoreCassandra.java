package com.telecominfraproject.wlan.client.datastore.cassandra;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;

import com.telecominfraproject.wlan.client.datastore.ClientDatastore;
import com.telecominfraproject.wlan.client.models.Client;
import com.telecominfraproject.wlan.client.session.models.ClientSession;

/**
 * @author dtoptygin
 *
 */
@Configuration
public class ClientDatastoreCassandra implements ClientDatastore {

    @Autowired ClientDAO clientDAO;
    @Autowired ClientSessionDAO clientSessionDAO;

    @Override
    public Client create(Client client) {
        return clientDAO.create(client);
    }

    @Override
    public Client getOrNull(int customerId, MacAddress clientMac) {
        return clientDAO.getOrNull(customerId, clientMac);
    }
    
    @Override
    public Client update(Client client) {
        return clientDAO.update(client);
    }

    @Override
    public Client delete(int customerId, MacAddress clientMac) {
        return clientDAO.delete(customerId, clientMac);
    }
    
    @Override
    public List<Client> get(int customerId, Set<MacAddress> clientMacSet) {
    	return clientDAO.get(customerId, clientMacSet);
    }
    
    @Override
    public PaginationResponse<Client> searchByMacAddress(int customerId, String macSubstring,
    		List<ColumnAndSort> sortBy, PaginationContext<Client> context) {
    	return clientDAO.searchByMacAddress(customerId, macSubstring, sortBy, context);
    }
    
    @Override
    public List<Client> getBlockedClients(int customerId) {
    	return clientDAO.getBlockedClients(customerId);
    }
    
    @Override
    public PaginationResponse<Client> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
    		PaginationContext<Client> context) {

    	if(context == null) {
    		context = new PaginationContext<>();
    	}
    	
    	return clientDAO.getForCustomer( customerId, sortBy, context);
    }

	@Override
	public ClientSession getSessionOrNull(int customerId, long equipmentId, MacAddress clientMac) {
		return clientSessionDAO.getSessionOrNull(customerId, equipmentId, clientMac);
	}

	@Override
	public ClientSession updateSession(ClientSession clientSession) {
		return clientSessionDAO.updateSession(clientSession);
	}

	@Override
	public ClientSession deleteSession(int customerId, long equipmentId, MacAddress clientMac) {
		return clientSessionDAO.deleteSession(customerId, equipmentId, clientMac);
	}

	@Override
	public List<ClientSession> getSessions(int customerId, Set<MacAddress> clientMacSet) {
		return clientSessionDAO.getSessions(customerId, clientMacSet);
	}

	@Override
	public PaginationResponse<ClientSession> getSessionsForCustomer(int customerId, Set<Long> equipmentIds, Set<Long> locationIds,
			String macSubstring, List<ColumnAndSort> sortBy, PaginationContext<ClientSession> context) {

		if(context == null) {
    		context = new PaginationContext<>();
    	}
		
		return clientSessionDAO.getSessionsForCustomer(customerId, equipmentIds, locationIds, macSubstring, sortBy, context);
	}
    
    
}
