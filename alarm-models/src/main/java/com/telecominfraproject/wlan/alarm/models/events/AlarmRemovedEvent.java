package com.telecominfraproject.wlan.alarm.models.events;

import com.telecominfraproject.wlan.alarm.models.Alarm;
import com.telecominfraproject.wlan.systemevent.models.CustomerEvent;

/**
 * @author dtoptygin
 *
 */
public class AlarmRemovedEvent extends CustomerEvent<Alarm> {
    private static final long serialVersionUID = 7142208488887559985L;

    public AlarmRemovedEvent(Alarm alarm){
        super(alarm.getCustomerId(), alarm.getLastModifiedTimestamp(), alarm);
    }
    
    /**
     * Constructor used by JSON
     */
    public AlarmRemovedEvent() {
        super(0, 0, null);
    }
    
}
