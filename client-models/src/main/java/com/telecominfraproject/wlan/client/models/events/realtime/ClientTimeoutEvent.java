package com.telecominfraproject.wlan.client.models.events.realtime;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasClientMac;
import com.telecominfraproject.wlan.systemevent.equipment.realtime.RealTimeEvent;
import com.telecominfraproject.wlan.systemevent.equipment.realtime.RealTimeEventType;

public class ClientTimeoutEvent extends RealTimeEvent implements HasClientMac {

    private static final long serialVersionUID = -3555658720107793679L;

    public static enum ClientTimeoutReason {
        IdleTooLong, FailedProbe, UNSUPPORTED;

        @JsonCreator
        public static ClientTimeoutReason getByName(String value) {
            return JsonDeserializationUtils.deserializEnum(value, ClientTimeoutReason.class, UNSUPPORTED);
        }

        public static boolean isUnsupported(ClientTimeoutReason value) {
            return UNSUPPORTED.equals(value);
        }
    }

    private String sessionId;
    private MacAddress clientMacAddress;
    private long lastRecvTime;
    private long lastSentTime;
    private ClientTimeoutReason timeoutReason;

    public ClientTimeoutEvent() {
        // serialization
        
    }

    public ClientTimeoutEvent(Long timestamp) {
        super(RealTimeEventType.STA_Client_Timeout, timestamp);
    }

    public ClientTimeoutEvent(RealTimeEventType eventType, Long timestamp) {
        super(eventType, timestamp);
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @Override
    public MacAddress getClientMacAddress() {
        return clientMacAddress;
    }

    public void setClientMacAddress(MacAddress clientMacAddress) {
        this.clientMacAddress = clientMacAddress;
    }

    public long getLastRecvTime() {
        return lastRecvTime;
    }

    public void setLastRecvTime(long lastRecvTime) {
        this.lastRecvTime = lastRecvTime;
    }

    public long getLastSentTime() {
        return lastSentTime;
    }

    public void setLastSentTime(long lastSentTime) {
        this.lastSentTime = lastSentTime;
    }

    public ClientTimeoutReason getTimeoutReason() {
        return timeoutReason;
    }

    public void setTimeoutReason(ClientTimeoutReason timeoutReason) {
        this.timeoutReason = timeoutReason;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(clientMacAddress, lastRecvTime, lastSentTime, sessionId, timeoutReason);
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
        if (!(obj instanceof ClientTimeoutEvent)) {
            return false;
        }
        ClientTimeoutEvent other = (ClientTimeoutEvent) obj;
        return Objects.equals(clientMacAddress, other.clientMacAddress) && this.lastRecvTime == other.lastRecvTime
                && this.lastSentTime == other.lastSentTime && this.sessionId == other.sessionId
                && this.timeoutReason == other.timeoutReason;
    }

    @Override
    public ClientTimeoutEvent clone() {
        return (ClientTimeoutEvent) super.clone();
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }

        if (ClientTimeoutReason.isUnsupported(timeoutReason) || hasUnsupportedValue(clientMacAddress)) {
            return true;
        }
        return false;
    }

}
