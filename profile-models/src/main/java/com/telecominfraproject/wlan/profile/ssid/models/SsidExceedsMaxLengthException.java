package com.telecominfraproject.wlan.profile.ssid.models;

import com.telecominfraproject.wlan.datastore.exceptions.DsDataValidationException;

public class SsidExceedsMaxLengthException extends DsDataValidationException {
    
    /**
     * 
     */
    private static final long serialVersionUID = 2195122908346739152L;

    public SsidExceedsMaxLengthException(String message)
    {
        super(message);
    }
}
