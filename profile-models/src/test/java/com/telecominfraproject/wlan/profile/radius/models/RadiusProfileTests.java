package com.telecominfraproject.wlan.profile.radius.models;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.server.exceptions.ConfigurationException;

public class RadiusProfileTests {
    private static final Logger LOG = LoggerFactory.getLogger(RadiusProfileTests.class);
    private RadiusProfile radiusProfile;

    @Before
    public void setup() throws Exception {
        radiusProfile = new RadiusProfile();
        RadiusServer primaryRadiusAuthServer = new RadiusServer();
        primaryRadiusAuthServer.setPort(1812);
        try {
            primaryRadiusAuthServer.setIpAddress(InetAddress.getByName("127.0.0.1"));
            primaryRadiusAuthServer.setPort(RadiusProfile.DEFAULT_RADIUS_AUTH_PORT);
            primaryRadiusAuthServer.setSecret("secret");
            primaryRadiusAuthServer.setTimeout(RadiusProfile.DEFAULT_RADIUS_TIMEOUT);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException(e);
        }
        radiusProfile.setPrimaryRadiusAuthServer(primaryRadiusAuthServer);

        RadiusServer primaryRadiusAccountingServer = new RadiusServer();
        primaryRadiusAccountingServer.setPort(1812);
        try {
            primaryRadiusAccountingServer.setIpAddress(InetAddress.getByName("127.0.0.1"));
            primaryRadiusAccountingServer.setPort(RadiusProfile.DEFAULT_RADIUS_ACCOUNTING_PORT);
            primaryRadiusAccountingServer.setSecret("secret");
            primaryRadiusAccountingServer.setTimeout(RadiusProfile.DEFAULT_RADIUS_TIMEOUT);
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException(e);
        }
        radiusProfile.setPrimaryRadiusAccountingServer(primaryRadiusAccountingServer);

    }

    @Test
    public void testJsonEncoding() throws Exception {
        String jsonDoc = radiusProfile.toString();
        assertNotNull(jsonDoc);
        LOG.debug(jsonDoc);
        RadiusProfile resultConfig = BaseJsonModel.fromString(jsonDoc, RadiusProfile.class);
        assertNotNull(resultConfig);
        assertTrue(radiusProfile.equals(resultConfig));
    }

    @Test
    public void testRadiusServerValication() throws Exception {
        RadiusServer rs = new RadiusServer();
        try {
            rs.validateConfig();
            fail("failed to detect missing IP address");
        } catch (ConfigurationException e) {
            // success
            LOG.debug(e.getLocalizedMessage());
        }
        try {
            rs.setIpAddress(InetAddress.getByAddress(new byte[] { 0, 0, 0, 0 }));
            rs.validateConfig();
            fail("failed to detect invalid server IP address");
        } catch (ConfigurationException e) {
            // success
            LOG.debug(e.getLocalizedMessage());
        }
        try {
            rs.setIpAddress(InetAddress.getByAddress(new byte[] { 10, 1, 1, 1 }));
            rs.validateConfig();
            fail("failed to detect empty shared secret");
        } catch (ConfigurationException e) {
            // success
            LOG.debug(e.getLocalizedMessage());
        }
        rs.setSecret("top!!!");
        rs.validateConfig();
    }

}
