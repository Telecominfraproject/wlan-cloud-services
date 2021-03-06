package com.telecominfraproject.wlan.alarm.models.events;

import com.telecominfraproject.wlan.alarm.models.Alarm;
import com.telecominfraproject.wlan.systemevent.models.EquipmentEventWithPayload;

/**
 * @author dtoptygin
 *
 */
public class AlarmChangedEvent extends EquipmentEventWithPayload<Alarm> {
    private static final long serialVersionUID = 7142209997917559985L;

    public AlarmChangedEvent(Alarm alarm){
        super(alarm.getCustomerId(), alarm.getEquipmentId(), alarm);
    }
    
    /**
     * Constructor used by JSON
     */
    public AlarmChangedEvent() {
        super(0, 0, 0, null);
    }
    
}
