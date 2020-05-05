package com.telecominfraproject.wlan.profile.mesh.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.equipment.NetworkInterfaceType;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author yongli
 *
 */
public class MeshPointInformation extends BaseJsonModel {
    /**
     * 
     */
    private static final long serialVersionUID = 8634788844426047213L;

    private long equipmentId;

    private RadioType meshRadio = RadioType.is5GHz;

    private MacAddress baseMacAddress;

    public MeshPointInformation(long equipmentId, MacAddress baseMacAddress) {
        this.equipmentId = equipmentId;
        this.baseMacAddress = baseMacAddress;
    }

    protected MeshPointInformation() {

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MeshPointInformation)) {
            return false;
        }
        MeshPointInformation other = (MeshPointInformation) obj;
        if (this.baseMacAddress == null) {
            if (other.baseMacAddress != null) {
                return false;
            }
        } else if (!this.baseMacAddress.equals(other.baseMacAddress)) {
            return false;
        }
        if (this.equipmentId != other.equipmentId) {
            return false;
        }
        return true;
    }

    public MacAddress getBaseMacAddress() {
        return baseMacAddress;
    }

    public long getEquipmentId() {
        return equipmentId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.baseMacAddress == null) ? 0 : this.baseMacAddress.hashCode());
        result = prime * result + (int) (this.equipmentId ^ (this.equipmentId >>> 32));
        return result;
    }

    public void setBaseMacAddress(MacAddress baseMacAddress) {
        this.baseMacAddress = baseMacAddress;
    }

    public void setEquipmentId(long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public RadioType getMeshRadio() {
        return meshRadio;
    }

    public void setMeshRadio(RadioType meshRadio) {
        this.meshRadio = meshRadio;
    }

    @JsonIgnore
    public MacAddress getMeshMacAddress() {
        if (null == this.baseMacAddress) {
            return null;
        }
        if (RadioType.is2dot4GHz.equals(this.meshRadio)) {
            return NetworkInterfaceType.getInterfaceMac(this.baseMacAddress, NetworkInterfaceType.RADIO_2G);
        } else {
            return NetworkInterfaceType.getInterfaceMac(this.baseMacAddress, NetworkInterfaceType.RADIO_5G);
        }
    }
    
    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (RadioType.isUnsupported(this.meshRadio)) {
            return true;
        }
        return false;
    }
}
