package com.telecominfraproject.wlan.equipment.models.events;

import java.util.Map;

import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasLocationId;
import com.telecominfraproject.wlan.equipment.models.Equipment;
import com.telecominfraproject.wlan.systemevent.models.EquipmentEventWithPayload;

/**
 * @author dtoptygin
 *
 */
public class EquipmentChangedEvent extends EquipmentEventWithPayload<Equipment> implements HasLocationId {
    private static final long serialVersionUID = 7142209997917559985L;
    private EquipmentChangeType equipmentChangeType;
    private Map<RadioType, Integer> newPrimaryChannels;
    private Map<RadioType, Integer> newBackupChannels;

    public EquipmentChangedEvent(Equipment equipment){
        super(equipment.getCustomerId(), equipment.getId(), equipment.getLastModifiedTimestamp(), equipment);
        equipmentChangeType = EquipmentChangeType.All;
    }
    
    public EquipmentChangedEvent(Equipment equipment, Map<RadioType, Integer> primaryChannels, Map<RadioType, Integer> backupChannels){
        super(equipment.getCustomerId(), equipment.getId(), equipment.getLastModifiedTimestamp(), equipment);
        this.equipmentChangeType = EquipmentChangeType.ChannelsOnly;
        this.newPrimaryChannels = primaryChannels;
        this.newBackupChannels = backupChannels;
    }
    
    @Override
    public long getLocationId() {
        if(getPayload() !=null) {
            return getPayload().getLocationId();
        }
        
        return 0;
    }

    /**
     * Constructor used by JSON
     */
    public EquipmentChangedEvent() {
        super(0, 0, 0, null);
    }

    public EquipmentChangeType getEquipmentChangeType() {
        return equipmentChangeType;
    }

    public void setEquipmentChangeType(EquipmentChangeType equipmentChangeType) {
        this.equipmentChangeType = equipmentChangeType;
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
