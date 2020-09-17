package com.telecominfraproject.wlan.client.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.extensibleenum.EnumWithId;

public class ClientType implements EnumWithId {


    private static Object lock = new Object();
    private static final Map<Integer, ClientType> ELEMENTS = new ConcurrentHashMap<>();
    private static final Map<String, ClientType> ELEMENTS_BY_NAME = new ConcurrentHashMap<>();

    public static final ClientType Misc = new ClientType(0, "Miscellaneous"), Mobile = new ClientType(1, "Mobile"),
            PersonalComputer = new ClientType(2, "PersonalComputer"), Printer = new ClientType(3, "Printer"),
            Video = new ClientType(4, "Video"), Game = new ClientType(5, "Game"), VoIP = new ClientType(6, "VoIP"),
            Monitoring = new ClientType(7, "Monitoring"), Max = new ClientType(8, "Max"),
            UNSUPPORTED = new ClientType(-1, "UNSUPPORTED");

    private final int id;
    private final String name;

    protected ClientType(int id, String name) {
        synchronized (lock) {
            this.id = id;
            this.name = name;

            ELEMENTS_BY_NAME.values().forEach(s -> {
                if (s.getName().equals(name)) {
                    throw new IllegalStateException(
                            "ClientType item for " + name + " is already defined, cannot have more than one of them");
                }
            });

            if (ELEMENTS.containsKey(id)) {
                throw new IllegalStateException("ClientType item " + name + "(" + id
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

    public static ClientType getById(int enumId) {
        return ELEMENTS.get(enumId);
    }

    @JsonCreator
    public static ClientType getByName(String value) {
        ClientType ret = ELEMENTS_BY_NAME.get(value);
        if (ret == null) {
            ret = UNSUPPORTED;
        }

        return ret;
    }


    public static List<ClientType> getValues() {
        return new ArrayList<>(ELEMENTS.values());
    }

    public static boolean isUnsupported(ClientType value) {
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
        if (!(obj instanceof ClientType)) {
            return false;
        }
        ClientType other = (ClientType) obj;
        return id == other.id;
    }

    @Override
    public String toString() {
        return name;
    }

}