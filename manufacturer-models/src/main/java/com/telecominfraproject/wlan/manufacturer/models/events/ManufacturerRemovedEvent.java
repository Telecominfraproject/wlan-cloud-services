package com.telecominfraproject.wlan.manufacturer.models.events;

import com.telecominfraproject.wlan.manufacturer.models.Manufacturer;
import com.telecominfraproject.wlan.systemevent.models.CustomerEvent;

/**
 * @author dtoptygin
 *
 */
public class ManufacturerRemovedEvent extends CustomerEvent<Manufacturer> {
    private static final long serialVersionUID = 7142208488887559985L;

    public ManufacturerRemovedEvent(Manufacturer manufacturer){
        super(manufacturer.getCustomerId(), manufacturer.getLastModifiedTimestamp(), manufacturer);
    }
    
    /**
     * Constructor used by JSON
     */
    public ManufacturerRemovedEvent() {
        super(0, 0, null);
    }
    
}
