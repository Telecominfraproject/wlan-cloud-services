package com.telecominfraproject.wlan.routing.models.events;

import com.telecominfraproject.wlan.routing.models.EquipmentGatewayRecord;
import com.telecominfraproject.wlan.systemevent.models.SystemEvent;

/**
 * @author dtoptygin
 *
 */
public class GatewayRemovedEvent extends SystemEvent {
    private static final long serialVersionUID = 7142208488887559985L;

    public GatewayRemovedEvent(EquipmentGatewayRecord gateway){
        super(gateway.getLastModifiedTimestamp());
    }
    
    /**
     * Constructor used by JSON
     */
    public GatewayRemovedEvent() {
        super(0);
    }
    
}
