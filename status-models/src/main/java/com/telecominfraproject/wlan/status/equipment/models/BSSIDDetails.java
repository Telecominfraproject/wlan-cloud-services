package com.telecominfraproject.wlan.status.equipment.models;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

public class BSSIDDetails extends BaseJsonModel
{
    private static final long serialVersionUID = -5127231626115415444L;
    private MacAddress bssid;
    private String ssid;
    
    public BSSIDDetails(MacAddress bssid, String ssid)
    {
        this.bssid = bssid;
        this.ssid = ssid;
    }
    
    @SuppressWarnings("unused")
	private BSSIDDetails()
    {
        // serialization
    }
    
    public MacAddress getBssid() {
        return bssid;
    }
    
    public void setBssid(MacAddress bssid) {
        this.bssid = bssid;
    }
    
    public String getSsid() {
        return ssid;
    }
    
    public void setSsid(String ssid) {
        this.ssid = ssid;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bssid == null) ? 0 : bssid.hashCode());
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
        BSSIDDetails other = (BSSIDDetails) obj;
        if (bssid == null) {
            if (other.bssid != null)
                return false;
        } else if (!bssid.equals(other.bssid))
            return false;
        if (ssid == null) {
            if (other.ssid != null)
                return false;
        } else if (!ssid.equals(other.ssid))
            return false;
        return true;
    }
    
    @Override
    public BSSIDDetails clone() {
    	BSSIDDetails ret = (BSSIDDetails) super.clone();
    	
    	if(this.bssid!=null) {
    		ret.bssid = this.bssid.clone();
    	}
    	
    	return ret;
    }

}
