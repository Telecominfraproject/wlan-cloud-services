package com.telecominfraproject.wlan.alarm.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasCustomerId;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasEquipmentId;
import com.telecominfraproject.wlan.status.models.StatusCode;

/**
 * @author dtoptygin
 *
 */
public class Alarm extends BaseJsonModel implements HasCustomerId, HasEquipmentId, Comparable<Alarm> {
    
	private static final long serialVersionUID = 5570757656953699233L;
	
    private int customerId;
    private long equipmentId;
    private AlarmCode alarmCode;
    private long createdTimestamp;
    
    private OriginatorType originatorType = OriginatorType.AP;
    private StatusCode severity = StatusCode.error;
    private AlarmScopeType scopeType = AlarmScopeType.EQUIPMENT;
    private String scopeId = "-1";
    private AlarmDetails details;
    private boolean acknowledged;
    
    private long lastModifiedTimestamp;

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

	public AlarmCode getAlarmCode() {
		return alarmCode;
	}

	public void setAlarmCode(AlarmCode alarmCode) {
		this.alarmCode = alarmCode;
	}

	public long getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(long createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	public OriginatorType getOriginatorType() {
		return originatorType;
	}

	public void setOriginatorType(OriginatorType originatorType) {
		this.originatorType = originatorType;
	}

	public StatusCode getSeverity() {
		return severity;
	}

	public void setSeverity(StatusCode severity) {
		this.severity = severity;
	}

	public AlarmScopeType getScopeType() {
		return scopeType;
	}

	public void setScopeType(AlarmScopeType scopeType) {
		this.scopeType = scopeType;
	}

	public String getScopeId() {
		return scopeId;
	}

	public void setScopeId(String scopeId) {
		this.scopeId = scopeId;
	}

	public AlarmDetails getDetails() {
		return details;
	}

	public void setDetails(AlarmDetails details) {
		this.details = details;
	}

	public boolean isAcknowledged() {
		return acknowledged;
	}

	public void setAcknowledged(boolean acknowledged) {
		this.acknowledged = acknowledged;
	}

	public long getLastModifiedTimestamp() {
		return lastModifiedTimestamp;
	}

	public void setLastModifiedTimestamp(long lastModifiedTimestamp) {
		this.lastModifiedTimestamp = lastModifiedTimestamp;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(acknowledged, alarmCode, details, createdTimestamp, customerId, equipmentId,
				lastModifiedTimestamp, originatorType, scopeId, scopeType, severity);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Alarm)) {
			return false;
		}
		Alarm other = (Alarm) obj;
		return acknowledged == other.acknowledged && alarmCode == other.alarmCode
				&& Objects.equals(details, other.details) && createdTimestamp == other.createdTimestamp
				&& customerId == other.customerId && equipmentId == other.equipmentId
				&& lastModifiedTimestamp == other.lastModifiedTimestamp && originatorType == other.originatorType
				&& Objects.equals(scopeId, other.scopeId) && scopeType == other.scopeType && severity == other.severity;
	}

	@Override
	public boolean hasUnsupportedValue() {
		if (super.hasUnsupportedValue()) {
			return true;
		}

		if(details!=null && details.hasUnsupportedValue()) {
			return true;
		}
		
		return false;
	}
	
    @Override
    public Alarm clone() {
    	Alarm ret = (Alarm) super.clone();
    	if(details!=null) {
    		ret.setDetails(details.clone());
    	}
    	
    	return ret;
    }

    @Override
    public int compareTo(Alarm o) {
    	int ret = Integer.compare(customerId, o.customerId);
    	if(ret == 0) {
    			ret = Long.compare(equipmentId, o.equipmentId);
    	    	if(ret == 0) {
        			ret = Integer.compare(alarmCode.getId(), o.alarmCode.getId());
        	    	if(ret == 0) {
            			ret = Long.compare(createdTimestamp, o.createdTimestamp);
        	    	}
    	    	}    	    	
    	}
    	
    	return ret;
    }
}
