package com.telecominfraproject.wlan.status.models.vendorextensions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.status.models.StatusDataType;

public class TestVendorExtentions {

    @Test
    public void testExtendedEnum() {
        TestVendorExtendedStatusModel t1 = new TestVendorExtendedStatusModel("t1", StatusDataType.EQUIPMENT_ADMIN);
        TestVendorExtendedStatusModel t2 = new TestVendorExtendedStatusModel("t2", VendorExtendedStatusDataType.EQUIPMENT_ADMIN);
        TestVendorExtendedStatusModel t3 = new TestVendorExtendedStatusModel("t3", VendorExtendedStatusDataType.VENDOR_STATUS_A);
                
        TestVendorExtendedStatusModel t1d = BaseJsonModel.fromString(t1.toString(), TestVendorExtendedStatusModel.class);  
        TestVendorExtendedStatusModel t2d = BaseJsonModel.fromString(t2.toString(), TestVendorExtendedStatusModel.class);  
        TestVendorExtendedStatusModel t3d = BaseJsonModel.fromString(t3.toString(), TestVendorExtendedStatusModel.class);

        assertEquals(t1.toString(), t1d.toString());
        assertEquals(t2.toString(), t2d.toString());
        assertEquals(t3.toString(), t3d.toString());
        
    }

}
