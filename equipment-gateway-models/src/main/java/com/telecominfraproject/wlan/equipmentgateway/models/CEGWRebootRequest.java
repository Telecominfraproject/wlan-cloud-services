/**
 *
 */
package com.telecominfraproject.wlan.equipmentgateway.models;

import java.util.Objects;

import com.telecominfraproject.wlan.status.equipment.models.EquipmentResetMethod;

/**
 * @author ekeddy
 *
 */
public class CEGWRebootRequest extends EquipmentCommand {

    /**
     *
     */

    /**
     *
     */
    private static final long serialVersionUID = -2483309934540104301L;

    /**
     * Swap the bank during reboot
     */
    private boolean useInactiveBank = false;

    /**
     * perform a reset before reboot
     */
    private EquipmentResetMethod performReset = EquipmentResetMethod.NoReset;

    /**
     * Constructor
     *
     * @param qrCode
     * @param equipmentId
     * @param useInactiveBank
     * @param performReset
     */
    public CEGWRebootRequest(String qrCode, long equipmentId, boolean useInactiveBank,
            EquipmentResetMethod performReset) {
        super(CEGWCommandType.RebootRequest, qrCode, equipmentId);
        this.setUseInactiveBank(useInactiveBank);
        this.setPerformReset(performReset);
    }

    /**
     * Constructor used by JSON
     */
    public CEGWRebootRequest() {
        super(CEGWCommandType.RebootRequest, null, 0);
    }

    public boolean isUseInactiveBank() {
        return useInactiveBank;
    }

    public void setUseInactiveBank(boolean useInactiveBank) {
        this.useInactiveBank = useInactiveBank;
    }

    public EquipmentResetMethod getPerformReset() {
        return performReset;
    }

    public void setPerformReset(EquipmentResetMethod performFactoryReset) {
        this.performReset = performFactoryReset;
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        if (EquipmentResetMethod.isUnsupported(performReset)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(performReset, useInactiveBank);
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
        if (!(obj instanceof CEGWRebootRequest)) {
            return false;
        }
        CEGWRebootRequest other = (CEGWRebootRequest) obj;
        return this.performReset == other.performReset && this.useInactiveBank == other.useInactiveBank;
    }

}
