package com.telecominfraproject.wlan.alarm.models.vendorextensions;

import com.telecominfraproject.wlan.alarm.models.AlarmCode;

 public class VendorExtendedAlarmCode extends AlarmCode {
    
    public static final AlarmCode 
    VENDOR_AC_A = new VendorExtendedAlarmCode(500, "VENDOR_AC_A", "description A") ,
    VENDOR_AC_B = new VendorExtendedAlarmCode(501, "VENDOR_AC_B", "description B")
    ;

    private VendorExtendedAlarmCode(int id, String name, String description) {
        super(id, name, description);
    }

 }