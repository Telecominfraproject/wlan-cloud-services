package com.telecominfraproject.wlan.firmware;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
import com.telecominfraproject.wlan.core.model.scheduler.DailyTimeRangeSchedule;
import com.telecominfraproject.wlan.datastore.exceptions.DsConcurrentModificationException;
import com.telecominfraproject.wlan.datastore.exceptions.DsDuplicateEntityException;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;
import com.telecominfraproject.wlan.firmware.models.CustomerFirmwareTrackRecord;
import com.telecominfraproject.wlan.firmware.models.CustomerFirmwareTrackSettings.TrackFlag;
import com.telecominfraproject.wlan.firmware.models.FirmwareTrackAssignmentDetails;
import com.telecominfraproject.wlan.firmware.models.FirmwareTrackAssignmentRecord;
import com.telecominfraproject.wlan.firmware.models.FirmwareTrackRecord;
import com.telecominfraproject.wlan.firmware.models.FirmwareVersion;
import com.telecominfraproject.wlan.firmware.models.ValidationMethod;
import com.telecominfraproject.wlan.remote.tests.BaseRemoteTest;

/**
 * @author dtoptygin
 *
 */
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = {
        "integration_test",
        "no_ssl","http_digest_auth","rest-template-single-user-per-service-digest-auth",
        }) //NOTE: these profiles will be ADDED to the list of active profiles  
public class FirmwareServiceRemoteTest extends BaseRemoteTest {

    
    @Autowired FirmwareServiceRemote remoteInterface; 
    
    @Before public void urlSetup(){
        configureBaseUrl("tip.wlan.firmwareServiceBaseUrl");
    }
    
    
    @Test
    public void testCreateUpdateDeleteFirmwareVersion() {

        // CREATE test
        FirmwareVersion firmwareVersion = new FirmwareVersion();
        firmwareVersion.setEquipmentType(EquipmentType.AP);
        firmwareVersion.setVersionName("vDAOTest123");
        firmwareVersion.setCommit("abc");
        firmwareVersion.setDescription("this is the description");
        firmwareVersion.setModelId("model1");
        firmwareVersion.setFilename("filename1");
        firmwareVersion.setValidationMethod(ValidationMethod.MD5_CHECKSUM);
        firmwareVersion.setValidationCode("check1");
        firmwareVersion.setReleaseDate(System.currentTimeMillis());

        FirmwareVersion ret = remoteInterface.createFirmwareVersion(firmwareVersion);
        // check that id got auto-assigned
        assertTrue(ret.getId() > 0);

        // GET by Id test
        ret = remoteInterface.getFirmwareVersion(ret.getId());
        assertFieldsEqual(firmwareVersion, ret);

        // GET by name test
        ret = remoteInterface.getFirmwareVersionByName(firmwareVersion.getVersionName());
        assertFieldsEqual(firmwareVersion, ret);

        FirmwareVersion firmwareVersion2 = firmwareVersion.clone();
        firmwareVersion2.setEquipmentType(EquipmentType.AP);
        firmwareVersion2.setVersionName("vDAOTest4001");
        firmwareVersion2.setCommit("abc");
        firmwareVersion2.setDescription("this is the description 400");
        FirmwareVersion ret2 = remoteInterface.createFirmwareVersion(firmwareVersion2);
        // check that id got auto-assigned
        assertTrue(ret2.getId() > 0);

        // GET all
        List<FirmwareVersion> results = remoteInterface.getAllFirmwareVersionsByEquipmentType(EquipmentType.AP, null);
        assertTrue(results.contains(ret));
        assertTrue(results.contains(ret2));


        // UPDATE test - success
        firmwareVersion = ret;
        // add more fields from FirmwareVersion to modify in here
        firmwareVersion.setEquipmentType(EquipmentType.AP);
        firmwareVersion.setVersionName(firmwareVersion.getVersionName() + "_modified");
        firmwareVersion.setDescription(firmwareVersion.getDescription() + "_modified");
        firmwareVersion.setFilename(firmwareVersion.getFilename() + "_modified");
        firmwareVersion.setValidationCode(firmwareVersion.getValidationCode() + "_modified");
        firmwareVersion.setReleaseDate(System.currentTimeMillis());

        ret = remoteInterface.updateFirmwareVersion(firmwareVersion);
        assertFieldsEqual(firmwareVersion, ret);

        // UPDATE test - fail because of concurrent modification exception
        try {
            FirmwareVersion firmwareVersionConcurrentUpdate = firmwareVersion.clone();
            firmwareVersionConcurrentUpdate
                    .setVersionName(firmwareVersionConcurrentUpdate.getVersionName() + "_modified_2");
            firmwareVersionConcurrentUpdate.setFilename(firmwareVersionConcurrentUpdate.getFilename() + "_modified_2");
            firmwareVersionConcurrentUpdate
                    .setValidationCode(firmwareVersionConcurrentUpdate.getValidationCode() + "_modified_2");
            remoteInterface.updateFirmwareVersion(firmwareVersionConcurrentUpdate);
            fail("failed to detect concurrent modification");
        } catch (DsConcurrentModificationException e) {
            // expected it
        }

        // read fresh FirmwareVersion and re-verify its content
        ret = remoteInterface.getFirmwareVersion(ret.getId());
        assertFieldsEqual(firmwareVersion, ret);

        // DELETE Test
        ret = remoteInterface.deleteFirmwareVersion(ret.getId());
        assertFieldsEqual(firmwareVersion, ret);

        try {
            remoteInterface.getFirmwareVersion(ret.getId());
            fail("failed to delete FirmwareVersion");
        } catch (DsEntityNotFoundException e) {
            // expected it
        }

    }

