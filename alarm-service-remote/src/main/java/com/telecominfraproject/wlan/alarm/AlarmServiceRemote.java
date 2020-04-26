package com.telecominfraproject.wlan.alarm;

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

import com.telecominfraproject.wlan.alarm.models.Alarm;

/**
 * @author dtoptygin
 *
 */
@Component
public class AlarmServiceRemote extends BaseRemoteClient implements AlarmServiceInterface {

    private static final Logger LOG = LoggerFactory.getLogger(AlarmServiceRemote.class);
    
    private static final ParameterizedTypeReference<List<Alarm>> Alarm_LIST_CLASS_TOKEN = new ParameterizedTypeReference<List<Alarm>>() {};

    private static final ParameterizedTypeReference<PaginationResponse<Alarm>> Alarm_PAGINATION_RESPONSE_CLASS_TOKEN = new ParameterizedTypeReference<PaginationResponse<Alarm>>() {};


    private String baseUrl;
            
    @Override
    public Alarm create(Alarm alarm) {
        
        LOG.debug("calling alarm.create {} ", alarm);

        if (BaseJsonModel.hasUnsupportedValue(alarm)) {
            LOG.error("Failed to create Alarm, unsupported value in {}", alarm);
            throw new DsDataValidationException("Alarm contains unsupported value");
        }

        HttpEntity<String> request = new HttpEntity<String>( alarm.toString(), headers );

        ResponseEntity<Alarm> responseEntity = restTemplate.postForEntity(
                getBaseUrl(),
                request, Alarm.class);
        
        Alarm ret = responseEntity.getBody();
        
        LOG.debug("completed alarm.create {} ", ret);
        
        return ret;
    }

    @Override
    public Alarm get(long alarmId) {
        
        LOG.debug("calling alarm.get {} ", alarmId);

        ResponseEntity<Alarm> responseEntity = restTemplate.getForEntity(
                getBaseUrl()
                +"?alarmId={alarmId}",
                Alarm.class, alarmId);
        
        Alarm ret = responseEntity.getBody();
        
        LOG.debug("completed alarm.get {} ", ret);
        
        return ret;
    }

    @Override
    public Alarm getOrNull(long alarmId) {
        
        LOG.debug("calling alarm.getOrNull {} ", alarmId);

        ResponseEntity<Alarm> responseEntity = restTemplate.getForEntity(
                getBaseUrl()
                +"/orNull?alarmId={alarmId}",
                Alarm.class, alarmId);
        
        Alarm ret = responseEntity.getBody();
        
        LOG.debug("completed alarm.getOrNull {} ", ret);
        
        return ret;
    }

	@Override
	public List<Alarm> get(Set<Long> alarmIdSet) {
		
        LOG.debug("get({})", alarmIdSet);

        if (alarmIdSet == null || alarmIdSet.isEmpty()) {
            return Collections.emptyList();
        }

        String setString = alarmIdSet.toString().substring(1, alarmIdSet.toString().length() - 1);
        
        try {
            ResponseEntity<List<Alarm>> responseEntity = restTemplate.exchange(
                    getBaseUrl() + "/inSet?alarmIdSet={alarmIdSet}", HttpMethod.GET,
                    null, Alarm_LIST_CLASS_TOKEN, setString);

            List<Alarm> result = responseEntity.getBody();
            if (null == result) {
                result = Collections.emptyList();
            }
            LOG.debug("get({}) return {} entries", alarmIdSet, result.size());
            return result;
        } catch (Exception exp) {
            LOG.error("getAllInSet({}) exception ", alarmIdSet, exp);
            throw exp;
        }

	}

	@Override
	public PaginationResponse<Alarm> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
			PaginationContext<Alarm> context) {
		
        LOG.debug("calling getForCustomer( {}, {}, {} )", customerId, sortBy, context);

        ResponseEntity<PaginationResponse<Alarm>> responseEntity = restTemplate.exchange(
                getBaseUrl()
                        + "/forCustomer?customerId={customerId}&sortBy={sortBy}&paginationContext={paginationContext}",
                HttpMethod.GET, null, Alarm_PAGINATION_RESPONSE_CLASS_TOKEN, customerId, sortBy, context);

        PaginationResponse<Alarm> ret = responseEntity.getBody();
        LOG.debug("completed getForCustomer {} ", ret.getItems().size());

        return ret;
	}
	
    @Override
    public Alarm update(Alarm alarm) {
        
        LOG.debug("calling alarm.update {} ", alarm);

        if (BaseJsonModel.hasUnsupportedValue(alarm)) {
            LOG.error("Failed to update Alarm, unsupported value in  {}", alarm);
            throw new DsDataValidationException("Alarm contains unsupported value");
        }
        
        HttpEntity<String> request = new HttpEntity<String>( alarm.toString(), headers );

        ResponseEntity<Alarm> responseEntity = restTemplate.exchange(
                getBaseUrl(),
                HttpMethod.PUT, request, Alarm.class);
        
        Alarm ret = responseEntity.getBody();
        
        LOG.debug("completed alarm.update {} ", ret);
        
        return ret;
    }

    @Override
    public Alarm delete(long alarmId) {
        
        LOG.debug("calling alarm.delete {} ", alarmId);

        ResponseEntity<Alarm> responseEntity =  restTemplate.exchange(
                getBaseUrl()
                +"?alarmId={alarmId}", HttpMethod.DELETE,
                null, Alarm.class, alarmId);
        
        Alarm ret = responseEntity.getBody();
        LOG.debug("completed alarm.delete {} ", ret);
        
        return ret;
    }    

    public String getBaseUrl() {
        if(baseUrl==null) {
            baseUrl = environment.getProperty("tip.wlan.alarmServiceBaseUrl").trim()+"/api/alarm";
        }

    	return baseUrl;
    }


}
