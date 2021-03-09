package com.telecominfraproject.wlan.status.equipment.report.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.utils.DecibelUtils;

/**
 * Contains scan information from neigbouring equipment that is not managed by
 * us.
 * 
 * @author ekeddy
 *
 */
public class UnmanagedNeighbourEquipmentInfo extends NeighbourEquipmentInfo {
    private static final long serialVersionUID = 5525006111579379607L;
    private List<NeighbourRadioInfo> radios;

    @Override
    public List<NeighbourRadioInfo> getRadios() {
        return radios;
    }

    public void setRadios(List<NeighbourRadioInfo> radios) {
        this.radios = radios;
    }

    /**
     * This will return the "lower" MacAddress (ie: 01:02:03:04:05:06 is lower
     * than ..:07).
     * 
     * If there's multiple radios using the same frequency, we return the lower
     * of them all.
     * 
     * NOTE: This is temporary until we have an unmanaged equipment table with
     * Ids (so, from multiple devices' perspective, we'll know which see the
     * same equipment as opposed to a set of radios and BSSIDs).
     * 
     * @param radio
     * @return
     */
    @JsonIgnoreProperties
    public MacAddress getMacAddress(RadioType radioType) 
    {
        MacAddress lowestMac = null;

        for (NeighbourRadioInfo radio : radios) 
        {
            for (NeighbourBssidInfo bssInfo : radio.getBssIds()) 
            {
                MacAddress newMac = bssInfo.getMacAddress();

                if (lowestMac == null && bssInfo.getMacAddress() != null) 
                {
                    lowestMac = bssInfo.getMacAddress();
                } 
                else if (lowestMac != null && lowestMac.getAddressAsLong() > newMac.getAddressAsLong()) 
                {
                    lowestMac = newMac;
                }
            }
        }

        return lowestMac;

    }

    @JsonIgnore
    public int getAverageSignalStrenght(RadioType radioType) {
        List<Integer> rssiList = new ArrayList<>();
        
        for (NeighbourRadioInfo radioInfo : this.radios) {
            if (radioInfo.getRadioType() == radioType) {
                for (NeighbourBssidInfo bssInfo : radioInfo.getBssIds()) {
                    rssiList.add(bssInfo.getRssi());
                }
            }
        }

        if (rssiList.size() > 0) {
        	int[] rssiArray = new int[rssiList.size()];
        	int index = 0;
        	for (Integer rssi : rssiList) {
        		rssiArray[index++] = rssi;
        	}
 
            return (int) Math.round(DecibelUtils.getAverageDecibel(rssiArray));
        } else {
            return 0;
        }
    }


    @JsonIgnore
    public boolean isConflictingNeighbour(RadioType radioType, int channel) 
    {
        for (NeighbourRadioInfo radioInfo : this.radios) 
        {
            if (radioInfo.getRadioType() == radioType) 
            {
                for (NeighbourBssidInfo bssInfo : radioInfo.getBssIds()) 
                {
                    if(bssInfo.getChannel() == channel)
                    {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @JsonIgnoreProperties
    public boolean isRadioTurnedOn(RadioType radio) {
        for (NeighbourRadioInfo radioInfo : this.radios) {
            if (radioInfo.getRadioType() == radio) {
                return true;
            }
        }

        return false;
    }

    @Override
    public UnmanagedNeighbourEquipmentInfo clone() {
        UnmanagedNeighbourEquipmentInfo ret = (UnmanagedNeighbourEquipmentInfo) super.clone();
        if (this.radios != null) {
            ret.radios = new ArrayList<>(this.radios.size());
            this.radios.forEach(r -> ret.radios.add(r.clone()));
        }
        return ret;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }

        if (hasUnsupportedValue(radios)) {
            return true;
        }
        return false;
    }

	@Override
	public int hashCode() {
		return Objects.hash(radios);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof UnmanagedNeighbourEquipmentInfo)) {
			return false;
		}
		UnmanagedNeighbourEquipmentInfo other = (UnmanagedNeighbourEquipmentInfo) obj;
		return Objects.equals(radios, other.radios);
	}

}
