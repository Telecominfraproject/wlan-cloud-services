package com.telecominfraproject.wlan.alarm.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telecominfraproject.wlan.core.model.extensibleenum.EnumWithId;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

/**
 * All available Alarm Originator types that can be handled by the CloudSDK. 
 * <br>This enumeration-like class can be extended by vendors - new elements can be defined by extending this class like so:
 * <br>
 * <pre>
 * public class VendorExtendedOriginatorType extends OriginatorType {
 *    
 *    public static final OriginatorType 
 *    VENDOR_OT_A = new VendorExtendedOriginatorType(100, "VENDOR_OT_A") ,
 *    VENDOR_OT_B = new VendorExtendedOriginatorType(101, "VENDOR_OT_B")
 *    ;
 *
 *    private VendorExtendedOriginatorType(int id, String name) {
 *        super(id, name);
 *    }
 *
 * }
 * </pre>
 * @see com.telecominfraproject.wlan.core.model.extensibleenum.EnumWithId
 * @see com.telecominfraproject.wlan.alarm.models.vendorextensions.TestVendorExtendedAlarmModel
 * <br>
 * @author ekeddy
 * @author dtop
 *
 */
public class OriginatorType  implements EnumWithId {

    private static final Logger LOG = LoggerFactory.getLogger(OriginatorType.class);

    private static Object lock = new Object();
    private static final Map<Integer, OriginatorType> ELEMENTS = new ConcurrentHashMap<>();
    private static final Map<String, OriginatorType> ELEMENTS_BY_NAME = new ConcurrentHashMap<>();

    public static final OriginatorType 

    AP = new OriginatorType(1, "AP"),
    SWITCH = new OriginatorType(2, "SWITCH"),
    NET = new OriginatorType(3, "NET"),
    
    UNSUPPORTED = new OriginatorType(-1, "UNSUPPORTED");
    
    static {
        //try to load all the subclasses explicitly - to avoid timing issues when items coming from subclasses may be registered some time later, after the parent class is loaded 
        Set<Class<? extends OriginatorType>> subclasses = BaseJsonModel.getReflections().getSubTypesOf(OriginatorType.class);
        for(Class<?> cls: subclasses) {
            try {
                Class.forName(cls.getName());
            } catch (ClassNotFoundException e) {
                LOG.warn("Cannot load class {} : {}", cls.getName(), e);
            }
        }
    }  

    private final int id;
    private final String name;
    
    protected OriginatorType(int id, String name) {
        synchronized(lock) {
            
            LOG.debug("Registering OriginatorType by {} : {}", this.getClass().getSimpleName(), name);

            this.id = id;
            this.name = name;

            ELEMENTS_BY_NAME.values().forEach(s -> {
                if(s.getName().equals(name)) {
                    throw new IllegalStateException("OriginatorType item for "+ name + " is already defined, cannot have more than one of them");
                }                
            });
    
            if(ELEMENTS.containsKey(id)) {
                throw new IllegalStateException("OriginatorType item "+ name + "("+id+") is already defined, cannot have more than one of them");
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
    public static OriginatorType[] values() {
        return new ArrayList<>(ELEMENTS.values()).toArray(new OriginatorType[0]);
    }

    public static OriginatorType getById(int enumId){
        return ELEMENTS.get(enumId);
    }
    
    @JsonCreator
    public static OriginatorType getByName(String value) {
        OriginatorType ret = ELEMENTS_BY_NAME.get(value);
        if (ret == null) {
            ret = UNSUPPORTED;
        }
        
        return ret;
    }


    public static List<OriginatorType> getValues() {
        return new ArrayList<>(ELEMENTS.values());
    }
    
    public static boolean isUnsupported(OriginatorType value) {
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
        if (!(obj instanceof OriginatorType)) {
            return false;
        }
        OriginatorType other = (OriginatorType) obj;
        return id == other.id;
    }   

    @Override
    public String toString() {
        return name;
    }
    
}
