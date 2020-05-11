package com.telecominfraproject.wlan.routing.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.core.model.service.GatewayType;

/**
 * @author mpreston
 *
 */
public class EquipmentGatewayRecord extends BaseJsonModel {

    /**
     * 
     */
    private static final long serialVersionUID = -8372191012659972427L;

    private long id;

    /** Unique identifier for the CustomerEquipmentGW */
    private String hostname;

    /** IP Address of the CustomerEquipmentGW */
    private String ipAddr;

    /** Port for the CustomerEquipmentGW */
    private int port;

    /**
     * Gateway type
     */
    private GatewayType gatewayType;

    ////////////////////////////////////////////////////////////////////////////

    private long createdTimestamp;
    private long lastModifiedTimestamp;

    public EquipmentGatewayRecord(GatewayType serviceType) {
        this.gatewayType = serviceType;
    }

    public EquipmentGatewayRecord() {
        this(GatewayType.CEGW);
    }

    @Override
    public EquipmentGatewayRecord clone() {
        return (EquipmentGatewayRecord) super.clone();
    }

    public long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public String getHostname() {
        return hostname;
    }

    public long getId() {
        return id;
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public long getLastModifiedTimestamp() {
        return lastModifiedTimestamp;
    }

    public int getPort() {
        return port;
    }

    public GatewayType getGatewayType() {
        return gatewayType;
    }


    @Override
    public boolean hasUnsupportedValue() {
        return super.hasUnsupportedValue() || GatewayType.isUnsupported(this.gatewayType);
    }

    public void setCreatedTimestamp(long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr;
    }

    public void setLastModifiedTimestamp(long lastModifiedTimestamp) {
        this.lastModifiedTimestamp = lastModifiedTimestamp;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setGatewayType(GatewayType gatewayType) {
        this.gatewayType = gatewayType;
    }

	@Override
	public int hashCode() {
		return Objects.hash(createdTimestamp, hostname, id, ipAddr, lastModifiedTimestamp, port, gatewayType);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof EquipmentGatewayRecord)) {
			return false;
		}
		EquipmentGatewayRecord other = (EquipmentGatewayRecord) obj;
		return createdTimestamp == other.createdTimestamp && Objects.equals(hostname, other.hostname)
				&& id == other.id && Objects.equals(ipAddr, other.ipAddr)
				&& lastModifiedTimestamp == other.lastModifiedTimestamp && port == other.port
				&& gatewayType == other.gatewayType;
	}

}