    @Test
    public void testCreateUpdateDeleteFirmwareTrack() {
        // CREATE test
        String firmwareTrackName = "testCreateUpdateDeleteFirmwareTrack1";
        FirmwareTrackRecord res = remoteInterface
                .createFirmwareTrack(new FirmwareTrackRecord(firmwareTrackName));
        assertEquals(firmwareTrackName, res.getTrackName());
        assertTrue(res.getRecordId() >= 0);

        String firmwareTrackName2 = "testTrack2";
        res = remoteInterface.createFirmwareTrack(new FirmwareTrackRecord(firmwareTrackName2));
        assertEquals(firmwareTrackName2, res.getTrackName());
        assertTrue(res.getRecordId() >= 1);

        // Unique constraint
        try {
            res = remoteInterface.createFirmwareTrack(new FirmwareTrackRecord(firmwareTrackName2));
            fail("Expected DsDuplicateEntityException");
        } catch (DsDuplicateEntityException e) {
        }

        // Get test
        FirmwareTrackRecord res2 = remoteInterface.getFirmwareTrackById(res.getRecordId());
        assertEquals(res, res2);

        res2 = remoteInterface.getFirmwareTrackByName(res.getTrackName());
        assertEquals(res, res2);

        // Update test
        FirmwareTrackRecord updatedRecord = res.clone();
        updatedRecord.setTrackName(updatedRecord.getTrackName() + "_modified");
        updatedRecord.setMaintenanceWindow(new DailyTimeRangeSchedule(null, LocalTime.of(1, 30), LocalTime.of(3, 0)));
        res = remoteInterface.updateFirmwareTrack(updatedRecord);
        assertEquals(updatedRecord.getTrackName(), res.getTrackName());
        assertEquals(updatedRecord.getRecordId(), res.getRecordId());
        assertEquals(updatedRecord.getMaintenanceWindow(), res.getMaintenanceWindow());

        // Try again - concurrent modification exception
        updatedRecord.setTrackName(updatedRecord.getTrackName() + "_modified2");
        try {
            res = remoteInterface.updateFirmwareTrack(updatedRecord);
            fail("Expected DsConcurrentModificationException");
        } catch (DsConcurrentModificationException e) {

        }

        // Delete test
        res2 = remoteInterface.deleteFirmwareTrackRecord(res.getRecordId());
        assertEquals(res, res2);
        try {
            res2 = remoteInterface.getFirmwareTrackById(res.getRecordId());
            fail("Expected DsEntityNotFoundException");
        } catch (DsEntityNotFoundException e) {
        }

        try {
            res2 = remoteInterface.getFirmwareTrackByName(res.getTrackName());
            fail("Expected DsEntityNotFoundException");
        } catch (DsEntityNotFoundException e) {
        }

    }

