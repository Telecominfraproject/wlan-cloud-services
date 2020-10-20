package com.telecominfraproject.wlan.profile.rf.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.equipment.models.RadioMode;
import com.telecominfraproject.wlan.server.exceptions.ConfigurationException;

public class RfConfigurationTests {
	
	@Test
	public void safeguardForAutoSelection()
	{
		for (RadioType radioType : RadioType.validValues()) {
		RfElementConfiguration rfConfig = RfElementConfiguration.createWithDefaults(radioType);
		assertEquals(false, rfConfig.getAutoChannelSelection());
		}
	}
	
    @Test
    public void validationTest() {
 	   RfElementConfiguration radioConfig2_4 = RfElementConfiguration.createWithDefaults(RadioType.is2dot4GHz);
 	   RfElementConfiguration radioConfig5 = RfElementConfiguration.createWithDefaults(RadioType.is5GHz);
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
