package com.telecominfraproject.wlan.profile.captiveportal.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

/**
 * Describes the authentication options available for a Captive Portal.
 * <ul>
 * <li>guest - accept the terms and can access.
 * <li>username - provide a username an password to authenticate against a known list.
 * <li>radius - provide a username and password to authenticate against a radius server.
 * <li>external - an external server will handle the authentication.
 * </ul>
 * @author preston
 *
 */
public enum CaptivePortalAuthenticationType {

    guest(0),
    username(1),
    radius(2),
    external(3),
    UNSUPPORTED(-1);

    private final int id;
    private static final Map<Integer, CaptivePortalAuthenticationType> ELEMENTS = new HashMap<>();

    private CaptivePortalAuthenticationType(int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }

    public static CaptivePortalAuthenticationType getById(int enumId) {
        if (ELEMENTS.isEmpty()) {
            synchronized (ELEMENTS) {
                if (ELEMENTS.isEmpty()) {
                    //initialize elements map
                    for(CaptivePortalAuthenticationType met : CaptivePortalAuthenticationType.values()) {
                        ELEMENTS.put(met.getId(), met);
                    }
                }
            }
        }
        return ELEMENTS.get(enumId);
    }
    
    @JsonCreator
    public static CaptivePortalAuthenticationType getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, CaptivePortalAuthenticationType.class, UNSUPPORTED);
    }
    
    public static boolean isUnsupported(CaptivePortalAuthenticationType value) {
        return UNSUPPORTED.equals(value);
    }
}
