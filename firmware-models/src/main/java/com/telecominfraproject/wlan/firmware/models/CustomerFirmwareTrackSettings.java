package com.telecominfraproject.wlan.firmware.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

/**
 * @author ekeddy
 *
 */
public class CustomerFirmwareTrackSettings extends BaseJsonModel {

    public enum TrackFlag {
        ALWAYS, NEVER, DEFAULT,

        UNSUPPORTED;

        @JsonCreator
        public static TrackFlag getByName(String value) {
            return JsonDeserializationUtils.deserializEnum(value, TrackFlag.class, UNSUPPORTED);
        }

        public static boolean isUnsupported(TrackFlag value) {
            return UNSUPPORTED.equals(value);
        }
    }

    private static final long serialVersionUID = -3619276433840685552L;

    public static CustomerFirmwareTrackSettings combine(final CustomerFirmwareTrackSettings customerOverrideSettings,
            final CustomerFirmwareTrackSettings systemDefaultSettings) {
        CustomerFirmwareTrackSettings ret = customerOverrideSettings.clone();
        if (TrackFlag.DEFAULT.equals(ret.autoUpgradeDeprecatedOnBind)) {
            ret.autoUpgradeDeprecatedOnBind = systemDefaultSettings.autoUpgradeDeprecatedOnBind;
        }
        if (TrackFlag.DEFAULT.equals(ret.autoUpgradeUnknownOnBind)) {
            ret.autoUpgradeUnknownOnBind = systemDefaultSettings.autoUpgradeUnknownOnBind;
        }
        if (TrackFlag.DEFAULT.equals(ret.autoUpgradeDeprecatedDuringMaintenance)) {
            ret.autoUpgradeDeprecatedDuringMaintenance = systemDefaultSettings.autoUpgradeDeprecatedDuringMaintenance;
        }
        if (TrackFlag.DEFAULT.equals(ret.autoUpgradeUnknownDuringMaintenance)) {
            ret.autoUpgradeUnknownDuringMaintenance = systemDefaultSettings.autoUpgradeUnknownDuringMaintenance;
        }
        return ret;
    }
    
    private TrackFlag autoUpgradeDeprecatedOnBind = TrackFlag.DEFAULT;
    private TrackFlag autoUpgradeUnknownOnBind = TrackFlag.DEFAULT;
    private TrackFlag autoUpgradeDeprecatedDuringMaintenance = TrackFlag.DEFAULT;
    private TrackFlag autoUpgradeUnknownDuringMaintenance = TrackFlag.DEFAULT;

    @Override
    public CustomerFirmwareTrackSettings clone() {
        CustomerFirmwareTrackSettings ret = (CustomerFirmwareTrackSettings) super.clone();
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
        if (!(obj instanceof CustomerFirmwareTrackSettings)) {
            return false;
        }
        CustomerFirmwareTrackSettings other = (CustomerFirmwareTrackSettings) obj;
        if (this.autoUpgradeDeprecatedDuringMaintenance != other.autoUpgradeDeprecatedDuringMaintenance) {
            return false;
        }
        if (this.autoUpgradeDeprecatedOnBind != other.autoUpgradeDeprecatedOnBind) {
            return false;
        }
        if (this.autoUpgradeUnknownDuringMaintenance != other.autoUpgradeUnknownDuringMaintenance) {
            return false;
        }
        if (this.autoUpgradeUnknownOnBind != other.autoUpgradeUnknownOnBind) {
            return false;
        }
        return true;
    }

    public TrackFlag getAutoUpgradeDeprecatedDuringMaintenance() {
        return autoUpgradeDeprecatedDuringMaintenance;
    }

    public TrackFlag getAutoUpgradeDeprecatedOnBind() {
        return autoUpgradeDeprecatedOnBind;
    }

    public TrackFlag getAutoUpgradeUnknownDuringMaintenance() {
        return autoUpgradeUnknownDuringMaintenance;
    }

    public TrackFlag getAutoUpgradeUnknownOnBind() {
        return autoUpgradeUnknownOnBind;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.autoUpgradeDeprecatedDuringMaintenance == null) ? 0
                : this.autoUpgradeDeprecatedDuringMaintenance.hashCode());
        result = prime * result
                + ((this.autoUpgradeDeprecatedOnBind == null) ? 0 : this.autoUpgradeDeprecatedOnBind.hashCode());
        result = prime * result + ((this.autoUpgradeUnknownDuringMaintenance == null) ? 0
                : this.autoUpgradeUnknownDuringMaintenance.hashCode());
        result = prime * result
                + ((this.autoUpgradeUnknownOnBind == null) ? 0 : this.autoUpgradeUnknownOnBind.hashCode());
        return result;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }

        if (TrackFlag.isUnsupported(autoUpgradeDeprecatedOnBind) || TrackFlag.isUnsupported(autoUpgradeUnknownOnBind)
                || TrackFlag.isUnsupported(autoUpgradeDeprecatedDuringMaintenance)
                || TrackFlag.isUnsupported(autoUpgradeUnknownDuringMaintenance)) {
            return true;
        }
        return false;
    }

    public void setAutoUpgradeDeprecatedDuringMaintenance(TrackFlag autoUpgradeDeprecatedDuringMaintenance) {
        this.autoUpgradeDeprecatedDuringMaintenance = autoUpgradeDeprecatedDuringMaintenance;
    }

    public void setAutoUpgradeDeprecatedOnBind(TrackFlag autoUpgradeDeprecatedOnBind) {
        this.autoUpgradeDeprecatedOnBind = autoUpgradeDeprecatedOnBind;
    }

    public void setAutoUpgradeUnknownDuringMaintenance(TrackFlag autoUpgradeUnknownDuringMaintenance) {
        this.autoUpgradeUnknownDuringMaintenance = autoUpgradeUnknownDuringMaintenance;
    }

    public void setAutoUpgradeUnknownOnBind(TrackFlag autoUpgradeUnknownOnBind) {
        this.autoUpgradeUnknownOnBind = autoUpgradeUnknownOnBind;
    }

}
