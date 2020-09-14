package com.telecominfraproject.wlan.systemevent.equipment.realtime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

public enum SipCallStopReason 
{
    BYE_OK,
    DROPPED,
    
    UNSUPPORTED;
    
    @JsonCreator
    public static SipCallStopReason getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, SipCallStopReason.class, UNSUPPORTED);
    }
    
    public static boolean isUnsupported(SipCallStopReason value) {
        return UNSUPPORTED.equals(value);
    }
}
