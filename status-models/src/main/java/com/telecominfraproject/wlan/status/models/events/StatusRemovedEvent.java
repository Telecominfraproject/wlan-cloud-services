package com.telecominfraproject.wlan.status.models.events;

import com.telecominfraproject.wlan.status.models.Status;
import com.telecominfraproject.wlan.systemevent.models.EquipmentEventWithPayload;

/**
 * @author dtoptygin
 *
 */
public class StatusRemovedEvent extends EquipmentEventWithPayload<Status> {
    private static final long serialVersionUID = 7142208488887559985L;

    public StatusRemovedEvent(Status status){
        super(status.getCustomerId(), status.getEquipmentId(), status.getLastModifiedTimestamp(), status);
    }
    
    /**
     * Constructor used by JSON
     */
    public StatusRemovedEvent() {
        super(0, 0, 0, null);
    }
    
}
