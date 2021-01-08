package com.telecominfraproject.wlan.servicemetric;

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
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.json.GenericResponse;
import com.telecominfraproject.wlan.core.model.pagination.ColumnAndSort;
import com.telecominfraproject.wlan.core.model.pagination.PaginationContext;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetric;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDataType;

/**
 * @author dtoptygin
 *
 */
@Component
public class ServiceMetricServiceRemote extends BaseRemoteClient implements ServiceMetricServiceInterface {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceMetricServiceRemote.class);
    
    private static final ParameterizedTypeReference<List<ServiceMetric>> ServiceMetric_LIST_CLASS_TOKEN = new ParameterizedTypeReference<List<ServiceMetric>>() {};

    private static final ParameterizedTypeReference<PaginationResponse<ServiceMetric>> ServiceMetric_PAGINATION_RESPONSE_CLASS_TOKEN = new ParameterizedTypeReference<PaginationResponse<ServiceMetric>>() {};


    private String baseUrl;
            
    @Override
    public GenericResponse create(ServiceMetric serviceMetric) {
        
        LOG.debug("calling serviceMetric.create {} ", serviceMetric);

        if (BaseJsonModel.hasUnsupportedValue(serviceMetric)) {
            LOG.error("Failed to create ServiceMetric, unsupported value in {}", serviceMetric);
            throw new DsDataValidationException("ServiceMetric contains unsupported value");
        }

        HttpEntity<String> request = new HttpEntity<String>( serviceMetric.toString(), headers );

        ResponseEntity<GenericResponse> responseEntity = restTemplate.postForEntity(
                getBaseUrl(),
                request, GenericResponse.class);
        
        GenericResponse ret = responseEntity.getBody();
        
        LOG.debug("completed serviceMetric.create {} ", ret);
        
        return ret;
    }


    @Override
    public GenericResponse create(List<ServiceMetric> serviceMetrics) {
        LOG.debug("calling bulk serviceMetric.create {} ", serviceMetrics.size());

        if (BaseJsonModel.hasUnsupportedValue(serviceMetrics)) {
            LOG.error("Failed to create ServiceMetric, unsupported value in {}", serviceMetrics);
            throw new DsDataValidationException("ServiceMetric contains unsupported value");
        }

        HttpEntity<String> request = new HttpEntity<String>( serviceMetrics.toString(), headers );

        ResponseEntity<GenericResponse> responseEntity = restTemplate.postForEntity(
                getBaseUrl()+"/bulk",
                request, GenericResponse.class);
        
        GenericResponse ret = responseEntity.getBody();
        
        LOG.debug("completed bulk serviceMetric.create {} ", serviceMetrics.size());
        
        return ret;
    }
    
    @Override
    public PaginationResponse<ServiceMetric> getForCustomer(long fromTime, long toTime, int customerId,
            Set<Long> locationIds, Set<Long> equipmentIds, Set<MacAddress> clientMacAdresses, Set<ServiceMetricDataType> dataTypes,
    		List<ColumnAndSort> sortBy, PaginationContext<ServiceMetric> context) {
		
        LOG.debug("calling getForCustomer( {}, {}, {}, {}, {}, {}, {}, {}, {} )", 
        		fromTime, toTime, customerId, locationIds, equipmentIds, 
        		clientMacAdresses, dataTypes, sortBy, context);

        String locationIdsStr = null;
        if (locationIds != null && !locationIds.isEmpty()) {
            locationIdsStr = locationIds.toString();
            // remove [] around the string, otherwise will get:
            // Failed to convert value of type 'java.lang.String' to required
            // type 'java.util.Set'; nested exception is
            // java.lang.NumberFormatException: For input string: "[690]"
            locationIdsStr = locationIdsStr.substring(1, locationIdsStr.length() - 1);
        }

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

        ResponseEntity<PaginationResponse<ServiceMetric>> responseEntity = restTemplate.exchange(
                getBaseUrl()
                        + "/forCustomer?fromTime={fromTime}&toTime={toTime}&customerId={customerId}"
                        + "&locationIds={locationIdsStr}&equipmentIds={equipmentIdsStr}&clientMacAdresses={clientMacAdresses}&dataTypes={dataTypesStr}"
                        + "&sortBy={sortBy}&paginationContext={paginationContext}",
                HttpMethod.GET, null, ServiceMetric_PAGINATION_RESPONSE_CLASS_TOKEN, 
                fromTime, toTime, customerId, locationIdsStr, equipmentIdsStr, clientMacAdresses, dataTypesStr, sortBy, context);

        PaginationResponse<ServiceMetric> ret = responseEntity.getBody();
        LOG.debug("completed getForCustomer {} ", ret.getItems().size());

        return ret;
	}
	
	@Override
	public GenericResponse delete(int customerId, long equipmentId, long createdBeforeTimestamp) {
        
        LOG.debug("calling serviceMetric.delete {} {} {}", customerId, equipmentId, createdBeforeTimestamp);

        ResponseEntity<GenericResponse> responseEntity =  restTemplate.exchange(
                getBaseUrl()
                +"?customerId={customerId}&equipmentId={equipmentId}&createdBeforeTimestamp={createdBeforeTimestamp}", HttpMethod.DELETE,
                null, GenericResponse.class, customerId, equipmentId, createdBeforeTimestamp);
        
        GenericResponse ret = responseEntity.getBody();
        LOG.debug("completed serviceMetric.delete {} {} {}", customerId, equipmentId, createdBeforeTimestamp);
        
        return ret;
    }    

    public String getBaseUrl() {
        if(baseUrl==null) {
            baseUrl = environment.getProperty("tip.wlan.serviceMetricServiceBaseUrl").trim()+"/api/serviceMetric";
        }

    	return baseUrl;
    }


}
