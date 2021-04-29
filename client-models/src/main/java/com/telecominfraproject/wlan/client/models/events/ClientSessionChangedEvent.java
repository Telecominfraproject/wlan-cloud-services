package com.telecominfraproject.wlan.client.models.events;

import com.telecominfraproject.wlan.client.session.models.ClientSession;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasLocationId;
import com.telecominfraproject.wlan.systemevent.models.EquipmentEventWithPayload;

/**
 * @author dtoptygin
 *
 */
public class ClientSessionChangedEvent extends EquipmentEventWithPayload<ClientSession> implements HasLocationId {
    private static final long serialVersionUID = 7142208487917559985L;

    public ClientSessionChangedEvent(ClientSession clientSession){
		super(clientSession.getCustomerId(), clientSession.getEquipmentId(), clientSession);
    }
    
    @Override
    public long getLocationId() {
        if(getPayload() !=null) {
            return getPayload().getLocationId();
        }
        
        return 0;
    }

    /**
     * Constructor used by JSON
     */
    public ClientSessionChangedEvent() {
        super(0, 0, 0, null);
    }
    
}
