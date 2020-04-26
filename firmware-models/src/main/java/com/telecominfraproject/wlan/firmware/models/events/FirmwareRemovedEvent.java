package com.telecominfraproject.wlan.firmware.models.events;

import com.telecominfraproject.wlan.firmware.models.Firmware;
import com.telecominfraproject.wlan.systemevent.models.CustomerEvent;

/**
 * @author dtoptygin
 *
 */
public class FirmwareRemovedEvent extends CustomerEvent<Firmware> {
    private static final long serialVersionUID = 7142208488887559985L;

    public FirmwareRemovedEvent(Firmware firmware){
        super(firmware.getCustomerId(), firmware.getLastModifiedTimestamp(), firmware);
    }
    
    /**
     * Constructor used by JSON
     */
    public FirmwareRemovedEvent() {
        super(0, 0, null);
    }
    
}
