package com.telecominfraproject.wlan.systemevent.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author dtoptygin
 *
 */
public class SystemEventRecord extends BaseJsonModel {
    private static final long serialVersionUID = 6763035984453691752L;

    private long id;
 
    private int customerId;
    private long equipmentId;
    private long eventTimestamp;

    private String payloadType;
    private SystemEvent payload;
    
    private long createdTimestamp;
    private long lastModifiedTimestamp;
    
    public SystemEventRecord() {
        // for serialization
    }

    @SuppressWarnings("rawtypes")
    public SystemEventRecord(SystemEvent systemEvent) {
        this.eventTimestamp = systemEvent.getEventTimestamp();
        this.payloadType = systemEvent.getClass().getSimpleName();
        this.payload = systemEvent;
        
        if(systemEvent instanceof CustomerEvent){
            this.customerId = ((CustomerEvent)systemEvent).getCustomerId();
        }
        
        if(systemEvent instanceof EquipmentEvent){
            this.equipmentId = ((EquipmentEvent)systemEvent).getEquipmentId();
        }
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

    public long getEventTimestamp() {
        return eventTimestamp;
    }

    public void setEventTimestamp(long eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }

    public String getPayloadType() {
        return payloadType;
    }

    public void setPayloadType(String payloadType) {
        this.payloadType = payloadType;
    }

    public SystemEvent getPayload() {
        return payload;
    }

    public void setPayload(SystemEvent payload) {
        this.payload = payload;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public long getLastModifiedTimestamp() {
        return lastModifiedTimestamp;
    }

    public void setLastModifiedTimestamp(long lastModifiedTimestamp) {
        this.lastModifiedTimestamp = lastModifiedTimestamp;
    }

    @Override
    public SystemEventRecord clone() {
        SystemEventRecord ret = (SystemEventRecord) super.clone();
        if(payload!=null){
            ret.payload = (SystemEvent) this.payload.clone();
        }
        
        return ret;
    }
    
    @JsonIgnore
    public SystemEventRecordId getRecordId() {
        return new SystemEventRecordId(customerId, equipmentId, payloadType, eventTimestamp);
    }
    
    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        
        if (hasUnsupportedValue(payload)) {
            return true;
        }
        return false;
    }
    
}