    @Test
    public void testCreateUpdateDeleteCustomerFirmwareTrackAssignment() {
        // CREATE test
        String firmwareTrackName = "testCreateUpdateDeleteCustomerFirmwareTrackAssignment1";
        FirmwareTrackRecord track1 = remoteInterface
                .createFirmwareTrack(new FirmwareTrackRecord(firmwareTrackName));

        String firmwareTrackName2 = "testCreateUpdateDeleteCustomerFirmwareTrackAssignment2";
        FirmwareTrackRecord track2 = remoteInterface
                .createFirmwareTrack(new FirmwareTrackRecord(firmwareTrackName2));

        int customerId = 1100000;
        CustomerFirmwareTrackRecord record = new CustomerFirmwareTrackRecord(track1.getRecordId(), customerId);
        record.getSettings().setAutoUpgradeDeprecatedOnBind(TrackFlag.ALWAYS);
        CustomerFirmwareTrackRecord res = remoteInterface.createCustomerFirmwareTrackRecord(record);
        assertEquals(record.getSettings().getAutoUpgradeDeprecatedOnBind(),
                res.getSettings().getAutoUpgradeDeprecatedOnBind());
        assertEquals(record.getTrackRecordId(), res.getTrackRecordId());
        assertEquals(record.getCustomerId(), res.getCustomerId());

        // Test unique constraint
        try {
            remoteInterface.createCustomerFirmwareTrackRecord(record);
            fail("Expected exception");
        } catch (Exception e) {

        }
        // Test unique constraint
        try {
            CustomerFirmwareTrackRecord record2 = new CustomerFirmwareTrackRecord(track2.getRecordId(), customerId);
            remoteInterface.createCustomerFirmwareTrackRecord(record2);
            fail("Expected exception");
        } catch (Exception e) {

        }

        record = res;
        res = remoteInterface.getCustomerFirmwareTrackRecord(record.getCustomerId());
        assertEquals(record, res);

        // Update test
        CustomerFirmwareTrackRecord updatedRecord = res.clone();
        updatedRecord.getSettings().setAutoUpgradeDeprecatedOnBind(TrackFlag.NEVER);
        updatedRecord.getSettings().setAutoUpgradeUnknownDuringMaintenance(TrackFlag.ALWAYS);
        res = remoteInterface.updateCustomerFirmwareTrackRecord(updatedRecord);
        assertEquals(updatedRecord.getSettings(), res.getSettings());
        assertEquals(updatedRecord.getTrackRecordId(), res.getTrackRecordId());
        assertEquals(updatedRecord.getCustomerId(), res.getCustomerId());

        // Try again - concurrent modification exception
        updatedRecord.getSettings().setAutoUpgradeDeprecatedOnBind(TrackFlag.DEFAULT);
        try {
            res = remoteInterface.updateCustomerFirmwareTrackRecord(updatedRecord);
            fail("Expected DsConcurrentModificationException");
        } catch (DsConcurrentModificationException e) {

        }

        // Delete test
        record = res;
        res = remoteInterface.deleteCustomerFirmwareTrackRecord(record.getCustomerId());
        assertEquals(record, res);
        res = remoteInterface.getCustomerFirmwareTrackRecord(record.getCustomerId());
        assertNull(res);

        // Cascade Delete test
        record = record.clone();
        record = remoteInterface.createCustomerFirmwareTrackRecord(record);
        res = remoteInterface.getCustomerFirmwareTrackRecord(record.getCustomerId());
        assertEquals(record, res);
        remoteInterface.deleteFirmwareTrackRecord(record.getTrackRecordId());
        res = remoteInterface.getCustomerFirmwareTrackRecord(record.getCustomerId());
        assertNull(res);
        
    }

