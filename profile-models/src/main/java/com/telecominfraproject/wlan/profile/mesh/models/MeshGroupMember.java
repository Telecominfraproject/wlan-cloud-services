package com.telecominfraproject.wlan.profile.mesh.models;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;

/**
 * @author yongli
 *
 */
public class MeshGroupMember extends BaseJsonModel {
    /**
     * 
     */
    private static final long serialVersionUID = -6017672176341037832L;

    private static final long UNKNOWN_EQUIPMENT_ID = -1L;

    private ApMeshMode meshMode;
    private long equipmentId = UNKNOWN_EQUIPMENT_ID;

    private long createdTimestamp;
    private long lastModifiedTimestamp;

    public MeshGroupMember(ApMeshMode meshMode, long equipmentId) {
        this(meshMode, equipmentId, 0L, 0L);
    }

    public MeshGroupMember(final MeshGroupMember other) {
        this(other.meshMode, other.equipmentId, other.createdTimestamp, other.lastModifiedTimestamp);
    }

    public MeshGroupMember(ApMeshMode meshMode, long equipmentId, long createdTimestamp, long lastModifiedTimestamp) {
        super();
        this.meshMode = meshMode;
        this.equipmentId = equipmentId;
        this.createdTimestamp = createdTimestamp;
        this.lastModifiedTimestamp = lastModifiedTimestamp;
    }

    protected MeshGroupMember() {
    }

    @Override
    public MeshGroupMember clone() {
        return (MeshGroupMember) super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MeshGroupMember)) {
            return false;
        }
        MeshGroupMember other = (MeshGroupMember) obj;
        if (this.equipmentId != other.equipmentId) {
            return false;
        }
        if (this.meshMode != other.meshMode) {
            return false;
        }
        return true;
    }

    public long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public long getEquipmentId() {
        return equipmentId;
    }

    public long getLastModifiedTimestamp() {
        return lastModifiedTimestamp;
    }

    public ApMeshMode getMeshMode() {
        return meshMode;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (this.equipmentId ^ (this.equipmentId >>> 32));
        result = prime * result + ((this.meshMode == null) ? 0 : this.meshMode.hashCode());
        return result;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue() || ApMeshMode.isUnsupported(meshMode)) {
            return true;
        }
        return false;
    }

    public void setCreatedTimestamp(long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public void setEquipmentId(long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public void setLastModifiedTimestamp(long lastModifiedTimestamp) {
        this.lastModifiedTimestamp = lastModifiedTimestamp;
    }

    public void setMeshMode(ApMeshMode meshMode) {
        this.meshMode = meshMode;
    }

    /**
     * Validate the data
     * 
     * @throws DsDataValidationException
     */
    public void validateData() {
        if (null == this.meshMode) {
            throw new DsDataValidationException("Missing mesh mode for the mesh group member");
        }
        switch (this.meshMode) {
        case MESH_POINT:
        case MESH_PORTAL:
            break;
        default:
            throw new DsDataValidationException("Invalid mesh " + this.meshMode + "mode set for the mesh group member");
        }
        if (UNKNOWN_EQUIPMENT_ID == this.equipmentId) {
            throw new DsDataValidationException("Missing equipment id for the mesh group member");
        }
    }
}
