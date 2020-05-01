package com.telecominfraproject.wlan.equipment.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

public class BannedChannel extends BaseJsonModel {
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
