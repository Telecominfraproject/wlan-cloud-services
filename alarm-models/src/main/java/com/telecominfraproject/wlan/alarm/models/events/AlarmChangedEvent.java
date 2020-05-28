package com.telecominfraproject.wlan.alarm.models.events;

import com.telecominfraproject.wlan.alarm.models.Alarm;
import com.telecominfraproject.wlan.systemevent.models.EquipmentEvent;

/**
 * @author dtoptygin
 *
 */
public class AlarmChangedEvent extends EquipmentEvent<Alarm> {
    private static final long serialVersionUID = 7142209997917559985L;

    public AlarmChangedEvent(Alarm alarm){
        super(alarm.getCustomerId(), alarm.getEquipmentId(), alarm.getLastModifiedTimestamp(), alarm);
    }
    
    /**
     * Constructor used by JSON
     */
    public AlarmChangedEvent() {
        super(0, 0, 0, null);
    }
    
}
