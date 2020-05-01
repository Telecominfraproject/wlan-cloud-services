package com.telecominfraproject.wlan.equipment.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

public enum TunnelIndicator {

    no(0L),
    primary(1L),
    secondary(2L),
    
    UNSUPPORTED(-1L);

    private final long id;
    private static final Map<Long, TunnelIndicator> ELEMENTS = new HashMap<>();

    private TunnelIndicator(long id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    public static TunnelIndicator getById(long enumId) {
        if (ELEMENTS.isEmpty()) {
            synchronized (ELEMENTS) {
                if (ELEMENTS.isEmpty()) {
                    //initialize elements map
                    for(TunnelIndicator met : TunnelIndicator.values()) {
                        ELEMENTS.put(met.getId(), met);
                    }
                }
            }
        }
        return ELEMENTS.get(enumId);
    }
    
    @JsonCreator
    public static TunnelIndicator getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, TunnelIndicator.class, UNSUPPORTED);
    }
    
    public static boolean isUnsupported(TunnelIndicator value) {
        return UNSUPPORTED.equals(value);
    }
}
