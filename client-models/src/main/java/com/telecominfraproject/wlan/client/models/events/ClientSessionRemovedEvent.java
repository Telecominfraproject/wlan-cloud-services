package com.telecominfraproject.wlan.client.models.events;

import com.telecominfraproject.wlan.client.session.models.ClientSession;
import com.telecominfraproject.wlan.systemevent.models.EquipmentEvent;

/**
 * @author dtoptygin
 *
 */
public class ClientSessionRemovedEvent extends EquipmentEvent<ClientSession> {
    private static final long serialVersionUID = 7142208487917559985L;

    public ClientSessionRemovedEvent(ClientSession clientSession){
		super(clientSession.getCustomerId(), clientSession.getEquipmentId(),
				(clientSession.getLastModifiedTimestamp() != 0 ? clientSession.getLastModifiedTimestamp()
						: System.currentTimeMillis()),
				clientSession);
    }
    
    /**
     * Constructor used by JSON
     */
    public ClientSessionRemovedEvent() {
        super(0, 0, 0, null);
    }
    
}
