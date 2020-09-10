package com.telecominfraproject.wlan.status.models.events;

import com.telecominfraproject.wlan.status.models.Status;
import com.telecominfraproject.wlan.systemevent.models.EquipmentEventWithPayload;

/**
 * @author dtoptygin
 *
 */
public class StatusChangedEvent extends EquipmentEventWithPayload<Status> {
    private static final long serialVersionUID = 7142209997917559985L;

    public StatusChangedEvent(Status status){
        super(status.getCustomerId(), status.getEquipmentId(), status.getLastModifiedTimestamp(), status);
    }
    
    /**
     * Constructor used by JSON
     */
    public StatusChangedEvent() {
        super(0, 0, 0, null);
    }
    
}
