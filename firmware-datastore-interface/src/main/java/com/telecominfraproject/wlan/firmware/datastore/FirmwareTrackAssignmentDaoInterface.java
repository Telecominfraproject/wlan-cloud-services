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
     * Retrieves a map of Platform names to the default FirmwareTrackAssignmentDetails for the particular 
     * track.
     */
    Map<String, FirmwareTrackAssignmentDetails> getAllDefaultFirmwareTrackAssignmentDetails(long firmwareTrackRecordId);
    
    /**
     * Retrieves the default FirmwareTrackAssignmentDetails for given track and platform using track record id.
     */
    FirmwareTrackAssignmentDetails getDefaultFirmwareTrackAssignmentDetailsForPlatform(long firmwareTrackRecordId, String platform);
    
    /**
     * Retrieves the default FirmwareTrackAssignmentDetails for given track and platform using the name of the track.
     */
    FirmwareTrackAssignmentDetails getDefaultFirmwareTrackAssignmentDetailsForPlatform(String trackName, String platform);


    FirmwareTrackAssignmentRecord createOrUpdateFirmwareTrackAssignment(FirmwareTrackAssignmentRecord assignment);

    FirmwareTrackAssignmentRecord deleteFirmwareTrackAssignment(long firmwareTrackRecordId, long firmwareVersionRecordId);
    
    void deleteFirmwareTrackAssignments(long firmwareTrackRecordId);

}
