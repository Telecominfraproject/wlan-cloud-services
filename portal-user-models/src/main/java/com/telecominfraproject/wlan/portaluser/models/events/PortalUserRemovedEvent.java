package com.telecominfraproject.wlan.portaluser.models.events;

import com.telecominfraproject.wlan.portaluser.models.PortalUserEventPayload;
import com.telecominfraproject.wlan.systemevent.models.CustomerEventWithPayload;

/**
 * @author dtoptygin
 *
 */
public class PortalUserRemovedEvent extends CustomerEventWithPayload<PortalUserEventPayload> {
    private static final long serialVersionUID = 7142208488887559985L;

    public PortalUserRemovedEvent(PortalUserEventPayload portalUser){
        super(portalUser.getCustomerId(), portalUser);
    }
    
    /**
     * Constructor used by JSON
     */
    public PortalUserRemovedEvent() {
        super(0, 0, null);
    }
    
}
