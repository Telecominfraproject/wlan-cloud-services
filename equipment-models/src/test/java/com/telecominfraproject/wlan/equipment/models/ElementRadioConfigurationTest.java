package com.telecominfraproject.wlan.equipment.models;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import com.telecominfraproject.wlan.core.model.equipment.RadioType;


public class ElementRadioConfigurationTest 
{
   @Test
   /**
    * Code was written that assumes a fresh instance of a 
    * ElementRadioConfiguration will always have an instantiated
    * list of banned channels.
    * 
    * Adding this test as a flag for anyone who changes that assumption.
    * 
    */
   public void neverANullBannedList() 
   {
      for(RadioType type : RadioType.validValues())
      {
         ElementRadioConfiguration config = ElementRadioConfiguration.createWithDefaults(type);
         assertNotNull(config.getBannedChannels());
      }
   }
   
   @Test
   /**
    * Code was written that assumes a fresh instance of a 
    * ElementRadioConfiguration will always have an instantiated
    * list of banned channels.
    * 
    * Adding this test as a flag for anyone who changes that assumption.
    * 
    */
   public void neverANullAllowedList() 
   {
      for(RadioType type : RadioType.validValues())
      {
         ElementRadioConfiguration config = ElementRadioConfiguration.createWithDefaults(type);
         assertNotNull(config.getAllowedChannels());
      }
   }
   
}
