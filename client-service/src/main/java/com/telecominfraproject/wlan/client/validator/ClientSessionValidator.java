package com.telecominfraproject.wlan.client.validator;

import static com.telecominfraproject.wlan.profile.ssid.models.SsidConfiguration.MAX_SSID_LENGTH;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;

import com.telecominfraproject.wlan.client.session.models.ClientSession;
import com.telecominfraproject.wlan.client.session.models.ClientSessionDetails;
import com.telecominfraproject.wlan.profile.ssid.models.SsidExceedsMaxLengthException;

@Configuration
public class ClientSessionValidator 
{
    private static final Logger LOG = LoggerFactory.getLogger(ClientSessionValidator.class);
    
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
		    String msg = String.format("given SSID: %s of length: %2d exceeds the maximum character limit of %2d.", ssid, ssid.length(), MAX_SSID_LENGTH);
		    LOG.error(msg);
			throw new SsidExceedsMaxLengthException(msg);
		}
	}
	
	private void throwIfNull(Object object, Class clazz) throws ClientSessionValidatorException
	{
	    if (object == null)
	    {
	        String msg = String.format("%s is required but null.", clazz.getName());
	        LOG.error(msg);
            throw new ClientSessionValidatorException(msg);
	    }
	}
}