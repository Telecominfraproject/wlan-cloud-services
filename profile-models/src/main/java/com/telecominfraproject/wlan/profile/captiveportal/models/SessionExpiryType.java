package com.telecominfraproject.wlan.profile.captiveportal.models;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

public enum SessionExpiryType 
{
    time_limited(1),
    unlimited(2),
    UNSUPPORTED(-1);
    
    private final int id;
    
    
    private SessionExpiryType(int id)
    {
        this.id = id;
    }
    
    public int getId() {
		return id;
	}
    
    public static boolean isUnsupported(SessionExpiryType value) {
        return UNSUPPORTED.equals(value);
    }
    
    @JsonCreator
    public static SessionExpiryType getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, SessionExpiryType.class, UNSUPPORTED);
    }

    
}
