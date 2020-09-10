package com.telecominfraproject.wlan.profile.models.events;

import com.telecominfraproject.wlan.profile.models.Profile;
import com.telecominfraproject.wlan.systemevent.models.CustomerEventWithPayload;

/**
 * @author dtoptygin
 *
 */
public class ProfileAddedEvent extends CustomerEventWithPayload<Profile> {
    private static final long serialVersionUID = 7142208487917559985L;

    public ProfileAddedEvent(Profile profile){
        super(profile.getCustomerId(), profile.getLastModifiedTimestamp(), profile);
    }
    
    /**
     * Constructor used by JSON
     */
    public ProfileAddedEvent() {
        super(0, 0, null);
    }
    
}
