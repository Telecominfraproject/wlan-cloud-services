package com.telecominfraproject.wlan.client.models.events.realtime;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.systemevent.equipment.realtime.RealTimeEvent;
import com.telecominfraproject.wlan.systemevent.equipment.realtime.RealTimeEventType;

public class ClientAuthEvent extends RealTimeEvent {

    private static final long serialVersionUID = 1221389696911864515L;

    private long sessionId;
    private String ssid;
    private MacAddress deviceMacAddress;
    private int authStatus;
    private RadioType radioType;

    protected ClientAuthEvent() {
        // serialization
        this(0L);
    }

    public ClientAuthEvent(Long timestamp) {
        super(RealTimeEventType.STA_Client_Auth, timestamp);
    }

    public ClientAuthEvent(RealTimeEventType eventType, Long timestamp) {
        super(eventType, timestamp);
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public MacAddress getDeviceMacAddress() {
        return deviceMacAddress;
    }

    public void setDeviceMacAddress(MacAddress deviceMacAddress) {
        this.deviceMacAddress = deviceMacAddress;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public int getAuthStatus() {
        return authStatus;
    }

    public void setAuthStatus(int authStatus) {
        this.authStatus = authStatus;
    }

    public RadioType getRadioType() {
        return radioType;
    }

    public void setRadioType(RadioType radioType) {
        this.radioType = radioType;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(authStatus, deviceMacAddress, radioType, sessionId, ssid);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof ClientAuthEvent)) {
            return false;
        }
        ClientAuthEvent other = (ClientAuthEvent) obj;
        return this.authStatus == other.authStatus && Objects.equals(deviceMacAddress, other.deviceMacAddress)
                && this.radioType == other.radioType && this.sessionId == other.sessionId
                && Objects.equals(ssid, other.ssid);
    }

    @Override
    public ClientAuthEvent clone() {
        return (ClientAuthEvent) super.clone();
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

}
