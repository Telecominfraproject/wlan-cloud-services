
package com.telecominfraproject.wlan.status.equipment.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.entity.CountryCode;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.status.models.StatusDataType;
import com.telecominfraproject.wlan.status.models.StatusDetails;

public class EquipmentManufacturerDataStatus extends StatusDetails {

    private static final long serialVersionUID = -6496935669860484813L;

    private String skuNumber;
    private String model;
    private String revision;
    private String serialNumber;
    private EquipmentManufacturerQrCode qrCode;
    private String manufacturerName;
    private String manufacturerDate;
    private String manufacturerUrl;
    private String modelDescription;
    private String referenceDesign;
    private CountryCode certificationRegion;
    private MacAddress macAddress;

    public EquipmentManufacturerDataStatus() {
        super();
    }
   
    public EquipmentManufacturerDataStatus(String skuNumber, String model, String revision, String serialNumber, EquipmentManufacturerQrCode qrCode,
            String manufacturerName, String manufacturerDate, String manufacturerUrl, String modelDescription, String referenceDesign,
            CountryCode certificationRegion, MacAddress macAddress) {
        super();
        this.skuNumber = skuNumber;
        this.model = model;
        this.revision = revision;
        this.serialNumber = serialNumber;
        this.qrCode = qrCode;
        this.manufacturerName = manufacturerName;
        this.manufacturerDate = manufacturerDate;
        this.manufacturerUrl = manufacturerUrl;
        this.modelDescription = modelDescription;
        this.referenceDesign = referenceDesign;
        this.certificationRegion = certificationRegion;
        this.macAddress = macAddress;
    }

    @Override
    public StatusDataType getStatusDataType() {
        return StatusDataType.EQUIPMENT_MANUFACTURER_DATA;
    }

    public String getSkuNumber() {
        return skuNumber;
    }

    public void setSkuNumber(String skuNumber) {
        this.skuNumber = skuNumber;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getRevision() {
        return revision;
    }

    public void setRevision(String revision) {
        this.revision = revision;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public EquipmentManufacturerQrCode getQrCode() {
        return qrCode;
    }

    public void setQrCode(EquipmentManufacturerQrCode qrCode) {
        this.qrCode = qrCode;
    }

    public String getManufacturerName() {
        return manufacturerName;
    }

    public void setManufacturerName(String manufacturerName) {
        this.manufacturerName = manufacturerName;
    }

    public String getManufacturerDate() {
        return manufacturerDate;
    }

    public void setManufacturerDate(String manufacturerDate) {
        this.manufacturerDate = manufacturerDate;
    }

    public String getManufacturerUrl() {
        return manufacturerUrl;
    }

    public void setManufacturerUrl(String manufacturerUrl) {
        this.manufacturerUrl = manufacturerUrl;
    }

    public String getModelDescription() {
        return modelDescription;
    }

    public void setModelDescription(String modelDescription) {
        this.modelDescription = modelDescription;
    }

    public String getReferenceDesign() {
        return referenceDesign;
    }

    public void setReferenceDesign(String referenceDesign) {
        this.referenceDesign = referenceDesign;
    }

    public CountryCode getCertificationRegion() {
        return certificationRegion;
    }

    public void setCertificationRegion(CountryCode certificationRegion) {
        this.certificationRegion = certificationRegion;
    }

    public MacAddress getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(MacAddress macAddress) {
        this.macAddress = macAddress;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }

        if (macAddress != null && hasUnsupportedValue(macAddress)) {
            return true;
        }

        return false;
    }

    @Override
    public EquipmentManufacturerDataStatus clone() {
        EquipmentManufacturerDataStatus ret = (EquipmentManufacturerDataStatus) super.clone();

        if (this.qrCode != null) {
            ret.qrCode = this.qrCode.clone();
        }

        if (this.macAddress != null) {
            ret.macAddress = this.macAddress.clone();
        }

        return ret;
    }

    @Override
    public int hashCode() {
        return Objects.hash(certificationRegion, macAddress, manufacturerDate, manufacturerName, manufacturerUrl, model, modelDescription, qrCode,
                referenceDesign, revision, serialNumber, skuNumber);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        EquipmentManufacturerDataStatus other = (EquipmentManufacturerDataStatus) obj;
        return Objects.equals(certificationRegion, other.certificationRegion) && Objects.equals(macAddress, other.macAddress)
                && Objects.equals(manufacturerDate, other.manufacturerDate) && Objects.equals(manufacturerName, other.manufacturerName)
                && Objects.equals(manufacturerUrl, other.manufacturerUrl) && Objects.equals(model, other.model)
                && Objects.equals(modelDescription, other.modelDescription) && Objects.equals(qrCode, other.qrCode)
                && Objects.equals(referenceDesign, other.referenceDesign) && Objects.equals(revision, other.revision)
                && Objects.equals(serialNumber, other.serialNumber) && Objects.equals(skuNumber, other.skuNumber);
    }


}
