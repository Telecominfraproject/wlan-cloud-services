package com.telecominfraproject.wlan.status.models;

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
 * All available status traits that can be handled by the CloudSDK. 
 * Status traits are specific features/bahaviours associated with certain status data types. 
 * They are used together with StatusDataType enum to determine which data types are affected by a certain feature.
 * 
 * <br>This enumeration-like class can be extended by vendors - new elements can be defined by extending this class like so:
 * <br>
 * <pre>
 * public class VendorExtendedStatusTrait extends StatusTrait {
 *    
 *    public static final StatusTrait 
 *    VENDOR_STATUS_TRAIT_A = new VendorExtendedStatusTrait(100, "VENDOR_STATUS_TRAIT_A") ,
 *    VENDOR_STATUS_TRAIT_B = new VendorExtendedStatusTrait(101, "VENDOR_STATUS_TRAIT_B")
 *    ;
 *
 *    private VendorExtendedStatusTrait(int id, String name) {
 *        super(id, name);
 *    }
 *
 * }
 * </pre>
 * @see com.telecominfraproject.wlan.core.model.extensibleenum.EnumWithId
 * @see com.telecominfraproject.wlan.status.models.vendorextensions.TestVendorExtendedStatusModel
 * <br>
 * @author dtop
 *
 */
public class StatusTrait implements EnumWithId {
    
    private static final Logger LOG = LoggerFactory.getLogger(StatusTrait.class);

    private static Object lock = new Object();
    private static final Map<Integer, StatusTrait> ELEMENTS = new ConcurrentHashMap<>();
    private static final Map<String, StatusTrait> ELEMENTS_BY_NAME = new ConcurrentHashMap<>();

    public static final StatusTrait 
    DeleteOnEquipmentDisconnect = new StatusTrait(1, "DeleteOnEquipmentDisconnect") ,

    UNSUPPORTED  = new StatusTrait(-1, "UNSUPPORTED") ;
    
    static {
        //try to load all the subclasses explicitly - to avoid timing issues when items coming from subclasses may be registered some time later, after the parent class is loaded 
        Set<Class<? extends StatusTrait>> subclasses = BaseJsonModel.getReflections().getSubTypesOf(StatusTrait.class);
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
    
    protected StatusTrait(int id, String name) {
        synchronized(lock) {
            
            LOG.debug("Registering StatusTrait by {} : {}", this.getClass().getSimpleName(), name);

            this.id = id;
            this.name = name;

            ELEMENTS_BY_NAME.values().forEach(s -> {
                if(s.getName().equals(name)) {
                    throw new IllegalStateException("StatusTrait item for "+ name + " is already defined, cannot have more than one of them");
                }                
            });
    
            if(ELEMENTS.containsKey(id)) {
                throw new IllegalStateException("StatusTrait item "+ name + "("+id+") is already defined, cannot have more than one of them");
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
    public static StatusTrait[] values() {
        return new ArrayList<>(ELEMENTS.values()).toArray(new StatusTrait[0]);
    }

    public static StatusTrait getById(int enumId){
        return ELEMENTS.get(enumId);
    }
    
    @JsonCreator
    public static StatusTrait getByName(String value) {
        StatusTrait ret = ELEMENTS_BY_NAME.get(value);
        if (ret == null) {
            ret = UNSUPPORTED;
        }
        
        return ret;
    }


    public static List<StatusTrait> getValues() {
        return new ArrayList<>(ELEMENTS.values());
    }
    
    public static boolean isUnsupported(StatusTrait value) {
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
        if (!(obj instanceof StatusTrait)) {
            return false;
        }
        StatusTrait other = (StatusTrait) obj;
        return id == other.id;
    }   

    @Override
    public String toString() {
        return name;
    }

}
