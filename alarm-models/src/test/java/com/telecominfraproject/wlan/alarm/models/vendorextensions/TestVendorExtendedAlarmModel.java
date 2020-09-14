package com.telecominfraproject.wlan.alarm.models.vendorextensions;

import com.telecominfraproject.wlan.alarm.models.AlarmCode;
import com.telecominfraproject.wlan.alarm.models.OriginatorType;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

public class TestVendorExtendedAlarmModel extends BaseJsonModel{

    private static final long serialVersionUID = -7571240065645860754L;
    
    private String name;
    private OriginatorType originatorType;
    private AlarmCode alarmCode;
    
    
    protected TestVendorExtendedAlarmModel() {
        //for deserializer
    }
    
    public TestVendorExtendedAlarmModel(String name, OriginatorType originatorType, AlarmCode alarmCode) {
        this.name = name;
        this.originatorType = originatorType;
        this.alarmCode = alarmCode;
    }
    
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public OriginatorType getOriginatorType() {
        return originatorType;
    }
    public void setOriginatorType(OriginatorType originatorType) {
        this.originatorType = originatorType;
    }

    public AlarmCode getAlarmCode() {
        return alarmCode;
    }
    public void setAlarmCode(AlarmCode alarmCode) {
        this.alarmCode = alarmCode;
    }

    public static void main(String[] args) {
        TestVendorExtendedAlarmModel t1 = new TestVendorExtendedAlarmModel("t1", OriginatorType.AP, AlarmCode.AssocFailure);
        TestVendorExtendedAlarmModel t2 = new TestVendorExtendedAlarmModel("t2", VendorExtendedOriginatorType.AP, VendorExtendedAlarmCode.AssocFailure);
        TestVendorExtendedAlarmModel t3 = new TestVendorExtendedAlarmModel("t3", VendorExtendedOriginatorType.VENDOR_OT_B, VendorExtendedAlarmCode.VENDOR_AC_B);
        
        System.out.println("t1  = "+ t1);
        System.out.println("t2  = "+ t2);
        System.out.println("t3  = "+ t3);
        
        TestVendorExtendedAlarmModel t1d = BaseJsonModel.fromString(t1.toString(), TestVendorExtendedAlarmModel.class);  
        TestVendorExtendedAlarmModel t2d = BaseJsonModel.fromString(t2.toString(), TestVendorExtendedAlarmModel.class);  
        TestVendorExtendedAlarmModel t3d = BaseJsonModel.fromString(t3.toString(), TestVendorExtendedAlarmModel.class);

        System.out.println("=======================");
        
        System.out.println("t1d = "+ t1d);
        System.out.println("t2d = "+ t2d);
        System.out.println("t3d = "+ t3d);

    }
}
