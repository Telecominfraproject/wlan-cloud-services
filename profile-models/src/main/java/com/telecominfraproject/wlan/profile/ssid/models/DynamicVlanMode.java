package com.telecominfraproject.wlan.profile.ssid.models;

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

public class DynamicVlanMode implements EnumWithId {

    private static final Logger LOG = LoggerFactory.getLogger(DynamicVlanMode.class);

    private static Object lock = new Object();
    private static final Map<Integer, DynamicVlanMode> ELEMENTS = new ConcurrentHashMap<>();
    private static final Map<String, DynamicVlanMode> ELEMENTS_BY_NAME = new ConcurrentHashMap<>();

    public static final DynamicVlanMode

    disabled = new DynamicVlanMode(0, "disabled"), enabled = new DynamicVlanMode(1, "enabled"),
            enabled_reject_if_no_radius_dynamic_vlan = new DynamicVlanMode(2,
                    "enabled_reject_if_no_radius_dynamic_vlan"),
            UNSUPPORTED = new DynamicVlanMode(-1, "UNSUPPORTED");

    static {
        // try to load all the subclasses explicitly - to avoid timing issues
        // when items coming from subclasses may be registered some time later,
        // after the parent class is loaded
        Set<Class<? extends DynamicVlanMode>> subclasses = BaseJsonModel.getReflections()
                .getSubTypesOf(DynamicVlanMode.class);
        for (Class<?> cls : subclasses) {
            try {
                Class.forName(cls.getName());
            } catch (ClassNotFoundException e) {
                LOG.warn("Cannot load class {} : {}", cls.getName(), e);
            }
        }
    }

    public static DynamicVlanMode getById(int enumId) {
        return ELEMENTS.get(enumId);
    }

    @JsonCreator
    public static DynamicVlanMode getByName(String value) {
        DynamicVlanMode ret = ELEMENTS_BY_NAME.get(value);
        if (ret == null) {
            ret = UNSUPPORTED;
        }

        return ret;
    }

    public static List<DynamicVlanMode> getValues() {
        return new ArrayList<>(ELEMENTS.values());
    }

    public static boolean isUnsupported(DynamicVlanMode value) {
        return (UNSUPPORTED.equals(value));
    }

    @JsonIgnore
    public static DynamicVlanMode[] values() {
        return new ArrayList<>(ELEMENTS.values()).toArray(new DynamicVlanMode[0]);
    }

    private final int id;

    private final String name;

    protected DynamicVlanMode(int id, String name) {
        synchronized (lock) {

            LOG.debug("Registering DynamicVlanMode by {} : {}", this.getClass().getSimpleName(), name);

            this.id = id;
            this.name = name;

            ELEMENTS_BY_NAME.values().forEach(s -> {
                if (s.getName().equals(name)) {
                    throw new IllegalStateException("DynamicVlanMode item for " + name
                            + " is already defined, cannot have more than one of them");
                }
            });

            if (ELEMENTS.containsKey(id)) {
                throw new IllegalStateException("DynamicVlanMode item " + name + "(" + id
                        + ") is already defined, cannot have more than one of them");
            }

            ELEMENTS.put(id, this);
            ELEMENTS_BY_NAME.put(name, this);
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DynamicVlanMode)) {
            return false;
        }
        DynamicVlanMode other = (DynamicVlanMode) obj;
        return id == other.id;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return id;
    }

    @JsonIgnore
    public String name() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }

}
