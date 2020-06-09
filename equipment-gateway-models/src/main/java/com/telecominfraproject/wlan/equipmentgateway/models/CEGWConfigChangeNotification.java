package com.telecominfraproject.wlan.equipmentgateway.models;

public class CEGWConfigChangeNotification extends EquipmentCommand {


	private static final long serialVersionUID = 4401284478686864193L;

	/**
     * Constructor
     * 
     * @param inventoryId
     * @param equipmentId
     */
    public CEGWConfigChangeNotification(String inventoryId, long equipmentId) {
        super(CEGWCommandType.ConfigChangeNotification, inventoryId, equipmentId);
    }

    /**
     * Constructor used by JSON
     */
    protected CEGWConfigChangeNotification() {
        super(CEGWCommandType.ConfigChangeNotification, null, 0);
    }
}
