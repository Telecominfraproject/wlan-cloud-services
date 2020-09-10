package com.telecominfraproject.wlan.client.models.events;

import com.telecominfraproject.wlan.client.models.Client;
import com.telecominfraproject.wlan.systemevent.models.CustomerEventWithPayload;

/**
 * @author dtoptygin
 *
 */
public class ClientChangedEvent extends CustomerEventWithPayload<Client> {
    private static final long serialVersionUID = 7142209997917559985L;

    public ClientChangedEvent(Client client){
        super(client.getCustomerId(), client.getLastModifiedTimestamp(), client);
    }
    
    /**
     * Constructor used by JSON
     */
    public ClientChangedEvent() {
        super(0, 0, null);
    }
    
}
