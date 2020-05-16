package com.telecominfraproject.wlan.status.equipment.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.status.models.StatusDataType;
import com.telecominfraproject.wlan.status.models.StatusDetails;

/**
 * 
 * This will be a copy of the neighbouring status details.
 * 
 * @author erikvilleneuve
 *
 */
public class EquipmentNeighbouringStatusData extends StatusDetails 
{
    static final long serialVersionUID = 489508020460226356L;
    private Map<RadioType, List<RoamingCandidate>> neighbours = new HashMap<>();

    public EquipmentNeighbouringStatusData()
    {
        // serial
    }
    
    @Override
    public StatusDataType getStatusDataType() {
    	return StatusDataType.NEIGHBOURINGINFO;
    }
    
    protected EquipmentNeighbouringStatusData(EquipmentNeighbouringStatusData data)
    {
        if(data != null)
        {
            this.neighbours.putAll(data.neighbours);
        }
    }
    
    public Map<RadioType, List<RoamingCandidate>> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(Map<RadioType, List<RoamingCandidate>> neighbours) {
        this.neighbours = neighbours;
    }

    public boolean needsToBeUpdatedOnDevice(EquipmentNeighbouringStatusData previousVersion) {
        if (previousVersion != null) {
        	List<RoamingCandidate> combinedList = new ArrayList<>(); 
        	neighbours.values().forEach( list -> combinedList.addAll(list) );
        	
        	List<RoamingCandidate> previousCombinedList = new ArrayList<>(); 
        	neighbours.values().forEach( list -> previousCombinedList.addAll(list) );

        	return areDifferentEnough(combinedList, previousCombinedList);
        }

        return true;
    }

    static boolean areDifferentEnough(List<RoamingCandidate> neighbour, List<RoamingCandidate> otherNeighbours) {
        if (neighbour != null && otherNeighbours != null) {
            if (neighbour.size() == otherNeighbours.size()) {
                for (int i = 0; i < neighbour.size(); i++) {
                    if (neighbour.get(i).needToBeUpdated(otherNeighbours.get(i))) {
                        return true;
                    }
                }
            } else {
                return true;
            }
        } else {
            // One of these is null...
            return neighbour != otherNeighbours;
        }

        return false;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }

        if (neighbours!=null ) {
        	boolean hasUnsupported = false;
        	
        	for(List<RoamingCandidate> listOfCandidates: neighbours.values()) {
        		if(hasUnsupportedValue(listOfCandidates)) {
        			hasUnsupported = true;
        			break;
        		}
        	}
        	
        	if(hasUnsupported) {
        		return true;
        	}
        }
        return false;
    }

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(neighbours);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!super.equals(obj)) {
			return false;
		}
		if (!(obj instanceof EquipmentNeighbouringStatusData)) {
			return false;
		}
		EquipmentNeighbouringStatusData other = (EquipmentNeighbouringStatusData) obj;
		return Objects.equals(neighbours, other.neighbours);
	}
    
}
