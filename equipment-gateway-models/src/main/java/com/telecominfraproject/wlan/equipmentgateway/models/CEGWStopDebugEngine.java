/**
 *
 */
package com.telecominfraproject.wlan.equipmentgateway.models;

/**
 * @author dtop
 *
 */
public class CEGWStopDebugEngine extends EquipmentCommand {
    /**
     *
     */

    /**
     *
     */
    private static final long serialVersionUID = -3205754604387989341L;

    /**
     * Constructor
     * 
     * @param equipmentQRCode
     * @param equipmentId
     */
    public CEGWStopDebugEngine(String equipmentQRCode, long equipmentId) {
        super(CEGWCommandType.StopDebugEngine, equipmentQRCode, equipmentId);
    }

    /**
     * Constructor used by JSON
     */
    public CEGWStopDebugEngine() {
        super(CEGWCommandType.StopDebugEngine, null, 0);
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        return false;
    }

}
