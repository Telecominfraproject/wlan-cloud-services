
package com.telecominfraproject.wlan.routing.models.events;

import com.telecominfraproject.wlan.routing.models.EquipmentGatewayRecord;
import com.telecominfraproject.wlan.systemevent.models.SystemEvent;

/**
 * @author dtoptygin
 *
 */
public class GatewayAddedEvent extends SystemEvent {
    private static final long serialVersionUID = 7142208487917559985L;

    private EquipmentGatewayRecord gateway;

    public GatewayAddedEvent(EquipmentGatewayRecord gateway) {
        super(System.currentTimeMillis());
        this.gateway = gateway;
    }

    /**
     * Constructor used by JSON
     */
    public GatewayAddedEvent() {
        super(0);
    }

    public EquipmentGatewayRecord getGateway() {
        return gateway;
    }

    public void setGateway(EquipmentGatewayRecord gateway) {
        this.gateway = gateway;
    }

    @Override
    public GatewayAddedEvent clone() {
        GatewayAddedEvent ret = (GatewayAddedEvent) super.clone();

        if (gateway != null) {
            ret.gateway = gateway.clone();
        }

        return ret;
    }
}
