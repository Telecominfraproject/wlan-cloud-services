package com.telecominfraproject.wlan.status.network.models;

import java.util.EnumMap;
import java.util.Map;

import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author dtop
 *
 */
public class ChannelUtilizationDetails extends BaseJsonModel {

    private static final long serialVersionUID = 3034727832551606882L;
    
    private int indicatorValue;
    private Map<RadioType, ChannelUtilizationPerRadioDetails> perRadioDetails = new EnumMap<>(RadioType.class);

    public int getIndicatorValue() {
        return indicatorValue;
    }

    public void setIndicatorValue(int indicatorValue) {
        this.indicatorValue = indicatorValue;
    }

    public Map<RadioType, ChannelUtilizationPerRadioDetails> getPerRadioDetails() {
		return perRadioDetails;
	}

	public void setPerRadioDetails(Map<RadioType, ChannelUtilizationPerRadioDetails> perRadioDetails) {
		this.perRadioDetails = perRadioDetails;
	}

	@Override
    public ChannelUtilizationDetails clone() {
        ChannelUtilizationDetails ret = (ChannelUtilizationDetails) super.clone();
               
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
    public ChannelUtilizationDetails combineWith(ChannelUtilizationDetails other) {

        if(other == null || other.perRadioDetails == null || other.perRadioDetails.isEmpty() ){
            return this;
        }

        if(this.indicatorValue==0){
            this.indicatorValue = other.indicatorValue;
        }

        if(perRadioDetails!=null) {
        	if(other.perRadioDetails!=null) {
	        	for(Map.Entry<RadioType, ChannelUtilizationPerRadioDetails> entry : other.perRadioDetails.entrySet()) {
	        		ChannelUtilizationPerRadioDetails existingDetails =perRadioDetails.putIfAbsent(entry.getKey(), entry.getValue()); 
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
