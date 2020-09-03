package com.telecominfraproject.wlan.portforwardinggateway;

import java.util.List;

import com.telecominfraproject.wlan.core.model.service.CloudServiceInformation;

/**
 * @author dtop
 *
 */
public interface PortForwarderGatewayInterface {
    String createSession(String inventoryId, int port);

    void stopSession(String sessionId);

    List<String> listSessions();

    String agentStatus(String inventoryId);
    String startAgent(String inventoryId);
    String stopAgent(String inventoryId);

    /**
     * Return the service information
     * 
     * @return
     */
    CloudServiceInformation getServerInformation();
}
