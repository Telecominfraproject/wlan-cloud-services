package com.telecominfraproject.wlan.profile.passpoint.models;

import java.net.URI;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.profile.models.ProfileDetails;
import com.telecominfraproject.wlan.profile.models.ProfileType;
import com.telecominfraproject.wlan.profile.models.common.FileCategory;
import com.telecominfraproject.wlan.profile.models.common.FileType;
import com.telecominfraproject.wlan.profile.models.common.ManagedFileInfo;

public class PasspointProfile extends ProfileDetails implements PushableConfiguration<PasspointProfile> {

    private static final long serialVersionUID = -8153311150616202124L;

    private boolean enableInterworkingAndHs20;

    private MacAddress hessid;

    private PasspointAccessNetworkType passpointAccessNetworkType;

    private PasspointNetworkAuthenticationType passpointNetworkAuthenticationType;

    private int additionalStepsRequiredForAccess;

    private int deauthRequestTimeout;

    private int operatingClass;

    private ManagedFileInfo termsAndConditionsFile;

    private String whitelistDomain;

    private boolean emergencyServicesReachable;

    private boolean unauthenticatedEmergencyServiceAccessible;

    private boolean internetConnectivity;

    private Set<PasspointConnectionCapability> connectionCapabilitySet;

    private String ipAddressTypeAvailability;

    private Set<String> qosMapSetConfiguration;

    private String apGeospatialLocation;

    private String apCivicLocation;

    private URI apPublicLocationIdUri;

    private PasspointGasAddress3Behaviour gasAddr3Behaviour;

    private int anqpDomainId;

    private boolean disableDownstreamGroupAddressedForwarding;

    private boolean enable2pt4GHz;
    private boolean enable5GHz;
   
    private List<Long> associatedAccessSsidProfileIds;

    private Long osuSsidProfileId;

    private Long passpointOperatorProfileId;

    private Long passpointVenueProfileId;
    
    private Set<Long> passpointOsuProviderProfileIds;

    private PasspointProfile() {

        setAccessNetworkType(PasspointAccessNetworkType.free_public_network);
        setAdditionalStepsRequiredForAccess(0);
        setAnqpDomainId(1234);
        connectionCapabilitySet = new HashSet<>();
        connectionCapabilitySet.add(PasspointConnectionCapability.createWithDefaults());
        setDeauthRequestTimeout(0);
        setDisableDownstreamGroupAddressedForwarding(false);
        setEmergencyServicesReachable(true);
        setEnableInterworkingAndHs20(true);
        setGasAddr3Behaviour(PasspointGasAddress3Behaviour.p2pSpecWorkaroundFromRequest);
        setInternetConnectivity(true);
        setIpAddressTypeAvailability(PasspointIPv4AddressType.public_IPv4_address_available.getName());
        setNetworkAuthenticationType(PasspointNetworkAuthenticationType.acceptance_of_terms_and_conditions);
        setOperatingClass(0);
        termsAndConditionsFile = new ManagedFileInfo();
        termsAndConditionsFile.setApExportUrl("https://localhost:9091/filestore/termsAndConditions");
        termsAndConditionsFile.setFileCategory(FileCategory.ExternalPolicyConfiguration);
        termsAndConditionsFile.setFileType(FileType.TEXT);
        enable2pt4GHz = true;
        enable5GHz = true;
    }

    public static PasspointProfile createWithDefaults() {
        return new PasspointProfile();
    }

    public boolean isEnableInterworkingAndHs20() {
        return enableInterworkingAndHs20;
    }

    public void setEnableInterworkingAndHs20(boolean enableInterworkingAndHs20) {
        this.enableInterworkingAndHs20 = enableInterworkingAndHs20;
    }

    @Override
    public ProfileType getProfileType() {
        return ProfileType.passpoint;
    }

    @Override
    public boolean needsToBeUpdatedOnDevice(PasspointProfile previousVersion) {
        if (this.equals(previousVersion))
            return false;
        return true;
    }

    public MacAddress getHessid() {
        return hessid;
    }

    public void setHessid(MacAddress hessid) {
        this.hessid = hessid;
    }

    public PasspointAccessNetworkType getAccessNetworkType() {
        return passpointAccessNetworkType;
    }

    public void setAccessNetworkType(PasspointAccessNetworkType passpointAccessNetworkType) {
        this.passpointAccessNetworkType = passpointAccessNetworkType;
    }

