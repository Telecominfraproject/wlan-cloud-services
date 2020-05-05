package com.telecominfraproject.wlan.profile.mesh.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

/**
 * Mesh Mode for AP
 * 
 * @author yongli
 *
 */
public enum ApMeshMode {
    /**
     * Not running MESH
     */
    STANDALONE(0, "AP-"),
    /**
     * Wired AP, accepting wireless connection from MESH_POINT
     */
    MESH_PORTAL(1, "MeshPortal-"),
    /**
     * Extender AP. connect to MESH_PORTAL.
     */
    MESH_POINT(2, "MeshPoint-"),
    /**
     * Unsupported value
     */
    UNSUPPORTED(-1, "");

    private static final Map<Integer, ApMeshMode> ELEMENTS = new HashMap<>();

    public static ApMeshMode getById(int enumId) {
        if (ELEMENTS.isEmpty()) {
            synchronized (ELEMENTS) {
                if (ELEMENTS.isEmpty()) {
                    // initialize elements map
                    for (ApMeshMode met : ApMeshMode.values()) {
                        ELEMENTS.put(met.getId(), met);
                    }
                }
            }
        }
        ApMeshMode result = ELEMENTS.get(enumId);
        if (null == result) {
            result = UNSUPPORTED;
        }
        return result;
    }

    @JsonCreator
    public static ApMeshMode getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, ApMeshMode.class, UNSUPPORTED);
    }

    public static boolean isUnsupported(ApMeshMode meshMode) {
        return (UNSUPPORTED.equals(meshMode));
    }

    private final int id;

    private final String prefix;

    ApMeshMode(int id, String prefix) {
        this.id = id;
        this.prefix = prefix;
    }

    public String getDefaultApName(final String suffix) {
        return this.prefix + suffix;
    }

    public int getId() {
        return id;
    }

}