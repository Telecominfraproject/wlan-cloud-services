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
    private long locationId;
    private long equipmentId;
    private long clientMac;
    private String dataType;
    private long eventTimestamp;

    public SystemEventRecordKey() {
        //for serialization
    }
    
    public SystemEventRecordKey(SystemEventRecord systemEventRecord) {
        this.customerId = systemEventRecord.getCustomerId();
        this.locationId = systemEventRecord.getLocationId();
        this.equipmentId = systemEventRecord.getEquipmentId();
        this.clientMac = systemEventRecord.getClientMac();
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

    public long getEventTimestamp() {
        return eventTimestamp;
    }

    public void setEventTimestamp(long eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
    }
    
	public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    public long getClientMac() {
        return clientMac;
    }

    public void setClientMac(long clientMac) {
        this.clientMac = clientMac;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    @Override
	public int hashCode() {
		return Objects.hash(customerId, locationId, equipmentId, clientMac, eventTimestamp, dataType);
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
		return customerId == other.customerId && locationId == other.locationId  && equipmentId == other.equipmentId
		        && clientMac == other.clientMac
				&& eventTimestamp == other.eventTimestamp && Objects.equals(dataType, other.dataType);
	}
    
    @Override
	public SystemEvent clone() {
		return (SystemEvent) super.clone();
	}

}
