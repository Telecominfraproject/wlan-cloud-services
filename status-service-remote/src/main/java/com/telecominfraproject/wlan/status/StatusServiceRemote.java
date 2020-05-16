package com.telecominfraproject.wlan.status;

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

import com.telecominfraproject.wlan.status.models.Status;
import com.telecominfraproject.wlan.status.models.StatusDataType;

/**
 * @author dtoptygin
 *
 */
@Component
public class StatusServiceRemote extends BaseRemoteClient implements StatusServiceInterface {

    private static final Logger LOG = LoggerFactory.getLogger(StatusServiceRemote.class);
    
    private static final ParameterizedTypeReference<List<Status>> Status_LIST_CLASS_TOKEN = new ParameterizedTypeReference<List<Status>>() {};

    private static final ParameterizedTypeReference<PaginationResponse<Status>> Status_PAGINATION_RESPONSE_CLASS_TOKEN = new ParameterizedTypeReference<PaginationResponse<Status>>() {};


    private String baseUrl;
            
    @Override
    public Status getOrNull(int customerId, long equipmentId, StatusDataType statusDataType) {
        
        LOG.debug("calling status.getOrNull {} {} {} ", customerId, equipmentId, statusDataType);

        ResponseEntity<Status> responseEntity = restTemplate.getForEntity(
                getBaseUrl()
                +"/orNull?customerId={customerId}&equipmentId={equipmentId}&statusDataType={statusDataType}",
                Status.class, customerId, equipmentId, statusDataType);
        
        Status ret = responseEntity.getBody();
        
        LOG.debug("completed status.getOrNull {} ", ret);
        
        return ret;
    }

    @Override
    public List<Status> get(int customerId, long equipmentId) {
		
        LOG.debug("get({}, {})", customerId, equipmentId);

        
        try {
            ResponseEntity<List<Status>> responseEntity = restTemplate.exchange(
                    getBaseUrl() + "/forEquipment?customerId={customerId}&equipmentId={equipmentId}", HttpMethod.GET,
                    null, Status_LIST_CLASS_TOKEN, customerId, equipmentId);

            List<Status> result = responseEntity.getBody();
            if (null == result) {
                result = Collections.emptyList();
            }
            LOG.debug("get({}, {}) returns {} entries", customerId, equipmentId, result.size());
            return result;
        } catch (Exception exp) {
            LOG.error("get({}, {}) exception ", customerId, equipmentId, exp);
            throw exp;
        }

	}

