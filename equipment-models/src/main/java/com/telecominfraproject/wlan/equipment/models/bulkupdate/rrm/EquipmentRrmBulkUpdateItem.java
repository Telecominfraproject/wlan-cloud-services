package com.telecominfraproject.wlan.equipment.models.bulkupdate.rrm;

import java.util.EnumMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
import com.telecominfraproject.wlan.core.model.equipment.RadioBestApSettings;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.equipment.models.ApElementConfiguration;
import com.telecominfraproject.wlan.equipment.models.ElementRadioConfiguration;
import com.telecominfraproject.wlan.equipment.models.Equipment;
import com.telecominfraproject.wlan.equipment.models.RadioConfiguration;

public class EquipmentRrmBulkUpdateItem extends BaseJsonModel {

    private static final Logger LOG = LoggerFactory.getLogger(EquipmentRrmBulkUpdateItem.class);

	private static final long serialVersionUID = 8162026873588293904L;

	private long equipmentId;
	
	private Map<RadioType, RrmBulkUpdateApDetails> perRadioDetails = new EnumMap<>(RadioType.class);

	public long getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(long equipmentId) {
		this.equipmentId = equipmentId;
	}

	public Map<RadioType, RrmBulkUpdateApDetails> getPerRadioDetails() {
		return perRadioDetails;
	}

	public void setPerRadioDetails(Map<RadioType, RrmBulkUpdateApDetails> perRadioDetails) {
		this.perRadioDetails = perRadioDetails;
	}
	
	public EquipmentRrmBulkUpdateItem() {
		// for serialization
	}

	public EquipmentRrmBulkUpdateItem(Equipment equipment) {
		if(equipment == null 
				|| (equipment.getEquipmentType()!= EquipmentType.AP) 
				) {
			LOG.error("Cannot build EquipmentRrmBulkUpdateItem from equipment {} ", equipment);
			throw new IllegalArgumentException("Cannot build EquipmentRrmBulkUpdateItem from supplied equipment");
		}
		
		ApElementConfiguration details = null;
		if (equipment.getDetails() == null) {
			details = ApElementConfiguration.createWithDefaults();
		} else if (equipment.getDetails() != null && equipment.getDetails() instanceof ApElementConfiguration) {
			details = (ApElementConfiguration) equipment.getDetails();
		} else {
			LOG.error("Cannot build EquipmentRrmBulkUpdateItem from equipment details that is not ApElementConfiguration {} ", equipment);
			throw new IllegalArgumentException("Cannot build EquipmentRrmBulkUpdateItem from supplied equipment: ApElementConfig type error");
		}
		
		details.getRadioMap().forEach((rt, radioCfg) -> {
			RrmBulkUpdateApDetails itemApDetails = new RrmBulkUpdateApDetails();
			perRadioDetails.put(rt, itemApDetails);
			itemApDetails.setChannelNumber(radioCfg.getChannelNumber());
			itemApDetails.setClientDisconnectThresholdDb(radioCfg.getClientDisconnectThresholdDb());
			itemApDetails.setProbeResponseThresholdDb(radioCfg.getProbeResponseThresholdDb());
			itemApDetails.setRxCellSizeDb(radioCfg.getRxCellSizeDb());
		});

		details.getAdvancedRadioMap().forEach((rt, radioCfg) -> {
			RrmBulkUpdateApDetails itemApDetails = perRadioDetails.get(rt);
			if(itemApDetails != null) {
				itemApDetails.setDropInSnrPercentage(radioCfg.getBestApSettings().getValue().getDropInSnrPercentage());
				itemApDetails.setMinLoadFactor(radioCfg.getBestApSettings().getValue().getMinLoadFactor());
			}
		});
	}

	/**
	 * Attempt to apply requested bulk changes to the supplied equipment object
	 * @param equipment
	 * @return true if the supplied equipment model was changed by this method
	 */
	public boolean applyToEquipment(Equipment equipment) {
		if(equipment == null 
				|| (equipment.getEquipmentType()!= EquipmentType.AP) 
				||	!(equipment.getDetails()!=null && equipment.getDetails() instanceof ApElementConfiguration) 
				|| perRadioDetails==null || perRadioDetails.isEmpty()) {
			return false;
		}

		AtomicBoolean modelChanged = new AtomicBoolean(false);

		ApElementConfiguration details = (ApElementConfiguration) equipment.getDetails();
		if(equipment.getDetails()==null) {
			details = ApElementConfiguration.createWithDefaults();
			equipment.setDetails(details);
		}
		
		//needed for the lambda below to work
		ApElementConfiguration finalDetails = details;
		
		perRadioDetails.forEach((rt, updateDetails) -> {
			ElementRadioConfiguration erc = finalDetails.getRadioMap().get(rt);
			if(erc == null) {
				erc = ElementRadioConfiguration.createWithDefaults(rt);
				finalDetails.getRadioMap().put(rt, erc);
			}
			
			if(erc.getChannelNumber()== null || erc.getChannelNumber().intValue() != updateDetails.getChannelNumber()) {
				erc.setChannelNumber(updateDetails.getChannelNumber());
				modelChanged.set(true);
			}
			
			if ((erc.getClientDisconnectThresholdDb() == null && updateDetails.getClientDisconnectThresholdDb() != null)
					|| !erc.getClientDisconnectThresholdDb().equals(updateDetails.getClientDisconnectThresholdDb())) {
				erc.setClientDisconnectThresholdDb(updateDetails.getClientDisconnectThresholdDb());
				modelChanged.set(true);
			}

			if ((erc.getProbeResponseThresholdDb() == null && updateDetails.getProbeResponseThresholdDb() != null)
					|| !erc.getProbeResponseThresholdDb().equals(updateDetails.getProbeResponseThresholdDb())) {
				erc.setProbeResponseThresholdDb(updateDetails.getProbeResponseThresholdDb());
				modelChanged.set(true);
			}

			if ((erc.getRxCellSizeDb() == null && updateDetails.getRxCellSizeDb() != null)
					|| !erc.getRxCellSizeDb().equals(updateDetails.getRxCellSizeDb())) {
				erc.setRxCellSizeDb(updateDetails.getRxCellSizeDb());
				modelChanged.set(true);
			}
			
			
			RadioConfiguration arc = finalDetails.getAdvancedRadioMap().get(rt);
			if(arc == null) {
				arc = RadioConfiguration.createWithDefaults(rt);
				finalDetails.getAdvancedRadioMap().put(rt, arc);
			}

			RadioBestApSettings rbs = arc.getBestApSettings().getValue(); 
			
			if(rbs.getDropInSnrPercentage()== null || rbs.getDropInSnrPercentage().intValue() != updateDetails.getDropInSnrPercentage()) {
				rbs.setDropInSnrPercentage(updateDetails.getDropInSnrPercentage());
				modelChanged.set(true);
			}

			if(rbs.getMinLoadFactor()== null || rbs.getMinLoadFactor().intValue() != updateDetails.getMinLoadFactor()) {
				rbs.setMinLoadFactor(updateDetails.getMinLoadFactor());
				modelChanged.set(true);
			}

		});
		
		return modelChanged.get();
	}
}
