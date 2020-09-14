package com.telecominfraproject.wlan.systemevent.equipment.realtime;

import java.util.Objects;

public class RealTimeSipCallStopEvent extends RealTimeSipCallEventWithStats {

    /**
     * 
     */
    private static final long serialVersionUID = -4689698521335747008L;
    private Integer callDuration;
    private SipCallStopReason reason;

    protected RealTimeSipCallStopEvent() {
        this(0L);
    }

    public RealTimeSipCallStopEvent(long timestamp) {
        this(0, 0, timestamp);
    }

    public RealTimeSipCallStopEvent(int customerId, long equipmentId, Long timestamp) {
        super(RealTimeEventType.SipCallStop, customerId, equipmentId, timestamp);
    }

    public Integer getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(Integer callDuration) {
        this.callDuration = callDuration;
    }

    public SipCallStopReason getReason() {
        return reason;
    }

    public void setReason(SipCallStopReason reason) {
        this.reason = reason;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(callDuration, reason);
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
        if (!(obj instanceof RealTimeSipCallStopEvent)) {
            return false;
        }
        RealTimeSipCallStopEvent other = (RealTimeSipCallStopEvent) obj;
        return Objects.equals(callDuration, other.callDuration) && this.reason == other.reason;
    }
    
    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (SipCallStopReason.isUnsupported(reason)) {
            return true;
        }
        return false;
    }
    

}
