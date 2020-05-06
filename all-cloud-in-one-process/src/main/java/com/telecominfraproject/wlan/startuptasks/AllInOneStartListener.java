package com.telecominfraproject.wlan.startuptasks;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import com.telecominfraproject.wlan.core.model.entity.CountryCode;
import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
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
		profileSsid.setProfileType(ProfileType.ssid);
		profileSsid.setName("Connectus-cloud");
		profileSsid.setDetails(SsidConfiguration.createWithDefaults());
		profileSsid = profileServiceInterface.create(profileSsid);

		Profile profileAp = new Profile();
		profileAp.setCustomerId(customer.getId());
		profileAp.setName("ApProfile");
		profileAp.setProfileType(ProfileType.equipment_ap);
		profileAp.setDetails(ApNetworkConfiguration.createWithDefaults());
		profileAp.getChildProfileIds().add(profileSsid.getId());
		profileAp = profileServiceInterface.create(profileAp);

		Equipment equipment_1 = new Equipment();
		equipment_1.setCustomerId(customer.getId());
		equipment_1.setEquipmentType(EquipmentType.AP);
		equipment_1.setLocationId(location_2.getId());
		equipment_1.setProfileId(profileAp.getId());
		equipment_1.setInventoryId("ap-1");
		equipment_1.setName("First AP");
		equipment_1.setSerial("serial-ap-1");
		equipment_1.setDetails(ApElementConfiguration.createWithDefaults());

		equipment_1 = equipmentServiceInterface.create(equipment_1);

		Equipment equipment_2 = new Equipment();
		equipment_2.setCustomerId(customer.getId());
		equipment_2.setEquipmentType(EquipmentType.AP);
		equipment_2.setLocationId(location_2.getId());
		equipment_2.setProfileId(profileAp.getId());
		equipment_2.setInventoryId("ap-2");
		equipment_2.setName("Second AP");
		equipment_2.setSerial("serial-ap-2");
		equipment_2.setDetails(ApElementConfiguration.createWithDefaults());

		equipment_2 = equipmentServiceInterface.create(equipment_2);


		LOG.info("Done creating initial objects");
		
		//print out SSID configurations used by ap-1
		ProfileContainer profileContainer = new ProfileContainer(profileServiceInterface.getProfileWithChildren(equipment_1.getProfileId()));
		
		List<Profile> ssidProfiles = profileContainer.getChildrenOfType(equipment_1.getProfileId(), ProfileType.ssid);
		List<SsidConfiguration> ssidConfigs = new ArrayList<>();
		ssidProfiles.forEach(p -> ssidConfigs.add((SsidConfiguration)p.getDetails()));
		LOG.info("SSID configs: {}", ssidConfigs);
		
	}

}
