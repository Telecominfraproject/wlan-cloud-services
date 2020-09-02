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
    
    private PortForwarderWebSocketHandler getForwarder() {
        if (forwarderWebSocketHandler == null) {
            forwarderWebSocketHandler =
                    appContext.getBean("portForwardingHandler", PortForwarderWebSocketHandler.class);
        }

        return forwarderWebSocketHandler;
    }

}
