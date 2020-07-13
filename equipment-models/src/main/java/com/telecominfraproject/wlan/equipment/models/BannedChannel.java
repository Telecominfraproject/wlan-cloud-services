package com.telecominfraproject.wlan.equipment.models;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

public class BannedChannel extends BaseJsonModel {
    /* Max banned duration (30 minutes) */
    public static final long MAX_BANNED_DURATION_IN_MS = TimeUnit.MINUTES.toMillis(30);
    
    private static final long serialVersionUID = 3362440598165746886L;
    private int channelNumber;
    private long bannedOnEpoc;

    public BannedChannel() {

    }

    public BannedChannel(int channelNumber, long bannedOnEpoc) {
        this.channelNumber = channelNumber;
        this.bannedOnEpoc = bannedOnEpoc;
    }

    public int getChannelNumber() {
        return channelNumber;
    }

    public void setChannelNumber(int channelNumber) {
        this.channelNumber = channelNumber;
    }

    public long getBannedOnEpoc() {
        return bannedOnEpoc;
    }

    public void setBannedOnEpoc(long bannedOnEpoc) {
        this.bannedOnEpoc = bannedOnEpoc;
    }

    @JsonIgnore
    public boolean isMaxBannedDurationExceeded() {
    	return bannedOnEpoc > System.currentTimeMillis() - MAX_BANNED_DURATION_IN_MS;
    }
    
	@Override
	public int hashCode() {
		return Objects.hash(bannedOnEpoc, channelNumber);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof BannedChannel)) {
			return false;
		}
		BannedChannel other = (BannedChannel) obj;
		return bannedOnEpoc == other.bannedOnEpoc && channelNumber == other.channelNumber;
	}

}
