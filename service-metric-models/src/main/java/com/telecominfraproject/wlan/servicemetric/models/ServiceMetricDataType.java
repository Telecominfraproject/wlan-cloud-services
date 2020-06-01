package com.telecominfraproject.wlan.servicemetric.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

public enum ServiceMetricDataType {

    ApNode(1),
    ApSsid(2),
    Client(3),
    Channel(4),
    Neighbour(5),
    QoE(6),
    ClientQoE(7),

    UNSUPPORTED   (-1);
    
    private final int id;
    private static final Map<Integer, ServiceMetricDataType> ELEMENTS = new HashMap<>();
    
    private ServiceMetricDataType(int id){
        this.id = id;
    }
    
    public int getId(){
        return this.id;
    }
    
    public static ServiceMetricDataType getById(int enumId){
        if(ELEMENTS.isEmpty()){
            //initialize elements map
            for(ServiceMetricDataType met : ServiceMetricDataType.values()){
                ELEMENTS.put(met.getId(), met);
            }
        }
        
        return ELEMENTS.get(enumId);
    }
    
    @JsonCreator
    public static ServiceMetricDataType getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, ServiceMetricDataType.class, UNSUPPORTED);
    }

    public static boolean isUnsupported(ServiceMetricDataType value) {
        return (UNSUPPORTED.equals(value));
    }	

}
