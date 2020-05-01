package com.telecominfraproject.wlan.equipment.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

public enum AntennaType {

    OMNI(0),
    OAP30_DIRECTIONAL(1),
    
    UNSUPPORTED(-1);

    private final int id;
    private static final Map<Integer, AntennaType> ELEMENTS = new HashMap<>();

    private AntennaType(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static AntennaType getById(int enumId) {
        if (ELEMENTS.isEmpty()) {
            synchronized (ELEMENTS) {
                if (ELEMENTS.isEmpty()) {
                    //initialize elements map
                    for(AntennaType met : AntennaType.values()) {
                        ELEMENTS.put(met.getId(), met);
                    }
                }
            }
        }
        return ELEMENTS.get(enumId);
    }
    @JsonCreator
    public static AntennaType getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, AntennaType.class, UNSUPPORTED);
    }
    
    public static boolean isUnsupported(AntennaType value) {
        return UNSUPPORTED.equals(value);
    }
}
