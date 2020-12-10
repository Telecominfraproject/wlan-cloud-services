package com.telecominfraproject.wlan.profile.passpoint.models.operator;

import java.util.HashSet;
import java.util.Locale;
import java.util.Objects;
import java.util.Set;

import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.profile.models.ProfileDetails;
import com.telecominfraproject.wlan.profile.models.ProfileType;
import com.telecominfraproject.wlan.profile.passpoint.models.PasspointDuple;


public class PasspointOperatorProfile extends ProfileDetails implements PushableConfiguration<PasspointOperatorProfile> {


    /**
     * 
     */
    private static final long serialVersionUID = -4766966404203380604L;
    // OSEN
    private boolean serverOnlyAuthenticatedL2EncryptionNetwork;
    private String x509CertificateLocation;
    private Set<PasspointDuple> operatorFriendlyName;
    private String defaultOperatorFriendlyName = "Default friendly passpoint_operator name";
    private String defaultOperatorFriendlyNameFr = "Nom de l'opérateur convivial par défaut";
    private Set<String> domainNameList;

    private PasspointOperatorProfile() {
        serverOnlyAuthenticatedL2EncryptionNetwork = false;
        x509CertificateLocation = "/etc/ca.pem";
        operatorFriendlyName = new HashSet<>();
        PasspointDuple enFriendlyName = PasspointDuple.createWithDefaults();
        enFriendlyName.setDupleName(defaultOperatorFriendlyName);
        operatorFriendlyName.add(enFriendlyName);
        PasspointDuple frFriendlyName = PasspointDuple.createWithDefaults();
        frFriendlyName.setLocale(Locale.CANADA_FRENCH);
        frFriendlyName.setDupleName(defaultOperatorFriendlyNameFr);
        operatorFriendlyName.add(frFriendlyName);
    }

    public static PasspointOperatorProfile createWithDefaults() {
        return new PasspointOperatorProfile();
    }

    @Override
    public ProfileType getProfileType() {
        return ProfileType.passpoint_operator;
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


    public Set<PasspointDuple> getOperatorFriendlyName() {
        return operatorFriendlyName;
    }


    public void setOperatorFriendlyName(Set<PasspointDuple> operatorFriendlyName) {
        this.operatorFriendlyName = operatorFriendlyName;
    }

    public Set<String> getDomainNameList() {
        return domainNameList;
    }

    public void setDomainNameList(Set<String> domainNameList) {
        this.domainNameList = domainNameList;
    }

    @Override
    public boolean needsToBeUpdatedOnDevice(PasspointOperatorProfile previousVersion) {
        if (this.equals(previousVersion)) {
            return false;
        }
        return true;
    }

    @Override
    public PasspointOperatorProfile clone() {
        PasspointOperatorProfile returnValue = (PasspointOperatorProfile) super.clone();
        if (this.operatorFriendlyName != null)
            returnValue.operatorFriendlyName = this.operatorFriendlyName;
        returnValue.serverOnlyAuthenticatedL2EncryptionNetwork = this.serverOnlyAuthenticatedL2EncryptionNetwork;
        returnValue.x509CertificateLocation = this.x509CertificateLocation;
        returnValue.domainNameList = this.domainNameList;
        return returnValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(defaultOperatorFriendlyName, defaultOperatorFriendlyNameFr, domainNameList,
                operatorFriendlyName, serverOnlyAuthenticatedL2EncryptionNetwork, x509CertificateLocation);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PasspointOperatorProfile other = (PasspointOperatorProfile) obj;
        return Objects.equals(defaultOperatorFriendlyName, other.defaultOperatorFriendlyName)
                && Objects.equals(defaultOperatorFriendlyNameFr, other.defaultOperatorFriendlyNameFr)
                && Objects.equals(domainNameList, other.domainNameList)
                && Objects.equals(operatorFriendlyName, other.operatorFriendlyName)
                && serverOnlyAuthenticatedL2EncryptionNetwork == other.serverOnlyAuthenticatedL2EncryptionNetwork
                && Objects.equals(x509CertificateLocation, other.x509CertificateLocation);
    }

   


}
