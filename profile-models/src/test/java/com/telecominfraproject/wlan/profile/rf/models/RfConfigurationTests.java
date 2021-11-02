package com.telecominfraproject.wlan.profile.rf.models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
    
    @Test
    public void testAutoExclusionChannels() {
        RfElementConfiguration rfConfig = RfElementConfiguration.createWithDefaults(RadioType.is5GHz);
        assertNotNull(rfConfig.getAutoExclusionChannels());
        assertTrue(rfConfig.getAutoExclusionChannels().isEmpty());
        
        Set<Integer> unsortedSet = new HashSet<Integer>(Arrays.asList(1, 23, 45, 12));
        rfConfig.setAutoExclusionChannels(unsortedSet);
        
        Set<Integer> sortedSet = rfConfig.getAutoExclusionChannels();
        List<Integer> list = new ArrayList<Integer>(sortedSet);
        
        assertEquals(Integer.valueOf(1), list.get(0));
        assertEquals(Integer.valueOf(12), list.get(1));
        assertEquals(Integer.valueOf(23), list.get(2));
        assertEquals(Integer.valueOf(45), list.get(3));
        
        Set<Integer> unsortedSet2 = new HashSet<Integer>(Arrays.asList(10, 45, 23, 12));
        rfConfig.setAutoExclusionChannels(unsortedSet2);
        
        Set<Integer> sortedSet2 = rfConfig.getAutoExclusionChannels();
        List<Integer> list2 = new ArrayList<Integer>(sortedSet2);
        
        assertEquals(Integer.valueOf(10), list2.get(0));
        assertEquals(Integer.valueOf(12), list2.get(1));
        assertEquals(Integer.valueOf(23), list2.get(2));
        assertEquals(Integer.valueOf(45), list2.get(3));
    }
    
    @Test
    public void testMaxAutoCellSize() {
        RfElementConfiguration rfConfig = RfElementConfiguration.createWithDefaults(RadioType.is5GHz);
        assertNotNull(rfConfig.getMaxAutoCellSize());
        
        assertEquals(RfElementConfiguration.DEFAULT_MAX_CELL_SIZE_DB, rfConfig.getMaxAutoCellSize().intValue());
        
        rfConfig.setMaxAutoCellSize(-93);
        //getMaxAutoCellSize always return default MaxAutoCellSize
        assertEquals(RfElementConfiguration.DEFAULT_MAX_CELL_SIZE_DB, rfConfig.getMaxAutoCellSize().intValue());
    }
}
