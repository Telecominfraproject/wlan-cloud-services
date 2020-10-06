package com.telecominfraproject.wlan.profile.passpoint.provider.models;

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


public class NonEapInnerAuthenticationTypes extends BaseJsonModel implements EnumWithId {

    private static final long serialVersionUID = 3414126959454909905L;

    private static final Logger LOG = LoggerFactory.getLogger(NonEapInnerAuthenticationTypes.class);

    private static Object lock = new Object();
    private static final Map<Integer, NonEapInnerAuthenticationTypes> ELEMENTS = new ConcurrentHashMap<>();
    private static final Map<String, NonEapInnerAuthenticationTypes> ELEMENTS_BY_NAME = new ConcurrentHashMap<>();

    public static final NonEapInnerAuthenticationTypes

    reserved = new NonEapInnerAuthenticationTypes(0, "Reserved"), pap = new NonEapInnerAuthenticationTypes(1, "PAP"),
            chap = new NonEapInnerAuthenticationTypes(2, "CHAP"),
            mschap = new NonEapInnerAuthenticationTypes(3, "MSCHAP"),
            mschap_v2 = new NonEapInnerAuthenticationTypes(4, "MSCHAPV2"),
            UNSUPPORTED = new NonEapInnerAuthenticationTypes(-1, "UNSUPPORTED");
    static {
        // try to load all the subclasses explicitly - to avoid timing issues
        // when items coming from subclasses may be registered some time later,
        // after the parent class is loaded
        Set<Class<? extends NonEapInnerAuthenticationTypes>> subclasses = BaseJsonModel.getReflections()
                .getSubTypesOf(NonEapInnerAuthenticationTypes.class);
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

    protected NonEapInnerAuthenticationTypes(int id, String name) {
        synchronized (lock) {

            LOG.debug("Registering NonEapInnerAuthenticationTypes by {} : {}", this.getClass().getSimpleName(), name);

            this.id = id;
            this.name = name;

            ELEMENTS_BY_NAME.values().forEach(s -> {
                if (s.getName().equals(name)) {
                    throw new IllegalStateException("NonEapInnerAuthenticationTypes item for " + name
                            + " is already defined, cannot have more than one of them");
                }
            });

            if (ELEMENTS.containsKey(id)) {
                throw new IllegalStateException("NonEapInnerAuthenticationTypes item " + name + "(" + id
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
    public static NonEapInnerAuthenticationTypes[] values() {
        return new ArrayList<>(ELEMENTS.values()).toArray(new NonEapInnerAuthenticationTypes[0]);
    }

    public static NonEapInnerAuthenticationTypes getById(int enumId) {
        return ELEMENTS.get(enumId);
    }

    @JsonCreator
    public static NonEapInnerAuthenticationTypes getByName(String value) {
        NonEapInnerAuthenticationTypes ret = ELEMENTS_BY_NAME.get(value);
        if (ret == null) {
            ret = UNSUPPORTED;
        }

        return ret;
    }


    public static List<NonEapInnerAuthenticationTypes> getValues() {
        return new ArrayList<>(ELEMENTS.values());
    }

    public static boolean isUnsupported(NonEapInnerAuthenticationTypes value) {
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
        if (!(obj instanceof NonEapInnerAuthenticationTypes)) {
            return false;
        }
        NonEapInnerAuthenticationTypes other = (NonEapInnerAuthenticationTypes) obj;
        return id == other.id && Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return name;
    }


}
