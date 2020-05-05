package com.telecominfraproject.wlan.profile.mesh.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

/**
 * SSID type supported for deployment.
 * 
 * @author yongli
 *
 */
public enum DeploymentSsidType {
    PSK, GUEST, UNSUPPORTED;

    @JsonCreator
    public static DeploymentSsidType getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, DeploymentSsidType.class, DeploymentSsidType.UNSUPPORTED);
    }

    static boolean isUnsupported(DeploymentSsidType value) {
        return (UNSUPPORTED.equals(value));
    }
}
