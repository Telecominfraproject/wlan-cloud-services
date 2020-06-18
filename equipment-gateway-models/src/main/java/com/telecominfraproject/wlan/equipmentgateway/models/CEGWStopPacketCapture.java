/**
 *
 */
package com.telecominfraproject.wlan.equipmentgateway.models;

import com.telecominfraproject.wlan.core.model.role.PortalUserRole;

/**
 * @author yongli
 *
 */
public class CEGWStopPacketCapture extends EquipmentCommand {
    /**
     *
     */
    private static final long serialVersionUID = -1524889834210615908L;

    /**
     *
     */

    public CEGWStopPacketCapture() {
        this(null, 0L, null, null);
    }

    public CEGWStopPacketCapture(String equipmentQRCode, long equipmentId, PortalUserRole userRole, String userName) {
        super(CEGWCommandType.StopPacketCapture, equipmentQRCode, equipmentId, userRole, userName);
    }

    @Override
    public CEGWStopPacketCapture clone() {
        return (CEGWStopPacketCapture) super.clone();
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        return false;
    }

}
