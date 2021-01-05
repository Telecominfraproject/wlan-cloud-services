package com.telecominfraproject.wlan.client.models.events;

import java.util.Arrays;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.systemevent.equipment.realtime.RealTimeEvent;
import com.telecominfraproject.wlan.systemevent.equipment.realtime.RealTimeEventType;

public class ClientIpAddressEvent extends RealTimeEvent {

    private static final long serialVersionUID = -5332534925768685589L;

    private long sessionId;
    private MacAddress deviceMacAddress;
    private byte[] ipAddr;
    
    protected ClientIpAddressEvent() {
        // serialization
        this(0L);
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


    /**
     * Use {@link #setDeviceMacAddress(deviceMacAddress)} instead. 
     * @param address 
     */    
    @Deprecated
    public void setMacAddressBytes(byte[] address) {
        this.deviceMacAddress = address == null?null:new MacAddress(address);
    }

    public MacAddress getDeviceMacAddress() {
        return deviceMacAddress;
    }

    public void setDeviceMacAddress(MacAddress deviceMacAddress) {
        this.deviceMacAddress = deviceMacAddress;
    }

    public byte[] getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(byte[] ipAddr) {
        this.ipAddr = ipAddr;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Arrays.hashCode(this.ipAddr);
        result = prime * result + Objects.hash(deviceMacAddress, sessionId);
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
        return Objects.equals(deviceMacAddress, other.deviceMacAddress) && Arrays.equals(ipAddr, other.ipAddr)
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
        
        if (hasUnsupportedValue(deviceMacAddress)) {
            return true;
        }
        return false;
    }
    
}
