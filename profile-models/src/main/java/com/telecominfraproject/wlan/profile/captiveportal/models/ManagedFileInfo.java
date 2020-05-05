package com.telecominfraproject.wlan.profile.captiveportal.models;

import java.util.Arrays;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * TODO: this may need to be moved into a separate file management service
 */
public class ManagedFileInfo extends BaseJsonModel {
    private static final long serialVersionUID = 2118515075507748034L;
    private byte[] md5checksum;
    private Long lastModifiedTimestamp;
    private String apExportUrl;
    private FileCategory fileCategory;
    private FileType fileType;
    private boolean altSlot;

    public ManagedFileInfo() {

    }

    public ManagedFileInfo(byte[] checksum, Long lastMod) {
        this.md5checksum = checksum;
        this.lastModifiedTimestamp = lastMod;
    }

    public byte[] getMd5checksum() {
        return md5checksum;
    }

    public void setMd5checksum(byte[] md5checksum) {
        this.md5checksum = md5checksum;
    }

    public Long getLastModifiedTimestamp() {
        return lastModifiedTimestamp;
    }

    public void setLastModifiedTimestamp(Long lastModifiedTimestamp) {
        this.lastModifiedTimestamp = lastModifiedTimestamp;
    }

    public String getApExportUrl() {
        return apExportUrl;
    }

    public void setApExportUrl(String apExportUrl) {
        this.apExportUrl = apExportUrl;
    }

    public FileCategory getFileCategory() {
        return fileCategory;
    }

    public void setFileCategory(FileCategory fileCategory) {
        this.fileCategory = fileCategory;
    }

    public FileType getFileType() {
        return fileType;
    }

    public void setFileType(FileType fileType) {
        this.fileType = fileType;
    }

    public boolean isAltSlot() {
        return altSlot;
    }

    public void setAltSlot(boolean altSlot) {
        this.altSlot = altSlot;
    }

    /**
     * This will resolve the managed file with the proper filename.
     * 
     * Do NOT write this back in the DB.
     * 
     * @param fileInfo
     * @param fwHostname
     * @return
     */
    public static ManagedFileInfo resolveWithPopulatedHostname(ManagedFileInfo fileInfo, String fwHostname) {
        if (fileInfo != null) {
            ManagedFileInfo clonedFileInfo = fileInfo.clone();

            if (clonedFileInfo.getApExportUrl() != null) {
                clonedFileInfo.setApExportUrl("https://" + fwHostname + clonedFileInfo.getApExportUrl());
            }

            return clonedFileInfo;
        }

        return null;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (altSlot ? 1231 : 1237);
        result = prime * result + ((apExportUrl == null) ? 0 : apExportUrl.hashCode());
        result = prime * result + ((fileCategory == null) ? 0 : fileCategory.hashCode());
        result = prime * result + ((fileType == null) ? 0 : fileType.hashCode());
        result = prime * result + ((lastModifiedTimestamp == null) ? 0 : lastModifiedTimestamp.hashCode());
        result = prime * result + Arrays.hashCode(md5checksum);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ManagedFileInfo other = (ManagedFileInfo) obj;
        if (altSlot != other.altSlot)
            return false;
        if (apExportUrl == null) {
            if (other.apExportUrl != null)
                return false;
        } else if (!apExportUrl.equals(other.apExportUrl))
            return false;
        if (fileCategory != other.fileCategory)
            return false;
        if (fileType != other.fileType)
            return false;
        if (lastModifiedTimestamp == null) {
            if (other.lastModifiedTimestamp != null)
                return false;
        } else if (!lastModifiedTimestamp.equals(other.lastModifiedTimestamp))
            return false;
        if (!Arrays.equals(md5checksum, other.md5checksum))
            return false;
        return true;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (FileCategory.isUnsupported(fileCategory)) {
            return true;
        }
        if (FileType.isUnsupported(fileType)) {
            return true;
        }
        return false;
    }

    @Override
    public ManagedFileInfo clone() {
        ManagedFileInfo cloned = new ManagedFileInfo();
        cloned.setAltSlot(isAltSlot());
        cloned.setApExportUrl(getApExportUrl());
        cloned.setFileCategory(getFileCategory());
        cloned.setFileType(getFileType());
        cloned.setLastModifiedTimestamp(getLastModifiedTimestamp());
        cloned.setMd5checksum(getMd5checksum());

        return cloned;
    }

}