    @Test
    public void testCreateUpdateDeleteFirmwareTrackAssignment() {
        String firmwareTrackName = "testCreateUpdateDeleteFirmwareTrackAssignment1";
        FirmwareTrackRecord firmwareTrackRecord = remoteInterface
                .createFirmwareTrack(new FirmwareTrackRecord(firmwareTrackName));
        FirmwareVersion firmwareVersionRecord = createFirmwareVersion("DAOTestModel1234");

        // CREATE test
        FirmwareTrackAssignmentRecord assignmentRecord = new FirmwareTrackAssignmentRecord(
                firmwareTrackRecord.getRecordId(), firmwareVersionRecord.getId());
        FirmwareTrackAssignmentDetails assignmentDetails = new FirmwareTrackAssignmentDetails(assignmentRecord, firmwareVersionRecord);
		FirmwareTrackAssignmentDetails res = remoteInterface
                .updateFirmwareTrackAssignment(assignmentDetails );
        assertEquals(assignmentDetails.getTrackRecordId(), res.getTrackRecordId());
        assertEquals(assignmentDetails.getFirmwareVersionRecordId(), res.getFirmwareVersionRecordId());
        assertEquals(assignmentDetails.isDefaultRevisionForTrack(), res.isDefaultRevisionForTrack());
        assertEquals(assignmentDetails.isDeprecated(), res.isDeprecated());

        // Update test
        assignmentDetails = res.clone();
        assignmentDetails.setDeprecated(true);
        assignmentDetails.setDefaultRevisionForTrack(true);
        res = remoteInterface.updateFirmwareTrackAssignment(assignmentDetails);
        assertEquals(assignmentDetails.getTrackRecordId(), res.getTrackRecordId());
        assertEquals(assignmentDetails.getFirmwareVersionRecordId(), res.getFirmwareVersionRecordId());
        assertEquals(assignmentDetails.isDefaultRevisionForTrack(), res.isDefaultRevisionForTrack());
        assertEquals(assignmentDetails.isDeprecated(), res.isDeprecated());

        // Update test - concurrent modification exception
        try {
            res = remoteInterface.updateFirmwareTrackAssignment(assignmentDetails);
            fail("Expected DsConcurrentModificationException");
        } catch (DsConcurrentModificationException e) {

        }

        // Delete test
        FirmwareTrackAssignmentRecord res2 = remoteInterface
                .deleteFirmwareTrackAssignment(res.getTrackRecordId(), res.getFirmwareVersionRecordId());
        assertEquals(res, res2);
        List<FirmwareTrackAssignmentDetails> assignmentsAfterDelete = remoteInterface.getFirmwareTrackAssignments(firmwareTrackName);
        assertTrue(!assignmentsAfterDelete.contains(res));

    }

