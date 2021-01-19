package com.telecominfraproject.wlan.client.models.events.realtime;

import java.util.Arrays;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasClientMac;
import com.telecominfraproject.wlan.systemevent.equipment.realtime.RealTimeEvent;
import com.telecominfraproject.wlan.systemevent.equipment.realtime.RealTimeEventType;

public class ClientIdEvent extends RealTimeEvent implements HasClientMac {

    private static final long serialVersionUID = 298223061973506469L;
    private long sessionId;
    private byte[] macAddressBytes;
    private MacAddress clientMacAddress;
    private String userId;

    public ClientIdEvent() {
        // serialization
        
    }

    public ClientIdEvent(Long timestamp) {
        super(RealTimeEventType.STA_Client_Id, timestamp);
    }

    /**
     * @return the sessionId
     */
    public long getSessionId() {
        return sessionId;
    }

    /**
     * @param sessionId
     *            the sessionId to set
     */
    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public MacAddress getClientMacAddress() {
        return clientMacAddress;
    }

    public void setClientMacAddress(MacAddress clientMacAddress) {
        this.clientMacAddress = clientMacAddress;
        // For backward compatibility
        this.macAddressBytes = clientMacAddress == null ? null : clientMacAddress.getAddress();
    }

    /**
     * @return the userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId
     *            the userId to set
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(macAddressBytes);
        result = prime * result + (int) (sessionId ^ (sessionId >>> 32));
        result = prime * result + ((userId == null) ? 0 : userId.hashCode());
        result = prime * result + ((clientMacAddress == null) ? 0 : clientMacAddress.hashCode());
        return result;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof ClientIdEvent)) {
            return false;
        }
        ClientIdEvent other = (ClientIdEvent) obj;
        if (!Arrays.equals(macAddressBytes, other.macAddressBytes))
            return false;
        if (sessionId != other.sessionId)
            return false;
        if (userId == null) {
            if (other.userId != null)
                return false;
        } else if (!userId.equals(other.userId))
            return false;
        if (clientMacAddress == null) {
            if (other.clientMacAddress != null)
                return false;
        } else if (!clientMacAddress.equals(other.clientMacAddress))
            return false;

        return true;
    }

    @Override
    public ClientIdEvent clone() {
        return (ClientIdEvent) super.clone();
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        return false;
    }

}
