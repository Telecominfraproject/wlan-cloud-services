package com.telecominfraproject.wlan.status.equipment.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

/**
 * @author dtop
 *
 */
public enum EquipmentProtocolState {
    init(0),
    joined(1),
    configuration_received(2),
    ready(3),
    error_when_joining(4),
    error_processing_configuration(5),
    
    UNSUPPORTED(-1);
    
    private final int id;
    private static final Map<Integer, EquipmentProtocolState> ELEMENTS = new HashMap<>();

    private EquipmentProtocolState(int id){
        this.id = id;
    }
    
    public int getId(){
        return this.id;
    }
    
    public static EquipmentProtocolState getById(int enumId){
        if(ELEMENTS.isEmpty()){
            //initialize elements map
            for(EquipmentProtocolState met : EquipmentProtocolState.values()){
                ELEMENTS.put(met.getId(), met);
            }
        }
        
        return ELEMENTS.get(enumId);
    }
    
    @JsonCreator
    public static EquipmentProtocolState getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, EquipmentProtocolState.class, UNSUPPORTED);
    }
    
    public static boolean isUnsupported(EquipmentProtocolState value) {
        return UNSUPPORTED.equals(value);
    }
}
