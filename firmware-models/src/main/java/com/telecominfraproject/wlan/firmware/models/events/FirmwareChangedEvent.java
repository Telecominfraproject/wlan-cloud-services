package com.telecominfraproject.wlan.firmware.models.events;

import com.telecominfraproject.wlan.firmware.models.Firmware;
import com.telecominfraproject.wlan.systemevent.models.CustomerEvent;

/**
 * @author dtoptygin
 *
 */
public class FirmwareChangedEvent extends CustomerEvent<Firmware> {
    private static final long serialVersionUID = 7142209997917559985L;

    public FirmwareChangedEvent(Firmware firmware){
        super(firmware.getCustomerId(), firmware.getLastModifiedTimestamp(), firmware);
    }
    
    /**
     * Constructor used by JSON
     */
    public FirmwareChangedEvent() {
        super(0, 0, null);
    }
    
}
