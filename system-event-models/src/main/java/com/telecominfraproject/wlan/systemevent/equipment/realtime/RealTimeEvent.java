package com.telecominfraproject.wlan.systemevent.equipment.realtime;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.json.interfaces.HasCustomerId;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasEquipmentId;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasProducedTimestamp;
import com.telecominfraproject.wlan.systemevent.models.SystemEvent;


public abstract class RealTimeEvent extends SystemEvent implements HasCustomerId, HasEquipmentId, HasProducedTimestamp {

	private static final long serialVersionUID = -406572942058780057L;

    /**
     * Type of event
     */
    private RealTimeEventType eventType;

    private long equipmentId;

    private int customerId;

    protected RealTimeEvent() {
        // serial
    	super(0);
    }

    protected RealTimeEvent(RealTimeEventType eventType, int customerId, long equipmentId, Long timestamp) {
        super(timestamp);
        this.customerId = customerId;
        this.equipmentId = equipmentId;
        this.eventType = eventType;
    }

    protected RealTimeEvent(RealTimeEventType eventType, Long timestamp) {
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
        result = prime * result + Objects.hash(customerId, equipmentId, eventType);
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
        return customerId == other.customerId && equipmentId == other.equipmentId
                && Objects.equals(eventType, other.eventType);
    }
    
}
