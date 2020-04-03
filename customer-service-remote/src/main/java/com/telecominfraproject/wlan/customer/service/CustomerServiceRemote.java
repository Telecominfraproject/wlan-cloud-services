package com.telecominfraproject.wlan.customer.service;

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
import com.telecominfraproject.wlan.core.model.pair.PairIntString;
import com.telecominfraproject.wlan.customer.models.Customer;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;

@Component
public class CustomerServiceRemote  extends BaseRemoteClient implements CustomerServiceInterface {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerServiceRemote.class);

    private static final ParameterizedTypeReference<List<Customer>> CUSTOMER_LIST_CLASS_TOKEN = new ParameterizedTypeReference<List<Customer>>() {
    };

    private static final ParameterizedTypeReference<List<PairIntString>> PAIR_INT_STRING_LIST_CLASS_TOKEN = new ParameterizedTypeReference<List<PairIntString>>() {
    };

    
    private String baseUrl;
    

	@Override
	public Customer create(Customer customer) {
		
        if (BaseJsonModel.hasUnsupportedValue(customer)) {
            LOG.error("Failed to update customer, request contains unsupported value: {}", customer);
            throw new DsDataValidationException("CreateCustomerRequest contains unsupported value");
        }
        
        HttpEntity<String> request = new HttpEntity<>(customer.toString(), headers);

        ResponseEntity<Customer> responseEntity = restTemplate.postForEntity(getBaseUrl(), request,
                Customer.class);

        return responseEntity.getBody();
	}

	@Override
	public Customer get(int customerId) {
        ResponseEntity<Customer> responseEntity = restTemplate.getForEntity(
                getBaseUrl() + "?customerId={customerId}", Customer.class,
                customerId);

        return responseEntity.getBody();
	}

	@Override
	public Customer getOrNull(int customerId) {
        ResponseEntity<Customer> responseEntity = restTemplate.getForEntity(
                getBaseUrl() + "/orNull?customerId={customerId}", Customer.class,
                customerId);

        return responseEntity.getBody();
	}

	@Override
	public Customer update(Customer customer) {
    
        if (BaseJsonModel.hasUnsupportedValue(customer)) {
            LOG.error("Failed to update customer, request contains unsupported value: {}", customer);
            throw new DsDataValidationException("UpdateCustomerRequest contains unsupported value");
        }
        HttpEntity<String> request = new HttpEntity<>(customer.toString(), headers);

        ResponseEntity<Customer> responseEntity = restTemplate.exchange(getBaseUrl(),
                HttpMethod.PUT, request, Customer.class);

        return responseEntity.getBody();

	}

	@Override
	public Customer delete(int customerId) {

		Customer ret = get(customerId);
        restTemplate.delete(getBaseUrl() + "?customerId={customerId}", customerId);

        return ret;
	}

	@Override
	public List<PairIntString> getAll(int batchSize, int continueAfterCustomerId) {
    
		LOG.debug("getAll({}, {})", batchSize, continueAfterCustomerId);

        ResponseEntity<List<PairIntString>> responseEntity = restTemplate.exchange(
                getBaseUrl() + "/all?batchSize={batchSize}&continueAfterCustomerId={continueAfterCustomerId}",
                HttpMethod.GET, null, PAIR_INT_STRING_LIST_CLASS_TOKEN, batchSize, continueAfterCustomerId);

        List<PairIntString> ret = responseEntity.getBody();

        LOG.debug("completed getAll({}, {}) returns {} entries", batchSize,
                continueAfterCustomerId, (null == ret) ? 0 : ret.size());

        return ret;

	}

	@Override
	public List<Customer> find(String criteria, int maxResults) {
        LOG.debug("find({}, {})", criteria, maxResults);
        try {
            ResponseEntity<List<Customer>> responseEntity = restTemplate.exchange(
                    getBaseUrl() + "/find?criteria={criteria}&maxResults={maxResults}",
                    HttpMethod.GET, null, CUSTOMER_LIST_CLASS_TOKEN, criteria, maxResults);

            List<Customer> result = responseEntity.getBody();
            if (null == result) {
                result = Collections.emptyList();
            }
            LOG.debug("find({}, {}) return {} entries", criteria, maxResults, result.size());
            return result;
        } catch (Exception exp) {
            LOG.error("getAllWithEmailLike({}, {}) exception ", criteria, maxResults, exp);
            throw exp;
        }

	}

	@Override
	public List<Customer> get(Set<Integer> customerIdSet) {
		
        LOG.debug("get({})", customerIdSet);

        if (customerIdSet == null || customerIdSet.isEmpty()) {
            return Collections.emptyList();
        }

        String setString = customerIdSet.toString().substring(1, customerIdSet.toString().length() - 1);
        try {
            ResponseEntity<List<Customer>> responseEntity = restTemplate.exchange(
                    getBaseUrl() + "/inSet?customerIdSet={customerIdSet}", HttpMethod.GET,
                    null, CUSTOMER_LIST_CLASS_TOKEN, setString);

            List<Customer> result = responseEntity.getBody();
            if (null == result) {
                result = Collections.emptyList();
            }
            LOG.debug("get({}) return {} entries", customerIdSet, result.size());
            return result;
        } catch (Exception exp) {
            LOG.error("getAllInSet({}) exception ", customerIdSet, exp);
            throw exp;
        }

	}

    public String getBaseUrl() {
        if(baseUrl==null){
            baseUrl = environment.getProperty("tip.wlan.customerServiceBaseUrl").trim() + "/api/customer";
        }
        return baseUrl;
    }

}
