package com.telecominfraproject.wlan.systemevent.models;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author ekeddy
 *
 */
public class SystemEventRecordId extends BaseJsonModel {
    private static final long serialVersionUID = -8445333816441019450L;

    private int customerId;
    private long equipmentId;
    private String payloadType;
    private long eventTimestamp;
    /**
     * 
     */
    public SystemEventRecordId() {
        super();
    }
    
    public SystemEventRecordId(int customerId, long equipmentId, String payloadType, long eventTimestamp) {
        super();
        this.customerId = customerId;
        this.equipmentId = equipmentId;
        this.payloadType = payloadType;
        this.eventTimestamp = eventTimestamp;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + customerId;
        result = prime * result + (int) (equipmentId ^ (equipmentId >>> 32));
        result = prime * result + (int) (eventTimestamp ^ (eventTimestamp >>> 32));
        result = prime * result + ((payloadType == null) ? 0 : payloadType.hashCode());
        return result;
    }
    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof SystemEventRecordId))
            return false;
        SystemEventRecordId other = (SystemEventRecordId) obj;
        if (customerId != other.customerId)
            return false;
        if (equipmentId != other.equipmentId)
            return false;
        if (eventTimestamp != other.eventTimestamp)
            return false;
        if (payloadType == null) {
            if (other.payloadType != null)
                return false;
        } else if (!payloadType.equals(other.payloadType))
            return false;
        return true;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getPayloadType() {
        return payloadType;
    }

    public void setPayloadType(String payloadType) {
        this.payloadType = payloadType;
    }

    public long getEventTimestamp() {
        return eventTimestamp;
    }

    public void setEventTimestamp(long eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }
    
    

}
