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


public class PasspointConnectionCapabilitiesStatus implements EnumWithId {


    private static final Logger LOG = LoggerFactory.getLogger(PasspointConnectionCapabilitiesStatus.class);

    private static Object lock = new Object();
    private static final Map<Integer, PasspointConnectionCapabilitiesStatus> ELEMENTS = new ConcurrentHashMap<>();
    private static final Map<String, PasspointConnectionCapabilitiesStatus> ELEMENTS_BY_NAME = new ConcurrentHashMap<>();

    public static final PasspointConnectionCapabilitiesStatus

    closed = new PasspointConnectionCapabilitiesStatus(0, "closed"), open = new PasspointConnectionCapabilitiesStatus(1, "open"),
            unknown = new PasspointConnectionCapabilitiesStatus(2, "unknown"),

            UNSUPPORTED = new PasspointConnectionCapabilitiesStatus(-1, "UNSUPPORTED");

    static {
        // try to load all the subclasses explicitly - to avoid timing issues
        // when items coming from subclasses may be registered some time later,
        // after the parent class is loaded
        Set<Class<? extends PasspointConnectionCapabilitiesStatus>> subclasses = BaseJsonModel.getReflections()
                .getSubTypesOf(PasspointConnectionCapabilitiesStatus.class);
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

    protected PasspointConnectionCapabilitiesStatus(int id, String name) {
        synchronized (lock) {

            LOG.debug("Registering PasspointConnectionCapabilitiesStatus by {} : {}", this.getClass().getSimpleName(), name);

            this.id = id;
            this.name = name;
            ELEMENTS_BY_NAME.values().forEach(s -> {
                if (s.getName().equals(name)) {
                    throw new IllegalStateException("PasspointConnectionCapabilitiesStatus item for " + name
                            + " is already defined, cannot have more than one of them");
                }
            });

            if (ELEMENTS.containsKey(id)) {
                throw new IllegalStateException("PasspointConnectionCapabilitiesStatus item " + name + "(" + id
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
    public static PasspointConnectionCapabilitiesStatus[] values() {
        return new ArrayList<>(ELEMENTS.values()).toArray(new PasspointConnectionCapabilitiesStatus[0]);
    }

    public static PasspointConnectionCapabilitiesStatus getById(int enumId) {
        return ELEMENTS.get(enumId);
    }

    @JsonCreator
    public static PasspointConnectionCapabilitiesStatus getByName(String value) {
        PasspointConnectionCapabilitiesStatus ret = ELEMENTS_BY_NAME.get(value);
        if (ret == null) {
            ret = UNSUPPORTED;
        }

        return ret;
    }


    public static List<PasspointConnectionCapabilitiesStatus> getValues() {
        return new ArrayList<>(ELEMENTS.values());
    }

    public static boolean isUnsupported(PasspointConnectionCapabilitiesStatus value) {
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
        if (!(obj instanceof PasspointConnectionCapabilitiesStatus)) {
            return false;
        }
        PasspointConnectionCapabilitiesStatus other = (PasspointConnectionCapabilitiesStatus) obj;
        return id == other.id;
    }

    @Override
    public String toString() {
        return name;
    }


}
