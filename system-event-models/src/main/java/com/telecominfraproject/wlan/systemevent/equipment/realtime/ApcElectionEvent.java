package com.telecominfraproject.wlan.systemevent.equipment.realtime;

import java.net.InetAddress;
import java.util.Objects;

import com.telecominfraproject.wlan.core.model.json.interfaces.HasCustomerId;
import com.telecominfraproject.wlan.core.model.json.interfaces.HasEquipmentId;

public class ApcElectionEvent extends RealTimeEvent implements HasCustomerId, HasEquipmentId {

    private static final long serialVersionUID = 8407097410957051875L;

    public enum ApcMode {
        BDR, DR, SR, NC
    }

    /**
     * IP address of the APC
     */
    private InetAddress apcDesignatedRouterIpAddress;

    /**
     * Backup IP address of the APC
     */
    private InetAddress apcBackupDesignatedRouterIpAddress;

    /**
     * IPv4 address of the equipment
     */
    private InetAddress localIpAddress;

    /**
     * IPv4 address of the RADIUS proxy server.
     */
    private InetAddress radiusProxyAddress;

    /**
     * APC mode of the Equipment
     */
    private ApcMode apcMode;
    
    /**
     * Is enabled
     */
    private Boolean enabled;

    public ApcElectionEvent() {
        super();
    }

    public ApcElectionEvent(RealTimeEventType eventType, int customerId, long locationId, long equipmentId,
            Long timestamp) {
        super(RealTimeEventType.APC_Election_event, customerId, locationId, equipmentId, timestamp);
    }

    public ApcElectionEvent(InetAddress apcDesignatedRouterIpAddress, InetAddress apcBackupDesignatedRouterIpAddress,
            InetAddress equipmentIpAddress, InetAddress radiusProxyAddress, String apcMode, Boolean isApcEnabled, RealTimeEventType eventType,
            int customerId, long locationId, long equipmentId, Long timestamp) {
        this(RealTimeEventType.APC_Election_event, customerId, locationId, equipmentId, timestamp);
        this.apcDesignatedRouterIpAddress = apcDesignatedRouterIpAddress;
        this.apcBackupDesignatedRouterIpAddress = apcBackupDesignatedRouterIpAddress;
        this.localIpAddress = equipmentIpAddress;
        this.radiusProxyAddress = radiusProxyAddress;
        this.apcMode = ApcMode.valueOf(apcMode);
        this.enabled = isApcEnabled;
    }

    public InetAddress getApcDesignatedRouterIpAddress() {
        return apcDesignatedRouterIpAddress;
    }

    public void setApcDesignatedRouterIpAddress(InetAddress apcDesignatedRouterIpAddress) {
        this.apcDesignatedRouterIpAddress = apcDesignatedRouterIpAddress;
    }

    public InetAddress getApcBackupDesignatedRouterIpAddress() {
        return apcBackupDesignatedRouterIpAddress;
    }

    public void setApcBackupDesignatedRouterIpAddress(InetAddress apcBackupDesignatedRouterIpAddress) {
        this.apcBackupDesignatedRouterIpAddress = apcBackupDesignatedRouterIpAddress;
    }

    public InetAddress getLocalIpAddress() {
        return localIpAddress;
    }

    public void setLocalIpAddress(InetAddress localIpAddress) {
        this.localIpAddress = localIpAddress;
    }

    public InetAddress getRadiusProxyAddress() {
        return radiusProxyAddress;
    }

    public void setRadiusProxyAddress(InetAddress radiusProxyAddress) {
        this.radiusProxyAddress = radiusProxyAddress;
    }

    public ApcMode getApcMode() {
        return apcMode;
    }

    public void setApcMode(ApcMode apcMode) {
        this.apcMode = apcMode;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + Objects.hash(apcBackupDesignatedRouterIpAddress, apcDesignatedRouterIpAddress,
                apcMode, enabled, localIpAddress, radiusProxyAddress);
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
        ApcElectionEvent other = (ApcElectionEvent) obj;
        return Objects.equals(apcBackupDesignatedRouterIpAddress, other.apcBackupDesignatedRouterIpAddress)
                && Objects.equals(apcDesignatedRouterIpAddress, other.apcDesignatedRouterIpAddress)
                && apcMode == other.apcMode && Objects.equals(enabled, other.enabled)
                && Objects.equals(localIpAddress, other.localIpAddress)
                && Objects.equals(radiusProxyAddress, other.radiusProxyAddress);
    }

    
}
