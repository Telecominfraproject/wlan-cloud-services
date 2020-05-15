package com.telecominfraproject.wlan.status.network.models;

import java.util.EnumMap;
import java.util.Map;

import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * 
 * @author ekeddy
 *
 */
public class CapacityDetails extends BaseJsonModel {
    private static final long serialVersionUID = 1433330700529415457L;

    private Map<RadioType, CapacityPerRadioDetails> perRadioDetails = new EnumMap<>(RadioType.class);


    public Map<RadioType, CapacityPerRadioDetails> getPerRadioDetails() {
		return perRadioDetails;
	}


	public void setPerRadioDetails(Map<RadioType, CapacityPerRadioDetails> perRadioDetails) {
		this.perRadioDetails = perRadioDetails;
	}


	@Override
    public CapacityDetails clone() {
        CapacityDetails ret = (CapacityDetails) super.clone();
        
        if(perRadioDetails!=null) {
        	ret.perRadioDetails = new EnumMap<>(RadioType.class);

        	perRadioDetails.forEach( (rt, cd) -> ret.perRadioDetails.put(rt, cd.clone()) );
        }
        
        return ret;
    }

    
    public CapacityDetails combineWith(CapacityDetails other) {

        if(other == null || other.perRadioDetails == null || other.perRadioDetails.isEmpty() ){
            return this;
        }

        if(perRadioDetails!=null) {
        	
        	if(other.perRadioDetails!=null) {
	        	for(Map.Entry<RadioType, CapacityPerRadioDetails> entry : other.perRadioDetails.entrySet()) {
	        		CapacityPerRadioDetails existingDetails = perRadioDetails.putIfAbsent(entry.getKey(), entry.getValue()); 
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
