package com.telecominfraproject.wlan.status.models.vendorextensions;

import java.util.Set;

import com.telecominfraproject.wlan.status.models.StatusDataType;
import com.telecominfraproject.wlan.status.models.StatusTrait;

public class VendorExtendedStatusDataType extends StatusDataType {
    
    public static final StatusDataType 
    VENDOR_STATUS_A = new VendorExtendedStatusDataType(100, "VENDOR_STATUS_A", Set.of(VendorExtendedStatusTrait.VENDOR_STATUS_TRAIT_A, VendorExtendedStatusTrait.VENDOR_STATUS_TRAIT_B)) ,
    VENDOR_STATUS_B = new VendorExtendedStatusDataType(101, "VENDOR_STATUS_B"),
    VENDOR_STATUS_C = new VendorExtendedStatusDataType(102, "VENDOR_STATUS_C", Set.of(VendorExtendedStatusTrait.VENDOR_STATUS_TRAIT_B))
    ;

    private VendorExtendedStatusDataType(int id, String name) {
        this(id, name, null);
    }

    private VendorExtendedStatusDataType(int id, String name, Set<StatusTrait> traits) {
        super(id, name, traits);
    }

}
