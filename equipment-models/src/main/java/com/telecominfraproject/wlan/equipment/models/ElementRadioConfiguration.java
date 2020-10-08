package com.telecominfraproject.wlan.equipment.models;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telecominfraproject.wlan.core.model.equipment.AutoOrManualValue;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author dtoptygin
 *
 */
public class ElementRadioConfiguration extends BaseJsonModel {

	private static final long serialVersionUID = -2441701182136504073L;

	/*
	 * These are weird since they are dependent on the radio type
	 */
	public final static int DEFAULT_RX_CELL_SIZE_DB = -90;
	public final static int DEFAULT_EIRP_TX_POWER = 18;

	private RadioType radioType;
	private Integer channelNumber; // The channel that was picked through the cloud's automatic assigment
	private Integer manualChannelNumber; // The channel that was manually entered
	private Integer backupChannelNumber; // Backup channel (this is never set by the customer: it's deducted from the
											// primary channel (either manual or auto)
	private List<BannedChannel> bannedChannels = new LinkedList<>();
	private List<Integer> allowedChannels = new LinkedList<>();
	private AutoOrManualValue rxCellSizeDb;
	private AutoOrManualValue probeResponseThresholdDb;
	private AutoOrManualValue clientDisconnectThresholdDb;
	private AutoOrManualValue eirpTxPower;
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
  			returnValue.setBackupChannelNumber(153);
		} else if (radioType == RadioType.is5GHzL) {
			returnValue.setChannelNumber(36);
			returnValue.setBackupChannelNumber(44);  
		} else if (radioType == RadioType.is5GHzU) {
			returnValue.setChannelNumber(149);
			returnValue.setBackupChannelNumber(154); 
		} else {
			returnValue.setChannelNumber(6);
			returnValue.setBackupChannelNumber(11);
		}

		return returnValue;
	}

	private ElementRadioConfiguration() {
		// Tx power default was discussed with Shaikh (set to 18)
		setEirpTxPower(AutoOrManualValue.createAutomaticInstance(DEFAULT_EIRP_TX_POWER));
		setRxCellSizeDb(AutoOrManualValue.createManualInstance(DEFAULT_RX_CELL_SIZE_DB));
		setProbeResponseThresholdDb(AutoOrManualValue.createManualInstance(-90));
		setClientDisconnectThresholdDb(AutoOrManualValue.createManualInstance(-90));
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

	@Override
	public ElementRadioConfiguration clone() {
		return (ElementRadioConfiguration) super.clone();
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
				&& Objects.equals(bannedChannels, other.bannedChannels) 
				&& Objects.equals(allowedChannels, other.allowedChannels) 
				&& this.bestAPSteerType == other.bestAPSteerType
				&& Objects.equals(channelNumber, other.channelNumber)
				&& Objects.equals(clientDisconnectThresholdDb, other.clientDisconnectThresholdDb)
				&& Objects.equals(deauthAttackDetection, other.deauthAttackDetection)
				&& Objects.equals(eirpTxPower, other.eirpTxPower)
				&& Objects.equals(getManualChannelNumber(), other.getManualChannelNumber())
				&& Objects.equals(perimeterDetectionEnabled, other.perimeterDetectionEnabled)
				&& Objects.equals(probeResponseThresholdDb, other.probeResponseThresholdDb)
				&& this.radioType == other.radioType && Objects.equals(rxCellSizeDb, other.rxCellSizeDb);
	}

	public Integer getActiveChannel(boolean autoChannelSelection) {
		return (autoChannelSelection) ? getChannelNumber() : getManualChannelNumber();
	}

	public Integer getBackupChannelNumber() {
		return backupChannelNumber;
	}

	public List<BannedChannel> getBannedChannels() {
		return bannedChannels;
	}

	public List<Integer> getAllowedChannels() {
		return allowedChannels;
	}
	
	public Integer getChannelNumber() {
		return channelNumber;
	}

	public AutoOrManualValue getClientDisconnectThresholdDb() {
		return clientDisconnectThresholdDb;
	}

	public Boolean getDeauthAttackDetection() {
		return this.deauthAttackDetection;
	}

	public AutoOrManualValue getEirpTxPower() {
		return eirpTxPower;
	}

	public Integer getManualChannelNumber() {
		return (this.manualChannelNumber == null) ? this.channelNumber : this.manualChannelNumber;
	}

	public AutoOrManualValue getProbeResponseThresholdDb() {
		return probeResponseThresholdDb;
	}

	public RadioType getRadioType() {
		return this.radioType;
	}

	public AutoOrManualValue getRxCellSizeDb() {
//        if(rxCellSizeDb != null && rxCellSizeDb.isAuto())
//        {
//            return AutoOrManualValue.createAutomaticInstance(Math.min(rxCellSizeDb.getValue(), getMinAutoCellSize()));
//        }
//
		return rxCellSizeDb;
	}

	@Override
	public int hashCode() {
		return Objects.hash(allowedChannelsPowerLevels, 
				backupChannelNumber, bannedChannels, allowedChannels,
				bestAPSteerType, channelNumber, clientDisconnectThresholdDb,
				deauthAttackDetection, eirpTxPower, getManualChannelNumber(),
				perimeterDetectionEnabled, probeResponseThresholdDb, radioType, rxCellSizeDb);
	}

	public void setBackupChannelNumber(Integer channelNumber) {
		this.backupChannelNumber = channelNumber;
	}

	public void setBannedChannels(List<BannedChannel> bannedChannels) {
		this.bannedChannels = bannedChannels;
	}

	public void setAllowedChannels(List<Integer> allowedChannels) {
		this.allowedChannels = allowedChannels;
	}
	
	public void setChannelNumber(Integer channelNumber) {
		this.channelNumber = channelNumber;
	}

	public void setClientDisconnectThresholdDb(AutoOrManualValue clientDisconnectThresholdDb) {
		this.clientDisconnectThresholdDb = clientDisconnectThresholdDb;
	}

	public void setDeauthAttackDetection(Boolean value) {
		this.deauthAttackDetection = value;
	}

	public void setEirpTxPower(AutoOrManualValue eirpTxPower) {
		this.eirpTxPower = eirpTxPower;
	}

	public void setManualChannelNumber(Integer channel) {
		this.manualChannelNumber = channel;
	}

	public void setProbeResponseThresholdDb(AutoOrManualValue probeResponseThresholdDb) {
		this.probeResponseThresholdDb = probeResponseThresholdDb;
	}

	public void setRadioType(RadioType radioType) {
		this.radioType = radioType;
	}

	public void setRxCellSizeDb(AutoOrManualValue rxCellSizeDb) {
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
		if (RadioType.isUnsupported(radioType) 
				|| hasUnsupportedValue(bannedChannels) 
				|| hasUnsupportedValue(rxCellSizeDb)
				|| hasUnsupportedValue(probeResponseThresholdDb) 
				|| hasUnsupportedValue(clientDisconnectThresholdDb)
				|| hasUnsupportedValue(eirpTxPower)
				|| BestAPSteerType.isUnsupported(bestAPSteerType)) {
			return true;
		}
		return false;
	}
}
