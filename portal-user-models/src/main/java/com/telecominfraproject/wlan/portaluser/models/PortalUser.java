package com.telecominfraproject.wlan.portaluser.models;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasCustomerId;
import com.telecominfraproject.wlan.core.model.role.PortalUserRole;

/**
 * @author dtoptygin
 *
 */
public class PortalUser extends BaseJsonModel implements HasCustomerId {
    
	private static final long serialVersionUID = 5570757656953699233L;
	
	private long id;
    private int customerId;

    //TODO: put more fields here, generate getters/setters for them
    private String username;
    private String password;
    private List<PortalUserRole> roles;
    
    private PortalUserDetails details;
    
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<PortalUserRole> getRoles() {
		return roles;
	}

	public void setRoles(List<PortalUserRole> roles) {
		// This is to cover the backwards-compatibility case when the
		// roles property is not provided, but the role property is provided.
		// This check can be removed when the deprecated getRole/setRole are
		// removed.
		if (roles != null) {
			this.roles = roles;
		}
	}

	@JsonIgnore
	@Deprecated
	public PortalUserRole getRole() {
		if (roles == null || roles.isEmpty()) {
			return null;
		}
		return roles.get(0);
	}
	
	@JsonIgnore
	@Deprecated
	public void setRole(PortalUserRole role) {
		roles = Arrays.asList(role);
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
    public PortalUser clone() {
    	PortalUser ret = (PortalUser) super.clone();
    	if(details!=null) {
    		ret.setDetails(details.clone());
    	}
    	
    	return ret;
    }

	@Override
	public int hashCode() {
		return Objects.hash(createdTimestamp, customerId, id, lastModifiedTimestamp, username, password, roles, details);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof PortalUser)) {
			return false;
		}
		PortalUser other = (PortalUser) obj;
		return createdTimestamp == other.createdTimestamp && customerId == other.customerId && id == other.id
				&& lastModifiedTimestamp == other.lastModifiedTimestamp 
				&& Objects.equals(username, other.username)
				&& Objects.equals(password, other.password)
				&& Objects.equals(roles, other.roles)
				&& Objects.equals(details, other.details);
	}
    
}
