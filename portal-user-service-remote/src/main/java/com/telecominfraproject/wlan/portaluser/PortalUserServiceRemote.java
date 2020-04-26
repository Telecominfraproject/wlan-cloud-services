package com.telecominfraproject.wlan.portaluser;

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

import com.telecominfraproject.wlan.portaluser.models.PortalUser;

/**
 * @author dtoptygin
 *
 */
@Component
public class PortalUserServiceRemote extends BaseRemoteClient implements PortalUserServiceInterface {

    private static final Logger LOG = LoggerFactory.getLogger(PortalUserServiceRemote.class);
    
    private static final ParameterizedTypeReference<List<PortalUser>> PortalUser_LIST_CLASS_TOKEN = new ParameterizedTypeReference<List<PortalUser>>() {};

    private static final ParameterizedTypeReference<PaginationResponse<PortalUser>> PortalUser_PAGINATION_RESPONSE_CLASS_TOKEN = new ParameterizedTypeReference<PaginationResponse<PortalUser>>() {};


    private String baseUrl;
            
    @Override
    public PortalUser create(PortalUser portalUser) {
        
        LOG.debug("calling portalUser.create {} ", portalUser);

        if (BaseJsonModel.hasUnsupportedValue(portalUser)) {
            LOG.error("Failed to create PortalUser, unsupported value in {}", portalUser);
            throw new DsDataValidationException("PortalUser contains unsupported value");
        }

        HttpEntity<String> request = new HttpEntity<String>( portalUser.toString(), headers );

        ResponseEntity<PortalUser> responseEntity = restTemplate.postForEntity(
                getBaseUrl(),
                request, PortalUser.class);
        
        PortalUser ret = responseEntity.getBody();
        
        LOG.debug("completed portalUser.create {} ", ret);
        
        return ret;
    }

    @Override
    public PortalUser get(long portalUserId) {
        
        LOG.debug("calling portalUser.get {} ", portalUserId);

        ResponseEntity<PortalUser> responseEntity = restTemplate.getForEntity(
                getBaseUrl()
                +"?portalUserId={portalUserId}",
                PortalUser.class, portalUserId);
        
        PortalUser ret = responseEntity.getBody();
        
        LOG.debug("completed portalUser.get {} ", ret);
        
        return ret;
    }

    @Override
    public PortalUser getOrNull(long portalUserId) {
        
        LOG.debug("calling portalUser.getOrNull {} ", portalUserId);

        ResponseEntity<PortalUser> responseEntity = restTemplate.getForEntity(
                getBaseUrl()
                +"/orNull?portalUserId={portalUserId}",
                PortalUser.class, portalUserId);
        
        PortalUser ret = responseEntity.getBody();
        
        LOG.debug("completed portalUser.getOrNull {} ", ret);
        
        return ret;
    }

	@Override
	public List<PortalUser> get(Set<Long> portalUserIdSet) {
		
        LOG.debug("get({})", portalUserIdSet);

        if (portalUserIdSet == null || portalUserIdSet.isEmpty()) {
            return Collections.emptyList();
        }

        String setString = portalUserIdSet.toString().substring(1, portalUserIdSet.toString().length() - 1);
        
        try {
            ResponseEntity<List<PortalUser>> responseEntity = restTemplate.exchange(
                    getBaseUrl() + "/inSet?portalUserIdSet={portalUserIdSet}", HttpMethod.GET,
                    null, PortalUser_LIST_CLASS_TOKEN, setString);

            List<PortalUser> result = responseEntity.getBody();
            if (null == result) {
                result = Collections.emptyList();
            }
            LOG.debug("get({}) return {} entries", portalUserIdSet, result.size());
            return result;
        } catch (Exception exp) {
            LOG.error("getAllInSet({}) exception ", portalUserIdSet, exp);
            throw exp;
        }

	}

	@Override
	public PaginationResponse<PortalUser> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
			PaginationContext<PortalUser> context) {
		
        LOG.debug("calling getForCustomer( {}, {}, {} )", customerId, sortBy, context);

        ResponseEntity<PaginationResponse<PortalUser>> responseEntity = restTemplate.exchange(
                getBaseUrl()
                        + "/forCustomer?customerId={customerId}&sortBy={sortBy}&paginationContext={paginationContext}",
                HttpMethod.GET, null, PortalUser_PAGINATION_RESPONSE_CLASS_TOKEN, customerId, sortBy, context);

        PaginationResponse<PortalUser> ret = responseEntity.getBody();
        LOG.debug("completed getForCustomer {} ", ret.getItems().size());

        return ret;
	}
	
    @Override
    public PortalUser update(PortalUser portalUser) {
        
        LOG.debug("calling portalUser.update {} ", portalUser);

        if (BaseJsonModel.hasUnsupportedValue(portalUser)) {
            LOG.error("Failed to update PortalUser, unsupported value in  {}", portalUser);
            throw new DsDataValidationException("PortalUser contains unsupported value");
        }
        
        HttpEntity<String> request = new HttpEntity<String>( portalUser.toString(), headers );

        ResponseEntity<PortalUser> responseEntity = restTemplate.exchange(
                getBaseUrl(),
                HttpMethod.PUT, request, PortalUser.class);
        
        PortalUser ret = responseEntity.getBody();
        
        LOG.debug("completed portalUser.update {} ", ret);
        
        return ret;
    }

    @Override
    public PortalUser delete(long portalUserId) {
        
        LOG.debug("calling portalUser.delete {} ", portalUserId);

        ResponseEntity<PortalUser> responseEntity =  restTemplate.exchange(
                getBaseUrl()
                +"?portalUserId={portalUserId}", HttpMethod.DELETE,
                null, PortalUser.class, portalUserId);
        
        PortalUser ret = responseEntity.getBody();
        LOG.debug("completed portalUser.delete {} ", ret);
        
        return ret;
    }    

    public String getBaseUrl() {
        if(baseUrl==null) {
            baseUrl = environment.getProperty("tip.wlan.portalUserServiceBaseUrl").trim()+"/api/portalUser";
        }

    	return baseUrl;
    }


}
