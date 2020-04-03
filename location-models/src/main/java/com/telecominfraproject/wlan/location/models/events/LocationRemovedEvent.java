package com.telecominfraproject.wlan.location.models.events;

import com.telecominfraproject.wlan.location.models.Location;
import com.telecominfraproject.wlan.systemevent.models.CustomerEvent;

/**
 * @author dtop
 *
 */
public class LocationRemovedEvent extends CustomerEvent<Location> {
    private static final long serialVersionUID = 7142208488887559985L;

    public LocationRemovedEvent(Location location){
        super(location.getCustomerId(), location.getLastModifiedTimestamp(), location);
    }
    
    /**
     * Constructor used by JSON
     */
    public LocationRemovedEvent() {
        super(0, 0, null);
    }
    
}
