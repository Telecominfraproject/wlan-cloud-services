package com.telecominfraproject.wlan.servicemetric.apnode.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasSourceTimestamp;

@JsonInclude(JsonInclude.Include.NON_NULL) 
public class RadioStatistics extends BaseJsonModel implements HasSourceTimestamp {
    private static final long serialVersionUID = 1185985309064758000L;

    /**
     * Actuall Cell Size
     */
    private List<Integer> actualCellSize;
    
    /**
     * The current primary channel
     */
    private Integer curChannel;
    
    /**
    *  The RSSI of last frame received.
    */
    private Integer rxLastRssi;

    /**
    *  The number of received data frames.
    */
    private Long rxDataBytes;

    /**
    *  The number of received retry frames.
    */
    private Integer numRxRetry;

    /**
    *  The number of received frames with errors.
    */
    private Integer numRxErr;

    // The number of retry tx attempts that have been made
    private Integer numTxRetryAttemps;
            
    private Long numTxFramesTransmitted;
    private Long numTxDataFrames;
    private Long numRxFramesReceived;
    private Long numRxDataFrames;
    
    private long sourceTimestampMs;

    
    @Override
    public RadioStatistics clone() {
        RadioStatistics ret = (RadioStatistics) super.clone();
        if (null != ret.actualCellSize) {
            ret.actualCellSize = new ArrayList<>(ret.actualCellSize);
        }
        return ret;
    }

    /**
     * @return the actualCellSize
     */
    public List<Integer> getActualCellSize() {
        return actualCellSize;
    }

    public Integer getNumRxErr() {
        return numRxErr;
    }

    public Integer getNumRxRetry() {
        return numRxRetry;
    }

    public Integer getNumTxRetryAttemps() {
        return numTxRetryAttemps;
    }

    public Long getRxDataBytes() {
        return rxDataBytes;
    }

    public Integer getRxLastRssi() {
        return rxLastRssi;
    }

    /**
     * @param actualCellSize the actualCellSize to set
     */
    public void setActualCellSize(List<Integer> actualCellSize) {
        this.actualCellSize = actualCellSize;
    }

    public void setNumRxErr(Integer numRxErr) {
        this.numRxErr = numRxErr;
    }

    public void setNumRxRetry(Integer numRxRetry) {
        this.numRxRetry = numRxRetry;
    }

    public void setNumTxRetryAttemps(Integer numTxRetryAttemps) {
        this.numTxRetryAttemps = numTxRetryAttemps;
    }
    
    public void setRxDataBytes(Long rxDataBytes) {
        this.rxDataBytes = rxDataBytes;
    }

    public void setRxLastRssi(Integer rxLastRssi) {
        this.rxLastRssi = rxLastRssi;
    }

    public Integer getCurChannel() {
        return curChannel;
    }

    public void setCurChannel(Integer curChannel) {
        this.curChannel = curChannel;
    }

    public Long getNumTxFramesTransmitted() {
        return numTxFramesTransmitted;
    }

    public void setNumTxFramesTransmitted(Long numTxFramesTransmitted) {
        this.numTxFramesTransmitted = numTxFramesTransmitted;
    }

    public Long getNumTxDataFrames() {
        return numTxDataFrames;
    }

    public void setNumTxDataFrames(Long numTxDataFrames) {
        this.numTxDataFrames = numTxDataFrames;
    }

    public Long getNumRxFramesReceived() {
        return numRxFramesReceived;
    }

    public void setNumRxFramesReceived(Long numRxFramesReceived) {
        this.numRxFramesReceived = numRxFramesReceived;
    }

    public Long getNumRxDataFrames() {
        return numRxDataFrames;
    }

    public void setNumRxDataFrames(Long numRxDataFrames) {
        this.numRxDataFrames = numRxDataFrames;
    }
    
    public void setSourceTimestampMs(long sourceTimestamp) {
        this.sourceTimestampMs = sourceTimestamp;
    }
    
    @Override
    public long getSourceTimestampMs() {
        return this.sourceTimestampMs;
    }

    @Override
    public int hashCode() {
        return Objects.hash(actualCellSize, curChannel, numRxDataFrames,
                numRxErr, numRxFramesReceived, numRxRetry, numTxDataFrames,
                numTxFramesTransmitted,numTxRetryAttemps,
                rxDataBytes, rxLastRssi, sourceTimestampMs);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RadioStatistics other = (RadioStatistics) obj;
        return Objects.equals(actualCellSize, other.actualCellSize) && Objects.equals(curChannel, other.curChannel)
                && Objects.equals(numRxDataFrames, other.numRxDataFrames) && Objects.equals(numRxErr, other.numRxErr)
                && Objects.equals(numRxFramesReceived, other.numRxFramesReceived) && Objects.equals(numRxRetry, other.numRxRetry)
                && Objects.equals(numTxDataFrames, other.numTxDataFrames) && Objects.equals(numTxFramesTransmitted, other.numTxFramesTransmitted)
                && Objects.equals(numTxRetryAttemps, other.numTxRetryAttemps) && Objects.equals(rxDataBytes, other.rxDataBytes)
                && Objects.equals(rxLastRssi, other.rxLastRssi) && sourceTimestampMs == other.sourceTimestampMs;
    }

}
