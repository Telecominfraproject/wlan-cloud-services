package com.telecominfraproject.wlan.servicemetric.apnode.models;

import java.net.InetAddress;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author dtop
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class RadiusMetrics extends BaseJsonModel {

    private static final long serialVersionUID = -7816750516216808477L;

    private InetAddress serverIp;
    private int numberOfNoAnswer;

    private int minLatencyMs;
    private int maxLatencyMs;
    private int avgLatencyMs;

    @Override
    public RadiusMetrics clone() {
        RadiusMetrics ret = (RadiusMetrics) super.clone();
        return ret;
    }

    /*
     * (non-Javadoc)
     * 
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
        if (!(obj instanceof RadiusMetrics)) {
            return false;
        }
        RadiusMetrics other = (RadiusMetrics) obj;
        if (avgLatencyMs != other.avgLatencyMs) {
            return false;
        }
        if (maxLatencyMs != other.maxLatencyMs) {
            return false;
        }
        if (minLatencyMs != other.minLatencyMs) {
            return false;
        }
        if (numberOfNoAnswer != other.numberOfNoAnswer) {
            return false;
        }
        if (serverIp == null) {
            if (other.serverIp != null) {
                return false;
            }
        } else if (!serverIp.equals(other.serverIp)) {
            return false;
        }
        return true;
    }

    public int getAvgLatencyMs() {
        return avgLatencyMs;
    }

    public int getMaxLatencyMs() {
        return maxLatencyMs;
    }

    public int getMinLatencyMs() {
        return minLatencyMs;
    }

    public int getNumberOfNoAnswer() {
        return numberOfNoAnswer;
    }

    public InetAddress getServerIp() {
        return serverIp;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + avgLatencyMs;
        result = prime * result + maxLatencyMs;
        result = prime * result + minLatencyMs;
        result = prime * result + numberOfNoAnswer;
        result = prime * result + ((serverIp == null) ? 0 : serverIp.hashCode());
        return result;
    }

    public void setAvgLatencyMs(int avgLatencyMs) {
        this.avgLatencyMs = avgLatencyMs;
    }

    public void setMaxLatencyMs(int maxLatencyMs) {
        this.maxLatencyMs = maxLatencyMs;
    }

    public void setMinLatencyMs(int minLatencyMs) {
        this.minLatencyMs = minLatencyMs;
    }

    public void setNumberOfNoAnswer(int numberOfNoAnswer) {
        this.numberOfNoAnswer = numberOfNoAnswer;
    }

    public void setServerIp(InetAddress serverIp) {
        this.serverIp = serverIp;
    }
}
