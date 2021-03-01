package com.telecominfraproject.wlan.profile.ssid.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.profile.ssid.models.WepConfiguration.WepAuthType;
import com.telecominfraproject.wlan.profile.ssid.models.WepKey.WepType;

public class SsidConfigurationTests 
{

   @Test
   /*
    * In this test, we need to set the wireless profile name (and ssid) since, 
    * by default, the contain random values.
    */
   public void makeSureWeDontPushAConfigurationWithADifferentProfileName()
   {
      SsidConfiguration ssidConfig = SsidConfiguration.createWithDefaults();
      ssidConfig.setSsid("My SSID");

      SsidConfiguration anotherSsidConfig = SsidConfiguration.createWithDefaults();
      anotherSsidConfig.setSsid("My SSID");

      assertFalse(ssidConfig.needsToBeUpdatedOnDevice(anotherSsidConfig));
      
      ssidConfig.setCaptivePortalId(500L);

      assertTrue(ssidConfig.needsToBeUpdatedOnDevice(anotherSsidConfig));

   }

   @Test
   public void testChangesIn80211kvr()
   {
       SsidConfiguration ssidConfig = SsidConfiguration.createWithDefaults();
       ssidConfig.setSsid("TestSSID");
       SsidConfiguration anotherSsidConfig = SsidConfiguration.createWithDefaults();
       anotherSsidConfig.setSsid("TestSSID");
       assertFalse(ssidConfig.needsToBeUpdatedOnDevice(anotherSsidConfig));

       anotherSsidConfig.getRadioBasedConfigs().get(RadioType.is2dot4GHz).setEnable80211k(false);
       assertTrue(ssidConfig.needsToBeUpdatedOnDevice(anotherSsidConfig));
       
   }
   
   @Test
   public void wepConfigTests() throws Exception {
       WepConfiguration wepConfig = WepConfiguration.createWithDefaults();
       assertNotNull(wepConfig);
       assertEquals(WepConfiguration.DEFAULT_PRIMARY_TX_KEY_ID, wepConfig.getPrimaryTxKeyId());
       assertEquals(WepAuthType.open, wepConfig.getWepAuthType());
       assertEquals(4,  wepConfig.getWepKeys().length);
       for (int i = 0; i < wepConfig.getWepKeys().length; i++) {
           wepConfig.getWepKeys()[i].validateWepKey();
       }
       
       WepKey goodAsciiKey64 = new WepKey("hello", WepType.wep64);
       testBadWepKeyCreation("bad+=", WepType.wep64);
       testBadWepKeyCreation("bad", WepType.wep64);
       
       WepKey goodAsciiKey128 = new WepKey("hellothereyou", WepType.wep128);
       testBadWepKeyCreation("hellothere_-+", WepType.wep128);
       testBadWepKeyCreation("badke", WepType.wep128);
       
       WepKey goodHexKey64 = new WepKey("AaBbCc0011", WepType.wep64);
       testBadWepKeyCreation("{}*(djaswx", WepType.wep64);
       testBadWepKeyCreation("Aab123", WepType.wep64);
       
       WepKey goodHexKey128 = new WepKey("AaBbCc0011DdEeFf2233556677", WepType.wep128);
       testBadWepKeyCreation("{}*(AaBbCc0011DdEeRr223355GgXx", WepType.wep128);
       testBadWepKeyCreation("AaBbCc0011DdEeRr223355Gg778976", WepType.wep128);
       
       // Validate the good keys...all should pass.
       goodAsciiKey64.validateWepKey();
       goodAsciiKey128.validateWepKey();
       goodHexKey64.validateWepKey();
       goodHexKey128.validateWepKey();
       
       
       // Now let's change the key of the key at index 0, all keys should change to match.
       wepConfig.setWepKeyAtIndex(goodAsciiKey128, 0);
       for (int i = 0; i < wepConfig.getWepKeys().length; i++) {
           assertEquals(goodAsciiKey128, wepConfig.getWepKeys()[i]);
       }
       
       // Test changing a different index with a different key...all keys should update again.
       wepConfig.setWepKeyAtIndex(goodHexKey64, 0);
       for (int i = 0; i < wepConfig.getWepKeys().length; i++) {
           assertEquals(goodHexKey64, wepConfig.getWepKeys()[i]);
       }
       
       // Try to test a key add at invalid indices:
       testBadKeyInsertExpectExceptionAndSwallowIt(wepConfig, goodAsciiKey64, goodHexKey64, -1);
       testBadKeyInsertExpectExceptionAndSwallowIt(wepConfig, goodAsciiKey64, goodHexKey64, 5);
       
       // Try addition of a null key.
       testBadKeyInsertExpectExceptionAndSwallowIt(wepConfig, null, goodHexKey64, 1);
       
       // Also test to make sure the ASCII to hex char converter is working:
       String helloInHex = "68656C6C6F";
       assertEquals(helloInHex, WepKey.convertAsciiKeyToHex("hello").toUpperCase());
   }
   
