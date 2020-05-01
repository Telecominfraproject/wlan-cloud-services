package com.telecominfraproject.wlan.equipment.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

public enum StateSetting {

    disabled(0L),
    enabled(1L),
    
    UNSUPPORTED(-1L);

    private final long id;
    private static final Map<Long, StateSetting> ELEMENTS = new HashMap<>();

    private StateSetting(long id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    public static StateSetting getById(long enumId) {
        if (ELEMENTS.isEmpty()) {
            synchronized (ELEMENTS) {
                if (ELEMENTS.isEmpty()) {
                    //initialize elements map
                    for(StateSetting met : StateSetting.values()) {
                        ELEMENTS.put(met.getId(), met);
                    }
                }
            }
        }
        return ELEMENTS.get(enumId);
    }
    
    @JsonCreator
    public static StateSetting getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, StateSetting.class, UNSUPPORTED);
    }
    
    public static boolean isUnsupported(StateSetting value) {
        return UNSUPPORTED.equals(value);
    }
}
