package com.telecominfraproject.wlan.servicemetric.apnode.models;

import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * Metric Data from network probes that are running on CE
 * 
 * @author dtoptygin
 *
 */
@JsonInclude(JsonInclude.Include.NON_NULL) 
public class NetworkProbeMetrics extends BaseJsonModel {

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
   public int hashCode() 
   {
      return Objects.hash(this.dhcpState, 
            this.dhcpLatencyMs, 
            this.dnsState,
            this.dnsLatencyMs,
            this.vlanIF,
            this.radiusLatencyMs,
            this.radiusState,
            this.dnsProbeResults);
   }

   @Override
   public boolean equals(Object obj) 
   {
      if(obj instanceof NetworkProbeMetrics)
      {
         NetworkProbeMetrics casted = (NetworkProbeMetrics) obj;

         return Objects.equals(casted.dhcpState, dhcpState) &&
               Objects.equals(casted.dnsState, dnsState) &&
               Objects.equals(casted.dnsLatencyMs, dnsLatencyMs) &&
               Objects.equals(casted.dhcpLatencyMs, dhcpLatencyMs) &&
               Objects.equals(casted.vlanIF, vlanIF) &&
               Objects.equals(casted.radiusLatencyMs, radiusLatencyMs) &&
               Objects.equals(casted.dnsProbeResults, dnsProbeResults) &&
               Objects.equals(casted.radiusState, radiusState);
      }

      return false;

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
}
