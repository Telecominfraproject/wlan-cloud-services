package com.telecominfraproject.wlan.profile.models;

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
 * All available profile types that can be handled by the CloudSDK. 
 * <br>This enumeration-like class can be extended by vendors - new elements can be defined by extending this class like so:
 * <br>
 * <pre>
 * public class VendorExtendedProfileType extends ProfileType {
 *    
 *    public static final ProfileType 
 *    VENDOR_PROFILE_A = new VendorExtendedProfileType(100, "VENDOR_PROFILE_A") ,
 *    VENDOR_PROFILE_B = new VendorExtendedProfileType(101, "VENDOR_PROFILE_B")
 *    ;
 *
 *    private VendorExtendedProfileType(int id, String name) {
 *        super(id, name);
 *    }
 *
 * }
 * </pre>
 * @see com.telecominfraproject.wlan.core.model.extensibleenum.EnumWithId
 * @see com.telecominfraproject.wlan.profile.models.vendorextensions.TestVendorExtendedProfileModel
 * <br>
 * @author dtop
 *
 */
public class ProfileType implements EnumWithId {

    private static final Logger LOG = LoggerFactory.getLogger(ProfileType.class);

    private static Object lock = new Object();
    private static final Map<Integer, ProfileType> ELEMENTS = new ConcurrentHashMap<>();
    private static final Map<String, ProfileType> ELEMENTS_BY_NAME = new ConcurrentHashMap<>();

    public static final ProfileType 

    equipment_ap = new ProfileType(1, "equipment_ap"),
    equipment_switch = new ProfileType(2, "equipment_switch"), 
    ssid = new ProfileType(3, "ssid"), 
    bonjour = new ProfileType(4, "bonjour"), 
    radius = new ProfileType(5, "radius"), 
    captive_portal = new ProfileType(6, "captive_portal"),
    mesh = new ProfileType(7, "mesh"),
    service_metrics_collection_config = new ProfileType(8,"service_metrics_collection_config"),
    rf = new ProfileType(9, "rf"),
    passpoint = new ProfileType(10,"passpoint"),
    passpoint_operator = new ProfileType(11, "passpoint_operator"),
    passpoint_venue = new ProfileType(12,"passpoint_venue"),
    passpoint_osu_id_provider = new ProfileType(13,"passpoint_osu_id_provider"),
    ap_event_rate = new ProfileType(14,"ap_event_rate"),
    UNSUPPORTED = new ProfileType(-1, "UNSUPPORTED");
    
    static {
        //try to load all the subclasses explicitly - to avoid timing issues when items coming from subclasses may be registered some time later, after the parent class is loaded 
        Set<Class<? extends ProfileType>> subclasses = BaseJsonModel.getReflections().getSubTypesOf(ProfileType.class);
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
    
    protected ProfileType(int id, String name) {
        synchronized(lock) {
            
            LOG.debug("Registering ProfileType by {} : {}", this.getClass().getSimpleName(), name);

            this.id = id;
            this.name = name;

            ELEMENTS_BY_NAME.values().forEach(s -> {
                if(s.getName().equals(name)) {
                    throw new IllegalStateException("ProfileType item for "+ name + " is already defined, cannot have more than one of them");
                }                
            });
    
            if(ELEMENTS.containsKey(id)) {
                throw new IllegalStateException("ProfileType item "+ name + "("+id+") is already defined, cannot have more than one of them");
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
    public static ProfileType[] values() {
        return new ArrayList<>(ELEMENTS.values()).toArray(new ProfileType[0]);
    }

    public static ProfileType getById(int enumId){
        return ELEMENTS.get(enumId);
    }
    
    @JsonCreator
    public static ProfileType getByName(String value) {
        ProfileType ret = ELEMENTS_BY_NAME.get(value);
        if (ret == null) {
            ret = UNSUPPORTED;
        }
        
        return ret;
    }


    public static List<ProfileType> getValues() {
        return new ArrayList<>(ELEMENTS.values());
    }
    
    public static boolean isUnsupported(ProfileType value) {
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
        if (!(obj instanceof ProfileType)) {
            return false;
        }
        ProfileType other = (ProfileType) obj;
        return id == other.id;
    }   

    @Override
    public String toString() {
        return name;
    }
    
}
