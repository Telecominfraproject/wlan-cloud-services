package com.telecominfraproject.wlan.equipmentgateway.service;

import java.util.List;

import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.equipmentgateway.models.CEGWBaseCommand;
import com.telecominfraproject.wlan.equipmentgateway.models.EquipmentCommandResponse;

@Component
public class EquipmentGatewayServiceEmpty implements EquipmentGatewayServiceInterface {

	@Override
	public List<EquipmentCommandResponse> sendCommands(List<CEGWBaseCommand> commands) {
		return null;
	}

}
