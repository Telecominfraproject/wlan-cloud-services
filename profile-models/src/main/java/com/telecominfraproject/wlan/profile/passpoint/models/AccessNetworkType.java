package com.telecominfraproject.wlan.profile.passpoint.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telecominfraproject.wlan.core.model.extensibleenum.EnumWithId;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;


public class AccessNetworkType implements EnumWithId {

    private static final Logger LOG = LoggerFactory.getLogger(AccessNetworkType.class);

    private static Object lock = new Object();
    private static final Map<Integer, AccessNetworkType> ELEMENTS = new ConcurrentHashMap<>();
    private static final Map<String, AccessNetworkType> ELEMENTS_BY_NAME = new ConcurrentHashMap<>();

    public static final AccessNetworkType

    private_network = new AccessNetworkType(0, "private_network"),
            private_network_guest_access = new AccessNetworkType(1, "private_network_guest_access"),
            changeable_public_network = new AccessNetworkType(2, "changeable_public_network"),
            free_public_network = new AccessNetworkType(3, "free_public_network"),
            person_device_network = new AccessNetworkType(4, "personal_device_network"),
            emergency_services_only_network = new AccessNetworkType(5, "emergency_services_only_network"),
            test_or_experimental = new AccessNetworkType(14, "test_or_experimental"),
            wildcard = new AccessNetworkType(15, "wildcard"),
            UNSUPPORTED = new AccessNetworkType(-1, "UNSUPPORTED");

    static {
        // try to load all the subclasses explicitly - to avoid timing issues
        // when items coming from subclasses may be registered some time later,
        // after the parent class is loaded
        Set<Class<? extends AccessNetworkType>> subclasses = BaseJsonModel.getReflections()
                .getSubTypesOf(AccessNetworkType.class);
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

    protected AccessNetworkType(int id, String name) {
        synchronized (lock) {

            LOG.debug("Registering AccessNetworkType by {} : {}", this.getClass().getSimpleName(), name);

            this.id = id;
            this.name = name;

            ELEMENTS_BY_NAME.values().forEach(s -> {
                if (s.getName().equals(name)) {
                    throw new IllegalStateException("AccessNetworkType item for " + name
                            + " is already defined, cannot have more than one of them");
                }
            });

            if (ELEMENTS.containsKey(id)) {
                throw new IllegalStateException("AccessNetworkType item " + name + "(" + id
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
    public static AccessNetworkType[] values() {
        return new ArrayList<>(ELEMENTS.values()).toArray(new AccessNetworkType[0]);
    }

    public static AccessNetworkType getById(int enumId) {
        return ELEMENTS.get(enumId);
    }

    @JsonCreator
    public static AccessNetworkType getByName(String value) {
        AccessNetworkType ret = ELEMENTS_BY_NAME.get(value);
        if (ret == null) {
            ret = UNSUPPORTED;
        }

        return ret;
    }


    public static List<AccessNetworkType> getValues() {
        return new ArrayList<>(ELEMENTS.values());
    }

    public static boolean isUnsupported(AccessNetworkType value) {
        return (UNSUPPORTED.equals(value));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AccessNetworkType)) {
            return false;
        }
        AccessNetworkType other = (AccessNetworkType) obj;
        return id == other.id && Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return name;
    }


}
