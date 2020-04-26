package com.telecominfraproject.wlan.servicemetric;

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

import com.telecominfraproject.wlan.servicemetric.models.ServiceMetric;

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
    public ServiceMetric create(ServiceMetric serviceMetric) {
        
        LOG.debug("calling serviceMetric.create {} ", serviceMetric);

        if (BaseJsonModel.hasUnsupportedValue(serviceMetric)) {
            LOG.error("Failed to create ServiceMetric, unsupported value in {}", serviceMetric);
            throw new DsDataValidationException("ServiceMetric contains unsupported value");
        }

        HttpEntity<String> request = new HttpEntity<String>( serviceMetric.toString(), headers );

        ResponseEntity<ServiceMetric> responseEntity = restTemplate.postForEntity(
                getBaseUrl(),
                request, ServiceMetric.class);
        
        ServiceMetric ret = responseEntity.getBody();
        
        LOG.debug("completed serviceMetric.create {} ", ret);
        
        return ret;
    }

    @Override
    public ServiceMetric get(long serviceMetricId) {
        
        LOG.debug("calling serviceMetric.get {} ", serviceMetricId);

        ResponseEntity<ServiceMetric> responseEntity = restTemplate.getForEntity(
                getBaseUrl()
                +"?serviceMetricId={serviceMetricId}",
                ServiceMetric.class, serviceMetricId);
        
        ServiceMetric ret = responseEntity.getBody();
        
        LOG.debug("completed serviceMetric.get {} ", ret);
        
        return ret;
    }

    @Override
    public ServiceMetric getOrNull(long serviceMetricId) {
        
        LOG.debug("calling serviceMetric.getOrNull {} ", serviceMetricId);

        ResponseEntity<ServiceMetric> responseEntity = restTemplate.getForEntity(
                getBaseUrl()
                +"/orNull?serviceMetricId={serviceMetricId}",
                ServiceMetric.class, serviceMetricId);
        
        ServiceMetric ret = responseEntity.getBody();
        
        LOG.debug("completed serviceMetric.getOrNull {} ", ret);
        
        return ret;
    }

	@Override
	public List<ServiceMetric> get(Set<Long> serviceMetricIdSet) {
		
        LOG.debug("get({})", serviceMetricIdSet);

        if (serviceMetricIdSet == null || serviceMetricIdSet.isEmpty()) {
            return Collections.emptyList();
        }

        String setString = serviceMetricIdSet.toString().substring(1, serviceMetricIdSet.toString().length() - 1);
        
        try {
            ResponseEntity<List<ServiceMetric>> responseEntity = restTemplate.exchange(
                    getBaseUrl() + "/inSet?serviceMetricIdSet={serviceMetricIdSet}", HttpMethod.GET,
                    null, ServiceMetric_LIST_CLASS_TOKEN, setString);

            List<ServiceMetric> result = responseEntity.getBody();
            if (null == result) {
                result = Collections.emptyList();
            }
            LOG.debug("get({}) return {} entries", serviceMetricIdSet, result.size());
            return result;
        } catch (Exception exp) {
            LOG.error("getAllInSet({}) exception ", serviceMetricIdSet, exp);
            throw exp;
        }

	}

	@Override
	public PaginationResponse<ServiceMetric> getForCustomer(int customerId, List<ColumnAndSort> sortBy,
			PaginationContext<ServiceMetric> context) {
		
        LOG.debug("calling getForCustomer( {}, {}, {} )", customerId, sortBy, context);

        ResponseEntity<PaginationResponse<ServiceMetric>> responseEntity = restTemplate.exchange(
                getBaseUrl()
                        + "/forCustomer?customerId={customerId}&sortBy={sortBy}&paginationContext={paginationContext}",
                HttpMethod.GET, null, ServiceMetric_PAGINATION_RESPONSE_CLASS_TOKEN, customerId, sortBy, context);

        PaginationResponse<ServiceMetric> ret = responseEntity.getBody();
        LOG.debug("completed getForCustomer {} ", ret.getItems().size());

        return ret;
	}
	
    @Override
    public ServiceMetric update(ServiceMetric serviceMetric) {
        
        LOG.debug("calling serviceMetric.update {} ", serviceMetric);

        if (BaseJsonModel.hasUnsupportedValue(serviceMetric)) {
            LOG.error("Failed to update ServiceMetric, unsupported value in  {}", serviceMetric);
            throw new DsDataValidationException("ServiceMetric contains unsupported value");
        }
        
        HttpEntity<String> request = new HttpEntity<String>( serviceMetric.toString(), headers );

        ResponseEntity<ServiceMetric> responseEntity = restTemplate.exchange(
                getBaseUrl(),
                HttpMethod.PUT, request, ServiceMetric.class);
        
        ServiceMetric ret = responseEntity.getBody();
        
        LOG.debug("completed serviceMetric.update {} ", ret);
        
        return ret;
    }

    @Override
    public ServiceMetric delete(long serviceMetricId) {
        
        LOG.debug("calling serviceMetric.delete {} ", serviceMetricId);

        ResponseEntity<ServiceMetric> responseEntity =  restTemplate.exchange(
                getBaseUrl()
                +"?serviceMetricId={serviceMetricId}", HttpMethod.DELETE,
                null, ServiceMetric.class, serviceMetricId);
        
        ServiceMetric ret = responseEntity.getBody();
        LOG.debug("completed serviceMetric.delete {} ", ret);
        
        return ret;
    }    

    public String getBaseUrl() {
        if(baseUrl==null) {
            baseUrl = environment.getProperty("tip.wlan.serviceMetricServiceBaseUrl").trim()+"/api/serviceMetric";
        }

    	return baseUrl;
    }


}
