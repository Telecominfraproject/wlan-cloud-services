package com.telecominfraproject.wlan.equipment.controller;

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
import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
import com.telecominfraproject.wlan.equipment.datastore.inmemory.EquipmentDatastoreInMemory;
import com.telecominfraproject.wlan.equipment.models.Equipment;

/**
 * @author dtoptygin
 * 
 * Integration test for EquipmentController
 *
 */
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = {
        "integration_test",
        }) //NOTE: these profiles will be ADDED to the list of active profiles
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = EquipmentControllerTest.class)
@Import(value = {
        EquipmentController.class,
        CloudEventDispatcherEmpty.class,
        EquipmentDatastoreInMemory.class,
        EquipmentControllerTest.Config.class, 
        })
public class EquipmentControllerTest {
    
    @Autowired private EquipmentController equipmentController;

    
    @Configuration
    //@PropertySource({ "classpath:persistence-${envTarget:dev}.properties" })
    static class Config {
        // Put all required @Bean -s in here
        
    }
    
    @Test
    public void testEquipmentCRUD() throws Exception {
        
        //Create new Equipment - success
        Equipment equipment = new Equipment();
        equipment.setName("test");
        equipment.setEquipmentType(EquipmentType.AP);

        Equipment ret = equipmentController.create(equipment);
        assertNotNull(ret);

        ret = equipmentController.get(ret.getId());
        assertEqualEquipments(equipment, ret);

        ret = equipmentController.getOrNull(ret.getId());
        assertEqualEquipments(equipment, ret);
        
        assertNull(equipmentController.getOrNull(-1));

        //Delete - success
        equipmentController.delete(ret.getId());
        
    }
        
    private void assertEqualEquipments(
            Equipment expected,
            Equipment actual) {
        
        assertEquals(expected.getName(), actual.getName());
        //TODO: add more fields to check here
    }

}
