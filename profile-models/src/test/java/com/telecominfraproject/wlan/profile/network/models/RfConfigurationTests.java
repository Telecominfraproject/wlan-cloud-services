package com.telecominfraproject.wlan.profile.network.models;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.telecominfraproject.wlan.core.model.equipment.RadioType;

public class RfConfigurationTests {
	
	@Test
	public void safeguardForAutoSelection()
	{
		
		for (RadioType radioType : RadioType.validValues()) {
		RfElementConfiguration rfConfig = RfElementConfiguration.createWithDefaults(radioType);
		assertEquals("This should never be 'enabled' by default, it really interferes with our"
				+ "cloud-based auto channel rebalancing", false, rfConfig.getAutoChannelSelection());
		}
	}

}
