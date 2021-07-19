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
import com.telecominfraproject.wlan.servicemetric.models.McsStats;
import com.telecominfraproject.wlan.servicemetric.models.McsType;
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
    private Integer rateCount;
    private int[] rates;
    private int[] mcs;
    private Integer vhtMcs;
    private Integer snr;
    private Integer rssi;
    private String sessionId;
    private String classificationName;
    ChannelBandwidth channelBandWidth;
    GuardInterval guardInterval;

    private Integer ciscoLastRate;
    
    private Double averageTxRate;
    private Double averageRxRate;

    private Long numTxDataFrames_12_Mbps;
    private Long numTxDataFrames_54_Mbps;
    private Long numTxDataFrames_108_Mbps;
    private Long numTxDataFrames_300_Mbps;
    private Long numTxDataFrames_450_Mbps;
    private Long numTxDataFrames_1300_Mbps;
    private Long numTxDataFrames_1300Plus_Mbps;
    private Long numRxDataFrames_12_Mbps;
    private Long numRxDataFrames_54_Mbps;
    private Long numRxDataFrames_108_Mbps;
    private Long numRxDataFrames_300_Mbps;
    private Long numRxDataFrames_450_Mbps;
    private Long numRxDataFrames_1300_Mbps;
    private Long numRxDataFrames_1300Plus_Mbps;

    private Long numTxTimeFramesTransmitted;
    private Long numRxTimeToMe;

    private Long numTxTimeData;
    private Long numRxTimeData;
    private Long numTxFramesTransmitted;
    private Long numTxSuccessWithRetry;
    private Long numTxMultipleRetries;
    private Long numTxDataTransmittedRetried;
    private Long numTxDataTransmitted;
    private Long numRxFramesReceived;
    private Long numRxDataFramesRetried;
    private Long numRxDataFrames;

    private Long numTx_1_Mbps;
    private Long numTx_6_Mbps;
    private Long numTx_9_Mbps;
    private Long numTx_12_Mbps;
    private Long numTx_18_Mbps;
    private Long numTx_24_Mbps;
    private Long numTx_36_Mbps;
    private Long numTx_48_Mbps;
    private Long numTx_54_Mbps;

    private Long numRx_1_Mbps;
    private Long numRx_6_Mbps;
    private Long numRx_9_Mbps;
    private Long numRx_12_Mbps;
    private Long numRx_18_Mbps;
    private Long numRx_24_Mbps;
    private Long numRx_36_Mbps;
    private Long numRx_48_Mbps;
    private Long numRx_54_Mbps;


    private Long numTxHT_6_5_Mbps;
    private Long numTxHT_7_1_Mbps;
    private Long numTxHT_13_Mbps;
    private Long numTxHT_13_5_Mbps;
    private Long numTxHT_14_3_Mbps;
    private Long numTxHT_15_Mbps;
    private Long numTxHT_19_5_Mbps;
    private Long numTxHT_21_7_Mbps;
    private Long numTxHT_26_Mbps;
    private Long numTxHT_27_Mbps;
    private Long numTxHT_28_7_Mbps;
    private Long numTxHT_28_8_Mbps;
    private Long numTxHT_29_2_Mbps;
    private Long numTxHT_30_Mbps;
    private Long numTxHT_32_5_Mbps;
    private Long numTxHT_39_Mbps;
    private Long numTxHT_40_5_Mbps;
    private Long numTxHT_43_2_Mbps;
    private Long numTxHT_45_Mbps;
    private Long numTxHT_52_Mbps;
    private Long numTxHT_54_Mbps;
    private Long numTxHT_57_5_Mbps;
    private Long numTxHT_57_7_Mbps;
    private Long numTxHT_58_5_Mbps;
    private Long numTxHT_60_Mbps;
    private Long numTxHT_65_Mbps;
    private Long numTxHT_72_1_Mbps;
    private Long numTxHT_78_Mbps;
    private Long numTxHT_81_Mbps;
    private Long numTxHT_86_6_Mbps;
    private Long numTxHT_86_8_Mbps;
    private Long numTxHT_87_8_Mbps;
    private Long numTxHT_90_Mbps;
    private Long numTxHT_97_5_Mbps;
    private Long numTxHT_104_Mbps;
    private Long numTxHT_108_Mbps;
    private Long numTxHT_115_5_Mbps;
    private Long numTxHT_117_Mbps;
    private Long numTxHT_117_1_Mbps;
    private Long numTxHT_120_Mbps;
    private Long numTxHT_121_5_Mbps;
    private Long numTxHT_130_Mbps;
    private Long numTxHT_130_3_Mbps;
    private Long numTxHT_135_Mbps;
    private Long numTxHT_144_3_Mbps;
    private Long numTxHT_150_Mbps;
    private Long numTxHT_156_Mbps;
    private Long numTxHT_162_Mbps;
    private Long numTxHT_173_1_Mbps;
    private Long numTxHT_173_3_Mbps;
    private Long numTxHT_175_5_Mbps;
    private Long numTxHT_180_Mbps;
    private Long numTxHT_195_Mbps;
    private Long numTxHT_200_Mbps;
    private Long numTxHT_208_Mbps;
    private Long numTxHT_216_Mbps;
    private Long numTxHT_216_6_Mbps;
    private Long numTxHT_231_1_Mbps;
    private Long numTxHT_234_Mbps;
    private Long numTxHT_240_Mbps;
    private Long numTxHT_243_Mbps;
    private Long numTxHT_260_Mbps;
    private Long numTxHT_263_2_Mbps;
    private Long numTxHT_270_Mbps;
    private Long numTxHT_288_7_Mbps;
    private Long numTxHT_288_8_Mbps;
    private Long numTxHT_292_5_Mbps;
    private Long numTxHT_300_Mbps;
    private Long numTxHT_312_Mbps;
    private Long numTxHT_324_Mbps;
    private Long numTxHT_325_Mbps;
    private Long numTxHT_346_7_Mbps;
    private Long numTxHT_351_Mbps;
    private Long numTxHT_351_2_Mbps;
    private Long numTxHT_360_Mbps;

    private Long numRxHT_6_5_Mbps;
    private Long numRxHT_7_1_Mbps;
    private Long numRxHT_13_Mbps;
    private Long numRxHT_13_5_Mbps;
    private Long numRxHT_14_3_Mbps;
    private Long numRxHT_15_Mbps;
    private Long numRxHT_19_5_Mbps;
    private Long numRxHT_21_7_Mbps;
    private Long numRxHT_26_Mbps;
    private Long numRxHT_27_Mbps;
    private Long numRxHT_28_7_Mbps;
    private Long numRxHT_28_8_Mbps;
    private Long numRxHT_29_2_Mbps;
    private Long numRxHT_30_Mbps;
    private Long numRxHT_32_5_Mbps;
    private Long numRxHT_39_Mbps;
    private Long numRxHT_40_5_Mbps;
    private Long numRxHT_43_2_Mbps;
    private Long numRxHT_45_Mbps;
    private Long numRxHT_52_Mbps;
    private Long numRxHT_54_Mbps;
    private Long numRxHT_57_5_Mbps;
    private Long numRxHT_57_7_Mbps;
    private Long numRxHT_58_5_Mbps;
    private Long numRxHT_60_Mbps;
    private Long numRxHT_65_Mbps;
    private Long numRxHT_72_1_Mbps;
    private Long numRxHT_78_Mbps;
    private Long numRxHT_81_Mbps;
    private Long numRxHT_86_6_Mbps;
    private Long numRxHT_86_8_Mbps;
    private Long numRxHT_87_8_Mbps;
    private Long numRxHT_90_Mbps;
    private Long numRxHT_97_5_Mbps;
    private Long numRxHT_104_Mbps;
    private Long numRxHT_108_Mbps;
    private Long numRxHT_115_5_Mbps;
    private Long numRxHT_117_Mbps;
    private Long numRxHT_117_1_Mbps;
    private Long numRxHT_120_Mbps;
    private Long numRxHT_121_5_Mbps;
    private Long numRxHT_130_Mbps;
    private Long numRxHT_130_3_Mbps;
    private Long numRxHT_135_Mbps;
    private Long numRxHT_144_3_Mbps;
    private Long numRxHT_150_Mbps;
    private Long numRxHT_156_Mbps;
    private Long numRxHT_162_Mbps;
    private Long numRxHT_173_1_Mbps;
    private Long numRxHT_173_3_Mbps;
    private Long numRxHT_175_5_Mbps;
    private Long numRxHT_180_Mbps;
    private Long numRxHT_195_Mbps;
    private Long numRxHT_200_Mbps;
    private Long numRxHT_208_Mbps;
    private Long numRxHT_216_Mbps;
    private Long numRxHT_216_6_Mbps;
    private Long numRxHT_231_1_Mbps;
    private Long numRxHT_234_Mbps;
    private Long numRxHT_240_Mbps;
    private Long numRxHT_243_Mbps;
    private Long numRxHT_260_Mbps;
    private Long numRxHT_263_2_Mbps;
    private Long numRxHT_270_Mbps;
    private Long numRxHT_288_7_Mbps;
    private Long numRxHT_288_8_Mbps;
    private Long numRxHT_292_5_Mbps;
    private Long numRxHT_300_Mbps;
    private Long numRxHT_312_Mbps;
    private Long numRxHT_324_Mbps;
    private Long numRxHT_325_Mbps;
    private Long numRxHT_346_7_Mbps;
    private Long numRxHT_351_Mbps;
    private Long numRxHT_351_2_Mbps;
    private Long numRxHT_360_Mbps;

    private Long numTxVHT_292_5_Mbps;
    private Long numTxVHT_325_Mbps;
    private Long numTxVHT_364_5_Mbps;
    private Long numTxVHT_390_Mbps;
    private Long numTxVHT_400_Mbps;
    private Long numTxVHT_403_Mbps;
    private Long numTxVHT_405_Mbps;
    private Long numTxVHT_432_Mbps;
    private Long numTxVHT_433_2_Mbps;
    private Long numTxVHT_450_Mbps;
    private Long numTxVHT_468_Mbps;
    private Long numTxVHT_480_Mbps;
    private Long numTxVHT_486_Mbps;
    private Long numTxVHT_520_Mbps;
    private Long numTxVHT_526_5_Mbps;
    private Long numTxVHT_540_Mbps;
    private Long numTxVHT_585_Mbps;
    private Long numTxVHT_600_Mbps;
    private Long numTxVHT_648_Mbps;
    private Long numTxVHT_650_Mbps;
    private Long numTxVHT_702_Mbps;
    private Long numTxVHT_720_Mbps;
    private Long numTxVHT_780_Mbps;
    private Long numTxVHT_800_Mbps;
    private Long numTxVHT_866_7_Mbps;
    private Long numTxVHT_877_5_Mbps;
    private Long numTxVHT_936_Mbps;
    private Long numTxVHT_975_Mbps;
    private Long numTxVHT_1040_Mbps;
    private Long numTxVHT_1053_Mbps;
    private Long numTxVHT_1053_1_Mbps;
    private Long numTxVHT_1170_Mbps;
    private Long numTxVHT_1300_Mbps;
    private Long numTxVHT_1404_Mbps;
    private Long numTxVHT_1560_Mbps;
    private Long numTxVHT_1579_5_Mbps;
    private Long numTxVHT_1733_1_Mbps;
    private Long numTxVHT_1733_4_Mbps;
    private Long numTxVHT_1755_Mbps;
    private Long numTxVHT_1872_Mbps;
    private Long numTxVHT_1950_Mbps;
    private Long numTxVHT_2080_Mbps;
    private Long numTxVHT_2106_Mbps;
    private Long numTxVHT_2340_Mbps;
    private Long numTxVHT_2600_Mbps;
    private Long numTxVHT_2808_Mbps;
    private Long numTxVHT_3120_Mbps;
    private Long numTxVHT_3466_8_Mbps;

    private Long numRxVHT_292_5_Mbps;
    private Long numRxVHT_325_Mbps;
    private Long numRxVHT_364_5_Mbps;
    private Long numRxVHT_390_Mbps;
    private Long numRxVHT_400_Mbps;
    private Long numRxVHT_403_Mbps;
    private Long numRxVHT_405_Mbps;
    private Long numRxVHT_432_Mbps;
    private Long numRxVHT_433_2_Mbps;
    private Long numRxVHT_450_Mbps;
    private Long numRxVHT_468_Mbps;
    private Long numRxVHT_480_Mbps;
    private Long numRxVHT_486_Mbps;
    private Long numRxVHT_520_Mbps;
    private Long numRxVHT_526_5_Mbps;
    private Long numRxVHT_540_Mbps;
    private Long numRxVHT_585_Mbps;
    private Long numRxVHT_600_Mbps;
    private Long numRxVHT_648_Mbps;
    private Long numRxVHT_650_Mbps;
    private Long numRxVHT_702_Mbps;
    private Long numRxVHT_720_Mbps;
    private Long numRxVHT_780_Mbps;
    private Long numRxVHT_800_Mbps;
    private Long numRxVHT_866_7_Mbps;
    private Long numRxVHT_877_5_Mbps;
    private Long numRxVHT_936_Mbps;
    private Long numRxVHT_975_Mbps;
    private Long numRxVHT_1040_Mbps;
    private Long numRxVHT_1053_Mbps;
    private Long numRxVHT_1053_1_Mbps;
    private Long numRxVHT_1170_Mbps;
    private Long numRxVHT_1300_Mbps;
    private Long numRxVHT_1404_Mbps;
    private Long numRxVHT_1560_Mbps;
    private Long numRxVHT_1579_5_Mbps;
    private Long numRxVHT_1733_1_Mbps;
    private Long numRxVHT_1733_4_Mbps;
    private Long numRxVHT_1755_Mbps;
    private Long numRxVHT_1872_Mbps;
    private Long numRxVHT_1950_Mbps;
    private Long numRxVHT_2080_Mbps;
    private Long numRxVHT_2106_Mbps;
    private Long numRxVHT_2340_Mbps;
    private Long numRxVHT_2600_Mbps;
    private Long numRxVHT_2808_Mbps;
    private Long numRxVHT_3120_Mbps;
    private Long numRxVHT_3466_8_Mbps;

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
    private List<McsStats> mcsStats;

    /**
     * The last RX MCS/rate index.
     */
    private McsType lastRxMcsIdx;

    /**
     * The last TX MCS/rate index.
     */
    private McsType lastTxMcsIdx;

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
        if (this.mcsStats != null) {
            ret.mcsStats = new ArrayList<>(this.mcsStats);
        }

        if(this.mcs!=null){
            ret.mcs = this.mcs.clone();
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
                && this.channelBandWidth == other.channelBandWidth && Objects.equals(ciscoLastRate, other.ciscoLastRate)
                && Objects.equals(classificationName, other.classificationName)
                && this.guardInterval == other.guardInterval && Objects.equals(lastRecvLayer3Ts, other.lastRecvLayer3Ts)
                && this.lastRxMcsIdx == other.lastRxMcsIdx && Objects.equals(lastSentLayer3Ts, other.lastSentLayer3Ts)
                && this.lastTxMcsIdx == other.lastTxMcsIdx && Arrays.equals(mcs, other.mcs)
                && Objects.equals(mcsStats, other.mcsStats) && Objects.equals(numRcvFrameForTx, other.numRcvFrameForTx)
                && Objects.equals(numRxAck, other.numRxAck) && Objects.equals(numRxBytes, other.numRxBytes)
                && Objects.equals(numRxControl, other.numRxControl) && Objects.equals(numRxCts, other.numRxCts)
                && Objects.equals(numRxData, other.numRxData) && Objects.equals(numRxDataFrames, other.numRxDataFrames)
                && Objects.equals(numRxDataFramesRetried, other.numRxDataFramesRetried)
                && Objects.equals(numRxDataFrames_108_Mbps, other.numRxDataFrames_108_Mbps)
                && Objects.equals(numRxDataFrames_12_Mbps, other.numRxDataFrames_12_Mbps)
                && Objects.equals(numRxDataFrames_1300Plus_Mbps, other.numRxDataFrames_1300Plus_Mbps)
                && Objects.equals(numRxDataFrames_1300_Mbps, other.numRxDataFrames_1300_Mbps)
                && Objects.equals(numRxDataFrames_300_Mbps, other.numRxDataFrames_300_Mbps)
                && Objects.equals(numRxDataFrames_450_Mbps, other.numRxDataFrames_450_Mbps)
                && Objects.equals(numRxDataFrames_54_Mbps, other.numRxDataFrames_54_Mbps)
                && Objects.equals(numRxDup, other.numRxDup)
                && Objects.equals(numRxFramesReceived, other.numRxFramesReceived)
                && Objects.equals(numRxHT_104_Mbps, other.numRxHT_104_Mbps)
                && Objects.equals(numRxHT_108_Mbps, other.numRxHT_108_Mbps)
                && Objects.equals(numRxHT_115_5_Mbps, other.numRxHT_115_5_Mbps)
                && Objects.equals(numRxHT_117_1_Mbps, other.numRxHT_117_1_Mbps)
                && Objects.equals(numRxHT_117_Mbps, other.numRxHT_117_Mbps)
                && Objects.equals(numRxHT_120_Mbps, other.numRxHT_120_Mbps)
                && Objects.equals(numRxHT_121_5_Mbps, other.numRxHT_121_5_Mbps)
                && Objects.equals(numRxHT_130_3_Mbps, other.numRxHT_130_3_Mbps)
                && Objects.equals(numRxHT_130_Mbps, other.numRxHT_130_Mbps)
                && Objects.equals(numRxHT_135_Mbps, other.numRxHT_135_Mbps)
                && Objects.equals(numRxHT_13_5_Mbps, other.numRxHT_13_5_Mbps)
                && Objects.equals(numRxHT_13_Mbps, other.numRxHT_13_Mbps)
                && Objects.equals(numRxHT_144_3_Mbps, other.numRxHT_144_3_Mbps)
                && Objects.equals(numRxHT_14_3_Mbps, other.numRxHT_14_3_Mbps)
                && Objects.equals(numRxHT_150_Mbps, other.numRxHT_150_Mbps)
                && Objects.equals(numRxHT_156_Mbps, other.numRxHT_156_Mbps)
                && Objects.equals(numRxHT_15_Mbps, other.numRxHT_15_Mbps)
                && Objects.equals(numRxHT_162_Mbps, other.numRxHT_162_Mbps)
                && Objects.equals(numRxHT_173_1_Mbps, other.numRxHT_173_1_Mbps)
                && Objects.equals(numRxHT_173_3_Mbps, other.numRxHT_173_3_Mbps)
                && Objects.equals(numRxHT_175_5_Mbps, other.numRxHT_175_5_Mbps)
                && Objects.equals(numRxHT_180_Mbps, other.numRxHT_180_Mbps)
                && Objects.equals(numRxHT_195_Mbps, other.numRxHT_195_Mbps)
                && Objects.equals(numRxHT_19_5_Mbps, other.numRxHT_19_5_Mbps)
                && Objects.equals(numRxHT_200_Mbps, other.numRxHT_200_Mbps)
                && Objects.equals(numRxHT_208_Mbps, other.numRxHT_208_Mbps)
                && Objects.equals(numRxHT_216_6_Mbps, other.numRxHT_216_6_Mbps)
                && Objects.equals(numRxHT_216_Mbps, other.numRxHT_216_Mbps)
                && Objects.equals(numRxHT_21_7_Mbps, other.numRxHT_21_7_Mbps)
                && Objects.equals(numRxHT_231_1_Mbps, other.numRxHT_231_1_Mbps)
                && Objects.equals(numRxHT_234_Mbps, other.numRxHT_234_Mbps)
                && Objects.equals(numRxHT_240_Mbps, other.numRxHT_240_Mbps)
                && Objects.equals(numRxHT_243_Mbps, other.numRxHT_243_Mbps)
                && Objects.equals(numRxHT_260_Mbps, other.numRxHT_260_Mbps)
                && Objects.equals(numRxHT_263_2_Mbps, other.numRxHT_263_2_Mbps)
                && Objects.equals(numRxHT_26_Mbps, other.numRxHT_26_Mbps)
                && Objects.equals(numRxHT_270_Mbps, other.numRxHT_270_Mbps)
                && Objects.equals(numRxHT_27_Mbps, other.numRxHT_27_Mbps)
                && Objects.equals(numRxHT_288_7_Mbps, other.numRxHT_288_7_Mbps)
                && Objects.equals(numRxHT_288_8_Mbps, other.numRxHT_288_8_Mbps)
                && Objects.equals(numRxHT_28_7_Mbps, other.numRxHT_28_7_Mbps)
                && Objects.equals(numRxHT_28_8_Mbps, other.numRxHT_28_8_Mbps)
                && Objects.equals(numRxHT_292_5_Mbps, other.numRxHT_292_5_Mbps)
                && Objects.equals(numRxHT_29_2_Mbps, other.numRxHT_29_2_Mbps)
                && Objects.equals(numRxHT_300_Mbps, other.numRxHT_300_Mbps)
                && Objects.equals(numRxHT_30_Mbps, other.numRxHT_30_Mbps)
                && Objects.equals(numRxHT_312_Mbps, other.numRxHT_312_Mbps)
                && Objects.equals(numRxHT_324_Mbps, other.numRxHT_324_Mbps)
                && Objects.equals(numRxHT_325_Mbps, other.numRxHT_325_Mbps)
                && Objects.equals(numRxHT_32_5_Mbps, other.numRxHT_32_5_Mbps)
                && Objects.equals(numRxHT_346_7_Mbps, other.numRxHT_346_7_Mbps)
                && Objects.equals(numRxHT_351_2_Mbps, other.numRxHT_351_2_Mbps)
                && Objects.equals(numRxHT_351_Mbps, other.numRxHT_351_Mbps)
                && Objects.equals(numRxHT_360_Mbps, other.numRxHT_360_Mbps)
                && Objects.equals(numRxHT_39_Mbps, other.numRxHT_39_Mbps)
                && Objects.equals(numRxHT_40_5_Mbps, other.numRxHT_40_5_Mbps)
                && Objects.equals(numRxHT_43_2_Mbps, other.numRxHT_43_2_Mbps)
                && Objects.equals(numRxHT_45_Mbps, other.numRxHT_45_Mbps)
                && Objects.equals(numRxHT_52_Mbps, other.numRxHT_52_Mbps)
                && Objects.equals(numRxHT_54_Mbps, other.numRxHT_54_Mbps)
                && Objects.equals(numRxHT_57_5_Mbps, other.numRxHT_57_5_Mbps)
                && Objects.equals(numRxHT_57_7_Mbps, other.numRxHT_57_7_Mbps)
                && Objects.equals(numRxHT_58_5_Mbps, other.numRxHT_58_5_Mbps)
                && Objects.equals(numRxHT_60_Mbps, other.numRxHT_60_Mbps)
                && Objects.equals(numRxHT_65_Mbps, other.numRxHT_65_Mbps)
                && Objects.equals(numRxHT_6_5_Mbps, other.numRxHT_6_5_Mbps)
                && Objects.equals(numRxHT_72_1_Mbps, other.numRxHT_72_1_Mbps)
                && Objects.equals(numRxHT_78_Mbps, other.numRxHT_78_Mbps)
                && Objects.equals(numRxHT_7_1_Mbps, other.numRxHT_7_1_Mbps)
                && Objects.equals(numRxHT_81_Mbps, other.numRxHT_81_Mbps)
                && Objects.equals(numRxHT_86_6_Mbps, other.numRxHT_86_6_Mbps)
                && Objects.equals(numRxHT_86_8_Mbps, other.numRxHT_86_8_Mbps)
                && Objects.equals(numRxHT_87_8_Mbps, other.numRxHT_87_8_Mbps)
                && Objects.equals(numRxHT_90_Mbps, other.numRxHT_90_Mbps)
                && Objects.equals(numRxHT_97_5_Mbps, other.numRxHT_97_5_Mbps)
                && Objects.equals(numRxLdpc, other.numRxLdpc) && Objects.equals(numRxManagement, other.numRxManagement)
                && Objects.equals(numRxNoFcsErr, other.numRxNoFcsErr)
                && Objects.equals(numRxNullData, other.numRxNullData)
                && Objects.equals(numRxPackets, other.numRxPackets)
                && Objects.equals(numRxProbeReq, other.numRxProbeReq) && Objects.equals(numRxPspoll, other.numRxPspoll)
                && Objects.equals(numRxRetry, other.numRxRetry) && Objects.equals(numRxRts, other.numRxRts)
                && Objects.equals(numRxStbc, other.numRxStbc) && Objects.equals(numRxTimeData, other.numRxTimeData)
                && Objects.equals(numRxTimeToMe, other.numRxTimeToMe)
                && Objects.equals(numRxVHT_1040_Mbps, other.numRxVHT_1040_Mbps)
                && Objects.equals(numRxVHT_1053_1_Mbps, other.numRxVHT_1053_1_Mbps)
                && Objects.equals(numRxVHT_1053_Mbps, other.numRxVHT_1053_Mbps)
                && Objects.equals(numRxVHT_1170_Mbps, other.numRxVHT_1170_Mbps)
                && Objects.equals(numRxVHT_1300_Mbps, other.numRxVHT_1300_Mbps)
                && Objects.equals(numRxVHT_1404_Mbps, other.numRxVHT_1404_Mbps)
                && Objects.equals(numRxVHT_1560_Mbps, other.numRxVHT_1560_Mbps)
                && Objects.equals(numRxVHT_1579_5_Mbps, other.numRxVHT_1579_5_Mbps)
                && Objects.equals(numRxVHT_1733_1_Mbps, other.numRxVHT_1733_1_Mbps)
                && Objects.equals(numRxVHT_1733_4_Mbps, other.numRxVHT_1733_4_Mbps)
                && Objects.equals(numRxVHT_1755_Mbps, other.numRxVHT_1755_Mbps)
                && Objects.equals(numRxVHT_1872_Mbps, other.numRxVHT_1872_Mbps)
                && Objects.equals(numRxVHT_1950_Mbps, other.numRxVHT_1950_Mbps)
                && Objects.equals(numRxVHT_2080_Mbps, other.numRxVHT_2080_Mbps)
                && Objects.equals(numRxVHT_2106_Mbps, other.numRxVHT_2106_Mbps)
                && Objects.equals(numRxVHT_2340_Mbps, other.numRxVHT_2340_Mbps)
                && Objects.equals(numRxVHT_2600_Mbps, other.numRxVHT_2600_Mbps)
                && Objects.equals(numRxVHT_2808_Mbps, other.numRxVHT_2808_Mbps)
                && Objects.equals(numRxVHT_292_5_Mbps, other.numRxVHT_292_5_Mbps)
                && Objects.equals(numRxVHT_3120_Mbps, other.numRxVHT_3120_Mbps)
                && Objects.equals(numRxVHT_325_Mbps, other.numRxVHT_325_Mbps)
                && Objects.equals(numRxVHT_3466_8_Mbps, other.numRxVHT_3466_8_Mbps)
                && Objects.equals(numRxVHT_364_5_Mbps, other.numRxVHT_364_5_Mbps)
                && Objects.equals(numRxVHT_390_Mbps, other.numRxVHT_390_Mbps)
                && Objects.equals(numRxVHT_400_Mbps, other.numRxVHT_400_Mbps)
                && Objects.equals(numRxVHT_403_Mbps, other.numRxVHT_403_Mbps)
                && Objects.equals(numRxVHT_405_Mbps, other.numRxVHT_405_Mbps)
                && Objects.equals(numRxVHT_432_Mbps, other.numRxVHT_432_Mbps)
                && Objects.equals(numRxVHT_433_2_Mbps, other.numRxVHT_433_2_Mbps)
                && Objects.equals(numRxVHT_450_Mbps, other.numRxVHT_450_Mbps)
                && Objects.equals(numRxVHT_468_Mbps, other.numRxVHT_468_Mbps)
                && Objects.equals(numRxVHT_480_Mbps, other.numRxVHT_480_Mbps)
                && Objects.equals(numRxVHT_486_Mbps, other.numRxVHT_486_Mbps)
                && Objects.equals(numRxVHT_520_Mbps, other.numRxVHT_520_Mbps)
                && Objects.equals(numRxVHT_526_5_Mbps, other.numRxVHT_526_5_Mbps)
                && Objects.equals(numRxVHT_540_Mbps, other.numRxVHT_540_Mbps)
                && Objects.equals(numRxVHT_585_Mbps, other.numRxVHT_585_Mbps)
                && Objects.equals(numRxVHT_600_Mbps, other.numRxVHT_600_Mbps)
                && Objects.equals(numRxVHT_648_Mbps, other.numRxVHT_648_Mbps)
                && Objects.equals(numRxVHT_650_Mbps, other.numRxVHT_650_Mbps)
                && Objects.equals(numRxVHT_702_Mbps, other.numRxVHT_702_Mbps)
                && Objects.equals(numRxVHT_720_Mbps, other.numRxVHT_720_Mbps)
                && Objects.equals(numRxVHT_780_Mbps, other.numRxVHT_780_Mbps)
                && Objects.equals(numRxVHT_800_Mbps, other.numRxVHT_800_Mbps)
                && Objects.equals(numRxVHT_866_7_Mbps, other.numRxVHT_866_7_Mbps)
                && Objects.equals(numRxVHT_877_5_Mbps, other.numRxVHT_877_5_Mbps)
                && Objects.equals(numRxVHT_936_Mbps, other.numRxVHT_936_Mbps)
                && Objects.equals(numRxVHT_975_Mbps, other.numRxVHT_975_Mbps)
                && Objects.equals(numRx_12_Mbps, other.numRx_12_Mbps)
                && Objects.equals(numRx_18_Mbps, other.numRx_18_Mbps)
                && Objects.equals(numRx_1_Mbps, other.numRx_1_Mbps)
                && Objects.equals(numRx_24_Mbps, other.numRx_24_Mbps)
                && Objects.equals(numRx_36_Mbps, other.numRx_36_Mbps)
                && Objects.equals(numRx_48_Mbps, other.numRx_48_Mbps)
                && Objects.equals(numRx_54_Mbps, other.numRx_54_Mbps)
                && Objects.equals(numRx_6_Mbps, other.numRx_6_Mbps) && Objects.equals(numRx_9_Mbps, other.numRx_9_Mbps)
                && Objects.equals(numTxAction, other.numTxAction)
                && Objects.equals(numTxAggrOneMpdu, other.numTxAggrOneMpdu)
                && Objects.equals(numTxAggrSucc, other.numTxAggrSucc)
                && Objects.equals(numTxByteSucc, other.numTxByteSucc) && Objects.equals(numTxBytes, other.numTxBytes)
                && Objects.equals(numTxControl, other.numTxControl) && Objects.equals(numTxData, other.numTxData)
                && Objects.equals(numTxDataFrames_108_Mbps, other.numTxDataFrames_108_Mbps)
                && Objects.equals(numTxDataFrames_12_Mbps, other.numTxDataFrames_12_Mbps)
                && Objects.equals(numTxDataFrames_1300Plus_Mbps, other.numTxDataFrames_1300Plus_Mbps)
                && Objects.equals(numTxDataFrames_1300_Mbps, other.numTxDataFrames_1300_Mbps)
                && Objects.equals(numTxDataFrames_300_Mbps, other.numTxDataFrames_300_Mbps)
                && Objects.equals(numTxDataFrames_450_Mbps, other.numTxDataFrames_450_Mbps)
                && Objects.equals(numTxDataFrames_54_Mbps, other.numTxDataFrames_54_Mbps)
                && Objects.equals(numTxDataRetries, other.numTxDataRetries)
                && Objects.equals(numTxDataTransmitted, other.numTxDataTransmitted)
                && Objects.equals(numTxDataTransmittedRetried, other.numTxDataTransmittedRetried)
                && Objects.equals(numTxDropped, other.numTxDropped) && Objects.equals(numTxEapol, other.numTxEapol)
                && Objects.equals(numTxFramesTransmitted, other.numTxFramesTransmitted)
                && Objects.equals(numTxHT_104_Mbps, other.numTxHT_104_Mbps)
                && Objects.equals(numTxHT_108_Mbps, other.numTxHT_108_Mbps)
                && Objects.equals(numTxHT_115_5_Mbps, other.numTxHT_115_5_Mbps)
                && Objects.equals(numTxHT_117_1_Mbps, other.numTxHT_117_1_Mbps)
                && Objects.equals(numTxHT_117_Mbps, other.numTxHT_117_Mbps)
                && Objects.equals(numTxHT_120_Mbps, other.numTxHT_120_Mbps)
                && Objects.equals(numTxHT_121_5_Mbps, other.numTxHT_121_5_Mbps)
                && Objects.equals(numTxHT_130_3_Mbps, other.numTxHT_130_3_Mbps)
                && Objects.equals(numTxHT_130_Mbps, other.numTxHT_130_Mbps)
                && Objects.equals(numTxHT_135_Mbps, other.numTxHT_135_Mbps)
                && Objects.equals(numTxHT_13_5_Mbps, other.numTxHT_13_5_Mbps)
                && Objects.equals(numTxHT_13_Mbps, other.numTxHT_13_Mbps)
                && Objects.equals(numTxHT_144_3_Mbps, other.numTxHT_144_3_Mbps)
                && Objects.equals(numTxHT_14_3_Mbps, other.numTxHT_14_3_Mbps)
                && Objects.equals(numTxHT_150_Mbps, other.numTxHT_150_Mbps)
                && Objects.equals(numTxHT_156_Mbps, other.numTxHT_156_Mbps)
                && Objects.equals(numTxHT_15_Mbps, other.numTxHT_15_Mbps)
                && Objects.equals(numTxHT_162_Mbps, other.numTxHT_162_Mbps)
                && Objects.equals(numTxHT_173_1_Mbps, other.numTxHT_173_1_Mbps)
                && Objects.equals(numTxHT_173_3_Mbps, other.numTxHT_173_3_Mbps)
                && Objects.equals(numTxHT_175_5_Mbps, other.numTxHT_175_5_Mbps)
                && Objects.equals(numTxHT_180_Mbps, other.numTxHT_180_Mbps)
                && Objects.equals(numTxHT_195_Mbps, other.numTxHT_195_Mbps)
                && Objects.equals(numTxHT_19_5_Mbps, other.numTxHT_19_5_Mbps)
                && Objects.equals(numTxHT_200_Mbps, other.numTxHT_200_Mbps)
                && Objects.equals(numTxHT_208_Mbps, other.numTxHT_208_Mbps)
                && Objects.equals(numTxHT_216_6_Mbps, other.numTxHT_216_6_Mbps)
                && Objects.equals(numTxHT_216_Mbps, other.numTxHT_216_Mbps)
                && Objects.equals(numTxHT_21_7_Mbps, other.numTxHT_21_7_Mbps)
                && Objects.equals(numTxHT_231_1_Mbps, other.numTxHT_231_1_Mbps)
                && Objects.equals(numTxHT_234_Mbps, other.numTxHT_234_Mbps)
                && Objects.equals(numTxHT_240_Mbps, other.numTxHT_240_Mbps)
                && Objects.equals(numTxHT_243_Mbps, other.numTxHT_243_Mbps)
                && Objects.equals(numTxHT_260_Mbps, other.numTxHT_260_Mbps)
                && Objects.equals(numTxHT_263_2_Mbps, other.numTxHT_263_2_Mbps)
                && Objects.equals(numTxHT_26_Mbps, other.numTxHT_26_Mbps)
                && Objects.equals(numTxHT_270_Mbps, other.numTxHT_270_Mbps)
                && Objects.equals(numTxHT_27_Mbps, other.numTxHT_27_Mbps)
                && Objects.equals(numTxHT_288_7_Mbps, other.numTxHT_288_7_Mbps)
                && Objects.equals(numTxHT_288_8_Mbps, other.numTxHT_288_8_Mbps)
                && Objects.equals(numTxHT_28_7_Mbps, other.numTxHT_28_7_Mbps)
                && Objects.equals(numTxHT_28_8_Mbps, other.numTxHT_28_8_Mbps)
                && Objects.equals(numTxHT_292_5_Mbps, other.numTxHT_292_5_Mbps)
                && Objects.equals(numTxHT_29_2_Mbps, other.numTxHT_29_2_Mbps)
                && Objects.equals(numTxHT_300_Mbps, other.numTxHT_300_Mbps)
                && Objects.equals(numTxHT_30_Mbps, other.numTxHT_30_Mbps)
                && Objects.equals(numTxHT_312_Mbps, other.numTxHT_312_Mbps)
                && Objects.equals(numTxHT_324_Mbps, other.numTxHT_324_Mbps)
                && Objects.equals(numTxHT_325_Mbps, other.numTxHT_325_Mbps)
                && Objects.equals(numTxHT_32_5_Mbps, other.numTxHT_32_5_Mbps)
                && Objects.equals(numTxHT_346_7_Mbps, other.numTxHT_346_7_Mbps)
                && Objects.equals(numTxHT_351_2_Mbps, other.numTxHT_351_2_Mbps)
                && Objects.equals(numTxHT_351_Mbps, other.numTxHT_351_Mbps)
                && Objects.equals(numTxHT_360_Mbps, other.numTxHT_360_Mbps)
                && Objects.equals(numTxHT_39_Mbps, other.numTxHT_39_Mbps)
                && Objects.equals(numTxHT_40_5_Mbps, other.numTxHT_40_5_Mbps)
                && Objects.equals(numTxHT_43_2_Mbps, other.numTxHT_43_2_Mbps)
                && Objects.equals(numTxHT_45_Mbps, other.numTxHT_45_Mbps)
                && Objects.equals(numTxHT_52_Mbps, other.numTxHT_52_Mbps)
                && Objects.equals(numTxHT_54_Mbps, other.numTxHT_54_Mbps)
                && Objects.equals(numTxHT_57_5_Mbps, other.numTxHT_57_5_Mbps)
                && Objects.equals(numTxHT_57_7_Mbps, other.numTxHT_57_7_Mbps)
                && Objects.equals(numTxHT_58_5_Mbps, other.numTxHT_58_5_Mbps)
                && Objects.equals(numTxHT_60_Mbps, other.numTxHT_60_Mbps)
                && Objects.equals(numTxHT_65_Mbps, other.numTxHT_65_Mbps)
                && Objects.equals(numTxHT_6_5_Mbps, other.numTxHT_6_5_Mbps)
                && Objects.equals(numTxHT_72_1_Mbps, other.numTxHT_72_1_Mbps)
                && Objects.equals(numTxHT_78_Mbps, other.numTxHT_78_Mbps)
                && Objects.equals(numTxHT_7_1_Mbps, other.numTxHT_7_1_Mbps)
                && Objects.equals(numTxHT_81_Mbps, other.numTxHT_81_Mbps)
                && Objects.equals(numTxHT_86_6_Mbps, other.numTxHT_86_6_Mbps)
                && Objects.equals(numTxHT_86_8_Mbps, other.numTxHT_86_8_Mbps)
                && Objects.equals(numTxHT_87_8_Mbps, other.numTxHT_87_8_Mbps)
                && Objects.equals(numTxHT_90_Mbps, other.numTxHT_90_Mbps)
                && Objects.equals(numTxHT_97_5_Mbps, other.numTxHT_97_5_Mbps)
                && Objects.equals(numTxLdpc, other.numTxLdpc) && Objects.equals(numTxManagement, other.numTxManagement)
                && Objects.equals(numTxMultiRetries, other.numTxMultiRetries)
                && Objects.equals(numTxMultipleRetries, other.numTxMultipleRetries)
                && Objects.equals(numTxNoAck, other.numTxNoAck) && Objects.equals(numTxPackets, other.numTxPackets)
                && Objects.equals(numTxPropResp, other.numTxPropResp) && Objects.equals(numTxQueued, other.numTxQueued)
                && Objects.equals(numTxRetryDropped, other.numTxRetryDropped)
                && Objects.equals(numTxRtsFail, other.numTxRtsFail) && Objects.equals(numTxRtsSucc, other.numTxRtsSucc)
                && Objects.equals(numTxStbc, other.numTxStbc) && Objects.equals(numTxSucc, other.numTxSucc)
                && Objects.equals(numTxSuccNoRetry, other.numTxSuccNoRetry)
                && Objects.equals(numTxSuccRetries, other.numTxSuccRetries)
                && Objects.equals(numTxSuccessWithRetry, other.numTxSuccessWithRetry)
                && Objects.equals(numTxTimeData, other.numTxTimeData)
                && Objects.equals(numTxTimeFramesTransmitted, other.numTxTimeFramesTransmitted)
                && Objects.equals(numTxVHT_1040_Mbps, other.numTxVHT_1040_Mbps)
                && Objects.equals(numTxVHT_1053_1_Mbps, other.numTxVHT_1053_1_Mbps)
                && Objects.equals(numTxVHT_1053_Mbps, other.numTxVHT_1053_Mbps)
                && Objects.equals(numTxVHT_1170_Mbps, other.numTxVHT_1170_Mbps)
                && Objects.equals(numTxVHT_1300_Mbps, other.numTxVHT_1300_Mbps)
                && Objects.equals(numTxVHT_1404_Mbps, other.numTxVHT_1404_Mbps)
                && Objects.equals(numTxVHT_1560_Mbps, other.numTxVHT_1560_Mbps)
                && Objects.equals(numTxVHT_1579_5_Mbps, other.numTxVHT_1579_5_Mbps)
                && Objects.equals(numTxVHT_1733_1_Mbps, other.numTxVHT_1733_1_Mbps)
                && Objects.equals(numTxVHT_1733_4_Mbps, other.numTxVHT_1733_4_Mbps)
                && Objects.equals(numTxVHT_1755_Mbps, other.numTxVHT_1755_Mbps)
                && Objects.equals(numTxVHT_1872_Mbps, other.numTxVHT_1872_Mbps)
                && Objects.equals(numTxVHT_1950_Mbps, other.numTxVHT_1950_Mbps)
                && Objects.equals(numTxVHT_2080_Mbps, other.numTxVHT_2080_Mbps)
                && Objects.equals(numTxVHT_2106_Mbps, other.numTxVHT_2106_Mbps)
                && Objects.equals(numTxVHT_2340_Mbps, other.numTxVHT_2340_Mbps)
                && Objects.equals(numTxVHT_2600_Mbps, other.numTxVHT_2600_Mbps)
                && Objects.equals(numTxVHT_2808_Mbps, other.numTxVHT_2808_Mbps)
                && Objects.equals(numTxVHT_292_5_Mbps, other.numTxVHT_292_5_Mbps)
                && Objects.equals(numTxVHT_3120_Mbps, other.numTxVHT_3120_Mbps)
                && Objects.equals(numTxVHT_325_Mbps, other.numTxVHT_325_Mbps)
                && Objects.equals(numTxVHT_3466_8_Mbps, other.numTxVHT_3466_8_Mbps)
                && Objects.equals(numTxVHT_364_5_Mbps, other.numTxVHT_364_5_Mbps)
                && Objects.equals(numTxVHT_390_Mbps, other.numTxVHT_390_Mbps)
                && Objects.equals(numTxVHT_400_Mbps, other.numTxVHT_400_Mbps)
                && Objects.equals(numTxVHT_403_Mbps, other.numTxVHT_403_Mbps)
                && Objects.equals(numTxVHT_405_Mbps, other.numTxVHT_405_Mbps)
                && Objects.equals(numTxVHT_432_Mbps, other.numTxVHT_432_Mbps)
                && Objects.equals(numTxVHT_433_2_Mbps, other.numTxVHT_433_2_Mbps)
                && Objects.equals(numTxVHT_450_Mbps, other.numTxVHT_450_Mbps)
                && Objects.equals(numTxVHT_468_Mbps, other.numTxVHT_468_Mbps)
                && Objects.equals(numTxVHT_480_Mbps, other.numTxVHT_480_Mbps)
                && Objects.equals(numTxVHT_486_Mbps, other.numTxVHT_486_Mbps)
                && Objects.equals(numTxVHT_520_Mbps, other.numTxVHT_520_Mbps)
                && Objects.equals(numTxVHT_526_5_Mbps, other.numTxVHT_526_5_Mbps)
                && Objects.equals(numTxVHT_540_Mbps, other.numTxVHT_540_Mbps)
                && Objects.equals(numTxVHT_585_Mbps, other.numTxVHT_585_Mbps)
                && Objects.equals(numTxVHT_600_Mbps, other.numTxVHT_600_Mbps)
                && Objects.equals(numTxVHT_648_Mbps, other.numTxVHT_648_Mbps)
                && Objects.equals(numTxVHT_650_Mbps, other.numTxVHT_650_Mbps)
                && Objects.equals(numTxVHT_702_Mbps, other.numTxVHT_702_Mbps)
                && Objects.equals(numTxVHT_720_Mbps, other.numTxVHT_720_Mbps)
                && Objects.equals(numTxVHT_780_Mbps, other.numTxVHT_780_Mbps)
                && Objects.equals(numTxVHT_800_Mbps, other.numTxVHT_800_Mbps)
                && Objects.equals(numTxVHT_866_7_Mbps, other.numTxVHT_866_7_Mbps)
                && Objects.equals(numTxVHT_877_5_Mbps, other.numTxVHT_877_5_Mbps)
                && Objects.equals(numTxVHT_936_Mbps, other.numTxVHT_936_Mbps)
                && Objects.equals(numTxVHT_975_Mbps, other.numTxVHT_975_Mbps)
                && Objects.equals(numTx_12_Mbps, other.numTx_12_Mbps)
                && Objects.equals(numTx_18_Mbps, other.numTx_18_Mbps)
                && Objects.equals(numTx_1_Mbps, other.numTx_1_Mbps)
                && Objects.equals(numTx_24_Mbps, other.numTx_24_Mbps)
                && Objects.equals(numTx_36_Mbps, other.numTx_36_Mbps)
                && Objects.equals(numTx_48_Mbps, other.numTx_48_Mbps)
                && Objects.equals(numTx_54_Mbps, other.numTx_54_Mbps)
                && Objects.equals(numTx_6_Mbps, other.numTx_6_Mbps) && Objects.equals(numTx_9_Mbps, other.numTx_9_Mbps)
                && this.radioType == other.radioType && Objects.equals(rateCount, other.rateCount)
                && Arrays.equals(rates, other.rates) && Objects.equals(rssi, other.rssi)
                && Objects.equals(rxBytes, other.rxBytes) && Objects.equals(rxDataBytes, other.rxDataBytes)
                && Objects.equals(rxDuplicatePackets, other.rxDuplicatePackets)
                && Objects.equals(rxLastRssi, other.rxLastRssi) && Objects.equals(sessionId, other.sessionId)
                && Objects.equals(snr, other.snr) && Objects.equals(txRetries, other.txRetries)
                && Objects.equals(vhtMcs, other.vhtMcs) && Objects.equals(wmmQueueStats, other.wmmQueueStats);
    }

    public Long getLastRecvLayer3Ts() {
        return lastRecvLayer3Ts;
    }

    public McsType getLastRxMcsIdx() {
        return lastRxMcsIdx;
    }

    public Long getLastSentLayer3Ts() {
        return lastSentLayer3Ts;
    }

    public McsType getLastTxMcsIdx() {
        return lastTxMcsIdx;
    }

    public int[] getMcs() {
        return mcs;
    }

    public List<McsStats> getMcsStats() 
    {
        // We need to populate the McsStats
        if(mcsStats != null)
        {
            for(McsStats stats : mcsStats)
            {
                if(stats.getRate() == null && stats.getMcsNum() != null)
                {
                    stats.setRate(getLastPhyRateKb(stats.getMcsNum()));
                }
            }
        }

        return mcsStats;
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

    public Integer getRateCount() {
        return rateCount;
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

    public String getSessionId() {
        return sessionId;
    }

    public Integer getSnr() {
        return snr;
    }

    public Integer getCiscoLastRate() {
        return ciscoLastRate;
    }

    public Integer getTxRetries() {
        return txRetries;
    }

    public Integer getVhtMcs() {
        return vhtMcs;
    }

    public Map<WmmQueueType, WmmQueueStats> getWmmQueueStats() {
        return wmmQueueStats;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Arrays.hashCode(this.mcs);
        result = prime * result + Arrays.hashCode(this.rates);
        result = prime * result + Objects.hash(averageRxRate, averageTxRate, channelBandWidth, ciscoLastRate,
                classificationName, guardInterval, lastRecvLayer3Ts, lastRxMcsIdx, lastSentLayer3Ts, lastTxMcsIdx,
                mcsStats, numRcvFrameForTx, numRxAck, numRxBytes, numRxControl, numRxCts, numRxData, numRxDataFrames,
                numRxDataFramesRetried, numRxDataFrames_108_Mbps, numRxDataFrames_12_Mbps,
                numRxDataFrames_1300Plus_Mbps, numRxDataFrames_1300_Mbps, numRxDataFrames_300_Mbps,
                numRxDataFrames_450_Mbps, numRxDataFrames_54_Mbps, numRxDup, numRxFramesReceived, numRxHT_104_Mbps,
                numRxHT_108_Mbps, numRxHT_115_5_Mbps, numRxHT_117_1_Mbps, numRxHT_117_Mbps, numRxHT_120_Mbps,
                numRxHT_121_5_Mbps, numRxHT_130_3_Mbps, numRxHT_130_Mbps, numRxHT_135_Mbps, numRxHT_13_5_Mbps,
                numRxHT_13_Mbps, numRxHT_144_3_Mbps, numRxHT_14_3_Mbps, numRxHT_150_Mbps, numRxHT_156_Mbps,
                numRxHT_15_Mbps, numRxHT_162_Mbps, numRxHT_173_1_Mbps, numRxHT_173_3_Mbps, numRxHT_175_5_Mbps,
                numRxHT_180_Mbps, numRxHT_195_Mbps, numRxHT_19_5_Mbps, numRxHT_200_Mbps, numRxHT_208_Mbps,
                numRxHT_216_6_Mbps, numRxHT_216_Mbps, numRxHT_21_7_Mbps, numRxHT_231_1_Mbps, numRxHT_234_Mbps,
                numRxHT_240_Mbps, numRxHT_243_Mbps, numRxHT_260_Mbps, numRxHT_263_2_Mbps, numRxHT_26_Mbps,
                numRxHT_270_Mbps, numRxHT_27_Mbps, numRxHT_288_7_Mbps, numRxHT_288_8_Mbps, numRxHT_28_7_Mbps,
                numRxHT_28_8_Mbps, numRxHT_292_5_Mbps, numRxHT_29_2_Mbps, numRxHT_300_Mbps, numRxHT_30_Mbps,
                numRxHT_312_Mbps, numRxHT_324_Mbps, numRxHT_325_Mbps, numRxHT_32_5_Mbps, numRxHT_346_7_Mbps,
                numRxHT_351_2_Mbps, numRxHT_351_Mbps, numRxHT_360_Mbps, numRxHT_39_Mbps, numRxHT_40_5_Mbps,
                numRxHT_43_2_Mbps, numRxHT_45_Mbps, numRxHT_52_Mbps, numRxHT_54_Mbps, numRxHT_57_5_Mbps,
                numRxHT_57_7_Mbps, numRxHT_58_5_Mbps, numRxHT_60_Mbps, numRxHT_65_Mbps, numRxHT_6_5_Mbps,
                numRxHT_72_1_Mbps, numRxHT_78_Mbps, numRxHT_7_1_Mbps, numRxHT_81_Mbps, numRxHT_86_6_Mbps,
                numRxHT_86_8_Mbps, numRxHT_87_8_Mbps, numRxHT_90_Mbps, numRxHT_97_5_Mbps, numRxLdpc, numRxManagement,
                numRxNoFcsErr, numRxNullData, numRxPackets, numRxProbeReq, numRxPspoll, numRxRetry, numRxRts, numRxStbc,
                numRxTimeData, numRxTimeToMe, numRxVHT_1040_Mbps, numRxVHT_1053_1_Mbps, numRxVHT_1053_Mbps,
                numRxVHT_1170_Mbps, numRxVHT_1300_Mbps, numRxVHT_1404_Mbps, numRxVHT_1560_Mbps, numRxVHT_1579_5_Mbps,
                numRxVHT_1733_1_Mbps, numRxVHT_1733_4_Mbps, numRxVHT_1755_Mbps, numRxVHT_1872_Mbps, numRxVHT_1950_Mbps,
                numRxVHT_2080_Mbps, numRxVHT_2106_Mbps, numRxVHT_2340_Mbps, numRxVHT_2600_Mbps, numRxVHT_2808_Mbps,
                numRxVHT_292_5_Mbps, numRxVHT_3120_Mbps, numRxVHT_325_Mbps, numRxVHT_3466_8_Mbps, numRxVHT_364_5_Mbps,
                numRxVHT_390_Mbps, numRxVHT_400_Mbps, numRxVHT_403_Mbps, numRxVHT_405_Mbps, numRxVHT_432_Mbps,
                numRxVHT_433_2_Mbps, numRxVHT_450_Mbps, numRxVHT_468_Mbps, numRxVHT_480_Mbps, numRxVHT_486_Mbps,
                numRxVHT_520_Mbps, numRxVHT_526_5_Mbps, numRxVHT_540_Mbps, numRxVHT_585_Mbps, numRxVHT_600_Mbps,
                numRxVHT_648_Mbps, numRxVHT_650_Mbps, numRxVHT_702_Mbps, numRxVHT_720_Mbps, numRxVHT_780_Mbps,
                numRxVHT_800_Mbps, numRxVHT_866_7_Mbps, numRxVHT_877_5_Mbps, numRxVHT_936_Mbps, numRxVHT_975_Mbps,
                numRx_12_Mbps, numRx_18_Mbps, numRx_1_Mbps, numRx_24_Mbps, numRx_36_Mbps, numRx_48_Mbps, numRx_54_Mbps,
                numRx_6_Mbps, numRx_9_Mbps, numTxAction, numTxAggrOneMpdu, numTxAggrSucc, numTxByteSucc, numTxBytes,
                numTxControl, numTxData, numTxDataFrames_108_Mbps, numTxDataFrames_12_Mbps,
                numTxDataFrames_1300Plus_Mbps, numTxDataFrames_1300_Mbps, numTxDataFrames_300_Mbps,
                numTxDataFrames_450_Mbps, numTxDataFrames_54_Mbps, numTxDataRetries, numTxDataTransmitted,
                numTxDataTransmittedRetried, numTxDropped, numTxEapol, numTxFramesTransmitted, numTxHT_104_Mbps,
                numTxHT_108_Mbps, numTxHT_115_5_Mbps, numTxHT_117_1_Mbps, numTxHT_117_Mbps, numTxHT_120_Mbps,
                numTxHT_121_5_Mbps, numTxHT_130_3_Mbps, numTxHT_130_Mbps, numTxHT_135_Mbps, numTxHT_13_5_Mbps,
                numTxHT_13_Mbps, numTxHT_144_3_Mbps, numTxHT_14_3_Mbps, numTxHT_150_Mbps, numTxHT_156_Mbps,
                numTxHT_15_Mbps, numTxHT_162_Mbps, numTxHT_173_1_Mbps, numTxHT_173_3_Mbps, numTxHT_175_5_Mbps,
                numTxHT_180_Mbps, numTxHT_195_Mbps, numTxHT_19_5_Mbps, numTxHT_200_Mbps, numTxHT_208_Mbps,
                numTxHT_216_6_Mbps, numTxHT_216_Mbps, numTxHT_21_7_Mbps, numTxHT_231_1_Mbps, numTxHT_234_Mbps,
                numTxHT_240_Mbps, numTxHT_243_Mbps, numTxHT_260_Mbps, numTxHT_263_2_Mbps, numTxHT_26_Mbps,
                numTxHT_270_Mbps, numTxHT_27_Mbps, numTxHT_288_7_Mbps, numTxHT_288_8_Mbps, numTxHT_28_7_Mbps,
                numTxHT_28_8_Mbps, numTxHT_292_5_Mbps, numTxHT_29_2_Mbps, numTxHT_300_Mbps, numTxHT_30_Mbps,
                numTxHT_312_Mbps, numTxHT_324_Mbps, numTxHT_325_Mbps, numTxHT_32_5_Mbps, numTxHT_346_7_Mbps,
                numTxHT_351_2_Mbps, numTxHT_351_Mbps, numTxHT_360_Mbps, numTxHT_39_Mbps, numTxHT_40_5_Mbps,
                numTxHT_43_2_Mbps, numTxHT_45_Mbps, numTxHT_52_Mbps, numTxHT_54_Mbps, numTxHT_57_5_Mbps,
                numTxHT_57_7_Mbps, numTxHT_58_5_Mbps, numTxHT_60_Mbps, numTxHT_65_Mbps, numTxHT_6_5_Mbps,
                numTxHT_72_1_Mbps, numTxHT_78_Mbps, numTxHT_7_1_Mbps, numTxHT_81_Mbps, numTxHT_86_6_Mbps,
                numTxHT_86_8_Mbps, numTxHT_87_8_Mbps, numTxHT_90_Mbps, numTxHT_97_5_Mbps, numTxLdpc, numTxManagement,
                numTxMultiRetries, numTxMultipleRetries, numTxNoAck, numTxPackets, numTxPropResp, numTxQueued,
                numTxRetryDropped, numTxRtsFail, numTxRtsSucc, numTxStbc, numTxSucc, numTxSuccNoRetry, numTxSuccRetries,
                numTxSuccessWithRetry, numTxTimeData, numTxTimeFramesTransmitted, numTxVHT_1040_Mbps,
                numTxVHT_1053_1_Mbps, numTxVHT_1053_Mbps, numTxVHT_1170_Mbps, numTxVHT_1300_Mbps, numTxVHT_1404_Mbps,
                numTxVHT_1560_Mbps, numTxVHT_1579_5_Mbps, numTxVHT_1733_1_Mbps, numTxVHT_1733_4_Mbps,
                numTxVHT_1755_Mbps, numTxVHT_1872_Mbps, numTxVHT_1950_Mbps, numTxVHT_2080_Mbps, numTxVHT_2106_Mbps,
                numTxVHT_2340_Mbps, numTxVHT_2600_Mbps, numTxVHT_2808_Mbps, numTxVHT_292_5_Mbps, numTxVHT_3120_Mbps,
                numTxVHT_325_Mbps, numTxVHT_3466_8_Mbps, numTxVHT_364_5_Mbps, numTxVHT_390_Mbps, numTxVHT_400_Mbps,
                numTxVHT_403_Mbps, numTxVHT_405_Mbps, numTxVHT_432_Mbps, numTxVHT_433_2_Mbps, numTxVHT_450_Mbps,
                numTxVHT_468_Mbps, numTxVHT_480_Mbps, numTxVHT_486_Mbps, numTxVHT_520_Mbps, numTxVHT_526_5_Mbps,
                numTxVHT_540_Mbps, numTxVHT_585_Mbps, numTxVHT_600_Mbps, numTxVHT_648_Mbps, numTxVHT_650_Mbps,
                numTxVHT_702_Mbps, numTxVHT_720_Mbps, numTxVHT_780_Mbps, numTxVHT_800_Mbps, numTxVHT_866_7_Mbps,
                numTxVHT_877_5_Mbps, numTxVHT_936_Mbps, numTxVHT_975_Mbps, numTx_12_Mbps, numTx_18_Mbps, numTx_1_Mbps,
                numTx_24_Mbps, numTx_36_Mbps, numTx_48_Mbps, numTx_54_Mbps, numTx_6_Mbps, numTx_9_Mbps, radioType,
                rateCount, rssi, rxBytes, rxDataBytes, rxDuplicatePackets, rxLastRssi, sessionId, snr, txRetries,
                vhtMcs, wmmQueueStats);
        return result;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (McsType.isUnsupported(lastRxMcsIdx) || McsType.isUnsupported(lastTxMcsIdx)
         || RadioType.isUnsupported(radioType) || hasUnsupportedValue(mcsStats)
         || (ChannelBandwidth.isUnsupported(this.channelBandWidth)) || (GuardInterval.isUnsupported(this.guardInterval))) {
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

    public void setLastRxMcsIdx(McsType lastRxMcsIdx) {
        this.lastRxMcsIdx = lastRxMcsIdx;
    }

    public void setLastSentLayer3Ts(Long lastSentLayer3Ts) {
        this.lastSentLayer3Ts = lastSentLayer3Ts;
    }

    public void setLastTxMcsIdx(McsType lastTxMcsIdx) {
        this.lastTxMcsIdx = lastTxMcsIdx;
    }


    public void setMcs(int[] mcs) {
        this.mcs = mcs;
    }

    public void setMcsStats(List<McsStats> mcsStats) {
        this.mcsStats = mcsStats;
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

    public void setRateCount(Integer rateCount) {
        this.rateCount = rateCount;
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

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public void setSnr(Integer snr) {
        this.snr = snr;
    }

    public void setCiscoLastRate(Integer rate) {
        this.ciscoLastRate = rate;
    }

    public void setTxRetries(Integer txRetries) {
        this.txRetries = txRetries;
    }

    public void setVhtMcs(Integer vhtMcs) {
        this.vhtMcs = vhtMcs;
    }

    public void setWmmQueueStats(Map<WmmQueueType, WmmQueueStats> wmmQueueStats) {
        this.wmmQueueStats = wmmQueueStats;
    }

    /**
     * Get the last Rx physical rate based on the McsType, Channel Bandwidth and
     * GuardInterval. Result is in kilobit per second.
     * 
     * <b>1 megabit per second = 1000 kilobit per second.</b>
     * 
     * @return physical rate in Kilobit per Second. Return null if information is not available.
     */
    public Long getLastRxPhyRateKb() {
        if((this.ciscoLastRate != null) && (this.ciscoLastRate != 0))
            return 1000 * (this.ciscoLastRate).longValue();

        return getLastPhyRateKb(this.lastRxMcsIdx);
    }

    /**
     * Get the last Tx physical rate based on the McsType, Channel Bandwidth and
     * GuardInterval. Result is in kilobit per second.
     * 
     * <b>1 megabit per second = 1000 kilobit per second.</b>
     * 
     * @return physical rate in Kilobit per Second. Return null if information is not available.
     */
    public Long getLastTxPhyRateKb() {
        if((this.ciscoLastRate != null) && (this.ciscoLastRate != 0))
            return 1000 * (this.ciscoLastRate).longValue();

        return getLastPhyRateKb(this.lastTxMcsIdx);
    }
    
    public Long getLastPhyRateKb(McsType lastMcsIdx) {
        if (lastMcsIdx == null) {
            return null;
        }
        // assume SGI, channel bandwidth based on radio type
        ChannelBandwidth bw = ClientRadioUtils.getDefaultChannelBandwidth(radioType);
        GuardInterval gi = GuardInterval.SGI;
        
        if (this.channelBandWidth != null && this.guardInterval != null) {
            bw = this.channelBandWidth;
            gi = this.guardInterval;
        }
        return ClientRadioUtils.getPhyRateKb(lastMcsIdx, bw, gi);
    }

    public Long getNumTxDataFrames_12_Mbps() {
        return numTxDataFrames_12_Mbps;
    }

    public void setNumTxDataFrames_12_Mbps(Long numTxDataFrames_12_Mbps) {
        if (numTxDataFrames_12_Mbps != null && numTxDataFrames_12_Mbps == 0) {
            this.numTxDataFrames_12_Mbps = null;
        } else {
            this.numTxDataFrames_12_Mbps = numTxDataFrames_12_Mbps;
        }
    }

    public Long getNumTxDataFrames_54_Mbps() {
        return numTxDataFrames_54_Mbps;
    }

    public void setNumTxDataFrames_54_Mbps(Long numTxDataFrames_54_Mbps) {
        if (numTxDataFrames_54_Mbps != null && numTxDataFrames_54_Mbps == 0) {
            this.numTxDataFrames_54_Mbps = null;
        } else {
            this.numTxDataFrames_54_Mbps = numTxDataFrames_54_Mbps;
        }
    }

    public Long getNumTxDataFrames_108_Mbps() {
        return numTxDataFrames_108_Mbps;
    }

    public void setNumTxDataFrames_108_Mbps(Long numTxDataFrames_108_Mbps) {
        if (numTxDataFrames_108_Mbps != null && numTxDataFrames_108_Mbps == 0) {
            this.numTxDataFrames_108_Mbps = null;
        } else {
            this.numTxDataFrames_108_Mbps = numTxDataFrames_108_Mbps;
        }
    }

    public Long getNumTxDataFrames_300_Mbps() {
        return numTxDataFrames_300_Mbps;
    }

    public void setNumTxDataFrames_300_Mbps(Long numTxDataFrames_300_Mbps) {
        if (numTxDataFrames_300_Mbps != null && numTxDataFrames_300_Mbps == 0) {
            this.numTxDataFrames_300_Mbps = null;
        } else {
            this.numTxDataFrames_300_Mbps = numTxDataFrames_300_Mbps;
        }
    }

    public Long getNumTxDataFrames_450_Mbps() {
        return numTxDataFrames_450_Mbps;
    }

    public void setNumTxDataFrames_450_Mbps(Long numTxDataFrames_450_Mbps) {
        if (numTxDataFrames_450_Mbps != null && numTxDataFrames_450_Mbps == 0) {
            this.numTxDataFrames_450_Mbps = null;
        } else {
            this.numTxDataFrames_450_Mbps = numTxDataFrames_450_Mbps;
        }
    }

    public Long getNumTxDataFrames_1300_Mbps() {
        return numTxDataFrames_1300_Mbps;
    }

    public void setNumTxDataFrames_1300_Mbps(Long numTxDataFrames_1300_Mbps) {
        if (numTxDataFrames_1300_Mbps != null && numTxDataFrames_1300_Mbps == 0) {
            this.numTxDataFrames_1300_Mbps = null;
        } else {
            this.numTxDataFrames_1300_Mbps = numTxDataFrames_1300_Mbps;
        }
    }

    public Long getNumTxDataFrames_1300Plus_Mbps() {
        return numTxDataFrames_1300Plus_Mbps;
    }

    public void setNumTxDataFrames_1300Plus_Mbps(Long numTxDataFrames_1300Plus_Mbps) {
        if (numTxDataFrames_1300Plus_Mbps != null && numTxDataFrames_1300Plus_Mbps == 0) {
            numTxDataFrames_1300Plus_Mbps = null;
        } else {
            this.numTxDataFrames_1300Plus_Mbps = numTxDataFrames_1300Plus_Mbps;
        }
    }

    public Long getNumRxDataFrames_12_Mbps() {
        return numRxDataFrames_12_Mbps;
    }

    public void setNumRxDataFrames_12_Mbps(Long numRxDataFrames_12_Mbps) {
        if (numRxDataFrames_12_Mbps != null && numRxDataFrames_12_Mbps == 0) {
            this.numRxDataFrames_12_Mbps = null;
        } else {
            this.numRxDataFrames_12_Mbps = numRxDataFrames_12_Mbps;
        }
    }

    public Long getNumRxDataFrames_54_Mbps() {
        return numRxDataFrames_54_Mbps;
    }

    public void setNumRxDataFrames_54_Mbps(Long numRxDataFrames_54_Mbps) {
        if (numRxDataFrames_54_Mbps != null && numRxDataFrames_54_Mbps == 0) {
            this.numRxDataFrames_54_Mbps = null;
        } else {
            this.numRxDataFrames_54_Mbps = numRxDataFrames_54_Mbps;
        }
    }

    public Long getNumRxDataFrames_108_Mbps() {
        return numRxDataFrames_108_Mbps;
    }

    public void setNumRxDataFrames_108_Mbps(Long numRxDataFrames_108_Mbps) {
        if (numRxDataFrames_108_Mbps != null && numRxDataFrames_108_Mbps == 0) {
            this.numRxDataFrames_108_Mbps = null;
        } else {
            this.numRxDataFrames_108_Mbps = numRxDataFrames_108_Mbps;
        }
    }

    public Long getNumRxDataFrames_300_Mbps() {
        return numRxDataFrames_300_Mbps;
    }

    public void setNumRxDataFrames_300_Mbps(Long numRxDataFrames_300_Mbps) {
        if (numRxDataFrames_300_Mbps != null && numRxDataFrames_300_Mbps == 0) {
            this.numRxDataFrames_300_Mbps = null;
        } else {
            this.numRxDataFrames_300_Mbps = numRxDataFrames_300_Mbps;
        }
    }

    public Long getNumRxDataFrames_450_Mbps() {
        return numRxDataFrames_450_Mbps;
    }

    public void setNumRxDataFrames_450_Mbps(Long numRxDataFrames_450_Mbps) {
        if (numRxDataFrames_450_Mbps != null && numRxDataFrames_450_Mbps == 0) {
            this.numRxDataFrames_450_Mbps = null;
        } else {
            this.numRxDataFrames_450_Mbps = numRxDataFrames_450_Mbps;
        }
    }

    public Long getNumRxDataFrames_1300_Mbps() {
        return numRxDataFrames_1300_Mbps;
    }

    public void setNumRxDataFrames_1300_Mbps(Long numRxDataFrames_1300_Mbps) {
        if (numRxDataFrames_1300_Mbps != null && numRxDataFrames_1300_Mbps == 0) {
            this.numRxDataFrames_1300_Mbps = null;
        } else {
            this.numRxDataFrames_1300_Mbps = numRxDataFrames_1300_Mbps;
        }
    }

    public Long getNumRxDataFrames_1300Plus_Mbps() {
        return numRxDataFrames_1300Plus_Mbps;
    }

    public void setNumRxDataFrames_1300Plus_Mbps(Long numRxDataFrames_1300Plus_Mbps) {
        if (numRxDataFrames_1300Plus_Mbps != null && numRxDataFrames_1300Plus_Mbps == 0) {
            this.numRxDataFrames_1300Plus_Mbps = null;
        } else {
            this.numRxDataFrames_1300Plus_Mbps = numRxDataFrames_1300Plus_Mbps;
        }
    }

    public Long getNumTxTimeFramesTransmitted() {
        return numTxTimeFramesTransmitted;
    }

    public void setNumTxTimeFramesTransmitted(Long numTxTimeFramesTransmitted) {
        this.numTxTimeFramesTransmitted = numTxTimeFramesTransmitted;
    }

    public Long getNumRxTimeToMe() {
        return numRxTimeToMe;
    }

    public void setNumRxTimeToMe(Long numRxTimeToMe) {
        this.numRxTimeToMe = numRxTimeToMe;
    }

    public Long getNumTx_6_Mbps() {
        return numTx_6_Mbps;
    }

    public void setNumTx_6_Mbps(Long numTx_6_Mbps) {
        this.numTx_6_Mbps = numTx_6_Mbps;
    }

    public Long getNumTxHT_260_Mbps() {
        return numTxHT_260_Mbps;
    }

    public void setNumTxHT_260_Mbps(Long numTxHT_260_Mbps) {
        this.numTxHT_260_Mbps = numTxHT_260_Mbps;
    }

    public Long getNumTxVHT_390_Mbps() {
        return numTxVHT_390_Mbps;
    }

    public void setNumTxVHT_390_Mbps(Long numTxVHT_390_Mbps) {
        this.numTxVHT_390_Mbps = numTxVHT_390_Mbps;
    }

    public Long getNumTxVHT_520_Mbps() {
        return numTxVHT_520_Mbps;
    }

    public void setNumTxVHT_520_Mbps(Long numTxVHT_520_Mbps) {
        this.numTxVHT_520_Mbps = numTxVHT_520_Mbps;
    }

    public Long getNumTxVHT_585_Mbps() {
        return numTxVHT_585_Mbps;
    }

    public void setNumTxVHT_585_Mbps(Long numTxVHT_585_Mbps) {
        this.numTxVHT_585_Mbps = numTxVHT_585_Mbps;
    }

    public Long getNumTxVHT_650_Mbps() {
        return numTxVHT_650_Mbps;
    }

    public void setNumTxVHT_650_Mbps(Long numTxVHT_650_Mbps) {
        this.numTxVHT_650_Mbps = numTxVHT_650_Mbps;
    }

    public Long getNumTxVHT_780_Mbps() {
        return numTxVHT_780_Mbps;
    }

    public void setNumTxVHT_780_Mbps(Long numTxVHT_780_Mbps) {
        this.numTxVHT_780_Mbps = numTxVHT_780_Mbps;
    }

    public Long getNumTxVHT_866_7_Mbps() {
        return numTxVHT_866_7_Mbps;
    }

    public void setNumTxVHT_866_7_Mbps(Long numTxVHT_866_7_Mbps) {
        this.numTxVHT_866_7_Mbps = numTxVHT_866_7_Mbps;
    }

    public Long getNumRxVHT_468_Mbps() {
        return numRxVHT_468_Mbps;
    }

    public void setNumRxVHT_468_Mbps(Long numRxVHT_468_Mbps) {
        this.numRxVHT_468_Mbps = numRxVHT_468_Mbps;
    }

    public Long getNumRxVHT_526_5_Mbps() {
        return numRxVHT_526_5_Mbps;
    }

    public void setNumRxVHT_526_5_Mbps(Long numRxVHT_526_5_Mbps) {
        this.numRxVHT_526_5_Mbps = numRxVHT_526_5_Mbps;
    }

    public Long getNumRxVHT_585_Mbps() {
        return numRxVHT_585_Mbps;
    }

    public void setNumRxVHT_585_Mbps(Long numRxVHT_585_Mbps) {
        this.numRxVHT_585_Mbps = numRxVHT_585_Mbps;
    }

    public Long getNumRxVHT_780_Mbps() {
        return numRxVHT_780_Mbps;
    }

    public void setNumRxVHT_780_Mbps(Long numRxVHT_780_Mbps) {
        this.numRxVHT_780_Mbps = numRxVHT_780_Mbps;
    }

    public Long getNumRxVHT_866_7_Mbps() {
        return numRxVHT_866_7_Mbps;
    }

    public void setNumRxVHT_866_7_Mbps(Long numRxVHT_866_7_Mbps) {
        this.numRxVHT_866_7_Mbps = numRxVHT_866_7_Mbps;
    }

    public Long getNumTxTimeData() {
        return numTxTimeData;
    }

    public void setNumTxTimeData(Long numTxTimeData) {
        this.numTxTimeData = numTxTimeData;
    }

    public Long getNumRxTimeData() {
        return numRxTimeData;
    }

    public void setNumRxTimeData(Long numRxTimeData) {
        this.numRxTimeData = numRxTimeData;
    }

    public Long getNumTxFramesTransmitted() {
        return numTxFramesTransmitted;
    }

    public void setNumTxFramesTransmitted(Long numTxFramesTransmitted) {
        this.numTxFramesTransmitted = numTxFramesTransmitted;
    }

    public Long getNumTxSuccessWithRetry() {
        return numTxSuccessWithRetry;
    }

    public void setNumTxSuccessWithRetry(Long numTxSuccessWithRetry) {
        this.numTxSuccessWithRetry = numTxSuccessWithRetry;
    }

    public Long getNumTxMultipleRetries() {
        return numTxMultipleRetries;
    }

    public void setNumTxMultipleRetries(Long numTxMultipleRetries) {
        this.numTxMultipleRetries = numTxMultipleRetries;
    }

    public Long getNumTxDataTransmittedRetried() {
        return numTxDataTransmittedRetried;
    }

    public void setNumTxDataTransmittedRetried(Long numTxDataTransmittedRetried) {
        this.numTxDataTransmittedRetried = numTxDataTransmittedRetried;
    }

    public Long getNumTxDataTransmitted() {
        return numTxDataTransmitted;
    }

    public void setNumTxDataTransmitted(Long numTxDataTransmitted) {
        this.numTxDataTransmitted = numTxDataTransmitted;
    }

    public Long getNumRxFramesReceived() {
        return numRxFramesReceived;
    }

    public void setNumRxFramesReceived(Long numRxFramesReceived) {
        this.numRxFramesReceived = numRxFramesReceived;
    }

    public Long getNumRxDataFramesRetried() {
        return numRxDataFramesRetried;
    }

    public void setNumRxDataFramesRetried(Long numRxDataFramesRetried) {
        this.numRxDataFramesRetried = numRxDataFramesRetried;
    }

    public Long getNumRxDataFrames() {
        return numRxDataFrames;
    }

    public void setNumRxDataFrames(Long numRxDataFrames) {
        this.numRxDataFrames = numRxDataFrames;
    }

    public Long getNumTx_1_Mbps() {
        return numTx_1_Mbps;
    }

    public void setNumTx_1_Mbps(Long numTx_1_Mbps) {
        this.numTx_1_Mbps = numTx_1_Mbps;
    }

    public Long getNumTx_9_Mbps() {
        return numTx_9_Mbps;
    }

    public void setNumTx_9_Mbps(Long numTx_9_Mbps) {
        this.numTx_9_Mbps = numTx_9_Mbps;
    }

    public Long getNumTx_12_Mbps() {
        return numTx_12_Mbps;
    }

    public void setNumTx_12_Mbps(Long numTx_12_Mbps) {
        this.numTx_12_Mbps = numTx_12_Mbps;
    }

    public Long getNumTx_18_Mbps() {
        return numTx_18_Mbps;
    }

    public void setNumTx_18_Mbps(Long numTx_18_Mbps) {
        this.numTx_18_Mbps = numTx_18_Mbps;
    }

    public Long getNumTx_24_Mbps() {
        return numTx_24_Mbps;
    }

    public void setNumTx_24_Mbps(Long numTx_24_Mbps) {
        this.numTx_24_Mbps = numTx_24_Mbps;
    }

    public Long getNumTx_36_Mbps() {
        return numTx_36_Mbps;
    }

    public void setNumTx_36_Mbps(Long numTx_36_Mbps) {
        this.numTx_36_Mbps = numTx_36_Mbps;
    }

    public Long getNumTx_48_Mbps() {
        return numTx_48_Mbps;
    }

    public void setNumTx_48_Mbps(Long numTx_48_Mbps) {
        this.numTx_48_Mbps = numTx_48_Mbps;
    }

    public Long getNumTx_54_Mbps() {
        return numTx_54_Mbps;
    }

    public void setNumTx_54_Mbps(Long numTx_54_Mbps) {
        this.numTx_54_Mbps = numTx_54_Mbps;
    }

    public Long getNumRx_1_Mbps() {
        return numRx_1_Mbps;
    }

    public void setNumRx_1_Mbps(Long numRx_1_Mbps) {
        this.numRx_1_Mbps = numRx_1_Mbps;
    }

    public Long getNumRx_6_Mbps() {
        return numRx_6_Mbps;
    }

    public void setNumRx_6_Mbps(Long numRx_6_Mbps) {
        this.numRx_6_Mbps = numRx_6_Mbps;
    }

    public Long getNumRx_9_Mbps() {
        return numRx_9_Mbps;
    }

    public void setNumRx_9_Mbps(Long numRx_9_Mbps) {
        this.numRx_9_Mbps = numRx_9_Mbps;
    }

    public Long getNumRx_12_Mbps() {
        return numRx_12_Mbps;
    }

    public void setNumRx_12_Mbps(Long numRx_12_Mbps) {
        this.numRx_12_Mbps = numRx_12_Mbps;
    }

    public Long getNumRx_18_Mbps() {
        return numRx_18_Mbps;
    }

    public void setNumRx_18_Mbps(Long numRx_18_Mbps) {
        this.numRx_18_Mbps = numRx_18_Mbps;
    }

    public Long getNumRx_24_Mbps() {
        return numRx_24_Mbps;
    }

    public void setNumRx_24_Mbps(Long numRx_24_Mbps) {
        this.numRx_24_Mbps = numRx_24_Mbps;
    }

    public Long getNumRx_36_Mbps() {
        return numRx_36_Mbps;
    }

    public void setNumRx_36_Mbps(Long numRx_36_Mbps) {
        this.numRx_36_Mbps = numRx_36_Mbps;
    }

    public Long getNumRx_48_Mbps() {
        return numRx_48_Mbps;
    }

    public void setNumRx_48_Mbps(Long numRx_48_Mbps) {
        this.numRx_48_Mbps = numRx_48_Mbps;
    }

    public Long getNumRx_54_Mbps() {
        return numRx_54_Mbps;
    }

    public void setNumRx_54_Mbps(Long numRx_54_Mbps) {
        this.numRx_54_Mbps = numRx_54_Mbps;
    }

    public Long getNumTxHT_6_5_Mbps() {
        return numTxHT_6_5_Mbps;
    }

    public void setNumTxHT_6_5_Mbps(Long numTxHT_6_5_Mbps) {
        this.numTxHT_6_5_Mbps = numTxHT_6_5_Mbps;
    }

    public Long getNumTxHT_7_1_Mbps() {
        return numTxHT_7_1_Mbps;
    }

    public void setNumTxHT_7_1_Mbps(Long numTxHT_7_1_Mbps) {
        this.numTxHT_7_1_Mbps = numTxHT_7_1_Mbps;
    }

    public Long getNumTxHT_13_Mbps() {
        return numTxHT_13_Mbps;
    }

    public void setNumTxHT_13_Mbps(Long numTxHT_13_Mbps) {
        this.numTxHT_13_Mbps = numTxHT_13_Mbps;
    }

    public Long getNumTxHT_13_5_Mbps() {
        return numTxHT_13_5_Mbps;
    }

    public void setNumTxHT_13_5_Mbps(Long numTxHT_13_5_Mbps) {
        this.numTxHT_13_5_Mbps = numTxHT_13_5_Mbps;
    }

    public Long getNumTxHT_14_3_Mbps() {
        return numTxHT_14_3_Mbps;
    }

    public void setNumTxHT_14_3_Mbps(Long numTxHT_14_3_Mbps) {
        this.numTxHT_14_3_Mbps = numTxHT_14_3_Mbps;
    }

    public Long getNumTxHT_15_Mbps() {
        return numTxHT_15_Mbps;
    }

    public void setNumTxHT_15_Mbps(Long numTxHT_15_Mbps) {
        this.numTxHT_15_Mbps = numTxHT_15_Mbps;
    }

    public Long getNumTxHT_19_5_Mbps() {
        return numTxHT_19_5_Mbps;
    }

    public void setNumTxHT_19_5_Mbps(Long numTxHT_19_5_Mbps) {
        this.numTxHT_19_5_Mbps = numTxHT_19_5_Mbps;
    }

    public Long getNumTxHT_21_7_Mbps() {
        return numTxHT_21_7_Mbps;
    }

    public void setNumTxHT_21_7_Mbps(Long numTxHT_21_7_Mbps) {
        this.numTxHT_21_7_Mbps = numTxHT_21_7_Mbps;
    }

    public Long getNumTxHT_26_Mbps() {
        return numTxHT_26_Mbps;
    }

    public void setNumTxHT_26_Mbps(Long numTxHT_26_Mbps) {
        this.numTxHT_26_Mbps = numTxHT_26_Mbps;
    }

    public Long getNumTxHT_27_Mbps() {
        return numTxHT_27_Mbps;
    }

    public void setNumTxHT_27_Mbps(Long numTxHT_27_Mbps) {
        this.numTxHT_27_Mbps = numTxHT_27_Mbps;
    }

    public Long getNumTxHT_28_7_Mbps() {
        return numTxHT_28_7_Mbps;
    }

    public void setNumTxHT_28_7_Mbps(Long numTxHT_28_7_Mbps) {
        this.numTxHT_28_7_Mbps = numTxHT_28_7_Mbps;
    }

    public Long getNumTxHT_28_8_Mbps() {
        return numTxHT_28_8_Mbps;
    }

    public void setNumTxHT_28_8_Mbps(Long numTxHT_28_8_Mbps) {
        this.numTxHT_28_8_Mbps = numTxHT_28_8_Mbps;
    }

    public Long getNumTxHT_29_2_Mbps() {
        return numTxHT_29_2_Mbps;
    }

    public void setNumTxHT_29_2_Mbps(Long numTxHT_29_2_Mbps) {
        this.numTxHT_29_2_Mbps = numTxHT_29_2_Mbps;
    }

    public Long getNumTxHT_30_Mbps() {
        return numTxHT_30_Mbps;
    }

    public void setNumTxHT_30_Mbps(Long numTxHT_30_Mbps) {
        this.numTxHT_30_Mbps = numTxHT_30_Mbps;
    }

    public Long getNumTxHT_32_5_Mbps() {
        return numTxHT_32_5_Mbps;
    }

    public void setNumTxHT_32_5_Mbps(Long numTxHT_32_5_Mbps) {
        this.numTxHT_32_5_Mbps = numTxHT_32_5_Mbps;
    }

    public Long getNumTxHT_39_Mbps() {
        return numTxHT_39_Mbps;
    }

    public void setNumTxHT_39_Mbps(Long numTxHT_39_Mbps) {
        this.numTxHT_39_Mbps = numTxHT_39_Mbps;
    }

    public Long getNumTxHT_40_5_Mbps() {
        return numTxHT_40_5_Mbps;
    }

    public void setNumTxHT_40_5_Mbps(Long numTxHT_40_5_Mbps) {
        this.numTxHT_40_5_Mbps = numTxHT_40_5_Mbps;
    }

    public Long getNumTxHT_43_2_Mbps() {
        return numTxHT_43_2_Mbps;
    }

    public void setNumTxHT_43_2_Mbps(Long numTxHT_43_2_Mbps) {
        this.numTxHT_43_2_Mbps = numTxHT_43_2_Mbps;
    }

    public Long getNumTxHT_45_Mbps() {
        return numTxHT_45_Mbps;
    }

    public void setNumTxHT_45_Mbps(Long numTxHT_45_Mbps) {
        this.numTxHT_45_Mbps = numTxHT_45_Mbps;
    }

    public Long getNumTxHT_52_Mbps() {
        return numTxHT_52_Mbps;
    }

    public void setNumTxHT_52_Mbps(Long numTxHT_52_Mbps) {
        this.numTxHT_52_Mbps = numTxHT_52_Mbps;
    }

    public Long getNumTxHT_54_Mbps() {
        return numTxHT_54_Mbps;
    }

    public void setNumTxHT_54_Mbps(Long numTxHT_54_Mbps) {
        this.numTxHT_54_Mbps = numTxHT_54_Mbps;
    }

    public Long getNumTxHT_57_5_Mbps() {
        return numTxHT_57_5_Mbps;
    }

    public void setNumTxHT_57_5_Mbps(Long numTxHT_57_5_Mbps) {
        this.numTxHT_57_5_Mbps = numTxHT_57_5_Mbps;
    }

    public Long getNumTxHT_57_7_Mbps() {
        return numTxHT_57_7_Mbps;
    }

    public void setNumTxHT_57_7_Mbps(Long numTxHT_57_7_Mbps) {
        this.numTxHT_57_7_Mbps = numTxHT_57_7_Mbps;
    }

    public Long getNumTxHT_58_5_Mbps() {
        return numTxHT_58_5_Mbps;
    }

    public void setNumTxHT_58_5_Mbps(Long numTxHT_58_5_Mbps) {
        this.numTxHT_58_5_Mbps = numTxHT_58_5_Mbps;
    }

    public Long getNumTxHT_60_Mbps() {
        return numTxHT_60_Mbps;
    }

    public void setNumTxHT_60_Mbps(Long numTxHT_60_Mbps) {
        this.numTxHT_60_Mbps = numTxHT_60_Mbps;
    }

    public Long getNumTxHT_65_Mbps() {
        return numTxHT_65_Mbps;
    }

    public void setNumTxHT_65_Mbps(Long numTxHT_65_Mbps) {
        this.numTxHT_65_Mbps = numTxHT_65_Mbps;
    }

    public Long getNumTxHT_72_1_Mbps() {
        return numTxHT_72_1_Mbps;
    }

    public void setNumTxHT_72_1_Mbps(Long numTxHT_72_1_Mbps) {
        this.numTxHT_72_1_Mbps = numTxHT_72_1_Mbps;
    }

    public Long getNumTxHT_78_Mbps() {
        return numTxHT_78_Mbps;
    }

    public void setNumTxHT_78_Mbps(Long numTxHT_78_Mbps) {
        this.numTxHT_78_Mbps = numTxHT_78_Mbps;
    }

    public Long getNumTxHT_81_Mbps() {
        return numTxHT_81_Mbps;
    }

    public void setNumTxHT_81_Mbps(Long numTxHT_81_Mbps) {
        this.numTxHT_81_Mbps = numTxHT_81_Mbps;
    }

    public Long getNumTxHT_86_6_Mbps() {
        return numTxHT_86_6_Mbps;
    }

    public void setNumTxHT_86_6_Mbps(Long numTxHT_86_6_Mbps) {
        this.numTxHT_86_6_Mbps = numTxHT_86_6_Mbps;
    }

    public Long getNumTxHT_86_8_Mbps() {
        return numTxHT_86_8_Mbps;
    }

    public void setNumTxHT_86_8_Mbps(Long numTxHT_86_8_Mbps) {
        this.numTxHT_86_8_Mbps = numTxHT_86_8_Mbps;
    }

    public Long getNumTxHT_87_8_Mbps() {
        return numTxHT_87_8_Mbps;
    }

    public void setNumTxHT_87_8_Mbps(Long numTxHT_87_8_Mbps) {
        this.numTxHT_87_8_Mbps = numTxHT_87_8_Mbps;
    }

    public Long getNumTxHT_90_Mbps() {
        return numTxHT_90_Mbps;
    }

    public void setNumTxHT_90_Mbps(Long numTxHT_90_Mbps) {
        this.numTxHT_90_Mbps = numTxHT_90_Mbps;
    }

    public Long getNumTxHT_97_5_Mbps() {
        return numTxHT_97_5_Mbps;
    }

    public void setNumTxHT_97_5_Mbps(Long numTxHT_97_5_Mbps) {
        this.numTxHT_97_5_Mbps = numTxHT_97_5_Mbps;
    }

    public Long getNumTxHT_104_Mbps() {
        return numTxHT_104_Mbps;
    }

    public void setNumTxHT_104_Mbps(Long numTxHT_104_Mbps) {
        this.numTxHT_104_Mbps = numTxHT_104_Mbps;
    }

    public Long getNumTxHT_108_Mbps() {
        return numTxHT_108_Mbps;
    }

    public void setNumTxHT_108_Mbps(Long numTxHT_108_Mbps) {
        this.numTxHT_108_Mbps = numTxHT_108_Mbps;
    }

    public Long getNumTxHT_115_5_Mbps() {
        return numTxHT_115_5_Mbps;
    }

    public void setNumTxHT_115_5_Mbps(Long numTxHT_115_5_Mbps) {
        this.numTxHT_115_5_Mbps = numTxHT_115_5_Mbps;
    }

    public Long getNumTxHT_117_Mbps() {
        return numTxHT_117_Mbps;
    }

    public void setNumTxHT_117_Mbps(Long numTxHT_117_Mbps) {
        this.numTxHT_117_Mbps = numTxHT_117_Mbps;
    }

    public Long getNumTxHT_117_1_Mbps() {
        return numTxHT_117_1_Mbps;
    }

    public void setNumTxHT_117_1_Mbps(Long numTxHT_117_1_Mbps) {
        this.numTxHT_117_1_Mbps = numTxHT_117_1_Mbps;
    }

    public Long getNumTxHT_120_Mbps() {
        return numTxHT_120_Mbps;
    }

    public void setNumTxHT_120_Mbps(Long numTxHT_120_Mbps) {
        this.numTxHT_120_Mbps = numTxHT_120_Mbps;
    }

    public Long getNumTxHT_121_5_Mbps() {
        return numTxHT_121_5_Mbps;
    }

    public void setNumTxHT_121_5_Mbps(Long numTxHT_121_5_Mbps) {
        this.numTxHT_121_5_Mbps = numTxHT_121_5_Mbps;
    }

    public Long getNumTxHT_130_Mbps() {
        return numTxHT_130_Mbps;
    }

    public void setNumTxHT_130_Mbps(Long numTxHT_130_Mbps) {
        this.numTxHT_130_Mbps = numTxHT_130_Mbps;
    }

    public Long getNumTxHT_130_3_Mbps() {
        return numTxHT_130_3_Mbps;
    }

    public void setNumTxHT_130_3_Mbps(Long numTxHT_130_3_Mbps) {
        this.numTxHT_130_3_Mbps = numTxHT_130_3_Mbps;
    }

    public Long getNumTxHT_135_Mbps() {
        return numTxHT_135_Mbps;
    }

    public void setNumTxHT_135_Mbps(Long numTxHT_135_Mbps) {
        this.numTxHT_135_Mbps = numTxHT_135_Mbps;
    }

    public Long getNumTxHT_144_3_Mbps() {
        return numTxHT_144_3_Mbps;
    }

    public void setNumTxHT_144_3_Mbps(Long numTxHT_144_3_Mbps) {
        this.numTxHT_144_3_Mbps = numTxHT_144_3_Mbps;
    }

    public Long getNumTxHT_150_Mbps() {
        return numTxHT_150_Mbps;
    }

    public void setNumTxHT_150_Mbps(Long numTxHT_150_Mbps) {
        this.numTxHT_150_Mbps = numTxHT_150_Mbps;
    }

    public Long getNumTxHT_156_Mbps() {
        return numTxHT_156_Mbps;
    }

    public void setNumTxHT_156_Mbps(Long numTxHT_156_Mbps) {
        this.numTxHT_156_Mbps = numTxHT_156_Mbps;
    }

    public Long getNumTxHT_162_Mbps() {
        return numTxHT_162_Mbps;
    }

    public void setNumTxHT_162_Mbps(Long numTxHT_162_Mbps) {
        this.numTxHT_162_Mbps = numTxHT_162_Mbps;
    }

    public Long getNumTxHT_173_1_Mbps() {
        return numTxHT_173_1_Mbps;
    }

    public void setNumTxHT_173_1_Mbps(Long numTxHT_173_1_Mbps) {
        this.numTxHT_173_1_Mbps = numTxHT_173_1_Mbps;
    }

    public Long getNumTxHT_173_3_Mbps() {
        return numTxHT_173_3_Mbps;
    }

    public void setNumTxHT_173_3_Mbps(Long numTxHT_173_3_Mbps) {
        this.numTxHT_173_3_Mbps = numTxHT_173_3_Mbps;
    }

    public Long getNumTxHT_175_5_Mbps() {
        return numTxHT_175_5_Mbps;
    }

    public void setNumTxHT_175_5_Mbps(Long numTxHT_175_5_Mbps) {
        this.numTxHT_175_5_Mbps = numTxHT_175_5_Mbps;
    }

    public Long getNumTxHT_180_Mbps() {
        return numTxHT_180_Mbps;
    }

    public void setNumTxHT_180_Mbps(Long numTxHT_180_Mbps) {
        this.numTxHT_180_Mbps = numTxHT_180_Mbps;
    }

    public Long getNumTxHT_195_Mbps() {
        return numTxHT_195_Mbps;
    }

    public void setNumTxHT_195_Mbps(Long numTxHT_195_Mbps) {
        this.numTxHT_195_Mbps = numTxHT_195_Mbps;
    }

    public Long getNumTxHT_200_Mbps() {
        return numTxHT_200_Mbps;
    }

    public void setNumTxHT_200_Mbps(Long numTxHT_200_Mbps) {
        this.numTxHT_200_Mbps = numTxHT_200_Mbps;
    }

    public Long getNumTxHT_208_Mbps() {
        return numTxHT_208_Mbps;
    }

    public void setNumTxHT_208_Mbps(Long numTxHT_208_Mbps) {
        this.numTxHT_208_Mbps = numTxHT_208_Mbps;
    }

    public Long getNumTxHT_216_Mbps() {
        return numTxHT_216_Mbps;
    }

    public void setNumTxHT_216_Mbps(Long numTxHT_216_Mbps) {
        this.numTxHT_216_Mbps = numTxHT_216_Mbps;
    }

    public Long getNumTxHT_216_6_Mbps() {
        return numTxHT_216_6_Mbps;
    }

    public void setNumTxHT_216_6_Mbps(Long numTxHT_216_6_Mbps) {
        this.numTxHT_216_6_Mbps = numTxHT_216_6_Mbps;
    }

    public Long getNumTxHT_231_1_Mbps() {
        return numTxHT_231_1_Mbps;
    }

    public void setNumTxHT_231_1_Mbps(Long numTxHT_231_1_Mbps) {
        this.numTxHT_231_1_Mbps = numTxHT_231_1_Mbps;
    }

    public Long getNumTxHT_234_Mbps() {
        return numTxHT_234_Mbps;
    }

    public void setNumTxHT_234_Mbps(Long numTxHT_234_Mbps) {
        this.numTxHT_234_Mbps = numTxHT_234_Mbps;
    }

    public Long getNumTxHT_240_Mbps() {
        return numTxHT_240_Mbps;
    }

    public void setNumTxHT_240_Mbps(Long numTxHT_240_Mbps) {
        this.numTxHT_240_Mbps = numTxHT_240_Mbps;
    }

    public Long getNumTxHT_243_Mbps() {
        return numTxHT_243_Mbps;
    }

    public void setNumTxHT_243_Mbps(Long numTxHT_243_Mbps) {
        this.numTxHT_243_Mbps = numTxHT_243_Mbps;
    }

    public Long getNumTxHT_263_2_Mbps() {
        return numTxHT_263_2_Mbps;
    }

    public void setNumTxHT_263_2_Mbps(Long numTxHT_263_2_Mbps) {
        this.numTxHT_263_2_Mbps = numTxHT_263_2_Mbps;
    }

    public Long getNumTxHT_270_Mbps() {
        return numTxHT_270_Mbps;
    }

    public void setNumTxHT_270_Mbps(Long numTxHT_270_Mbps) {
        this.numTxHT_270_Mbps = numTxHT_270_Mbps;
    }

    public Long getNumTxHT_288_7_Mbps() {
        return numTxHT_288_7_Mbps;
    }

    public void setNumTxHT_288_7_Mbps(Long numTxHT_288_7_Mbps) {
        this.numTxHT_288_7_Mbps = numTxHT_288_7_Mbps;
    }

    public Long getNumTxHT_288_8_Mbps() {
        return numTxHT_288_8_Mbps;
    }

    public void setNumTxHT_288_8_Mbps(Long numTxHT_288_8_Mbps) {
        this.numTxHT_288_8_Mbps = numTxHT_288_8_Mbps;
    }

    public Long getNumTxHT_292_5_Mbps() {
        return numTxHT_292_5_Mbps;
    }

    public void setNumTxHT_292_5_Mbps(Long numTxHT_292_5_Mbps) {
        this.numTxHT_292_5_Mbps = numTxHT_292_5_Mbps;
    }

    public Long getNumTxHT_300_Mbps() {
        return numTxHT_300_Mbps;
    }

    public void setNumTxHT_300_Mbps(Long numTxHT_300_Mbps) {
        this.numTxHT_300_Mbps = numTxHT_300_Mbps;
    }

    public Long getNumTxHT_312_Mbps() {
        return numTxHT_312_Mbps;
    }

    public void setNumTxHT_312_Mbps(Long numTxHT_312_Mbps) {
        this.numTxHT_312_Mbps = numTxHT_312_Mbps;
    }

    public Long getNumTxHT_324_Mbps() {
        return numTxHT_324_Mbps;
    }

    public void setNumTxHT_324_Mbps(Long numTxHT_324_Mbps) {
        this.numTxHT_324_Mbps = numTxHT_324_Mbps;
    }

    public Long getNumTxHT_325_Mbps() {
        return numTxHT_325_Mbps;
    }

    public void setNumTxHT_325_Mbps(Long numTxHT_325_Mbps) {
        this.numTxHT_325_Mbps = numTxHT_325_Mbps;
    }

    public Long getNumTxHT_346_7_Mbps() {
        return numTxHT_346_7_Mbps;
    }

    public void setNumTxHT_346_7_Mbps(Long numTxHT_346_7_Mbps) {
        this.numTxHT_346_7_Mbps = numTxHT_346_7_Mbps;
    }

    public Long getNumTxHT_351_Mbps() {
        return numTxHT_351_Mbps;
    }

    public void setNumTxHT_351_Mbps(Long numTxHT_351_Mbps) {
        this.numTxHT_351_Mbps = numTxHT_351_Mbps;
    }

    public Long getNumTxHT_351_2_Mbps() {
        return numTxHT_351_2_Mbps;
    }

    public void setNumTxHT_351_2_Mbps(Long numTxHT_351_2_Mbps) {
        this.numTxHT_351_2_Mbps = numTxHT_351_2_Mbps;
    }

    public Long getNumTxHT_360_Mbps() {
        return numTxHT_360_Mbps;
    }

    public void setNumTxHT_360_Mbps(Long numTxHT_360_Mbps) {
        this.numTxHT_360_Mbps = numTxHT_360_Mbps;
    }

    public Long getNumRxHT_6_5_Mbps() {
        return numRxHT_6_5_Mbps;
    }

    public void setNumRxHT_6_5_Mbps(Long numRxHT_6_5_Mbps) {
        this.numRxHT_6_5_Mbps = numRxHT_6_5_Mbps;
    }

    public Long getNumRxHT_7_1_Mbps() {
        return numRxHT_7_1_Mbps;
    }

    public void setNumRxHT_7_1_Mbps(Long numRxHT_7_1_Mbps) {
        this.numRxHT_7_1_Mbps = numRxHT_7_1_Mbps;
    }

    public Long getNumRxHT_13_Mbps() {
        return numRxHT_13_Mbps;
    }

    public void setNumRxHT_13_Mbps(Long numRxHT_13_Mbps) {
        this.numRxHT_13_Mbps = numRxHT_13_Mbps;
    }

    public Long getNumRxHT_13_5_Mbps() {
        return numRxHT_13_5_Mbps;
    }

    public void setNumRxHT_13_5_Mbps(Long numRxHT_13_5_Mbps) {
        this.numRxHT_13_5_Mbps = numRxHT_13_5_Mbps;
    }

    public Long getNumRxHT_14_3_Mbps() {
        return numRxHT_14_3_Mbps;
    }

    public void setNumRxHT_14_3_Mbps(Long numRxHT_14_3_Mbps) {
        this.numRxHT_14_3_Mbps = numRxHT_14_3_Mbps;
    }

    public Long getNumRxHT_15_Mbps() {
        return numRxHT_15_Mbps;
    }

    public void setNumRxHT_15_Mbps(Long numRxHT_15_Mbps) {
        this.numRxHT_15_Mbps = numRxHT_15_Mbps;
    }

    public Long getNumRxHT_19_5_Mbps() {
        return numRxHT_19_5_Mbps;
    }

    public void setNumRxHT_19_5_Mbps(Long numRxHT_19_5_Mbps) {
        this.numRxHT_19_5_Mbps = numRxHT_19_5_Mbps;
    }

    public Long getNumRxHT_21_7_Mbps() {
        return numRxHT_21_7_Mbps;
    }

    public void setNumRxHT_21_7_Mbps(Long numRxHT_21_7_Mbps) {
        this.numRxHT_21_7_Mbps = numRxHT_21_7_Mbps;
    }

    public Long getNumRxHT_26_Mbps() {
        return numRxHT_26_Mbps;
    }

    public void setNumRxHT_26_Mbps(Long numRxHT_26_Mbps) {
        this.numRxHT_26_Mbps = numRxHT_26_Mbps;
    }

    public Long getNumRxHT_27_Mbps() {
        return numRxHT_27_Mbps;
    }

    public void setNumRxHT_27_Mbps(Long numRxHT_27_Mbps) {
        this.numRxHT_27_Mbps = numRxHT_27_Mbps;
    }

    public Long getNumRxHT_28_7_Mbps() {
        return numRxHT_28_7_Mbps;
    }

    public void setNumRxHT_28_7_Mbps(Long numRxHT_28_7_Mbps) {
        this.numRxHT_28_7_Mbps = numRxHT_28_7_Mbps;
    }

    public Long getNumRxHT_28_8_Mbps() {
        return numRxHT_28_8_Mbps;
    }

    public void setNumRxHT_28_8_Mbps(Long numRxHT_28_8_Mbps) {
        this.numRxHT_28_8_Mbps = numRxHT_28_8_Mbps;
    }

    public Long getNumRxHT_29_2_Mbps() {
        return numRxHT_29_2_Mbps;
    }

    public void setNumRxHT_29_2_Mbps(Long numRxHT_29_2_Mbps) {
        this.numRxHT_29_2_Mbps = numRxHT_29_2_Mbps;
    }

    public Long getNumRxHT_30_Mbps() {
        return numRxHT_30_Mbps;
    }

    public void setNumRxHT_30_Mbps(Long numRxHT_30_Mbps) {
        this.numRxHT_30_Mbps = numRxHT_30_Mbps;
    }

    public Long getNumRxHT_32_5_Mbps() {
        return numRxHT_32_5_Mbps;
    }

    public void setNumRxHT_32_5_Mbps(Long numRxHT_32_5_Mbps) {
        this.numRxHT_32_5_Mbps = numRxHT_32_5_Mbps;
    }

    public Long getNumRxHT_39_Mbps() {
        return numRxHT_39_Mbps;
    }

    public void setNumRxHT_39_Mbps(Long numRxHT_39_Mbps) {
        this.numRxHT_39_Mbps = numRxHT_39_Mbps;
    }

    public Long getNumRxHT_40_5_Mbps() {
        return numRxHT_40_5_Mbps;
    }

    public void setNumRxHT_40_5_Mbps(Long numRxHT_40_5_Mbps) {
        this.numRxHT_40_5_Mbps = numRxHT_40_5_Mbps;
    }

    public Long getNumRxHT_43_2_Mbps() {
        return numRxHT_43_2_Mbps;
    }

    public void setNumRxHT_43_2_Mbps(Long numRxHT_43_2_Mbps) {
        this.numRxHT_43_2_Mbps = numRxHT_43_2_Mbps;
    }

    public Long getNumRxHT_45_Mbps() {
        return numRxHT_45_Mbps;
    }

    public void setNumRxHT_45_Mbps(Long numRxHT_45_Mbps) {
        this.numRxHT_45_Mbps = numRxHT_45_Mbps;
    }

    public Long getNumRxHT_52_Mbps() {
        return numRxHT_52_Mbps;
    }

    public void setNumRxHT_52_Mbps(Long numRxHT_52_Mbps) {
        this.numRxHT_52_Mbps = numRxHT_52_Mbps;
    }

    public Long getNumRxHT_54_Mbps() {
        return numRxHT_54_Mbps;
    }

    public void setNumRxHT_54_Mbps(Long numRxHT_54_Mbps) {
        this.numRxHT_54_Mbps = numRxHT_54_Mbps;
    }

    public Long getNumRxHT_57_5_Mbps() {
        return numRxHT_57_5_Mbps;
    }

    public void setNumRxHT_57_5_Mbps(Long numRxHT_57_5_Mbps) {
        this.numRxHT_57_5_Mbps = numRxHT_57_5_Mbps;
    }

    public Long getNumRxHT_57_7_Mbps() {
        return numRxHT_57_7_Mbps;
    }

    public void setNumRxHT_57_7_Mbps(Long numRxHT_57_7_Mbps) {
        this.numRxHT_57_7_Mbps = numRxHT_57_7_Mbps;
    }

    public Long getNumRxHT_58_5_Mbps() {
        return numRxHT_58_5_Mbps;
    }

    public void setNumRxHT_58_5_Mbps(Long numRxHT_58_5_Mbps) {
        this.numRxHT_58_5_Mbps = numRxHT_58_5_Mbps;
    }

    public Long getNumRxHT_60_Mbps() {
        return numRxHT_60_Mbps;
    }

    public void setNumRxHT_60_Mbps(Long numRxHT_60_Mbps) {
        this.numRxHT_60_Mbps = numRxHT_60_Mbps;
    }

    public Long getNumRxHT_65_Mbps() {
        return numRxHT_65_Mbps;
    }

    public void setNumRxHT_65_Mbps(Long numRxHT_65_Mbps) {
        this.numRxHT_65_Mbps = numRxHT_65_Mbps;
    }

    public Long getNumRxHT_72_1_Mbps() {
        return numRxHT_72_1_Mbps;
    }

    public void setNumRxHT_72_1_Mbps(Long numRxHT_72_1_Mbps) {
        this.numRxHT_72_1_Mbps = numRxHT_72_1_Mbps;
    }

    public Long getNumRxHT_78_Mbps() {
        return numRxHT_78_Mbps;
    }

    public void setNumRxHT_78_Mbps(Long numRxHT_78_Mbps) {
        this.numRxHT_78_Mbps = numRxHT_78_Mbps;
    }

    public Long getNumRxHT_81_Mbps() {
        return numRxHT_81_Mbps;
    }

    public void setNumRxHT_81_Mbps(Long numRxHT_81_Mbps) {
        this.numRxHT_81_Mbps = numRxHT_81_Mbps;
    }

    public Long getNumRxHT_86_6_Mbps() {
        return numRxHT_86_6_Mbps;
    }

    public void setNumRxHT_86_6_Mbps(Long numRxHT_86_6_Mbps) {
        this.numRxHT_86_6_Mbps = numRxHT_86_6_Mbps;
    }

    public Long getNumRxHT_86_8_Mbps() {
        return numRxHT_86_8_Mbps;
    }

    public void setNumRxHT_86_8_Mbps(Long numRxHT_86_8_Mbps) {
        this.numRxHT_86_8_Mbps = numRxHT_86_8_Mbps;
    }

    public Long getNumRxHT_87_8_Mbps() {
        return numRxHT_87_8_Mbps;
    }

    public void setNumRxHT_87_8_Mbps(Long numRxHT_87_8_Mbps) {
        this.numRxHT_87_8_Mbps = numRxHT_87_8_Mbps;
    }

    public Long getNumRxHT_90_Mbps() {
        return numRxHT_90_Mbps;
    }

    public void setNumRxHT_90_Mbps(Long numRxHT_90_Mbps) {
        this.numRxHT_90_Mbps = numRxHT_90_Mbps;
    }

    public Long getNumRxHT_97_5_Mbps() {
        return numRxHT_97_5_Mbps;
    }

    public void setNumRxHT_97_5_Mbps(Long numRxHT_97_5_Mbps) {
        this.numRxHT_97_5_Mbps = numRxHT_97_5_Mbps;
    }

    public Long getNumRxHT_104_Mbps() {
        return numRxHT_104_Mbps;
    }

    public void setNumRxHT_104_Mbps(Long numRxHT_104_Mbps) {
        this.numRxHT_104_Mbps = numRxHT_104_Mbps;
    }

    public Long getNumRxHT_108_Mbps() {
        return numRxHT_108_Mbps;
    }

    public void setNumRxHT_108_Mbps(Long numRxHT_108_Mbps) {
        this.numRxHT_108_Mbps = numRxHT_108_Mbps;
    }

    public Long getNumRxHT_115_5_Mbps() {
        return numRxHT_115_5_Mbps;
    }

    public void setNumRxHT_115_5_Mbps(Long numRxHT_115_5_Mbps) {
        this.numRxHT_115_5_Mbps = numRxHT_115_5_Mbps;
    }

    public Long getNumRxHT_117_Mbps() {
        return numRxHT_117_Mbps;
    }

    public void setNumRxHT_117_Mbps(Long numRxHT_117_Mbps) {
        this.numRxHT_117_Mbps = numRxHT_117_Mbps;
    }

    public Long getNumRxHT_117_1_Mbps() {
        return numRxHT_117_1_Mbps;
    }

    public void setNumRxHT_117_1_Mbps(Long numRxHT_117_1_Mbps) {
        this.numRxHT_117_1_Mbps = numRxHT_117_1_Mbps;
    }

    public Long getNumRxHT_120_Mbps() {
        return numRxHT_120_Mbps;
    }

    public void setNumRxHT_120_Mbps(Long numRxHT_120_Mbps) {
        this.numRxHT_120_Mbps = numRxHT_120_Mbps;
    }

    public Long getNumRxHT_121_5_Mbps() {
        return numRxHT_121_5_Mbps;
    }

    public void setNumRxHT_121_5_Mbps(Long numRxHT_121_5_Mbps) {
        this.numRxHT_121_5_Mbps = numRxHT_121_5_Mbps;
    }

    public Long getNumRxHT_130_Mbps() {
        return numRxHT_130_Mbps;
    }

    public void setNumRxHT_130_Mbps(Long numRxHT_130_Mbps) {
        this.numRxHT_130_Mbps = numRxHT_130_Mbps;
    }

    public Long getNumRxHT_130_3_Mbps() {
        return numRxHT_130_3_Mbps;
    }

    public void setNumRxHT_130_3_Mbps(Long numRxHT_130_3_Mbps) {
        this.numRxHT_130_3_Mbps = numRxHT_130_3_Mbps;
    }

    public Long getNumRxHT_135_Mbps() {
        return numRxHT_135_Mbps;
    }

    public void setNumRxHT_135_Mbps(Long numRxHT_135_Mbps) {
        this.numRxHT_135_Mbps = numRxHT_135_Mbps;
    }

    public Long getNumRxHT_144_3_Mbps() {
        return numRxHT_144_3_Mbps;
    }

    public void setNumRxHT_144_3_Mbps(Long numRxHT_144_3_Mbps) {
        this.numRxHT_144_3_Mbps = numRxHT_144_3_Mbps;
    }

    public Long getNumRxHT_150_Mbps() {
        return numRxHT_150_Mbps;
    }

    public void setNumRxHT_150_Mbps(Long numRxHT_150_Mbps) {
        this.numRxHT_150_Mbps = numRxHT_150_Mbps;
    }

    public Long getNumRxHT_156_Mbps() {
        return numRxHT_156_Mbps;
    }

    public void setNumRxHT_156_Mbps(Long numRxHT_156_Mbps) {
        this.numRxHT_156_Mbps = numRxHT_156_Mbps;
    }

    public Long getNumRxHT_162_Mbps() {
        return numRxHT_162_Mbps;
    }

    public void setNumRxHT_162_Mbps(Long numRxHT_162_Mbps) {
        this.numRxHT_162_Mbps = numRxHT_162_Mbps;
    }

    public Long getNumRxHT_173_1_Mbps() {
        return numRxHT_173_1_Mbps;
    }

    public void setNumRxHT_173_1_Mbps(Long numRxHT_173_1_Mbps) {
        this.numRxHT_173_1_Mbps = numRxHT_173_1_Mbps;
    }

    public Long getNumRxHT_173_3_Mbps() {
        return numRxHT_173_3_Mbps;
    }

    public void setNumRxHT_173_3_Mbps(Long numRxHT_173_3_Mbps) {
        this.numRxHT_173_3_Mbps = numRxHT_173_3_Mbps;
    }

    public Long getNumRxHT_175_5_Mbps() {
        return numRxHT_175_5_Mbps;
    }

    public void setNumRxHT_175_5_Mbps(Long numRxHT_175_5_Mbps) {
        this.numRxHT_175_5_Mbps = numRxHT_175_5_Mbps;
    }

    public Long getNumRxHT_180_Mbps() {
        return numRxHT_180_Mbps;
    }

    public void setNumRxHT_180_Mbps(Long numRxHT_180_Mbps) {
        this.numRxHT_180_Mbps = numRxHT_180_Mbps;
    }

    public Long getNumRxHT_195_Mbps() {
        return numRxHT_195_Mbps;
    }

    public void setNumRxHT_195_Mbps(Long numRxHT_195_Mbps) {
        this.numRxHT_195_Mbps = numRxHT_195_Mbps;
    }

    public Long getNumRxHT_200_Mbps() {
        return numRxHT_200_Mbps;
    }

    public void setNumRxHT_200_Mbps(Long numRxHT_200_Mbps) {
        this.numRxHT_200_Mbps = numRxHT_200_Mbps;
    }

    public Long getNumRxHT_208_Mbps() {
        return numRxHT_208_Mbps;
    }

    public void setNumRxHT_208_Mbps(Long numRxHT_208_Mbps) {
        this.numRxHT_208_Mbps = numRxHT_208_Mbps;
    }

    public Long getNumRxHT_216_Mbps() {
        return numRxHT_216_Mbps;
    }

    public void setNumRxHT_216_Mbps(Long numRxHT_216_Mbps) {
        this.numRxHT_216_Mbps = numRxHT_216_Mbps;
    }

    public Long getNumRxHT_216_6_Mbps() {
        return numRxHT_216_6_Mbps;
    }

    public void setNumRxHT_216_6_Mbps(Long numRxHT_216_6_Mbps) {
        this.numRxHT_216_6_Mbps = numRxHT_216_6_Mbps;
    }

    public Long getNumRxHT_231_1_Mbps() {
        return numRxHT_231_1_Mbps;
    }

    public void setNumRxHT_231_1_Mbps(Long numRxHT_231_1_Mbps) {
        this.numRxHT_231_1_Mbps = numRxHT_231_1_Mbps;
    }

    public Long getNumRxHT_234_Mbps() {
        return numRxHT_234_Mbps;
    }

    public void setNumRxHT_234_Mbps(Long numRxHT_234_Mbps) {
        this.numRxHT_234_Mbps = numRxHT_234_Mbps;
    }

    public Long getNumRxHT_240_Mbps() {
        return numRxHT_240_Mbps;
    }

    public void setNumRxHT_240_Mbps(Long numRxHT_240_Mbps) {
        this.numRxHT_240_Mbps = numRxHT_240_Mbps;
    }

    public Long getNumRxHT_243_Mbps() {
        return numRxHT_243_Mbps;
    }

    public void setNumRxHT_243_Mbps(Long numRxHT_243_Mbps) {
        this.numRxHT_243_Mbps = numRxHT_243_Mbps;
    }

    public Long getNumRxHT_260_Mbps() {
        return numRxHT_260_Mbps;
    }

    public void setNumRxHT_260_Mbps(Long numRxHT_260_Mbps) {
        this.numRxHT_260_Mbps = numRxHT_260_Mbps;
    }

    public Long getNumRxHT_263_2_Mbps() {
        return numRxHT_263_2_Mbps;
    }

    public void setNumRxHT_263_2_Mbps(Long numRxHT_263_2_Mbps) {
        this.numRxHT_263_2_Mbps = numRxHT_263_2_Mbps;
    }

    public Long getNumRxHT_270_Mbps() {
        return numRxHT_270_Mbps;
    }

    public void setNumRxHT_270_Mbps(Long numRxHT_270_Mbps) {
        this.numRxHT_270_Mbps = numRxHT_270_Mbps;
    }

    public Long getNumRxHT_288_7_Mbps() {
        return numRxHT_288_7_Mbps;
    }

    public void setNumRxHT_288_7_Mbps(Long numRxHT_288_7_Mbps) {
        this.numRxHT_288_7_Mbps = numRxHT_288_7_Mbps;
    }

    public Long getNumRxHT_288_8_Mbps() {
        return numRxHT_288_8_Mbps;
    }

    public void setNumRxHT_288_8_Mbps(Long numRxHT_288_8_Mbps) {
        this.numRxHT_288_8_Mbps = numRxHT_288_8_Mbps;
    }

    public Long getNumRxHT_292_5_Mbps() {
        return numRxHT_292_5_Mbps;
    }

    public void setNumRxHT_292_5_Mbps(Long numRxHT_292_5_Mbps) {
        this.numRxHT_292_5_Mbps = numRxHT_292_5_Mbps;
    }

    public Long getNumRxHT_300_Mbps() {
        return numRxHT_300_Mbps;
    }

    public void setNumRxHT_300_Mbps(Long numRxHT_300_Mbps) {
        this.numRxHT_300_Mbps = numRxHT_300_Mbps;
    }

    public Long getNumRxHT_312_Mbps() {
        return numRxHT_312_Mbps;
    }

    public void setNumRxHT_312_Mbps(Long numRxHT_312_Mbps) {
        this.numRxHT_312_Mbps = numRxHT_312_Mbps;
    }

    public Long getNumRxHT_324_Mbps() {
        return numRxHT_324_Mbps;
    }

    public void setNumRxHT_324_Mbps(Long numRxHT_324_Mbps) {
        this.numRxHT_324_Mbps = numRxHT_324_Mbps;
    }

    public Long getNumRxHT_325_Mbps() {
        return numRxHT_325_Mbps;
    }

    public void setNumRxHT_325_Mbps(Long numRxHT_325_Mbps) {
        this.numRxHT_325_Mbps = numRxHT_325_Mbps;
    }

    public Long getNumRxHT_346_7_Mbps() {
        return numRxHT_346_7_Mbps;
    }

    public void setNumRxHT_346_7_Mbps(Long numRxHT_346_7_Mbps) {
        this.numRxHT_346_7_Mbps = numRxHT_346_7_Mbps;
    }

    public Long getNumRxHT_351_Mbps() {
        return numRxHT_351_Mbps;
    }

    public void setNumRxHT_351_Mbps(Long numRxHT_351_Mbps) {
        this.numRxHT_351_Mbps = numRxHT_351_Mbps;
    }

    public Long getNumRxHT_351_2_Mbps() {
        return numRxHT_351_2_Mbps;
    }

    public void setNumRxHT_351_2_Mbps(Long numRxHT_351_2_Mbps) {
        this.numRxHT_351_2_Mbps = numRxHT_351_2_Mbps;
    }

    public Long getNumRxHT_360_Mbps() {
        return numRxHT_360_Mbps;
    }

    public void setNumRxHT_360_Mbps(Long numRxHT_360_Mbps) {
        this.numRxHT_360_Mbps = numRxHT_360_Mbps;
    }

    public Long getNumTxVHT_292_5_Mbps() {
        return numTxVHT_292_5_Mbps;
    }

    public void setNumTxVHT_292_5_Mbps(Long numTxVHT_292_5_Mbps) {
        this.numTxVHT_292_5_Mbps = numTxVHT_292_5_Mbps;
    }

    public Long getNumTxVHT_325_Mbps() {
        return numTxVHT_325_Mbps;
    }

    public void setNumTxVHT_325_Mbps(Long numTxVHT_325_Mbps) {
        this.numTxVHT_325_Mbps = numTxVHT_325_Mbps;
    }

    public Long getNumTxVHT_364_5_Mbps() {
        return numTxVHT_364_5_Mbps;
    }

    public void setNumTxVHT_364_5_Mbps(Long numTxVHT_364_5_Mbps) {
        this.numTxVHT_364_5_Mbps = numTxVHT_364_5_Mbps;
    }

    public Long getNumTxVHT_400_Mbps() {
        return numTxVHT_400_Mbps;
    }

    public void setNumTxVHT_400_Mbps(Long numTxVHT_400_Mbps) {
        this.numTxVHT_400_Mbps = numTxVHT_400_Mbps;
    }

    public Long getNumTxVHT_403_Mbps() {
        return numTxVHT_403_Mbps;
    }

    public void setNumTxVHT_403_Mbps(Long numTxVHT_403_Mbps) {
        this.numTxVHT_403_Mbps = numTxVHT_403_Mbps;
    }

    public Long getNumTxVHT_405_Mbps() {
        return numTxVHT_405_Mbps;
    }

    public void setNumTxVHT_405_Mbps(Long numTxVHT_405_Mbps) {
        this.numTxVHT_405_Mbps = numTxVHT_405_Mbps;
    }

    public Long getNumTxVHT_432_Mbps() {
        return numTxVHT_432_Mbps;
    }

    public void setNumTxVHT_432_Mbps(Long numTxVHT_432_Mbps) {
        this.numTxVHT_432_Mbps = numTxVHT_432_Mbps;
    }

    public Long getNumTxVHT_433_2_Mbps() {
        return numTxVHT_433_2_Mbps;
    }

    public void setNumTxVHT_433_2_Mbps(Long numTxVHT_433_2_Mbps) {
        this.numTxVHT_433_2_Mbps = numTxVHT_433_2_Mbps;
    }

    public Long getNumTxVHT_450_Mbps() {
        return numTxVHT_450_Mbps;
    }

    public void setNumTxVHT_450_Mbps(Long numTxVHT_450_Mbps) {
        this.numTxVHT_450_Mbps = numTxVHT_450_Mbps;
    }

    public Long getNumTxVHT_468_Mbps() {
        return numTxVHT_468_Mbps;
    }

    public void setNumTxVHT_468_Mbps(Long numTxVHT_468_Mbps) {
        this.numTxVHT_468_Mbps = numTxVHT_468_Mbps;
    }

    public Long getNumTxVHT_480_Mbps() {
        return numTxVHT_480_Mbps;
    }

    public void setNumTxVHT_480_Mbps(Long numTxVHT_480_Mbps) {
        this.numTxVHT_480_Mbps = numTxVHT_480_Mbps;
    }

    public Long getNumTxVHT_486_Mbps() {
        return numTxVHT_486_Mbps;
    }

    public void setNumTxVHT_486_Mbps(Long numTxVHT_486_Mbps) {
        this.numTxVHT_486_Mbps = numTxVHT_486_Mbps;
    }

    public Long getNumTxVHT_526_5_Mbps() {
        return numTxVHT_526_5_Mbps;
    }

    public void setNumTxVHT_526_5_Mbps(Long numTxVHT_526_5_Mbps) {
        this.numTxVHT_526_5_Mbps = numTxVHT_526_5_Mbps;
    }

    public Long getNumTxVHT_540_Mbps() {
        return numTxVHT_540_Mbps;
    }

    public void setNumTxVHT_540_Mbps(Long numTxVHT_540_Mbps) {
        this.numTxVHT_540_Mbps = numTxVHT_540_Mbps;
    }

    public Long getNumTxVHT_600_Mbps() {
        return numTxVHT_600_Mbps;
    }

    public void setNumTxVHT_600_Mbps(Long numTxVHT_600_Mbps) {
        this.numTxVHT_600_Mbps = numTxVHT_600_Mbps;
    }

    public Long getNumTxVHT_648_Mbps() {
        return numTxVHT_648_Mbps;
    }

    public void setNumTxVHT_648_Mbps(Long numTxVHT_648_Mbps) {
        this.numTxVHT_648_Mbps = numTxVHT_648_Mbps;
    }

    public Long getNumTxVHT_702_Mbps() {
        return numTxVHT_702_Mbps;
    }

    public void setNumTxVHT_702_Mbps(Long numTxVHT_702_Mbps) {
        this.numTxVHT_702_Mbps = numTxVHT_702_Mbps;
    }

    public Long getNumTxVHT_720_Mbps() {
        return numTxVHT_720_Mbps;
    }

    public void setNumTxVHT_720_Mbps(Long numTxVHT_720_Mbps) {
        this.numTxVHT_720_Mbps = numTxVHT_720_Mbps;
    }

    public Long getNumTxVHT_800_Mbps() {
        return numTxVHT_800_Mbps;
    }

    public void setNumTxVHT_800_Mbps(Long numTxVHT_800_Mbps) {
        this.numTxVHT_800_Mbps = numTxVHT_800_Mbps;
    }

    public Long getNumTxVHT_877_5_Mbps() {
        return numTxVHT_877_5_Mbps;
    }

    public void setNumTxVHT_877_5_Mbps(Long numTxVHT_877_5_Mbps) {
        this.numTxVHT_877_5_Mbps = numTxVHT_877_5_Mbps;
    }

    public Long getNumTxVHT_936_Mbps() {
        return numTxVHT_936_Mbps;
    }

    public void setNumTxVHT_936_Mbps(Long numTxVHT_936_Mbps) {
        this.numTxVHT_936_Mbps = numTxVHT_936_Mbps;
    }

    public Long getNumTxVHT_975_Mbps() {
        return numTxVHT_975_Mbps;
    }

    public void setNumTxVHT_975_Mbps(Long numTxVHT_975_Mbps) {
        this.numTxVHT_975_Mbps = numTxVHT_975_Mbps;
    }

    public Long getNumTxVHT_1040_Mbps() {
        return numTxVHT_1040_Mbps;
    }

    public void setNumTxVHT_1040_Mbps(Long numTxVHT_1040_Mbps) {
        this.numTxVHT_1040_Mbps = numTxVHT_1040_Mbps;
    }

    public Long getNumTxVHT_1053_Mbps() {
        return numTxVHT_1053_Mbps;
    }

    public void setNumTxVHT_1053_Mbps(Long numTxVHT_1053_Mbps) {
        this.numTxVHT_1053_Mbps = numTxVHT_1053_Mbps;
    }

    public Long getNumTxVHT_1053_1_Mbps() {
        return numTxVHT_1053_1_Mbps;
    }

    public void setNumTxVHT_1053_1_Mbps(Long numTxVHT_1053_1_Mbps) {
        this.numTxVHT_1053_1_Mbps = numTxVHT_1053_1_Mbps;
    }

    public Long getNumTxVHT_1170_Mbps() {
        return numTxVHT_1170_Mbps;
    }

    public void setNumTxVHT_1170_Mbps(Long numTxVHT_1170_Mbps) {
        this.numTxVHT_1170_Mbps = numTxVHT_1170_Mbps;
    }

    public Long getNumTxVHT_1300_Mbps() {
        return numTxVHT_1300_Mbps;
    }

    public void setNumTxVHT_1300_Mbps(Long numTxVHT_1300_Mbps) {
        this.numTxVHT_1300_Mbps = numTxVHT_1300_Mbps;
    }

    public Long getNumTxVHT_1404_Mbps() {
        return numTxVHT_1404_Mbps;
    }

    public void setNumTxVHT_1404_Mbps(Long numTxVHT_1404_Mbps) {
        this.numTxVHT_1404_Mbps = numTxVHT_1404_Mbps;
    }

    public Long getNumTxVHT_1560_Mbps() {
        return numTxVHT_1560_Mbps;
    }

    public void setNumTxVHT_1560_Mbps(Long numTxVHT_1560_Mbps) {
        this.numTxVHT_1560_Mbps = numTxVHT_1560_Mbps;
    }

    public Long getNumTxVHT_1579_5_Mbps() {
        return numTxVHT_1579_5_Mbps;
    }

    public void setNumTxVHT_1579_5_Mbps(Long numTxVHT_1579_5_Mbps) {
        this.numTxVHT_1579_5_Mbps = numTxVHT_1579_5_Mbps;
    }

    public Long getNumTxVHT_1733_1_Mbps() {
        return numTxVHT_1733_1_Mbps;
    }

    public void setNumTxVHT_1733_1_Mbps(Long numTxVHT_1733_1_Mbps) {
        this.numTxVHT_1733_1_Mbps = numTxVHT_1733_1_Mbps;
    }

    public Long getNumTxVHT_1733_4_Mbps() {
        return numTxVHT_1733_4_Mbps;
    }

    public void setNumTxVHT_1733_4_Mbps(Long numTxVHT_1733_4_Mbps) {
        this.numTxVHT_1733_4_Mbps = numTxVHT_1733_4_Mbps;
    }

    public Long getNumTxVHT_1755_Mbps() {
        return numTxVHT_1755_Mbps;
    }

    public void setNumTxVHT_1755_Mbps(Long numTxVHT_1755_Mbps) {
        this.numTxVHT_1755_Mbps = numTxVHT_1755_Mbps;
    }

    public Long getNumTxVHT_1872_Mbps() {
        return numTxVHT_1872_Mbps;
    }

    public void setNumTxVHT_1872_Mbps(Long numTxVHT_1872_Mbps) {
        this.numTxVHT_1872_Mbps = numTxVHT_1872_Mbps;
    }

    public Long getNumTxVHT_1950_Mbps() {
        return numTxVHT_1950_Mbps;
    }

    public void setNumTxVHT_1950_Mbps(Long numTxVHT_1950_Mbps) {
        this.numTxVHT_1950_Mbps = numTxVHT_1950_Mbps;
    }

    public Long getNumTxVHT_2080_Mbps() {
        return numTxVHT_2080_Mbps;
    }

    public void setNumTxVHT_2080_Mbps(Long numTxVHT_2080_Mbps) {
        this.numTxVHT_2080_Mbps = numTxVHT_2080_Mbps;
    }

    public Long getNumTxVHT_2106_Mbps() {
        return numTxVHT_2106_Mbps;
    }

    public void setNumTxVHT_2106_Mbps(Long numTxVHT_2106_Mbps) {
        this.numTxVHT_2106_Mbps = numTxVHT_2106_Mbps;
    }

    public Long getNumTxVHT_2340_Mbps() {
        return numTxVHT_2340_Mbps;
    }

    public void setNumTxVHT_2340_Mbps(Long numTxVHT_2340_Mbps) {
        this.numTxVHT_2340_Mbps = numTxVHT_2340_Mbps;
    }

    public Long getNumTxVHT_2600_Mbps() {
        return numTxVHT_2600_Mbps;
    }

    public void setNumTxVHT_2600_Mbps(Long numTxVHT_2600_Mbps) {
        this.numTxVHT_2600_Mbps = numTxVHT_2600_Mbps;
    }

    public Long getNumTxVHT_2808_Mbps() {
        return numTxVHT_2808_Mbps;
    }

    public void setNumTxVHT_2808_Mbps(Long numTxVHT_2808_Mbps) {
        this.numTxVHT_2808_Mbps = numTxVHT_2808_Mbps;
    }

    public Long getNumTxVHT_3120_Mbps() {
        return numTxVHT_3120_Mbps;
    }

    public void setNumTxVHT_3120_Mbps(Long numTxVHT_3120_Mbps) {
        this.numTxVHT_3120_Mbps = numTxVHT_3120_Mbps;
    }

    public Long getNumTxVHT_3466_8_Mbps() {
        return numTxVHT_3466_8_Mbps;
    }

    public void setNumTxVHT_3466_8_Mbps(Long numTxVHT_3466_8_Mbps) {
        this.numTxVHT_3466_8_Mbps = numTxVHT_3466_8_Mbps;
    }

    
    public Long getNumRxVHT_292_5_Mbps() {
        return numRxVHT_292_5_Mbps;
    }

    public void setNumRxVHT_292_5_Mbps(Long numRxVHT_292_5_Mbps) {
        this.numRxVHT_292_5_Mbps = numRxVHT_292_5_Mbps;
    }

    public Long getNumRxVHT_325_Mbps() {
        return numRxVHT_325_Mbps;
    }

    public void setNumRxVHT_325_Mbps(Long numRxVHT_325_Mbps) {
        this.numRxVHT_325_Mbps = numRxVHT_325_Mbps;
    }

    public Long getNumRxVHT_364_5_Mbps() {
        return numRxVHT_364_5_Mbps;
    }

    public void setNumRxVHT_364_5_Mbps(Long numRxVHT_364_5_Mbps) {
        this.numRxVHT_364_5_Mbps = numRxVHT_364_5_Mbps;
    }

    public Long getNumRxVHT_390_Mbps() {
        return numRxVHT_390_Mbps;
    }

    public void setNumRxVHT_390_Mbps(Long numRxVHT_390_Mbps) {
        this.numRxVHT_390_Mbps = numRxVHT_390_Mbps;
    }

    public Long getNumRxVHT_400_Mbps() {
        return numRxVHT_400_Mbps;
    }

    public void setNumRxVHT_400_Mbps(Long numRxVHT_400_Mbps) {
        this.numRxVHT_400_Mbps = numRxVHT_400_Mbps;
    }

    public Long getNumRxVHT_403_Mbps() {
        return numRxVHT_403_Mbps;
    }

    public void setNumRxVHT_403_Mbps(Long numRxVHT_403_Mbps) {
        this.numRxVHT_403_Mbps = numRxVHT_403_Mbps;
    }

    public Long getNumRxVHT_405_Mbps() {
        return numRxVHT_405_Mbps;
    }

    public void setNumRxVHT_405_Mbps(Long numRxVHT_405_Mbps) {
        this.numRxVHT_405_Mbps = numRxVHT_405_Mbps;
    }

    public Long getNumRxVHT_432_Mbps() {
        return numRxVHT_432_Mbps;
    }

    public void setNumRxVHT_432_Mbps(Long numRxVHT_432_Mbps) {
        this.numRxVHT_432_Mbps = numRxVHT_432_Mbps;
    }

    public Long getNumRxVHT_433_2_Mbps() {
        return numRxVHT_433_2_Mbps;
    }

    public void setNumRxVHT_433_2_Mbps(Long numRxVHT_433_2_Mbps) {
        this.numRxVHT_433_2_Mbps = numRxVHT_433_2_Mbps;
    }

    public Long getNumRxVHT_450_Mbps() {
        return numRxVHT_450_Mbps;
    }

    public void setNumRxVHT_450_Mbps(Long numRxVHT_450_Mbps) {
        this.numRxVHT_450_Mbps = numRxVHT_450_Mbps;
    }

    public Long getNumRxVHT_480_Mbps() {
        return numRxVHT_480_Mbps;
    }

    public void setNumRxVHT_480_Mbps(Long numRxVHT_480_Mbps) {
        this.numRxVHT_480_Mbps = numRxVHT_480_Mbps;
    }

    public Long getNumRxVHT_486_Mbps() {
        return numRxVHT_486_Mbps;
    }

    public void setNumRxVHT_486_Mbps(Long numRxVHT_486_Mbps) {
        this.numRxVHT_486_Mbps = numRxVHT_486_Mbps;
    }

    public Long getNumRxVHT_520_Mbps() {
        return numRxVHT_520_Mbps;
    }

    public void setNumRxVHT_520_Mbps(Long numRxVHT_520_Mbps) {
        this.numRxVHT_520_Mbps = numRxVHT_520_Mbps;
    }

    public Long getNumRxVHT_540_Mbps() {
        return numRxVHT_540_Mbps;
    }

    public void setNumRxVHT_540_Mbps(Long numRxVHT_540_Mbps) {
        this.numRxVHT_540_Mbps = numRxVHT_540_Mbps;
    }

    public Long getNumRxVHT_600_Mbps() {
        return numRxVHT_600_Mbps;
    }

    public void setNumRxVHT_600_Mbps(Long numRxVHT_600_Mbps) {
        this.numRxVHT_600_Mbps = numRxVHT_600_Mbps;
    }

    public Long getNumRxVHT_648_Mbps() {
        return numRxVHT_648_Mbps;
    }

    public void setNumRxVHT_648_Mbps(Long numRxVHT_648_Mbps) {
        this.numRxVHT_648_Mbps = numRxVHT_648_Mbps;
    }

    public Long getNumRxVHT_650_Mbps() {
        return numRxVHT_650_Mbps;
    }

    public void setNumRxVHT_650_Mbps(Long numRxVHT_650_Mbps) {
        this.numRxVHT_650_Mbps = numRxVHT_650_Mbps;
    }

    public Long getNumRxVHT_702_Mbps() {
        return numRxVHT_702_Mbps;
    }

    public void setNumRxVHT_702_Mbps(Long numRxVHT_702_Mbps) {
        this.numRxVHT_702_Mbps = numRxVHT_702_Mbps;
    }

    public Long getNumRxVHT_720_Mbps() {
        return numRxVHT_720_Mbps;
    }

    public void setNumRxVHT_720_Mbps(Long numRxVHT_720_Mbps) {
        this.numRxVHT_720_Mbps = numRxVHT_720_Mbps;
    }

    public Long getNumRxVHT_800_Mbps() {
        return numRxVHT_800_Mbps;
    }

    public void setNumRxVHT_800_Mbps(Long numRxVHT_800_Mbps) {
        this.numRxVHT_800_Mbps = numRxVHT_800_Mbps;
    }

    public Long getNumRxVHT_877_5_Mbps() {
        return numRxVHT_877_5_Mbps;
    }

    public void setNumRxVHT_877_5_Mbps(Long numRxVHT_877_5_Mbps) {
        this.numRxVHT_877_5_Mbps = numRxVHT_877_5_Mbps;
    }

    public Long getNumRxVHT_936_Mbps() {
        return numRxVHT_936_Mbps;
    }

    public void setNumRxVHT_936_Mbps(Long numRxVHT_936_Mbps) {
        this.numRxVHT_936_Mbps = numRxVHT_936_Mbps;
    }

    public Long getNumRxVHT_975_Mbps() {
        return numRxVHT_975_Mbps;
    }

    public void setNumRxVHT_975_Mbps(Long numRxVHT_975_Mbps) {
        this.numRxVHT_975_Mbps = numRxVHT_975_Mbps;
    }

    public Long getNumRxVHT_1040_Mbps() {
        return numRxVHT_1040_Mbps;
    }

    public void setNumRxVHT_1040_Mbps(Long numRxVHT_1040_Mbps) {
        this.numRxVHT_1040_Mbps = numRxVHT_1040_Mbps;
    }

    public Long getNumRxVHT_1053_Mbps() {
        return numRxVHT_1053_Mbps;
    }

    public void setNumRxVHT_1053_Mbps(Long numRxVHT_1053_Mbps) {
        this.numRxVHT_1053_Mbps = numRxVHT_1053_Mbps;
    }

    public Long getNumRxVHT_1053_1_Mbps() {
        return numRxVHT_1053_1_Mbps;
    }

    public void setNumRxVHT_1053_1_Mbps(Long numRxVHT_1053_1_Mbps) {
        this.numRxVHT_1053_1_Mbps = numRxVHT_1053_1_Mbps;
    }

    public Long getNumRxVHT_1170_Mbps() {
        return numRxVHT_1170_Mbps;
    }

    public void setNumRxVHT_1170_Mbps(Long numRxVHT_1170_Mbps) {
        this.numRxVHT_1170_Mbps = numRxVHT_1170_Mbps;
    }

    public Long getNumRxVHT_1300_Mbps() {
        return numRxVHT_1300_Mbps;
    }

    public void setNumRxVHT_1300_Mbps(Long numRxVHT_1300_Mbps) {
        this.numRxVHT_1300_Mbps = numRxVHT_1300_Mbps;
    }

    public Long getNumRxVHT_1404_Mbps() {
        return numRxVHT_1404_Mbps;
    }

    public void setNumRxVHT_1404_Mbps(Long numRxVHT_1404_Mbps) {
        this.numRxVHT_1404_Mbps = numRxVHT_1404_Mbps;
    }

    public Long getNumRxVHT_1560_Mbps() {
        return numRxVHT_1560_Mbps;
    }

    public void setNumRxVHT_1560_Mbps(Long numRxVHT_1560_Mbps) {
        this.numRxVHT_1560_Mbps = numRxVHT_1560_Mbps;
    }

    public Long getNumRxVHT_1579_5_Mbps() {
        return numRxVHT_1579_5_Mbps;
    }

    public void setNumRxVHT_1579_5_Mbps(Long numRxVHT_1579_5_Mbps) {
        this.numRxVHT_1579_5_Mbps = numRxVHT_1579_5_Mbps;
    }

    public Long getNumRxVHT_1733_1_Mbps() {
        return numRxVHT_1733_1_Mbps;
    }

    public void setNumRxVHT_1733_1_Mbps(Long numRxVHT_1733_1_Mbps) {
        this.numRxVHT_1733_1_Mbps = numRxVHT_1733_1_Mbps;
    }

    public Long getNumRxVHT_1733_4_Mbps() {
        return numRxVHT_1733_4_Mbps;
    }

    public void setNumRxVHT_1733_4_Mbps(Long numRxVHT_1733_4_Mbps) {
        this.numRxVHT_1733_4_Mbps = numRxVHT_1733_4_Mbps;
    }

    public Long getNumRxVHT_1755_Mbps() {
        return numRxVHT_1755_Mbps;
    }

    public void setNumRxVHT_1755_Mbps(Long numRxVHT_1755_Mbps) {
        this.numRxVHT_1755_Mbps = numRxVHT_1755_Mbps;
    }

    public Long getNumRxVHT_1872_Mbps() {
        return numRxVHT_1872_Mbps;
    }

    public void setNumRxVHT_1872_Mbps(Long numRxVHT_1872_Mbps) {
        this.numRxVHT_1872_Mbps = numRxVHT_1872_Mbps;
    }

    public Long getNumRxVHT_1950_Mbps() {
        return numRxVHT_1950_Mbps;
    }

    public void setNumRxVHT_1950_Mbps(Long numRxVHT_1950_Mbps) {
        this.numRxVHT_1950_Mbps = numRxVHT_1950_Mbps;
    }

    public Long getNumRxVHT_2080_Mbps() {
        return numRxVHT_2080_Mbps;
    }

    public void setNumRxVHT_2080_Mbps(Long numRxVHT_2080_Mbps) {
        this.numRxVHT_2080_Mbps = numRxVHT_2080_Mbps;
    }

    public Long getNumRxVHT_2106_Mbps() {
        return numRxVHT_2106_Mbps;
    }

    public void setNumRxVHT_2106_Mbps(Long numRxVHT_2106_Mbps) {
        this.numRxVHT_2106_Mbps = numRxVHT_2106_Mbps;
    }

    public Long getNumRxVHT_2340_Mbps() {
        return numRxVHT_2340_Mbps;
    }

    public void setNumRxVHT_2340_Mbps(Long numRxVHT_2340_Mbps) {
        this.numRxVHT_2340_Mbps = numRxVHT_2340_Mbps;
    }

    public Long getNumRxVHT_2600_Mbps() {
        return numRxVHT_2600_Mbps;
    }

    public void setNumRxVHT_2600_Mbps(Long numRxVHT_2600_Mbps) {
        this.numRxVHT_2600_Mbps = numRxVHT_2600_Mbps;
    }

    public Long getNumRxVHT_2808_Mbps() {
        return numRxVHT_2808_Mbps;
    }

    public void setNumRxVHT_2808_Mbps(Long numRxVHT_2808_Mbps) {
        this.numRxVHT_2808_Mbps = numRxVHT_2808_Mbps;
    }

    public Long getNumRxVHT_3120_Mbps() {
        return numRxVHT_3120_Mbps;
    }

    public void setNumRxVHT_3120_Mbps(Long numRxVHT_3120_Mbps) {
        this.numRxVHT_3120_Mbps = numRxVHT_3120_Mbps;
    }

    public Long getNumRxVHT_3466_8_Mbps() {
        return numRxVHT_3466_8_Mbps;
    }

    public void setNumRxVHT_3466_8_Mbps(Long numRxVHT_3466_8_Mbps) {
        this.numRxVHT_3466_8_Mbps = numRxVHT_3466_8_Mbps;
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

    public GuardInterval getGuardInterval() {
        return guardInterval;
    }

    public void setGuardInterval(GuardInterval guardInterval) {
        this.guardInterval = guardInterval;
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
