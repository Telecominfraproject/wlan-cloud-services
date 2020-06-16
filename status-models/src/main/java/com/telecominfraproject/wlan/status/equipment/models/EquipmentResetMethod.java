package com.telecominfraproject.wlan.status.equipment.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

/**
 * Methods to reset equipment.
 * 
 * @author yongli
 *
 */
public enum EquipmentResetMethod {
    /**
     * Do no perform any reset
     */
    NoReset, 
    /**
     * Perform configuration reset.
     * 
     * Configuration DB rebuild, preserve local configuration.
     */
    ConfigReset, 
    /**
     * Perform factory reset.
     * 
     * Full return to factory reset
     */
    FactoryReset,
    /**
     * Unsupported value
     */
    UNSUPPORTED;
    
    @JsonCreator
    public static EquipmentResetMethod getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, EquipmentResetMethod.class, UNSUPPORTED);
    }
    
    public static boolean isUnsupported(EquipmentResetMethod value) {
        return UNSUPPORTED.equals(value);
    }
}
