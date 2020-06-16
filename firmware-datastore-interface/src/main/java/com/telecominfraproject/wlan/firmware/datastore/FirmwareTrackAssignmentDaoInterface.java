package com.telecominfraproject.wlan.firmware.datastore;

import java.util.List;
import java.util.Map;

import com.telecominfraproject.wlan.firmware.models.FirmwareTrackAssignmentDetails;
import com.telecominfraproject.wlan.firmware.models.FirmwareTrackAssignmentRecord;

/**
 * @author ekeddy
 * @author dtoptygin
 *
 */
public interface FirmwareTrackAssignmentDaoInterface {

	List<FirmwareTrackAssignmentDetails> getFirmwareTrackDetails(String firmwareTrackName);
	
    FirmwareTrackAssignmentDetails getFirmwareTrackAssignmentDetails(long firmwareTrackRecordId, long firmwareVersionRecordId);
    
    /**
     * Retrieves a map of equipmentModel names to the default FirmwareTrackAssignmentDetails for the particular 
     * track.
     */
    Map<String, FirmwareTrackAssignmentDetails> getAllDefaultFirmwareTrackAssignmentDetails(long firmwareTrackRecordId);
    
    /**
     * Retrieves the default FirmwareTrackAssignmentDetails for given track and equipmentModel using track record id.
     */
    FirmwareTrackAssignmentDetails getDefaultFirmwareTrackAssignmentDetailsForEquipmentModel(long firmwareTrackRecordId, String equipmentModel);
    
    /**
     * Retrieves the default FirmwareTrackAssignmentDetails for given track and equipmentModel using the name of the track.
     */
    FirmwareTrackAssignmentDetails getDefaultFirmwareTrackAssignmentDetailsForEquipmentModelByTrackName(String trackName, String equipmentModel);


    FirmwareTrackAssignmentRecord createOrUpdateFirmwareTrackAssignment(FirmwareTrackAssignmentRecord assignment);

    FirmwareTrackAssignmentRecord deleteFirmwareTrackAssignment(long firmwareTrackRecordId, long firmwareVersionRecordId);
    
    void deleteFirmwareTrackAssignments(long firmwareTrackRecordId);

}
