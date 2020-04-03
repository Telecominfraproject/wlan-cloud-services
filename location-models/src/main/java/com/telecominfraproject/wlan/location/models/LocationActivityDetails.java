package com.telecominfraproject.wlan.location.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

public class LocationActivityDetails extends BaseJsonModel {
	
    private static final long serialVersionUID = 1984631488472859086L;
    private String busyTime; // Format XX:XX (24h syntax) 
    private String quietTime; // Format XX:XX (24h syntax)
    private String timezone;
    
    private long lastBusySnapshot;

    public LocationActivityDetails()
    {
        // for serialization
        this.busyTime = "13:30";
        this.quietTime = "3:00";
        
        this.lastBusySnapshot = 0L;
        
        this.timezone = "Canada/Eastern";
    }

    public String getBusyTime() {
        return busyTime;
    }

    public void setBusyTime(String busyTime) {
        this.busyTime = busyTime;
    }

    public String getQuietTime() {
        return quietTime;
    }

    public void setQuietTime(String quietTimeEpoc) {
        this.quietTime = quietTimeEpoc;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public long getLastBusySnapshot() {
        return lastBusySnapshot;
    }

    public void setLastBusySnapshot(long lastBusySnapshot) {
        this.lastBusySnapshot = lastBusySnapshot;
    }

	@Override
	public int hashCode() {
		return Objects.hash(busyTime, lastBusySnapshot, quietTime, timezone);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof LocationActivityDetails)) {
			return false;
		}
		LocationActivityDetails other = (LocationActivityDetails) obj;
		return Objects.equals(busyTime, other.busyTime) && lastBusySnapshot == other.lastBusySnapshot
				&& Objects.equals(quietTime, other.quietTime)
				&& Objects.equals(timezone, other.timezone);
	}

    
}
