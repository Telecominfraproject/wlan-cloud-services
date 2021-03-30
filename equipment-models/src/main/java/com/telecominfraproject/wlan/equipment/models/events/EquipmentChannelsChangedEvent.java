package com.telecominfraproject.wlan.equipment.models.events;

import java.util.Map;

import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.equipment.models.Equipment;

public class EquipmentChannelsChangedEvent extends EquipmentChangedEvent {
    private static final long serialVersionUID = -6113044717280041145L;
    private Map<RadioType, Integer> newPrimaryChannels;
    private Map<RadioType, Integer> newBackupChannels;

    public EquipmentChannelsChangedEvent(Equipment equipment, Map<RadioType, Integer> primaryChannels, Map<RadioType, Integer> backupChannels){
        super(equipment);
        setEquipmentChangeType(EquipmentChangeType.ChannelsOnly);
        this.newPrimaryChannels = primaryChannels;
        this.newBackupChannels = backupChannels;
    }
    
    /**
     * Constructor used by JSON
     */
    private EquipmentChannelsChangedEvent() {
        super();
    }

    public Map<RadioType, Integer> getNewPrimaryChannels() {
        return newPrimaryChannels;
    }

    public void setNewPrimaryChannels(Map<RadioType, Integer> primaryChannels) {
        this.newPrimaryChannels = primaryChannels;
    }

    public Map<RadioType, Integer> getNewBackupChannels() {
        return newBackupChannels;
    }

    public void setNewBackupChannels(Map<RadioType, Integer> backupChannels) {
        this.newBackupChannels = backupChannels;
    }
    
}
