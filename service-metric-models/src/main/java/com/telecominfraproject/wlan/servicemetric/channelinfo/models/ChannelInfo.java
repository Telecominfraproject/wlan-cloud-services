package com.telecominfraproject.wlan.servicemetric.channelinfo.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.telecominfraproject.wlan.core.model.equipment.ChannelBandwidth;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

@JsonInclude(JsonInclude.Include.NON_NULL) 
public class ChannelInfo extends BaseJsonModel 
{
    private static final long serialVersionUID = 3149015856936675456L;
    private Integer chanNumber;
    private ChannelBandwidth bandwidth;
    private Integer totalUtilization;
    private Integer wifiUtilization;
    private Integer noiseFloor;

    public ChannelInfo()
    {
        // serialization
    }

    public Integer getChanNumber() {
        return chanNumber;
    }

    public void setChanNumber(Integer chanNumber) {
        this.chanNumber = chanNumber;
    }

    public ChannelBandwidth getBandwidth() {
        return bandwidth;
    }

    public void setBandwidth(ChannelBandwidth bandwidth) {
        this.bandwidth = bandwidth;
    }

    public Integer getTotalUtilization() {
        return totalUtilization;
    }

    public void setTotalUtilization(Integer totalUtilization) {
        this.totalUtilization = totalUtilization;
    }

    public Integer getWifiUtilization() {
        return wifiUtilization;
    }

    public void setWifiUtilization(Integer wifiUtilization) {
        this.wifiUtilization = wifiUtilization;
    }

    public Integer getNoiseFloor() {
        return noiseFloor;
    }

    public void setNoiseFloor(Integer noiseFloor) {
        this.noiseFloor = noiseFloor;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (ChannelBandwidth.isUnsupported(bandwidth)) {
            return true;
        }
        return false;
    }
    
    @Override
    public ChannelInfo clone() {
        return (ChannelInfo) super.clone();
    }
}
