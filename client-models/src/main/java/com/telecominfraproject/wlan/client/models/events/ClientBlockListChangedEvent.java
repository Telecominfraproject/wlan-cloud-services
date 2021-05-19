package com.telecominfraproject.wlan.client.models.events;

import com.telecominfraproject.wlan.client.models.Client;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasClientMac;
import com.telecominfraproject.wlan.systemevent.models.CustomerEventWithPayload;

/**
 * @author dtoptygin
 *
 */
public class ClientBlockListChangedEvent extends CustomerEventWithPayload<Client> implements HasClientMac{
    private static final long serialVersionUID = 7142209997917559985L;

    public ClientBlockListChangedEvent(Client client){
        super(client.getCustomerId(), client);
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
    public ClientBlockListChangedEvent() {
        super(0, 0, null);
    }
    
}
