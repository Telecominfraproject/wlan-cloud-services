package com.telecominfraproject.wlan.alarm.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

/**
 * @author ekeddy
 *
 */
public enum OriginatorType {

    AP(1),
    SWITCH(2),
    NET(3),
    
    UNSUPPORTED(-1);
    
    private final int id;
    private static final Map<Integer, OriginatorType> ELEMENTS = new HashMap<>();

    private OriginatorType(int id){
        this.id = id;
    }
    
    public int getId(){
        return this.id;
    }
    
    public static OriginatorType getById(int enumId){
        if(ELEMENTS.isEmpty()){
            //initialize elements map
            for(OriginatorType met : OriginatorType.values()){
                ELEMENTS.put(met.getId(), met);
            }
        }
        
        return ELEMENTS.get(enumId);
    }
    
    @JsonCreator
    public static OriginatorType getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, OriginatorType.class, UNSUPPORTED);
    }
    
    public static boolean isUnsupported(OriginatorType value) {
        return UNSUPPORTED.equals(value);
    }
}
