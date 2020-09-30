package com.telecominfraproject.wlan.profile.passpoint.hotspot.models;

import java.net.URI;
import java.util.Objects;
import java.util.Set;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.profile.models.ProfileDetails;
import com.telecominfraproject.wlan.profile.models.ProfileType;
import com.telecominfraproject.wlan.profile.passpoint.models.AccessNetworkType;
import com.telecominfraproject.wlan.profile.passpoint.models.ConnectionCapabilitiesIpProtocol;
import com.telecominfraproject.wlan.profile.passpoint.models.ConnectionCapabilitiesStatus;
import com.telecominfraproject.wlan.profile.passpoint.models.GasAddress3Behaviour;
import com.telecominfraproject.wlan.profile.passpoint.models.IPv4PasspointAddressType;
import com.telecominfraproject.wlan.profile.passpoint.models.NetworkAuthenticationType;


public class Hotspot2Profile extends ProfileDetails implements PushableConfiguration<Hotspot2Profile> {

    private static final long serialVersionUID = -8153311150616202124L;

    private boolean enableInterworkingAndHs20;

    private MacAddress hessid;

    private AccessNetworkType accessNetworkType;

    private NetworkAuthenticationType networkAuthenticationType;

    private int additionalStepsRequiredForAccess;

    private int deauthRequestTimeout;

    private int operatingClass;

    private Hotspot2TermsAndConditionsFile termsAndConditionsFile;

    private String whitelistDomain;

    private boolean emergencyServicesReachable;

    private boolean unauthenticatedEmergencyServiceAccessible;

    private boolean internetConnectivity;

    private String ipAddressTypeAvailability;

    private String connectionCapability;

    private Set<String> qosMapSetConfiguration;

    private String apGeospatialLocation;

    private String apCivicLocation;

    private URI apPublicLocationIdUri;

    private GasAddress3Behaviour gasAddr3Behaviour;

    private int anqpDomainId;

    private boolean disableDownstreamGroupAddressedForwarding;


    private Hotspot2Profile() {

        setAccessNetworkType(AccessNetworkType.free_public_network);
        setAdditionalStepsRequiredForAccess(0);
        setAnqpDomainId(1234);
        setConnectionCapability(ConnectionCapabilitiesIpProtocol.TCP.getId() + ":" + "8080" + ":"
                + ConnectionCapabilitiesStatus.open.getName());
        setDeauthRequestTimeout(0);
        setDisableDownstreamGroupAddressedForwarding(false);
        setEmergencyServicesReachable(true);
        setEnableInterworkingAndHs20(true);
        setGasAddr3Behaviour(GasAddress3Behaviour.p2pSpecWorkaroundFromRequest);
        setInternetConnectivity(true);
        setIpAddressTypeAvailability(IPv4PasspointAddressType.public_IPv4_address_available.getName());
        setNetworkAuthenticationType(NetworkAuthenticationType.acceptance_of_terms_and_conditions);
        setOperatingClass(0);
        setTermsAndConditionsFile(Hotspot2TermsAndConditionsFile.createWithDefaults());
    }

    public static Hotspot2Profile createWithDefaults() {
        return new Hotspot2Profile();
    }

    public boolean isEnableInterworkingAndHs20() {
        return enableInterworkingAndHs20;
    }


    public void setEnableInterworkingAndHs20(boolean enableInterworkingAndHs20) {
        this.enableInterworkingAndHs20 = enableInterworkingAndHs20;
    }


    @Override
    public ProfileType getProfileType() {
        return ProfileType.hotspot_2pt0;
    }

    @Override
    public boolean needsToBeUpdatedOnDevice(Hotspot2Profile previousVersion) {
        // TODO Auto-generated method stub
        return false;
    }


    public MacAddress getHessid() {
        return hessid;
    }


    public void setHessid(MacAddress hessid) {
        this.hessid = hessid;
    }


    public AccessNetworkType getAccessNetworkType() {
        return accessNetworkType;
    }


    public void setAccessNetworkType(AccessNetworkType accessNetworkType) {
        this.accessNetworkType = accessNetworkType;
    }


    public NetworkAuthenticationType getNetworkAuthenticationType() {
        return networkAuthenticationType;
    }


    public void setNetworkAuthenticationType(NetworkAuthenticationType networkAuthenticationType) {
        this.networkAuthenticationType = networkAuthenticationType;
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
        // # format: <1-octet encoded value as hex str>
        // # (ipv4_type & 0x3f) << 2 | (ipv6_type & 0x3)
        return ipAddressTypeAvailability;
    }


    public void setIpAddressTypeAvailability(String ipAddressTypeAvailability) {
        this.ipAddressTypeAvailability = ipAddressTypeAvailability;
    }


