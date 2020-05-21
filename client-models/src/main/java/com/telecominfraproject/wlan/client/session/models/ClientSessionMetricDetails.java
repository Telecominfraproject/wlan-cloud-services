package com.telecominfraproject.wlan.client.session.models;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author ekeddy
 *
 */
public class ClientSessionMetricDetails extends BaseJsonModel 
{
    private static final long serialVersionUID = -6626815155700131150L;
    
    private static final Logger LOG = LoggerFactory.getLogger(ClientSessionMetricDetails.class);
    
    private Long rxBytes;
    private Long txBytes;
    private Long totalRxPackets;
    private Long totalTxPackets;
    private Float rxMbps;
    private Float txMbps;
    private Integer rssi;
    private Integer snr;
    private Long rxRateKbps; // from MCS
    private Long txRateKbps; // from MCS
    private long lastMetricTimestamp;
    private Long lastRxTimestamp;
    private Long lastTxTimestamp;
    private String classification;

    /**
     * The number of dataframes transmitted TO the client from the AP.
     */
    private Integer txDataFrames;
    
    /**
     * The number of data frames transmitted TO the client that were retried.
     * Note this is not the same as the number of retries.
     */
    private Integer txDataFramesRetried;

    /**
     * The number of dataframes transmitted FROM the client TO the AP.
     */
    private Integer rxDataFrames;
    

    public Long getRxBytes() {
        return rxBytes;
    }
    public void setRxBytes(Long rxBytes) {
        this.rxBytes = rxBytes;
    }
    public Long getTxBytes() {
        return txBytes;
    }
    public void setTxBytes(Long txBytes) {
        this.txBytes = txBytes;
    }
    public Long getTotalRxPackets() {
        return totalRxPackets;
    }
    public void setTotalRxPackets(Long totalRxPackets) {
        this.totalRxPackets = totalRxPackets;
    }
    public Long getTotalTxPackets() {
        return totalTxPackets;
    }
    public void setTotalTxPackets(Long totalTxPackets) {
        this.totalTxPackets = totalTxPackets;
    }
    public Float getRxMbps() {
        return rxMbps;
    }
    public void setRxMbps(Float rxMbps) {
        this.rxMbps = rxMbps;
    }
    public Float getTxMbps() {
        return txMbps;
    }
    public void setTxMbps(Float txMbps) {
        this.txMbps = txMbps;
    }
    public Integer getRssi() {
        return rssi;
    }
    public void setRssi(Integer rssi) {
        this.rssi = rssi;
    }
    public Integer getSnr() {
        return snr;
    }
    public void setSnr(Integer snr) {
        this.snr = snr;
    }
    public Long getRxRateKbps() {
        return rxRateKbps;
    }
    public void setRxRateKbps(Long rxRateKbps) {
        this.rxRateKbps = rxRateKbps;
    }
    public Long getTxRateKbps() {
        return txRateKbps;
    }
    public void setTxRateKbps(Long txRateKbps) {
        this.txRateKbps = txRateKbps;
    }
    
    
    public Integer getTxDataFrames() {
        return txDataFrames;
    }
    public void setTxDataFrames(Integer txDataFrames) {
        this.txDataFrames = txDataFrames;
    }
    public Integer getTxDataFramesRetried() {
        return txDataFramesRetried;
    }
    public void setTxDataFramesRetried(Integer txDataFramesRetried) {
        this.txDataFramesRetried = txDataFramesRetried;
    }
    
    public long getLastMetricTimestamp() {
        return lastMetricTimestamp;
    }
    public void setLastMetricTimestamp(long lastMetricTimestamp) {
        this.lastMetricTimestamp = lastMetricTimestamp;
    }
    
