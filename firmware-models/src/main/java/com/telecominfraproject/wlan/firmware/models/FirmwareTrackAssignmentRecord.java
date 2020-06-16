package com.telecominfraproject.wlan.firmware.models;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author ekeddy
 *
 */
public class FirmwareTrackAssignmentRecord extends BaseJsonModel {
    private static final long serialVersionUID = 3094740666187212145L;
    private long trackRecordId;
    private long firmwareVersionRecordId;
    private boolean defaultRevisionForTrack = false;
    private boolean deprecated = false;
    private long createdTimestamp;
    private long lastModifiedTimestamp;


    public FirmwareTrackAssignmentRecord() {
        super();
    }

    public FirmwareTrackAssignmentRecord(long trackRecordId, long firmwareVersionRecordId) {
        super();
        this.trackRecordId = trackRecordId;
        this.firmwareVersionRecordId = firmwareVersionRecordId;
    }
    
    @Override
    public FirmwareTrackAssignmentRecord clone() {
        FirmwareTrackAssignmentRecord ret = (FirmwareTrackAssignmentRecord) super.clone();
        return ret;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof FirmwareTrackAssignmentRecord)) {
            return false;
        }
        FirmwareTrackAssignmentRecord other = (FirmwareTrackAssignmentRecord) obj;
        if (this.createdTimestamp != other.createdTimestamp) {
            return false;
        }
        if (this.defaultRevisionForTrack != other.defaultRevisionForTrack) {
            return false;
        }
        if (this.deprecated != other.deprecated) {
            return false;
        }
        if (this.firmwareVersionRecordId != other.firmwareVersionRecordId) {
            return false;
        }
        if (this.lastModifiedTimestamp != other.lastModifiedTimestamp) {
            return false;
        }
        if (this.trackRecordId != other.trackRecordId) {
            return false;
        }
        return true;
    }

    public long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public long getFirmwareVersionRecordId() {
        return firmwareVersionRecordId;
    }


    public long getLastModifiedTimestamp() {
        return lastModifiedTimestamp;
    }

    public long getTrackRecordId() {
        return trackRecordId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (this.createdTimestamp ^ (this.createdTimestamp >>> 32));
        result = prime * result + (this.defaultRevisionForTrack ? 1231 : 1237);
        result = prime * result + (this.deprecated ? 1231 : 1237);
        result = prime * result + (int) (this.firmwareVersionRecordId ^ (this.firmwareVersionRecordId >>> 32));
        result = prime * result + (int) (this.lastModifiedTimestamp ^ (this.lastModifiedTimestamp >>> 32));
        result = prime * result + (int) (this.trackRecordId ^ (this.trackRecordId >>> 32));
        return result;
    }


    public boolean isDefaultRevisionForTrack() {
        return defaultRevisionForTrack;
    }


    public boolean isDeprecated() {
        return deprecated;
    }

    public void setCreatedTimestamp(long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public void setDefaultRevisionForTrack(boolean defaultRevisionForTrack) {
        this.defaultRevisionForTrack = defaultRevisionForTrack;
    }

    public void setDeprecated(boolean deprecated) {
        this.deprecated = deprecated;
    }


    public void setFirmwareVersionRecordId(long firmwareVersionRecordId) {
        this.firmwareVersionRecordId = firmwareVersionRecordId;
    }

    public void setLastModifiedTimestamp(long lastModifiedTimestamp) {
        this.lastModifiedTimestamp = lastModifiedTimestamp;
    }

    public void setTrackRecordId(long trackRecordId) {
        this.trackRecordId = trackRecordId;
    }

}
