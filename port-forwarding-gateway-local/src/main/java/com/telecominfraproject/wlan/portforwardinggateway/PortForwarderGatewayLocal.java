package com.telecominfraproject.wlan.portforwardinggateway;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import com.telecominfraproject.wlan.core.model.service.CloudServiceInformation;
import com.telecominfraproject.wlan.portforwardinggateway.controller.PortForwarderController;

/**
 * @author dtop
 *
 */
@Configuration
public class PortForwarderGatewayLocal implements PortForwarderGatewayInterface {

    @Autowired private PortForwarderController portForwarderController;
    
    @Override
    public String createSession(String inventoryId, int port) {
        return portForwarderController.createSession(inventoryId, port);
    }

    @Override
    public void stopSession(String sessionId) {
        portForwarderController.stopSession(sessionId);
    }

    @Override
    public List<String> listSessions() {
        return portForwarderController.listSessions();
    }

    @Override
    public String agentStatus(String inventoryId) {
        return portForwarderController.agentStatus(inventoryId);
    }

    @Override
    public CloudServiceInformation getServerInformation() {
        return portForwarderController.getServerInformation();
    }
    
    @Override
    public String startAgent(String inventoryId) {
        return portForwarderController.startAgent(inventoryId);
    }

    @Override
    public String stopAgent(String inventoryId) {
        return portForwarderController.stopAgent(inventoryId);
    }
}
