package com.telecominfraproject.wlan.equipment.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.ChannelHopSettings;
import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.core.model.equipment.RadioBestApSettings;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
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
    private static final Integer DEFAULT_BEACON_INTERVAL = 100;
    private static final StateSetting DEFAULT_LEGACY_BSS_RATE = StateSetting.enabled;


    private RadioType radioType;
    private StateSetting radioAdminState;
    private Integer fragmentationThresholdBytes;
    private Integer rtsCtsThreshold;
    private StateSetting autoChannelSelection;
    private RadioMode radioMode;
    private MimoMode mimoMode;
    private StateSetting wmmState;
    private StateSetting uapsdState;
    private Integer maxNumClients;
    private StateSetting stationIsolation;
    private MulticastRate multicastRate;
    private ManagementRate managementRate;
    private ActiveScanSettings activeScanSettings;
    private ChannelHopSettings channelHopSettings;
    private RadioBestApSettings bestApSettings;
    private StateSetting forceScanDuringVoice;
    private StateSetting legacyBSSRate;
    /**
     * Must be multiples of 100 tu. Should not exceed 500 tu. Default 100 tu.
     */
    private Integer beaconInterval;

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
        configuration.setBestApSettings(RadioBestApSettings.createWithDefaults(type));

        if (type == RadioType.is2dot4GHz) {
            configuration.setRadioMode(RadioMode.modeN);
        } else {
            configuration.setRadioMode(RadioMode.modeAC);
        }
        
        return configuration;
    }
    
    /*
     * Use static creator please.
     */
    protected RadioConfiguration() {
        /* The following come from UAP defaults */
        setAutoChannelSelection(StateSetting.disabled); // we don't want UAP's
                                                        // auto selection
        setFragmentationThresholdBytes(2346);
        setMaxNumClients(100);
        setMimoMode(MimoMode.twoByTwo);
        setMulticastRate(MulticastRate.auto);
        setRadioAdminState(StateSetting.enabled);
        setRtsCtsThreshold(65535);
        setStationIsolation(StateSetting.disabled);
        setUapsdState(StateSetting.enabled); // maps to "get radio wlan[0-1]
                                             // wme-apsd" on the AP
        setWmmState(StateSetting.enabled); // maps to "get radio wlan[0-1] wme"
                                           // on the AP
        setManagementRate(ManagementRate.auto);
        setActiveScanSettings(ActiveScanSettings.createWithDefaults());
        setChannelHopSettings(ChannelHopSettings.createWithDefaults());
        setBestApSettings(RadioBestApSettings.createWithDefaults(RadioType.is5GHz));
        setForceScanDuringVoice(StateSetting.disabled);
        setBeaconInterval(DEFAULT_BEACON_INTERVAL);
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
        return Objects.equals(activeScanSettings, other.activeScanSettings)
                && this.autoChannelSelection == other.autoChannelSelection
                && Objects.equals(beaconInterval, other.beaconInterval)
                && Objects.equals(bestApSettings, other.bestApSettings)
                && Objects.equals(channelHopSettings, other.channelHopSettings)
                && Objects.equals(deauthAttackDetection, other.deauthAttackDetection)
                && this.forceScanDuringVoice == other.forceScanDuringVoice
                && Objects.equals(fragmentationThresholdBytes, other.fragmentationThresholdBytes)
                && this.legacyBSSRate == other.legacyBSSRate && this.managementRate == other.managementRate
                && Objects.equals(maxNumClients, other.maxNumClients) && this.mimoMode == other.mimoMode
                && this.multicastRate == other.multicastRate && this.radioAdminState == other.radioAdminState
                && this.radioMode == other.radioMode && this.radioType == other.radioType
                && Objects.equals(rtsCtsThreshold, other.rtsCtsThreshold)
                && this.stationIsolation == other.stationIsolation && this.uapsdState == other.uapsdState
                && this.wmmState == other.wmmState;
    }

    public ActiveScanSettings getActiveScanSettings() {
        return activeScanSettings;
    }

    public StateSetting getAutoChannelSelection() {
        return autoChannelSelection;
    }

    /**
     * @return the beaconInterval
     */
    public Integer getBeaconInterval() {
        return beaconInterval;
    }

    public RadioBestApSettings getBestApSettings() {
        return bestApSettings;
    }

    public ChannelHopSettings getChannelHopSettings() {
        return channelHopSettings;
    }

    public Boolean getDeauthAttackDetection() {
        return deauthAttackDetection;
    }

    public StateSetting getForceScanDuringVoice() {
        return forceScanDuringVoice;
    }

    public Integer getFragmentationThresholdBytes() {
        return fragmentationThresholdBytes;
    }
    
    public StateSetting getLegacyBSSRate() {
        return legacyBSSRate;
    }

    public ManagementRate getManagementRate() {
        return managementRate;
    }

    public Integer getMaxNumClients() {
        return maxNumClients;
    }

    public MimoMode getMimoMode() {
        return mimoMode;
    }

    public MulticastRate getMulticastRate() {
        return this.multicastRate;
    }

    public StateSetting getRadioAdminState() {
        return radioAdminState;
    }

    public RadioMode getRadioMode() {
        return radioMode;
    }

    public RadioType getRadioType() {
        return radioType;
    }

    public Integer getRtsCtsThreshold() {
        return rtsCtsThreshold;
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
        return Objects.hash(activeScanSettings, autoChannelSelection, beaconInterval, bestApSettings,
                channelHopSettings, deauthAttackDetection, forceScanDuringVoice, fragmentationThresholdBytes,
                legacyBSSRate, managementRate, maxNumClients, mimoMode, multicastRate, radioAdminState, radioMode,
                radioType, rtsCtsThreshold, stationIsolation, uapsdState, wmmState);
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (RadioType.isUnsupported(radioType) || StateSetting.isUnsupported(radioAdminState)
                || StateSetting.isUnsupported(autoChannelSelection) || RadioMode.isUnsupported(radioMode)
                || MimoMode.isUnsupported(mimoMode) || StateSetting.isUnsupported(wmmState)
                || StateSetting.isUnsupported(uapsdState) || StateSetting.isUnsupported(stationIsolation)
                || MulticastRate.isUnsupported(multicastRate) || ManagementRate.isUnsupported(managementRate)
                || StateSetting.isUnsupported(forceScanDuringVoice) || StateSetting.isUnsupported(legacyBSSRate)) {
            return true;
        }
        if (hasUnsupportedValue(activeScanSettings) || hasUnsupportedValue(channelHopSettings)
                || hasUnsupportedValue(bestApSettings)) {
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

    public void setActiveScanSettings(ActiveScanSettings activeScanSettings) {
        this.activeScanSettings = activeScanSettings;
    }

    public void setAutoChannelSelection(StateSetting autoChannelSelection) {
        this.autoChannelSelection = autoChannelSelection;
    }

    /**
     * @param beaconInterval
     *            the beaconInterval to set
     */
    public void setBeaconInterval(Integer beaconInterval) {
        this.beaconInterval = beaconInterval;
    }

    public void setBestApSettings(RadioBestApSettings bestApSettings) {
        this.bestApSettings = bestApSettings;
    }

    public void setChannelHopSettings(ChannelHopSettings channelHopSettings) {
        this.channelHopSettings = channelHopSettings;
    }

    public void setDeauthAttackDetection(Boolean deauthAttackDetection) {
        this.deauthAttackDetection = deauthAttackDetection;
    }

    public void setForceScanDuringVoice(StateSetting forceScanDuringVoice) {
        this.forceScanDuringVoice = forceScanDuringVoice;
    }

    public void setFragmentationThresholdBytes(Integer fragmentationThresholdBytes) {
        this.fragmentationThresholdBytes = fragmentationThresholdBytes;
    }
    
    public void setLegacyBSSRate(StateSetting legacyBSSRate) {
        this.legacyBSSRate = legacyBSSRate;
    }

    public void setManagementRate(ManagementRate managementRate) {
        this.managementRate = managementRate;
    }

    public void setMaxNumClients(Integer maxNumClients) {
        this.maxNumClients = maxNumClients;
    }

    public void setMimoMode(MimoMode mimoMode) {
        this.mimoMode = mimoMode;
    }

    public void setMulticastRate(MulticastRate rate) {
        this.multicastRate = rate;
    }

    public void setRadioAdminState(StateSetting radioAdminState) {
        this.radioAdminState = radioAdminState;
    }

    public void setRadioMode(RadioMode radioMode) {
        this.radioMode = radioMode;
    }

    public void setRadioType(RadioType radioType) {
        this.radioType = radioType;
    }

    public void setRtsCtsThreshold(Integer rtsCtsThreshold) {
        this.rtsCtsThreshold = rtsCtsThreshold;
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

    /**
     * Ensures that there is no conflict in business logic due to misconfigured
     * values.
     */
    public void validate() {
        if (radioType == RadioType.is2dot4GHz) {
            if (radioMode == RadioMode.modeAC) {
                throw new ConfigurationException("Radio Configuration not valid: 2.4GHz radio can't be set to AC radio mode.");
            }
        } else {
            if (radioMode == RadioMode.modeGN) {
                throw new ConfigurationException("Radio Configuration not valid: 5GHz radio can't be set to GN radio mode.");
            }
        }
    }
}
