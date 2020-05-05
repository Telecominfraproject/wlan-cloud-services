package com.telecominfraproject.wlan.profile.captiveportal.models;

import org.junit.Test;

import com.telecominfraproject.wlan.profile.captiveportal.user.models.TimedAccessUserDetails;
import com.telecominfraproject.wlan.profile.captiveportal.user.models.TimedAccessUserRecord;

public class TimedAccessUserRecordTest 
{
    @Test
    public void testValidation()
    {
        TimedAccessUserDetails details = new TimedAccessUserDetails();
        
        TimedAccessUserRecord record = new TimedAccessUserRecord();
        record.setUserDetails(details);
        record.setPassword("abc123");
        record.setUsername("myuser");
        
        TimedAccessUserRecord.validateTimedAccessUserRecord(record);
    }

}
