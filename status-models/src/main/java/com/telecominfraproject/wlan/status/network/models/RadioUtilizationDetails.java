package com.telecominfraproject.wlan.status.network.models;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author dtop
 *
 */
public class RadioUtilizationDetails extends BaseJsonModel {

    private static final long serialVersionUID = 3034727832551606882L;
    
    private Map<RadioType, RadioUtilizationPerRadioDetails> perRadioDetails = new EnumMap<>(RadioType.class);
    

    public RadioUtilizationDetails() {
    	// serialize
    }
    
    public Map<RadioType, RadioUtilizationPerRadioDetails> getPerRadioDetails() {
		return perRadioDetails;
	}

	public void setPerRadioDetails(Map<RadioType, RadioUtilizationPerRadioDetails> perRadioDetails) {
		this.perRadioDetails = perRadioDetails;
	}
	
	@Override
    public RadioUtilizationDetails clone() {
        RadioUtilizationDetails ret = (RadioUtilizationDetails) super.clone();
        
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
    public RadioUtilizationDetails combineWith(RadioUtilizationDetails other) {
        
        if(other == null || other.perRadioDetails == null || other.perRadioDetails.isEmpty() ){
            return this;
        }

        if(perRadioDetails!=null) {
        	if(other.perRadioDetails!=null) {
	        	for(Map.Entry<RadioType, RadioUtilizationPerRadioDetails> entry : other.perRadioDetails.entrySet()) {
	        		RadioUtilizationPerRadioDetails existingDetails = perRadioDetails.putIfAbsent(entry.getKey(), entry.getValue()); 
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

    @Override
    public int hashCode() {
        return Objects.hash(perRadioDetails);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof RadioUtilizationDetails)) {
            return false;
        }
        RadioUtilizationDetails other = (RadioUtilizationDetails) obj;
        return Objects.equals(perRadioDetails, other.perRadioDetails);
   }
   
   @Override
   public boolean hasUnsupportedValue() {
       if (super.hasUnsupportedValue()) {
           return true;
       }
       return false;
   }    
}
