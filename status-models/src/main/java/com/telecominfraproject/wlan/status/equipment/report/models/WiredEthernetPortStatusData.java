package com.telecominfraproject.wlan.status.equipment.report.models;

import com.telecominfraproject.wlan.status.models.StatusDataType;
import com.telecominfraproject.wlan.status.models.StatusDetails;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author rsharma
 */
public class WiredEthernetPortStatusData extends StatusDetails {
    private static final long serialVersionUID = 1684955685155400122L;

    // key is ifName, value is the list of wiredPortStatus that link to the ifName
    private Map<String, List<WiredPortStatus>> interfacePortStatusMap;
    
    public WiredEthernetPortStatusData() {
    	this.interfacePortStatusMap = new HashMap<>();
    }

    public WiredEthernetPortStatusData(Map<String, List<WiredPortStatus>> interfacePortStatusMap) {
        this.interfacePortStatusMap = interfacePortStatusMap;
    }

    public Map<String, List<WiredPortStatus>> getInterfacePortStatusMap() {
        return interfacePortStatusMap;
    }

    public void setInterfacePortStatusMap(Map<String, List<WiredPortStatus>> interfacePortStatusMap) {
        this.interfacePortStatusMap = interfacePortStatusMap;
    }

    @Override
    public StatusDataType getStatusDataType() {
        return StatusDataType.WIRED_ETHERNET_PORT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof WiredEthernetPortStatusData)) {
            return false;
        }
        WiredEthernetPortStatusData that = (WiredEthernetPortStatusData) o;
        return Objects.equals(getInterfacePortStatusMap(), that.getInterfacePortStatusMap());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInterfacePortStatusMap());
    }

    @Override
    public WiredEthernetPortStatusData clone() {
        WiredEthernetPortStatusData data = (WiredEthernetPortStatusData) super.clone();

        if (interfacePortStatusMap != null) {
            data.interfacePortStatusMap = new HashMap<>(interfacePortStatusMap);
        }
        return data;
    }
}
