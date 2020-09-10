package com.telecominfraproject.wlan.location.models.events;

import com.telecominfraproject.wlan.location.models.Location;
import com.telecominfraproject.wlan.systemevent.models.CustomerEventWithPayload;

/**
 * @author dtop
 *
 */
public class LocationAddedEvent extends CustomerEventWithPayload<Location> {
    private static final long serialVersionUID = 7142208487917559985L;

    public LocationAddedEvent(Location location){
        super(location.getCustomerId(), location.getLastModifiedTimestamp(), location);
    }
    
    /**
     * Constructor used by JSON
     */
    public LocationAddedEvent() {
        super(0, 0, null);
    }
    
}
