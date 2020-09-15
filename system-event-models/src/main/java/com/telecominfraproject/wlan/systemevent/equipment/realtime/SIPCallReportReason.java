package com.telecominfraproject.wlan.systemevent.equipment.realtime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

/**
 * @author yongli
 *
 */
public enum SIPCallReportReason {

    /**
     * Report of a roaming away from an existing AP. Report message contains the
     * statistics gathered on the existing AP.
     */
    ROAMED_FROM,
    /**
     * Report of a roaming into a new AP. Report message contains WiFi
     * association id only.
     */
    ROAMED_TO,
    /**
     * Report the MOS from SIP publish. Report message contains the MOS from SIP
     * publish message
     */
    GOT_PUBLISH,

    /**
     * Unsupported value read
     */
    UNSUPPORTED;

    @JsonCreator
    public static SIPCallReportReason getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, SIPCallReportReason.class, UNSUPPORTED);
    }

    public static boolean isUnsupported(SIPCallReportReason value) {
        return UNSUPPORTED.equals(value);
    }
}