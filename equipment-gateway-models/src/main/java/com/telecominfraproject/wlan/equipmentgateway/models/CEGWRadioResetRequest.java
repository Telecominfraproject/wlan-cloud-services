/**
 *
 */
package com.telecominfraproject.wlan.equipmentgateway.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.RadioType;

/**
 * A Request for resetting a radio.
 * 
 * @author ekeddy
 *
 */
public class CEGWRadioResetRequest extends EquipmentCommand {

    /**
     *
     */
    private static final long serialVersionUID = -7415410522452755178L;
    private RadioType radio;
    private RadioResetMethod resetMethod;

    protected CEGWRadioResetRequest() {
        super(CEGWCommandType.RadioReset, null, 0);
    }

    public CEGWRadioResetRequest(String qrCode, long equipmentId, RadioType radio, RadioResetMethod resetMethod) {
        super(CEGWCommandType.RadioReset, qrCode, equipmentId);
        this.radio = radio;
        this.resetMethod = resetMethod;
    }

    public RadioType getRadio() {
        return radio;
    }

    public void setRadio(RadioType radio) {
        this.radio = radio;
    }

    public RadioResetMethod getResetMethod() {
        return resetMethod;
    }

    public void setResetMethod(RadioResetMethod resetMethod) {
        this.resetMethod = resetMethod;
    }

    /**
     * Construct a reset request for an init reset.
     * 
     * @param qrCode
     * @param equipmentId
     * @param radio
     * @return
     */
    public static CEGWRadioResetRequest init(String qrCode, long equipmentId, RadioType radio) {
        return new CEGWRadioResetRequest(qrCode, equipmentId, radio, RadioResetMethod.init);
    }

    /**
     * Construct a reset request for a reload reset.
     * 
     * @param qrCode
     * @param equipmentId
     * @param radio
     * @return
     */
    public static CEGWRadioResetRequest reload(String qrCode, long equipmentId, RadioType radio) {
        return new CEGWRadioResetRequest(qrCode, equipmentId, radio, RadioResetMethod.reload);
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (RadioResetMethod.isUnsupported(resetMethod) || RadioType.isUnsupported(radio)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(radio, resetMethod);
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
        if (!(obj instanceof CEGWRadioResetRequest)) {
            return false;
        }
        CEGWRadioResetRequest other = (CEGWRadioResetRequest) obj;
        return this.radio == other.radio && this.resetMethod == other.resetMethod;
    }

}
