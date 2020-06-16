package com.telecominfraproject.wlan.status.equipment.models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.telecominfraproject.wlan.status.models.StatusDataType;
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
    
    private boolean rebooted = false;
    /**
     * reset method
     */
    private EquipmentResetMethod performReset = EquipmentResetMethod.NoReset;


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
    
    @Override
    public StatusDataType getStatusDataType() {
    	return StatusDataType.FIRMWARE;
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

	public boolean isRebooted() {
		return rebooted;
	}

	public void setRebooted(boolean rebooted) {
		this.rebooted = rebooted;
	}

	public EquipmentResetMethod getPerformReset() {
		return performReset;
	}

	public void setPerformReset(EquipmentResetMethod performReset) {
		this.performReset = performReset;
	}

	@Override
	public int hashCode() {
		return Objects.hash(activeSwVersion, alternateSwVersion, performReset, reason, rebooted, retries, switchBank,
				targetSwVersion, upgradeStartTime, upgradeState);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof EquipmentUpgradeStatusData)) {
			return false;
		}
		EquipmentUpgradeStatusData other = (EquipmentUpgradeStatusData) obj;
		return Objects.equals(activeSwVersion, other.activeSwVersion)
				&& Objects.equals(alternateSwVersion, other.alternateSwVersion) && performReset == other.performReset
				&& reason == other.reason && rebooted == other.rebooted && retries == other.retries
				&& Objects.equals(switchBank, other.switchBank)
				&& Objects.equals(targetSwVersion, other.targetSwVersion)
				&& Objects.equals(upgradeStartTime, other.upgradeStartTime) && upgradeState == other.upgradeState;
	}
    
}
