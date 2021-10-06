package com.telecominfraproject.wlan.equipment.models;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.equipment.SourceSelectionValue;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author dtoptygin
 *
 */
public class ElementRadioConfiguration extends BaseJsonModel
        implements PushableConfiguration<ElementRadioConfiguration> {

    private static final long serialVersionUID = -2441701182136504073L;

    /*
     * These are weird since they are dependent on the radio type
     */
    public final static int DEFAULT_RX_CELL_SIZE_DB = -90;
    public final static int DEFAULT_EIRP_TX_POWER_DB = 18;
    public final static int DEFAULT_PROBE_RESPONSE_THRESHOLD_DB = -90;
    public final static int DEFAULT_CLIENT_DISCONNECT_THRESHOLD_DB = -90;

    private RadioType radioType;
    private Integer channelNumber; // The channel that was picked through the
                                   // cloud's automatic assignment
    private Integer manualChannelNumber; // The channel that was manually
                                         // entered
    private Integer backupChannelNumber; // The backup channel that was picked
                                         // through the cloud's automatic
                                         // assignment
    private Integer manualBackupChannelNumber; // The backup channel that was
                                               // manually entered

    private SourceSelectionValue rxCellSizeDb;
    private SourceSelectionValue probeResponseThresholdDb;
    private SourceSelectionValue clientDisconnectThresholdDb;
    private SourceSelectionValue eirpTxPower;
    private Boolean perimeterDetectionEnabled;
    // Initialize here to cover backward compatibility.
    private BestAPSteerType bestAPSteerType = BestAPSteerType.both;

    private Boolean deauthAttackDetection;
    private Set<ChannelPowerLevel> allowedChannelsPowerLevels = new HashSet<>();

    /**
     * Static creator
     *
     * @param radioType
     * @return
     */
    public static ElementRadioConfiguration createWithDefaults(RadioType radioType) {
        ElementRadioConfiguration returnValue = new ElementRadioConfiguration();

        returnValue.setRadioType(radioType);

        if (radioType == RadioType.is5GHz) {
            returnValue.setChannelNumber(36);
            returnValue.setBackupChannelNumber(44);
        } else if (radioType == RadioType.is5GHzL) {
            returnValue.setChannelNumber(36);
            returnValue.setBackupChannelNumber(44);
        } else if (radioType == RadioType.is5GHzU) {
            returnValue.setChannelNumber(149);
            returnValue.setBackupChannelNumber(157);
        } else {
            returnValue.setChannelNumber(6);
            returnValue.setBackupChannelNumber(11);
        }

        return returnValue;
    }

    private ElementRadioConfiguration() {
        // Tx power default was discussed with Shaikh (set to 18)
        setEirpTxPower(SourceSelectionValue.createProfileInstance(DEFAULT_EIRP_TX_POWER_DB));
        setRxCellSizeDb(SourceSelectionValue.createProfileInstance(DEFAULT_RX_CELL_SIZE_DB));
        setProbeResponseThresholdDb(SourceSelectionValue.createProfileInstance(DEFAULT_PROBE_RESPONSE_THRESHOLD_DB));
        setClientDisconnectThresholdDb(SourceSelectionValue.createProfileInstance(DEFAULT_CLIENT_DISCONNECT_THRESHOLD_DB));
        setPerimeterDetectionEnabled(true);
        setBestAPSteerType(BestAPSteerType.both);
    }

    @JsonIgnore
    public void alterActiveChannel(Integer channelNumber, boolean autoChannelSelection) {
        if (autoChannelSelection) {
            setChannelNumber(channelNumber);
        } else {
            setManualChannelNumber(channelNumber);
        }
    }

    @JsonIgnore
    public void alterActiveBackupChannel(Integer channelNumber, boolean autoChannelSelection) {
        if (autoChannelSelection) {
            setBackupChannelNumber(channelNumber);
        } else {
            setManualBackupChannelNumber(channelNumber);
        }
    }

    @Override
    public ElementRadioConfiguration clone() {
        return (ElementRadioConfiguration) super.clone();
    }

    public Integer getActiveChannel(boolean autoChannelSelection) {
        return (autoChannelSelection) ? getChannelNumber() : getManualChannelNumber();
    }

    public Integer getActiveBackupChannel(boolean autoChannelSelection) {
        return (autoChannelSelection) ? getBackupChannelNumber() : getManualBackupChannelNumber();
    }

    public Integer getBackupChannelNumber() {
        return backupChannelNumber;
    }

    public Integer getChannelNumber() {
        return channelNumber;
    }

    public SourceSelectionValue getClientDisconnectThresholdDb() {
        return clientDisconnectThresholdDb;
    }

    public Boolean getDeauthAttackDetection() {
        return this.deauthAttackDetection;
    }

    public SourceSelectionValue getEirpTxPower() {
        return eirpTxPower;
    }

    public Integer getManualChannelNumber() {
        return (this.manualChannelNumber == null) ? this.channelNumber : this.manualChannelNumber;
    }

    public Integer getManualBackupChannelNumber() {
        return (this.manualBackupChannelNumber == null) ? this.backupChannelNumber : this.manualBackupChannelNumber;
    }

    public SourceSelectionValue getProbeResponseThresholdDb() {
        return probeResponseThresholdDb;
    }

    public RadioType getRadioType() {
        return this.radioType;
    }

    public SourceSelectionValue getRxCellSizeDb() {
        return rxCellSizeDb;
    }

    public void setBackupChannelNumber(Integer channelNumber) {
        this.backupChannelNumber = channelNumber;
    }

    public void setChannelNumber(Integer channelNumber) {
        this.channelNumber = channelNumber;
    }

    public void setClientDisconnectThresholdDb(SourceSelectionValue clientDisconnectThresholdDb) {
        this.clientDisconnectThresholdDb = clientDisconnectThresholdDb;
    }

    public void setDeauthAttackDetection(Boolean value) {
        this.deauthAttackDetection = value;
    }

    public void setEirpTxPower(SourceSelectionValue eirpTxPower) {
        this.eirpTxPower = eirpTxPower;
    }

    public void setManualChannelNumber(Integer channel) {
        this.manualChannelNumber = channel;
    }

    public void setManualBackupChannelNumber(Integer channel) {
        this.manualBackupChannelNumber = channel;
    }

    public void setProbeResponseThresholdDb(SourceSelectionValue probeResponseThresholdDb) {
        this.probeResponseThresholdDb = probeResponseThresholdDb;
    }

    public void setRadioType(RadioType radioType) {
        this.radioType = radioType;
    }

    public void setRxCellSizeDb(SourceSelectionValue rxCellSizeDb) {
        this.rxCellSizeDb = rxCellSizeDb;
    }

    public Boolean getPerimeterDetectionEnabled() {
        return perimeterDetectionEnabled;
    }

    public void setPerimeterDetectionEnabled(Boolean perimeterDetectionEnabled) {
        this.perimeterDetectionEnabled = perimeterDetectionEnabled;
    }

    public BestAPSteerType getBestAPSteerType() {
        return bestAPSteerType;
    }

    public void setBestAPSteerType(BestAPSteerType bestAPSteerType) {
        this.bestAPSteerType = bestAPSteerType;
    }

    public Set<ChannelPowerLevel> getAllowedChannelsPowerLevels() {
        return allowedChannelsPowerLevels;
    }

    public void setAllowedChannelsPowerLevels(Set<ChannelPowerLevel> allowedChannelsPowerLevels) {
        this.allowedChannelsPowerLevels = allowedChannelsPowerLevels;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (RadioType.isUnsupported(radioType) || SourceSelectionValue.hasUnsupportedValue(clientDisconnectThresholdDb)
                || SourceSelectionValue.hasUnsupportedValue(eirpTxPower)
                || SourceSelectionValue.hasUnsupportedValue(probeResponseThresholdDb)
                || SourceSelectionValue.hasUnsupportedValue(rxCellSizeDb)
                || BestAPSteerType.isUnsupported(bestAPSteerType)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(allowedChannelsPowerLevels, backupChannelNumber, bestAPSteerType, channelNumber,
                clientDisconnectThresholdDb, deauthAttackDetection, eirpTxPower, getManualChannelNumber(),
                getManualBackupChannelNumber(), perimeterDetectionEnabled, probeResponseThresholdDb, radioType,
                rxCellSizeDb);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ElementRadioConfiguration)) {
            return false;
        }
        ElementRadioConfiguration other = (ElementRadioConfiguration) obj;
        return Objects.equals(allowedChannelsPowerLevels, other.allowedChannelsPowerLevels)
                && Objects.equals(backupChannelNumber, other.backupChannelNumber)
                && this.bestAPSteerType == other.bestAPSteerType && Objects.equals(channelNumber, other.channelNumber)
                && Objects.equals(clientDisconnectThresholdDb, other.clientDisconnectThresholdDb)
                && Objects.equals(deauthAttackDetection, other.deauthAttackDetection)
                && Objects.equals(eirpTxPower, other.eirpTxPower)
                && Objects.equals(getManualChannelNumber(), other.getManualChannelNumber())
                && Objects.equals(getManualBackupChannelNumber(), other.getManualBackupChannelNumber())
                && Objects.equals(perimeterDetectionEnabled, other.perimeterDetectionEnabled)
                && Objects.equals(probeResponseThresholdDb, other.probeResponseThresholdDb)
                && this.radioType == other.radioType && Objects.equals(rxCellSizeDb, other.rxCellSizeDb);
    }

    @Override
    /**
     * If we go from "previousVersion" to our current version, would that
     * require a push to the device?
     */
    public boolean needsToBeUpdatedOnDevice(ElementRadioConfiguration previousVersion) {

        return !(Objects.equals(backupChannelNumber, previousVersion.backupChannelNumber)
                && this.bestAPSteerType == previousVersion.bestAPSteerType
                && Objects.equals(channelNumber, previousVersion.channelNumber)
                && Objects.equals(clientDisconnectThresholdDb, previousVersion.clientDisconnectThresholdDb)
                && Objects.equals(deauthAttackDetection, previousVersion.deauthAttackDetection)
                && Objects.equals(eirpTxPower, previousVersion.eirpTxPower)
                && Objects.equals(getManualChannelNumber(), previousVersion.getManualChannelNumber())
                && Objects.equals(getManualBackupChannelNumber(), previousVersion.getManualBackupChannelNumber())
                && Objects.equals(perimeterDetectionEnabled, previousVersion.perimeterDetectionEnabled)
                && Objects.equals(probeResponseThresholdDb, previousVersion.probeResponseThresholdDb)
                && this.radioType == previousVersion.radioType
                && Objects.equals(rxCellSizeDb, previousVersion.rxCellSizeDb));
    }

}
