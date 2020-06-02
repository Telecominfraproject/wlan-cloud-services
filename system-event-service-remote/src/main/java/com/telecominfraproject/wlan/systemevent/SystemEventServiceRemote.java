package com.telecominfraproject.wlan.systemevent;

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
import com.telecominfraproject.wlan.core.model.json.GenericResponse;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;
import com.telecominfraproject.wlan.systemevent.models.SystemEventRecord;

/**
 * @author dtoptygin
 *
 */
@Component
public class SystemEventServiceRemote extends BaseRemoteClient implements SystemEventServiceInterface {

    private static final Logger LOG = LoggerFactory.getLogger(SystemEventServiceRemote.class);
    
    private static final ParameterizedTypeReference<List<SystemEventRecord>> SystemEventRecord_LIST_CLASS_TOKEN = new ParameterizedTypeReference<List<SystemEventRecord>>() {};

    private static final ParameterizedTypeReference<PaginationResponse<SystemEventRecord>> SystemEventRecord_PAGINATION_RESPONSE_CLASS_TOKEN = new ParameterizedTypeReference<PaginationResponse<SystemEventRecord>>() {};


    private String baseUrl;
            
    @Override
    public GenericResponse create(SystemEventRecord systemEventRecord) {
        
        LOG.debug("calling systemEventRecord.create {} ", systemEventRecord);

        if (BaseJsonModel.hasUnsupportedValue(systemEventRecord)) {
            LOG.error("Failed to create SystemEventRecord, unsupported value in {}", systemEventRecord);
            throw new DsDataValidationException("SystemEventRecord contains unsupported value");
        }

        HttpEntity<String> request = new HttpEntity<String>( systemEventRecord.toString(), headers );

        ResponseEntity<GenericResponse> responseEntity = restTemplate.postForEntity(
                getBaseUrl(),
                request, GenericResponse.class);
        
        GenericResponse ret = responseEntity.getBody();
        
        LOG.debug("completed systemEventRecord.create {} ", ret);
        
        return ret;
    }


    @Override
    public GenericResponse create(List<SystemEventRecord> systemEventRecords) {
        LOG.debug("calling bulk systemEventRecord.create {} ", systemEventRecords.size());

        if (BaseJsonModel.hasUnsupportedValue(systemEventRecords)) {
            LOG.error("Failed to create SystemEventRecord, unsupported value in {}", systemEventRecords);
            throw new DsDataValidationException("SystemEventRecord contains unsupported value");
        }

        HttpEntity<String> request = new HttpEntity<String>( systemEventRecords.toString(), headers );

        ResponseEntity<GenericResponse> responseEntity = restTemplate.postForEntity(
                getBaseUrl()+"/bulk",
                request, GenericResponse.class);
        
        GenericResponse ret = responseEntity.getBody();
        
        LOG.debug("completed bulk systemEventRecord.create {} ", systemEventRecords.size());
        
        return ret;
    }
    
    @Override
    public PaginationResponse<SystemEventRecord> getForCustomer(long fromTime, long toTime, int customerId,
    		Set<Long> equipmentIds, Set<String> dataTypes,
    		List<ColumnAndSort> sortBy, PaginationContext<SystemEventRecord> context) {
		
        LOG.debug("calling getForCustomer( {}, {}, {}, {}, {}, {}, {} )", 
        		fromTime, toTime, customerId, equipmentIds, 
        		dataTypes, sortBy, context);

        String equipmentIdsStr = null;
        if (equipmentIds != null && !equipmentIds.isEmpty()) {
            equipmentIdsStr = equipmentIds.toString();
            // remove [] around the string, otherwise will get:
            // Failed to convert value of type 'java.lang.String' to required
            // type 'java.util.Set'; nested exception is
            // java.lang.NumberFormatException: For input string: "[690]"
            equipmentIdsStr = equipmentIdsStr.substring(1, equipmentIdsStr.length() - 1);
        }

        String dataTypesStr = null;
        if (dataTypes != null && !dataTypes.isEmpty()) {
        	dataTypesStr = dataTypes.toString();
            // remove [] around the string, otherwise will get:
            // Failed to convert value of type 'java.lang.String' to required
            // type 'java.util.Set'; nested exception is
            // java.lang.NumberFormatException: For input string: "[690]"
        	dataTypesStr = dataTypesStr.substring(1, dataTypesStr.length() - 1);
        }

        ResponseEntity<PaginationResponse<SystemEventRecord>> responseEntity = restTemplate.exchange(
                getBaseUrl()
                        + "/forCustomer?fromTime={fromTime}&toTime={toTime}&customerId={customerId}"
                        + "&equipmentIds={equipmentIdsStr}&dataTypes={dataTypesStr}"
                        + "&sortBy={sortBy}&paginationContext={paginationContext}",
                HttpMethod.GET, null, SystemEventRecord_PAGINATION_RESPONSE_CLASS_TOKEN, 
                fromTime, toTime, customerId, equipmentIdsStr, dataTypesStr, sortBy, context);

        PaginationResponse<SystemEventRecord> ret = responseEntity.getBody();
        LOG.debug("completed getForCustomer {} ", ret.getItems().size());

        return ret;
	}
	
	@Override
	public GenericResponse delete(int customerId, long equipmentId, long createdBeforeTimestamp) {
        
        LOG.debug("calling systemEventRecord.delete {} {} {}", customerId, equipmentId, createdBeforeTimestamp);

        ResponseEntity<GenericResponse> responseEntity =  restTemplate.exchange(
                getBaseUrl()
                +"?customerId={customerId}&equipmentId={equipmentId}&createdBeforeTimestamp={createdBeforeTimestamp}", HttpMethod.DELETE,
                null, GenericResponse.class, customerId, equipmentId, createdBeforeTimestamp);
        
        GenericResponse ret = responseEntity.getBody();
        LOG.debug("completed systemEventRecord.delete {} {} {}", customerId, equipmentId, createdBeforeTimestamp);
        
        return ret;
    }    

    public String getBaseUrl() {
        if(baseUrl==null) {
            baseUrl = environment.getProperty("tip.wlan.systemEventServiceBaseUrl").trim()+"/api/systemEvent";
        }

    	return baseUrl;
    }


}
