package com.telecominfraproject.wlan.routing;

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

import com.telecominfraproject.wlan.routing.models.Routing;

/**
 * @author dtoptygin
 *
 */
@Component
public class RoutingServiceRemote extends BaseRemoteClient implements RoutingServiceInterface {

    private static final Logger LOG = LoggerFactory.getLogger(RoutingServiceRemote.class);
    
    private static final ParameterizedTypeReference<List<Routing>> Routing_LIST_CLASS_TOKEN = new ParameterizedTypeReference<List<Routing>>() {};

    private static final ParameterizedTypeReference<PaginationResponse<Routing>> Routing_PAGINATION_RESPONSE_CLASS_TOKEN = new ParameterizedTypeReference<PaginationResponse<Routing>>() {};


    private String baseUrl;
            
    @Override
    public Routing create(Routing routing) {
        
        LOG.debug("calling routing.create {} ", routing);

        if (BaseJsonModel.hasUnsupportedValue(routing)) {
            LOG.error("Failed to create Routing, unsupported value in {}", routing);
            throw new DsDataValidationException("Routing contains unsupported value");
        }

        HttpEntity<String> request = new HttpEntity<String>( routing.toString(), headers );

        ResponseEntity<Routing> responseEntity = restTemplate.postForEntity(
                getBaseUrl(),
                request, Routing.class);
        
        Routing ret = responseEntity.getBody();
        
        LOG.debug("completed routing.create {} ", ret);
        
        return ret;
    }

    @Override
    public Routing get(long routingId) {
        
        LOG.debug("calling routing.get {} ", routingId);

        ResponseEntity<Routing> responseEntity = restTemplate.getForEntity(
                getBaseUrl()
                +"?routingId={routingId}",
                Routing.class, routingId);
        
        Routing ret = responseEntity.getBody();
        
        LOG.debug("completed routing.get {} ", ret);
        
        return ret;
    }

    @Override
    public Routing getOrNull(long routingId) {
        
        LOG.debug("calling routing.getOrNull {} ", routingId);

        ResponseEntity<Routing> responseEntity = restTemplate.getForEntity(
                getBaseUrl()
                +"/orNull?routingId={routingId}",
                Routing.class, routingId);
        
        Routing ret = responseEntity.getBody();
        
        LOG.debug("completed routing.getOrNull {} ", ret);
        
        return ret;
    }

	@Override
	public List<Routing> get(Set<Long> routingIdSet) {
		
        LOG.debug("get({})", routingIdSet);

        if (routingIdSet == null || routingIdSet.isEmpty()) {
            return Collections.emptyList();
        }

        String setString = routingIdSet.toString().substring(1, routingIdSet.toString().length() - 1);
        
        try {
            ResponseEntity<List<Routing>> responseEntity = restTemplate.exchange(
                    getBaseUrl() + "/inSet?routingIdSet={routingIdSet}", HttpMethod.GET,
                    null, Routing_LIST_CLASS_TOKEN, setString);

            List<Routing> result = responseEntity.getBody();
            if (null == result) {
                result = Collections.emptyList();
            }
            LOG.debug("get({}) return {} entries", routingIdSet, result.size());
            return result;
        } catch (Exception exp) {
            LOG.error("getAllInSet({}) exception ", routingIdSet, exp);
            throw exp;
        }

	}

	@Override
	public PaginationResponse<Routing> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
			PaginationContext<Routing> context) {
		
        LOG.debug("calling getForCustomer( {}, {}, {} )", customerId, sortBy, context);

        ResponseEntity<PaginationResponse<Routing>> responseEntity = restTemplate.exchange(
                getBaseUrl()
                        + "/forCustomer?customerId={customerId}&sortBy={sortBy}&paginationContext={paginationContext}",
                HttpMethod.GET, null, Routing_PAGINATION_RESPONSE_CLASS_TOKEN, customerId, sortBy, context);

        PaginationResponse<Routing> ret = responseEntity.getBody();
        LOG.debug("completed getForCustomer {} ", ret.getItems().size());

        return ret;
	}
	
    @Override
    public Routing update(Routing routing) {
        
        LOG.debug("calling routing.update {} ", routing);

        if (BaseJsonModel.hasUnsupportedValue(routing)) {
            LOG.error("Failed to update Routing, unsupported value in  {}", routing);
            throw new DsDataValidationException("Routing contains unsupported value");
        }
        
        HttpEntity<String> request = new HttpEntity<String>( routing.toString(), headers );

        ResponseEntity<Routing> responseEntity = restTemplate.exchange(
                getBaseUrl(),
                HttpMethod.PUT, request, Routing.class);
        
        Routing ret = responseEntity.getBody();
        
        LOG.debug("completed routing.update {} ", ret);
        
        return ret;
    }

    @Override
    public Routing delete(long routingId) {
        
        LOG.debug("calling routing.delete {} ", routingId);

        ResponseEntity<Routing> responseEntity =  restTemplate.exchange(
                getBaseUrl()
                +"?routingId={routingId}", HttpMethod.DELETE,
                null, Routing.class, routingId);
        
        Routing ret = responseEntity.getBody();
        LOG.debug("completed routing.delete {} ", ret);
        
        return ret;
    }    

    public String getBaseUrl() {
        if(baseUrl==null) {
            baseUrl = environment.getProperty("tip.wlan.routingServiceBaseUrl").trim()+"/api/routing";
        }

    	return baseUrl;
    }


}
