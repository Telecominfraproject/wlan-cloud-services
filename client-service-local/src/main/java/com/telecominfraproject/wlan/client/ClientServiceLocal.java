package com.telecominfraproject.wlan.client;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.client.controller.ClientController;
import com.telecominfraproject.wlan.client.controller.ClientController.ListOfClientSessions;
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
@Component
public class ClientServiceLocal implements ClientServiceInterface {

    @Autowired private ClientController clientController;
    private static final Logger LOG = LoggerFactory.getLogger(ClientServiceLocal.class);
    
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
	public PaginationResponse<Client> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
			PaginationContext<Client> paginationContext) {
		return clientController.getForCustomer(customerId, sortBy, paginationContext);
	}

    @Override
	public Client update(Client client) {
		return clientController.update(client);
	}

    @Override
	public Client delete(int customerId, MacAddress macAddress) {
		return clientController.delete(customerId, macAddress);
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
			List<ColumnAndSort> sortBy, PaginationContext<ClientSession> paginationContext) {
		return clientController.getSessionsForCustomer(customerId, equipmentIds, locationIds, sortBy, paginationContext);
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
	
}
