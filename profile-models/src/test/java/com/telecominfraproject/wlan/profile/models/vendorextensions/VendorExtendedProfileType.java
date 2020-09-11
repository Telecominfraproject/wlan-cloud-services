package com.telecominfraproject.wlan.profile.models.vendorextensions;

import com.telecominfraproject.wlan.profile.models.ProfileType;

public class VendorExtendedProfileType extends ProfileType {
      
    public static final ProfileType 
    VENDOR_PROFILE_A = new VendorExtendedProfileType(100, "VENDOR_PROFILE_A") ,
    VENDOR_PROFILE_B = new VendorExtendedProfileType(101, "VENDOR_PROFILE_B")
    ;

    private VendorExtendedProfileType(int id, String name) {
        super(id, name);
    }

}