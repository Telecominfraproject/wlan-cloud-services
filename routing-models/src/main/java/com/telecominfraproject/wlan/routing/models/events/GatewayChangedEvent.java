package com.telecominfraproject.wlan.routing.models.events;

import com.telecominfraproject.wlan.routing.models.EquipmentGatewayRecord;
import com.telecominfraproject.wlan.systemevent.models.SystemEvent;

/**
 * @author dtoptygin
 *
 */
public class GatewayChangedEvent extends SystemEvent {
    private static final long serialVersionUID = 7142209997917559985L;

    private EquipmentGatewayRecord gateway;

    public GatewayChangedEvent(EquipmentGatewayRecord gateway){
        super();
        this.gateway = gateway;
    }
    
    /**
     * Constructor used by JSON
     */
    public GatewayChangedEvent() {
        super(0);
    }
    
    public EquipmentGatewayRecord getGateway() {
		return gateway;
	}

	public void setGateway(EquipmentGatewayRecord gateway) {
		this.gateway = gateway;
	}

	@Override
	public GatewayChangedEvent clone() {
		GatewayChangedEvent ret = (GatewayChangedEvent) super.clone();
		
		if(gateway!=null) {
			ret.gateway = gateway.clone();
		}
		
		return ret;
	}

}
