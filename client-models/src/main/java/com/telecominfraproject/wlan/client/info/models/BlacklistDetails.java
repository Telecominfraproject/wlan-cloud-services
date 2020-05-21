package com.telecominfraproject.wlan.client.info.models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.role.PortalUserRole;

/**
 * 
 * The blacklisting attributes for a client device.
 * 
 * Blacklisting is enabled for a client when all the the following apply:
 * <li> the enabled flag is set to true 
 * <li> the startTime is null or less than or equal to the current time 
 * <li> the endTime is null or greater than or equal to the current time
 * 
 * @author ekeddy
 *
 */
public class BlacklistDetails extends BaseJsonModel {

    private static final long serialVersionUID = 2251097210417403911L;

    /**
     * When enabled, blacklisting applies to the client, subject to
     * the optional start/end times.
     */
    private boolean enabled;
    
    /**
     * Optional startTime when blacklisting becomes enabled.
     */
    private Long startTime;
    
    /**
     * Optional endTime when blacklisting ceases to be enabled
     */
    private Long endTime;
    

    public BlacklistDetails() {
    }
    
    public BlacklistDetails(boolean enabled, Long startTime, Long endTime, PortalUserRole ownerType) {
        this.enabled = enabled;
        this.startTime = startTime;
        this.endTime = endTime;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    public Long getStartTime() {
        return startTime;
    }
    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }
    public Long getEndTime() {
        return endTime;
    }
    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }
        
    @Override
	public int hashCode() {
		return Objects.hash(enabled, endTime, startTime);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof BlacklistDetails)) {
			return false;
		}
		BlacklistDetails other = (BlacklistDetails) obj;
		return enabled == other.enabled && Objects.equals(endTime, other.endTime)
				&& Objects.equals(startTime, other.startTime);
	}

	@Override
    public BlacklistDetails clone() {
        BlacklistDetails ret = (BlacklistDetails) super.clone();
        return ret;
    }    
    
    @JsonIgnore
    public boolean isActive() 
    {
        return isActive(System.currentTimeMillis());
    }
    
    @JsonIgnore
    public boolean isExpired() 
    {
        return isExpired(System.currentTimeMillis());
    }
    
    // Great for unit testing! 
    @JsonIgnore
    public boolean isActive(long curTime) 
    {
        if(!enabled) {
            return false;
        }

        return (startTime == null || curTime>startTime) && (endTime==null || curTime <endTime);
    }

    
    // Great for unit testing! 
    @JsonIgnore
    public boolean isExpired(long curTime) 
    {
        return endTime != null && curTime >= endTime;
    }
    
    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }

        return false;
    }
    
}
