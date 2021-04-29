package com.telecominfraproject.wlan.equipment.models.events;

import com.telecominfraproject.wlan.core.model.json.interfaces.HasLocationId;
import com.telecominfraproject.wlan.equipment.models.Equipment;
import com.telecominfraproject.wlan.systemevent.models.EquipmentEventWithPayload;

/**
 * @author dtoptygin
 *
 */
public class EquipmentRemovedEvent extends EquipmentEventWithPayload<Equipment> implements HasLocationId {
    private static final long serialVersionUID = 7142208488887559985L;

    public EquipmentRemovedEvent(Equipment equipment){
        super(equipment.getCustomerId(), equipment.getId(), equipment);
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
    public EquipmentRemovedEvent() {
        super(0, 0, 0, null);
    }
    
}
