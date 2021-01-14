package com.telecominfraproject.wlan.status.models.vendorextensions;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.status.models.StatusDataType;
import com.telecominfraproject.wlan.status.models.StatusTrait;

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

    @Test
    public void testExtendedTraits() {
        assertTrue(StatusDataType.EQUIPMENT_ADMIN.supportsTrait(StatusTrait.DeleteOnEquipmentDisconnect));
        assertTrue(StatusDataType.EQUIPMENT_ADMIN.supportsTrait(null));
        assertTrue(StatusDataType.EQUIPMENT_ADMIN.supportsTraits(null));
        assertTrue(StatusDataType.EQUIPMENT_ADMIN.supportsTraits(Set.of(StatusTrait.DeleteOnEquipmentDisconnect)));
        
        assertFalse(VendorExtendedStatusDataType.VENDOR_STATUS_A.supportsTraits(Set.of(StatusTrait.DeleteOnEquipmentDisconnect)));
        assertTrue(VendorExtendedStatusDataType.VENDOR_STATUS_A.supportsTraits(Set.of(VendorExtendedStatusTrait.VENDOR_STATUS_TRAIT_A)));
        assertTrue(VendorExtendedStatusDataType.VENDOR_STATUS_A.supportsTraits(Set.of(VendorExtendedStatusTrait.VENDOR_STATUS_TRAIT_B)));
        assertTrue(VendorExtendedStatusDataType.VENDOR_STATUS_A.supportsTraits(Set.of(VendorExtendedStatusTrait.VENDOR_STATUS_TRAIT_A, VendorExtendedStatusTrait.VENDOR_STATUS_TRAIT_B)));
        
        assertFalse(VendorExtendedStatusDataType.VENDOR_STATUS_B.supportsTraits(Set.of(VendorExtendedStatusTrait.VENDOR_STATUS_TRAIT_A)));
        assertFalse(VendorExtendedStatusDataType.VENDOR_STATUS_B.supportsTraits(Set.of(VendorExtendedStatusTrait.VENDOR_STATUS_TRAIT_B)));
        assertFalse(VendorExtendedStatusDataType.VENDOR_STATUS_B.supportsTraits(Set.of(VendorExtendedStatusTrait.VENDOR_STATUS_TRAIT_A, VendorExtendedStatusTrait.VENDOR_STATUS_TRAIT_B)));

        assertFalse(VendorExtendedStatusDataType.VENDOR_STATUS_C.supportsTraits(Set.of(VendorExtendedStatusTrait.VENDOR_STATUS_TRAIT_A)));
        assertTrue(VendorExtendedStatusDataType.VENDOR_STATUS_C.supportsTraits(Set.of(VendorExtendedStatusTrait.VENDOR_STATUS_TRAIT_B)));
        assertFalse(VendorExtendedStatusDataType.VENDOR_STATUS_C.supportsTraits(Set.of(VendorExtendedStatusTrait.VENDOR_STATUS_TRAIT_A, VendorExtendedStatusTrait.VENDOR_STATUS_TRAIT_B)));

        assertEquals(Set.of(VendorExtendedStatusDataType.VENDOR_STATUS_A), VendorExtendedStatusDataType.getByTraits(Set.of(VendorExtendedStatusTrait.VENDOR_STATUS_TRAIT_A)));
        assertEquals(Set.of(VendorExtendedStatusDataType.VENDOR_STATUS_A, VendorExtendedStatusDataType.VENDOR_STATUS_C), VendorExtendedStatusDataType.getByTraits(Set.of(VendorExtendedStatusTrait.VENDOR_STATUS_TRAIT_B)));
        assertEquals(Set.of(VendorExtendedStatusDataType.VENDOR_STATUS_A), VendorExtendedStatusDataType.getByTraits(Set.of(VendorExtendedStatusTrait.VENDOR_STATUS_TRAIT_A, VendorExtendedStatusTrait.VENDOR_STATUS_TRAIT_B)));
        
        assertEquals(new HashSet<>(StatusDataType.getValues()), VendorExtendedStatusDataType.getByTraits(null));
        assertEquals(new HashSet<>(StatusDataType.getValues()), VendorExtendedStatusDataType.getByTraits(Collections.emptyList()));
        
    }
}
