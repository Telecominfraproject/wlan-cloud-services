package com.telecominfraproject.wlan.status.network.models;

import com.telecominfraproject.wlan.core.model.entity.MinMaxAvgValueInt;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;


public class CapacityPerRadioDetails extends BaseJsonModel {
    private static final long serialVersionUID = 3115679240240112531L;


    // A metric that represents the relative maximum airtime.
    private Integer totalCapacity;
    
    private MinMaxAvgValueInt availableCapacity;
    private MinMaxAvgValueInt unavailableCapacity;
    private MinMaxAvgValueInt usedCapacity;
    private MinMaxAvgValueInt unusedCapacity;
    
    public Integer getTotalCapacity() {
        return totalCapacity;
    }

    public void setTotalCapacity(Integer totalCapacity) {
        this.totalCapacity = totalCapacity;
    }

    public MinMaxAvgValueInt getAvailableCapacity() {
		return availableCapacity;
	}

	public void setAvailableCapacity(MinMaxAvgValueInt availableCapacity) {
		this.availableCapacity = availableCapacity;
	}

	public MinMaxAvgValueInt getUnavailableCapacity() {
		return unavailableCapacity;
	}

	public void setUnavailableCapacity(MinMaxAvgValueInt unavailableCapacity) {
		this.unavailableCapacity = unavailableCapacity;
	}

	public MinMaxAvgValueInt getUsedCapacity() {
		return usedCapacity;
	}

	public void setUsedCapacity(MinMaxAvgValueInt usedCapacity) {
		this.usedCapacity = usedCapacity;
	}

	public MinMaxAvgValueInt getUnusedCapacity() {
		return unusedCapacity;
	}

	public void setUnusedCapacity(MinMaxAvgValueInt unusedCapacity) {
		this.unusedCapacity = unusedCapacity;
	}

	@Override
    public CapacityPerRadioDetails clone() {
    	CapacityPerRadioDetails ret = (CapacityPerRadioDetails) super.clone();
    	
    	if(availableCapacity!=null) {
    		ret.availableCapacity = availableCapacity.clone();
    	}
    	
    	if(unavailableCapacity!=null) {
    		ret.unavailableCapacity = unavailableCapacity.clone();
    	}

    	if(usedCapacity!=null) {
    		ret.usedCapacity = usedCapacity.clone();
    	}

    	if(unusedCapacity!=null) {
    		ret.unusedCapacity = unusedCapacity.clone();
    	}

    	return ret;
    }

    public CapacityPerRadioDetails combineWith(CapacityPerRadioDetails other) {
        
        if(other == null){
            return this;
        }
        
        if(this.totalCapacity == null){
            this.totalCapacity = other.totalCapacity;
        }
        
        if(this.availableCapacity == null){
            this.availableCapacity = other.availableCapacity;
        } else {
        	if(other.availableCapacity!=null) {
        		this.availableCapacity.combineWith(other.availableCapacity);
        	}
        }

        if(this.unavailableCapacity == null){
            this.unavailableCapacity = other.unavailableCapacity;
        } else {
        	if(other.unavailableCapacity!=null) {
        		this.unavailableCapacity.combineWith(other.unavailableCapacity);
        	}
        }

        if(this.usedCapacity == null){
            this.usedCapacity = other.usedCapacity;
        } else {
        	if(other.usedCapacity!=null) {
        		this.usedCapacity.combineWith(other.usedCapacity);
        	}
        }

        if(this.unusedCapacity == null){
            this.unusedCapacity = other.unusedCapacity;
        } else {
        	if(other.unusedCapacity!=null) {
        		this.unusedCapacity.combineWith(other.unusedCapacity);
        	}
        }

        return this;
    }

}
