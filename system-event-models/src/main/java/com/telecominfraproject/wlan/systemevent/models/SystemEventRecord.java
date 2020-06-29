package com.telecominfraproject.wlan.systemevent.models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasCustomerId;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasEquipmentId;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasProducedTimestamp;

/**
 * @author dtoptygin
 *
 */
public class SystemEventRecord extends BaseJsonModel implements HasProducedTimestamp {
    private static final long serialVersionUID = 6763035984453691752L;

    private int customerId;
    private long equipmentId;
    private String dataType;
    private long eventTimestamp;

    private SystemEvent details;
    
    public SystemEventRecord() {
        // for serialization
    }

    public SystemEventRecord(SystemEvent systemEvent) {
        this.eventTimestamp = systemEvent.getEventTimestamp();
        this.dataType = systemEvent.getClass().getSimpleName();
        this.details = systemEvent;
        
        if(systemEvent instanceof HasCustomerId){
            this.customerId = ((HasCustomerId)systemEvent).getCustomerId();
        }
        
        if(systemEvent instanceof HasEquipmentId){
            this.equipmentId = ((HasEquipmentId)systemEvent).getEquipmentId();
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

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public SystemEvent getDetails() {
        return details;
    }

    public void setDetails(SystemEvent details) {
    	if(details!=null) {
    		dataType = details.getClass().getSimpleName();
    	}
        this.details = details;
    }

    @Override
    public SystemEventRecord clone() {
        SystemEventRecord ret = (SystemEventRecord) super.clone();
        if(details!=null){
            ret.details = this.details.clone();
        }
        
        return ret;
    }
    
    @JsonIgnore
    public SystemEventRecordKey getRecordKey() {
        return new SystemEventRecordKey(customerId, equipmentId, dataType, eventTimestamp);
    }
    
    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        
        if (hasUnsupportedValue(details)) {
            return true;
        }
        return false;
    }

	@Override
	public int hashCode() {
		return Objects.hash(customerId, equipmentId, eventTimestamp, details, dataType);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof SystemEventRecord)) {
			return false;
		}
		SystemEventRecord other = (SystemEventRecord) obj;
		return customerId == other.customerId && equipmentId == other.equipmentId
				&& eventTimestamp == other.eventTimestamp && Objects.equals(details, other.details)
				&& Objects.equals(dataType, other.dataType);
	}

	@Override
	@JsonIgnore
	public long getProducedTimestampMs() {
		return eventTimestamp;
	}
    
}
