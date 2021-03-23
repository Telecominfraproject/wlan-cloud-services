
package com.telecominfraproject.wlan.status.equipment.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

public enum EquipmentManufacturer {

    CIG("CIG"), 
    CX("Compex"), 
    EC("Edge-Core"), 
    LO("Light-On"), 
    LS("Linksys"),
    SC("Sercomm"),
    PW("Proware (TP-Link)"), 
    WNC("WNC"),
    WT("WallysTech"),
    UNKNOWN("unknown"),
        
    UNSUPPORTED("UNSUPPORTED");

    private String manufacturerName;

    public String getManufacturerName() {
        return manufacturerName;
    }

    private EquipmentManufacturer(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    @JsonCreator
    public static EquipmentManufacturer getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, EquipmentManufacturer.class, UNSUPPORTED);
    }
    
    public static EquipmentManufacturer getByManufacturer(String manufacturer) {
        
        for (EquipmentManufacturer equipmentMfg : EquipmentManufacturer.values()) {
            if (equipmentMfg.getManufacturerName().equals(manufacturer)) {
                return equipmentMfg;
            }
        }

        return EquipmentManufacturer.UNSUPPORTED;
    }

    public static boolean isUnsupported(EquipmentManufacturer value) {
        return (UNSUPPORTED.equals(value));
    }
}
