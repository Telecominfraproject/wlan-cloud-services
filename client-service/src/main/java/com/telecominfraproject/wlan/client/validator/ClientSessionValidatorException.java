package com.telecominfraproject.wlan.client.validator;

import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;

public class ClientSessionValidatorException extends DsDataValidationException
{
	private static final long serialVersionUID = 2225776244463518888L;
	
	public ClientSessionValidatorException(String message)
	{
		super(message);
	}
}