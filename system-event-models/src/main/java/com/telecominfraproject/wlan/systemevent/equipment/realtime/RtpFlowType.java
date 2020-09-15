package com.telecominfraproject.wlan.systemevent.equipment.realtime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

public enum RtpFlowType {

    VOICE, VIDEO,

    UNSUPPORTED;

    @JsonCreator
    public static RtpFlowType getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, RtpFlowType.class, UNSUPPORTED);
    }

    public static boolean isUnsupported(RtpFlowType value) {
        return UNSUPPORTED.equals(value);
    }
}
