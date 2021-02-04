package com.telecominfraproject.wlan.client.session.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasCustomerId;

public class ClientSession extends BaseJsonModel implements HasCustomerId {

    private static final long serialVersionUID = -7714023056859882994L;

    private MacAddress macAddress;
    private int customerId;
    private long equipmentId;
    private long locationId;
    private ClientSessionDetails details;
    private long lastModifiedTimestamp;

    public long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public long getLocationId() {
		return locationId;
	}

	public void setLocationId(long locationId) {
		this.locationId = locationId;
	}

	public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public MacAddress getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(MacAddress macAddress) {
		this.macAddress = macAddress;
	}

	public long getLastModifiedTimestamp() {
		return lastModifiedTimestamp;
	}

	public void setLastModifiedTimestamp(long lastModifiedTimestamp) {
		this.lastModifiedTimestamp = lastModifiedTimestamp;
	}

	public ClientSessionDetails getDetails() {
		return details;
	}

	public void setDetails(ClientSessionDetails details) {
		this.details = details;
	}

	@Override
    public ClientSession clone() {
        ClientSession ret = (ClientSession) super.clone();
        if (this.macAddress != null) {
            ret.setMacAddress(this.macAddress.clone());
        }
        if (this.details != null) {
            ret.setDetails(this.details.clone());
        }
        return ret;
    }

    @Override
	public int hashCode() {
		return Objects.hash(customerId, details, equipmentId, locationId, lastModifiedTimestamp, macAddress);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ClientSession)) {
			return false;
		}
		ClientSession other = (ClientSession) obj;
		return customerId == other.customerId && Objects.equals(details, other.details)
				&& equipmentId == other.equipmentId 
				&& locationId == other.locationId 
				&& lastModifiedTimestamp == other.lastModifiedTimestamp
				&& Objects.equals(macAddress, other.macAddress);
	}

	@Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }

        if (hasUnsupportedValue(macAddress)) {
            return true;
        }
        return false;
    }


}