    public String getConnectionCapability() {
        // # Connection Capability
        // # This can be used to advertise what type of IP traffic can be sent
        // through the
        // # hotspot (e.g., due to firewall allowing/blocking protocols/ports).
        // # format: <IP Protocol>:<Port Number>:<Status>
        // # IP Protocol: 1 = ICMP, 6 = TCP, 17 = UDP
        // # Port Number: 0..65535
        // # Status: 0 = Closed, 1 = Open, 2 = Unknown
        // # Each hs20_conn_capab line is added to the list of advertised
        // tuples.
        // #hs20_conn_capab=1:0:2
        // #hs20_conn_capab=6:22:1
        // #hs20_conn_capab=17:5060:0
        return connectionCapability;
    }


    public void setConnectionCapability(String connectionCapability) {
        this.connectionCapability = connectionCapability;
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


    public GasAddress3Behaviour getGasAddr3Behaviour() {
        return gasAddr3Behaviour;
    }

    public void setGasAddr3Behaviour(GasAddress3Behaviour gasAddr3Behaviour) {
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

    public Hotspot2TermsAndConditionsFile getTermsAndConditionsFile() {
        return termsAndConditionsFile;
    }

    public void setTermsAndConditionsFile(Hotspot2TermsAndConditionsFile termsAndConditionsFile) {
        this.termsAndConditionsFile = termsAndConditionsFile;
    }

    @Override
    public Hotspot2Profile clone() {
        Hotspot2Profile returnValue = (Hotspot2Profile) super.clone();
        returnValue.setAccessNetworkType(accessNetworkType);
        returnValue.setAdditionalStepsRequiredForAccess(additionalStepsRequiredForAccess);
        returnValue.setAnqpDomainId(anqpDomainId);
        returnValue.setApCivicLocation(apCivicLocation);
        returnValue.setApGeospatialLocation(apGeospatialLocation);
        returnValue.setApPublicLocationIdUri(apPublicLocationIdUri);
        returnValue.setConnectionCapability(connectionCapability);
        returnValue.setDeauthRequestTimeout(deauthRequestTimeout);
        returnValue.setDisableDownstreamGroupAddressedForwarding(disableDownstreamGroupAddressedForwarding);
        returnValue.setEmergencyServicesReachable(emergencyServicesReachable);
        returnValue.setEnableInterworkingAndHs20(enableInterworkingAndHs20);
        returnValue.setGasAddr3Behaviour(gasAddr3Behaviour);
        returnValue.setHessid(hessid);
        returnValue.setInternetConnectivity(internetConnectivity);
        returnValue.setIpAddressTypeAvailability(ipAddressTypeAvailability);
        returnValue.setNetworkAuthenticationType(networkAuthenticationType);
        returnValue.setOperatingClass(operatingClass);
        returnValue.setQosMapSetConfiguration(qosMapSetConfiguration);
        returnValue.setTermsAndConditionsFile(termsAndConditionsFile);
        returnValue.setUnauthenticatedEmergencyServiceAccessible(unauthenticatedEmergencyServiceAccessible);
        returnValue.setWhitelistDomain(whitelistDomain);
        return returnValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessNetworkType, additionalStepsRequiredForAccess, anqpDomainId, apCivicLocation,
                apGeospatialLocation, apPublicLocationIdUri, connectionCapability, deauthRequestTimeout,
                disableDownstreamGroupAddressedForwarding, emergencyServicesReachable, enableInterworkingAndHs20,
                gasAddr3Behaviour, hessid, internetConnectivity, ipAddressTypeAvailability, networkAuthenticationType,
                operatingClass, qosMapSetConfiguration, termsAndConditionsFile,
                unauthenticatedEmergencyServiceAccessible, whitelistDomain);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Hotspot2Profile)) {
            return false;
        }
        Hotspot2Profile other = (Hotspot2Profile) obj;
        return Objects.equals(accessNetworkType, other.accessNetworkType)
                && additionalStepsRequiredForAccess == other.additionalStepsRequiredForAccess
                && anqpDomainId == other.anqpDomainId && Objects.equals(apCivicLocation, other.apCivicLocation)
                && Objects.equals(apGeospatialLocation, other.apGeospatialLocation)
                && Objects.equals(apPublicLocationIdUri, other.apPublicLocationIdUri)
                && Objects.equals(connectionCapability, other.connectionCapability)
                && deauthRequestTimeout == other.deauthRequestTimeout
                && disableDownstreamGroupAddressedForwarding == other.disableDownstreamGroupAddressedForwarding
                && emergencyServicesReachable == other.emergencyServicesReachable
                && enableInterworkingAndHs20 == other.enableInterworkingAndHs20
                && Objects.equals(gasAddr3Behaviour, other.gasAddr3Behaviour) && Objects.equals(hessid, other.hessid)
                && internetConnectivity == other.internetConnectivity
                && Objects.equals(ipAddressTypeAvailability, other.ipAddressTypeAvailability)
                && Objects.equals(networkAuthenticationType, other.networkAuthenticationType)
                && operatingClass == other.operatingClass
                && Objects.equals(qosMapSetConfiguration, other.qosMapSetConfiguration)
                && Objects.equals(termsAndConditionsFile, other.termsAndConditionsFile)
                && unauthenticatedEmergencyServiceAccessible == other.unauthenticatedEmergencyServiceAccessible
                && Objects.equals(whitelistDomain, other.whitelistDomain);
    }


}
