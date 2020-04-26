package com.telecominfraproject.wlan.manufacturer.controller;

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

import com.telecominfraproject.wlan.manufacturer.datastore.inmemory.ManufacturerDatastoreInMemory;
import com.telecominfraproject.wlan.manufacturer.models.Manufacturer;

/**
 * @author dtoptygin
 * 
 * Integration test for ManufacturerController
 *
 */
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = {
        "integration_test",
        }) //NOTE: these profiles will be ADDED to the list of active profiles
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = ManufacturerControllerTest.class)
@Import(value = {
        ManufacturerController.class,
        CloudEventDispatcherEmpty.class,
        ManufacturerDatastoreInMemory.class,
        ManufacturerControllerTest.Config.class, 
        })
public class ManufacturerControllerTest {
    
    @Autowired private ManufacturerController manufacturerController;

    
    @Configuration
    //@PropertySource({ "classpath:persistence-${envTarget:dev}.properties" })
    static class Config {
        // Put all required @Bean -s in here
        
    }
    
    @Test
    public void testManufacturerCRUD() throws Exception {
        
        //Create new Manufacturer - success
        Manufacturer manufacturer = new Manufacturer();
        manufacturer.setSampleStr("test");

        Manufacturer ret = manufacturerController.create(manufacturer);
        assertNotNull(ret);

        ret = manufacturerController.get(ret.getId());
        assertEqualManufacturers(manufacturer, ret);

        ret = manufacturerController.getOrNull(ret.getId());
        assertEqualManufacturers(manufacturer, ret);
        
        assertNull(manufacturerController.getOrNull(-1));

        //Delete - success
        manufacturerController.delete(ret.getId());
        
    }
        
    private void assertEqualManufacturers(
            Manufacturer expected,
            Manufacturer actual) {
        
        assertEquals(expected.getSampleStr(), actual.getSampleStr());
        //TODO: add more fields to check here
    }

}
