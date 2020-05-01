package com.telecominfraproject.wlan.equipment.models;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AutoOrManualIntegerTests 
{
    @Test
    public void testBasicEquals()
    {
        assertEquals(AutoOrManualValue.createAutomaticInstance(-90), AutoOrManualValue.createAutomaticInstance(-90));
    }

}
