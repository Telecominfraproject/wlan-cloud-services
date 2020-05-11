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
import com.telecominfraproject.wlan.core.model.service.GatewayType;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;
import com.telecominfraproject.wlan.routing.models.EquipmentGatewayRecord;
import com.telecominfraproject.wlan.routing.models.EquipmentRoutingRecord;

/**
 * @author dtoptygin
 *
 */
@Component
public class RoutingServiceRemote extends BaseRemoteClient implements RoutingServiceInterface {

    private static final Logger LOG = LoggerFactory.getLogger(RoutingServiceRemote.class);
    
    private static final ParameterizedTypeReference<List<EquipmentRoutingRecord>> Routing_LIST_CLASS_TOKEN = new ParameterizedTypeReference<List<EquipmentRoutingRecord>>() {};
    private static final ParameterizedTypeReference<List<EquipmentGatewayRecord>> Gateway_LIST_CLASS_TOKEN = new ParameterizedTypeReference<List<EquipmentGatewayRecord>>() {};

    private static final ParameterizedTypeReference<PaginationResponse<EquipmentRoutingRecord>> Routing_PAGINATION_RESPONSE_CLASS_TOKEN = new ParameterizedTypeReference<PaginationResponse<EquipmentRoutingRecord>>() {};


    private String baseUrl;
            
    @Override
    public EquipmentRoutingRecord create(EquipmentRoutingRecord routing) {
        
        LOG.debug("calling routing.create {} ", routing);

        if (BaseJsonModel.hasUnsupportedValue(routing)) {
            LOG.error("Failed to create EquipmentRoutingRecord, unsupported value in {}", routing);
            throw new DsDataValidationException("EquipmentRoutingRecord contains unsupported value");
        }

        HttpEntity<String> request = new HttpEntity<String>( routing.toString(), headers );

        ResponseEntity<EquipmentRoutingRecord> responseEntity = restTemplate.postForEntity(
                getBaseUrl(),
                request, EquipmentRoutingRecord.class);
        
        EquipmentRoutingRecord ret = responseEntity.getBody();
        
        LOG.debug("completed routing.create {} ", ret);
        
        return ret;
    }

    @Override
    public EquipmentRoutingRecord get(long routingId) {
        
        LOG.debug("calling routing.get {} ", routingId);

        ResponseEntity<EquipmentRoutingRecord> responseEntity = restTemplate.getForEntity(
                getBaseUrl()
                +"?routingId={routingId}",
                EquipmentRoutingRecord.class, routingId);
        
        EquipmentRoutingRecord ret = responseEntity.getBody();
        
        LOG.debug("completed routing.get {} ", ret);
        
        return ret;
    }

    @Override
    public EquipmentRoutingRecord getOrNull(long routingId) {
        
        LOG.debug("calling routing.getOrNull {} ", routingId);

        ResponseEntity<EquipmentRoutingRecord> responseEntity = restTemplate.getForEntity(
                getBaseUrl()
                +"/orNull?routingId={routingId}",
                EquipmentRoutingRecord.class, routingId);
        
        EquipmentRoutingRecord ret = responseEntity.getBody();
        
        LOG.debug("completed routing.getOrNull {} ", ret);
        
        return ret;
    }

