package com.telecominfraproject.wlan.status.models.vendorextensions;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.status.models.StatusDataType;

public class TestVendorExtendedStatusModel extends BaseJsonModel{

    private static final long serialVersionUID = -7571240065645860754L;
    
    private String name;
    private StatusDataType dataType;
    
    protected TestVendorExtendedStatusModel() {
        //for deserializer
    }
    
    public TestVendorExtendedStatusModel(String name, StatusDataType dataType) {
        this.name = name;
        this.dataType = dataType;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public StatusDataType getDataType() {
        return dataType;
    }
    public void setDataType(StatusDataType dataType) {
        this.dataType = dataType;
    }

    public static void main(String[] args) {
        TestVendorExtendedStatusModel t1 = new TestVendorExtendedStatusModel("t1", StatusDataType.EQUIPMENT_ADMIN);
        TestVendorExtendedStatusModel t2 = new TestVendorExtendedStatusModel("t2", VendorExtendedStatusDataType.EQUIPMENT_ADMIN);
        TestVendorExtendedStatusModel t3 = new TestVendorExtendedStatusModel("t3", VendorExtendedStatusDataType.VENDOR_STATUS_A);
        
        System.out.println("t1  = "+ t1);
        System.out.println("t2  = "+ t2);
        System.out.println("t3  = "+ t3);
        
        TestVendorExtendedStatusModel t1d = BaseJsonModel.fromString(t1.toString(), TestVendorExtendedStatusModel.class);  
        TestVendorExtendedStatusModel t2d = BaseJsonModel.fromString(t2.toString(), TestVendorExtendedStatusModel.class);  
        TestVendorExtendedStatusModel t3d = BaseJsonModel.fromString(t3.toString(), TestVendorExtendedStatusModel.class);

        System.out.println("=======================");
        
        System.out.println("t1d = "+ t1d);
        System.out.println("t2d = "+ t2d);
        System.out.println("t3d = "+ t3d);

    }
}
