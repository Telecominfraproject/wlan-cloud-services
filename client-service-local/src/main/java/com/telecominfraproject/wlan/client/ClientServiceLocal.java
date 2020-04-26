package com.telecominfraproject.wlan.client;

import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;

import com.telecominfraproject.wlan.client.controller.ClientController;
import com.telecominfraproject.wlan.client.models.Client;

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
        LOG.debug("calling clientController.create {} ", client);
        return clientController.create(client);
    }

    @Override
    public Client get(long clientId) {
        LOG.debug("calling clientController.get {} ", clientId);
        return clientController.get(clientId);
    }
    
    @Override
    public Client getOrNull(long clientId) {
        LOG.debug("calling clientController.getOrNull {} ", clientId);
        return clientController.getOrNull(clientId);
    }
    
    @Override
    public List<Client> get(Set<Long> clientIdSet) {
        LOG.debug("calling clientController.getAllInSet {} ", clientIdSet);
        return clientController.getAllInSet(clientIdSet);
    }
    
    @Override
    public PaginationResponse<Client> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
    		PaginationContext<Client> context) {
        LOG.debug("calling clientController.getForCustomer {} ", customerId);
        return clientController.getForCustomer(customerId, sortBy, context);
    }

    @Override
    public Client update(Client client) {
        LOG.debug("calling clientController.update {} ", client);
        return clientController.update(client);
    }

    @Override
    public Client delete(long clientId) {
        LOG.debug("calling clientController.delete {} ", clientId);
        return clientController.delete(clientId);
    }

}
