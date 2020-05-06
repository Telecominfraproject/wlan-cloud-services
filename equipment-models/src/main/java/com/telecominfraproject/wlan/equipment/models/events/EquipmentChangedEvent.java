package com.telecominfraproject.wlan.equipment.models.events;

import com.telecominfraproject.wlan.equipment.models.Equipment;
import com.telecominfraproject.wlan.systemevent.models.EquipmentEvent;

/**
 * @author dtoptygin
 *
 */
public class EquipmentChangedEvent extends EquipmentEvent<Equipment> {
    private static final long serialVersionUID = 7142209997917559985L;

    public EquipmentChangedEvent(Equipment equipment){
        super(equipment.getCustomerId(), equipment.getId(), equipment.getLastModifiedTimestamp(), equipment);
    }
    
    /**
     * Constructor used by JSON
     */
    public EquipmentChangedEvent() {
        super(0, 0, 0, null);
    }
    
}
