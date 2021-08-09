package com.telecominfraproject.wlan.servicemetric.apssid.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasSourceTimestamp;

/**
 * @author ekeddy
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SsidStatistics extends BaseJsonModel implements HasSourceTimestamp {

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
     * The number of received bytes.
     */
    private Long rxBytes;

    /**
     * The number of received retry frames.
     */
    private Integer numRxRetry;

    /**
     * The number of received ethernet and local generated frames for transmit.
     */
    private Long numRcvFrameForTx;

    /**
     * The number of bytes successfully transmitted.
     */
    private Long numTxBytesSucc;

    /**
     * The number of Tx data frames with retries.
     */
    private Integer numTxDataRetries;
    
    /**
     * Timestamp from the AP source statistics used for this metric
     */
    private long sourceTimestampMs;

    @Override
    public SsidStatistics clone() {
        SsidStatistics ret = (SsidStatistics) super.clone();
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
        if (numClient == null) {
            if (other.numClient != null) {
                return false;
            }
        } else if (!numClient.equals(other.numClient)) {
            return false;
        }
        if (numRcvFrameForTx == null) {
            if (other.numRcvFrameForTx != null) {
                return false;
            }
        } else if (!numRcvFrameForTx.equals(other.numRcvFrameForTx)) {
            return false;
        }
        if (numRxData == null) {
            if (other.numRxData != null) {
                return false;
            }
        } else if (!numRxData.equals(other.numRxData)) {
            return false;
        }
        if (numRxNoFcsErr == null) {
            if (other.numRxNoFcsErr != null) {
                return false;
            }
        } else if (!numRxNoFcsErr.equals(other.numRxNoFcsErr)) {
            return false;
        }
        if (numRxRetry == null) {
            if (other.numRxRetry != null) {
                return false;
            }
        } else if (!numRxRetry.equals(other.numRxRetry)) {
            return false;
        }
        if (numTxBytesSucc == null) {
            if (other.numTxBytesSucc != null) {
                return false;
            }
        } else if (!numTxBytesSucc.equals(other.numTxBytesSucc)) {
            return false;
        }
        if (numTxDataRetries == null) {
            if (other.numTxDataRetries != null) {
                return false;
            }
        } else if (!numTxDataRetries.equals(other.numTxDataRetries)) {
            return false;
        }
        if (rxBytes == null) {
            if (other.rxBytes != null) {
                return false;
            }
        } else if (!rxBytes.equals(other.rxBytes)) {
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
        return true;
    }

    public MacAddress getBssid() {
        return bssid;
    }

    public Integer getNumClient() {
        return numClient;
    }

    public Long getNumRcvFrameForTx() {
        return numRcvFrameForTx;
    }

    public Integer getNumRxData() {
        return numRxData;
    }

    public Integer getNumRxNoFcsErr() {
        return numRxNoFcsErr;
    }

    public Integer getNumRxRetry() {
        return numRxRetry;
    }

    public Long getNumTxBytesSucc() {
        return numTxBytesSucc;
    }

    public Integer getNumTxDataRetries() {
        return numTxDataRetries;
    }

    public Long getRxBytes() {
        return rxBytes;
    }

    public Integer getRxLastRssi() {
        return rxLastRssi;
    }

    public String getSsid() {
        return ssid;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((bssid == null) ? 0 : bssid.hashCode());
        result = prime * result + ((numClient == null) ? 0 : numClient.hashCode());
        result = prime * result + ((numRcvFrameForTx == null) ? 0 : numRcvFrameForTx.hashCode());
        result = prime * result + ((numRxData == null) ? 0 : numRxData.hashCode());
        result = prime * result + ((numRxNoFcsErr == null) ? 0 : numRxNoFcsErr.hashCode());
        result = prime * result + ((numRxRetry == null) ? 0 : numRxRetry.hashCode());
        result = prime * result + ((numTxBytesSucc == null) ? 0 : numTxBytesSucc.hashCode());
        result = prime * result + ((numTxDataRetries == null) ? 0 : numTxDataRetries.hashCode());
        result = prime * result + ((rxBytes == null) ? 0 : rxBytes.hashCode());
        result = prime * result + ((rxLastRssi == null) ? 0 : rxLastRssi.hashCode());
        result = prime * result + ((ssid == null) ? 0 : ssid.hashCode());
        return result;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        return false;
    }

    public void setBssid(MacAddress bssid) {
        this.bssid = bssid;
    }

    public void setNumClient(Integer numClient) {
        this.numClient = numClient;
    }

    public void setNumRcvFrameForTx(Long numRcvFrameForTx) {
        this.numRcvFrameForTx = numRcvFrameForTx;
    }

    public void setNumRxData(Integer numRxData) {
        this.numRxData = numRxData;
    }

    public void setNumRxNoFcsErr(Integer numRxNoFcsErr) {
        this.numRxNoFcsErr = numRxNoFcsErr;
    }

    public void setNumRxRetry(Integer numRxRetry) {
        this.numRxRetry = numRxRetry;
    }

    public void setNumTxBytesSucc(Long numTxBytesSucc) {
        this.numTxBytesSucc = numTxBytesSucc;
    }

    public void setNumTxDataRetries(Integer numTxDataRetries) {
        this.numTxDataRetries = numTxDataRetries;
    }

    public void setRxBytes(Long rxBytes) {
        this.rxBytes = rxBytes;
    }

    public void setRxLastRssi(Integer rxLastRssi) {
        this.rxLastRssi = rxLastRssi;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }
    
    public void setSourceTimestampMs(long sourceTimestamp) {
        this.sourceTimestampMs = sourceTimestamp;
    }
    
    @Override
    public long getSourceTimestampMs() {
        return this.sourceTimestampMs;
    }
}