    @Test
    public void testGetAllFirmwareVersionForTrack() throws Exception {
        String trackName1 = "testGetAllFirmwareVersionForTrack1";
        String trackName2 = "testGetAllFirmwareVersionForTrack2";
        String trackName3 = "testGetAllFirmwareVersionForTrack3";
        FirmwareTrackRecord trackRec1 = remoteInterface
                .createFirmwareTrack(new FirmwareTrackRecord(trackName1));
        FirmwareTrackRecord trackRec2 = remoteInterface
                .createFirmwareTrack(new FirmwareTrackRecord(trackName2));
        remoteInterface.createFirmwareTrack(new FirmwareTrackRecord(trackName3));

        FirmwareVersion versionRec1 = createFirmwareVersion("0.9.1");
        FirmwareVersion versionRec2 = createFirmwareVersion("0.9.2");
        FirmwareVersion versionRec3 = createFirmwareVersion("0.9.3");
        FirmwareVersion versionRec4 = createFirmwareVersion("0.9.mitel.1");

        // Put all 0.9 versions in track 1 with 0.9.2 being the default for the
        // track
        Set<String> versionsInTrack1 = new HashSet<>();
        FirmwareTrackAssignmentRecord assignmentRecord = new FirmwareTrackAssignmentRecord(trackRec1.getRecordId(),
                versionRec1.getId());
        
        FirmwareTrackAssignmentDetails assignmentDetails = new FirmwareTrackAssignmentDetails(assignmentRecord, versionRec1);

        versionsInTrack1.add(versionRec1.getVersionName());
        remoteInterface.updateFirmwareTrackAssignment(assignmentDetails);
        assignmentRecord = new FirmwareTrackAssignmentRecord(trackRec1.getRecordId(), versionRec2.getId());
        assignmentRecord.setDefaultRevisionForTrack(true);
        versionsInTrack1.add(versionRec2.getVersionName());
        assignmentDetails = new FirmwareTrackAssignmentDetails(assignmentRecord, versionRec2);
        FirmwareTrackAssignmentDetails defaultAssignmentRecord_track1 = remoteInterface.updateFirmwareTrackAssignment(assignmentDetails);
        assignmentRecord = new FirmwareTrackAssignmentRecord(trackRec1.getRecordId(), versionRec3.getId());
        versionsInTrack1.add(versionRec3.getVersionName());
        assignmentDetails = new FirmwareTrackAssignmentDetails(assignmentRecord, versionRec3);
        remoteInterface.updateFirmwareTrackAssignment(assignmentDetails);

        // Put 0.9.2 and mitel in the second track
        Set<String> versionsInTrack2 = new HashSet<>();
        assignmentRecord = new FirmwareTrackAssignmentRecord(trackRec2.getRecordId(), versionRec2.getId());
        assignmentDetails = new FirmwareTrackAssignmentDetails(assignmentRecord, versionRec2);
        versionsInTrack2.add(versionRec2.getVersionName());
        FirmwareTrackAssignmentDetails defaultAssignmentRecord_track2 = remoteInterface.updateFirmwareTrackAssignment(assignmentDetails);
        assignmentRecord = new FirmwareTrackAssignmentRecord(trackRec2.getRecordId(), versionRec4.getId());
        assignmentRecord.setDefaultRevisionForTrack(true);
        assignmentDetails = new FirmwareTrackAssignmentDetails(assignmentRecord, versionRec4);
        versionsInTrack2.add(versionRec4.getVersionName());
        FirmwareTrackAssignmentDetails nonDefaultAssignmentRecord_track2 = remoteInterface.updateFirmwareTrackAssignment(assignmentDetails);

        List<FirmwareTrackAssignmentDetails> detailsList = remoteInterface.getFirmwareTrackAssignments(trackName1);
        assertEquals(versionsInTrack1.size(), detailsList.size());
        Set<String> copy = new HashSet<>(versionsInTrack1);
        for (FirmwareTrackAssignmentDetails v : detailsList) {
            assertTrue(copy.contains(v.getVersionName()));
            copy.remove(v.getVersionName());
        }

        detailsList = remoteInterface.getFirmwareTrackAssignments(trackName2);
        assertEquals(versionsInTrack2.size(), detailsList.size());
        copy = new HashSet<>(versionsInTrack2);
        for (FirmwareTrackAssignmentDetails v : detailsList) {
            assertTrue(copy.contains(v.getVersionName()));
            copy.remove(v.getVersionName());
        }

        assertTrue(detailsList.contains(defaultAssignmentRecord_track2));
        assertTrue(detailsList.contains(nonDefaultAssignmentRecord_track2));

        detailsList = remoteInterface.getFirmwareTrackAssignments(trackName3);
        assertTrue(detailsList.isEmpty());
        
    }
    
