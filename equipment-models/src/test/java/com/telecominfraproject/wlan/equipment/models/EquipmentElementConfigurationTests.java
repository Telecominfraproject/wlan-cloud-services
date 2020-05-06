package com.telecominfraproject.wlan.equipment.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Test;

import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.equipment.models.ApElementConfiguration.ApModel;


public class EquipmentElementConfigurationTests 
{
   @Test
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
      assertEquals(2, generated.getAdvancedRadioMap().size());
   }
 
   @Test
   /*
    * We want to make sure things are inherited.
    */
   public void testMinCellSize() throws Exception
   {
       ApElementConfiguration generated = (ApElementConfiguration) ApElementConfiguration.fromFile(EquipmentElementConfigurationTests.class.getResource("oldFormat.json").getFile(), ApElementConfiguration.class);
       
       ElementRadioConfiguration bgRadio = generated.getRadioMap().get(RadioType.is2dot4GHz);
       assertEquals(ElementRadioConfiguration.MIN_BG_RADIO_CELL_SIZE, bgRadio.getMinAutoCellSize());
       
       ElementRadioConfiguration acRadio = generated.getRadioMap().get(RadioType.is5GHz);
       assertEquals(ElementRadioConfiguration.MIN_AC_RADIO_CELL_SIZE, acRadio.getMinAutoCellSize());
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
       ElementRadioConfiguration radioConfig = apElemConfig.getRadioMap().get(RadioType.is5GHz);
       assertTrue(radioConfig.getActiveChannel() >= 149);
       assertTrue(radioConfig.getChannelNumber() >= 149);
       assertTrue(radioConfig.getBackupChannelNumber() >= 149);
       
   }
   
}
