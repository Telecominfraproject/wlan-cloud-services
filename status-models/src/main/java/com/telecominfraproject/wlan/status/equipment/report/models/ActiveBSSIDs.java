package com.telecominfraproject.wlan.status.equipment.report.models;

import java.util.List;
import java.util.Objects;

import com.telecominfraproject.wlan.status.models.StatusDetails;

/**
 * A summary of the AP's active BSSIDs
 * 
 * @author erikvilleneuve
 *
 */
public class ActiveBSSIDs extends StatusDetails 
{
    private static final long serialVersionUID = 7649635073161985205L;
    private List<ActiveBSSID> activeBSSIDs;

    public ActiveBSSIDs()
    {
        //serial
    }

    public List<ActiveBSSID> getActiveBSSIDs() {
        return activeBSSIDs;
    }

    public void setActiveBSSIDs(List<ActiveBSSID> activeBSSIDs) {
        this.activeBSSIDs = activeBSSIDs;
    }
    
    @Override
    public boolean hasUnsupportedValue()
    {
        return hasUnsupportedValue(this.activeBSSIDs);
    }

	@Override
	public int hashCode() {
		return Objects.hash(activeBSSIDs);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ActiveBSSIDs)) {
			return false;
		}
		ActiveBSSIDs other = (ActiveBSSIDs) obj;
		return Objects.equals(activeBSSIDs, other.activeBSSIDs);
	}

}
