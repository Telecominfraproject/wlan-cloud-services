package com.telecominfraproject.wlan.status.equipment.report.models;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.status.models.StatusDataType;
import com.telecominfraproject.wlan.status.models.StatusDetails;

public class ClientConnectionDetails extends StatusDetails 
{
    private static final long serialVersionUID = 2800044337803284405L;
    private Map<RadioType, Integer> numClientsPerRadio = new EnumMap<>(RadioType.class);
    
    public ClientConnectionDetails()
    {
        // serial
    }

    @Override
    public StatusDataType getStatusDataType() {
    	return StatusDataType.CLIENT_DETAILS;
    }
    
	public Map<RadioType, Integer> getNumClientsPerRadio() {
		return numClientsPerRadio;
	}

	public void setNumClientsPerRadio(Map<RadioType, Integer> numClientsPerRadio) {
		this.numClientsPerRadio = numClientsPerRadio;
	}

	@Override
	public int hashCode() {
		return Objects.hash(numClientsPerRadio);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ClientConnectionDetails)) {
			return false;
		}
		ClientConnectionDetails other = (ClientConnectionDetails) obj;
		return Objects.equals(numClientsPerRadio, other.numClientsPerRadio);
	}
    
	@Override
	public ClientConnectionDetails clone() {
		ClientConnectionDetails ret = (ClientConnectionDetails) super.clone();

		if(numClientsPerRadio!=null) {
        	ret.numClientsPerRadio = new EnumMap<>(RadioType.class);

        	numClientsPerRadio.forEach( (rt, cd) -> ret.numClientsPerRadio.put(rt, cd) );
        }
        
		return ret;
	}
}
