package com.telecominfraproject.wlan.profile.passpoint.hotspot.models;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.Set;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.equipment.PushableConfiguration;
import com.telecominfraproject.wlan.profile.models.ProfileDetails;
import com.telecominfraproject.wlan.profile.models.ProfileType;
import com.telecominfraproject.wlan.profile.models.common.FileCategory;
import com.telecominfraproject.wlan.profile.models.common.FileType;
import com.telecominfraproject.wlan.profile.models.common.ManagedFileInfo;
import com.telecominfraproject.wlan.profile.passpoint.models.AccessNetworkType;
import com.telecominfraproject.wlan.profile.passpoint.models.GasAddress3Behaviour;
import com.telecominfraproject.wlan.profile.passpoint.models.IPv4PasspointAddressType;
import com.telecominfraproject.wlan.profile.passpoint.models.MccMnc;
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

    private ManagedFileInfo termsAndConditionsFile;

    private String whitelistDomain;

    private boolean emergencyServicesReachable;

    private boolean unauthenticatedEmergencyServiceAccessible;

    private boolean internetConnectivity;

    private Set<ConnectionCapability> connectionCapabilitySet;

    private String ipAddressTypeAvailability;

    private Set<String> qosMapSetConfiguration;

    private String apGeospatialLocation;

    private String apCivicLocation;

    private URI apPublicLocationIdUri;

    private GasAddress3Behaviour gasAddr3Behaviour;

    private int anqpDomainId;

    private boolean disableDownstreamGroupAddressedForwarding;

    private boolean enable2pt4GHz;
    private boolean enable5GHz;

    private List<String> associatedSsids;

    private List<MccMnc> mccMnc3gppCellularNetworkInfo;

    private Hotspot2Profile() {

        setAccessNetworkType(AccessNetworkType.free_public_network);
        setAdditionalStepsRequiredForAccess(0);
        setAnqpDomainId(1234);
        connectionCapabilitySet = new HashSet<>();
        connectionCapabilitySet.add(ConnectionCapability.createWithDefaults());
        setDeauthRequestTimeout(0);
        setDisableDownstreamGroupAddressedForwarding(false);
        setEmergencyServicesReachable(true);
        setEnableInterworkingAndHs20(true);
        setGasAddr3Behaviour(GasAddress3Behaviour.p2pSpecWorkaroundFromRequest);
        setInternetConnectivity(true);
        setIpAddressTypeAvailability(IPv4PasspointAddressType.public_IPv4_address_available.getName());
        setNetworkAuthenticationType(NetworkAuthenticationType.acceptance_of_terms_and_conditions);
        setOperatingClass(0);
        termsAndConditionsFile = new ManagedFileInfo();
        termsAndConditionsFile.setApExportUrl("https://localhost:9091/filestore/termsAndConditions");
        termsAndConditionsFile.setFileCategory(FileCategory.ExternalPolicyConfiguration);
        termsAndConditionsFile.setFileType(FileType.TEXT);
        enable2pt4GHz = true;
        enable5GHz = true;
        mccMnc3gppCellularNetworkInfo = new ArrayList<>();
        MccMnc mccMnc = MccMnc.createWithDefaults();
        mccMnc.setMcc(302);
        mccMnc.setMnc(720);
        mccMnc.setIso("ca");
        mccMnc.setCountry("Canada");
        mccMnc.setCountryCode(1);
        mccMnc.setNetwork("Rogers AT&T Wireless");
        mccMnc3gppCellularNetworkInfo.add(mccMnc);
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

    public ManagedFileInfo getTermsAndConditionsFile() {
        return termsAndConditionsFile;
    }

    public void setTermsAndConditionsFile(ManagedFileInfo termsAndConditionsFile) {
        this.termsAndConditionsFile = termsAndConditionsFile;
    }


    public Set<ConnectionCapability> getConnectionCapabilitySet() {
        return connectionCapabilitySet;
    }


    public void setConnectionCapabilitySet(Set<ConnectionCapability> connectionCapabilitySet) {
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


    public List<String> getAssociatedSsids() {
        return associatedSsids;
    }


    public void setAssociatedSsids(List<String> associatedSsids) {
        this.associatedSsids = associatedSsids;
    }

    public List<MccMnc> getMccMnc3gppCellularNetworkInfo() {
        return mccMnc3gppCellularNetworkInfo;
    }

    public String getMccMncList() {
        StringBuffer buffer = new StringBuffer();
        ListIterator<MccMnc> listIterator = mccMnc3gppCellularNetworkInfo.listIterator();
        while (listIterator.hasNext()) {
            buffer.append(listIterator.next().getMccMncPairing());
            if (listIterator.hasNext()) {
                buffer.append(";");
            }
        }
        return buffer.toString();
    }

    public void setMccMnc3gppCellularNetworkInfo(List<MccMnc> mccMnc3gppCellularNetworkInfo) {
        this.mccMnc3gppCellularNetworkInfo = mccMnc3gppCellularNetworkInfo;
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
        returnValue.setNetworkAuthenticationType(networkAuthenticationType);
        returnValue.setOperatingClass(operatingClass);
        returnValue.setQosMapSetConfiguration(qosMapSetConfiguration);
        returnValue.setTermsAndConditionsFile(termsAndConditionsFile);
        returnValue.setUnauthenticatedEmergencyServiceAccessible(unauthenticatedEmergencyServiceAccessible);
        returnValue.setWhitelistDomain(whitelistDomain);
        returnValue.setEnable2pt4GHz(enable2pt4GHz);
        returnValue.setEnable5GHz(enable5GHz);

        if (associatedSsids != null) {
            returnValue.setAssociatedSsids(associatedSsids);
        }

        return returnValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(accessNetworkType, additionalStepsRequiredForAccess, anqpDomainId, apCivicLocation,
                apGeospatialLocation, apPublicLocationIdUri, associatedSsids, connectionCapabilitySet,
                deauthRequestTimeout, disableDownstreamGroupAddressedForwarding, emergencyServicesReachable,
                enable2pt4GHz, enable5GHz, enableInterworkingAndHs20, gasAddr3Behaviour, hessid, internetConnectivity,
                ipAddressTypeAvailability, networkAuthenticationType, operatingClass, qosMapSetConfiguration,
                termsAndConditionsFile, unauthenticatedEmergencyServiceAccessible, whitelistDomain);
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
                && Objects.equals(associatedSsids, other.associatedSsids)
                && Objects.equals(connectionCapabilitySet, other.connectionCapabilitySet)
                && deauthRequestTimeout == other.deauthRequestTimeout
                && disableDownstreamGroupAddressedForwarding == other.disableDownstreamGroupAddressedForwarding
                && emergencyServicesReachable == other.emergencyServicesReachable
                && enable2pt4GHz == other.enable2pt4GHz && enable5GHz == other.enable5GHz
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
