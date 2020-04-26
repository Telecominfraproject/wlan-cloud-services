package com.telecominfraproject.wlan.status.models.events;

import com.telecominfraproject.wlan.status.models.Status;
import com.telecominfraproject.wlan.systemevent.models.CustomerEvent;

/**
 * @author dtoptygin
 *
 */
public class StatusRemovedEvent extends CustomerEvent<Status> {
    private static final long serialVersionUID = 7142208488887559985L;

    public StatusRemovedEvent(Status status){
        super(status.getCustomerId(), status.getLastModifiedTimestamp(), status);
    }
    
    /**
     * Constructor used by JSON
     */
    public StatusRemovedEvent() {
        super(0, 0, null);
    }
    
}