    public PasspointNetworkAuthenticationType getNetworkAuthenticationType() {
        return passpointNetworkAuthenticationType;
    }

    public void setNetworkAuthenticationType(PasspointNetworkAuthenticationType passpointNetworkAuthenticationType) {
        this.passpointNetworkAuthenticationType = passpointNetworkAuthenticationType;
    }

    public int getAdditionalStepsRequiredForAccess() {
        return additionalStepsRequiredForAccess;
    }

    public void setAdditionalStepsRequiredForAccess(int additionalStepsRequiredForAccess) {
        this.additionalStepsRequiredForAccess = additionalStepsRequiredForAccess;
    }

    public int getDeauthRequestTimeout() {
        return deauthRequestTimeout;
    }

    public void setDeauthRequestTimeout(int deauthRequestTimeout) {
        this.deauthRequestTimeout = deauthRequestTimeout;
    }

    public int getOperatingClass() {
        return operatingClass;
    }

    public void setOperatingClass(int operatingClass) {
        this.operatingClass = operatingClass;
    }

    public String getWhitelistDomain() {
        return whitelistDomain;
    }

    public void setWhitelistDomain(String whitelistDomain) {
        this.whitelistDomain = whitelistDomain;
    }

    public boolean isEmergencyServicesReachable() {
        return emergencyServicesReachable;
    }

    public void setEmergencyServicesReachable(boolean emergencyServicesReachable) {
        this.emergencyServicesReachable = emergencyServicesReachable;
    }

    public boolean isUnauthenticatedEmergencyServiceAccessible() {
        return unauthenticatedEmergencyServiceAccessible;
    }

    public void setUnauthenticatedEmergencyServiceAccessible(boolean unauthenticatedEmergencyServiceAccessible) {
        this.unauthenticatedEmergencyServiceAccessible = unauthenticatedEmergencyServiceAccessible;
    }

    public boolean isInternetConnectivity() {
        return internetConnectivity;
    }

    public void setInternetConnectivity(boolean internetConnectivity) {
        this.internetConnectivity = internetConnectivity;
    }

    
    public String getIpAddressTypeAvailability() {
        return ipAddressTypeAvailability;
    }
    
    public void setIpAddressTypeAvailability(String ipAddressTypeAvailability) {
        this.ipAddressTypeAvailability = ipAddressTypeAvailability;
    }

    public Set<String> getQosMapSetConfiguration() {
        //
        // QoS Map Set configuration
        //
        // <DSCP Exceptions[DSCP,UP]>,]<UP 0 range[low,high]>,...<UP 7
        // range[low,high
        //
        // per hosted.conf and 802.11u 7.3.2.95
        return qosMapSetConfiguration;
    }

    public void setQosMapSetConfiguration(Set<String> qosMapSetConfiguration) {
        this.qosMapSetConfiguration = qosMapSetConfiguration;
    }

    public String getApGeospatialLocation() {
        return apGeospatialLocation;
    }

    public void setApGeospatialLocation(String apGeospatialLocation) {
        this.apGeospatialLocation = apGeospatialLocation;
    }

    public String getApCivicLocation() {
        return apCivicLocation;
    }

    public void setApCivicLocation(String apCivicLocation) {
        this.apCivicLocation = apCivicLocation;
    }

    public URI getApPublicLocationIdUri() {
        return apPublicLocationIdUri;
    }

    public void setApPublicLocationIdUri(URI apPublicLocationIdUri) {
        this.apPublicLocationIdUri = apPublicLocationIdUri;
    }

    public PasspointGasAddress3Behaviour getGasAddr3Behaviour() {
        return gasAddr3Behaviour;
    }

    public void setGasAddr3Behaviour(PasspointGasAddress3Behaviour gasAddr3Behaviour) {
        this.gasAddr3Behaviour = gasAddr3Behaviour;
    }

    public int getAnqpDomainId() {
        return anqpDomainId;
    }

    public void setAnqpDomainId(int anqpDomainId) {
        this.anqpDomainId = anqpDomainId;
    }

    public boolean isDisableDownstreamGroupAddressedForwarding() {
        return disableDownstreamGroupAddressedForwarding;
    }

    public void setDisableDownstreamGroupAddressedForwarding(boolean disableDownstreamGroupAddressedForwarding) {
        this.disableDownstreamGroupAddressedForwarding = disableDownstreamGroupAddressedForwarding;
    }

