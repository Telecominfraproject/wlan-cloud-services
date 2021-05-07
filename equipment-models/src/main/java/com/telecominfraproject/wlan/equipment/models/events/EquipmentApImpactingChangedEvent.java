package com.telecominfraproject.wlan.equipment.models.events;

import com.telecominfraproject.wlan.equipment.models.Equipment;

public class EquipmentApImpactingChangedEvent extends EquipmentChangedEvent {
    private static final long serialVersionUID = 5222048279956123654L;

    public EquipmentApImpactingChangedEvent(Equipment equipment){
        super(equipment);
        setEquipmentChangeType(EquipmentChangeType.ApImpacting);
    }
    
    /**
     * Constructor used by JSON
     */
    private EquipmentApImpactingChangedEvent() {
        super();
    }
}
