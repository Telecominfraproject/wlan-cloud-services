package com.telecominfraproject.wlan.client.models.events.realtime;

import java.net.InetAddress;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasClientMac;
import com.telecominfraproject.wlan.systemevent.equipment.realtime.RealTimeEvent;
import com.telecominfraproject.wlan.systemevent.equipment.realtime.RealTimeEventType;

public class ClientIpAddressEvent extends RealTimeEvent implements HasClientMac {

    private static final long serialVersionUID = -5332534925768685589L;

    private long sessionId;
    private MacAddress clientMacAddress;
    private InetAddress ipAddr;

    public ClientIpAddressEvent() {
        // serialization
        
    }

    public ClientIpAddressEvent(Long timestamp) {
        super(RealTimeEventType.STA_Client_IP, timestamp);
    }

    public ClientIpAddressEvent(RealTimeEventType eventType, Long timestamp) {
        super(eventType, timestamp);
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public MacAddress getClientMacAddress() {
        return clientMacAddress;
    }

    public void setClientMacAddress(MacAddress clientMacAddress) {
        this.clientMacAddress = clientMacAddress;
    }

    public InetAddress getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(InetAddress ipAddr) {
        this.ipAddr = ipAddr;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(ipAddr, clientMacAddress, sessionId);
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
        if (!(obj instanceof ClientIpAddressEvent)) {
            return false;
        }
        ClientIpAddressEvent other = (ClientIpAddressEvent) obj;
        return Objects.equals(clientMacAddress, other.clientMacAddress) && Objects.equals(ipAddr, other.ipAddr)
                && this.sessionId == other.sessionId;
    }

    @Override
    public ClientIpAddressEvent clone() {
        return (ClientIpAddressEvent) super.clone();
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

}
