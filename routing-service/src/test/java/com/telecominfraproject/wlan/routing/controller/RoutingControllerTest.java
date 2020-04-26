package com.telecominfraproject.wlan.routing.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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

import com.telecominfraproject.wlan.routing.datastore.inmemory.RoutingDatastoreInMemory;
import com.telecominfraproject.wlan.routing.models.Routing;

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

    
    @Configuration
    //@PropertySource({ "classpath:persistence-${envTarget:dev}.properties" })
    static class Config {
        // Put all required @Bean -s in here
        
    }
    
    @Test
    public void testRoutingCRUD() throws Exception {
        
        //Create new Routing - success
        Routing routing = new Routing();
        routing.setSampleStr("test");

        Routing ret = routingController.create(routing);
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
            Routing expected,
            Routing actual) {
        
        assertEquals(expected.getSampleStr(), actual.getSampleStr());
        //TODO: add more fields to check here
    }

}
