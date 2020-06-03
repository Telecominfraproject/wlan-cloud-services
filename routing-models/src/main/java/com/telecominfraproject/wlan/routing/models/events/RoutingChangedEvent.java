package com.telecominfraproject.wlan.routing.models.events;

import com.telecominfraproject.wlan.routing.models.EquipmentRoutingRecord;
import com.telecominfraproject.wlan.systemevent.models.EquipmentEvent;

/**
 * @author dtoptygin
 *
 */
public class RoutingChangedEvent extends EquipmentEvent<EquipmentRoutingRecord> {
    private static final long serialVersionUID = 7142209997917559985L;

    public RoutingChangedEvent(EquipmentRoutingRecord routing){
        super(routing.getCustomerId(), routing.getEquipmentId(), routing.getLastModifiedTimestamp(), routing);
    }
    
    /**
     * Constructor used by JSON
     */
    public RoutingChangedEvent() {
        super(0,0, 0, null);
    }
    
}
