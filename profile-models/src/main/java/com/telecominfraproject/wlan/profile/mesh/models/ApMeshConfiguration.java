package com.telecominfraproject.wlan.profile.mesh.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author yongli
 *
 */
public abstract class ApMeshConfiguration extends BaseJsonModel {
    /**
     * 
     */
    private static final long serialVersionUID = 7076846589657746716L;

    private ApMeshMode meshMode;
    private Long groupId;
    private long lastModifiedTimestamp;

    protected ApMeshConfiguration(ApMeshMode meshMode, Long groupId, long lastModifiedTimestamp) {
        this.meshMode = meshMode;
        this.groupId = groupId;
        this.lastModifiedTimestamp = lastModifiedTimestamp;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ApMeshConfiguration)) {
            return false;
        }
        ApMeshConfiguration other = (ApMeshConfiguration) obj;
        return Objects.equals(groupId, other.groupId) && this.lastModifiedTimestamp == other.lastModifiedTimestamp
                && this.meshMode == other.meshMode;
    }

    public ApMeshMode getMeshMode() {
        return meshMode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(groupId, lastModifiedTimestamp, meshMode);
    }

    @Override
    public boolean hasUnsupportedValue() {
        return ApMeshMode.isUnsupported(this.meshMode);
    }

    public void setMeshMode(ApMeshMode meshMode) {
        this.meshMode = meshMode;
    }

    @Override
    public ApMeshConfiguration clone() {
        return (ApMeshConfiguration) super.clone();
    }

    public long getLastModifiedTimestamp() {
        return this.lastModifiedTimestamp;
    }

    public Long getGroupId() {
        return groupId;
    }

    public void setGroupId(Long groupId) {
        this.groupId = groupId;
    }
}
