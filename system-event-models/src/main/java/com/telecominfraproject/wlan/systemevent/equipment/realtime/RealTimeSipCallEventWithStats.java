/**
 * 
 */
package com.telecominfraproject.wlan.systemevent.equipment.realtime;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
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
    private MacAddress macAddress;
    private List<RtpFlowStats> statuses;

    protected RealTimeSipCallEventWithStats(RealTimeEventType eventType, int customerId, long equipmentId,
            Long timestamp) {
        super(eventType, customerId, equipmentId, timestamp);
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
        return macAddress;
    }

    public void setClientMacAddress(MacAddress clientMacAddress) {
        macAddress = clientMacAddress;
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

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = (prime * result) + Objects.hash(associationId, macAddress, sipCallId, statuses);
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
        return Objects.equals(associationId, other.associationId) && Objects.equals(macAddress, other.macAddress)
                && Objects.equals(sipCallId, other.sipCallId) && Objects.equals(statuses, other.statuses);
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

        if (hasUnsupportedValue(macAddress) || hasUnsupportedValue(statuses)) {
            return true;
        }
        return false;
    }

}