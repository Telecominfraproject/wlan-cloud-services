package com.telecominfraproject.wlan.portaluser.models.events;

import com.telecominfraproject.wlan.portaluser.models.PortalUserEventPayload;
import com.telecominfraproject.wlan.systemevent.models.CustomerEventWithPayload;

/**
 * @author dtoptygin
 *
 */
public class PortalUserAddedEvent extends CustomerEventWithPayload<PortalUserEventPayload> {
    private static final long serialVersionUID = 7142208487917559985L;

    public PortalUserAddedEvent(PortalUserEventPayload portalUser){
        
        super(portalUser.getCustomerId(), portalUser);
    }
    
    /**
     * Constructor used by JSON
     */
    public PortalUserAddedEvent() {
        super(0, 0, null);
    }
    
}
