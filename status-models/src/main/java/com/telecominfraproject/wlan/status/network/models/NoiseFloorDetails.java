package com.telecominfraproject.wlan.status.network.models;

import java.util.EnumMap;
import java.util.Map;

import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author dtop
 *
 */
public class NoiseFloorDetails extends BaseJsonModel {

    private static final long serialVersionUID = 3034727832551606882L;
    
    private Map<RadioType, NoiseFloorPerRadioDetails> perRadioDetails = new EnumMap<>(RadioType.class);
    private Integer indicatorValue;

	public Map<RadioType, NoiseFloorPerRadioDetails> getPerRadioDetails() {
		return perRadioDetails;
	}

	public void setPerRadioDetails(Map<RadioType, NoiseFloorPerRadioDetails> perRadioDetails) {
		this.perRadioDetails = perRadioDetails;
	}

	public Integer getIndicatorValue() {
        return indicatorValue;
    }

    public void setIndicatorValue(Integer indicatorValue) {
        this.indicatorValue = indicatorValue;
    }

    @Override
    public NoiseFloorDetails clone() {
        NoiseFloorDetails ret = (NoiseFloorDetails) super.clone();
        
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
    public NoiseFloorDetails combineWith(NoiseFloorDetails other) {
        
        if(other == null || other.perRadioDetails == null || other.perRadioDetails.isEmpty() ){
            return this;
        }

        if(this.indicatorValue==0){
            this.indicatorValue = other.indicatorValue;
        }

        if(perRadioDetails!=null) {
        	if(other.perRadioDetails!=null) {
	        	for(Map.Entry<RadioType, NoiseFloorPerRadioDetails> entry : other.perRadioDetails.entrySet()) {
	        		NoiseFloorPerRadioDetails existingDetails =perRadioDetails.putIfAbsent(entry.getKey(), entry.getValue()); 
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