	@Override
	public PaginationResponse<Status> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
			PaginationContext<Status> context) {
		
        LOG.debug("calling getForCustomer( {}, {}, {} )", customerId, sortBy, context);

        ResponseEntity<PaginationResponse<Status>> responseEntity = restTemplate.exchange(
                getBaseUrl()
                        + "/forCustomer?customerId={customerId}&sortBy={sortBy}&paginationContext={paginationContext}",
                HttpMethod.GET, null, Status_PAGINATION_RESPONSE_CLASS_TOKEN, 
                customerId, sortBy, context);

        PaginationResponse<Status> ret = responseEntity.getBody();
        LOG.debug("completed getForCustomer {} ", ret.getItems().size());

        return ret;
	}
	
	@Override
	public PaginationResponse<Status> getForCustomer(int customerId, Set<Long> equipmentIds,
			Set<StatusDataType> statusDataTypes, List<ColumnAndSort> sortBy, PaginationContext<Status> context) {

        LOG.debug("calling getForCustomer( {}, {}, {}, {}, {} )", customerId, equipmentIds, statusDataTypes, sortBy, context);

        String equipmentIdsStr = null;
        if (equipmentIds != null && !equipmentIds.isEmpty()) {
            equipmentIdsStr = equipmentIds.toString();
            // remove [] around the string, otherwise will get:
            // Failed to convert value of type 'java.lang.String' to required
            // type 'java.util.Set'; nested exception is
            // java.lang.NumberFormatException: For input string: "[690]"
            equipmentIdsStr = equipmentIdsStr.substring(1, equipmentIdsStr.length() - 1);
        }

        String statusDataTypesStr = null;
        if (statusDataTypes != null && !statusDataTypes.isEmpty()) {
        	statusDataTypesStr = statusDataTypes.toString();
            // remove [] around the string, otherwise will get:
            // Failed to convert value of type 'java.lang.String' to required
            // type 'java.util.Set'; nested exception is
            // java.lang.NumberFormatException: For input string: "[690]"
        	statusDataTypesStr = statusDataTypesStr.substring(1, statusDataTypesStr.length() - 1);
        }

        ResponseEntity<PaginationResponse<Status>> responseEntity = restTemplate.exchange(
                getBaseUrl()
                        + "/forCustomerWithFilter?customerId={customerId}&equipmentIds={equipmentIdsStr}&statusDataTypes={statusDataTypesStr}"
                        + "&sortBy={sortBy}&paginationContext={paginationContext}",
                HttpMethod.GET, null, Status_PAGINATION_RESPONSE_CLASS_TOKEN, 
                customerId, equipmentIdsStr, statusDataTypesStr, sortBy, context);

        PaginationResponse<Status> ret = responseEntity.getBody();
        LOG.debug("completed getForCustomer( {}, {}, {}, {}, {} ) {} ", customerId, equipmentIds, statusDataTypes, sortBy, context, ret.getItems().size());

        return ret;

	}
	
    @Override
    public Status update(Status status) {
        
        LOG.debug("calling status.update {} ", status);

        if (BaseJsonModel.hasUnsupportedValue(status)) {
            LOG.error("Failed to update Status, unsupported value in  {}", status);
            throw new DsDataValidationException("Status contains unsupported value");
        }
        
        HttpEntity<String> request = new HttpEntity<String>( status.toString(), headers );

        ResponseEntity<Status> responseEntity = restTemplate.exchange(
                getBaseUrl(),
                HttpMethod.PUT, request, Status.class);
        
        Status ret = responseEntity.getBody();
        
        LOG.debug("completed status.update {} ", ret);
        
        return ret;
    }

    @Override
    public List<Status> update(List<Status> statusList) {
        LOG.debug("calling status.update {} ", statusList);

        if (BaseJsonModel.hasUnsupportedValue(statusList)) {
            LOG.error("Failed to update Status, unsupported value in  {}", statusList);
            throw new DsDataValidationException("Status contains unsupported value");
        }
        
        HttpEntity<String> request = new HttpEntity<String>( statusList.toString(), headers );

        ResponseEntity<List<Status>> responseEntity = restTemplate.exchange(
                getBaseUrl() + "/bulk",
                HttpMethod.PUT, request, Status_LIST_CLASS_TOKEN);
        
        List<Status> ret = responseEntity.getBody();
        
        LOG.debug("completed status.update {} ", ret);
        
        return ret;
    }
    
    @Override
    public List<Status> delete(int customerId, long equipmentId) {
		
        LOG.debug("delete({}, {})", customerId, equipmentId);

        
        try {
            ResponseEntity<List<Status>> responseEntity = restTemplate.exchange(
                    getBaseUrl() + "?customerId={customerId}&equipmentId={equipmentId}", HttpMethod.DELETE,
                    null, Status_LIST_CLASS_TOKEN, customerId, equipmentId);

            List<Status> result = responseEntity.getBody();
            if (null == result) {
                result = Collections.emptyList();
            }
            LOG.debug("delete({}, {}) returns {} entries", customerId, equipmentId, result.size());
            return result;
        } catch (Exception exp) {
            LOG.error("delete({}, {}) exception ", customerId, equipmentId, exp);
            throw exp;
        }

	}
    
    public String getBaseUrl() {
        if(baseUrl==null) {
            baseUrl = environment.getProperty("tip.wlan.statusServiceBaseUrl").trim()+"/api/status";
        }

    	return baseUrl;
    }


}
