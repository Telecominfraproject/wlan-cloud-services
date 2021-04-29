package com.telecominfraproject.wlan.client.models.events;

import com.telecominfraproject.wlan.client.models.Client;
import com.telecominfraproject.wlan.systemevent.models.CustomerEventWithPayload;

/**
 * @author dtoptygin
 *
 */
public class ClientAddedEvent extends CustomerEventWithPayload<Client> {
    private static final long serialVersionUID = 7142208487917559985L;

    public ClientAddedEvent(Client client){
        super(client.getCustomerId(), client);
    }
    
    /**
     * Constructor used by JSON
     */
    public ClientAddedEvent() {
        super(0, 0, null);
    }
    
}
