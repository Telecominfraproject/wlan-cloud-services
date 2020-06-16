package com.telecominfraproject.wlan.firmware.datastore;

import com.telecominfraproject.wlan.firmware.models.FirmwareTrackRecord;

/**
 * @author ekeddy
 * @author dtoptygin
 *
 */
public interface FirmwareTrackDaoInterface {

    FirmwareTrackRecord createFirmwareTrack(FirmwareTrackRecord firmwareTrack);

    FirmwareTrackRecord getFirmwareTrackByName(String firmwareTrackName);

    FirmwareTrackRecord getFirmwareTrackByNameOrNull(String firmwareTrackName);

    FirmwareTrackRecord getFirmwareTrackById(long firmwareTrackRecordId);

    FirmwareTrackRecord updateFirmwareTrack(FirmwareTrackRecord firmwareTrackRecord);

    FirmwareTrackRecord deleteFirmwareTrackRecord(long firmwareTrackRecordId);

}
