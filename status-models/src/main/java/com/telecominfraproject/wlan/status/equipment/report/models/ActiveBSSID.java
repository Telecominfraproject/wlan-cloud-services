package com.telecominfraproject.wlan.status.equipment.report.models;

import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

public class ActiveBSSID extends BaseJsonModel 
{
    private static final long serialVersionUID = -1376242932063931393L;
    private String bssid;
    private String ssid;
    private RadioType radioType;
    private int numDevicesConnected;

    public ActiveBSSID()
    {
        // serial
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public RadioType getRadioType() {
        return radioType;
    }

    public void setRadioType(RadioType radioType) {
        this.radioType = radioType;
    }

    public int getNumDevicesConnected() {
        return numDevicesConnected;
    }

    public void setNumDevicesConnected(int numDevicesConnected) {
        this.numDevicesConnected = numDevicesConnected;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bssid == null) ? 0 : bssid.hashCode());
        result = prime * result + numDevicesConnected;
        result = prime * result + ((radioType == null) ? 0 : radioType.hashCode());
        result = prime * result + ((ssid == null) ? 0 : ssid.hashCode());
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
        ActiveBSSID other = (ActiveBSSID) obj;
        if (bssid == null) {
            if (other.bssid != null)
                return false;
        } else if (!bssid.equals(other.bssid))
            return false;
        if (numDevicesConnected != other.numDevicesConnected)
            return false;
        if (radioType != other.radioType)
            return false;
        if (ssid == null) {
            if (other.ssid != null)
                return false;
        } else if (!ssid.equals(other.ssid))
            return false;
        return true;
    }

    @Override
    public boolean hasUnsupportedValue()
    {
        return RadioType.isUnsupported(radioType);
    }

    @Override
    public ActiveBSSID clone() {
    	ActiveBSSID ret  = (ActiveBSSID) super.clone();
    	return ret;
    }
}
