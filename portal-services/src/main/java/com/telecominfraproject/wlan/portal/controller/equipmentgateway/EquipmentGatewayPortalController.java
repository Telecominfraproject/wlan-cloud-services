package com.telecominfraproject.wlan.portal.controller.equipmentgateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.telecominfraproject.wlan.core.model.json.GenericResponse;
import com.telecominfraproject.wlan.equipment.EquipmentServiceInterface;
import com.telecominfraproject.wlan.equipment.models.Equipment;
import com.telecominfraproject.wlan.equipmentgateway.models.CEGWCommandResultCode;
import com.telecominfraproject.wlan.equipmentgateway.models.CEGWFirmwareDownloadRequest;
import com.telecominfraproject.wlan.equipmentgateway.models.EquipmentCommandResponse;
import com.telecominfraproject.wlan.equipmentgateway.service.EquipmentGatewayServiceInterface;
import com.telecominfraproject.wlan.firmware.FirmwareServiceInterface;
import com.telecominfraproject.wlan.firmware.models.FirmwareVersion;

/**
 * @author dtoptygin
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


}
