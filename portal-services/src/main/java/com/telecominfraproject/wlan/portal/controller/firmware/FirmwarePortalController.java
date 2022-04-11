package com.telecominfraproject.wlan.portal.controller.firmware;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
import com.telecominfraproject.wlan.firmware.FirmwareServiceInterface;
import com.telecominfraproject.wlan.firmware.models.CustomerFirmwareTrackRecord;
import com.telecominfraproject.wlan.firmware.models.CustomerFirmwareTrackSettings;
import com.telecominfraproject.wlan.firmware.models.FirmwareTrackAssignmentDetails;
import com.telecominfraproject.wlan.firmware.models.FirmwareTrackRecord;
import com.telecominfraproject.wlan.firmware.models.FirmwareVersion;

/**
 * @author dtoptygin
 *
 */
@RestController
@RequestMapping(value = "/portal/firmware")
public class FirmwarePortalController  {

    private static final Logger LOG = LoggerFactory.getLogger(FirmwarePortalController.class);

    // For serialization:
    // need these wrapper classes so that each element in returned container is
    // marked with "model_type" attribute
    // all callers of this class can deal with normal collections
    public static class ListOfFirmwareVersions extends ArrayList<FirmwareVersion> {
        private static final long serialVersionUID = 1158560190003268713L;
    }

    public static class ListOfFirmwareTrackAssignmentDetails extends ArrayList<FirmwareTrackAssignmentDetails> {
        private static final long serialVersionUID = 1158560190003268714L;
    }

    @Autowired
    private FirmwareServiceInterface firmwareServiceInterface;

    @RequestMapping(value = "/version", method = RequestMethod.POST)
    public FirmwareVersion createFirmwareVersion(@RequestBody FirmwareVersion firmwareVersion) {

        LOG.debug("Creating FirmwareVersion {}", firmwareVersion);
        FirmwareVersion ret = firmwareServiceInterface.createFirmwareVersion(firmwareVersion);
        LOG.debug("Created FirmwareVersion {}", ret);

        return ret;
    }

    @RequestMapping(value = "/version", method = RequestMethod.GET)
    public FirmwareVersion getFirmwareVersion(@RequestParam long firmwareVersionId) {
        LOG.debug("Retrieving FirmwareVersion {}", firmwareVersionId);
        FirmwareVersion ret = firmwareServiceInterface.getFirmwareVersion(firmwareVersionId);
        LOG.debug("Retrieved FirmwareVersion {}", ret);
        return ret;
    }

    @RequestMapping(value = "/version/byName", method = RequestMethod.GET)
    public FirmwareVersion getFirmwareVersionByName(@RequestParam String firmwareVersionName) {
        LOG.debug("Retrieving FirmwareVersion {}", firmwareVersionName);
        FirmwareVersion ret = firmwareServiceInterface.getFirmwareVersionByName(firmwareVersionName);
        LOG.debug("Retrieved FirmwareVersion {}", ret);
        return ret;
    }

    @RequestMapping(value = "/version/byEquipmentType", method = RequestMethod.GET)
    public ListOfFirmwareVersions getAllFirmwareVersionsByEquipmentType(
    		@RequestParam EquipmentType equipmentType, 
    		@RequestParam(required = false) String modelId) {
        LOG.debug("Retrieving all FirmwareVersions for type {} model {}", equipmentType, modelId);
        List<FirmwareVersion> result = firmwareServiceInterface.getAllFirmwareVersionsByEquipmentType(equipmentType, modelId);
        ListOfFirmwareVersions ret = new ListOfFirmwareVersions();
        ret.addAll(result);
        return ret;
    }

    @RequestMapping(value = "/model/byEquipmentType", method = RequestMethod.GET)
    public List<String> getAllFirmwareModelIdsByEquipmentType(@RequestParam EquipmentType equipmentType) {
        LOG.debug("Retrieving all known model ids of FirmwareVersions for type {} ", equipmentType);
        List<String> result = firmwareServiceInterface.getAllFirmwareModelIdsByEquipmentType(equipmentType);
        return result;
    }

    @RequestMapping(value = "/version", method = RequestMethod.PUT)
    public FirmwareVersion updateFirmwareVersion(@RequestBody FirmwareVersion firmwareVersion) {
        LOG.debug("Updating FirmwareVersion {}", firmwareVersion);
        FirmwareVersion ret = firmwareServiceInterface.updateFirmwareVersion(firmwareVersion);
        LOG.debug("Updated FirmwareVersion {}", ret);

        return ret;
    }
    
    @RequestMapping(value = "/version", method = RequestMethod.DELETE)
    public FirmwareVersion deleteFirmwareVersion(@RequestParam long firmwareVersionId) {
        LOG.debug("Deleting FirmwareVersion {}", firmwareVersionId);
        FirmwareVersion ret = firmwareServiceInterface.deleteFirmwareVersion(firmwareVersionId);
        LOG.debug("Deleted FirmwareVersion {}", ret);

        return ret;
    }

    @RequestMapping(value = "/track", method = RequestMethod.POST)
    public FirmwareTrackRecord createFirmwareTrack(@RequestBody FirmwareTrackRecord firmwareTrack) {
        LOG.debug("calling createFirmwareTrack({})", firmwareTrack);
        FirmwareTrackRecord result = firmwareServiceInterface.createFirmwareTrack(firmwareTrack);
        return result;
    }

    @RequestMapping(value = "/track", method = RequestMethod.GET)
    public FirmwareTrackRecord getFirmwareTrackById(@RequestParam long firmwareTrackId) {
        LOG.debug("calling getFirmwareTrackById({})", firmwareTrackId);
        return firmwareServiceInterface.getFirmwareTrackById(firmwareTrackId);
    }

