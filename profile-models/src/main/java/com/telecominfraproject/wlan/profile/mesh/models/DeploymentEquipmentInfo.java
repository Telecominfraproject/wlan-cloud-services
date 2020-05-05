package com.telecominfraproject.wlan.profile.mesh.models;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.equipment.models.NetworkForwardMode;

/**
 * Deployment information for equipment
 * 
 * @author yongli
 *
 */
public class DeploymentEquipmentInfo extends BaseJsonModel {

    /**
     * 
     */
    private static final long serialVersionUID = 2579993652990479991L;

    private long equipmentId;

    private String qrCode;

    /**
     * Name for the AP.
     */
    private String apName;

    /**
     * Use mesh mode as the role of the AP
     */
    private ApMeshMode role;

    private NetworkForwardMode forwardMode;

    public DeploymentEquipmentInfo(long equipmentId, String qrCode, String apName, ApMeshMode role,
            NetworkForwardMode forwardMode) {
        this.equipmentId = equipmentId;
        this.qrCode = qrCode;
        this.apName = apName;
        this.role = role;
        this.forwardMode = forwardMode;
    }

    protected DeploymentEquipmentInfo() {

    }

    @Override
    public DeploymentEquipmentInfo clone() {
        return (DeploymentEquipmentInfo) super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof DeploymentEquipmentInfo)) {
            return false;
        }
        DeploymentEquipmentInfo other = (DeploymentEquipmentInfo) obj;
        if (this.apName == null) {
            if (other.apName != null) {
                return false;
            }
        } else if (!this.apName.equals(other.apName)) {
            return false;
        }
        if (this.equipmentId != other.equipmentId) {
            return false;
        }
        if (this.forwardMode != other.forwardMode) {
            return false;
        }
        if (this.qrCode == null) {
            if (other.qrCode != null) {
                return false;
            }
        } else if (!this.qrCode.equals(other.qrCode)) {
            return false;
        }
        if (this.role != other.role) {
            return false;
        }
        return true;
    }

    public String getApName() {
        return apName;
    }

    public long getEquipmentId() {
        return equipmentId;
    }

    public NetworkForwardMode getForwardMode() {
        return forwardMode;
    }

    public String getQrCode() {
        return qrCode;
    }

    public ApMeshMode getRole() {
        return role;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.apName == null) ? 0 : this.apName.hashCode());
        result = prime * result + (int) (this.equipmentId ^ (this.equipmentId >>> 32));
        result = prime * result + ((this.forwardMode == null) ? 0 : this.forwardMode.hashCode());
        result = prime * result + ((this.qrCode == null) ? 0 : this.qrCode.hashCode());
        result = prime * result + ((this.role == null) ? 0 : this.role.hashCode());
        return result;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (ApMeshMode.isUnsupported(this.role)) {
            return true;
        }
        if (NetworkForwardMode.isUnsupported(this.forwardMode)) {
            return true;
        }
        return false;
    }

    public void setApName(String apName) {
        this.apName = apName;
    }

    public void setEquipmentId(long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public void setForwardMode(NetworkForwardMode mode) {
        this.forwardMode = mode;
    }

    public void setQrCode(String qrCode) {
        this.qrCode = qrCode;
    }

    public void setRole(ApMeshMode role) {
        this.role = role;
    }

}
