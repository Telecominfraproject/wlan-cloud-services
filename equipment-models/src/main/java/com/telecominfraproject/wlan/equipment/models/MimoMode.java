package com.telecominfraproject.wlan.equipment.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

public enum MimoMode {

    none            (0),
    oneByOne            (1),
    twoByTwo            (2),
    threeByThree            (3),
    fourByFour            (4),
    
    UNSUPPORTED (-1);
    
    private final int id;
    private static final Map<Integer, MimoMode> ELEMENTS = new HashMap<>();
    
    private MimoMode(int id){
        this.id = id;
    }
    
    public int getId(){
        return this.id;
    }
    
    public static MimoMode getById(int enumId){
        if(ELEMENTS.isEmpty()){
            //initialize elements map
            for(MimoMode met : MimoMode.values()){
                ELEMENTS.put(met.getId(), met);
            }
        }
        
        return ELEMENTS.get(enumId);
    }

    @JsonCreator
    public static MimoMode getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, MimoMode.class, UNSUPPORTED);
    }
    
    public static boolean isUnsupported(MimoMode value) {
        return UNSUPPORTED.equals(value);
    }
}
