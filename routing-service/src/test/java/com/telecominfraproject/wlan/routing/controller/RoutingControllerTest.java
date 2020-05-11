package com.telecominfraproject.wlan.routing.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.telecominfraproject.wlan.cloudeventdispatcher.CloudEventDispatcherEmpty;
import com.telecominfraproject.wlan.core.model.service.GatewayType;
import com.telecominfraproject.wlan.routing.datastore.inmemory.RoutingDatastoreInMemory;
import com.telecominfraproject.wlan.routing.models.EquipmentGatewayRecord;
import com.telecominfraproject.wlan.routing.models.EquipmentRoutingRecord;

/**
 * @author dtoptygin
 * 
 * Integration test for RoutingController
 *
 */
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = {
        "integration_test",
        }) //NOTE: these profiles will be ADDED to the list of active profiles
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = RoutingControllerTest.class)
@Import(value = {
        RoutingController.class,
        CloudEventDispatcherEmpty.class,
        RoutingDatastoreInMemory.class,
        RoutingControllerTest.Config.class, 
        })
public class RoutingControllerTest {
    
    @Autowired private RoutingController routingController;

    private static final AtomicLong testSequence = new AtomicLong(100);

    
    @Configuration
    //@PropertySource({ "classpath:persistence-${envTarget:dev}.properties" })
    static class Config {
        // Put all required @Bean -s in here
        
    }
    
    @Test
    public void testRoutingCRUD() throws Exception {
        
        //Create new EquipmentRoutingRecord - success
    	EquipmentGatewayRecord gateway = new EquipmentGatewayRecord();
    	gateway.setGatewayType(GatewayType.CEGW);
    	gateway.setHostname("test-hostname-controller");
    	gateway.setIpAddr("127.0.0.1");
    	gateway.setPort(4242);
    	gateway = routingController.registerGateway(gateway);
    	
    	EquipmentRoutingRecord routing = new EquipmentRoutingRecord();
    	routing.setCustomerId((int) testSequence.incrementAndGet());
    	routing.setEquipmentId(testSequence.incrementAndGet());

    	routing.setGatewayId(gateway.getId());

        //create
        EquipmentRoutingRecord ret = routingController.create(routing);
        assertNotNull(ret);

        ret = routingController.get(ret.getId());
        assertEqualRoutings(routing, ret);

        ret = routingController.getOrNull(ret.getId());
        assertEqualRoutings(routing, ret);
        
        assertNull(routingController.getOrNull(-1));

        //Delete - success
        routingController.delete(ret.getId());
        
    }
        
    private void assertEqualRoutings(
            EquipmentRoutingRecord expected,
            EquipmentRoutingRecord actual) {
        
        assertEquals(expected.getEquipmentId(), actual.getEquipmentId());
        //TODO: add more fields to check here
    }

}
