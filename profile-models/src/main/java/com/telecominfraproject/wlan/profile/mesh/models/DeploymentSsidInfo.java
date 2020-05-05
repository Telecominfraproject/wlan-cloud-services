package com.telecominfraproject.wlan.profile.mesh.models;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author yongli
 *
 */
public class DeploymentSsidInfo extends BaseJsonModel {
    /**
     * 
     */
    private static final long serialVersionUID = 8972513792442526058L;
    private static final String DEFAULT_SSID_NAME = "Default-SSID";

    private DeploymentSsidType type;
    private String ssidName = DEFAULT_SSID_NAME;
    private String securityKey;

    public DeploymentSsidInfo(DeploymentSsidType type, String ssidName, String securityKey) {
        this.type = type;
        this.ssidName = ssidName;
        this.securityKey = securityKey;
    }

    protected DeploymentSsidInfo() {
    }

    @Override
    public DeploymentSsidInfo clone() {
        return (DeploymentSsidInfo) super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof DeploymentSsidInfo)) {
            return false;
        }
        DeploymentSsidInfo other = (DeploymentSsidInfo) obj;
        if (this.securityKey == null) {
            if (other.securityKey != null) {
                return false;
            }
        } else if (!this.securityKey.equals(other.securityKey)) {
            return false;
        }
        if (this.ssidName == null) {
            if (other.ssidName != null) {
                return false;
            }
        } else if (!this.ssidName.equals(other.ssidName)) {
            return false;
        }
        if (this.type != other.type) {
            return false;
        }
        return true;
    }

    public String getSecurityKey() {
        return securityKey;
    }

    public String getSsidName() {
        return ssidName;
    }

    public DeploymentSsidType getType() {
        return type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.securityKey == null) ? 0 : this.securityKey.hashCode());
        result = prime * result + ((this.ssidName == null) ? 0 : this.ssidName.hashCode());
        result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
        return result;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (DeploymentSsidType.isUnsupported(this.type)) {
            return true;
        }
        return false;
    }

    public void setSecurityKey(String securityKey) {
        this.securityKey = securityKey;
    }

    public void setSsidName(String ssidName) {
        this.ssidName = ssidName;
    }
    
    public void setType(DeploymentSsidType type) {
        this.type = type;
    }
}
