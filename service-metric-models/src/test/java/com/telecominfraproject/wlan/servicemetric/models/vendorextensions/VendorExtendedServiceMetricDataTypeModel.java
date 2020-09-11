package com.telecominfraproject.wlan.servicemetric.models.vendorextensions;

import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;
import com.telecominfraproject.wlan.servicemetric.models.ServiceMetricDataType;

public class VendorExtendedServiceMetricDataTypeModel extends BaseJsonModel{

    private static final long serialVersionUID = -7571240065645860754L;
    
    private String name;
    private ServiceMetricDataType dataType;
    
    protected VendorExtendedServiceMetricDataTypeModel() {
        //for deserializer
    }
    
    public VendorExtendedServiceMetricDataTypeModel(String name, ServiceMetricDataType dataType) {
        this.name = name;
        this.dataType = dataType;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public ServiceMetricDataType getDataType() {
        return dataType;
    }
    public void setDataType(ServiceMetricDataType dataType) {
        this.dataType = dataType;
    }

    public static void main(String[] args) {
        VendorExtendedServiceMetricDataTypeModel t1 = new VendorExtendedServiceMetricDataTypeModel("t1", ServiceMetricDataType.ApNode);
        VendorExtendedServiceMetricDataTypeModel t2 = new VendorExtendedServiceMetricDataTypeModel("t2", VendorExtendedServiceMetricDataType.ApNode);
        VendorExtendedServiceMetricDataTypeModel t3 = new VendorExtendedServiceMetricDataTypeModel("t3", VendorExtendedServiceMetricDataType.VENDOR_SMD_B);
        
        System.out.println("t1  = "+ t1);
        System.out.println("t2  = "+ t2);
        System.out.println("t3  = "+ t3);
        
        VendorExtendedServiceMetricDataTypeModel t1d = BaseJsonModel.fromString(t1.toString(), VendorExtendedServiceMetricDataTypeModel.class);  
        VendorExtendedServiceMetricDataTypeModel t2d = BaseJsonModel.fromString(t2.toString(), VendorExtendedServiceMetricDataTypeModel.class);  
        VendorExtendedServiceMetricDataTypeModel t3d = BaseJsonModel.fromString(t3.toString(), VendorExtendedServiceMetricDataTypeModel.class);

        System.out.println("=======================");
        
        System.out.println("t1d = "+ t1d);
        System.out.println("t2d = "+ t2d);
        System.out.println("t3d = "+ t3d);

    }
}
