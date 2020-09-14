package com.telecominfraproject.wlan.systemevent.equipment.realtime;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

public enum StreamingVideoType 
{    
    UNKNOWN(0),
    NETFLIX(1),
    YOUTUBE(2),
    PLEX(3),
    
    UNSUPPORTED(-1);
    
    private int id;
    
    private StreamingVideoType(int id)
    {
        this.id = id;
    }
    
    public int getId()
    {
        return id;
    }
    
    @JsonCreator
    public static StreamingVideoType getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, StreamingVideoType.class, UNSUPPORTED);
    }

    public static StreamingVideoType getById(int id)
    {
        for(StreamingVideoType type : StreamingVideoType.values())
        {
            if(type.getId() == id)
            {
                return type;
            }
        }
        
        throw new IllegalArgumentException("Could not find a StreamingVideoType with id : " + id);
    }
    
    public static boolean isUnsupported(StreamingVideoType value) {
        return UNSUPPORTED.equals(value);
    }


}
