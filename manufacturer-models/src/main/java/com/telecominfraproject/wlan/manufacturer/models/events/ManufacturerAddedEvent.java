package com.telecominfraproject.wlan.manufacturer.models.events;

import com.telecominfraproject.wlan.manufacturer.models.Manufacturer;
import com.telecominfraproject.wlan.systemevent.models.CustomerEvent;

/**
 * @author dtoptygin
 *
 */
public class ManufacturerAddedEvent extends CustomerEvent<Manufacturer> {
    private static final long serialVersionUID = 7142208487917559985L;

    public ManufacturerAddedEvent(Manufacturer manufacturer){
        super(manufacturer.getCustomerId(), manufacturer.getLastModifiedTimestamp(), manufacturer);
    }
    
    /**
     * Constructor used by JSON
     */
    public ManufacturerAddedEvent() {
        super(0, 0, null);
    }
    
}
