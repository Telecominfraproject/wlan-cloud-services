package com.telecominfraproject.wlan.servicemetrics.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class WmmQueueStats extends BaseJsonModel {

	private static final long serialVersionUID = 1784481171729854130L;

    public static enum WmmQueueType {
        BE, BK, VI, VO, UNSUPPORTED;
        
        @JsonCreator
        public static WmmQueueType getByName(String value) {
            return JsonDeserializationUtils.deserializEnum(value, WmmQueueType.class, UNSUPPORTED);
        }

        public static boolean isUnsupported(WmmQueueType queueType) {
            return UNSUPPORTED.equals(queueType);
        }
    }

    private WmmQueueType queueType;
    private long txFrames;
    private long txBytes;

    private long txFailedFrames;
    private long txFailedBytes;

    private long rxFrames;
    private long rxBytes;

    private long rxFailedFrames;
    private long rxFailedBytes;

    private long forwardFrames;
    private long forwardBytes;

    private long txExpiredFrames;
    private long txExpiredBytes;

    public WmmQueueStats() {
        // for serialization
    }

    public WmmQueueStats(WmmQueueType queueType, long txFrames, long txBytes, long txFailedFrames, long txFailedBytes,
            long rxFrames, long rxBytes, long rxFailedFrames, long rxFailedBytes, long forwardFrames, long forwardBytes,
            long txExpiredFrames, long txExpiredBytes) {
        super();
        this.queueType = queueType;
        this.txFrames = txFrames;
        this.txBytes = txBytes;
        this.txFailedFrames = txFailedFrames;
        this.txFailedBytes = txFailedBytes;
        this.rxFrames = rxFrames;
        this.rxBytes = rxBytes;
        this.rxFailedFrames = rxFailedFrames;
        this.rxFailedBytes = rxFailedBytes;
        this.forwardFrames = forwardFrames;
        this.forwardBytes = forwardBytes;
        this.txExpiredFrames = txExpiredFrames;
        this.txExpiredBytes = txExpiredBytes;
    }

    public WmmQueueType getQueueType() {
        return queueType;
    }

    public void setQueueType(WmmQueueType queueType) {
        this.queueType = queueType;
    }

    public long getTxFrames() {
        return txFrames;
    }

    public void setTxFrames(long txFrames) {
        this.txFrames = txFrames;
    }

    public long getTxBytes() {
        return txBytes;
    }

    public void setTxBytes(long txBytes) {
        this.txBytes = txBytes;
    }

    public long getTxFailedFrames() {
        return txFailedFrames;
    }

    public void setTxFailedFrames(long txFailedFrames) {
        this.txFailedFrames = txFailedFrames;
    }

    public long getTxFailedBytes() {
        return txFailedBytes;
    }

    public void setTxFailedBytes(long txFailedBytes) {
        this.txFailedBytes = txFailedBytes;
    }

    public long getRxFrames() {
        return rxFrames;
    }

    public void setRxFrames(long rxFrames) {
        this.rxFrames = rxFrames;
    }

    public long getRxBytes() {
        return rxBytes;
    }

    public void setRxBytes(long rxBytes) {
        this.rxBytes = rxBytes;
    }

    public long getRxFailedFrames() {
        return rxFailedFrames;
    }

    public void setRxFailedFrames(long rxFailedFrames) {
        this.rxFailedFrames = rxFailedFrames;
    }

    public long getRxFailedBytes() {
        return rxFailedBytes;
    }

    public void setRxFailedBytes(long rxFailedBytes) {
        this.rxFailedBytes = rxFailedBytes;
    }

    public long getForwardFrames() {
        return forwardFrames;
    }

    public void setForwardFrames(long forwardFrames) {
        this.forwardFrames = forwardFrames;
    }

    public long getForwardBytes() {
        return forwardBytes;
    }

    public void setForwardBytes(long forwardBytes) {
        this.forwardBytes = forwardBytes;
    }

    public long getTxExpiredFrames() {
        return txExpiredFrames;
    }

    public void setTxExpiredFrames(long txExpiredFrames) {
        this.txExpiredFrames = txExpiredFrames;
    }

    public long getTxExpiredBytes() {
        return txExpiredBytes;
    }

    public void setTxExpiredBytes(long txExpiredBytes) {
        this.txExpiredBytes = txExpiredBytes;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (forwardBytes ^ (forwardBytes >>> 32));
        result = prime * result + (int) (forwardFrames ^ (forwardFrames >>> 32));
        result = prime * result + ((queueType == null) ? 0 : queueType.hashCode());
        result = prime * result + (int) (rxBytes ^ (rxBytes >>> 32));
        result = prime * result + (int) (rxFailedBytes ^ (rxFailedBytes >>> 32));
        result = prime * result + (int) (rxFailedFrames ^ (rxFailedFrames >>> 32));
        result = prime * result + (int) (rxFrames ^ (rxFrames >>> 32));
        result = prime * result + (int) (txBytes ^ (txBytes >>> 32));
        result = prime * result + (int) (txExpiredBytes ^ (txExpiredBytes >>> 32));
        result = prime * result + (int) (txExpiredFrames ^ (txExpiredFrames >>> 32));
        result = prime * result + (int) (txFailedBytes ^ (txFailedBytes >>> 32));
        result = prime * result + (int) (txFailedFrames ^ (txFailedFrames >>> 32));
        result = prime * result + (int) (txFrames ^ (txFrames >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        WmmQueueStats other = (WmmQueueStats) obj;
        if (forwardBytes != other.forwardBytes)
            return false;
        if (forwardFrames != other.forwardFrames)
            return false;
        if (queueType != other.queueType)
            return false;
        if (rxBytes != other.rxBytes)
            return false;
        if (rxFailedBytes != other.rxFailedBytes)
            return false;
        if (rxFailedFrames != other.rxFailedFrames)
            return false;
        if (rxFrames != other.rxFrames)
            return false;
        if (txBytes != other.txBytes)
            return false;
        if (txExpiredBytes != other.txExpiredBytes)
            return false;
        if (txExpiredFrames != other.txExpiredFrames)
            return false;
        if (txFailedBytes != other.txFailedBytes)
            return false;
        if (txFailedFrames != other.txFailedFrames)
            return false;
        if (txFrames != other.txFrames)
            return false;
        return true;
    }
    
    @Override 
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (WmmQueueType.isUnsupported(queueType)) {
            return true;
        }
        return false;
    }

    @Override
    public WmmQueueStats clone() {
        return (WmmQueueStats) super.clone();
    }
}
