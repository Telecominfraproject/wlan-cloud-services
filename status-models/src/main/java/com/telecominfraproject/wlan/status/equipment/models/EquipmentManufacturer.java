
package com.telecominfraproject.wlan.status.equipment.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

public enum EquipmentManufacturer {

    CIG("CIG", "CIG"), 
    CX("Compex", "CX"), 
    EC("Edge-Core", "EC"), 
    LO("Light-On", "LO"), 
    SC("Sercomm", "SC"),
    PW("Proware (TP-Link)", "PW"), 
    WNC("WNC", "WNC"),
    WT("WallysTech", "WT"),
    
    UNSUPPORTED("UNSUPPORTED", "UNSUPPORTED");

    private String manufacturerName;
    private String abbreviation;

    public String getAbbreviation() {
        return abbreviation;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    private EquipmentManufacturer(String manufacturerName, String abbreviation) {
        this.manufacturerName = manufacturerName;
        this.abbreviation = abbreviation;
    }

    @JsonCreator
    public static EquipmentManufacturer getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, EquipmentManufacturer.class, UNSUPPORTED);
    }

    public static boolean isUnsupported(EquipmentManufacturer value) {
        return (UNSUPPORTED.equals(value));
    }
}
