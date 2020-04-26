package com.telecominfraproject.wlan.firmware;

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

import com.telecominfraproject.wlan.firmware.models.Firmware;

/**
 * @author dtoptygin
 *
 */
@Component
public class FirmwareServiceRemote extends BaseRemoteClient implements FirmwareServiceInterface {

    private static final Logger LOG = LoggerFactory.getLogger(FirmwareServiceRemote.class);
    
    private static final ParameterizedTypeReference<List<Firmware>> Firmware_LIST_CLASS_TOKEN = new ParameterizedTypeReference<List<Firmware>>() {};

    private static final ParameterizedTypeReference<PaginationResponse<Firmware>> Firmware_PAGINATION_RESPONSE_CLASS_TOKEN = new ParameterizedTypeReference<PaginationResponse<Firmware>>() {};


    private String baseUrl;
            
    @Override
    public Firmware create(Firmware firmware) {
        
        LOG.debug("calling firmware.create {} ", firmware);

        if (BaseJsonModel.hasUnsupportedValue(firmware)) {
            LOG.error("Failed to create Firmware, unsupported value in {}", firmware);
            throw new DsDataValidationException("Firmware contains unsupported value");
        }

        HttpEntity<String> request = new HttpEntity<String>( firmware.toString(), headers );

        ResponseEntity<Firmware> responseEntity = restTemplate.postForEntity(
                getBaseUrl(),
                request, Firmware.class);
        
        Firmware ret = responseEntity.getBody();
        
        LOG.debug("completed firmware.create {} ", ret);
        
        return ret;
    }

    @Override
    public Firmware get(long firmwareId) {
        
        LOG.debug("calling firmware.get {} ", firmwareId);

        ResponseEntity<Firmware> responseEntity = restTemplate.getForEntity(
                getBaseUrl()
                +"?firmwareId={firmwareId}",
                Firmware.class, firmwareId);
        
        Firmware ret = responseEntity.getBody();
        
        LOG.debug("completed firmware.get {} ", ret);
        
        return ret;
    }

    @Override
    public Firmware getOrNull(long firmwareId) {
        
        LOG.debug("calling firmware.getOrNull {} ", firmwareId);

        ResponseEntity<Firmware> responseEntity = restTemplate.getForEntity(
                getBaseUrl()
                +"/orNull?firmwareId={firmwareId}",
                Firmware.class, firmwareId);
        
        Firmware ret = responseEntity.getBody();
        
        LOG.debug("completed firmware.getOrNull {} ", ret);
        
        return ret;
    }

	@Override
	public List<Firmware> get(Set<Long> firmwareIdSet) {
		
        LOG.debug("get({})", firmwareIdSet);

        if (firmwareIdSet == null || firmwareIdSet.isEmpty()) {
            return Collections.emptyList();
        }

        String setString = firmwareIdSet.toString().substring(1, firmwareIdSet.toString().length() - 1);
        
        try {
            ResponseEntity<List<Firmware>> responseEntity = restTemplate.exchange(
                    getBaseUrl() + "/inSet?firmwareIdSet={firmwareIdSet}", HttpMethod.GET,
                    null, Firmware_LIST_CLASS_TOKEN, setString);

            List<Firmware> result = responseEntity.getBody();
            if (null == result) {
                result = Collections.emptyList();
            }
            LOG.debug("get({}) return {} entries", firmwareIdSet, result.size());
            return result;
        } catch (Exception exp) {
            LOG.error("getAllInSet({}) exception ", firmwareIdSet, exp);
            throw exp;
        }

	}

	@Override
	public PaginationResponse<Firmware> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
			PaginationContext<Firmware> context) {
		
        LOG.debug("calling getForCustomer( {}, {}, {} )", customerId, sortBy, context);

        ResponseEntity<PaginationResponse<Firmware>> responseEntity = restTemplate.exchange(
                getBaseUrl()
                        + "/forCustomer?customerId={customerId}&sortBy={sortBy}&paginationContext={paginationContext}",
                HttpMethod.GET, null, Firmware_PAGINATION_RESPONSE_CLASS_TOKEN, customerId, sortBy, context);

        PaginationResponse<Firmware> ret = responseEntity.getBody();
        LOG.debug("completed getForCustomer {} ", ret.getItems().size());

        return ret;
	}
	
    @Override
    public Firmware update(Firmware firmware) {
        
        LOG.debug("calling firmware.update {} ", firmware);

        if (BaseJsonModel.hasUnsupportedValue(firmware)) {
            LOG.error("Failed to update Firmware, unsupported value in  {}", firmware);
            throw new DsDataValidationException("Firmware contains unsupported value");
        }
        
        HttpEntity<String> request = new HttpEntity<String>( firmware.toString(), headers );

        ResponseEntity<Firmware> responseEntity = restTemplate.exchange(
                getBaseUrl(),
                HttpMethod.PUT, request, Firmware.class);
        
        Firmware ret = responseEntity.getBody();
        
        LOG.debug("completed firmware.update {} ", ret);
        
        return ret;
    }

    @Override
    public Firmware delete(long firmwareId) {
        
        LOG.debug("calling firmware.delete {} ", firmwareId);

        ResponseEntity<Firmware> responseEntity =  restTemplate.exchange(
                getBaseUrl()
                +"?firmwareId={firmwareId}", HttpMethod.DELETE,
                null, Firmware.class, firmwareId);
        
        Firmware ret = responseEntity.getBody();
        LOG.debug("completed firmware.delete {} ", ret);
        
        return ret;
    }    

    public String getBaseUrl() {
        if(baseUrl==null) {
            baseUrl = environment.getProperty("tip.wlan.firmwareServiceBaseUrl").trim()+"/api/firmware";
        }

    	return baseUrl;
    }


}
