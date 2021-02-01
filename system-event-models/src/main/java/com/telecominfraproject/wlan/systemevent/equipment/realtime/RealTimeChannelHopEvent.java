package com.telecominfraproject.wlan.systemevent.equipment.realtime;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.ChannelHopReason;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasCustomerId;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasEquipmentId;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasProducedTimestamp;

public class RealTimeChannelHopEvent extends RealTimeEvent implements HasCustomerId, HasEquipmentId, HasProducedTimestamp  {

	private static final long serialVersionUID = -6563378014741823278L;
    private Integer oldChannel;
    private Integer newChannel;
    private ChannelHopReason reasonCode;
    private RadioType radioType;

    /**
     * Constructor used by JSON
     */
    private RealTimeChannelHopEvent() {
        super();
    }
    
    public static RealTimeChannelHopEvent createWithDefaults () {
        return new RealTimeChannelHopEvent();
    }
    
    public RealTimeChannelHopEvent(RealTimeEventType eventType, int customerId, long locationId, long equipmentId, RadioType radioType,
            Long timestamp) {
        super(RealTimeEventType.Channel_Hop, customerId, locationId, equipmentId, timestamp);
        this.radioType = radioType;
    }

    public RealTimeChannelHopEvent(RealTimeEventType eventType, int customerId, long locationId, long equipmentId, RadioType radioType,
            int newChannel, int oldChannel, ChannelHopReason reasonCode, Long timestamp) {
        this(eventType, customerId, locationId, equipmentId, radioType, timestamp);
        this.newChannel = newChannel;
        this.oldChannel = oldChannel;
        this.reasonCode = reasonCode;
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
        result = prime * result + Objects.hash(newChannel, oldChannel, radioType, reasonCode);
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
        return Objects.equals(getEventTimestamp(), other.getEventTimestamp())
        		&& Objects.equals(newChannel, other.newChannel)
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
