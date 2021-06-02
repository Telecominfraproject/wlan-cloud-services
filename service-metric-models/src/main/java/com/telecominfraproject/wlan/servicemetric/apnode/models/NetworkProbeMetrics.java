package com.telecominfraproject.wlan.servicemetric.apnode.models;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasSourceTimestamp;

/**
 * Metric Data from network probes that are running on CE
 * 
 * @author dtoptygin
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class NetworkProbeMetrics extends BaseJsonModel implements HasSourceTimestamp {

    private static final long serialVersionUID = 7293164162279703547L;

    private String vlanIF;
    private StateUpDownError dhcpState;
    private long dhcpLatencyMs;
    private StateUpDownError dnsState;
    private long dnsLatencyMs;
   
   //these two radius metrics are not used (for now)
   //see RadiusMetrics collected in a separate object
   private StateUpDownError radiusState;
   private long radiusLatencyMs;

   private List<DnsProbeMetric> dnsProbeResults;

   /** 
    * timestamp for the source data (AP stats) for this metrics report
    */
   private long sourceTimestampMs;

   public String getVlanIF()
   {
      return this.vlanIF;
   }

   public void setVlanIF(String vlanIF)
   {
      this.vlanIF = vlanIF;
   }

   public StateUpDownError getDhcpState()
   {
      return this.dhcpState;
   }

   public void setDhcpLatencyMs(long latency)
   {
      this.dhcpLatencyMs = latency;
   }
   
   public long getDhcpLatencyMs()
   {
      return this.dhcpLatencyMs;
   }
   
   public void setDnsLatencyMs(long latency)
   {
      this.dnsLatencyMs = latency;
   }
   
   public long getDnsLatencyMs()
   {
      return this.dnsLatencyMs;
   }
   
   public void setDhcpState(StateUpDownError dhcpState)
   {
      this.dhcpState = dhcpState;
   }

   public StateUpDownError getDnsState()
   {
      return this.dnsState;
   }

   public void setDnsState(StateUpDownError dnsState)
   {
      this.dnsState = dnsState;
   }

   public StateUpDownError getRadiusState()
   {
      return this.radiusState;
   }

   public void setRadiusState(StateUpDownError radiusState)
   {
      this.radiusState = radiusState;
   }

   public long getRadiusLatencyInMs()
   {
      return this.radiusLatencyMs;
   }

   public void setRadiusLatencyInMs(long latency)
   {
      this.radiusLatencyMs = latency;
   }

   
   public List<DnsProbeMetric> getDnsProbeResults() {
    return dnsProbeResults;
}

public void setDnsProbeResults(List<DnsProbeMetric> dnsProbeResults) {
    this.dnsProbeResults = dnsProbeResults;
}



    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (StateUpDownError.isUnsupported(dhcpState) || StateUpDownError.isUnsupported(dnsState)
                || StateUpDownError.isUnsupported(radiusState)) {
            return true;
        }
        if (dnsProbeResults != null) {
            for (DnsProbeMetric metric : dnsProbeResults) {
                if (metric.hasUnsupportedValue()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public NetworkProbeMetrics clone() {
        return (NetworkProbeMetrics) super.clone();
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(dhcpLatencyMs, dhcpState, dnsLatencyMs, dnsProbeResults, dnsState, radiusLatencyMs, radiusState, sourceTimestampMs, vlanIF);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        NetworkProbeMetrics other = (NetworkProbeMetrics) obj;
        return dhcpLatencyMs == other.dhcpLatencyMs && dhcpState == other.dhcpState && dnsLatencyMs == other.dnsLatencyMs
                && Objects.equals(dnsProbeResults, other.dnsProbeResults) && dnsState == other.dnsState && radiusLatencyMs == other.radiusLatencyMs
                && radiusState == other.radiusState && sourceTimestampMs == other.sourceTimestampMs && Objects.equals(vlanIF, other.vlanIF);
    }
    
    public void setSourceTimestampMs(long sourceTimestamp) {
        this.sourceTimestampMs = sourceTimestamp;
    }
    
    @Override
    public long getSourceTimestampMs() {
        return this.sourceTimestampMs;
    }
}
