package com.telecominfraproject.wlan.servicemetrics.models;

import java.net.InetAddress;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * Tunnel Metric Data
 * 
 * @author yongli
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class TunnelMetricData extends BaseJsonModel {
    
	private static final long serialVersionUID = 7183171446778118080L;

    /**
     * IP address of tunnel peer
     */
    private InetAddress ipAddr;
    /**
     * number of seconds tunnel was configured
     */
    private Long cfgTime;
    /**
     * number of seconds tunnel was up in current bin
     */
    private Long upTime;
    /**
     * number of 'ping' sent in the current bin in case tunnel was DOWN
     */
    private Long pingsSent;
    /**
     * number of 'ping' response received by peer in the current bin in case
     * tunnel was DOWN
     */
    private Long pingsRecvd;
    /**
     * Indicate if the current tunnel is the active one
     */
    private Boolean activeTun;

    public InetAddress getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(InetAddress ipAddr) {
        this.ipAddr = ipAddr;
    }

    public Long getCfgTime() {
        return cfgTime;
    }

    public void setCfgTime(Long cfgTime) {
        this.cfgTime = cfgTime;
    }

    public Long getUpTime() {
        return upTime;
    }

    public void setUpTime(Long upTime) {
        this.upTime = upTime;
    }

    public Long getPingsSent() {
        return pingsSent;
    }

    public void setPingsSent(Long pingsSent) {
        this.pingsSent = pingsSent;
    }

    public Long getPingsRecvd() {
        return pingsRecvd;
    }

    public void setPingsRecvd(Long pingsRecvd) {
        this.pingsRecvd = pingsRecvd;
    }

    public Boolean getActiveTun() {
        return activeTun;
    }

    public void setActiveTun(Boolean activeTun) {
        this.activeTun = activeTun;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((activeTun == null) ? 0 : activeTun.hashCode());
        result = prime * result + ((cfgTime == null) ? 0 : cfgTime.hashCode());
        result = prime * result + ((ipAddr == null) ? 0 : ipAddr.hashCode());
        result = prime * result + ((pingsRecvd == null) ? 0 : pingsRecvd.hashCode());
        result = prime * result + ((pingsSent == null) ? 0 : pingsSent.hashCode());
        result = prime * result + ((upTime == null) ? 0 : upTime.hashCode());
        return result;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof TunnelMetricData)) {
            return false;
        }
        TunnelMetricData other = (TunnelMetricData) obj;
        if (activeTun == null) {
            if (other.activeTun != null) {
                return false;
            }
        } else if (!activeTun.equals(other.activeTun)) {
            return false;
        }
        if (cfgTime == null) {
            if (other.cfgTime != null) {
                return false;
            }
        } else if (!cfgTime.equals(other.cfgTime)) {
            return false;
        }
        if (ipAddr == null) {
            if (other.ipAddr != null) {
                return false;
            }
        } else if (!ipAddr.equals(other.ipAddr)) {
            return false;
        }
        if (pingsRecvd == null) {
            if (other.pingsRecvd != null) {
                return false;
            }
        } else if (!pingsRecvd.equals(other.pingsRecvd)) {
            return false;
        }
        if (pingsSent == null) {
            if (other.pingsSent != null) {
                return false;
            }
        } else if (!pingsSent.equals(other.pingsSent)) {
            return false;
        }
        if (upTime == null) {
            if (other.upTime != null) {
                return false;
            }
        } else if (!upTime.equals(other.upTime)) {
            return false;
        }
        return true;
    }
    
    @Override
    public TunnelMetricData clone() {
        return (TunnelMetricData) super.clone();
    }
}
