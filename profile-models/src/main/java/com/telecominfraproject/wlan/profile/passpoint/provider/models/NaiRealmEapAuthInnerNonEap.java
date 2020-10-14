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


public class NaiRealmEapAuthInnerNonEap extends BaseJsonModel implements EnumWithId {

    private static final long serialVersionUID = 3414126959454909905L;

    private static final Logger LOG = LoggerFactory.getLogger(NaiRealmEapAuthInnerNonEap.class);

    private static Object lock = new Object();
    private static final Map<Integer, NaiRealmEapAuthInnerNonEap> ELEMENTS = new ConcurrentHashMap<>();
    private static final Map<String, NaiRealmEapAuthInnerNonEap> ELEMENTS_BY_NAME = new ConcurrentHashMap<>();

    public static final NaiRealmEapAuthInnerNonEap

    NAI_REALM_INNER_NON_EAP_PAP = new NaiRealmEapAuthInnerNonEap(1, "PAP"),
            NAI_REALM_INNER_NON_EAP_CHAP = new NaiRealmEapAuthInnerNonEap(2, "CHAP"),
            NAI_REALM_INNER_NON_EAP_MSCHAP = new NaiRealmEapAuthInnerNonEap(3, "MSCHAP"),
            NAI_REALM_INNER_NON_EAP_MSCHAPV2 = new NaiRealmEapAuthInnerNonEap(4, "MSCHAPV2"),
            UNSUPPORTED = new NaiRealmEapAuthInnerNonEap(-1, "UNSUPPORTED");
    static {
        // try to load all the subclasses explicitly - to avoid timing issues
        // when items coming from subclasses may be registered some time later,
        // after the parent class is loaded
        Set<Class<? extends NaiRealmEapAuthInnerNonEap>> subclasses = BaseJsonModel.getReflections()
                .getSubTypesOf(NaiRealmEapAuthInnerNonEap.class);
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

    protected NaiRealmEapAuthInnerNonEap(int id, String name) {
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
    public static NaiRealmEapAuthInnerNonEap[] values() {
        return new ArrayList<>(ELEMENTS.values()).toArray(new NaiRealmEapAuthInnerNonEap[0]);
    }

    public static NaiRealmEapAuthInnerNonEap getById(int enumId) {
        return ELEMENTS.get(enumId);
    }

    @JsonCreator
    public static NaiRealmEapAuthInnerNonEap getByName(String value) {
        NaiRealmEapAuthInnerNonEap ret = ELEMENTS_BY_NAME.get(value);
        if (ret == null) {
            ret = UNSUPPORTED;
        }

        return ret;
    }


    public static List<NaiRealmEapAuthInnerNonEap> getValues() {
        return new ArrayList<>(ELEMENTS.values());
    }

    public static boolean isUnsupported(NaiRealmEapAuthInnerNonEap value) {
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
        if (!(obj instanceof NaiRealmEapAuthInnerNonEap)) {
            return false;
        }
        NaiRealmEapAuthInnerNonEap other = (NaiRealmEapAuthInnerNonEap) obj;
        return id == other.id && Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return name;
    }


}
