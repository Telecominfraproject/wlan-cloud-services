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
public enum BackgroundPosition {

    left_top("left top"),
    left_center("left center"),
    left_bottom("left bottom"),
    right_top("right top"),
    right_center("right center"),
    right_bottom("right bottom"),
    center_top("center top"),
    center_center("center center"),
    center_bottom("center bottom"),
    
    UNSUPPORTED("Unsupported");

    private final String cssValue;
    private static final Map<String, BackgroundPosition> ELEMENTS = new HashMap<>();

    private BackgroundPosition(String cssValue) {
        this.cssValue = cssValue;
    }

    public String getCssValue() {
        return this.cssValue;
    }

    public static BackgroundPosition getByCssValue(String cssValue) {
        if (ELEMENTS.isEmpty()) {
            synchronized (ELEMENTS) {
                if (ELEMENTS.isEmpty()) {
                    //initialize elements map
                    for(BackgroundPosition item : BackgroundPosition.values()) {
                        ELEMENTS.put(item.getCssValue(), item);
                    }
                }
            }
        }
        return ELEMENTS.get(cssValue);
    }
    
    @JsonCreator
    public static BackgroundPosition getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, BackgroundPosition.class, UNSUPPORTED);
    }
    
    public static boolean isUnsupported(BackgroundPosition value) {
        return UNSUPPORTED.equals(value);
    }
}
