package com.telecominfraproject.wlan.equipment.models;

import static org.junit.Assert.*;

import org.junit.Test;

import com.telecominfraproject.wlan.core.model.equipment.ChannelBandwidth;

public class ChannelBandwidthTests 
{
    @Test
    public void testBasicCase()
    {
        assertEquals(20, ChannelBandwidth.is20MHz.getNumericalValue());
        assertEquals(40, ChannelBandwidth.is40MHz.getNumericalValue());
        assertEquals(80, ChannelBandwidth.is80MHz.getNumericalValue());
        assertEquals(160, ChannelBandwidth.is160MHz.getNumericalValue());
        assertEquals(0, ChannelBandwidth.auto.getNumericalValue());
    }

}
