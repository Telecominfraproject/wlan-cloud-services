package com.telecominfraproject.wlan.routing.models.events;

import com.telecominfraproject.wlan.routing.models.Routing;
import com.telecominfraproject.wlan.systemevent.models.CustomerEvent;

/**
 * @author dtoptygin
 *
 */
public class RoutingChangedEvent extends CustomerEvent<Routing> {
    private static final long serialVersionUID = 7142209997917559985L;

    public RoutingChangedEvent(Routing routing){
        super(routing.getCustomerId(), routing.getLastModifiedTimestamp(), routing);
    }
    
    /**
     * Constructor used by JSON
     */
    public RoutingChangedEvent() {
        super(0, 0, null);
    }
    
}
