package com.telecominfraproject.wlan.servicemetrics.models;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.servicemetrics.models.WmmQueueStats.WmmQueueType;

/**
 * Node-level metric data from the Access Point.
 * 
 * @author yongli
 *
 */
public class ApNodeMetrics extends MetricData implements ClientMetricsInterface 
{
    private static final long serialVersionUID = 3133201391801512954L;
    
    public static final String TYPE_NAME = ApNodeMetrics.class.getSimpleName();

    /**
     * How many seconds the AP measured for the metric
     */
    private Integer periodLengthSec = 60;

    /**
     * Client MAC addresses seen during the period
     */
    private List<byte[]> clientMacAddresses = new ArrayList<>();
    
    /**
     * Total number the bytes transmitted on 2.4GHz radio
     */
    private Long txBytes2G;

    /**
     * Total number of byte received on 2.4GHz radio
     */
    private Long rxBytes2G;

    /**
     * Total number the bytes transmitted on 5GHz radio
     */
    private Long txBytes5G;

    /**
     * Total number of byte received on 5GHz radio
     */
    private Long rxBytes5G;

    /**
     * The noise floor for 2.4G radio
     */
    private Integer noiseFloor2G;

    /**
     * The noise floor for 5G radio
     */
    private Integer noiseFloor5G;

    /**
     * Tunnel metrics
     */
    private List<TunnelMetricData> tunnelMetrics = new ArrayList<>();

    private List<NetworkProbeMetrics> networkProbeMetrics = new ArrayList<>();

    private List<RadiusMetrics> radiusMetrics = new ArrayList<>();

    /*
     * CloudLink availability
     */
    private Integer cloudLinkAvailability;

    /* Clound link latency */
    private Long cloudLinkLatencyInMs;

    /**
     * The channel utilization for 2.4G radio
     */
    private Integer channelUtilization2G;

    /**
     * The channel utilization for 5G radio
     */
    private Integer channelUtilization5G;

    private ApPerformance apPerformance;

    private List<VlanSubnet> vlanSubnet;

    private List<RadioUtilization> radioUtilization2G;

    private List<RadioUtilization> radioUtilization5G;

    private RadioStatistics radioStats2G;
    private RadioStatistics radioStats5G;
    
    private List<McsStats> mcsStats2G;
    private List<McsStats> mcsStats5G;
    
    /**
     * Our wmm queues
     */
    Map<WmmQueueType, WmmQueueStats> twoGWmmQueue;
    Map<WmmQueueType, WmmQueueStats> fiveGWmmQueue;
    
    public String getType() {
        return TYPE_NAME;
    }

    public Integer getPeriodLengthSec() {
        return periodLengthSec;
    }

    public void setPeriodLengthSec(Integer periodLengthSec) {
        this.periodLengthSec = periodLengthSec;
    }

    /**
     * Total number of clients seen during the period.
     * @return total
     */
    public long getClientCount() {
        return clientMacAddresses != null ? clientMacAddresses.size() : 0;
    }

    public List<byte[]> getClientMacAddresses() {
        return clientMacAddresses;
    }

    public void setClientMacAddresses(List<byte[]> clientMacAddresses) {
        this.clientMacAddresses = clientMacAddresses;
    }

    public List<TunnelMetricData> getTunnelMetrics() {
        return tunnelMetrics;
    }

    public void setTunnelMetrics(List<TunnelMetricData> tunnelMetrics) {
        this.tunnelMetrics = tunnelMetrics;
    }

    public List<NetworkProbeMetrics> getNetworkProbeMetrics() {
        return networkProbeMetrics;
    }

    public void setNetworkProbeMetrics(List<NetworkProbeMetrics> networkProbeMetrics) {
        this.networkProbeMetrics = networkProbeMetrics;
    }

    public Integer getNoiseFloor2G() {
        return noiseFloor2G;
    }

    /**
     * Return the last actual cell size which is measure at the same time as noise floor.
     * @return cell size
     */
    public Integer getCellSize2G() {
        if (null == this.radioStats2G) {
            return null;
        }
        List<Integer> values = this.radioStats2G.getActualCellSize();
        if (null == values || values.isEmpty()) {
            return null;
        }
        return values.get(values.size()-1);
    }
    
