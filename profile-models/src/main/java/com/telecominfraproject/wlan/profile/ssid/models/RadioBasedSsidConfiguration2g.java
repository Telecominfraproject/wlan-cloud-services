package com.telecominfraproject.wlan.profile.ssid.models;

import java.util.Objects;

public class RadioBasedSsidConfiguration2g extends RadioBasedSsidConfiguration {

    /**
     * 
     */
    private static final long serialVersionUID = -8286945561452628427L;
    private Boolean enable80211b;

    RadioBasedSsidConfiguration2g() {
        super();
        enable80211b = Boolean.FALSE;
    }

    public static RadioBasedSsidConfiguration2g generateDefault() {
        return new RadioBasedSsidConfiguration2g();
    }

    public Boolean getEnable80211b() {
        return enable80211b;
    }

    public void setEnable80211b(Boolean enable80211b) {
        this.enable80211b = enable80211b;
    }

    @Override
    public boolean needsToBeUpdatedOnDevice(RadioBasedSsidConfiguration previousVersion) {
        if (previousVersion instanceof RadioBasedSsidConfiguration2g) {
            return !Objects.equals(this, (RadioBasedSsidConfiguration2g) previousVersion);
        }
        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(enable80211b);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        RadioBasedSsidConfiguration2g other = (RadioBasedSsidConfiguration2g) obj;
        return Objects.equals(enable80211b, other.enable80211b);
    }

}
