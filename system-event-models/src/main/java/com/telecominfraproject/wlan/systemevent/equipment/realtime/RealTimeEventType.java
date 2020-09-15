package com.telecominfraproject.wlan.systemevent.equipment.realtime;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.extensibleenum.EnumWithId;

public class RealTimeEventType implements EnumWithId {


    private static Object lock = new Object();
    private static final Map<Integer, RealTimeEventType> ELEMENTS = new ConcurrentHashMap<>();
    private static final Map<String, RealTimeEventType> ELEMENTS_BY_NAME = new ConcurrentHashMap<>();

    public static final RealTimeEventType RRM_Cell_Size = new RealTimeEventType(1, "RRM_Cell_Size"),
            STA_Client_Auth = new RealTimeEventType(2, "STA_Client_Auth"),
            STA_Client_Assoc = new RealTimeEventType(3, "STA_Client_Assoc"),
            STA_Client_Disconnect = new RealTimeEventType(4, "STA_Client_Disconnect"),
            STA_Client_Timeout = new RealTimeEventType(5, "STA_Client_Auth"),
            STA_Client_IP = new RealTimeEventType(6, "STA_Client_IP"),
            STA_Client_FirstData = new RealTimeEventType(7, "STA_Client_FirstData"),
            APC_Election_event = new RealTimeEventType(8, "STA_Client_Auth"),
            EAPOL_Event = new RealTimeEventType(9, "EAPOL_Event"),
            Radio_Reset = new RealTimeEventType(10, "Radio_Reset"),
            RRM_Best_Ap = new RealTimeEventType(11, "RRM_Best_Ap"),
            Channel_Change = new RealTimeEventType(12, "Channel_Change"),
            Channel_Hop = new RealTimeEventType(13, "STA_Client_Auth"),
            STA_Client_Id = new RealTimeEventType(14, "STA_Client_Id"),
            CaptivePortalAuthEvent = new RealTimeEventType(15, "CaptivePortalAuthEvent"),
            DhcpFingerPrint = new RealTimeEventType(16, "DhcpFingerPrint"),
            SipCallStart = new RealTimeEventType(17, "SipCallStart"),
            SipCallStop = new RealTimeEventType(18, "SipCallStop"),
            SipCallReport = new RealTimeEventType(19, "SipCallReport"),
            ClientPortEnabled = new RealTimeEventType(20, "ClientPortEnabled"),
            STA_Client_Failure = new RealTimeEventType(21, "STA_Client_Failure"),
            Radio_Driver = new RealTimeEventType(22, "Radio_Driver"),
            User_Agent = new RealTimeEventType(23, "User_Agent"),
            ClientConnectedSuccess = new RealTimeEventType(24, "ClientConnectedSuccess"),
            WDSLinkStateEvent = new RealTimeEventType(25, "WDSLinkStateEvent"),
            VideoStreamStart = new RealTimeEventType(26, "VideoStreamStart"),
            VideoStreamDebugStart = new RealTimeEventType(27, "VideoStreamDebugStart"),
            VideoStreamStop = new RealTimeEventType(28, "VideoStreamStop"),
            UNSUPPORTED = new RealTimeEventType(-1, "UNSUPPORTED");

    private final int id;
    private final String name;

    protected RealTimeEventType(int id, String name) {
        synchronized (lock) {
            this.id = id;
            this.name = name;

            ELEMENTS_BY_NAME.values().forEach(s -> {
                if (s.getName().equals(name)) {
                    throw new IllegalStateException("RealTimeEventType item for " + name
                            + " is already defined, cannot have more than one of them");
                }
            });

            if (ELEMENTS.containsKey(id)) {
                throw new IllegalStateException("RealTimeEventType item " + name + "(" + id
                        + ") is already defined, cannot have more than one of them");
            }

            ELEMENTS.put(id, this);
            ELEMENTS_BY_NAME.put(name, this);
        }
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    public static RealTimeEventType getById(int enumId) {
        return ELEMENTS.get(enumId);
    }

    @JsonCreator
    public static RealTimeEventType getByName(String value) {
        RealTimeEventType ret = ELEMENTS_BY_NAME.get(value);
        if (ret == null) {
            ret = UNSUPPORTED;
        }

        return ret;
    }


    public static List<RealTimeEventType> getValues() {
        return new ArrayList<>(ELEMENTS.values());
    }

    public static boolean isUnsupported(RealTimeEventType value) {
        return (UNSUPPORTED.equals(value));
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RealTimeEventType)) {
            return false;
        }
        RealTimeEventType other = (RealTimeEventType) obj;
        return id == other.id;
    }

    @Override
    public String toString() {
        return name;
    }

}

