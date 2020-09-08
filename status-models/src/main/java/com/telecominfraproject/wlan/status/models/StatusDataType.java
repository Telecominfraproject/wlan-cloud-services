package com.telecominfraproject.wlan.status.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.extensibleenum.EnumWithId;

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
    
    private static Object lock = new Object();
    private static final Map<Integer, StatusDataType> ELEMENTS = new ConcurrentHashMap<>();
    private static final Map<String, StatusDataType> ELEMENTS_BY_NAME = new ConcurrentHashMap<>();

    public static final StatusDataType 
    /**
     * Equipment Administrative status
     */
    EQUIPMENT_ADMIN = new StatusDataType(1, "EQUIPMENT_ADMIN") ,
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
    PROTOCOL = new StatusDataType(4, "PROTOCOL") ,
    /**
     * Firmware upgrade status
     */
    FIRMWARE = new StatusDataType(5, "FIRMWARE") ,
    /**
     * Peer status
     */
    PEERINFO = new StatusDataType(6, "PEERINFO"),
    /**
     * LAN status
     */
    LANINFO = new StatusDataType(7, "LANINFO"),
    /**
     * Neighbouring information status
     */
    NEIGHBOURINGINFO = new StatusDataType(8, "NEIGHBOURINGINFO"),
    
// These are from EquipmentReportType
    OS_PERFORMANCE = new StatusDataType(9, "OS_PERFORMANCE"),
    NEIGHBOUR_SCAN = new StatusDataType(10, "NEIGHBOUR_SCAN"),
    RADIO_UTILIZATION = new StatusDataType(11, "RADIO_UTILIZATION"),
    ACTIVE_BSSIDS = new StatusDataType(12, "ACTIVE_BSSIDS"),
    CLIENT_DETAILS = new StatusDataType(13, "CLIENT_DETAILS"),
    
    CUSTOMER_DASHBOARD = new StatusDataType(14, "CUSTOMER_DASHBOARD"),
    
    EQUIPMENT_DEBUG_SESSION = new StatusDataType(15, "EQUIPMENT_DEBUG_SESSION"),

    UNSUPPORTED  = new StatusDataType(-1, "UNSUPPORTED") ;
    
    private final int id;
    private final String name;
    
    protected StatusDataType(int id, String name) {
        synchronized(lock) {
            this.id = id;
            this.name = name;

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

}
