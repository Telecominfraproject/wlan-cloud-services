package com.telecominfraproject.wlan.status.network.models;

import com.telecominfraproject.wlan.status.models.StatusDataType;
import com.telecominfraproject.wlan.status.models.StatusDetails;

/**
 * @author dtop
 *
 */
public class NetworkAggregateStatusData extends StatusDetails {
 
    private static final long serialVersionUID = 4586203613209434309L;
    
    private CommonProbeDetails dhcpDetails;
    private CommonProbeDetails dnsDetails;
    private CommonProbeDetails cloudLinkDetails;
    private NoiseFloorDetails noiseFloorDetails;
    private ChannelUtilizationDetails channelUtilizationDetails;
    private RadioUtilizationDetails radioUtilizationDetails;
    private UserDetails userDetails;
    private TrafficDetails trafficDetails;
    private RadiusDetails radiusDetails;
    private EquipmentPerformanceDetails equipmentPerformanceDetails;
    private CapacityDetails capacityDetails;
    
    private int numberOfReportingEquipment;
    private int numberOfTotalEquipment;
    
    private long beginGenerationTsMs;
    private long endGenerationTsMs;
    
    private long beginAggregationTsMs;
    private long endAggregationTsMs;
    private int numMetricsAggregated;
    
    private int coverage;
    private int behavior;
    private int handoff;
    private int wlanLatency;
    
    
    @Override
    public StatusDataType getStatusDataType() {
    	return StatusDataType.NETWORK_AGGREGATE;
    }
    
