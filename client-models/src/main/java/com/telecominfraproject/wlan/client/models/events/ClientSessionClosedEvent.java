package com.telecominfraproject.wlan.client.models.events;

import com.telecominfraproject.wlan.client.session.models.ClientSession;
import com.telecominfraproject.wlan.systemevent.models.EquipmentEventWithPayload;

/**
 * @author dtoptygin
 *
 */
public class ClientSessionClosedEvent extends EquipmentEventWithPayload<ClientSession> {
    private static final long serialVersionUID = 7142208487917559985L;

    public ClientSessionClosedEvent(ClientSession clientSession){
		super(clientSession.getCustomerId(), clientSession.getEquipmentId(),
				(clientSession.getLastModifiedTimestamp() != 0 ? clientSession.getLastModifiedTimestamp()
						: System.currentTimeMillis()),
				clientSession);
    }
    
    /**
     * Constructor used by JSON
     */
    public ClientSessionClosedEvent() {
        super(0, 0, 0, null);
    }
    
}
