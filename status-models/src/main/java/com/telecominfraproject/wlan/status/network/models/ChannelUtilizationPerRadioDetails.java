package com.telecominfraproject.wlan.status.network.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author dtop
 *
 */
public class ChannelUtilizationPerRadioDetails extends BaseJsonModel {

    private static final long serialVersionUID = -4773437330014768087L;
    
    private MinMaxAvgValueInt channelUtilization;
    private int numGoodEquipment;
    private int numWarnEquipment;
    private int numBadEquipment;

    public MinMaxAvgValueInt getChannelUtilization() {
		return channelUtilization;
	}

	public void setChannelUtilization(MinMaxAvgValueInt channelUtilization) {
		this.channelUtilization = channelUtilization;
	}

	public int getNumGoodEquipment() {
		return numGoodEquipment;
	}

	public void setNumGoodEquipment(int numGoodEquipment) {
		this.numGoodEquipment = numGoodEquipment;
	}

	public int getNumWarnEquipment() {
		return numWarnEquipment;
	}

	public void setNumWarnEquipment(int numWarnEquipment) {
		this.numWarnEquipment = numWarnEquipment;
	}

	public int getNumBadEquipment() {
		return numBadEquipment;
	}

	public void setNumBadEquipment(int numBadEquipment) {
		this.numBadEquipment = numBadEquipment;
	}

	@Override
    public ChannelUtilizationPerRadioDetails clone() {
        ChannelUtilizationPerRadioDetails ret = (ChannelUtilizationPerRadioDetails) super.clone();
        
        if(this.channelUtilization!=null){
            ret.channelUtilization = this.channelUtilization.clone();
        }

        return ret;
    }
    
    /**
     * @param other
     * @return this object, merged with other
     */
    public ChannelUtilizationPerRadioDetails combineWith(ChannelUtilizationPerRadioDetails other) {
        
        if(other == null){
            return this;
        }
        
        if(this.channelUtilization !=null){
            this.channelUtilization.combineWith(other.channelUtilization);
        } else {
        	this.channelUtilization = other.channelUtilization;
        }

        if(this.numGoodEquipment == 0){
            this.numGoodEquipment = other.numGoodEquipment;
        }

        if(this.numWarnEquipment == 0){
            this.numWarnEquipment = other.numWarnEquipment;
        }

        if(this.numBadEquipment == 0){
            this.numBadEquipment = other.numBadEquipment;
        }

        return this;
    }

	@Override
	public int hashCode() {
		return Objects.hash(channelUtilization, numBadEquipment, numGoodEquipment, numWarnEquipment);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ChannelUtilizationPerRadioDetails)) {
			return false;
		}
		ChannelUtilizationPerRadioDetails other = (ChannelUtilizationPerRadioDetails) obj;
		return Objects.equals(channelUtilization, other.channelUtilization) && numBadEquipment == other.numBadEquipment
				&& numGoodEquipment == other.numGoodEquipment && numWarnEquipment == other.numWarnEquipment;
	}

}
