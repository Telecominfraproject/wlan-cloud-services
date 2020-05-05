package com.telecominfraproject.wlan.profile.captiveportal.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

/**
 * Describes the authentication options available for RADIUS.
 *
 */
public enum RadiusAuthenticationMethod {

    PAP(1),
    CHAP(2),
    MSCHAPv2(3),
    
    UNSUPPORTED(-1);

    private final int id;
    private static final Map<Integer, RadiusAuthenticationMethod> ELEMENTS = new HashMap<>();

    private RadiusAuthenticationMethod(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static RadiusAuthenticationMethod getById(int enumId) {
        if (ELEMENTS.isEmpty()) {
            synchronized (ELEMENTS) {
                if (ELEMENTS.isEmpty()) {
                    //initialize elements map
                    for(RadiusAuthenticationMethod met : RadiusAuthenticationMethod.values()) {
                        ELEMENTS.put(met.getId(), met);
                    }
                }
            }
        }
        return ELEMENTS.get(enumId);
    }
    
    @JsonCreator
    public static RadiusAuthenticationMethod getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, RadiusAuthenticationMethod.class, UNSUPPORTED);
    }
    
    public static boolean isUnsupported(RadiusAuthenticationMethod value) {
        return UNSUPPORTED.equals(value);
    }
}
