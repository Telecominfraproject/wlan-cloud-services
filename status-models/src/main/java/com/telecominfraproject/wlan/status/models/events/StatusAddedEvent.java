package com.telecominfraproject.wlan.status.models.events;

import com.telecominfraproject.wlan.status.models.Status;
import com.telecominfraproject.wlan.systemevent.models.CustomerEvent;

/**
 * @author dtoptygin
 *
 */
public class StatusAddedEvent extends CustomerEvent<Status> {
    private static final long serialVersionUID = 7142208487917559985L;

    public StatusAddedEvent(Status status){
        super(status.getCustomerId(), status.getLastModifiedTimestamp(), status);
    }
    
    /**
     * Constructor used by JSON
     */
    public StatusAddedEvent() {
        super(0, 0, null);
    }
    
}
