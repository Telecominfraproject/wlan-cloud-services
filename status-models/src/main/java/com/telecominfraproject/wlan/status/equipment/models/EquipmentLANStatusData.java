package com.telecominfraproject.wlan.status.equipment.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import com.telecominfraproject.wlan.status.models.StatusDataType;
import com.telecominfraproject.wlan.status.models.StatusDetails;

/**
 * Equipment LAN status data
 * 
 * @author yongli
 *
 */
public class EquipmentLANStatusData extends StatusDetails {
    /**
     * serial number
     */
    private static final long serialVersionUID = -7980523350484133341L;

    /*
     * This is a map of the VLAN_ID to the VLAN's lan status.
     */
    private Map<Integer, VLANStatusData> vlanStatusDataMap;


    public EquipmentLANStatusData()
    {
        // serialization
        super();
        
        // We'll just populate an empty one
        this.vlanStatusDataMap = new HashMap<>();
    }
    
    @Override
    public StatusDataType getStatusDataType() {
    	return StatusDataType.LANINFO;
    }
    
    public EquipmentLANStatusData(EquipmentLANStatusData data)
    {
        this();
        
        if(data !=null && data.vlanStatusDataMap != null)
        {
            this.vlanStatusDataMap = new HashMap<>(data.vlanStatusDataMap);
        }
        else
        {
            this.vlanStatusDataMap.clear();
        }
    }
    
    public Map<Integer, VLANStatusData> getVlanStatusDataMap() {
        return vlanStatusDataMap;
    }

    public void setVlanStatusDataMap(Map<Integer, VLANStatusData> vlanStatusDataMap) {
        this.vlanStatusDataMap = vlanStatusDataMap;
    }

    @Override
    public EquipmentLANStatusData clone() {
        EquipmentLANStatusData result = (EquipmentLANStatusData) super.clone();
        if (null != getVlanStatusDataMap()) {
            result.setVlanStatusDataMap(new HashMap<>());

            for (Entry<Integer, VLANStatusData> entry : getVlanStatusDataMap().entrySet()) {
                result.getVlanStatusDataMap().put(entry.getKey(), entry.getValue().clone());
            }
        }
        return result;

    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof EquipmentLANStatusData)) {
            return false;
        }
        EquipmentLANStatusData other = (EquipmentLANStatusData) obj;
        if (vlanStatusDataMap == null) {
            if (other.vlanStatusDataMap != null) {
                return false;
            }
        } else if (!vlanStatusDataMap.equals(other.vlanStatusDataMap)) {
            return false;
        }
        return true;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((vlanStatusDataMap == null) ? 0 : vlanStatusDataMap.hashCode());
        return result;
    }

}
