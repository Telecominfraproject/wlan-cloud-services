package com.telecominfraproject.wlan.alarm.models.vendorextensions;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

import com.telecominfraproject.wlan.alarm.models.AlarmCode;
import com.telecominfraproject.wlan.alarm.models.OriginatorType;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

public class TestVendorExtentions {

    @Test
    public void testExtendedEnum() {

        TestVendorExtendedAlarmModel t1 = new TestVendorExtendedAlarmModel("t1", OriginatorType.AP, AlarmCode.AssocFailure);
        TestVendorExtendedAlarmModel t2 = new TestVendorExtendedAlarmModel("t2", VendorExtendedOriginatorType.AP, VendorExtendedAlarmCode.AssocFailure);
        TestVendorExtendedAlarmModel t3 = new TestVendorExtendedAlarmModel("t3", VendorExtendedOriginatorType.VENDOR_OT_B, VendorExtendedAlarmCode.VENDOR_AC_B);

        TestVendorExtendedAlarmModel t1d = BaseJsonModel.fromString(t1.toString(), TestVendorExtendedAlarmModel.class);  
        TestVendorExtendedAlarmModel t2d = BaseJsonModel.fromString(t2.toString(), TestVendorExtendedAlarmModel.class);  
        TestVendorExtendedAlarmModel t3d = BaseJsonModel.fromString(t3.toString(), TestVendorExtendedAlarmModel.class);

        assertEquals(t1.toString(), t1d.toString());
        assertEquals(t2.toString(), t2d.toString());
        assertEquals(t3.toString(), t3d.toString());
        
    }

}
