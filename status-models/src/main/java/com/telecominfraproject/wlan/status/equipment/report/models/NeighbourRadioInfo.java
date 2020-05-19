package com.telecominfraproject.wlan.status.equipment.report.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author ekeddy
 *
 */
public class NeighbourRadioInfo extends BaseJsonModel {
    private static final long serialVersionUID = -5474176952026949684L;
    
    private RadioType radioType;
	private int channel;
	private List<NeighbourBssidInfo> bssIds;

	public NeighbourRadioInfo() {
	}
	public NeighbourRadioInfo(RadioType radioType, int channel) {
		this.radioType = radioType;
		this.channel = channel;
	}
	public RadioType getRadioType() {
		return radioType;
	}
	public void setRadioType(RadioType radioType) {
		this.radioType = radioType;
	}
	public List<NeighbourBssidInfo> getBssIds() {
		return bssIds;
	}
	public void setBssIds(List<NeighbourBssidInfo> bssIds) {
		this.bssIds = bssIds;
	}
	public int getChannel() {
		return channel;
	}
	public void setChannel(int channel) {
		this.channel = channel;
	}
	@Override
    public NeighbourRadioInfo clone() {
    	NeighbourRadioInfo ret = (NeighbourRadioInfo)super.clone();
    	if(this.bssIds != null) {
    		ret.bssIds = new ArrayList<>(this.bssIds.size());
    		this.bssIds.forEach(b -> ret.bssIds.add(b.clone()));
    	}
    	return ret;
    }
    
    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (RadioType.isUnsupported(radioType) || hasUnsupportedValue(bssIds)) {
            return true;
        }
        return false;
    }
    
	@Override
	public int hashCode() {
		return Objects.hash(bssIds, channel, radioType);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof NeighbourRadioInfo)) {
			return false;
		}
		NeighbourRadioInfo other = (NeighbourRadioInfo) obj;
		return Objects.equals(bssIds, other.bssIds) && channel == other.channel && radioType == other.radioType;
	}

}
