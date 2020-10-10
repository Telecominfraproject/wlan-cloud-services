package com.telecominfraproject.wlan.profile.rf.models;

import static org.junit.Assert.assertEquals;

import org.junit.Ignore;
import org.junit.Test;

import com.telecominfraproject.wlan.core.model.equipment.AutoOrManualValue;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.equipment.models.ElementRadioConfiguration;

public class RfConfigurationTests {
	
	@Test
	public void safeguardForAutoSelection()
	{
		for (RadioType radioType : RadioType.validValues()) {
		RfElementConfiguration rfConfig = RfElementConfiguration.createWithDefaults(radioType);
		assertEquals(false, rfConfig.getAutoChannelSelection());
		}
	}
}
