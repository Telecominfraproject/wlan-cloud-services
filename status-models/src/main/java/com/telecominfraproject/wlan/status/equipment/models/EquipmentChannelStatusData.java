package com.telecominfraproject.wlan.status.equipment.models;

import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;

import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.status.models.StatusDataType;
import com.telecominfraproject.wlan.status.models.StatusDetails;

public class EquipmentChannelStatusData extends StatusDetails {
	private static final long serialVersionUID = 8570180206588083190L;

    private Map<RadioType, Integer> channelStatusDataMap = new EnumMap<>(RadioType.class);


    public EquipmentChannelStatusData()
    {
        super();
    }
    
    @Override
    public StatusDataType getStatusDataType() {
    	return StatusDataType.RADIO_CHANNEL;
    }
    
    public EquipmentChannelStatusData(EquipmentChannelStatusData data)
    {
        if (data !=null) {
            this.channelStatusDataMap.putAll(data.channelStatusDataMap);
        }
    }
    
    public Map<RadioType, Integer> getChannelNumberStatusDataMap() {
        return channelStatusDataMap;
    }

    public void setChannelNumberStatusDataMap(Map<RadioType, Integer> channelNumberStatusDataMap) {
        this.channelStatusDataMap = channelNumberStatusDataMap;
    }

    @Override
    public EquipmentChannelStatusData clone() {
        EquipmentChannelStatusData result = (EquipmentChannelStatusData) super.clone();
        if (getChannelNumberStatusDataMap() != null) {
            result.setChannelNumberStatusDataMap(new EnumMap<>(RadioType.class));
            
            this.channelStatusDataMap.forEach((k, v) -> {
            	result.channelStatusDataMap.put(k, v);
            });
        }
        return result;

    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof EquipmentChannelStatusData)) {
            return false;
        }
        EquipmentChannelStatusData other = (EquipmentChannelStatusData) obj;
        return Objects.equals(channelStatusDataMap, other.channelStatusDataMap);
    }

    @Override
    public int hashCode() {
    	return Objects.hash(channelStatusDataMap);
    	
    }

}
