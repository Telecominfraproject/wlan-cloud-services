package com.telecominfraproject.wlan.profile.captiveportal.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

public class MacWhitelistRecord extends BaseJsonModel {
    private static final long serialVersionUID = -7260315843621754323L;

    private MacAddress macAddress;
    private String notes;

    private long lastModifiedTimestamp;


    public long getLastModifiedTimestamp() {
        return lastModifiedTimestamp;
    }

    public void setLastModifiedTimestamp(long lastModifiedTimestamp) {
        this.lastModifiedTimestamp = lastModifiedTimestamp;
    }

	public MacAddress getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(MacAddress macAddress) {
		this.macAddress = macAddress;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	@Override
	public int hashCode() {
		return Objects.hash(lastModifiedTimestamp, macAddress, notes);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof MacWhitelistRecord)) {
			return false;
		}
		MacWhitelistRecord other = (MacWhitelistRecord) obj;
		return lastModifiedTimestamp == other.lastModifiedTimestamp && Objects.equals(macAddress, other.macAddress)
				&& Objects.equals(notes, other.notes);
	}

}
