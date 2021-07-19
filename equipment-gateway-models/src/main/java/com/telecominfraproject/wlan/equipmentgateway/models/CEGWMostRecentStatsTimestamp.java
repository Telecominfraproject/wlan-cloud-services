package com.telecominfraproject.wlan.equipmentgateway.models;

public class CEGWMostRecentStatsTimestamp extends CEGatewayCommand {

    private static final long serialVersionUID = -9087897865195978158L;

    public CEGWMostRecentStatsTimestamp() {    
    }
    
    public CEGWMostRecentStatsTimestamp(CEGWCommandType commandType, String inventoryId, long equipmentId) {
        super(commandType,inventoryId,equipmentId);
    }
}
