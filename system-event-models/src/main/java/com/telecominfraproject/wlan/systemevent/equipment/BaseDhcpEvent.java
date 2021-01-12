/**
 * 
 */
package com.telecominfraproject.wlan.systemevent.equipment;

import java.net.InetAddress;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.systemevent.models.SystemEvent;

public abstract class BaseDhcpEvent extends SystemEvent {

    private static final long serialVersionUID = 606411494287591881L;
    private int xId;
    private int vlanId;
    private InetAddress dhcpServerIp;
    private InetAddress clientIp;
    private InetAddress relayIp;
    private MacAddress deviceMacAddress;
    private long sessionId; // association sessionid
    private int customerId;
    private long equipmentId;

    public BaseDhcpEvent(int customerId, long equipmentId, long eventTimestamp, Long sessionId) {
        super(eventTimestamp);
        this.customerId = customerId;
        this.equipmentId = equipmentId;
        if (sessionId != null) {
            setSessionId(sessionId);
        } else {
            setSessionId(0L);
        }
    }

    public BaseDhcpEvent() {
        // for serialization
    }

    public MacAddress getDeviceMacAddress() {
        return deviceMacAddress;
    }

    public void setDeviceMacAddress(MacAddress deviceMacAddress) {
        this.deviceMacAddress = deviceMacAddress;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    @Override
    public int getCustomerId() {
        return customerId;
    }

    public void setEquipmentId(long equipmentId) {
        this.equipmentId = equipmentId;
    }

    @Override
    public long getEquipmentId() {
        return equipmentId;
    }

    public int getxId() {
        return xId;
    }

    public void setxId(int xId) {
        this.xId = xId;
    }

    public Integer getVlanId() {
        return vlanId;
    }

    public void setVlanId(Integer vlanId) {
        this.vlanId = vlanId;
    }

    public InetAddress getDhcpServerIp() {
        return dhcpServerIp;
    }

    public void setDhcpServerIp(InetAddress dhcpServerIp) {
        this.dhcpServerIp = dhcpServerIp;
    }

    public InetAddress getClientIp() {
        return clientIp;
    }

    public void setClientIp(InetAddress clientIp) {
        this.clientIp = clientIp;
    }

    public InetAddress getRelayIp() {
        return relayIp;
    }

    public void setRelayIp(InetAddress relayIp) {
        this.relayIp = relayIp;
    }

    public long getSessionId() {
        return sessionId;
    }

    public void setSessionId(long sessionId) {
        this.sessionId = sessionId;
    }
    
    @Override
    public BaseDhcpEvent clone() {
        BaseDhcpEvent ret = (BaseDhcpEvent) super.clone();
        
        return ret;
    }


    @Override
    public boolean hasUnsupportedValue() {
        if (super.hasUnsupportedValue()) {
            return true;
        }

        if (hasUnsupportedValue(deviceMacAddress)) {
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(clientIp, customerId, deviceMacAddress, dhcpServerIp, equipmentId,
                relayIp, sessionId, vlanId, xId);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        BaseDhcpEvent other = (BaseDhcpEvent) obj;
        return Objects.equals(clientIp, other.clientIp) && customerId == other.customerId
                && Objects.equals(deviceMacAddress, other.deviceMacAddress)
                && Objects.equals(dhcpServerIp, other.dhcpServerIp) && equipmentId == other.equipmentId
                && Objects.equals(relayIp, other.relayIp) && sessionId == other.sessionId && vlanId == other.vlanId
                && xId == other.xId;
    }
    
    
}
