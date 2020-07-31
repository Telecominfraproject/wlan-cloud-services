package com.telecominfraproject.wlan.adoptionmetrics;

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
import com.telecominfraproject.wlan.core.model.json.GenericResponse;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;

import com.telecominfraproject.wlan.adoptionmetrics.models.ServiceAdoptionMetrics;

/**
 * @author dtoptygin
 *
 */
@Component
public class AdoptionMetricsServiceRemote extends BaseRemoteClient implements AdoptionMetricsServiceInterface {

    private static final Logger LOG = LoggerFactory.getLogger(AdoptionMetricsServiceRemote.class);
    
    private static final ParameterizedTypeReference<List<ServiceAdoptionMetrics>> ServiceAdoptionMetrics_LIST_CLASS_TOKEN = new ParameterizedTypeReference<List<ServiceAdoptionMetrics>>() {};

    private static final ParameterizedTypeReference<PaginationResponse<ServiceAdoptionMetrics>> ServiceAdoptionMetrics_PAGINATION_RESPONSE_CLASS_TOKEN = new ParameterizedTypeReference<PaginationResponse<ServiceAdoptionMetrics>>() {};


    private String baseUrl;
            
    @Override
    public GenericResponse update(List<ServiceAdoptionMetrics> serviceAdoptionMetricsList) {
        
        LOG.debug("calling adoptionMetrics.update {} ", serviceAdoptionMetricsList);

        HttpEntity<String> request = new HttpEntity<String>( serviceAdoptionMetricsList.toString(), headers );

        ResponseEntity<GenericResponse> responseEntity = restTemplate.postForEntity(
                getBaseUrl(),
                request, GenericResponse.class);
        
        GenericResponse ret = responseEntity.getBody();
        
        LOG.debug("completed adoptionMetrics.update {} ", ret);
        
        return ret;
    }

    @Override
    public List<ServiceAdoptionMetrics> get(int year, Set<Long> equipmentIds) {
		
        LOG.debug("get({} {})", year, equipmentIds);

        if (equipmentIds == null || equipmentIds.isEmpty()) {
            throw new IllegalArgumentException("equipmentIds must be provided");
        }

        String setString = equipmentIds.toString();
        setString = setString.substring(1, setString.length() - 1);
        
        try {
            ResponseEntity<List<ServiceAdoptionMetrics>> responseEntity = restTemplate.exchange(
                    getBaseUrl() + "/perEquipmentPerDay?year={year}&equipmentIds={equipmentIds}", HttpMethod.GET,
                    null, ServiceAdoptionMetrics_LIST_CLASS_TOKEN, year, setString);

            List<ServiceAdoptionMetrics> result = responseEntity.getBody();
            if (null == result) {
                result = Collections.emptyList();
            }
            LOG.debug("get({} {}) return {} entries", year, equipmentIds, result.size());
            return result;
        } catch (Exception exp) {
            LOG.error("get({} {}) exception ", year, equipmentIds, exp);
            throw exp;
        }

	}

    @Override
    public List<ServiceAdoptionMetrics> getAggregatePerLocationPerDay(int year, Set<Long> locationIds) {
        LOG.debug("getAggregatePerLocationPerDay({} {})", year, locationIds);

        if (locationIds == null || locationIds.isEmpty()) {
            throw new IllegalArgumentException("locationIds must be provided");
        }

        String setString = locationIds.toString();
        setString = setString.substring(1, setString.length() - 1);
        
        try {
            ResponseEntity<List<ServiceAdoptionMetrics>> responseEntity = restTemplate.exchange(
                    getBaseUrl() + "/perLocationPerDay?year={year}&locationIds={locationIds}", HttpMethod.GET,
                    null, ServiceAdoptionMetrics_LIST_CLASS_TOKEN, year, setString);

            List<ServiceAdoptionMetrics> result = responseEntity.getBody();
            if (null == result) {
                result = Collections.emptyList();
            }
            LOG.debug("getAggregatePerLocationPerDay({} {}) return {} entries", year, locationIds, result.size());
            return result;
        } catch (Exception exp) {
            LOG.error("getAggregatePerLocationPerDay({} {}) exception ", year, locationIds, exp);
            throw exp;
        }
    }
    
    @Override
    public List<ServiceAdoptionMetrics> getAggregatePerCustomerPerDay(int year, Set<Integer> customerIds) {
        LOG.debug("getAggregatePerCustomerPerDay({} {})", year, customerIds);

        if (customerIds == null || customerIds.isEmpty()) {
            throw new IllegalArgumentException("customerIds must be provided");
        }

        String setString = customerIds.toString();
        setString = setString.substring(1, setString.length() - 1);
        
        try {
            ResponseEntity<List<ServiceAdoptionMetrics>> responseEntity = restTemplate.exchange(
                    getBaseUrl() + "/perCustomerPerDay?year={year}&customerIds={customerIds}", HttpMethod.GET,
                    null, ServiceAdoptionMetrics_LIST_CLASS_TOKEN, year, setString);

            List<ServiceAdoptionMetrics> result = responseEntity.getBody();
            if (null == result) {
                result = Collections.emptyList();
            }
            LOG.debug("getAggregatePerCustomerPerDay({} {}) return {} entries", year, customerIds, result.size());
            return result;
        } catch (Exception exp) {
            LOG.error("getAggregatePerCustomerPerDay({} {}) exception ", year, customerIds, exp);
            throw exp;
        }
    }

