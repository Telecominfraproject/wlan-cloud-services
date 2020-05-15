package com.telecominfraproject.wlan.status.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasCustomerId;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasEquipmentId;

/**
 * @author dtoptygin
 *
 */
public class Status extends BaseJsonModel implements HasCustomerId, HasEquipmentId {
    
	private static final long serialVersionUID = 5570757656953699233L;
	
	private long id;
    private int customerId;
    private long equipmentId;

    //TODO: put more fields here, generate getters/setters for them
    private StatusDataType statusDataType;
    private StatusDetails details;
    
    private long createdTimestamp;
    private long lastModifiedTimestamp;
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
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

	public StatusDetails getDetails() {
		return details;
	}

	public void setDetails(StatusDetails details) {
		this.details = details;
	}
	
	public long getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(long equipmentId) {
		this.equipmentId = equipmentId;
	}

	public StatusDataType getStatusDataType() {
		return statusDataType;
	}

	public void setStatusDataType(StatusDataType statusDataType) {
		this.statusDataType = statusDataType;
	}

	@Override
	public boolean hasUnsupportedValue() {
		if (super.hasUnsupportedValue()) {
			return true;
		}

		if(details!=null && details.hasUnsupportedValue()) {
			return true;
		}

		if(statusDataType!=null && StatusDataType.isUnsupported(statusDataType)) {
			return true;
		}

		return false;
	}
	
    @Override
    public Status clone() {
    	Status ret = (Status) super.clone();
    	if(details!=null) {
    		ret.setDetails(details.clone());
    	}
    	
    	return ret;
    }

	@Override
	public int hashCode() {
		return Objects.hash(createdTimestamp, customerId, details, equipmentId, id, lastModifiedTimestamp,
				statusDataType);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Status)) {
			return false;
		}
		Status other = (Status) obj;
		return createdTimestamp == other.createdTimestamp && customerId == other.customerId
				&& Objects.equals(details, other.details) && equipmentId == other.equipmentId && id == other.id
				&& lastModifiedTimestamp == other.lastModifiedTimestamp && statusDataType == other.statusDataType;
	}

    
}
