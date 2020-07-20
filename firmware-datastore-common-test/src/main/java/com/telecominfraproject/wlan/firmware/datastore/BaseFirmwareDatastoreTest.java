package com.telecominfraproject.wlan.firmware.datastore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

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

public abstract class BaseFirmwareDatastoreTest {

    @Autowired
    private FirmwareDatastore firmwareDatastore;

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

        FirmwareVersion ret = firmwareDatastore.create(firmwareVersion);
        // check that id got auto-assigned
        assertTrue(ret.getId() > 0);

        // GET by Id test
        ret = firmwareDatastore.get(ret.getId());
        assertFieldsEqual(firmwareVersion, ret);

        // GET by name test
        ret = firmwareDatastore.getByNameOrNull(firmwareVersion.getVersionName());
        assertFieldsEqual(firmwareVersion, ret);

        ret = firmwareDatastore.getByNameOrNull("non-existent");
        assertNull(ret);

        ret = firmwareDatastore.getByName(firmwareVersion.getVersionName());
        assertFieldsEqual(firmwareVersion, ret);

        FirmwareVersion firmwareVersion2 = firmwareVersion.clone();
        firmwareVersion2.setEquipmentType(EquipmentType.AP);
        firmwareVersion2.setVersionName("vDAOTest4001");
        firmwareVersion2.setCommit("abc");
        firmwareVersion2.setDescription("this is the description 400");
        FirmwareVersion ret2 = firmwareDatastore.create(firmwareVersion2);
        // check that id got auto-assigned
        assertTrue(ret2.getId() > 0);

        // GET all
        Map<EquipmentType, List<FirmwareVersion>> results = firmwareDatastore.getAllGroupedByEquipmentType();
        assertTrue(results.get(EquipmentType.AP).contains(ret));
        assertTrue(results.get(EquipmentType.AP).contains(ret2));

        // UPDATE test - success
        firmwareVersion = ret;
        // add more fields from FirmwareVersion to modify in here
        firmwareVersion.setEquipmentType(EquipmentType.AP);
        firmwareVersion.setModelId(firmwareVersion.getModelId() + "_modified");
        firmwareVersion.setVersionName(firmwareVersion.getVersionName() + "_modified");
        firmwareVersion.setDescription(firmwareVersion.getDescription() + "_modified");
        firmwareVersion.setFilename(firmwareVersion.getFilename() + "_modified");
        firmwareVersion.setValidationCode(firmwareVersion.getValidationCode() + "_modified");
        firmwareVersion.setReleaseDate(System.currentTimeMillis());

        ret = firmwareDatastore.update(firmwareVersion);
        assertFieldsEqual(firmwareVersion, ret);

        // UPDATE test - fail because of concurrent modification exception
        try {
            FirmwareVersion firmwareVersionConcurrentUpdate = firmwareVersion.clone();
            firmwareVersionConcurrentUpdate.setModelId(firmwareVersionConcurrentUpdate.getModelId() + "_modified_2");
            firmwareVersionConcurrentUpdate
                    .setVersionName(firmwareVersionConcurrentUpdate.getVersionName() + "_modified_2");
            firmwareVersionConcurrentUpdate.setFilename(firmwareVersionConcurrentUpdate.getFilename() + "_modified_2");
            firmwareVersionConcurrentUpdate
                    .setValidationCode(firmwareVersionConcurrentUpdate.getValidationCode() + "_modified_2");
            firmwareDatastore.update(firmwareVersionConcurrentUpdate);
            fail("failed to detect concurrent modification");
        } catch (DsConcurrentModificationException e) {
            // expected it
        }

        // read fresh FirmwareVersion and re-verify its content
        ret = firmwareDatastore.get(ret.getId());
        assertFieldsEqual(firmwareVersion, ret);

        // DELETE Test
        ret = firmwareDatastore.delete(ret.getId());
        assertFieldsEqual(firmwareVersion, ret);

