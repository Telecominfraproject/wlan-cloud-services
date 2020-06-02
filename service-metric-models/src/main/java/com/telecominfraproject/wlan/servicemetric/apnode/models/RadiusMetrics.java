package com.telecominfraproject.wlan.servicemetric.apnode.models;

import java.net.InetAddress;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.telecominfraproject.wlan.core.model.entity.MinMaxAvgValueInt;
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

    private MinMaxAvgValueInt latencyMs;

    @Override
    public RadiusMetrics clone() {
        RadiusMetrics ret = (RadiusMetrics) super.clone();
        
        if(latencyMs!=null) {
        	ret.latencyMs = latencyMs.clone();
        }
        
        return ret;
    }

    @Override
	public int hashCode() {
		return Objects.hash(latencyMs, numberOfNoAnswer, serverIp);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof RadiusMetrics)) {
			return false;
		}
		RadiusMetrics other = (RadiusMetrics) obj;
		return Objects.equals(latencyMs, other.latencyMs) && numberOfNoAnswer == other.numberOfNoAnswer
				&& Objects.equals(serverIp, other.serverIp);
	}

	public MinMaxAvgValueInt getLatencyMs() {
		return latencyMs;
	}

	public void setLatencyMs(MinMaxAvgValueInt latencyMs) {
		this.latencyMs = latencyMs;
	}

	public int getNumberOfNoAnswer() {
        return numberOfNoAnswer;
    }

    public InetAddress getServerIp() {
        return serverIp;
    }

    public void setNumberOfNoAnswer(int numberOfNoAnswer) {
        this.numberOfNoAnswer = numberOfNoAnswer;
    }

    public void setServerIp(InetAddress serverIp) {
        this.serverIp = serverIp;
    }
}
