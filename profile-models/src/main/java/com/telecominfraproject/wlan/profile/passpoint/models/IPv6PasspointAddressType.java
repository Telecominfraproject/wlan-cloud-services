package com.telecominfraproject.wlan.profile.passpoint.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telecominfraproject.wlan.core.model.extensibleenum.EnumWithId;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;


public class IPv6PasspointAddressType implements EnumWithId {


    private static final Logger LOG = LoggerFactory.getLogger(IPv6PasspointAddressType.class);

    private static Object lock = new Object();
    private static final Map<Integer, IPv6PasspointAddressType> ELEMENTS = new ConcurrentHashMap<>();
    private static final Map<String, IPv6PasspointAddressType> ELEMENTS_BY_NAME = new ConcurrentHashMap<>();

    public static final IPv6PasspointAddressType

    address_type_not_available = new IPv6PasspointAddressType(0, "address_type_not_available"),
            address_type_available = new IPv6PasspointAddressType(1, "address_type_available"),
            availability_of_the_address_type_is_unknown = new IPv6PasspointAddressType(2,
                    "availability_of_the_address_type_is_unknown"),
            reserved = new IPv6PasspointAddressType(3, "reserved"),
            UNSUPPORTED = new IPv6PasspointAddressType(-1, "UNSUPPORTED");

    static {
        // try to load all the subclasses explicitly - to avoid timing issues
        // when items coming from subclasses may be registered some time later,
        // after the parent class is loaded
        Set<Class<? extends IPv6PasspointAddressType>> subclasses = BaseJsonModel.getReflections()
                .getSubTypesOf(IPv6PasspointAddressType.class);
        for (Class<?> cls : subclasses) {
            try {
                Class.forName(cls.getName());
            } catch (ClassNotFoundException e) {
                LOG.warn("Cannot load class {} : {}", cls.getName(), e);
            }
        }
    }

    private final int id;
    private final String name;

    protected IPv6PasspointAddressType(int id, String name) {
        synchronized (lock) {

            LOG.debug("Registering IPv6PasspointAddressType by {} : {}", this.getClass().getSimpleName(), name);

            this.id = id;
            this.name = name;

            ELEMENTS_BY_NAME.values().forEach(s -> {
                if (s.getName().equals(name)) {
                    throw new IllegalStateException("IPv6PasspointAddressType item for " + name
                            + " is already defined, cannot have more than one of them");
                }
            });

            if (ELEMENTS.containsKey(id)) {
                throw new IllegalStateException("IPv6PasspointAddressType item " + name + "(" + id
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

    @JsonIgnore
    public String name() {
        return name;
    }

    @JsonIgnore
    public static IPv6PasspointAddressType[] values() {
        return new ArrayList<>(ELEMENTS.values()).toArray(new IPv6PasspointAddressType[0]);
    }

    public static IPv6PasspointAddressType getById(int enumId) {
        return ELEMENTS.get(enumId);
    }

    @JsonCreator
    public static IPv6PasspointAddressType getByName(String value) {
        IPv6PasspointAddressType ret = ELEMENTS_BY_NAME.get(value);
        if (ret == null) {
            ret = UNSUPPORTED;
        }

        return ret;
    }


    public static List<IPv6PasspointAddressType> getValues() {
        return new ArrayList<>(ELEMENTS.values());
    }

    public static boolean isUnsupported(IPv6PasspointAddressType value) {
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
        if (!(obj instanceof IPv6PasspointAddressType)) {
            return false;
        }
        IPv6PasspointAddressType other = (IPv6PasspointAddressType) obj;
        return id == other.id;
    }

    @Override
    public String toString() {
        return name;
    }


}
