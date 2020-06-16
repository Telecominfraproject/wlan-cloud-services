package com.telecominfraproject.wlan.firmware.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author ekeddy
 *
 */
public class CustomerFirmwareTrackRecord extends BaseJsonModel {
    private static final long serialVersionUID = 6049369594675825448L;

    private int customerId;
    private long trackRecordId;
    private CustomerFirmwareTrackSettings settings;
    
    
    private long createdTimestamp;
    private long lastModifiedTimestamp;

    
    public CustomerFirmwareTrackRecord() {
        
    }
    public CustomerFirmwareTrackRecord(long trackId, int customerId) {
        this.customerId = customerId;
        this.trackRecordId = trackId;
        this.settings = new CustomerFirmwareTrackSettings();
    }
    public long getTrackRecordId() {
        return trackRecordId;
    }
    public void setTrackRecordId(long trackRecordId) {
        this.trackRecordId = trackRecordId;
    }
    public int getCustomerId() {
        return customerId;
    }
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public CustomerFirmwareTrackSettings getSettings() {
        return settings;
    }
    public void setSettings(CustomerFirmwareTrackSettings settings) {
        this.settings = settings;
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

    @Override
    public int hashCode() {
        return Objects.hash(createdTimestamp, customerId, lastModifiedTimestamp, settings, trackRecordId);
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof CustomerFirmwareTrackRecord)) {
            return false;
        }
        CustomerFirmwareTrackRecord other = (CustomerFirmwareTrackRecord) obj;
        return this.createdTimestamp == other.createdTimestamp && this.customerId == other.customerId
                && this.lastModifiedTimestamp == other.lastModifiedTimestamp && Objects.equals(settings, other.settings)
                && this.trackRecordId == other.trackRecordId;
    }

    @Override
    public CustomerFirmwareTrackRecord clone() {
        CustomerFirmwareTrackRecord ret = (CustomerFirmwareTrackRecord) super.clone();
        if (ret.settings != null) {
            ret.settings = this.settings.clone();
        }
        return ret;
    }
    
    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        
        if (hasUnsupportedValue(settings)) {
            return true;
        }
        return false;
    }
    
}