    public Long getLastRxTimestamp() {
        return lastRxTimestamp;
    }
    public void setLastRxTimestamp(Long lastRxTimestamp) {
        this.lastRxTimestamp = lastRxTimestamp;
    }
    public Long getLastTxTimestamp() {
        return lastTxTimestamp;
    }
    public void setLastTxTimestamp(Long lastTxTimestamp) {
        this.lastTxTimestamp = lastTxTimestamp;
    }
    
    
    public Integer getRxDataFrames() {
        return rxDataFrames;
    }
    public void setRxDataFrames(Integer rxDataFrames) {
        this.rxDataFrames = rxDataFrames;
    }
    public String getClassification() {
        return classification;
    }
    public void setClassification(String classification) {
        this.classification = classification;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((classification == null) ? 0 : classification.hashCode());
        result = prime * result + (int) (lastMetricTimestamp ^ (lastMetricTimestamp >>> 32));
        result = prime * result + ((lastRxTimestamp == null) ? 0 : lastRxTimestamp.hashCode());
        result = prime * result + ((lastTxTimestamp == null) ? 0 : lastTxTimestamp.hashCode());
        result = prime * result + ((rssi == null) ? 0 : rssi.hashCode());
        result = prime * result + ((rxBytes == null) ? 0 : rxBytes.hashCode());
        result = prime * result + ((rxDataFrames == null) ? 0 : rxDataFrames.hashCode());
        result = prime * result + ((rxMbps == null) ? 0 : rxMbps.hashCode());
        result = prime * result + ((rxRateKbps == null) ? 0 : rxRateKbps.hashCode());
        result = prime * result + ((snr == null) ? 0 : snr.hashCode());
        result = prime * result + ((totalRxPackets == null) ? 0 : totalRxPackets.hashCode());
        result = prime * result + ((totalTxPackets == null) ? 0 : totalTxPackets.hashCode());
        result = prime * result + ((txBytes == null) ? 0 : txBytes.hashCode());
        result = prime * result + ((txDataFrames == null) ? 0 : txDataFrames.hashCode());
        result = prime * result + ((txDataFramesRetried == null) ? 0 : txDataFramesRetried.hashCode());
        result = prime * result + ((txMbps == null) ? 0 : txMbps.hashCode());
        result = prime * result + ((txRateKbps == null) ? 0 : txRateKbps.hashCode());
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
        ClientSessionMetricDetails other = (ClientSessionMetricDetails) obj;
        if (classification == null) {
            if (other.classification != null)
                return false;
        } else if (!classification.equals(other.classification))
            return false;
        if (lastMetricTimestamp != other.lastMetricTimestamp)
            return false;
        if (lastRxTimestamp == null) {
            if (other.lastRxTimestamp != null)
                return false;
        } else if (!lastRxTimestamp.equals(other.lastRxTimestamp))
            return false;
        if (lastTxTimestamp == null) {
            if (other.lastTxTimestamp != null)
                return false;
        } else if (!lastTxTimestamp.equals(other.lastTxTimestamp))
            return false;
        if (rssi == null) {
            if (other.rssi != null)
                return false;
        } else if (!rssi.equals(other.rssi))
            return false;
        if (rxBytes == null) {
            if (other.rxBytes != null)
                return false;
        } else if (!rxBytes.equals(other.rxBytes))
            return false;
        if (rxDataFrames == null) {
            if (other.rxDataFrames != null)
                return false;
        } else if (!rxDataFrames.equals(other.rxDataFrames))
            return false;
        if (rxMbps == null) {
            if (other.rxMbps != null)
                return false;
        } else if (!rxMbps.equals(other.rxMbps))
            return false;
        if (rxRateKbps == null) {
            if (other.rxRateKbps != null)
                return false;
        } else if (!rxRateKbps.equals(other.rxRateKbps))
            return false;
        if (snr == null) {
            if (other.snr != null)
                return false;
        } else if (!snr.equals(other.snr))
            return false;
        if (totalRxPackets == null) {
            if (other.totalRxPackets != null)
                return false;
        } else if (!totalRxPackets.equals(other.totalRxPackets))
            return false;
        if (totalTxPackets == null) {
            if (other.totalTxPackets != null)
                return false;
        } else if (!totalTxPackets.equals(other.totalTxPackets))
            return false;
        if (txBytes == null) {
            if (other.txBytes != null)
                return false;
        } else if (!txBytes.equals(other.txBytes))
            return false;
        if (txDataFrames == null) {
            if (other.txDataFrames != null)
                return false;
        } else if (!txDataFrames.equals(other.txDataFrames))
            return false;
        if (txDataFramesRetried == null) {
            if (other.txDataFramesRetried != null)
                return false;
        } else if (!txDataFramesRetried.equals(other.txDataFramesRetried))
            return false;
        if (txMbps == null) {
            if (other.txMbps != null)
                return false;
        } else if (!txMbps.equals(other.txMbps))
            return false;
        if (txRateKbps == null) {
            if (other.txRateKbps != null)
                return false;
        } else if (!txRateKbps.equals(other.txRateKbps))
            return false;
        return true;
    }
    

