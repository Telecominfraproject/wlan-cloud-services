package com.telecominfraproject.wlan.status.equipment.report.models;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.status.models.StatusDataType;
import com.telecominfraproject.wlan.status.models.StatusDetails;

/**
 * @author dtop
 *
 */
public class RadioUtilizationReport extends StatusDetails {

	private static final long serialVersionUID = -7411506063107508280L;
    private Map<RadioType, EquipmentPerRadioUtilizationDetails> radioUtilization = new EnumMap<>(RadioType.class);
    private Map<RadioType, EquipmentCapacityDetails> capacityDetails = new EnumMap<>(RadioType.class);
    private Map<RadioType, Integer> avgNoiseFloor = new EnumMap<>(RadioType.class);

    @Override
    public StatusDataType getStatusDataType() {
    	return StatusDataType.RADIO_UTILIZATION;
    }
    
    public Map<RadioType, EquipmentPerRadioUtilizationDetails> getRadioUtilization() {
		return radioUtilization;
	}

	public void setRadioUtilization(Map<RadioType, EquipmentPerRadioUtilizationDetails> radioUtilization) {
		this.radioUtilization = radioUtilization;
	}

	public Map<RadioType, EquipmentCapacityDetails> getCapacityDetails() {
		return capacityDetails;
	}

	public void setCapacityDetails(Map<RadioType, EquipmentCapacityDetails> capacityDetails) {
		this.capacityDetails = capacityDetails;
	}

	public Map<RadioType, Integer> getAvgNoiseFloor() {
		return avgNoiseFloor;
	}

	public void setAvgNoiseFloor(Map<RadioType, Integer> avgNoiseFloor) {
		this.avgNoiseFloor = avgNoiseFloor;
	}

	@Override
    public RadioUtilizationReport clone() {
		RadioUtilizationReport ret = (RadioUtilizationReport) super.clone();
		
		if(radioUtilization!=null) {
			ret.radioUtilization = new EnumMap<>(RadioType.class);
			radioUtilization.forEach( (rt, cd) -> ret.radioUtilization.put(rt, cd.clone()) );
		}
		
		if(capacityDetails!=null) {
			ret.capacityDetails = new EnumMap<>(RadioType.class);
			capacityDetails.forEach( (rt, cd) -> ret.capacityDetails.put(rt, cd.clone()) );
		}

		if(avgNoiseFloor!=null) {
			ret.avgNoiseFloor = new EnumMap<>(RadioType.class);
			avgNoiseFloor.forEach( (rt, cd) -> ret.avgNoiseFloor.put(rt, cd) );
		}

		return ret;
    }

	@Override
	public int hashCode() {
		return Objects.hash(avgNoiseFloor, capacityDetails, radioUtilization);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof RadioUtilizationReport)) {
			return false;
		}
		RadioUtilizationReport other = (RadioUtilizationReport) obj;
		return Objects.equals(avgNoiseFloor, other.avgNoiseFloor)
				&& Objects.equals(capacityDetails, other.capacityDetails)
				&& Objects.equals(radioUtilization, other.radioUtilization);
	}

}
