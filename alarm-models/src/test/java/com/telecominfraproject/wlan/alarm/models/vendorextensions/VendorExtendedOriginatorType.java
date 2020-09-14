package com.telecominfraproject.wlan.alarm.models.vendorextensions;

import com.telecominfraproject.wlan.alarm.models.OriginatorType;

public class VendorExtendedOriginatorType extends OriginatorType {

    public static final OriginatorType 
    VENDOR_OT_A = new VendorExtendedOriginatorType(100, "VENDOR_OT_A"),
    VENDOR_OT_B = new VendorExtendedOriginatorType(101, "VENDOR_OT_B");

    private VendorExtendedOriginatorType(int id, String name) {
        super(id, name);
    }

}