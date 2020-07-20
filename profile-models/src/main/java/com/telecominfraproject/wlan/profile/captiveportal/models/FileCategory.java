package com.telecominfraproject.wlan.profile.captiveportal.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

/**
 * TODO: this may need to be moved into a separate file management service
 */
public enum FileCategory 
{
    CaptivePortalLogo,
    CaptivePortalBackground,
    ExternalPolicyConfiguration,
    UsernamePasswordList,
    DeviceMacBlockList,
    DoNotSteerClientList,
    
    UNSUPPORTED;
    
    @JsonCreator
    public static FileCategory getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, FileCategory.class, UNSUPPORTED);
    }
    
    public static boolean isUnsupported(FileCategory value) {
        return UNSUPPORTED.equals(value);
    }
}
