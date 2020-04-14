package com.telecominfraproject.wlan.startuptasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import com.telecominfraproject.wlan.core.model.entity.CountryCode;
import com.telecominfraproject.wlan.customer.models.Customer;
import com.telecominfraproject.wlan.customer.service.CustomerServiceInterface;
import com.telecominfraproject.wlan.location.models.Location;
import com.telecominfraproject.wlan.location.models.LocationDetails;
import com.telecominfraproject.wlan.location.models.LocationType;
import com.telecominfraproject.wlan.location.service.LocationServiceInterface;

/**
 * Listen for context started event so that we can populate initial dataset in the in-memory datastores
 */
@Configuration
public class AllInOneStartListener implements ApplicationRunner {
	
    private static final Logger LOG = LoggerFactory.getLogger(AllInOneStartListener.class);

    @Autowired
    private CustomerServiceInterface customerServiceInterface;

    @Autowired
    private LocationServiceInterface locationServiceInterface;

    @Override
    public void run(ApplicationArguments args) {
    	        LOG.info("Creating initial objects");

    	        Customer customer = new Customer();
    	        customer.setEmail("test@example.com");
    	        customer.setName("Test Customer");
    	        
    			customer = customerServiceInterface.create(customer );

		       Location location_1 = new Location();
		       location_1.setParentId(0);
		       location_1.setCustomerId(customer.getId());
		       location_1.setLocationType(LocationType.SITE);
		       location_1.setName("Menlo Park");
		       location_1.setDetails(LocationDetails.createWithDefaults());
		       location_1.getDetails().setCountryCode(CountryCode.usa);
		       
		       location_1 = locationServiceInterface.create(location_1);
		       
		       Location location_1_1 = new Location();
		       location_1_1.setParentId(location_1.getId());
		       location_1_1.setCustomerId(customer.getId());
		       location_1_1.setLocationType(LocationType.BUILDING);
		       location_1_1.setName("Building 1");
		       
		       location_1_1 = locationServiceInterface.create(location_1_1);

		       Location location_1_1_1 = new Location();
		       location_1_1_1.setParentId(location_1_1.getId());
		       location_1_1_1.setCustomerId(customer.getId());
		       location_1_1_1.setLocationType(LocationType.FLOOR);
		       location_1_1_1.setName("Floor 1");
		       
		       location_1_1_1 = locationServiceInterface.create(location_1_1_1);

		       Location location_1_1_2 = new Location();
		       location_1_1_2.setParentId(location_1_1.getId());
		       location_1_1_2.setCustomerId(customer.getId());
		       location_1_1_2.setLocationType(LocationType.FLOOR);
		       location_1_1_2.setName("Floor 2");
		       
		       location_1_1_2 = locationServiceInterface.create(location_1_1_2);

		       Location location_1_1_3 = new Location();
		       location_1_1_3.setParentId(location_1_1.getId());
		       location_1_1_3.setCustomerId(customer.getId());
		       location_1_1_3.setLocationType(LocationType.FLOOR);
		       location_1_1_3.setName("Floor 3");
		       
		       location_1_1_3 = locationServiceInterface.create(location_1_1_3);

		       Location location_1_2 = new Location();
		       location_1_2.setParentId(location_1.getId());
		       location_1_2.setCustomerId(customer.getId());
		       location_1_2.setLocationType(LocationType.BUILDING);
		       location_1_2.setName("Building 2");
		       
		       location_1_2 = locationServiceInterface.create(location_1_2);

		       
		       Location location_2 = new Location();
		       location_2.setParentId(0);
		       location_2.setCustomerId(customer.getId());
		       location_2.setLocationType(LocationType.SITE);
		       location_2.setName("Ottawa");
		       location_2.setDetails(LocationDetails.createWithDefaults());
		       location_2.getDetails().setCountryCode(CountryCode.ca);

		       location_2 = locationServiceInterface.create(location_2);

    			LOG.info("Done creating initial objects");
    	        
    }
    
}
