package com.telecominfraproject.wlan.firmware.models;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.scheduler.DailyTimeRangeSchedule;
import com.telecominfraproject.wlan.core.model.scheduler.EmptySchedule;
import com.telecominfraproject.wlan.core.model.scheduler.ScheduleRestriction;
import com.telecominfraproject.wlan.core.model.scheduler.ScheduleSetting;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;

/**
 * 
 * @author ekeddy
 *
 */
public class FirmwareTrackRecord extends BaseJsonModel {

    private static final long serialVersionUID = 1179471669381335112L;
    public static final String DEFAULT_TRACK_NAME = "DEFAULT";
    private static final Set<Class<? extends ScheduleSetting>> SUPPORTED_MAINTENANCE_TYPE = new HashSet<>(
            Arrays.asList(EmptySchedule.class, DailyTimeRangeSchedule.class));

    /**
     * Check if this is supported as maintenance window
     * 
     * @param maintenanceWindow
     * @return
     */
    public static boolean isSupportedMaintenanceSetting(final ScheduleSetting maintenanceWindow) {
        return SUPPORTED_MAINTENANCE_TYPE.contains(maintenanceWindow.getClass());
    }

    private long recordId;
    private String trackName;
    private ScheduleSetting maintenanceWindow;
    private long createdTimestamp;

    private long lastModifiedTimestamp;

    public FirmwareTrackRecord() {
    }

    public FirmwareTrackRecord(String trackName) {
        this.trackName = trackName;
    }

    @Override
    public FirmwareTrackRecord clone() {
        FirmwareTrackRecord ret = (FirmwareTrackRecord) super.clone();
        if (null != this.maintenanceWindow) {
            ret.maintenanceWindow = this.maintenanceWindow.clone();
        }
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
        if (!(obj instanceof FirmwareTrackRecord)) {
            return false;
        }
        FirmwareTrackRecord other = (FirmwareTrackRecord) obj;
        if (this.createdTimestamp != other.createdTimestamp) {
            return false;
        }
        if (this.lastModifiedTimestamp != other.lastModifiedTimestamp) {
            return false;
        }
        if (this.maintenanceWindow == null) {
            if (other.maintenanceWindow != null) {
                return false;
            }
        } else if (!this.maintenanceWindow.equals(other.maintenanceWindow)) {
            return false;
        }
        if (this.recordId != other.recordId) {
            return false;
        }
        if (this.trackName == null) {
            if (other.trackName != null) {
                return false;
            }
        } else if (!this.trackName.equals(other.trackName)) {
            return false;
        }
        return true;
    }

    public long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public long getLastModifiedTimestamp() {
        return lastModifiedTimestamp;
    }

    public ScheduleSetting getMaintenanceWindow() {
        return maintenanceWindow;
    }

    public long getRecordId() {
        return recordId;
    }

    public String getTrackName() {
        return trackName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (this.createdTimestamp ^ (this.createdTimestamp >>> 32));
        result = prime * result + (int) (this.lastModifiedTimestamp ^ (this.lastModifiedTimestamp >>> 32));
        result = prime * result + ((this.maintenanceWindow == null) ? 0 : this.maintenanceWindow.hashCode());
        result = prime * result + (int) (this.recordId ^ (this.recordId >>> 32));
        result = prime * result + ((this.trackName == null) ? 0 : this.trackName.hashCode());
        return result;
    }

    public void setCreatedTimestamp(long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public void setLastModifiedTimestamp(long lastModifiedTimestamp) {
        this.lastModifiedTimestamp = lastModifiedTimestamp;
    }

    public void setMaintenanceWindow(ScheduleSetting maintenanceWindow) {
        this.maintenanceWindow = maintenanceWindow;
    }

    public void setRecordId(long recordId) {
        this.recordId = recordId;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public void validateValue() {
        if (hasUnsupportedValue()) {
            throw new DsDataValidationException("Setting contains unsupported value");
        }
        if (null != this.maintenanceWindow) {
            if (!isSupportedMaintenanceSetting(this.maintenanceWindow)) {
                throw new DsDataValidationException(
                        "Unsupported maintenance window type " + this.maintenanceWindow.getClass().getSimpleName());
            }
            ScheduleRestriction.FIRMWARE_MAINTENANCE.isValidSchedule(this.maintenanceWindow);
        }
    }
}
