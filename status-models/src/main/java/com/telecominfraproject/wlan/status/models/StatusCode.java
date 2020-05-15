package com.telecominfraproject.wlan.status.models;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

/**
 * @author dtop
 *
 */
public enum StatusCode {
    normal(0),
    requiresAttention(1),
    error(2),
    /**
     * for example, a feature's been disable and is in none of the states above.
     */
    disabled(3),
    
    UNSUPPORTED(-1); 
    
    private final int id;
    private static final Map<Integer, StatusCode> ELEMENTS = new HashMap<>();

    private StatusCode(int id){
        this.id = id;
    }
    
    public int getId(){
        return this.id;
    }
    
    public static StatusCode getById(int enumId){
        if(ELEMENTS.isEmpty()){
            //initialize elements map
            for(StatusCode met : StatusCode.values()){
                ELEMENTS.put(met.getId(), met);
            }
        }
        
        return ELEMENTS.get(enumId);
    }

    @JsonCreator
    public static StatusCode getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, StatusCode.class, UNSUPPORTED);
    }
    
    public static boolean isUnsupported(StatusCode value) {
        return UNSUPPORTED.equals(value);
    }

    public static boolean hasUnsupported(Set<StatusCode> valueSet) {
        return (valueSet.contains(UNSUPPORTED));
    }
}
