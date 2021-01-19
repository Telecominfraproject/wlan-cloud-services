package com.telecominfraproject.wlan.client.models.events.realtime;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasClientMac;
import com.telecominfraproject.wlan.systemevent.equipment.realtime.RealTimeEvent;
import com.telecominfraproject.wlan.systemevent.equipment.realtime.RealTimeEventType;

public class ClientFirstDataEvent extends RealTimeEvent implements HasClientMac {

    private static final long serialVersionUID = 298223061973506469L;
    private long sessionId;
    private MacAddress clientMacAddress;
    private long firstDataRcvdTs;
    private long firstDataSentTs;

    public ClientFirstDataEvent() {
        // serialization
        
    }

    public ClientFirstDataEvent(Long timestamp) {
        super(RealTimeEventType.STA_Client_FirstData, timestamp);
    }

    public ClientFirstDataEvent(RealTimeEventType eventType, Long timestamp) {
        super(eventType, timestamp);
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public MacAddress getClientMacAddress() {
        return clientMacAddress;
    }

    public void setClientMacAddress(MacAddress clientMacAddress) {
        this.clientMacAddress = clientMacAddress;
    }

    public long getFirstDataRcvdTs() {
        return firstDataRcvdTs;
    }

    public void setFirstDataRcvdTs(long firstDataRcvdTs) {
        this.firstDataRcvdTs = firstDataRcvdTs;
    }

    public long getFirstDataSentTs() {
        return firstDataSentTs;
    }

    public void setFirstDataSentTs(long firstDataSentTs) {
        this.firstDataSentTs = firstDataSentTs;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(clientMacAddress, firstDataRcvdTs, firstDataSentTs, sessionId);
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
        if (!(obj instanceof ClientFirstDataEvent)) {
            return false;
        }
        ClientFirstDataEvent other = (ClientFirstDataEvent) obj;
        return Objects.equals(clientMacAddress, other.clientMacAddress) && this.firstDataRcvdTs == other.firstDataRcvdTs
                && this.firstDataSentTs == other.firstDataSentTs && this.sessionId == other.sessionId;
    }

    @Override
    public ClientFirstDataEvent clone() {
        return (ClientFirstDataEvent) super.clone();
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
