package com.telecominfraproject.wlan.status.equipment.models;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.status.models.StatusDataType;
import com.telecominfraproject.wlan.status.models.StatusDetails;

public class EquipmentChannelStatusData extends StatusDetails {
    private static final long serialVersionUID = 470569467119609438L;

    private Map<RadioType, Integer> channelNumberStatusDataMap = new EnumMap<>(RadioType.class);


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
            this.channelNumberStatusDataMap.putAll(data.channelNumberStatusDataMap);
        }
    }
    
    public Map<RadioType, Integer> getChannelNumberStatusDataMap() {
        return channelNumberStatusDataMap;
    }

    public void setChannelNumberStatusDataMap(Map<RadioType, Integer> channelNumberStatusDataMap) {
        this.channelNumberStatusDataMap = channelNumberStatusDataMap;
    }

    @Override
    public EquipmentChannelStatusData clone() {
        EquipmentChannelStatusData result = (EquipmentChannelStatusData) super.clone();
        if (getChannelNumberStatusDataMap() != null) {
            result.setChannelNumberStatusDataMap(new EnumMap<>(RadioType.class));
            
            this.channelNumberStatusDataMap.forEach((k, v) -> {
            	result.channelNumberStatusDataMap.put(k, v);
            });
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        EquipmentChannelStatusData other = (EquipmentChannelStatusData) obj;
        return Objects.equals(channelNumberStatusDataMap, other.channelNumberStatusDataMap);
    }

    @Override
    public int hashCode() {
    	return Objects.hash(channelNumberStatusDataMap);
    	
    }

}
