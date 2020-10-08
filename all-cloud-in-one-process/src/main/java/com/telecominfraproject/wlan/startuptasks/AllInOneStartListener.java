package com.telecominfraproject.wlan.startuptasks;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import com.telecominfraproject.wlan.alarm.AlarmServiceInterface;
import com.telecominfraproject.wlan.alarm.models.Alarm;
import com.telecominfraproject.wlan.alarm.models.AlarmCode;
import com.telecominfraproject.wlan.alarm.models.AlarmDetails;
import com.telecominfraproject.wlan.client.ClientServiceInterface;
import com.telecominfraproject.wlan.client.info.models.ClientInfoDetails;
import com.telecominfraproject.wlan.client.models.Client;
import com.telecominfraproject.wlan.client.session.models.ClientDhcpDetails;
import com.telecominfraproject.wlan.client.session.models.ClientSession;
import com.telecominfraproject.wlan.client.session.models.ClientSessionDetails;
import com.telecominfraproject.wlan.client.session.models.ClientSessionMetricDetails;
import com.telecominfraproject.wlan.core.model.entity.CountryCode;
import com.telecominfraproject.wlan.core.model.entity.MinMaxAvgValueInt;
import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.equipment.SecurityType;
import com.telecominfraproject.wlan.core.model.role.PortalUserRole;
import com.telecominfraproject.wlan.core.model.scheduler.EmptySchedule;
import com.telecominfraproject.wlan.core.model.service.bonjour.BonjourService;
import com.telecominfraproject.wlan.customer.models.Customer;
import com.telecominfraproject.wlan.customer.models.CustomerDetails;
import com.telecominfraproject.wlan.customer.models.EquipmentAutoProvisioningSettings;
import com.telecominfraproject.wlan.customer.service.CustomerServiceInterface;
import com.telecominfraproject.wlan.equipment.EquipmentServiceInterface;
import com.telecominfraproject.wlan.equipment.models.ApElementConfiguration;
import com.telecominfraproject.wlan.equipment.models.Equipment;
import com.telecominfraproject.wlan.firmware.FirmwareServiceInterface;
import com.telecominfraproject.wlan.firmware.models.CustomerFirmwareTrackRecord;
import com.telecominfraproject.wlan.firmware.models.CustomerFirmwareTrackSettings.TrackFlag;
import com.telecominfraproject.wlan.firmware.models.FirmwareTrackAssignmentDetails;
import com.telecominfraproject.wlan.firmware.models.FirmwareTrackAssignmentRecord;
import com.telecominfraproject.wlan.firmware.models.FirmwareTrackRecord;
import com.telecominfraproject.wlan.firmware.models.FirmwareVersion;
import com.telecominfraproject.wlan.firmware.models.ValidationMethod;
import com.telecominfraproject.wlan.location.models.Location;
import com.telecominfraproject.wlan.location.models.LocationDetails;
import com.telecominfraproject.wlan.location.models.LocationType;
import com.telecominfraproject.wlan.location.service.LocationServiceInterface;
import com.telecominfraproject.wlan.portaluser.PortalUserServiceInterface;
import com.telecominfraproject.wlan.portaluser.models.PortalUser;
import com.telecominfraproject.wlan.profile.ProfileServiceInterface;
import com.telecominfraproject.wlan.profile.bonjour.models.BonjourGatewayProfile;
import com.telecominfraproject.wlan.profile.bonjour.models.BonjourServiceSet;
import com.telecominfraproject.wlan.profile.captiveportal.models.CaptivePortalAuthenticationType;
import com.telecominfraproject.wlan.profile.captiveportal.models.CaptivePortalConfiguration;
import com.telecominfraproject.wlan.profile.captiveportal.models.SessionExpiryType;
import com.telecominfraproject.wlan.profile.captiveportal.user.models.TimedAccessUserDetails;
import com.telecominfraproject.wlan.profile.captiveportal.user.models.TimedAccessUserRecord;
import com.telecominfraproject.wlan.profile.metrics.ServiceMetricsCollectionConfigProfile;
import com.telecominfraproject.wlan.profile.models.Profile;
import com.telecominfraproject.wlan.profile.models.ProfileContainer;
import com.telecominfraproject.wlan.profile.models.ProfileType;
import com.telecominfraproject.wlan.profile.models.common.FileCategory;
import com.telecominfraproject.wlan.profile.models.common.FileType;
import com.telecominfraproject.wlan.profile.models.common.ManagedFileInfo;
import com.telecominfraproject.wlan.profile.network.models.ApNetworkConfiguration;
import com.telecominfraproject.wlan.profile.network.models.RadioProfileConfiguration;
import com.telecominfraproject.wlan.profile.passpoint.hotspot.models.Hotspot2Profile;
import com.telecominfraproject.wlan.profile.passpoint.models.Hotspot20Duple;
import com.telecominfraproject.wlan.profile.passpoint.models.MccMnc;
import com.telecominfraproject.wlan.profile.passpoint.operator.models.OperatorProfile;
import com.telecominfraproject.wlan.profile.passpoint.provider.models.Hotspot20IdProviderProfile;
import com.telecominfraproject.wlan.profile.passpoint.provider.models.NaiRealmInformation;
import com.telecominfraproject.wlan.profile.passpoint.provider.models.OsuIcon;
import com.telecominfraproject.wlan.profile.passpoint.venue.models.VenueProfile;
import com.telecominfraproject.wlan.profile.radius.models.RadiusProfile;
import com.telecominfraproject.wlan.profile.radius.models.RadiusServer;
import com.telecominfraproject.wlan.profile.radius.models.RadiusServiceRegion;
import com.telecominfraproject.wlan.profile.rf.models.RfConfiguration;
import com.telecominfraproject.wlan.profile.ssid.models.SsidConfiguration;
import com.telecominfraproject.wlan.profile.ssid.models.SsidConfiguration.SecureMode;
import com.telecominfraproject.wlan.servicemetric.ServiceMetricServiceInterface;
import com.telecominfraproject.wlan.servicemetric.apnode.models.ApNodeMetrics;
import com.telecominfraproject.wlan.servicemetric.apnode.models.ApPerformance;
import com.telecominfraproject.wlan.servicemetric.apnode.models.EthernetLinkState;
import com.telecominfraproject.wlan.servicemetric.apnode.models.RadioUtilization;
import com.telecominfraproject.wlan.servicemetric.client.models.ClientMetrics;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetric;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDataType;
import com.telecominfraproject.wlan.status.StatusServiceInterface;
import com.telecominfraproject.wlan.status.dashboard.models.CustomerPortalDashboardStatus;
import com.telecominfraproject.wlan.status.equipment.models.EquipmentAdminStatusData;
import com.telecominfraproject.wlan.status.equipment.models.EquipmentProtocolState;
import com.telecominfraproject.wlan.status.equipment.models.EquipmentProtocolStatusData;
import com.telecominfraproject.wlan.status.equipment.report.models.EquipmentCapacityDetails;
import com.telecominfraproject.wlan.status.equipment.report.models.EquipmentPerRadioUtilizationDetails;
import com.telecominfraproject.wlan.status.equipment.report.models.OperatingSystemPerformance;
import com.telecominfraproject.wlan.status.equipment.report.models.RadioUtilizationReport;
import com.telecominfraproject.wlan.status.models.Status;
import com.telecominfraproject.wlan.status.models.StatusCode;
import com.telecominfraproject.wlan.status.models.events.StatusChangedEvent;
import com.telecominfraproject.wlan.systemevent.SystemEventServiceInterface;
import com.telecominfraproject.wlan.systemevent.models.SystemEventRecord;

/**
 * Listen for context started event so that we can populate initial dataset in
 * the in-memory datastores
 */
@Configuration
public class AllInOneStartListener implements ApplicationRunner {

    private static final Logger LOG = LoggerFactory.getLogger(AllInOneStartListener.class);

    @Autowired
    private CustomerServiceInterface customerServiceInterface;

    @Autowired
    private LocationServiceInterface locationServiceInterface;

    @Autowired
    private EquipmentServiceInterface equipmentServiceInterface;

    @Autowired
    private ProfileServiceInterface profileServiceInterface;

    @Autowired
    private StatusServiceInterface statusServiceInterface;

    @Autowired
    private ClientServiceInterface clientServiceInterface;

    @Autowired
    private AlarmServiceInterface alarmServiceInterface;

    @Autowired
    private PortalUserServiceInterface portalUserServiceInterface;

    @Autowired
    private ServiceMetricServiceInterface serviceMetricInterface;

    @Autowired
    private SystemEventServiceInterface systemEventInterface;

    @Autowired
    private FirmwareServiceInterface firmwareInterface;

    @Value("${tip.wlan.numEquipmentToCreateOnStartup:0}")
    private int numEquipmentToCreateOnStartup;

    @Value("${tip.wlan.numClientsPerApToCreateOnStartup:0}")
    private int numClientsPerApToCreateOnStartup;

    @Value("${tip.wlan.numMetricsPerEquipmentToCreateOnStartup:5}")
    private int numMetricsPerEquipmentToCreateOnStartup;


    @Override
    public void run(ApplicationArguments args) {
        LOG.info("Creating initial objects");
        try {
            createInitialObjects(args);
        } catch (Exception e) {
            LOG.error("Got Exception ", e);
            throw e;
        }
    }

