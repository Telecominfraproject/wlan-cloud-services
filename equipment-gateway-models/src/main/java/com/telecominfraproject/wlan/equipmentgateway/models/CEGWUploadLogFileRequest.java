/**
 *
 */
package com.telecominfraproject.wlan.equipmentgateway.models;

import java.util.Objects;

/**
 * Command for requesting AP to upload log file.
 *
 * @author yongli
 *
 */
public class CEGWUploadLogFileRequest extends EquipmentCommand {
    /**
     *
     */
    private static final long serialVersionUID = -5386986488304875678L;
    /**
     *
     */
    private String uploadURLPrefix;

    public CEGWUploadLogFileRequest(String qrCode, long equipmentId, String uploadURLPrefix) {
        super(CEGWCommandType.UploadLogFile, qrCode, equipmentId);
        this.uploadURLPrefix = uploadURLPrefix;
    }

    protected CEGWUploadLogFileRequest() {
        super(CEGWCommandType.UploadLogFile, null, 0);
    }

    @Override
    public CEGWUploadLogFileRequest clone() {
        return (CEGWUploadLogFileRequest) super.clone();
    }

    public String getUploadURLPrefix() {
        return this.uploadURLPrefix;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        return false;
    }

    public void setUploadURLPrefix(String uploadURLPrefix) {
        this.uploadURLPrefix = uploadURLPrefix;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(uploadURLPrefix);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof CEGWUploadLogFileRequest)) {
            return false;
        }
        CEGWUploadLogFileRequest other = (CEGWUploadLogFileRequest) obj;
        return Objects.equals(uploadURLPrefix, other.uploadURLPrefix);
    }
}
