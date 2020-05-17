package com.telecominfraproject.wlan.profile.mesh.models;

import java.util.ArrayList;
import java.util.List;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;
import com.telecominfraproject.wlan.profile.models.ProfileDetails;
import com.telecominfraproject.wlan.profile.models.ProfileType;


/**
 * @author yongli
 *
 */
public class MeshGroup extends ProfileDetails implements PushableConfiguration<MeshGroup> {
    private static final long serialVersionUID = -2813468276759852483L;
    private MeshGroupProperty property;
    /**
     * Member of the mesh group
     */
    private List<MeshGroupMember> members;


    @Override
    public ProfileType getProfileType() {
    	return ProfileType.mesh;
    }

    @Override
    public MeshGroup clone() {
        MeshGroup result = (MeshGroup) super.clone();
        if (null != this.property) {
            result.property = this.property.clone();
        }
        if (null != this.members) {
            result.members = new ArrayList<>(this.members.size());
            for (MeshGroupMember m : this.members) {
                result.members.add(m.clone());
            }
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof MeshGroup)) {
            return false;
        }
        MeshGroup other = (MeshGroup) obj;
        if (this.members == null) {
            if (other.members != null) {
                return false;
            }
        } else if (!this.members.equals(other.members)) {
            return false;
        }
        if (this.property == null) {
            if (other.property != null) {
                return false;
            }
        } else if (!this.property.equals(other.property)) {
            return false;
        }
        return true;
    }

    public List<MeshGroupMember> getMembers() {
        return members;
    }

    public MeshGroupProperty getProperty() {
        return property;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.members == null) ? 0 : this.members.hashCode());
        result = prime * result + ((this.property == null) ? 0 : this.property.hashCode());
        return result;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (BaseJsonModel.hasUnsupportedValue(property) || BaseJsonModel.hasUnsupportedValue(this.members)) {
            return true;
        }
        return false;
    }

    @Override
    public boolean needsToBeUpdatedOnDevice(final MeshGroup previousVersion) {
        if (this.property.needsToBeUpdatedOnDevice(previousVersion.getProperty())) {
            return true;
        }
        return false;
    }

    public void setMembers(List<MeshGroupMember> members) {
        this.members = members;
    }

    public void setProperty(MeshGroupProperty property) {
        this.property = property;
    }

    /**
     * Validate data.
     * 
     * @throws DsDataValidationException
     */
    public void validateData() {
        if (hasUnsupportedValue()) {
            throw new DsDataValidationException("Mesh group contains invalid data");
        }
        if (null == this.property) {
            throw new DsDataValidationException("Missing mesh group properties");
        }
        this.property.validateData();
        // validate membership
        if ((null == this.members) || (this.members.size() < 2)) {
            throw new DsDataValidationException("Not enough member in the mesh group");
        }

        // look for at least one mesh
        int portalCount = 0;
        int pointCount = 0;
        for (MeshGroupMember m : this.members) {
            m.validateData();
            switch (m.getMeshMode()) {
            case MESH_PORTAL:
                ++portalCount;
                break;
            case MESH_POINT:
                ++pointCount;
                break;
            default:
                // member validation already caught this
                break;
            }
        }
        if (portalCount < 1) {
            throw new DsDataValidationException("No mesh point configued in the mesh group");
        }
        if (pointCount < 1) {
            throw new DsDataValidationException("No mesh portal configured in the mesh group");
        }
    }
}
