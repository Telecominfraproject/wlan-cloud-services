/**
 * 
 */
package com.telecominfraproject.wlan.systemevent.equipment.realtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasClientMac;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasCustomerId;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasEquipmentId;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasProducedTimestamp;


/**
 * @author yongli
 *
 */
public abstract class RealTimeSipCallEventWithStats extends RealTimeEvent
        implements HasCustomerId, HasEquipmentId, HasClientMac, HasProducedTimestamp {

    /**
     * 
     */
    private static final long serialVersionUID = -8908272967317508366L;

    private Long sipCallId;
    private Long associationId;
    private MacAddress clientMacAddress;
    private List<RtpFlowStats> statuses;
    private int channel;
    private RadioType radioType;
    private List<String> codecs;
    private String providerDomain;

    public RealTimeSipCallEventWithStats(RealTimeEventType eventType, int customerId, long locationId, long equipmentId,
            Long timestamp) {
        super(eventType, customerId, locationId, equipmentId, timestamp);
    }

    public Long getSipCallId() {
        return sipCallId;
    }

    public void setSipCallId(Long sipCallId) {
        this.sipCallId = sipCallId;
    }

    public Long getAssociationId() {
        return associationId;
    }

    public void setAssociationId(Long associationId) {
        this.associationId = associationId;
    }

    @Override
    public MacAddress getClientMacAddress() {
        return clientMacAddress;
    }

    public void setClientMacAddress(MacAddress clientMacAddress) {
        this.clientMacAddress = clientMacAddress;
    }

    public List<RtpFlowStats> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<RtpFlowStats> statuses) {
        this.statuses = statuses;
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

    public List<String> getCodecs() {
        return codecs;
    }

    public void setCodecs(List<String> codecs) {
        this.codecs = codecs;
    }

    public String getProviderDomain() {
        return providerDomain;
    }

    public void setProviderDomain(String providerDomain) {
        this.providerDomain = providerDomain;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = (prime * result) +
                Objects.hash(associationId, clientMacAddress, sipCallId, statuses, channel, radioType, codecs, providerDomain);
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
        if (!(obj instanceof RealTimeSipCallEventWithStats)) {
            return false;
        }
        RealTimeSipCallEventWithStats other = (RealTimeSipCallEventWithStats) obj;
        return Objects.equals(associationId, other.associationId) && Objects.equals(clientMacAddress, other.clientMacAddress)
                && Objects.equals(sipCallId, other.sipCallId) && Objects.equals(statuses, other.statuses)
                && Objects.equals(channel, other.channel) && Objects.equals(radioType, other.radioType)
                && Objects.equals(codecs, other.codecs) && Objects.equals(providerDomain, other.providerDomain);
    }

    @Override
    public RealTimeSipCallEventWithStats clone() {
        RealTimeSipCallEventWithStats result = (RealTimeSipCallEventWithStats) super.clone();
        if (result.statuses != null) {
            result.statuses = new ArrayList<>(statuses.size());
            for (RtpFlowStats status : statuses) {
                result.statuses.add(status.clone());
            }
        }
        return result;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }

        if (hasUnsupportedValue(clientMacAddress) || hasUnsupportedValue(statuses)) {
            return true;
        }
        return false;
    }

}