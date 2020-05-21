package com.telecominfraproject.wlan.client.datastore.inmemory;

import java.util.Objects;

import com.telecominfraproject.wlan.client.session.models.ClientSession;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;

public class CustomerEquipmentMacKey{
	final int customerId;
	final long equipmentId;
	final MacAddress mac;
	
	public CustomerEquipmentMacKey(int customerId, long equipmentId, MacAddress mac) {
		this.customerId = customerId;
		this.equipmentId = equipmentId;
		this.mac = mac;
	}

	public CustomerEquipmentMacKey(ClientSession clientSession) {
		this.customerId = clientSession.getCustomerId();
		this.equipmentId = clientSession.getEquipmentId();
		this.mac = clientSession.getMacAddress().clone();
	}

	@Override
	public int hashCode() {
		return Objects.hash(customerId, equipmentId, mac);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof CustomerEquipmentMacKey)) {
			return false;
		}
		CustomerEquipmentMacKey other = (CustomerEquipmentMacKey) obj;
		return customerId == other.customerId && equipmentId == other.equipmentId && Objects.equals(mac, other.mac);
	}
	
	
}