package com.telecominfraproject.wlan.startuptasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import com.telecominfraproject.wlan.customer.models.Customer;
import com.telecominfraproject.wlan.customer.service.CustomerServiceInterface;

/**
 * Listen for context started event so that we can populate initial dataset in the in-memory datastores
 */
@Configuration
public class AllInOneStartListener implements ApplicationRunner {
	
    private static final Logger LOG = LoggerFactory.getLogger(AllInOneStartListener.class);

    @Autowired
    private CustomerServiceInterface customerServiceInterface;


    @Override
    public void run(ApplicationArguments args) {
    	        LOG.info("Creating initial objects");

    	        Customer customer = new Customer();
    	        customer.setEmail("test@example.com");
    	        customer.setName("Test Customer");
    	        
    			customerServiceInterface.create(customer );

    			LOG.info("Done creating initial objects");
    	        
    }
    
}
