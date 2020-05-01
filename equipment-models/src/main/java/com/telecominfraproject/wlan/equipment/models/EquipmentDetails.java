package com.telecominfraproject.wlan.equipment.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author dtoptygin
 *
 */
public class EquipmentDetails extends BaseJsonModel {
    
	private static final long serialVersionUID = 5570757656953699233L;
	
    //TODO: put more fields here, generate getters/setters for them
    private String sampleDetailsStr;

	public String getSampleDetailsStr() {
		return sampleDetailsStr;
	}

	public void setSampleDetailsStr(String sampleDetailsStr) {
		this.sampleDetailsStr = sampleDetailsStr;
	}

	@Override
	public boolean hasUnsupportedValue() {
		if (super.hasUnsupportedValue()) {
			return true;
		}

		return false;
	}
	
    @Override
    public EquipmentDetails clone() {
    	EquipmentDetails ret = (EquipmentDetails) super.clone();
    	
        return ret;
    }

	@Override
	public int hashCode() {
		return Objects.hash(sampleDetailsStr);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof EquipmentDetails)) {
			return false;
		}
		EquipmentDetails other = (EquipmentDetails) obj;
		return Objects.equals(sampleDetailsStr, other.sampleDetailsStr);
	}

	
}
