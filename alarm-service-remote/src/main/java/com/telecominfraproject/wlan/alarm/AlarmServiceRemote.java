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

import com.telecominfraproject.wlan.alarm.models.Alarm;
import com.telecominfraproject.wlan.alarm.models.AlarmCode;
import com.telecominfraproject.wlan.alarm.models.AlarmCounts;
import com.telecominfraproject.wlan.core.client.BaseRemoteClient;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.json.GenericResponse;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;

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
    public GenericResponse resetAlarmCounters() {
        
        LOG.debug("calling resetAlarmCounters ");
        
        ResponseEntity<GenericResponse> responseEntity = restTemplate.postForEntity(
                getBaseUrl() + "/resetCounts",
                null, GenericResponse.class);
        
        GenericResponse ret = responseEntity.getBody();
        
        LOG.debug("completed resetAlarmCounters {} ", ret);
        
        return ret;
    }

    @Override
    public Alarm getOrNull(int customerId, long equipmentId, AlarmCode alarmCode, long createdTimestamp) {
    	
        LOG.debug("calling alarm.getOrNull  {} {} {} {}", customerId, equipmentId, alarmCode, createdTimestamp);

        ResponseEntity<Alarm> responseEntity = restTemplate.getForEntity(
                getBaseUrl()
                +"/orNull?customerId={customerId}&equipmentId={equipmentId}&alarmCode={alarmCode}&createdTimestamp={createdTimestamp}",
                Alarm.class, customerId, equipmentId, alarmCode, createdTimestamp);
        
        Alarm ret = responseEntity.getBody();
        
        LOG.debug("completed alarm.getOrNull {} ", ret);
        
        return ret;
    }

    @Override
    	public List<Alarm> get(int customerId, Set<Long> equipmentIdSet, Set<AlarmCode> alarmCodeSet,
    			long createdAfterTimestamp) {
		
		LOG.debug("get({}, {}, {},{})", customerId, equipmentIdSet, alarmCodeSet, createdAfterTimestamp);

        if (equipmentIdSet == null || equipmentIdSet.isEmpty()) {
            throw new IllegalArgumentException("equipmentIdSet must be provided");
        }

        String equipmentIdSetStr = equipmentIdSet.toString();
        equipmentIdSetStr = equipmentIdSetStr.substring(1, equipmentIdSetStr.length() - 1);

        String alarmCodeSetStr = null;
        if (alarmCodeSet != null && !alarmCodeSet.isEmpty()) {
        	alarmCodeSetStr = alarmCodeSet.toString();
            // remove [] around the string, otherwise will get:
            // Failed to convert value of type 'java.lang.String' to required
            // type 'java.util.Set'; nested exception is
            // java.lang.NumberFormatException: For input string: "[690]"
        	alarmCodeSetStr = alarmCodeSetStr.substring(1, alarmCodeSetStr.length() - 1);
        }
        
        try {
            ResponseEntity<List<Alarm>> responseEntity = restTemplate.exchange(
                    getBaseUrl() + "/forEquipment?customerId={customerId}&equipmentIdSet={equipmentIdSetStr}&alarmCodeSet={alarmCodeSetStr}&createdAfterTimestamp={createdAfterTimestamp}", HttpMethod.GET,
                    null, Alarm_LIST_CLASS_TOKEN, customerId, equipmentIdSetStr, alarmCodeSetStr, createdAfterTimestamp);

            List<Alarm> result = responseEntity.getBody();
            if (null == result) {
                result = Collections.emptyList();
            }
            LOG.debug("get({}, {}, {},{}) return {} entries", customerId, equipmentIdSet, alarmCodeSet, createdAfterTimestamp, result.size());
            return result;
        } catch (Exception exp) {
            LOG.error("getAllInSet({}, {}, {},{}) exception ", customerId, equipmentIdSet, alarmCodeSet, createdAfterTimestamp, exp);
            throw exp;
        }

	}

    @Override
    public AlarmCounts getAlarmCounts(int customerId, Set<Long> equipmentIdSet, Set<AlarmCode> alarmCodeSet) {

		LOG.debug("getAlarmCounts({}, {}, {})", customerId, equipmentIdSet, alarmCodeSet);

        String equipmentIdSetStr = null;
        if (equipmentIdSet != null && !equipmentIdSet.isEmpty()) {
            equipmentIdSetStr = equipmentIdSet.toString();
            equipmentIdSetStr = equipmentIdSetStr.substring(1, equipmentIdSetStr.length() - 1);
        }

        String alarmCodeSetStr = null;
        if (alarmCodeSet != null && !alarmCodeSet.isEmpty()) {
        	alarmCodeSetStr = alarmCodeSet.toString();
            // remove [] around the string, otherwise will get:
            // Failed to convert value of type 'java.lang.String' to required
            // type 'java.util.Set'; nested exception is
            // java.lang.NumberFormatException: For input string: "[690]"
        	alarmCodeSetStr = alarmCodeSetStr.substring(1, alarmCodeSetStr.length() - 1);
        }
        
        ResponseEntity<AlarmCounts> responseEntity = restTemplate.exchange(
                    getBaseUrl() + "/counts?customerId={customerId}&equipmentIdSet={equipmentIdSetStr}&alarmCodeSet={alarmCodeSetStr}", HttpMethod.GET,
                    null, AlarmCounts.class, customerId, equipmentIdSetStr, alarmCodeSetStr);

        AlarmCounts ret = responseEntity.getBody();
        LOG.debug("completed getAlarmCounts {} ", ret);

        return ret;

    }
    
    @Override
    public PaginationResponse<Alarm> getForCustomer(int customerId, Set<Long> equipmentIdSet,
    		Set<AlarmCode> alarmCodeSet, long createdAfterTimestamp, List<ColumnAndSort> sortBy,
    		PaginationContext<Alarm> context) {
		
        LOG.debug("calling getForCustomer( {}, {}, {}, {}, {}, {} )", customerId, equipmentIdSet, alarmCodeSet, createdAfterTimestamp, sortBy, context);

        String equipmentIdSetStr = null;
        if (equipmentIdSet != null && !equipmentIdSet.isEmpty()) {
            equipmentIdSetStr = equipmentIdSet.toString();
            equipmentIdSetStr = equipmentIdSetStr.substring(1, equipmentIdSetStr.length() - 1);
        }
        
        String alarmCodeSetStr = null;
        if (alarmCodeSet != null && !alarmCodeSet.isEmpty()) {
        	alarmCodeSetStr = alarmCodeSet.toString();
            // remove [] around the string, otherwise will get:
            // Failed to convert value of type 'java.lang.String' to required
            // type 'java.util.Set'; nested exception is
            // java.lang.NumberFormatException: For input string: "[690]"
        	alarmCodeSetStr = alarmCodeSetStr.substring(1, alarmCodeSetStr.length() - 1);
        }

        ResponseEntity<PaginationResponse<Alarm>> responseEntity = restTemplate.exchange(
                getBaseUrl()
                		+ "/forCustomer?customerId={customerId}&equipmentIdSet={equipmentIdSetStr}&alarmCodeSet={alarmCodeSetStr}&createdAfterTimestamp={createdAfterTimestamp}&sortBy={sortBy}&paginationContext={paginationContext}", 
                		HttpMethod.GET,
                null, Alarm_PAGINATION_RESPONSE_CLASS_TOKEN, customerId, equipmentIdSetStr, alarmCodeSetStr, createdAfterTimestamp, sortBy, context);

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
    public Alarm delete(int customerId, long equipmentId, AlarmCode alarmCode, long createdTimestamp) {
        
        LOG.debug("calling alarm.delete {} {} {} {}", customerId, equipmentId, alarmCode, createdTimestamp);

        ResponseEntity<Alarm> responseEntity =  restTemplate.exchange(
                getBaseUrl()
                +"?customerId={customerId}&equipmentId={equipmentId}&alarmCode={alarmCode}&createdTimestamp={createdTimestamp}",
                HttpMethod.DELETE, null,
                Alarm.class, customerId, equipmentId, alarmCode, createdTimestamp);
        
        Alarm ret = responseEntity.getBody();
        LOG.debug("completed alarm.delete {} ", ret);
        
        return ret;
    }    

    @Override
    public List<Alarm> delete(int customerId, long equipmentId) {
        LOG.debug("calling alarm.delete {} {} ", customerId, equipmentId);

        ResponseEntity<List<Alarm>> responseEntity =  restTemplate.exchange(
                getBaseUrl()
                +"/forEquipment?customerId={customerId}&equipmentId={equipmentId}",
                HttpMethod.DELETE, null,
                Alarm_LIST_CLASS_TOKEN, customerId, equipmentId);
        
        List<Alarm> ret = responseEntity.getBody();
        LOG.debug("completed alarm.delete {} ", ret.size());
        
        return ret;
    }
    
    public String getBaseUrl() {
        if(baseUrl==null) {
            baseUrl = environment.getProperty("tip.wlan.alarmServiceBaseUrl").trim()+"/api/alarm";
        }

    	return baseUrl;
    }


}
