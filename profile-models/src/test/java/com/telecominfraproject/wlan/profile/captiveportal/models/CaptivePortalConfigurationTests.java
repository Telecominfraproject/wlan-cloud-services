package com.telecominfraproject.wlan.profile.captiveportal.models;

import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class CaptivePortalConfigurationTests {

    @Test
    public void testAllowlistValidation() throws Exception {
        
        CaptivePortalConfiguration config = new CaptivePortalConfiguration();
        List<String> allowList = new ArrayList<>();
        
        // Test too many entries;
        for (int i = 0; i < 40; i++) {
            allowList.add("String" + i);
        }
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not be able to provision the allowlist with more than 32 entries.");
        
        // Test entry with a '/' character in it.
        allowList.add("http://lovelyDayToday.com");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a '/' character");
        
        // Test invalid values 0.0.0.0, 255.255.255.255, 127.0.0.1
        allowList.add("0.0.0.0");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with value 0.0.0.0");
        allowList.add("255.255.255.255");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with value 255.255.255.255");
        allowList.add("127.0.0.1");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with value 127.0.0.1");
        
        // Test with values outside the IPv4 range
        allowList.add("256.0.0.0");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value outside of 0 - 255");
        allowList.add("0.256.0.0");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value outside of 0 - 255");
        allowList.add("0.0.256.0");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value outside of 0 - 255");
        allowList.add("0.0.0.256");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value outside of 0 - 255");
        allowList.add("-1.0.0.0");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value outside of 0 - 255");
        allowList.add("0.-1.0.0");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value outside of 0 - 255");
        allowList.add("0.0.-1.0");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value outside of 0 - 255");
        allowList.add("0.0.0.-1");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value outside of 0 - 255");
        allowList.add("a.0.0.0");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value outside of 0 - 255");
        allowList.add("0.b.0.0");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value outside of 0 - 255");
        allowList.add("0.0.c.0");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value outside of 0 - 255");
        allowList.add("0.0.0.d");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value outside of 0 - 255");
        
        // Test with a valid IPv4 dot notation address:
        allowList.add("1.2.3.4");
        config.setWalledGardenAllowlist(allowList);
        CaptivePortalConfiguration.validateCaptivePortalProfile(config);
        
        // Test invalid values 0.0.0.0, 255.255.255.255, 127.0.0.1 in range format
        allowList.add("0.0.0.0-1.2.3.4");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with value 0.0.0.0");
        allowList.add("255.255.255.255-1.2.3.4");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with value 255.255.255.255");
        allowList.add("127.0.0.1-1.2.3.4");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with value 127.0.0.1");
        allowList.add("1.2.3.4-0.0.0.0");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with value 0.0.0.0");
        allowList.add("1.2.3.4-255.255.255.255");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with value 255.255.255.255");
        allowList.add("1.2.3.4-127.0.0.1");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with value 127.0.0.1");
        
        // Test with values outside the IPv4 allowed values in range format
        allowList.add("256.0.0.0-1.2.3.4");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value outside of 0 - 255");
        allowList.add("0.256.0.0-1.2.3.4");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value outside of 0 - 255");
        allowList.add("0.0.256.0-1.2.3.4");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value outside of 0 - 255");
        allowList.add("0.0.0.256-1.2.3.4");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value outside of 0 - 255");
        allowList.add("-1.0.0.0-1.2.3.4");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value outside of 0 - 255");
        allowList.add("0.-1.0.0-1.2.3.4");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value outside of 0 - 255");
        allowList.add("0.0.-1.0-1.2.3.4");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value outside of 0 - 255");
        allowList.add("0.0.0.-1-1.2.3.4");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value outside of 0 - 255");
        allowList.add("a.0.0.0-1.2.3.4");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value outside of 0 - 255");
        allowList.add("0.b.0.0-1.2.3.4");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value outside of 0 - 255");
        allowList.add("0.0.c.0-1.2.3.4");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value outside of 0 - 255");
        allowList.add("0.0.0.d-1.2.3.4");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value outside of 0 - 255");
        
        allowList.add("1.2.3.4-256.0.0.0");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value outside of 0 - 255");
        allowList.add("1.2.3.4-0.256.0.0");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value outside of 0 - 255");
        allowList.add("1.2.3.4-0.0.256.0");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value outside of 0 - 255");
        allowList.add("1.2.3.4-0.0.0.256");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value outside of 0 - 255");
        allowList.add("1.2.3.4--1.0.0.0");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value outside of 0 - 255");
        allowList.add("1.2.3.4-0.-1.0.0");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value outside of 0 - 255");
        allowList.add("1.2.3.4-0.0.-1.0");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value outside of 0 - 255");
        allowList.add("1.2.3.4-0.0.0.-1");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value outside of 0 - 255");
        allowList.add("1.2.3.4-a.0.0.0");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value outside of 0 - 255");
        allowList.add("1.2.3.4-0.b.0.0");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value outside of 0 - 255");
        allowList.add("1.2.3.4-0.0.c.0");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value outside of 0 - 255");
        allowList.add("1.2.3.4-0.0.0.d");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value outside of 0 - 255");
        
        // Test with a valid IPv4 range dot notation address:
        allowList.add("1.2.3.4-5.6.7.8");
        config.setWalledGardenAllowlist(allowList);
        CaptivePortalConfiguration.validateCaptivePortalProfile(config);
        
        // Test with invalid string urls that terminate with either the '.' or '*' characters.
        allowList.add("sweet.corn.");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value that ends with '.'");
        allowList.add("sweet.corn*");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value that ends with '*'");
        allowList.add("sweet.*");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value that ends with '*'");
        allowList.add("popped.sweet.corn.");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value that ends with '.'");
        allowList.add("popped.sweet.corn*");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value that ends with '*'");
        allowList.add("popped.sweet.*");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value that ends with '*'");
        allowList.add("hot.popped.sweet.corn.");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value that ends with '.'");
        allowList.add("hot.popped.sweet.corn*");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value that ends with '*'");
        allowList.add("hot.popped.sweet.*");
        config.setWalledGardenAllowlist(allowList);
        testCaptivePortalConfigValidity(config, allowList, "Should not beable to provision the allowlist with a value that ends with '*'");
        
        // Test some valid string urls.
        allowList.add("sweet.corn");
        config.setWalledGardenAllowlist(allowList);
        CaptivePortalConfiguration.validateCaptivePortalProfile(config);
        allowList.add("www.someurl0.com");
        config.setWalledGardenAllowlist(allowList);
        CaptivePortalConfiguration.validateCaptivePortalProfile(config);
        allowList.add("popped.sweet.corn");
        config.setWalledGardenAllowlist(allowList);
        CaptivePortalConfiguration.validateCaptivePortalProfile(config);
        allowList.add("hot.popped.sweet.corn");
        config.setWalledGardenAllowlist(allowList);
        CaptivePortalConfiguration.validateCaptivePortalProfile(config);
        allowList.add("*.sweet.corn");
        config.setWalledGardenAllowlist(allowList);
        CaptivePortalConfiguration.validateCaptivePortalProfile(config);
        allowList.add("*.*.sweet.corn");
        config.setWalledGardenAllowlist(allowList);
        CaptivePortalConfiguration.validateCaptivePortalProfile(config);
        allowList.add("*.*.*.corn");
        config.setWalledGardenAllowlist(allowList);
        CaptivePortalConfiguration.validateCaptivePortalProfile(config);
    }
    
    private void testCaptivePortalConfigValidity(CaptivePortalConfiguration config, List<String> allowList, String errorMsg){
        config.setWalledGardenAllowlist(allowList);
        try {
            CaptivePortalConfiguration.validateCaptivePortalProfile(config);
            fail(errorMsg);
        } catch(Exception expected) { 
            // expected
        }
        allowList.clear();
    }
}
