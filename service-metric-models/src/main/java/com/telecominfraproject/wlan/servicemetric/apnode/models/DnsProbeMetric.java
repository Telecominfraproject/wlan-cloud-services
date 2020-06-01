package com.telecominfraproject.wlan.servicemetric.apnode.models;

import java.net.InetAddress;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * 
 * @author wqiu
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class DnsProbeMetric extends BaseJsonModel {
	
    private static final long serialVersionUID = 263527176158876106L;

    private InetAddress dnsServerIp;
    private StateUpDownError dnsState;
    private long dnsLatencyMs;

    public DnsProbeMetric() {

    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (StateUpDownError.isUnsupported(dnsState)) {
            return true;
        }
        return false;
    }
    
    public InetAddress getDnsServerIp() {
        return dnsServerIp;
    }
    public void setDnsServerIp(InetAddress dnsServerIp) {
        this.dnsServerIp = dnsServerIp;
    }
    public StateUpDownError getDnsState() {
        return dnsState;
    }
    public void setDnsState(StateUpDownError dnsState) {
        this.dnsState = dnsState;
    }
    public long getDnsLatencyMs() {
        return dnsLatencyMs;
    }
    public void setDnsLatencyMs(long dnsLatencyMs) {
        this.dnsLatencyMs = dnsLatencyMs;
    }


}