    public EquipmentPerformanceDetails getEquipmentPerformanceDetails() {
		return equipmentPerformanceDetails;
	}
	public void setEquipmentPerformanceDetails(EquipmentPerformanceDetails equipmentPerformanceDetails) {
		this.equipmentPerformanceDetails = equipmentPerformanceDetails;
	}
	public int getNumberOfReportingEquipment() {
		return numberOfReportingEquipment;
	}
	public void setNumberOfReportingEquipment(int numberOfReportingEquipment) {
		this.numberOfReportingEquipment = numberOfReportingEquipment;
	}
	public int getNumberOfTotalEquipment() {
		return numberOfTotalEquipment;
	}
	public void setNumberOfTotalEquipment(int numberOfTotalEquipment) {
		this.numberOfTotalEquipment = numberOfTotalEquipment;
	}
	public TrafficDetails getTrafficDetails() {
        return trafficDetails;
    }
    public void setTrafficDetails(TrafficDetails trafficDetails) {
        this.trafficDetails = trafficDetails;
    }
    public long getBeginAggregationTsMs() {
        return beginAggregationTsMs;
    }
    public void setBeginAggregationTsMs(long beginAggregationTsMs) {
        this.beginAggregationTsMs = beginAggregationTsMs;
    }
    public long getEndAggregationTsMs() {
        return endAggregationTsMs;
    }
    public void setEndAggregationTsMs(long endAggregationTsMs) {
        this.endAggregationTsMs = endAggregationTsMs;
    }
    public int getNumMetricsAggregated() {
        return numMetricsAggregated;
    }
    public void setNumMetricsAggregated(int numMetricsAggregated) {
        this.numMetricsAggregated = numMetricsAggregated;
    }
    public UserDetails getUserDetails() {
        return userDetails;
    }
    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }
    public CommonProbeDetails getDhcpDetails() {
        return dhcpDetails;
    }
    public void setDhcpDetails(CommonProbeDetails dhcpDetails) {
        this.dhcpDetails = dhcpDetails;
    }
    public CommonProbeDetails getDnsDetails() {
        return dnsDetails;
    }
    public void setDnsDetails(CommonProbeDetails dnsDetails) {
        this.dnsDetails = dnsDetails;
    }    

    public int getCoverage() {
        return coverage;
    }
    public void setCoverage(int coverage) {
        this.coverage = coverage;
    }
    public int getBehavior() {
        return behavior;
    }
    public void setBehavior(int behavior) {
        this.behavior = behavior;
    }
    public int getHandoff() {
        return handoff;
    }
    public void setHandoff(int handoff) {
        this.handoff = handoff;
    }
    public int getWlanLatency() {
        return wlanLatency;
    }
    public void setWlanLatency(int latency) {
        this.wlanLatency = latency;
    }

    
    public EquipmentPerformanceDetails getApPerformanceDetails() {
        return equipmentPerformanceDetails;
    }
    public void setApPerformanceDetails(EquipmentPerformanceDetails apPerformanceDetails) {
        this.equipmentPerformanceDetails = apPerformanceDetails;
    }
    public CapacityDetails getCapacityDetails() {
        return capacityDetails;
    }
    public void setCapacityDetails(CapacityDetails capacityDetails) {
        this.capacityDetails = capacityDetails;
    }
    public NoiseFloorDetails getNoiseFloorDetails() {
        return noiseFloorDetails;
    }
    public void setNoiseFloorDetails(NoiseFloorDetails noiseFloorDetails) {
        this.noiseFloorDetails = noiseFloorDetails;
    }
    
    public CommonProbeDetails getCloudLinkDetails() {
        return cloudLinkDetails;
    }
    public void setCloudLinkDetails(CommonProbeDetails cloudLinkDetails) {
        this.cloudLinkDetails = cloudLinkDetails;
    }
    
    public ChannelUtilizationDetails getChannelUtilizationDetails() {
        return channelUtilizationDetails;
    }
    public void setChannelUtilizationDetails(ChannelUtilizationDetails channelUtilizationDetails) {
        this.channelUtilizationDetails = channelUtilizationDetails;
    }
    
    public RadiusDetails getRadiusDetails() {
        return radiusDetails;
    }
    public void setRadiusDetails(RadiusDetails radiusDetails) {
        this.radiusDetails = radiusDetails;
    }

    public long getBeginGenerationTsMs() {
        return beginGenerationTsMs;
    }
    public void setBeginGenerationTsMs(long beginGenerationTsMs) {
        this.beginGenerationTsMs = beginGenerationTsMs;
    }
    public long getEndGenerationTsMs() {
        return endGenerationTsMs;
    }
    public void setEndGenerationTsMs(long endGenerationTsMs) {
        this.endGenerationTsMs = endGenerationTsMs;
    }
    public RadioUtilizationDetails getRadioUtilizationDetails() {
        return radioUtilizationDetails;
    }
    public void setRadioUtilizationDetails(RadioUtilizationDetails radioUtilizationDetails) {
        this.radioUtilizationDetails = radioUtilizationDetails;
    }
    
    @Override
    public NetworkAggregateStatusData clone() {
        NetworkAggregateStatusData ret = (NetworkAggregateStatusData) super.clone();
        
        if(this.dhcpDetails!=null){
           ret.dhcpDetails = this.dhcpDetails.clone(); 
        }
        
        if(this.dnsDetails!=null){
            ret.dnsDetails = this.dnsDetails.clone(); 
        }

        if(this.noiseFloorDetails!=null){
            ret.noiseFloorDetails = this.noiseFloorDetails.clone(); 
        }

        if(this.cloudLinkDetails!=null){
            ret.cloudLinkDetails = this.cloudLinkDetails.clone(); 
        }

        if(this.channelUtilizationDetails!=null){
            ret.channelUtilizationDetails = this.channelUtilizationDetails.clone(); 
        }

        if(this.radioUtilizationDetails!=null){
            ret.radioUtilizationDetails = this.radioUtilizationDetails.clone(); 
        }

        if(this.userDetails!=null){
            ret.userDetails = this.userDetails.clone(); 
        }

        if(this.trafficDetails!=null){
            ret.trafficDetails = this.trafficDetails.clone(); 
        }

        if(this.radiusDetails!=null){
            ret.radiusDetails = this.radiusDetails.clone(); 
        }

        if(this.equipmentPerformanceDetails!=null){
            ret.equipmentPerformanceDetails = this.equipmentPerformanceDetails.clone(); 
        }

        if(this.capacityDetails!=null){
            ret.capacityDetails = this.capacityDetails.clone(); 
        }

        return ret;
    }
    
    /**
     * @param other
     * @return this object, merged with other
     */
    public NetworkAggregateStatusData combineWith(NetworkAggregateStatusData other) {
        
        if(other == null){
            return this;
        }
        
        if(dhcpDetails!=null){
            this.dhcpDetails.combineWith(other.dhcpDetails);
        } else if(other.dhcpDetails!=null){
            this.dhcpDetails = other.dhcpDetails;
        }
        
        if(dnsDetails!=null){
            this.dnsDetails.combineWith(other.dnsDetails);
        } else if(other.dnsDetails!=null){
            this.dnsDetails = other.dnsDetails;
        }

        if(noiseFloorDetails!=null){
            this.noiseFloorDetails.combineWith(other.noiseFloorDetails);
        } else if(other.noiseFloorDetails!=null){
            this.noiseFloorDetails = other.noiseFloorDetails;
        }
        
        if(cloudLinkDetails!=null){
            this.cloudLinkDetails.combineWith(other.cloudLinkDetails);
        } else if(other.cloudLinkDetails!=null){
            this.cloudLinkDetails = other.cloudLinkDetails;
        }

        if(channelUtilizationDetails!=null){
            this.channelUtilizationDetails.combineWith(other.channelUtilizationDetails);
        } else if(other.channelUtilizationDetails!=null){
            this.channelUtilizationDetails = other.channelUtilizationDetails;
        }

        if(radioUtilizationDetails!=null){
            this.radioUtilizationDetails.combineWith(other.radioUtilizationDetails);
        } else if(other.radioUtilizationDetails!=null){
            this.radioUtilizationDetails = other.radioUtilizationDetails;
        }

        if(userDetails!=null){
            this.userDetails.combineWith(other.userDetails);
        } else if(other.userDetails!=null){
            this.userDetails = other.userDetails;
        }

        if(equipmentPerformanceDetails!=null){
            this.equipmentPerformanceDetails.combineWith(other.equipmentPerformanceDetails);
        } else if(other.equipmentPerformanceDetails!=null){
            this.equipmentPerformanceDetails = other.equipmentPerformanceDetails;
        }

        if(capacityDetails!=null){
            this.capacityDetails.combineWith(other.capacityDetails);
        } else if(other.capacityDetails!=null){
            this.capacityDetails = other.capacityDetails;
        }

        if(trafficDetails!=null){
            this.trafficDetails.combineWith(other.trafficDetails);
        } else if(other.trafficDetails!=null){
            this.trafficDetails = other.trafficDetails;
        }

        if(radiusDetails!=null) {
            this.radiusDetails.combineWith(other.radiusDetails);
        }else if(other.radiusDetails!=null) {
            this.radiusDetails = other.radiusDetails;
        }
        
        if(this.numberOfReportingEquipment == 0){
            this.numberOfReportingEquipment = other.numberOfReportingEquipment;
        }

        if(this.beginAggregationTsMs == 0){
            this.beginAggregationTsMs = other.beginAggregationTsMs;
        }

        if(this.endAggregationTsMs == 0){
            this.endAggregationTsMs = other.endAggregationTsMs;
        }

        if(this.beginGenerationTsMs == 0){
            this.beginGenerationTsMs = other.beginGenerationTsMs;
        }

        if(this.endGenerationTsMs == 0){
            this.endGenerationTsMs = other.endGenerationTsMs;
        }

        if(this.numMetricsAggregated == 0){
            this.numMetricsAggregated = other.numMetricsAggregated;
        }

        
        if(this.numberOfTotalEquipment == 0){
            this.numberOfTotalEquipment = other.numberOfTotalEquipment;
        }
        
        if(this.coverage == 0){
            this.coverage = other.coverage;
        }
        
        if(this.behavior == 0){
            this.behavior = other.behavior;
        }
        
        if(this.handoff == 0){
            this.handoff = other.handoff;
        }
        
        if(this.wlanLatency == 0){
            this.wlanLatency = other.wlanLatency;
        }
        
        return this;
    }
    
    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }

        if (hasUnsupportedValue(dhcpDetails) || hasUnsupportedValue(dnsDetails) || hasUnsupportedValue(cloudLinkDetails)
                || hasUnsupportedValue(noiseFloorDetails) || hasUnsupportedValue(channelUtilizationDetails)
                || hasUnsupportedValue(userDetails) || hasUnsupportedValue(trafficDetails)
                || hasUnsupportedValue(radiusDetails) || hasUnsupportedValue(equipmentPerformanceDetails)
                || hasUnsupportedValue(capacityDetails)) {
            return true;
        }
        return false;
    }
}
