package com.telecominfraproject.wlan.profile.rf.models;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.ChannelBandwidth;
import com.telecominfraproject.wlan.core.model.equipment.ChannelHopSettings;
import com.telecominfraproject.wlan.core.model.equipment.RadioBestApSettings;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.equipment.models.ActiveScanSettings;
import com.telecominfraproject.wlan.equipment.models.ApElementConfiguration.ApModel;
import com.telecominfraproject.wlan.equipment.models.ManagementRate;
import com.telecominfraproject.wlan.equipment.models.MimoMode;
import com.telecominfraproject.wlan.equipment.models.MulticastRate;
import com.telecominfraproject.wlan.equipment.models.NeighbouringAPListConfiguration;
import com.telecominfraproject.wlan.equipment.models.RadioMode;
import com.telecominfraproject.wlan.equipment.models.StateSetting;
import com.telecominfraproject.wlan.server.exceptions.ConfigurationException;

public class RfElementConfiguration extends BaseJsonModel {

    private static final long serialVersionUID = 1071268246965238451L;
    public final static int DEFAULT_MIN_CELL_SIZE_DB = -65;
    public final static int DEFAULT_MAX_CELL_SIZE_DB = -90;

    private final static Map<RadioType, Integer> MIN_CELL_SIZE_MAP;
    static {
        Map<RadioType, Integer> map = new HashMap<>();
        map.put(RadioType.is2dot4GHz, DEFAULT_MIN_CELL_SIZE_DB);
        map.put(RadioType.is5GHz, DEFAULT_MIN_CELL_SIZE_DB);
        map.put(RadioType.is5GHzL, DEFAULT_MIN_CELL_SIZE_DB);
        map.put(RadioType.is5GHzU, DEFAULT_MIN_CELL_SIZE_DB);
        MIN_CELL_SIZE_MAP = Collections.unmodifiableMap(map);
    }
    private final static Map<RadioType, Integer> MAX_CELL_SIZE_MAP;
    static {
        Map<RadioType, Integer> maxMap = new HashMap<>();
        maxMap.put(RadioType.is2dot4GHz, DEFAULT_MAX_CELL_SIZE_DB);
        maxMap.put(RadioType.is5GHz, DEFAULT_MAX_CELL_SIZE_DB);
        maxMap.put(RadioType.is5GHzL, DEFAULT_MAX_CELL_SIZE_DB);
        maxMap.put(RadioType.is5GHzU, DEFAULT_MAX_CELL_SIZE_DB);
        MAX_CELL_SIZE_MAP = Collections.unmodifiableMap(maxMap);
    }
    public final static int DEFAULT_RX_CELL_SIZE_DB = -90;
    public final static int DEFAULT_EIRP_TX_POWER_DB = 18;
    public final static int DEFAULT_PROBE_RESPONSE_THRESHOLD_DB = -90;
    public final static int DEFAULT_CLIENT_DISCONNECT_THRESHOLD_DB = -90;
    public final static int MIN_EIRP_TX_POWER = 1;
    public final static int MAX_EIRP_TX_POWER = 32;
    public final static int DEFAULT_BEACON_INTERVAL = 100;

    private RadioType radioType;
    private RadioMode radioMode;
    private String rf;
    private Integer beaconInterval; // keep this in sync with fields in RadioCfg
                                    // (in protobuf)
    private StateSetting forceScanDuringVoice;
    private Integer rtsCtsThreshold;
    private ChannelBandwidth channelBandwidth;
    private MimoMode mimoMode;
    private Integer maxNumClients;
    private boolean autoChannelSelection;
    private boolean autoCellSizeSelection;
    private ActiveScanSettings activeScanSettings;
    private NeighbouringAPListConfiguration neighbouringListApConfig;
    private Integer minAutoCellSize;
    private Integer maxAutoCellSize;
    private Boolean perimeterDetectionEnabled;
    private ChannelHopSettings channelHopSettings;
    private Boolean bestApEnabled;

    private MulticastRate multicastRate;
    private ManagementRate managementRate;
    private Integer rxCellSizeDb;
    private Integer probeResponseThresholdDb;
    private Integer clientDisconnectThresholdDb;
    private Integer eirpTxPower;
    private RadioBestApSettings bestApSettings;

