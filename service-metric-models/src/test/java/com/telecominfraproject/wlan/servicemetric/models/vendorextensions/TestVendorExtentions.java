package com.telecominfraproject.wlan.servicemetric.models.vendorextensions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDataType;

public class TestVendorExtentions {

    @Test
    public void testExtendedEnum() {
        VendorExtendedServiceMetricDataTypeModel t1 = new VendorExtendedServiceMetricDataTypeModel("t1", ServiceMetricDataType.ApNode);
        VendorExtendedServiceMetricDataTypeModel t2 = new VendorExtendedServiceMetricDataTypeModel("t2", VendorExtendedServiceMetricDataType.ApNode);
        VendorExtendedServiceMetricDataTypeModel t3 = new VendorExtendedServiceMetricDataTypeModel("t3", VendorExtendedServiceMetricDataType.VENDOR_SMD_B);
                
        VendorExtendedServiceMetricDataTypeModel t1d = BaseJsonModel.fromString(t1.toString(), VendorExtendedServiceMetricDataTypeModel.class);  
        VendorExtendedServiceMetricDataTypeModel t2d = BaseJsonModel.fromString(t2.toString(), VendorExtendedServiceMetricDataTypeModel.class);  
        VendorExtendedServiceMetricDataTypeModel t3d = BaseJsonModel.fromString(t3.toString(), VendorExtendedServiceMetricDataTypeModel.class);

        assertEquals(t1.toString(), t1d.toString());
        assertEquals(t2.toString(), t2d.toString());
        assertEquals(t3.toString(), t3d.toString());
        
    }

}
