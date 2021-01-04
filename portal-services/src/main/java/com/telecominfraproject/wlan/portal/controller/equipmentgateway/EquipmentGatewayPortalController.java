package com.telecominfraproject.wlan.portal.controller.equipmentgateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.telecominfraproject.wlan.core.model.json.GenericResponse;
import com.telecominfraproject.wlan.equipment.EquipmentServiceInterface;
import com.telecominfraproject.wlan.equipment.models.Equipment;
import com.telecominfraproject.wlan.equipment.models.RadioChannelChangeSettings;
import com.telecominfraproject.wlan.equipmentgateway.models.CEGWCommandResultCode;
import com.telecominfraproject.wlan.equipmentgateway.models.CEGWFirmwareDownloadRequest;
import com.telecominfraproject.wlan.equipmentgateway.models.CEGWNewChannelRequest;
import com.telecominfraproject.wlan.equipmentgateway.models.CEGWRebootRequest;
import com.telecominfraproject.wlan.equipmentgateway.models.EquipmentCommandResponse;
import com.telecominfraproject.wlan.equipmentgateway.service.EquipmentGatewayServiceInterface;
import com.telecominfraproject.wlan.firmware.FirmwareServiceInterface;
import com.telecominfraproject.wlan.firmware.models.FirmwareVersion;
import com.telecominfraproject.wlan.status.equipment.models.EquipmentResetMethod;

/**
 * @author dtoptygin
 * @author mikehansen
 *
 */
@RestController
@RequestMapping(value = "/portal")
public class EquipmentGatewayPortalController  {

    private static final Logger LOG = LoggerFactory.getLogger(EquipmentGatewayPortalController.class);

    @Autowired
    private EquipmentServiceInterface equipmentServiceInterface;

    @Autowired
    private EquipmentGatewayServiceInterface equipmentGatewayServiceInterface;

    @Autowired
    private FirmwareServiceInterface firmwareServiceInterface;

    @RequestMapping(value = "/equipmentGateway/requestFirmwareUpdate", method = RequestMethod.POST)
    public GenericResponse requestFirmwareUpdate(@RequestParam long equipmentId, @RequestParam long firmwareVersionId) {
        LOG.debug("requestFirmwareUpdate {} {}", equipmentId, firmwareVersionId);

        Equipment equipment = equipmentServiceInterface.get(equipmentId);
        FirmwareVersion fwVersion = firmwareServiceInterface.getFirmwareVersion(firmwareVersionId);
        
        CEGWFirmwareDownloadRequest fwDownloadRequest = new CEGWFirmwareDownloadRequest(equipment.getInventoryId(),
                equipment.getId(), fwVersion.getVersionName(), fwVersion.getFilename(),
                fwVersion.getValidationMethod(), fwVersion.getValidationCode());

        EquipmentCommandResponse response = equipmentGatewayServiceInterface.sendCommand(fwDownloadRequest);
        LOG.debug("FW Download Response {}", response);

        if(response.getResultCode() == CEGWCommandResultCode.Success) {
        	return new GenericResponse(true,"");
        } else {
        	return new GenericResponse(false, "Failed to request firmware update: "+ response.getResultCode() + " " + response.getResultDetail());
        }
    }
    
    @RequestMapping(value = "/equipmentGateway/requestChannelChange", method = RequestMethod.POST)
    public GenericResponse requestChannelChange(@RequestParam long equipmentId, @RequestBody RadioChannelChangeSettings radioChannelChangeSettings) {
        LOG.debug("requestChannelChange {} {}", equipmentId);

        Equipment equipment = equipmentServiceInterface.get(equipmentId);       
 
        CEGWNewChannelRequest newChannelRequest = new CEGWNewChannelRequest(equipment.getInventoryId(), equipmentId, radioChannelChangeSettings.getBackupChannel(), radioChannelChangeSettings.getPrimaryChannel());
        
        EquipmentCommandResponse response = equipmentGatewayServiceInterface.sendCommand(newChannelRequest);
        LOG.debug("Channel Change Response {}", response);

        if(response.getResultCode() == CEGWCommandResultCode.Success) {
            return new GenericResponse(true,"");
        } else {
            return new GenericResponse(false, "Failed to initiate channel change: "+ response.getResultCode() + " " + response.getResultDetail());
        }
    }
    
    @RequestMapping(value = "/equipmentGateway/requestApReboot", method = RequestMethod.POST)
    public GenericResponse requestApReboot(@RequestParam long equipmentId) {
        LOG.debug("requestApReboot {}", equipmentId);

        Equipment equipment = equipmentServiceInterface.get(equipmentId);
       // No config change, just plain reboot
        CEGWRebootRequest apRebootRequest = new CEGWRebootRequest(equipment.getInventoryId(),
                equipment.getId(), false, EquipmentResetMethod.NoReset);

        EquipmentCommandResponse response = equipmentGatewayServiceInterface.sendCommand(apRebootRequest);
        LOG.debug("AP reboot response {}", response);

        if(response.getResultCode() == CEGWCommandResultCode.Success) {
            return new GenericResponse(true,"");
        } else {
            return new GenericResponse(false, "Failed to trigger AP reboot: "+ response.getResultCode() + " " + response.getResultDetail());
        }
    }
    
    @RequestMapping(value = "/equipmentGateway/requestApSwitchSoftwareBank", method = RequestMethod.POST)
    public GenericResponse requestApSwitchSoftwareBank(@RequestParam long equipmentId) {
        LOG.debug("requestApSwitchSoftwareBank {}", equipmentId);

        Equipment equipment = equipmentServiceInterface.get(equipmentId);
       // Reboot, switch active/inactive software bank
        CEGWRebootRequest apSwitchSoftwareBank = new CEGWRebootRequest(equipment.getInventoryId(),
                equipment.getId(), true, EquipmentResetMethod.NoReset);

        EquipmentCommandResponse response = equipmentGatewayServiceInterface.sendCommand(apSwitchSoftwareBank);
        LOG.debug("AP switch software bank response {}", response);

        if(response.getResultCode() == CEGWCommandResultCode.Success) {
            return new GenericResponse(true,"");
        } else {
            return new GenericResponse(false, "Failed to trigger AP switch software bank: "+ response.getResultCode() + " " + response.getResultDetail());
        }
    }
    
    @RequestMapping(value = "/equipmentGateway/requestApFactoryReset", method = RequestMethod.POST)
    public GenericResponse requestApFactoryReset(@RequestParam long equipmentId) {
        LOG.debug("requestApFactoryReset {}", equipmentId);

        Equipment equipment = equipmentServiceInterface.get(equipmentId);
       // Reboot Ap with factory settings
        CEGWRebootRequest apFactoryReset = new CEGWRebootRequest(equipment.getInventoryId(),
                equipment.getId(), false, EquipmentResetMethod.FactoryReset);

        EquipmentCommandResponse response = equipmentGatewayServiceInterface.sendCommand(apFactoryReset);
        LOG.debug("AP factory reset response {}", response);

        if(response.getResultCode() == CEGWCommandResultCode.Success) {
            return new GenericResponse(true,"");
        } else {
            return new GenericResponse(false, "Failed to trigger AP factory reset: "+ response.getResultCode() + " " + response.getResultDetail());
        }
    }


}
