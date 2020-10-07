package com.telecominfraproject.wlan.profile.passpoint.hotspot.models;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
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

	private List<String> associatedAccessSsidNames;

	private String osuSsidName;

	private List<MccMnc> mccMnc3gppCellularNetworkInfo;

	private String operatorProfileName;

	private String venueProfileName;

	private Set<String> idProviderProfileNames;

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
		operatorProfileName = "TipWlan-Hotspot20-Operator";
		venueProfileName = "TipWlan-Hotspot20-Venue";
		idProviderProfileNames = new HashSet<>();
		idProviderProfileNames.add("TipWlan-Hotspot20-OSU-Provider");
		osuSsidName = "TipWlan-cloud-3-radios";
		associatedAccessSsidNames = new ArrayList<>();
		associatedAccessSsidNames.add("TipWlan-cloud-hotspot-access");

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
		return associatedAccessSsidNames;
	}

	public void setAssociatedSsids(List<String> associatedSsids) {
		this.associatedAccessSsidNames = associatedSsids;
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

	public List<String> getAssociatedAccessSsidNames() {
		return associatedAccessSsidNames;
	}

	public void setAssociatedAccessSsidNames(List<String> associatedAccessSsidNames) {
		this.associatedAccessSsidNames = associatedAccessSsidNames;
	}

	public String getOsuSsidName() {
		return osuSsidName;
	}

	public void setOsuSsidName(String osuSsidName) {
		this.osuSsidName = osuSsidName;
	}

	public String getOperatorProfileName() {
		return operatorProfileName;
	}

	public void setOperatorProfileName(String operatorProfileName) {
		this.operatorProfileName = operatorProfileName;
	}

	public String getVenueProfileName() {
		return venueProfileName;
	}

	public void setVenueProfileName(String venueProfileName) {
		this.venueProfileName = venueProfileName;
	}

	public Set<String> getIdProviderProfileNames() {
		return idProviderProfileNames;
	}

	public void setIdProviderProfileNames(Set<String> idProviderProfileNames) {
		this.idProviderProfileNames = idProviderProfileNames;
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

		if (associatedAccessSsidNames != null) {
			returnValue.setAssociatedSsids(associatedAccessSsidNames);
		}

		returnValue.setOsuSsidName(osuSsidName);

		if (idProviderProfileNames != null) {
			returnValue.setIdProviderProfileNames(idProviderProfileNames);
		}

		returnValue.setOperatorProfileName(operatorProfileName);
		returnValue.setVenueProfileName(venueProfileName);

		return returnValue;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accessNetworkType == null) ? 0 : accessNetworkType.hashCode());
		result = prime * result + additionalStepsRequiredForAccess;
		result = prime * result + anqpDomainId;
		result = prime * result + ((apCivicLocation == null) ? 0 : apCivicLocation.hashCode());
		result = prime * result + ((apGeospatialLocation == null) ? 0 : apGeospatialLocation.hashCode());
		result = prime * result + ((apPublicLocationIdUri == null) ? 0 : apPublicLocationIdUri.hashCode());
		result = prime * result + ((associatedAccessSsidNames == null) ? 0 : associatedAccessSsidNames.hashCode());
		result = prime * result + ((connectionCapabilitySet == null) ? 0 : connectionCapabilitySet.hashCode());
		result = prime * result + deauthRequestTimeout;
		result = prime * result + (disableDownstreamGroupAddressedForwarding ? 1231 : 1237);
		result = prime * result + (emergencyServicesReachable ? 1231 : 1237);
		result = prime * result + (enable2pt4GHz ? 1231 : 1237);
		result = prime * result + (enable5GHz ? 1231 : 1237);
		result = prime * result + (enableInterworkingAndHs20 ? 1231 : 1237);
		result = prime * result + ((gasAddr3Behaviour == null) ? 0 : gasAddr3Behaviour.hashCode());
		result = prime * result + ((hessid == null) ? 0 : hessid.hashCode());
		result = prime * result + ((idProviderProfileNames == null) ? 0 : idProviderProfileNames.hashCode());
		result = prime * result + (internetConnectivity ? 1231 : 1237);
		result = prime * result + ((ipAddressTypeAvailability == null) ? 0 : ipAddressTypeAvailability.hashCode());
		result = prime * result
				+ ((mccMnc3gppCellularNetworkInfo == null) ? 0 : mccMnc3gppCellularNetworkInfo.hashCode());
		result = prime * result + ((networkAuthenticationType == null) ? 0 : networkAuthenticationType.hashCode());
		result = prime * result + operatingClass;
		result = prime * result + ((operatorProfileName == null) ? 0 : operatorProfileName.hashCode());
		result = prime * result + ((osuSsidName == null) ? 0 : osuSsidName.hashCode());
		result = prime * result + ((qosMapSetConfiguration == null) ? 0 : qosMapSetConfiguration.hashCode());
		result = prime * result + ((termsAndConditionsFile == null) ? 0 : termsAndConditionsFile.hashCode());
		result = prime * result + (unauthenticatedEmergencyServiceAccessible ? 1231 : 1237);
		result = prime * result + ((venueProfileName == null) ? 0 : venueProfileName.hashCode());
		result = prime * result + ((whitelistDomain == null) ? 0 : whitelistDomain.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Hotspot2Profile other = (Hotspot2Profile) obj;
		if (accessNetworkType == null) {
			if (other.accessNetworkType != null)
				return false;
		} else if (!accessNetworkType.equals(other.accessNetworkType))
			return false;
		if (additionalStepsRequiredForAccess != other.additionalStepsRequiredForAccess)
			return false;
		if (anqpDomainId != other.anqpDomainId)
			return false;
		if (apCivicLocation == null) {
			if (other.apCivicLocation != null)
				return false;
		} else if (!apCivicLocation.equals(other.apCivicLocation))
			return false;
		if (apGeospatialLocation == null) {
			if (other.apGeospatialLocation != null)
				return false;
		} else if (!apGeospatialLocation.equals(other.apGeospatialLocation))
			return false;
		if (apPublicLocationIdUri == null) {
			if (other.apPublicLocationIdUri != null)
				return false;
		} else if (!apPublicLocationIdUri.equals(other.apPublicLocationIdUri))
			return false;
		if (associatedAccessSsidNames == null) {
			if (other.associatedAccessSsidNames != null)
				return false;
		} else if (!associatedAccessSsidNames.equals(other.associatedAccessSsidNames))
			return false;
		if (connectionCapabilitySet == null) {
			if (other.connectionCapabilitySet != null)
				return false;
		} else if (!connectionCapabilitySet.equals(other.connectionCapabilitySet))
			return false;
		if (deauthRequestTimeout != other.deauthRequestTimeout)
			return false;
		if (disableDownstreamGroupAddressedForwarding != other.disableDownstreamGroupAddressedForwarding)
			return false;
		if (emergencyServicesReachable != other.emergencyServicesReachable)
			return false;
		if (enable2pt4GHz != other.enable2pt4GHz)
			return false;
		if (enable5GHz != other.enable5GHz)
			return false;
		if (enableInterworkingAndHs20 != other.enableInterworkingAndHs20)
			return false;
		if (gasAddr3Behaviour == null) {
			if (other.gasAddr3Behaviour != null)
				return false;
		} else if (!gasAddr3Behaviour.equals(other.gasAddr3Behaviour))
			return false;
		if (hessid == null) {
			if (other.hessid != null)
				return false;
		} else if (!hessid.equals(other.hessid))
			return false;
		if (idProviderProfileNames == null) {
			if (other.idProviderProfileNames != null)
				return false;
		} else if (!idProviderProfileNames.equals(other.idProviderProfileNames))
			return false;
		if (internetConnectivity != other.internetConnectivity)
			return false;
		if (ipAddressTypeAvailability == null) {
			if (other.ipAddressTypeAvailability != null)
				return false;
		} else if (!ipAddressTypeAvailability.equals(other.ipAddressTypeAvailability))
			return false;
		if (mccMnc3gppCellularNetworkInfo == null) {
			if (other.mccMnc3gppCellularNetworkInfo != null)
				return false;
		} else if (!mccMnc3gppCellularNetworkInfo.equals(other.mccMnc3gppCellularNetworkInfo))
			return false;
		if (networkAuthenticationType == null) {
			if (other.networkAuthenticationType != null)
				return false;
		} else if (!networkAuthenticationType.equals(other.networkAuthenticationType))
			return false;
		if (operatingClass != other.operatingClass)
			return false;
		if (operatorProfileName == null) {
			if (other.operatorProfileName != null)
				return false;
		} else if (!operatorProfileName.equals(other.operatorProfileName))
			return false;
		if (osuSsidName == null) {
			if (other.osuSsidName != null)
				return false;
		} else if (!osuSsidName.equals(other.osuSsidName))
			return false;
		if (qosMapSetConfiguration == null) {
			if (other.qosMapSetConfiguration != null)
				return false;
		} else if (!qosMapSetConfiguration.equals(other.qosMapSetConfiguration))
			return false;
		if (termsAndConditionsFile == null) {
			if (other.termsAndConditionsFile != null)
				return false;
		} else if (!termsAndConditionsFile.equals(other.termsAndConditionsFile))
			return false;
		if (unauthenticatedEmergencyServiceAccessible != other.unauthenticatedEmergencyServiceAccessible)
			return false;
		if (venueProfileName == null) {
			if (other.venueProfileName != null)
				return false;
		} else if (!venueProfileName.equals(other.venueProfileName))
			return false;
		if (whitelistDomain == null) {
			if (other.whitelistDomain != null)
				return false;
		} else if (!whitelistDomain.equals(other.whitelistDomain))
			return false;
		return true;
	}

}
