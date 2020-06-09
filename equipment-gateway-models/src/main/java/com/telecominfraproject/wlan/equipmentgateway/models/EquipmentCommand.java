package com.telecominfraproject.wlan.equipmentgateway.models;

import com.telecominfraproject.wlan.core.model.role.PortalUserRole;

public class EquipmentCommand extends CEGWBaseCommand {

	private static final long serialVersionUID = -1981617366942595011L;
	
	protected EquipmentCommand() {
    }

    protected EquipmentCommand(CEGWCommandType commandType, String inventoryId, long equipmentId) {
        this(commandType, inventoryId, equipmentId, null, null);
    }

    protected EquipmentCommand(CEGWCommandType commandType, String inventoryId, long equipmentId,
            PortalUserRole userRole, final String userName) {
        super(commandType, inventoryId, equipmentId, userRole, userName);
    }

}
