package com.telecominfraproject.wlan.firmware.datastore.inmemory;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
import com.telecominfraproject.wlan.datastore.inmemory.BaseInMemoryDatastore;
import com.telecominfraproject.wlan.firmware.datastore.FirmwareDatastore;
import com.telecominfraproject.wlan.firmware.models.CustomerFirmwareTrackRecord;
import com.telecominfraproject.wlan.firmware.models.FirmwareTrackAssignmentDetails;
import com.telecominfraproject.wlan.firmware.models.FirmwareTrackAssignmentRecord;
import com.telecominfraproject.wlan.firmware.models.FirmwareTrackRecord;
import com.telecominfraproject.wlan.firmware.models.FirmwareVersion;

/**
 * @author ekeddy
 *
 */
@Component
public class FirmwareDatastoreInMemory extends BaseInMemoryDatastore implements FirmwareDatastore {

	@Autowired 
	private FirmwareVersionDatastoreInMemory firmwareVersionDatastore;
	
	@Autowired 
	private FirmwareTrackDatastoreInMemory firmwareTrackDatastore;

	@Autowired 
	private FirmwareTrackAssignmentDatastoreInMemory firmwareTrackAssignmentDatastore;

	@Autowired 
	private CustomerFirmwareTrackDatastoreInMemory customerFirmwareTrackDatastore;

    @SuppressWarnings("unused")
	private static final Logger LOG = LoggerFactory.getLogger(FirmwareDatastoreInMemory.class);
    

    public FirmwareVersion create(FirmwareVersion firmware) {
		return firmwareVersionDatastore.create(firmware);
	}

	public FirmwareVersion get(long firmwareId) {
		return firmwareVersionDatastore.get(firmwareId);
	}

	public FirmwareVersion getByName(String versionName) {
		return firmwareVersionDatastore.getByName(versionName);
	}

	public FirmwareVersion getByNameOrNull(String versionName) {
		return firmwareVersionDatastore.getByNameOrNull(versionName);
	}

	public FirmwareVersion update(FirmwareVersion firmware) {
		return firmwareVersionDatastore.update(firmware);
	}

	public FirmwareVersion delete(long firmwareId) {
		return firmwareVersionDatastore.delete(firmwareId);
	}

	public Map<EquipmentType, List<FirmwareVersion>> getAllGroupedByEquipmentType() {
		return firmwareVersionDatastore.getAllGroupedByEquipmentType();
	}



    public FirmwareTrackRecord createFirmwareTrack(FirmwareTrackRecord firmwareTrack) {
		return firmwareTrackDatastore.createFirmwareTrack(firmwareTrack);
	}

	public FirmwareTrackRecord getFirmwareTrackById(long firmwareTrackId) {
		return firmwareTrackDatastore.getFirmwareTrackById(firmwareTrackId);
	}

	public FirmwareTrackRecord getFirmwareTrackByName(String trackName) {
		return firmwareTrackDatastore.getFirmwareTrackByName(trackName);
	}

	public FirmwareTrackRecord getFirmwareTrackByNameOrNull(String trackName) {
		return firmwareTrackDatastore.getFirmwareTrackByNameOrNull(trackName);
	}

	public FirmwareTrackRecord updateFirmwareTrack(FirmwareTrackRecord firmwareTrack) {
		return firmwareTrackDatastore.updateFirmwareTrack(firmwareTrack);
	}

	public FirmwareTrackRecord deleteFirmwareTrackRecord(long firmwareId) {
		return firmwareTrackDatastore.deleteFirmwareTrackRecord(firmwareId);
	}


	

    public FirmwareTrackAssignmentRecord createOrUpdateFirmwareTrackAssignment(
			FirmwareTrackAssignmentRecord assignmentRecord) {
		return firmwareTrackAssignmentDatastore.createOrUpdateFirmwareTrackAssignment(assignmentRecord);
	}

	public FirmwareTrackAssignmentRecord deleteFirmwareTrackAssignment(long firmwareTrackRecordId,
			long firmwareVersionRecordId) {
		return firmwareTrackAssignmentDatastore.deleteFirmwareTrackAssignment(firmwareTrackRecordId,
				firmwareVersionRecordId);
	}

	@Override
	public void deleteFirmwareTrackAssignments(long firmwareTrackRecordId) {
		firmwareTrackAssignmentDatastore.deleteFirmwareTrackAssignments(firmwareTrackRecordId);
	}
	
	public List<FirmwareTrackAssignmentDetails> getFirmwareTrackDetails(String firmwareTrackName) {
		return firmwareTrackAssignmentDatastore.getFirmwareTrackDetails(firmwareTrackName);
	}

	public FirmwareTrackAssignmentDetails getFirmwareTrackAssignmentDetails(long firmwareTrackRecordId,
			long firmwareVersionRecordId) {
		return firmwareTrackAssignmentDatastore.getFirmwareTrackAssignmentDetails(firmwareTrackRecordId,
				firmwareVersionRecordId);
	}

	public Map<String, FirmwareTrackAssignmentDetails> getAllDefaultFirmwareTrackAssignmentDetails(
			long firmwareTrackRecordId) {
		return firmwareTrackAssignmentDatastore.getAllDefaultFirmwareTrackAssignmentDetails(firmwareTrackRecordId);
	}

	public FirmwareTrackAssignmentDetails getDefaultFirmwareTrackAssignmentDetailsForEquipmentModel(
			long firmwareTrackRecordId, String equipmentModel) {
		return firmwareTrackAssignmentDatastore
				.getDefaultFirmwareTrackAssignmentDetailsForEquipmentModel(firmwareTrackRecordId, equipmentModel);
	}

	public FirmwareTrackAssignmentDetails getDefaultFirmwareTrackAssignmentDetailsForEquipmentModelByTrackName(String trackName,
			String equipmentModel) {
		return firmwareTrackAssignmentDatastore.getDefaultFirmwareTrackAssignmentDetailsForEquipmentModelByTrackName(trackName,
				equipmentModel);
	}

	
	public CustomerFirmwareTrackRecord createCustomerFirmwareTrackRecord(CustomerFirmwareTrackRecord record) {
		return customerFirmwareTrackDatastore.createCustomerFirmwareTrackRecord(record);
	}

	public CustomerFirmwareTrackRecord updateCustomerFirmwareTrackRecord(CustomerFirmwareTrackRecord record) {
		return customerFirmwareTrackDatastore.updateCustomerFirmwareTrackRecord(record);
	}

	public CustomerFirmwareTrackRecord getCustomerFirmwareTrackRecord(int customerId) {
		return customerFirmwareTrackDatastore.getCustomerFirmwareTrackRecord(customerId);
	}

	public CustomerFirmwareTrackRecord deleteCustomerFirmwareTrackRecord(int customerId) {
		return customerFirmwareTrackDatastore.deleteCustomerFirmwareTrackRecord(customerId);
	}

	@Override
	public void deleteCustomerFirmwareTrackRecords(long firmwareTrackRecordId) {
		customerFirmwareTrackDatastore.deleteCustomerFirmwareTrackRecords(firmwareTrackRecordId);
	}
	
}
