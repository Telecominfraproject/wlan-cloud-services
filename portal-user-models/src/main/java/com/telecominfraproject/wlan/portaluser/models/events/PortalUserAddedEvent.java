package com.telecominfraproject.wlan.portaluser.models.events;

import com.telecominfraproject.wlan.portaluser.models.PortalUser;
import com.telecominfraproject.wlan.systemevent.models.CustomerEventWithPayload;

/**
 * @author dtoptygin
 *
 */
public class PortalUserAddedEvent extends CustomerEventWithPayload<PortalUser> {
    private static final long serialVersionUID = 7142208487917559985L;

    public PortalUserAddedEvent(PortalUser portalUser){
        super(portalUser.getCustomerId(), portalUser.getLastModifiedTimestamp(), portalUser);
    }
    
    /**
     * Constructor used by JSON
     */
    public PortalUserAddedEvent() {
        super(0, 0, null);
    }
    
}
