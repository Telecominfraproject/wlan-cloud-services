package com.telecominfraproject.wlan.equipment.models.events;

import com.telecominfraproject.wlan.equipment.models.Equipment;

public class EquipmentBlinkLEDsEvent extends EquipmentChangedEvent {
    private static final long serialVersionUID = 5222048279956123654L;
    public EquipmentBlinkLEDsEvent(Equipment equipment){
        super(equipment);
        setEquipmentChangeType(EquipmentChangeType.blinkLEDs);
    }
    
    /**
     * Constructor used by JSON
     */
    private EquipmentBlinkLEDsEvent() {
        super();
    }
}
