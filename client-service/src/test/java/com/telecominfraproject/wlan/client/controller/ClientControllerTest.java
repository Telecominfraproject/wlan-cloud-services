package com.telecominfraproject.wlan.client.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.telecominfraproject.wlan.client.datastore.inmemory.ClientDatastoreInMemory;
import com.telecominfraproject.wlan.client.info.models.ClientInfoDetails;
import com.telecominfraproject.wlan.client.models.Client;
import com.telecominfraproject.wlan.cloudeventdispatcher.CloudEventDispatcherEmpty;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;

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
    
    private static final AtomicLong testSequence = new AtomicLong(2000);

    
    @Configuration
    //@PropertySource({ "classpath:persistence-${envTarget:dev}.properties" })
    static class Config {
        // Put all required @Bean -s in here
        
    }
    
    @Test
    public void testClientCRUD() throws Exception {
        
        //Create new Client - success
        Client client = new Client();
        long nextId = testSequence.getAndIncrement();
        client.setCustomerId((int) nextId);
        client.setMacAddress(new MacAddress(nextId));
        ClientInfoDetails details = new ClientInfoDetails();
        details.setAlias("test");
		client.setDetails(details );

        Client ret = clientController.create(client);
        assertNotNull(ret);

        ret = clientController.getOrNull(ret.getCustomerId(), ret.getMacAddress());
        assertEqualClients(client, ret);

        //Delete - success
        clientController.delete(ret.getCustomerId(), ret.getMacAddress());
        
    }
        
    private void assertEqualClients(
            Client expected,
            Client actual) {
        
        assertEquals(expected.getDetails(), actual.getDetails());
        //TODO: add more fields to check here
    }

}
