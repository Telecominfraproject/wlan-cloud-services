package com.telecominfraproject.wlan.location.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

public enum LocationType {
    
    COUNTRY(1), SITE(2), BUILDING(3), FLOOR(4), UNSUPPORTED(-1);
    
    private final int id;
    private static final Map<Integer, LocationType> ELEMENTS = new HashMap<>();
    
    private LocationType(int id){
        this.id = id;
    }
    
    public int getId(){
        return this.id;
    }
    
    public static LocationType getById(int enumId){
        if(ELEMENTS.isEmpty()){
            //initialize elements map
            for(LocationType met : LocationType.values()){
                ELEMENTS.put(met.getId(), met);
            }
        }
        
        return ELEMENTS.get(enumId);
    }

    @JsonCreator
    public static LocationType getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, LocationType.class, UNSUPPORTED);
    }
    
    public static boolean isUnsupported(LocationType value) {
        return UNSUPPORTED.equals(value);
    }
}
