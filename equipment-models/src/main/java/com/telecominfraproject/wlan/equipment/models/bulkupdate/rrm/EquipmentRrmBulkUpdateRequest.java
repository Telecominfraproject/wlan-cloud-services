package com.telecominfraproject.wlan.equipment.models.bulkupdate.rrm;

import java.util.ArrayList;
import java.util.List;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

public class EquipmentRrmBulkUpdateRequest extends BaseJsonModel {

	private static final long serialVersionUID = -6675868440623722451L;
	
	private List<EquipmentRrmBulkUpdateItem> items = new ArrayList<>();

	public List<EquipmentRrmBulkUpdateItem> getItems() {
		return items;
	}

	public void setItems(List<EquipmentRrmBulkUpdateItem> items) {
		this.items = items;
	}

}
