package com.telecominfraproject.wlan.customer.service;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.core.model.pair.PairIntString;
import com.telecominfraproject.wlan.customer.models.Customer;

@Component
public class CustomerServiceLocal implements CustomerServiceInterface {

	@Autowired CustomerServiceController customerServiceController;
	
	@Override
	public Customer create(Customer customer) {
		return customerServiceController.create(customer);
	}

	@Override
	public Customer get(int customerId) {
		return customerServiceController.get(customerId);
	}

	@Override
	public Customer getOrNull(int customerId) {
		return customerServiceController.getOrNull(customerId);
	}

	@Override
	public Customer update(Customer customer) {
		return customerServiceController.update(customer);
	}

	@Override
	public Customer delete(int customerId) {
		return customerServiceController.delete(customerId);
	}

	@Override
	public List<PairIntString> getAll(int batchSize, int continueAfterCustomerId) {
		return customerServiceController.getAllCustomerIdAndNames(batchSize, continueAfterCustomerId);
	}

	@Override
	public List<Customer> find(String value, int maxRecords) {
		return customerServiceController.find(value, maxRecords);
	}

	@Override
	public List<Customer> get(Set<Integer> customerIdSet) {
		return customerServiceController.getAllInSet(customerIdSet);
	}

}
