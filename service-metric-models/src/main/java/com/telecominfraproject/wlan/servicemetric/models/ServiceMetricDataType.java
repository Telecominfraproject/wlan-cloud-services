package com.telecominfraproject.wlan.servicemetric.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telecominfraproject.wlan.core.model.extensibleenum.EnumWithId;

/**
 * All available ServiceMetricDataTypes that can be handled by the CloudSDK. 
 * <br>This enumeration-like class can be extended by vendors - new elements can be defined by extending this class like so:
 * <br>
 * <pre>
 * public class VendorExtendedServiceMetricDataType extends ServiceMetricDataType {
 *    
 *    public static final ServiceMetricDataType 
 *    VENDOR_SMD_A = new VendorExtendedServiceMetricDataType(100, "VENDOR_SMD_A") ,
 *    VENDOR_SMD_B = new VendorExtendedServiceMetricDataType(101, "VENDOR_SMD_B")
 *    ;
 *
 *    private VendorExtendedServiceMetricDataType(int id, String name) {
 *        super(id, name);
 *    }
 *
 * }
 * </pre>
 * @see com.telecominfraproject.wlan.core.model.extensibleenum.EnumWithId
 * @see com.telecominfraproject.wlan.servicemetric.models.vendorextensions.TestVendorExtendedServiceMetricDataTypeModel
 * <br>
 * @author dtop
 *
 */
public class ServiceMetricDataType implements EnumWithId {

    private static Object lock = new Object();
    private static final Map<Integer, ServiceMetricDataType> ELEMENTS = new ConcurrentHashMap<>();
    private static final Map<String, ServiceMetricDataType> ELEMENTS_BY_NAME = new ConcurrentHashMap<>();

    public static final ServiceMetricDataType 

    ApNode = new ServiceMetricDataType(1, "ApNode"),
    ApSsid = new ServiceMetricDataType(2, "ApSsid"),
    Client = new ServiceMetricDataType(3, "Client"),
    Channel = new ServiceMetricDataType(4, "Channel"),
    Neighbour = new ServiceMetricDataType(5, "Neighbour"),
    QoE = new ServiceMetricDataType(6, "QoE"),
    ClientQoE = new ServiceMetricDataType(7, "ClientQoE"),

    UNSUPPORTED = new ServiceMetricDataType(-1, "UNSUPPORTED");
    
    private final int id;
    private final String name;
    
    protected ServiceMetricDataType(int id, String name) {
        synchronized(lock) {
            this.id = id;
            this.name = name;

            ELEMENTS_BY_NAME.values().forEach(s -> {
                if(s.getName().equals(name)) {
                    throw new IllegalStateException("ServiceMetricDataType item for "+ name + " is already defined, cannot have more than one of them");
                }                
            });
    
            if(ELEMENTS.containsKey(id)) {
                throw new IllegalStateException("ServiceMetricDataType item "+ name + "("+id+") is already defined, cannot have more than one of them");
            }
    
            ELEMENTS.put(id, this);
            ELEMENTS_BY_NAME.put(name, this);
        }
    }
    
    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @JsonIgnore
    public String name() {
        return name;
    }

    @JsonIgnore
    public static ServiceMetricDataType[] values() {
        return new ArrayList<>(ELEMENTS.values()).toArray(new ServiceMetricDataType[0]);
    }

    public static ServiceMetricDataType getById(int enumId){
        return ELEMENTS.get(enumId);
    }
    
    @JsonCreator
    public static ServiceMetricDataType getByName(String value) {
        ServiceMetricDataType ret = ELEMENTS_BY_NAME.get(value);
        if (ret == null) {
            ret = UNSUPPORTED;
        }
        
        return ret;
    }


    public static List<ServiceMetricDataType> getValues() {
        return new ArrayList<>(ELEMENTS.values());
    }
    
    public static boolean isUnsupported(ServiceMetricDataType value) {
        return (UNSUPPORTED.equals(value));
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ServiceMetricDataType)) {
            return false;
        }
        ServiceMetricDataType other = (ServiceMetricDataType) obj;
        return id == other.id;
    }   

    @Override
    public String toString() {
        return name;
    }
    

}
