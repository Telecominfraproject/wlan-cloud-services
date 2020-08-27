package com.telecominfraproject.wlan.status.models.vendorextensions;

import com.telecominfraproject.wlan.status.models.StatusDataType;

public class VendorExtendedStatusDataType extends StatusDataType {
    
    public static final StatusDataType 
    VENDOR_STATUS_A = new VendorExtendedStatusDataType(100, "VENDOR_STATUS_A") ,
    VENDOR_STATUS_B = new VendorExtendedStatusDataType(101, "VENDOR_STATUS_B")
    ;

    private VendorExtendedStatusDataType(int id, String name) {
        super(id, name);
    }

}
