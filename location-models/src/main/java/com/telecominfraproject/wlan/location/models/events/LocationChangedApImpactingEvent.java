package com.telecominfraproject.wlan.location.models.events;

import com.telecominfraproject.wlan.core.model.json.interfaces.HasLocationId;
import com.telecominfraproject.wlan.location.models.Location;
import com.telecominfraproject.wlan.systemevent.models.CustomerEventWithPayload;

public class LocationChangedApImpactingEvent extends CustomerEventWithPayload<Location> implements HasLocationId {
	private static final long serialVersionUID = -8369849230866347412L;

	public LocationChangedApImpactingEvent(Location location){
		super(location.getCustomerId(), location);
    }
    
    @Override
    public long getLocationId() {
        if (getPayload() != null) {
            return getPayload().getId();
        }
        
        return 0;
    }
    
    /**
     * Constructor used by JSON
     */
    public LocationChangedApImpactingEvent() {
    	super(0, 0, null);
    }
    
}
