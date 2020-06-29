package com.telecominfraproject.wlan.customer.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;


/**
 * @author dtoptygin
 *
 */
public class EquipmentAutoProvisioningSettings extends BaseJsonModel{
    private static final long serialVersionUID = 3061657893135029599L;

    public static final String DEFAULT_MODEL_NAME="default";
    
    private boolean enabled;
    private long locationId;
    private Map<String, Long> equipmentProfileIdPerModel = new HashMap<>();

    public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public long getLocationId() {
		return locationId;
	}

	public void setLocationId(long locationId) {
		this.locationId = locationId;
	}

	public Map<String, Long> getEquipmentProfileIdPerModel() {
		return equipmentProfileIdPerModel;
	}

	public void setEquipmentProfileIdPerModel(Map<String, Long> equipmentProfileIdPerModel) {
		this.equipmentProfileIdPerModel = equipmentProfileIdPerModel;
	}

	@Override
    public EquipmentAutoProvisioningSettings clone() {
        EquipmentAutoProvisioningSettings ret = (EquipmentAutoProvisioningSettings) super.clone();

        if(equipmentProfileIdPerModel!=null) {
        	ret.equipmentProfileIdPerModel = new HashMap<>(equipmentProfileIdPerModel);
        }
        
        return ret;
    }

	@Override
	public int hashCode() {
		return Objects.hash(enabled, equipmentProfileIdPerModel, locationId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof EquipmentAutoProvisioningSettings)) {
			return false;
		}
		EquipmentAutoProvisioningSettings other = (EquipmentAutoProvisioningSettings) obj;
		return enabled == other.enabled && Objects.equals(equipmentProfileIdPerModel, other.equipmentProfileIdPerModel)
				&& locationId == other.locationId;
	}
    
}
