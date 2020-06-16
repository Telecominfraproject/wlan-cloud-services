package com.telecominfraproject.wlan.firmware;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.telecominfraproject.wlan.remote.tests.BaseRemoteTest;

/**
 * @author dtoptygin
 *
 */
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = {
        "integration_test",
        "no_ssl","http_digest_auth","rest-template-single-user-per-service-digest-auth",
        }) //NOTE: these profiles will be ADDED to the list of active profiles  
public class FirmwareServiceRemoteTest extends BaseRemoteTest {

    
    @Autowired FirmwareServiceRemote remoteInterface; 
    
    @Before public void urlSetup(){
        configureBaseUrl("tip.wlan.firmwareServiceBaseUrl");
    }
    
    
    @Test
    public void testFirmwareCRUD() throws Exception {
        
    }

}
