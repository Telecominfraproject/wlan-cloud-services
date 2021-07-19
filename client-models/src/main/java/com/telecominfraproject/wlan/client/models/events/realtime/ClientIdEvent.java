package com.telecominfraproject.wlan.client.models.events.realtime;

import java.util.Arrays;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasClientMac;
import com.telecominfraproject.wlan.systemevent.equipment.realtime.RealTimeEvent;
import com.telecominfraproject.wlan.systemevent.equipment.realtime.RealTimeEventType;

public class ClientIdEvent extends RealTimeEvent implements HasClientMac {

    private static final long serialVersionUID = 298223061973506469L;
    private String sessionId;
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
    public String getSessionId() {
        return sessionId;
    }

    /**
     * @param sessionId
     *            the sessionId to set
     */
    public void setSessionId(String sessionId) {
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



    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Arrays.hashCode(macAddressBytes);
        result = prime * result + Objects.hash(clientMacAddress, sessionId, userId);
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
        ClientIdEvent other = (ClientIdEvent) obj;
        return Objects.equals(clientMacAddress, other.clientMacAddress) && Arrays.equals(macAddressBytes, other.macAddressBytes)
                && Objects.equals(sessionId, other.sessionId) && Objects.equals(userId, other.userId);
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
