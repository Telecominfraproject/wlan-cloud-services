package com.telecominfraproject.wlan.equipment.models.events;

import com.telecominfraproject.wlan.equipment.models.Equipment;

public class EquipmentCustomerChangedEvent extends EquipmentChangedEvent {

    private static final long serialVersionUID = 4650302079238674307L;
    private Equipment existingEquipment; // old equipment
    private Equipment equipment; // new configured equipment

    public EquipmentCustomerChangedEvent(Equipment existingEquipment, Equipment equipment) {
        super(equipment);
        setEquipmentChangeType(EquipmentChangeType.CustomerOnly);
        this.setExistingEquipment(existingEquipment);
        this.setEquipment(equipment);
    }

    /**
     * Constructor used by JSON
     */
    @SuppressWarnings("unused")
    private EquipmentCustomerChangedEvent() {
        super();
    }

    public Equipment getExistingEquipment() {
        return existingEquipment;
    }

    public void setExistingEquipment(Equipment existingEquipment) {
        this.existingEquipment = existingEquipment;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }
}
