package com.telecominfraproject.wlan.systemevent;

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

import com.telecominfraproject.wlan.systemevent.models.SystemEventContainer;

/**
 * @author dtoptygin
 *
 */
@Component
public class SystemEventServiceRemote extends BaseRemoteClient implements SystemEventServiceInterface {

    private static final Logger LOG = LoggerFactory.getLogger(SystemEventServiceRemote.class);
    
    private static final ParameterizedTypeReference<List<SystemEventContainer>> SystemEventRecord_LIST_CLASS_TOKEN = new ParameterizedTypeReference<List<SystemEventContainer>>() {};

    private static final ParameterizedTypeReference<PaginationResponse<SystemEventContainer>> SystemEventRecord_PAGINATION_RESPONSE_CLASS_TOKEN = new ParameterizedTypeReference<PaginationResponse<SystemEventContainer>>() {};


    private String baseUrl;
            
    @Override
    public SystemEventContainer create(SystemEventContainer systemEventRecord) {
        
        LOG.debug("calling systemEvent.create {} ", systemEventRecord);

        if (BaseJsonModel.hasUnsupportedValue(systemEventRecord)) {
            LOG.error("Failed to create SystemEventContainer, unsupported value in {}", systemEventRecord);
            throw new DsDataValidationException("SystemEventContainer contains unsupported value");
        }

        HttpEntity<String> request = new HttpEntity<String>( systemEventRecord.toString(), headers );

        ResponseEntity<SystemEventContainer> responseEntity = restTemplate.postForEntity(
                getBaseUrl(),
                request, SystemEventContainer.class);
        
        SystemEventContainer ret = responseEntity.getBody();
        
        LOG.debug("completed systemEvent.create {} ", ret);
        
        return ret;
    }

    @Override
    public SystemEventContainer get(long systemEventRecordId) {
        
        LOG.debug("calling systemEvent.get {} ", systemEventRecordId);

        ResponseEntity<SystemEventContainer> responseEntity = restTemplate.getForEntity(
                getBaseUrl()
                +"?systemEventRecordId={systemEventRecordId}",
                SystemEventContainer.class, systemEventRecordId);
        
        SystemEventContainer ret = responseEntity.getBody();
        
        LOG.debug("completed systemEvent.get {} ", ret);
        
        return ret;
    }

    @Override
    public SystemEventContainer getOrNull(long systemEventRecordId) {
        
        LOG.debug("calling systemEvent.getOrNull {} ", systemEventRecordId);

        ResponseEntity<SystemEventContainer> responseEntity = restTemplate.getForEntity(
                getBaseUrl()
                +"/orNull?systemEventRecordId={systemEventRecordId}",
                SystemEventContainer.class, systemEventRecordId);
        
        SystemEventContainer ret = responseEntity.getBody();
        
        LOG.debug("completed systemEvent.getOrNull {} ", ret);
        
        return ret;
    }

	@Override
	public List<SystemEventContainer> get(Set<Long> systemEventRecordIdSet) {
		
        LOG.debug("get({})", systemEventRecordIdSet);

        if (systemEventRecordIdSet == null || systemEventRecordIdSet.isEmpty()) {
            return Collections.emptyList();
        }

        String setString = systemEventRecordIdSet.toString().substring(1, systemEventRecordIdSet.toString().length() - 1);
        
        try {
            ResponseEntity<List<SystemEventContainer>> responseEntity = restTemplate.exchange(
                    getBaseUrl() + "/inSet?systemEventRecordIdSet={systemEventRecordIdSet}", HttpMethod.GET,
                    null, SystemEventRecord_LIST_CLASS_TOKEN, setString);

            List<SystemEventContainer> result = responseEntity.getBody();
            if (null == result) {
                result = Collections.emptyList();
            }
            LOG.debug("get({}) return {} entries", systemEventRecordIdSet, result.size());
            return result;
        } catch (Exception exp) {
            LOG.error("getAllInSet({}) exception ", systemEventRecordIdSet, exp);
            throw exp;
        }

	}

	@Override
	public PaginationResponse<SystemEventContainer> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
			PaginationContext<SystemEventContainer> context) {
		
        LOG.debug("calling getForCustomer( {}, {}, {} )", customerId, sortBy, context);

        ResponseEntity<PaginationResponse<SystemEventContainer>> responseEntity = restTemplate.exchange(
                getBaseUrl()
                        + "/forCustomer?customerId={customerId}&sortBy={sortBy}&paginationContext={paginationContext}",
                HttpMethod.GET, null, SystemEventRecord_PAGINATION_RESPONSE_CLASS_TOKEN, customerId, sortBy, context);

        PaginationResponse<SystemEventContainer> ret = responseEntity.getBody();
        LOG.debug("completed getForCustomer {} ", ret.getItems().size());

        return ret;
	}
	
    @Override
    public SystemEventContainer update(SystemEventContainer systemEventRecord) {
        
        LOG.debug("calling systemEvent.update {} ", systemEventRecord);

        if (BaseJsonModel.hasUnsupportedValue(systemEventRecord)) {
            LOG.error("Failed to update SystemEventContainer, unsupported value in  {}", systemEventRecord);
            throw new DsDataValidationException("SystemEventContainer contains unsupported value");
        }
        
        HttpEntity<String> request = new HttpEntity<String>( systemEventRecord.toString(), headers );

        ResponseEntity<SystemEventContainer> responseEntity = restTemplate.exchange(
                getBaseUrl(),
                HttpMethod.PUT, request, SystemEventContainer.class);
        
        SystemEventContainer ret = responseEntity.getBody();
        
        LOG.debug("completed systemEvent.update {} ", ret);
        
        return ret;
    }

    @Override
    public SystemEventContainer delete(long systemEventRecordId) {
        
        LOG.debug("calling systemEvent.delete {} ", systemEventRecordId);

        ResponseEntity<SystemEventContainer> responseEntity =  restTemplate.exchange(
                getBaseUrl()
                +"?systemEventRecordId={systemEventRecordId}", HttpMethod.DELETE,
                null, SystemEventContainer.class, systemEventRecordId);
        
        SystemEventContainer ret = responseEntity.getBody();
        LOG.debug("completed systemEvent.delete {} ", ret);
        
        return ret;
    }    

    public String getBaseUrl() {
        if(baseUrl==null) {
            baseUrl = environment.getProperty("tip.wlan.systemEventServiceBaseUrl").trim()+"/api/systemEvent";
        }

    	return baseUrl;
    }


}
