package com.telecominfraproject.wlan.profile.captiveportal.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

/**
 * Valid options for CSS property background-repeat.
 * @author mpreston
 *
 */
public enum BackgroundRepeat {

    repeat_x("repeat-x"),
    repeat_y("repeat-y"),
    repeat("repeat"),
    space("space"),
    round("round"),
    no_repeat("no-repeat"),
    cover("cover"),
    UNSUPPORTED("Unsupported");

    private final String cssValue;
    private static final Map<String, BackgroundRepeat> ELEMENTS = new HashMap<>();

    private BackgroundRepeat(String cssValue) {
        this.cssValue = cssValue;
    }

    public String getCssValue() {
        return this.cssValue;
    }

    public static BackgroundRepeat getByCssValue(String cssValue) {
        if (ELEMENTS.isEmpty()) {
            synchronized (ELEMENTS) {
                if (ELEMENTS.isEmpty()) {
                    //initialize elements map
                    for(BackgroundRepeat item : BackgroundRepeat.values()) {
                        ELEMENTS.put(item.getCssValue(), item);
                    }
                }
            }
        }
        return ELEMENTS.get(cssValue);
    }
    
    @JsonCreator
    public static BackgroundRepeat getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, BackgroundRepeat.class, UNSUPPORTED);
    }
    
    public static boolean isUnsupported(BackgroundRepeat value) {
        return UNSUPPORTED.equals(value);
    }
}
