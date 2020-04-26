package com.telecominfraproject.wlan.alarm.controller;

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

import com.telecominfraproject.wlan.alarm.datastore.inmemory.AlarmDatastoreInMemory;
import com.telecominfraproject.wlan.alarm.models.Alarm;

/**
 * @author dtoptygin
 * 
 * Integration test for AlarmController
 *
 */
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = {
        "integration_test",
        }) //NOTE: these profiles will be ADDED to the list of active profiles
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = AlarmControllerTest.class)
@Import(value = {
        AlarmController.class,
        CloudEventDispatcherEmpty.class,
        AlarmDatastoreInMemory.class,
        AlarmControllerTest.Config.class, 
        })
public class AlarmControllerTest {
    
    @Autowired private AlarmController alarmController;

    
    @Configuration
    //@PropertySource({ "classpath:persistence-${envTarget:dev}.properties" })
    static class Config {
        // Put all required @Bean -s in here
        
    }
    
    @Test
    public void testAlarmCRUD() throws Exception {
        
        //Create new Alarm - success
        Alarm alarm = new Alarm();
        alarm.setSampleStr("test");

        Alarm ret = alarmController.create(alarm);
        assertNotNull(ret);

        ret = alarmController.get(ret.getId());
        assertEqualAlarms(alarm, ret);

        ret = alarmController.getOrNull(ret.getId());
        assertEqualAlarms(alarm, ret);
        
        assertNull(alarmController.getOrNull(-1));

        //Delete - success
        alarmController.delete(ret.getId());
        
    }
        
    private void assertEqualAlarms(
            Alarm expected,
            Alarm actual) {
        
        assertEquals(expected.getSampleStr(), actual.getSampleStr());
        //TODO: add more fields to check here
    }

}
