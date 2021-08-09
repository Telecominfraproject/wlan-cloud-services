package com.telecominfraproject.wlan.servicemetric.apnode.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.util.CollectionUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.servicemetric.models.McsStats;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDataType;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDetails;
import com.telecominfraproject.wlan.servicemetric.models.WmmQueueStats;
import com.telecominfraproject.wlan.servicemetric.models.WmmQueueStats.WmmQueueType;

/**
 * Node-level metric data from the Access Point.
 * 
 * @author yongli
 *
 */
public class ApNodeMetrics extends ServiceMetricDetails 
{
    private static final long serialVersionUID = 3133201391801512954L;
    
    /**
     * How many seconds the AP measured for the metric
     */
    private Integer periodLengthSec = 60;

    /**
     * Client MAC addresses seen during the period
     */
    private Map<RadioType, List<MacAddress>> clientMacAddressesPerRadio = new EnumMap<>(RadioType.class);
    
    /**
     * Total number the bytes transmitted on radio
     */
    private Map<RadioType, Long> txBytesPerRadio = new EnumMap<>(RadioType.class);

    /**
     * Total number of bytes received on radio
     */
    private Map<RadioType, Long> rxBytesPerRadio = new EnumMap<>(RadioType.class);


    private Map<RadioType, Integer> noiseFloorPerRadio = new EnumMap<>(RadioType.class);

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
     * The channel utilization for radio
     */
    private Map<RadioType, Integer> channelUtilizationPerRadio = new EnumMap<>(RadioType.class);

    private ApPerformance apPerformance;

    private List<VlanSubnet> vlanSubnet;

    private Map<RadioType, List<RadioUtilization>> radioUtilizationPerRadio = new EnumMap<>(RadioType.class);


    private Map<RadioType, RadioStatistics> radioStatsPerRadio = new EnumMap<>(RadioType.class);
    
    private Map<RadioType, List<McsStats>> mcsStatsPerRadio = new EnumMap<>(RadioType.class);
    
    private Map<RadioType, Map<WmmQueueType, WmmQueueStats>> wmmQueuesPerRadio = new EnumMap<>(RadioType.class);
    

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
    	Set<MacAddress> distinctMacs = new HashSet<>();
    	if(clientMacAddressesPerRadio!=null) {
    		clientMacAddressesPerRadio.values().forEach(macList -> macList.forEach(mac -> distinctMacs.add(mac) ) );
    	}
    	
