package com.telecominfraproject.wlan.equipmentgateway.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.equipmentgateway.models.CEGWBaseCommand;
import com.telecominfraproject.wlan.equipmentgateway.models.EquipmentCommandResponse;
import com.telecominfraproject.wlan.opensync.external.integration.controller.OpensyncCloudGatewayController;

@Component
public class EquipmentGatewayServiceLocal implements EquipmentGatewayServiceInterface {

	@Autowired OpensyncCloudGatewayController gatewayController;

	@Override
	public List<EquipmentCommandResponse> sendCommands(List<CEGWBaseCommand> commands) {
		return gatewayController.sendCommands(commands);
	}
	

}
