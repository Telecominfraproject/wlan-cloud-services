package com.telecominfraproject.wlan.routing.models.events;

import com.telecominfraproject.wlan.routing.models.Routing;
import com.telecominfraproject.wlan.systemevent.models.CustomerEvent;

/**
 * @author dtoptygin
 *
 */
public class RoutingAddedEvent extends CustomerEvent<Routing> {
    private static final long serialVersionUID = 7142208487917559985L;

    public RoutingAddedEvent(Routing routing){
        super(routing.getCustomerId(), routing.getLastModifiedTimestamp(), routing);
    }
    
    /**
     * Constructor used by JSON
     */
    public RoutingAddedEvent() {
        super(0, 0, null);
    }
    
}
