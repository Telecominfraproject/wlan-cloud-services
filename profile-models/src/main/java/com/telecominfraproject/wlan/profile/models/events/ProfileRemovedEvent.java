package com.telecominfraproject.wlan.profile.models.events;

import com.telecominfraproject.wlan.profile.models.Profile;
import com.telecominfraproject.wlan.systemevent.models.CustomerEventWithPayload;

/**
 * @author dtoptygin
 *
 */
public class ProfileRemovedEvent extends CustomerEventWithPayload<Profile> {
    private static final long serialVersionUID = 7142208488887559985L;

    public ProfileRemovedEvent(Profile profile){
        super(profile.getCustomerId(), profile);
    }
    
    /**
     * Constructor used by JSON
     */
    public ProfileRemovedEvent() {
        super(0, 0, null);
    }
    
}
