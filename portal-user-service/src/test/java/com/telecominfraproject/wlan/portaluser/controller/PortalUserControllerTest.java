package com.telecominfraproject.wlan.portaluser.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

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

import com.telecominfraproject.wlan.portaluser.datastore.inmemory.PortalUserDatastoreInMemory;
import com.telecominfraproject.wlan.portaluser.models.PortalUser;

/**
 * @author dtoptygin
 * 
 * Integration test for PortalUserController
 *
 */
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = {
        "integration_test",
        }) //NOTE: these profiles will be ADDED to the list of active profiles
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = PortalUserControllerTest.class)
@Import(value = {
        PortalUserController.class,
        CloudEventDispatcherEmpty.class,
        PortalUserDatastoreInMemory.class,
        PortalUserControllerTest.Config.class, 
        })
public class PortalUserControllerTest {
    
    @Autowired private PortalUserController portalUserController;

    
    @Configuration
    //@PropertySource({ "classpath:persistence-${envTarget:dev}.properties" })
    static class Config {
        // Put all required @Bean -s in here
        
    }
    
    @Test
    public void testPortalUserCRUD() throws Exception {
        
        //Create new PortalUser - success
        PortalUser portalUser = new PortalUser();
        portalUser.setSampleStr("test");

        PortalUser ret = portalUserController.create(portalUser);
        assertNotNull(ret);

        ret = portalUserController.get(ret.getId());
        assertEqualPortalUsers(portalUser, ret);

        ret = portalUserController.getOrNull(ret.getId());
        assertEqualPortalUsers(portalUser, ret);
        
        assertNull(portalUserController.getOrNull(-1));

        //Delete - success
        portalUserController.delete(ret.getId());
        
    }
        
    private void assertEqualPortalUsers(
            PortalUser expected,
            PortalUser actual) {
        
        assertEquals(expected.getSampleStr(), actual.getSampleStr());
        //TODO: add more fields to check here
    }

}
