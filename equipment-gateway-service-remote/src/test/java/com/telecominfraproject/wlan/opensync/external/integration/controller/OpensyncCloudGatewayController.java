package com.telecominfraproject.wlan.opensync.external.integration.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.telecominfraproject.wlan.equipmentgateway.models.CEGWBaseCommand;
import com.telecominfraproject.wlan.equipmentgateway.models.CEGWCommandResultCode;
import com.telecominfraproject.wlan.equipmentgateway.models.EquipmentCommandResponse;

@RestController
public class OpensyncCloudGatewayController {

	private static final Logger LOG = LoggerFactory.getLogger(OpensyncCloudGatewayController.class);

    public static class ListOfEquipmentCommandResponses extends ArrayList<EquipmentCommandResponse> {
        private static final long serialVersionUID = 3070319062835500930L;
    }

    {
    	LOG.info("***** Using mock OpensyncCloudGatewayController");
    }
    
	@RequestMapping(value = "/commands", method = RequestMethod.POST)
	public ListOfEquipmentCommandResponses sendCommands(@RequestBody List<CEGWBaseCommand> commands) {
		ListOfEquipmentCommandResponses ret = new ListOfEquipmentCommandResponses(); 
		if(commands == null) {
			return ret;
		}
		
		commands.forEach(command -> {
			LOG.debug("sendCommands - processing {}", command);
			ret.add(new EquipmentCommandResponse(CEGWCommandResultCode.Success, "", command, "mockGw", 4242) );
		});
		
		return ret;
	}
			
}
