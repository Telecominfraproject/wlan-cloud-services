package com.telecominfraproject.wlan.client;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.core.client.BaseRemoteClient;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;

import com.telecominfraproject.wlan.client.models.Client;

/**
 * @author dtoptygin
 *
 */
@Component
public class ClientServiceRemote extends BaseRemoteClient implements ClientServiceInterface {

    private static final Logger LOG = LoggerFactory.getLogger(ClientServiceRemote.class);
    
    private static final ParameterizedTypeReference<List<Client>> Client_LIST_CLASS_TOKEN = new ParameterizedTypeReference<List<Client>>() {};

    private static final ParameterizedTypeReference<PaginationResponse<Client>> Client_PAGINATION_RESPONSE_CLASS_TOKEN = new ParameterizedTypeReference<PaginationResponse<Client>>() {};


    private String baseUrl;
            
    @Override
    public Client create(Client client) {
        
        LOG.debug("calling client.create {} ", client);

        if (BaseJsonModel.hasUnsupportedValue(client)) {
            LOG.error("Failed to create Client, unsupported value in {}", client);
            throw new DsDataValidationException("Client contains unsupported value");
        }

        HttpEntity<String> request = new HttpEntity<String>( client.toString(), headers );

        ResponseEntity<Client> responseEntity = restTemplate.postForEntity(
                getBaseUrl(),
                request, Client.class);
        
        Client ret = responseEntity.getBody();
        
        LOG.debug("completed client.create {} ", ret);
        
        return ret;
    }

    @Override
    public Client get(long clientId) {
        
        LOG.debug("calling client.get {} ", clientId);

        ResponseEntity<Client> responseEntity = restTemplate.getForEntity(
                getBaseUrl()
                +"?clientId={clientId}",
                Client.class, clientId);
        
        Client ret = responseEntity.getBody();
        
        LOG.debug("completed client.get {} ", ret);
        
        return ret;
    }

    @Override
    public Client getOrNull(long clientId) {
        
        LOG.debug("calling client.getOrNull {} ", clientId);

        ResponseEntity<Client> responseEntity = restTemplate.getForEntity(
                getBaseUrl()
                +"/orNull?clientId={clientId}",
                Client.class, clientId);
        
        Client ret = responseEntity.getBody();
        
        LOG.debug("completed client.getOrNull {} ", ret);
        
        return ret;
    }

	@Override
	public List<Client> get(Set<Long> clientIdSet) {
		
        LOG.debug("get({})", clientIdSet);

        if (clientIdSet == null || clientIdSet.isEmpty()) {
            return Collections.emptyList();
        }

        String setString = clientIdSet.toString().substring(1, clientIdSet.toString().length() - 1);
        
        try {
            ResponseEntity<List<Client>> responseEntity = restTemplate.exchange(
                    getBaseUrl() + "/inSet?clientIdSet={clientIdSet}", HttpMethod.GET,
                    null, Client_LIST_CLASS_TOKEN, setString);

            List<Client> result = responseEntity.getBody();
            if (null == result) {
                result = Collections.emptyList();
            }
            LOG.debug("get({}) return {} entries", clientIdSet, result.size());
            return result;
        } catch (Exception exp) {
            LOG.error("getAllInSet({}) exception ", clientIdSet, exp);
            throw exp;
        }

	}

	@Override
	public PaginationResponse<Client> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
			PaginationContext<Client> context) {
		
        LOG.debug("calling getForCustomer( {}, {}, {} )", customerId, sortBy, context);

        ResponseEntity<PaginationResponse<Client>> responseEntity = restTemplate.exchange(
                getBaseUrl()
                        + "/forCustomer?customerId={customerId}&sortBy={sortBy}&paginationContext={paginationContext}",
                HttpMethod.GET, null, Client_PAGINATION_RESPONSE_CLASS_TOKEN, customerId, sortBy, context);

        PaginationResponse<Client> ret = responseEntity.getBody();
        LOG.debug("completed getForCustomer {} ", ret.getItems().size());

        return ret;
	}
	
    @Override
    public Client update(Client client) {
        
        LOG.debug("calling client.update {} ", client);

        if (BaseJsonModel.hasUnsupportedValue(client)) {
            LOG.error("Failed to update Client, unsupported value in  {}", client);
            throw new DsDataValidationException("Client contains unsupported value");
        }
        
        HttpEntity<String> request = new HttpEntity<String>( client.toString(), headers );

        ResponseEntity<Client> responseEntity = restTemplate.exchange(
                getBaseUrl(),
                HttpMethod.PUT, request, Client.class);
        
        Client ret = responseEntity.getBody();
        
        LOG.debug("completed client.update {} ", ret);
        
        return ret;
    }

    @Override
    public Client delete(long clientId) {
        
        LOG.debug("calling client.delete {} ", clientId);

        ResponseEntity<Client> responseEntity =  restTemplate.exchange(
                getBaseUrl()
                +"?clientId={clientId}", HttpMethod.DELETE,
                null, Client.class, clientId);
        
        Client ret = responseEntity.getBody();
        LOG.debug("completed client.delete {} ", ret);
        
        return ret;
    }    

    public String getBaseUrl() {
        if(baseUrl==null) {
            baseUrl = environment.getProperty("tip.wlan.clientServiceBaseUrl").trim()+"/api/client";
        }

    	return baseUrl;
    }


}
