package com.telecominfraproject.wlan.systemevent.equipment.realtime;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.ChannelHopReason;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;

public class RealTimeChannelHopEvent extends RealTimeEvent {

    private static final long serialVersionUID = -2724701914926591192L;
    private Integer oldChannel;
    private Integer newChannel;
    private ChannelHopReason reasonCode;
    private long changeTimestamp;
    private RadioType radioType;

    public RealTimeChannelHopEvent(RealTimeEventType eventType, int customerId, long equipmentId, RadioType radioType,
            Long timestamp) {
        super(RealTimeEventType.Channel_Hop, customerId, equipmentId, timestamp);
        this.radioType = radioType;
    }

    public RealTimeChannelHopEvent(RealTimeEventType eventType, int customerId, long equipmentId, RadioType radioType,
            int newChannel, int oldChannel, ChannelHopReason reasonCode, Long timestamp) {
        this(eventType, customerId, equipmentId, radioType, timestamp);
        this.newChannel = newChannel;
        this.oldChannel = oldChannel;
        this.reasonCode = reasonCode;
    }

    public long getChangeTimestamp() {
        return changeTimestamp;
    }

    public void setChangeTimestamp(long changeTimestamp) {
        this.changeTimestamp = changeTimestamp;
    }

    public Integer getOldChannel() {
        return oldChannel;
    }

    public void setOldChannel(Integer oldChannel) {
        this.oldChannel = oldChannel;
    }

    public Integer getNewChannel() {
        return newChannel;
    }

    public void setNewChannel(Integer newChannel) {
        this.newChannel = newChannel;
    }

    public ChannelHopReason getReasonCode() {
        return reasonCode;
    }

    public void setReasonCode(ChannelHopReason reasonCode) {
        this.reasonCode = reasonCode;
    }
    
    public RadioType getRadioType() {
        return radioType;
    }

    public void setRadioType(RadioType radioType) {
        this.radioType = radioType;
    }

    @Override
    public RealTimeChannelHopEvent clone() {
        RealTimeChannelHopEvent ret = (RealTimeChannelHopEvent) super.clone();
        ret.radioType = this.radioType;
        ret.newChannel = this.newChannel;
        ret.oldChannel = this.oldChannel;
        ret.reasonCode = this.reasonCode;
        
        return ret;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(changeTimestamp, newChannel, oldChannel, radioType, reasonCode);
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
        if (!(obj instanceof RealTimeChannelHopEvent)) {
            return false;
        }
        RealTimeChannelHopEvent other = (RealTimeChannelHopEvent) obj;
        return changeTimestamp == other.changeTimestamp && Objects.equals(newChannel, other.newChannel)
                && Objects.equals(oldChannel, other.oldChannel) && radioType == other.radioType
                && reasonCode == other.reasonCode;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (ChannelHopReason.isUnsupported(reasonCode)) {
            return true;
        }
        return false;
    }

}
