package com.telecominfraproject.wlan.location.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.entity.CountryCode;
import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasCustomerId;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;

/**
 * @author dtop
 *
 */
public class Location extends BaseJsonModel implements PushableConfiguration<Location>, HasCustomerId {

	private static final long serialVersionUID = 7891602187654246292L;

	private long id;
	private long parentId;

	private LocationType locationType;
	private int customerId;
	private String name;
	private LocationDetails details;

	private long createdTimestamp;
	private long lastModifiedTimestamp;

	public Location() {
		// Defaults (they'll be overwritten if defined)
		details = LocationDetails.createWithDefaults();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public LocationType getLocationType() {
		return locationType;
	}

	public void setLocationType(LocationType locationType) {
		this.locationType = locationType;
	}

	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocationDetails getDetails() {
		return details;
	}

	public void setDetails(LocationDetails details) {
		this.details = details;
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

	@Override
	public Location clone() {
		Location ret = (Location) super.clone();
		return ret;
	}

	@Override
	public int hashCode() {
		return Objects.hash(createdTimestamp, customerId, details, id, lastModifiedTimestamp, locationType, name,
				parentId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Location)) {
			return false;
		}
		Location other = (Location) obj;
		return createdTimestamp == other.createdTimestamp && customerId == other.customerId
				&& Objects.equals(details, other.details) && id == other.id
				&& lastModifiedTimestamp == other.lastModifiedTimestamp && locationType == other.locationType
				&& Objects.equals(name, other.name) && parentId == other.parentId;
	}

	@Override
	public boolean needsToBeUpdatedOnDevice(Location previousVersion) {
		// If the country codes are different, a new config needs to be pushed to
		// the device.
		//
		return !Objects.equals(Location.getCountryCode(previousVersion),
				Location.getCountryCode(this));
	}

	public static CountryCode getCountryCode(Location location) {
		if (location != null && location.getDetails() != null) {
			return location.getDetails().getCountryCode();
		}

		return null;
	}
	
	@Override
	public boolean hasUnsupportedValue() {
		if (super.hasUnsupportedValue()) {
			return true;
		}

		if (LocationType.isUnsupported(locationType) || hasUnsupportedValue(details)) {
			return true;
		}
		return false;
	}

	/**
	 * Validate the data.
	 * 
	 * @throws DsDataValidationException
	 */
	public void validateValue() {
		if (hasUnsupportedValue()) {
			throw new DsDataValidationException("Record contains unsupported value");
		}
		if (null == this.details) {
			throw new DsDataValidationException("Missing location details");
		}
		if (null != this.details.getMaintenanceWindow() && !LocationType.SITE.equals(locationType)) {
			throw new DsDataValidationException("Maintainence window is only allowed on top level location");
		}
	}

}
