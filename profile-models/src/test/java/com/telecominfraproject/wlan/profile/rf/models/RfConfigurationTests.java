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
		assertEquals(false, rfConfig.getAutoCellSizeSelection());
		}
	}
	
    @Test
    public void validationTest() {
 	   RfElementConfiguration rfConfig2_4 = RfElementConfiguration.createWithDefaults(RadioType.is2dot4GHz);
 	   RfElementConfiguration rfConfig5 = RfElementConfiguration.createWithDefaults(RadioType.is5GHz);
       rfConfig2_4.validate();
       rfConfig5.validate();
       
       // Validate defaults
    rfConfig2_4.setRadioMode(RadioMode.modeGN);
    rfConfig5.setRadioMode(RadioMode.modeGN);
   
    // Should fail on 5G radio
    rfConfig2_4.validate();
    try {
       rfConfig5.validate();
       fail("Should not be able to set 5G radio to mode GN");
    } catch (ConfigurationException expected) {}
   
    rfConfig2_4.setRadioMode(RadioMode.modeAC);
    rfConfig5.setRadioMode(RadioMode.modeAC);
   
   // Should fail on 2.4G radio
    try {
       rfConfig2_4.validate();
       fail("Should not be able to set 2.4G radio to mode AC");
    } catch (ConfigurationException expected) {}
    rfConfig5.validate();
   
    rfConfig2_4.setRadioMode(RadioMode.modeN);
    rfConfig5.setRadioMode(RadioMode.modeN);
   
    // Should have no failures for mode N
       rfConfig2_4.setRadioMode(RadioMode.modeGN);
       rfConfig5.setRadioMode(RadioMode.modeGN);
    }
}
