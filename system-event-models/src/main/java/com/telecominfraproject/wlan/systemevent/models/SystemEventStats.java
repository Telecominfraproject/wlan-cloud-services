
package com.telecominfraproject.wlan.systemevent.models;

import java.util.ArrayList;
import java.util.List;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * Total and per equipment counts of the System Events
 */
public class SystemEventStats extends BaseJsonModel {

    private static final long serialVersionUID = 6630012772286577077L;

    /**
     * Total count of all system events.
     */
    private long totalCount;

    /**
     * Counts of system events per equipment Id.
     */
    private List<EquipmentEventStats> equipmentStats = new ArrayList<>();

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public List<EquipmentEventStats> getEquipmentStats() {
        return equipmentStats;
    }

    public void setEquipmentStats(List<EquipmentEventStats> equipmentStats) {
        this.equipmentStats = equipmentStats;
    }

    @Override
    public SystemEventStats clone() {
        SystemEventStats ret = (SystemEventStats) super.clone();
        if (equipmentStats != null) {
            ret.equipmentStats = new ArrayList<>(equipmentStats);
        }
        return ret;
    }

}
