package com.telecominfraproject.wlan.equipment.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

public enum GettingIP {

    dhcp(0),
    manual(1),
    
    UNSUPPORTED(-1);

    private final int id;
    private static final Map<Integer, GettingIP> ELEMENTS = new HashMap<>();

    private GettingIP(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static GettingIP getById(int enumId) {
        if (ELEMENTS.isEmpty()) {
            synchronized (ELEMENTS) {
                if (ELEMENTS.isEmpty()) {
                    //initialize elements map
                    for(GettingIP met : GettingIP.values()) {
                        ELEMENTS.put(met.getId(), met);
                    }
                }
            }
        }
        return ELEMENTS.get(enumId);
    }
    
    @JsonCreator
    public static GettingIP getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, GettingIP.class, UNSUPPORTED);
    }
    
    public static boolean isUnsupported(GettingIP value) {
        return UNSUPPORTED.equals(value);
    }
}
