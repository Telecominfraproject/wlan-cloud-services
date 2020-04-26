package com.telecominfraproject.wlan.status.models.events;

import com.telecominfraproject.wlan.status.models.Status;
import com.telecominfraproject.wlan.systemevent.models.CustomerEvent;

/**
 * @author dtoptygin
 *
 */
public class StatusChangedEvent extends CustomerEvent<Status> {
    private static final long serialVersionUID = 7142209997917559985L;

    public StatusChangedEvent(Status status){
        super(status.getCustomerId(), status.getLastModifiedTimestamp(), status);
    }
    
    /**
     * Constructor used by JSON
     */
    public StatusChangedEvent() {
        super(0, 0, null);
    }
    
}
