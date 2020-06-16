package com.telecominfraproject.wlan.firmware.datastore;

import com.telecominfraproject.wlan.firmware.models.CustomerFirmwareTrackRecord;

/**
 * @author ekeddy
 * @author dtoptygin
 *
 */
public interface CustomerFirmwareTrackDaoInterface {

    CustomerFirmwareTrackRecord createCustomerFirmwareTrackRecord(CustomerFirmwareTrackRecord record);
    CustomerFirmwareTrackRecord updateCustomerFirmwareTrackRecord(CustomerFirmwareTrackRecord record);
    CustomerFirmwareTrackRecord getCustomerFirmwareTrackRecord(int customerId);
    CustomerFirmwareTrackRecord deleteCustomerFirmwareTrackRecord(int customerId);
    void deleteCustomerFirmwareTrackRecords(long firmwareTrackRecordId);
}
