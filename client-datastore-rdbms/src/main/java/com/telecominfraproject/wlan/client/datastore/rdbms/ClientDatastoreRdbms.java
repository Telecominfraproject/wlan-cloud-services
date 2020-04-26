package com.telecominfraproject.wlan.client.datastore.rdbms;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;

import com.telecominfraproject.wlan.client.datastore.ClientDatastore;
import com.telecominfraproject.wlan.client.models.Client;

/**
 * @author dtoptygin
 *
 */
@Configuration
public class ClientDatastoreRdbms implements ClientDatastore {

    @Autowired ClientDAO clientDAO;

    @Override
    public Client create(Client client) {
        return clientDAO.create(client);
    }

    @Override
    public Client get(long clientId) {
        return clientDAO.get(clientId);
    }

    @Override
    public Client getOrNull(long clientId) {
        return clientDAO.getOrNull(clientId);
    }
    
    @Override
    public Client update(Client client) {
        return clientDAO.update(client);
    }

    @Override
    public Client delete(long clientId) {
        return clientDAO.delete(clientId);
    }
    
    @Override
    public List<Client> get(Set<Long> clientIdSet) {
    	return clientDAO.get(clientIdSet);
    }
    
    @Override
    public PaginationResponse<Client> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
    		PaginationContext<Client> context) {
    	return clientDAO.getForCustomer( customerId, sortBy, context);
    }
}
