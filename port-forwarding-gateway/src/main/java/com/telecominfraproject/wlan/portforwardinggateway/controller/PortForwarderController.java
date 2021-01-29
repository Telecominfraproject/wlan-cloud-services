package com.telecominfraproject.wlan.portforwardinggateway.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.telecominfraproject.wlan.cloudeventdispatcher.CloudEventDispatcherInterface;
import com.telecominfraproject.wlan.core.model.service.CloudServiceInformation;
import com.telecominfraproject.wlan.core.server.container.ConnectorProperties;
import com.telecominfraproject.wlan.equipment.EquipmentServiceInterface;
import com.telecominfraproject.wlan.equipment.models.Equipment;
import com.telecominfraproject.wlan.equipmentgateway.models.CEGWCommandResultCode;
import com.telecominfraproject.wlan.equipmentgateway.models.CEGWStartDebugEngine;
import com.telecominfraproject.wlan.equipmentgateway.models.CEGWStopDebugEngine;
import com.telecominfraproject.wlan.equipmentgateway.models.EquipmentCommandResponse;
import com.telecominfraproject.wlan.equipmentgateway.service.EquipmentGatewayServiceInterface;
import com.telecominfraproject.wlan.portforwardinggateway.websocket.ForwarderSession;
import com.telecominfraproject.wlan.portforwardinggateway.websocket.PortForwarderWebSocketHandler;
import com.telecominfraproject.wlan.status.StatusServiceInterface;
import com.telecominfraproject.wlan.status.equipment.models.EquipmentDebugSessionStatusData;
import com.telecominfraproject.wlan.status.models.Status;
import com.telecominfraproject.wlan.status.models.StatusDetails;
import com.telecominfraproject.wlan.systemevent.equipment.debug.EquipmentDebugSessionStartedEvent;
import com.telecominfraproject.wlan.systemevent.equipment.debug.EquipmentDebugSessionStoppedEvent;
import com.telecominfraproject.wlan.systemevent.models.SystemEvent;
import com.telecominfraproject.wlan.systemevent.models.SystemEventRecord;

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
    private StatusServiceInterface statusServiceInterface;
    @Autowired
    private CloudEventDispatcherInterface cloudEventDispatcherInterface;
    

    @Autowired
    private EquipmentServiceInterface equipmentServiceInterface;

    @RequestMapping(value = "/createSession/inventoryId/{inventoryId}/port/{port}/", method = RequestMethod.POST)
    public String createSession(@PathVariable String inventoryId, @PathVariable int port) {
        LOG.info("createSession({}, {})", inventoryId, port);
        
        Equipment equipment = equipmentServiceInterface.getByInventoryIdOrNull(inventoryId);
        if(equipment == null) {
            throw new IllegalStateException("Cannot find equipment " + inventoryId);
        }
        
        if(equipment.getCustomerId() == 0) {
            throw new IllegalStateException("Equipment " + inventoryId + " does not belong to any customer");
        }
        
        //append external host name to the local session Id
        String externalHostName = connectorProperties.getExternallyVisibleHostName();
        String result = getForwarder().startForwarderSession(inventoryId, port) + "@" + externalHostName;

        //extract the port on which the gateway listens for ssh requests
        int portForwarderGatewayPort = Integer.parseInt(result.substring(0, result.indexOf('-')));
        
        //update the equipment status to indicate that debug session has been started on it
        Status status = new Status();
        status.setCustomerId(equipment.getCustomerId());
        status.setEquipmentId(equipment.getId());
        status.setLastModifiedTimestamp(System.currentTimeMillis());
        StatusDetails details = new EquipmentDebugSessionStatusData(getCurrentlyAuthenticatedUser(), portForwarderGatewayPort, port);
        status.setDetails(details );
        status = statusServiceInterface.update(status);
        
        //create system event to record that debug session has been started on the equipment
        SystemEvent systemEvent = new EquipmentDebugSessionStartedEvent(equipment.getCustomerId(), equipment.getId(),
                status.getLastModifiedTimestamp(), getCurrentlyAuthenticatedUser(), portForwarderGatewayPort, port);
        
        SystemEventRecord eventRecord =  new SystemEventRecord(systemEvent);
        eventRecord.setLocationId(equipment.getLocationId());
        cloudEventDispatcherInterface.publishEvent(eventRecord);
        
        LOG.info("createSession({}, {}) returns {}", inventoryId, port, result);
        
        return result;
    }

    @RequestMapping(value = "/stopSession/{sessionId}/", method = RequestMethod.POST)
    public void stopSession(@PathVariable String sessionId) {
        LOG.info("stopSession({})", sessionId);
        String localSessionId = sessionId.indexOf('@')>0 ? sessionId.substring(0, sessionId.indexOf('@')) : sessionId;
        ForwarderSession forwarderSession = getForwarder().getForwardingSession(localSessionId);
        
        if(forwarderSession==null){
            throw new IllegalStateException("Could not find session " + localSessionId);
        }

        getForwarder().stopForwarderSession(localSessionId);
        LOG.info("stopSession({}) ", localSessionId);

        Equipment equipment = equipmentServiceInterface.getByInventoryIdOrNull(forwarderSession.getInventoryId());
        if(equipment == null) {
            LOG.warn("Cannot find equipment {} ", forwarderSession.getInventoryId());
            return;
        }
        
        if(equipment.getCustomerId() == 0) {
            LOG.warn("Equipment {} does not belong to any customer", forwarderSession.getInventoryId());
            return;
        }

        int portForwarderGatewayPort = forwarderSession.getListenOnLocalPort();
        int port = forwarderSession.getConnectToPortOnEquipment();
        
        //update the equipment status to indicate that debug session has been stopped on it
        Status status = new Status();
        status.setCustomerId(equipment.getCustomerId());
        status.setEquipmentId(equipment.getId());
        status.setLastModifiedTimestamp(System.currentTimeMillis());
        StatusDetails details = new EquipmentDebugSessionStatusData(null, -1, -1);
        status.setDetails(details );
        status = statusServiceInterface.update(status);
        
        //create system event to record that debug session has been stopped on the equipment
        SystemEvent systemEvent = new EquipmentDebugSessionStoppedEvent(equipment.getCustomerId(), equipment.getId(),
                status.getLastModifiedTimestamp(), getCurrentlyAuthenticatedUser(), portForwarderGatewayPort, port);
        
        SystemEventRecord eventRecord =  new SystemEventRecord(systemEvent);
        eventRecord.setLocationId(equipment.getLocationId());
        cloudEventDispatcherInterface.publishEvent(eventRecord);


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
        return new CloudServiceInformation(connectorProperties.getExternallyVisibleHostName(), connectorProperties.getExternallyVisiblePort());
    }
    
    @RequestMapping(value = "/startAgent/inventoryId/{inventoryId}/", method = RequestMethod.POST)
    public String startAgent(@PathVariable String inventoryId) {
        LOG.debug("startAgent {}", inventoryId);

        Equipment equipment = equipmentServiceInterface.getByInventoryIdOrNull(inventoryId);
        if(equipment == null) {
            throw new IllegalStateException("Cannot find equipment "+ inventoryId);
        }

        CEGWStartDebugEngine startAgentRequest = new CEGWStartDebugEngine(inventoryId, equipment.getId(),
                connectorProperties.getExternallyVisibleHostName(), connectorProperties.getExternallyVisiblePort());

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

    /**
     * Get the user name of authenticated user
     * 
     * @return username or null
     */
    public String getCurrentlyAuthenticatedUser() {
        Object principalObject = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ((null != principalObject) && (principalObject instanceof UserDetails)) {
            UserDetails userDetails = (UserDetails) principalObject;

            if ((userDetails.getUsername() != null)) {
                return userDetails.getUsername();
            }
        }
        return null;
    }
}
