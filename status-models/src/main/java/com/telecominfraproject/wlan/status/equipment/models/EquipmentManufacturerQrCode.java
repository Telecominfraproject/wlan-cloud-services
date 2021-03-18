
package com.telecominfraproject.wlan.status.equipment.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

public class EquipmentManufacturerQrCode extends BaseJsonModel {

    private static final long serialVersionUID = 1097677833521866340L;

    private EquipmentType deviceType;
    private MacAddress deviceMac;
    private String vendorName;
    private String serialNumber;
    private String modelName;
    private String hwRevision;

    public EquipmentManufacturerQrCode() {
        super();
        deviceType = EquipmentType.AP;
    }

    public EquipmentManufacturerQrCode(EquipmentType deviceType, MacAddress deviceMac, String vendorName, String serialNumber, String modelName,
            String hwRevision) {
        super();
        this.deviceType = deviceType;
        this.deviceMac = deviceMac;
        this.vendorName = vendorName;
        this.serialNumber = serialNumber;
        this.modelName = modelName;
        this.hwRevision = hwRevision;
    }

    public EquipmentType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(EquipmentType deviceType) {
        this.deviceType = deviceType;
    }

    public MacAddress getDeviceMac() {
        return deviceMac;
    }

    public void setDeviceMac(MacAddress deviceMac) {
        this.deviceMac = deviceMac;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public String getHwRevision() {
        return hwRevision;
    }

    public void setHwRevision(String hwRevision) {
        this.hwRevision = hwRevision;
    }

    @Override
    public boolean hasUnsupportedValue() {

        if (hasUnsupportedValue(deviceMac) || EquipmentType.isUnsupported(deviceType)) {
            return true;
        }

        return false;
    }

    public EquipmentManufacturerQrCode clone() {
        EquipmentManufacturerQrCode ret = (EquipmentManufacturerQrCode) super.clone();
        if (this.deviceMac != null) {
            ret.deviceMac = this.deviceMac.clone();
        }
        return ret;
    }

    @Override
    public int hashCode() {
        return Objects.hash(deviceMac, deviceType, hwRevision, modelName, serialNumber, vendorName);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EquipmentManufacturerQrCode other = (EquipmentManufacturerQrCode) obj;
        return Objects.equals(deviceMac, other.deviceMac) && Objects.equals(deviceType, other.deviceType) && Objects.equals(hwRevision, other.hwRevision)
                && Objects.equals(modelName, other.modelName) && Objects.equals(serialNumber, other.serialNumber)
                && Objects.equals(vendorName, other.vendorName);
    }

}
