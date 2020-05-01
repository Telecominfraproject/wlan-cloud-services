package com.telecominfraproject.wlan.equipment.models;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author ekeddy
 *
 */
public class ActiveScanSettings extends BaseJsonModel {

    private static final long serialVersionUID = -8302353000975189803L;
    private static final boolean DEFAULT_ENABLED_SETTING = true;
    private static final int DEFAULT_SCAN_FREQUENCY_SECONDS = 10;
    private static final int DEFAULT_SCAN_DURATION_MILLIS = 65;

    private boolean enabled = DEFAULT_ENABLED_SETTING;
    private Integer scanFrequencySeconds = DEFAULT_SCAN_FREQUENCY_SECONDS;
    private Integer scanDurationMillis = DEFAULT_SCAN_DURATION_MILLIS;

    private ActiveScanSettings() {
        // for json
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Integer getScanFrequencySeconds() {
        return scanFrequencySeconds;
    }

    public void setScanFrequencySeconds(Integer scanFrequencySeconds) {
        this.scanFrequencySeconds = scanFrequencySeconds;
    }

    public Integer getScanDurationMillis() {
        return scanDurationMillis;
    }

    public void setScanDurationMillis(Integer scanDurationMillis) {
        this.scanDurationMillis = scanDurationMillis;
    }

    @Override
    public ActiveScanSettings clone() {
        return (ActiveScanSettings) super.clone();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (enabled ? 1231 : 1237);
        result = prime * result + ((scanDurationMillis == null) ? 0 : scanDurationMillis.hashCode());
        result = prime * result + ((scanFrequencySeconds == null) ? 0 : scanFrequencySeconds.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ActiveScanSettings other = (ActiveScanSettings) obj;
        if (enabled != other.enabled)
            return false;
        if (scanDurationMillis == null) {
            if (other.scanDurationMillis != null)
                return false;
        } else if (!scanDurationMillis.equals(other.scanDurationMillis))
            return false;
        if (scanFrequencySeconds == null) {
            if (other.scanFrequencySeconds != null)
                return false;
        } else if (!scanFrequencySeconds.equals(other.scanFrequencySeconds))
            return false;
        return true;
    }

    public static ActiveScanSettings createWithDefaults() {
        return new ActiveScanSettings();
    }

}
