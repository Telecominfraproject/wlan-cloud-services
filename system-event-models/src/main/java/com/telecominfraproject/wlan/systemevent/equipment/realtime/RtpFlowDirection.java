package com.telecominfraproject.wlan.systemevent.equipment.realtime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

public enum RtpFlowDirection {

    UPSTREAM, DOWNSTREAM,

    UNSUPPORTED;

    @JsonCreator
    public static RtpFlowDirection getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, RtpFlowDirection.class, UNSUPPORTED);
    }

    public static boolean isUnsupported(RtpFlowDirection value) {
        return UNSUPPORTED.equals(value);
    }
}
