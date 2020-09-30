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


public class ConnectionCapabilitiesStatus implements EnumWithId {


    private static final Logger LOG = LoggerFactory.getLogger(ConnectionCapabilitiesStatus.class);

    private static Object lock = new Object();
    private static final Map<Integer, ConnectionCapabilitiesStatus> ELEMENTS = new ConcurrentHashMap<>();
    private static final Map<String, ConnectionCapabilitiesStatus> ELEMENTS_BY_NAME = new ConcurrentHashMap<>();

    public static final ConnectionCapabilitiesStatus

    closed = new ConnectionCapabilitiesStatus(0, "closed"), open = new ConnectionCapabilitiesStatus(1, "open"),
            unknown = new ConnectionCapabilitiesStatus(2, "unknown"),

            UNSUPPORTED = new ConnectionCapabilitiesStatus(-1, "UNSUPPORTED");

    static {
        // try to load all the subclasses explicitly - to avoid timing issues
        // when items coming from subclasses may be registered some time later,
        // after the parent class is loaded
        Set<Class<? extends ConnectionCapabilitiesStatus>> subclasses = BaseJsonModel.getReflections()
                .getSubTypesOf(ConnectionCapabilitiesStatus.class);
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

    protected ConnectionCapabilitiesStatus(int id, String name) {
        synchronized (lock) {

            LOG.debug("Registering ConnectionCapabilitiesStatus by {} : {}", this.getClass().getSimpleName(), name);

            this.id = id;
            this.name = name;
            ELEMENTS_BY_NAME.values().forEach(s -> {
                if (s.getName().equals(name)) {
                    throw new IllegalStateException("ConnectionCapabilitiesStatus item for " + name
                            + " is already defined, cannot have more than one of them");
                }
            });

            if (ELEMENTS.containsKey(id)) {
                throw new IllegalStateException("ConnectionCapabilitiesStatus item " + name + "(" + id
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
    public static ConnectionCapabilitiesStatus[] values() {
        return new ArrayList<>(ELEMENTS.values()).toArray(new ConnectionCapabilitiesStatus[0]);
    }

    public static ConnectionCapabilitiesStatus getById(int enumId) {
        return ELEMENTS.get(enumId);
    }

    @JsonCreator
    public static ConnectionCapabilitiesStatus getByName(String value) {
        ConnectionCapabilitiesStatus ret = ELEMENTS_BY_NAME.get(value);
        if (ret == null) {
            ret = UNSUPPORTED;
        }

        return ret;
    }


    public static List<ConnectionCapabilitiesStatus> getValues() {
        return new ArrayList<>(ELEMENTS.values());
    }

    public static boolean isUnsupported(ConnectionCapabilitiesStatus value) {
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
        if (!(obj instanceof ConnectionCapabilitiesStatus)) {
            return false;
        }
        ConnectionCapabilitiesStatus other = (ConnectionCapabilitiesStatus) obj;
        return id == other.id;
    }

    @Override
    public String toString() {
        return name;
    }


}