    /**
     * Grab the minimum cell size (largest value) from all the actual cell size.
     * 
     * @return cell size.
     */
    public Integer getMinCellSize2G() {
        if (null == this.radioStats2G) {
            return null;
        }
        List<Integer> values = this.radioStats2G.getActualCellSize();
        if (null == values || values.isEmpty()) {
            return null;
        }
        return Collections.max(values);
    }
    
    public void setNoiseFloor2G(Integer noiseFloor2G) {
        this.noiseFloor2G = noiseFloor2G;
    }

    public Integer getNoiseFloor5G() {
        return noiseFloor5G;
    }

    /**
     * Return the last actual cell size which is measure at the same time as noise floor.
     * @return cell size
     */
    public Integer getCellSize5G() {
        if (null == this.radioStats5G) {
            return null;
        }
        List<Integer> values = this.radioStats5G.getActualCellSize();
        if (null == values || values.isEmpty()) {
            return null;
        }
        return values.get(values.size()-1);
    }
    
    /**
     * Grab the minimum cell size (largest value) from all the actual cell size.
     * 
     * @return cell size.
     */
    public Integer getMinCellSize5G() {
        if (null == this.radioStats5G) {
            return null;
        }
        List<Integer> values = this.radioStats5G.getActualCellSize();
        if (null == values || values.isEmpty()) {
            return null;
        }
        return Collections.max(values);
    }
    
    public void setNoiseFloor5G(Integer noiseFloor5G) {
        this.noiseFloor5G = noiseFloor5G;
    }

    public Integer getCloudLinkAvailability() {
        return this.cloudLinkAvailability;
    }

    public void setCloudLinkAvailability(Integer cloudAvailability) {
        this.cloudLinkAvailability = cloudAvailability;
    }

    public Long getCloudLinkLatencyInMs() {
        return this.cloudLinkLatencyInMs;
    }

    public void setCloudLinkLatencyInMs(Long cloudLatency) {
        this.cloudLinkLatencyInMs = cloudLatency;
    }

    public Integer getChannelUtilization2G() {
        return channelUtilization2G;
    }

    public void setChannelUtilization2G(Integer channelUtilization2G) {
        this.channelUtilization2G = channelUtilization2G;
    }

    public Integer getChannelUtilization5G() {
        return channelUtilization5G;
    }

    public void setChannelUtilization5G(Integer channelUtilization5G) {
        this.channelUtilization5G = channelUtilization5G;
    }

    public Long getTxBytes2G() {
        return txBytes2G;
    }

    public void setTxBytes2G(Long txBytes2G) {
        this.txBytes2G = txBytes2G;
    }

    public Long getRxBytes2G() {
        return rxBytes2G;
    }

    public void setRxBytes2G(Long rxBytes2G) {
        this.rxBytes2G = rxBytes2G;
    }

    public Long getTxBytes5G() {
        return txBytes5G;
    }

    public void setTxBytes5G(Long txBytes5G) {
        this.txBytes5G = txBytes5G;
    }

    public Long getRxBytes5G() {
        return rxBytes5G;
    }

    public void setRxBytes5G(Long rxBytes5G) {
        this.rxBytes5G = rxBytes5G;
    }

    public List<RadiusMetrics> getRadiusMetrics() {
        return radiusMetrics;
    }

    public void setRadiusMetrics(List<RadiusMetrics> radiusMetrics) {
        this.radiusMetrics = radiusMetrics;
    }

    public ApPerformance getApPerformance() {
        return apPerformance;
    }

    public void setApPerformance(ApPerformance apPerformance) {
        this.apPerformance = apPerformance;
    }

    public List<VlanSubnet> getVlanSubnet() {
      return vlanSubnet;
   }

   public void setVlanSubnet(List<VlanSubnet> vlanSubnet) {
      this.vlanSubnet = vlanSubnet;
   }
 
   public List<RadioUtilization> getRadioUtilization2G() {
       return radioUtilization2G;
   }

   public void setRadioUtilization2G(List<RadioUtilization> radioUtilization2G) {
       this.radioUtilization2G = radioUtilization2G;
   }

