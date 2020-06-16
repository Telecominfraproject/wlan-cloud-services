package com.telecominfraproject.wlan.firmware.models;

import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;


public class FirmwareTrackAssignmentDetails extends FirmwareTrackAssignmentRecord {
    private static final long serialVersionUID = -7872721628958719364L;

    private String description;
    private EquipmentType equipmentType;
    
    private String modelId;
    private long releaseDate;
    private String versionName;
    private String commit;
    
    public FirmwareTrackAssignmentDetails() {
        
    }
    
    public FirmwareTrackAssignmentDetails(FirmwareTrackAssignmentRecord assignmentRecord, FirmwareVersion version) {
        if(assignmentRecord != null) {
            setTrackRecordId(assignmentRecord.getTrackRecordId());
            setFirmwareVersionRecordId(assignmentRecord.getFirmwareVersionRecordId());
            setLastModifiedTimestamp(assignmentRecord.getLastModifiedTimestamp());
            setCreatedTimestamp(assignmentRecord.getCreatedTimestamp());
            setDefaultRevisionForTrack(assignmentRecord.isDefaultRevisionForTrack());
            setDeprecated(assignmentRecord.isDeprecated());
        }
        if(version != null) {
            setFirmwareVersionRecordId(version.getId());
            setDescription(version.getDescription());
            setEquipmentType(version.getEquipmentType());
            setModelId(version.getModelId());
            setReleaseDate(version.getReleaseDate());
            setVersionName(version.getVersionName());
            setCommit(version.getCommit());
        }
    }
    
    @Override
    public FirmwareTrackAssignmentDetails clone() {
        FirmwareTrackAssignmentDetails ret = (FirmwareTrackAssignmentDetails) super.clone();
        return ret;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof FirmwareTrackAssignmentDetails)) {
            return false;
        }
        FirmwareTrackAssignmentDetails other = (FirmwareTrackAssignmentDetails) obj;
        if (this.commit == null) {
            if (other.commit != null) {
                return false;
            }
        } else if (!this.commit.equals(other.commit)) {
            return false;
        }
        if (this.description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!this.description.equals(other.description)) {
            return false;
        }
        if (this.equipmentType != other.equipmentType) {
            return false;
        }
        if (this.modelId == null) {
            if (other.modelId != null) {
                return false;
            }
        } else if (!this.modelId.equals(other.modelId)) {
            return false;
        }
        if (this.releaseDate != other.releaseDate) {
            return false;
        }
        if (this.versionName == null) {
            if (other.versionName != null) {
                return false;
            }
        } else if (!this.versionName.equals(other.versionName)) {
            return false;
        }
        return true;
    }
    
    public String getCommit() {
        return commit;
    }
    public String getDescription() {
        return description;
    }
    public EquipmentType getEquipmentType() {
        return equipmentType;
    }
    public String getModelId() {
        return modelId;
    }
    public long getReleaseDate() {
        return releaseDate;
    }
    public String getVersionName() {
        return versionName;
    }
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.commit == null) ? 0 : this.commit.hashCode());
        result = prime * result + ((this.description == null) ? 0 : this.description.hashCode());
        result = prime * result + ((this.equipmentType == null) ? 0 : this.equipmentType.hashCode());
        result = prime * result + ((this.modelId == null) ? 0 : this.modelId.hashCode());
        result = prime * result + (int) (this.releaseDate ^ (this.releaseDate >>> 32));
        result = prime * result + ((this.versionName == null) ? 0 : this.versionName.hashCode());
        return result;
    }
    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (EquipmentType.isUnsupported(equipmentType)) {
            return true;
        }
        return false;
    }
    public void setCommit(String commit) {
        this.commit = commit;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public void setEquipmentType(EquipmentType equipmentType) {
        this.equipmentType = equipmentType;
    }
    public void setModelId(String modelId) {
        this.modelId = modelId;
    }
    public void setReleaseDate(long releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }
    
}
