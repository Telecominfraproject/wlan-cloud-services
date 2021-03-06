package com.telecominfraproject.wlan.routing.models.events;

import com.telecominfraproject.wlan.routing.models.EquipmentRoutingRecord;
import com.telecominfraproject.wlan.systemevent.models.EquipmentEventWithPayload;

/**
 * @author dtoptygin
 *
 */
public class RoutingChangedEvent extends EquipmentEventWithPayload<EquipmentRoutingRecord> {
    private static final long serialVersionUID = 7142209997917559985L;

    public RoutingChangedEvent(EquipmentRoutingRecord routing){
        super(routing.getCustomerId(), routing.getEquipmentId(), routing);
    }
    
    /**
     * Constructor used by JSON
     */
    public RoutingChangedEvent() {
        super(0,0, 0, null);
    }
    
}