    private RfElementConfiguration() {
        long timestamp = System.currentTimeMillis();
        setRf("DefaultRf-" + timestamp);
        setBeaconInterval(DEFAULT_BEACON_INTERVAL);
        setForceScanDuringVoice(StateSetting.disabled);
        setRtsCtsThreshold(65535);
        setMimoMode(MimoMode.twoByTwo);
        setMaxNumClients(100);
        setMulticastRate(MulticastRate.auto);
        setAutoChannelSelection(false);
        setAutoCellSizeSelection(false);
        setActiveScanSettings(ActiveScanSettings.createWithDefaults());
        setManagementRate(ManagementRate.auto);
        setRxCellSizeDb(DEFAULT_RX_CELL_SIZE_DB);
        setProbeResponseThresholdDb(DEFAULT_PROBE_RESPONSE_THRESHOLD_DB);
        setClientDisconnectThresholdDb(DEFAULT_CLIENT_DISCONNECT_THRESHOLD_DB);
        setEirpTxPower(DEFAULT_EIRP_TX_POWER_DB);
        setBestApEnabled(null);
        setNeighbouringListApConfig(NeighbouringAPListConfiguration.createDefault());
        setPerimeterDetectionEnabled(true);
        setChannelHopSettings(ChannelHopSettings.createWithDefaults());
    }

    public static RfElementConfiguration createWithDefaults(RadioType radioType) {
        RfElementConfiguration ret = new RfElementConfiguration();
        ret.setRadioType(radioType);
        ret.setBestApSettings(RadioBestApSettings.createWithDefaults(radioType));
        ret.setMinAutoCellSize(MIN_CELL_SIZE_MAP.get(radioType));
        ret.setMaxAutoCellSize(MAX_CELL_SIZE_MAP.get(radioType));
        if (radioType == RadioType.is5GHz || radioType == RadioType.is5GHzL || radioType == RadioType.is5GHzU) {
            ret.setChannelBandwidth(ChannelBandwidth.is80MHz);
            ret.setRadioMode(RadioMode.modeAC);
        } else {
            ret.setChannelBandwidth(ChannelBandwidth.is20MHz);
            ret.setRadioMode(RadioMode.modeN);
        }
        return ret;
    }

    public static RfElementConfiguration createWithDefaults(RadioType radioType, ApModel apModel) {
        RfElementConfiguration ret = createWithDefaults(radioType);
        if (apModel == ApModel.OUTDOOR) {
            // TODO: This logic is carried over from ApElementConfig during RF
            // profile implementation. Is it still necessary?
            // NAAS-8919 change mimo for outdoor to 3x3
            ret.setMimoMode(MimoMode.threeByThree);
        }
        return ret;
    }

    public RadioType getRadioType() {
        return radioType;
    }

    public void setRadioType(RadioType radioType) {
        this.radioType = radioType;
    }

    public RadioMode getRadioMode() {
        return radioMode;
    }

    public void setRadioMode(RadioMode radioMode) {
        this.radioMode = radioMode;
    }

    public String getRf() {
        return rf;
    }

    public void setRf(String rf) {
        this.rf = rf;
    }

    public Integer getBeaconInterval() {
        return beaconInterval;
    }

    public void setBeaconInterval(Integer beaconInterval) {
        this.beaconInterval = beaconInterval;
    }

    public StateSetting getForceScanDuringVoice() {
        return forceScanDuringVoice;
    }

    public void setForceScanDuringVoice(StateSetting forceScanDuringVoice) {
        this.forceScanDuringVoice = forceScanDuringVoice;
    }

    public Integer getRtsCtsThreshold() {
        return rtsCtsThreshold;
    }

    public void setRtsCtsThreshold(Integer rtsCtsThreshold) {
        this.rtsCtsThreshold = rtsCtsThreshold;
    }

    public ChannelBandwidth getChannelBandwidth() {
        return channelBandwidth;
    }

    public void setChannelBandwidth(ChannelBandwidth channelBandwidth) {
        this.channelBandwidth = channelBandwidth;
    }

    public MimoMode getMimoMode() {
        return mimoMode;
    }

