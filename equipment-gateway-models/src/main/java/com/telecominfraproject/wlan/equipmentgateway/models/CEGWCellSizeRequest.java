package com.telecominfraproject.wlan.equipmentgateway.models;

import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.equipment.models.CellSizeAttributes;

public class CEGWCellSizeRequest extends EquipmentCommand {
    private static final long serialVersionUID = 1204349463504941253L;
    private Map<RadioType, CellSizeAttributes> cellSizeAttributesMap = new EnumMap<>(RadioType.class);

    protected CEGWCellSizeRequest() {
        // serial
    }

    public CEGWCellSizeRequest(String inventoryId, long equipmentId, Map<RadioType, CellSizeAttributes> cellSizeAttributesMap) {
        super(CEGWCommandType.CellSizeRequest, inventoryId, equipmentId);
        this.cellSizeAttributesMap = cellSizeAttributesMap;
    }
    
    @JsonIgnore
    public CellSizeAttributes getCellSize(RadioType radioType) {
        return cellSizeAttributesMap.get(radioType);
    }
    
    @JsonIgnore
    public void setCellSize(RadioType radioType, CellSizeAttributes cellSizeAttributes) {
        this.cellSizeAttributesMap.put(radioType, cellSizeAttributes);
    }
    
    public Map<RadioType, CellSizeAttributes> getCellSizeAttributesMap() {
        return cellSizeAttributesMap;
    }

    public void setCellSizeAttributesMap(Map<RadioType, CellSizeAttributes> cellSizeAttributesMap) {
        this.cellSizeAttributesMap = cellSizeAttributesMap;
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
            if (hasUnsupportedValue(cellSizeAttributesMap)) {
                return true;
            }
        }
        
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(cellSizeAttributesMap);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        CEGWCellSizeRequest other = (CEGWCellSizeRequest) obj;
        return Objects.equals(cellSizeAttributesMap, other.cellSizeAttributesMap);
    }

}
