package com.telecominfraproject.wlan.equipment.models;

import static org.junit.Assert.fail;

import org.junit.Test;

import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.server.exceptions.ConfigurationException;

public class RadioConfigurationTests 
{   
   @Test
   public void validationTest() {
       RadioConfiguration radioConfig2_4 = RadioConfiguration.createWithDefaults(RadioType.is2dot4GHz);
       RadioConfiguration radioConfig5 = RadioConfiguration.createWithDefaults(RadioType.is5GHz);
       radioConfig2_4.validate();
       radioConfig5.validate();
       
       // Validate defaults
       radioConfig2_4.setRadioMode(RadioMode.modeGN);
       radioConfig5.setRadioMode(RadioMode.modeGN);
       
       // Should fail on 5G radio
       radioConfig2_4.validate();
       try {
           radioConfig5.validate();
           fail("Should not be able to set 5G radio to mode GN");
       } catch (ConfigurationException expected) {}
       
       radioConfig2_4.setRadioMode(RadioMode.modeAC);
       radioConfig5.setRadioMode(RadioMode.modeAC);
       
       // Should fail on 2.4G radio
       try {
           radioConfig2_4.validate();
           fail("Should not be able to set 2.4G radio to mode AC");
       } catch (ConfigurationException expected) {}
       radioConfig5.validate();
       
       radioConfig2_4.setRadioMode(RadioMode.modeN);
       radioConfig5.setRadioMode(RadioMode.modeN);
       
       // Should have no failures for mode N
       radioConfig2_4.setRadioMode(RadioMode.modeGN);
       radioConfig5.setRadioMode(RadioMode.modeGN);
   }

}
