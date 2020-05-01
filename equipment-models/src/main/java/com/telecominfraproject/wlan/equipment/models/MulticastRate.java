package com.telecominfraproject.wlan.equipment.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

public enum MulticastRate {

    auto(0L),
    rate6mbps(1L),
    rate9mbps(2L),
    rate12mbps(3L),
    rate18mbps(4L),
    rate24mbps(5L),
    rate36mbps(6L),
    rate48mbps(7L),
    rate54mbps(8L),
    
    UNSUPPORTED(-1L);
    
    private final long id;
    private static final Map<Long, MulticastRate> ELEMENTS = new HashMap<>();

    private MulticastRate(long id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    public static MulticastRate getById(long enumId) {
        if (ELEMENTS.isEmpty()) {
            synchronized (ELEMENTS) {
                if (ELEMENTS.isEmpty()) {
                    //initialize elements map
                    for(MulticastRate met : MulticastRate.values()) {
                        ELEMENTS.put(met.getId(), met);
                    }
                }
            }
        }
        return ELEMENTS.get(enumId);
    }
    
    @JsonCreator
    public static MulticastRate getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, MulticastRate.class, UNSUPPORTED);
    }
    
    public static boolean isUnsupported(MulticastRate value) {
        return UNSUPPORTED.equals(value);
    }
}
