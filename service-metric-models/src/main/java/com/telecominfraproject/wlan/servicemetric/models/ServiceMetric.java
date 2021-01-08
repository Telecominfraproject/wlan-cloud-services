package com.telecominfraproject.wlan.servicemetric.models;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasClientMac;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasCustomerId;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasEquipmentId;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasLocationId;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasProducedTimestamp;

/**
 * @author dtoptygin
 *
 */
public class ServiceMetric extends BaseJsonModel implements HasCustomerId, HasEquipmentId, HasClientMac, HasLocationId, HasProducedTimestamp {
    
	private static final long serialVersionUID = 5570757656953699233L;
	
    private int customerId;
	private long equipmentId;
	private long locationId;
	private long clientMac;
	private ServiceMetricDataType dataType;
    private long createdTimestamp;    

    private ServiceMetricDetails details;
    

    public ServiceMetric() {
		// for serialization
	}

	public ServiceMetric(int customerId, long equipmentId) {
		super();
		this.customerId = customerId;
		this.equipmentId = equipmentId;
	}

	public ServiceMetric(int customerId, long equipmentId, long clientMac) {
		super();
		this.customerId = customerId;
		this.equipmentId = equipmentId;
		this.clientMac = clientMac;
	}

	public ServiceMetric(int customerId, long equipmentId, MacAddress clientMacAddress) {
		super();
		this.customerId = customerId;
		this.equipmentId = equipmentId;
		this.clientMac = clientMacAddress.getAddressAsLong();
	}
	
	public int getCustomerId() {
		return customerId;
	}

	public void setCustomerId(int customerId) {
		this.customerId = customerId;
	}

	public long getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(long equipmentId) {
		this.equipmentId = equipmentId;
	}

	public long getClientMac() {
		return clientMac;
	}

	public void setClientMac(long clientMac) {
		this.clientMac = clientMac;
	}

	public long getLocationId() {
		return locationId;
	}

	public void setLocationId(long locationId) {
		this.locationId = locationId;
	}

	public ServiceMetricDetails getDetails() {
		return details;
	}

	public void setDetails(ServiceMetricDetails details) {
		this.details = details;
		//automatically set the status data type based on status details
		if(details!=null) {
			setDataType(details.getDataType());
		}
		
	}

	public long getCreatedTimestamp() {
		return createdTimestamp;
	}

	public void setCreatedTimestamp(long createdTimestamp) {
		this.createdTimestamp = createdTimestamp;
	}

	@Override
	public MacAddress getClientMacAddress() {
		if(clientMac==0) {
			return null;
		}
		return new MacAddress(clientMac);
	}
	
	public ServiceMetricDataType getDataType() {
		return dataType;
	}

	public void setDataType(ServiceMetricDataType dataType) {
		this.dataType = dataType;
	}

	@Override
	public int hashCode() {
		return Objects.hash(clientMac, createdTimestamp, customerId, details, equipmentId, dataType, locationId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ServiceMetric)) {
			return false;
		}
		ServiceMetric other = (ServiceMetric) obj;
		return clientMac == other.clientMac && createdTimestamp == other.createdTimestamp
				&& customerId == other.customerId && Objects.equals(details, other.details)
				&& equipmentId == other.equipmentId && dataType == other.dataType
				&& locationId == other.locationId;
	}

	@Override
	public boolean hasUnsupportedValue() {
		if (super.hasUnsupportedValue()) {
			return true;
		}

		if(details!=null && details.hasUnsupportedValue()) {
			return true;
		}
		
		if(ServiceMetricDataType.isUnsupported(dataType)) {
			return true;
		}

		return false;
	}
	
    @Override
    public ServiceMetric clone() {
    	ServiceMetric ret = (ServiceMetric) super.clone();
    	if(details!=null) {
    		ret.details = (details.clone());
    	}
    	
    	return ret;
    }

	@Override
	@JsonIgnore
	public long getProducedTimestampMs() {
		return createdTimestamp;
	}
    
    
}
