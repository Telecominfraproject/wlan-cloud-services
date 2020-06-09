package com.telecominfraproject.wlan.equipmentgateway.models;

public class CEGWStartDebugEngine extends EquipmentCommand {

	private static final long serialVersionUID = -6768986657458247552L;
	
	/**
     * Gateway hostname
     */
    private String gatewayHostname;
    /**
     * Gateway port
     */
    private Integer gatewayPort;

    /**
     * Constructor
     * 
     * @param inventoryId
     * @param equipmentId
     * @param gatewayHostname
     * @param gatewayPort
     */
    public CEGWStartDebugEngine(String inventoryId, long equipmentId, String gatewayHostname, Integer gatewayPort) {
        super(CEGWCommandType.StartDebugEngine, inventoryId, equipmentId);
        this.setGatewayHostname(gatewayHostname);
        this.setGatewayPort(gatewayPort);
    }

    /**
     * Constructor used by JSON
     */
    public CEGWStartDebugEngine() {
        super(CEGWCommandType.StartDebugEngine, null, 0);
    }

    public String getGatewayHostname() {
        return gatewayHostname;
    }

    public void setGatewayHostname(String gatewayHostname) {
        this.gatewayHostname = gatewayHostname;
    }

    public Integer getGatewayPort() {
        return gatewayPort;
    }

    public void setGatewayPort(Integer gatewayPort) {
        this.gatewayPort = gatewayPort;
    }
    
    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        return false;
    }
}
