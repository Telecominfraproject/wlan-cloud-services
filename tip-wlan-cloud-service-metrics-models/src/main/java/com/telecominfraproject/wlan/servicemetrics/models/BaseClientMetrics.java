package com.telecominfraproject.wlan.servicemetrics.models;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author yongli
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public abstract class BaseClientMetrics extends BaseJsonModel {
    private static final long serialVersionUID = 7010806309246953581L;
    private byte[] macAddress;
    private MacAddress deviceMacAddress;
    private Integer secondsSinceLastRecv;

    @Override
    public BaseClientMetrics clone() {
        BaseClientMetrics ret = (BaseClientMetrics) super.clone();

        if (this.macAddress != null) {
            ret.macAddress = this.macAddress.clone();
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
        if (!(obj instanceof BaseClientMetrics)) {
            return false;
        }
        BaseClientMetrics other = (BaseClientMetrics) obj;
        return Objects.equals(deviceMacAddress, other.deviceMacAddress) && Arrays.equals(macAddress, other.macAddress)
                && Objects.equals(secondsSinceLastRecv, other.secondsSinceLastRecv);
    }

    public MacAddress getDeviceMacAddress() {
        return deviceMacAddress;
    }

    /**
     * @deprecated use {@link #getDeviceMacAddress} instead.
     */
    public byte[] getMacAddress() {
        return macAddress;
    }

    public Integer getSecondsSinceLastRecv() {
        return secondsSinceLastRecv;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(this.macAddress);
        result = prime * result + Objects.hash(deviceMacAddress, secondsSinceLastRecv);
        return result;
    }

    public void setDeviceMacAddress(MacAddress deviceMacAddress) {
        this.deviceMacAddress = deviceMacAddress;
        // For backward compatibility
        this.macAddress = deviceMacAddress == null ? null : deviceMacAddress.getAddress();
    }

    /**
     * @deprecated use {@link #setDeviceMacAddress(deviceMacAddress)} instead.
     */
    public void setMacAddress(byte[] macAddress) {
        this.macAddress = macAddress;
        this.deviceMacAddress = macAddress == null ? null : new MacAddress(macAddress);
    }

    public void setSecondsSinceLastRecv(Integer secondsSinceLastRecv) {
        this.secondsSinceLastRecv = secondsSinceLastRecv;
    }
}
