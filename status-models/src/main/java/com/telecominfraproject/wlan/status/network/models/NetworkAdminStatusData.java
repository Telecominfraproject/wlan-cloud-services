package com.telecominfraproject.wlan.status.network.models;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.status.models.StatusCode;
import com.telecominfraproject.wlan.status.models.StatusDataType;
import com.telecominfraproject.wlan.status.models.StatusDetails;

/**
 * @author ekeddy
 *
 */
public class NetworkAdminStatusData extends StatusDetails {
 
    private static final long serialVersionUID = 4586203613209434309L;
    
    private StatusCode dhcpStatus;
    private StatusCode dnsStatus;
    private StatusCode cloudLinkStatus;
    private StatusCode radiusStatus;
    private Map<RadioType, Integer> averageCoveragePerRadio = new EnumMap<>(RadioType.class);   
    private Map<StatusCode, Integer> equipmentCountsBySeverity = new EnumMap<>(StatusCode.class);
        
    @Override
    public StatusDataType getStatusDataType() {
    	return StatusDataType.NETWORK_ADMIN;
    }    

    public StatusCode getDhcpStatus() {
        return dhcpStatus;
    }

    public void setDhcpStatus(StatusCode dhcpStatus) {
        this.dhcpStatus = dhcpStatus;
    }

    public StatusCode getDnsStatus() {
        return dnsStatus;
    }

    public void setDnsStatus(StatusCode dnsStatus) {
        this.dnsStatus = dnsStatus;
    }

    public StatusCode getCloudLinkStatus() {
        return cloudLinkStatus;
    }

    public void setCloudLinkStatus(StatusCode cloudLinkStatus) {
        this.cloudLinkStatus = cloudLinkStatus;
    }

    public StatusCode getRadiusStatus() {
        return radiusStatus;
    }

    public void setRadiusStatus(StatusCode radiusStatus) {
        this.radiusStatus = radiusStatus;
    }

    public Map<RadioType, Integer> getAverageCoveragePerRadio() {
		return averageCoveragePerRadio;
	}

	public void setAverageCoveragePerRadio(Map<RadioType, Integer> averageCoveragePerRadio) {
		this.averageCoveragePerRadio = averageCoveragePerRadio;
	}

	public Map<StatusCode, Integer> getEquipmentCountsBySeverity() {
		return equipmentCountsBySeverity;
	}

	public void setEquipmentCountsBySeverity(Map<StatusCode, Integer> equipmentCountsBySeverity) {
		this.equipmentCountsBySeverity = equipmentCountsBySeverity;
	}

	@Override
    public NetworkAdminStatusData clone() {
        NetworkAdminStatusData ret = (NetworkAdminStatusData) super.clone();
        if (this.equipmentCountsBySeverity != null) {
            ret.equipmentCountsBySeverity = new EnumMap<>(this.equipmentCountsBySeverity);
        }

        if (this.averageCoveragePerRadio != null) {
            ret.averageCoveragePerRadio = new EnumMap<>(this.averageCoveragePerRadio);
        }

        return ret;
    }

    /**
     * @param other
     * @return this object, merged with other
     */
    public NetworkAdminStatusData combineWith(NetworkAdminStatusData other) {
        
        if(other == null){
            return this;
        }
       
        if(averageCoveragePerRadio!=null) {
        	if(other.averageCoveragePerRadio!=null) {
	        	for(Map.Entry<RadioType, Integer> entry : other.averageCoveragePerRadio.entrySet()) {
	        		Integer existingEntry =averageCoveragePerRadio.putIfAbsent(entry.getKey(), entry.getValue()); 
	        		if( existingEntry!=null && existingEntry==0) {
	        			averageCoveragePerRadio.put(entry.getKey(), entry.getValue());
	        		}
	        	}
        	}
        } else {
        	if(other.averageCoveragePerRadio!=null) {
        		this.averageCoveragePerRadio = new HashMap<>(other.averageCoveragePerRadio);
        	}
        }

        if(equipmentCountsBySeverity!=null) {
        	if(other.equipmentCountsBySeverity!=null) {
	        	for(Map.Entry<StatusCode, Integer> entry : other.equipmentCountsBySeverity.entrySet()) {
	        		Integer existingEntry =equipmentCountsBySeverity.putIfAbsent(entry.getKey(), entry.getValue()); 
	        		if( existingEntry!=null && existingEntry==0) {
	        			equipmentCountsBySeverity.put(entry.getKey(), entry.getValue());
	        		}
	        	}
        	}
        } else {
        	if(other.equipmentCountsBySeverity!=null) {
        		this.equipmentCountsBySeverity = new HashMap<>(other.equipmentCountsBySeverity);
        	}
        }

        return this;
    }
    
    @Override
	public int hashCode() {
		return Objects.hash(averageCoveragePerRadio, cloudLinkStatus, dhcpStatus, dnsStatus, equipmentCountsBySeverity,
				radiusStatus);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof NetworkAdminStatusData)) {
			return false;
		}
		NetworkAdminStatusData other = (NetworkAdminStatusData) obj;
		return Objects.equals(averageCoveragePerRadio, other.averageCoveragePerRadio)
				&& cloudLinkStatus == other.cloudLinkStatus && dhcpStatus == other.dhcpStatus
				&& dnsStatus == other.dnsStatus
				&& Objects.equals(equipmentCountsBySeverity, other.equipmentCountsBySeverity)
				&& radiusStatus == other.radiusStatus;
	}

	@Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (StatusCode.isUnsupported(dhcpStatus) ||
                StatusCode.isUnsupported(dnsStatus) ||
                StatusCode.isUnsupported(cloudLinkStatus) ||
                StatusCode.isUnsupported( radiusStatus)) {
            return true;
        }
        
        if ((null != equipmentCountsBySeverity) && StatusCode.hasUnsupported(equipmentCountsBySeverity.keySet())) {
            return true;
        }
        return false;
    }
}
