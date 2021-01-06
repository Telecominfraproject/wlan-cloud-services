/**
 * 
 */
package com.telecominfraproject.wlan.client.models.events.realtime;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.systemevent.equipment.realtime.RealTimeEvent;
import com.telecominfraproject.wlan.systemevent.equipment.realtime.RealTimeEventType;

public class ClientFailureEvent extends RealTimeEvent {
    private static final long serialVersionUID = -16021752050335131L;

    private long sessionId;
    private String ssid;
    private MacAddress deviceMacAddress;
    private int reasonCode;
    private String reasonString;

    public ClientFailureEvent(Long timestamp) {
        super(RealTimeEventType.STA_Client_Failure, timestamp);
    }

    protected ClientFailureEvent() {
        // serialization
        this(0L);
    }

    public MacAddress getDeviceMacAddress() {
        return deviceMacAddress;
    }

    public void setDeviceMacAddress(MacAddress deviceMacAddress) {
        this.deviceMacAddress = deviceMacAddress;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public int getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(int reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getReasonString() {
        return reasonString;
    }

    public void setReasonString(String reasonString) {
        this.reasonString = reasonString;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(deviceMacAddress, reasonCode, reasonString, sessionId, ssid);
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
        if (!(obj instanceof ClientFailureEvent)) {
            return false;
        }
        ClientFailureEvent other = (ClientFailureEvent) obj;
        return Objects.equals(deviceMacAddress, other.deviceMacAddress) && Objects.equals(reasonCode, other.reasonCode)
                && Objects.equals(reasonString, other.reasonString) && this.sessionId == other.sessionId
                && Objects.equals(ssid, other.ssid);
    }

    @Override
    public ClientFailureEvent clone() {
        return (ClientFailureEvent) super.clone();
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (hasUnsupportedValue(deviceMacAddress)) {
            return true;
        }
        return false;
    }

}
