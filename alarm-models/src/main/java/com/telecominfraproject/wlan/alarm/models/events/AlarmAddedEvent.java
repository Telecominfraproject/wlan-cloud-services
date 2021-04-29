package com.telecominfraproject.wlan.alarm.models.events;

import com.telecominfraproject.wlan.alarm.models.Alarm;
import com.telecominfraproject.wlan.systemevent.models.EquipmentEventWithPayload;

/**
 * @author dtoptygin
 *
 */
public class AlarmAddedEvent extends EquipmentEventWithPayload<Alarm> {
    private static final long serialVersionUID = 7142208487917559985L;

    /**
     * Constructor with current event time stamp
     * @param alarm
     */
    public AlarmAddedEvent(Alarm alarm){
        super(alarm.getCustomerId(), alarm.getEquipmentId(), alarm);
    }

    /**
	 * Constructor with explicit event time stamp. This constructor is used to
	 * workaround the issue with how the System events are defined in the cassandra
	 * schema: No two events can have the same combination of
	 * customerId+equipmentId+dataType+eventTimestamp, so the application code must
	 * work around this limitation by creating unique event time stamps if 2
	 * alarms are raised at the same time.
	 *
	 * @param alarm
	 * @param eventTimestamp
	 */
    public AlarmAddedEvent(Alarm alarm, long eventTimestamp){
        super(alarm.getCustomerId(), alarm.getEquipmentId(), eventTimestamp, alarm);
    }

    /**
     * Constructor used by JSON
     */
    public AlarmAddedEvent() {
        super(0, 0, 0, null);
    }
    
}
