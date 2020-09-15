package com.telecominfraproject.wlan.systemevent.equipment.realtime;

import java.util.Objects;

import com.telecominfraproject.wlan.systemevent.models.SystemEvent;


public abstract class RealTimeEvent extends SystemEvent {

    /**
     * 
     */
    private static final long serialVersionUID = -7566162295309710952L;

    /**
     * Timestamp when event occurred
     */
    private Long eventTimestamp;

    /**
     * Type of event
     */
    private RealTimeEventType eventType;

    private long equipmentId;

    private int customerId;

    protected RealTimeEvent() {
        // serial
    }

    protected RealTimeEvent(RealTimeEventType eventType, int customerId, long equipmentId, Long timestamp) {
        super(timestamp);
        this.customerId = customerId;
        this.equipmentId = equipmentId;
        this.eventType = eventType;
        eventTimestamp = timestamp;
    }

    protected RealTimeEvent(RealTimeEventType eventType, Long timestamp) {
        super(timestamp);
        this.eventType = eventType;
        eventTimestamp = timestamp;
    }

    @Override
    public RealTimeEvent clone() {
        return (RealTimeEvent) super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof RealTimeEvent)) {
            return false;
        }
        RealTimeEvent other = (RealTimeEvent) obj;
        return Objects.equals(eventTimestamp, other.eventTimestamp) && (eventType == other.eventType);
    }

    @Override
    public long getEventTimestamp() {
        return eventTimestamp;
    }

    public RealTimeEventType getEventType() {
        return eventType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = (prime * result) + Objects.hash(eventTimestamp, eventType);
        return result;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (RealTimeEventType.isUnsupported(eventType)) {
            return true;
        }
        return false;
    }

    public void setEventType(RealTimeEventType eventType) {
        this.eventType = eventType;
    }

    @Override
    public int getCustomerId() {
        // subclasses will override this as needed
        return customerId;
    }

    @Override
    public long getEquipmentId() {
        // subclasses will override this as needed
        return equipmentId;
    }

}
