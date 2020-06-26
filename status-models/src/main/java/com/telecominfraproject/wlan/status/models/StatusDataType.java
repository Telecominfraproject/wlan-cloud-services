package com.telecominfraproject.wlan.status.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

public enum StatusDataType {
    /**
     * Equipment Administrative status
     */
    EQUIPMENT_ADMIN(1),
    /**
     * Network Administrative status
     */
    NETWORK_ADMIN(2),
    /**
     * Network Aggregate status
     */
    NETWORK_AGGREGATE(3),
    /**
     * Protocol status
     */
    PROTOCOL(4),
    /**
     * Firmware upgrade status
     */
    FIRMWARE(5),
    /**
     * Peer status
     */
    PEERINFO(6),
    /**
     * LAN status
     */
    LANINFO(7),
    /**
     * Neighbouring information status
     */
    NEIGHBOURINGINFO(8)
    ,
// These are from EquipmentReportType
	OS_PERFORMANCE(9),
	NEIGHBOUR_SCAN(10),
	RADIO_UTILIZATION(11),
	ACTIVE_BSSIDS(12),
	CLIENT_DETAILS(13),
	
	CUSTOMER_DASHBOARD(14),

    UNSUPPORTED   (-1);
    
    private final int id;
    private static final Map<Integer, StatusDataType> ELEMENTS = new HashMap<>();
    
    private StatusDataType(int id){
        this.id = id;
    }
    
    public int getId(){
        return this.id;
    }
    
    public static StatusDataType getById(int enumId){
        if(ELEMENTS.isEmpty()){
            //initialize elements map
            for(StatusDataType met : StatusDataType.values()){
                ELEMENTS.put(met.getId(), met);
            }
        }
        
        return ELEMENTS.get(enumId);
    }
    
    @JsonCreator
    public static StatusDataType getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, StatusDataType.class, UNSUPPORTED);
    }

    public static boolean isUnsupported(StatusDataType value) {
        return (UNSUPPORTED.equals(value));
    }	
	
}
