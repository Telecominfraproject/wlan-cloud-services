package com.telecominfraproject.wlan.profile.radius.models;

import static org.junit.Assert.assertFalse;
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
        RadiusServer primaryRadiusServer = new RadiusServer();
        primaryRadiusServer.setAuthPort(1812);
        try {
            primaryRadiusServer.setIpAddress(InetAddress.getByName("192.168.0.1"));
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException(e);
        }
        primaryRadiusServer.setSecret("testing123");
        radiusProfile.setPrimaryRadiusServer(primaryRadiusServer);
        
        RadiusServer secondaryRadiusServer = new RadiusServer();
        secondaryRadiusServer.setAuthPort(1812);
        try {
            secondaryRadiusServer.setIpAddress(InetAddress.getByName("192.168.0.2"));
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException(e);
        }
        secondaryRadiusServer.setSecret("testing123");
        radiusProfile.setSecondaryRadiusServer(secondaryRadiusServer);
        

        RadiusSubnetConfiguration subnetConfig = new RadiusSubnetConfiguration();
        subnetConfig.setSubnetAddress(InetAddress.getByAddress(new byte[] { 10, 10, 1, 0 }));
        subnetConfig.setSubnetCidrPrefix(8);
        subnetConfig.setSubnetName(
                subnetConfig.getSubnetAddress().getHostAddress() + "/" + subnetConfig.getSubnetCidrPrefix());

        RadiusProxyConfiguration proxyConfig = new RadiusProxyConfiguration();
        proxyConfig.setFloatingIpAddress(InetAddress.getByAddress(new byte[] { 10, 10, 1, 2 }));
        proxyConfig.setFloatingIfVlan(10);
        proxyConfig.setSharedSecret("Test Secret");
        proxyConfig.setFloatingIfCidrPrefix(24);
        subnetConfig.setProxyConfig(proxyConfig);

        radiusProfile.setRadiusSubnetConfiguration(subnetConfig);
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

    @Test
    public void testSubnetOperation() throws Exception {
        InetAddress ip = InetAddress.getByAddress(new byte[] { (byte) 192, (byte) 168, (byte) 254, 1 });
        InetAddress subnet1 = InetAddress.getByAddress(new byte[] { (byte) 192, (byte) 168, (byte) 254, 1 });

        for (int i = 1; i <= 32; ++i) {
            assertTrue(RadiusProfile.isIpInSubnet(ip, subnet1, i));
        }
        assertTrue(RadiusProfile.isIpInSubnet(InetAddress.getByAddress(new byte[] {(byte) 192,(byte) 168,(byte) 254,2}), subnet1, 30));
        assertFalse(RadiusProfile.isIpInSubnet(InetAddress.getByAddress(new byte[] {(byte) 192,(byte) 168,(byte) 254,5}), subnet1, 30));
    }
    
    @Test
    public void subnetConfigTest() throws Exception {
        BaseSubnetConfiguration a = new BaseSubnetConfiguration();
        a.setSubnetAddress(InetAddress.getByName("172.16.255.1"));
        a.setSubnetCidrPrefix(24);
        BaseSubnetConfiguration b = a.clone();
        b.setSubnetCidrPrefix(23);
        assertTrue("overlap " + a + " and " + b, a.overlapSubnet(b));
        BaseSubnetConfiguration c = a.clone();
        c.setSubnetAddress(InetAddress.getByName("172.16.254.1"));
        assertFalse("not overlap " + a + " and " + c, a.overlapSubnet(c));
        c.setSubnetCidrPrefix(23);
        assertTrue("overlap " + a + " and " + c, a.overlapSubnet(c));
    }
}
