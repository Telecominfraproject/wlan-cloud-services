package com.telecominfraproject.wlan.status.equipment.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.telecominfraproject.wlan.status.models.StatusDetails;

/**
 * Equipment upgrade status data
 * 
 * @author yongli
 *
 */
@JsonSerialize()
@JsonIgnoreProperties(ignoreUnknown = true)
public class EquipmentUpgradeStatusData extends StatusDetails {
    /**
     * Serial number
     */
    private static final long serialVersionUID = -4996656433150367108L;
    
    private String activeSwVersion;
    private String alternateSwVersion;
    /**
     * Target version
     */
    private String targetSwVersion;
    /**
     * Number of retries
     */
    private int retries = 0;
    /**
     * Upgrade state
     */
    private EquipmentUpgradeState upgradeState;
    /**
     * Failure reason
     */
    private EquipmentUpgradeState.FailureReason reason;
    /**
     * Upgrade start time
     */
    private Long upgradeStartTime;
    
    private Boolean switchBank;

    public EquipmentUpgradeStatusData(EquipmentUpgradeStatusData  data)
    {
        this();
        this.activeSwVersion = data.activeSwVersion;
        this.alternateSwVersion = data.alternateSwVersion;
        this.reason = data.reason;
        this.retries = data.retries;
        this.switchBank = data.switchBank;
        this.targetSwVersion = data.targetSwVersion;
        this.upgradeStartTime = data.upgradeStartTime;
        this.upgradeState = data.upgradeState;
    }
    
    public EquipmentUpgradeStatusData()
    {
        // nothing to do for serialization
    }
    
    
    public String getActiveSwVersion() {
        return activeSwVersion;
    }

    public void setActiveSwVersion(String activeSwVersion) {
        this.activeSwVersion = activeSwVersion;
    }

    public String getAlternateSwVersion() {
        return alternateSwVersion;
    }

    public void setAlternateSwVersion(String alternateSwVersion) {
        this.alternateSwVersion = alternateSwVersion;
    }

    @Override
    public EquipmentUpgradeStatusData clone() {
        return (EquipmentUpgradeStatusData) super.clone();
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof EquipmentUpgradeStatusData)) {
            return false;
        }
        EquipmentUpgradeStatusData other = (EquipmentUpgradeStatusData) obj;
        if (reason != other.reason) {
            return false;
        }
        if (retries != other.retries) {
            return false;
        }
        if (targetSwVersion == null) {
            if (other.targetSwVersion != null) {
                return false;
            }
        } else if (!targetSwVersion.equals(other.targetSwVersion)) {
            return false;
        }
        if (upgradeStartTime == null) {
            if (other.upgradeStartTime != null) {
                return false;
            }
        } else if (!upgradeStartTime.equals(other.upgradeStartTime)) {
            return false;
        }
        if (upgradeState != other.upgradeState) {
            return false;
        }
        return true;
    }

    public EquipmentUpgradeState.FailureReason getReason() {
        return reason;
    }

    public int getRetries() {
        return retries;
    }

    public String getTargetSwVersion() {
        return targetSwVersion;
    }


    public Long getUpgradeStartTime() {
        return upgradeStartTime;
    }

    public EquipmentUpgradeState getUpgradeState() {
        return upgradeState;
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((reason == null) ? 0 : reason.hashCode());
        result = prime * result + retries;
        result = prime * result + ((targetSwVersion == null) ? 0 : targetSwVersion.hashCode());
        result = prime * result + ((upgradeStartTime == null) ? 0 : upgradeStartTime.hashCode());
        result = prime * result + ((upgradeState == null) ? 0 : upgradeState.hashCode());
        return result;
    }

    public void setReason(EquipmentUpgradeState.FailureReason reason) {
        this.reason = reason;
    }

    public void setRetries(int retries) {
        this.retries = retries;
    }

    public void setTargetSwVersion(String targetSwVersion) {
        this.targetSwVersion = targetSwVersion;
    }

    public void setUpgradeStartTime(Long upgradeStartTime) {
        this.upgradeStartTime = upgradeStartTime;
    }

    public void setUpgradeState(EquipmentUpgradeState upgradeState) {
        this.upgradeState = upgradeState;
    }

    public void setUpgradeState(EquipmentUpgradeState upgradeState, EquipmentUpgradeState.FailureReason reason) {
        this.upgradeState = upgradeState;
        this.reason = reason;
    }

    /**
     * @return the switchBank
     */
    public boolean getSwitchBank() {
        return Boolean.TRUE.equals(this.switchBank);
    }

    /**
     * @param switchBank the switchBank to set
     */
    public void setSwitchBank(Boolean switchBank) {
        this.switchBank = switchBank;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (EquipmentUpgradeState.FailureReason.isUnsupported(reason)
                || EquipmentUpgradeState.isUnsupported(upgradeState)) {
            return true;
        }
        return false;
    }
}
