package com.telecominfraproject.wlan.profile.models.common;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

/**
 * TODO: this may need to be moved into a separate file management service
 */
public enum FileType 
{
    PNG(".png"),
    JPG(".jpg"),
    PROTOBUF(".pb"),
    TEXT(".txt");
    
    private final String extension;
    
    private FileType(String extension)
    {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }
    
    @JsonCreator
    public static FileType getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, FileType.class, null);
    }
    
    public static boolean isUnsupported(FileType value) {
        return null == value;
    }
}
