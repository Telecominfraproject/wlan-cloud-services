package com.telecominfraproject.wlan.portaluser.models.events;

import com.telecominfraproject.wlan.portaluser.models.PortalUser;
import com.telecominfraproject.wlan.systemevent.models.CustomerEvent;

/**
 * @author dtoptygin
 *
 */
public class PortalUserChangedEvent extends CustomerEvent<PortalUser> {
    private static final long serialVersionUID = 7142209997917559985L;

    public PortalUserChangedEvent(PortalUser portalUser){
        super(portalUser.getCustomerId(), portalUser.getLastModifiedTimestamp(), portalUser);
    }
    
    /**
     * Constructor used by JSON
     */
    public PortalUserChangedEvent() {
        super(0, 0, null);
    }
    
}
