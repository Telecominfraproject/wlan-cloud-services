package com.telecominfraproject.wlan.alarm.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasCustomerId;

/**
 * @author dtoptygin
 *
 */
public class Alarm extends BaseJsonModel implements HasCustomerId {
    
	private static final long serialVersionUID = 5570757656953699233L;
	
	private long id;
    private int customerId;

    //TODO: put more fields here, generate getters/setters for them
    private String sampleStr;
    private AlarmDetails details;
    
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

    public void setSampleStr(String sampleStr) {
        this.sampleStr = sampleStr;
    }

    public String getSampleStr() {
        return sampleStr;
    }

	public AlarmDetails getDetails() {
		return details;
	}

	public void setDetails(AlarmDetails details) {
		this.details = details;
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
	public int hashCode() {
		return Objects.hash(createdTimestamp, customerId, id, lastModifiedTimestamp, sampleStr, details);
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
		return createdTimestamp == other.createdTimestamp && customerId == other.customerId && id == other.id
				&& lastModifiedTimestamp == other.lastModifiedTimestamp 
				&& Objects.equals(sampleStr, other.sampleStr)
				&& Objects.equals(details, other.details);
	}
    
}
