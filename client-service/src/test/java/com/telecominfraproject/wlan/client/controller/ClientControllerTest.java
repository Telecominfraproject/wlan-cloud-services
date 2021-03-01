package com.telecominfraproject.wlan.client.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
import com.telecominfraproject.wlan.client.session.models.ClientSession;
import com.telecominfraproject.wlan.client.session.models.ClientSessionDetails;
import com.telecominfraproject.wlan.client.validator.ClientSessionValidator;
import com.telecominfraproject.wlan.client.validator.ClientSessionValidatorException;
import com.telecominfraproject.wlan.cloudeventdispatcher.CloudEventDispatcherEmpty;
import com.telecominfraproject.wlan.core.model.equipment.MacAddress;
import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;

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
        ClientSessionValidator.class,
        ClientSession.class,
        DsDataValidationException.class,
        ClientSessionDetails.class,
        })
public class ClientControllerTest {
    
    @Autowired private ClientController clientController;
    @InjectMocks private ClientController mockClientController;
    @Mock ClientSessionValidator clientSessionValidator;
    @Mock ClientSession clientSession;
    @Mock ClientSessionDetails clientSessionDetails;
    
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
    
    @Test(expected = DsDataValidationException.class)
    public void givenClientSessionValidatorException_whenUpdateClientSession_assertThrows() throws Exception
    {
        when(clientSession.getDetails()).thenReturn(clientSessionDetails);
        when(clientSession.getDetails().getSsid()).thenReturn("non-null");
        doThrow(ClientSessionValidatorException.class)
          .when(clientSessionValidator).validateClientSession(clientSession);

        mockClientController.updateSession(clientSession);
    }

    @Test(expected = DsDataValidationException.class)
    public void givenClientSessionValidatorException_whenUpdateClientSessionBulk_assertThrows() throws Exception
    {
        List<ClientSession> clientSessions = new ArrayList<ClientSession>();
        clientSessions.add(clientSession);

        when(clientSession.getDetails()).thenReturn(clientSessionDetails);
        when(clientSession.getDetails().getSsid()).thenReturn("non-null");
        doThrow(ClientSessionValidatorException.class)
          .when(clientSessionValidator).validateClientSession(clientSession);

        mockClientController.updateSessionsBulk(clientSessions);
    }
        
    private void assertEqualClients(
            Client expected,
            Client actual) {
        
        assertEquals(expected.getDetails(), actual.getDetails());
        //TODO: add more fields to check here
    }

}
