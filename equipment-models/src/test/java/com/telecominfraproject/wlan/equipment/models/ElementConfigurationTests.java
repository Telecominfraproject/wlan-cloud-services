package com.telecominfraproject.wlan.equipment.models;

import static org.junit.Assert.assertEquals;

import java.net.InetAddress;
import java.util.List;

import org.junit.Test;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;


public class ElementConfigurationTests {

    @Test
    public void testApElementConfiguration() throws Exception {
        ApElementConfiguration origin = ApElementConfiguration.createWithDefaults();
        List<PeerInfo> list = origin.getPeerInfoList();
        PeerInfo peerInfo = new PeerInfo();
        peerInfo.setPeerIP(InetAddress.getByName("127.0.0.1"));
        peerInfo.setPeerMAC(new byte[] {1,2,3,4,5,6});
        peerInfo.setRadiusSecret("bla bla");
        list.add(peerInfo );
        
        String jsonStr = origin.toString();
        BaseJsonModel decoded = BaseJsonModel.fromString(jsonStr, ApElementConfiguration.class);
        assertEquals(origin, decoded);
    }

}
