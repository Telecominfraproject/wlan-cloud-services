package com.telecominfraproject.wlan.portal.controller.equipment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.telecominfraproject.wlan.alarm.AlarmServiceLocal;
import com.telecominfraproject.wlan.alarm.controller.AlarmController;
import com.telecominfraproject.wlan.alarm.datastore.inmemory.AlarmDatastoreInMemory;
import com.telecominfraproject.wlan.cloudeventdispatcher.CloudEventDispatcherEmpty;
import com.telecominfraproject.wlan.core.model.entity.CountryCode;
import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;
import com.telecominfraproject.wlan.equipment.EquipmentServiceLocal;
import com.telecominfraproject.wlan.equipment.controller.EquipmentController;
import com.telecominfraproject.wlan.equipment.datastore.inmemory.EquipmentDatastoreInMemory;
import com.telecominfraproject.wlan.equipment.models.ApElementConfiguration;
import com.telecominfraproject.wlan.equipment.models.ChannelPowerLevel;
import com.telecominfraproject.wlan.equipment.models.ElementRadioConfiguration;
import com.telecominfraproject.wlan.equipment.models.Equipment;
import com.telecominfraproject.wlan.equipment.models.RadioConfiguration;
import com.telecominfraproject.wlan.location.datastore.LocationDatastore;
import com.telecominfraproject.wlan.location.datastore.inmemory.LocationDatastoreInMemory;
import com.telecominfraproject.wlan.location.models.Location;
import com.telecominfraproject.wlan.location.models.LocationDetails;
import com.telecominfraproject.wlan.location.models.LocationType;
import com.telecominfraproject.wlan.location.service.LocationServiceController;
import com.telecominfraproject.wlan.location.service.LocationServiceLocal;
import com.telecominfraproject.wlan.profile.ProfileServiceLocal;
import com.telecominfraproject.wlan.profile.controller.ProfileController;
import com.telecominfraproject.wlan.profile.datastore.inmemory.ProfileDatastoreInMemory;
import com.telecominfraproject.wlan.profile.models.ProfileByCustomerRequestFactory;
import com.telecominfraproject.wlan.status.StatusServiceLocal;
import com.telecominfraproject.wlan.status.controller.StatusController;
import com.telecominfraproject.wlan.status.datastore.inmemory.StatusDatastoreInMemory;

@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = {
        "integration_test",
        }) //NOTE: these profiles will be ADDED to the list of active profiles
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = EquipmentPortalControllerTest.class)
@Import(value = {
        EquipmentServiceLocal.class,
        EquipmentController.class,
        EquipmentDatastoreInMemory.class,
        EquipmentPortalController.class,
        LocationServiceLocal.class,
        LocationServiceController.class,
        LocationDatastoreInMemory.class,
        CloudEventDispatcherEmpty.class,
        AlarmServiceLocal.class,
        AlarmController.class,
        AlarmDatastoreInMemory.class,
        ProfileServiceLocal.class,
        ProfileDatastoreInMemory.class,
        ProfileController.class,
        ProfileByCustomerRequestFactory.class,
        StatusServiceLocal.class,
        StatusDatastoreInMemory.class,
        StatusController.class
        })
public class EquipmentPortalControllerTest {
    private static AtomicLong locationIncrementer = new AtomicLong(1);
    private static AtomicLong equipmentIncrementer = new AtomicLong(1);
    private static Set<Long> locationIds = new HashSet<Long>();
    private static Set<Long> equipmentIds = new HashSet<Long>();
    
    @Autowired private EquipmentPortalController equipmentPortalController;
    @Autowired private LocationDatastore locationDatastore;
    
    @BeforeEach
    public void setup()
    {
        locationIds.clear();
        equipmentIds.clear();
    }
    
    protected Location createLocation(int customerId, LocationType locationType, CountryCode countryCode) {
        Location location = new Location();
        location.setCustomerId(customerId);

        // Setup location details with default CmapLocationActivityDetails
        LocationDetails details = LocationDetails.createWithDefaults();
        details.setCountryCode(countryCode);
        location.setDetails(details);

        location.setName("Location-UT" + locationIncrementer.getAndIncrement());
        location.setLocationType(locationType);
        
        Location created = locationDatastore.create(location);
        assertNotNull(created);

        return created;
    }
    
