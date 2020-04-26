package com.telecominfraproject.wlan.systemevent.controller;

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

import com.telecominfraproject.wlan.systemevent.datastore.inmemory.SystemEventDatastoreInMemory;
import com.telecominfraproject.wlan.systemevent.models.SystemEventContainer;

/**
 * @author dtoptygin
 * 
 * Integration test for SystemEventController
 *
 */
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = {
        "integration_test",
        }) //NOTE: these profiles will be ADDED to the list of active profiles
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = SystemEventControllerTest.class)
@Import(value = {
        SystemEventController.class,
        CloudEventDispatcherEmpty.class,
        SystemEventDatastoreInMemory.class,
        SystemEventControllerTest.Config.class, 
        })
public class SystemEventControllerTest {
    
    @Autowired private SystemEventController systemEventController;

    
    @Configuration
    //@PropertySource({ "classpath:persistence-${envTarget:dev}.properties" })
    static class Config {
        // Put all required @Bean -s in here
        
    }
    
    @Test
    public void testSystemEventRecordCRUD() throws Exception {
        
        //Create new SystemEventContainer - success
        SystemEventContainer systemEventRecord = new SystemEventContainer();
        systemEventRecord.setSampleStr("test");

        SystemEventContainer ret = systemEventController.create(systemEventRecord);
        assertNotNull(ret);
        assertEqualSystemEventRecords(systemEventRecord, ret);
        
    }
        
    private void assertEqualSystemEventRecords(
            SystemEventContainer expected,
            SystemEventContainer actual) {
        
        assertEquals(expected.getSampleStr(), actual.getSampleStr());
        //TODO: add more fields to check here
    }

}
