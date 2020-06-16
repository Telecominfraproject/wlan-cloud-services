package com.telecominfraproject.wlan.firmware.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.telecominfraproject.wlan.cloudeventdispatcher.CloudEventDispatcherInterface;
import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;
import com.telecominfraproject.wlan.datastore.exceptions.DsEntityNotFoundException;
import com.telecominfraproject.wlan.firmware.datastore.FirmwareDatastore;
import com.telecominfraproject.wlan.firmware.models.CustomerFirmwareTrackRecord;
import com.telecominfraproject.wlan.firmware.models.CustomerFirmwareTrackSettings;
import com.telecominfraproject.wlan.firmware.models.CustomerFirmwareTrackSettings.TrackFlag;
import com.telecominfraproject.wlan.firmware.models.FirmwareTrackAssignmentDetails;
import com.telecominfraproject.wlan.firmware.models.FirmwareTrackAssignmentRecord;
import com.telecominfraproject.wlan.firmware.models.FirmwareTrackRecord;
import com.telecominfraproject.wlan.firmware.models.FirmwareVersion;
import com.telecominfraproject.wlan.systemevent.models.SystemEvent;


/**
 * @author dtoptygin
 *
 */
@RestController
@Transactional
@RequestMapping(value="/api/firmware")
public class FirmwareController {

    private static final Logger LOG = LoggerFactory.getLogger(FirmwareController.class);

    @Autowired private FirmwareDatastore firmwareDatastore;
    @Autowired private CloudEventDispatcherInterface cloudEventDispatcher;
    @Autowired Environment environment;
    
    CustomerFirmwareTrackSettings defaultCustomerTrackSettings;
    
    @PostConstruct
    private void postConstruct() {
        defaultCustomerTrackSettings = new CustomerFirmwareTrackSettings();
        defaultCustomerTrackSettings.setAutoUpgradeDeprecatedOnBind(environment
                .getProperty("whizcontrol.autoupgrade.deprecated", TrackFlag.class, TrackFlag.NEVER));
        defaultCustomerTrackSettings.setAutoUpgradeUnknownOnBind(environment
                .getProperty("whizcontrol.autoupgrade.unknown", TrackFlag.class, TrackFlag.NEVER));
        defaultCustomerTrackSettings.setAutoUpgradeDeprecatedDuringMaintenance(environment.getProperty(
                "whizcontrol.maintenanceupgrade.deprecated", TrackFlag.class, TrackFlag.NEVER));
        defaultCustomerTrackSettings.setAutoUpgradeUnknownDuringMaintenance(environment
                .getProperty("whizcontrol.maintenanceupgrade.unknown", TrackFlag.class, TrackFlag.NEVER));
        LOG.info("Default CustomerFirmwareTrackSettings: {}", defaultCustomerTrackSettings);    	
    }

    private void publishEvent(SystemEvent event) {
        if (event == null) {
            return;
        }
        
        try {
            cloudEventDispatcher.publishEvent(event);
        } catch (Exception e) {
            LOG.error("Failed to publish event : {}", event, e);
        }
    }

    @RequestMapping(value = "/version", method = RequestMethod.POST)
    public FirmwareVersion createFirmwareVersion(@RequestBody FirmwareVersion firmwareVersion) {

        LOG.debug("Creating FirmwareVersion {}", firmwareVersion);
        if (firmwareVersion.getValidationMethod() != null
                && (firmwareVersion.getValidationCode() == null || firmwareVersion.getValidationCode().isEmpty())) {
            throw new DsDataValidationException(
                    "A validation code was not provided for the validation method " + firmwareVersion);
        }

        FirmwareVersion ret = firmwareDatastore.create(firmwareVersion);

        LOG.debug("Created FirmwareVersion {}", ret);

        return ret;
    }

    @RequestMapping(value = "/version", method = RequestMethod.GET)
    public FirmwareVersion getFirmwareVersion(@RequestParam long firmwareVersionId) {
        LOG.debug("Retrieving FirmwareVersion {}", firmwareVersionId);
        FirmwareVersion ret = firmwareDatastore.get(firmwareVersionId);
        LOG.debug("Retrieved FirmwareVersion {}", ret);
        return ret;
    }

    @RequestMapping(value = "/version/byName", method = RequestMethod.GET)
    public FirmwareVersion getFirmwareVersionByName(@RequestParam String firmwareVersionName) {
        LOG.debug("Retrieving FirmwareVersion {}", firmwareVersionName);
        FirmwareVersion ret = firmwareDatastore.getByName(firmwareVersionName);
        LOG.debug("Retrieved FirmwareVersion {}", ret);
        return ret;
    }

