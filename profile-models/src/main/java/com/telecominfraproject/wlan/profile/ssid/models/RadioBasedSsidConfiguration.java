package com.telecominfraproject.wlan.profile.ssid.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

public class RadioBasedSsidConfiguration extends BaseJsonModel
        implements PushableConfiguration<RadioBasedSsidConfiguration> {
    private static final long serialVersionUID = -4512565916653485178L;
    /**
     * enable 801.11r
     */
    private Boolean enable80211r;
    /**
     * enable 802.11k
     */
    private Boolean enable80211k;
    /**
     * enable 802.11v
     */
    private Boolean enable80211v;

    RadioBasedSsidConfiguration() {
        // all null
    }

    public Boolean getEnable80211r() {
        return enable80211r;
    }

    public void setEnable80211r(Boolean enable80211r) {
        this.enable80211r = enable80211r;
    }

    public Boolean getEnable80211k() {
        return enable80211k;
    }

    public void setEnable80211k(Boolean enable80211k) {
        this.enable80211k = enable80211k;
    }

    public Boolean getEnable80211v() {
        return enable80211v;
    }

    public void setEnable80211v(Boolean enable80211v) {
        this.enable80211v = enable80211v;
    }

    @Override
    public boolean needsToBeUpdatedOnDevice(RadioBasedSsidConfiguration previousVersion) {
        return !Objects.equals(this, previousVersion);
    }

    public static RadioBasedSsidConfiguration generateDefault(RadioType radioType) {
        // We don't care about the radio type yet.
        return new RadioBasedSsidConfiguration();
    }

    @Override
    public int hashCode() {
        return Objects.hash(enable80211k, enable80211r, enable80211v);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RadioBasedSsidConfiguration other = (RadioBasedSsidConfiguration) obj;
        return Objects.equals(enable80211k, other.enable80211k) && Objects.equals(enable80211r, other.enable80211r)
                && Objects.equals(enable80211v, other.enable80211v);
    }
    
    

}