    @Test
    public void testEquipmentUpdateChannelNumValidation() {

        Location location = createLocation(2, LocationType.SITE, CountryCode.US);
        
        //Create new Equipment - success
        Equipment equipment = new Equipment();
        equipment.setName("testName-"+ equipmentIncrementer.getAndIncrement());
        equipment.setInventoryId("test-inv-"+equipmentIncrementer.getAndIncrement());
        equipment.setEquipmentType(EquipmentType.AP);
        equipment.setLocationId(location.getId());
        equipment.setDetails(ApElementConfiguration.createWithDefaults());

        ElementRadioConfiguration element2dot4RadioConfig = ((ApElementConfiguration)equipment.getDetails()).getRadioMap().get(RadioType.is2dot4GHz);       
        element2dot4RadioConfig.setAllowedChannelsPowerLevels(new HashSet<>());
        
        for (int i = 1; i <= 11; i++) {
            ChannelPowerLevel cpl = new ChannelPowerLevel();
            cpl.setChannelNumber(i);
            cpl.setChannelWidth(20);
            cpl.setDfs(false);
            cpl.setPowerLevel(30);
            element2dot4RadioConfig.getAllowedChannelsPowerLevels().add(cpl);
        }

        Equipment ret = equipmentPortalController.createEquipment(equipment);
        assertNotNull(ret);
        long equipmentId = ret.getId();

        ret = equipmentPortalController.getEquipment(equipmentId);
        assertEqualEquipments(equipment, ret);

        ElementRadioConfiguration retElement2dot4RadioConfig = ((ApElementConfiguration)ret.getDetails()).getRadioMap().get(RadioType.is2dot4GHz);
        assertEquals(retElement2dot4RadioConfig.getChannelNumber().intValue(), 6);

        //Update success
        retElement2dot4RadioConfig.setChannelNumber(1);
        retElement2dot4RadioConfig.setManualChannelNumber(2);
        retElement2dot4RadioConfig.setBackupChannelNumber(3);
        retElement2dot4RadioConfig.setManualBackupChannelNumber(4);

        Equipment updatedEquipment = equipmentPortalController.updateEquipment(ret);

        Equipment retUpdate = equipmentPortalController.getEquipment(ret.getId());
        assertEqualEquipments(retUpdate, updatedEquipment);
        ElementRadioConfiguration ret2Element2dot4RadioConfig = ((ApElementConfiguration)retUpdate.getDetails()).getRadioMap().get(RadioType.is2dot4GHz);
        assertEquals(ret2Element2dot4RadioConfig.getChannelNumber().intValue(), 1);
        assertEquals(ret2Element2dot4RadioConfig.getManualChannelNumber().intValue(), 2);
        assertEquals(ret2Element2dot4RadioConfig.getBackupChannelNumber().intValue(), 3);
        assertEquals(ret2Element2dot4RadioConfig.getManualBackupChannelNumber().intValue(), 4);

        //Update failure
        ret2Element2dot4RadioConfig.setChannelNumber(12);
        try {
            equipmentPortalController.updateEquipment(retUpdate);
            fail("EquipmentService update channelNumber failed");
        } catch (DsDataValidationException e) {
            // pass
        }
        
        ret2Element2dot4RadioConfig.setChannelNumber(6);
        ret2Element2dot4RadioConfig.setManualChannelNumber(13);
        try {
            equipmentPortalController.updateEquipment(retUpdate);
            fail("EquipmentService update manualChannelNumber failed");
        } catch (DsDataValidationException e) {
            // pass
        }
        
        ret2Element2dot4RadioConfig.setManualChannelNumber(7);
        ret2Element2dot4RadioConfig.setBackupChannelNumber(14);
        try {
            equipmentPortalController.updateEquipment(retUpdate);
            fail("EquipmentService update backupChannelNumber failed");
        } catch (DsDataValidationException e) {
            // pass
        }
        
        ret2Element2dot4RadioConfig.setBackupChannelNumber(8);
        ret2Element2dot4RadioConfig.setManualBackupChannelNumber(15);
        try {
            equipmentPortalController.updateEquipment(retUpdate);
            fail("EquipmentService update manualBackupChannelNumber failed");
        } catch (DsDataValidationException e) {
            // pass
        }
        
        //Tolerate null now
        ret2Element2dot4RadioConfig.setManualBackupChannelNumber(null);
        equipmentPortalController.updateEquipment(retUpdate);
        
        // Clean up after test
        equipmentPortalController.deleteEquipment(equipmentId);
        locationDatastore.delete(location.getId());
    }
    
