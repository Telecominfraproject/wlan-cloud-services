/**
 *
 */
package com.telecominfraproject.wlan.equipmentgateway.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

/**
 * Interface the capture is performed
 *
 * @author yongli
 *
 */
public enum CaptureInterface {
    ETHERNET(0, true), RADIO2G(1, true), RADIO5G(2, true), INTERNAL(3, false),

    UNSUPPORTED(-1, false);

    /**
     * Identity value
     */
    private int id;

    /**
     * Determines if this interface may be used for packet capture.
     */
    private boolean supportPacketCapture;
    static Map<Integer, CaptureInterface> ELEMENTS = new HashMap<>();

    private CaptureInterface(int id, boolean supportPacketCapture) {
        this.id = id;
        this.supportPacketCapture = supportPacketCapture;
    }

    public int getId() {
        return this.id;
    }

    public boolean isSupportPacketCapture() {
        return supportPacketCapture;
    }

    public static CaptureInterface getById(int enumId) {
        if (ELEMENTS.isEmpty()) {
            // initialize elements map
            for (CaptureInterface met : CaptureInterface.values()) {
                ELEMENTS.put(met.getId(), met);
            }
        }

        return ELEMENTS.get(enumId);
    }

    @JsonCreator
    public static CaptureInterface getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, CaptureInterface.class, UNSUPPORTED);
    }

    public static boolean isUnsupported(CaptureInterface value) {
        return UNSUPPORTED.equals(value);
    }
}
