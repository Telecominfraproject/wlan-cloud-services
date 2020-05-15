package com.telecominfraproject.wlan.status.equipment.report.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author ekeddy
 *
 */
public class EquipmentCapacityDetails extends BaseJsonModel {
    
    private static final long serialVersionUID = -4373799662182001087L;

    /**
     *  A theoretical maximum based on channel bandwidth
     */
    private int totalCapacity;
    
    /**
     * The percentage of capacity that is available for clients.
     */
    private int availableCapacity;
    
    /**
     * The percentage of capacity that is not available for clients (e.g. beacons, noise, non-wifi)
     */
    private int unavailableCapacity;
    
    /**
     * The percentage of the overall capacity that is not being used.
     */
    private int unusedCapacity;
    
    /**
     * The percentage of the overall capacity that is currently being used by associated clients.
     */
    private int usedCapacity;
    
    public int getTotalCapacity() {
        return totalCapacity;
    }
    public void setTotalCapacity(int maxCapacity) {
        this.totalCapacity = maxCapacity;
    }
    public int getAvailableCapacity() {
        return availableCapacity;
    }
    public void setAvailableCapacity(int availableCapacity) {
        this.availableCapacity = availableCapacity;
    }
    public int getUnavailableCapacity() {
        return unavailableCapacity;
    }
    public void setUnavailableCapacity(int unavailableCapacity) {
        this.unavailableCapacity = unavailableCapacity;
    }
    public int getUnusedCapacity() {
        return unusedCapacity;
    }
    public void setUnusedCapacity(int unusedCapacity) {
        this.unusedCapacity = unusedCapacity;
    }
    public int getUsedCapacity() {
        return usedCapacity;
    }
    public void setUsedCapacity(int usedCapacity) {
        this.usedCapacity = usedCapacity;
    }
    
	@Override
	public int hashCode() {
		return Objects.hash(availableCapacity, totalCapacity, unavailableCapacity, unusedCapacity, usedCapacity);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof EquipmentCapacityDetails)) {
			return false;
		}
		EquipmentCapacityDetails other = (EquipmentCapacityDetails) obj;
		return availableCapacity == other.availableCapacity && totalCapacity == other.totalCapacity
				&& unavailableCapacity == other.unavailableCapacity && unusedCapacity == other.unusedCapacity
				&& usedCapacity == other.usedCapacity;
	}
	
	@Override
	public EquipmentCapacityDetails clone() {
		return (EquipmentCapacityDetails) super.clone();
	}
}
