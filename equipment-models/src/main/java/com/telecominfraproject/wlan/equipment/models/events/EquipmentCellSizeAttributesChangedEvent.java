package com.telecominfraproject.wlan.equipment.models.events;

import java.util.Map;

import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.equipment.models.CellSizeAttributes;
import com.telecominfraproject.wlan.equipment.models.Equipment;

public class EquipmentCellSizeAttributesChangedEvent extends EquipmentChangedEvent {
    private static final long serialVersionUID = -5336592950746293096L;
    private Map<RadioType, CellSizeAttributes> cellSizeAttributesMap;

    public EquipmentCellSizeAttributesChangedEvent(Equipment equipment, Map<RadioType, CellSizeAttributes> cellSizeAttributesMap){
        super(equipment);
        setEquipmentChangeType(EquipmentChangeType.CellSizeAttributesOnly);
        this.cellSizeAttributesMap = cellSizeAttributesMap;
    }
    
    /**
     * Constructor used by JSON
     */
    private EquipmentCellSizeAttributesChangedEvent() {
        super();
    }

    public Map<RadioType, CellSizeAttributes> getCellSizeAttributesMap() {
        return cellSizeAttributesMap;
    }

    public void setCellSizeAttributesMap(Map<RadioType, CellSizeAttributes> cellSizeAttributesMap) {
        this.cellSizeAttributesMap = cellSizeAttributesMap;
    }

}
