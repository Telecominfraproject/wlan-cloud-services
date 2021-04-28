package com.telecominfraproject.wlan.systemevent.equipment.realtime;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.json.interfaces.HasLocationId;
import com.telecominfraproject.wlan.systemevent.models.SystemEvent;

public abstract class RealTimeEvent extends SystemEvent implements HasLocationId {

    private static final long serialVersionUID = -406572942058780057L;

    /**
     * Type of event
     */
    private RealTimeEventType eventType;

    private long equipmentId;

    private long locationId;

    private int customerId;
    

    public RealTimeEvent() {

    }

    /**
     * Creates a RealTimeEvent with the current time stamp.
     * @param eventType
     * @param customerId
     * @param locationId
     * @param equipmentId
     */
    public RealTimeEvent(RealTimeEventType eventType, int customerId, long locationId, long equipmentId) {
        super(System.currentTimeMillis());
        this.customerId = customerId;
        this.locationId = locationId;
        this.equipmentId = equipmentId;
        this.eventType = eventType;
    }

    public RealTimeEvent(RealTimeEventType eventType, int customerId, long locationId, long equipmentId, Long timestamp) {
        super(timestamp);
        this.customerId = customerId;
        this.locationId = locationId;
        this.equipmentId = equipmentId;
        this.eventType = eventType;
    }

    public RealTimeEvent(RealTimeEventType eventType, Long timestamp) {
        super(timestamp);
        this.eventType = eventType;
    }

    public void setEventType(RealTimeEventType eventType) {
        this.eventType = eventType;
    }

    @Override
    public int getCustomerId() {
        return customerId;
    }

    @Override
    public long getEquipmentId() {
        return equipmentId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setEquipmentId(long equipmentId) {
        this.equipmentId = equipmentId;
    }

    
    @Override
    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    @Override
    public RealTimeEvent clone() {
        return (RealTimeEvent) super.clone();
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(customerId, locationId, equipmentId, eventType);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        RealTimeEvent other = (RealTimeEvent) obj;
        return customerId == other.customerId && locationId == other.locationId && equipmentId == other.equipmentId
                && Objects.equals(eventType, other.eventType);
    }

}
