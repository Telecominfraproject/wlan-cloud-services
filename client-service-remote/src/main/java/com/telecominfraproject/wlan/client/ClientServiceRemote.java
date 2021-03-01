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

import com.telecominfraproject.wlan.client.models.Client;
import com.telecominfraproject.wlan.client.session.models.ClientSession;
import com.telecominfraproject.wlan.core.client.BaseRemoteClient;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;

/**
 * @author dtoptygin
 *
 */
@Component
public class ClientServiceRemote extends BaseRemoteClient implements ClientServiceInterface {

    private static final Logger LOG = LoggerFactory.getLogger(ClientServiceRemote.class);
    
    private static final ParameterizedTypeReference<List<Client>> Client_LIST_CLASS_TOKEN = new ParameterizedTypeReference<List<Client>>() {};
    private static final ParameterizedTypeReference<List<ClientSession>> ClientSession_LIST_CLASS_TOKEN = new ParameterizedTypeReference<List<ClientSession>>() {};

    private static final ParameterizedTypeReference<PaginationResponse<Client>> Client_PAGINATION_RESPONSE_CLASS_TOKEN = new ParameterizedTypeReference<PaginationResponse<Client>>() {};
    private static final ParameterizedTypeReference<PaginationResponse<ClientSession>> ClientSession_PAGINATION_RESPONSE_CLASS_TOKEN = new ParameterizedTypeReference<PaginationResponse<ClientSession>>() {};


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
    public Client getOrNull(int customerId, MacAddress clientMac) {
        
        LOG.debug("calling client.getOrNull {} {}", customerId, clientMac);

        ResponseEntity<Client> responseEntity = restTemplate.getForEntity(
                getBaseUrl()
                +"/orNull?customerId={customerId}&macAddress={clientMac}",
                Client.class, customerId, clientMac);
        
        Client ret = responseEntity.getBody();
        
        LOG.debug("completed client.getOrNull {} ", ret);
        
        return ret;
    }

    @Override
    public List<Client> get(int customerId, Set<MacAddress> clientMacSet) {
		
        LOG.debug("get({} {})", customerId, clientMacSet);

        if (clientMacSet == null || clientMacSet.isEmpty()) {
            return Collections.emptyList();
        }

        try {
            ResponseEntity<List<Client>> responseEntity = restTemplate.exchange(
                    getBaseUrl() + "/inSet?customerId={customerId}&clientMacSet={clientMacSet}", HttpMethod.GET,
                    null, Client_LIST_CLASS_TOKEN, customerId, clientMacSet);

            List<Client> result = responseEntity.getBody();
            if (null == result) {
                result = Collections.emptyList();
            }
            LOG.debug("get({} {}) return {} entries", customerId, clientMacSet, result.size());
            return result;
        } catch (Exception exp) {
            LOG.error("get({} {}) exception ", customerId, clientMacSet, exp);
            throw exp;
        }

	}
    
    @Override
    public PaginationResponse<Client> searchByMacAddress(int customerId, String macSubstring, 
    		List<ColumnAndSort> sortBy, PaginationContext<Client> context) {
		
        LOG.debug("searchByMacAddress({} {})", customerId, macSubstring);

        try {
            ResponseEntity<PaginationResponse<Client>> responseEntity = restTemplate.exchange(
                    getBaseUrl() + "/searchByMac?customerId={customerId}&macSubstring={macSubstring}&sortBy={sortBy}&paginationContext={context}", 
                    HttpMethod.GET, null, Client_PAGINATION_RESPONSE_CLASS_TOKEN, customerId, macSubstring, sortBy, context);

            PaginationResponse<Client> result = responseEntity.getBody();
            LOG.debug("searchByMacAddress({} {}) return {} entries", customerId, macSubstring, result.getItems().size());
            return result;
        } catch (Exception exp) {
            LOG.error("searchByMacAddress({} {}) exception ", customerId, macSubstring, exp);
            throw exp;
        }

	}
    
