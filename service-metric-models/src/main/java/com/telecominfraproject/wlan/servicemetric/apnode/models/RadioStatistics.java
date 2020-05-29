package com.telecominfraproject.wlan.servicemetric.apnode.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

@JsonInclude(JsonInclude.Include.NON_NULL) 
public class RadioStatistics extends BaseJsonModel {
    private static final long serialVersionUID = 1185985309064758000L;

    /**
    *  The number of radio resets
    */
    private Integer numRadioResets;

    /**
    *  The number of channel changes.
    */
    private Integer numChanChanges;

    /**
    *  The number of tx power changes.
    */
    private Integer numTxPowerChanges;

    /**
    *  The number of channel changes due to radar detections.
    */
    private Integer numRadarChanChanges;

    /**
    *  The number of free TX buffers available.
    */
    private Integer numFreeTxBuf;

    /**
    *  11g protection, 2.4GHz only.
    */
    private Integer elevenGProtection;

    /**
    *  The number of scanning requests.
    */
    private Integer numScanReq;

    /**
    *  The number of scanning successes.
    */
    private Integer numScanSucc;

    /**
    *  The Current EIRP.
    */
    private Integer curEirp;

    /**
     * Actuall Cell Size
     */
    private List<Integer> actualCellSize;
    
    /**
     * The current primary channel
     */
    private Integer curChannel;
    
    /**
     * The current backup channel
     */
    private Integer curBackupChannel;
    
    /**
    *  The RSSI of last frame received.
    */
    private Integer rxLastRssi;

    /**
    *  The number of received frames.
    */
    private Integer numRx;

    /**
    *  The number of received frames without FCS errors.
    */
    private Integer numRxNoFcsErr;

    /**
    *  The number of received frames with FCS errors.
    */
    private Integer numRxFcsErr;

    /**
    *  The number of received data frames.
    */
    private Integer numRxData;

    /**
    *  The number of received management frames.
    */
    private Integer numRxManagement;

    /**
    *  The number of received control frames.
    */
    private Integer numRxControl;

    /**
    *  The number of received data frames.
    */
    private Long rxDataBytes;

    /**
    *  The number of received RTS frames.
    */
    private Integer numRxRts;

    /**
    *  The number of received CTS frames.
    */
    private Integer numRxCts;

    /**
    *  The number of all received ACK frames (Acks + BlockAcks).
    */
    private Integer numRxAck;

    /**
    *  The number of received beacon frames.
    */
    private Integer numRxBeacon;

    /**
    *  The number of received probe request frames.
    */
    private Integer numRxProbeReq;

    /**
    *  The number of received probe response frames.
    */
    private Integer numRxProbeResp;

    /**
    *  The number of received retry frames.
    */
    private Integer numRxRetry;

    /**
    *  The number of frames received during off-channel scanning.
    */
    private Integer numRxOffChan;

    /**
    *  The number of received duplicated frames.
    */
    private Integer numRxDup;

    /**
    *  The number of received Broadcast/Multicast frames.
    */
    private Integer numRxBcMc;

    /**
    *  The number of received null data frames.
    */
    private Integer numRxNullData;

    /**
    *  The number of received ps-poll frames
    */
    private Integer numRxPspoll;

    /**
    *  The number of received frames with errors.
    */
    private Integer numRxErr;

    /**
    *  The number of received STBC frames.
    */
    private Integer numRxStbc;

    /**
    *  The number of received LDPC frames.
    */
    private Integer numRxLdpc;

    /**
    *  The number of dropped rx frames, runt.
    */
    private Integer numRxDropRunt;

    /**
    *  The number of dropped rx frames, invalid source MAC.
    */
    private Integer numRxDropInvalidSrcMac;

    /**
    *  The number of dropped rx frames, AMSDU no receive.
    */
    private Integer numRxDropAmsduNoRcv;

    /**
    *  The number of dropped rx frames, Ethernet header runt.
    */
    private Integer numRxDropEthHdrRunt;

