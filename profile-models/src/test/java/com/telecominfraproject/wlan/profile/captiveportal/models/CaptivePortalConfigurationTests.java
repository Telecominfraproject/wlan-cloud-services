package com.telecominfraproject.wlan.profile.captiveportal.models;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class CaptivePortalConfigurationTests {

    @Test
    public void testWhitelistValidation() throws Exception {
        
        CaptivePortalConfiguration config = new CaptivePortalConfiguration();
        List<String> whiteList = new ArrayList<>();
        
        // Test too many entries;
        for (int i = 0; i < 40; i++) {
            whiteList.add("String" + i);
        }
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not be able to provision the whitelist with more than 32 entries.");
        
        // Test entry with a '/' character in it.
        whiteList.add("http://lovelyDayToday.com");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a '/' character");
        
        // Test invalid values 0.0.0.0, 255.255.255.255, 127.0.0.1
        whiteList.add("0.0.0.0");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with value 0.0.0.0");
        whiteList.add("255.255.255.255");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with value 255.255.255.255");
        whiteList.add("127.0.0.1");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with value 127.0.0.1");
        
        // Test with values outside the IPv4 range
        whiteList.add("256.0.0.0");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value outside of 0 - 255");
        whiteList.add("0.256.0.0");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value outside of 0 - 255");
        whiteList.add("0.0.256.0");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value outside of 0 - 255");
        whiteList.add("0.0.0.256");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value outside of 0 - 255");
        whiteList.add("-1.0.0.0");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value outside of 0 - 255");
        whiteList.add("0.-1.0.0");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value outside of 0 - 255");
        whiteList.add("0.0.-1.0");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value outside of 0 - 255");
        whiteList.add("0.0.0.-1");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value outside of 0 - 255");
        whiteList.add("a.0.0.0");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value outside of 0 - 255");
        whiteList.add("0.b.0.0");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value outside of 0 - 255");
        whiteList.add("0.0.c.0");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value outside of 0 - 255");
        whiteList.add("0.0.0.d");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value outside of 0 - 255");
        
        // Test with a valid IPv4 dot notation address:
        whiteList.add("1.2.3.4");
        config.setWalledGardenWhitelist(whiteList);
        CaptivePortalConfiguration.validateCaptivePortalProfile(config);
        
        // Test invalid values 0.0.0.0, 255.255.255.255, 127.0.0.1 in range format
        whiteList.add("0.0.0.0-1.2.3.4");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with value 0.0.0.0");
        whiteList.add("255.255.255.255-1.2.3.4");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with value 255.255.255.255");
        whiteList.add("127.0.0.1-1.2.3.4");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with value 127.0.0.1");
        whiteList.add("1.2.3.4-0.0.0.0");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with value 0.0.0.0");
        whiteList.add("1.2.3.4-255.255.255.255");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with value 255.255.255.255");
        whiteList.add("1.2.3.4-127.0.0.1");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with value 127.0.0.1");
        
        // Test with values outside the IPv4 allowed values in range format
        whiteList.add("256.0.0.0-1.2.3.4");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value outside of 0 - 255");
        whiteList.add("0.256.0.0-1.2.3.4");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value outside of 0 - 255");
        whiteList.add("0.0.256.0-1.2.3.4");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value outside of 0 - 255");
        whiteList.add("0.0.0.256-1.2.3.4");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value outside of 0 - 255");
        whiteList.add("-1.0.0.0-1.2.3.4");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value outside of 0 - 255");
        whiteList.add("0.-1.0.0-1.2.3.4");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value outside of 0 - 255");
        whiteList.add("0.0.-1.0-1.2.3.4");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value outside of 0 - 255");
        whiteList.add("0.0.0.-1-1.2.3.4");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value outside of 0 - 255");
        whiteList.add("a.0.0.0-1.2.3.4");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value outside of 0 - 255");
        whiteList.add("0.b.0.0-1.2.3.4");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value outside of 0 - 255");
        whiteList.add("0.0.c.0-1.2.3.4");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value outside of 0 - 255");
        whiteList.add("0.0.0.d-1.2.3.4");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value outside of 0 - 255");
        
        whiteList.add("1.2.3.4-256.0.0.0");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value outside of 0 - 255");
        whiteList.add("1.2.3.4-0.256.0.0");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value outside of 0 - 255");
        whiteList.add("1.2.3.4-0.0.256.0");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value outside of 0 - 255");
        whiteList.add("1.2.3.4-0.0.0.256");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value outside of 0 - 255");
        whiteList.add("1.2.3.4--1.0.0.0");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value outside of 0 - 255");
        whiteList.add("1.2.3.4-0.-1.0.0");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value outside of 0 - 255");
        whiteList.add("1.2.3.4-0.0.-1.0");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value outside of 0 - 255");
        whiteList.add("1.2.3.4-0.0.0.-1");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value outside of 0 - 255");
        whiteList.add("1.2.3.4-a.0.0.0");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value outside of 0 - 255");
        whiteList.add("1.2.3.4-0.b.0.0");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value outside of 0 - 255");
        whiteList.add("1.2.3.4-0.0.c.0");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value outside of 0 - 255");
        whiteList.add("1.2.3.4-0.0.0.d");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value outside of 0 - 255");
        
        // Test with a valid IPv4 range dot notation address:
        whiteList.add("1.2.3.4-5.6.7.8");
        config.setWalledGardenWhitelist(whiteList);
        CaptivePortalConfiguration.validateCaptivePortalProfile(config);
        
        // Test with invalid string urls that terminate with either the '.' or '*' characters.
        whiteList.add("sweet.corn.");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value that ends with '.'");
        whiteList.add("sweet.corn*");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value that ends with '*'");
        whiteList.add("sweet.*");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value that ends with '*'");
        whiteList.add("popped.sweet.corn.");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value that ends with '.'");
        whiteList.add("popped.sweet.corn*");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value that ends with '*'");
        whiteList.add("popped.sweet.*");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value that ends with '*'");
        whiteList.add("hot.popped.sweet.corn.");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value that ends with '.'");
        whiteList.add("hot.popped.sweet.corn*");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value that ends with '*'");
        whiteList.add("hot.popped.sweet.*");
        config.setWalledGardenWhitelist(whiteList);
        testCaptivePortalConfigValidity(config, whiteList, "Should not beable to provision the whitelist with a value that ends with '*'");
        
        // Test some valid string urls.
        whiteList.add("sweet.corn");
        config.setWalledGardenWhitelist(whiteList);
        CaptivePortalConfiguration.validateCaptivePortalProfile(config);
        whiteList.add("www.someurl0.com");
        config.setWalledGardenWhitelist(whiteList);
        CaptivePortalConfiguration.validateCaptivePortalProfile(config);
        whiteList.add("popped.sweet.corn");
        config.setWalledGardenWhitelist(whiteList);
        CaptivePortalConfiguration.validateCaptivePortalProfile(config);
        whiteList.add("hot.popped.sweet.corn");
        config.setWalledGardenWhitelist(whiteList);
        CaptivePortalConfiguration.validateCaptivePortalProfile(config);
        whiteList.add("*.sweet.corn");
        config.setWalledGardenWhitelist(whiteList);
        CaptivePortalConfiguration.validateCaptivePortalProfile(config);
        whiteList.add("*.*.sweet.corn");
        config.setWalledGardenWhitelist(whiteList);
        CaptivePortalConfiguration.validateCaptivePortalProfile(config);
        whiteList.add("*.*.*.corn");
        config.setWalledGardenWhitelist(whiteList);
        CaptivePortalConfiguration.validateCaptivePortalProfile(config);
    }
    
    private void testCaptivePortalConfigValidity(CaptivePortalConfiguration config, List<String> whiteList, String errorMsg){
        config.setWalledGardenWhitelist(whiteList);
        try {
            CaptivePortalConfiguration.validateCaptivePortalProfile(config);
            fail(errorMsg);
        } catch(Exception expected) { }
        whiteList.clear();
    }
}