    public void createInitialObjects(ApplicationArguments args) {
        Customer customer = new Customer();
        customer.setEmail("test@example.com");
        customer.setName("Test Customer");

        customer = customerServiceInterface.create(customer);

        for (int i = 0; i < 5; i++) {
            PortalUser portalUser = new PortalUser();
            portalUser.setCustomerId(customer.getId());
            portalUser.setRole(PortalUserRole.CustomerIT);
            portalUser.setPassword("pwd" + i);
            portalUser.setUsername("user-" + i);
            portalUserServiceInterface.create(portalUser);
        }

        Location location_1 = new Location();
        location_1.setParentId(0);
        location_1.setCustomerId(customer.getId());
        location_1.setLocationType(LocationType.SITE);
        location_1.setName("Menlo Park");
        location_1.setDetails(LocationDetails.createWithDefaults());
        location_1.getDetails().setCountryCode(CountryCode.usa);

        location_1 = locationServiceInterface.create(location_1);

        Location location_1_1 = new Location();
        location_1_1.setParentId(location_1.getId());
        location_1_1.setCustomerId(customer.getId());
        location_1_1.setLocationType(LocationType.BUILDING);
        location_1_1.setName("Building 1");

        location_1_1 = locationServiceInterface.create(location_1_1);

        Location location_1_1_1 = new Location();
        location_1_1_1.setParentId(location_1_1.getId());
        location_1_1_1.setCustomerId(customer.getId());
        location_1_1_1.setLocationType(LocationType.FLOOR);
        location_1_1_1.setName("Floor 1");

        location_1_1_1 = locationServiceInterface.create(location_1_1_1);

        Location location_1_1_2 = new Location();
        location_1_1_2.setParentId(location_1_1.getId());
        location_1_1_2.setCustomerId(customer.getId());
        location_1_1_2.setLocationType(LocationType.FLOOR);
        location_1_1_2.setName("Floor 2");

        location_1_1_2 = locationServiceInterface.create(location_1_1_2);

        Location location_1_1_3 = new Location();
        location_1_1_3.setParentId(location_1_1.getId());
        location_1_1_3.setCustomerId(customer.getId());
        location_1_1_3.setLocationType(LocationType.FLOOR);
        location_1_1_3.setName("Floor 3");

        location_1_1_3 = locationServiceInterface.create(location_1_1_3);

        Location location_1_2 = new Location();
        location_1_2.setParentId(location_1.getId());
        location_1_2.setCustomerId(customer.getId());
        location_1_2.setLocationType(LocationType.BUILDING);
        location_1_2.setName("Building 2");

        location_1_2 = locationServiceInterface.create(location_1_2);

        Location location_2 = new Location();
        location_2.setParentId(0);
        location_2.setCustomerId(customer.getId());
        location_2.setLocationType(LocationType.SITE);
        location_2.setName("Ottawa");
        location_2.setDetails(LocationDetails.createWithDefaults());
        location_2.getDetails().setCountryCode(CountryCode.ca);

        location_2 = locationServiceInterface.create(location_2);

        Profile profileRadius = new Profile();
        profileRadius.setCustomerId(customer.getId());
        profileRadius.setProfileType(ProfileType.radius);
        profileRadius.setName("Radius-Profile");

        RadiusProfile radiusDetails = new RadiusProfile();
        RadiusServiceRegion radiusServiceRegion = new RadiusServiceRegion();
        RadiusServer radiusServer = new RadiusServer();
        radiusServer.setAuthPort(1812);
        try {
            radiusServer.setIpAddress(InetAddress.getByName("192.168.0.1"));
        } catch (UnknownHostException e) {
            throw new IllegalArgumentException(e);
        }
        radiusServer.setSecret("testing123");
        radiusServiceRegion.addRadiusServer("Radius-Profile", radiusServer);
        radiusServiceRegion.setRegionName("Ottawa");
        radiusDetails.addRadiusServiceRegion(radiusServiceRegion);
        profileRadius.setDetails(radiusDetails);
        profileRadius = profileServiceInterface.create(profileRadius);

        Profile profileMetrics_3_radios = new Profile();
        profileMetrics_3_radios.setCustomerId(customer.getId());
        profileMetrics_3_radios.setProfileType(ProfileType.metrics);
        profileMetrics_3_radios.setName("Metrics-Profile-3-radios");
        Set<RadioType> profileMetrics_3_radioTypes = new HashSet<>();
        profileMetrics_3_radioTypes.add(RadioType.is2dot4GHz);
        profileMetrics_3_radioTypes.add(RadioType.is5GHzL);
        profileMetrics_3_radioTypes.add(RadioType.is5GHzU);

        Set<ServiceMetricDataType> metricTypes = new HashSet<>();
        metricTypes.add(ServiceMetricDataType.ApNode);
        metricTypes.add(ServiceMetricDataType.ApSsid);
        metricTypes.add(ServiceMetricDataType.Channel);
        metricTypes.add(ServiceMetricDataType.Client);
        metricTypes.add(ServiceMetricDataType.Neighbour);
        ServiceMetricsCollectionConfigProfile metricsProfileDetails3Radios = ServiceMetricsCollectionConfigProfile
                .createWithDefaults();
        metricsProfileDetails3Radios.setAllNetworkConfigParametersToDefaults(profileMetrics_3_radioTypes, metricTypes,
                true);
        profileMetrics_3_radios.setDetails(metricsProfileDetails3Radios);
        profileMetrics_3_radios = profileServiceInterface.create(profileMetrics_3_radios);

        Profile profileMetrics_2_radios = new Profile();
        profileMetrics_2_radios.setCustomerId(customer.getId());
        profileMetrics_2_radios.setProfileType(ProfileType.metrics);
        profileMetrics_2_radios.setName("Metrics-Profile-2-radios");
        Set<RadioType> profileMetrics_2_radioTypes = new HashSet<>();
        profileMetrics_2_radioTypes.add(RadioType.is2dot4GHz);
        profileMetrics_2_radioTypes.add(RadioType.is5GHz);

        ServiceMetricsCollectionConfigProfile metricsProfileDetails2Radios = ServiceMetricsCollectionConfigProfile
                .createWithDefaults();
        metricsProfileDetails3Radios.setAllNetworkConfigParametersToDefaults(profileMetrics_2_radioTypes, metricTypes,
                true);

        profileMetrics_2_radios.setDetails(metricsProfileDetails2Radios);
        profileMetrics_2_radios = profileServiceInterface.create(profileMetrics_2_radios);

        Profile profileSsidEAP = new Profile();
        profileSsidEAP.setCustomerId(customer.getId());
        profileSsidEAP.setName("TipWlan-cloud-Enterprise");
        SsidConfiguration ssidConfigEAP = SsidConfiguration.createWithDefaults();
        Set<RadioType> appliedRadiosEAP = new HashSet<RadioType>();
        appliedRadiosEAP.add(RadioType.is2dot4GHz);
        appliedRadiosEAP.add(RadioType.is5GHzL);
        appliedRadiosEAP.add(RadioType.is5GHzU);
        ssidConfigEAP.setAppliedRadios(appliedRadiosEAP);
        ssidConfigEAP.setSecureMode(SecureMode.wpaEAP);
        ssidConfigEAP.setKeyStr("testing123");
        profileSsidEAP.setDetails(ssidConfigEAP);
        ssidConfigEAP.setRadiusServiceName(profileRadius.getName());
        Set<Long> childIds = new HashSet<Long>();
        childIds.add(profileRadius.getId());
        profileSsidEAP.setChildProfileIds(childIds);
        profileSsidEAP = profileServiceInterface.create(profileSsidEAP);

        Profile profileSsidPsk = new Profile();
        profileSsidPsk.setCustomerId(customer.getId());
        profileSsidPsk.setName("TipWlan-cloud-hotspot-access");
        SsidConfiguration ssidConfigPsk = SsidConfiguration.createWithDefaults();
        Set<RadioType> appliedRadiosPsk = new HashSet<RadioType>();
        appliedRadiosPsk.add(RadioType.is2dot4GHz);
        appliedRadiosPsk.add(RadioType.is5GHzL);
        appliedRadiosPsk.add(RadioType.is5GHzU);
        ssidConfigPsk.setSsid("TipWlan-cloud-hotspot-access");
        ssidConfigPsk.setAppliedRadios(appliedRadiosPsk);
        ssidConfigPsk.setSecureMode(SecureMode.wpa2PSK);
        ssidConfigPsk.setKeyStr("testing123");
        profileSsidPsk.setDetails(ssidConfigPsk);
        profileSsidPsk = profileServiceInterface.create(profileSsidPsk);

        Profile passpointOperatorProfile = new Profile();
        passpointOperatorProfile.setCustomerId(customer.getId());
        passpointOperatorProfile.setName("TipWlan-Hotspot20-Operator");
        passpointOperatorProfile.setProfileType(ProfileType.operator);
        passpointOperatorProfile.setDetails(OperatorProfile.createWithDefaults());
        passpointOperatorProfile = profileServiceInterface.create(passpointOperatorProfile);

        Profile passpointVenueProfile = new Profile();
        passpointVenueProfile.setCustomerId(customer.getId());
        passpointVenueProfile.setName("TipWlan-Hotspot20-Venue");
        passpointVenueProfile.setProfileType(ProfileType.venue);
        passpointVenueProfile.setDetails(VenueProfile.createWithDefaults());
        passpointVenueProfile = profileServiceInterface.create(passpointVenueProfile);

        Profile hotspot20IdProviderProfile = new Profile();
        hotspot20IdProviderProfile.setCustomerId(customer.getId());
        hotspot20IdProviderProfile.setName("TipWlan-Hotspot20-OSU-Provider");
        hotspot20IdProviderProfile.setProfileType(ProfileType.id_provider);
        MccMnc mccMnc = MccMnc.createWithDefaults();
        mccMnc.setMcc(302);
        mccMnc.setMnc(720);
        mccMnc.setIso("ca");
        mccMnc.setCountry("Canada");
        mccMnc.setCountryCode(1);
        mccMnc.setNetwork("Rogers AT&T Wireless");
        List<MccMnc> mccMncList = new ArrayList<>();
        mccMncList.add(mccMnc);
        Set<String> naiRealms = new HashSet<>();
        naiRealms.add("rogers.com");
        hotspot20IdProviderProfile = createOsuProviderProfile(customer, hotspot20IdProviderProfile, mccMncList,
                naiRealms,"https://example.com/osu/rogers/","rogers","rogers.com");

        Profile hotspot20IdProviderProfile2 = new Profile();
        hotspot20IdProviderProfile2.setCustomerId(customer.getId());
        hotspot20IdProviderProfile2.setName("TipWlan-Hotspot20-OSU-Provider-2");
        hotspot20IdProviderProfile2.setProfileType(ProfileType.id_provider);
        MccMnc mccMnc2 = MccMnc.createWithDefaults();
        mccMnc2.setMcc(302);
        mccMnc2.setMnc(220);
        mccMnc2.setIso("ca");
        mccMnc2.setCountry("Canada");
        mccMnc2.setCountryCode(1);
        mccMnc2.setNetwork("Telus Mobility");
        mccMncList = new ArrayList<>();
        mccMncList.add(mccMnc2);
        naiRealms = new HashSet<>();
        naiRealms.add("telus.com");
        hotspot20IdProviderProfile2 = createOsuProviderProfile(customer, hotspot20IdProviderProfile2, mccMncList,
                naiRealms, "https://example.com/osu/telus/","telus","telus.com");

        Profile passpointHotspotConfig = new Profile();
        passpointHotspotConfig.setCustomerId(customer.getId());
        passpointHotspotConfig.setName("TipWlan-Hotspot20-Config");
        passpointHotspotConfig.setProfileType(ProfileType.hotspot_2pt0);
        Set<Long> passpointHotspotConfigChildIds = new HashSet<>();
        passpointHotspotConfigChildIds.add(passpointOperatorProfile.getId());
        passpointHotspotConfigChildIds.add(passpointVenueProfile.getId());
        passpointHotspotConfigChildIds.add(hotspot20IdProviderProfile.getId());
        passpointHotspotConfigChildIds.add(hotspot20IdProviderProfile2.getId());
        passpointHotspotConfig.setChildProfileIds(passpointHotspotConfigChildIds);
        passpointHotspotConfig.setDetails(Hotspot2Profile.createWithDefaults());
        Set<String> providerNames = new HashSet<>();
        providerNames.add(hotspot20IdProviderProfile.getName());
        providerNames.add(hotspot20IdProviderProfile2.getName());
        ((Hotspot2Profile) passpointHotspotConfig.getDetails()).setIdProviderProfileNames(providerNames);
        ((Hotspot2Profile) passpointHotspotConfig.getDetails())
                .setOperatorProfileName(passpointOperatorProfile.getName());
        ((Hotspot2Profile) passpointHotspotConfig.getDetails()).setVenueProfileName(passpointVenueProfile.getName());
        passpointHotspotConfig = profileServiceInterface.create(passpointHotspotConfig);
        profileSsidPsk.getChildProfileIds().add(passpointHotspotConfig.getId());
        profileSsidPsk = profileServiceInterface.update(profileSsidPsk);

        Profile profileSsid_3_radios = new Profile();
        profileSsid_3_radios.setCustomerId(customer.getId());
        profileSsid_3_radios.setName("TipWlan-cloud-3-radios");
        SsidConfiguration ssidConfig_3_radios = SsidConfiguration.createWithDefaults();
        Set<RadioType> appliedRadios_3_radios = new HashSet<RadioType>();
        appliedRadios_3_radios.add(RadioType.is2dot4GHz);
        appliedRadios_3_radios.add(RadioType.is5GHzL);
        appliedRadios_3_radios.add(RadioType.is5GHzU);
        ssidConfig_3_radios.setAppliedRadios(appliedRadios_3_radios);
        ssidConfig_3_radios.setSsid("TipWlan-cloud-3-radios");
        profileSsid_3_radios.setDetails(ssidConfig_3_radios);
        Set<Long> ssidChildIds = new HashSet<>();
        ssidChildIds.add(hotspot20IdProviderProfile.getId());
        ssidChildIds.add(hotspot20IdProviderProfile2.getId());
        profileSsid_3_radios.setChildProfileIds(ssidChildIds);
        profileSsid_3_radios = profileServiceInterface.create(profileSsid_3_radios);

        Profile profileSsid_2_radios = new Profile();
        profileSsid_2_radios.setCustomerId(customer.getId());
        profileSsid_2_radios.setName("TipWlan-cloud-2-radios");
        SsidConfiguration ssidConfig_2_radios = SsidConfiguration.createWithDefaults();
        Set<RadioType> appliedRadios_2_radios = new HashSet<RadioType>();
        appliedRadios_2_radios.add(RadioType.is2dot4GHz);
        appliedRadios_2_radios.add(RadioType.is5GHz);
        ssidConfig_2_radios.setAppliedRadios(appliedRadios_2_radios);
        ssidConfig_2_radios.setSsid("TipWlan-cloud-2-radios");
        profileSsid_2_radios.setDetails(ssidConfig_2_radios);
        profileSsid_2_radios.setChildProfileIds(ssidChildIds);
        profileSsid_2_radios = profileServiceInterface.create(profileSsid_2_radios);

        Profile profileRf = new Profile();
        profileRf.setCustomerId(customer.getId());
        profileRf.setName("TipWlan-rf");
        RfConfiguration rfConfig = RfConfiguration.createWithDefaults();
        rfConfig.getRfConfigMap().forEach((x, y) -> y.setRf("TipWlan-rf"));
        profileRf.setDetails(rfConfig);
        profileRf = profileServiceInterface.create(profileRf);

        // Captive portal profile
        Profile profileCaptivePortal = new Profile();
        profileCaptivePortal.setCustomerId(customer.getId());
        profileCaptivePortal.setName("Captive-portal");
        profileCaptivePortal.setProfileType(ProfileType.captive_portal);

        CaptivePortalConfiguration captivePortalConfig = new CaptivePortalConfiguration();
        captivePortalConfig.setAuthenticationType(CaptivePortalAuthenticationType.guest);
        captivePortalConfig.setBrowserTitle("Access the network as Guest");
        captivePortalConfig.setExpiryType(SessionExpiryType.unlimited);
        captivePortalConfig.setMaxUsersWithSameCredentials(42);
        captivePortalConfig.setName(profileCaptivePortal.getName());
        captivePortalConfig.setSuccessPageMarkdownText("Welcome to the network");
        captivePortalConfig.setUserAcceptancePolicy("Use this network at your own risk. No warranty of any kind.");

        List<TimedAccessUserRecord> userList = new ArrayList<>();
        constructCaptivePortalUserList(userList);
        captivePortalConfig.setUserList(userList);

        ManagedFileInfo info = new ManagedFileInfo();
        info.setAltSlot(true);
        info.setApExportUrl("userList");
        info.setFileCategory(FileCategory.UsernamePasswordList);
        info.setFileType(FileType.TEXT);
        captivePortalConfig.setUsernamePasswordFile(info);
        profileCaptivePortal.setDetails(captivePortalConfig);

        profileCaptivePortal = profileServiceInterface.create(profileCaptivePortal);

        // BonjourGateway profile
        Profile bonjourProfile = new Profile();
        bonjourProfile.setCustomerId(customer.getId());
        bonjourProfile.setName("Bonjour-gateway");
        bonjourProfile.setProfileType(ProfileType.bonjour);
        BonjourGatewayProfile bonjourGatewayConfig = new BonjourGatewayProfile();
        bonjourGatewayConfig.setProfileDescription("Bonjour Gateway Configuration for Design Testing");
        BonjourServiceSet bonjourServiceSet = new BonjourServiceSet();
        bonjourServiceSet.setVlanId((short) 1);
        bonjourServiceSet.addServiceName(BonjourService.SFTP.getServiceName());
        bonjourServiceSet.addServiceName(BonjourService.SSH.getServiceName());
        bonjourServiceSet.addServiceName(BonjourService.AirPort.getServiceName());
        bonjourGatewayConfig.addBonjourServiceSet(bonjourServiceSet);
        bonjourProfile.setDetails(bonjourGatewayConfig);
        bonjourProfile = profileServiceInterface.create(bonjourProfile);


        Profile profileSsid_3_radios_plusBonjour = new Profile();
        profileSsid_3_radios_plusBonjour.setCustomerId(customer.getId());
        profileSsid_3_radios_plusBonjour.setName("TipWlan-3-radios-bonjour");
        profileSsid_3_radios_plusBonjour.setProfileType(ProfileType.ssid);
        SsidConfiguration ssidConfig_3_radios_plusBonjour = SsidConfiguration.createWithDefaults();
        ssidConfig_3_radios_plusBonjour.setBonjourGatewayProfileId(bonjourProfile.getId());
        ssidConfig_3_radios_plusBonjour.setVlanId(1);
        profileSsid_3_radios_plusBonjour.getChildProfileIds().add(bonjourProfile.getId());
        // reuse from existing 3 radio applied
        ssidConfig_3_radios_plusBonjour.setAppliedRadios(appliedRadios_3_radios);
        ssidConfig_3_radios_plusBonjour.setSsid("TipWlan-3-radios-bonjour");
        profileSsid_3_radios_plusBonjour.setDetails(ssidConfig_3_radios_plusBonjour);
        profileSsid_3_radios_plusBonjour = profileServiceInterface.create(profileSsid_3_radios_plusBonjour);


        Profile profileAp_3_radios = new Profile();
        profileAp_3_radios.setCustomerId(customer.getId());
        profileAp_3_radios.setName("ApProfile-3-radios");
        profileAp_3_radios.setDetails(ApNetworkConfiguration.createWithDefaults());

        Map<RadioType, RadioProfileConfiguration> radioProfileMap_3_radios = new HashMap<>();
        radioProfileMap_3_radios.put(RadioType.is2dot4GHz,
                RadioProfileConfiguration.createWithDefaults(RadioType.is2dot4GHz));
        radioProfileMap_3_radios.put(RadioType.is5GHzL,
                RadioProfileConfiguration.createWithDefaults(RadioType.is5GHzL));
        radioProfileMap_3_radios.put(RadioType.is5GHzU,
                RadioProfileConfiguration.createWithDefaults(RadioType.is5GHzU));
        ((ApNetworkConfiguration) profileAp_3_radios.getDetails()).setRadioMap(radioProfileMap_3_radios);
        profileAp_3_radios.getChildProfileIds().add(profileSsid_3_radios.getId());
        profileAp_3_radios.getChildProfileIds().add(profileSsidPsk.getId());
        profileAp_3_radios.getChildProfileIds().add(profileMetrics_3_radios.getId());
        profileAp_3_radios.getChildProfileIds().add(profileRf.getId());
        profileAp_3_radios = profileServiceInterface.create(profileAp_3_radios);

        // TipWlan-cloud-hotspot-access
        Profile profileAp_2_radios = new Profile();
        profileAp_2_radios.setCustomerId(customer.getId());
        profileAp_2_radios.setName("ApProfile-2-radios");
        profileAp_2_radios.setDetails(ApNetworkConfiguration.createWithDefaults());
        Map<RadioType, RadioProfileConfiguration> radioProfileMap_2_radios = new HashMap<>();
        radioProfileMap_2_radios.put(RadioType.is2dot4GHz,
                RadioProfileConfiguration.createWithDefaults(RadioType.is2dot4GHz));
        radioProfileMap_2_radios.put(RadioType.is5GHz, RadioProfileConfiguration.createWithDefaults(RadioType.is5GHz));
        ((ApNetworkConfiguration) profileAp_2_radios.getDetails()).setRadioMap(radioProfileMap_2_radios);
        profileAp_2_radios.getChildProfileIds().add(profileSsid_2_radios.getId());
        profileAp_2_radios.getChildProfileIds().add(profileMetrics_2_radios.getId());
        profileAp_3_radios.getChildProfileIds().add(profileRf.getId());
        profileAp_2_radios = profileServiceInterface.create(profileAp_2_radios);

        Profile enterpriseProfileAp = new Profile();
        enterpriseProfileAp.setCustomerId(customer.getId());
        enterpriseProfileAp.setName("EnterpriseApProfile");
        enterpriseProfileAp.setDetails(ApNetworkConfiguration.createWithDefaults());
        enterpriseProfileAp.getChildProfileIds().add(profileSsidEAP.getId());
        enterpriseProfileAp = profileServiceInterface.create(enterpriseProfileAp);

        Profile pskProfileAp = new Profile();
        pskProfileAp.setCustomerId(customer.getId());
        pskProfileAp.setName("PskApProfile");
        pskProfileAp.setDetails(ApNetworkConfiguration.createWithDefaults());
        pskProfileAp.getChildProfileIds().add(profileSsidPsk.getId());
        pskProfileAp = profileServiceInterface.create(profileSsidPsk);

        // configure equipment auto-provisioning for the customer
        CustomerDetails details = new CustomerDetails();
        EquipmentAutoProvisioningSettings autoProvisioning = new EquipmentAutoProvisioningSettings();
        autoProvisioning.setEnabled(true);
        autoProvisioning.setLocationId(location_2.getId());

        // populate auto-provisioning equipment profiles per model
        Map<String, Long> equipmentProfileIdPerModel = new HashMap<>();
        equipmentProfileIdPerModel.put(EquipmentAutoProvisioningSettings.DEFAULT_MODEL_NAME,
                profileAp_3_radios.getId());
        equipmentProfileIdPerModel.put("EA8300-CA", profileAp_3_radios.getId());
        equipmentProfileIdPerModel.put("EA8300", profileAp_3_radios.getId());
        equipmentProfileIdPerModel.put("TIP_AP", profileAp_2_radios.getId());
        equipmentProfileIdPerModel.put("ECW5410", profileAp_2_radios.getId());
        equipmentProfileIdPerModel.put("ECW5211", profileAp_2_radios.getId());
        equipmentProfileIdPerModel.put("AP2220", profileAp_2_radios.getId());

        autoProvisioning.setEquipmentProfileIdPerModel(equipmentProfileIdPerModel);
        details.setAutoProvisioning(autoProvisioning);

        customer.setDetails(details);
        customer = customerServiceInterface.update(customer);

        List<Equipment> equipmentList = new ArrayList<>();

        for (int i = 1; i <= numEquipmentToCreateOnStartup; i++) {
            Equipment equipment = new Equipment();
            equipment.setCustomerId(customer.getId());
            equipment.setEquipmentType(EquipmentType.AP);

            // spread APs across locations
            if (i <= 12) {
                equipment.setLocationId(location_1_1_1.getId());
            } else if (i <= 15) {
                equipment.setLocationId(location_1_1_2.getId());
            } else if (i <= 21) {
                equipment.setLocationId(location_1_1_3.getId());
            } else if (i <= 32) {
                equipment.setLocationId(location_1_2.getId());
            } else {
                equipment.setLocationId(location_1_2.getId());
            }

            // spread AP profiles between Enterprise SSID based and SSID
            // setting the region to the location used in location_2, so assign
            // profiles
            // based on this
            if (i <= 32) {
                equipment.setProfileId(profileAp_3_radios.getId());
            } else {
                equipment.setProfileId(enterpriseProfileAp.getId());
            }
            equipment.setInventoryId("ap-" + i);
            equipment.setName("AP " + i);
            equipment.setBaseMacAddress(new MacAddress(
                    new byte[] { 0x74, (byte) 0x9C, (byte) 0xE3, getRandomByte(), getRandomByte(), getRandomByte() }));
            equipment.setSerial("serial-ap-" + i);
            equipment.setDetails(ApElementConfiguration.createWithDefaults());

            String eqModel;
            switch (i % 4) {
                case 0:
                    eqModel = "ea8300";
                    break;
                case 1:
                    eqModel = "ecw5211";
                    break;
                case 2:
                    eqModel = "ecw5410";
                    break;
                case 3:
                    eqModel = "ap2220";
                    break;
                default:
                    eqModel = "ap2220";
            }
            equipment.getDetails().setEquipmentModel(eqModel);

            equipment = equipmentServiceInterface.create(equipment);
            equipmentList.add(equipment);

            createStatusForEquipment(equipment);

            createAlarmsForEquipment(equipment);

            if (i <= 32) {
                createClientSessions(equipment, ssidConfig_3_radios);
            } else {
                createClientSessions(equipment, ssidConfigEAP);
            }

            createServiceMetrics(equipment);

        }

        createDashboardStatus(customer, equipmentList);

        createFirmwareObjects(customer);

        LOG.info("Done creating initial objects");

        if (equipmentList.size() > 40) {
            // print out SSID configurations used by ap-1
            ProfileContainer profileContainer = new ProfileContainer(
                    profileServiceInterface.getProfileWithChildren(equipmentList.get(0).getProfileId()));

            List<Profile> ssidProfiles = profileContainer.getChildrenOfType(equipmentList.get(0).getProfileId(),
                    ProfileType.ssid);
            List<SsidConfiguration> ssidConfigs = new ArrayList<>();
            ssidProfiles.forEach(p -> ssidConfigs.add((SsidConfiguration) p.getDetails()));
            LOG.info("SSID configs: {}", ssidConfigs);

            // print out SSID configurations used by ap-33
            profileContainer = new ProfileContainer(
                    profileServiceInterface.getProfileWithChildren(equipmentList.get(32).getProfileId()));

            ssidProfiles = profileContainer.getChildrenOfType(equipmentList.get(32).getProfileId(), ProfileType.ssid);
            List<SsidConfiguration> ssidConfigs2 = new ArrayList<>();

            ssidProfiles.forEach(p -> ssidConfigs2.add((SsidConfiguration) p.getDetails()));
            LOG.info("Enterprise SSID configs: {}", ssidConfigs2);
        }

    }

