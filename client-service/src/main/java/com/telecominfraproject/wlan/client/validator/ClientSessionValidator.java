package com.telecominfraproject.wlan.client.validator;

import org.springframework.context.annotation.Configuration;

import com.telecominfraproject.wlan.client.session.models.ClientSession;
import com.telecominfraproject.wlan.client.session.models.ClientSessionDetails;

@Configuration
public class ClientSessionValidator 
{
	protected static final int MAX_SSID_LENGTH = 32;

	public void validateClientSession(ClientSession clientSession) throws ClientSessionValidatorException
	{
	    throwIfNull(clientSession, ClientSession.class);
	    throwIfNull(clientSession.getDetails(), ClientSessionDetails.class);
	    
        if (clientSession.getDetails().getSsid() != null)
		{
			checkSsidLength(clientSession.getDetails().getSsid());
		}
	}

	private void checkSsidLength(String ssid) throws ClientSessionValidatorException
	{
		if (ssid.length() > MAX_SSID_LENGTH)
		{
			throw new ClientSessionValidatorException(String.format("given SSID: %s of length: %2d exceeds the maximum character limit of %2d.", ssid, ssid.length(), MAX_SSID_LENGTH));
		}
	}
	
	private void throwIfNull(Object object, Class clazz) throws ClientSessionValidatorException
	{
	    if (object == null)
	    {
	        throw new ClientSessionValidatorException(String.format("%s is required but null.", clazz.getName()));
	    }
	}
}