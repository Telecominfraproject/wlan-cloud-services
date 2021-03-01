package com.telecominfraproject.wlan.client.validator;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.telecominfraproject.wlan.client.session.models.ClientSession;
import com.telecominfraproject.wlan.client.session.models.ClientSessionDetails;

/**
 * @author tcurrie
 * 
 * Unit test for ClientSessionValidator
 *
 */
@RunWith(SpringRunner.class)
@ActiveProfiles(profiles = {
		"integration_test",
}) //NOTE: these profiles will be ADDED to the list of active profiles
@SpringBootTest(webEnvironment = WebEnvironment.NONE, classes = ClientSessionValidatorTest.class)
@Import(value = {
		ClientSessionValidator.class,
		ClientSession.class,
		ClientSessionDetails.class
})
public class ClientSessionValidatorTest {

	private ClientSessionValidator clientSessionValidator;
	@Mock ClientSession clientSession;
	@Mock ClientSessionDetails clientSessionDetails;

	@Configuration
	//@PropertySource({ "classpath:persistence-${envTarget:dev}.properties" })
	static class Config {
		// Put all required @Bean -s in here

	}
	
	@Before
	public void setup()
	{
		clientSessionValidator = new ClientSessionValidator();
	}
	
	
	@Test
	public void givenLessThanSsidLimit_whenValidateClientSession_happyPath() throws Exception
	{
		when(clientSession.getDetails()).thenReturn(clientSessionDetails);
		when(clientSessionDetails.getSsid()).thenReturn(sampleSsidFromLength(ClientSessionValidator.MAX_SSID_LENGTH - 1));
		
		clientSessionValidator.validateClientSession(clientSession);
	}
	
	@Test
	public void givenEqualToSsidLimit_whenValidateClientSession_happyPath() throws Exception
	{
		when(clientSession.getDetails()).thenReturn(clientSessionDetails);
		when(clientSessionDetails.getSsid()).thenReturn(sampleSsidFromLength(ClientSessionValidator.MAX_SSID_LENGTH));
		
		clientSessionValidator.validateClientSession(clientSession);
	}
	
	@Test(expected = ClientSessionValidatorException.class) 
	public void givenGreaterThanSsidLimit_whenValidateClientSession_assertThrows() throws Exception
	{
		when(clientSession.getDetails()).thenReturn(clientSessionDetails);
		when(clientSessionDetails.getSsid()).thenReturn(sampleSsidFromLength(ClientSessionValidator.MAX_SSID_LENGTH + 1));
		
		clientSessionValidator.validateClientSession(clientSession);
	}
	
	@Test(expected = ClientSessionValidatorException.class) 
    public void givenNulLCLientSession_whenValidateClientSession_assertThrows() throws Exception
    {
        clientSessionValidator.validateClientSession(null);
    }
	
	@Test(expected = ClientSessionValidatorException.class) 
    public void givenNullClientSessionDetails_whenValidateClientSession_assertThrows() throws Exception
    {
        when(clientSession.getDetails()).thenReturn(null);
        
        clientSessionValidator.validateClientSession(clientSession);
    }
	
	private String sampleSsidFromLength(int length)
	{
		String filled = "a";
		return filled.repeat(length);
	}
}