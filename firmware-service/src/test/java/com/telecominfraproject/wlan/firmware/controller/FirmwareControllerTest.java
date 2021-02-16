package com.telecominfraproject.wlan.firmware.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

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
import com.telecominfraproject.wlan.firmware.datastore.inmemory.CustomerFirmwareTrackDatastoreInMemory;
import com.telecominfraproject.wlan.firmware.datastore.inmemory.FirmwareDatastoreInMemory;
import com.telecominfraproject.wlan.firmware.datastore.inmemory.FirmwareTrackAssignmentDatastoreInMemory;
import com.telecominfraproject.wlan.firmware.datastore.inmemory.FirmwareTrackDatastoreInMemory;
import com.telecominfraproject.wlan.firmware.datastore.inmemory.FirmwareVersionDatastoreInMemory;
import com.telecominfraproject.wlan.firmware.models.FirmwareVersion;
import com.telecominfraproject.wlan.firmware.models.ValidationMethod;

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
        FirmwareDatastoreInMemory.class, FirmwareVersionDatastoreInMemory.class,
		FirmwareTrackDatastoreInMemory.class, FirmwareTrackAssignmentDatastoreInMemory.class,
		CustomerFirmwareTrackDatastoreInMemory.class,        
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
        FirmwareVersion firmware = new FirmwareVersion();
        firmware.setEquipmentType(EquipmentType.AP);
        firmware.setVersionName("FirmwareControllerTest-version");
        firmware.setCommit("abc");
        firmware.setDescription("this is the description");
        firmware.setModelId("FirmwareControllerTest-model1");
        firmware.setFilename("filename1");
        firmware.setValidationMethod(ValidationMethod.MD5_CHECKSUM);
        firmware.setValidationCode("check1");
        firmware.setReleaseDate(System.currentTimeMillis());

        FirmwareVersion ret = firmwareController.createFirmwareVersion(firmware);
        assertNotNull(ret);

        ret = firmwareController.getFirmwareVersion(ret.getId());
        assertFieldsEqual(firmware, ret);

        //Delete - success
        firmwareController.deleteFirmwareVersion(ret.getId());
        
    }
    
    @Test
    public void testFirmwareCRUDWithNoValidationMethod() throws Exception {
        
        //Create new Firmware - success
        FirmwareVersion firmware = new FirmwareVersion();
        firmware.setEquipmentType(EquipmentType.AP);
        firmware.setVersionName("FirmwareControllerTest-version");
        firmware.setCommit("abc");
        firmware.setDescription("this is the description");
        firmware.setModelId("FirmwareControllerTest-model1");
        firmware.setFilename("filename1");
        firmware.setValidationMethod(null);
        firmware.setReleaseDate(System.currentTimeMillis());

        FirmwareVersion ret = firmwareController.createFirmwareVersion(firmware);
        assertNotNull(ret);

        ret = firmwareController.getFirmwareVersion(ret.getId());
        assertFieldsEqual(firmware, ret);

        //Delete - success
        firmwareController.deleteFirmwareVersion(ret.getId());
        
    }

    private void assertFieldsEqual(FirmwareVersion expected, FirmwareVersion actual) {
        // add more fields from FirmwareVersion to check in here
        assertEquals(expected.getEquipmentType(), actual.getEquipmentType());
        assertEquals(expected.getVersionName(), actual.getVersionName());
        assertEquals(expected.getModelId(), actual.getModelId());
        assertEquals(expected.getDescription(), actual.getDescription());
        assertEquals(expected.getFilename(), actual.getFilename());
        assertEquals(expected.getValidationMethod(), actual.getValidationMethod());
        assertEquals(expected.getValidationCode(), actual.getValidationCode());
        assertEquals(expected.getReleaseDate(), actual.getReleaseDate());
    }

}
