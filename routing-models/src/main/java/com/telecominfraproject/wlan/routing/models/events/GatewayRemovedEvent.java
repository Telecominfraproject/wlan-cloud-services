package com.telecominfraproject.wlan.routing.models.events;

import com.telecominfraproject.wlan.routing.models.EquipmentGatewayRecord;
import com.telecominfraproject.wlan.systemevent.models.SystemEvent;

/**
 * @author dtoptygin
 *
 */
public class GatewayRemovedEvent extends SystemEvent {
    private static final long serialVersionUID = 7142208488887559985L;

    private EquipmentGatewayRecord gateway;

    public GatewayRemovedEvent(EquipmentGatewayRecord gateway){
        super(gateway.getLastModifiedTimestamp());
        this.gateway = gateway;
    }
    
    /**
     * Constructor used by JSON
     */
    public GatewayRemovedEvent() {
        super(0);
    }
    
    public EquipmentGatewayRecord getGateway() {
		return gateway;
	}

	public void setGateway(EquipmentGatewayRecord gateway) {
		this.gateway = gateway;
	}

	@Override
	public GatewayRemovedEvent clone() {
		GatewayRemovedEvent ret = (GatewayRemovedEvent) super.clone();
		
		if(gateway!=null) {
			ret.gateway = gateway.clone();
		}
		
		return ret;
	}

}
