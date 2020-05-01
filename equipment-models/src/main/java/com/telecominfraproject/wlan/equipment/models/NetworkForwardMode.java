package com.telecominfraproject.wlan.equipment.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

/**
 * Forwarding Mode.
 * 
 * @author yongli
 *
 */
public enum NetworkForwardMode {
    BRIDGE(0), NAT(1), UNSUPPORTED(-1);

    private final int id;
    private static final Map<Integer, NetworkForwardMode> ELEMENTS = new HashMap<>();

    NetworkForwardMode(int id) {
        this.id = id;
    }

    /**
     * @return the id
     */
    public int getId() {
        return id;
    }

    public static NetworkForwardMode getById(int enumId) {
        if (ELEMENTS.isEmpty()) {
            synchronized (ELEMENTS) {
                if (ELEMENTS.isEmpty()) {
                    // initialize elements map
                    for (NetworkForwardMode met : NetworkForwardMode.values()) {
                        ELEMENTS.put(met.getId(), met);
                    }
                }
            }
        }
        return ELEMENTS.get(enumId);
    }

    @JsonCreator
    public static NetworkForwardMode getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, NetworkForwardMode.class, UNSUPPORTED);
    }

    public static boolean isUnsupported(NetworkForwardMode value) {
        return UNSUPPORTED.equals(value);
    }
}
