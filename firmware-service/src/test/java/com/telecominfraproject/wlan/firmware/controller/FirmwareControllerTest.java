package com.telecominfraproject.wlan.firmware.controller;

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

import com.telecominfraproject.wlan.firmware.datastore.inmemory.FirmwareDatastoreInMemory;
import com.telecominfraproject.wlan.firmware.models.Firmware;

/**
 * @author dtoptygin
 * 
 * Integration test for FirmwareController
 *
 */
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = {
        "integration_test",
        }) //NOTE: these profiles will be ADDED to the list of active profiles
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = FirmwareControllerTest.class)
@Import(value = {
        FirmwareController.class,
        CloudEventDispatcherEmpty.class,
        FirmwareDatastoreInMemory.class,
        FirmwareControllerTest.Config.class, 
        })
public class FirmwareControllerTest {
    
    @Autowired private FirmwareController firmwareController;

    
    @Configuration
    //@PropertySource({ "classpath:persistence-${envTarget:dev}.properties" })
    static class Config {
        // Put all required @Bean -s in here
        
    }
    
    @Test
    public void testFirmwareCRUD() throws Exception {
        
        //Create new Firmware - success
        Firmware firmware = new Firmware();
        firmware.setSampleStr("test");

        Firmware ret = firmwareController.create(firmware);
        assertNotNull(ret);

        ret = firmwareController.get(ret.getId());
        assertEqualFirmwares(firmware, ret);

        ret = firmwareController.getOrNull(ret.getId());
        assertEqualFirmwares(firmware, ret);
        
        assertNull(firmwareController.getOrNull(-1));

        //Delete - success
        firmwareController.delete(ret.getId());
        
    }
        
    private void assertEqualFirmwares(
            Firmware expected,
            Firmware actual) {
        
        assertEquals(expected.getSampleStr(), actual.getSampleStr());
        //TODO: add more fields to check here
    }

}
