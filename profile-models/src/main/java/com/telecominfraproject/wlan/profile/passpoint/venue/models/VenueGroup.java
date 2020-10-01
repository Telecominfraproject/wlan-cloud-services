package com.telecominfraproject.wlan.profile.passpoint.venue.models;

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


public class VenueGroup implements EnumWithId {


    private static final Logger LOG = LoggerFactory.getLogger(VenueGroup.class);

    private static Object lock = new Object();
    private static final Map<Integer, VenueGroup> ELEMENTS = new ConcurrentHashMap<>();
    private static final Map<String, VenueGroup> ELEMENTS_BY_NAME = new ConcurrentHashMap<>();

    public static final VenueGroup

    unspecified = new VenueGroup(0, "unspecified"), assembly = new VenueGroup(1, "assembly"),
            business = new VenueGroup(2, "business"), eductional = new VenueGroup(3, "educational"),
            factory_and_industrial = new VenueGroup(4, "factory_and_industrial"),
            institutional = new VenueGroup(5, "institutional"), mercantile = new VenueGroup(6, "mercantile"),
            residential = new VenueGroup(7, "residential"), storage = new VenueGroup(8, "storage"),
            utility_and_miscellaneous = new VenueGroup(9, "utility_and_miscellaneous"),
            vehicular = new VenueGroup(10, "vehicular"), outdoor = new VenueGroup(11, "outdoor"),

            UNSUPPORTED = new VenueGroup(-1, "UNSUPPORTED");

    static {
        // try to load all the subclasses explicitly - to avoid timing issues
        // when items coming from subclasses may be registered some time later,
        // after the parent class is loaded
        Set<Class<? extends VenueGroup>> subclasses = BaseJsonModel.getReflections().getSubTypesOf(VenueGroup.class);
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

    protected VenueGroup(int id, String name) {
        synchronized (lock) {

            LOG.debug("Registering VenueGroup by {} : {}", this.getClass().getSimpleName(), name);

            this.id = id;
            this.name = name;

            ELEMENTS_BY_NAME.values().forEach(s -> {
                if (s.getName().equals(name)) {
                    throw new IllegalStateException("VenueGroup item for " + name
                            + " is already defined, cannot have more than one of them");
                }
            });

            if (ELEMENTS.containsKey(id)) {
                throw new IllegalStateException("VenueGroup item " + name + "(" + id
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
    public static VenueGroup[] values() {
        return new ArrayList<>(ELEMENTS.values()).toArray(new VenueGroup[0]);
    }

    public static VenueGroup getById(int enumId) {
        return ELEMENTS.get(enumId);
    }

    @JsonCreator
    public static VenueGroup getByName(String value) {
        VenueGroup ret = ELEMENTS_BY_NAME.get(value);
        if (ret == null) {
            ret = UNSUPPORTED;
        }

        return ret;
    }


    public static List<VenueGroup> getValues() {
        return new ArrayList<>(ELEMENTS.values());
    }

    public static boolean isUnsupported(VenueGroup value) {
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
        if (!(obj instanceof VenueGroup)) {
            return false;
        }
        VenueGroup other = (VenueGroup) obj;
        return id == other.id;
    }

    @Override
    public String toString() {
        return name;
    }


}
