/**
 *
 */
package com.telecominfraproject.wlan.equipmentgateway.models;

/**
 * @author mpreston
 *
 */
public class CEGWReportCurrentVLANRequest extends EquipmentCommand {

    /**
     *
     */

    /**
     *
     */
    private static final long serialVersionUID = -3919064524706577505L;

    /**
     * Constructor
     *
     * @param equipmentQRCode
     * @param equipmentId
     */
    public CEGWReportCurrentVLANRequest(String equipmentQRCode, long equipmentId) {
        super(CEGWCommandType.ReportCurrentVLANRequest, equipmentQRCode, equipmentId);
    }

    /**
     * Constructor used by JSON
     */
    protected CEGWReportCurrentVLANRequest() {
        super(CEGWCommandType.ReportCurrentVLANRequest, null, 0);
    }

    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        return false;
    }

}
