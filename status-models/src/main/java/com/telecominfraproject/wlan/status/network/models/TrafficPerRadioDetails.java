package com.telecominfraproject.wlan.status.network.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.utils.NumberUtils;

/**
 * @author dtop
 *
 */
public class TrafficPerRadioDetails extends BaseJsonModel {

    private static final long serialVersionUID = 3034727832551606882L;
    
    private float minRxMbps;
    private float maxRxMbps;
    private float avgRxMbps;
    private float totalRxMbps;

    private float minTxMbps;
    private float maxTxMbps;
    private float avgTxMbps;
    private float totalTxMbps;
    
    private int numGoodEquipment;
    private int numWarnEquipment;
    private int numBadEquipment;


    private int totalApsReported;

    public float getMinRxMbps() {
        return minRxMbps;
    }

    public void setMinRxMbps(float minRxMbps) {
        this.minRxMbps = minRxMbps;
    }

    public float getMaxRxMbps() {
        return maxRxMbps;
    }

    public void setMaxRxMbps(float maxRxMbps) {
        this.maxRxMbps = maxRxMbps;
    }

    public float getAvgRxMbps() {
        return avgRxMbps;
    }

    public void setAvgRxMbps(float avgRxMbps) {
        this.avgRxMbps = avgRxMbps;
    }

    public float getMinTxMbps() {
        return minTxMbps;
    }

    public void setMinTxMbps(float minTxMbps) {
        this.minTxMbps = minTxMbps;
    }

    public float getMaxTxMbps() {
        return maxTxMbps;
    }

    public void setMaxTxMbps(float maxTxMbps) {
        this.maxTxMbps = maxTxMbps;
    }

    public float getAvgTxMbps() {
        return avgTxMbps;
    }

    public void setAvgTxMbps(float avgTxMbps) {
        this.avgTxMbps = avgTxMbps;
    }

    public int getTotalApsReported() {
        return totalApsReported;
    }

    public void setTotalApsReported(int totalApsReported) {
        this.totalApsReported = totalApsReported;
    }

    public float getTotalRxMbps() {
        return totalRxMbps;
    }

    public void setTotalRxMbps(float totalRxMbps) {
        this.totalRxMbps = totalRxMbps;
    }

    public float getTotalTxMbps() {
        return totalTxMbps;
    }

    public void setTotalTxMbps(float totalTxMbps) {
        this.totalTxMbps = totalTxMbps;
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
    public TrafficPerRadioDetails clone() {
        TrafficPerRadioDetails ret = (TrafficPerRadioDetails) super.clone();
        
        return ret;
    }
    
    /**
     * @param other
     * @return this object, merged with other
     */
    public TrafficPerRadioDetails combineWith(TrafficPerRadioDetails other) {
        
        if(other == null){
            return this;
        }
        
        if(NumberUtils.isFloatZero(this.minRxMbps)){
            this.minRxMbps = other.minRxMbps;
        }

        if(NumberUtils.isFloatZero(this.maxRxMbps)){
            this.maxRxMbps = other.maxRxMbps;
        }

        if(NumberUtils.isFloatZero(this.avgRxMbps)){
            this.avgRxMbps = other.avgRxMbps;
        }

        if(NumberUtils.isFloatZero(this.totalRxMbps)){
            this.totalRxMbps = other.totalRxMbps;
        }

        if(NumberUtils.isFloatZero(this.minTxMbps)){
            this.minTxMbps = other.minTxMbps;
        }

        if(NumberUtils.isFloatZero(this.maxTxMbps)){
            this.maxTxMbps = other.maxTxMbps;
        }

        if(NumberUtils.isFloatZero(this.avgTxMbps)){
            this.avgTxMbps = other.avgTxMbps;
        }

        if(NumberUtils.isFloatZero(this.totalTxMbps)){
            this.totalTxMbps = other.totalTxMbps;
        }

        if(this.totalApsReported == 0){
            this.totalApsReported = other.totalApsReported;
        }

        if(this.numBadEquipment==0){
            this.numBadEquipment = other.numBadEquipment;
        }

        if(this.numGoodEquipment==0){
            this.numGoodEquipment = other.numGoodEquipment;
        }

        if(this.numWarnEquipment==0){
            this.numWarnEquipment = other.numWarnEquipment;
        }

        return this;
    }

	@Override
	public int hashCode() {
		return Objects.hash(avgRxMbps, avgTxMbps, maxRxMbps, maxTxMbps, minRxMbps, minTxMbps, numBadEquipment,
				numGoodEquipment, numWarnEquipment, totalApsReported, totalRxMbps, totalTxMbps);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof TrafficPerRadioDetails)) {
			return false;
		}
		TrafficPerRadioDetails other = (TrafficPerRadioDetails) obj;
		return Float.floatToIntBits(avgRxMbps) == Float.floatToIntBits(other.avgRxMbps)
				&& Float.floatToIntBits(avgTxMbps) == Float.floatToIntBits(other.avgTxMbps)
				&& Float.floatToIntBits(maxRxMbps) == Float.floatToIntBits(other.maxRxMbps)
				&& Float.floatToIntBits(maxTxMbps) == Float.floatToIntBits(other.maxTxMbps)
				&& Float.floatToIntBits(minRxMbps) == Float.floatToIntBits(other.minRxMbps)
				&& Float.floatToIntBits(minTxMbps) == Float.floatToIntBits(other.minTxMbps)
				&& numBadEquipment == other.numBadEquipment && numGoodEquipment == other.numGoodEquipment
				&& numWarnEquipment == other.numWarnEquipment && totalApsReported == other.totalApsReported
				&& Float.floatToIntBits(totalRxMbps) == Float.floatToIntBits(other.totalRxMbps)
				&& Float.floatToIntBits(totalTxMbps) == Float.floatToIntBits(other.totalTxMbps);
	}


    
}
