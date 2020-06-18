/**
 *
 */
package com.telecominfraproject.wlan.equipmentgateway.models;

import java.util.Objects;

/**
 * @author ekeddy
 *
 */
public class CEGWFirmwareFlashRequest extends EquipmentCommand {

    /**
     *
     */
    private static final long serialVersionUID = 3529329281547180892L;
    private String firmwareVersion;

    /**
     * @param qrCode
     * @param equipmentId
     * @param filePath
     * @param firmwareVersion
     */
    public CEGWFirmwareFlashRequest(String qrCode, long equipmentId, String firmwareVersion) {
        super(CEGWCommandType.FirmwareFlashRequest, qrCode, equipmentId);
        this.setFirmwareVersion(firmwareVersion);
    }

    /**
     * Constructor used by JSON
     */
    public CEGWFirmwareFlashRequest() {
        super(CEGWCommandType.FirmwareFlashRequest, null, 0);
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
        result = prime * result + Objects.hash(firmwareVersion);
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
        if (!(obj instanceof CEGWFirmwareFlashRequest)) {
            return false;
        }
        CEGWFirmwareFlashRequest other = (CEGWFirmwareFlashRequest) obj;
        return Objects.equals(firmwareVersion, other.firmwareVersion);
    }

}
