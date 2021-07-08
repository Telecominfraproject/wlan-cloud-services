package com.telecominfraproject.wlan.alarm.controller;

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
import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
import com.telecominfraproject.wlan.equipment.EquipmentServiceLocal;
import com.telecominfraproject.wlan.equipment.controller.EquipmentController;
import com.telecominfraproject.wlan.equipment.datastore.inmemory.EquipmentDatastoreInMemory;
import com.telecominfraproject.wlan.equipment.models.Equipment;
import com.telecominfraproject.wlan.status.StatusServiceLocal;
import com.telecominfraproject.wlan.status.controller.StatusController;
import com.telecominfraproject.wlan.status.datastore.inmemory.StatusDatastoreInMemory;
import com.telecominfraproject.wlan.alarm.datastore.inmemory.AlarmDatastoreInMemory;
import com.telecominfraproject.wlan.alarm.models.Alarm;
import com.telecominfraproject.wlan.alarm.models.AlarmCode;
import com.telecominfraproject.wlan.alarm.models.AlarmDetails;

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
        EquipmentServiceLocal.class,
        EquipmentController.class,
        EquipmentDatastoreInMemory.class,
        StatusServiceLocal.class,
        StatusController.class,
        StatusDatastoreInMemory.class
        })
public class AlarmControllerTest {
    
    @Autowired private AlarmController alarmController;
    @Autowired EquipmentServiceLocal equipmentServicelocal;

    
    @Configuration
    //@PropertySource({ "classpath:persistence-${envTarget:dev}.properties" })
    static class Config {
        // Put all required @Bean -s in here
        
    }
    
    private static final AtomicLong testSequence = new AtomicLong(1000);

    @Test
    public void testAlarmCRUD() throws Exception {
        
        //Create new Alarm - success
        Alarm alarm = new Alarm();
        int customerId = (int) testSequence.getAndIncrement();
        alarm.setCustomerId(customerId);
        alarm.setEquipmentId(createEquipmentObject(customerId).getId());
        alarm.setAlarmCode(AlarmCode.AccessPointIsUnreachable);
        alarm.setCreatedTimestamp(System.currentTimeMillis());
        
        alarm.setScopeId("test-scope-"  + alarm.getEquipmentId());
        
        AlarmDetails details = new AlarmDetails();
        details.setMessage("test-details-" + alarm.getEquipmentId());
        alarm.setDetails(details );

        Alarm ret = alarmController.create(alarm);
        assertNotNull(ret);

        ret = alarmController.getOrNull(ret.getCustomerId(), ret.getEquipmentId(), ret.getAlarmCode(), ret.getCreatedTimestamp());
        assertEqualAlarms(alarm, ret);
        
        assertNull(alarmController.getOrNull(-1, -1, AlarmCode.AccessPointIsUnreachable, -1));

        //Delete - success
        alarmController.delete(ret.getCustomerId(), ret.getEquipmentId(), ret.getAlarmCode(), ret.getCreatedTimestamp());
        ret = alarmController.getOrNull(ret.getCustomerId(), ret.getEquipmentId(), ret.getAlarmCode(), ret.getCreatedTimestamp());
        assertNull(ret);
    }
        
    private void assertEqualAlarms(
            Alarm expected,
            Alarm actual) {
        
        assertEquals(expected.getDetails(), actual.getDetails());
        //TODO: add more fields to check here
    }
    
    private Equipment createEquipmentObject(int customerId)
    {
        Equipment equipment = new Equipment();
        equipment.setName("testName");
        equipment.setInventoryId("test-inv");
        equipment.setEquipmentType(EquipmentType.AP);
        equipment.setCustomerId(customerId);
        return equipmentServicelocal.create(equipment);
    }
}
