package com.telecominfraproject.wlan.servicemetric.client.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.telecominfraproject.wlan.core.model.equipment.ChannelBandwidth;
import com.telecominfraproject.wlan.core.model.equipment.GuardInterval;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDataType;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDetails;
import com.telecominfraproject.wlan.servicemetric.models.WmmQueueStats;
import com.telecominfraproject.wlan.servicemetric.models.WmmQueueStats.WmmQueueType;

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
    private int[] rates;
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
     * The number of received management frames.
     */
    private Integer numRxManagement;

    /**
     * The number of received control frames.
     */
    private Integer numRxControl;

    /**
     * The number of received bytes.
     */
    private Long rxBytes;

    /**
     * The number of received data bytes.
     */
    private Long rxDataBytes;

    /**
     * The number of received RTS frames.
     */
    private Integer numRxRts;

    /**
     * The number of received CTS frames.
     */
    private Integer numRxCts;

    /**
     * The number of all received ACK frames (Acks + BlockAcks).
     */
    private Integer numRxAck;

    /**
     * The number of received probe request frames.
     */
    private Integer numRxProbeReq;

    /**
     * The number of received retry frames.
     */
    private Integer numRxRetry;

    /**
     * The number of received duplicated frames.
     */
    private Integer numRxDup;

    /**
     * The number of received null data frames.
     */
    private Integer numRxNullData;

    /**
     * The number of received ps-poll frames.
     */
    private Integer numRxPspoll;

    /**
     * The number of received STBC frames.
     */
    private Integer numRxStbc;

    /**
     * The number of received LDPC frames.
     */
    private Integer numRxLdpc;

    /**
     * The timestamp of last received layer three user traffic (IP data)
     */
    private Long lastRecvLayer3Ts;

    /**
     * The number of received ethernet and local generated frames for transmit.
     */
    private Long numRcvFrameForTx;

    /**
     * The number of TX frames queued.
     */
    private Long numTxQueued;

    /**
     * The number of every TX frame dropped.
     */
    private Integer numTxDropped;

    /**
     * The number of TX frame dropped due to retries.
     */
    private Integer numTxRetryDropped;

    /**
     * The number of frames successfully transmitted.
     */
    private Integer numTxSucc;

    /**
     * The Number of Tx bytes successfully transmitted.
     */
    private Long numTxByteSucc;

    /**
     * The number of successfully transmitted frames at first attempt.
     */
    private Integer numTxSuccNoRetry;

    /**
     * The number of successfully transmitted frames with retries.
     */
    private Integer numTxSuccRetries;

    /**
     * The number of Tx frames with retries.
     */
    private Integer numTxMultiRetries;

    /**
     * The number of TX management frames.
     */
    private Integer numTxManagement;

    /**
     * The number of Tx control frames.
     */
    private Integer numTxControl;

    /**
     * The number of Tx action frames.
     */
    private Integer numTxAction;

    /**
     * The number of TX probe response.
     */
    private Integer numTxPropResp;

    /**
     * The number of Tx data frames.
     */
    private Integer numTxData;

    /**
     * The number of Tx data frames with retries,done.
     */
    private Integer numTxDataRetries;

    /**
     * The number of RTS frames sent successfully, done.
     */
    private Integer numTxRtsSucc;

    /**
     * The number of RTS frames failed transmission.
     */
    private Integer numTxRtsFail;

    /**
     * The number of TX frames failed because of not Acked.
     */
    private Integer numTxNoAck;

    /**
     * The number of EAPOL frames sent.
     */
    private Integer numTxEapol;

    /**
     * The number of total LDPC frames sent.
     */
    private Integer numTxLdpc;

    /**
     * The number of total STBC frames sent.
     */
    private Integer numTxStbc;

    /**
     * The number of aggregation frames sent successfully.
     */
    private Integer numTxAggrSucc;

    /**
     * The number of aggregation frames sent using single MPDU (where the A-MPDU
     * contains only one MPDU. ).
     */
    private Integer numTxAggrOneMpdu;

    /**
     * The timestamp of last successfully sent layer three user traffic (IP
     * data).
     */
    private Long lastSentLayer3Ts;

    private Map<WmmQueueType, WmmQueueStats> wmmQueueStats;

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
        ClientMetrics ret = (ClientMetrics) super.clone();

        if (this.wmmQueueStats != null) {
            ret.wmmQueueStats = new EnumMap<>(WmmQueueType.class);
            if(!this.wmmQueueStats.isEmpty()) {
            	ret.wmmQueueStats.putAll(this.wmmQueueStats);
            }
        }
        
        if(this.rates!=null){
            ret.rates = this.rates.clone();
        }

        return ret;
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
                && this.channelBandWidth == other.channelBandWidth && Objects.equals(lastRecvLayer3Ts, other.lastRecvLayer3Ts)
                && Objects.equals(classificationName, other.classificationName)
                && Objects.equals(lastSentLayer3Ts, other.lastSentLayer3Ts)
                && Objects.equals(numRcvFrameForTx, other.numRcvFrameForTx)
                && Objects.equals(numRxAck, other.numRxAck) && Objects.equals(numRxBytes, other.numRxBytes)
                && Objects.equals(numRxControl, other.numRxControl) && Objects.equals(numRxCts, other.numRxCts)
                && Objects.equals(numRxData, other.numRxData) 
                && Objects.equals(numRxDup, other.numRxDup)
                && Objects.equals(numRxFramesReceived, other.numRxFramesReceived)
                && Objects.equals(numRxLdpc, other.numRxLdpc) && Objects.equals(numRxManagement, other.numRxManagement)
                && Objects.equals(numRxNoFcsErr, other.numRxNoFcsErr)
                && Objects.equals(numRxNullData, other.numRxNullData)
                && Objects.equals(numRxPackets, other.numRxPackets)
                && Objects.equals(numRxProbeReq, other.numRxProbeReq) && Objects.equals(numRxPspoll, other.numRxPspoll)
                && Objects.equals(numRxRetry, other.numRxRetry) && Objects.equals(numRxRts, other.numRxRts)
                && Objects.equals(numRxStbc, other.numRxStbc) 
                && Objects.equals(numTxAction, other.numTxAction)
                && Objects.equals(numTxAggrOneMpdu, other.numTxAggrOneMpdu)
                && Objects.equals(numTxAggrSucc, other.numTxAggrSucc)
                && Objects.equals(numTxByteSucc, other.numTxByteSucc) && Objects.equals(numTxBytes, other.numTxBytes)
                && Objects.equals(numTxControl, other.numTxControl) && Objects.equals(numTxData, other.numTxData)
                && Objects.equals(numTxDataRetries, other.numTxDataRetries)
                && Objects.equals(numTxDropped, other.numTxDropped) && Objects.equals(numTxEapol, other.numTxEapol)
                && Objects.equals(numTxFramesTransmitted, other.numTxFramesTransmitted)
                && Objects.equals(numTxLdpc, other.numTxLdpc) && Objects.equals(numTxManagement, other.numTxManagement)
                && Objects.equals(numTxMultiRetries, other.numTxMultiRetries)
                && Objects.equals(numTxNoAck, other.numTxNoAck) && Objects.equals(numTxPackets, other.numTxPackets)
                && Objects.equals(numTxPropResp, other.numTxPropResp) && Objects.equals(numTxQueued, other.numTxQueued)
                && Objects.equals(numTxRetryDropped, other.numTxRetryDropped)
                && Objects.equals(numTxRtsFail, other.numTxRtsFail) && Objects.equals(numTxRtsSucc, other.numTxRtsSucc)
                && Objects.equals(numTxStbc, other.numTxStbc) && Objects.equals(numTxSucc, other.numTxSucc)
                && Objects.equals(numTxSuccNoRetry, other.numTxSuccNoRetry)
                && Objects.equals(numTxSuccRetries, other.numTxSuccRetries)
                && this.radioType == other.radioType
                && Arrays.equals(rates, other.rates) && Objects.equals(rssi, other.rssi)
                && Objects.equals(rxBytes, other.rxBytes) && Objects.equals(rxDataBytes, other.rxDataBytes)
                && Objects.equals(rxDuplicatePackets, other.rxDuplicatePackets)
                && Objects.equals(rxLastRssi, other.rxLastRssi)
                && Objects.equals(snr, other.snr) && Objects.equals(txRetries, other.txRetries)
                && Objects.equals(wmmQueueStats, other.wmmQueueStats);
    }

    public Long getLastRecvLayer3Ts() {
        return lastRecvLayer3Ts;
    }

    public Long getLastSentLayer3Ts() {
        return lastSentLayer3Ts;
    }

    public Long getNumRcvFrameForTx() {
        return numRcvFrameForTx;
    }

    public Integer getNumRxAck() {
        return numRxAck;
    }

    public Long getNumRxBytes() {
        return numRxBytes;
    }

    public Integer getNumRxControl() {
        return numRxControl;
    }

    public Integer getNumRxCts() {
        return numRxCts;
    }

    public Integer getNumRxData() {
        return numRxData;
    }

    public Integer getNumRxDup() {
        return numRxDup;
    }

    public Integer getNumRxLdpc() {
        return numRxLdpc;
    }

    public Integer getNumRxManagement() {
        return numRxManagement;
    }

    public Integer getNumRxNoFcsErr() {
        return numRxNoFcsErr;
    }

    public Integer getNumRxNullData() {
        return numRxNullData;
    }

    public Long getNumRxPackets() {
        return numRxPackets;
    }

    public Integer getNumRxProbeReq() {
        return numRxProbeReq;
    }

    public Integer getNumRxPspoll() {
        return numRxPspoll;
    }

    public Integer getNumRxRetry() {
        return numRxRetry;
    }

    public Integer getNumRxRts() {
        return numRxRts;
    }

    public Integer getNumRxStbc() {
        return numRxStbc;
    }

    public Integer getNumTxAction() {
        return numTxAction;
    }

    public Integer getNumTxAggrOneMpdu() {
        return numTxAggrOneMpdu;
    }

    public Integer getNumTxAggrSucc() {
        return numTxAggrSucc;
    }

    public Long getNumTxBytes() {
        return numTxBytes;
    }

    public Long getNumTxByteSucc() {
        return numTxByteSucc;
    }

    public Integer getNumTxControl() {
        return numTxControl;
    }

    public Integer getNumTxData() {
        return numTxData;
    }

    public Integer getNumTxDataRetries() {
        return numTxDataRetries;
    }

    public Integer getNumTxDropped() {
        return numTxDropped;
    }

    public Integer getNumTxEapol() {
        return numTxEapol;
    }

    public Integer getNumTxLdpc() {
        return numTxLdpc;
    }

    public Integer getNumTxManagement() {
        return numTxManagement;
    }

    public Integer getNumTxMultiRetries() {
        return numTxMultiRetries;
    }

    public Integer getNumTxNoAck() {
        return numTxNoAck;
    }

    public Long getNumTxPackets() {
        return numTxPackets;
    }

    public Integer getNumTxPropResp() {
        return numTxPropResp;
    }

    public Long getNumTxQueued() {
        return numTxQueued;
    }

    public Integer getNumTxRetryDropped() {
        return numTxRetryDropped;
    }

    public Integer getNumTxRtsFail() {
        return numTxRtsFail;
    }

    public Integer getNumTxRtsSucc() {
        return numTxRtsSucc;
    }

    public Integer getNumTxStbc() {
        return numTxStbc;
    }

    public Integer getNumTxSucc() {
        return numTxSucc;
    }

    public Integer getNumTxSuccNoRetry() {
        return numTxSuccNoRetry;
    }

    public Integer getNumTxSuccRetries() {
        return numTxSuccRetries;
    }

    public RadioType getRadioType() {
        return radioType;
    }

    public int[] getRates() {
        return rates;
    }

    public Integer getRssi() {
        return rssi;
    }

    public Long getRxBytes() {
        return rxBytes;
    }

    public Long getRxDataBytes() {
        return rxDataBytes;
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

    public Map<WmmQueueType, WmmQueueStats> getWmmQueueStats() {
        return wmmQueueStats;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Arrays.hashCode(this.rates);
        result = prime * result + Objects.hash(averageRxRate, averageTxRate, channelBandWidth,
                classificationName, lastRecvLayer3Ts, lastSentLayer3Ts,
                numRcvFrameForTx, numRxAck, numRxBytes, numRxControl, numRxCts, numRxData,
                numRxDup, numRxFramesReceived, numRxLdpc, numRxManagement,
                numRxNoFcsErr, numRxNullData, numRxPackets, numRxProbeReq, numRxPspoll, numRxRetry, numRxRts, numRxStbc,
                numTxAction, numTxAggrOneMpdu, numTxAggrSucc, numTxByteSucc, numTxBytes,
                numTxControl, numTxData, numTxDataRetries, numTxDropped, numTxEapol, numTxFramesTransmitted, 
                numTxLdpc, numTxManagement,
                numTxMultiRetries, numTxNoAck, numTxPackets, numTxPropResp, numTxQueued,
                numTxRetryDropped, numTxRtsFail, numTxRtsSucc, numTxStbc, numTxSucc, numTxSuccNoRetry, numTxSuccRetries, 
                radioType, rssi, rxBytes, rxDataBytes, rxDuplicatePackets, rxLastRssi, snr, txRetries,
                wmmQueueStats);
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
        if (null != wmmQueueStats) {
            for (Entry<WmmQueueType, WmmQueueStats> entry : wmmQueueStats.entrySet()) {
                if (WmmQueueType.isUnsupported(entry.getKey())) {
                    return true;
                }
                if (hasUnsupportedValue(entry.getValue())) {
                    return true;
                }
            }
        }
        return false;
    }

    public void setLastRecvLayer3Ts(Long lastRecvLayer3Ts) {
        this.lastRecvLayer3Ts = lastRecvLayer3Ts;
    }

    public void setLastSentLayer3Ts(Long lastSentLayer3Ts) {
        this.lastSentLayer3Ts = lastSentLayer3Ts;
    }

    public void setNumRcvFrameForTx(Long numRcvFrameForTx) {
        this.numRcvFrameForTx = numRcvFrameForTx;
    }

    public void setNumRxAck(Integer numRxAck) {
        this.numRxAck = numRxAck;
    }

    public void setNumRxBytes(Long numRxBytes) {
        this.numRxBytes = numRxBytes;
    }

    public void setNumRxControl(Integer numRxControl) {
        this.numRxControl = numRxControl;
    }

    public void setNumRxCts(Integer numRxCts) {
        this.numRxCts = numRxCts;
    }

    public void setNumRxData(Integer numRxData) {
        this.numRxData = numRxData;
    }

    public void setNumRxDup(Integer numRxDup) {
        this.numRxDup = numRxDup;
    }

    public void setNumRxLdpc(Integer numRxLdpc) {
        this.numRxLdpc = numRxLdpc;
    }

    public void setNumRxManagement(Integer numRxManagement) {
        this.numRxManagement = numRxManagement;
    }

    public void setNumRxNoFcsErr(Integer numRxNoFcsErr) {
        this.numRxNoFcsErr = numRxNoFcsErr;
    }

    public void setNumRxNullData(Integer numRxNullData) {
        this.numRxNullData = numRxNullData;
    }

    public void setNumRxPackets(Long numRxPackets) {
        this.numRxPackets = numRxPackets;
    }

    public void setNumRxProbeReq(Integer numRxProbeReq) {
        this.numRxProbeReq = numRxProbeReq;
    }

    public void setNumRxPspoll(Integer numRxPspoll) {
        this.numRxPspoll = numRxPspoll;
    }

    public void setNumRxRetry(Integer numRxRetry) {
        this.numRxRetry = numRxRetry;
    }

    public void setNumRxRts(Integer numRxRts) {
        this.numRxRts = numRxRts;
    }

    public void setNumRxStbc(Integer numRxStbc) {
        this.numRxStbc = numRxStbc;
    }

    public void setNumTxAction(Integer numTxAction) {
        this.numTxAction = numTxAction;
    }

    public void setNumTxAggrOneMpdu(Integer numTxAggrOneMpdu) {
        this.numTxAggrOneMpdu = numTxAggrOneMpdu;
    }

    public void setNumTxAggrSucc(Integer numTxAggrSucc) {
        this.numTxAggrSucc = numTxAggrSucc;
    }

    public void setNumTxBytes(Long numTxBytes) {
        this.numTxBytes = numTxBytes;
    }

    public void setNumTxByteSucc(Long numTxByteSucc) {
        this.numTxByteSucc = numTxByteSucc;
    }

    public void setNumTxControl(Integer numTxControl) {
        this.numTxControl = numTxControl;
    }

    public void setNumTxData(Integer numTxData) {
        this.numTxData = numTxData;
    }

    public void setNumTxDataRetries(Integer numTxDataRetries) {
        this.numTxDataRetries = numTxDataRetries;
    }

    public void setNumTxDropped(Integer numTxDropped) {
        this.numTxDropped = numTxDropped;
    }

    public void setNumTxEapol(Integer numTxEapol) {
        this.numTxEapol = numTxEapol;
    }

    public void setNumTxLdpc(Integer numTxLdpc) {
        this.numTxLdpc = numTxLdpc;
    }

    public void setNumTxManagement(Integer numTxManagement) {
        this.numTxManagement = numTxManagement;
    }

    public void setNumTxMultiRetries(Integer numTxMultiRetries) {
        this.numTxMultiRetries = numTxMultiRetries;
    }

    public void setNumTxNoAck(Integer numTxNoAck) {
        this.numTxNoAck = numTxNoAck;
    }

    public void setNumTxPackets(Long numTxPackets) {
        this.numTxPackets = numTxPackets;
    }

    public void setNumTxPropResp(Integer numTxPropResp) {
        this.numTxPropResp = numTxPropResp;
    }

    public void setNumTxQueued(Long numTxQueued) {
        this.numTxQueued = numTxQueued;
    }

    public void setNumTxRetryDropped(Integer numTxRetryDropped) {
        this.numTxRetryDropped = numTxRetryDropped;
    }

    public void setNumTxRtsFail(Integer numTxRtsFail) {
        this.numTxRtsFail = numTxRtsFail;
    }

    public void setNumTxRtsSucc(Integer numTxRtsSucc) {
        this.numTxRtsSucc = numTxRtsSucc;
    }

    public void setNumTxStbc(Integer numTxStbc) {
        this.numTxStbc = numTxStbc;
    }

    public void setNumTxSucc(Integer numTxSucc) {
        this.numTxSucc = numTxSucc;
    }

    public void setNumTxSuccNoRetry(Integer numTxSuccNoRetry) {
        this.numTxSuccNoRetry = numTxSuccNoRetry;
    }

    public void setNumTxSuccRetries(Integer numTxSuccRetries) {
        this.numTxSuccRetries = numTxSuccRetries;
    }

    public void setRadioType(RadioType radioType) {
        this.radioType = radioType;
    }

    public void setRates(int[] rates) {
        this.rates = rates;
    }

    public void setRssi(Integer rssi) {
        this.rssi = rssi;
    }

    public void setRxBytes(Long rxBytes) {
        this.rxBytes = rxBytes;
    }

    public void setRxDataBytes(Long rxDataBytes) {
        this.rxDataBytes = rxDataBytes;
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

    public void setWmmQueueStats(Map<WmmQueueType, WmmQueueStats> wmmQueueStats) {
        this.wmmQueueStats = wmmQueueStats;
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
