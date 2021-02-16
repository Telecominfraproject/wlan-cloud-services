package com.telecominfraproject.wlan.firmware.models;

import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * @author ekeddy
 *
 */
public class FirmwareVersion extends BaseJsonModel {

    private static final long serialVersionUID = -5462726189319796609L;

    private long id;

    private EquipmentType equipmentType;
    private String modelId;
    private String versionName;
    private String description;
    private String filename;
    private String commit;
    private ValidationMethod validationMethod;
    private String validationCode;
    private long releaseDate;

    private long createdTimestamp;
    private long lastModifiedTimestamp;

    @Override
    public FirmwareVersion clone() {
        FirmwareVersion ret = (FirmwareVersion) super.clone();
        return ret;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof FirmwareVersion)) {
            return false;
        }
        FirmwareVersion other = (FirmwareVersion) obj;
        if (this.commit == null) {
            if (other.commit != null) {
                return false;
            }
        } else if (!this.commit.equals(other.commit)) {
            return false;
        }
        if (this.createdTimestamp != other.createdTimestamp) {
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
        if (this.filename == null) {
            if (other.filename != null) {
                return false;
            }
        } else if (!this.filename.equals(other.filename)) {
            return false;
        }
        if (this.id != other.id) {
            return false;
        }
        if (this.lastModifiedTimestamp != other.lastModifiedTimestamp) {
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
        if (this.validationCode == null) {
            if (other.validationCode != null) {
                return false;
            }
        } else if (!this.validationCode.equals(other.validationCode)) {
            return false;
        }
        if (this.validationMethod != other.validationMethod) {
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

    public long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public String getDescription() {
        return description;
    }

    public EquipmentType getEquipmentType() {
        return equipmentType;
    }

    public String getFilename() {
        return filename;
    }

    public long getId() {
        return id;
    }

    public long getLastModifiedTimestamp() {
        return lastModifiedTimestamp;
    }

    public String getModelId() {
        return modelId;
    }

    public long getReleaseDate() {
        return releaseDate;
    }

    public String getValidationCode() {
        return validationCode;
    }

    public ValidationMethod getValidationMethod() {
    	if (validationMethod == null) {
    		setValidationMethod(ValidationMethod.NONE);
    		return validationMethod;
    	}
        return validationMethod;
    }


    public String getVersionName() {
        return versionName;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((this.commit == null) ? 0 : this.commit.hashCode());
        result = prime * result + (int) (this.createdTimestamp ^ (this.createdTimestamp >>> 32));
        result = prime * result + ((this.description == null) ? 0 : this.description.hashCode());
        result = prime * result + ((this.equipmentType == null) ? 0 : this.equipmentType.hashCode());
        result = prime * result + ((this.filename == null) ? 0 : this.filename.hashCode());
        result = prime * result + (int) (this.id ^ (this.id >>> 32));
        result = prime * result + (int) (this.lastModifiedTimestamp ^ (this.lastModifiedTimestamp >>> 32));
        result = prime * result + ((this.modelId == null) ? 0 : this.modelId.hashCode());
        result = prime * result + (int) (this.releaseDate ^ (this.releaseDate >>> 32));
        result = prime * result + ((this.validationCode == null) ? 0 : this.validationCode.hashCode());
        result = prime * result + ((this.validationMethod == null) ? 0 : this.validationMethod.hashCode());
        result = prime * result + ((this.versionName == null) ? 0 : this.versionName.hashCode());
        return result;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }

        if (EquipmentType.isUnsupported(equipmentType) || ValidationMethod.isUnsupported(validationMethod)) {
            return true;
        }
        return false;
    }

    public void setCommit(String commit) {
        this.commit = commit;
    }

    public void setCreatedTimestamp(long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setEquipmentType(EquipmentType equipmentType) {
        this.equipmentType = equipmentType;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setLastModifiedTimestamp(long lastModifiedTimestamp) {
        this.lastModifiedTimestamp = lastModifiedTimestamp;
    }

    public void setModelId(String modelId) {
        this.modelId = modelId;
    }

    public void setReleaseDate(long releaseDate) {
        this.releaseDate = releaseDate;
    }

    public void setValidationCode(String checksum) {
        this.validationCode = checksum;
    }

    public void setValidationMethod(ValidationMethod validationMethod) {
        this.validationMethod = validationMethod;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

}
