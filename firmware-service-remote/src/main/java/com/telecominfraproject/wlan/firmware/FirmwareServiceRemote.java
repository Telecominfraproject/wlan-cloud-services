package com.telecominfraproject.wlan.firmware;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.core.client.BaseRemoteClient;
import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
import com.telecominfraproject.wlan.core.model.pagination.PaginationResponse;
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
@Component
public class FirmwareServiceRemote extends BaseRemoteClient implements FirmwareServiceInterface {

    private static final Logger LOG = LoggerFactory.getLogger(FirmwareServiceRemote.class);
    
    private static final ParameterizedTypeReference<List<FirmwareVersion>> FirmwareVersion_LIST_CLASS_TOKEN = new ParameterizedTypeReference<List<FirmwareVersion>>() {};

    private static final ParameterizedTypeReference<PaginationResponse<FirmwareVersion>> Firmware_Version_PAGINATION_RESPONSE_CLASS_TOKEN = new ParameterizedTypeReference<PaginationResponse<FirmwareVersion>>() {};


    private String baseUrl;

    public String getBaseUrl() {
        if(baseUrl==null) {
            baseUrl = environment.getProperty("tip.wlan.firmwareServiceBaseUrl").trim()+"/api/firmware";
        }

    	return baseUrl;
    }

	@Override
	public FirmwareVersion createFirmwareVersion(FirmwareVersion firmwareVersion) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FirmwareVersion getFirmwareVersion(long firmwareVersionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FirmwareVersion getFirmwareVersionByName(String versionName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FirmwareVersion> getAllFirmwareVersionsByEquipmentType(EquipmentType equipmentType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FirmwareVersion updateFirmwareVersion(FirmwareVersion firmwareVersion) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FirmwareVersion deleteFirmwareVersion(long firmwareVersionId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FirmwareTrackRecord createFirmwareTrack(FirmwareTrackRecord firmwareTrackRecord) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FirmwareTrackRecord getFirmwareTrackByName(String firmwareTrackName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FirmwareTrackRecord getFirmwareTrackById(long firmwareTrackRecordId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FirmwareTrackRecord updateFirmwareTrack(FirmwareTrackRecord firmwareTrackRecord) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FirmwareTrackRecord deleteFirmwareTrackRecord(long firmwareTrackRecordId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<FirmwareTrackAssignmentDetails> getFirmwareTrackAssignments(String trackName) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FirmwareTrackAssignmentDetails updateFirmwareTrackAssignment(
			FirmwareTrackAssignmentDetails assignmentDetails) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FirmwareTrackAssignmentRecord deleteFirmwareTrackAssignment(long firmwareTrackRecordId,
			long firmwareVersionRecordId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CustomerFirmwareTrackRecord getCustomerFirmwareTrackRecord(int customerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CustomerFirmwareTrackRecord createCustomerFirmwareTrackRecord(CustomerFirmwareTrackRecord record) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CustomerFirmwareTrackRecord updateCustomerFirmwareTrackRecord(CustomerFirmwareTrackRecord record) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CustomerFirmwareTrackRecord deleteCustomerFirmwareTrackRecord(int customerId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public CustomerFirmwareTrackSettings getDefaultCustomerTrackSetting() {
		// TODO Auto-generated method stub
		return null;
	}

    
}
