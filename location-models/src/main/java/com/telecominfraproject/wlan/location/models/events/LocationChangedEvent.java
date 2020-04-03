package com.telecominfraproject.wlan.location.models.events;

import com.telecominfraproject.wlan.location.models.Location;
import com.telecominfraproject.wlan.systemevent.models.CustomerEvent;

/**
 * @author dtop
 *
 */
public class LocationChangedEvent extends CustomerEvent<Location> {
    private static final long serialVersionUID = 7142209997917559985L;

    public LocationChangedEvent(Location location){
        super(location.getCustomerId(), location.getLastModifiedTimestamp(), location);
    }
    
    /**
     * Constructor used by JSON
     */
    public LocationChangedEvent() {
        super(0, 0, null);
    }
    
}