    @RequestMapping(value = "/version/byEquipmentType", method = RequestMethod.GET)
    public List<FirmwareVersion> getAllFirmwareVersionsByEquipmentType(@RequestParam EquipmentType equipmentType) {
        LOG.debug("Retrieving all FirmwareVersions for type {} ", equipmentType);
        Map<EquipmentType, List<FirmwareVersion>> retMap = firmwareDatastore.getAllGroupedByEquipmentType();

        LOG.debug("Returning all FirmwareVersions for type {}, count={}", equipmentType,
                retMap.get(equipmentType).size());

        List<FirmwareVersion> ret = retMap.get(equipmentType);
        if (ret == null) {
            ret = Collections.emptyList();
        }

        return ret;
    }

    @RequestMapping(value = "/version", method = RequestMethod.PUT)
    public FirmwareVersion updateFirmwareVersion(@RequestBody FirmwareVersion firmwareVersion) {
        LOG.debug("Updating FirmwareVersion {}", firmwareVersion);
        FirmwareVersion origin = firmwareDatastore.get(firmwareVersion.getId());
        // can not change modelId, otherwise we will have to update default
        // equipmentModel setting for all tracks
        if ((null != origin.getModelId()) && !origin.getModelId().equals(firmwareVersion.getModelId())) {
            throw new DsDataValidationException("Can not change modelId for firmware version");
        }
        FirmwareVersion ret = firmwareDatastore.update(firmwareVersion);

        LOG.debug("Updated FirmwareVersion {}", ret);

        return ret;
    }
    
    @RequestMapping(value = "/version", method = RequestMethod.DELETE)
    public FirmwareVersion deleteFirmwareVersion(@RequestParam long firmwareVersionId) {

        LOG.debug("Deleting FirmwareVersion {}", firmwareVersionId);
        FirmwareVersion ret = firmwareDatastore.delete(firmwareVersionId);
        LOG.debug("Deleted FirmwareVersion {}", ret);

        return ret;
    }

    @RequestMapping(value = "/track", method = RequestMethod.POST)
    public FirmwareTrackRecord createFirmwareTrack(@RequestBody FirmwareTrackRecord firmwareTrack) {
        LOG.debug("calling createFirmwareTrack({})", firmwareTrack);
        firmwareTrack.validateValue();
        FirmwareTrackRecord result = firmwareDatastore.createFirmwareTrack(firmwareTrack);
        return result;
    }

    @RequestMapping(value = "/track", method = RequestMethod.GET)
    public FirmwareTrackRecord getFirmwareTrackById(@RequestParam long firmwareTrackId) {
        LOG.debug("calling getFirmwareTrackById({})", firmwareTrackId);
        return firmwareDatastore.getFirmwareTrackById(firmwareTrackId);
    }

    @RequestMapping(value = "/track/byName", method = RequestMethod.GET)
    public FirmwareTrackRecord getFirmwareTrackByName(@RequestParam String firmwareTrackName) {
        LOG.debug("calling getFirmwareTrackByName({})", firmwareTrackName);
        FirmwareTrackRecord result = firmwareDatastore.getFirmwareTrackByName(firmwareTrackName);
        return result;
    }

    @RequestMapping(value = "/track", method = RequestMethod.PUT)
    public FirmwareTrackRecord updateFirmwareTrack(@RequestBody FirmwareTrackRecord firmwareTrackRecord) {
        LOG.debug("calling updateFirmwareTrack({})", firmwareTrackRecord);
        firmwareTrackRecord.validateValue();
        // make sure we are not try to change name for default track
        if (!FirmwareTrackRecord.DEFAULT_TRACK_NAME.equals(firmwareTrackRecord.getTrackName())) {
            FirmwareTrackRecord originValue = firmwareDatastore
                    .getFirmwareTrackById(firmwareTrackRecord.getRecordId());
            if (FirmwareTrackRecord.DEFAULT_TRACK_NAME.equals(originValue.getTrackName())) {
                throw new DsDataValidationException("Can not change name for default track");
            }
        }
        FirmwareTrackRecord result = firmwareDatastore.updateFirmwareTrack(firmwareTrackRecord);
        return result;
    }

    @RequestMapping(value = "/track", method = RequestMethod.DELETE)
    public FirmwareTrackRecord deleteFirmwareTrackRecord(@RequestParam long firmwareTrackId) {
        LOG.debug("calling deleteFirmwareTrackRecord({})", firmwareTrackId);
        FirmwareTrackRecord result = firmwareDatastore.deleteFirmwareTrackRecord(firmwareTrackId);
        return result;
    }


    @RequestMapping(value = "/trackAssignment", method = RequestMethod.GET)
    public List<FirmwareTrackAssignmentDetails> getFirmwareTrackAssignments (
            @RequestParam String firmwareTrackName) {
        LOG.debug("calling getAllFirmwareVersionsByTrackName({})", firmwareTrackName);
        return firmwareDatastore.getFirmwareTrackDetails(firmwareTrackName);
    }

