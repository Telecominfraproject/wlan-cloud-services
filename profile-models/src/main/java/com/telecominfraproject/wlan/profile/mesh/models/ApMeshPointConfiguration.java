package com.telecominfraproject.wlan.profile.mesh.models;

/**
 * @author yongli
 *
 */
public class ApMeshPointConfiguration extends ApMeshConfiguration {
    /**
     * 
     */
    private static final long serialVersionUID = -8581717655003594570L;

    protected ApMeshPointConfiguration() {
        this(null, 0L);
    }

    public ApMeshPointConfiguration(Long groupId, long lastModifiedTimestamp) {
        super(ApMeshMode.MESH_POINT, groupId, lastModifiedTimestamp);
    }

    @Override
    public ApMeshPointConfiguration clone() {
        return (ApMeshPointConfiguration) super.clone();
    }
}