    public ManagedFileInfo getTermsAndConditionsFile() {
        return termsAndConditionsFile;
    }

    public void setTermsAndConditionsFile(ManagedFileInfo termsAndConditionsFile) {
        this.termsAndConditionsFile = termsAndConditionsFile;
    }

    public Set<PasspointConnectionCapability> getConnectionCapabilitySet() {
        return connectionCapabilitySet;
    }

    public void setConnectionCapabilitySet(Set<PasspointConnectionCapability> connectionCapabilitySet) {
        this.connectionCapabilitySet = connectionCapabilitySet;
    }

    public boolean isEnable2pt4GHz() {
        return enable2pt4GHz;
    }

    public void setEnable2pt4GHz(boolean enable2pt4gHz) {
        enable2pt4GHz = enable2pt4gHz;
    }

    public boolean isEnable5GHz() {
        return enable5GHz;
    }

    public void setEnable5GHz(boolean enable5gHz) {
        enable5GHz = enable5gHz;
    }
    public PasspointAccessNetworkType getPasspointAccessNetworkType() {
        return passpointAccessNetworkType;
    }

    public void setPasspointAccessNetworkType(PasspointAccessNetworkType passpointAccessNetworkType) {
        this.passpointAccessNetworkType = passpointAccessNetworkType;
    }

    public PasspointNetworkAuthenticationType getPasspointNetworkAuthenticationType() {
        return passpointNetworkAuthenticationType;
    }

    public void setPasspointNetworkAuthenticationType(
            PasspointNetworkAuthenticationType passpointNetworkAuthenticationType) {
        this.passpointNetworkAuthenticationType = passpointNetworkAuthenticationType;
    }

    public List<Long> getAssociatedAccessSsidProfileIds() {
        return associatedAccessSsidProfileIds;
    }

    public void setAssociatedAccessSsidProfileIds(List<Long> associatedAccessSsidProfileIds) {
        this.associatedAccessSsidProfileIds = associatedAccessSsidProfileIds;
    }

    public Long getOsuSsidProfileId() {
        return osuSsidProfileId;
    }

    public void setOsuSsidProfileId(Long osuSsidProfileId) {
        this.osuSsidProfileId = osuSsidProfileId;
    }

    public Long getPasspointOperatorProfileId() {
        return passpointOperatorProfileId;
    }

    public void setPasspointOperatorProfileId(Long passpointOperatorProfileId) {
        this.passpointOperatorProfileId = passpointOperatorProfileId;
    }

    public Long getPasspointVenueProfileId() {
        return passpointVenueProfileId;
    }

    public void setPasspointVenueProfileId(Long passpointVenueProfileId) {
        this.passpointVenueProfileId = passpointVenueProfileId;
    }

    public Set<Long> getPasspointOsuProviderProfileIds() {
        return passpointOsuProviderProfileIds;
    }

    public void setPasspointOsuProviderProfileIds(Set<Long> passpointOsuProviderProfileIds) {
        this.passpointOsuProviderProfileIds = passpointOsuProviderProfileIds;
    }

