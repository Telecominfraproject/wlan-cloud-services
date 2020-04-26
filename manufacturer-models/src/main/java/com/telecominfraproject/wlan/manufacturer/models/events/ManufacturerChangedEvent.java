package com.telecominfraproject.wlan.manufacturer.models.events;

import com.telecominfraproject.wlan.manufacturer.models.Manufacturer;
import com.telecominfraproject.wlan.systemevent.models.CustomerEvent;

/**
 * @author dtoptygin
 *
 */
public class ManufacturerChangedEvent extends CustomerEvent<Manufacturer> {
    private static final long serialVersionUID = 7142209997917559985L;

    public ManufacturerChangedEvent(Manufacturer manufacturer){
        super(manufacturer.getCustomerId(), manufacturer.getLastModifiedTimestamp(), manufacturer);
    }
    
    /**
     * Constructor used by JSON
     */
    public ManufacturerChangedEvent() {
        super(0, 0, null);
    }
    
}
