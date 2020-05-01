package com.telecominfraproject.wlan.equipment.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

public enum RadioMode {

    modeN(0),
    modeAC(1),
    modeGN(2),
    
    UNSUPPORTED(-1);

    private final int id;
    private static final Map<Integer, RadioMode> ELEMENTS = new HashMap<>();

    private RadioMode(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static RadioMode getById(int enumId) {
        if (ELEMENTS.isEmpty()) {
            synchronized (ELEMENTS) {
                if (ELEMENTS.isEmpty()) {
                    //initialize elements map
                    for(RadioMode met : RadioMode.values()) {
                        ELEMENTS.put(met.getId(), met);
                    }
                }
            }
        }
        return ELEMENTS.get(enumId);
    }
    
    @JsonCreator
    public static RadioMode getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, RadioMode.class, UNSUPPORTED);
    }
    
    public static boolean isUnsupported(RadioMode value) {
        return UNSUPPORTED.equals(value);
    }

}
