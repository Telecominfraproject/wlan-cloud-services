package com.telecominfraproject.wlan.equipmentgateway.models;

public class CEGWCloseSessionRequest extends CEGatewayCommand {


	private static final long serialVersionUID = -263965970528271895L;

	public CEGWCloseSessionRequest(String inventoryId, long equipmentId) {
        super(CEGWCommandType.CloseSessionRequest, inventoryId, equipmentId);
    }

    /**
     * Constructor used by JSON
     */
    public CEGWCloseSessionRequest() {
        super(CEGWCommandType.CloseSessionRequest, null, 0);
    }

    @Override
    public CEGWCloseSessionRequest clone() {
        return (CEGWCloseSessionRequest) super.clone();
    }

}
