package com.telecominfraproject.wlan.equipment.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

public enum GettingDNS {

    dhcp(0),
    manual(1),
    
    UNSUPPORTED(-1);

    private final int id;
    private static final Map<Integer, GettingDNS> ELEMENTS = new HashMap<>();

    private GettingDNS(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static GettingDNS getById(int enumId) {
        if (ELEMENTS.isEmpty()) {
            synchronized (ELEMENTS) {
                if (ELEMENTS.isEmpty()) {
                    //initialize elements map
                    for(GettingDNS met : GettingDNS.values()) {
                        ELEMENTS.put(met.getId(), met);
                    }
                }
            }
        }
        return ELEMENTS.get(enumId);
    }
    
    @JsonCreator
    public static GettingDNS getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, GettingDNS.class, UNSUPPORTED);
    }
    
    public static boolean isUnsupported(GettingDNS value) {
        return UNSUPPORTED.equals(value);
    }
}
