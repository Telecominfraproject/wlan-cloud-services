package com.telecominfraproject.wlan.equipment.models;

import java.util.HashMap;
import java.util.Map;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author dtop
 * <br> Total and per-oui counts of the equipment for a given customer
 */
public class CustomerEquipmentCounts extends BaseJsonModel {

	private static final long serialVersionUID = -501180321109045883L;
	
	private int customerId;
	private int totalCount;
	private Map<String, Integer> ouiCounts = new HashMap<>();
	
	public int getCustomerId() {
		return customerId;
	}
	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}
	public Map<String, Integer> getOuiCounts() {
		return ouiCounts;
	}
	public void setOuiCounts(Map<String, Integer> ouiCounts) {
		this.ouiCounts = ouiCounts;
	}
	
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
	@Override
	public CustomerEquipmentCounts clone() {
		CustomerEquipmentCounts ret = (CustomerEquipmentCounts) super.clone();
		if(ouiCounts!=null) {
			ret.ouiCounts = new HashMap<>(ouiCounts);
		}
		
		return ret;
	}
}
