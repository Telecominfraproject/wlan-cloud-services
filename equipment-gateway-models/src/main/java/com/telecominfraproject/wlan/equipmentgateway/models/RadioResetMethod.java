/**
 *
 */
package com.telecominfraproject.wlan.equipmentgateway.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

/**
 * Methods to reset radio
 * 
 * @author ekeddy
 *
 */
public enum RadioResetMethod {
    /**
     * Quick reset
     */
    init,

    /**
     * Reset with reload of micro-code.
     */
    reload, UNSUPPORTED;

    @JsonCreator
    public static RadioResetMethod getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, RadioResetMethod.class, UNSUPPORTED);
    }

    public static boolean isUnsupported(RadioResetMethod value) {
        return UNSUPPORTED.equals(value);
    }
}
