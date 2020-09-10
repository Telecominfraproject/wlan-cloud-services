package com.telecominfraproject.wlan.equipment.models.events;

import com.telecominfraproject.wlan.equipment.models.Equipment;
import com.telecominfraproject.wlan.systemevent.models.EquipmentEventWithPayload;

/**
 * @author dtoptygin
 *
 */
public class EquipmentAddedEvent extends EquipmentEventWithPayload<Equipment> {
    private static final long serialVersionUID = 7142208487917559985L;

    public EquipmentAddedEvent(Equipment equipment){
        super(equipment.getCustomerId(), equipment.getId(), equipment.getLastModifiedTimestamp(), equipment);
    }
    
    /**
     * Constructor used by JSON
     */
    public EquipmentAddedEvent() {
        super(0, 0, 0, null);
    }
    
}
