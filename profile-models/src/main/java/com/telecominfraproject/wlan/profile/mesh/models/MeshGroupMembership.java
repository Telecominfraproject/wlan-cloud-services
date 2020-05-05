package com.telecominfraproject.wlan.profile.mesh.models;

/**
 * @author yongli
 *
 */
public class MeshGroupMembership extends MeshGroupMember {
    private static final long serialVersionUID = -7243952205301132192L;
    private long groupId;

    public MeshGroupMembership(ApMeshMode meshMode, long equipmentId, long groupId) {
        super(meshMode, equipmentId);
        this.groupId = groupId;
    }
    
    public MeshGroupMembership(final MeshGroupMember meshMember, long groupId) {
        super(meshMember);
        this.groupId = groupId;
    }

    protected MeshGroupMembership() {
    }

    @Override
    public MeshGroupMembership clone() {
        return (MeshGroupMembership) super.clone();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof MeshGroupMembership)) {
            return false;
        }
        MeshGroupMembership other = (MeshGroupMembership) obj;
        if (this.getGroupId() != other.getGroupId()) {
            return false;
        }
        return true;
    }

    public long getGroupId() {
        return groupId;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + (int) (this.getGroupId() ^ (this.getGroupId() >>> 32));
        return result;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }
}
