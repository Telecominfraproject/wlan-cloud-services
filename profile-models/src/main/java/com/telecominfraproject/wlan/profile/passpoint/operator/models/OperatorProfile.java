package com.telecominfraproject.wlan.profile.passpoint.operator.models;

import java.util.Objects;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.profile.models.ProfileDetails;
import com.telecominfraproject.wlan.profile.models.ProfileType;


public class OperatorProfile extends ProfileDetails implements PushableConfiguration<OperatorProfile> {


    /**
     * 
     */
    private static final long serialVersionUID = -4766966404203380604L;
    private String domainName;
    // OSEN
    private boolean serverOnlyAuthenticatedL2EncryptionNetwork;
    private String x509CertificateLocation;
    private OperatorFriendlyName operatorFriendlyName;

    private OperatorProfile() {
        domainName = "telecominfraproject.atlassian.net";
        serverOnlyAuthenticatedL2EncryptionNetwork = false;
        x509CertificateLocation = "/etc/ca.pem";
        operatorFriendlyName = OperatorFriendlyName.createWithDefaults();
    }

    public static OperatorProfile createWithDefaults() {
        return new OperatorProfile();
    }

    @Override
    public ProfileType getProfileType() {
        return ProfileType.operator;
    }


    public String getDomainName() {
        return domainName;
    }


    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }


    public boolean isServerOnlyAuthenticatedL2EncryptionNetwork() {
        return serverOnlyAuthenticatedL2EncryptionNetwork;
    }


    public void setServerOnlyAuthenticatedL2EncryptionNetwork(boolean serverOnlyAuthenticatedL2EncryptionNetwork) {
        this.serverOnlyAuthenticatedL2EncryptionNetwork = serverOnlyAuthenticatedL2EncryptionNetwork;
    }


    public String getX509CertificateLocation() {
        return x509CertificateLocation;
    }


    public void setX509CertificateLocation(String x509CertificateLocation) {
        this.x509CertificateLocation = x509CertificateLocation;
    }


    public OperatorFriendlyName getOperatorFriendlyName() {
        return operatorFriendlyName;
    }


    public void setOperatorFriendlyName(OperatorFriendlyName operatorFriendlyName) {
        this.operatorFriendlyName = operatorFriendlyName;
    }

    @Override
    public boolean needsToBeUpdatedOnDevice(OperatorProfile previousVersion) {
        if (this.equals(previousVersion)) {
            return false;
        }
        return true;
    }

    @Override
    public OperatorProfile clone() {
        OperatorProfile returnValue = (OperatorProfile) super.clone();
        returnValue.domainName = this.domainName;
        returnValue.operatorFriendlyName = this.operatorFriendlyName;
        returnValue.serverOnlyAuthenticatedL2EncryptionNetwork = this.serverOnlyAuthenticatedL2EncryptionNetwork;
        returnValue.x509CertificateLocation = this.x509CertificateLocation;
        return returnValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(domainName, operatorFriendlyName, serverOnlyAuthenticatedL2EncryptionNetwork,
                x509CertificateLocation);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof OperatorProfile)) {
            return false;
        }
        OperatorProfile other = (OperatorProfile) obj;
        return Objects.equals(domainName, other.domainName)
                && Objects.equals(operatorFriendlyName, other.operatorFriendlyName)
                && serverOnlyAuthenticatedL2EncryptionNetwork == other.serverOnlyAuthenticatedL2EncryptionNetwork
                && Objects.equals(x509CertificateLocation, other.x509CertificateLocation);
    }

}
