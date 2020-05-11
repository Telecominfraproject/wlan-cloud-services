package com.telecominfraproject.wlan.routing.models.events;

import com.telecominfraproject.wlan.routing.models.EquipmentGatewayRecord;
import com.telecominfraproject.wlan.systemevent.models.SystemEvent;

/**
 * @author dtoptygin
 *
 */
public class GatewayChangedEvent extends SystemEvent {
    private static final long serialVersionUID = 7142209997917559985L;

    public GatewayChangedEvent(EquipmentGatewayRecord gateway){
        super(gateway.getLastModifiedTimestamp());
    }
    
    /**
     * Constructor used by JSON
     */
    public GatewayChangedEvent() {
        super(0);
    }
    
}
