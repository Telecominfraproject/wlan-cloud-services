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
    public Status create(Status status) {
        
        LOG.debug("calling status.create {} ", status);

        if (BaseJsonModel.hasUnsupportedValue(status)) {
            LOG.error("Failed to create Status, unsupported value in {}", status);
            throw new DsDataValidationException("Status contains unsupported value");
        }

        HttpEntity<String> request = new HttpEntity<String>( status.toString(), headers );

        ResponseEntity<Status> responseEntity = restTemplate.postForEntity(
                getBaseUrl(),
                request, Status.class);
        
        Status ret = responseEntity.getBody();
        
        LOG.debug("completed status.create {} ", ret);
        
        return ret;
    }

    @Override
    public Status get(long statusId) {
        
        LOG.debug("calling status.get {} ", statusId);

        ResponseEntity<Status> responseEntity = restTemplate.getForEntity(
                getBaseUrl()
                +"?statusId={statusId}",
                Status.class, statusId);
        
        Status ret = responseEntity.getBody();
        
        LOG.debug("completed status.get {} ", ret);
        
        return ret;
    }

    @Override
    public Status getOrNull(long statusId) {
        
        LOG.debug("calling status.getOrNull {} ", statusId);

        ResponseEntity<Status> responseEntity = restTemplate.getForEntity(
                getBaseUrl()
                +"/orNull?statusId={statusId}",
                Status.class, statusId);
        
        Status ret = responseEntity.getBody();
        
        LOG.debug("completed status.getOrNull {} ", ret);
        
        return ret;
    }

	@Override
	public List<Status> get(Set<Long> statusIdSet) {
		
        LOG.debug("get({})", statusIdSet);

        if (statusIdSet == null || statusIdSet.isEmpty()) {
            return Collections.emptyList();
        }

        String setString = statusIdSet.toString().substring(1, statusIdSet.toString().length() - 1);
        
        try {
            ResponseEntity<List<Status>> responseEntity = restTemplate.exchange(
                    getBaseUrl() + "/inSet?statusIdSet={statusIdSet}", HttpMethod.GET,
                    null, Status_LIST_CLASS_TOKEN, setString);

            List<Status> result = responseEntity.getBody();
            if (null == result) {
                result = Collections.emptyList();
            }
            LOG.debug("get({}) return {} entries", statusIdSet, result.size());
            return result;
        } catch (Exception exp) {
            LOG.error("getAllInSet({}) exception ", statusIdSet, exp);
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
                HttpMethod.GET, null, Status_PAGINATION_RESPONSE_CLASS_TOKEN, customerId, sortBy, context);

        PaginationResponse<Status> ret = responseEntity.getBody();
        LOG.debug("completed getForCustomer {} ", ret.getItems().size());

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
    public Status delete(long statusId) {
        
        LOG.debug("calling status.delete {} ", statusId);

        ResponseEntity<Status> responseEntity =  restTemplate.exchange(
                getBaseUrl()
                +"?statusId={statusId}", HttpMethod.DELETE,
                null, Status.class, statusId);
        
        Status ret = responseEntity.getBody();
        LOG.debug("completed status.delete {} ", ret);
        
        return ret;
    }    

    public String getBaseUrl() {
        if(baseUrl==null) {
            baseUrl = environment.getProperty("tip.wlan.statusServiceBaseUrl").trim()+"/api/status";
        }

    	return baseUrl;
    }


}
