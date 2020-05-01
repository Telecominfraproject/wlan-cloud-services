package com.telecominfraproject.wlan.equipment.models;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AutoOrManualStringTests {
    private static final Logger LOG = LoggerFactory.getLogger(AutoOrManualStringTests.class);

    @Test
    public void testBasicSerialization() {
        AutoOrManualString autoOrManualString = new AutoOrManualString(true, "Testing");
        LOG.debug("Serialized: {}", autoOrManualString.toString());
        AutoOrManualString rebuiltString = AutoOrManualString.fromString(autoOrManualString.toString(),
                AutoOrManualString.class);
        assertEquals(autoOrManualString, rebuiltString);
    }

}
