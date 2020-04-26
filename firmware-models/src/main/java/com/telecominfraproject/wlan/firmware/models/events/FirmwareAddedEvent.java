package com.telecominfraproject.wlan.firmware.models.events;

import com.telecominfraproject.wlan.firmware.models.Firmware;
import com.telecominfraproject.wlan.systemevent.models.CustomerEvent;

/**
 * @author dtoptygin
 *
 */
public class FirmwareAddedEvent extends CustomerEvent<Firmware> {
    private static final long serialVersionUID = 7142208487917559985L;

    public FirmwareAddedEvent(Firmware firmware){
        super(firmware.getCustomerId(), firmware.getLastModifiedTimestamp(), firmware);
    }
    
    /**
     * Constructor used by JSON
     */
    public FirmwareAddedEvent() {
        super(0, 0, null);
    }
    
}
