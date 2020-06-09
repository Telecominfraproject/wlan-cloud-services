package com.telecominfraproject.wlan.equipmentgateway.service;

import java.util.Arrays;
import java.util.List;

import com.telecominfraproject.wlan.equipmentgateway.models.CEGWBaseCommand;
import com.telecominfraproject.wlan.equipmentgateway.models.EquipmentCommandResponse;

/**
 * @author dtop
 * <br>Use this interface to deliver commands to the equipment. 
 * Each equipment command identifies a target equipment; routing service is used to determine host/port of the gateway that is managing the connected equipment.
 * Method sendCommands accepts a batch of commands, each command in there can be for a different equipment, they will be routed accordingly.   
 */
public interface EquipmentGatewayServiceInterface {

	/**
	 * Send a batch of commands to the specified equipment gateway
	 * @param commands - list of commands to send to equipment
	 * @return list of command responses - one for each command in the incoming batch, in the same order as the incoming commands.
	 */
	List<EquipmentCommandResponse> sendCommands(List<CEGWBaseCommand> commands);
	
	/**
	 * Convenience method to send a single command to the equipment gateway
	 * @param command
	 * @return command response
	 */
	default EquipmentCommandResponse sendCommand(CEGWBaseCommand command) {
		return sendCommands(Arrays.asList(command)).get(0);
	}
}
