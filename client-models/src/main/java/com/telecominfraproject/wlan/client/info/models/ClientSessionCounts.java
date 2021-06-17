
package com.telecominfraproject.wlan.client.info.models;

import java.util.HashMap;
import java.util.Map;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * Total and per-oui/equipment/radio counts of the Client Sessions
 */
public class ClientSessionCounts extends BaseJsonModel {

    private static final long serialVersionUID = 7697349699510565184L;

    private int customerId;
    /**
     * Total count of all client sessions for the customer Id.
     */
    private long totalCount;
    /**
     * Counts of client sessions per equipment Id.
     */
    private Map<String, Long> equipmentCounts = new HashMap<>();
    /**
     * Counts of client sessions per OUI.
     */
    private Map<String, Long> ouiCounts = new HashMap<>();
    /**
     * Counts of client sessions per Radio Type.
     */
    private Map<String, Long> radioCounts = new HashMap<>();

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(long totalCount) {
        this.totalCount = totalCount;
    }

    public Map<String, Long> getEquipmentCounts() {
        return equipmentCounts;
    }

    public void setEquipmentCounts(Map<String, Long> equipmentCounts) {
        this.equipmentCounts = equipmentCounts;
    }

    public Map<String, Long> getOuiCounts() {
        return ouiCounts;
    }

    public void setOuiCounts(Map<String, Long> ouiCounts) {
        this.ouiCounts = ouiCounts;
    }

    public Map<String, Long> getRadioCounts() {
        return radioCounts;
    }

    public void setRadioCounts(Map<String, Long> radioCounts) {
        this.radioCounts = radioCounts;
    }

    @Override
    public ClientSessionCounts clone() {
        ClientSessionCounts ret = (ClientSessionCounts) super.clone();
        if (equipmentCounts != null) {
            ret.equipmentCounts = new HashMap<>(equipmentCounts);
        }
        if (ouiCounts != null) {
            ret.ouiCounts = new HashMap<>(ouiCounts);
        }
        if (radioCounts != null) {
            ret.radioCounts = new HashMap<>(radioCounts);
        }

        return ret;
    }

}