    protected Profile createOsuProviderProfile(Customer customer, Profile hotspot20IdProviderProfile,
            List<MccMnc> mccMncList, Set<String> realms, String serverUri, String suffix, String domainName) {

        Hotspot20IdProviderProfile passpointIdProviderProfile = Hotspot20IdProviderProfile.createWithDefaults();

        passpointIdProviderProfile.setMccMncList(mccMncList);
        OsuIcon icon1 = new OsuIcon();
        icon1.setIconLocale(Locale.CANADA); 
        icon1.setIconWidth(32);
        icon1.setIconHeight(32);
        icon1.setLanguageCode(Locale.CANADA.getISO3Language()); 
        icon1.setIconName("icon32eng");
        icon1.setImageUrl("https://localhost:9096/icon32eng.png");
        icon1.setFilePath("/tmp/icon32eng.png");
        OsuIcon icon2 = new OsuIcon();
        icon2.setIconLocale(Locale.CANADA_FRENCH); 
        icon2.setIconWidth(32);
        icon2.setIconHeight(32);
        icon2.setLanguageCode(Locale.CANADA_FRENCH.getISO3Language()); 
        icon2.setIconName("icon32fra");
        icon2.setImageUrl("https://localhost:9096/icon32fra.png");
        icon2.setFilePath("/tmp/icon32fra.png");
        OsuIcon icon3 = new OsuIcon();
        icon3.setIconLocale(Locale.US); 
        icon3.setIconWidth(32);
        icon3.setIconHeight(32);
        icon3.setLanguageCode(Locale.US.getISO3Language()); 
        icon3.setIconName("icon32usa");
        icon3.setImageUrl("https://localhost:9096/icon32usa.png");
        icon3.setFilePath("/tmp/icon32usa.png");
        List<OsuIcon> osuIconList = new ArrayList<>();
        osuIconList.add(icon1);
        osuIconList.add(icon2);
        osuIconList.add(icon3);
        passpointIdProviderProfile.setOsuIconList(osuIconList);
        List<NaiRealmInformation> naiRealmList = new ArrayList<>();

        NaiRealmInformation naiRealmInfo = NaiRealmInformation.createWithDefaults();
        naiRealmInfo.setNaiRealms(realms);

        naiRealmList.add(naiRealmInfo);
        passpointIdProviderProfile.setNaiRealmList(naiRealmList);
        passpointIdProviderProfile.setDomainName(domainName);
        passpointIdProviderProfile.setOsuNaiStandalone("anonymous@" + domainName);
        passpointIdProviderProfile.setOsuNaiShared("anonymous@" + domainName);
        List<Integer> methodList = new ArrayList<>();
        methodList.add(1);
        methodList.add(0);
        passpointIdProviderProfile.setOsuMethodList(methodList);
        Hotspot20Duple enOsuProvider = Hotspot20Duple.createWithDefaults();
        enOsuProvider.setLocale(Locale.CANADA);
        enOsuProvider.setDupleName("Example provider " + suffix);
        Hotspot20Duple frOsuProvider = Hotspot20Duple.createWithDefaults();
        frOsuProvider.setLocale(Locale.CANADA_FRENCH);
        frOsuProvider.setDupleName("Exemple de fournisseur " + suffix);
        List<Hotspot20Duple> friendlyNameList = new ArrayList<>();
        friendlyNameList.add(enOsuProvider);
        friendlyNameList.add(frOsuProvider);
        passpointIdProviderProfile.setOsuFriendlyName(friendlyNameList);
        List<Hotspot20Duple> osuServiceDescription = new ArrayList<>();
        Hotspot20Duple enService = Hotspot20Duple.createWithDefaults();
        enService.setLocale(Locale.CANADA);
        enService.setDupleName("Example services " + suffix);
        osuServiceDescription.add(enService);
        Hotspot20Duple frService = Hotspot20Duple.createWithDefaults();
        frService.setLocale(Locale.CANADA_FRENCH);
        frService.setDupleName("Exemples de services " + suffix);
        osuServiceDescription.add(frService);
        passpointIdProviderProfile.setOsuServiceDescription(osuServiceDescription);
        passpointIdProviderProfile.setOsuSsid("TipWlan-cloud-3-radios");
        passpointIdProviderProfile.setOsuServerUri(serverUri);
        passpointIdProviderProfile.setRadiusProfileAccounting("Radius-Profile");
        passpointIdProviderProfile.setRadiusProfileAuth("Radius-Profile");
        hotspot20IdProviderProfile.setDetails(passpointIdProviderProfile);
        hotspot20IdProviderProfile = profileServiceInterface.create(hotspot20IdProviderProfile);
        return hotspot20IdProviderProfile;
    }