	@Override
	public List<EquipmentRoutingRecord> get(Set<Long> routingIdSet) {
		
        LOG.debug("get({})", routingIdSet);

        if (routingIdSet == null || routingIdSet.isEmpty()) {
            return Collections.emptyList();
        }

        String setString = routingIdSet.toString().substring(1, routingIdSet.toString().length() - 1);
        
        try {
            ResponseEntity<List<EquipmentRoutingRecord>> responseEntity = restTemplate.exchange(
                    getBaseUrl() + "/inSet?routingIdSet={routingIdSet}", HttpMethod.GET,
                    null, Routing_LIST_CLASS_TOKEN, setString);

            List<EquipmentRoutingRecord> result = responseEntity.getBody();
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
	public PaginationResponse<EquipmentRoutingRecord> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
			PaginationContext<EquipmentRoutingRecord> context) {
		
        LOG.debug("calling getForCustomer( {}, {}, {} )", customerId, sortBy, context);

        ResponseEntity<PaginationResponse<EquipmentRoutingRecord>> responseEntity = restTemplate.exchange(
                getBaseUrl()
                        + "/forCustomer?customerId={customerId}&sortBy={sortBy}&paginationContext={paginationContext}",
                HttpMethod.GET, null, Routing_PAGINATION_RESPONSE_CLASS_TOKEN, customerId, sortBy, context);

        PaginationResponse<EquipmentRoutingRecord> ret = responseEntity.getBody();
        LOG.debug("completed getForCustomer {} ", ret.getItems().size());

        return ret;
	}
	
    @Override
    public EquipmentRoutingRecord update(EquipmentRoutingRecord routing) {
        
        LOG.debug("calling routing.update {} ", routing);

        if (BaseJsonModel.hasUnsupportedValue(routing)) {
            LOG.error("Failed to update EquipmentRoutingRecord, unsupported value in  {}", routing);
            throw new DsDataValidationException("EquipmentRoutingRecord contains unsupported value");
        }
        
        HttpEntity<String> request = new HttpEntity<String>( routing.toString(), headers );

        ResponseEntity<EquipmentRoutingRecord> responseEntity = restTemplate.exchange(
                getBaseUrl(),
                HttpMethod.PUT, request, EquipmentRoutingRecord.class);
        
        EquipmentRoutingRecord ret = responseEntity.getBody();
        
        LOG.debug("completed routing.update {} ", ret);
        
        return ret;
    }

    @Override
    public EquipmentRoutingRecord delete(long routingId) {
        
        LOG.debug("calling routing.delete {} ", routingId);

        ResponseEntity<EquipmentRoutingRecord> responseEntity =  restTemplate.exchange(
                getBaseUrl()
                +"?routingId={routingId}", HttpMethod.DELETE,
                null, EquipmentRoutingRecord.class, routingId);
        
        EquipmentRoutingRecord ret = responseEntity.getBody();
        LOG.debug("completed routing.delete {} ", ret);
        
        return ret;
    }    

    @Override
	public EquipmentGatewayRecord registerGateway(EquipmentGatewayRecord equipmentGwRecord) {
        LOG.debug("calling registerGateway {} ", equipmentGwRecord);

        if (BaseJsonModel.hasUnsupportedValue(equipmentGwRecord)) {
            LOG.error("Failed to create EquipmentGatewayRecord, unsupported value in {}", equipmentGwRecord);
            throw new DsDataValidationException("EquipmentGatewayRecord contains unsupported value");
        }

        HttpEntity<String> request = new HttpEntity<String>( equipmentGwRecord.toString(), headers );

        ResponseEntity<EquipmentGatewayRecord> responseEntity = restTemplate.postForEntity(
                getBaseUrl() + "/gateway",
                request, EquipmentGatewayRecord.class);
        
        EquipmentGatewayRecord ret = responseEntity.getBody();
        
        LOG.debug("completed registerGateway {} ", ret);
        
        return ret;
	}

	@Override
	public EquipmentGatewayRecord getGateway(long gatewayId) {
        LOG.debug("calling getGateway {} ", gatewayId);

        ResponseEntity<EquipmentGatewayRecord> responseEntity = restTemplate.getForEntity(
                getBaseUrl()
                +"/gateway?gatewayId={gatewayId}",
                EquipmentGatewayRecord.class, gatewayId);
        
        EquipmentGatewayRecord ret = responseEntity.getBody();
        
        LOG.debug("completed getGateway {} ", ret);
        
        return ret;
	}

	@Override
	public List<EquipmentGatewayRecord> getGateway(String hostname) {
        LOG.debug("getGateway({})", hostname);

        ResponseEntity<List<EquipmentGatewayRecord>> responseEntity = restTemplate.exchange(
                getBaseUrl() + "/gateway/byHostname?hostname={hostname}", HttpMethod.GET,
                null, Gateway_LIST_CLASS_TOKEN, hostname);

        List<EquipmentGatewayRecord> result = responseEntity.getBody();
        if (null == result) {
            result = Collections.emptyList();
        }
        LOG.debug("getGateway({}) return {} entries", hostname, result.size());
        return result;
	}

	@Override
	public List<EquipmentGatewayRecord> getGateway(GatewayType gatewayType) {
        LOG.debug("getGateway({})", gatewayType);

        ResponseEntity<List<EquipmentGatewayRecord>> responseEntity = restTemplate.exchange(
                getBaseUrl() + "/gateway/byType?gatewayType={gatewayType}", HttpMethod.GET,
                null, Gateway_LIST_CLASS_TOKEN, gatewayType);

        List<EquipmentGatewayRecord> result = responseEntity.getBody();
        if (null == result) {
            result = Collections.emptyList();
        }
        LOG.debug("getGateway({}) return {} entries", gatewayType, result.size());
        return result;
	}

	@Override
	public List<EquipmentRoutingRecord> getRegisteredRouteList(long equipmentId) {
        LOG.debug("getRegisteredRouteList({})", equipmentId);

        ResponseEntity<List<EquipmentRoutingRecord>> responseEntity = restTemplate.exchange(
                getBaseUrl() + "/byEquipmentId?equipmentId={equipmentId}", HttpMethod.GET,
                null, Routing_LIST_CLASS_TOKEN, equipmentId);

        List<EquipmentRoutingRecord> result = responseEntity.getBody();
        if (null == result) {
            result = Collections.emptyList();
        }
        LOG.debug("getRegisteredRouteList({}) return {} entries", equipmentId, result.size());
        return result;
	}

	@Override
	public List<EquipmentGatewayRecord> getRegisteredGatewayRecordList(long equipmentId) {
        LOG.debug("getRegisteredGatewayRecordList({})", equipmentId);

        ResponseEntity<List<EquipmentGatewayRecord>> responseEntity = restTemplate.exchange(
                getBaseUrl() + "/gateway/byEquipmentId?equipmentId={equipmentId}", HttpMethod.GET,
                null, Gateway_LIST_CLASS_TOKEN, equipmentId);

        List<EquipmentGatewayRecord> result = responseEntity.getBody();
        if (null == result) {
            result = Collections.emptyList();
        }
        LOG.debug("getRegisteredGatewayRecordList({}) return {} entries", equipmentId, result.size());
        return result;
	}

	@Override
	public EquipmentGatewayRecord updateGateway(EquipmentGatewayRecord equipmentGwRecord) {
        LOG.debug("calling updateGateway {} ", equipmentGwRecord);

        if (BaseJsonModel.hasUnsupportedValue(equipmentGwRecord)) {
            LOG.error("Failed to update EquipmentGatewayRecord, unsupported value in  {}", equipmentGwRecord);
            throw new DsDataValidationException("EquipmentGatewayRecord contains unsupported value");
        }
        
        HttpEntity<String> request = new HttpEntity<String>( equipmentGwRecord.toString(), headers );

        ResponseEntity<EquipmentGatewayRecord> responseEntity = restTemplate.exchange(
                getBaseUrl() + "/gateway",
                HttpMethod.PUT, request, EquipmentGatewayRecord.class);
        
        EquipmentGatewayRecord ret = responseEntity.getBody();
        
        LOG.debug("completed updateGateway {} ", ret);
        
        return ret;
	}

	@Override
	public EquipmentGatewayRecord deleteGateway(long gatewayId) {
        LOG.debug("calling deleteGateway {} ", gatewayId);

        ResponseEntity<EquipmentGatewayRecord> responseEntity =  restTemplate.exchange(
                getBaseUrl()
                +"/gateway?gatewayId={gatewayId}", HttpMethod.DELETE,
                null, EquipmentGatewayRecord.class, gatewayId);
        
        EquipmentGatewayRecord ret = responseEntity.getBody();
        LOG.debug("completed deleteGateway {} ", ret);
        
        return ret;
	}

	@Override
	public List<EquipmentGatewayRecord> deleteGateway(String hostname) {
        LOG.debug("deleteGateway({})", hostname);

        ResponseEntity<List<EquipmentGatewayRecord>> responseEntity = restTemplate.exchange(
                getBaseUrl() + "/gateway/byHostname?hostname={hostname}", HttpMethod.DELETE,
                null, Gateway_LIST_CLASS_TOKEN, hostname);

        List<EquipmentGatewayRecord> result = responseEntity.getBody();
        if (null == result) {
            result = Collections.emptyList();
        }
        LOG.debug("deleteGateway({}) return {} entries", hostname, result.size());
        return result;
	}

	public String getBaseUrl() {
        if(baseUrl==null) {
            baseUrl = environment.getProperty("tip.wlan.routingServiceBaseUrl").trim()+"/api/routing";
        }

    	return baseUrl;
    }


}
