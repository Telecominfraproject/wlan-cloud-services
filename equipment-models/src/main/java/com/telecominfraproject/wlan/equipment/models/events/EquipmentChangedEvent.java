package com.telecominfraproject.wlan.equipment.models.events;

import com.telecominfraproject.wlan.core.model.json.interfaces.HasLocationId;
import com.telecominfraproject.wlan.equipment.models.Equipment;
import com.telecominfraproject.wlan.systemevent.models.EquipmentEventWithPayload;

public class EquipmentChangedEvent extends EquipmentEventWithPayload<Equipment> implements HasLocationId {
    private static final long serialVersionUID = 1763876722436732538L;
    private EquipmentChangeType equipmentChangeType;

    public EquipmentChangedEvent(Equipment equipment){
        super(equipment.getCustomerId(), equipment.getId(), equipment.getLastModifiedTimestamp(), equipment);
        equipmentChangeType = EquipmentChangeType.All;
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
    
}
