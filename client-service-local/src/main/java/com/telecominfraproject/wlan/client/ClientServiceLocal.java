package com.telecominfraproject.wlan.client;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.client.controller.ClientController;
import com.telecominfraproject.wlan.client.controller.ClientController.ListOfClientSessions;
import com.telecominfraproject.wlan.client.info.models.ClientSessionCounts;
import com.telecominfraproject.wlan.client.models.Client;
import com.telecominfraproject.wlan.client.session.models.ClientSession;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.json.GenericResponse;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;

/**
 * @author dtoptygin
 *
 */
@Component
public class ClientServiceLocal implements ClientServiceInterface {

    @Autowired private ClientController clientController;
    
    @Override
	public Client create(Client client) {
		return clientController.create(client);
	}

    @Override
	public Client getOrNull(int customerId, MacAddress macAddress) {
		return clientController.getOrNull(customerId, macAddress);
	}

    @Override
	public List<Client> get(int customerId, Set<MacAddress> clientMacSet) {
		return clientController.getAllInSet(customerId, clientMacSet);
	}
    
    @Override
	public PaginationResponse<Client> getForCustomer(int customerId, String macSubstring, 
			List<ColumnAndSort> sortBy, PaginationContext<Client> paginationContext) {
		return clientController.getForCustomer(customerId, macSubstring, sortBy, paginationContext);
	}

    @Override
	public Client update(Client client) {
		return clientController.update(client);
	}

    @Override
	public Client delete(int customerId, MacAddress macAddress) {
		return clientController.delete(customerId, macAddress);
	}
    
    @Override
    public GenericResponse delete(long createdBeforeTimestamp) {
    	return clientController.delete(createdBeforeTimestamp); 
    }

    @Override
    public List<Client> getBlockedClients(int customerId) {
    	return clientController.getBlockedClients(customerId);
    }
    
    //
    // Methods related to Client Sessions
    //
    

    @Override
	public ClientSession getSessionOrNull(int customerId, long equipmentId, MacAddress macAddress) {
		return clientController.getSessionOrNull(customerId, equipmentId, macAddress);
	}

    @Override
	public ListOfClientSessions getSessions(int customerId, Set<MacAddress> clientMacSet) {
		return clientController.getAllSessionsInSet(customerId, clientMacSet);
	}

    @Override
	public PaginationResponse<ClientSession> getSessionsForCustomer(int customerId, Set<Long> equipmentIds, Set<Long> locationIds,
			String macSubstring, List<ColumnAndSort> sortBy, PaginationContext<ClientSession> paginationContext) {
		return clientController.getSessionsForCustomer(customerId, equipmentIds, locationIds, macSubstring, sortBy, paginationContext);
	}

    @Override
	public ClientSession updateSession(ClientSession clientSession) {
		return clientController.updateSession(clientSession);
	}

    @Override
	public List<ClientSession> updateSessions(List<ClientSession> clientSessions) {
		return clientController.updateSessionsBulk(clientSessions);
	}

    @Override
	public ClientSession deleteSession(int customerId, long equipmentId, MacAddress macAddress) {
		return clientController.deleteSession(customerId, equipmentId, macAddress);
	}
    
    @Override
    public GenericResponse deleteSessions(long createdBeforeTimestamp) {
    	return clientController.deleteSessions(createdBeforeTimestamp);
    }

    @Override
    public ClientSessionCounts getSessionCounts(int customerId) {
        return clientController.getSessionCounts(customerId);
    }
	
}
