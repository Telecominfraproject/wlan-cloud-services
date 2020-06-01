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
    private String equipmentModel;

	public String getEquipmentModel() {
		return equipmentModel;
	}

	public void setEquipmentModel(String equipmentModel) {
		this.equipmentModel = equipmentModel;
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
		return Objects.hash(equipmentModel);
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
		return Objects.equals(equipmentModel, other.equipmentModel);
	}

	
}
