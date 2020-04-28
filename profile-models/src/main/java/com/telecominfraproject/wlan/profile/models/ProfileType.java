package com.telecominfraproject.wlan.profile.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

public enum ProfileType {

    equipment_ap(1),
    equipment_switch(2), 
    ssid(3), 
    bonjour(4), 
    radius(5), 
    captive_portal(6), 
    UNSUPPORTED   (-1);
    
    private final int id;
    private static final Map<Integer, ProfileType> ELEMENTS = new HashMap<>();
    
    private ProfileType(int id){
        this.id = id;
    }
    
    public int getId(){
        return this.id;
    }
    
    public static ProfileType getById(int enumId){
        if(ELEMENTS.isEmpty()){
            //initialize elements map
            for(ProfileType met : ProfileType.values()){
                ELEMENTS.put(met.getId(), met);
            }
        }
        
        return ELEMENTS.get(enumId);
    }
    
    @JsonCreator
    public static ProfileType getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, ProfileType.class, UNSUPPORTED);
    }

    public static boolean isUnsupported(ProfileType value) {
        return (UNSUPPORTED.equals(value));
    }
}
