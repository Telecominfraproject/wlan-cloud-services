package com.telecominfraproject.wlan.routing.models.events;

import com.telecominfraproject.wlan.routing.models.EquipmentRoutingRecord;
import com.telecominfraproject.wlan.systemevent.models.EquipmentEventWithPayload;

/**
 * @author dtoptygin
 *
 */
public class RoutingAddedEvent extends EquipmentEventWithPayload<EquipmentRoutingRecord> {
    private static final long serialVersionUID = 7142208487917559985L;

    public RoutingAddedEvent(EquipmentRoutingRecord routing){
        super(routing.getCustomerId(), routing.getEquipmentId(), routing.getLastModifiedTimestamp(), routing);
    }
    
    /**
     * Constructor used by JSON
     */
    public RoutingAddedEvent() {
        super(0, 0, 0, null);
    }
    
}
