package com.telecominfraproject.wlan.profile;

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

import com.telecominfraproject.wlan.profile.models.Profile;

/**
 * @author dtoptygin
 *
 */
@Component
public class ProfileServiceRemote extends BaseRemoteClient implements ProfileServiceInterface {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileServiceRemote.class);
    
    private static final ParameterizedTypeReference<List<Profile>> Profile_LIST_CLASS_TOKEN = new ParameterizedTypeReference<List<Profile>>() {};

    private static final ParameterizedTypeReference<PaginationResponse<Profile>> Profile_PAGINATION_RESPONSE_CLASS_TOKEN = new ParameterizedTypeReference<PaginationResponse<Profile>>() {};


    private String baseUrl;
            
    @Override
    public Profile create(Profile profile) {
        
        LOG.debug("calling profile.create {} ", profile);

        if (BaseJsonModel.hasUnsupportedValue(profile)) {
            LOG.error("Failed to create Profile, unsupported value in {}", profile);
            throw new DsDataValidationException("Profile contains unsupported value");
        }

        HttpEntity<String> request = new HttpEntity<String>( profile.toString(), headers );

        ResponseEntity<Profile> responseEntity = restTemplate.postForEntity(
                getBaseUrl(),
                request, Profile.class);
        
        Profile ret = responseEntity.getBody();
        
        LOG.debug("completed profile.create {} ", ret);
        
        return ret;
    }

    @Override
    public Profile get(long profileId) {
        
        LOG.debug("calling profile.get {} ", profileId);

        ResponseEntity<Profile> responseEntity = restTemplate.getForEntity(
                getBaseUrl()
                +"?profileId={profileId}",
                Profile.class, profileId);
        
        Profile ret = responseEntity.getBody();
        
        LOG.debug("completed profile.get {} ", ret);
        
        return ret;
    }

    @Override
    public Profile getOrNull(long profileId) {
        
        LOG.debug("calling profile.getOrNull {} ", profileId);

        ResponseEntity<Profile> responseEntity = restTemplate.getForEntity(
                getBaseUrl()
                +"/orNull?profileId={profileId}",
                Profile.class, profileId);
        
        Profile ret = responseEntity.getBody();
        
        LOG.debug("completed profile.getOrNull {} ", ret);
        
        return ret;
    }

	@Override
	public List<Profile> get(Set<Long> profileIdSet) {
		
        LOG.debug("get({})", profileIdSet);

        if (profileIdSet == null || profileIdSet.isEmpty()) {
            return Collections.emptyList();
        }

        String setString = profileIdSet.toString().substring(1, profileIdSet.toString().length() - 1);
        
        try {
            ResponseEntity<List<Profile>> responseEntity = restTemplate.exchange(
                    getBaseUrl() + "/inSet?profileIdSet={profileIdSet}", HttpMethod.GET,
                    null, Profile_LIST_CLASS_TOKEN, setString);

            List<Profile> result = responseEntity.getBody();
            if (null == result) {
                result = Collections.emptyList();
            }
            LOG.debug("get({}) return {} entries", profileIdSet, result.size());
            return result;
        } catch (Exception exp) {
            LOG.error("getAllInSet({}) exception ", profileIdSet, exp);
            throw exp;
        }

	}

	@Override
	public PaginationResponse<Profile> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
			PaginationContext<Profile> context) {
		
        LOG.debug("calling getForCustomer( {}, {}, {} )", customerId, sortBy, context);

        ResponseEntity<PaginationResponse<Profile>> responseEntity = restTemplate.exchange(
                getBaseUrl()
                        + "/forCustomer?customerId={customerId}&sortBy={sortBy}&paginationContext={paginationContext}",
                HttpMethod.GET, null, Profile_PAGINATION_RESPONSE_CLASS_TOKEN, customerId, sortBy, context);

        PaginationResponse<Profile> ret = responseEntity.getBody();
        LOG.debug("completed getForCustomer {} ", ret.getItems().size());

        return ret;
	}
	
    @Override
    public Profile update(Profile profile) {
        
        LOG.debug("calling profile.update {} ", profile);

        if (BaseJsonModel.hasUnsupportedValue(profile)) {
            LOG.error("Failed to update Profile, unsupported value in  {}", profile);
            throw new DsDataValidationException("Profile contains unsupported value");
        }
        
        HttpEntity<String> request = new HttpEntity<String>( profile.toString(), headers );

        ResponseEntity<Profile> responseEntity = restTemplate.exchange(
                getBaseUrl(),
                HttpMethod.PUT, request, Profile.class);
        
        Profile ret = responseEntity.getBody();
        
        LOG.debug("completed profile.update {} ", ret);
        
        return ret;
    }

    @Override
    public Profile delete(long profileId) {
        
        LOG.debug("calling profile.delete {} ", profileId);

        ResponseEntity<Profile> responseEntity =  restTemplate.exchange(
                getBaseUrl()
                +"?profileId={profileId}", HttpMethod.DELETE,
                null, Profile.class, profileId);
        
        Profile ret = responseEntity.getBody();
        LOG.debug("completed profile.delete {} ", ret);
        
        return ret;
    }    

    public String getBaseUrl() {
        if(baseUrl==null) {
            baseUrl = environment.getProperty("tip.wlan.profileServiceBaseUrl").trim()+"/api/profile";
        }

    	return baseUrl;
    }


}
