package com.telecominfraproject.wlan.location.models.events;

import com.telecominfraproject.wlan.location.models.Location;

public class LocationChangedApImpactingEvent extends LocationChangedEvent {
	private static final long serialVersionUID = -5258621806247788808L;

	public LocationChangedApImpactingEvent(Location location){
        super(location);
    }
    
    /**
     * Constructor used by JSON
     */
    public LocationChangedApImpactingEvent() {
        super(null);
    }
    
}
