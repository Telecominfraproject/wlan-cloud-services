package com.telecominfraproject.wlan.status.network.models;

import java.util.EnumMap;
import java.util.Map;

import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.utils.NumberUtils;

/**
 * @author dtop
 *
 */
public class TrafficDetails extends BaseJsonModel {

    private static final long serialVersionUID = 3034727832551606882L;
    
    private Map<RadioType, TrafficPerRadioDetails> perRadioDetails = new EnumMap<>(RadioType.class);
    
    private float indicatorValueRxMbps;
    private float indicatorValueTxMbps;

    public float getIndicatorValueRxMbps() {
        return indicatorValueRxMbps;
    }

    public void setIndicatorValueRxMbps(float indicatorValueRxMbps) {
        this.indicatorValueRxMbps = indicatorValueRxMbps;
    }

    public float getIndicatorValueTxMbps() {
        return indicatorValueTxMbps;
    }

    public void setIndicatorValueTxMbps(float indicatorValueTxMbps) {
        this.indicatorValueTxMbps = indicatorValueTxMbps;
    }

    public Map<RadioType, TrafficPerRadioDetails> getPerRadioDetails() {
		return perRadioDetails;
	}

	public void setPerRadioDetails(Map<RadioType, TrafficPerRadioDetails> perRadioDetails) {
		this.perRadioDetails = perRadioDetails;
	}

	@Override
    public TrafficDetails clone() {
        TrafficDetails ret = (TrafficDetails) super.clone();
        
        if(perRadioDetails!=null) {
        	ret.perRadioDetails = new EnumMap<>(RadioType.class);

        	perRadioDetails.forEach( (rt, cd) -> ret.perRadioDetails.put(rt, cd.clone()) );
        }

        return ret;
    }
    
    /**
     * @param other
     * @return this object, merged with other
     */
    public TrafficDetails combineWith(TrafficDetails other) {
        
        if(other == null){
            return this;
        }

        if (NumberUtils.isFloatZero(this.indicatorValueRxMbps)) {
            this.indicatorValueRxMbps = other.indicatorValueRxMbps;
        }

        if (NumberUtils.isFloatZero(this.indicatorValueTxMbps)) {
            this.indicatorValueTxMbps = other.indicatorValueTxMbps;
        }
        
        if(perRadioDetails!=null) {
        	if(other.perRadioDetails!=null) {
	        	for(Map.Entry<RadioType, TrafficPerRadioDetails> entry : other.perRadioDetails.entrySet()) {
	        		TrafficPerRadioDetails existingDetails = perRadioDetails.putIfAbsent(entry.getKey(), entry.getValue()); 
	        		if( existingDetails!=null) {
	        			perRadioDetails.get(entry.getKey()).combineWith(existingDetails);
	        		}
	        	}
        	}
        } else {
        	if(other.perRadioDetails!=null) {
        		this.perRadioDetails = new EnumMap<>(other.perRadioDetails);
        	}
        }

        return this;
    }

}
