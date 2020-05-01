package com.telecominfraproject.wlan.equipment.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author dtop
 * 
 * For channelWidth the value is in MHz, -1 means AUTO
 */
public class ChannelPowerLevel extends BaseJsonModel implements Comparable<ChannelPowerLevel>{

    private static final long serialVersionUID = -5086402852552958089L;

    private int channelNumber;
    private int powerLevel;
    private boolean dfs;
    /**
     * Value is in MHz, -1 means AUTO
     */
    private int channelWidth;
    
    
    public int getChannelNumber() {
        return channelNumber;
    }
    public void setChannelNumber(int channelNumber) {
        this.channelNumber = channelNumber;
    }
    public int getPowerLevel() {
        return powerLevel;
    }
    public void setPowerLevel(int powerLevel) {
        this.powerLevel = powerLevel;
    }
    public boolean isDfs() {
        return dfs;
    }
    public void setDfs(boolean dfs) {
        this.dfs = dfs;
    }
    
    /**
     * Value is in MHz, -1 means AUTO
     * @return
     */
    public int getChannelWidth() {
        return channelWidth;
    }
    
    /**
     * Value is in MHz, -1 means AUTO
     * @param channelWidth
     */
    public void setChannelWidth(int channelWidth) {
        this.channelWidth = channelWidth;
    }
    
    @Override
	public int hashCode() {
		return Objects.hash(channelNumber);
	}
    
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ChannelPowerLevel)) {
			return false;
		}
		ChannelPowerLevel other = (ChannelPowerLevel) obj;
		return channelNumber == other.channelNumber;
	}
	
	@Override
    public ChannelPowerLevel clone() {
       return (ChannelPowerLevel) super.clone();
    }

    @Override
    public int compareTo(ChannelPowerLevel o) {
        return Integer.compare(this.channelNumber, o.channelNumber);
    }

}
