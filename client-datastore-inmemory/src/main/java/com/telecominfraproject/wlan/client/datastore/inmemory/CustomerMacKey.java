package com.telecominfraproject.wlan.client.datastore.inmemory;

import java.util.Objects;

import com.telecominfraproject.wlan.client.models.Client;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;

public class CustomerMacKey{
	final int customerId;
	final MacAddress mac;
	
	public CustomerMacKey(int customerId, MacAddress mac) {
		this.customerId = customerId;
		this.mac = mac;
	}

	public CustomerMacKey(Client client) {
		this.customerId = client.getCustomerId();
		this.mac = client.getMacAddress().clone();
	}

	@Override
	public int hashCode() {
		return Objects.hash(customerId, mac);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof CustomerMacKey)) {
			return false;
		}
		CustomerMacKey other = (CustomerMacKey) obj;
		return customerId == other.customerId && Objects.equals(mac, other.mac);
	}
	
	
}