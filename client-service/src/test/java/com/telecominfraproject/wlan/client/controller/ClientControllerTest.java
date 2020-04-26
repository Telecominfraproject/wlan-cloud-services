package com.telecominfraproject.wlan.client.controller;

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

import com.telecominfraproject.wlan.client.datastore.inmemory.ClientDatastoreInMemory;
import com.telecominfraproject.wlan.client.models.Client;

/**
 * @author dtoptygin
 * 
 * Integration test for ClientController
 *
 */
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = {
        "integration_test",
        }) //NOTE: these profiles will be ADDED to the list of active profiles
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = ClientControllerTest.class)
@Import(value = {
        ClientController.class,
        CloudEventDispatcherEmpty.class,
        ClientDatastoreInMemory.class,
        ClientControllerTest.Config.class, 
        })
public class ClientControllerTest {
    
    @Autowired private ClientController clientController;

    
    @Configuration
    //@PropertySource({ "classpath:persistence-${envTarget:dev}.properties" })
    static class Config {
        // Put all required @Bean -s in here
        
    }
    
    @Test
    public void testClientCRUD() throws Exception {
        
        //Create new Client - success
        Client client = new Client();
        client.setSampleStr("test");

        Client ret = clientController.create(client);
        assertNotNull(ret);

        ret = clientController.get(ret.getId());
        assertEqualClients(client, ret);

        ret = clientController.getOrNull(ret.getId());
        assertEqualClients(client, ret);
        
        assertNull(clientController.getOrNull(-1));

        //Delete - success
        clientController.delete(ret.getId());
        
    }
        
    private void assertEqualClients(
            Client expected,
            Client actual) {
        
        assertEquals(expected.getSampleStr(), actual.getSampleStr());
        //TODO: add more fields to check here
    }

}
