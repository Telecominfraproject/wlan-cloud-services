package com.telecominfraproject.wlan.equipment.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.core.model.equipment.RadioBestApSettings;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.equipment.SourceSelectionSteering;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.server.exceptions.ConfigurationException;


/**
 * @author dtoptygin
 *
 */
public class RadioConfiguration extends BaseJsonModel implements PushableConfiguration<RadioConfiguration> {
    /**
     * 
     */
    private static final long serialVersionUID = 8400985901848309466L;
    private static final Integer DEFAULT_DTIM_PERIOD = 2;
    private static final StateSetting DEFAULT_LEGACY_BSS_RATE = StateSetting.enabled;

    private RadioType radioType;
    private StateSetting radioAdminState;
    private Integer fragmentationThresholdBytes;
    private StateSetting wmmState;
    private StateSetting uapsdState;
    private StateSetting stationIsolation;
    private SourceSelectionMulticast multicastRate;
    private SourceSelectionManagement managementRate;
    private SourceSelectionSteering bestApSettings;
    private StateSetting legacyBSSRate;

    /**
     * Indicating that only every nth beacon includes a TIM, where n is the period.
     * Default value is 1. In low power mode, stations will only awake to listen for those beacons in order to then determine if they need stay awake for data frame receipt
     */
    private Integer dtimPeriod;

    // keep this in sync with fields in RadioCfg (in protobuf)

    /**
     * Detect for de-authentication attack. Default should be true.
     */
    private Boolean deauthAttackDetection;

    /**
     * Will return a default radio configuration
     * 
     * @param type
     * @return
     */
    public static RadioConfiguration createWithDefaults(RadioType type) {
        RadioConfiguration configuration = new RadioConfiguration();
        configuration.setRadioType(type);
        configuration.setBestApSettings(SourceSelectionSteering.createManualInstance(
        		RadioBestApSettings.createWithDefaults(type)));
        
        return configuration;
    }
    
    /*
     * Use static creator please.
     */
    protected RadioConfiguration() {
        /* The following come from UAP defaults */
        setFragmentationThresholdBytes(2346);
        setRadioAdminState(StateSetting.enabled);
        setStationIsolation(StateSetting.disabled);
        setUapsdState(StateSetting.enabled); // maps to "get radio wlan[0-1]
                                             // wme-apsd" on the AP
        setWmmState(StateSetting.enabled); // maps to "get radio wlan[0-1] wme"
                                           // on the AP
        setMulticastRate(SourceSelectionMulticast.createProfileInstance(
        		MulticastRate.auto));
        setManagementRate(SourceSelectionManagement.createProfileInstance(
        		ManagementRate.auto));
        setBestApSettings(SourceSelectionSteering.createProfileInstance(
        		RadioBestApSettings.createWithDefaults(RadioType.is5GHz)));
        setDtimPeriod(DEFAULT_DTIM_PERIOD);
        setLegacyBSSRate(DEFAULT_LEGACY_BSS_RATE);
    }

    @Override
    public RadioConfiguration clone() {
        return (RadioConfiguration) super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof RadioConfiguration)) {
            return false;
        }
        RadioConfiguration other = (RadioConfiguration) obj;
        
        return Objects.equals(dtimPeriod, other.dtimPeriod)
        		&& Objects.equals(multicastRate, other.multicastRate)
                && Objects.equals(bestApSettings, other.bestApSettings)
                && Objects.equals(deauthAttackDetection, other.deauthAttackDetection)
                && Objects.equals(fragmentationThresholdBytes, other.fragmentationThresholdBytes)
                && Objects.equals(managementRate, other.managementRate)
                && this.legacyBSSRate == other.legacyBSSRate
                && this.radioAdminState == other.radioAdminState
                && this.radioType == other.radioType
                && this.stationIsolation == other.stationIsolation && this.uapsdState == other.uapsdState
                && this.wmmState == other.wmmState;
    }
    
    /**
     * @return the dtimPeriod
     */
    public Integer getDtimPeriod() {
        return dtimPeriod;
    }

    public SourceSelectionSteering getBestApSettings() {
        return bestApSettings;
    }

    public Boolean getDeauthAttackDetection() {
        return deauthAttackDetection;
    }
    
    public Integer getFragmentationThresholdBytes() {
        return fragmentationThresholdBytes;
    }
    
    public StateSetting getLegacyBSSRate() {
        return legacyBSSRate;
    }

    public SourceSelectionMulticast getMulticastRate() {
    	return multicastRate;
    }
    
    public SourceSelectionManagement getManagementRate() {
        return managementRate;
    }

    public StateSetting getRadioAdminState() {
        return radioAdminState;
    }

    public RadioType getRadioType() {
        return radioType;
    }

    public StateSetting getStationIsolation() {
        return this.stationIsolation;
    }

    public StateSetting getUapsdState() {
        return uapsdState;
    }

    public StateSetting getWmmState() {
        return wmmState;
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        		dtimPeriod, bestApSettings, deauthAttackDetection, multicastRate,
                fragmentationThresholdBytes, legacyBSSRate, managementRate, 
                radioAdminState, radioType, stationIsolation, 
                uapsdState, wmmState);
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (RadioType.isUnsupported(radioType) 
        		|| StateSetting.isUnsupported(radioAdminState)
                || StateSetting.isUnsupported(wmmState)
                || StateSetting.isUnsupported(uapsdState) 
                || StateSetting.isUnsupported(stationIsolation)
                || SourceSelectionMulticast.hasUnsupportedValue(multicastRate)
                || SourceSelectionManagement.hasUnsupportedValue(managementRate)
                || SourceSelectionSteering.hasUnsupportedValue(bestApSettings)
                || StateSetting.isUnsupported(legacyBSSRate)
                ) {
            return true;
        }
        if (hasUnsupportedValue(bestApSettings)) {
            return true;
        }
        return false;
    }

    @Override
    /**
     * If we go from "previousVersion" to our current version, would that
     * require a push to the device?
     */
    public boolean needsToBeUpdatedOnDevice(RadioConfiguration previousVersion) {
        return !equals(previousVersion);
    }

    public void setDtimPeriod(Integer defaultDtimPeriod) {
        this.dtimPeriod = defaultDtimPeriod;        
    }
    
    public void setBestApSettings(SourceSelectionSteering bestApSettings) {
        this.bestApSettings = bestApSettings;
    }

    public void setDeauthAttackDetection(Boolean deauthAttackDetection) {
        this.deauthAttackDetection = deauthAttackDetection;
    }

    public void setFragmentationThresholdBytes(Integer fragmentationThresholdBytes) {
        this.fragmentationThresholdBytes = fragmentationThresholdBytes;
    }
    
    public void setLegacyBSSRate(StateSetting legacyBSSRate) {
        this.legacyBSSRate = legacyBSSRate;
    }
    
    public void setMulticastRate(SourceSelectionMulticast multicastRate) {
    	this.multicastRate = multicastRate;
    }

    public void setManagementRate(SourceSelectionManagement managementRate) {
        this.managementRate = managementRate;
    }

    public void setRadioAdminState(StateSetting radioAdminState) {
        this.radioAdminState = radioAdminState;
    }

    public void setRadioType(RadioType radioType) {
        this.radioType = radioType;
    }

    public void setStationIsolation(StateSetting state) {
        this.stationIsolation = state;
    }

    public void setUapsdState(StateSetting uapsdState) {
        this.uapsdState = uapsdState;
    }

    public void setWmmState(StateSetting wmmState) {
        this.wmmState = wmmState;
    }

}
