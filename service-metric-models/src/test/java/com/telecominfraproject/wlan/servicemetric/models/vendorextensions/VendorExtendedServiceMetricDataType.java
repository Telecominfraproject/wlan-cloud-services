package com.telecominfraproject.wlan.servicemetric.models.vendorextensions;

import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDataType;

public class VendorExtendedServiceMetricDataType extends ServiceMetricDataType {
    
    public static final ServiceMetricDataType 
    VENDOR_SMD_A = new VendorExtendedServiceMetricDataType(100, "VENDOR_SMD_A") ,
    VENDOR_SMD_B = new VendorExtendedServiceMetricDataType(101, "VENDOR_SMD_B")
    ;

    private VendorExtendedServiceMetricDataType(int id, String name) {
        super(id, name);
    }

}
