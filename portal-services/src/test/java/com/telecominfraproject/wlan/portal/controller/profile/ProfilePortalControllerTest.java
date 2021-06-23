package com.telecominfraproject.wlan.portal.controller.profile;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.telecominfraproject.wlan.cloudeventdispatcher.CloudEventDispatcherEmpty;
import com.telecominfraproject.wlan.core.model.equipment.EquipmentType;
import com.telecominfraproject.wlan.core.model.pair.PairLongLong;
import com.telecominfraproject.wlan.equipment.EquipmentServiceLocal;
import com.telecominfraproject.wlan.equipment.controller.EquipmentController;
import com.telecominfraproject.wlan.equipment.datastore.EquipmentDatastore;
import com.telecominfraproject.wlan.equipment.datastore.inmemory.EquipmentDatastoreInMemory;
import com.telecominfraproject.wlan.equipment.models.Equipment;
import com.telecominfraproject.wlan.portal.controller.profile.ProfilePortalController.ListOfPairLongLong;
import com.telecominfraproject.wlan.profile.ProfileServiceLocal;
import com.telecominfraproject.wlan.profile.controller.ProfileController;
import com.telecominfraproject.wlan.profile.datastore.ProfileDatastore;
import com.telecominfraproject.wlan.profile.datastore.inmemory.ProfileDatastoreInMemory;
import com.telecominfraproject.wlan.profile.models.Profile;
import com.telecominfraproject.wlan.profile.models.ProfileByCustomerRequestFactory;
import com.telecominfraproject.wlan.profile.models.ProfileType;


@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = {
        "integration_test",
        }) //NOTE: these profiles will be ADDED to the list of active profiles
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = ProfilePortalControllerTest.class)
@Import(value = {
        EquipmentServiceLocal.class,
        EquipmentController.class,
        EquipmentDatastoreInMemory.class,
        ProfileServiceLocal.class,
        ProfileController.class,
        ProfileDatastoreInMemory.class,
        ProfileByCustomerRequestFactory.class,
        CloudEventDispatcherEmpty.class,
        ProfilePortalController.class
        })
public class ProfilePortalControllerTest {
    private static AtomicLong profileIncrementer = new AtomicLong(1);
    private static AtomicLong equipmentIncrementer = new AtomicLong(1);
    private static Set<Long> profileIds = new HashSet<Long>();
    
    @Autowired private ProfilePortalController profilePortalController;
    @Autowired private EquipmentDatastore equipmentDatastore;
    @Autowired private ProfileDatastore profileDatastore;
    
    @BeforeEach
    public void setup()
    {
        profileIds = new HashSet<Long>();
    }
    
