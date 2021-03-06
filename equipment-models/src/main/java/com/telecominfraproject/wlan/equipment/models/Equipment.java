package com.telecominfraproject.wlan.equipment.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasCustomerId;

/**
 * @author dtoptygin
 *
 */
public class Equipment extends BaseJsonModel implements HasCustomerId {
    
	private static final long serialVersionUID = 5570757656953699233L;
	
	private long id;
    private int customerId;

    private long profileId;
    private long locationId;
    private EquipmentType equipmentType;
    private String inventoryId;
    
    private String name;
    private EquipmentDetails details;
    
    private  String latitude;
    private  String longitude;
    
    private MacAddress baseMacAddress;

    private  String serial;
    
    private long createdTimestamp;
    private long lastModifiedTimestamp;
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

    public long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public long getLastModifiedTimestamp() {
        return lastModifiedTimestamp;
    }

    public void setLastModifiedTimestamp(long lastModifiedTimestamp) {
        this.lastModifiedTimestamp = lastModifiedTimestamp;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

	public EquipmentDetails getDetails() {
		return details;
	}

	public void setDetails(EquipmentDetails details) {
		this.details = details;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public long getProfileId() {
		return profileId;
	}

	public void setProfileId(long profileId) {
		this.profileId = profileId;
	}

	public long getLocationId() {
		return locationId;
	}

	public void setLocationId(long locationId) {
		this.locationId = locationId;
	}

	public EquipmentType getEquipmentType() {
		return equipmentType;
	}

	public void setEquipmentType(EquipmentType equipmentType) {
		this.equipmentType = equipmentType;
	}

	public String getInventoryId() {
		return inventoryId;
	}

	public void setInventoryId(String inventoryId) {
		this.inventoryId = inventoryId;
	}

	public String getSerial() {
		return serial;
	}

	public void setSerial(String serial) {
		this.serial = serial;
	}

	public MacAddress getBaseMacAddress() {
		return baseMacAddress;
	}

	public void setBaseMacAddress(MacAddress baseMacAddress) {
		this.baseMacAddress = baseMacAddress;
	}

	@Override
	public boolean hasUnsupportedValue() {
		if (super.hasUnsupportedValue()) {
			return true;
		}

		if(details!=null && details.hasUnsupportedValue()) {
			return true;
		}

		if(equipmentType==null || EquipmentType.isUnsupported(equipmentType)) {
			return true;
		}

		return false;
	}
	
    @Override
    public Equipment clone() {
    	Equipment ret = (Equipment) super.clone();
    	if(details!=null) {
    		ret.setDetails(details.clone());
    	}
    	
    	if(baseMacAddress!=null) {
    		ret.baseMacAddress = baseMacAddress.clone();
    	}
    	
    	return ret;
    }

	@Override
	public int hashCode() {
		return Objects.hash(createdTimestamp, customerId, details, equipmentType, id, inventoryId,
				lastModifiedTimestamp, locationId, name, profileId, latitude, longitude, serial, baseMacAddress);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Equipment)) {
			return false;
		}
		Equipment other = (Equipment) obj;
		return createdTimestamp == other.createdTimestamp && customerId == other.customerId
				&& Objects.equals(details, other.details) && equipmentType == other.equipmentType && id == other.id
				&& Objects.equals(inventoryId, other.inventoryId)
				&& lastModifiedTimestamp == other.lastModifiedTimestamp && locationId == other.locationId
				&& Objects.equals(name, other.name) && profileId == other.profileId
						&& Objects.equals(latitude, other.latitude)
						&& Objects.equals(longitude, other.longitude)
						&& Objects.equals(serial, other.serial)
						&& Objects.equals(baseMacAddress, other.baseMacAddress)
						;
	}
    
}
