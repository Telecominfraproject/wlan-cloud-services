package com.telecominfraproject.wlan.equipment.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

public enum DeviceMode {

    standaloneAP(0),
    managedAP(1),
    gatewaywithAP(2),
    gatewayOnly(3),
    
    UNSUPPORTED(-1);

    private final int id;
    private static final Map<Integer, DeviceMode> ELEMENTS = new HashMap<>();

    private DeviceMode(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static DeviceMode getById(int enumId) {
        if (ELEMENTS.isEmpty()) {
            synchronized (ELEMENTS) {
                if (ELEMENTS.isEmpty()) {
                    //initialize elements map
                    for(DeviceMode met : DeviceMode.values()) {
                        ELEMENTS.put(met.getId(), met);
                    }
                }
            }
        }
        return ELEMENTS.get(enumId);
    }
    
    @JsonCreator
    public static DeviceMode getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, DeviceMode.class, null);
    }
    
    public static boolean isUnsupported(DeviceMode value) {
        return null == value;
    }
}