    @Test
    public void getCountsOfEquipmentThatUseProfilesTest()
    {
        
        Profile parentProfile_1 = createProfile(); // 3 equipment
        Profile parentProfile_2 = createProfile(); // 2 equipment
        Profile parentProfile_3 = createProfile(); // 1 equipment
        
        Profile ssidProfile_1 = createSsidProfile(); // linked to parent ids: 1 = 3 expected count
        Profile ssidProfile_2 = createSsidProfile(); // linked to parent ids: 1,2 = 5 expected count
        Profile ssidProfile_3 = createSsidProfile(); // linked to parent ids: 3 = 1 expected count
        
        Profile captivePortalProfile_1 = createCaptivePortalProfile(); // linked to 4 -> 1 = 3 expected count
        Profile captivePortalProfile_2 = createCaptivePortalProfile(); // linked to 5 and 6 -> 5 + 1 = 6 expected count
        
        Profile radiusProfile_1 = createRadiusProfile(); // 7, 8 -> 4, 5, 6 -> 1 , 2 , 3 -> 6 expected
        Profile radiusProfile_2 = createRadiusProfile(); // 0 expected
        
        // link ssid to AP profiles
        linkChildToParent(ssidProfile_1.getId(), parentProfile_1.getId());
        linkChildToParent(ssidProfile_2.getId(), parentProfile_1.getId());
        linkChildToParent(ssidProfile_2.getId(), parentProfile_2.getId());
        linkChildToParent(ssidProfile_3.getId(), parentProfile_3.getId());
        
        //link captive portal profiles to ssid profiles
        linkChildToParent(captivePortalProfile_1.getId(), ssidProfile_1.getId());
        linkChildToParent(captivePortalProfile_2.getId(), ssidProfile_2.getId());
        linkChildToParent(captivePortalProfile_2.getId(), ssidProfile_3.getId());
        
        //link radius profiles to captive portal profiles
        linkChildToParent(radiusProfile_1.getId(), captivePortalProfile_1.getId());
        linkChildToParent(radiusProfile_1.getId(), captivePortalProfile_2.getId());
        
        createEquipment(parentProfile_1.getId());
        createEquipment(parentProfile_1.getId());
        createEquipment(parentProfile_1.getId());
        createEquipment(parentProfile_2.getId());
        createEquipment(parentProfile_2.getId());
        createEquipment(parentProfile_3.getId());
        
        ListOfPairLongLong ret = profilePortalController.getCountsOfEquipmentThatUseProfiles(profileIds);
        
        List<PairLongLong> expectedReturn = new ArrayList<PairLongLong>() {{ 
            add(new PairLongLong(parentProfile_1.getId(), 3));
            add(new PairLongLong(parentProfile_2.getId(), 2));
            add(new PairLongLong(parentProfile_3.getId(), 1));
            add(new PairLongLong(ssidProfile_1.getId(), 3));
            add(new PairLongLong(ssidProfile_2.getId(), 5));
            add(new PairLongLong(ssidProfile_3.getId(), 1));
            add(new PairLongLong(captivePortalProfile_1.getId(), 3));
            add(new PairLongLong(captivePortalProfile_2.getId(), 6));
            add(new PairLongLong(radiusProfile_1.getId(), 6));
            add(new PairLongLong(radiusProfile_2.getId(), 0));
        }};
        
        expectedReturn.forEach(pair -> assertTrue(ret.contains(pair)));
    }
    
    private Profile createProfile()
    {
        Profile profile = new Profile();
        profile.setName("test" + profileIncrementer.getAndIncrement());
        profile.setProfileType(ProfileType.equipment_ap);
        profile.setCustomerId(2);

        Profile created = profileDatastore.create(profile);
        profileIds.add(created.getId());
        
        return created;
    }
    
    private Profile createSsidProfile()
    {
        Profile profile = new Profile();
        profile.setName("test" + profileIncrementer.getAndIncrement());
        profile.setProfileType(ProfileType.ssid);
        profile.setCustomerId(2);
        
        Profile created = profileDatastore.create(profile);
        profileIds.add(created.getId());
        
        return created;
    }
    
    private Profile createCaptivePortalProfile()
    {
        Profile profile = new Profile();
        profile.setName("test" + profileIncrementer.getAndIncrement());
        profile.setProfileType(ProfileType.captive_portal);
        profile.setCustomerId(2);
        
        Profile created = profileDatastore.create(profile);
        profileIds.add(created.getId());
        
        return created;
    }
    
    private Profile createRadiusProfile()
    {
        Profile profile = new Profile();
        profile.setName("test" + profileIncrementer.getAndIncrement());
        profile.setProfileType(ProfileType.radius);
        profile.setCustomerId(2);
        
        Profile created = profileDatastore.create(profile);
        profileIds.add(created.getId());
        
        return created;
    }
    
    private void linkChildToParent(long childId, long parentId)
    {
        Profile parentProfile = profileDatastore.get(parentId);
        parentProfile.getChildProfileIds().add(childId);
        
        profileDatastore.update(parentProfile);
    }
    
    private Equipment createEquipment(long profileId)
    {
        Equipment equipment = new Equipment();
        equipment.setCustomerId(2);
        equipment.setEquipmentType(EquipmentType.AP);
        equipment.setName("test" + equipmentIncrementer.getAndIncrement());
        equipment.setProfileId(profileId);
        return equipmentDatastore.create(equipment);
    }
}