   @Test
   public void testWepConfigSerialization() throws Exception
   {
       String config = "{\"_type\":\"WepConfiguration\", \"primaryTxKeyId\":1,\"wepKeys\":[{\"_type\":\"WepKey\",\"txKey\":\"hello\",\"txKeyConverted\":\"68656c6c6f\",\"txKeyType\":\"wep64\"},{\"_type\":\"WepKey\",\"txKey\":\"hello\",\"txKeyConverted\":\"68656c6c6f\",\"txKeyType\":\"wep64\"},{\"_type\":\"WepKey\",\"txKey\":\"hello\",\"txKeyConverted\":\"68656c6c6f\",\"txKeyType\":\"wep64\"},{\"_type\":\"WepKey\",\"txKey\":\"hello\",\"txKeyConverted\":\"68656c6c6f\",\"txKeyType\":\"wep64\"}], \"wepAuthType\":\"open\"}";
   
       WepConfiguration object = WepConfiguration.fromString(config, WepConfiguration.class);
       for (int i = 0; i < 4; i++) {
           assertEquals("hello", object.getWepKeys()[i].getTxKey());
           assertEquals("68656c6c6f", object.getWepKeys()[i].getTxKeyConverted());
       }
   }
   
   @Test
   public void testWebConfigClone() throws Exception
   {
       WepConfiguration wepConfig = WepConfiguration.createWithDefaults();
       WepConfiguration clone = wepConfig.clone();
       
       assertEquals(wepConfig, clone);
       
       WepKey[] keys = wepConfig.getWepKeys();
       for (int i = 0; i < keys.length; i++) {
           if (keys[i] != null) {
               if (keys[i].getTxKey() != null) {
                   keys[i].setTxKey("AAAAA");
               }

               if (keys[i].getTxKeyConverted() != null) {
                   keys[i].setTxKeyConverted("AAAAA");
               }
           }
       }
       
       assertFalse(wepConfig.equals(clone));
       for (int i = 0; i < keys.length; i++) {
           assertEquals("AAAAA", wepConfig.getWepKeys()[i].getTxKey());
           assertFalse("AAAAA".equals(clone.getWepKeys()[i].getTxKey()));
       }
   }
   
   @Test
   public void testUnderSsidLimit()
   {
       SsidConfiguration ssidConfig = SsidConfiguration.createWithDefaults();
       String filled = "a";
       String shortSsid = filled.repeat(SsidConfiguration.MAX_SSID_LENGTH - 1);
       ssidConfig.setSsid(shortSsid);

       ssidConfig.hasUnsupportedValue();
   }
   
   @Test
   public void testEqualToSsidLimit()
   {
       SsidConfiguration ssidConfig = SsidConfiguration.createWithDefaults();
       String filled = "a";
       String justRightSsid = filled.repeat(SsidConfiguration.MAX_SSID_LENGTH);
       ssidConfig.setSsid(justRightSsid);

       ssidConfig.hasUnsupportedValue();
   }
   
   @Test(expected = SsidExceedsMaxLengthException.class)
   public void testExceedsSsidLimit()
   {
       SsidConfiguration ssidConfig = SsidConfiguration.createWithDefaults();
       String filled = "a";
       String longSsid = filled.repeat(SsidConfiguration.MAX_SSID_LENGTH + 1);
       ssidConfig.setSsid(longSsid);

       ssidConfig.hasUnsupportedValue();
   }
   
   private void testBadKeyInsertExpectExceptionAndSwallowIt(WepConfiguration config, WepKey key, WepKey expectedResult, int index) {
       try {
           config.setWepKeyAtIndex(key, index);
       } catch (Exception e) {
           // expected it
       }
       
       for (int i = 0; i < config.getWepKeys().length; i++) {
           assertEquals(expectedResult, config.getWepKeys()[i]);
       }
   }
   
   private void testBadWepKeyCreation(String key, WepType type) {
       try {
           new WepKey(key, type);
       } catch (Exception e) {
           // expected
       }
   }
}