    /**
    *  The number of dropped rx frames, AMSDU deagg sequence.
    */
    private Integer numRxAmsduDeaggSeq;

    /**
    *  The number of dropped rx frames, AMSDU deagg intermediate.
    */
    private Integer numRxAmsduDeaggItmd;

    /**
    *  The number of dropped rx frames, AMSDU deagg last.
    */
    private Integer numRxAmsduDeaggLast;

    /**
     * The number of dropped rx frames, no frame control field.
     */
    private Long numRxDropNoFcField;

    /**
     * The number of dropped rx frames, bad protocol.
     */
    private Long numRxDropBadProtocol;

    /**
    *  The number of received ethernet and local generated frames for transmit.
    */
    private Long numRcvFrameForTx;

    /**
    *  The number of TX frames queued.
    */
    private Long numTxQueued;

    /**
    *  The number of received ethernet and local generated broadcast frames for transmit.
    */
    private Integer numRcvBcForTx;

    /**
    *  The number of every TX frame dropped.
    */
    private Integer numTxDropped;

    /**
    *  The number of TX frame dropped due to retries.
    */
    private Integer numTxRetryDropped;

    /**
    *  The number of broadcast frames dropped.
    */
    private Integer numTxBcDropped;

    /**
    *  The number of frames successfully transmitted.
    */
    private Integer numTxSucc;

    /**
    *  The number of transmitted PS unicast frame.
    */
    private Integer numTxPsUnicast;

    /**
    *  The number of transmitted DTIM multicast frames.
    */
    private Integer numTxDtimMc;

    /**
    *  The number of successfully transmitted frames at firt attemp.
    */
    private Integer numTxSuccNoRetry;

    /**
    *  The number of successfully transmitted frames with retries.
    */
    private Integer numTxSuccRetries;

    /**
    *  The number of Tx frames with retries.
    */
    private Integer numTxMultiRetries;

    /**
    *  The number of TX management frames.
    */
    private Integer numTxManagement;

    /**
    *  The number of Tx control frames.
    */
    private Integer numTxControl;

    /**
    *  The number of Tx action frames.
    */
    private Integer numTxAction;

    /**
    *  The number of successfully transmitted beacon.
    */
    @Deprecated // according to HJ (NAAS-7861) this isn't populated properly. Will leave so we can see historical data. 
    private Integer numTxBeaconSucc;

    /**
    *  The number of unsuccessfully transmitted beacon.
    */
    @Deprecated // according to HJ (NAAS-7861) this isn't populated properly. Will leave so we can see historical data.
    private Integer numTxBeaconFail;

    /**
    *  The number of successive beacon tx failure.
    */
    private Integer numTxBeaconSuFail;

    /**
    *  The number of TX probe response.
    */
    private Integer numTxProbeResp;

    /**
    *  The number of Tx data frames.
    */
    private Integer numTxData;

    /**
    *  The number of Tx data frames with retries.
    */
    private Integer numTxDataRetries;

    /**
    *  The number of RTS frames sent successfully .
    */
    private Integer numTxRtsSucc;

    /**
    *  The number of RTS frames failed transmission.
    */
    private Integer numTxRtsFail;

    /**
    *  The number of CTS frames sent.
    */
    private Integer numTxCts;

    /**
    *  The number of TX frames failed because of not Acked.
    */
    private Integer numTxNoAck;

    /**
    *  The number of EAPOL frames sent.
    */
    private Integer numTxEapol;

    /**
    *  The number of total LDPC frames sent.
    */
    private Integer numTxLdpc;

    /**
    *  The number of total STBC frames sent.
    */
    private Integer numTxStbc;

    /**
    *  The number of aggregation frames sent successfully.
    */
    private Integer numTxAggrSucc;

    /**
    *  The number of aggregation frames sent using single MPDU.
    */
    private Integer numTxAggrOneMpdu;

    /**
    *  The number of Tx frames dropped because of rate limit and burst exceeded.
    */
    private Integer numTxRateLimitDrop;
    
