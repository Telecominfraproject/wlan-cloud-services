package com.telecominfraproject.wlan.routing.models.events;

import com.telecominfraproject.wlan.routing.models.EquipmentGatewayRecord;
import com.telecominfraproject.wlan.systemevent.models.SystemEvent;

/**
 * @author dtoptygin
 *
 */
public class GatewayAddedEvent extends SystemEvent {
    private static final long serialVersionUID = 7142208487917559985L;

    public GatewayAddedEvent(EquipmentGatewayRecord routing){
        super(routing.getLastModifiedTimestamp());
    }
    
    /**
     * Constructor used by JSON
     */
    public GatewayAddedEvent() {
        super(0);
    }
    
}