    @Override
    public List<Client> getBlockedClients(int customerId) {
        LOG.debug("getBlockedClients {}", customerId);

        try {
            ResponseEntity<List<Client>> responseEntity = restTemplate.exchange(
                    getBaseUrl() + "/blocked?customerId={customerId}", HttpMethod.GET,
                    null, Client_LIST_CLASS_TOKEN, customerId);

            List<Client> result = responseEntity.getBody();
            if (null == result) {
                result = Collections.emptyList();
            }
            LOG.debug("getBlockedClients({}) return {} entries", customerId, result.size());
            return result;
        } catch (Exception exp) {
            LOG.error("getBlockedClients({}) exception ", customerId, exp);
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
    public Client delete(int customerId, MacAddress clientMac) {
        
        LOG.debug("calling client.delete {} {}", customerId, clientMac);

        ResponseEntity<Client> responseEntity =  restTemplate.exchange(
                getBaseUrl()
                +"?customerId={customerId}&macAddress={clientMac}", HttpMethod.DELETE,
                null, Client.class, customerId, clientMac);
        
        Client ret = responseEntity.getBody();
        LOG.debug("completed client.delete {} ", ret);
        
        return ret;
    }    

    //
    // Methods for managing client sessions
    //
    
    @Override
    public ClientSession getSessionOrNull(int customerId, long equipmentId, MacAddress clientMac) {
        
        LOG.debug("calling client.getSessionOrNull {} {} {}", customerId, equipmentId, clientMac);

        ResponseEntity<ClientSession> responseEntity = restTemplate.getForEntity(
                getBaseUrl()
                +"/session/orNull?customerId={customerId}&equipmentId={equipmentId}&macAddress={clientMac}",
                ClientSession.class, customerId, equipmentId, clientMac);
        
        ClientSession ret = responseEntity.getBody();
        
        LOG.debug("completed client.getSessionOrNull {} ", ret);
        
        return ret;
    }

    @Override
    public List<ClientSession> getSessions(int customerId, Set<MacAddress> clientMacSet) {
		
        LOG.debug("getSessions({} {})", customerId, clientMacSet);

        if (clientMacSet == null || clientMacSet.isEmpty()) {
            return Collections.emptyList();
        }

        try {
            ResponseEntity<List<ClientSession>> responseEntity = restTemplate.exchange(
                    getBaseUrl() + "/session/inSet?customerId={customerId}&clientMacSet={clientMacSet}", HttpMethod.GET,
                    null, ClientSession_LIST_CLASS_TOKEN, customerId, clientMacSet);

            List<ClientSession> result = responseEntity.getBody();
            if (null == result) {
                result = Collections.emptyList();
            }
            LOG.debug("getSessions({} {}) return {} entries", customerId, clientMacSet, result.size());
            return result;
        } catch (Exception exp) {
            LOG.error("getSessions({} {}) exception ", customerId, clientMacSet, exp);
            throw exp;
        }

	}

    @Override
    public PaginationResponse<ClientSession> getSessionsForCustomer(int customerId, Set<Long> equipmentIds, Set<Long> locationIds,
    		String macSubstring, List<ColumnAndSort> sortBy, PaginationContext<ClientSession> context) {
		
        LOG.debug("calling getSessionsForCustomer( {}, {}, {}, {}, {}, {} )", customerId, equipmentIds, locationIds, macSubstring, sortBy, context);

        String equipmentIdsStr = null;
        if (equipmentIds != null && !equipmentIds.isEmpty()) {
            equipmentIdsStr = equipmentIds.toString();
            // remove [] around the string, otherwise will get:
            // Failed to convert value of type 'java.lang.String' to required
            // type 'java.util.Set'; nested exception is
            // java.lang.NumberFormatException: For input string: "[690]"
            equipmentIdsStr = equipmentIdsStr.substring(1, equipmentIdsStr.length() - 1);
        }

        String locationIdsStr = null;
        if (locationIds != null && !locationIds.isEmpty()) {
        	locationIdsStr = locationIds.toString();
        	locationIdsStr = locationIdsStr.substring(1, locationIdsStr.length() - 1);
        }

        ResponseEntity<PaginationResponse<ClientSession>> responseEntity = restTemplate.exchange(
                getBaseUrl()
                        + "/session/forCustomer?customerId={customerId}&equipmentIds={equipmentIdsStr}&locationIds={locationIdsStr}&macSubstring={macSubstring}&sortBy={sortBy}&paginationContext={paginationContext}",
                HttpMethod.GET, null, ClientSession_PAGINATION_RESPONSE_CLASS_TOKEN, customerId, equipmentIdsStr, locationIdsStr, macSubstring, sortBy, context);

        PaginationResponse<ClientSession> ret = responseEntity.getBody();
        LOG.debug("completed getSessionsForCustomer {} ", ret.getItems().size());

        return ret;
	}
	
    @Override
    public ClientSession updateSession(ClientSession clientSession) {
        
        LOG.debug("calling client.updateSession {} ", clientSession);

        if (BaseJsonModel.hasUnsupportedValue(clientSession)) {
            LOG.error("Failed to update Client session, unsupported value in  {}", clientSession);
            throw new DsDataValidationException("Client session contains unsupported value");
        }
        
        HttpEntity<String> request = new HttpEntity<String>( clientSession.toString(), headers );

        ResponseEntity<ClientSession> responseEntity = restTemplate.exchange(
                getBaseUrl()+ "/session",
                HttpMethod.PUT, request, ClientSession.class);
        
        ClientSession ret = responseEntity.getBody();
        
        LOG.debug("completed client.updateSession {} ", ret);
        
        return ret;
    }

    @Override
    public ClientSession deleteSession(int customerId, long equipmentId, MacAddress clientMac) {
        
        LOG.debug("calling client.deleteSession {} {} {}", customerId, equipmentId, clientMac);

        ResponseEntity<ClientSession> responseEntity =  restTemplate.exchange(
                getBaseUrl() + "/session"
                +"?customerId={customerId}&equipmentId={equipmentId}&macAddress={clientMac}", HttpMethod.DELETE,
                null, ClientSession.class, customerId, equipmentId, clientMac);
        
        ClientSession ret = responseEntity.getBody();
        LOG.debug("completed client.deleteSession {} ", ret);
        
        return ret;
    }    
    
    @Override
    public List<ClientSession> updateSessions(List<ClientSession> clientSessions) {
        LOG.debug("calling session.update {} ", clientSessions);

        if (BaseJsonModel.hasUnsupportedValue(clientSessions)) {
            LOG.error("Failed to update Client sessions, unsupported value in  {}", clientSessions);
            throw new DsDataValidationException("Client session contains unsupported value");
        }
        
        HttpEntity<String> request = new HttpEntity<String>( clientSessions.toString(), headers );

        ResponseEntity<List<ClientSession>> responseEntity = restTemplate.exchange(
                getBaseUrl() + "/session/bulk",
                HttpMethod.PUT, request, ClientSession_LIST_CLASS_TOKEN);
        
        List<ClientSession> ret = responseEntity.getBody();
        
        LOG.debug("completed status.update {} ", ret);
        
        return ret;
    }
    
    public String getBaseUrl() {
        if(baseUrl==null) {
            baseUrl = environment.getProperty("tip.wlan.clientServiceBaseUrl").trim()+"/api/client";
        }

    	return baseUrl;
    }


}
