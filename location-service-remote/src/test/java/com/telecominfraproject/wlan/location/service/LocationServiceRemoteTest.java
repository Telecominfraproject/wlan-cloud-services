package com.telecominfraproject.wlan.location.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import com.telecominfraproject.wlan.core.model.entity.CountryCode;
import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.location.models.Location;
import com.telecominfraproject.wlan.location.models.LocationDetails;
import com.telecominfraproject.wlan.location.models.LocationType;
import com.telecominfraproject.wlan.remote.tests.BaseRemoteTest;

//NOTE: these profiles will be ADDED to the list of active profiles  
@ActiveProfiles(profiles = {
      "integration_test",
      "no_ssl","http_digest_auth","rest-template-single-user-per-service-digest-auth",
      }) 
public class LocationServiceRemoteTest extends BaseRemoteTest {

    @Autowired LocationServiceInterface testInterface;

    @Before public void urlSetup(){
        configureBaseUrl("tip.wlan.locationServiceBaseUrl");
    }
    
    private static final AtomicInteger customerSequence = new AtomicInteger(1);

    @Test
    public void testCreateUpdateDelete() {
        
        //CREATE test
        Location location = new Location();
        location.setLocationType(LocationType.SITE);
        location.setCustomerId(customerSequence.getAndIncrement());
        location.setName("testName");
        location.setDetails(generateDetails(CountryCode.ca));
        
        Location ret = testInterface.create(location);
        //check that id got auto-assigned
        assertTrue(ret.getId()>0);
                
        //GET by Id test
        ret = testInterface.get(ret.getId());     
        assertFieldEquals(location, ret);

        //UPDATE test - success
        location = ret;
        location.setLocationType(LocationType.FLOOR);
        location.setCustomerId(customerSequence.getAndIncrement());
        location.setName("testName_updated");
        location.setDetails(generateDetails(CountryCode.usa));

        Location retFromUpdate = testInterface.update(location);
        assertFieldEquals(location, retFromUpdate);
        
        //UPDATE test - fail because of concurrent modification exception
        try{
        	Location customerAddressConcurrentUpdate = ret.clone();
        	customerAddressConcurrentUpdate.setLocationType(LocationType.BUILDING);
        	testInterface.update(customerAddressConcurrentUpdate);
        	fail("failed to detect concurrent modification");
        }catch (DsConcurrentModificationException e) {
        	// expected it
        }

        
        //DELETE Test
        testInterface.delete(ret.getId());
        
        try{
        	testInterface.get(ret.getId());
            fail("failed to delete Location");
        }catch (Exception e) {
            // expected it
        }
                
    }
    
    @Test
    public void testGetAllForCustomer() {
        
    	int customerId_1 = customerSequence.getAndIncrement();
    	int customerId_2 = customerSequence.getAndIncrement();
    	
        //create 10 records for customer 2
        for(int i=0; i<10; i++){
            Location equipmentLocationRecord = new Location();
            equipmentLocationRecord.setLocationType(LocationType.SITE);
            equipmentLocationRecord.setCustomerId(customerId_1);
            equipmentLocationRecord.setName("testName_"+i);
            equipmentLocationRecord.setDetails(generateDetails(CountryCode.usa));
            
            Location ret = testInterface.create(equipmentLocationRecord);
            //check that id got auto-assigned
            assertTrue(ret.getId()>0);
        }
                
        //create 5 records for customer 3
        for(int i=0; i<5; i++){
            Location equipmentLocationRecord = new Location();
            equipmentLocationRecord.setLocationType(LocationType.SITE);
            equipmentLocationRecord.setCustomerId(customerId_2);
            equipmentLocationRecord.setName("testName_"+i);
            
            Location ret = testInterface.create(equipmentLocationRecord);
            //check that id got auto-assigned
            assertTrue(ret.getId()>0);
        }
        
        assertEquals(10, testInterface.getAllForCustomer(customerId_1).size());
        assertEquals(5, testInterface.getAllForCustomer(customerId_2).size());
                
    }

    @Test
    public void testChildrenCase()
    {
       /* Grand parent */
       Location grandpa = testInterface.create(generateLocation(0));

       Location homer = testInterface.create(generateLocation(grandpa.getId()));
       Location bart = testInterface.create(generateLocation(homer.getId()));
       Location lisa = testInterface.create(generateLocation(homer.getId()));

       Location bartJr = testInterface.create(generateLocation(bart.getId()));

       // Just to see if Flanders ends up in the mix
       @SuppressWarnings("unused")
       Location flanders = testInterface.create(generateLocation(0));

       List<Location> children = testInterface.getAllDescendants(grandpa.getId());

       assertNotNull(children);
       assertEquals(4, children.size());

       assertTrue(children.contains(homer));
       assertTrue(children.contains(bart));
       assertTrue(children.contains(bartJr));
       assertTrue(children.contains(lisa));
    }

    @Test
    public void testTopLevelQuery()
    {
          /* Grand parent */
          Location grandpa = testInterface.create(generateLocation(0));

          Location homer = testInterface.create(generateLocation(grandpa.getId()));
          Location bart = testInterface.create(generateLocation(homer.getId()));

          @SuppressWarnings("unused")
          Location lisa = testInterface.create(generateLocation(homer.getId()));
          @SuppressWarnings("unused")
          Location bartJr = testInterface.create(generateLocation(bart.getId()));

          // Just to see if Flanders ends up in the mix
          Location orphan = testInterface.create(generateLocation(999));

          Location topParent = testInterface.getTopLevelLocation(bart.getId());
          assertNotNull(topParent);
          assertFieldEquals(grandpa, topParent);
          
          /* We'll test the case also where there's no proper parent */
          Location orphanParent = testInterface.getTopLevelLocation(orphan.getId());
          assertNull(orphanParent);
          
    }

    private Location generateLocation(long parentId)
    {
       Location location = new Location();
       location.setParentId(parentId);
       location.setCustomerId(10);
       location.setLocationType(LocationType.BUILDING);
       location.setName(UUID.randomUUID().toString());
       location.setDetails(generateDetails(CountryCode.usa));

       return location;
    }

    private LocationDetails generateDetails(CountryCode code) {
       LocationDetails details = LocationDetails.createWithDefaults();
       details.setCountryCode(code);
       
       return details;
    }

    private void assertFieldEquals(Location expected, Location actual) {
        assertEquals(expected.getParentId(), actual.getParentId());
        assertEquals(expected.getCustomerId(), actual.getCustomerId());
        assertEquals(expected.getLocationType(), actual.getLocationType());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getDetails(), actual.getDetails());

    }

}
