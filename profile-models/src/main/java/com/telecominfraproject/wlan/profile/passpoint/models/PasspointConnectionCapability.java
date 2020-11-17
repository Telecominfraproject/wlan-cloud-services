package com.telecominfraproject.wlan.profile.passpoint.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;


public class PasspointConnectionCapability extends BaseJsonModel implements PushableConfiguration<PasspointConnectionCapability> {

    private static final long serialVersionUID = 2984364475752127139L;
    private int connectionCapabilitiesPortNumber;

    private PasspointConnectionCapabilitiesIpProtocol passpointConnectionCapabilitiesIpProtocol;

    private PasspointConnectionCapabilitiesStatus passpointConnectionCapabilitiesStatus;

    private PasspointConnectionCapability() {
        setConnectionCapabilitiesIpProtocol(PasspointConnectionCapabilitiesIpProtocol.TCP);
        setConnectionCapabilitiesPortNumber(8888);
        setConnectionCapabilitiesStatus(PasspointConnectionCapabilitiesStatus.open);
    }

    public static PasspointConnectionCapability createWithDefaults() {
        return new PasspointConnectionCapability();
    }

    @Override
    public boolean needsToBeUpdatedOnDevice(PasspointConnectionCapability previousVersion) {
        if (this.equals(previousVersion))
            return false;
        return true;
    }

    public PasspointConnectionCapabilitiesIpProtocol getConnectionCapabilitiesIpProtocol() {
        return passpointConnectionCapabilitiesIpProtocol;
    }

    public void setConnectionCapabilitiesIpProtocol(PasspointConnectionCapabilitiesIpProtocol passpointConnectionCapabilitiesIpProtocol) {
        this.passpointConnectionCapabilitiesIpProtocol = passpointConnectionCapabilitiesIpProtocol;
    }


    public PasspointConnectionCapabilitiesStatus getConnectionCapabilitiesStatus() {
        return passpointConnectionCapabilitiesStatus;
    }


    public void setConnectionCapabilitiesStatus(PasspointConnectionCapabilitiesStatus passpointConnectionCapabilitiesStatus) {
        this.passpointConnectionCapabilitiesStatus = passpointConnectionCapabilitiesStatus;
    }

    public int getConnectionCapabilitiesPortNumber() {
        return connectionCapabilitiesPortNumber;
    }

    public void setConnectionCapabilitiesPortNumber(int connectionCapabilitiesPortNumber) {
        this.connectionCapabilitiesPortNumber = connectionCapabilitiesPortNumber;
    }

    @Override
    public int hashCode() {
        return Objects.hash(passpointConnectionCapabilitiesIpProtocol, connectionCapabilitiesPortNumber,
                passpointConnectionCapabilitiesStatus);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PasspointConnectionCapability)) {
            return false;
        }
        PasspointConnectionCapability other = (PasspointConnectionCapability) obj;
        return Objects.equals(passpointConnectionCapabilitiesIpProtocol, other.passpointConnectionCapabilitiesIpProtocol)
                && connectionCapabilitiesPortNumber == other.connectionCapabilitiesPortNumber
                && Objects.equals(passpointConnectionCapabilitiesStatus, other.passpointConnectionCapabilitiesStatus);
    }
    
    
}
