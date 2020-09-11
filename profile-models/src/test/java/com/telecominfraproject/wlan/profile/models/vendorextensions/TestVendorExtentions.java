package com.telecominfraproject.wlan.profile.models.vendorextensions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.profile.models.ProfileType;

public class TestVendorExtentions {

    @Test
    public void testExtendedEnum() {

        TestVendorExtendedProfileModel t1 = new TestVendorExtendedProfileModel("t1", ProfileType.equipment_ap);
        TestVendorExtendedProfileModel t2 = new TestVendorExtendedProfileModel("t2", VendorExtendedProfileType.VENDOR_PROFILE_A);
        TestVendorExtendedProfileModel t3 = new TestVendorExtendedProfileModel("t3", VendorExtendedProfileType.VENDOR_PROFILE_B);

        TestVendorExtendedProfileModel t1d = BaseJsonModel.fromString(t1.toString(), TestVendorExtendedProfileModel.class);  
        TestVendorExtendedProfileModel t2d = BaseJsonModel.fromString(t2.toString(), TestVendorExtendedProfileModel.class);  
        TestVendorExtendedProfileModel t3d = BaseJsonModel.fromString(t3.toString(), TestVendorExtendedProfileModel.class);

        assertEquals(t1.toString(), t1d.toString());
        assertEquals(t2.toString(), t2d.toString());
        assertEquals(t3.toString(), t3d.toString());
        
    }

}
