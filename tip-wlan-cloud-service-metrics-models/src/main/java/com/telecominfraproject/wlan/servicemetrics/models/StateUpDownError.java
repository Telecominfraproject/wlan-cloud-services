package com.telecominfraproject.wlan.servicemetrics.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

public enum StateUpDownError {

    disabled(0L), enabled(1L),
    /**
     * This state means it wasn't possible to determine whether the state is up
     * or down (ie: we couldn't launch our script or something).
     */
    error(2L), 
    /**
     * Unsupported value
     */
    UNSUPPORTED(-1);

    private final long id;
    private static final Map<Long, StateUpDownError> ELEMENTS = new HashMap<>();

    private StateUpDownError(long id) {
        this.id = id;
    }

    public long getId() {
        return this.id;
    }

    public static StateUpDownError getById(long enumId) {
        if (ELEMENTS.isEmpty()) {
            synchronized (ELEMENTS) {
                if (ELEMENTS.isEmpty()) {
                    // initialize elements map
                    for (StateUpDownError met : StateUpDownError.values()) {
                        ELEMENTS.put(met.getId(), met);
                    }
                }
            }
        }
        return ELEMENTS.get(enumId);
    }

    @JsonCreator
    public static StateUpDownError getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, StateUpDownError.class, UNSUPPORTED);
    }
    
    public static boolean isUnsupported(StateUpDownError value) {
        return UNSUPPORTED.equals(value);
    }
}
