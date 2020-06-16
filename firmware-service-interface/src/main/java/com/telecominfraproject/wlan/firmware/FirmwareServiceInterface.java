package com.telecominfraproject.wlan.firmware;

import java.util.List;

import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
import com.telecominfraproject.wlan.firmware.models.CustomerFirmwareTrackRecord;
import com.telecominfraproject.wlan.firmware.models.CustomerFirmwareTrackSettings;
import com.telecominfraproject.wlan.firmware.models.FirmwareTrackAssignmentDetails;
import com.telecominfraproject.wlan.firmware.models.FirmwareTrackAssignmentRecord;
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

    List<FirmwareVersion> getAllFirmwareVersionsByEquipmentType(EquipmentType equipmentType);

    FirmwareVersion updateFirmwareVersion(FirmwareVersion firmwareVersion);

    FirmwareVersion deleteFirmwareVersion(long firmwareVersionId);

    FirmwareTrackRecord createFirmwareTrack(FirmwareTrackRecord firmwareTrackRecord);

    FirmwareTrackRecord getFirmwareTrackByName(String firmwareTrackName);

    FirmwareTrackRecord getFirmwareTrackById(long firmwareTrackRecordId);

    FirmwareTrackRecord updateFirmwareTrack(FirmwareTrackRecord firmwareTrackRecord);

    FirmwareTrackRecord deleteFirmwareTrackRecord(long firmwareTrackRecordId);

    List<FirmwareTrackAssignmentDetails> getFirmwareTrackAssignments(String trackName);

    FirmwareTrackAssignmentDetails updateFirmwareTrackAssignment(FirmwareTrackAssignmentDetails assignmentDetails);

    FirmwareTrackAssignmentRecord deleteFirmwareTrackAssignment(long firmwareTrackRecordId, long firmwareVersionRecordId);

    CustomerFirmwareTrackRecord getCustomerFirmwareTrackRecord(int customerId);
    CustomerFirmwareTrackRecord createCustomerFirmwareTrackRecord(CustomerFirmwareTrackRecord record);
    CustomerFirmwareTrackRecord updateCustomerFirmwareTrackRecord(CustomerFirmwareTrackRecord record);
    CustomerFirmwareTrackRecord deleteCustomerFirmwareTrackRecord(int customerId);
    CustomerFirmwareTrackSettings getDefaultCustomerTrackSetting();
    
}