    public void setMimoMode(MimoMode mimoMode) {
        this.mimoMode = mimoMode;
    }

    public Integer getMaxNumClients() {
        return maxNumClients;
    }

    public void setMaxNumClients(Integer maxNumClients) {
        this.maxNumClients = maxNumClients;
    }

    public MulticastRate getMulticastRate() {
        return multicastRate;
    }

    public void setMulticastRate(MulticastRate multicastRate) {
        this.multicastRate = multicastRate;
    }

    public boolean getAutoChannelSelection() {
        return autoChannelSelection;
    }

    public void setAutoChannelSelection(boolean autoChannelSelection) {
        this.autoChannelSelection = autoChannelSelection;
    }

    public boolean getAutoCellSizeSelection() {
        return autoCellSizeSelection;
    }

    public void setAutoCellSizeSelection(boolean autoCellSizeSelection) {
        this.autoCellSizeSelection = autoCellSizeSelection;
    }

    public ActiveScanSettings getActiveScanSettings() {
        return activeScanSettings;
    }

    public void setActiveScanSettings(ActiveScanSettings activeScanSettings) {
        this.activeScanSettings = activeScanSettings;
    }

    public ManagementRate getManagementRate() {
        return managementRate;
    }

    public void setManagementRate(ManagementRate managementRate) {
        this.managementRate = managementRate;
    }

    public Integer getRxCellSizeDb() {
        return rxCellSizeDb;
    }

    public void setRxCellSizeDb(Integer rxCellSizeDb) {
        this.rxCellSizeDb = rxCellSizeDb;
    }

    public Integer getProbeResponseThresholdDb() {
        return probeResponseThresholdDb;
    }

    public void setProbeResponseThresholdDb(Integer probeResponseThresholdDb) {
        this.probeResponseThresholdDb = probeResponseThresholdDb;
    }

    public Integer getClientDisconnectThresholdDb() {
        return clientDisconnectThresholdDb;
    }

    public void setClientDisconnectThresholdDb(Integer clientDisconnectThresholdDb) {
        this.clientDisconnectThresholdDb = clientDisconnectThresholdDb;
    }

    public Integer getEirpTxPower() {
        return eirpTxPower;
    }

    public void setEirpTxPower(Integer eirpTxPower) {
        if (eirpTxPower > MAX_EIRP_TX_POWER) {
            this.eirpTxPower = MAX_EIRP_TX_POWER;
        } else if (eirpTxPower < MIN_EIRP_TX_POWER) {
            this.eirpTxPower = MAX_EIRP_TX_POWER;
        } else {
            this.eirpTxPower = eirpTxPower;
        }
    }

    public Boolean getBestApEnabled() {
        return bestApEnabled;
    }

    public void setBestApEnabled(Boolean bestApEnabled) {
        this.bestApEnabled = bestApEnabled;
    }

    public NeighbouringAPListConfiguration getNeighbouringListApConfig() {
        return neighbouringListApConfig;
    }

    public void setNeighbouringListApConfig(NeighbouringAPListConfiguration neighbouringListApConfig) {
        this.neighbouringListApConfig = neighbouringListApConfig;
    }

    public Integer getMinAutoCellSize() {
        if (minAutoCellSize == null) {
            if (MIN_CELL_SIZE_MAP.containsKey(this.radioType)) {
                return MIN_CELL_SIZE_MAP.get(this.radioType);
            } else {
                return MIN_CELL_SIZE_MAP.get(RadioType.is2dot4GHz);
            }
        }

        return minAutoCellSize;
    }

    public void setMinAutoCellSize(Integer minAutoCellSize) {
        this.minAutoCellSize = minAutoCellSize;
    }

    public Integer getMaxAutoCellSize() {
        return maxAutoCellSize;
    }

    public void setMaxAutoCellSize(Integer maxAutoCellSize) {
        this.maxAutoCellSize = maxAutoCellSize;
    }

    public Boolean getPerimeterDetectionEnabled() {
        return perimeterDetectionEnabled;
    }

    public void setPerimeterDetectionEnabled(Boolean perimeterDetectionEnabled) {
        this.perimeterDetectionEnabled = perimeterDetectionEnabled;
    }

