package com.telecominfraproject.wlan.firmware;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
import com.telecominfraproject.wlan.firmware.controller.FirmwareController;
import com.telecominfraproject.wlan.firmware.models.CustomerFirmwareTrackRecord;
import com.telecominfraproject.wlan.firmware.models.CustomerFirmwareTrackSettings;
import com.telecominfraproject.wlan.firmware.models.FirmwareTrackAssignmentDetails;
import com.telecominfraproject.wlan.firmware.models.FirmwareTrackRecord;
import com.telecominfraproject.wlan.firmware.models.FirmwareVersion;

/**
 * @author dtoptygin
 *
 */
@Component
public class FirmwareServiceLocal implements FirmwareServiceInterface {

    @Autowired private FirmwareController firmwareController;
    @SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(FirmwareServiceLocal.class);
    
	public FirmwareVersion createFirmwareVersion(FirmwareVersion firmwareVersion) {
		return firmwareController.createFirmwareVersion(firmwareVersion);
	}
	public FirmwareVersion getFirmwareVersion(long firmwareVersionId) {
		return firmwareController.getFirmwareVersion(firmwareVersionId);
	}
	public FirmwareVersion getFirmwareVersionByName(String versionName) {
		return firmwareController.getFirmwareVersionByName(versionName);
	}
	public List<FirmwareVersion> getAllFirmwareVersionsByEquipmentType(EquipmentType equipmentType, String modelId) {
		return firmwareController.getAllFirmwareVersionsByEquipmentType(equipmentType, modelId);
	}
	public List<String> getAllFirmwareModelIdsByEquipmentType(EquipmentType equipmentType) {
		return firmwareController.getAllFirmwareModelIdsByEquipmentType(equipmentType);
	}
	public FirmwareVersion updateFirmwareVersion(FirmwareVersion firmwareVersion) {
		return firmwareController.updateFirmwareVersion(firmwareVersion);
	}
	public FirmwareVersion deleteFirmwareVersion(long firmwareVersionId) {
		return firmwareController.deleteFirmwareVersion(firmwareVersionId);
	}
	
	
	public FirmwareTrackRecord createFirmwareTrack(FirmwareTrackRecord firmwareTrack) {
		return firmwareController.createFirmwareTrack(firmwareTrack);
	}
	public FirmwareTrackRecord getFirmwareTrackById(long firmwareTrackId) {
		return firmwareController.getFirmwareTrackById(firmwareTrackId);
	}
	public FirmwareTrackRecord getFirmwareTrackByName(String firmwareTrackName) {
		return firmwareController.getFirmwareTrackByName(firmwareTrackName);
	}
	public FirmwareTrackRecord updateFirmwareTrack(FirmwareTrackRecord firmwareTrackRecord) {
		return firmwareController.updateFirmwareTrack(firmwareTrackRecord);
	}
	public FirmwareTrackRecord deleteFirmwareTrackRecord(long firmwareTrackId) {
		return firmwareController.deleteFirmwareTrackRecord(firmwareTrackId);
	}
	
	
	public List<FirmwareTrackAssignmentDetails> getFirmwareTrackAssignments(String firmwareTrackName) {
		return firmwareController.getFirmwareTrackAssignments(firmwareTrackName);
	}
	public FirmwareTrackAssignmentDetails updateFirmwareTrackAssignment(
			FirmwareTrackAssignmentDetails assignmentDetails) {
		return firmwareController.updateFirmwareTrackAssignment(assignmentDetails);
	}
	public FirmwareTrackAssignmentDetails deleteFirmwareTrackAssignment(long trackId, long firmwareVersionId) {
		return firmwareController.deleteFirmwareTrackAssignment(trackId, firmwareVersionId);
	}
	
	
	public CustomerFirmwareTrackSettings getDefaultCustomerTrackSetting() {
		return firmwareController.getDefaultCustomerTrackSetting();
	}
	public CustomerFirmwareTrackSettings updateDefaultCustomerTrackSetting(CustomerFirmwareTrackSettings defaultSettings) {
	    return firmwareController.updateDefaultCustomerTrackSetting(defaultSettings);
	}
	public CustomerFirmwareTrackRecord getCustomerFirmwareTrackRecord(int customerId) {
		return firmwareController.getCustomerFirmwareTrackRecord(customerId);
	}
	public CustomerFirmwareTrackRecord createCustomerFirmwareTrackRecord(CustomerFirmwareTrackRecord customerTrack) {
		return firmwareController.createCustomerFirmwareTrackRecord(customerTrack);
	}
	public CustomerFirmwareTrackRecord updateCustomerFirmwareTrackRecord(CustomerFirmwareTrackRecord customerTrack) {
		return firmwareController.updateCustomerFirmwareTrackRecord(customerTrack);
	}
	public CustomerFirmwareTrackRecord deleteCustomerFirmwareTrackRecord(int customerId) {
		return firmwareController.deleteCustomerFirmwareTrackRecord(customerId);
	}    

    
}
