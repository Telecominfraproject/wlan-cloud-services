package com.telecominfraproject.wlan.status.network.models;

import com.telecominfraproject.wlan.core.model.entity.MinMaxAvgValueInt;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author dtop
 *
 */
public class ClientActivityAggregatedStats extends BaseJsonModel {

	private static final long serialVersionUID = -3033360501494696046L;

	private MinMaxAvgValueInt mbps;
	
	private int highClientCount;
	private int mediumClientCount;
	private int lowClientCount;
	
	public MinMaxAvgValueInt getMbps() {
		return mbps;
	}
	public void setMbps(MinMaxAvgValueInt mbps) {
		this.mbps = mbps;
	}
	public int getHighClientCount() {
		return highClientCount;
	}
	public void setHighClientCount(int highClientCount) {
		this.highClientCount = highClientCount;
	}
	public int getMediumClientCount() {
		return mediumClientCount;
	}
	public void setMediumClientCount(int mediumClientCount) {
		this.mediumClientCount = mediumClientCount;
	}
	public int getLowClientCount() {
		return lowClientCount;
	}
	public void setLowClientCount(int lowClientCount) {
		this.lowClientCount = lowClientCount;
	}

	@Override
	public ClientActivityAggregatedStats clone() {
		ClientActivityAggregatedStats ret = (ClientActivityAggregatedStats) super.clone();
		
		if(mbps!=null) {
			ret.mbps = mbps.clone();
		}
		
		return ret;
	}
	
    public ClientActivityAggregatedStats combineWith(ClientActivityAggregatedStats other) {
        if(other == null){
            return this;
        }

        if(mbps != null) {
        	mbps.combineWith(other.mbps);
        } else {
        	mbps = other.mbps;
        }
        
        if(highClientCount == 0) {
        	highClientCount = other.highClientCount;
        }

        if(mediumClientCount == 0) {
        	mediumClientCount = other.mediumClientCount;
        }
        
        if(lowClientCount == 0) {
        	lowClientCount = other.lowClientCount;
        }

        return this;
    }

}