    protected void constructCaptivePortalUserList(List<TimedAccessUserRecord> userList) {

        TimedAccessUserRecord userRecord1 = new TimedAccessUserRecord();
        userRecord1.setActivationTime(System.currentTimeMillis());
        userRecord1.setExpirationTime(System.currentTimeMillis() + 1000 * 60 * 60 * 8); // 8
        // hr
        userRecord1.setPassword("testing123");
        userRecord1.setUsername("customer");
        TimedAccessUserDetails userDetails1 = new TimedAccessUserDetails();
        userDetails1.setFirstName("Pac");
        userDetails1.setLastName("Man");
        userDetails1.setPasswordNeedsReset(false);
        userRecord1.setUserDetails(userDetails1);
        userRecord1.setNumDevices(1);

        MacAddress macAddress = new MacAddress("7C:AB:60:E6:EA:4E");
        List<MacAddress> userMacAddresses1 = new ArrayList<>();
        userMacAddresses1.add(macAddress);
        userRecord1.setUserMacAddresses(userMacAddresses1);
        userList.add(userRecord1);

        TimedAccessUserRecord userRecord2 = new TimedAccessUserRecord();
        userRecord2.setActivationTime(System.currentTimeMillis());
        userRecord2.setExpirationTime(System.currentTimeMillis() + 1000 * 60 * 60); // 1
                                                                                    // hr
        userRecord2.setPassword("testing123");
        userRecord2.setUsername("customer");
        TimedAccessUserDetails userDetails2 = new TimedAccessUserDetails();
        userDetails2.setFirstName("Q");
        userDetails2.setLastName("Bert");
        userDetails2.setPasswordNeedsReset(false);
        userRecord2.setUserDetails(userDetails2);

        MacAddress macAddress2 = new MacAddress("C0:9A:D0:76:A8:68");
        List<MacAddress> userMacAddresses2 = new ArrayList<>();

        userMacAddresses2.add(macAddress2);
        userRecord2.setUserMacAddresses(userMacAddresses2);
        userList.add(userRecord2);

        TimedAccessUserRecord userRecord3 = new TimedAccessUserRecord();
        userRecord3.setActivationTime(System.currentTimeMillis());
        userRecord3.setExpirationTime(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7); // 1
        // week
        userRecord3.setPassword("testing1234");
        userRecord3.setUsername("customer2");
        TimedAccessUserDetails userDetails3 = new TimedAccessUserDetails();
        userDetails3.setFirstName("Duke");
        userDetails3.setLastName("Nukem");
        userDetails3.setPasswordNeedsReset(false);
        userRecord3.setUserDetails(userDetails3);
        userRecord3.setNumDevices(1);

        MacAddress macAddress3 = new MacAddress("7C:AB:60:E6:EA:4D");
        List<MacAddress> userMacAddresses3 = new ArrayList<>();
        userMacAddresses3.add(macAddress3);
        userRecord3.setUserMacAddresses(userMacAddresses3);
        userList.add(userRecord3);

        TimedAccessUserRecord userRecord4 = new TimedAccessUserRecord();
        userRecord4.setActivationTime(System.currentTimeMillis());
        userRecord4.setExpirationTime(System.currentTimeMillis() + 1000 * 60 * 60 * 24); // 1
        // day
        userRecord4.setPassword("testing1234");
        userRecord4.setUsername("customer2");
        TimedAccessUserDetails userDetails4 = new TimedAccessUserDetails();
        userDetails4.setFirstName("Missile");
        userDetails4.setLastName("Commander");
        userDetails4.setPasswordNeedsReset(false);
        userRecord4.setUserDetails(userDetails4);

        MacAddress macAddress4 = new MacAddress("C0:9A:D0:76:A8:63");
        List<MacAddress> userMacAddresses4 = new ArrayList<>();

        userMacAddresses4.add(macAddress4);
        userRecord4.setUserMacAddresses(userMacAddresses4);
        userList.add(userRecord4);


        Path path = Paths.get("/tmp/tip-wlan-filestore/userList");

        try {
            Files.deleteIfExists(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (TimedAccessUserRecord userRecord : userList) {

            byte[] bytes = ("username=" + userRecord.getUsername() + ", password=" + userRecord.getPassword()
                    + ", firstname=" + userRecord.getUserDetails().getFirstName() + ", lastname="
                    + userRecord.getUserDetails().getLastName() + System.lineSeparator()).getBytes();
            try {
                Files.write(path, bytes, StandardOpenOption.APPEND);
                System.out.println("Successfully written data to the file");
            } catch (NoSuchFileException e) {
                try {
                    Files.write(path, bytes);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();

            }


        }

    }

    private void createFirmwareObjects(Customer customer) {
        FirmwareVersion firmwareVersion_ap2220 = new FirmwareVersion();
        firmwareVersion_ap2220.setEquipmentType(EquipmentType.AP);
        firmwareVersion_ap2220.setVersionName("ap2220-2020-06-25-ce03472");
        firmwareVersion_ap2220.setCommit("ce03472");
        firmwareVersion_ap2220.setDescription("");
        firmwareVersion_ap2220.setModelId("ap2220");
        firmwareVersion_ap2220.setFilename(
                "https://tip-read:tip-read@tip.jfrog.io/artifactory/tip-wlan-ap-firmware/ap2220/ap2220-2020-06-25-ce03472.tar.gz");
        firmwareVersion_ap2220.setValidationMethod(ValidationMethod.MD5_CHECKSUM);
        firmwareVersion_ap2220.setValidationCode("c69370aa5b6622d91a0fba3a5441f31c");
        firmwareVersion_ap2220.setReleaseDate(System.currentTimeMillis());

        firmwareVersion_ap2220 = firmwareInterface.createFirmwareVersion(firmwareVersion_ap2220);

        FirmwareVersion firmwareVersion_ea8300 = new FirmwareVersion();
        firmwareVersion_ea8300.setEquipmentType(EquipmentType.AP);
        firmwareVersion_ea8300.setVersionName("ea8300-2020-06-25-ce03472");
        firmwareVersion_ea8300.setCommit("ce03472");
        firmwareVersion_ea8300.setDescription("");
        firmwareVersion_ea8300.setModelId("ea8300");
        firmwareVersion_ea8300.setFilename(
                "https://tip-read:tip-read@tip-read:tip-read@tip.jfrog.io/artifactory/tip-wlan-ap-firmware/ea8300/ea8300-2020-06-25-ce03472.tar.gz");
        firmwareVersion_ea8300.setValidationMethod(ValidationMethod.MD5_CHECKSUM);
        firmwareVersion_ea8300.setValidationCode("b209deb9847bdf40a31e45edf2e5a8d7");
        firmwareVersion_ea8300.setReleaseDate(System.currentTimeMillis());

        firmwareVersion_ea8300 = firmwareInterface.createFirmwareVersion(firmwareVersion_ea8300);

        FirmwareVersion firmwareVersion_ea8300ca = new FirmwareVersion();
        firmwareVersion_ea8300ca.setEquipmentType(EquipmentType.AP);
        firmwareVersion_ea8300ca.setVersionName("ea8300-2020-06-25-ce03472");
        firmwareVersion_ea8300ca.setCommit("ce03472");
        firmwareVersion_ea8300ca.setDescription("");
        firmwareVersion_ea8300ca.setModelId("ea8300-ca");
        firmwareVersion_ea8300ca.setFilename(
                "https://tip-read:tip-read@tip.jfrog.io/artifactory/tip-wlan-ap-firmware/ea8300/ea8300-2020-06-25-ce03472.tar.gz");
        firmwareVersion_ea8300ca.setValidationMethod(ValidationMethod.MD5_CHECKSUM);
        firmwareVersion_ea8300ca.setValidationCode("b209deb9847bdf40a31e45edf2e5a8d7");
        firmwareVersion_ea8300ca.setReleaseDate(System.currentTimeMillis());

        firmwareVersion_ea8300ca = firmwareInterface.createFirmwareVersion(firmwareVersion_ea8300ca);

        FirmwareVersion firmwareVersion_ecw5211 = new FirmwareVersion();
        firmwareVersion_ecw5211.setEquipmentType(EquipmentType.AP);
        firmwareVersion_ecw5211.setVersionName("ecw5211-2020-06-26-4ff7208");
        firmwareVersion_ecw5211.setCommit("4ff7208");
        firmwareVersion_ecw5211.setDescription("");
        firmwareVersion_ecw5211.setModelId("ecw5211");
        firmwareVersion_ecw5211.setFilename(
                "https://tip-read:tip-read@tip.jfrog.io/artifactory/tip-wlan-ap-firmware/ecw5211/ecw5211-2020-06-26-4ff7208.tar.gz");
        firmwareVersion_ecw5211.setValidationMethod(ValidationMethod.MD5_CHECKSUM);
        firmwareVersion_ecw5211.setValidationCode("133072b0e8a440063109604375938fba");
        firmwareVersion_ecw5211.setReleaseDate(System.currentTimeMillis());

        firmwareVersion_ecw5211 = firmwareInterface.createFirmwareVersion(firmwareVersion_ecw5211);

        FirmwareVersion firmwareVersion_ecw5410 = new FirmwareVersion();
        firmwareVersion_ecw5410.setEquipmentType(EquipmentType.AP);
        firmwareVersion_ecw5410.setVersionName("ecw5410-2020-06-25-ce03472");
        firmwareVersion_ecw5410.setCommit("ce03472");
        firmwareVersion_ecw5410.setDescription("");
        firmwareVersion_ecw5410.setModelId("ecw5410");
        firmwareVersion_ecw5410.setFilename(
                "https://tip-read:tip-read@tip.jfrog.io/artifactory/tip-wlan-ap-firmware/ecw5410/ecw5410-2020-06-25-ce03472.tar.gz");
        firmwareVersion_ecw5410.setValidationMethod(ValidationMethod.MD5_CHECKSUM);
        firmwareVersion_ecw5410.setValidationCode("2940ca34eeab85be18f3a4b79f4da6d9");
        firmwareVersion_ecw5410.setReleaseDate(System.currentTimeMillis());

        firmwareVersion_ecw5410 = firmwareInterface.createFirmwareVersion(firmwareVersion_ecw5410);

        FirmwareTrackRecord track = new FirmwareTrackRecord(FirmwareTrackRecord.DEFAULT_TRACK_NAME);
        track.setMaintenanceWindow(new EmptySchedule());
        track = firmwareInterface.createFirmwareTrack(track);

        firmwareInterface.updateFirmwareTrackAssignment(new FirmwareTrackAssignmentDetails(
                new FirmwareTrackAssignmentRecord(track.getRecordId(), firmwareVersion_ap2220.getId()),
                firmwareVersion_ap2220));

        firmwareInterface.updateFirmwareTrackAssignment(new FirmwareTrackAssignmentDetails(
                new FirmwareTrackAssignmentRecord(track.getRecordId(), firmwareVersion_ea8300.getId()),
                firmwareVersion_ea8300));

        firmwareInterface.updateFirmwareTrackAssignment(new FirmwareTrackAssignmentDetails(
                new FirmwareTrackAssignmentRecord(track.getRecordId(), firmwareVersion_ea8300ca.getId()),
                firmwareVersion_ea8300ca));

        firmwareInterface.updateFirmwareTrackAssignment(new FirmwareTrackAssignmentDetails(
                new FirmwareTrackAssignmentRecord(track.getRecordId(), firmwareVersion_ecw5211.getId()),
                firmwareVersion_ecw5211));

        firmwareInterface.updateFirmwareTrackAssignment(new FirmwareTrackAssignmentDetails(
                new FirmwareTrackAssignmentRecord(track.getRecordId(), firmwareVersion_ecw5410.getId()),
                firmwareVersion_ecw5410));

        CustomerFirmwareTrackRecord customerTrack = new CustomerFirmwareTrackRecord(track.getRecordId(),
                customer.getId());
        customerTrack.getSettings().setAutoUpgradeDeprecatedDuringMaintenance(TrackFlag.DEFAULT);
        customerTrack.getSettings().setAutoUpgradeDeprecatedOnBind(TrackFlag.DEFAULT);
        customerTrack.getSettings().setAutoUpgradeUnknownDuringMaintenance(TrackFlag.DEFAULT);
        customerTrack.getSettings().setAutoUpgradeUnknownOnBind(TrackFlag.DEFAULT);

        customerTrack = firmwareInterface.createCustomerFirmwareTrackRecord(customerTrack);

    }

    private void createDashboardStatus(Customer customer, List<Equipment> equipmentList) {
        // create current status
        Status status = createDashboardStatusObject(customer, equipmentList);
        status = statusServiceInterface.update(status);

        long currentTs = System.currentTimeMillis();

        List<SystemEventRecord> systemEventRecords = new ArrayList<>();
        // create history of statuses for the last 4 hours - each status changes
        // every 5 minutes, 12 times per hour
        for (int i = 0; i < 12 * 4; i++) {
            currentTs -= ((CustomerPortalDashboardStatus) status.getDetails()).getTimeBucketMs();

            status = createDashboardStatusObject(customer, equipmentList);
            status.setCreatedTimestamp(currentTs);
            status.setLastModifiedTimestamp(currentTs);
            CustomerPortalDashboardStatus ds = (CustomerPortalDashboardStatus) status.getDetails();
            ds.setTimeBucketId(ds.getTimeBucketId() - (ds.getTimeBucketMs() * i));
            ds.getTrafficBytesDownstream().addAndGet(getRandomLong(-5000000, 5000000));
            ds.getTrafficBytesUpstream().addAndGet(getRandomLong(-5000000, 5000000));

            SystemEventRecord ser = new SystemEventRecord(new StatusChangedEvent(status));
            systemEventRecords.add(ser);

        }
        systemEventInterface.create(systemEventRecords);

    }

    private Status createDashboardStatusObject(Customer customer, List<Equipment> equipmentList) {
        CustomerPortalDashboardStatus dashboardStatus = new CustomerPortalDashboardStatus();
        long timeBucketId = System.currentTimeMillis();
        timeBucketId = timeBucketId - timeBucketId % dashboardStatus.getTimeBucketMs();
        dashboardStatus.setTimeBucketId(timeBucketId);

        dashboardStatus.setTotalProvisionedEquipment(equipmentList.size());
        dashboardStatus.incrementAssociatedClientsCountPerRadio(RadioType.is2dot4GHz,
                equipmentList.size() * getRandomInt(1, 20));
        dashboardStatus.incrementAssociatedClientsCountPerRadio(RadioType.is5GHzL,
                equipmentList.size() * getRandomInt(1, 20));
        dashboardStatus.incrementAssociatedClientsCountPerRadio(RadioType.is5GHzU,
                equipmentList.size() * getRandomInt(1, 20));

        dashboardStatus.incrementClientCountPerOui("000000", 8);
        dashboardStatus.incrementClientCountPerOui("000001", 30);
        dashboardStatus.incrementClientCountPerOui("0F0F0F", 42);

        dashboardStatus.incrementEquipmentCountPerOui("749CE3", 50);
        dashboardStatus.incrementEquipmentCountPerOui("3C2C99", 1);
        dashboardStatus.incrementEquipmentCountPerOui("1CEA0B", 1);

        dashboardStatus.getEquipmentInServiceCount().addAndGet(2);

        dashboardStatus.getEquipmentWithClientsCount().addAndGet(2);

        dashboardStatus.getTrafficBytesDownstream().addAndGet(getRandomLong(10000000, 100000000));
        dashboardStatus.getTrafficBytesUpstream().addAndGet(getRandomLong(10000000, 100000000));

        Status status = new Status();
        status.setCustomerId(customer.getId());
        status.setDetails(dashboardStatus);

        return status;
    }

    private void createServiceMetrics(Equipment equipment) {
        List<ServiceMetric> metricRecordList = new ArrayList<>();

        for (int mCnt = 0; mCnt < numMetricsPerEquipmentToCreateOnStartup; mCnt++) {
            ServiceMetric smr = new ServiceMetric(equipment.getCustomerId(), equipment.getId());
            metricRecordList.add(smr);

            ApNodeMetrics apNodeMetrics = new ApNodeMetrics();
            smr.setDetails(apNodeMetrics);
            ApPerformance apPerformance = new ApPerformance();
            apNodeMetrics.setApPerformance(apPerformance);

            smr.setCreatedTimestamp(System.currentTimeMillis() - mCnt * 60000);
            apNodeMetrics.setChannelUtilization(RadioType.is2dot4GHz, getRandomInt(30, 70));
            apNodeMetrics.setChannelUtilization(RadioType.is5GHzL, getRandomInt(30, 70));
            apNodeMetrics.setChannelUtilization(RadioType.is5GHzU, getRandomInt(30, 70));

            apPerformance.setCpuTemperature(getRandomInt(25, 90));
            apPerformance.setCpuUtilized(new int[] { getRandomInt(5, 98), getRandomInt(5, 98) });

            apPerformance.setEthLinkState(EthernetLinkState.UP1000_FULL_DUPLEX);

            apPerformance.setFreeMemory(getRandomInt(30000000, 70000000));
            apPerformance.setUpTime(getRandomLong(30000000, 70000000));

            apNodeMetrics.setRxBytes(RadioType.is2dot4GHz, getRandomLong(1000000, 10000000));
            apNodeMetrics.setTxBytes(RadioType.is2dot4GHz, getRandomLong(1000000, 10000000));
            apNodeMetrics.setRxBytes(RadioType.is5GHzL, getRandomLong(1000000, 10000000));
            apNodeMetrics.setTxBytes(RadioType.is5GHzL, getRandomLong(1000000, 10000000));
            apNodeMetrics.setRxBytes(RadioType.is5GHzU, getRandomLong(1000000, 10000000));
            apNodeMetrics.setTxBytes(RadioType.is5GHzU, getRandomLong(1000000, 10000000));
            apNodeMetrics.setPeriodLengthSec(60);

            apNodeMetrics.setNoiseFloor(RadioType.is2dot4GHz, Integer.valueOf(-98));
            apNodeMetrics.setNoiseFloor(RadioType.is5GHzL, Integer.valueOf(-98));
            apNodeMetrics.setNoiseFloor(RadioType.is5GHzU, Integer.valueOf(-98));

            List<MacAddress> clientMacAddresses_2g = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                MacAddress macAddress = new MacAddress(new byte[] { 0x74, (byte) 0x9C, getRandomByte(), getRandomByte(),
                        getRandomByte(), getRandomByte() });
                clientMacAddresses_2g.add(macAddress);
            }
            apNodeMetrics.setClientMacAddresses(RadioType.is2dot4GHz, clientMacAddresses_2g);

            List<MacAddress> clientMacAddresses_5gl = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                MacAddress macAddress = new MacAddress(new byte[] { 0x74, (byte) 0x9C, getRandomByte(), getRandomByte(),
                        getRandomByte(), getRandomByte() });
                clientMacAddresses_5gl.add(macAddress);
            }
            apNodeMetrics.setClientMacAddresses(RadioType.is5GHzL, clientMacAddresses_5gl);

            List<MacAddress> clientMacAddresses_5gu = new ArrayList<>();
            for (int i = 0; i < 6; i++) {
                MacAddress macAddress = new MacAddress(new byte[] { 0x74, (byte) 0x9C, getRandomByte(), getRandomByte(),
                        getRandomByte(), getRandomByte() });
                clientMacAddresses_5gu.add(macAddress);
            }
            apNodeMetrics.setClientMacAddresses(RadioType.is5GHzU, clientMacAddresses_5gu);

            apNodeMetrics.setRadioUtilization(RadioType.is2dot4GHz, new ArrayList<>());
            apNodeMetrics.setRadioUtilization(RadioType.is5GHzL, new ArrayList<>());
            apNodeMetrics.setRadioUtilization(RadioType.is5GHzU, new ArrayList<>());

            int numRadioUtilReports = getRandomInt(5, 10);

            for (int i = 0; i < numRadioUtilReports; i++) {
                RadioUtilization radioUtil = new RadioUtilization();
                int surveyDurationMs = getRandomInt(5000, 10000);
                int busyTx = getRandomInt(0, surveyDurationMs / 3);
                int busyRx = getRandomInt(0, surveyDurationMs / 3);
                int busy = getRandomInt(busyTx + busyRx, surveyDurationMs);

                radioUtil.setTimestampSeconds((int) ((smr.getCreatedTimestamp() - surveyDurationMs) / 1000));
                radioUtil.setAssocClientTx(100 * busyTx / surveyDurationMs);
                radioUtil.setAssocClientRx(100 * busyRx / surveyDurationMs);
                radioUtil.setNonWifi(100 * (busy - busyTx - busyRx) / surveyDurationMs);

                switch (i % 3) {
                    case 0:
                        apNodeMetrics.getRadioUtilization(RadioType.is2dot4GHz).add(radioUtil);
                        break;
                    case 1:
                        apNodeMetrics.getRadioUtilization(RadioType.is5GHzL).add(radioUtil);
                        break;
                    case 2:
                        apNodeMetrics.getRadioUtilization(RadioType.is5GHzU).add(radioUtil);
                        break;
                    default:
                        // do nothing
                }
            }

            // now create sample ClientMetrics for this equipment

            for (MacAddress clientMac : clientMacAddresses_2g) {
                ServiceMetric smrClient = new ServiceMetric(equipment.getCustomerId(), equipment.getId());
                metricRecordList.add(smrClient);

                ClientMetrics clientMetrics = new ClientMetrics();
                smrClient.setDetails(clientMetrics);
                smrClient.setCreatedTimestamp(smr.getCreatedTimestamp());
                smrClient.setClientMac(clientMac.getAddressAsLong());

                clientMetrics.setPeriodLengthSec(60);
                clientMetrics.setRadioType(RadioType.is2dot4GHz);

                clientMetrics.setNumRxPackets(getRandomLong(30000, 70000));
                clientMetrics.setNumTxPackets(getRandomLong(30000, 70000));
                clientMetrics.setNumRxBytes(getRandomLong(3000000, 7000000));
                clientMetrics.setNumTxBytes(getRandomLong(3000000, 7000000));

                clientMetrics.setSessionId(getRandomLong(3000000, 7000000));

                clientMetrics.setTxRetries(getRandomInt(30, 70));
                clientMetrics.setRxDuplicatePackets(getRandomInt(30, 70));
                clientMetrics.setSnr(getRandomInt(-70, -30));
                clientMetrics.setRssi(getRandomInt(20, 70));

            }

            for (MacAddress clientMac : clientMacAddresses_5gl) {
                ServiceMetric smrClient = new ServiceMetric(equipment.getCustomerId(), equipment.getId());
                metricRecordList.add(smrClient);

                ClientMetrics clientMetrics = new ClientMetrics();
                smrClient.setDetails(clientMetrics);
                smrClient.setCreatedTimestamp(smr.getCreatedTimestamp());
                smrClient.setClientMac(clientMac.getAddressAsLong());

                clientMetrics.setPeriodLengthSec(60);
                clientMetrics.setRadioType(RadioType.is5GHzL);

                clientMetrics.setNumRxPackets(getRandomLong(30000, 70000));
                clientMetrics.setNumTxPackets(getRandomLong(30000, 70000));
                clientMetrics.setNumRxBytes(getRandomLong(3000000, 7000000));
                clientMetrics.setNumTxBytes(getRandomLong(3000000, 7000000));

                clientMetrics.setSessionId(getRandomLong(3000000, 7000000));

                clientMetrics.setTxRetries(getRandomInt(30, 70));
                clientMetrics.setRxDuplicatePackets(getRandomInt(30, 70));
                clientMetrics.setSnr(getRandomInt(-70, -30));
                clientMetrics.setRssi(getRandomInt(20, 70));

            }

            for (MacAddress clientMac : clientMacAddresses_5gu) {
                ServiceMetric smrClient = new ServiceMetric(equipment.getCustomerId(), equipment.getId());
                metricRecordList.add(smrClient);

                ClientMetrics clientMetrics = new ClientMetrics();
                smrClient.setDetails(clientMetrics);
                smrClient.setCreatedTimestamp(smr.getCreatedTimestamp());
                smrClient.setClientMac(clientMac.getAddressAsLong());

                clientMetrics.setPeriodLengthSec(60);
                clientMetrics.setRadioType(RadioType.is5GHzU);

                clientMetrics.setNumRxPackets(getRandomLong(30000, 70000));
                clientMetrics.setNumTxPackets(getRandomLong(30000, 70000));
                clientMetrics.setNumRxBytes(getRandomLong(3000000, 7000000));
                clientMetrics.setNumTxBytes(getRandomLong(3000000, 7000000));

                clientMetrics.setSessionId(getRandomLong(3000000, 7000000));

                clientMetrics.setTxRetries(getRandomInt(30, 70));
                clientMetrics.setRxDuplicatePackets(getRandomInt(30, 70));
                clientMetrics.setSnr(getRandomInt(-70, -30));
                clientMetrics.setRssi(getRandomInt(20, 70));

            }
        }

        serviceMetricInterface.create(metricRecordList);
    }

    private void createClientSessions(Equipment equipment, SsidConfiguration ssidConfig) {
        int numClientsPerAp = numClientsPerApToCreateOnStartup;
        Client client;
        ClientSession clientSession;
        MacAddress macAddress;

        for (int i = 0; i < numClientsPerAp; i++) {
            client = new Client();
            client.setCustomerId(equipment.getCustomerId());
            macAddress = new MacAddress(new byte[] { 0x74, (byte) 0x9C, getRandomByte(), getRandomByte(),
                    getRandomByte(), getRandomByte() });

            client.setMacAddress(macAddress);
            ClientInfoDetails details = new ClientInfoDetails();
            details.setAlias("alias " + macAddress.getAddressAsLong());
            details.setApFingerprint("fp " + macAddress.getAddressAsString());
            details.setHostName("hostName-" + macAddress.getAddressAsLong());
            details.setUserName("user-" + macAddress.getAddressAsLong());
            client.setDetails(details);
            this.clientServiceInterface.create(client);

            RadioType radioType;

            int idx = (int) (macAddress.getAddressAsLong() % 3);
            switch (idx) {
                case 0:
                    radioType = RadioType.is2dot4GHz;
                    break;
                case 1:
                    radioType = RadioType.is5GHzL;
                    break;
                case 2:
                    radioType = RadioType.is5GHzU;
                    break;
                default:
                    radioType = RadioType.is5GHzL;
            }

            clientSession = new ClientSession();
            clientSession.setMacAddress(macAddress);
            clientSession.setCustomerId(equipment.getCustomerId());
            clientSession.setEquipmentId(equipment.getId());
            clientSession.setLocationId(equipment.getLocationId());

            ClientSessionDetails sessionDetails = new ClientSessionDetails();
            sessionDetails.setApFingerprint(details.getApFingerprint());
            sessionDetails.setHostname(details.getHostName());
            try {
                sessionDetails.setIpAddress(
                        Inet4Address.getByAddress(new byte[] { (byte) 192, (byte) 168, 10, getRandomByte() }));
            } catch (UnknownHostException e) {
                // nothing to do here
            }
            sessionDetails.setRadioType(radioType);
            sessionDetails.setSecurityType(SecurityType.PSK);
            sessionDetails.setSsid(ssidConfig.getSsid());
            sessionDetails.setSessionId(System.currentTimeMillis());
            sessionDetails.setAssocTimestamp(System.currentTimeMillis() - getRandomLong(10000, 1000000));

            ClientDhcpDetails dhcpDetails = new ClientDhcpDetails(System.currentTimeMillis());
            dhcpDetails
                    .setLeaseStartTimestamp(System.currentTimeMillis() - getRandomLong(0, TimeUnit.HOURS.toMillis(4)));
            dhcpDetails.setLeaseTimeInSeconds((int) TimeUnit.HOURS.toSeconds(4));
            try {
                dhcpDetails.setDhcpServerIp(InetAddress.getByName("192.168.0.1"));
                dhcpDetails.setGatewayIp(dhcpDetails.getDhcpServerIp());
                dhcpDetails.setPrimaryDns(InetAddress.getByName("8.8.8.8"));
                dhcpDetails.setSecondaryDns(dhcpDetails.getDhcpServerIp());
                dhcpDetails.setSubnetMask(InetAddress.getByName("192.168.0.255"));
            } catch (UnknownHostException e) {
                // nothing to do
            }

            sessionDetails.setDhcpDetails(dhcpDetails);

            ClientSessionMetricDetails metricDetails = new ClientSessionMetricDetails();
            metricDetails.setRssi(getRandomInt(-60, -40));
            metricDetails.setRxBytes(getRandomLong(10000, 10000000));
            metricDetails.setTxBytes(getRandomLong(10000, 10000000));
            metricDetails.setRxMbps(getRandomFloat(50, 100));
            metricDetails.setTxMbps(getRandomFloat(50, 100));
            metricDetails.setSnr(getRandomInt(-90, -50));

            sessionDetails.setMetricDetails(metricDetails);

            clientSession.setDetails(sessionDetails);

            this.clientServiceInterface.updateSession(clientSession);

        }
    }

    private void createStatusForEquipment(Equipment equipment) {
        List<Status> statusList = new ArrayList<>();
        // StatusDataType.EQUIPMENT_ADMIN
        // StatusDataType.OS_PERFORMANCE
        // StatusDataType.PROTOCOL
        // StatusDataType.RADIO_UTILIZATION

        Status status = new Status();
        status.setCustomerId(equipment.getCustomerId());
        status.setEquipmentId(equipment.getId());
        EquipmentAdminStatusData eqAdminStatusData = new EquipmentAdminStatusData();
        eqAdminStatusData.setStatusCode(StatusCode.normal);
        status.setDetails(eqAdminStatusData);
        statusList.add(status);

        status = new Status();
        status.setCustomerId(equipment.getCustomerId());
        status.setEquipmentId(equipment.getId());
        OperatingSystemPerformance eqOsPerformance = new OperatingSystemPerformance();
        eqOsPerformance.setUptimeInSeconds(getRandomLong(10, 10000));
        eqOsPerformance.setAvgCpuTemperature(getRandomFloat(25, 80));
        eqOsPerformance.setAvgCpuUtilization(getRandomFloat(5, 100));
        eqOsPerformance.setAvgCpuPerCore(new float[] { getRandomFloat(5, 100), getRandomFloat(5, 100) });
        eqOsPerformance.setAvgFreeMemoryKb(getRandomInt(20000, 70000));
        eqOsPerformance.setTotalAvailableMemoryKb(512000);
        status.setDetails(eqOsPerformance);
        statusList.add(status);

        status = new Status();
        status.setCustomerId(equipment.getCustomerId());
        status.setEquipmentId(equipment.getId());
        EquipmentProtocolStatusData eqProtocolStatus = new EquipmentProtocolStatusData();
        eqProtocolStatus.setBaseMacAddress(equipment.getBaseMacAddress());
        eqProtocolStatus.setPoweredOn(true);
        eqProtocolStatus.setProtocolState(EquipmentProtocolState.ready);
        try {
            eqProtocolStatus.setReportedIpV4Addr(
                    Inet4Address.getByAddress(new byte[] { (byte) 192, (byte) 168, 1, getRandomByte() }));
            eqProtocolStatus.setReportedIpV6Addr(Inet6Address
                    .getByAddress(new byte[] { (byte) 0xfe, (byte) 0x80, 0, 0, 0, 0, 0, 0, 2, 24, (byte) 0xb4,
                            (byte) 0xff, (byte) 0xfe, getRandomByte(), getRandomByte(), getRandomByte() }));
        } catch (Exception e) {
            // nothing to do here
        }
        eqProtocolStatus.setReportedMacAddr(eqProtocolStatus.getBaseMacAddress());
        eqProtocolStatus.setSerialNumber(equipment.getSerial());
        status.setDetails(eqProtocolStatus);
        statusList.add(status);

        // now prepare radio utilization status
        status = new Status();
        status.setCustomerId(equipment.getCustomerId());
        status.setEquipmentId(equipment.getId());
        RadioUtilizationReport eqRadioUtilReport = new RadioUtilizationReport();

        Map<RadioType, Integer> avgNoiseFloor = new EnumMap<>(RadioType.class);
        avgNoiseFloor.put(RadioType.is2dot4GHz, getRandomInt(-95, -40));
        avgNoiseFloor.put(RadioType.is5GHzL, getRandomInt(-95, -40));
        avgNoiseFloor.put(RadioType.is5GHzU, getRandomInt(-95, -40));
        eqRadioUtilReport.setAvgNoiseFloor(avgNoiseFloor);

        Map<RadioType, EquipmentCapacityDetails> capacityDetails = new EnumMap<>(RadioType.class);
        EquipmentCapacityDetails cap = new EquipmentCapacityDetails();
        cap.setTotalCapacity(getRandomInt(70, 100));
        cap.setUnavailableCapacity(getRandomInt(0, 30));
        cap.setAvailableCapacity(cap.getTotalCapacity() - cap.getUnavailableCapacity());
        cap.setUsedCapacity(getRandomInt(5, cap.getAvailableCapacity()));
        cap.setUnusedCapacity(cap.getAvailableCapacity() - cap.getUsedCapacity());
        capacityDetails.put(RadioType.is2dot4GHz, cap);

        cap = new EquipmentCapacityDetails();
        cap.setTotalCapacity(getRandomInt(70, 100));
        cap.setUnavailableCapacity(getRandomInt(0, 30));
        cap.setAvailableCapacity(cap.getTotalCapacity() - cap.getUnavailableCapacity());
        cap.setUsedCapacity(getRandomInt(5, cap.getAvailableCapacity()));
        cap.setUnusedCapacity(cap.getAvailableCapacity() - cap.getUsedCapacity());
        capacityDetails.put(RadioType.is5GHzL, cap);

        cap = new EquipmentCapacityDetails();
        cap.setTotalCapacity(getRandomInt(70, 100));
        cap.setUnavailableCapacity(getRandomInt(0, 30));
        cap.setAvailableCapacity(cap.getTotalCapacity() - cap.getUnavailableCapacity());
        cap.setUsedCapacity(getRandomInt(5, cap.getAvailableCapacity()));
        cap.setUnusedCapacity(cap.getAvailableCapacity() - cap.getUsedCapacity());
        capacityDetails.put(RadioType.is5GHzU, cap);

        eqRadioUtilReport.setCapacityDetails(capacityDetails);

        Map<RadioType, EquipmentPerRadioUtilizationDetails> radioUtilization = new EnumMap<>(RadioType.class);
        EquipmentPerRadioUtilizationDetails ut = new EquipmentPerRadioUtilizationDetails();
        ut.setWifiFromOtherBss(new MinMaxAvgValueInt(getRandomInt(0, 5), getRandomInt(5, 10), getRandomInt(5, 10)));
        radioUtilization.put(RadioType.is2dot4GHz, ut);

        ut = new EquipmentPerRadioUtilizationDetails();
        ut.setWifiFromOtherBss(new MinMaxAvgValueInt(getRandomInt(0, 5), getRandomInt(5, 10), getRandomInt(5, 10)));
        radioUtilization.put(RadioType.is5GHzL, ut);

        ut = new EquipmentPerRadioUtilizationDetails();
        ut.setWifiFromOtherBss(new MinMaxAvgValueInt(getRandomInt(0, 5), getRandomInt(5, 10), getRandomInt(5, 10)));
        radioUtilization.put(RadioType.is5GHzU, ut);

        eqRadioUtilReport.setRadioUtilization(radioUtilization);

        status.setDetails(eqRadioUtilReport);
        statusList.add(status);

        statusServiceInterface.update(statusList);
    }

    private void createAlarmsForEquipment(Equipment equipment) {

        if (equipment.getId() % 7 != 0) {
            // only some APs will have an alarm
            return;
        }

        Alarm alarm = new Alarm();
        alarm.setCustomerId(equipment.getCustomerId());
        alarm.setEquipmentId(equipment.getId());
        alarm.setAlarmCode(AlarmCode.MemoryUtilization);
        alarm.setCreatedTimestamp(System.currentTimeMillis());

        AlarmDetails details = new AlarmDetails();
        details.setMessage("Available memory is too low");
        alarm.setDetails(details);

        alarmServiceInterface.create(alarm);
    }

    private static byte getRandomByte() {
        byte ret = (byte) (225 * Math.random());
        return ret;
    }

    private static long getRandomLong(long min, long max) {
        long ret = min + (long) ((max - min) * Math.random());
        return ret;
    }

    private static int getRandomInt(int min, int max) {
        int ret = min + (int) ((max - min) * Math.random());
        return ret;
    }

    private static float getRandomFloat(float min, float max) {
        float ret = min + (float) ((max - min) * Math.random());
        return ret;
    }

}
