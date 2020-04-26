package com.telecominfraproject.wlan.status.controller;

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

import com.telecominfraproject.wlan.status.datastore.inmemory.StatusDatastoreInMemory;
import com.telecominfraproject.wlan.status.models.Status;

/**
 * @author dtoptygin
 * 
 * Integration test for StatusController
 *
 */
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = {
        "integration_test",
        }) //NOTE: these profiles will be ADDED to the list of active profiles
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = StatusControllerTest.class)
@Import(value = {
        StatusController.class,
        CloudEventDispatcherEmpty.class,
        StatusDatastoreInMemory.class,
        StatusControllerTest.Config.class, 
        })
public class StatusControllerTest {
    
    @Autowired private StatusController statusController;

    
    @Configuration
    //@PropertySource({ "classpath:persistence-${envTarget:dev}.properties" })
    static class Config {
        // Put all required @Bean -s in here
        
    }
    
    @Test
    public void testStatusCRUD() throws Exception {
        
        //Create new Status - success
        Status status = new Status();
        status.setSampleStr("test");

        Status ret = statusController.create(status);
        assertNotNull(ret);

        ret = statusController.get(ret.getId());
        assertEqualStatuss(status, ret);

        ret = statusController.getOrNull(ret.getId());
        assertEqualStatuss(status, ret);
        
        assertNull(statusController.getOrNull(-1));

        //Delete - success
        statusController.delete(ret.getId());
        
    }
        
    private void assertEqualStatuss(
            Status expected,
            Status actual) {
        
        assertEquals(expected.getSampleStr(), actual.getSampleStr());
        //TODO: add more fields to check here
    }

}
