package com.telecominfraproject.wlan.client.models.events;

import com.telecominfraproject.wlan.client.session.models.ClientSession;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasClientMac;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasLocationId;
import com.telecominfraproject.wlan.systemevent.models.EquipmentEventWithPayload;

/**
 * @author dtoptygin
 *
 */
public class ClientSessionRemovedEvent extends EquipmentEventWithPayload<ClientSession> implements HasLocationId, HasClientMac {
    private static final long serialVersionUID = 7142208487917559985L;

    public ClientSessionRemovedEvent(ClientSession clientSession){
		super(clientSession.getCustomerId(), clientSession.getEquipmentId(), clientSession);
    }
    
    @Override
    public long getLocationId() {
        if(getPayload() !=null) {
            return getPayload().getLocationId();
        }
        
        return 0;
    }
    
    @Override
    public MacAddress getClientMacAddress() {
    	if (getPayload() != null) {
    		return getPayload().getMacAddress();
    	}
    	
    	return null; 
    }

    /**
     * Constructor used by JSON
     */
    public ClientSessionRemovedEvent() {
        super(0, 0, 0, null);
    }
    
}
