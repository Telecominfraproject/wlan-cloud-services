package com.telecominfraproject.wlan.status.network.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author dtop
 *
 */
public class NoiseFloorPerRadioDetails extends BaseJsonModel {

    private static final long serialVersionUID = -4773437330014768087L;
    
    private MinMaxAvgValueInt noiseFloor;
    private int numGoodEquipment;
    private int numWarnEquipment;
    private int numBadEquipment;

    public MinMaxAvgValueInt getNoiseFloor() {
		return noiseFloor;
	}

	public void setNoiseFloor(MinMaxAvgValueInt noiseFloor) {
		this.noiseFloor = noiseFloor;
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
    public NoiseFloorPerRadioDetails clone() {
        NoiseFloorPerRadioDetails ret = (NoiseFloorPerRadioDetails) super.clone();
        
        if(this.noiseFloor!=null){
            ret.noiseFloor = this.noiseFloor.clone();
        }

        return ret;
    }
    
    /**
     * @param other
     * @return this object, merged with other
     */
    public NoiseFloorPerRadioDetails combineWith(NoiseFloorPerRadioDetails other) {
        
        if(other == null){
            return this;
        }
        
        if(this.noiseFloor !=null){
            this.noiseFloor.combineWith(other.noiseFloor);
        } else {
        	this.noiseFloor = other.noiseFloor;
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
		return Objects.hash(noiseFloor, numBadEquipment, numGoodEquipment, numWarnEquipment);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof NoiseFloorPerRadioDetails)) {
			return false;
		}
		NoiseFloorPerRadioDetails other = (NoiseFloorPerRadioDetails) obj;
		return Objects.equals(noiseFloor, other.noiseFloor) && numBadEquipment == other.numBadEquipment
				&& numGoodEquipment == other.numGoodEquipment && numWarnEquipment == other.numWarnEquipment;
	}

}
