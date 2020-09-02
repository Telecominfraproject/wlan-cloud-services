package com.telecominfraproject.wlan.portforwardinggateway;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import com.telecominfraproject.wlan.core.client.BaseRemoteClient;
import com.telecominfraproject.wlan.core.model.service.CloudServiceInformation;

/**
 * @author dtop
 *
 */
@Configuration
public class PortForwarderGatewayRemote extends BaseRemoteClient implements PortForwarderGatewayInterface {

    private static final Logger LOG = LoggerFactory.getLogger(PortForwarderGatewayRemote.class);
    private static final ParameterizedTypeReference<List<String>> stringListClassToken = new ParameterizedTypeReference<List<String>>() {};
    
    private String baseUrl;

    @Override
    public String createSession(String inventoryId, int port) {
        //@RequestMapping(value = "/createSession/inventoryId/{inventoryId}/port/{port}/", method = RequestMethod.POST)
        
        LOG.debug("createSession({}, {})", inventoryId, port);

        HttpEntity<String> request = new HttpEntity<>("", headers);

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(getBaseUrl()
                + "/createSession/inventoryId/{inventoryId}/port/{port}/", request, String.class, inventoryId, port);

        String result = responseEntity.getBody();

        LOG.debug("createSession returned {}", result);
        return result;
        
    }

    @Override
    public void stopSession(String sessionId) {
        //@RequestMapping(value = "/stopSession/{sessionId}/", method = RequestMethod.POST)
        LOG.debug("stopSession({})", sessionId);

        HttpEntity<String> request = new HttpEntity<>("", headers);

        restTemplate.postForEntity(getBaseUrl()
                + "/stopSession/{sessionId}/", request, String.class, sessionId);

        LOG.debug("stopSession done");
    }

    @Override
    public List<String> listSessions() {
        //@RequestMapping(value = "/listSessions/", method = RequestMethod.GET)
        LOG.debug("listSessions()");

        ResponseEntity<List<String>> responseEntity = restTemplate.exchange(
                getBaseUrl()+"/listSessions/", 
                HttpMethod.GET,
                null, stringListClassToken);

        List<String> result = responseEntity.getBody();
        LOG.debug("listSessions returned {} entries", (result == null) ? 0 : result.size());
        LOG.trace("listSessions returned {}", result);
        return result;
    }
    
    @Override
    public String agentStatus(String inventoryId) {
        //@RequestMapping(value = "/agentStatus/inventoryId/{inventoryId}/", method = RequestMethod.GET)
        LOG.debug("agentStatus({})", inventoryId);

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(getBaseUrl()
                + "/agentStatus/inventoryId/{inventoryId}/", String.class, inventoryId);

        String result = responseEntity.getBody();
        
        LOG.debug("agentStatus({}) : {}", inventoryId, result);
        return result;
    }
    
    
    public String getBaseUrl() {
        if (baseUrl == null) {
            baseUrl = environment.getProperty(
                    "tip.wlan.portForwarderGatewayBaseUrl").trim()
                    + "/api/portFwd";
        }
        return baseUrl;
    }

    @Override
    public CloudServiceInformation getServerInformation() {
        LOG.debug("getServerInformation()");
        // @RequestMapping(value = "/agentStatus/websocket/", method = RequestMethod.GET)
        ResponseEntity<CloudServiceInformation> responseEntity =
                restTemplate.getForEntity(getBaseUrl() + "/agentStatus/websocket/", CloudServiceInformation.class);
        CloudServiceInformation result = responseEntity.getBody();
        LOG.debug("getServerInformation() returns {}", result);
        return result;
    }

}