   public List<RadioUtilization> getRadioUtilization5G() {
       return radioUtilization5G;
   }

   public void setRadioUtilization5G(List<RadioUtilization> radioUtilization5G) {
       this.radioUtilization5G = radioUtilization5G;
   }
   public RadioStatistics getRadioStats2G() {
       return radioStats2G;
   }

   public void setRadioStats2G(RadioStatistics radioStats2G) {
       this.radioStats2G = radioStats2G;
   }

   public List<McsStats> getMcsStats2G() {
       return mcsStats2G;
   }

   public void setMcsStats2G(List<McsStats> mcsStats2G) {
       this.mcsStats2G = mcsStats2G;
   }

   public List<McsStats> getMcsStats5G() {
       return mcsStats5G;
   }

   public void setMcsStats5G(List<McsStats> mcsStats5G) {
       this.mcsStats5G = mcsStats5G;
   }

   public RadioStatistics getRadioStats5G() {
       return radioStats5G;
   }

   public void setRadioStats5G(RadioStatistics radioStats5G) {
       this.radioStats5G = radioStats5G;
   }


   public Map<WmmQueueType, WmmQueueStats> getTwoGWmmQueue() {
       return twoGWmmQueue;
   }

   public void setTwoGWmmQueue(Map<WmmQueueType, WmmQueueStats> twoGWmmQueue) {
      this.twoGWmmQueue = twoGWmmQueue;
   }

   public Map<WmmQueueType, WmmQueueStats> getFiveGWmmQueue() {
      return fiveGWmmQueue;
   }

   public void setFiveGWmmQueue(Map<WmmQueueType, WmmQueueStats> fiveGWmmQueue) {
      this.fiveGWmmQueue = fiveGWmmQueue;
   }


   //
   // Functions for use in rule engine
   //

   public boolean aggregateIsDhcpReachable() {
        return NetworkProbeMetricsUtil.isDhcpReachable(networkProbeMetrics);
    }
    
    @JsonIgnore
    public boolean isDHCPReachableOnVlan(String vlan) {
        return NetworkProbeMetricsUtil.isDhcpReachable(vlan, networkProbeMetrics);
    }

    public Long aggregateAvgDHCPLatency() {
        return NetworkProbeMetricsUtil.getAvgDHCPLatency(networkProbeMetrics);
    }

    public Long aggregateMaxDHCPLatency() {
        return NetworkProbeMetricsUtil.getMaxDHCPLatency(networkProbeMetrics);
    }

    public Long aggregateMinDHCPLatency() {
        return NetworkProbeMetricsUtil.getMinDHCPLatency(networkProbeMetrics);
    }

    public boolean aggregateIsDnsReachable() {
        return NetworkProbeMetricsUtil.isDnsReachable(networkProbeMetrics);
    }
    
    @JsonIgnore
    public boolean isDNSReachableOnVlan(String vlan) {
        return NetworkProbeMetricsUtil.isDnsReachable(vlan, networkProbeMetrics);
    }

    @JsonIgnore
    public long getDNSLatency(String vlan) {
        Long dnsLatency = NetworkProbeMetricsUtil.getDNSLatency(vlan, networkProbeMetrics);
        return dnsLatency==null?0:dnsLatency;
    }
    public long aggregateAvgDNSLatency() {
        Long avgLatency = NetworkProbeMetricsUtil.getAvgDNSLatency(networkProbeMetrics);
        return avgLatency==null?0:avgLatency;
    }

    public long aggregateMaxDNSLatency() {
        Long dnsLatency = NetworkProbeMetricsUtil.getMaxDNSLatency(networkProbeMetrics);
        return dnsLatency==null?0:dnsLatency;
    }

    public long aggregateMinDNSLatency() {
        Long dnsLatency = NetworkProbeMetricsUtil.getMinDNSLatency(networkProbeMetrics);
        return dnsLatency==null?0:dnsLatency;
    }

    //
    public boolean aggregateIsRadiusReachable() {
        return RadiusMetricsUtil.isRadiusReachable(radiusMetrics);
    }
    
