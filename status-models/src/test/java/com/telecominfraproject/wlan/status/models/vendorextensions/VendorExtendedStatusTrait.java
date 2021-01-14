package com.telecominfraproject.wlan.status.models.vendorextensions;

import com.telecominfraproject.wlan.status.models.StatusTrait;

public class VendorExtendedStatusTrait extends StatusTrait {
    
    public static final StatusTrait 
    VENDOR_STATUS_TRAIT_A = new VendorExtendedStatusTrait(100, "VENDOR_STATUS_TRAIT_A") ,
    VENDOR_STATUS_TRAIT_B = new VendorExtendedStatusTrait(101, "VENDOR_STATUS_TRAIT_B")
    ;

    private VendorExtendedStatusTrait(int id, String name) {
        super(id, name);
    }

}