    // The number of retry tx attempts that have been made
    private Integer numTxRetryAttemps;
    
    // The total number of tx attempts
    private Integer numTxTotalAttemps;
    
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
            
    private Long numChannelBusy64s;
    private Long numTxTimeData;
    private Long numTxTime_BC_MC_Data;
    private Long numRxTimeData;
    private Long numTxFramesTransmitted;
    private Long numTxSuccessWithRetry;
    private Long numTxMultipleRetries;
    private Long numTxDataTransmittedRetried;
    private Long numTxDataTransmitted;
    private Long numTxDataFrames;
    private Long numRxFramesReceived;
    private Long numRxRetryFrames;
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

    public Integer getCurEirp() {
        return curEirp;
    }

    public Integer getElevenGProtection() {
        return elevenGProtection;
    }

    public Integer getNumChanChanges() {
        return numChanChanges;
    }

    public Integer getNumFreeTxBuf() {
        return numFreeTxBuf;
    }

    public Integer getNumRadarChanChanges() {
        return numRadarChanChanges;
    }

    public Integer getNumRadioResets() {
        return numRadioResets;
    }

    public Integer getNumRcvBcForTx() {
        return numRcvBcForTx;
    }

    public Long getNumRcvFrameForTx() {
        return numRcvFrameForTx;
    }


    public Integer getNumRx() {
        return numRx;
    }

    public Integer getNumRxAck() {
        return numRxAck;
    }

    public Integer getNumRxAmsduDeaggItmd() {
        return numRxAmsduDeaggItmd;
    }

    public Integer getNumRxAmsduDeaggLast() {
        return numRxAmsduDeaggLast;
    }

    public Integer getNumRxAmsduDeaggSeq() {
        return numRxAmsduDeaggSeq;
    }

    public Integer getNumRxBcMc() {
        return numRxBcMc;
    }

