package com.telecominfraproject.wlan.firmware;

import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
    private static final ParameterizedTypeReference<List<FirmwareTrackAssignmentDetails>> FirmwareTrackAssignmentDetails_LIST_CLASS_TOKEN = new ParameterizedTypeReference<List<FirmwareTrackAssignmentDetails>>() {};
    
    
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
        LOG.debug("calling createFirmwareVersion {} ", firmwareVersion);

        HttpEntity<String> request = new HttpEntity<String>( firmwareVersion.toString(), headers );

        ResponseEntity<FirmwareVersion> responseEntity = restTemplate.postForEntity(
                getBaseUrl() + "/version",
                request, FirmwareVersion.class);
        
        FirmwareVersion ret = responseEntity.getBody();
        
        LOG.debug("completed createFirmwareVersion {} ", ret);
        
        return ret;
	}

	@Override
	public FirmwareVersion getFirmwareVersion(long firmwareVersionId) {
        LOG.debug("calling getFirmwareVersion {}", firmwareVersionId);

        ResponseEntity<FirmwareVersion> responseEntity = restTemplate.getForEntity(
                getBaseUrl()
                +"/version?firmwareVersionId={firmwareVersionId}",
                FirmwareVersion.class, firmwareVersionId);
        
        FirmwareVersion ret = responseEntity.getBody();
        
        LOG.debug("completed getFirmwareVersion {} ", ret);
        
        return ret;
	}

	@Override
	public FirmwareVersion getFirmwareVersionByName(String firmwareVersionName) {
        LOG.debug("calling getFirmwareVersionByName {}", firmwareVersionName);

        ResponseEntity<FirmwareVersion> responseEntity = restTemplate.getForEntity(
                getBaseUrl()
                +"/version/byName?firmwareVersionName={firmwareVersionName}",
                FirmwareVersion.class, firmwareVersionName);
        
        FirmwareVersion ret = responseEntity.getBody();
        
        LOG.debug("completed getFirmwareVersionByName {} ", ret);
        
        return ret;
	}

	@Override
	public List<FirmwareVersion> getAllFirmwareVersionsByEquipmentType(EquipmentType equipmentType) {
        LOG.debug("getAllFirmwareVersionsByEquipmentType( {})", equipmentType);

        try {
            ResponseEntity<List<FirmwareVersion>> responseEntity = restTemplate.exchange(
                    getBaseUrl() + "/version/byEquipmentType?equipmentType={equipmentType}", HttpMethod.GET,
                    null, FirmwareVersion_LIST_CLASS_TOKEN, equipmentType);

            List<FirmwareVersion> result = responseEntity.getBody();
            if (null == result) {
                result = Collections.emptyList();
            }
            LOG.debug("getAllFirmwareVersionsByEquipmentType({}) return {} entries", equipmentType, result.size());
            return result;
        } catch (Exception exp) {
            LOG.error("getAllFirmwareVersionsByEquipmentType({}) exception ", equipmentType, exp);
            throw exp;
        }

	}

	@Override
	public FirmwareVersion updateFirmwareVersion(FirmwareVersion firmwareVersion) {
        LOG.debug("calling updateFirmwareVersion {} ", firmwareVersion);

        HttpEntity<String> request = new HttpEntity<String>( firmwareVersion.toString(), headers );

        ResponseEntity<FirmwareVersion> responseEntity = restTemplate.exchange(
                getBaseUrl() + "/version",
                HttpMethod.PUT, request, FirmwareVersion.class);
        
        FirmwareVersion ret = responseEntity.getBody();
        
        LOG.debug("completed updateFirmwareVersion {} ", ret);
        
        return ret;
	}

	@Override
	public FirmwareVersion deleteFirmwareVersion(long firmwareVersionId) {
        LOG.debug("calling deleteFirmwareVersion {} ", firmwareVersionId);

        ResponseEntity<FirmwareVersion> responseEntity =  restTemplate.exchange(
                getBaseUrl()
                +"/version?firmwareVersionId={firmwareVersionId}", HttpMethod.DELETE,
                null, FirmwareVersion.class, firmwareVersionId);
        
        FirmwareVersion ret = responseEntity.getBody();
        LOG.debug("completed deleteFirmwareVersion {} ", ret);
        
        return ret;
	}

	@Override
	public FirmwareTrackRecord createFirmwareTrack(FirmwareTrackRecord firmwareTrackRecord) {
        LOG.debug("calling createFirmwareTrack {} ", firmwareTrackRecord);

        HttpEntity<String> request = new HttpEntity<String>( firmwareTrackRecord.toString(), headers );

        ResponseEntity<FirmwareTrackRecord> responseEntity = restTemplate.postForEntity(
                getBaseUrl() + "/track",
                request, FirmwareTrackRecord.class);
        
        FirmwareTrackRecord ret = responseEntity.getBody();
        
        LOG.debug("completed createFirmwareTrack {} ", ret);
        
        return ret;
	}

	@Override
	public FirmwareTrackRecord getFirmwareTrackByName(String firmwareTrackName) {
        LOG.debug("calling getFirmwareTrackByName {}", firmwareTrackName);

        ResponseEntity<FirmwareTrackRecord> responseEntity = restTemplate.getForEntity(
                getBaseUrl()
                +"/track/byName?firmwareTrackName={firmwareTrackName}",
                FirmwareTrackRecord.class, firmwareTrackName);
        
        FirmwareTrackRecord ret = responseEntity.getBody();
        
        LOG.debug("completed getFirmwareTrackByName {} ", ret);
        
        return ret;
	}

	@Override
	public FirmwareTrackRecord getFirmwareTrackById(long firmwareTrackRecordId) {
        LOG.debug("calling getFirmwareTrackById {}", firmwareTrackRecordId);

        ResponseEntity<FirmwareTrackRecord> responseEntity = restTemplate.getForEntity(
                getBaseUrl()
                +"/track?firmwareTrackId={firmwareTrackRecordId}",
                FirmwareTrackRecord.class, firmwareTrackRecordId);
        
        FirmwareTrackRecord ret = responseEntity.getBody();
        
        LOG.debug("completed getFirmwareTrackById {} ", ret);
        
        return ret;
	}

	@Override
	public FirmwareTrackRecord updateFirmwareTrack(FirmwareTrackRecord firmwareTrackRecord) {
        LOG.debug("calling updateFirmwareTrack {} ", firmwareTrackRecord);

        HttpEntity<String> request = new HttpEntity<String>( firmwareTrackRecord.toString(), headers );

        ResponseEntity<FirmwareTrackRecord> responseEntity = restTemplate.exchange(
                getBaseUrl() + "/track",
                HttpMethod.PUT, request, FirmwareTrackRecord.class);
        
        FirmwareTrackRecord ret = responseEntity.getBody();
        
        LOG.debug("completed updateFirmwareTrack {} ", ret);
        
        return ret;
	}

	@Override
	public FirmwareTrackRecord deleteFirmwareTrackRecord(long firmwareTrackRecordId) {
        LOG.debug("calling deleteFirmwareTrackRecord {} ", firmwareTrackRecordId);

        ResponseEntity<FirmwareTrackRecord> responseEntity =  restTemplate.exchange(
                getBaseUrl()
                +"/track?firmwareTrackId={firmwareTrackRecordId}", HttpMethod.DELETE,
                null, FirmwareTrackRecord.class, firmwareTrackRecordId);
        
        FirmwareTrackRecord ret = responseEntity.getBody();
        LOG.debug("completed deleteFirmwareTrackRecord {} ", ret);
        
        return ret;
	}

	@Override
	public List<FirmwareTrackAssignmentDetails> getFirmwareTrackAssignments(String trackName) {
        LOG.debug("getFirmwareTrackAssignments( {})", trackName);

        try {
            ResponseEntity<List<FirmwareTrackAssignmentDetails>> responseEntity = restTemplate.exchange(
                    getBaseUrl() + "/trackAssignment?firmwareTrackName={trackName}", HttpMethod.GET,
                    null, FirmwareTrackAssignmentDetails_LIST_CLASS_TOKEN, trackName);

            List<FirmwareTrackAssignmentDetails> result = responseEntity.getBody();
            if (null == result) {
                result = Collections.emptyList();
            }
            LOG.debug("getFirmwareTrackAssignments({}) return {} entries", trackName, result.size());
            return result;
        } catch (Exception exp) {
            LOG.error("getFirmwareTrackAssignments({}) exception ", trackName, exp);
            throw exp;
        }
	}

	@Override
	public FirmwareTrackAssignmentDetails updateFirmwareTrackAssignment(
			FirmwareTrackAssignmentDetails assignmentDetails) {
        LOG.debug("calling updateFirmwareTrackAssignment {} ", assignmentDetails);

        HttpEntity<String> request = new HttpEntity<String>( assignmentDetails.toString(), headers );

        ResponseEntity<FirmwareTrackAssignmentDetails> responseEntity = restTemplate.exchange(
                getBaseUrl() + "/trackAssignment",
                HttpMethod.PUT, request, FirmwareTrackAssignmentDetails.class);
        
        FirmwareTrackAssignmentDetails ret = responseEntity.getBody();
        
        LOG.debug("completed updateFirmwareTrackAssignment {} ", ret);
        
        return ret;
	}

	@Override
	public FirmwareTrackAssignmentDetails deleteFirmwareTrackAssignment(long firmwareTrackId, long firmwareVersionId) {
        LOG.debug("calling deleteFirmwareTrackAssignment {} {}", firmwareTrackId, firmwareVersionId);

        ResponseEntity<FirmwareTrackAssignmentDetails> responseEntity =  restTemplate.exchange(
                getBaseUrl()
                +"/trackAssignment?firmwareTrackId={firmwareTrackId}&firmwareVersionId={firmwareVersionId}", HttpMethod.DELETE,
                null, FirmwareTrackAssignmentDetails.class, firmwareTrackId, firmwareVersionId);
        
        FirmwareTrackAssignmentDetails ret = responseEntity.getBody();
        LOG.debug("completed deleteFirmwareTrackAssignment {} ", ret);
        
        return ret;
	}

	@Override
	public CustomerFirmwareTrackRecord getCustomerFirmwareTrackRecord(int customerId) {
        LOG.debug("calling getCustomerFirmwareTrackRecord {}", customerId);

        ResponseEntity<CustomerFirmwareTrackRecord> responseEntity = restTemplate.getForEntity(
                getBaseUrl()
                +"/customerTrack?customerId={customerId}",
                CustomerFirmwareTrackRecord.class, customerId);
        
        CustomerFirmwareTrackRecord ret = responseEntity.getBody();
        
        LOG.debug("completed getCustomerFirmwareTrackRecord {} ", ret);
        
        return ret;
	}

	@Override
	public CustomerFirmwareTrackRecord createCustomerFirmwareTrackRecord(CustomerFirmwareTrackRecord record) {
        LOG.debug("calling createCustomerFirmwareTrackRecord {} ", record);

        HttpEntity<String> request = new HttpEntity<String>( record.toString(), headers );

        ResponseEntity<CustomerFirmwareTrackRecord> responseEntity = restTemplate.postForEntity(
                getBaseUrl() + "/customerTrack",
                request, CustomerFirmwareTrackRecord.class);
        
        CustomerFirmwareTrackRecord ret = responseEntity.getBody();
        
        LOG.debug("completed CustomerFirmwareTrackRecord {} ", ret);
        
        return ret;
	}
	

	@Override
	public CustomerFirmwareTrackRecord updateCustomerFirmwareTrackRecord(CustomerFirmwareTrackRecord record) {
        LOG.debug("calling updateCustomerFirmwareTrackRecord {} ", record);

        HttpEntity<String> request = new HttpEntity<String>( record.toString(), headers );

        ResponseEntity<CustomerFirmwareTrackRecord> responseEntity = restTemplate.exchange(
                getBaseUrl() + "/customerTrack",
                HttpMethod.PUT, request, CustomerFirmwareTrackRecord.class);
        
        CustomerFirmwareTrackRecord ret = responseEntity.getBody();
        
        LOG.debug("completed updateCustomerFirmwareTrackRecord {} ", ret);
        
        return ret;
	}

	@Override
	public CustomerFirmwareTrackRecord deleteCustomerFirmwareTrackRecord(int customerId) {
        LOG.debug("calling deleteCustomerFirmwareTrackRecord {} ", customerId);

        ResponseEntity<CustomerFirmwareTrackRecord> responseEntity =  restTemplate.exchange(
                getBaseUrl()
                +"/customerTrack?customerId={customerId}", HttpMethod.DELETE,
                null, CustomerFirmwareTrackRecord.class, customerId);
        
        CustomerFirmwareTrackRecord ret = responseEntity.getBody();
        LOG.debug("completed deleteCustomerFirmwareTrackRecord {} ", ret);
        
        return ret;
	}

	@Override
	public CustomerFirmwareTrackSettings getDefaultCustomerTrackSetting() {
        LOG.debug("calling getDefaultCustomerTrackSetting ");

        ResponseEntity<CustomerFirmwareTrackSettings> responseEntity = restTemplate.getForEntity(
                getBaseUrl()
                +"/customerTrack/default",
                CustomerFirmwareTrackSettings.class);
        
        CustomerFirmwareTrackSettings ret = responseEntity.getBody();
        
        LOG.debug("completed getDefaultCustomerTrackSetting {} ", ret);
        
        return ret;
	}

    
}
