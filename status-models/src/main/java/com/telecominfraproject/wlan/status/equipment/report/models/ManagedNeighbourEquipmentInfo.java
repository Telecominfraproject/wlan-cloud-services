package com.telecominfraproject.wlan.status.equipment.report.models;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;

/**
 * @author ekeddy
 *
 */
public class ManagedNeighbourEquipmentInfo extends NeighbourEquipmentInfo {
    private static final long serialVersionUID = -3878544641084510796L;
    
    private long equipmentId;
    private Map<RadioType, NeighbourRadioInfo> radioInfo = new EnumMap<>(RadioType.class);

	public long getEquipmentId() {
		return equipmentId;
	}

	public void setEquipmentId(long equipmentId) {
		this.equipmentId = equipmentId;
	}
	
	public Map<RadioType, NeighbourRadioInfo> getRadioInfo() {
		return radioInfo;
	}

	public void setRadioInfo(Map<RadioType, NeighbourRadioInfo> radioInfo) {
		this.radioInfo = radioInfo;
	}

	@JsonIgnore
	public NeighbourRadioInfo getRadioInfo(RadioType radio) {
	    return this.radioInfo.get(radio);
	}
	
	/**
	 * This will return the "average" RSSI value for a specific radio based on the RSSIs
	 * of it's independent SSIDs. 
	 * 
	 * @param radioType
	 * @return
	 */
	@JsonIgnore
	public Integer getAverageSignalStrenght(RadioType radioType)
	{
	   NeighbourRadioInfo info = getRadioInfo(radioType);
	   
	   int runningTotal = 0;
	   int numEntries = 0;
	   
	   if(info != null)
	   {
	      for(NeighbourBssidInfo singleBssidInfo :  info.getBssIds())
	      {
	         runningTotal += singleBssidInfo.getSignal();
	         numEntries++;
	      }
	   }
	   
	   if(numEntries != 0)
	   {
	      return runningTotal / numEntries;
	   }
	   else
	   {
	      return null;
	   }
	   
	}

	@JsonIgnore
	public boolean isConflictingNeighbour(RadioType radioType, int channel)
	{
	    NeighbourRadioInfo info = getRadioInfo(radioType);

	    if(info != null)
	    {
	        for(NeighbourBssidInfo singleBssidInfo :  info.getBssIds())
	        {
	            if(singleBssidInfo.getChannel() == channel)
	            {
	                return true;
	            }
	        }
	    }

	    return false;

	}


	@Override
	@JsonIgnore
	public List<NeighbourRadioInfo> getRadios() {
		List<NeighbourRadioInfo> ret = new ArrayList<>();
		if(radioInfo!=null) {
			ret.addAll(radioInfo.values());
		}
		return ret;
	}

	
    @Override
    public ManagedNeighbourEquipmentInfo clone() {
        ManagedNeighbourEquipmentInfo neighbourInfo = (ManagedNeighbourEquipmentInfo) super.clone();
        
        neighbourInfo.equipmentId = this.equipmentId;
        
		if(radioInfo!=null) {
			neighbourInfo.radioInfo = new EnumMap<>(RadioType.class);

			radioInfo.forEach( (rt, cd) -> neighbourInfo.radioInfo.put(rt, cd.clone()) );
        }
        
        return neighbourInfo;
        
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }

		if(radioInfo!=null) {
			for(NeighbourRadioInfo n: radioInfo.values()) {
				if(hasUnsupportedValue(n)) {
					return true;
				}
			}
        }
        
        return false;
    }

	@Override
	public int hashCode() {
		return Objects.hash(equipmentId, radioInfo);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof ManagedNeighbourEquipmentInfo)) {
			return false;
		}
		ManagedNeighbourEquipmentInfo other = (ManagedNeighbourEquipmentInfo) obj;
		return equipmentId == other.equipmentId && Objects.equals(radioInfo, other.radioInfo);
	}
    
}
