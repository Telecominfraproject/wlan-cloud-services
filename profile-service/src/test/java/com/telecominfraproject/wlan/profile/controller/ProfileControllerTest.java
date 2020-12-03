package com.telecominfraproject.wlan.profile.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.telecominfraproject.wlan.cloudeventdispatcher.CloudEventDispatcherEmpty;

import com.telecominfraproject.wlan.profile.datastore.inmemory.ProfileDatastoreInMemory;
import com.telecominfraproject.wlan.profile.models.Profile;
import com.telecominfraproject.wlan.profile.models.ProfileByCustomerRequestFactory;
import com.telecominfraproject.wlan.profile.models.ProfileType;
import com.telecominfraproject.wlan.server.exceptions.GenericErrorException;

/**
 * @author dtoptygin
 * 
 * Integration test for ProfileController
 *
 */
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = {
        "integration_test",
        }) //NOTE: these profiles will be ADDED to the list of active profiles
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = ProfileControllerTest.class)
@Import(value = {
        ProfileController.class,
        CloudEventDispatcherEmpty.class,
        ProfileDatastoreInMemory.class,
        ProfileControllerTest.Config.class,
        ProfileByCustomerRequestFactory.class, 
        })
public class ProfileControllerTest {
    
    @Autowired private ProfileController profileController;

    
    @Configuration
    //@PropertySource({ "classpath:persistence-${envTarget:dev}.properties" })
    static class Config {
        // Put all required @Bean -s in here
        
    }
    
    @Test
    public void testProfileCRUD() throws Exception {
        
        //Create new Profile - success
        Profile profile = new Profile();
        profile.setName("test");
        profile.setProfileType(ProfileType.equipment_ap);

        Profile ret = profileController.create(profile);
        assertNotNull(ret);

        ret = profileController.get(ret.getId());
        assertEqualProfiles(profile, ret);

        ret = profileController.getOrNull(ret.getId());
        assertEqualProfiles(profile, ret);
        
        assertNull(profileController.getOrNull(-1));

        //Delete - success
        profileController.delete(ret.getId());
        
    }
        
    private void assertEqualProfiles(
            Profile expected,
            Profile actual) {
        
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getProfileType(), actual.getProfileType());
        //TODO: add more fields to check here
    }

}
