package com.telecominfraproject.wlan.systemevent.equipment.realtime;

import java.util.Objects;

public class RealTimeSipCallReportEvent extends RealTimeSipCallEventWithStats {
    /**
     * 
     */
    private static final long serialVersionUID = 1341263590224414034L;

    private SIPCallReportReason reportReason;

    /**
     * Used for JSON encode / decode
     */
    @Deprecated
    public RealTimeSipCallReportEvent() {
        this(0, 0L, 0L);
    }
    
    public RealTimeSipCallReportEvent(Long timestamp) {
        this(0, 0L, timestamp);
    }
    
    public RealTimeSipCallReportEvent(int customerId, long equipmentId, Long timestamp) {
        super(RealTimeEventType.SipCallReport, customerId, equipmentId, timestamp);
    }

    @Override 
    public RealTimeSipCallReportEvent clone() {
        return (RealTimeSipCallReportEvent) super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof RealTimeSipCallReportEvent)) {
            return false;
        }
        RealTimeSipCallReportEvent other = (RealTimeSipCallReportEvent) obj;
        return this.reportReason == other.reportReason;
    }
    
    public SIPCallReportReason getReportReason() {
        return reportReason;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(reportReason);
        return result;
    }
    
    public void setReportReason(SIPCallReportReason reportReason) {
        this.reportReason = reportReason;
    }
    
    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }

        if (SIPCallReportReason.isUnsupported(reportReason)) {
            return true;
        }
        return false;
    }
    
}
