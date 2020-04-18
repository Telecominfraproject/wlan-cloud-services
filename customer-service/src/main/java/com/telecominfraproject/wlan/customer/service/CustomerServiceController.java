package com.telecominfraproject.wlan.customer.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.telecominfraproject.wlan.cloudeventdispatcher.CloudEventDispatcherInterface;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.pair.PairIntString;
import com.telecominfraproject.wlan.customer.datastore.CustomerDatastore;
import com.telecominfraproject.wlan.customer.models.Customer;
import com.telecominfraproject.wlan.customer.models.events.CustomerAddedEvent;
import com.telecominfraproject.wlan.customer.models.events.CustomerChangedEvent;
import com.telecominfraproject.wlan.customer.models.events.CustomerRemovedEvent;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;
import com.telecominfraproject.wlan.systemevent.models.SystemEvent;

@RestController
@Transactional
@RequestMapping(value="/api/customer")
public class CustomerServiceController {

    private static final Logger LOG = LoggerFactory.getLogger(CustomerServiceController.class);

    public static class ListOfCustomers extends ArrayList<Customer> {
        private static final long serialVersionUID = 3070319062835500930L;
    }

    public static class ListOfPairIntStrings extends ArrayList<PairIntString> {
        private static final long serialVersionUID = -2741465769595978625L;
    }

    @Autowired private CustomerDatastore customerDatastore;
    @Autowired private CloudEventDispatcherInterface cloudEventDispatcher;

    /**
     * Creates new customer record.
     * 
     * To call this method use: <br>
     * curl --request POST --header "Content-Type: application/json; charset=utf-8" --data '{"email":"test@example.org","name":"Example Inc."}' http://localhost:9090/customer
     * 
     * @param customer object
     * @return created customer object
     * @throws RuntimeException
     *             if customer record already exists
     */
    @RequestMapping(method = RequestMethod.POST)
    public Customer create(@RequestBody final Customer customer) {
        if (BaseJsonModel.hasUnsupportedValue(customer)) {
            LOG.error("Failed to create customer, request contains unsupported value: {}", customer);
            throw new DsDataValidationException("Customer contains unsupported value");
        }

        Customer dbRequest = customer.clone();
        long ts = System.currentTimeMillis();
        if (dbRequest.getCreatedTimestamp() == 0) {
            dbRequest.setCreatedTimestamp(ts);
        }
        dbRequest.setLastModifiedTimestamp(ts);

        Customer result = customerDatastore.create(dbRequest);

        LOG.debug("Created customer {}", dbRequest);

        CustomerAddedEvent event = new CustomerAddedEvent(result);
        publishEvent(event);

        return result;
    }
    
    /**
     * Updates customer record
     * 
     * <br>
     * curl --request PUT --header "Content-Type: application/json;
     * charset=utf-8" --data '{"id":42, "email":"test@example.org","name":"Example
     * 123 Inc."}'
     * http://localhost:9090/customer
     * 
     * @param customer
     * @return customer object
     * @throws RuntimeException
     *             if customer record does not exist
     */
    @RequestMapping(method = RequestMethod.PUT)
    public Customer update(@RequestBody Customer customer) {

        if (BaseJsonModel.hasUnsupportedValue(customer)) {
            LOG.error("Failed to update customer, request contains unsupported value: {}", customer);
            throw new DsDataValidationException("UpdateCustomerRequest contains unsupported value");
        }

        // now update customer record
        Customer ret = customerDatastore.update(customer);
        
        CustomerChangedEvent event = new CustomerChangedEvent(ret);
        publishEvent(event);

        return ret;
    }
    
    /**
     * Removes customer record
     * 
     * <br>
     * curl --request DELETE --header "Content-Type: application/json;
     * charset=utf-8" http://localhost:9090/customer?customerId=42
     * 
     * @param email
     * @return deleted customer object
     * @throws RuntimeException
     *             if customer record does not exist
     */
    @RequestMapping(method = RequestMethod.DELETE)
    public Customer delete(@RequestParam int customerId) {

        Customer customer = customerDatastore.delete(customerId);

        CustomerRemovedEvent event = new CustomerRemovedEvent(customer);
        publishEvent(event);

        return customer;
    }


    /**
     * Retrieves customer record by customer id
     * 
     * <br>
     * curl --request GET --header "Content-Type: application/json;
     * charset=utf-8" http://localhost:9090/customer?customerId=42
     * 
     * @param customerId
     * @return customer object
     * @throws RuntimeException
     *             if customer record does not exist
     */
    @RequestMapping(method = RequestMethod.GET)
    public Customer get(@RequestParam int customerId) {
        Customer customer = customerDatastore.get(customerId);
        return customer;
    }


    /**
     * Retrieves customer record by customer id
     * 
     * <br>
     * curl --request GET --header "Content-Type: application/json;
     * charset=utf-8" http://localhost:9090/customer/orNull?customerId=42
     * 
     * @param customerId
     * @return customer object or null if customer record does not exist
     */
    @RequestMapping(value = "/orNull", method = RequestMethod.GET)
    public Customer getOrNull(@RequestParam int customerId) {
        Customer customer = customerDatastore.getOrNull(customerId);

        return customer;
    }

    @RequestMapping(value = "/inSet", method = RequestMethod.GET)
    public ListOfCustomers getAllInSet(@RequestParam Set<Integer> customerIdSet) {
        LOG.debug("getAllInSet({})", customerIdSet);
        try {
            List<Customer> result = customerDatastore.get(customerIdSet);
            LOG.debug("getAllInSet({}) return {} entries", customerIdSet, result.size());
            ListOfCustomers ret = new ListOfCustomers();
            ret.addAll(result);
            return ret;
        } catch (Exception exp) {
             LOG.error("getAllInSet({}) exception ", customerIdSet, exp);
             throw exp;
        }
    }
    
    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public ListOfCustomers find(@RequestParam String criteria, @RequestParam int maxResults) {
        LOG.debug("find({}, {})", criteria, maxResults);
        try {
            List<Customer> result = customerDatastore.find(criteria, maxResults);
            LOG.debug("find({}, {}) return {} entries", criteria, maxResults, result.size());
            ListOfCustomers ret = new ListOfCustomers();
            ret.addAll(result);
            return ret;
        } catch (Exception exp) {
            LOG.error("find({}, {}) exception ", criteria, maxResults, exp);
            throw exp;
        }
    }
    
    /**
     * Retrieve a batch of pairs<customerId, customerName>, sorted by
     * customerId, batch starts after the id specified by
     * continueAfterCustomerId (excluding)
     * 
     * @param batchSize
     *            - requested batch size
     * @param continueAfterCustomerId
     *            - (excluding) return records that come after this customer id.
     * @return
     */
    @RequestMapping(value = "/all", method = RequestMethod.GET)
    public ListOfPairIntStrings getAllCustomerIdAndNames(@RequestParam int batchSize,
            @RequestParam int continueAfterCustomerId) {
        LOG.debug("Calling getAllCustomerIdAndNames({}, {}, {})", batchSize, continueAfterCustomerId);
        ListOfPairIntStrings ret = new ListOfPairIntStrings();
        ret.addAll(customerDatastore.getAll(batchSize, continueAfterCustomerId));

        LOG.debug("Returning getAllCustomerIdAndNames {}", ret.size());

        return ret;
    }

    
    private void publishEvent(SystemEvent event) {
        if (event == null) {
            return;
        }
        
        try {
            cloudEventDispatcher.publishEvent(event);
        } catch (Exception e) {
            LOG.error("Failed to publish event : {}", event, e);
        }
    }

}
