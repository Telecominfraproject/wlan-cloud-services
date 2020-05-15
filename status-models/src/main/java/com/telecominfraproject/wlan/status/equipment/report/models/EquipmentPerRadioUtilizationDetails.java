package com.telecominfraproject.wlan.status.equipment.report.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.status.network.models.MinMaxAvgValueInt;

public class EquipmentPerRadioUtilizationDetails extends BaseJsonModel {
    private static final long serialVersionUID = -3017963520011190786L;
    
    private MinMaxAvgValueInt wifiFromOtherBss;
    
    public MinMaxAvgValueInt getWifiFromOtherBss() {
		return wifiFromOtherBss;
	}

	public void setWifiFromOtherBss(MinMaxAvgValueInt wifiFromOtherBss) {
		this.wifiFromOtherBss = wifiFromOtherBss;
	}

	
	@Override
	public int hashCode() {
		return Objects.hash(wifiFromOtherBss);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof EquipmentPerRadioUtilizationDetails)) {
			return false;
		}
		EquipmentPerRadioUtilizationDetails other = (EquipmentPerRadioUtilizationDetails) obj;
		return Objects.equals(wifiFromOtherBss, other.wifiFromOtherBss);
	}

	@Override
    public EquipmentPerRadioUtilizationDetails clone() {
		EquipmentPerRadioUtilizationDetails ret = (EquipmentPerRadioUtilizationDetails) super.clone();
		
		if(wifiFromOtherBss!=null) {
			ret.wifiFromOtherBss = wifiFromOtherBss.clone();
		}
		return ret;
    }

}