    public ChannelHopSettings getChannelHopSettings() {
        return channelHopSettings;
    }

    public void setChannelHopSettings(ChannelHopSettings channelHopSettings) {
        this.channelHopSettings = channelHopSettings;
    }

    public RadioBestApSettings getBestApSettings() {
        return bestApSettings;
    }

    public void setBestApSettings(RadioBestApSettings bestApSettings) {
        this.bestApSettings = bestApSettings;
    }

    @Override
    public RfElementConfiguration clone() {
        return (RfElementConfiguration) super.clone();
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (StateSetting.isUnsupported(forceScanDuringVoice) || RadioMode.isUnsupported(radioMode)
                || RadioType.isUnsupported(radioType) || ChannelBandwidth.isUnsupported(channelBandwidth)
                || MimoMode.isUnsupported(mimoMode) || MulticastRate.isUnsupported(multicastRate)
                || ActiveScanSettings.hasUnsupportedValue(activeScanSettings)
                || ManagementRate.isUnsupported(managementRate)
                || NeighbouringAPListConfiguration.hasUnsupportedValue(neighbouringListApConfig)
                || ChannelHopSettings.hasUnsupportedValue(channelHopSettings)
                || RadioBestApSettings.hasUnsupportedValue(bestApSettings)) {
            return true;
        }
        return false;
    }

    /**
     * Ensures that there is no conflict in business logic due to misconfigured
     * values.
     */
    public void validate() {
        if (radioType == RadioType.is2dot4GHz) {
            if (radioMode == RadioMode.modeAC) {
                throw new ConfigurationException(
                        "Radio Configuration not valid: 2.4GHz radio can't be set to AC radio mode.");
            }
        } else {
            if (radioMode == RadioMode.modeGN) {
                throw new ConfigurationException(
                        "Radio Configuration not valid: 5GHz radio can't be set to GN radio mode.");
            }
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(activeScanSettings, autoChannelSelection, beaconInterval, bestApEnabled, bestApSettings,
                channelBandwidth, channelHopSettings, clientDisconnectThresholdDb, eirpTxPower, forceScanDuringVoice,
                managementRate, maxNumClients, mimoMode, minAutoCellSize, multicastRate, neighbouringListApConfig,
                perimeterDetectionEnabled, probeResponseThresholdDb, radioMode, radioType, rf, rtsCtsThreshold,
                rxCellSizeDb, autoCellSizeSelection, maxAutoCellSize);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RfElementConfiguration other = (RfElementConfiguration) obj;
        return Objects.equals(activeScanSettings, other.activeScanSettings)
                && autoChannelSelection == other.autoChannelSelection
                && autoCellSizeSelection == other.autoCellSizeSelection
                && Objects.equals(beaconInterval, other.beaconInterval)
                && Objects.equals(bestApEnabled, other.bestApEnabled)
                && Objects.equals(bestApSettings, other.bestApSettings) && channelBandwidth == other.channelBandwidth
                && Objects.equals(channelHopSettings, other.channelHopSettings)
                && Objects.equals(clientDisconnectThresholdDb, other.clientDisconnectThresholdDb)
                && Objects.equals(eirpTxPower, other.eirpTxPower) && forceScanDuringVoice == other.forceScanDuringVoice
                && managementRate == other.managementRate && Objects.equals(maxNumClients, other.maxNumClients)
                && mimoMode == other.mimoMode && Objects.equals(minAutoCellSize, other.minAutoCellSize)
                && Objects.equals(maxAutoCellSize, other.maxAutoCellSize)
                && multicastRate == other.multicastRate
                && Objects.equals(neighbouringListApConfig, other.neighbouringListApConfig)
                && Objects.equals(perimeterDetectionEnabled, other.perimeterDetectionEnabled)
                && Objects.equals(probeResponseThresholdDb, other.probeResponseThresholdDb)
                && radioMode == other.radioMode && radioType == other.radioType && Objects.equals(rf, other.rf)
                && Objects.equals(rtsCtsThreshold, other.rtsCtsThreshold)
                && Objects.equals(rxCellSizeDb, other.rxCellSizeDb);
    }

}
