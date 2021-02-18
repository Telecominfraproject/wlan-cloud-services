package com.telecominfraproject.wlan.equipment.models.bulkupdate.rrm;

import com.telecominfraproject.wlan.core.model.equipment.SourceSelectionValue;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author dtop
 *
 */
public class RrmBulkUpdateApDetails extends BaseJsonModel {

	private static final long serialVersionUID = -3802390238799127555L;

	/**
	 * mapped to equipment->details->radioMap->manualChannelNumber)
	 */
	private int channelNumber;
	
	/**
	 * mapped to equipment->details->radioMap->manualBackupChannelNumber
	 */
	private int backupChannelNumber;
	
	/**
	 * mapped to equipment->details->radioMap->rxCellSizeDb->value
	 */
	private SourceSelectionValue rxCellSizeDb;

	/**
	 * mapped to equipment->details->radioMap->probeResponseThresholdDb->value
	 */
	private SourceSelectionValue probeResponseThresholdDb;
	
	
	/**
	 * mapped to equipment->details->radioMap->clientDisconnectThresholdDb->value
	 */
	private SourceSelectionValue clientDisconnectThresholdDb;
	
	/**
	 * mapped to equipment->details->radioMap->bestApSettings->dropInSnrPercentage
	 */
	private int dropInSnrPercentage;
	
	
	/**
	 * mapped to equipment->details->radioMap->bestApSettings->minLoadFactor
	 */
	private int minLoadFactor;
	
	public int getChannelNumber() {
		return channelNumber;
	}
	public void setChannelNumber(int channelNumber) {
		this.channelNumber = channelNumber;
	}
	public int getBackupChannelNumber() {
		return backupChannelNumber;
	}
	public void setBackupChannelNumber(int backupChannelNumber) {
		this.backupChannelNumber = backupChannelNumber;
	}
	public SourceSelectionValue getRxCellSizeDb() {
		return rxCellSizeDb;
	}
	public void setRxCellSizeDb(SourceSelectionValue rxCellSizeDb) {
		this.rxCellSizeDb = rxCellSizeDb;
	}
	public SourceSelectionValue getProbeResponseThresholdDb() {
		return probeResponseThresholdDb;
	}
	public void setProbeResponseThresholdDb(SourceSelectionValue probeResponseThresholdDb) {
		this.probeResponseThresholdDb = probeResponseThresholdDb;
	}
	public SourceSelectionValue getClientDisconnectThresholdDb() {
		return clientDisconnectThresholdDb;
	}
	public void setClientDisconnectThresholdDb(SourceSelectionValue clientDisconnectThresholdDb) {
		this.clientDisconnectThresholdDb = clientDisconnectThresholdDb;
	}
	public int getDropInSnrPercentage() {
		return dropInSnrPercentage;
	}
	public void setDropInSnrPercentage(int dropInSnrPercentage) {
		this.dropInSnrPercentage = dropInSnrPercentage;
	}
	public int getMinLoadFactor() {
		return minLoadFactor;
	}
	public void setMinLoadFactor(int minLoadFactor) {
		this.minLoadFactor = minLoadFactor;
	}
	
	@Override
	public boolean hasUnsupportedValue() {
		if (SourceSelectionValue.hasUnsupportedValue(clientDisconnectThresholdDb)
				|| SourceSelectionValue.hasUnsupportedValue(probeResponseThresholdDb)
				|| SourceSelectionValue.hasUnsupportedValue(rxCellSizeDb)) {
			return true;
		}
		return false;
	}
			
}
