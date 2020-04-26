package com.telecominfraproject.wlan.profile.models.events;

import com.telecominfraproject.wlan.profile.models.Profile;
import com.telecominfraproject.wlan.systemevent.models.CustomerEvent;

/**
 * @author dtoptygin
 *
 */
public class ProfileChangedEvent extends CustomerEvent<Profile> {
    private static final long serialVersionUID = 7142209997917559985L;

    public ProfileChangedEvent(Profile profile){
        super(profile.getCustomerId(), profile.getLastModifiedTimestamp(), profile);
    }
    
    /**
     * Constructor used by JSON
     */
    public ProfileChangedEvent() {
        super(0, 0, null);
    }
    
}
