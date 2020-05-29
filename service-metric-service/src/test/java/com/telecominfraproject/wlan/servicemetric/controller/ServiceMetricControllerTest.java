package com.telecominfraproject.wlan.servicemetric.controller;

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
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.servicemetric.apnode.models.ApNodeMetrics;
import com.telecominfraproject.wlan.servicemetric.datastore.inmemory.ServiceMetricDatastoreInMemory;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetric;

/**
 * @author dtoptygin
 * 
 * Integration test for ServiceMetricController
 *
 */
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = {
        "integration_test",
        }) //NOTE: these profiles will be ADDED to the list of active profiles
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = ServiceMetricControllerTest.class)
@Import(value = {
        ServiceMetricController.class,
        CloudEventDispatcherEmpty.class,
        ServiceMetricDatastoreInMemory.class,
        ServiceMetricControllerTest.Config.class, 
        })
public class ServiceMetricControllerTest {
    
    @Autowired private ServiceMetricController serviceMetricController;

    
    @Configuration
    //@PropertySource({ "classpath:persistence-${envTarget:dev}.properties" })
    static class Config {
        // Put all required @Bean -s in here
        
    }
    
    @Test
    public void testServiceMetricCRUD() throws Exception {
        
        //Create new ServiceMetric - success
        ServiceMetric serviceMetric = new ServiceMetric();
        serviceMetric.setCustomerId((int)System.currentTimeMillis());
        serviceMetric.setEquipmentId(System.currentTimeMillis());
        serviceMetric.setClientMac(0);
        serviceMetric.setCreatedTimestamp(System.currentTimeMillis());
        
        ApNodeMetrics details = new ApNodeMetrics();
        details.setTxBytes(RadioType.is2dot4GHz, 42L);
        serviceMetric.setDetails(details );
        serviceMetricController.create(serviceMetric);

        //Delete - success
        serviceMetricController.delete(serviceMetric.getCustomerId(), serviceMetric.getEquipmentId(), System.currentTimeMillis() + 1);
        
    }
        
}
