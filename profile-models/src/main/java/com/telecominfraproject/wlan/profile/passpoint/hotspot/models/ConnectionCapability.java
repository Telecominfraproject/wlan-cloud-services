package com.telecominfraproject.wlan.profile.passpoint.hotspot.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.profile.passpoint.models.ConnectionCapabilitiesIpProtocol;
import com.telecominfraproject.wlan.profile.passpoint.models.ConnectionCapabilitiesStatus;


public class ConnectionCapability extends BaseJsonModel implements PushableConfiguration<ConnectionCapability> {

    private static final long serialVersionUID = 2984364475752127139L;
    private int connectionCapabilitiesPortNumber;

    private ConnectionCapabilitiesIpProtocol connectionCapabilitiesIpProtocol;

    private ConnectionCapabilitiesStatus connectionCapabilitiesStatus;

    private ConnectionCapability() {
        setConnectionCapabilitiesIpProtocol(ConnectionCapabilitiesIpProtocol.TCP);
        setConnectionCapabilitiesPortNumber(8888);
        setConnectionCapabilitiesStatus(ConnectionCapabilitiesStatus.open);
    }

    public static ConnectionCapability createWithDefaults() {
        return new ConnectionCapability();
    }

    @Override
    public boolean needsToBeUpdatedOnDevice(ConnectionCapability previousVersion) {
        if (this.equals(previousVersion))
            return false;
        return true;
    }

    public ConnectionCapabilitiesIpProtocol getConnectionCapabilitiesIpProtocol() {
        return connectionCapabilitiesIpProtocol;
    }

    public void setConnectionCapabilitiesIpProtocol(ConnectionCapabilitiesIpProtocol connectionCapabilitiesIpProtocol) {
        this.connectionCapabilitiesIpProtocol = connectionCapabilitiesIpProtocol;
    }


    public ConnectionCapabilitiesStatus getConnectionCapabilitiesStatus() {
        return connectionCapabilitiesStatus;
    }


    public void setConnectionCapabilitiesStatus(ConnectionCapabilitiesStatus connectionCapabilitiesStatus) {
        this.connectionCapabilitiesStatus = connectionCapabilitiesStatus;
    }

    public int getConnectionCapabilitiesPortNumber() {
        return connectionCapabilitiesPortNumber;
    }

    public void setConnectionCapabilitiesPortNumber(int connectionCapabilitiesPortNumber) {
        this.connectionCapabilitiesPortNumber = connectionCapabilitiesPortNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(connectionCapabilitiesIpProtocol, connectionCapabilitiesPortNumber,
                connectionCapabilitiesStatus);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ConnectionCapability)) {
            return false;
        }
        ConnectionCapability other = (ConnectionCapability) obj;
        return Objects.equals(connectionCapabilitiesIpProtocol, other.connectionCapabilitiesIpProtocol)
                && connectionCapabilitiesPortNumber == other.connectionCapabilitiesPortNumber
                && Objects.equals(connectionCapabilitiesStatus, other.connectionCapabilitiesStatus);
    }
    
    
}
