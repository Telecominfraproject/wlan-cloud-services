package com.telecominfraproject.wlan.profile.ssid.models;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.telecominfraproject.wlan.core.model.equipment.RadioType;

class RadioBasedSsidConfiguration2gTest {

    @Test
    void testNeedsToBeUpdatedOnDevice() {
        RadioBasedSsidConfiguration2g cfg1 = RadioBasedSsidConfiguration2g.generateDefault();
        assertFalse(cfg1.needsToBeUpdatedOnDevice(RadioBasedSsidConfiguration2g.generateDefault()));
        RadioBasedSsidConfiguration2g cfg2 = RadioBasedSsidConfiguration2g.generateDefault();
        cfg2.setEnable80211b(true);
        assert (cfg1.needsToBeUpdatedOnDevice(cfg2));
        assert (cfg1.needsToBeUpdatedOnDevice(RadioBasedSsidConfiguration.generateDefault(RadioType.is2dot4GHz)));

    }

    @Test
    void testEqualsObject() {
        RadioBasedSsidConfiguration2g cfg1 = RadioBasedSsidConfiguration2g.generateDefault();
        assert (cfg1.equals(RadioBasedSsidConfiguration2g.generateDefault()));
        RadioBasedSsidConfiguration2g cfg2 = RadioBasedSsidConfiguration2g.generateDefault();
        cfg2.setEnable80211b(true);
        assertFalse(cfg1.equals(cfg2));
    }

    @Test
    void testGenerateDefault() {
        RadioBasedSsidConfiguration2g cfg = RadioBasedSsidConfiguration2g.generateDefault();
        assert (cfg instanceof RadioBasedSsidConfiguration2g);
    }

    @Test
    void testGetEnable80211b() {
        RadioBasedSsidConfiguration2g cfg = RadioBasedSsidConfiguration2g.generateDefault();        
        assert(cfg.getEnable80211b().equals(Boolean.FALSE)); //default
    }

    @Test
    void testSetEnable80211b() {
        RadioBasedSsidConfiguration2g cfg = RadioBasedSsidConfiguration2g.generateDefault();        
        assert(cfg.getEnable80211b().equals(Boolean.FALSE)); //default
        cfg.setEnable80211b(Boolean.TRUE);
        assert(cfg.getEnable80211b().equals(Boolean.TRUE)); //default
    }

}
