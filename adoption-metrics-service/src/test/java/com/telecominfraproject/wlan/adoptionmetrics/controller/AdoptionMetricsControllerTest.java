package com.telecominfraproject.wlan.adoptionmetrics.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.telecominfraproject.wlan.adoptionmetrics.datastore.inmemory.AdoptionMetricsDatastoreInMemory;
import com.telecominfraproject.wlan.cloudeventdispatcher.CloudEventDispatcherEmpty;

/**
 * @author dtoptygin
 * 
 * Integration test for AdoptionMetricsController
 *
 */
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = {
        "integration_test",
        }) //NOTE: these profiles will be ADDED to the list of active profiles
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = AdoptionMetricsControllerTest.class)
@Import(value = {
        AdoptionMetricsController.class,
        CloudEventDispatcherEmpty.class,
        AdoptionMetricsDatastoreInMemory.class,
        AdoptionMetricsControllerTest.Config.class, 
        })
public class AdoptionMetricsControllerTest {
    
    @Autowired private AdoptionMetricsController adoptionMetricsController;

    
    @Configuration
    //@PropertySource({ "classpath:persistence-${envTarget:dev}.properties" })
    static class Config {
        // Put all required @Bean -s in here
        
    }
    
    @Test
    public void testServiceAdoptionMetricsCRUD() throws Exception {
        //nothing to do here yet        
    }
        
}