    @Test
    public void testEquipmentUpdateChannelNumValidationWithCorrection() {
        Location location1 = createLocation(2, LocationType.SITE, CountryCode.US);
        Location location2 = createLocation(2, LocationType.BUILDING, CountryCode.SA);
        
        
        //Create new Equipment - success
        Equipment equipment = new Equipment();
        equipment.setName("testName-"+equipmentIncrementer.getAndIncrement());
        equipment.setInventoryId("test-inv-"+equipmentIncrementer.getAndIncrement());
        equipment.setEquipmentType(EquipmentType.AP);
        equipment.setLocationId(location1.getId());
        
        ApElementConfiguration apConfig = ApElementConfiguration.createWithDefaults();
        if (apConfig.getElementRadioConfiguration(RadioType.is5GHz) == null) {
            Map<RadioType, ElementRadioConfiguration> radioMapInit = apConfig.getRadioMap();
            Map<RadioType, RadioConfiguration> advRadioMapInit = apConfig.getAdvancedRadioMap();
            radioMapInit.put(RadioType.is5GHz, ElementRadioConfiguration.createWithDefaults(RadioType.is5GHz));
            apConfig.setRadioMap(radioMapInit);
            advRadioMapInit.put(RadioType.is5GHz, RadioConfiguration.createWithDefaults(RadioType.is5GHz));
            apConfig.setAdvancedRadioMap(advRadioMapInit);
        }
        
        equipment.setDetails(apConfig);

        ElementRadioConfiguration element5GRadioConfig = ((ApElementConfiguration)equipment.getDetails()).getRadioMap().get(RadioType.is5GHz);       
        element5GRadioConfig.setAllowedChannelsPowerLevels(new HashSet<>());
        
        for (int i = 36; i <= 64; i=i+4) {
            ChannelPowerLevel cpl = new ChannelPowerLevel();
            cpl.setChannelNumber(i);
            cpl.setChannelWidth(20);
            cpl.setDfs(false);
            cpl.setPowerLevel(30);
            element5GRadioConfig.getAllowedChannelsPowerLevels().add(cpl);
        }
        for (int i = 149; i <= 161; i=i+4) {
            ChannelPowerLevel cpl = new ChannelPowerLevel();
            cpl.setChannelNumber(i);
            cpl.setChannelWidth(20);
            cpl.setDfs(false);
            cpl.setPowerLevel(30);
            element5GRadioConfig.getAllowedChannelsPowerLevels().add(cpl);
        }

        Equipment ret = equipmentPortalController.createEquipment(equipment);
        assertNotNull(ret);
        long equipmentId = ret.getId();

        ret = equipmentPortalController.getEquipment(equipmentId);
        assertEqualEquipments(equipment, ret);

        ElementRadioConfiguration retElement5GRadioConfig = ((ApElementConfiguration)ret.getDetails()).getRadioMap().get(RadioType.is5GHz);
        assertEquals(retElement5GRadioConfig.getChannelNumber().intValue(), ElementRadioConfiguration.DEFAULT_CHANNEL_NUMBER_5GHZ);
        assertEquals(retElement5GRadioConfig.getBackupChannelNumber().intValue(), ElementRadioConfiguration.DEFAULT_BACKUP_CHANNEL_NUMBER_5GHZ);

        //Update success
        retElement5GRadioConfig.setChannelNumber(149);
        retElement5GRadioConfig.setManualChannelNumber(52);
        retElement5GRadioConfig.setBackupChannelNumber(157);
        retElement5GRadioConfig.setManualBackupChannelNumber(60);

        Equipment updatedEquipment = equipmentPortalController.updateEquipment(ret);

        Equipment retUpdate = equipmentPortalController.getEquipment(ret.getId());
        assertEqualEquipments(retUpdate, updatedEquipment);
        ElementRadioConfiguration ret2Element5GRadioConfig = ((ApElementConfiguration)retUpdate.getDetails()).getRadioMap().get(RadioType.is5GHz);
        assertEquals(ret2Element5GRadioConfig.getChannelNumber().intValue(), 149);
        assertEquals(ret2Element5GRadioConfig.getManualChannelNumber().intValue(), 52);
        assertEquals(ret2Element5GRadioConfig.getBackupChannelNumber().intValue(), 157);
        assertEquals(ret2Element5GRadioConfig.getManualBackupChannelNumber().intValue(), 60);

        //Update failure
        ret2Element5GRadioConfig.setChannelNumber(165);
        try {
            equipmentPortalController.updateEquipment(retUpdate);
            fail("EquipmentService update channelNumber failed");
        } catch (DsDataValidationException e) {
            // pass
        }
        
        ret2Element5GRadioConfig.setChannelNumber(157);
        ret2Element5GRadioConfig.setManualChannelNumber(165);
        try {
            equipmentPortalController.updateEquipment(retUpdate);
            fail("EquipmentService update manualChannelNumber failed");
        } catch (DsDataValidationException e) {
            // pass
        }
        
        ret2Element5GRadioConfig.setManualChannelNumber(44);
        ret2Element5GRadioConfig.setBackupChannelNumber(14);
        try {
            equipmentPortalController.updateEquipment(retUpdate);
            fail("EquipmentService update backupChannelNumber failed");
        } catch (DsDataValidationException e) {
            // pass
        }
        
        ret2Element5GRadioConfig.setBackupChannelNumber(36);
        ret2Element5GRadioConfig.setManualBackupChannelNumber(15);
        try {
            equipmentPortalController.updateEquipment(retUpdate);
            fail("EquipmentService update manualBackupChannelNumber failed");
        } catch (DsDataValidationException e) {
            // pass
        }
        
        ret2Element5GRadioConfig.setManualBackupChannelNumber(52);
        equipmentPortalController.updateEquipment(retUpdate);
        
        Equipment ret2Update = equipmentPortalController.getEquipment(ret.getId());
        ElementRadioConfiguration ret3Element5GRadioConfig = ((ApElementConfiguration)ret2Update.getDetails()).getRadioMap().get(RadioType.is5GHz);
        assertEquals(ret3Element5GRadioConfig.getChannelNumber().intValue(), 157);
        assertEquals(ret3Element5GRadioConfig.getManualChannelNumber().intValue(), 44);
        assertEquals(ret3Element5GRadioConfig.getBackupChannelNumber().intValue(), 36);
        assertEquals(ret3Element5GRadioConfig.getManualBackupChannelNumber().intValue(), 52);
        
        // CHANGE Country Code
        ret2Update.setLocationId(location2.getId());
        Equipment ret3Update = equipmentPortalController.updateEquipment(ret2Update);

        ElementRadioConfiguration ret4Element5GRadioConfig = ((ApElementConfiguration) ret3Update.getDetails())
                .getRadioMap().get(RadioType.is5GHz);
        // These are auto-corrected values
        assertEquals(ret4Element5GRadioConfig.getChannelNumber().intValue(), ElementRadioConfiguration.DEFAULT_CHANNEL_NUMBER_5GHZ);
        assertEquals(ret4Element5GRadioConfig.getManualChannelNumber().intValue(), ElementRadioConfiguration.DEFAULT_CHANNEL_NUMBER_5GHZ);
        assertEquals(ret4Element5GRadioConfig.getBackupChannelNumber().intValue(), ElementRadioConfiguration.DEFAULT_BACKUP_CHANNEL_NUMBER_5GHZ);
        assertEquals(ret4Element5GRadioConfig.getManualBackupChannelNumber().intValue(), ElementRadioConfiguration.DEFAULT_BACKUP_CHANNEL_NUMBER_5GHZ);

        for (int i = 149; i <= 161; i = i + 4) {
            ChannelPowerLevel cpl = new ChannelPowerLevel();
            cpl.setChannelNumber(i);
            cpl.setChannelWidth(20);
            cpl.setDfs(false);
            cpl.setPowerLevel(30);
            ret4Element5GRadioConfig.getAllowedChannelsPowerLevels().remove(cpl);
        }

        Equipment ret5Update = equipmentPortalController.updateEquipment(ret3Update);
        ElementRadioConfiguration ret5Element5GRadioConfig = ((ApElementConfiguration) ret5Update.getDetails())
                .getRadioMap().get(RadioType.is5GHz);
        assertEquals(ret5Element5GRadioConfig.getChannelNumber().intValue(), ElementRadioConfiguration.DEFAULT_CHANNEL_NUMBER_5GHZ);
        assertEquals(ret5Element5GRadioConfig.getManualChannelNumber().intValue(), ElementRadioConfiguration.DEFAULT_CHANNEL_NUMBER_5GHZ);
        assertEquals(ret5Element5GRadioConfig.getBackupChannelNumber().intValue(), ElementRadioConfiguration.DEFAULT_BACKUP_CHANNEL_NUMBER_5GHZ);
        assertEquals(ret5Element5GRadioConfig.getManualBackupChannelNumber().intValue(), ElementRadioConfiguration.DEFAULT_BACKUP_CHANNEL_NUMBER_5GHZ);

        // Clean up after test
        equipmentPortalController.deleteEquipment(equipmentId);
        locationDatastore.delete(location1.getId());
        locationDatastore.delete(location2.getId());
    }
    
    private void assertEqualEquipments(
            Equipment expected,
            Equipment actual) {
        
        assertEquals(expected.getName(), actual.getName());
        //TODO: add more fields to check here
    }
}
