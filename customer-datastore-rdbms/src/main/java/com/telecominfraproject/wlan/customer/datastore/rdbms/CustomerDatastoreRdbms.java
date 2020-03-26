package com.telecominfraproject.wlan.customer.datastore.rdbms;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;

import com.telecominfraproject.wlan.core.model.pair.PairIntString;
import com.telecominfraproject.wlan.customer.datastore.CustomerDatastore;
import com.telecominfraproject.wlan.customer.models.Customer;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;

/**
 * @author dtoptygin
 *
 */
@Transactional(noRollbackFor = { DsEntityNotFoundException.class })
@Configuration
public class CustomerDatastoreRdbms implements CustomerDatastore {

    @Autowired
    private CustomerDAO customerInfoDAO;

    @Override
    public Customer create(final Customer customer) {
        return customerInfoDAO.create(customer);
    }

    @Override
    public Customer get(int id) {
        return customerInfoDAO.get(id);
    }

    @Override
    public Customer getOrNull(int id) {
        return customerInfoDAO.getOrNull(id);
    }

    @Override
    public List<PairIntString> getAll(int batchSize, int continueAfterCustomerId) {
        return customerInfoDAO.getAll(batchSize, continueAfterCustomerId);
    }

    @Override
    public List<Customer> find(String filterStr, int maxResults) {
        return customerInfoDAO.find(filterStr, maxResults);
    }

    @Override
    public Customer update(Customer customer) {
        return customerInfoDAO.update(customer);
    }

    @Override
    public Customer delete(int id) {
        return customerInfoDAO.delete(id);
    }


    @Override
    public List<Customer> get(Set<Integer> customerIdSet) {
        return customerInfoDAO.getCustomersInSet(customerIdSet);
    }

}