        return distinctMacs.size();
    }

    public List<MacAddress> getClientMacAddresses(RadioType radioType) {
        return clientMacAddressesPerRadio.get(radioType);
    }

    public void setClientMacAddresses(RadioType radioType, List<MacAddress> clientMacAddresses) {
        this.clientMacAddressesPerRadio.put(radioType, clientMacAddresses);
    }
    
    public Map<RadioType, List<MacAddress>> getClientMacAddressesPerRadio() {
		return clientMacAddressesPerRadio;
	}

	public void setClientMacAddressesPerRadio(Map<RadioType, List<MacAddress>> clientMacAddressesPerRadio) {
		this.clientMacAddressesPerRadio = clientMacAddressesPerRadio;
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

    public Integer getNoiseFloor(RadioType radioType) {
        return noiseFloorPerRadio.get(radioType);
    }

    /**
     * Return the last actual cell size which is measured at the same time as noise floor.
     * @return cell size
     */
    public Integer getCellSize(RadioType radioType) {
        if (radioStatsPerRadio == null || radioStatsPerRadio.get(radioType) == null) {
            return null;
        }
        List<Integer> values = this.radioStatsPerRadio.get(radioType).getActualCellSize();
        if (values == null || values.isEmpty()) {
            return null;
        }
        return values.get(values.size()-1);
    }
    
    /**
     * Grab the minimum cell size (largest value) from all the actual cell size.
     * 
     * @return cell size.
     */
    public Integer getMinCellSize(RadioType radioType) {
        if (radioStatsPerRadio == null || radioStatsPerRadio.get(radioType) == null) {
            return null;
        }
        List<Integer> values = this.radioStatsPerRadio.get(radioType).getActualCellSize();
        if (null == values || values.isEmpty()) {
            return null;
        }
        return Collections.max(values);
    }
    
    public void setNoiseFloor(RadioType radioType, Integer noiseFloor) {
        this.noiseFloorPerRadio.put(radioType, noiseFloor);
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

    public Integer getChannelUtilization(RadioType radioType) {
        return channelUtilizationPerRadio.get(radioType);
    }

    public void setChannelUtilization(RadioType radioType, Integer channelUtilization) {
    	channelUtilizationPerRadio.put(radioType, channelUtilization);
    }


    public Long getTxBytes(RadioType radioType) {
        return txBytesPerRadio.get(radioType);
    }

    public void setTxBytes(RadioType radioType, Long txBytes) {
    	txBytesPerRadio.put(radioType, txBytes);
    }

    public Long getRxBytes(RadioType radioType) {
        return rxBytesPerRadio.get(radioType);
    }

    public void setRxBytes(RadioType radioType, Long rxBytes) {
        this.rxBytesPerRadio.put(radioType,  rxBytes);
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
 
   public List<RadioUtilization> getRadioUtilization(RadioType radioType) {
       return radioUtilizationPerRadio.get(radioType);
   }

   public void setRadioUtilization(RadioType radioType, List<RadioUtilization> radioUtilization) {
       this.radioUtilizationPerRadio.put(radioType,  radioUtilization);
   }

   public RadioStatistics getRadioStats(RadioType radioType) {
       return radioStatsPerRadio.get(radioType);
   }

   public void setRadioStats(RadioType radioType, RadioStatistics radioStats) {
       this.radioStatsPerRadio.put(radioType, radioStats);
   }

   public List<McsStats> getMcsStats(RadioType radioType) {
       return mcsStatsPerRadio.get(radioType);
   }

   public void setMcsStats(RadioType radioType, List<McsStats> mcsStats) {
       this.mcsStatsPerRadio.put(radioType, mcsStats);
   }


   public Map<WmmQueueType, WmmQueueStats> getWmmQueue(RadioType radioType) {
       return wmmQueuesPerRadio.get(radioType);
   }

   public void setWmmQueue(RadioType radioType, Map<WmmQueueType, WmmQueueStats> wmmQueue) {
      this.wmmQueuesPerRadio.put(radioType, wmmQueue);
   }



   //
   // Utility Functions 
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

	public Map<RadioType, Long> getTxBytesPerRadio() {
		return txBytesPerRadio;
	}

	public void setTxBytesPerRadio(Map<RadioType, Long> txBytesPerRadio) {
		this.txBytesPerRadio = txBytesPerRadio;
	}

	public Map<RadioType, Long> getRxBytesPerRadio() {
		return rxBytesPerRadio;
	}

	public void setRxBytesPerRadio(Map<RadioType, Long> rxBytesPerRadio) {
		this.rxBytesPerRadio = rxBytesPerRadio;
	}

	public Map<RadioType, Integer> getNoiseFloorPerRadio() {
		return noiseFloorPerRadio;
	}

	public void setNoiseFloorPerRadio(Map<RadioType, Integer> noiseFloorPerRadio) {
		this.noiseFloorPerRadio = noiseFloorPerRadio;
	}

	public Map<RadioType, Integer> getChannelUtilizationPerRadio() {
		return channelUtilizationPerRadio;
	}

	public void setChannelUtilizationPerRadio(Map<RadioType, Integer> channelUtilizationPerRadio) {
		this.channelUtilizationPerRadio = channelUtilizationPerRadio;
	}

	public Map<RadioType, List<RadioUtilization>> getRadioUtilizationPerRadio() {
		return radioUtilizationPerRadio;
	}

	public void setRadioUtilizationPerRadio(Map<RadioType, List<RadioUtilization>> radioUtilizationPerRadio) {
		this.radioUtilizationPerRadio = radioUtilizationPerRadio;
	}

	public Map<RadioType, RadioStatistics> getRadioStatsPerRadio() {
		return radioStatsPerRadio;
	}

	public void setRadioStatsPerRadio(Map<RadioType, RadioStatistics> radioStatsPerRadio) {
		this.radioStatsPerRadio = radioStatsPerRadio;
	}

	public Map<RadioType, List<McsStats>> getMcsStatsPerRadio() {
		return mcsStatsPerRadio;
	}

	public void setMcsStatsPerRadio(Map<RadioType, List<McsStats>> mcsStatsPerRadio) {
		this.mcsStatsPerRadio = mcsStatsPerRadio;
	}

	public Map<RadioType, Map<WmmQueueType, WmmQueueStats>> getWmmQueuesPerRadio() {
		return wmmQueuesPerRadio;
	}

	public void setWmmQueuesPerRadio(Map<RadioType, Map<WmmQueueType, WmmQueueStats>> wmmQueuesPerRadio) {
		this.wmmQueuesPerRadio = wmmQueuesPerRadio;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(apPerformance, channelUtilizationPerRadio, clientMacAddressesPerRadio,
				cloudLinkAvailability, cloudLinkLatencyInMs, mcsStatsPerRadio, networkProbeMetrics, noiseFloorPerRadio,
				periodLengthSec, radioStatsPerRadio, radioUtilizationPerRadio, radiusMetrics, rxBytesPerRadio,
				tunnelMetrics, txBytesPerRadio, vlanSubnet, wmmQueuesPerRadio);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof ApNodeMetrics)) {
			return false;
		}
		ApNodeMetrics other = (ApNodeMetrics) obj;
		return Objects.equals(apPerformance, other.apPerformance)
				&& Objects.equals(channelUtilizationPerRadio, other.channelUtilizationPerRadio)
				&& Objects.equals(clientMacAddressesPerRadio, other.clientMacAddressesPerRadio)
				&& Objects.equals(cloudLinkAvailability, other.cloudLinkAvailability)
				&& Objects.equals(cloudLinkLatencyInMs, other.cloudLinkLatencyInMs)
				&& Objects.equals(mcsStatsPerRadio, other.mcsStatsPerRadio)
				&& Objects.equals(networkProbeMetrics, other.networkProbeMetrics)
				&& Objects.equals(noiseFloorPerRadio, other.noiseFloorPerRadio)
				&& Objects.equals(periodLengthSec, other.periodLengthSec)
				&& Objects.equals(radioStatsPerRadio, other.radioStatsPerRadio)
				&& Objects.equals(radioUtilizationPerRadio, other.radioUtilizationPerRadio)
				&& Objects.equals(radiusMetrics, other.radiusMetrics)
				&& Objects.equals(rxBytesPerRadio, other.rxBytesPerRadio)
				&& Objects.equals(tunnelMetrics, other.tunnelMetrics)
				&& Objects.equals(txBytesPerRadio, other.txBytesPerRadio)
				&& Objects.equals(vlanSubnet, other.vlanSubnet)
				&& Objects.equals(wmmQueuesPerRadio, other.wmmQueuesPerRadio);
	}

	@Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }

        if (hasUnsupportedValue(tunnelMetrics) || hasUnsupportedValue(networkProbeMetrics)
                || hasUnsupportedValue(radiusMetrics) || hasUnsupportedValue(apPerformance)
                || hasUnsupportedValue(vlanSubnet) ) {
            return true;
        }
        
        AtomicInteger ai = new AtomicInteger(0);
        if(radioUtilizationPerRadio!=null) {
        	radioUtilizationPerRadio.values().forEach(c -> { if (hasUnsupportedValue(c)) { ai.incrementAndGet();} }); 
        }

        if(radioStatsPerRadio!=null) {
        	radioStatsPerRadio.values().forEach(c -> { if (hasUnsupportedValue(c)) { ai.incrementAndGet();} }); 
        }

        if(mcsStatsPerRadio!=null) {
        	mcsStatsPerRadio.values().forEach(c -> { if (hasUnsupportedValue(c)) { ai.incrementAndGet();} }); 
        }

        if(wmmQueuesPerRadio!=null) {
        	wmmQueuesPerRadio.values().forEach(c -> { if (hasUnsupportedValue(c)) { ai.incrementAndGet();} }); 
        }

        if(ai.get()>0) {
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
        
        if(this.clientMacAddressesPerRadio !=null) {
            ret.clientMacAddressesPerRadio = new EnumMap<>(RadioType.class);
            
			clientMacAddressesPerRadio.forEach((key, macList) -> {
				List<MacAddress> newList = new ArrayList<>();
				macList.forEach(m -> newList.add(m.clone()));
				ret.clientMacAddressesPerRadio.put(key, newList);
			});

        }
        
        if(this.wmmQueuesPerRadio!=null) {
        	ret.wmmQueuesPerRadio = new EnumMap<>(RadioType.class);
        	this.wmmQueuesPerRadio.forEach((rt,  wq) -> {
        		Map<WmmQueueStats.WmmQueueType, WmmQueueStats> newWm = new EnumMap<>(WmmQueueType.class);
        		ret.wmmQueuesPerRadio.put(rt, newWm);
        		wq.forEach((k, v) -> newWm.put(k, v.clone()));
        	});
        }
               
        if(this.mcsStatsPerRadio !=null) {
            ret.mcsStatsPerRadio = new EnumMap<>(RadioType.class);
			this.mcsStatsPerRadio.forEach((k, listV) -> {
				List<McsStats> newList = new ArrayList<>();
				ret.mcsStatsPerRadio.put(k, newList);
				listV.forEach(mcs -> newList.add(mcs.clone()));
			});
        }

        
        if(this.networkProbeMetrics !=null) {
            ret.networkProbeMetrics = new ArrayList<>();
            for(NetworkProbeMetrics npm: this.networkProbeMetrics){
                ret.networkProbeMetrics.add(npm.clone());
            }
        }
        
        if(this.radioStatsPerRadio !=null) {
            ret.radioStatsPerRadio = new EnumMap<>(RadioType.class);
            this.radioStatsPerRadio.forEach((k, v) -> ret.radioStatsPerRadio.put(k, v.clone()));
        }

        if(this.radioUtilizationPerRadio !=null) {
            ret.radioUtilizationPerRadio = new EnumMap<>(RadioType.class);
			this.radioUtilizationPerRadio.forEach((k, listV) -> {
				List<RadioUtilization> newList = new ArrayList<>();
				ret.radioUtilizationPerRadio.put(k, newList);
				listV.forEach(ru -> newList.add(ru.clone()));
			});
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
        
        if(this.vlanSubnet !=null) {
          ret.vlanSubnet = new ArrayList<>();
          for(VlanSubnet vs: this.vlanSubnet){
              ret.vlanSubnet.add(vs.clone());
          }
        }
        
        return ret;
    }

    @Override
    public ServiceMetricDataType getDataType() {
    	return ServiceMetricDataType.ApNode;
    }
}
