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
		assertEquals("This should never be 'enabled' by default, it really interferes with our"
				+ "cloud-based auto channel rebalancing", false, rfConfig.getAutoChannelSelection());
		}
	}
	
	@Test
    @Ignore
    public void autoCellSizeNeverSmallerThanMinCellSize()
    {
        RfConfiguration radio = RfConfiguration.createWithDefaults();
        radio.getRfConfig(RadioType.is5GHz).setMinAutoCellSize(-80);
        radio.getRfConfig(RadioType.is5GHz).setRxCellSizeDb(AutoOrManualValue.createAutomaticInstance(-65));
        assertEquals(AutoOrManualValue.createAutomaticInstance(-80), radio.getRfConfig(RadioType.is5GHz).getRxCellSizeDb());
    }

}