    @Override
    public ClientSessionMetricDetails clone() {
        ClientSessionMetricDetails ret = (ClientSessionMetricDetails) super.clone();
        return ret;
    }

    public void merge(ClientSessionMetricDetails other) {
        boolean isLatestMetric = false;
        if(lastMetricTimestamp<other.lastMetricTimestamp) {
            this.setLastMetricTimestamp(other.lastMetricTimestamp);
            isLatestMetric = true;
        }

        // These properties just take the latest value
        if(isLatestMetric) {
            if(other.rssi != null) {
                this.rssi = other.rssi;
            }
            if(other.rxMbps != null) {
                this.rxMbps = other.rxMbps;
            }
            if(other.txMbps != null) {
                this.txMbps = other.txMbps;
            }
            if(other.rxRateKbps != null) {
                this.rxRateKbps = other.rxRateKbps;
            }
            if(other.txRateKbps != null) {
                this.txRateKbps = other.txRateKbps; 
            }
            if(other.totalRxPackets != null) {
                this.totalRxPackets = other.totalRxPackets;
            }
            if(other.totalTxPackets != null) {
                this.totalTxPackets = other.totalTxPackets;
            }
            if(other.lastRxTimestamp != null) {
                this.lastRxTimestamp = other.lastRxTimestamp;
            }
            if(other.lastTxTimestamp != null) {
                this.lastTxTimestamp = other.lastTxTimestamp;
            }
        }
        if(other.txDataFrames != null) {
            // this is a delta
            this.setTxDataFrames(sum(this.txDataFrames,other.txDataFrames));
        }
        if(other.txDataFramesRetried != null) {
            // this is a delta
            this.setTxDataFramesRetried(sum(this.txDataFramesRetried,other.txDataFramesRetried));
        }
        if(other.rxDataFrames != null) {
            // this is a delta
            this.setRxDataFrames(sum(this.rxDataFrames,other.rxDataFrames));
        }
        if(other.rxBytes != null)
        {
            // We keep the sum going
            LOG.trace("RxBytes {}: adding {} to {}", this.rxBytes, other.rxBytes);
            this.setRxBytes(sum(this.rxBytes, other.rxBytes));
        }
        if(other.txBytes != null)
        {
            // We keep the sum going
            LOG.trace("TxBytes {}: adding {} to {}", this.txBytes, other.txBytes);
            this.setTxBytes(sum(this.txBytes, other.txBytes));
        }
        
        if(other.classification != null)
        {
            this.classification = other.classification;
        }

    }

    private static Integer sum(Integer v1, Integer v2) {
        if(v1 == null) return v2;
        if(v2 == null) return v1;
        return Integer.sum(v1, v2);
    }

    
    private static Long sum(Long v1, Long v2) {
        if(v1 == null) return v2;
        if(v2 == null) return v1;
        return Long.sum(v1, v2);
    }

    
}
