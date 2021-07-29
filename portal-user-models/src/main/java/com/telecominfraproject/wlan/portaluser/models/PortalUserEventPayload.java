package com.telecominfraproject.wlan.portaluser.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasCustomerId;

/*
 * For third party users that require a simplified PortalUser to populate events
 */
public class PortalUserEventPayload extends BaseJsonModel implements HasCustomerId {
    
	private static final long serialVersionUID = 5570757656953699233L;
	
	private long id;
    private int customerId;
    private String username;
    private PortalUserDetails details;
    
    private long createdTimestamp;
    private long lastModifiedTimestamp;
    
    public PortalUserEventPayload() {}
    
    public PortalUserEventPayload(PortalUser portalUser) {
        this.setId(portalUser.getId());
        this.setCustomerId(portalUser.getCustomerId());

        this.setUsername(portalUser.getUsername());
                
        if(portalUser.getDetails() !=null) {
            this.setDetails(portalUser.getDetails());
        }
        
        this.setCreatedTimestamp(portalUser.getCreatedTimestamp());
        this.setLastModifiedTimestamp(portalUser.getLastModifiedTimestamp());
    }
    
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

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

	public PortalUserDetails getDetails() {
		return details;
	}
	
	public void setDetails(PortalUserDetails details) {
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
    public PortalUserEventPayload clone() {
    	PortalUserEventPayload ret = (PortalUserEventPayload) super.clone();
    	if(details!=null) {
    		ret.setDetails(details.clone());
    	}
    	
    	return ret;
    }

	@Override
	public int hashCode() {
		return Objects.hash(createdTimestamp, customerId, id, lastModifiedTimestamp, username, details);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof PortalUserEventPayload)) {
			return false;
		}
		PortalUserEventPayload other = (PortalUserEventPayload) obj;
		return createdTimestamp == other.createdTimestamp && customerId == other.customerId && id == other.id
				&& lastModifiedTimestamp == other.lastModifiedTimestamp 
				&& Objects.equals(username, other.username)
				&& Objects.equals(details, other.details);
	}
    
}
