package com.telecominfraproject.wlan.status.network.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author dtop
 *
 */
public class MinMaxAvgValueInt extends BaseJsonModel {

	private static final long serialVersionUID = -5381055784986531588L;
	
	private int minValue;
    private int maxValue;
    private int avgValue;
    
	public int getMinValue() {
		return minValue;
	}
	public void setMinValue(int minValue) {
		this.minValue = minValue;
	}
	public int getMaxValue() {
		return maxValue;
	}
	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}
	public int getAvgValue() {
		return avgValue;
	}
	public void setAvgValue(int avgValue) {
		this.avgValue = avgValue;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(avgValue, maxValue, minValue);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof MinMaxAvgValueInt)) {
			return false;
		}
		MinMaxAvgValueInt other = (MinMaxAvgValueInt) obj;
		return avgValue == other.avgValue && maxValue == other.maxValue && minValue == other.minValue;
	}

	@Override
	public MinMaxAvgValueInt clone() {
		return (MinMaxAvgValueInt) super.clone();
	}
	
    public MinMaxAvgValueInt combineWith(MinMaxAvgValueInt other) {
        if(other == null){
            return this;
        }

        if(minValue == 0) {
        	minValue = other.minValue;
        }
        
        if(maxValue == 0) {
        	maxValue = other.maxValue;
        }

        if(avgValue == 0) {
        	avgValue = other.avgValue;
        }

        return this;
    }
	
}
