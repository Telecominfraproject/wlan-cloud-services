package com.telecominfraproject.wlan.location.models.events;

import com.telecominfraproject.wlan.core.model.json.interfaces.HasLocationId;
import com.telecominfraproject.wlan.location.models.Location;
import com.telecominfraproject.wlan.systemevent.models.CustomerEventWithPayload;

/**
 * @author dtop
 *
 */
public class LocationAddedEvent extends CustomerEventWithPayload<Location> implements HasLocationId {
    private static final long serialVersionUID = 7142208487917559985L;

    public LocationAddedEvent(Location location){
        super(location.getCustomerId(), location.getLastModifiedTimestamp(), location);
    }

    @Override
    public long getLocationId() {
        if(getPayload() !=null) {
            return getPayload().getId();
        }
        
        return 0;
    }

    /**
     * Constructor used by JSON
     */
    public LocationAddedEvent() {
        super(0, 0, null);
    }
    
}
