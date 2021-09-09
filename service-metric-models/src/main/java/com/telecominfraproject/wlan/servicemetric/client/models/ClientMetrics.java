package com.telecominfraproject.wlan.servicemetric.client.models;

import java.util.Arrays;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.telecominfraproject.wlan.core.model.equipment.ChannelBandwidth;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDataType;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDetails;

/**
 * @author ekeddy
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientMetrics extends ServiceMetricDetails {

    private static final long serialVersionUID = 7242365268669169773L;

    private Integer secondsSinceLastRecv;

    private Long numRxPackets;
    private Long numTxPackets;
    private Long numRxBytes;
    private Long numTxBytes;
    private Integer txRetries;
    private Integer rxDuplicatePackets;
    private Integer snr;
    private Integer rssi;
    private String classificationName;
    ChannelBandwidth channelBandWidth;
    
    private Double averageTxRate;
    private Double averageRxRate;

    private Long numTxFramesTransmitted;
    private Long numRxFramesReceived;

    /**
     * The RSSI of last frame received.
     */
    private Integer rxLastRssi;

    /**
     * The number of received frames without FCS errors.
     */
    private Integer numRxNoFcsErr;

    /**
     * The number of received data frames.
     */
    private Integer numRxData;

    /**
     * The number of received bytes.
     */
    private Long rxBytes;

    /**
     * The number of received retry frames.
     */
    private Integer numRxRetry;

    /**
     * The number of every TX frame dropped.
     */
    private Integer numTxDropped;

    /**
     * The number of Tx data frames with retries,done.
     */
    private Integer numTxDataRetries;

    /**
     * Radio Type
     */
    private RadioType radioType;

    /**
     * How many seconds the AP measured for the metric
     */
    private Integer periodLengthSec = 5;

    @Override
    public ServiceMetricDataType getDataType() {
    	return ServiceMetricDataType.Client;
    }
    
    @Override
    public ClientMetrics clone() {
        return (ClientMetrics) super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof ClientMetrics)) {
            return false;
        }
        ClientMetrics other = (ClientMetrics) obj;
        return Objects.equals(averageRxRate, other.averageRxRate) && Objects.equals(averageTxRate, other.averageTxRate)
                && this.channelBandWidth == other.channelBandWidth && Objects.equals(classificationName, other.classificationName)
                && Objects.equals(numRxBytes, other.numRxBytes) && Objects.equals(numRxData, other.numRxData) 
                && Objects.equals(numRxFramesReceived, other.numRxFramesReceived) && Objects.equals(numRxNoFcsErr, other.numRxNoFcsErr)
                && Objects.equals(numRxPackets, other.numRxPackets) && Objects.equals(numRxRetry, other.numRxRetry)
                && Objects.equals(numTxBytes, other.numTxBytes) && Objects.equals(numTxDataRetries, other.numTxDataRetries)
                && Objects.equals(numTxDropped, other.numTxDropped) && Objects.equals(numTxFramesTransmitted, other.numTxFramesTransmitted)
                && Objects.equals(numTxPackets, other.numTxPackets) && this.radioType == other.radioType
                && Objects.equals(rssi, other.rssi) && Objects.equals(rxBytes, other.rxBytes)
                && Objects.equals(rxDuplicatePackets, other.rxDuplicatePackets) && Objects.equals(rxLastRssi, other.rxLastRssi)
                && Objects.equals(snr, other.snr) && Objects.equals(txRetries, other.txRetries);
    }

    public Long getNumRxBytes() {
        return numRxBytes;
    }

    public Integer getNumRxData() {
        return numRxData;
    }

    public Integer getNumRxNoFcsErr() {
        return numRxNoFcsErr;
    }

    public Long getNumRxPackets() {
        return numRxPackets;
    }

    public Integer getNumRxRetry() {
        return numRxRetry;
    }

    public Long getNumTxBytes() {
        return numTxBytes;
    }

    public Integer getNumTxDataRetries() {
        return numTxDataRetries;
    }

    public Integer getNumTxDropped() {
        return numTxDropped;
    }

    public Long getNumTxPackets() {
        return numTxPackets;
    }

    public RadioType getRadioType() {
        return radioType;
    }

    public Integer getRssi() {
        return rssi;
    }

    public Long getRxBytes() {
        return rxBytes;
    }

    public Integer getRxDuplicatePackets() {
        return rxDuplicatePackets;
    }

    public Integer getRxLastRssi() {
        return rxLastRssi;
    }

    public Integer getSnr() {
        return snr;
    }

    public Integer getTxRetries() {
        return txRetries;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(averageRxRate, averageTxRate, channelBandWidth,
                classificationName, numRxBytes, numRxData, numRxFramesReceived,
                numRxNoFcsErr, numRxPackets, numRxRetry, numTxBytes,
                numTxDataRetries, numTxDropped, numTxFramesTransmitted, numTxPackets,
                radioType, rssi, rxBytes, rxDuplicatePackets, rxLastRssi, snr, txRetries);
        return result;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (RadioType.isUnsupported(radioType) || (ChannelBandwidth.isUnsupported(this.channelBandWidth))) {
            return true;
        }
        return false;
    }

    public void setNumRxBytes(Long numRxBytes) {
        this.numRxBytes = numRxBytes;
    }

    public void setNumRxData(Integer numRxData) {
        this.numRxData = numRxData;
    }

    public void setNumRxNoFcsErr(Integer numRxNoFcsErr) {
        this.numRxNoFcsErr = numRxNoFcsErr;
    }

    public void setNumRxPackets(Long numRxPackets) {
        this.numRxPackets = numRxPackets;
    }

    public void setNumRxRetry(Integer numRxRetry) {
        this.numRxRetry = numRxRetry;
    }

    public void setNumTxBytes(Long numTxBytes) {
        this.numTxBytes = numTxBytes;
    }

    public void setNumTxDataRetries(Integer numTxDataRetries) {
        this.numTxDataRetries = numTxDataRetries;
    }

    public void setNumTxDropped(Integer numTxDropped) {
        this.numTxDropped = numTxDropped;
    }

    public void setNumTxPackets(Long numTxPackets) {
        this.numTxPackets = numTxPackets;
    }

    public void setRadioType(RadioType radioType) {
        this.radioType = radioType;
    }

    public void setRssi(Integer rssi) {
        this.rssi = rssi;
    }

    public void setRxBytes(Long rxBytes) {
        this.rxBytes = rxBytes;
    }

    public void setRxDuplicatePackets(Integer rxDuplicatePackets) {
        this.rxDuplicatePackets = rxDuplicatePackets;
    }

    public void setRxLastRssi(Integer rxLastRssi) {
        this.rxLastRssi = rxLastRssi;
    }

    public void setSnr(Integer snr) {
        this.snr = snr;
    }

    public void setTxRetries(Integer txRetries) {
        this.txRetries = txRetries;
    }

    public Long getNumTxFramesTransmitted() {
        return numTxFramesTransmitted;
    }

    public void setNumTxFramesTransmitted(Long numTxFramesTransmitted) {
        this.numTxFramesTransmitted = numTxFramesTransmitted;
    }

    public Long getNumRxFramesReceived() {
        return numRxFramesReceived;
    }

    public void setNumRxFramesReceived(Long numRxFramesReceived) {
        this.numRxFramesReceived = numRxFramesReceived;
    }

    public String getClassificationName() {
        return classificationName;
    }

    public void setClassificationName(String classificationName) {
        this.classificationName = classificationName;
    }

    public ChannelBandwidth getChannelBandWidth() {
        return channelBandWidth;
    }

    public void setChannelBandWidth(ChannelBandwidth channelBandWidth) {
        this.channelBandWidth = channelBandWidth;
    }

    public Double getAverageTxRate() {
        return averageTxRate;
    }

    public Double getAverageRxRate() {
        return averageRxRate;
    }

    public void setAverageTxRate(Double averageTxRate) {
        this.averageTxRate = averageTxRate;
    }

    public void setAverageRxRate(Double averageRxRate) {
        this.averageRxRate = averageRxRate;
    }

	public Integer getSecondsSinceLastRecv() {
		return secondsSinceLastRecv;
	}

	public void setSecondsSinceLastRecv(Integer secondsSinceLastRecv) {
		this.secondsSinceLastRecv = secondsSinceLastRecv;
	}

	public Integer getPeriodLengthSec() {
		return periodLengthSec;
	}

	public void setPeriodLengthSec(Integer periodLengthSec) {
		this.periodLengthSec = periodLengthSec;
	}
}
