package com.telecominfraproject.wlan.alarm.models.vendorextensions;

import com.telecominfraproject.wlan.alarm.models.AlarmCode;

 public class VendorExtendedAlarmCode extends AlarmCode {
    
    public static final AlarmCode 
    VENDOR_AC_A = new VendorExtendedAlarmCode(300, "VENDOR_AC_A", "description A") ,
    VENDOR_AC_B = new VendorExtendedAlarmCode(301, "VENDOR_AC_B", "description B") ;
    
    @Deprecated
    public static final AlarmCode 
    VENDOR_AC_DEPRECATED = new VendorExtendedAlarmCode(302, "VENDOR_AC_DEPRECATED", "description DEPRECATED")
    ;

    private VendorExtendedAlarmCode(int id, String name, String description) {
        super(id, name, description);
    }

 }