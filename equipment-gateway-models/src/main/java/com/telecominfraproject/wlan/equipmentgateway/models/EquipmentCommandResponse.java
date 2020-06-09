package com.telecominfraproject.wlan.equipmentgateway.models;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

public class EquipmentCommandResponse extends BaseJsonModel {

	private static final long serialVersionUID = 5977811650122183402L;
	
    private CEGWCommandResultCode resultCode;

    /**
     * Detail for the result
     */
    private String resultDetail;
    
    private CEGWBaseCommand command;
    
    private String gatewayHost;
    private int gatewayPort;

	/**
     * @param resultCode
     * @param resultDetail
     * @param command - equipment command for which this response is generated
     */
    public EquipmentCommandResponse(CEGWCommandResultCode resultCode, String resultDetail, CEGWBaseCommand command, String gatewayHost, int gatewayPort) {
        this.resultCode = resultCode;
        this.resultDetail = resultDetail;
        this.command = command;
        this.gatewayHost = gatewayHost;
        this.gatewayPort = gatewayPort;
    }

    public EquipmentCommandResponse() {
    }


    public CEGWCommandResultCode getResultCode() {
        return resultCode;
    }

    @Override
    public EquipmentCommandResponse clone() {
        return (EquipmentCommandResponse) super.clone();
    }

    public String getResultDetail() {
        return resultDetail;
    }

    public void setResultDetail(String resultDetail) {
        this.resultDetail = resultDetail;
    }

	public CEGWBaseCommand getCommand() {
		return command;
	}

	public void setCommand(CEGWBaseCommand command) {
		this.command = command;
	}

	public void setResultCode(CEGWCommandResultCode resultCode) {
		this.resultCode = resultCode;
	}

	public String getGatewayHost() {
		return gatewayHost;
	}

	public void setGatewayHost(String gatewayHost) {
		this.gatewayHost = gatewayHost;
	}

	public int getGatewayPort() {
		return gatewayPort;
	}

	public void setGatewayPort(int gatewayPort) {
		this.gatewayPort = gatewayPort;
	}

}
