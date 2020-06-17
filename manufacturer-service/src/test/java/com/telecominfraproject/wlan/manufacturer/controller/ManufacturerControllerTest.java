package com.telecominfraproject.wlan.manufacturer.controller;

import static org.junit.Assert.assertTrue;

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
import com.telecominfraproject.wlan.manufacturer.models.ManufacturerDetailsRecord;

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
        
        final String companyName1 = "Complex Company Inc. 1";
        final String companyAlias1 = "Complex 1";
                
        
        //CREATE test
        ManufacturerDetailsRecord manufacturerDetails = new ManufacturerDetailsRecord();
        manufacturerDetails.setManufacturerName(companyName1);
        manufacturerDetails.setManufacturerAlias(companyAlias1);
        
        ManufacturerDetailsRecord ret = manufacturerController.createManufacturerDetails(manufacturerDetails);
        
        //GET by id
        ret = manufacturerController.getById(ret.getId());    
        assertTrue(manufacturerDetails.equals(ret));
        
        manufacturerController.deleteManufacturerDetails(ret.getId());
        
    }
        

}