    public long aggregateAvgRadiusLatency() {
        return RadiusMetricsUtil.getAvgRadiusLatency(radiusMetrics);
    }
    
    public boolean hasRadiusMetrics() {
        return !CollectionUtils.isEmpty(this.radiusMetrics);
    }

    public long aggregateMaxRadiusLatency() {
        return RadiusMetricsUtil.getMaxRadiusLatency(radiusMetrics);
    }

    public long aggregateMinRadiusLatency() {
        return RadiusMetricsUtil.getMinRadiusLatency(radiusMetrics);
    }


    //

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result
            + ((apPerformance == null) ? 0 : apPerformance.hashCode());
      result = prime
            * result
            + ((channelUtilization2G == null) ? 0 : channelUtilization2G
                  .hashCode());
      result = prime
            * result
            + ((channelUtilization5G == null) ? 0 : channelUtilization5G
                  .hashCode());
      result = prime
            * result
            + ((clientMacAddresses == null) ? 0 : clientMacAddresses.hashCode());
      result = prime
            * result
            + ((cloudLinkAvailability == null) ? 0 : cloudLinkAvailability
                  .hashCode());
      result = prime
            * result
            + ((cloudLinkLatencyInMs == null) ? 0 : cloudLinkLatencyInMs
                  .hashCode());
      result = prime
            * result
            + ((networkProbeMetrics == null) ? 0 : networkProbeMetrics
                  .hashCode());
      result = prime * result
            + ((noiseFloor2G == null) ? 0 : noiseFloor2G.hashCode());
      result = prime * result
            + ((noiseFloor5G == null) ? 0 : noiseFloor5G.hashCode());
      result = prime * result
            + ((periodLengthSec == null) ? 0 : periodLengthSec.hashCode());
      result = prime * result
            + ((radiusMetrics == null) ? 0 : radiusMetrics.hashCode());
      result = prime * result
            + ((rxBytes2G == null) ? 0 : rxBytes2G.hashCode());
      result = prime * result
            + ((rxBytes5G == null) ? 0 : rxBytes5G.hashCode());
      result = prime * result
            + ((tunnelMetrics == null) ? 0 : tunnelMetrics.hashCode());
      result = prime * result
            + ((txBytes2G == null) ? 0 : txBytes2G.hashCode());
      result = prime * result
            + ((txBytes5G == null) ? 0 : txBytes5G.hashCode());
      result = prime * result
            + ((vlanSubnet == null) ? 0 : vlanSubnet.hashCode());
      result = prime * result
              + ((radioUtilization2G == null) ? 0 : radioUtilization2G.hashCode());
      result = prime * result
              + ((radioUtilization5G == null) ? 0 : radioUtilization5G.hashCode());
      result = prime * result
              + ((radioStats2G == null) ? 0 : radioStats2G.hashCode());
      result = prime * result
              + ((radioStats5G == null) ? 0 : radioStats5G.hashCode());
      result = prime * result
              + ((mcsStats2G == null) ? 0 : mcsStats2G.hashCode());
      result = prime * result
              + ((mcsStats5G == null) ? 0 : mcsStats5G.hashCode());
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
      ApNodeMetrics other = (ApNodeMetrics) obj;
      if (apPerformance == null) {
         if (other.apPerformance != null)
            return false;
      } else if (!apPerformance.equals(other.apPerformance))
         return false;
      if (channelUtilization2G == null) {
         if (other.channelUtilization2G != null)
            return false;
      } else if (!channelUtilization2G.equals(other.channelUtilization2G))
         return false;
      if (channelUtilization5G == null) {
         if (other.channelUtilization5G != null)
            return false;
      } else if (!channelUtilization5G.equals(other.channelUtilization5G))
         return false;
      if (clientMacAddresses == null) {
         if (other.clientMacAddresses != null)
            return false;
      } 
      else 
      {
         for (int i = 0; i < clientMacAddresses.size(); i++) 
         {
            if (!Arrays.equals(clientMacAddresses.get(i), other.clientMacAddresses.get(i))) 
            {
               return false;
            }
         }
      }

