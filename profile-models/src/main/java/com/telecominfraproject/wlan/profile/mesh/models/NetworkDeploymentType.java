package com.telecominfraproject.wlan.profile.mesh.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

/**
 * @author yongli
 *
 */
public enum NetworkDeploymentType {
    /**
     * Single AP with NAT gateway
     */
    SINGLE(0),

    /**
     * Mesh AP with NAT Mesh Portal
     */
    MESH(1),

    /**
     * Multiple AP in bridge mode
     */
    MULTIPLE(2),

    UNSUPPORTED(-1);

    private static final Map<Integer, NetworkDeploymentType> ELEMENTS;

    static {
        ELEMENTS = new HashMap<>();
        for (NetworkDeploymentType value : values()) {
            if (NetworkDeploymentType.isUnsupported(value)) {
                continue;
            }
            ELEMENTS.put(value.getId(), value);
        }
    }

    public static NetworkDeploymentType getById(int id) {
        NetworkDeploymentType result = ELEMENTS.get(id);
        if (null == result) {
            result = UNSUPPORTED;
        }
        return result;
    }

    @JsonCreator
    public static NetworkDeploymentType getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, NetworkDeploymentType.class,
                NetworkDeploymentType.UNSUPPORTED);
    }

    static boolean isUnsupported(NetworkDeploymentType value) {
        return (UNSUPPORTED.equals(value));
    }

    private final int id;

    NetworkDeploymentType(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}
