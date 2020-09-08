package com.telecominfraproject.wlan.systemevent.equipment.debug;

import java.util.Objects;

import com.telecominfraproject.wlan.systemevent.models.SystemEvent;

/**
 * @author dtop
 *
 */
public class EquipmentDebugSessionStartedEvent extends SystemEvent {
    private static final long serialVersionUID = -2430769929028051157L;

    private int customerId;
    private long equipmentId;
    private long eventTimestamp;
    
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

    public EquipmentDebugSessionStartedEvent() {
        // for serialization
    }
    
    public EquipmentDebugSessionStartedEvent(int customerId, long equipmentId, long eventTimestamp, String sessionUser,
            int portForwarderGatewayPort, int equipmentPort) {
        this.customerId = customerId;
        this.equipmentId = equipmentId;
        this.eventTimestamp = eventTimestamp;
        this.sessionUser = sessionUser;
        this.portForwarderGatewayPort = portForwarderGatewayPort;
        this.equipmentPort = equipmentPort;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(long equipmentId) {
        this.equipmentId = equipmentId;
    }

    public long getEventTimestamp() {
        return eventTimestamp;
    }

    public void setEventTimestamp(long eventTimestamp) {
        this.eventTimestamp = eventTimestamp;
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
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(customerId, equipmentId, equipmentPort, eventTimestamp,
                portForwarderGatewayPort, sessionUser);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        if (!(obj instanceof EquipmentDebugSessionStartedEvent)) {
            return false;
        }
        EquipmentDebugSessionStartedEvent other = (EquipmentDebugSessionStartedEvent) obj;
        return customerId == other.customerId && equipmentId == other.equipmentId
                && equipmentPort == other.equipmentPort && eventTimestamp == other.eventTimestamp
                && portForwarderGatewayPort == other.portForwarderGatewayPort
                && Objects.equals(sessionUser, other.sessionUser);
    }

    @Override
    public EquipmentDebugSessionStartedEvent clone() {
        return (EquipmentDebugSessionStartedEvent) super.clone();
    }    
}
