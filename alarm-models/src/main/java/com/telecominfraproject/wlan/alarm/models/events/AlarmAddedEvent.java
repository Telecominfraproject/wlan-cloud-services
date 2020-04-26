package com.telecominfraproject.wlan.alarm.models.events;

import com.telecominfraproject.wlan.alarm.models.Alarm;
import com.telecominfraproject.wlan.systemevent.models.CustomerEvent;

/**
 * @author dtoptygin
 *
 */
public class AlarmAddedEvent extends CustomerEvent<Alarm> {
    private static final long serialVersionUID = 7142208487917559985L;

    public AlarmAddedEvent(Alarm alarm){
        super(alarm.getCustomerId(), alarm.getLastModifiedTimestamp(), alarm);
    }
    
    /**
     * Constructor used by JSON
     */
    public AlarmAddedEvent() {
        super(0, 0, null);
    }
    
}
