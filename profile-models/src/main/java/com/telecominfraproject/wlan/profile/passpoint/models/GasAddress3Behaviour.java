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


public class GasAddress3Behaviour implements EnumWithId {


    private static final Logger LOG = LoggerFactory.getLogger(GasAddress3Behaviour.class);

    private static Object lock = new Object();
    private static final Map<Integer, GasAddress3Behaviour> ELEMENTS = new ConcurrentHashMap<>();
    private static final Map<String, GasAddress3Behaviour> ELEMENTS_BY_NAME = new ConcurrentHashMap<>();

    public static final GasAddress3Behaviour

    p2pSpecWorkaroundFromRequest = new GasAddress3Behaviour(0, "p2pSpecWorkaroundFromRequest"),
            ieee80211StandardCompliantOnly = new GasAddress3Behaviour(1, "ieee80211StandardCompliantOnly"),
            forceNonCompliantBehaviourFromRequest = new GasAddress3Behaviour(2,
                    "forceNonCompliantBehaviourFromRequest"),

            UNSUPPORTED = new GasAddress3Behaviour(-1, "UNSUPPORTED");

    static {
        // try to load all the subclasses explicitly - to avoid timing issues
        // when items coming from subclasses may be registered some time later,
        // after the parent class is loaded
        Set<Class<? extends GasAddress3Behaviour>> subclasses = BaseJsonModel.getReflections()
                .getSubTypesOf(GasAddress3Behaviour.class);
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

    protected GasAddress3Behaviour(int id, String name) {
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
    public static GasAddress3Behaviour[] values() {
        return new ArrayList<>(ELEMENTS.values()).toArray(new GasAddress3Behaviour[0]);
    }

    public static GasAddress3Behaviour getById(int enumId) {
        return ELEMENTS.get(enumId);
    }

    @JsonCreator
    public static GasAddress3Behaviour getByName(String value) {
        GasAddress3Behaviour ret = ELEMENTS_BY_NAME.get(value);
        if (ret == null) {
            ret = UNSUPPORTED;
        }

        return ret;
    }


    public static List<GasAddress3Behaviour> getValues() {
        return new ArrayList<>(ELEMENTS.values());
    }

    public static boolean isUnsupported(GasAddress3Behaviour value) {
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
        if (!(obj instanceof GasAddress3Behaviour)) {
            return false;
        }
        GasAddress3Behaviour other = (GasAddress3Behaviour) obj;
        return id == other.id;
    }

    @Override
    public String toString() {
        return name;
    }


}