    @RequestMapping(value = "/track/byName", method = RequestMethod.GET)
    public FirmwareTrackRecord getFirmwareTrackByName(@RequestParam String firmwareTrackName) {
        LOG.debug("calling getFirmwareTrackByName({})", firmwareTrackName);
        FirmwareTrackRecord result = firmwareServiceInterface.getFirmwareTrackByName(firmwareTrackName);
        return result;
    }

    @RequestMapping(value = "/track", method = RequestMethod.PUT)
    public FirmwareTrackRecord updateFirmwareTrack(@RequestBody FirmwareTrackRecord firmwareTrackRecord) {
        LOG.debug("calling updateFirmwareTrack({})", firmwareTrackRecord);
        FirmwareTrackRecord result = firmwareServiceInterface.updateFirmwareTrack(firmwareTrackRecord);
        return result;
    }

    @RequestMapping(value = "/track", method = RequestMethod.DELETE)
    public FirmwareTrackRecord deleteFirmwareTrackRecord(@RequestParam long firmwareTrackId) {
        LOG.debug("calling deleteFirmwareTrackRecord({})", firmwareTrackId);
        FirmwareTrackRecord result = firmwareServiceInterface.deleteFirmwareTrackRecord(firmwareTrackId);
        return result;
    }


    @RequestMapping(value = "/trackAssignment", method = RequestMethod.GET)
    public ListOfFirmwareTrackAssignmentDetails getFirmwareTrackAssignments (
            @RequestParam String firmwareTrackName) {
        LOG.debug("calling getAllFirmwareVersionsByTrackName({})", firmwareTrackName);
        ListOfFirmwareTrackAssignmentDetails ret = new ListOfFirmwareTrackAssignmentDetails();
        List<FirmwareTrackAssignmentDetails> result = firmwareServiceInterface.getFirmwareTrackAssignments(firmwareTrackName);
        ret.addAll(result);
        return ret;
    }

    @RequestMapping(value = "/trackAssignment", method = RequestMethod.PUT)
    public FirmwareTrackAssignmentDetails updateFirmwareTrackAssignment(@RequestBody FirmwareTrackAssignmentDetails assignmentDetails) {
        LOG.debug("calling updateFirmwareTrackAssignment({})", assignmentDetails);
        FirmwareTrackAssignmentDetails ret = firmwareServiceInterface.updateFirmwareTrackAssignment(assignmentDetails);        
        return ret;
    }
    
    @RequestMapping(value = "/trackAssignment", method = RequestMethod.DELETE)
    public FirmwareTrackAssignmentDetails deleteFirmwareTrackAssignment(@RequestParam long firmwareTrackId,
            @RequestParam long firmwareVersionId) {
        LOG.debug("calling deleteFirmwareTrackAssignment({},{})", firmwareTrackId, firmwareVersionId);
        FirmwareTrackAssignmentDetails ret = firmwareServiceInterface.deleteFirmwareTrackAssignment(firmwareTrackId, firmwareVersionId);
        return ret;
    }

    @RequestMapping(value = "/customerTrack/default", method = RequestMethod.GET)
    public CustomerFirmwareTrackSettings getDefaultCustomerTrackSetting() {
        LOG.debug("calling getDefaultCustomerTrackSetting()");
        return firmwareServiceInterface.getDefaultCustomerTrackSetting();
    }

    @RequestMapping(value = "/customerTrack/default", method = RequestMethod.PUT)
    public CustomerFirmwareTrackSettings updateDefaultCustomerTrackSetting(@RequestBody CustomerFirmwareTrackSettings defaultSettings) {
        LOG.debug("calling updateDefaultCustomerTrackSetting({})", defaultSettings);
        return firmwareServiceInterface.updateDefaultCustomerTrackSetting(defaultSettings);
    }

    @RequestMapping(value = "/customerTrack", method = RequestMethod.GET)
    public CustomerFirmwareTrackRecord getCustomerFirmwareTrackRecord(@RequestParam int customerId) {
        LOG.debug("calling getCustomerFirmwareTrackRecord({})", customerId);
        return firmwareServiceInterface.getCustomerFirmwareTrackRecord(customerId);
    }
    

    @RequestMapping(value = "/customerTrack", method = RequestMethod.POST)
    public CustomerFirmwareTrackRecord createCustomerFirmwareTrackRecord(@RequestBody CustomerFirmwareTrackRecord customerTrack) {
        LOG.debug("calling createCustomerFirmwareTrackRecord({})", customerTrack);
        CustomerFirmwareTrackRecord result = firmwareServiceInterface.createCustomerFirmwareTrackRecord(customerTrack);
        return result;
    }
    
    
    @RequestMapping(value = "/customerTrack", method = RequestMethod.PUT)
    public CustomerFirmwareTrackRecord updateCustomerFirmwareTrackRecord(@RequestBody CustomerFirmwareTrackRecord customerTrack) {
        LOG.debug("calling updateCustomerFirmwareTrackRecord({})", customerTrack);
		CustomerFirmwareTrackRecord result = firmwareServiceInterface.updateCustomerFirmwareTrackRecord(customerTrack);
        return result;
    }

    
        
    @RequestMapping(value = "/customerTrack", method = RequestMethod.DELETE)
    public CustomerFirmwareTrackRecord deleteCustomerFirmwareTrackRecord(@RequestParam int customerId) {
        LOG.debug("calling deleteCustomerFirmwareTrackRecord({})", customerId);
        CustomerFirmwareTrackRecord result = firmwareServiceInterface.deleteCustomerFirmwareTrackRecord(customerId);
        return result;    	
    }

}
