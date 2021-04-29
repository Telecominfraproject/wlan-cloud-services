package com.telecominfraproject.wlan.equipment.models;

import java.util.EnumMap;
import java.util.Map;

import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

public class EquipmentCellSizeAttributesUpdateRequest extends BaseJsonModel {
    private static final long serialVersionUID = 4332550605030562832L;
	private long equipmentId;
	private Map<RadioType, CellSizeAttributes> cellSizeAttributesMap = new EnumMap<>(RadioType.class);
    private Map<RadioType, Boolean> autoCellSizeSelections = new EnumMap<>(RadioType.class);
    
    public long getEquipmentId() {
        return equipmentId;
    }
    
    public void setEquipmentId(long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public Map<RadioType, CellSizeAttributes> getCellSizeAttributesMap() {
        return cellSizeAttributesMap;
    }

    public void setCellSizeAttributesMap(Map<RadioType, CellSizeAttributes> cellSizeAttributesMap) {
        this.cellSizeAttributesMap = cellSizeAttributesMap;
    }

    public Map<RadioType, Boolean> getAutoCellSizeSelections() {
        return autoCellSizeSelections;
    }
    public void setAutoCellSizeSelections(Map<RadioType, Boolean> autoCellSizeSelections) {
        this.autoCellSizeSelections = autoCellSizeSelections;
    }
    
    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (cellSizeAttributesMap != null && !cellSizeAttributesMap.isEmpty()) {
            if (cellSizeAttributesMap.get(RadioType.UNSUPPORTED) != null) {
                return true;
            }
        }
        
        return false;
    }

}
