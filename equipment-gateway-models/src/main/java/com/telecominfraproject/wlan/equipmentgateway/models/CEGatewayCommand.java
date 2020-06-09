package com.telecominfraproject.wlan.equipmentgateway.models;

public class CEGatewayCommand extends CEGWBaseCommand {

	private static final long serialVersionUID = 4545056375390115226L;

	protected CEGatewayCommand() {
    }

    protected CEGatewayCommand(CEGWCommandType commandType, String inventoryId, long equipmentId) {
        super(commandType, inventoryId, equipmentId);
    }
}
