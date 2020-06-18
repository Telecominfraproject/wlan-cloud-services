/**
 *
 */
package com.telecominfraproject.wlan.equipmentgateway.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.role.PortalUserRole;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;

/**
 * CE Gateway Command to start packet file capture
 *
 * @author yongli
 *
 */
public class CEGWStartPacketFileCapture extends EquipmentCommand {

    /**
     *
     */
    private static final long serialVersionUID = 4050109090171370215L;

    private static final long MAX_DURATION_SEC = 3600L;

    private static final long MAX_FILE_SIZE_KB = 100000L;

    private static final long MIN_DUARTION_SEC = 10L;

    private static final long MIN_FILE_SIZE_KB = 64L;

    private static final long MIN_PACKET_TRUNCATE_BYTE = 24L;

    /**
     *
     */

    /**
     * Capture 5G radio or capture 2G radio or Ethernet
     */
    private CaptureInterface captureInterface;

    /**
     * Filter by client MAC address (optional)
     */
    private MacAddress clientMacAddressFilter;

    /**
     * How many seconds to capture (optional)
     */
    private Long durationSecond;
    /**
     * Capture file size limit (optional)
     */
    private Long fileSizeKb;

    /**
     * Upload captured file
     */
    private boolean fileUploadEnabled;

    /**
     * Filter out beacon
     */
    private boolean noBeaconFilterEnabled;

    /**
     * Packet truncate byte size (optional)
     */
    private Long packetTruncateByte;

    /**
     * Used for JSON
     */
    @Deprecated
    public CEGWStartPacketFileCapture() {
        this(null, 0L, null, null);
    }

    /**
     * Constructor
     *
     * @param qrCode
     * @param equipmentId
     * @param userRole
     * @param userName
     */
    public CEGWStartPacketFileCapture(String qrCode, long equipmentId, PortalUserRole userRole, String userName) {
        super(CEGWCommandType.StartPacketFileCapture, qrCode, equipmentId, userRole, userName);
    }

    @Override
    public CEGWStartPacketFileCapture clone() {
        CEGWStartPacketFileCapture result = (CEGWStartPacketFileCapture) super.clone();
        if (null != result.clientMacAddressFilter) {
            result.clientMacAddressFilter = this.clientMacAddressFilter.clone();
        }
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
        if (!(obj instanceof CEGWStartPacketFileCapture)) {
            return false;
        }
        CEGWStartPacketFileCapture other = (CEGWStartPacketFileCapture) obj;
        return this.captureInterface == other.captureInterface
                && Objects.equals(clientMacAddressFilter, other.clientMacAddressFilter)
                && Objects.equals(durationSecond, other.durationSecond) && Objects.equals(fileSizeKb, other.fileSizeKb)
                && this.fileUploadEnabled == other.fileUploadEnabled
                && this.noBeaconFilterEnabled == other.noBeaconFilterEnabled
                && Objects.equals(packetTruncateByte, other.packetTruncateByte);
    }

    public CaptureInterface getCaptureInterface() {
        return captureInterface;
    }

    public MacAddress getClientMacAddressFilter() {
        return clientMacAddressFilter;
    }

    public Long getDurationSecond() {
        return durationSecond;
    }

    public Long getFileSizeKb() {
        return fileSizeKb;
    }

    public Long getPacketTruncateByte() {
        return packetTruncateByte;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(captureInterface, clientMacAddressFilter, durationSecond, fileSizeKb,
                fileUploadEnabled, noBeaconFilterEnabled, packetTruncateByte);
        return result;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }

        if (hasUnsupportedValue(clientMacAddressFilter)) {
            return true;
        }

        if (CaptureInterface.isUnsupported(captureInterface)) {
            return true;
        }

        return false;
    }

    public boolean isFileUploadEnabled() {
        return fileUploadEnabled;
    }

    public boolean isNoBeaconFilterEnabled() {
        return noBeaconFilterEnabled;
    }

    public void setCaptureInterface(CaptureInterface captureInterface) {
        this.captureInterface = captureInterface;
    }

    public void setClientMacAddressFilter(MacAddress clientMacAddressFilter) {
        this.clientMacAddressFilter = clientMacAddressFilter;
    }

    public void setDurationSecond(Long durationSecond) {
        this.durationSecond = durationSecond;
    }

    public void setFileSizeKb(Long fileSizeKb) {
        this.fileSizeKb = fileSizeKb;
    }

    public void setFileUploadEnabled(boolean fileUploadEnabled) {
        this.fileUploadEnabled = fileUploadEnabled;
    }

    public void setNoBeaconFilterEnabled(boolean noBeaconFilterEnabled) {
        this.noBeaconFilterEnabled = noBeaconFilterEnabled;
    }

    public void setPacketTruncateByte(Long packetTruncateByte) {
        this.packetTruncateByte = packetTruncateByte;
    }

    /**
     * Validate the command
     *
     * @throws DsDataValidationException
     */
    public void validateCommand() {
        if (hasUnsupportedValue()) {
            throw new DsDataValidationException("command contains unsupported value");
        }
        if (null == this.captureInterface) {
            throw new DsDataValidationException("capture interface is not specified");
        }
        switch (this.captureInterface) {
        case ETHERNET:
        case RADIO2G:
        case RADIO5G:
            break;
        default:
            throw new DsDataValidationException("unsupported capture interface " + this.captureInterface);
        }
        if (null != this.packetTruncateByte) {
            if (this.packetTruncateByte < MIN_PACKET_TRUNCATE_BYTE) {
                throw new DsDataValidationException("packet truncate byte value is too small, minimum value in byte is "
                        + MIN_PACKET_TRUNCATE_BYTE);
            }
        }
        if (null != this.durationSecond) {
            if (this.durationSecond < MIN_DUARTION_SEC) {
                throw new DsDataValidationException(
                        "capture duration is too smal, minimum in seccond is " + MIN_DUARTION_SEC);
            }
            if (this.durationSecond > MAX_DURATION_SEC) {
                throw new DsDataValidationException(
                        "capture duration is too big, maximum value in second is " + MAX_DURATION_SEC);
            }
        }
        if (null != this.fileSizeKb) {
            if (this.fileSizeKb < MIN_FILE_SIZE_KB) {
                throw new DsDataValidationException(
                        "capture file size is too small, minimum value in KB is " + MIN_FILE_SIZE_KB);
            }
            if (this.fileSizeKb > MAX_FILE_SIZE_KB) {
                throw new DsDataValidationException(
                        "capture file size is too big, maximum value in KB is " + MAX_FILE_SIZE_KB);
            }
        }
    }

}
