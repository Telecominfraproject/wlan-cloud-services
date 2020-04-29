package com.telecominfraproject.wlan.profile.models;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasCustomerId;

/**
 * @author dtoptygin
 *
 */
public class Profile extends BaseJsonModel implements HasCustomerId {
    
	private static final long serialVersionUID = 5570757656953699233L;
	
	private long id;
    private int customerId;

    //TODO: put more fields here, generate getters/setters for them
    private ProfileType profileType;
    private String name;
    private ProfileDetails details;
    
    private long createdTimestamp;
    private long lastModifiedTimestamp;

    private Set<Long> childProfileIds = new HashSet<>();

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

	public ProfileDetails getDetails() {
		return details;
	}

	public void setDetails(ProfileDetails details) {
		this.details = details;
	}

	public ProfileType getProfileType() {
		return profileType;
	}

	public void setProfileType(ProfileType profileType) {
		this.profileType = profileType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Long> getChildProfileIds() {
		return childProfileIds;
	}

	public void setChildProfileIds(Set<Long> childProfileIds) {
		this.childProfileIds.clear();
		
		if(childProfileIds!=null && !childProfileIds.isEmpty()) {
			this.childProfileIds.addAll(childProfileIds);
		}
	}

	@Override
	public boolean hasUnsupportedValue() {
		if (super.hasUnsupportedValue()) {
			return true;
		}

		if(profileType==null || ProfileType.isUnsupported(profileType)) {
			return true;
		}
		
		if(details!=null && details.hasUnsupportedValue()) {
			return true;
		}
		
		return false;
	}
	
    @Override
    public Profile clone() {
    	Profile ret = (Profile) super.clone();
    	if(details!=null) {
    		ret.setDetails(details.clone());
    	}
    	
    	if(childProfileIds!=null) {
    		ret.childProfileIds = new HashSet<>(childProfileIds);
    	}
    	
    	return ret;
    }

	@Override
	public int hashCode() {
		return Objects.hash(createdTimestamp, customerId, id, lastModifiedTimestamp, name, profileType, details, childProfileIds);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Profile)) {
			return false;
		}
		Profile other = (Profile) obj;
		return createdTimestamp == other.createdTimestamp && customerId == other.customerId && id == other.id
				&& lastModifiedTimestamp == other.lastModifiedTimestamp 
				&& Objects.equals(name, other.name)
				&& Objects.equals(profileType, other.profileType)
				&& Objects.equals(details, other.details)
				&& Objects.equals(childProfileIds, other.childProfileIds)
				;
	}
    
}