    @RequestMapping(value = "/trackAssignment", method = RequestMethod.PUT)
    public FirmwareTrackAssignmentDetails updateFirmwareTrackAssignment(
    		@RequestBody FirmwareTrackAssignmentDetails assignmentDetails) {
        LOG.debug("calling updateFirmwareTrackAssignment({})", assignmentDetails);
        FirmwareTrackAssignmentRecord result = firmwareDatastore.createOrUpdateFirmwareTrackAssignment(assignmentDetails);        
        FirmwareVersion version = firmwareDatastore.get(result.getFirmwareVersionRecordId());
		FirmwareTrackAssignmentDetails ret = new FirmwareTrackAssignmentDetails(result, version );
        return ret;
    }
    
    @RequestMapping(value = "/trackAssignment", method = RequestMethod.DELETE)
    public FirmwareTrackAssignmentDetails deleteFirmwareTrackAssignment(@RequestParam long firmwareTrackId,
            @RequestParam long firmwareVersionId) {
        LOG.debug("calling deleteFirmwareTrackAssignment({},{})", firmwareTrackId, firmwareVersionId);
        FirmwareVersion version = firmwareDatastore.get(firmwareVersionId);
        FirmwareTrackAssignmentRecord assignment = firmwareDatastore.deleteFirmwareTrackAssignment(firmwareTrackId, firmwareVersionId);
        FirmwareTrackAssignmentDetails result = new FirmwareTrackAssignmentDetails(assignment, version);
        return result;
    }

    @RequestMapping(value = "/customerTrack/default", method = RequestMethod.GET)
    public CustomerFirmwareTrackSettings getDefaultCustomerTrackSetting() {
        if (defaultCustomerTrackSettings == null) {
                if (defaultCustomerTrackSettings == null) {
                    defaultCustomerTrackSettings = new CustomerFirmwareTrackSettings();
                    defaultCustomerTrackSettings.setAutoUpgradeDeprecatedOnBind(environment
                            .getProperty("whizcontrol.autoupgrade.deprecated", TrackFlag.class, TrackFlag.NEVER));
                    defaultCustomerTrackSettings.setAutoUpgradeUnknownOnBind(environment
                            .getProperty("whizcontrol.autoupgrade.unknown", TrackFlag.class, TrackFlag.NEVER));
                    defaultCustomerTrackSettings.setAutoUpgradeDeprecatedDuringMaintenance(environment.getProperty(
                            "whizcontrol.maintenanceupgrade.deprecated", TrackFlag.class, TrackFlag.NEVER));
                    defaultCustomerTrackSettings.setAutoUpgradeUnknownDuringMaintenance(environment
                            .getProperty("whizcontrol.maintenanceupgrade.unknown", TrackFlag.class, TrackFlag.NEVER));
                    LOG.info("Default CustomerFirmwareTrackSettings: {}", defaultCustomerTrackSettings);
                }
        }
        return defaultCustomerTrackSettings;
    }


    @RequestMapping(value = "/customerTrack", method = RequestMethod.GET)
    public CustomerFirmwareTrackRecord getCustomerFirmwareTrackRecord(@RequestParam int customerId) {
        LOG.debug("calling getCustomerFirmwareTrackRecord({})", customerId);
        return firmwareDatastore.getCustomerFirmwareTrackRecord(customerId);
    }
    

    @RequestMapping(value = "/customerTrack", method = RequestMethod.POST)
    public CustomerFirmwareTrackRecord createCustomerFirmwareTrackRecord(@RequestBody CustomerFirmwareTrackRecord customerTrack) {
        LOG.debug("calling createCustomerFirmwareTrackRecord({})", customerTrack);
        CustomerFirmwareTrackRecord result = firmwareDatastore.createCustomerFirmwareTrackRecord(customerTrack);
        return result;
    }
    
    
    @RequestMapping(value = "/customerTrack", method = RequestMethod.PUT)
    public CustomerFirmwareTrackRecord updateCustomerFirmwareTrackRecord(@RequestBody CustomerFirmwareTrackRecord customerTrack) {
        LOG.debug("calling updateCustomerFirmwareTrackRecord({})", customerTrack);
        CustomerFirmwareTrackRecord result;
        try {
        	@SuppressWarnings("unused")
			CustomerFirmwareTrackRecord existing = firmwareDatastore.getCustomerFirmwareTrackRecord(customerTrack.getCustomerId());
        	result = firmwareDatastore.updateCustomerFirmwareTrackRecord(customerTrack);
        }catch (DsEntityNotFoundException e) {
			// no record found
        	result = firmwareDatastore.createCustomerFirmwareTrackRecord(customerTrack);
        }
        return result;
    }

    
        
    @RequestMapping(value = "/customerTrack", method = RequestMethod.DELETE)
    public CustomerFirmwareTrackRecord deleteCustomerFirmwareTrackRecord(@RequestParam int customerId) {
        LOG.debug("calling deleteCustomerFirmwareTrackRecord({})", customerId);
        CustomerFirmwareTrackRecord result = firmwareDatastore.deleteCustomerFirmwareTrackRecord(customerId);
        return result;    	
    }

    
}
