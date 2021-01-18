/**
 * 
 */
package com.telecominfraproject.wlan.client.models.events.realtime;

import java.util.Objects;

import com.telecominfraproject.wlan.client.models.events.utils.WlanReasonCode;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasClientMac;
import com.telecominfraproject.wlan.systemevent.equipment.realtime.RealTimeEvent;
import com.telecominfraproject.wlan.systemevent.equipment.realtime.RealTimeEventType;

public class ClientFailureEvent extends RealTimeEvent implements HasClientMac {
    private static final long serialVersionUID = -16021752050335131L;

    private long sessionId;
    private String ssid;
    private MacAddress clientMacAddress;
    private WlanReasonCode reasonCode;
    private String reasonString;

    public ClientFailureEvent(Long timestamp) {
        super(RealTimeEventType.STA_Client_Failure, timestamp);
    }

    public ClientFailureEvent() {
        // serialization
        
    }

    @Override
    public MacAddress getClientMacAddress() {
        return clientMacAddress;
    }

    public void setClientMacAddress(MacAddress clientMacAddress) {
        this.clientMacAddress = clientMacAddress;
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

    public WlanReasonCode getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(WlanReasonCode reasonCode) {
        this.reasonCode = reasonCode;
    }

    public String getReasonString() {
        return reasonString;
    }

    public void setReasonString(String reasonString) {
        this.reasonString = reasonString;
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
        if (hasUnsupportedValue(clientMacAddress)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(clientMacAddress, reasonCode, reasonString, sessionId, ssid);
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
        ClientFailureEvent other = (ClientFailureEvent) obj;
        return Objects.equals(clientMacAddress, other.clientMacAddress) && Objects.equals(reasonCode, other.reasonCode)
                && Objects.equals(reasonString, other.reasonString) && sessionId == other.sessionId
                && Objects.equals(ssid, other.ssid);
    }

}
