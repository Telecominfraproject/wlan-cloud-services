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


public class IPv4PasspointAddressType implements EnumWithId {


    private static final Logger LOG = LoggerFactory.getLogger(IPv4PasspointAddressType.class);

    private static Object lock = new Object();
    private static final Map<Integer, IPv4PasspointAddressType> ELEMENTS = new ConcurrentHashMap<>();
    private static final Map<String, IPv4PasspointAddressType> ELEMENTS_BY_NAME = new ConcurrentHashMap<>();

    public static final IPv4PasspointAddressType

    address_type_not_available = new IPv4PasspointAddressType(0, "address_type_not_available"),
            public_IPv4_address_available = new IPv4PasspointAddressType(1, "public_IPv4_address_available"),
            port_restricted_IPv4_address_available = new IPv4PasspointAddressType(2,
                    "port_restricted_IPv4_address_available"),
            single_NATed_private_IPv4_address_available = new IPv4PasspointAddressType(3, "dns_redirection"),
            double_NATed_private_IPv4_address_available = new IPv4PasspointAddressType(4,
                    "single_NATed_private_IPv4_address_available"),
            port_restricted_IPv4_address_and_single_NATed_IPv4_address_available = new IPv4PasspointAddressType(5,
                    "port_restricted_IPv4_address_and_single_NATed_IPv4_address_available"),
            port_restricted_IPv4_address_and_double_NATed_IPv4_address_available = new IPv4PasspointAddressType(6,
                    "port_restricted_IPv4_address_and_double_NATed_IPv4_address_available"),
            availability_of_the_address_type_is_unknown = new IPv4PasspointAddressType(7,
                    "availability_of_the_address_type_is_unknown"),
            UNSUPPORTED = new IPv4PasspointAddressType(-1, "UNSUPPORTED");

    static {
        // try to load all the subclasses explicitly - to avoid timing issues
        // when items coming from subclasses may be registered some time later,
        // after the parent class is loaded
        Set<Class<? extends IPv4PasspointAddressType>> subclasses = BaseJsonModel.getReflections()
                .getSubTypesOf(IPv4PasspointAddressType.class);
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

    protected IPv4PasspointAddressType(int id, String name) {
        synchronized (lock) {

            LOG.debug("Registering IPv4PasspointAddressType by {} : {}", this.getClass().getSimpleName(), name);

            this.id = id;
            this.name = name;
            ELEMENTS_BY_NAME.values().forEach(s -> {
                if (s.getName().equals(name)) {
                    throw new IllegalStateException("IPv4PasspointAddressType item for " + name
                            + " is already defined, cannot have more than one of them");
                }
            });

            if (ELEMENTS.containsKey(id)) {
                throw new IllegalStateException("IPv4PasspointAddressType item " + name + "(" + id
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
    public static IPv4PasspointAddressType[] values() {
        return new ArrayList<>(ELEMENTS.values()).toArray(new IPv4PasspointAddressType[0]);
    }

    public static IPv4PasspointAddressType getById(int enumId) {
        return ELEMENTS.get(enumId);
    }

    @JsonCreator
    public static IPv4PasspointAddressType getByName(String value) {
        IPv4PasspointAddressType ret = ELEMENTS_BY_NAME.get(value);
        if (ret == null) {
            ret = UNSUPPORTED;
        }

        return ret;
    }


    public static List<IPv4PasspointAddressType> getValues() {
        return new ArrayList<>(ELEMENTS.values());
    }

    public static boolean isUnsupported(IPv4PasspointAddressType value) {
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
        if (!(obj instanceof IPv4PasspointAddressType)) {
            return false;
        }
        IPv4PasspointAddressType other = (IPv4PasspointAddressType) obj;
        return id == other.id;
    }

    @Override
    public String toString() {
        return name;
    }


}
