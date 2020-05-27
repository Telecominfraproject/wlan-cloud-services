package com.telecominfraproject.wlan.servicemetrics.models;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.servicemetrics.models.WmmQueueStats.WmmQueueType;

/**
 * @author ekeddy
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SsidStatistics extends BaseJsonModel {

    private static final long serialVersionUID = 8903368367046455020L;

    /**
     * SSID
     */
    private String ssid;

    /**
     * BSSID six bytes
     */
    private MacAddress bssid;

    /**
     * Number client associated to this BSS
     */
    private Integer numClient;

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
     * The number of received ethernet and local generated frames for transmit.
     */
    private Long numRcvFrameForTx;

    /**
     * The number of TX frames queued.
     */
    private Long numTxQueued;

    /**
     * The number of received ethernet and local generated broadcast frames for
     * transmit.
     */
    private Integer numRcvBcForTx;

    /**
     * The number of every TX frame dropped.
     */
    private Integer numTxDropped;

    /**
     * The number of TX frame dropped due to retries.
     */
    private Integer numTxRetryDropped;

    /**
     * The number of broadcast frames dropped.
     */
    private Integer numTxBcDropped;

    /**
     * The number of frames successfully transmitted.
     */
    private Integer numTxSucc;

    /**
     * The number of bytes successfully transmitted.
     */
    private Long numTxBytesSucc;

    /**
     * The number of transmitted PS unicast frame.
     */
    private Integer numTxPsUnicast;

    /**
     * The number of transmitted DTIM multicast frames.
     */
    private Integer numTxDtimMc;

    /**
     * The number of successfully transmitted frames at firt attemp.
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
     * The number of Tx data frames with retries.
     */
    private Integer numTxDataRetries;

    /**
     * The number of RTS frames sent successfully.
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

    private Map<WmmQueueType, WmmQueueStats> wmmQueueStats;

    private List<McsStats> mcsStats;

    @Override
    public SsidStatistics clone() {
        SsidStatistics ret = (SsidStatistics) super.clone();
        if (this.wmmQueueStats != null) {
            ret.wmmQueueStats = new EnumMap<>(this.wmmQueueStats);
        }
        if (this.mcsStats != null) {
            ret.mcsStats = new ArrayList<>(this.mcsStats);
        }
        return ret;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof SsidStatistics)) {
            return false;
        }
        SsidStatistics other = (SsidStatistics) obj;
        if (bssid == null) {
            if (other.bssid != null) {
                return false;
            }
        } else if (!bssid.equals(other.bssid)) {
            return false;
        }
        if (mcsStats == null) {
            if (other.mcsStats != null) {
                return false;
            }
        } else if (!mcsStats.equals(other.mcsStats)) {
            return false;
        }
        if (numClient == null) {
            if (other.numClient != null) {
                return false;
            }
        } else if (!numClient.equals(other.numClient)) {
            return false;
        }
        if (numRcvBcForTx == null) {
            if (other.numRcvBcForTx != null) {
                return false;
            }
        } else if (!numRcvBcForTx.equals(other.numRcvBcForTx)) {
            return false;
        }
        if (numRcvFrameForTx == null) {
            if (other.numRcvFrameForTx != null) {
                return false;
            }
        } else if (!numRcvFrameForTx.equals(other.numRcvFrameForTx)) {
            return false;
        }
        if (numRxAck == null) {
            if (other.numRxAck != null) {
                return false;
            }
        } else if (!numRxAck.equals(other.numRxAck)) {
            return false;
        }
        if (numRxControl == null) {
            if (other.numRxControl != null) {
                return false;
            }
        } else if (!numRxControl.equals(other.numRxControl)) {
            return false;
        }
        if (numRxCts == null) {
            if (other.numRxCts != null) {
                return false;
            }
        } else if (!numRxCts.equals(other.numRxCts)) {
            return false;
        }
        if (numRxData == null) {
            if (other.numRxData != null) {
                return false;
            }
        } else if (!numRxData.equals(other.numRxData)) {
            return false;
        }
        if (numRxDup == null) {
            if (other.numRxDup != null) {
                return false;
            }
        } else if (!numRxDup.equals(other.numRxDup)) {
            return false;
        }
        if (numRxLdpc == null) {
            if (other.numRxLdpc != null) {
                return false;
            }
        } else if (!numRxLdpc.equals(other.numRxLdpc)) {
            return false;
        }
        if (numRxManagement == null) {
            if (other.numRxManagement != null) {
                return false;
            }
        } else if (!numRxManagement.equals(other.numRxManagement)) {
            return false;
        }
        if (numRxNoFcsErr == null) {
            if (other.numRxNoFcsErr != null) {
                return false;
            }
        } else if (!numRxNoFcsErr.equals(other.numRxNoFcsErr)) {
            return false;
        }
        if (numRxNullData == null) {
            if (other.numRxNullData != null) {
                return false;
            }
        } else if (!numRxNullData.equals(other.numRxNullData)) {
            return false;
        }
        if (numRxProbeReq == null) {
            if (other.numRxProbeReq != null) {
                return false;
            }
        } else if (!numRxProbeReq.equals(other.numRxProbeReq)) {
            return false;
        }
        if (numRxPspoll == null) {
            if (other.numRxPspoll != null) {
                return false;
            }
        } else if (!numRxPspoll.equals(other.numRxPspoll)) {
            return false;
        }
        if (numRxRetry == null) {
            if (other.numRxRetry != null) {
                return false;
            }
        } else if (!numRxRetry.equals(other.numRxRetry)) {
            return false;
        }
        if (numRxRts == null) {
            if (other.numRxRts != null) {
                return false;
            }
        } else if (!numRxRts.equals(other.numRxRts)) {
            return false;
        }
        if (numRxStbc == null) {
            if (other.numRxStbc != null) {
                return false;
            }
        } else if (!numRxStbc.equals(other.numRxStbc)) {
            return false;
        }
        if (numTxAction == null) {
            if (other.numTxAction != null) {
                return false;
            }
        } else if (!numTxAction.equals(other.numTxAction)) {
            return false;
        }
        if (numTxAggrOneMpdu == null) {
            if (other.numTxAggrOneMpdu != null) {
                return false;
            }
        } else if (!numTxAggrOneMpdu.equals(other.numTxAggrOneMpdu)) {
            return false;
        }
        if (numTxAggrSucc == null) {
            if (other.numTxAggrSucc != null) {
                return false;
            }
        } else if (!numTxAggrSucc.equals(other.numTxAggrSucc)) {
            return false;
        }
        if (numTxBcDropped == null) {
            if (other.numTxBcDropped != null) {
                return false;
            }
        } else if (!numTxBcDropped.equals(other.numTxBcDropped)) {
            return false;
        }
        if (numTxBytesSucc == null) {
            if (other.numTxBytesSucc != null) {
                return false;
            }
        } else if (!numTxBytesSucc.equals(other.numTxBytesSucc)) {
            return false;
        }
        if (numTxControl == null) {
            if (other.numTxControl != null) {
                return false;
            }
        } else if (!numTxControl.equals(other.numTxControl)) {
            return false;
        }
        if (numTxData == null) {
            if (other.numTxData != null) {
                return false;
            }
        } else if (!numTxData.equals(other.numTxData)) {
            return false;
        }
        if (numTxDataRetries == null) {
            if (other.numTxDataRetries != null) {
                return false;
            }
        } else if (!numTxDataRetries.equals(other.numTxDataRetries)) {
            return false;
        }
        if (numTxDropped == null) {
            if (other.numTxDropped != null) {
                return false;
            }
        } else if (!numTxDropped.equals(other.numTxDropped)) {
            return false;
        }
        if (numTxDtimMc == null) {
            if (other.numTxDtimMc != null) {
                return false;
            }
        } else if (!numTxDtimMc.equals(other.numTxDtimMc)) {
            return false;
        }
        if (numTxEapol == null) {
            if (other.numTxEapol != null) {
                return false;
            }
        } else if (!numTxEapol.equals(other.numTxEapol)) {
            return false;
        }
        if (numTxLdpc == null) {
            if (other.numTxLdpc != null) {
                return false;
            }
        } else if (!numTxLdpc.equals(other.numTxLdpc)) {
            return false;
        }
        if (numTxManagement == null) {
            if (other.numTxManagement != null) {
                return false;
            }
        } else if (!numTxManagement.equals(other.numTxManagement)) {
            return false;
        }
        if (numTxMultiRetries == null) {
            if (other.numTxMultiRetries != null) {
                return false;
            }
        } else if (!numTxMultiRetries.equals(other.numTxMultiRetries)) {
            return false;
        }
        if (numTxNoAck == null) {
            if (other.numTxNoAck != null) {
                return false;
            }
        } else if (!numTxNoAck.equals(other.numTxNoAck)) {
            return false;
        }
        if (numTxPropResp == null) {
            if (other.numTxPropResp != null) {
                return false;
            }
        } else if (!numTxPropResp.equals(other.numTxPropResp)) {
            return false;
        }
        if (numTxPsUnicast == null) {
            if (other.numTxPsUnicast != null) {
                return false;
            }
        } else if (!numTxPsUnicast.equals(other.numTxPsUnicast)) {
            return false;
        }
        if (numTxQueued == null) {
            if (other.numTxQueued != null) {
                return false;
            }
        } else if (!numTxQueued.equals(other.numTxQueued)) {
            return false;
        }
        if (numTxRetryDropped == null) {
            if (other.numTxRetryDropped != null) {
                return false;
            }
        } else if (!numTxRetryDropped.equals(other.numTxRetryDropped)) {
            return false;
        }
        if (numTxRtsFail == null) {
            if (other.numTxRtsFail != null) {
                return false;
            }
        } else if (!numTxRtsFail.equals(other.numTxRtsFail)) {
            return false;
        }
        if (numTxRtsSucc == null) {
            if (other.numTxRtsSucc != null) {
                return false;
            }
        } else if (!numTxRtsSucc.equals(other.numTxRtsSucc)) {
            return false;
        }
        if (numTxStbc == null) {
            if (other.numTxStbc != null) {
                return false;
            }
        } else if (!numTxStbc.equals(other.numTxStbc)) {
            return false;
        }
        if (numTxSucc == null) {
            if (other.numTxSucc != null) {
                return false;
            }
        } else if (!numTxSucc.equals(other.numTxSucc)) {
            return false;
        }
        if (numTxSuccNoRetry == null) {
            if (other.numTxSuccNoRetry != null) {
                return false;
            }
        } else if (!numTxSuccNoRetry.equals(other.numTxSuccNoRetry)) {
            return false;
        }
        if (numTxSuccRetries == null) {
            if (other.numTxSuccRetries != null) {
                return false;
            }
        } else if (!numTxSuccRetries.equals(other.numTxSuccRetries)) {
            return false;
        }
        if (rxBytes == null) {
            if (other.rxBytes != null) {
                return false;
            }
        } else if (!rxBytes.equals(other.rxBytes)) {
            return false;
        }
        if (rxDataBytes == null) {
            if (other.rxDataBytes != null) {
                return false;
            }
        } else if (!rxDataBytes.equals(other.rxDataBytes)) {
            return false;
        }
        if (rxLastRssi == null) {
            if (other.rxLastRssi != null) {
                return false;
            }
        } else if (!rxLastRssi.equals(other.rxLastRssi)) {
            return false;
        }
        if (ssid == null) {
            if (other.ssid != null) {
                return false;
            }
        } else if (!ssid.equals(other.ssid)) {
            return false;
        }
        if (wmmQueueStats == null) {
            if (other.wmmQueueStats != null) {
                return false;
            }
        } else if (!wmmQueueStats.equals(other.wmmQueueStats)) {
            return false;
        }
        return true;
    }

    public MacAddress getBssid() {
        return bssid;
    }

    public List<McsStats> getMcsStats() {
        return mcsStats;
    }

    public Integer getNumClient() {
        return numClient;
    }

    public Integer getNumRcvBcForTx() {
        return numRcvBcForTx;
    }

    public Long getNumRcvFrameForTx() {
        return numRcvFrameForTx;
    }

    public Integer getNumRxAck() {
        return numRxAck;
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

    public Integer getNumTxBcDropped() {
        return numTxBcDropped;
    }

    public Long getNumTxBytesSucc() {
        return numTxBytesSucc;
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

    public Integer getNumTxPropResp() {
        return numTxPropResp;
    }

    public Integer getNumTxPsUnicast() {
        return numTxPsUnicast;
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

    public Long getRxBytes() {
        return rxBytes;
    }

    public Long getRxDataBytes() {
        return rxDataBytes;
    }

    public Integer getRxLastRssi() {
        return rxLastRssi;
    }

    public String getSsid() {
        return ssid;
    }

    public Map<WmmQueueType, WmmQueueStats> getWmmQueueStats() {
        return wmmQueueStats;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bssid == null) ? 0 : bssid.hashCode());
        result = prime * result + ((mcsStats == null) ? 0 : mcsStats.hashCode());
        result = prime * result + ((numClient == null) ? 0 : numClient.hashCode());
        result = prime * result + ((numRcvBcForTx == null) ? 0 : numRcvBcForTx.hashCode());
        result = prime * result + ((numRcvFrameForTx == null) ? 0 : numRcvFrameForTx.hashCode());
        result = prime * result + ((numRxAck == null) ? 0 : numRxAck.hashCode());
        result = prime * result + ((numRxControl == null) ? 0 : numRxControl.hashCode());
        result = prime * result + ((numRxCts == null) ? 0 : numRxCts.hashCode());
        result = prime * result + ((numRxData == null) ? 0 : numRxData.hashCode());
        result = prime * result + ((numRxDup == null) ? 0 : numRxDup.hashCode());
        result = prime * result + ((numRxLdpc == null) ? 0 : numRxLdpc.hashCode());
        result = prime * result + ((numRxManagement == null) ? 0 : numRxManagement.hashCode());
        result = prime * result + ((numRxNoFcsErr == null) ? 0 : numRxNoFcsErr.hashCode());
        result = prime * result + ((numRxNullData == null) ? 0 : numRxNullData.hashCode());
        result = prime * result + ((numRxProbeReq == null) ? 0 : numRxProbeReq.hashCode());
        result = prime * result + ((numRxPspoll == null) ? 0 : numRxPspoll.hashCode());
        result = prime * result + ((numRxRetry == null) ? 0 : numRxRetry.hashCode());
        result = prime * result + ((numRxRts == null) ? 0 : numRxRts.hashCode());
        result = prime * result + ((numRxStbc == null) ? 0 : numRxStbc.hashCode());
        result = prime * result + ((numTxAction == null) ? 0 : numTxAction.hashCode());
        result = prime * result + ((numTxAggrOneMpdu == null) ? 0 : numTxAggrOneMpdu.hashCode());
        result = prime * result + ((numTxAggrSucc == null) ? 0 : numTxAggrSucc.hashCode());
        result = prime * result + ((numTxBcDropped == null) ? 0 : numTxBcDropped.hashCode());
        result = prime * result + ((numTxBytesSucc == null) ? 0 : numTxBytesSucc.hashCode());
        result = prime * result + ((numTxControl == null) ? 0 : numTxControl.hashCode());
        result = prime * result + ((numTxData == null) ? 0 : numTxData.hashCode());
        result = prime * result + ((numTxDataRetries == null) ? 0 : numTxDataRetries.hashCode());
        result = prime * result + ((numTxDropped == null) ? 0 : numTxDropped.hashCode());
        result = prime * result + ((numTxDtimMc == null) ? 0 : numTxDtimMc.hashCode());
        result = prime * result + ((numTxEapol == null) ? 0 : numTxEapol.hashCode());
        result = prime * result + ((numTxLdpc == null) ? 0 : numTxLdpc.hashCode());
        result = prime * result + ((numTxManagement == null) ? 0 : numTxManagement.hashCode());
        result = prime * result + ((numTxMultiRetries == null) ? 0 : numTxMultiRetries.hashCode());
        result = prime * result + ((numTxNoAck == null) ? 0 : numTxNoAck.hashCode());
        result = prime * result + ((numTxPropResp == null) ? 0 : numTxPropResp.hashCode());
        result = prime * result + ((numTxPsUnicast == null) ? 0 : numTxPsUnicast.hashCode());
        result = prime * result + ((numTxQueued == null) ? 0 : numTxQueued.hashCode());
        result = prime * result + ((numTxRetryDropped == null) ? 0 : numTxRetryDropped.hashCode());
        result = prime * result + ((numTxRtsFail == null) ? 0 : numTxRtsFail.hashCode());
        result = prime * result + ((numTxRtsSucc == null) ? 0 : numTxRtsSucc.hashCode());
        result = prime * result + ((numTxStbc == null) ? 0 : numTxStbc.hashCode());
        result = prime * result + ((numTxSucc == null) ? 0 : numTxSucc.hashCode());
        result = prime * result + ((numTxSuccNoRetry == null) ? 0 : numTxSuccNoRetry.hashCode());
        result = prime * result + ((numTxSuccRetries == null) ? 0 : numTxSuccRetries.hashCode());
        result = prime * result + ((rxBytes == null) ? 0 : rxBytes.hashCode());
        result = prime * result + ((rxDataBytes == null) ? 0 : rxDataBytes.hashCode());
        result = prime * result + ((rxLastRssi == null) ? 0 : rxLastRssi.hashCode());
        result = prime * result + ((ssid == null) ? 0 : ssid.hashCode());
        result = prime * result + ((wmmQueueStats == null) ? 0 : wmmQueueStats.hashCode());
        return result;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }

        if (hasUnsupportedValue(wmmQueueStats) || hasUnsupportedValue(mcsStats)) {
            return true;
        }
        return false;
    }

    public void setBssid(MacAddress bssid) {
        this.bssid = bssid;
    }

    public void setMcsStats(List<McsStats> mcsStats) {
        this.mcsStats = mcsStats;
    }

    public void setNumClient(Integer numClient) {
        this.numClient = numClient;
    }

    public void setNumRcvBcForTx(Integer numRcvBcForTx) {
        this.numRcvBcForTx = numRcvBcForTx;
    }

    public void setNumRcvFrameForTx(Long numRcvFrameForTx) {
        this.numRcvFrameForTx = numRcvFrameForTx;
    }

    public void setNumRxAck(Integer numRxAck) {
        this.numRxAck = numRxAck;
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

    public void setNumTxBcDropped(Integer numTxBcDropped) {
        this.numTxBcDropped = numTxBcDropped;
    }

    public void setNumTxBytesSucc(Long numTxBytesSucc) {
        this.numTxBytesSucc = numTxBytesSucc;
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

    public void setNumTxPropResp(Integer numTxPropResp) {
        this.numTxPropResp = numTxPropResp;
    }

    public void setNumTxPsUnicast(Integer numTxPsUnicast) {
        this.numTxPsUnicast = numTxPsUnicast;
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

    public void setRxBytes(Long rxBytes) {
        this.rxBytes = rxBytes;
    }

    public void setRxDataBytes(Long rxDataBytes) {
        this.rxDataBytes = rxDataBytes;
    }

    public void setRxLastRssi(Integer rxLastRssi) {
        this.rxLastRssi = rxLastRssi;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public void setWmmQueueStats(Map<WmmQueueType, WmmQueueStats> wmmQueueStats) {
        this.wmmQueueStats = wmmQueueStats;
    }
}
