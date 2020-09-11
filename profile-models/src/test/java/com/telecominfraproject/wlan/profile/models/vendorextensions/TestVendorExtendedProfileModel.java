package com.telecominfraproject.wlan.profile.models.vendorextensions;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.profile.models.ProfileType;

public class TestVendorExtendedProfileModel extends BaseJsonModel{

    private static final long serialVersionUID = -7571240065645860754L;
    
    private String name;
    private ProfileType dataType;
    
    protected TestVendorExtendedProfileModel() {
        //for deserializer
    }
    
    public TestVendorExtendedProfileModel(String name, ProfileType dataType) {
        this.name = name;
        this.dataType = dataType;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public ProfileType getDataType() {
        return dataType;
    }
    public void setDataType(ProfileType dataType) {
        this.dataType = dataType;
    }

    public static void main(String[] args) {
        TestVendorExtendedProfileModel t1 = new TestVendorExtendedProfileModel("t1", ProfileType.equipment_ap);
        TestVendorExtendedProfileModel t2 = new TestVendorExtendedProfileModel("t2", VendorExtendedProfileType.VENDOR_PROFILE_A);
        TestVendorExtendedProfileModel t3 = new TestVendorExtendedProfileModel("t3", VendorExtendedProfileType.VENDOR_PROFILE_B);
        
        System.out.println("t1  = "+ t1);
        System.out.println("t2  = "+ t2);
        System.out.println("t3  = "+ t3);
        
        TestVendorExtendedProfileModel t1d = BaseJsonModel.fromString(t1.toString(), TestVendorExtendedProfileModel.class);  
        TestVendorExtendedProfileModel t2d = BaseJsonModel.fromString(t2.toString(), TestVendorExtendedProfileModel.class);  
        TestVendorExtendedProfileModel t3d = BaseJsonModel.fromString(t3.toString(), TestVendorExtendedProfileModel.class);

        System.out.println("=======================");
        
        System.out.println("t1d = "+ t1d);
        System.out.println("t2d = "+ t2d);
        System.out.println("t3d = "+ t3d);

    }
}
