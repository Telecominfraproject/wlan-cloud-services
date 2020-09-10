package com.telecominfraproject.wlan.client.models.events;

import com.telecominfraproject.wlan.client.models.Client;
import com.telecominfraproject.wlan.systemevent.models.CustomerEventWithPayload;

/**
 * @author dtoptygin
 *
 */
public class ClientRemovedEvent extends CustomerEventWithPayload<Client> {
    private static final long serialVersionUID = 7142208488887559985L;

    public ClientRemovedEvent(Client client){
        super(client.getCustomerId(), client.getLastModifiedTimestamp(), client);
    }
    
    /**
     * Constructor used by JSON
     */
    public ClientRemovedEvent() {
        super(0, 0, null);
    }
    
}
