package com.telecominfraproject.wlan.equipment.models.bulkupdate.rrm;

import com.telecominfraproject.wlan.core.model.equipment.AutoOrManualValue;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author dtop
 *
 */
public class RrmBulkUpdateApDetails extends BaseJsonModel {

	private static final long serialVersionUID = 291599335843057919L;

	/**
	 * mapped to equipment->details->radioMap->channelNumber
	 */
	private int channelNumber; 
	
	/**
	 * mapped to equipment->details->radioMap->rxCellSizeDb->value
	 */
	private AutoOrManualValue rxCellSizeDb;

	/**
	 * mapped to equipment->details->radioMap->probeResponseThresholdDb->value
	 */
	private AutoOrManualValue probeResponseThresholdDb;
	
	
	/**
	 * mapped to equipment->details->radioMap->clientDisconnectThresholdDb->value
	 */
	private AutoOrManualValue clientDisconnectThresholdDb;
	
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
	public AutoOrManualValue getRxCellSizeDb() {
		return rxCellSizeDb;
	}
	public void setRxCellSizeDb(AutoOrManualValue rxCellSizeDb) {
		this.rxCellSizeDb = rxCellSizeDb;
	}
	public AutoOrManualValue getProbeResponseThresholdDb() {
		return probeResponseThresholdDb;
	}
	public void setProbeResponseThresholdDb(AutoOrManualValue probeResponseThresholdDb) {
		this.probeResponseThresholdDb = probeResponseThresholdDb;
	}
	public AutoOrManualValue getClientDisconnectThresholdDb() {
		return clientDisconnectThresholdDb;
	}
	public void setClientDisconnectThresholdDb(AutoOrManualValue clientDisconnectThresholdDb) {
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
	
			
}
