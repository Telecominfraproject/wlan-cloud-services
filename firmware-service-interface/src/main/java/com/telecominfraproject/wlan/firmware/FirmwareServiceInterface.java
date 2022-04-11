package com.telecominfraproject.wlan.firmware;

import java.util.List;

import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
import com.telecominfraproject.wlan.firmware.models.CustomerFirmwareTrackRecord;
import com.telecominfraproject.wlan.firmware.models.CustomerFirmwareTrackSettings;
import com.telecominfraproject.wlan.firmware.models.FirmwareTrackAssignmentDetails;
import com.telecominfraproject.wlan.firmware.models.FirmwareTrackRecord;
import com.telecominfraproject.wlan.firmware.models.FirmwareVersion;


/**
 * @author dtoptygin
 *
 */
public interface FirmwareServiceInterface {
    
    FirmwareVersion createFirmwareVersion(FirmwareVersion firmwareVersion);

    FirmwareVersion getFirmwareVersion(long firmwareVersionId);

    FirmwareVersion getFirmwareVersionByName(String versionName);

    /**
     * @param equipmentType - filter firmware versions by equipment type
     * @param modelId - optional filter by equipment model, if null - then firmware versions for all the equipment models are returned
     * @return list of firmware versions that satisfy the supplied filters
     */
    List<FirmwareVersion> getAllFirmwareVersionsByEquipmentType(EquipmentType equipmentType, String modelId);

    FirmwareVersion updateFirmwareVersion(FirmwareVersion firmwareVersion);

    FirmwareVersion deleteFirmwareVersion(long firmwareVersionId);

    FirmwareTrackRecord createFirmwareTrack(FirmwareTrackRecord firmwareTrackRecord);

    FirmwareTrackRecord getFirmwareTrackByName(String firmwareTrackName);
  
//TODO: implement this for completeness:    
//    List<FirmwareTrackRecord> getAllFirmwareTracks();

    FirmwareTrackRecord getFirmwareTrackById(long firmwareTrackRecordId);

    FirmwareTrackRecord updateFirmwareTrack(FirmwareTrackRecord firmwareTrackRecord);

    FirmwareTrackRecord deleteFirmwareTrackRecord(long firmwareTrackRecordId);

    List<FirmwareTrackAssignmentDetails> getFirmwareTrackAssignments(String trackName);

    FirmwareTrackAssignmentDetails updateFirmwareTrackAssignment(FirmwareTrackAssignmentDetails assignmentDetails);

    FirmwareTrackAssignmentDetails deleteFirmwareTrackAssignment(long firmwareTrackRecordId, long firmwareVersionRecordId);

    CustomerFirmwareTrackRecord getCustomerFirmwareTrackRecord(int customerId);
    CustomerFirmwareTrackRecord createCustomerFirmwareTrackRecord(CustomerFirmwareTrackRecord record);
    CustomerFirmwareTrackRecord updateCustomerFirmwareTrackRecord(CustomerFirmwareTrackRecord record);
    CustomerFirmwareTrackRecord deleteCustomerFirmwareTrackRecord(int customerId);
    CustomerFirmwareTrackSettings getDefaultCustomerTrackSetting();
    CustomerFirmwareTrackSettings updateDefaultCustomerTrackSetting(CustomerFirmwareTrackSettings defaultSettings);

	/**
	 * @param equipmentType
	 * @return list of equipment models taken from the all known firmware versions 
	 */
	List<String> getAllFirmwareModelIdsByEquipmentType(EquipmentType equipmentType);
    
}
