package com.telecominfraproject.wlan.portforwardinggateway.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.telecominfraproject.wlan.core.model.service.CloudServiceInformation;
import com.telecominfraproject.wlan.core.server.container.ConnectorProperties;
import com.telecominfraproject.wlan.equipment.EquipmentServiceInterface;
import com.telecominfraproject.wlan.equipment.models.Equipment;
import com.telecominfraproject.wlan.equipmentgateway.models.CEGWCommandResultCode;
import com.telecominfraproject.wlan.equipmentgateway.models.CEGWStartDebugEngine;
import com.telecominfraproject.wlan.equipmentgateway.models.CEGWStopDebugEngine;
import com.telecominfraproject.wlan.equipmentgateway.models.EquipmentCommandResponse;
import com.telecominfraproject.wlan.equipmentgateway.service.EquipmentGatewayServiceInterface;
import com.telecominfraproject.wlan.portforwardinggateway.websocket.PortForwarderWebSocketHandler;

/**
 * Port Forwarder Controller
 * 
 * @author dtop
 * 
 */
@RestController
@RequestMapping("/api/portFwd")
public class PortForwarderController {

    private static final Logger LOG = LoggerFactory.getLogger(PortForwarderController.class);
    private static final int MAX_LOG_ENTRY = 50;
    private @Autowired ApplicationContext appContext;
    private @Autowired ConnectorProperties connectorProperties;
    private PortForwarderWebSocketHandler forwarderWebSocketHandler;
    
    @Autowired
    private EquipmentGatewayServiceInterface equipmentGatewayServiceInterface;

    @Autowired
    private EquipmentServiceInterface equipmentServiceInterface;

    @RequestMapping(value = "/createSession/inventoryId/{inventoryId}/port/{port}/", method = RequestMethod.POST)
    public String createSession(@PathVariable String inventoryId, @PathVariable int port) {
        LOG.info("createSession({}, {})", inventoryId, port);
        String externalHostName = connectorProperties.getExternalHostName();
        String result = getForwarder().startForwarderSession(inventoryId, port) + "@" + externalHostName;
        LOG.info("createSession({}, {}) returns {}", inventoryId, port, result);
        return result;
    }

    @RequestMapping(value = "/stopSession/{sessionId}/", method = RequestMethod.POST)
    public void stopSession(@PathVariable String sessionId) {
        LOG.info("stopSession({})", sessionId);
        getForwarder().stopForwarderSession(sessionId);
        LOG.info("stopSession({}) returns void", sessionId);
    }

    @RequestMapping(value = "/listSessions/", method = RequestMethod.GET)
    public List<String> listSessions() {
        LOG.info("listSessions()");
        List<String> result = getForwarder().getForwardingSessions();
        if ((null != result) && (result.size() < MAX_LOG_ENTRY)) {
            LOG.info("listSessions() returns {} entries", result.size());
        } else {
            LOG.info("listSessions() returns {}", result);
        }
        return result;
    }

    @RequestMapping(value = "/agentStatus/inventoryId/{inventoryId}/", method = RequestMethod.GET)
    public String agentStatus(@PathVariable String inventoryId) {
        LOG.info("agentStatus({})", inventoryId);
        String result = getForwarder().isWebSocketSessionExist(inventoryId) ? "up" : "down";
        LOG.info("agentStatus({}) returns {}", inventoryId, result);
        return result;
    }

    @RequestMapping(value = "/agentStatus/websocket/", method = RequestMethod.GET)
    public CloudServiceInformation getServerInformation() {
        return new CloudServiceInformation(connectorProperties.getExternalHostName(), connectorProperties.getExternalPort());
    }
    
    @RequestMapping(value = "/startAgent/inventoryId/{inventoryId}/", method = RequestMethod.POST)
    public String startAgent(@PathVariable String inventoryId) {
        LOG.debug("startAgent {}", inventoryId);

        Equipment equipment = equipmentServiceInterface.getByInventoryIdOrNull(inventoryId);
        if(equipment == null) {
            throw new IllegalStateException("Cannot find equipment "+ inventoryId);
        }

        CEGWStartDebugEngine startAgentRequest = new CEGWStartDebugEngine(inventoryId, equipment.getId(),
                connectorProperties.getExternalHostName(), connectorProperties.getExternalPort());

        EquipmentCommandResponse cmdResponse = equipmentGatewayServiceInterface.sendCommand(startAgentRequest);
        LOG.debug("startAgent Response {}", cmdResponse);
        
        String result = (cmdResponse.getResultCode() == CEGWCommandResultCode.Success) ? "success"
                : ("Failure : " + cmdResponse.getResultCode() + " - " + cmdResponse.getResultDetail());

        return result;
    }

    @RequestMapping(value = "/stopAgent/inventoryId/{inventoryId}/", method = RequestMethod.POST)
    public String stopAgent(@PathVariable String inventoryId) {
        LOG.debug("stopAgent {}", inventoryId);

        Equipment equipment = equipmentServiceInterface.getByInventoryIdOrNull(inventoryId);
        if(equipment == null) {
            throw new IllegalStateException("Cannot find equipment "+ inventoryId);
        }

        CEGWStopDebugEngine stopAgentRequest = new CEGWStopDebugEngine(inventoryId, equipment.getId());

        EquipmentCommandResponse cmdResponse = equipmentGatewayServiceInterface.sendCommand(stopAgentRequest);
        LOG.debug("stopAgent Response {}", cmdResponse);
        
        String result = (cmdResponse.getResultCode() == CEGWCommandResultCode.Success) ? "success"
                : ("Failure : " + cmdResponse.getResultCode() + " - " + cmdResponse.getResultDetail());

        return result;

    }

    
    private PortForwarderWebSocketHandler getForwarder() {
        if (forwarderWebSocketHandler == null) {
            forwarderWebSocketHandler =
                    appContext.getBean("portForwardingHandler", PortForwarderWebSocketHandler.class);
        }

        return forwarderWebSocketHandler;
    }

}