        try {
            firmwareDatastore.get(ret.getId());
            fail("failed to delete FirmwareVersion");
        } catch (DsEntityNotFoundException e) {
            // expected it
        }

    }

    @Test
    public void testCreateUpdateDeleteFirmwareTrack() {
        // CREATE test
        String firmwareTrackName = "testCreateUpdateDeleteFirmwareTrack1";
        FirmwareTrackRecord res = firmwareDatastore
                .createFirmwareTrack(new FirmwareTrackRecord(firmwareTrackName));
        assertEquals(firmwareTrackName, res.getTrackName());
        assertTrue(res.getRecordId() >= 0);

        String firmwareTrackName2 = "testTrack2";
        res = firmwareDatastore.createFirmwareTrack(new FirmwareTrackRecord(firmwareTrackName2));
        assertEquals(firmwareTrackName2, res.getTrackName());
        assertTrue(res.getRecordId() >= 1);

        // Unique constraint
        try {
            res = firmwareDatastore.createFirmwareTrack(new FirmwareTrackRecord(firmwareTrackName2));
            fail("Expected DsDuplicateEntityException");
        } catch (DsDuplicateEntityException e) {
        }

        // Get test
        FirmwareTrackRecord res2 = firmwareDatastore.getFirmwareTrackById(res.getRecordId());
        assertEquals(res, res2);

        res2 = firmwareDatastore.getFirmwareTrackByName(res.getTrackName());
        assertEquals(res, res2);

        res2 = firmwareDatastore.getFirmwareTrackByNameOrNull(res.getTrackName());
        assertEquals(res, res2);

        // Update test
        FirmwareTrackRecord updatedRecord = res.clone();
        updatedRecord.setTrackName(updatedRecord.getTrackName() + "_modified");
        updatedRecord.setMaintenanceWindow(new DailyTimeRangeSchedule(null, LocalTime.of(1, 30), LocalTime.of(3, 0)));
        res = firmwareDatastore.updateFirmwareTrack(updatedRecord);
        assertEquals(updatedRecord.getTrackName(), res.getTrackName());
        assertEquals(updatedRecord.getRecordId(), res.getRecordId());
        assertEquals(updatedRecord.getMaintenanceWindow(), res.getMaintenanceWindow());

        // Try again - concurrent modification exception
        updatedRecord.setTrackName(updatedRecord.getTrackName() + "_modified2");
        try {
            res = firmwareDatastore.updateFirmwareTrack(updatedRecord);
            fail("Expected DsConcurrentModificationException");
        } catch (DsConcurrentModificationException e) {

        }

        // Delete test
        res2 = firmwareDatastore.deleteFirmwareTrackRecord(res.getRecordId());
        assertEquals(res, res2);
        try {
            res2 = firmwareDatastore.getFirmwareTrackById(res.getRecordId());
            fail("Expected DsEntityNotFoundException");
        } catch (DsEntityNotFoundException e) {
        }

        try {
            res2 = firmwareDatastore.getFirmwareTrackByName(res.getTrackName());
            fail("Expected DsEntityNotFoundException");
        } catch (DsEntityNotFoundException e) {
        }

        res2 = firmwareDatastore.getFirmwareTrackByNameOrNull(res.getTrackName());
        assertNull(res2);
        
    }

    @Test
    public void testCreateUpdateDeleteCustomerFirmwareTrackAssignment() {
        // CREATE test
        String firmwareTrackName = "testCreateUpdateDeleteCustomerFirmwareTrackAssignment1";
        FirmwareTrackRecord track1 = firmwareDatastore
                .createFirmwareTrack(new FirmwareTrackRecord(firmwareTrackName));

        String firmwareTrackName2 = "testCreateUpdateDeleteCustomerFirmwareTrackAssignment2";
        FirmwareTrackRecord track2 = firmwareDatastore
                .createFirmwareTrack(new FirmwareTrackRecord(firmwareTrackName2));

        int customerId = 1100000;
        CustomerFirmwareTrackRecord record = new CustomerFirmwareTrackRecord(track1.getRecordId(), customerId);
        record.getSettings().setAutoUpgradeDeprecatedOnBind(TrackFlag.ALWAYS);
        CustomerFirmwareTrackRecord res = firmwareDatastore.createCustomerFirmwareTrackRecord(record);
        assertEquals(record.getSettings().getAutoUpgradeDeprecatedOnBind(),
                res.getSettings().getAutoUpgradeDeprecatedOnBind());
        assertEquals(record.getTrackRecordId(), res.getTrackRecordId());
        assertEquals(record.getCustomerId(), res.getCustomerId());

        // Test unique constraint
        try {
            firmwareDatastore.createCustomerFirmwareTrackRecord(record);
            fail("Expected exception");
        } catch (Exception e) {

        }
        // Test unique constraint
        try {
            CustomerFirmwareTrackRecord record2 = new CustomerFirmwareTrackRecord(track2.getRecordId(), customerId);
            firmwareDatastore.createCustomerFirmwareTrackRecord(record2);
            fail("Expected exception");
        } catch (Exception e) {

        }

        record = res;
        res = firmwareDatastore.getCustomerFirmwareTrackRecord(record.getCustomerId());
        assertEquals(record, res);

        // Update test
        CustomerFirmwareTrackRecord updatedRecord = res.clone();
        updatedRecord.getSettings().setAutoUpgradeDeprecatedOnBind(TrackFlag.NEVER);
        updatedRecord.getSettings().setAutoUpgradeUnknownDuringMaintenance(TrackFlag.ALWAYS);
        res = firmwareDatastore.updateCustomerFirmwareTrackRecord(updatedRecord);
        assertEquals(updatedRecord.getSettings(), res.getSettings());
        assertEquals(updatedRecord.getTrackRecordId(), res.getTrackRecordId());
        assertEquals(updatedRecord.getCustomerId(), res.getCustomerId());

        // Try again - concurrent modification exception
        updatedRecord.getSettings().setAutoUpgradeDeprecatedOnBind(TrackFlag.DEFAULT);
        try {
            res = firmwareDatastore.updateCustomerFirmwareTrackRecord(updatedRecord);
            fail("Expected DsConcurrentModificationException");
        } catch (DsConcurrentModificationException e) {

        }

        // Delete test
        record = res;
        res = firmwareDatastore.deleteCustomerFirmwareTrackRecord(record.getCustomerId());
        assertEquals(record, res);
        res = firmwareDatastore.getCustomerFirmwareTrackRecord(record.getCustomerId());
        assertNull(res);

        // Delete by track record id test
        record = record.clone();
        record = firmwareDatastore.createCustomerFirmwareTrackRecord(record);
        res = firmwareDatastore.getCustomerFirmwareTrackRecord(record.getCustomerId());
        assertEquals(record, res);
        firmwareDatastore.deleteCustomerFirmwareTrackRecords(record.getTrackRecordId());
        res = firmwareDatastore.getCustomerFirmwareTrackRecord(record.getCustomerId());
        assertNull(res);

        // Cascade Delete test
        record = record.clone();
        record = firmwareDatastore.createCustomerFirmwareTrackRecord(record);
        res = firmwareDatastore.getCustomerFirmwareTrackRecord(record.getCustomerId());
        assertEquals(record, res);
        firmwareDatastore.deleteFirmwareTrackRecord(record.getTrackRecordId());
        res = firmwareDatastore.getCustomerFirmwareTrackRecord(record.getCustomerId());
        assertNull(res);
        
    }

    @Test
    public void testCreateUpdateDeleteFirmwareTrackAssignment() {
        String firmwareTrackName = "testCreateUpdateDeleteFirmwareTrackAssignment1";
        FirmwareTrackRecord firmwareTrackRecord = firmwareDatastore
                .createFirmwareTrack(new FirmwareTrackRecord(firmwareTrackName));
        FirmwareVersion firmwareVersionRecord = createFirmwareVersion("DAOTestModel1234");

        // CREATE test
        FirmwareTrackAssignmentRecord assignmentRecord = new FirmwareTrackAssignmentRecord(
                firmwareTrackRecord.getRecordId(), firmwareVersionRecord.getId());
        FirmwareTrackAssignmentRecord res = firmwareDatastore
                .createOrUpdateFirmwareTrackAssignment(assignmentRecord);
        assertEquals(assignmentRecord.getTrackRecordId(), res.getTrackRecordId());
        assertEquals(assignmentRecord.getFirmwareVersionRecordId(), res.getFirmwareVersionRecordId());
        assertEquals(assignmentRecord.isDefaultRevisionForTrack(), res.isDefaultRevisionForTrack());
        assertEquals(assignmentRecord.isDeprecated(), res.isDeprecated());

        // Update test
        assignmentRecord = res.clone();
        assignmentRecord.setDeprecated(true);
        assignmentRecord.setDefaultRevisionForTrack(true);
        res = firmwareDatastore.createOrUpdateFirmwareTrackAssignment(assignmentRecord);
        assertEquals(assignmentRecord.getTrackRecordId(), res.getTrackRecordId());
        assertEquals(assignmentRecord.getFirmwareVersionRecordId(), res.getFirmwareVersionRecordId());
        assertEquals(assignmentRecord.isDefaultRevisionForTrack(), res.isDefaultRevisionForTrack());
        assertEquals(assignmentRecord.isDeprecated(), res.isDeprecated());

        // Update test - concurrent modification exception
        try {
            res = firmwareDatastore.createOrUpdateFirmwareTrackAssignment(assignmentRecord);
            fail("Expected DsConcurrentModificationException");
        } catch (DsConcurrentModificationException e) {

        }

        // Delete test
        FirmwareTrackAssignmentRecord res2 = firmwareDatastore
                .deleteFirmwareTrackAssignment(res.getTrackRecordId(), res.getFirmwareVersionRecordId());
        assertEquals(res, res2);
        res2 = firmwareDatastore.getFirmwareTrackAssignmentDetails(res.getTrackRecordId(),
                res.getFirmwareVersionRecordId());
        assertNull(res2);

        res2 = firmwareDatastore.createOrUpdateFirmwareTrackAssignment(assignmentRecord);
        firmwareDatastore.deleteFirmwareTrackAssignments(res2.getTrackRecordId());
        res2 = firmwareDatastore.getFirmwareTrackAssignmentDetails(res2.getTrackRecordId(),
                res.getFirmwareVersionRecordId());
        assertNull(res2);

    }

    @Test
    public void testGetAllFirmwareVersionForTrack() throws Exception {
        String trackName1 = "testGetAllFirmwareVersionForTrack1";
        String trackName2 = "testGetAllFirmwareVersionForTrack2";
        String trackName3 = "testGetAllFirmwareVersionForTrack3";
        FirmwareTrackRecord trackRec1 = firmwareDatastore
                .createFirmwareTrack(new FirmwareTrackRecord(trackName1));
        FirmwareTrackRecord trackRec2 = firmwareDatastore
                .createFirmwareTrack(new FirmwareTrackRecord(trackName2));
        firmwareDatastore.createFirmwareTrack(new FirmwareTrackRecord(trackName3));

        FirmwareVersion versionRec1 = createFirmwareVersion("0.9.1");
        FirmwareVersion versionRec2 = createFirmwareVersion("0.9.2");
        FirmwareVersion versionRec3 = createFirmwareVersion("0.9.3");
        FirmwareVersion versionRec4 = createFirmwareVersion("0.9.mitel.1");

        // Put all 0.9 versions in track 1 with 0.9.2 being the default for the
        // track
        Set<String> versionsInTrack1 = new HashSet<>();
        FirmwareTrackAssignmentRecord assignmentRecord = new FirmwareTrackAssignmentRecord(trackRec1.getRecordId(),
                versionRec1.getId());
        versionsInTrack1.add(versionRec1.getVersionName());
        firmwareDatastore.createOrUpdateFirmwareTrackAssignment(assignmentRecord);
        assignmentRecord = new FirmwareTrackAssignmentRecord(trackRec1.getRecordId(), versionRec2.getId());
        assignmentRecord.setDefaultRevisionForTrack(true);
        versionsInTrack1.add(versionRec2.getVersionName());
        FirmwareTrackAssignmentRecord defaultAssignmentRecord_track1 = firmwareDatastore.createOrUpdateFirmwareTrackAssignment(assignmentRecord);
        assignmentRecord = new FirmwareTrackAssignmentRecord(trackRec1.getRecordId(), versionRec3.getId());
        versionsInTrack1.add(versionRec3.getVersionName());
        firmwareDatastore.createOrUpdateFirmwareTrackAssignment(assignmentRecord);

        // Put 0.9.2 and mitel in the second track
        Set<String> versionsInTrack2 = new HashSet<>();
        assignmentRecord = new FirmwareTrackAssignmentRecord(trackRec2.getRecordId(), versionRec2.getId());
        versionsInTrack2.add(versionRec2.getVersionName());
        firmwareDatastore.createOrUpdateFirmwareTrackAssignment(assignmentRecord);
        assignmentRecord = new FirmwareTrackAssignmentRecord(trackRec2.getRecordId(), versionRec4.getId());
        assignmentRecord.setDefaultRevisionForTrack(true);
        versionsInTrack2.add(versionRec4.getVersionName());
        firmwareDatastore.createOrUpdateFirmwareTrackAssignment(assignmentRecord);

        List<FirmwareTrackAssignmentDetails> detailsList = firmwareDatastore.getFirmwareTrackDetails(trackName1);
        assertEquals(versionsInTrack1.size(), detailsList.size());
        Set<String> copy = new HashSet<>(versionsInTrack1);
        for (FirmwareTrackAssignmentDetails v : detailsList) {
            assertTrue(copy.contains(v.getVersionName()));
            copy.remove(v.getVersionName());
        }

        detailsList = firmwareDatastore.getFirmwareTrackDetails(trackName2);
        assertEquals(versionsInTrack2.size(), detailsList.size());
        copy = new HashSet<>(versionsInTrack2);
        for (FirmwareTrackAssignmentDetails v : detailsList) {
            assertTrue(copy.contains(v.getVersionName()));
            copy.remove(v.getVersionName());
        }

        FirmwareTrackAssignmentDetails details = firmwareDatastore.getFirmwareTrackAssignmentDetails(
                detailsList.get(0).getTrackRecordId(), detailsList.get(0).getFirmwareVersionRecordId());
        assertEquals(detailsList.get(0), details);

        detailsList = firmwareDatastore.getFirmwareTrackDetails(trackName3);
        assertTrue(detailsList.isEmpty());

        String equipmentModel = versionRec2.getModelId();
        Map<String, FirmwareTrackAssignmentDetails> defaultTrackAssignmentsPerEquipmentModel = firmwareDatastore.getAllDefaultFirmwareTrackAssignmentDetails(trackRec1.getRecordId());
        assertEquals(1, defaultTrackAssignmentsPerEquipmentModel.size());
        assertTrue(defaultTrackAssignmentsPerEquipmentModel.containsKey(equipmentModel));
        
        FirmwareTrackAssignmentDetails trackAssignmentDetails1 =  firmwareDatastore.getDefaultFirmwareTrackAssignmentDetailsForEquipmentModel(trackRec1.getRecordId(), equipmentModel);        
        FirmwareTrackAssignmentDetails trackAssignmentDetails1_1 =  firmwareDatastore.getDefaultFirmwareTrackAssignmentDetailsForEquipmentModelByTrackName(trackRec1.getTrackName(), equipmentModel);
        assertEquals(trackAssignmentDetails1, trackAssignmentDetails1_1);
        assertEquals(trackAssignmentDetails1, defaultTrackAssignmentsPerEquipmentModel.get(equipmentModel));
        assertEquals(trackAssignmentDetails1, new FirmwareTrackAssignmentDetails(defaultAssignmentRecord_track1, versionRec2));
        
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

        firmwareVersion1 = firmwareDatastore.create(firmwareVersion1);
        
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

        firmwareVersion2 = firmwareDatastore.create(firmwareVersion2);

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

        firmwareVersion3 = firmwareDatastore.create(firmwareVersion3);

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

        firmwareVersion4 = firmwareDatastore.create(firmwareVersion4);

        List<FirmwareVersion> allAPVersions = new ArrayList<>();
        allAPVersions.add(firmwareVersion1);
        allAPVersions.add(firmwareVersion2);
        allAPVersions.add(firmwareVersion3);
        
        Set<String> allModelIds = new HashSet<>();
        
        allAPVersions.forEach(v -> allModelIds.add(v.getModelId()));
        
        Set<String> retrievedModelIds = new HashSet<>();
        retrievedModelIds.addAll(firmwareDatastore.getAllFirmwareModelIdsByEquipmentType(EquipmentType.AP));
        
        assertTrue(retrievedModelIds.containsAll(allModelIds));
        
        retrievedModelIds.clear();
        firmwareDatastore.getAllFirmwareVersionsByEquipmentType(EquipmentType.AP, null).forEach(v -> retrievedModelIds.add(v.getModelId()));
        assertTrue(retrievedModelIds.containsAll(allModelIds));

        List<FirmwareVersion> retrievedVersions = firmwareDatastore.getAllFirmwareVersionsByEquipmentType(EquipmentType.AP, firmwareVersion1.getModelId());
        assertEquals(2, retrievedVersions.size());
        assertTrue(retrievedVersions.contains(firmwareVersion1));
        assertTrue(retrievedVersions.contains(firmwareVersion2));

        //clean up
        allAPVersions.forEach(v -> firmwareDatastore.delete(v.getId()));
        firmwareDatastore.delete(firmwareVersion4.getId());
        
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
        return firmwareDatastore.create(firmwareVersion);
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
