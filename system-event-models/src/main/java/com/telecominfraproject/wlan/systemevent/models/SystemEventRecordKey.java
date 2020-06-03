package com.telecominfraproject.wlan.systemevent.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author ekeddy
 *
 */
public class SystemEventRecordKey extends BaseJsonModel {
    private static final long serialVersionUID = -8445333816441019450L;

    private int customerId;
    private long equipmentId;
    private String dataType;
    private long eventTimestamp;

    public SystemEventRecordKey() {
        super();
    }
    
    public SystemEventRecordKey(int customerId, long equipmentId, String dataType, long eventTimestamp) {
        super();
        this.customerId = customerId;
        this.equipmentId = equipmentId;
        this.dataType = dataType;
        this.eventTimestamp = eventTimestamp;
    }

    public SystemEventRecordKey(SystemEventRecord systemEventRecord) {
        super();
        this.customerId = systemEventRecord.getCustomerId();
        this.equipmentId = systemEventRecord.getEquipmentId();
        this.dataType = systemEventRecord.getDataType();
        this.eventTimestamp = systemEventRecord.getEventTimestamp();
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
        return dataType;
    }

    public void setPayloadType(String payloadType) {
        this.dataType = payloadType;
    }

    public long getEventTimestamp() {
        return eventTimestamp;
    }

    public void setEventTimestamp(long eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }

	@Override
	public int hashCode() {
		return Objects.hash(customerId, equipmentId, eventTimestamp, dataType);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof SystemEventRecordKey)) {
			return false;
		}
		SystemEventRecordKey other = (SystemEventRecordKey) obj;
		return customerId == other.customerId && equipmentId == other.equipmentId
				&& eventTimestamp == other.eventTimestamp && Objects.equals(dataType, other.dataType);
	}
    
    @Override
	public SystemEvent clone() {
		return (SystemEvent) super.clone();
	}

}
