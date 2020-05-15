package com.telecominfraproject.wlan.status.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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

import com.telecominfraproject.wlan.status.datastore.inmemory.StatusDatastoreInMemory;
import com.telecominfraproject.wlan.status.equipment.models.EquipmentAdminStatusData;
import com.telecominfraproject.wlan.status.models.Status;
import com.telecominfraproject.wlan.status.models.StatusCode;
import com.telecominfraproject.wlan.status.models.StatusDataType;

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

    private static final AtomicLong testSequence = new AtomicLong(1);
    
    @Configuration
    //@PropertySource({ "classpath:persistence-${envTarget:dev}.properties" })
    static class Config {
        // Put all required @Bean -s in here
        
    }
    
    @Test
    public void testStatusCRUD() throws Exception {
        
        //Create new Status - success
        Status status = new Status();
        long nextId = testSequence.getAndIncrement();
        status.setCustomerId((int) nextId);
        status.setEquipmentId(testSequence.getAndIncrement()); 
        status.setStatusDataType(StatusDataType.EQUIPMENT_ADMIN);
        EquipmentAdminStatusData details = new EquipmentAdminStatusData();
        details.setStatusCode(StatusCode.normal);
        status.setDetails(details );

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
        
        assertEquals(expected.getDetails(), actual.getDetails());
        //TODO: add more fields to check here
    }

}
