package com.telecominfraproject.wlan.equipment.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Ignore;
import org.junit.Test;

import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.equipment.models.ApElementConfiguration.ApModel;


public class EquipmentElementConfigurationTests 
{
   @Test
   @Ignore("needs further review - do we always initialize ApElementConfiguration with defaults or do we require explicit initialization (like right now)")
   public void testMigrationFromOldFormat() throws IOException
   {
      String oldJsonFilename = EquipmentElementConfigurationTests.class.getResource("oldFormat.json").getFile();
      
      String jsonPayload = com.google.common.io.Files.toString(new File(oldJsonFilename), Charset.defaultCharset());

      /*
       * We make sure we will still have valid radio maps after loading old data
       */
      ApElementConfiguration generated = (ApElementConfiguration) ApElementConfiguration.fromString(jsonPayload, ApElementConfiguration.class);
      
      assertNotNull(generated.getRadioMap());
      assertEquals(2, generated.getRadioMap().size());

      assertNotNull(generated.getAdvancedRadioMap());
      assertEquals(3, generated.getAdvancedRadioMap().size());
   }
 
   @Test
   public void weTestTheClone() throws Exception
   {
       ApElementConfiguration config = ApElementConfiguration.createWithDefaults();
       assertEquals(config, config.clone());
   }
   
   
   @Test
   public void testOutdoorChannels()
   {
       ApElementConfiguration apElemConfig = ApElementConfiguration.createWithDefaults("xxx", ApModel.OUTDOOR);
       ElementRadioConfiguration radioConfig = apElemConfig.getRadioMap().get(RadioType.is5GHzU);
       assertTrue(radioConfig.getActiveChannel(true) >= 149);
       assertTrue(radioConfig.getChannelNumber() >= 149);
       assertTrue(radioConfig.getBackupChannelNumber() >= 157);
       assertTrue(radioConfig.getActiveBackupChannel(true) >= 157);
       
   }
   
}
