/**
 * 
 */
package com.telecominfraproject.wlan.servicemetrics.models;

import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

/**
 * @author yongli
 *
 */
public enum EthernetLinkState {
    DOWN(0, 0, false), UP1000_FULL_DUPLEX(1, 1000, true), UP1000_HALF_DUPLEX(2, 1000, false), UP100_FULL_DUPLEX(3, 100,
            true), UP100_HALF_DUPLEX(4, 100, false), UP10_FULL_DUPLEX(5, 10, true), UP10_HALF_DUPLEX(6, 10, false),

    UNSUPPORTED(-1, 0, false);

    private static Map<Integer, EthernetLinkState> ID_MAP;

    static {
        ID_MAP = new HashMap<>();
        for (EthernetLinkState e : EthernetLinkState.values()) {
            if (isUnsupported(e)) {
                continue;
            }
            ID_MAP.put(e.getId(), e);
        }
    }

    public static EthernetLinkState getById(int id) {
        EthernetLinkState result = ID_MAP.get(id);
        if (null == result) {
            result = UNSUPPORTED;
        }
        return result;
    }

    @JsonCreator
    public static EthernetLinkState getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, EthernetLinkState.class, UNSUPPORTED);
    }

    public static boolean isUnsupported(EthernetLinkState value) {
        return UNSUPPORTED.equals(value);
    }

    /**
     * Id
     */
    private final int id;

    /**
     * Speed in MB
     */
    private final int speed;

    /**
     * Is full duplex
     */
    private final boolean fullDuplex;

    EthernetLinkState(int id, int speed, boolean fullDuplex) {
        this.id = id;
        this.speed = speed;
        this.fullDuplex = fullDuplex;
    }

    /**
     * @return the id
     */
    public int getId() {
        return this.id;
    }

    /**
     * @return the speed
     */
    public int getSpeed() {
        return this.speed;
    }

    /**
     * @return the fullDuplex
     */
    public boolean isFullDuplex() {
        return this.fullDuplex;
    }
}
