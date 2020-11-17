package com.telecominfraproject.wlan.profile.passpoint.models.venue;

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


public class PasspointVenueGroup implements EnumWithId {


    private static final Logger LOG = LoggerFactory.getLogger(PasspointVenueGroup.class);

    private static Object lock = new Object();
    private static final Map<Integer, PasspointVenueGroup> ELEMENTS = new ConcurrentHashMap<>();
    private static final Map<String, PasspointVenueGroup> ELEMENTS_BY_NAME = new ConcurrentHashMap<>();

    public static final PasspointVenueGroup

    unspecified = new PasspointVenueGroup(0, "unspecified"), assembly = new PasspointVenueGroup(1, "assembly"),
            business = new PasspointVenueGroup(2, "business"), eductional = new PasspointVenueGroup(3, "educational"),
            factory_and_industrial = new PasspointVenueGroup(4, "factory_and_industrial"),
            institutional = new PasspointVenueGroup(5, "institutional"), mercantile = new PasspointVenueGroup(6, "mercantile"),
            residential = new PasspointVenueGroup(7, "residential"), storage = new PasspointVenueGroup(8, "storage"),
            utility_and_miscellaneous = new PasspointVenueGroup(9, "utility_and_miscellaneous"),
            vehicular = new PasspointVenueGroup(10, "vehicular"), outdoor = new PasspointVenueGroup(11, "outdoor"),

            UNSUPPORTED = new PasspointVenueGroup(-1, "UNSUPPORTED");

    static {
        // try to load all the subclasses explicitly - to avoid timing issues
        // when items coming from subclasses may be registered some time later,
        // after the parent class is loaded
        Set<Class<? extends PasspointVenueGroup>> subclasses = BaseJsonModel.getReflections().getSubTypesOf(PasspointVenueGroup.class);
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

    protected PasspointVenueGroup(int id, String name) {
        synchronized (lock) {

            LOG.debug("Registering PasspointVenueGroup by {} : {}", this.getClass().getSimpleName(), name);

            this.id = id;
            this.name = name;

            ELEMENTS_BY_NAME.values().forEach(s -> {
                if (s.getName().equals(name)) {
                    throw new IllegalStateException("PasspointVenueGroup item for " + name
                            + " is already defined, cannot have more than one of them");
                }
            });

            if (ELEMENTS.containsKey(id)) {
                throw new IllegalStateException("PasspointVenueGroup item " + name + "(" + id
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
    public static PasspointVenueGroup[] values() {
        return new ArrayList<>(ELEMENTS.values()).toArray(new PasspointVenueGroup[0]);
    }

    public static PasspointVenueGroup getById(int enumId) {
        return ELEMENTS.get(enumId);
    }

    @JsonCreator
    public static PasspointVenueGroup getByName(String value) {
        PasspointVenueGroup ret = ELEMENTS_BY_NAME.get(value);
        if (ret == null) {
            ret = UNSUPPORTED;
        }

        return ret;
    }


    public static List<PasspointVenueGroup> getValues() {
        return new ArrayList<>(ELEMENTS.values());
    }

    public static boolean isUnsupported(PasspointVenueGroup value) {
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
        if (!(obj instanceof PasspointVenueGroup)) {
            return false;
        }
        PasspointVenueGroup other = (PasspointVenueGroup) obj;
        return id == other.id;
    }

    @Override
    public String toString() {
        return name;
    }


}