    public Integer getNumRxBeacon() {
        return numRxBeacon;
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

    public Integer getNumRxDropAmsduNoRcv() {
        return numRxDropAmsduNoRcv;
    }

    public Long getNumRxDropBadProtocol() {
        return numRxDropBadProtocol;
    }

    public Integer getNumRxDropEthHdrRunt() {
        return numRxDropEthHdrRunt;
    }

    public Integer getNumRxDropInvalidSrcMac() {
        return numRxDropInvalidSrcMac;
    }

    public Long getNumRxDropNoFcField() {
        return numRxDropNoFcField;
    }

    public Integer getNumRxDropRunt() {
        return numRxDropRunt;
    }

    public Integer getNumRxDup() {
        return numRxDup;
    }

    public Integer getNumRxErr() {
        return numRxErr;
    }

    public Integer getNumRxFcsErr() {
        return numRxFcsErr;
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

    public Integer getNumRxOffChan() {
        return numRxOffChan;
    }

    public Integer getNumRxProbeReq() {
        return numRxProbeReq;
    }

    public Integer getNumRxProbeResp() {
        return numRxProbeResp;
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

    public Integer getNumScanReq() {
        return numScanReq;
    }

    public Integer getNumScanSucc() {
        return numScanSucc;
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

    public Integer getNumTxBcDropped() {
        return numTxBcDropped;
    }

    public Integer getNumTxBeaconFail() {
        return numTxBeaconFail;
    }

    public Integer getNumTxBeaconSucc() {
        return numTxBeaconSucc;
    }

    public Integer getNumTxBeaconSuFail() {
        return numTxBeaconSuFail;
    }

    public Integer getNumTxControl() {
        return numTxControl;
    }

    public Integer getNumTxCts() {
        return numTxCts;
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

    public Integer getNumTxDtimMc() {
        return numTxDtimMc;
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

    public Integer getNumTxPowerChanges() {
        return numTxPowerChanges;
    }

    public Integer getNumTxProbeResp() {
        return numTxProbeResp;
    }

    public Integer getNumTxPsUnicast() {
        return numTxPsUnicast;
    }

    public Long getNumTxQueued() {
        return numTxQueued;
    }

    public Integer getNumTxRateLimitDrop() {
        return numTxRateLimitDrop;
    }

    public Integer getNumTxRetryAttemps() {
        return numTxRetryAttemps;
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

    public Integer getNumTxTotalAttemps() {
        return numTxTotalAttemps;
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

    public void setCurEirp(Integer curEirp) {
        this.curEirp = curEirp;
    }

    public void setElevenGProtection(Integer elevenGProtection) {
        this.elevenGProtection = elevenGProtection;
    }

    public void setNumChanChanges(Integer numChanChanges) {
        this.numChanChanges = numChanChanges;
    }

    public void setNumFreeTxBuf(Integer numFreeTxBuf) {
        this.numFreeTxBuf = numFreeTxBuf;
    }

    public void setNumRadarChanChanges(Integer numRadarChanChanges) {
        this.numRadarChanChanges = numRadarChanChanges;
    }

    public void setNumRadioResets(Integer numRadioResets) {
        this.numRadioResets = numRadioResets;
    }

    public void setNumRcvBcForTx(Integer numRcvBcForTx) {
        this.numRcvBcForTx = numRcvBcForTx;
    }

    public void setNumRcvFrameForTx(Long numRcvFrameForTx) {
        this.numRcvFrameForTx = numRcvFrameForTx;
    }

    public void setNumRx(Integer numRx) {
        this.numRx = numRx;
    }

    public void setNumRxAck(Integer numRxAck) {
        this.numRxAck = numRxAck;
    }

    public void setNumRxAmsduDeaggItmd(Integer numRxAmsduDeaggItmd) {
        this.numRxAmsduDeaggItmd = numRxAmsduDeaggItmd;
    }

    public void setNumRxAmsduDeaggLast(Integer numRxAmsduDeaggLast) {
        this.numRxAmsduDeaggLast = numRxAmsduDeaggLast;
    }

    public void setNumRxAmsduDeaggSeq(Integer numRxAmsduDeaggSeq) {
        this.numRxAmsduDeaggSeq = numRxAmsduDeaggSeq;
    }

    public void setNumRxBcMc(Integer numRxBcMc) {
        this.numRxBcMc = numRxBcMc;
    }

    public void setNumRxBeacon(Integer numRxBeacon) {
        this.numRxBeacon = numRxBeacon;
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

    public void setNumRxDropAmsduNoRcv(Integer numRxDropAmsduNoRcv) {
        this.numRxDropAmsduNoRcv = numRxDropAmsduNoRcv;
    }

    public void setNumRxDropBadProtocol(Long numRxDropBadProtocol) {
        this.numRxDropBadProtocol = numRxDropBadProtocol;
    }

    public void setNumRxDropEthHdrRunt(Integer numRxDropEthHdrRunt) {
        this.numRxDropEthHdrRunt = numRxDropEthHdrRunt;
    }

    public void setNumRxDropInvalidSrcMac(Integer numRxDropInvalidSrcMac) {
        this.numRxDropInvalidSrcMac = numRxDropInvalidSrcMac;
    }

    public void setNumRxDropNoFcField(Long numRxDropNoFcField) {
        this.numRxDropNoFcField = numRxDropNoFcField;
    }

    public void setNumRxDropRunt(Integer numRxDropRunt) {
        this.numRxDropRunt = numRxDropRunt;
    }

    public void setNumRxDup(Integer numRxDup) {
        this.numRxDup = numRxDup;
    }

    public void setNumRxErr(Integer numRxErr) {
        this.numRxErr = numRxErr;
    }

    public void setNumRxFcsErr(Integer numRxFcsErr) {
        this.numRxFcsErr = numRxFcsErr;
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

    public void setNumRxOffChan(Integer numRxOffChan) {
        this.numRxOffChan = numRxOffChan;
    }

    public void setNumRxProbeReq(Integer numRxProbeReq) {
        this.numRxProbeReq = numRxProbeReq;
    }

    public void setNumRxProbeResp(Integer numRxProbeResp) {
        this.numRxProbeResp = numRxProbeResp;
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

    public void setNumScanReq(Integer numScanReq) {
        this.numScanReq = numScanReq;
    }

    public void setNumScanSucc(Integer numScanSucc) {
        this.numScanSucc = numScanSucc;
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

    public void setNumTxBcDropped(Integer numTxBcDropped) {
        this.numTxBcDropped = numTxBcDropped;
    }

    public void setNumTxBeaconFail(Integer numTxBeaconFail) {
        this.numTxBeaconFail = numTxBeaconFail;
    }

    public void setNumTxBeaconSucc(Integer numTxBeaconSucc) {
        this.numTxBeaconSucc = numTxBeaconSucc;
    }

    public void setNumTxBeaconSuFail(Integer numTxBeaconSuFail) {
        this.numTxBeaconSuFail = numTxBeaconSuFail;
    }

    public void setNumTxControl(Integer numTxControl) {
        this.numTxControl = numTxControl;
    }

    public void setNumTxCts(Integer numTxCts) {
        this.numTxCts = numTxCts;
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

    public void setNumTxDtimMc(Integer numTxDtimMc) {
        this.numTxDtimMc = numTxDtimMc;
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

    public void setNumTxPowerChanges(Integer numTxPowerChanges) {
        this.numTxPowerChanges = numTxPowerChanges;
    }

    public void setNumTxProbeResp(Integer numTxProbeResp) {
        this.numTxProbeResp = numTxProbeResp;
    }

    public void setNumTxPsUnicast(Integer numTxPsUnicast) {
        this.numTxPsUnicast = numTxPsUnicast;
    }

    public void setNumTxQueued(Long numTxQueued) {
        this.numTxQueued = numTxQueued;
    }

    public void setNumTxRateLimitDrop(Integer numTxRateLimitDrop) {
        this.numTxRateLimitDrop = numTxRateLimitDrop;
    }

    public void setNumTxRetryAttemps(Integer numTxRetryAttemps) {
        this.numTxRetryAttemps = numTxRetryAttemps;
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

    public void setNumTxTotalAttemps(Integer numTxTotalAttemps) {
        this.numTxTotalAttemps = numTxTotalAttemps;
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

    public Integer getCurBackupChannel() {
        return curBackupChannel;
    }

    public void setCurBackupChannel(Integer curBackupChannel) {
        this.curBackupChannel = curBackupChannel;
    }

    public Long getNumTxDataFrames_12_Mbps() {
        return numTxDataFrames_12_Mbps;
    }

    public void setNumTxDataFrames_12_Mbps(Long numTxDataFrames_12_Mbps) {
        this.numTxDataFrames_12_Mbps = numTxDataFrames_12_Mbps;
    }

    public Long getNumTxDataFrames_54_Mbps() {
        return numTxDataFrames_54_Mbps;
    }

    public void setNumTxDataFrames_54_Mbps(Long numTxDataFrames_54_Mbps) {
        this.numTxDataFrames_54_Mbps = numTxDataFrames_54_Mbps;
    }

    public Long getNumTxDataFrames_108_Mbps() {
        return numTxDataFrames_108_Mbps;
    }

    public void setNumTxDataFrames_108_Mbps(Long numTxDataFrames_108_Mbps) {
        this.numTxDataFrames_108_Mbps = numTxDataFrames_108_Mbps;
    }

    public Long getNumTxDataFrames_300_Mbps() {
        return numTxDataFrames_300_Mbps;
    }

    public void setNumTxDataFrames_300_Mbps(Long numTxDataFrames_300_Mbps) {
        this.numTxDataFrames_300_Mbps = numTxDataFrames_300_Mbps;
    }

    public Long getNumTxDataFrames_450_Mbps() {
        return numTxDataFrames_450_Mbps;
    }

    public void setNumTxDataFrames_450_Mbps(Long numTxDataFrames_450_Mbps) {
        this.numTxDataFrames_450_Mbps = numTxDataFrames_450_Mbps;
    }

    public Long getNumTxDataFrames_1300_Mbps() {
        return numTxDataFrames_1300_Mbps;
    }

    public void setNumTxDataFrames_1300_Mbps(Long numTxDataFrames_1300_Mbps) {
        this.numTxDataFrames_1300_Mbps = numTxDataFrames_1300_Mbps;
    }

    public Long getNumTxDataFrames_1300Plus_Mbps() {
        return numTxDataFrames_1300Plus_Mbps;
    }

    public void setNumTxDataFrames_1300Plus_Mbps(Long numTxDataFrames_1300Plus_Mbps) {
        this.numTxDataFrames_1300Plus_Mbps = numTxDataFrames_1300Plus_Mbps;
    }

    public Long getNumRxDataFrames_12_Mbps() {
        return numRxDataFrames_12_Mbps;
    }

    public void setNumRxDataFrames_12_Mbps(Long numRxDataFrames_12_Mbps) {
        this.numRxDataFrames_12_Mbps = numRxDataFrames_12_Mbps;
    }

    public Long getNumRxDataFrames_54_Mbps() {
        return numRxDataFrames_54_Mbps;
    }

    public void setNumRxDataFrames_54_Mbps(Long numRxDataFrames_54_Mbps) {
        this.numRxDataFrames_54_Mbps = numRxDataFrames_54_Mbps;
    }

    public Long getNumRxDataFrames_108_Mbps() {
        return numRxDataFrames_108_Mbps;
    }

    public void setNumRxDataFrames_108_Mbps(Long numRxDataFrames_108_Mbps) {
        this.numRxDataFrames_108_Mbps = numRxDataFrames_108_Mbps;
    }

    public Long getNumRxDataFrames_300_Mbps() {
        return numRxDataFrames_300_Mbps;
    }

    public void setNumRxDataFrames_300_Mbps(Long numRxDataFrames_300_Mbps) {
        this.numRxDataFrames_300_Mbps = numRxDataFrames_300_Mbps;
    }

    public Long getNumRxDataFrames_450_Mbps() {
        return numRxDataFrames_450_Mbps;
    }

    public void setNumRxDataFrames_450_Mbps(Long numRxDataFrames_450_Mbps) {
        this.numRxDataFrames_450_Mbps = numRxDataFrames_450_Mbps;
    }

    public Long getNumRxDataFrames_1300_Mbps() {
        return numRxDataFrames_1300_Mbps;
    }

    public void setNumRxDataFrames_1300_Mbps(Long numRxDataFrames_1300_Mbps) {
        this.numRxDataFrames_1300_Mbps = numRxDataFrames_1300_Mbps;
    }

    public Long getNumRxDataFrames_1300Plus_Mbps() {
        return numRxDataFrames_1300Plus_Mbps;
    }

    public void setNumRxDataFrames_1300Plus_Mbps(Long numRxDataFrames_1300Plus_Mbps) {
        this.numRxDataFrames_1300Plus_Mbps = numRxDataFrames_1300Plus_Mbps;
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

    public Long getNumChannelBusy64s() {
        return numChannelBusy64s;
    }

    public void setNumChannelBusy64s(Long numChannelBusy64s) {
        this.numChannelBusy64s = numChannelBusy64s;
    }

    public Long getNumTxTimeData() {
        return numTxTimeData;
    }

    public void setNumTxTimeData(Long numTxTimeData) {
        this.numTxTimeData = numTxTimeData;
    }

    public Long getNumTxTime_BC_MC_Data() {
        return numTxTime_BC_MC_Data;
    }

    public void setNumTxTime_BC_MC_Data(Long numTxTime_BC_MC_Data) {
        this.numTxTime_BC_MC_Data = numTxTime_BC_MC_Data;
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

    public Long getNumRxRetryFrames() {
        return numRxRetryFrames;
    }

    public void setNumRxRetryFrames(Long numRxRetryFrames) {
        this.numRxRetryFrames = numRxRetryFrames;
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

}
