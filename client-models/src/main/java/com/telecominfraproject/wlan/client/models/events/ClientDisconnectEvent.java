package com.telecominfraproject.wlan.client.models.events;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;
import com.telecominfraproject.wlan.systemevent.equipment.realtime.RealTimeEvent;
import com.telecominfraproject.wlan.systemevent.equipment.realtime.RealTimeEventType;

public class ClientDisconnectEvent extends RealTimeEvent {

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
    private MacAddress deviceMacAddress;
    private long lastRecvTime;
    private long lastSentTime;
    private DisconnectFrameType frameType;
    private DisconnectInitiator initiator;
    private int reasonCode;
    private Integer internalReasonCode;
    private Integer rssi;
    private String ssid;
    private RadioType radioType;


    protected ClientDisconnectEvent() {
        // serialization
        this(0L);
    }

    public ClientDisconnectEvent(Long timestamp) {
        super(RealTimeEventType.STA_Client_Disconnect, timestamp);
    }

    public ClientDisconnectEvent(RealTimeEventType eventType, Long timestamp) {
        super(eventType, timestamp);
    }

    @Override
    public ClientDisconnectEvent clone() {
        return (ClientDisconnectEvent) super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof ClientDisconnectEvent)) {
            return false;
        }
        ClientDisconnectEvent other = (ClientDisconnectEvent) obj;
        return Objects.equals(deviceMacAddress, other.deviceMacAddress) && this.frameType == other.frameType
                && this.initiator == other.initiator && Objects.equals(internalReasonCode, other.internalReasonCode)
                && this.lastRecvTime == other.lastRecvTime && this.lastSentTime == other.lastSentTime
                && Arrays.equals(macAddressBytes, other.macAddressBytes) && this.reasonCode == other.reasonCode
                && Objects.equals(rssi, other.rssi) && this.sessionId == other.sessionId
                && Objects.equals(ssid, other.ssid) && Objects.equals(radioType, other.radioType);
    }

    public MacAddress getDeviceMacAddress() {
        return deviceMacAddress;
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

    public int getReasonCode() {
        return reasonCode;
    }

    public Integer getRssi() {
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
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Arrays.hashCode(this.macAddressBytes);
        result = prime * result + Objects.hash(deviceMacAddress, frameType, initiator, internalReasonCode, lastRecvTime,
                lastSentTime, reasonCode, rssi, sessionId, ssid, radioType);
        return result;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }

        if (DisconnectFrameType.isUnsupported(frameType) || DisconnectInitiator.isUnsupported(initiator)
                || hasUnsupportedValue(deviceMacAddress) || RadioType.isUnsupported(radioType)){
            return true;
        }
        return false;
    }

    public void setDeviceMacAddress(MacAddress deviceMacAddress) {
        this.deviceMacAddress = deviceMacAddress;
        // For backward compatibility
        this.macAddressBytes = deviceMacAddress == null ? null : deviceMacAddress.getAddress();
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

    /**
     * Use {@link #setDeviceMacAddress(deviceMacAddress)} instead.
     * 
     * @param address
     */
    @Deprecated
    public void setMacAddressBytes(byte[] address) {
        this.deviceMacAddress = address == null ? null : new MacAddress(address);
    }

    public void setReasonCode(int reasonCode) {
        this.reasonCode = reasonCode;
    }

    public void setRssi(Integer rssi) {
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
}
