package com.telecominfraproject.wlan.equipment.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

public enum BestAPSteerType {
    both(0L), loadBalanceOnly(1L), linkQualityOnly(2L),

    UNSUPPORTED(-1L);

    private final long id;
    private static final Map<Long, BestAPSteerType> ELEMENTS = new HashMap<>();

    private BestAPSteerType(long id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    public static BestAPSteerType getById(long enumId) {
        if (ELEMENTS.isEmpty()) {
            synchronized (ELEMENTS) {
                if (ELEMENTS.isEmpty()) {
                    // initialize elements map
                    for (BestAPSteerType met : BestAPSteerType.values()) {
                        ELEMENTS.put(met.getId(), met);
                    }
                }
            }
        }
        return ELEMENTS.get(enumId);
    }

    @JsonCreator
    public static BestAPSteerType getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, BestAPSteerType.class, UNSUPPORTED);
    }

    public static boolean isUnsupported(BestAPSteerType value) {
        return UNSUPPORTED.equals(value);
    }

}