    @Test
    public void testgetAllFirmwareModelIdsByEquipmentType() {

        FirmwareVersion firmwareVersion1 = new FirmwareVersion();
        firmwareVersion1.setEquipmentType(EquipmentType.AP);
        firmwareVersion1.setVersionName("fvDAOTest123_1");
        firmwareVersion1.setCommit("abc");
        firmwareVersion1.setDescription("this is the description");
        firmwareVersion1.setModelId("test_all_fw_models_model1");
        firmwareVersion1.setFilename("filename1");
        firmwareVersion1.setValidationMethod(ValidationMethod.MD5_CHECKSUM);
        firmwareVersion1.setValidationCode("check1");
        firmwareVersion1.setReleaseDate(System.currentTimeMillis());

        firmwareVersion1 = remoteInterface.createFirmwareVersion(firmwareVersion1);
        
        FirmwareVersion firmwareVersion2 = new FirmwareVersion();
        firmwareVersion2.setEquipmentType(EquipmentType.AP);
        firmwareVersion2.setVersionName("fvDAOTest123_2");
        firmwareVersion2.setCommit("abc");
        firmwareVersion2.setDescription("this is the description");
        firmwareVersion2.setModelId("test_all_fw_models_model1");
        firmwareVersion2.setFilename("filename2");
        firmwareVersion2.setValidationMethod(ValidationMethod.MD5_CHECKSUM);
        firmwareVersion2.setValidationCode("check1");
        firmwareVersion2.setReleaseDate(System.currentTimeMillis());

        firmwareVersion2 = remoteInterface.createFirmwareVersion(firmwareVersion2);

        FirmwareVersion firmwareVersion3 = new FirmwareVersion();
        firmwareVersion3.setEquipmentType(EquipmentType.AP);
        firmwareVersion3.setVersionName("fvDAOTest123_3");
        firmwareVersion3.setCommit("abc");
        firmwareVersion3.setDescription("this is the description");
        firmwareVersion3.setModelId("test_all_fw_models_model2");
        firmwareVersion3.setFilename("filename1");
        firmwareVersion3.setValidationMethod(ValidationMethod.MD5_CHECKSUM);
        firmwareVersion3.setValidationCode("check1");
        firmwareVersion3.setReleaseDate(System.currentTimeMillis());

        firmwareVersion3 = remoteInterface.createFirmwareVersion(firmwareVersion3);

        FirmwareVersion firmwareVersion4 = new FirmwareVersion();
        firmwareVersion4.setEquipmentType(EquipmentType.SWITCH);
        firmwareVersion4.setVersionName("fvDAOTest123_4");
        firmwareVersion4.setCommit("abc");
        firmwareVersion4.setDescription("this is the description");
        firmwareVersion4.setModelId("test_all_fw_models_model3");
        firmwareVersion4.setFilename("filename1");
        firmwareVersion4.setValidationMethod(ValidationMethod.MD5_CHECKSUM);
        firmwareVersion4.setValidationCode("check1");
        firmwareVersion4.setReleaseDate(System.currentTimeMillis());

        firmwareVersion4 = remoteInterface.createFirmwareVersion(firmwareVersion4);

        List<FirmwareVersion> allAPVersions = new ArrayList<>();
        allAPVersions.add(firmwareVersion1);
        allAPVersions.add(firmwareVersion2);
        allAPVersions.add(firmwareVersion3);
        
        Set<String> allModelIds = new HashSet<>();
        
        allAPVersions.forEach(v -> allModelIds.add(v.getModelId()));
        
        Set<String> retrievedModelIds = new HashSet<>();
        retrievedModelIds.addAll(remoteInterface.getAllFirmwareModelIdsByEquipmentType(EquipmentType.AP));
        
        assertTrue(retrievedModelIds.containsAll(allModelIds));
        
        retrievedModelIds.clear();
        remoteInterface.getAllFirmwareVersionsByEquipmentType(EquipmentType.AP, null).forEach(v -> retrievedModelIds.add(v.getModelId()));
        assertTrue(retrievedModelIds.containsAll(allModelIds));

        List<FirmwareVersion> retrievedVersions = remoteInterface.getAllFirmwareVersionsByEquipmentType(EquipmentType.AP, firmwareVersion1.getModelId());
        assertEquals(2, retrievedVersions.size());
        assertTrue(retrievedVersions.contains(firmwareVersion1));
        assertTrue(retrievedVersions.contains(firmwareVersion2));

        //clean up
        allAPVersions.forEach(v -> remoteInterface.deleteFirmwareVersion(v.getId()));
        remoteInterface.deleteFirmwareVersion(firmwareVersion4.getId());
        
    }

    private FirmwareVersion createFirmwareVersion(String version) {
        FirmwareVersion firmwareVersion = new FirmwareVersion();
        firmwareVersion.setEquipmentType(EquipmentType.AP);
        firmwareVersion.setVersionName(version);
        firmwareVersion.setCommit("abc");
        firmwareVersion.setDescription("this is the description");
        firmwareVersion.setModelId("model1");
        firmwareVersion.setFilename("filename1");
        firmwareVersion.setValidationMethod(ValidationMethod.MD5_CHECKSUM);
        firmwareVersion.setValidationCode("check1");
        firmwareVersion.setReleaseDate(System.currentTimeMillis());
        return remoteInterface.createFirmwareVersion(firmwareVersion);
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
