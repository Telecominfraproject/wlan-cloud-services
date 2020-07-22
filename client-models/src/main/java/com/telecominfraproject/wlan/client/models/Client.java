package com.telecominfraproject.wlan.client.models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasCustomerId;

/**
 * @author dtoptygin
 *
 */
public class Client extends BaseJsonModel implements HasCustomerId {
    
	private static final long serialVersionUID = 5570757656953699233L;
	
	private MacAddress macAddress;
    private int customerId;

    //TODO: put more fields here, generate getters/setters for them
    private ClientDetails details;
    
    private long createdTimestamp;
    private long lastModifiedTimestamp;
    
	@JsonIgnore
    //not-serializable flag, used to propagate the information to the datasource caller about whether to generate ClientBlockListChangedEvent
    private boolean needToUpdateBlocklist;
    
	public MacAddress getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(MacAddress macAddress) {
		this.macAddress = macAddress;
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

	public ClientDetails getDetails() {
		return details;
	}

	public void setDetails(ClientDetails details) {
		this.details = details;
	}

	@JsonIgnore
	public boolean isNeedToUpdateBlocklist() {
		return needToUpdateBlocklist;
	}

	@JsonIgnore
	public void setNeedToUpdateBlocklist(boolean needToUpdateBlocklist) {
		this.needToUpdateBlocklist = needToUpdateBlocklist;
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
    public Client clone() {
    	Client ret = (Client) super.clone();
    	if(details!=null) {
    		ret.setDetails(details.clone());
    	}
    	
    	return ret;
    }

	@Override
	public int hashCode() {
		return Objects.hash(createdTimestamp, customerId, macAddress, lastModifiedTimestamp, details);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Client)) {
			return false;
		}
		Client other = (Client) obj;
		return createdTimestamp == other.createdTimestamp && customerId == other.customerId 
				&& lastModifiedTimestamp == other.lastModifiedTimestamp 
				&& Objects.equals(macAddress, other.macAddress)
				&& Objects.equals(details, other.details);
	}
    
}
