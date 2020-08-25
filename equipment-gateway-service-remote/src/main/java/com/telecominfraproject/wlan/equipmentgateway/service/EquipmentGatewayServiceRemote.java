package com.telecominfraproject.wlan.equipmentgateway.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.telecominfraproject.wlan.core.client.BaseRemoteClient;
import com.telecominfraproject.wlan.equipmentgateway.models.CEGWBaseCommand;
import com.telecominfraproject.wlan.equipmentgateway.models.EquipmentCommandResponse;
import com.telecominfraproject.wlan.routing.RoutingServiceInterface;
import com.telecominfraproject.wlan.routing.models.EquipmentGatewayRecord;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EquipmentGatewayServiceRemote  extends BaseRemoteClient implements EquipmentGatewayServiceInterface {

    private static final Logger LOG = LoggerFactory.getLogger(EquipmentGatewayServiceRemote.class);

    private static final ParameterizedTypeReference<List<EquipmentCommandResponse>> EquipmentCommandResponse_LIST_CLASS_TOKEN = new ParameterizedTypeReference<List<EquipmentCommandResponse>>() {};

    @Autowired RoutingServiceInterface routingServiceInterface;

	private String protocol;
    
    @Override
    public List<EquipmentCommandResponse> sendCommands(List<CEGWBaseCommand> commands) {
    	List<EquipmentCommandResponse> ret = new ArrayList<>();
    	
    	if(commands == null) {
    		return ret;
    	}
    	
    	// split the incoming batch of commands into sub-batches per equipment id
    	Map<Long, List<CEGWBaseCommand>> equipmentIdToCommandsMap = new HashMap<>();
    	commands.forEach(c -> {
    		List<CEGWBaseCommand> cmds = equipmentIdToCommandsMap.get(c.getEquipmentId() );
    		if(cmds == null) {
    			cmds = new ArrayList<>();
    			equipmentIdToCommandsMap.put(c.getEquipmentId(),  cmds);
    		}
    		
    		cmds.add(c);
    	});
    	
    	// for each equipmentId find out EquipmentGatewayRecords (may be multiple)
    	Map<Long, List<EquipmentGatewayRecord>> equipmentIdToGatewaysMap = new HashMap<>();
    	equipmentIdToCommandsMap.keySet().forEach(eqId -> {
    		List<EquipmentGatewayRecord> gateways = equipmentIdToGatewaysMap.get(eqId);
    		if(gateways == null) {
    			gateways = new ArrayList<>();
    			equipmentIdToGatewaysMap.put(eqId, gateways);	
    		}
    		
    		List<EquipmentGatewayRecord> gwList = routingServiceInterface.getRegisteredGatewayRecordList(eqId);
    		if(gwList.isEmpty()) {
    			LOG.debug("Equipment {} is offline, command not delivered", eqId);
    		}
    		
    		gateways.addAll(gwList);
    		
    	});
    	
    	//create a map of EquipmentGatewayRecord to a batch of command for that gateway 
    	Map<EquipmentGatewayRecord, List<CEGWBaseCommand>> gatewayToCommandsMap = new HashMap<>();
    	equipmentIdToGatewaysMap.forEach( (eqId, gwList) -> gwList.forEach( gw -> {
    		List<CEGWBaseCommand> cmds = gatewayToCommandsMap.get(gw);
    		if(cmds ==  null) {
    			cmds = new ArrayList<>();
    			gatewayToCommandsMap.put(gw, cmds);
    		}
    		
    		cmds.addAll(equipmentIdToCommandsMap.get(eqId));
    		
    	}) );
    	
    	// for each EquipmentGatewayRecords send the corresponding sub-batch of commands
    	gatewayToCommandsMap.forEach( (gw, cmds) -> {
    		ret.addAll(sendCommands(gw.getHostname(), gw.getPort(), cmds));
    	} );
    	
    	// TODO resolve multiple responses per-command, if any 
    	//	(i.e. if one GW replied that equipment route is not found, and another GW replied with success, then the result should be success)
    	return ret;
    }
    

    private List<EquipmentCommandResponse> sendCommands(String gatewayHost, int gatewayPort, List<CEGWBaseCommand> commands) {
        HttpEntity<String> request = new HttpEntity<>(commands.toString(), headers);
        LOG.debug("Sending to gateway {}:{} commands {}", gatewayHost, gatewayPort, commands);
		List<EquipmentCommandResponse> ret = null;
		try {
			ResponseEntity<List<EquipmentCommandResponse>> responseEntity = restTemplate.exchange(
					getProtocol() + "://" + gatewayHost + ":" + gatewayPort + "/api/commands",
					HttpMethod.POST, request, EquipmentCommandResponse_LIST_CLASS_TOKEN);
			ret = responseEntity.getBody();
		} catch (Exception ex) {
			LOG.error(" Unable to send commands to gatewayHost {}. Error: {}", gatewayHost, ex.getMessage());
            ret = Collections.emptyList();
		}
        LOG.debug("Response from gateway {}:{}  {}", gatewayHost, gatewayPort, ret);
        
    	return ret;
    }
    
    public String getProtocol() {
        if (protocol == null) {
            protocol = environment.getProperty("tip.wlan.equipmentGatewayProtocol", "https").trim();
        }
        return protocol;
    }

}
