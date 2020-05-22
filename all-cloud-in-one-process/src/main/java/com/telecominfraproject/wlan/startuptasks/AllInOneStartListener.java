package com.telecominfraproject.wlan.startuptasks;

import java.net.Inet4Address;
import java.net.Inet6Address;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import com.telecominfraproject.wlan.client.ClientServiceInterface;
import com.telecominfraproject.wlan.client.info.models.ClientInfoDetails;
import com.telecominfraproject.wlan.client.models.Client;
import com.telecominfraproject.wlan.client.models.ClientDetails;
import com.telecominfraproject.wlan.client.session.models.ClientSession;
import com.telecominfraproject.wlan.client.session.models.ClientSessionDetails;
import com.telecominfraproject.wlan.client.session.models.ClientSessionMetricDetails;
import com.telecominfraproject.wlan.core.model.entity.CountryCode;
import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.core.model.equipment.RadioType;
import com.telecominfraproject.wlan.core.model.equipment.SecurityType;
import com.telecominfraproject.wlan.customer.models.Customer;
import com.telecominfraproject.wlan.customer.service.CustomerServiceInterface;
import com.telecominfraproject.wlan.equipment.EquipmentServiceInterface;
import com.telecominfraproject.wlan.equipment.models.ApElementConfiguration;
import com.telecominfraproject.wlan.equipment.models.Equipment;
import com.telecominfraproject.wlan.location.models.Location;
import com.telecominfraproject.wlan.location.models.LocationDetails;
import com.telecominfraproject.wlan.location.models.LocationType;
import com.telecominfraproject.wlan.location.service.LocationServiceInterface;
import com.telecominfraproject.wlan.profile.ProfileServiceInterface;
import com.telecominfraproject.wlan.profile.models.Profile;
import com.telecominfraproject.wlan.profile.models.ProfileContainer;
import com.telecominfraproject.wlan.profile.models.ProfileType;
import com.telecominfraproject.wlan.profile.network.models.ApNetworkConfiguration;
import com.telecominfraproject.wlan.profile.ssid.models.SsidConfiguration;
import com.telecominfraproject.wlan.status.StatusServiceInterface;
import com.telecominfraproject.wlan.status.equipment.models.EquipmentAdminStatusData;
import com.telecominfraproject.wlan.status.equipment.models.EquipmentProtocolState;
import com.telecominfraproject.wlan.status.equipment.models.EquipmentProtocolStatusData;
import com.telecominfraproject.wlan.status.equipment.report.models.EquipmentCapacityDetails;
import com.telecominfraproject.wlan.status.equipment.report.models.EquipmentPerRadioUtilizationDetails;
import com.telecominfraproject.wlan.status.equipment.report.models.OperatingSystemPerformance;
import com.telecominfraproject.wlan.status.equipment.report.models.RadioUtilizationReport;
import com.telecominfraproject.wlan.status.models.Status;
import com.telecominfraproject.wlan.status.models.StatusCode;
import com.telecominfraproject.wlan.status.network.models.MinMaxAvgValueInt;

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

	@Override
	public void run(ApplicationArguments args) {
		LOG.info("Creating initial objects");

		Customer customer = new Customer();
		customer.setEmail("test@example.com");
		customer.setName("Test Customer");

		customer = customerServiceInterface.create(customer);

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

		Profile profileSsid = new Profile();
		profileSsid.setCustomerId(customer.getId());
		profileSsid.setName("Connectus-cloud");
		SsidConfiguration ssidConfig = SsidConfiguration.createWithDefaults();
		Set<RadioType> appliedRadios = new HashSet<RadioType>();
		appliedRadios.add(RadioType.is2dot4GHz);
		appliedRadios.add(RadioType.is5GHzL);
		appliedRadios.add(RadioType.is5GHzU);
		ssidConfig.setAppliedRadios(appliedRadios);
		profileSsid.setDetails(ssidConfig);
		profileSsid = profileServiceInterface.create(profileSsid);

		Profile profileAp = new Profile();
		profileAp.setCustomerId(customer.getId());
		profileAp.setName("ApProfile");
		profileAp.setDetails(ApNetworkConfiguration.createWithDefaults());
		profileAp.getChildProfileIds().add(profileSsid.getId());
		profileAp = profileServiceInterface.create(profileAp);

		List<Equipment> equipmentList = new ArrayList<>();
		int numEquipment = 50;
		for (int i = 1; i <= numEquipment; i++) {
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
				equipment.setLocationId(location_2.getId());
			}

			equipment.setProfileId(profileAp.getId());
			equipment.setInventoryId("ap-" + i);
			equipment.setName("AP " + i);
			equipment.setSerial("serial-ap-" + i);
			equipment.setDetails(ApElementConfiguration.createWithDefaults());

			equipment = equipmentServiceInterface.create(equipment);
			equipmentList.add(equipment);

			createStatusForEquipment(equipment);
			
			createClientSessions(equipment, ssidConfig);

		}

		LOG.info("Done creating initial objects");

		// print out SSID configurations used by ap-1
		ProfileContainer profileContainer = new ProfileContainer(
				profileServiceInterface.getProfileWithChildren(equipmentList.get(0).getProfileId()));

		List<Profile> ssidProfiles = profileContainer.getChildrenOfType(equipmentList.get(0).getProfileId(),
				ProfileType.ssid);
		List<SsidConfiguration> ssidConfigs = new ArrayList<>();
		ssidProfiles.forEach(p -> ssidConfigs.add((SsidConfiguration) p.getDetails()));
		LOG.info("SSID configs: {}", ssidConfigs);

	}

	private void createClientSessions(Equipment equipment, SsidConfiguration ssidConfig) {
		int numClientsPerAp = 20;
		Client client;
		ClientSession clientSession;
		MacAddress macAddress;
		
		for(int i=0; i< numClientsPerAp; i++) {
			client = new Client();
			client.setCustomerId(equipment.getCustomerId());
			macAddress = new MacAddress(
					new byte[] { 0x74, (byte) 0x9C, getRandomByte(), getRandomByte(), getRandomByte(), getRandomByte() });
			
			client.setMacAddress(macAddress);
			ClientInfoDetails details = new ClientInfoDetails();
			details.setAlias("alias "+ macAddress.getAddressAsLong());
			details.setApFingerprint("fp "+ macAddress.getAddressAsString());
			details.setHostName("hostName-"+ macAddress.getAddressAsLong());
			details.setUserName("user-"+ macAddress.getAddressAsLong());
			client.setDetails(details );
			this.clientServiceInterface.create(client);
			
			RadioType radioType	;
			
			int idx = (int) (macAddress.getAddressAsLong() %  3) ;
			switch(idx) {
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
			
			ClientSessionDetails sessionDetails = new ClientSessionDetails();
			sessionDetails.setApFingerprint(details.getApFingerprint());
			sessionDetails.setHostname(details.getHostName());
			try {
				sessionDetails.setIpAddress(Inet4Address.getByAddress(new byte[] { (byte) 192, (byte) 168, 10, getRandomByte() }));
			} catch (UnknownHostException e) {
				//nothing to do here
			}			
			sessionDetails.setRadioType(radioType);
			sessionDetails.setSecurityType(SecurityType.PSK);
			sessionDetails.setSsid(ssidConfig.getSsid());
			sessionDetails.setSessionId(System.currentTimeMillis());
			sessionDetails.setAssocTimestamp(System.currentTimeMillis()- getRandomLong(10000,1000000));
			
			ClientSessionMetricDetails metricDetails = new ClientSessionMetricDetails();
			metricDetails.setRssi(getRandomInt(-60, -40));
			metricDetails.setRxBytes(getRandomLong(10000, 10000000));
			metricDetails.setTxBytes(getRandomLong(10000, 10000000));
			metricDetails.setRxMbps(getRandomFloat(50,100));
			metricDetails.setTxMbps(getRandomFloat(50,100));
			metricDetails.setSnr(getRandomInt(-90, -50));
			
			sessionDetails.setMetricDetails(metricDetails );
			
			clientSession.setDetails(sessionDetails );
			
			this.clientServiceInterface.updateSession(clientSession);
			
		}
	}

	private void createStatusForEquipment(Equipment equipment) {
		List<Status> statusList = new ArrayList<>();
//		StatusDataType.EQUIPMENT_ADMIN	
//		StatusDataType.OS_PERFORMANCE
//		StatusDataType.PROTOCOL
//		StatusDataType.RADIO_UTILIZATION

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
		eqOsPerformance.setAvgFreeMemory(getRandomInt(10000000, 50000000));
		status.setDetails(eqOsPerformance);
		statusList.add(status);

		status = new Status();
		status.setCustomerId(equipment.getCustomerId());
		status.setEquipmentId(equipment.getId());
		EquipmentProtocolStatusData eqProtocolStatus = new EquipmentProtocolStatusData();
		eqProtocolStatus.setBaseMacAddress(new MacAddress(
				new byte[] { 0x74, (byte) 0x9C, (byte) 0xE3, getRandomByte(), getRandomByte(), getRandomByte() }));
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
