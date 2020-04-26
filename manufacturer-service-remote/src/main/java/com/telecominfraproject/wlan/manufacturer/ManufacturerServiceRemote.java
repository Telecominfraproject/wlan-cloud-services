package com.telecominfraproject.wlan.manufacturer;

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

import com.telecominfraproject.wlan.manufacturer.models.Manufacturer;

/**
 * @author dtoptygin
 *
 */
@Component
public class ManufacturerServiceRemote extends BaseRemoteClient implements ManufacturerServiceInterface {

    private static final Logger LOG = LoggerFactory.getLogger(ManufacturerServiceRemote.class);
    
    private static final ParameterizedTypeReference<List<Manufacturer>> Manufacturer_LIST_CLASS_TOKEN = new ParameterizedTypeReference<List<Manufacturer>>() {};

    private static final ParameterizedTypeReference<PaginationResponse<Manufacturer>> Manufacturer_PAGINATION_RESPONSE_CLASS_TOKEN = new ParameterizedTypeReference<PaginationResponse<Manufacturer>>() {};


    private String baseUrl;
            
    @Override
    public Manufacturer create(Manufacturer manufacturer) {
        
        LOG.debug("calling manufacturer.create {} ", manufacturer);

        if (BaseJsonModel.hasUnsupportedValue(manufacturer)) {
            LOG.error("Failed to create Manufacturer, unsupported value in {}", manufacturer);
            throw new DsDataValidationException("Manufacturer contains unsupported value");
        }

        HttpEntity<String> request = new HttpEntity<String>( manufacturer.toString(), headers );

        ResponseEntity<Manufacturer> responseEntity = restTemplate.postForEntity(
                getBaseUrl(),
                request, Manufacturer.class);
        
        Manufacturer ret = responseEntity.getBody();
        
        LOG.debug("completed manufacturer.create {} ", ret);
        
        return ret;
    }

    @Override
    public Manufacturer get(long manufacturerId) {
        
        LOG.debug("calling manufacturer.get {} ", manufacturerId);

        ResponseEntity<Manufacturer> responseEntity = restTemplate.getForEntity(
                getBaseUrl()
                +"?manufacturerId={manufacturerId}",
                Manufacturer.class, manufacturerId);
        
        Manufacturer ret = responseEntity.getBody();
        
        LOG.debug("completed manufacturer.get {} ", ret);
        
        return ret;
    }

    @Override
    public Manufacturer getOrNull(long manufacturerId) {
        
        LOG.debug("calling manufacturer.getOrNull {} ", manufacturerId);

        ResponseEntity<Manufacturer> responseEntity = restTemplate.getForEntity(
                getBaseUrl()
                +"/orNull?manufacturerId={manufacturerId}",
                Manufacturer.class, manufacturerId);
        
        Manufacturer ret = responseEntity.getBody();
        
        LOG.debug("completed manufacturer.getOrNull {} ", ret);
        
        return ret;
    }

	@Override
	public List<Manufacturer> get(Set<Long> manufacturerIdSet) {
		
        LOG.debug("get({})", manufacturerIdSet);

        if (manufacturerIdSet == null || manufacturerIdSet.isEmpty()) {
            return Collections.emptyList();
        }

        String setString = manufacturerIdSet.toString().substring(1, manufacturerIdSet.toString().length() - 1);
        
        try {
            ResponseEntity<List<Manufacturer>> responseEntity = restTemplate.exchange(
                    getBaseUrl() + "/inSet?manufacturerIdSet={manufacturerIdSet}", HttpMethod.GET,
                    null, Manufacturer_LIST_CLASS_TOKEN, setString);

            List<Manufacturer> result = responseEntity.getBody();
            if (null == result) {
                result = Collections.emptyList();
            }
            LOG.debug("get({}) return {} entries", manufacturerIdSet, result.size());
            return result;
        } catch (Exception exp) {
            LOG.error("getAllInSet({}) exception ", manufacturerIdSet, exp);
            throw exp;
        }

	}

	@Override
	public PaginationResponse<Manufacturer> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
			PaginationContext<Manufacturer> context) {
		
        LOG.debug("calling getForCustomer( {}, {}, {} )", customerId, sortBy, context);

        ResponseEntity<PaginationResponse<Manufacturer>> responseEntity = restTemplate.exchange(
                getBaseUrl()
                        + "/forCustomer?customerId={customerId}&sortBy={sortBy}&paginationContext={paginationContext}",
                HttpMethod.GET, null, Manufacturer_PAGINATION_RESPONSE_CLASS_TOKEN, customerId, sortBy, context);

        PaginationResponse<Manufacturer> ret = responseEntity.getBody();
        LOG.debug("completed getForCustomer {} ", ret.getItems().size());

        return ret;
	}
	
    @Override
    public Manufacturer update(Manufacturer manufacturer) {
        
        LOG.debug("calling manufacturer.update {} ", manufacturer);

        if (BaseJsonModel.hasUnsupportedValue(manufacturer)) {
            LOG.error("Failed to update Manufacturer, unsupported value in  {}", manufacturer);
            throw new DsDataValidationException("Manufacturer contains unsupported value");
        }
        
        HttpEntity<String> request = new HttpEntity<String>( manufacturer.toString(), headers );

        ResponseEntity<Manufacturer> responseEntity = restTemplate.exchange(
                getBaseUrl(),
                HttpMethod.PUT, request, Manufacturer.class);
        
        Manufacturer ret = responseEntity.getBody();
        
        LOG.debug("completed manufacturer.update {} ", ret);
        
        return ret;
    }

    @Override
    public Manufacturer delete(long manufacturerId) {
        
        LOG.debug("calling manufacturer.delete {} ", manufacturerId);

        ResponseEntity<Manufacturer> responseEntity =  restTemplate.exchange(
                getBaseUrl()
                +"?manufacturerId={manufacturerId}", HttpMethod.DELETE,
                null, Manufacturer.class, manufacturerId);
        
        Manufacturer ret = responseEntity.getBody();
        LOG.debug("completed manufacturer.delete {} ", ret);
        
        return ret;
    }    

    public String getBaseUrl() {
        if(baseUrl==null) {
            baseUrl = environment.getProperty("tip.wlan.manufacturerServiceBaseUrl").trim()+"/api/manufacturer";
        }

    	return baseUrl;
    }


}
