package com.telecominfraproject.wlan.equipment.models;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.DeploymentType;
import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;

/**
 * @author dtoptygin
 *
 */
public class ApElementConfiguration extends CommonElementConfiguration {

	public static enum ApModel {
		INDOOR, OUTDOOR
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -5866232445606459196L;

	public static final String DEFAULT_VERSION = "AP-V1";

	/**
	 * These are the radio indexes in the radio map.
	 */
	public static final int FIVE_GHZ_RADIO_INDEX = 0;
	public static final int TWO_POINT_FOUR_GHZ_RADIO_INDEX = 1;

	public static ApElementConfiguration createWithDefaults() {
		return createWithDefaults(DEFAULT_VERSION, ApModel.INDOOR);
	}

	public static ApElementConfiguration createWithDefaults(String elementVersion) {
		return createWithDefaults(elementVersion, ApModel.INDOOR);
	}

	private Map<RadioType, ElementRadioConfiguration> radioMap;

	/*
	 * These are the "advanced" radio configuration. They can be redefined via the
	 * associated AP Profile or through tweaking the global defaults.
	 * 
	 */
	private Map<RadioType, RadioConfiguration> advancedRadioMap;

	
    private ApElementConfiguration() {
        //for serialization
        this(false);
    }

	
	/*
	 * NOTE: Please use the static creator
	 */
	private ApElementConfiguration(boolean useDefaults) {
		super(EquipmentType.AP);

		if(useDefaults) {
    		/* We populate the radio map */
    		radioMap = new EnumMap<>(RadioType.class);
    		radioMap.put(RadioType.is5GHzL, ElementRadioConfiguration.createWithDefaults(RadioType.is5GHzL));
    		radioMap.put(RadioType.is5GHzU, ElementRadioConfiguration.createWithDefaults(RadioType.is5GHzU));
    		radioMap.put(RadioType.is2dot4GHz, ElementRadioConfiguration.createWithDefaults(RadioType.is2dot4GHz));
    
    		/* We populate the advanced radio map */
    		advancedRadioMap = new EnumMap<>(RadioType.class);
    		advancedRadioMap.put(RadioType.is2dot4GHz, RadioConfiguration.createWithDefaults(RadioType.is2dot4GHz));
    		advancedRadioMap.put(RadioType.is5GHzL, RadioConfiguration.createWithDefaults(RadioType.is5GHzL));
    		advancedRadioMap.put(RadioType.is5GHzU, RadioConfiguration.createWithDefaults(RadioType.is5GHzU));
		}
		
	}

	@Override
	public ApElementConfiguration clone() {
		ApElementConfiguration ret = (ApElementConfiguration) super.clone();

		if (radioMap != null) {
			ret.radioMap = new HashMap<>();

			for (Map.Entry<RadioType, ElementRadioConfiguration> entry : radioMap.entrySet()) {
				ret.radioMap.put(entry.getKey(), entry.getValue().clone());
			}
		}

		if (advancedRadioMap != null) {
			ret.advancedRadioMap = new EnumMap<>(RadioType.class);

			for (Map.Entry<RadioType, RadioConfiguration> entry : advancedRadioMap.entrySet()) {
				ret.advancedRadioMap.put(entry.getKey(), entry.getValue().clone());
			}
		}

		return ret;
	}

	public Map<RadioType, RadioConfiguration> getAdvancedRadioMap() {
		return this.advancedRadioMap;
	}

	public Map<RadioType, ElementRadioConfiguration> getRadioMap() {
		return radioMap;
	}
	
	public ElementRadioConfiguration getElementRadioConfiguration(RadioType radioType) {
		if (radioMap == null) {
			return null;
		}
		return radioMap.get(radioType);
	}

	public static ApElementConfiguration createWithDefaults(String elementVersion, ApModel model) {

		// The constructor will populate the radio maps
		ApElementConfiguration returnValue = new ApElementConfiguration(true);
		returnValue.setElementConfigVersion(elementVersion);
		returnValue.setEquipmentType(EquipmentType.AP);

		returnValue.setGettingDNS(GettingDNS.dhcp);
		returnValue.setGettingIP(GettingIP.dhcp);
		returnValue.setDeviceMode(DeviceMode.standaloneAP);
		returnValue.setDeploymentType(DeploymentType.CEILING);
		returnValue.setSyntheticClientEnabled(null);
		returnValue.setFrameReportThrottleEnabled(true);
		returnValue.setAntennaType(AntennaType.OMNI);
		returnValue.setCostSavingEventsEnabled(true);

		// TODO: MikeH Do we have outdoor AP, if so, need U/L Band channels for defaults?
		if (model == ApModel.OUTDOOR) {
			returnValue.getRadioMap().get(RadioType.is5GHzU).setBackupChannelNumber(157);
			returnValue.getRadioMap().get(RadioType.is5GHzU).setChannelNumber(149);
		}

		returnValue.setForwardMode(NetworkForwardMode.BRIDGE);
		return returnValue;
	}

	@Override
	public boolean hasUnsupportedValue() {
		if (super.hasUnsupportedValue()) {
			return true;
		}

		if (hasUnsupportedValue(radioMap.values())) {
			return true;
		}

		/*
		 * These are the "advanced" radio configuration. They can be redefined via the
		 * associated AP Profile or through tweaking the global defaults.
		 * 
		 */
		if (null != advancedRadioMap) {
			for (Entry<RadioType, RadioConfiguration> entry : advancedRadioMap.entrySet()) {
				if (RadioType.isUnsupported(entry.getKey()) || hasUnsupportedValue(entry.getValue())) {
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public boolean needsToBeUpdatedOnDevice(Object previousVersion) {
		if (previousVersion instanceof ApElementConfiguration) {
			ApElementConfiguration casted = (ApElementConfiguration) previousVersion;
			return !Objects.equals(this, casted);
		} else if (previousVersion instanceof Equipment) {
			ApElementConfiguration casted = (ApElementConfiguration) ((Equipment) previousVersion).getDetails();
			return !Objects.equals(this, casted);
		}
		return true;
	}

	public void setAdvancedRadioMap(Map<RadioType, RadioConfiguration> advancedRadioMap) {
		this.advancedRadioMap = advancedRadioMap;
	}

	public void setRadioMap(Map<RadioType, ElementRadioConfiguration> radioMap) {
		this.radioMap = radioMap;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(advancedRadioMap, radioMap);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof ApElementConfiguration)) {
			return false;
		}
		ApElementConfiguration other = (ApElementConfiguration) obj;
		return Objects.equals(advancedRadioMap, other.advancedRadioMap) && Objects.equals(radioMap, other.radioMap);
	}

	public static void main(String[] args) {
		System.out.println(createWithDefaults());
	}

}
