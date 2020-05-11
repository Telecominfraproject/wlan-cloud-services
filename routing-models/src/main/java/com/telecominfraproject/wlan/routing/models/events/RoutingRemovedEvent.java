package com.telecominfraproject.wlan.routing.models.events;

import com.telecominfraproject.wlan.routing.models.EquipmentRoutingRecord;
import com.telecominfraproject.wlan.systemevent.models.CustomerEvent;

/**
 * @author dtoptygin
 *
 */
public class RoutingRemovedEvent extends CustomerEvent<EquipmentRoutingRecord> {
    private static final long serialVersionUID = 7142208488887559985L;

    public RoutingRemovedEvent(EquipmentRoutingRecord routing){
        super(routing.getCustomerId(), routing.getLastModifiedTimestamp(), routing);
    }
    
    /**
     * Constructor used by JSON
     */
    public RoutingRemovedEvent() {
        super(0, 0, null);
    }
    
}
