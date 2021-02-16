/**
 *
 */
package com.telecominfraproject.wlan.equipmentgateway.models;

import java.util.Objects;

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

    /**
     * @param qrCode
     * @param equipmentId
     * @param filePath
     * @param firmwareVersion
     */
    public CEGWFirmwareDownloadRequest(String qrCode, long equipmentId, String firmwareVersion, String filePath) {
        super(CEGWCommandType.FirmwareDownloadRequest, qrCode, equipmentId);
        this.setFirmwareVersion(firmwareVersion);
        this.setFilePath(filePath);
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

        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(filePath, firmwareVersion);
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
        return Objects.equals(filePath, other.filePath) && Objects.equals(firmwareVersion, other.firmwareVersion);
    }

}
