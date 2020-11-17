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


public class PasspointIPv4AddressType implements EnumWithId {


    private static final Logger LOG = LoggerFactory.getLogger(PasspointIPv4AddressType.class);

    private static Object lock = new Object();
    private static final Map<Integer, PasspointIPv4AddressType> ELEMENTS = new ConcurrentHashMap<>();
    private static final Map<String, PasspointIPv4AddressType> ELEMENTS_BY_NAME = new ConcurrentHashMap<>();

    public static final PasspointIPv4AddressType

    address_type_not_available = new PasspointIPv4AddressType(0, "address_type_not_available"),
            public_IPv4_address_available = new PasspointIPv4AddressType(1, "public_IPv4_address_available"),
            port_restricted_IPv4_address_available = new PasspointIPv4AddressType(2,
                    "port_restricted_IPv4_address_available"),
            single_NATed_private_IPv4_address_available = new PasspointIPv4AddressType(3, "dns_redirection"),
            double_NATed_private_IPv4_address_available = new PasspointIPv4AddressType(4,
                    "single_NATed_private_IPv4_address_available"),
            port_restricted_IPv4_address_and_single_NATed_IPv4_address_available = new PasspointIPv4AddressType(5,
                    "port_restricted_IPv4_address_and_single_NATed_IPv4_address_available"),
            port_restricted_IPv4_address_and_double_NATed_IPv4_address_available = new PasspointIPv4AddressType(6,
                    "port_restricted_IPv4_address_and_double_NATed_IPv4_address_available"),
            availability_of_the_address_type_is_unknown = new PasspointIPv4AddressType(7,
                    "availability_of_the_address_type_is_unknown"),
            UNSUPPORTED = new PasspointIPv4AddressType(-1, "UNSUPPORTED");

    static {
        // try to load all the subclasses explicitly - to avoid timing issues
        // when items coming from subclasses may be registered some time later,
        // after the parent class is loaded
        Set<Class<? extends PasspointIPv4AddressType>> subclasses = BaseJsonModel.getReflections()
                .getSubTypesOf(PasspointIPv4AddressType.class);
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

    protected PasspointIPv4AddressType(int id, String name) {
        synchronized (lock) {

            LOG.debug("Registering PasspointIPv4AddressType by {} : {}", this.getClass().getSimpleName(), name);

            this.id = id;
            this.name = name;
            ELEMENTS_BY_NAME.values().forEach(s -> {
                if (s.getName().equals(name)) {
                    throw new IllegalStateException("PasspointIPv4AddressType item for " + name
                            + " is already defined, cannot have more than one of them");
                }
            });

            if (ELEMENTS.containsKey(id)) {
                throw new IllegalStateException("PasspointIPv4AddressType item " + name + "(" + id
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
    public static PasspointIPv4AddressType[] values() {
        return new ArrayList<>(ELEMENTS.values()).toArray(new PasspointIPv4AddressType[0]);
    }

    public static PasspointIPv4AddressType getById(int enumId) {
        return ELEMENTS.get(enumId);
    }

    @JsonCreator
    public static PasspointIPv4AddressType getByName(String value) {
        PasspointIPv4AddressType ret = ELEMENTS_BY_NAME.get(value);
        if (ret == null) {
            ret = UNSUPPORTED;
        }

        return ret;
    }


    public static List<PasspointIPv4AddressType> getValues() {
        return new ArrayList<>(ELEMENTS.values());
    }

    public static boolean isUnsupported(PasspointIPv4AddressType value) {
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
        if (!(obj instanceof PasspointIPv4AddressType)) {
            return false;
        }
        PasspointIPv4AddressType other = (PasspointIPv4AddressType) obj;
        return id == other.id;
    }

    @Override
    public String toString() {
        return name;
    }


}
