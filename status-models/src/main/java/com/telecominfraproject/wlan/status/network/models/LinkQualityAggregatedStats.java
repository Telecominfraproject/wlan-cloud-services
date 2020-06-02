package com.telecominfraproject.wlan.status.network.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.entity.MinMaxAvgValueInt;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author dtop
 *
 */
public class LinkQualityAggregatedStats extends BaseJsonModel {

	private static final long serialVersionUID = 4260365949719099388L;

	private MinMaxAvgValueInt snr;

	private int badClientCount;
	private int averageClientCount;
	private int goodClientCount;
		
	public MinMaxAvgValueInt getSnr() {
		return snr;
	}
	public void setSnr(MinMaxAvgValueInt snr) {
		this.snr = snr;
	}
	public int getBadClientCount() {
		return badClientCount;
	}
	public void setBadClientCount(int badClientCount) {
		this.badClientCount = badClientCount;
	}
	public int getAverageClientCount() {
		return averageClientCount;
	}
	public void setAverageClientCount(int averageClientCount) {
		this.averageClientCount = averageClientCount;
	}
	public int getGoodClientCount() {
		return goodClientCount;
	}
	public void setGoodClientCount(int goodClientCount) {
		this.goodClientCount = goodClientCount;
	}

	@Override
	public LinkQualityAggregatedStats clone() {
		LinkQualityAggregatedStats ret = (LinkQualityAggregatedStats) super.clone();
		
		if(snr!=null) {
			ret.snr = snr.clone();
		}
		
		return ret;
	}
		
    @Override
	public int hashCode() {
		return Objects.hash(averageClientCount, badClientCount, goodClientCount, snr);
	}
    
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof LinkQualityAggregatedStats)) {
			return false;
		}
		LinkQualityAggregatedStats other = (LinkQualityAggregatedStats) obj;
		return averageClientCount == other.averageClientCount && badClientCount == other.badClientCount
				&& goodClientCount == other.goodClientCount && Objects.equals(snr, other.snr);
	}
	
	public LinkQualityAggregatedStats combineWith(LinkQualityAggregatedStats other) {
        if(other == null){
            return this;
        }

        if(snr != null) {
        	snr.combineWith(other.snr);
        } else {
        	snr = other.snr;
        }
        
        if(badClientCount == 0) {
        	badClientCount = other.badClientCount;
        }

        if(goodClientCount == 0) {
        	goodClientCount = other.goodClientCount;
        }
        
        if(averageClientCount == 0) {
        	averageClientCount = other.averageClientCount;
        }

        return this;
    }
    
}
