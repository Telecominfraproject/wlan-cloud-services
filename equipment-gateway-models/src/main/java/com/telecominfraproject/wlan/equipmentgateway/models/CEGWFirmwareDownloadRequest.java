/**
 *
 */
package com.telecominfraproject.wlan.equipmentgateway.models;

import java.util.Objects;

import com.telecominfraproject.wlan.firmware.models.ValidationMethod;

/**
 * @author ekeddy
 *
 */
public class CEGWFirmwareDownloadRequest extends EquipmentCommand {

    /**
     *
     */
    private static final long serialVersionUID = -6558620760271952936L;
    private String firmwareVersion;
    private String filePath;
    private ValidationMethod validationMethod;
    private String validationCode;

    /**
     * @param qrCode
     * @param equipmentId
     * @param filePath
     * @param firmwareVersion
     * @param validationMethod2
     * @param validationCode
     */
    public CEGWFirmwareDownloadRequest(String qrCode, long equipmentId, String firmwareVersion, String filePath,
           ValidationMethod validationMethod2, String validationCode) {
        super(CEGWCommandType.FirmwareDownloadRequest, qrCode, equipmentId);
        this.setFirmwareVersion(firmwareVersion);
        this.setFilePath(filePath);
        this.setValidationMethod(validationMethod2);
        this.setValidationCode(validationCode);
    }

    /**
     * Constructor used by JSON
     */
    protected CEGWFirmwareDownloadRequest() {
        super(CEGWCommandType.FirmwareDownloadRequest, null, 0);
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public ValidationMethod getValidationMethod() {
        return validationMethod;
    }

    public void setValidationMethod(ValidationMethod validationMethod) {
        this.validationMethod = validationMethod;
    }

    public String getValidationCode() {
        return validationCode;
    }

    public void setValidationCode(String validationCode) {
        this.validationCode = validationCode;
    }

    public String getFirmwareVersion() {
        return firmwareVersion;
    }

    public void setFirmwareVersion(String firmwareVersion) {
        this.firmwareVersion = firmwareVersion;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }

        if (ValidationMethod.isUnsupported(validationMethod)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(filePath, firmwareVersion, validationCode, validationMethod);
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
        if (!(obj instanceof CEGWFirmwareDownloadRequest)) {
            return false;
        }
        CEGWFirmwareDownloadRequest other = (CEGWFirmwareDownloadRequest) obj;
        return Objects.equals(filePath, other.filePath) && Objects.equals(firmwareVersion, other.firmwareVersion)
                && Objects.equals(validationCode, other.validationCode)
                && this.validationMethod == other.validationMethod;
    }

}
