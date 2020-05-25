package com.telecominfraproject.wlan.alarm.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

/**
 * @author ekeddy
 *
 */
public enum AlarmScopeType {
    CLIENT(10),
    EQUIPMENT(20),
    VLAN(30),
    CUSTOMER(100),
    LOCATION(200),
    
    UNSUPPORTED(-1)
    ;
    
    private final int id;
    private static final Map<Integer, AlarmScopeType> ELEMENTS = new HashMap<>();

    private AlarmScopeType(int id){
        this.id = id;
    }
    
    public int getId(){
        return this.id;
    }
    
    public static AlarmScopeType getById(int enumId){
        if(ELEMENTS.isEmpty()){
            //initialize elements map
            for(AlarmScopeType met : AlarmScopeType.values()){
                ELEMENTS.put(met.getId(), met);
            }
        }
        
        return ELEMENTS.get(enumId);
    }
    
    @JsonCreator
    public static AlarmScopeType getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, AlarmScopeType.class, UNSUPPORTED);
    }
    
    public static boolean isUnsupported(AlarmScopeType value) {
        return UNSUPPORTED.equals(value);
    }
}