      if (cloudLinkAvailability == null) {
         if (other.cloudLinkAvailability != null)
            return false;
      } else if (!cloudLinkAvailability.equals(other.cloudLinkAvailability))
         return false;
      if (cloudLinkLatencyInMs == null) {
         if (other.cloudLinkLatencyInMs != null)
            return false;
      } else if (!cloudLinkLatencyInMs.equals(other.cloudLinkLatencyInMs))
         return false;
      if (networkProbeMetrics == null) {
         if (other.networkProbeMetrics != null)
            return false;
      } else if (!networkProbeMetrics.equals(other.networkProbeMetrics))
         return false;
      if (noiseFloor2G == null) {
         if (other.noiseFloor2G != null)
            return false;
      } else if (!noiseFloor2G.equals(other.noiseFloor2G))
         return false;
      if (noiseFloor5G == null) {
         if (other.noiseFloor5G != null)
            return false;
      } else if (!noiseFloor5G.equals(other.noiseFloor5G))
         return false;
      if (periodLengthSec == null) {
         if (other.periodLengthSec != null)
            return false;
      } else if (!periodLengthSec.equals(other.periodLengthSec))
         return false;
      if (radiusMetrics == null) {
         if (other.radiusMetrics != null)
            return false;
      } else if (!radiusMetrics.equals(other.radiusMetrics))
         return false;
      if (rxBytes2G == null) {
         if (other.rxBytes2G != null)
            return false;
      } else if (!rxBytes2G.equals(other.rxBytes2G))
         return false;
      if (rxBytes5G == null) {
         if (other.rxBytes5G != null)
            return false;
      } else if (!rxBytes5G.equals(other.rxBytes5G))
         return false;
      if (tunnelMetrics == null) {
         if (other.tunnelMetrics != null)
            return false;
      } else if (!tunnelMetrics.equals(other.tunnelMetrics))
         return false;
      if (txBytes2G == null) {
         if (other.txBytes2G != null)
            return false;
      } else if (!txBytes2G.equals(other.txBytes2G))
         return false;
      if (txBytes5G == null) {
         if (other.txBytes5G != null)
            return false;
      } else if (!txBytes5G.equals(other.txBytes5G))
         return false;
      if (vlanSubnet == null) {
         if (other.vlanSubnet != null)
            return false;
      } else if (!vlanSubnet.equals(other.vlanSubnet))
         return false;
      if (radioUtilization2G == null) {
          if (other.radioUtilization2G != null)
             return false;
       } else if (!radioUtilization2G.equals(other.radioUtilization2G))
          return false;
      if (radioUtilization5G == null) {
          if (other.radioUtilization5G != null)
             return false;
       } else if (!radioUtilization5G.equals(other.radioUtilization5G))
          return false;
      if (radioStats2G == null) {
          if (other.radioStats2G != null)
             return false;
       } else if (!radioStats2G.equals(other.radioStats2G))
          return false;
      if (radioStats5G == null) {
          if (other.radioStats5G != null)
             return false;
       } else if (!radioStats5G.equals(other.radioStats5G))
          return false;
      if (mcsStats2G == null) {
          if (other.mcsStats2G != null)
             return false;
       } else if (!mcsStats2G.equals(other.mcsStats2G))
          return false;
      if (mcsStats5G == null) {
          if (other.mcsStats5G != null)
             return false;
       } else if (!mcsStats5G.equals(other.mcsStats5G))
          return false;
      return true;
   }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }

        if (hasUnsupportedValue(tunnelMetrics) || hasUnsupportedValue(networkProbeMetrics)
                || hasUnsupportedValue(radiusMetrics) || hasUnsupportedValue(apPerformance)
                || hasUnsupportedValue(vlanSubnet) || hasUnsupportedValue(radioUtilization2G)
                || hasUnsupportedValue(radioUtilization5G) || hasUnsupportedValue(radioStats2G)
                || hasUnsupportedValue(radioStats5G) || hasUnsupportedValue(mcsStats2G)
                || hasUnsupportedValue(mcsStats5G) || hasUnsupportedValue(twoGWmmQueue)
                || hasUnsupportedValue(fiveGWmmQueue)) {
            return true;
        }
        return false;
    }
    
    @Override
    public ApNodeMetrics clone() {
        ApNodeMetrics ret = (ApNodeMetrics) super.clone();
        
        if(this.apPerformance !=null) {
            ret.apPerformance = this.apPerformance.clone();
        }
        
        if(this.clientMacAddresses !=null) {
            ret.clientMacAddresses = new ArrayList<>();
            for(byte[]cMac: this.clientMacAddresses){
                ret.clientMacAddresses.add(cMac.clone());
            }
        }
        
        if(this.fiveGWmmQueue !=null) {
            ret.fiveGWmmQueue = new EnumMap<>(WmmQueueType.class);
            for(Map.Entry<WmmQueueStats.WmmQueueType, WmmQueueStats> mapEntry: this.fiveGWmmQueue.entrySet()){
                ret.fiveGWmmQueue.put(mapEntry.getKey(), mapEntry.getValue().clone());
            }
        }
        
        if(this.mcsStats2G !=null) {
            ret.mcsStats2G = new ArrayList<>();
            for(McsStats ms: this.mcsStats2G){
                ret.mcsStats2G.add(ms.clone());
            }
        }
        
        if(this.mcsStats5G !=null) {
            ret.mcsStats5G = new ArrayList<>();
            for(McsStats ms: this.mcsStats5G){
                ret.mcsStats5G.add(ms.clone());
            }
        }
        
        if(this.networkProbeMetrics !=null) {
            ret.networkProbeMetrics = new ArrayList<>();
            for(NetworkProbeMetrics npm: this.networkProbeMetrics){
                ret.networkProbeMetrics.add(npm.clone());
            }
        }
        
        if(this.radioStats2G !=null) {
            ret.radioStats2G = this.radioStats2G.clone();
        }
        
        if(this.radioStats5G !=null) {
            ret.radioStats5G = this.radioStats5G.clone();
        }
        
        if(this.radioUtilization2G !=null) {
            ret.radioUtilization2G = new ArrayList<>();
            for(RadioUtilization ru: this.radioUtilization2G){
                ret.radioUtilization2G.add(ru.clone());
            }
        }
        
        if(this.radioUtilization5G !=null) {
            ret.radioUtilization5G = new ArrayList<>();
            for(RadioUtilization ru: this.radioUtilization5G){
                ret.radioUtilization5G.add(ru.clone());
            }
        }
        
        if(this.radiusMetrics !=null) {
            ret.radiusMetrics = new ArrayList<>();
            for(RadiusMetrics rm:this.radiusMetrics){
                ret.radiusMetrics.add(rm.clone());
            }
        }
        
        if(this.tunnelMetrics !=null) {
            ret.tunnelMetrics = new ArrayList<>();
            for(TunnelMetricData tm: this.tunnelMetrics){
                ret.tunnelMetrics.add(tm.clone());
            }
        }
        
        if(this.twoGWmmQueue !=null) {
          ret.twoGWmmQueue = new EnumMap<>(WmmQueueType.class);
          for(Map.Entry<WmmQueueStats.WmmQueueType, WmmQueueStats> mapEntry: this.twoGWmmQueue.entrySet()){
              ret.twoGWmmQueue.put(mapEntry.getKey(), mapEntry.getValue().clone());
          }

        }
        
        if(this.vlanSubnet !=null) {
          ret.vlanSubnet = new ArrayList<>();
          for(VlanSubnet vs: this.vlanSubnet){
              ret.vlanSubnet.add(vs.clone());
          }
        }
        
        return ret;
    }

    @Override
    public Set<MacAddress> getDeviceMacAddresses() 
    {
        if(clientMacAddresses != null)
        {
            Set<MacAddress> returnValue = new HashSet<>();
            
            for(byte[] mac : clientMacAddresses)
            {
                returnValue.add(new MacAddress(mac));
            }
            
            return returnValue;
            
        }

        return Collections.emptySet();
    }

    @Override
    public boolean containsClientMac(MacAddress clientMac) 
    {
        return getDeviceMacAddresses().contains(clientMac);
    }

    public static boolean isDataType(String dataType) {
        return TYPE_NAME.equals(dataType);
    }
}
