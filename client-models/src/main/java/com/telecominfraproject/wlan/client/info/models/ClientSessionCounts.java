
package com.telecominfraproject.wlan.client.info.models;

import java.util.HashMap;
import java.util.Map;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * Total and per-oui counts of the Client Sessions
 */
public class ClientSessionCounts extends BaseJsonModel {

    private static final long serialVersionUID = 7697349699510565184L;

    private int totalCount;
    private Map<String, Integer> ouiCounts = new HashMap<>();

    public Map<String, Integer> getOuiCounts() {
        return ouiCounts;
    }

    public void setOuiCounts(Map<String, Integer> ouiCounts) {
        this.ouiCounts = ouiCounts;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    @Override
    public ClientSessionCounts clone() {
        ClientSessionCounts ret = (ClientSessionCounts) super.clone();
        if (ouiCounts != null) {
            ret.ouiCounts = new HashMap<>(ouiCounts);
        }

        return ret;
    }
}
