package com.telecominfraproject.wlan.alarm.models.events;

import com.telecominfraproject.wlan.alarm.models.Alarm;
import com.telecominfraproject.wlan.systemevent.models.EquipmentEvent;

/**
 * @author dtoptygin
 *
 */
public class AlarmAddedEvent extends EquipmentEvent<Alarm> {
    private static final long serialVersionUID = 7142208487917559985L;

    public AlarmAddedEvent(Alarm alarm){
        super(alarm.getCustomerId(), alarm.getEquipmentId(), alarm.getLastModifiedTimestamp(), alarm);
    }
    
    /**
     * Constructor used by JSON
     */
    public AlarmAddedEvent() {
        super(0, 0, 0, null);
    }
    
}
