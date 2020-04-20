package com.telecominfraproject.wlan.equipment.models.events;

import com.telecominfraproject.wlan.equipment.models.Equipment;
import com.telecominfraproject.wlan.systemevent.models.CustomerEvent;

/**
 * @author dtoptygin
 *
 */
public class EquipmentRemovedEvent extends CustomerEvent<Equipment> {
    private static final long serialVersionUID = 7142208488887559985L;

    public EquipmentRemovedEvent(Equipment equipment){
        super(equipment.getCustomerId(), equipment.getLastModifiedTimestamp(), equipment);
    }
    
    /**
     * Constructor used by JSON
     */
    public EquipmentRemovedEvent() {
        super(0, 0, null);
    }
    
}
