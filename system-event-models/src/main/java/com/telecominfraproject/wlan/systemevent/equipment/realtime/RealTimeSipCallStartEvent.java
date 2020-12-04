package com.telecominfraproject.wlan.systemevent.equipment.realtime;

import java.util.List;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasClientMac;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasCustomerId;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasEquipmentId;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasProducedTimestamp;

public class RealTimeSipCallStartEvent extends RealTimeEvent
        implements HasCustomerId, HasEquipmentId, HasClientMac, HasProducedTimestamp {

    /**
     * 
     */
    private static final long serialVersionUID = -7289926906539107435L;
    private Long sipCallId;
    private Long associationId;
    private MacAddress macAddress;
    private List<String> codecs;
    private String providerDomain;
    private String deviceInfo;
    private int channel;
    private RadioType radioType;

    public RealTimeSipCallStartEvent(int customerId, long equipmentId, Long timestamp) {
        super(RealTimeEventType.SipCallStart, customerId, equipmentId, timestamp);
    }

    public RealTimeSipCallStartEvent(long timestamp) {
        super(RealTimeEventType.SipCallStart, 0, 0, timestamp);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof RealTimeSipCallStartEvent)) {
            return false;
        }
        RealTimeSipCallStartEvent other = (RealTimeSipCallStartEvent) obj;
        return Objects.equals(associationId, other.associationId) && Objects.equals(codecs, other.codecs)
                && Objects.equals(deviceInfo, other.deviceInfo) && Objects.equals(macAddress, other.macAddress)
                && Objects.equals(providerDomain, other.providerDomain) && Objects.equals(sipCallId, other.sipCallId)
                && Objects.equals(channel, other.channel) && Objects.equals(radioType, other.radioType);
    }

    public Long getAssociationId() {
        return associationId;
    }

    public List<String> getCodecs() {
        return codecs;
    }

    public String getDeviceInfo() {
        return deviceInfo;
    }


    @Override
    public MacAddress getClientMacAddress() {
        return macAddress;
    }


    public void setClientMacAddress(MacAddress clientMacAddress) {
        macAddress = clientMacAddress;
    }

    public String getProviderDomain() {
        return providerDomain;
    }

    public Long getSipCallId() {
        return sipCallId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = (prime * result)
                + Objects.hash(associationId, codecs, deviceInfo, macAddress, providerDomain, sipCallId, channel, radioType);
        return result;
    }

    public void setAssociationId(Long associationId) {
        this.associationId = associationId;
    }

    public void setCodecs(List<String> codecs) {
        this.codecs = codecs;
    }

    public void setDeviceInfo(String deviceInfo) {
        this.deviceInfo = deviceInfo;
    }

    /**
     * Keep this such that we can read older data
     * 
     * @param macAddress
     */
    @Deprecated
    public void setMac(MacAddress macAddress) {
        setMacAddress(macAddress);
    }

    public void setMacAddress(MacAddress macAddress) {
        this.macAddress = macAddress;
    }

    public void setProviderDomain(String providerDomain) {
        this.providerDomain = providerDomain;
    }

    public void setSipCallId(Long sipCallId) {
        this.sipCallId = sipCallId;
    }

    public boolean hasValidSipCallId() {
        return (sipCallId != null) && (sipCallId != 0);
    }

    public boolean hasValidAssociationId() {
        return (associationId != null) && (associationId != 0);
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public RadioType getRadioType() {
        return radioType;
    }

    public void setRadioType(RadioType radioType) {
        this.radioType = radioType;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }

        return hasUnsupportedValue(macAddress);
    }

    @Override
    public RealTimeSipCallStartEvent clone() {
        return (RealTimeSipCallStartEvent) super.clone();
    }
}
