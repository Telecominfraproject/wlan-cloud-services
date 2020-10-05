package com.telecominfraproject.wlan.profile.passpoint.operator.models;

import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.profile.models.ProfileDetails;
import com.telecominfraproject.wlan.profile.models.ProfileType;
import com.telecominfraproject.wlan.profile.passpoint.models.Hotspot20Duple;


public class OperatorProfile extends ProfileDetails implements PushableConfiguration<OperatorProfile> {


    /**
     * 
     */
    private static final long serialVersionUID = -4766966404203380604L;
    private String domainName;
    // OSEN
    private boolean serverOnlyAuthenticatedL2EncryptionNetwork;
    private String x509CertificateLocation;
    private Set<Hotspot20Duple> operatorFriendlyName;
    private String defaultOperatorFriendlyName = "Default friendly operator name";
    private String defaultOperatorFriendlyNameFr = "Nom de l'opérateur convivial par défaut";

    private OperatorProfile() {
        domainName = "telecominfraproject.atlassian.net";
        serverOnlyAuthenticatedL2EncryptionNetwork = false;
        x509CertificateLocation = "/etc/ca.pem";
        operatorFriendlyName = new HashSet<>();
        Hotspot20Duple enFriendlyName = Hotspot20Duple.createWithDefaults();
        enFriendlyName.setDupleName(defaultOperatorFriendlyName);
        operatorFriendlyName.add(enFriendlyName);
        Hotspot20Duple frFriendlyName = Hotspot20Duple.createWithDefaults();
        frFriendlyName.setLocale(Locale.CANADA_FRENCH);
        frFriendlyName.setDupleName(defaultOperatorFriendlyNameFr);
        operatorFriendlyName.add(frFriendlyName);
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


    public Set<Hotspot20Duple> getOperatorFriendlyName() {
        return operatorFriendlyName;
    }


    public void setOperatorFriendlyName(Set<Hotspot20Duple> operatorFriendlyName) {
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
        if (this.operatorFriendlyName != null)
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
