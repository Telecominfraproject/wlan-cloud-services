package com.telecominfraproject.wlan.client.models.events;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.systemevent.equipment.realtime.RealTimeEvent;
import com.telecominfraproject.wlan.systemevent.equipment.realtime.RealTimeEventType;

public class ClientAssocEvent extends RealTimeEvent {
    private static final long serialVersionUID = 7015822981315570338L;

    private long sessionId;
    private String ssid;
    private MacAddress deviceMacAddress;
    private RadioType radioType;
    private boolean isReassociation;
    private Integer status;
    private Integer rssi;
    private Integer internalSC;
    private Boolean using11k;  
    private Boolean using11r; 
    private Boolean using11v;

    protected ClientAssocEvent() {
        // serialization
        this(0L);
    }

    public ClientAssocEvent(int customerId, long equipmentId, long timestamp, long sessionId, String ssid,
            MacAddress deviceMacAddress, RadioType radioType, boolean isReassociation, 
            Integer status, Integer internalSC, Integer rssi) {
        super(RealTimeEventType.STA_Client_Assoc, customerId, equipmentId, timestamp);
        this.sessionId = sessionId;
        this.ssid = ssid;
        setDeviceMacAddress(deviceMacAddress);
        this.radioType = radioType;
        this.isReassociation = isReassociation;
        this.status = status;
        this.internalSC = internalSC;
        this.rssi = rssi;
    }

    public ClientAssocEvent(Long timestamp) {
        super(RealTimeEventType.STA_Client_Assoc, timestamp);
    }

    @Override
    public ClientAssocEvent clone() {
        return (ClientAssocEvent) super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof ClientAssocEvent)) {
            return false;
        }
        ClientAssocEvent other = (ClientAssocEvent) obj;
        return Objects.equals(deviceMacAddress, other.deviceMacAddress) && Objects.equals(internalSC, other.internalSC)
                && this.isReassociation == other.isReassociation && this.radioType == other.radioType
                && Objects.equals(rssi, other.rssi) && this.sessionId == other.sessionId
                && Objects.equals(ssid, other.ssid) && Objects.equals(status, other.status)
                && Objects.equals(using11k, other.using11k) && Objects.equals(using11r, other.using11r)
                && Objects.equals(using11v, other.using11v);
    }

    public MacAddress getDeviceMacAddress() {
        return deviceMacAddress;
    }

    public RadioType getRadioType() {
        return radioType;
    }

    public Integer getRssi() {
        return rssi;
    }

    public long getSessionId() {
        return sessionId;
    }

    public String getSsid() {
        return ssid;
    }

    public Integer getStatus() {
        return status;
    }

    public Boolean getUsing11k() {
        return using11k;
    }

    public void setUsing11k(Boolean using11k) {
        this.using11k = using11k;
    }

    public Boolean getUsing11r() {
        return using11r;
    }

    public void setUsing11r(Boolean using11r) {
        this.using11r = using11r;
    }

    public Boolean getUsing11v() {
        return using11v;
    }

    public void setUsing11v(Boolean using11v) {
        this.using11v = using11v;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(deviceMacAddress, internalSC, isReassociation, radioType, rssi,
                sessionId, ssid, status, using11k, using11r, using11v);
        return result;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (RadioType.isUnsupported(radioType) || hasUnsupportedValue(deviceMacAddress)) {
            return true;
        }
        return false;
    }

    public boolean isReassociation() {
        return isReassociation;
    }

    public void setDeviceMacAddress(MacAddress deviceMacAddress) {
        this.deviceMacAddress = deviceMacAddress;
    }

    /**
     * Use {@link #setDeviceMacAddress(deviceMacAddress)} instead.
     * 
     * @param address
     */
    @Deprecated
    public void setMacAddressBytes(byte[] address) {
        this.deviceMacAddress = address == null ? null : new MacAddress(address);
    }

    public void setRadioType(RadioType radioType) {
        this.radioType = radioType;
    }

    public void setReassociation(boolean isReassociation) {
        this.isReassociation = isReassociation;
    }

    public void setRssi(Integer rssi) {
        this.rssi = rssi;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getInternalSC() {
        return internalSC;
    }

    public void setInternalSC(Integer internalSC) {
        this.internalSC = internalSC;
    }

}
