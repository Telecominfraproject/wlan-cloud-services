package com.telecominfraproject.wlan.status.equipment.models;

import java.util.Objects;

import com.telecominfraproject.wlan.status.models.StatusDataType;
import com.telecominfraproject.wlan.status.models.StatusDetails;

/**
 * Status of the Remote Debug Session for the Customer Equipment
 * 
 * @author dtop
 *
 */
public class EquipmentDebugSessionStatusData extends StatusDetails {

    private static final long serialVersionUID = 5282800813124689810L;

    /**
     * Developer's name taken from the CN of the client certificate that was used to initiate the debug session 
     */
    private String sessionUser;
    
    /**
     * Port on which portForwarder gateway is listening for ssh connections for this session
     */
    private int portForwarderGatewayPort;
    
    /**
     * Local port on equipment for which packets are being forwarded to portForwarder gateway  
     */
    private int equipmentPort;

    
    public EquipmentDebugSessionStatusData() {
        //for serialization
    }
    
    public EquipmentDebugSessionStatusData(String sessionUser, int portForwarderGatewayPort, int equipmentPort) {
        this.sessionUser = sessionUser;
        this.portForwarderGatewayPort = portForwarderGatewayPort;
        this.equipmentPort = equipmentPort;
    }


    @Override
    public StatusDataType getStatusDataType() {
    	return StatusDataType.EQUIPMENT_DEBUG_SESSION;
    }
    
    
    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }
        return false;
    }

    public String getSessionUser() {
        return sessionUser;
    }

    public void setSessionUser(String sessionUser) {
        this.sessionUser = sessionUser;
    }

    public int getPortForwarderGatewayPort() {
        return portForwarderGatewayPort;
    }

    public void setPortForwarderGatewayPort(int portForwarderGatewayPort) {
        this.portForwarderGatewayPort = portForwarderGatewayPort;
    }

    public int getEquipmentPort() {
        return equipmentPort;
    }

    public void setEquipmentPort(int equipmentPort) {
        this.equipmentPort = equipmentPort;
    }

    @Override
    public int hashCode() {
        return Objects.hash(equipmentPort, portForwarderGatewayPort, sessionUser);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof EquipmentDebugSessionStatusData)) {
            return false;
        }
        EquipmentDebugSessionStatusData other = (EquipmentDebugSessionStatusData) obj;
        return equipmentPort == other.equipmentPort && portForwarderGatewayPort == other.portForwarderGatewayPort
                && Objects.equals(sessionUser, other.sessionUser);
    }
    
    @Override
    public EquipmentDebugSessionStatusData clone() {
        return (EquipmentDebugSessionStatusData) super.clone();
    }
}