    @Override
    public PasspointProfile clone() {
        PasspointProfile returnValue = (PasspointProfile) super.clone();
        returnValue.setAccessNetworkType(passpointAccessNetworkType);
        returnValue.setAdditionalStepsRequiredForAccess(additionalStepsRequiredForAccess);
        returnValue.setAnqpDomainId(anqpDomainId);
        returnValue.setApCivicLocation(apCivicLocation);
        returnValue.setApGeospatialLocation(apGeospatialLocation);
        returnValue.setApPublicLocationIdUri(apPublicLocationIdUri);

        if (connectionCapabilitySet != null) {
            returnValue.setConnectionCapabilitySet(connectionCapabilitySet);
        }

        returnValue.setDeauthRequestTimeout(deauthRequestTimeout);
        returnValue.setDisableDownstreamGroupAddressedForwarding(disableDownstreamGroupAddressedForwarding);
        returnValue.setEmergencyServicesReachable(emergencyServicesReachable);
        returnValue.setEnableInterworkingAndHs20(enableInterworkingAndHs20);
        returnValue.setGasAddr3Behaviour(gasAddr3Behaviour);
        returnValue.setHessid(hessid);
        returnValue.setInternetConnectivity(internetConnectivity);
        returnValue.setIpAddressTypeAvailability(ipAddressTypeAvailability);
        returnValue.setNetworkAuthenticationType(passpointNetworkAuthenticationType);
        returnValue.setOperatingClass(operatingClass);
        returnValue.setQosMapSetConfiguration(qosMapSetConfiguration);
        returnValue.setTermsAndConditionsFile(termsAndConditionsFile);
        returnValue.setUnauthenticatedEmergencyServiceAccessible(unauthenticatedEmergencyServiceAccessible);
        returnValue.setWhitelistDomain(whitelistDomain);
        returnValue.setEnable2pt4GHz(enable2pt4GHz);
        returnValue.setEnable5GHz(enable5GHz);

        if (associatedAccessSsidProfileIds != null) {
            returnValue.setAssociatedAccessSsidProfileIds(associatedAccessSsidProfileIds);
        }

        returnValue.setOsuSsidProfileId(osuSsidProfileId);

        if (passpointOsuProviderProfileIds != null) {
            returnValue.setPasspointOsuProviderProfileIds(passpointOsuProviderProfileIds);
        }

        returnValue.setPasspointOperatorProfileId(passpointOperatorProfileId);
        returnValue.setPasspointVenueProfileId(passpointVenueProfileId);

        return returnValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(additionalStepsRequiredForAccess, anqpDomainId, apCivicLocation, apGeospatialLocation,
                apPublicLocationIdUri, associatedAccessSsidProfileIds, connectionCapabilitySet, deauthRequestTimeout,
                disableDownstreamGroupAddressedForwarding, emergencyServicesReachable, enable2pt4GHz, enable5GHz,
                enableInterworkingAndHs20, gasAddr3Behaviour, hessid, internetConnectivity, ipAddressTypeAvailability,
                operatingClass, osuSsidProfileId, passpointAccessNetworkType, passpointNetworkAuthenticationType,
                passpointOperatorProfileId, passpointOsuProviderProfileIds, passpointVenueProfileId,
                qosMapSetConfiguration, termsAndConditionsFile, unauthenticatedEmergencyServiceAccessible,
                whitelistDomain);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PasspointProfile other = (PasspointProfile) obj;
        return additionalStepsRequiredForAccess == other.additionalStepsRequiredForAccess
                && anqpDomainId == other.anqpDomainId && Objects.equals(apCivicLocation, other.apCivicLocation)
                && Objects.equals(apGeospatialLocation, other.apGeospatialLocation)
                && Objects.equals(apPublicLocationIdUri, other.apPublicLocationIdUri)
                && Objects.equals(associatedAccessSsidProfileIds, other.associatedAccessSsidProfileIds)
                && Objects.equals(connectionCapabilitySet, other.connectionCapabilitySet)
                && deauthRequestTimeout == other.deauthRequestTimeout
                && disableDownstreamGroupAddressedForwarding == other.disableDownstreamGroupAddressedForwarding
                && emergencyServicesReachable == other.emergencyServicesReachable
                && enable2pt4GHz == other.enable2pt4GHz && enable5GHz == other.enable5GHz
                && enableInterworkingAndHs20 == other.enableInterworkingAndHs20
                && Objects.equals(gasAddr3Behaviour, other.gasAddr3Behaviour) && Objects.equals(hessid, other.hessid)
                && internetConnectivity == other.internetConnectivity
                && Objects.equals(ipAddressTypeAvailability, other.ipAddressTypeAvailability)
                && operatingClass == other.operatingClass && Objects.equals(osuSsidProfileId, other.osuSsidProfileId)
                && Objects.equals(passpointAccessNetworkType, other.passpointAccessNetworkType)
                && Objects.equals(passpointNetworkAuthenticationType, other.passpointNetworkAuthenticationType)
                && Objects.equals(passpointOperatorProfileId, other.passpointOperatorProfileId)
                && Objects.equals(passpointOsuProviderProfileIds, other.passpointOsuProviderProfileIds)
                && Objects.equals(passpointVenueProfileId, other.passpointVenueProfileId)
                && Objects.equals(qosMapSetConfiguration, other.qosMapSetConfiguration)
                && Objects.equals(termsAndConditionsFile, other.termsAndConditionsFile)
                && unauthenticatedEmergencyServiceAccessible == other.unauthenticatedEmergencyServiceAccessible
                && Objects.equals(whitelistDomain, other.whitelistDomain);
    }

}
