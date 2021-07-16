
package com.telecominfraproject.wlan.systemevent.models;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * Total and per-oui/equipment/radio counts of the Client Sessions
 */
public class EquipmentEventStats extends BaseJsonModel {

    private static final long serialVersionUID = 6630012772286577077L;

    /**
     * The equipment ID.
     */
    private long equipmentId;

    /**
     * Count of system events for the equipment.
     */
    private long totalCount;

    /**
     * Last event time.
     */
    private long lastEventTime;

    public long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public long getLastEventTime() {
        return lastEventTime;
    }

    public void setLastEventTime(long lastEventTime) {
        this.lastEventTime = lastEventTime;
    }

    @Override
    public EquipmentEventStats clone() {
        EquipmentEventStats ret = (EquipmentEventStats) super.clone();
        return ret;
    }

}