    @Override
    public List<ServiceAdoptionMetrics> getAllPerDay(int year) {
        LOG.debug("getAllPerDay({})", year);
        
        try {
            ResponseEntity<List<ServiceAdoptionMetrics>> responseEntity = restTemplate.exchange(
                    getBaseUrl() + "/allPerDay?year={year}", HttpMethod.GET,
                    null, ServiceAdoptionMetrics_LIST_CLASS_TOKEN, year);

            List<ServiceAdoptionMetrics> result = responseEntity.getBody();
            if (null == result) {
                result = Collections.emptyList();
            }
            LOG.debug("getAllPerDay({}) return {} entries", year, result.size());
            return result;
        } catch (Exception exp) {
            LOG.error("getAllPerDay({}) exception ", year, exp);
            throw exp;
        }
    }
    
    @Override
    public List<ServiceAdoptionMetrics> getAllPerWeek(int year) {
        LOG.debug("getAllPerWeek({})", year);
        
        try {
            ResponseEntity<List<ServiceAdoptionMetrics>> responseEntity = restTemplate.exchange(
                    getBaseUrl() + "/allPerWeek?year={year}", HttpMethod.GET,
                    null, ServiceAdoptionMetrics_LIST_CLASS_TOKEN, year);

            List<ServiceAdoptionMetrics> result = responseEntity.getBody();
            if (null == result) {
                result = Collections.emptyList();
            }
            LOG.debug("getAllPerWeek({}) return {} entries", year, result.size());
            return result;
        } catch (Exception exp) {
            LOG.error("getAllPerWeek({}) exception ", year, exp);
            throw exp;
        }
    }
    
    @Override
    public List<ServiceAdoptionMetrics> getAllPerMonth(int year) {
        LOG.debug("getAllPerMonth({})", year);
        
        try {
            ResponseEntity<List<ServiceAdoptionMetrics>> responseEntity = restTemplate.exchange(
                    getBaseUrl() + "/allPerMonth?year={year}", HttpMethod.GET,
                    null, ServiceAdoptionMetrics_LIST_CLASS_TOKEN, year);

            List<ServiceAdoptionMetrics> result = responseEntity.getBody();
            if (null == result) {
                result = Collections.emptyList();
            }
            LOG.debug("getAllPerMonth({}) return {} entries", year, result.size());
            return result;
        } catch (Exception exp) {
            LOG.error("getAllPerMonth({}) exception ", year, exp);
            throw exp;
        }
    }
    
    @Override
    public GenericResponse updateUniqueMacs(long timestampMs, int customerId, long locationId, long equipmentId, Set<Long> clientMacSet) {
        LOG.debug("calling updateUniqueMacs {} {} {} {} {} ", timestampMs, customerId, locationId, equipmentId, clientMacSet);

        String setString = clientMacSet.toString();
        setString = setString.substring(1, setString.length() - 1);

        ResponseEntity<GenericResponse> responseEntity = restTemplate.exchange(
                getBaseUrl() + "/uniqueMacs?timestampMs={timestampMs}&customerId={customerId}&locationId={locationId}&equipmentId={equipmentId}&clientMacSet={clientMacSet}", 
                HttpMethod.POST,
                null, GenericResponse.class, 
                timestampMs, customerId, locationId, equipmentId, setString);

        
        GenericResponse ret = responseEntity.getBody();
        
        LOG.debug("completed updateUniqueMacs {} ", ret);
        
        return ret;
    }
    
    @Override
    public GenericResponse finalizeUniqueMacsCount(int year, int dayOfYear) {
        LOG.debug("calling finalizeUniqueMacsCount {} {} ", year, dayOfYear);

        ResponseEntity<GenericResponse> responseEntity = restTemplate.exchange(
                getBaseUrl() + "/uniqueMacs/finalize?year={year}&dayOfYear={dayOfYear}", 
                HttpMethod.POST,
                null, GenericResponse.class, 
                year, dayOfYear);

        
        GenericResponse ret = responseEntity.getBody();
        
        LOG.debug("completed finalizeUniqueMacsCount {} ", ret);
        
        return ret;
    }
    
    @Override
    public long getUniqueMacsCount(int year, int dayOfYear, int customerId, long locationId, long equipmentId) {
        LOG.debug("calling getUniqueMacs {} {} {} {} {} ", year, dayOfYear, customerId, locationId, equipmentId);

        ResponseEntity<Long> responseEntity = restTemplate.exchange(
                getBaseUrl() + "/uniqueMacsCount?year={year}&dayOfYear={dayOfYear}&customerId={customerId}&locationId={locationId}&equipmentId={equipmentId}", 
                HttpMethod.GET,
                null, Long.class, 
                year, dayOfYear, customerId, locationId, equipmentId);
        
        Long ret = responseEntity.getBody();
        
        LOG.debug("completed updateUniqueMacs {} ", ret);
        
        return ret;
    }
    
    @Override
    public GenericResponse deleteUniqueMacs(long createdBeforeTimestampMs, int customerId, long locationId, long equipmentId) {
        LOG.debug("calling deleteUniqueMacs {} {} {} {} ", createdBeforeTimestampMs, customerId, locationId, equipmentId);

        ResponseEntity<GenericResponse> responseEntity = restTemplate.exchange(
                getBaseUrl() + "/uniqueMacs?createdBeforeTimestampMs={createdBeforeTimestampMs}&customerId={customerId}&locationId={locationId}&equipmentId={equipmentId}", 
                HttpMethod.DELETE,
                null, GenericResponse.class, 
                createdBeforeTimestampMs, customerId, locationId, equipmentId);
        
        GenericResponse ret = responseEntity.getBody();
        
        LOG.debug("completed deleteUniqueMacs {} ", ret);
        
        return ret;
    }

    public String getBaseUrl() {
        if(baseUrl==null) {
            baseUrl = environment.getProperty("tip.wlan.adoptionMetricsServiceBaseUrl").trim()+"/api/adoptionMetrics";
        }

    	return baseUrl;
    }


}
