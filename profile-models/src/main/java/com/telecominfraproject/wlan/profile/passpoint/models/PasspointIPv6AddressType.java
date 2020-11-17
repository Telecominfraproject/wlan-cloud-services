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


public class PasspointIPv6AddressType implements EnumWithId {


    private static final Logger LOG = LoggerFactory.getLogger(PasspointIPv6AddressType.class);

    private static Object lock = new Object();
    private static final Map<Integer, PasspointIPv6AddressType> ELEMENTS = new ConcurrentHashMap<>();
    private static final Map<String, PasspointIPv6AddressType> ELEMENTS_BY_NAME = new ConcurrentHashMap<>();

    public static final PasspointIPv6AddressType

    address_type_not_available = new PasspointIPv6AddressType(0, "address_type_not_available"),
            address_type_available = new PasspointIPv6AddressType(1, "address_type_available"),
            availability_of_the_address_type_is_unknown = new PasspointIPv6AddressType(2,
                    "availability_of_the_address_type_is_unknown"),
            reserved = new PasspointIPv6AddressType(3, "reserved"),
            UNSUPPORTED = new PasspointIPv6AddressType(-1, "UNSUPPORTED");

    static {
        // try to load all the subclasses explicitly - to avoid timing issues
        // when items coming from subclasses may be registered some time later,
        // after the parent class is loaded
        Set<Class<? extends PasspointIPv6AddressType>> subclasses = BaseJsonModel.getReflections()
                .getSubTypesOf(PasspointIPv6AddressType.class);
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

    protected PasspointIPv6AddressType(int id, String name) {
        synchronized (lock) {

            LOG.debug("Registering PasspointIPv6AddressType by {} : {}", this.getClass().getSimpleName(), name);

            this.id = id;
            this.name = name;

            ELEMENTS_BY_NAME.values().forEach(s -> {
                if (s.getName().equals(name)) {
                    throw new IllegalStateException("PasspointIPv6AddressType item for " + name
                            + " is already defined, cannot have more than one of them");
                }
            });

            if (ELEMENTS.containsKey(id)) {
                throw new IllegalStateException("PasspointIPv6AddressType item " + name + "(" + id
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
    public static PasspointIPv6AddressType[] values() {
        return new ArrayList<>(ELEMENTS.values()).toArray(new PasspointIPv6AddressType[0]);
    }

    public static PasspointIPv6AddressType getById(int enumId) {
        return ELEMENTS.get(enumId);
    }

    @JsonCreator
    public static PasspointIPv6AddressType getByName(String value) {
        PasspointIPv6AddressType ret = ELEMENTS_BY_NAME.get(value);
        if (ret == null) {
            ret = UNSUPPORTED;
        }

        return ret;
    }


    public static List<PasspointIPv6AddressType> getValues() {
        return new ArrayList<>(ELEMENTS.values());
    }

    public static boolean isUnsupported(PasspointIPv6AddressType value) {
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
        if (!(obj instanceof PasspointIPv6AddressType)) {
            return false;
        }
        PasspointIPv6AddressType other = (PasspointIPv6AddressType) obj;
        return id == other.id;
    }

    @Override
    public String toString() {
        return name;
    }


}
