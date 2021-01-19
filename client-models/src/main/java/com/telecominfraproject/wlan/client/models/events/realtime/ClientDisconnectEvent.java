package com.telecominfraproject.wlan.client.models.events.realtime;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.client.models.events.utils.WlanReasonCode;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasClientMac;
import com.telecominfraproject.wlan.systemevent.equipment.realtime.RealTimeEvent;
import com.telecominfraproject.wlan.systemevent.equipment.realtime.RealTimeEventType;

public class ClientDisconnectEvent extends RealTimeEvent implements HasClientMac {

    public static enum DisconnectFrameType {
        Deauth, Disassoc,

        UNSUPPORTED;

        @JsonCreator
        public static DisconnectFrameType getByName(String value) {
            return JsonDeserializationUtils.deserializEnum(value, DisconnectFrameType.class, UNSUPPORTED);
        }

        public static boolean isUnsupported(DisconnectFrameType value) {
            return UNSUPPORTED.equals(value);
        }
    }

    public static enum DisconnectInitiator {
        AccessPoint, Client,

        UNSUPPORTED;

        @JsonCreator
        public static DisconnectInitiator getByName(String value) {
            return JsonDeserializationUtils.deserializEnum(value, DisconnectInitiator.class, UNSUPPORTED);
        }

        public static boolean isUnsupported(DisconnectInitiator value) {
            return UNSUPPORTED.equals(value);
        }
    }

    private static final long serialVersionUID = -7674230178565760938L;
    private long sessionId;
    private byte[] macAddressBytes;
    private MacAddress clientMacAddress;
    private long lastRecvTime;
    private long lastSentTime;
    private DisconnectFrameType frameType;
    private DisconnectInitiator initiator;
    private WlanReasonCode reasonCode;
    private int internalReasonCode;
    private int rssi;
    private String ssid;
    private RadioType radioType;

    public ClientDisconnectEvent() {
        // serialization
        
    }

    public ClientDisconnectEvent(Long timestamp) {
        super(RealTimeEventType.STA_Client_Disconnect, timestamp);
    }

    public ClientDisconnectEvent(RealTimeEventType eventType, Long timestamp) {
        super(eventType, timestamp);
    }

    @Override
    public MacAddress getClientMacAddress() {
        return clientMacAddress;
    }

    public DisconnectFrameType getFrameType() {
        return frameType;
    }

    public DisconnectInitiator getInitiator() {
        return initiator;
    }

    public Integer getInternalReasonCode() {
        return internalReasonCode;
    }

    public long getLastRecvTime() {
        return lastRecvTime;
    }

    public long getLastSentTime() {
        return lastSentTime;
    }

    public WlanReasonCode getReasonCode() {
        return reasonCode;
    }

    public int getRssi() {
        return rssi;
    }

    public long getSessionId() {
        return sessionId;
    }

    public RadioType getRadioType() {
        return radioType;
    }

    public String getSsid() {
        return ssid;
    }

    public void setClientMacAddress(MacAddress clientMacAddress) {
        this.clientMacAddress = clientMacAddress;
    }

    public void setFrameType(DisconnectFrameType frameType) {
        this.frameType = frameType;
    }

    public void setInitiator(DisconnectInitiator initiator) {
        this.initiator = initiator;
    }

    public void setInternalReasonCode(Integer internalReasonCode) {
        this.internalReasonCode = internalReasonCode;
    }

    public void setLastRecvTime(long lastRecvTime) {
        this.lastRecvTime = lastRecvTime;
    }

    public void setLastSentTime(long lastSentTime) {
        this.lastSentTime = lastSentTime;
    }

    public void setReasonCode(WlanReasonCode reasonCode) {
        this.reasonCode = reasonCode;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public void setRadioType(RadioType radioType) {
        this.radioType = radioType;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }

        if (DisconnectFrameType.isUnsupported(frameType) || DisconnectInitiator.isUnsupported(initiator)
                || hasUnsupportedValue(clientMacAddress) || RadioType.isUnsupported(radioType)) {
            return true;
        }
        return false;
    }

    @Override
    public ClientDisconnectEvent clone() {
        return (ClientDisconnectEvent) super.clone();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Arrays.hashCode(macAddressBytes);
        result = prime * result + Objects.hash(clientMacAddress, frameType, initiator, internalReasonCode, lastRecvTime,
                lastSentTime, radioType, reasonCode, rssi, sessionId, ssid);
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
        ClientDisconnectEvent other = (ClientDisconnectEvent) obj;
        return Objects.equals(clientMacAddress, other.clientMacAddress) && frameType == other.frameType
                && initiator == other.initiator && internalReasonCode == other.internalReasonCode
                && lastRecvTime == other.lastRecvTime && lastSentTime == other.lastSentTime
                && Arrays.equals(macAddressBytes, other.macAddressBytes) && radioType == other.radioType
                && Objects.equals(reasonCode, other.reasonCode) && rssi == other.rssi && sessionId == other.sessionId
                && Objects.equals(ssid, other.ssid);
    }


}
