package com.telecominfraproject.wlan.equipment.models;

import java.util.EnumMap;
import java.util.Map;

import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

public class EquipmentChannelsUpdateRequest extends BaseJsonModel {

    private static final long serialVersionUID = -1002757535616506599L;
	private long equipmentId;
	private Map<RadioType, Integer> primaryChannels = new EnumMap<>(RadioType.class);
	private Map<RadioType, Integer> backupChannels = new EnumMap<>(RadioType.class);
    private Map<RadioType, Boolean> autoChannelSelections = new EnumMap<>(RadioType.class);
    
    public long getEquipmentId() {
        return equipmentId;
    }
    
    public void setEquipmentId(long equipmentId) {
        this.equipmentId = equipmentId;
    }
    
    public Map<RadioType, Integer> getPrimaryChannels() {
        return primaryChannels;
    }
    
    public void setPrimaryChannels(Map<RadioType, Integer> primaryChannels) {
        this.primaryChannels = primaryChannels;
    }

    public Map<RadioType, Integer> getBackupChannels() {
        return backupChannels;
    }

    public void setBackupChannels(Map<RadioType, Integer> backupChannels) {
        this.backupChannels = backupChannels;
    }

    public Map<RadioType, Boolean> getAutoChannelSelections() {
        return autoChannelSelections;
    }

    public void setAutoChannelSelections(Map<RadioType, Boolean> autoChannelSelections) {
        this.autoChannelSelections = autoChannelSelections;
    }
    
    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }

        return false;
    }

}
