package com.telecominfraproject.wlan.status.models;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
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
 * All available status data types that can be handled by the CloudSDK. 
 * <br>This enumeration-like class can be extended by vendors - new elements can be defined by extending this class like so:
 * <br>
 * <pre>
 * public class VendorExtendedStatusDataType extends StatusDataType {
 *    
 *    public static final StatusDataType 
 *    VENDOR_STATUS_A = new VendorExtendedStatusDataType(100, "VENDOR_STATUS_A") ,
 *    VENDOR_STATUS_B = new VendorExtendedStatusDataType(101, "VENDOR_STATUS_B")
 *    ;
 *
 *    private VendorExtendedStatusDataType(int id, String name) {
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
public class StatusDataType implements EnumWithId {
    
    private static final Logger LOG = LoggerFactory.getLogger(StatusDataType.class);

    private static Object lock = new Object();
    private static final Map<Integer, StatusDataType> ELEMENTS = new ConcurrentHashMap<>();
    private static final Map<String, StatusDataType> ELEMENTS_BY_NAME = new ConcurrentHashMap<>();

    public static final StatusDataType 
    /**
     * Equipment Administrative status
     */
    EQUIPMENT_ADMIN = new StatusDataType(1, "EQUIPMENT_ADMIN", Set.of(StatusTrait.DeleteOnEquipmentDisconnect)) ,
    /**
     * Network Administrative status
     */
    NETWORK_ADMIN = new StatusDataType(2, "NETWORK_ADMIN") ,
    /**
     * Network Aggregate status
     */
    NETWORK_AGGREGATE = new StatusDataType(3, "NETWORK_AGGREGATE") ,
    /**
     * Protocol status
     */
    PROTOCOL = new StatusDataType(4, "PROTOCOL", Set.of(StatusTrait.DeleteOnEquipmentDisconnect)) ,
    /**
     * Firmware upgrade status
     */
    FIRMWARE = new StatusDataType(5, "FIRMWARE") ,
    /**
     * Peer status
     */
    PEERINFO = new StatusDataType(6, "PEERINFO", Set.of(StatusTrait.DeleteOnEquipmentDisconnect)),
    /**
     * LAN status
     */
    LANINFO = new StatusDataType(7, "LANINFO", Set.of(StatusTrait.DeleteOnEquipmentDisconnect)),
    /**
     * Neighbouring information status
     */
    NEIGHBOURINGINFO = new StatusDataType(8, "NEIGHBOURINGINFO"),
    
// These are from EquipmentReportType
    OS_PERFORMANCE = new StatusDataType(9, "OS_PERFORMANCE", Set.of(StatusTrait.DeleteOnEquipmentDisconnect)),
    NEIGHBOUR_SCAN = new StatusDataType(10, "NEIGHBOUR_SCAN"),
    RADIO_UTILIZATION = new StatusDataType(11, "RADIO_UTILIZATION", Set.of(StatusTrait.DeleteOnEquipmentDisconnect)),
    ACTIVE_BSSIDS = new StatusDataType(12, "ACTIVE_BSSIDS", Set.of(StatusTrait.DeleteOnEquipmentDisconnect)),
    CLIENT_DETAILS = new StatusDataType(13, "CLIENT_DETAILS", Set.of(StatusTrait.DeleteOnEquipmentDisconnect)),
    
    CUSTOMER_DASHBOARD = new StatusDataType(14, "CUSTOMER_DASHBOARD"),
    
    EQUIPMENT_DEBUG_SESSION = new StatusDataType(15, "EQUIPMENT_DEBUG_SESSION"),
    
    RADIO_CHANNEL = new StatusDataType(16, "RADIO_CHANNEL", Set.of(StatusTrait.DeleteOnEquipmentDisconnect)),
    
    /*
     * Manufacturer identity data for Equipment
     * 
     */    
    EQUIPMENT_MANUFACTURER_DATA = new StatusDataType(17, "EQUIPMENT_MANUFACTURER_DATA", Set.of(StatusTrait.DeleteOnEquipmentDisconnect)),

    UNSUPPORTED  = new StatusDataType(-1, "UNSUPPORTED") ;
    
    static {
        //try to load all the subclasses explicitly - to avoid timing issues when items coming from subclasses may be registered some time later, after the parent class is loaded 
        Set<Class<? extends StatusDataType>> subclasses = BaseJsonModel.getReflections().getSubTypesOf(StatusDataType.class);
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
    private final Set<StatusTrait> traits = new HashSet<>();

    protected StatusDataType(int id, String name) {
        this(id, name, null);
    }

    protected StatusDataType(int id, String name, Set<StatusTrait> traits) {
        synchronized(lock) {
            
            LOG.debug("Registering StatusDataType by {} : {}", this.getClass().getSimpleName(), name);

            this.id = id;
            this.name = name;
            
            if(traits!=null) {
                this.traits.addAll(traits);
            }

            ELEMENTS_BY_NAME.values().forEach(s -> {
                if(s.getName().equals(name)) {
                    throw new IllegalStateException("StatusDataType item for "+ name + " is already defined, cannot have more than one of them");
                }                
            });
    
            if(ELEMENTS.containsKey(id)) {
                throw new IllegalStateException("StatusDataType item "+ name + "("+id+") is already defined, cannot have more than one of them");
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
    public static StatusDataType[] values() {
        return new ArrayList<>(ELEMENTS.values()).toArray(new StatusDataType[0]);
    }

    public static StatusDataType getById(int enumId){
        return ELEMENTS.get(enumId);
    }
    
    @JsonCreator
    public static StatusDataType getByName(String value) {
        StatusDataType ret = ELEMENTS_BY_NAME.get(value);
        if (ret == null) {
            ret = UNSUPPORTED;
        }
        
        return ret;
    }


    public static List<StatusDataType> getValues() {
        return new ArrayList<>(ELEMENTS.values());
    }
    
    public static boolean isUnsupported(StatusDataType value) {
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
        if (!(obj instanceof StatusDataType)) {
            return false;
        }
        StatusDataType other = (StatusDataType) obj;
        return id == other.id;
    }   

    @Override
    public String toString() {
        return name;
    }

    public boolean supportsTrait(StatusTrait incomingTrait) {
        return incomingTrait==null || this.traits.contains(incomingTrait);
    }
    
    public boolean supportsTraits(Collection<StatusTrait> incomingTraits) {
        return incomingTraits==null || this.traits.containsAll(incomingTraits);
    }
    
    public static Set<StatusDataType> getByTraits(Collection<StatusTrait> incomingTraits){
        Set<StatusDataType> ret = new HashSet<>();
        ELEMENTS.values().forEach(v -> { if(v.supportsTraits(incomingTraits)) {ret.add(v);} });
        return ret;
    }

}
