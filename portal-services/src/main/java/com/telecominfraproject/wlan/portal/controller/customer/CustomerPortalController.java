package com.telecominfraproject.wlan.portal.controller.customer;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.telecominfraproject.wlan.core.model.pair.PairIntString;
import com.telecominfraproject.wlan.customer.models.Customer;
import com.telecominfraproject.wlan.customer.service.CustomerServiceInterface;

/**
 * @author dtoptygin
 *
 */
@RestController
@RequestMapping(value = "/portal")
public class CustomerPortalController  {

    private static final Logger logger = LoggerFactory.getLogger(CustomerPortalController.class);

    // For serialization:
    // need these wrapper classes so that each element in returned container is
    // marked with "_type" attribute
    // all callers of this class can deal with normal collections
    public static class ListOfCustomers extends ArrayList<Customer> {
        private static final long serialVersionUID = 1158560190003268713L;
    }

    @Autowired
    private CustomerServiceInterface customerServiceInterface;

    /**
     * Retrieves customer information for the provided customer id.
     * 
     * @param customerId
     *            - unique identifier for the customer whose details are to be
     *            retrieved.
     * 
     * @return an object containing the general customer information parameters.
     *         {@link com.telecominfraproject.wlan.customer.models.Customer}
     */
    @RequestMapping(value = "/customer", method = RequestMethod.GET)
    public Customer getCustomer(@RequestParam int customerId) {
        //checkRequestAccessValidityForCustomer(customerId, "getCustomer", DEAULT_ACCESS_RIGHTS_REQUIRED, customerId);
        logger.debug("Getting customer {}", customerId);

        Customer customer = customerServiceInterface.get(customerId);

        return customer;
    }

    /**
     * Updates the general customer information for the provided customer.
     * 
     * @param customer
     *            - customer whose details are to be updated.
     * 
     * @return an object containing the general customer information parameters
     *         of the recently updated customer.
     *         {@link com.telecominfraproject.wlan.customer.models.Customer}
     */
    @RequestMapping(value = "/customer", method = RequestMethod.PUT)
    public Customer updateCustomer(@RequestBody Customer customer) {

        logger.debug("Updating customer {}", customer.getId());

        //Customer existing = customerServiceInterface.get(customer.getId());

        Customer ret = customerServiceInterface.update(customer);
        //logCustomerOperationResult(customerId, "updateCustomer", existing, ret, ret.getId());

        return ret;

    }

    /**
     * Retrieves all customers whose email or company name match the provided
     * customer identifying query string.
     * 
     * @param criteria
     *            - what to look for.
     * @return a list of all customers whose email or name matches the criteria string.
     */
    @RequestMapping(value = "/customer/find", method = RequestMethod.GET)
    public ListOfCustomers findCustomers(@RequestParam String criteria, @RequestParam int maxResults) {
        ListOfCustomers result = new ListOfCustomers();
        result.addAll(customerServiceInterface.find(criteria, maxResults));

        return result;
    }


    @RequestMapping(value = "/customer/all", method = RequestMethod.GET)
    public List<PairIntString> getAllCustomerIdAndNames(@RequestParam int batchSize,
            @RequestParam(required = false, defaultValue = "0") int continueAfterCustomerId) {
        List<PairIntString> ret = customerServiceInterface.getAll(batchSize, continueAfterCustomerId);

        return ret;
    }

}
