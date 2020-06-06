package com.telecominfraproject.wlan.firmware.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

/**
 * @author ekeddy
 *
 */
public enum ValidationMethod {
	
	MD5_CHECKSUM(1),
	
	UNSUPPORTED(-1);
	
    private final int id;
    private static final Map<Integer, ValidationMethod> ELEMENTS = new HashMap<>();
    
    private ValidationMethod(int id){
        this.id = id;
    }
    
    public int getId(){
        return this.id;
    }
    
    public static ValidationMethod getById(int enumId){
        if(ELEMENTS.isEmpty()){
            //initialize elements map
            for(ValidationMethod met : ValidationMethod.values()){
                ELEMENTS.put(met.getId(), met);
            }
        }
        
        return ELEMENTS.get(enumId);
    }
    
    @JsonCreator
    public static ValidationMethod getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, ValidationMethod.class, UNSUPPORTED);
    }
    
    public static boolean isUnsupported(ValidationMethod value) {
        return UNSUPPORTED.equals(value);
    }

}
