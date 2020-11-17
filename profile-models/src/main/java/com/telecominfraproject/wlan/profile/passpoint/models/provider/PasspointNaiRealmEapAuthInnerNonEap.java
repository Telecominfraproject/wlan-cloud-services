package com.telecominfraproject.wlan.profile.passpoint.models.provider;

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


public class PasspointNaiRealmEapAuthInnerNonEap extends BaseJsonModel implements EnumWithId {

    private static final long serialVersionUID = 3414126959454909905L;

    private static final Logger LOG = LoggerFactory.getLogger(PasspointNaiRealmEapAuthInnerNonEap.class);

    private static Object lock = new Object();
    private static final Map<Integer, PasspointNaiRealmEapAuthInnerNonEap> ELEMENTS = new ConcurrentHashMap<>();
    private static final Map<String, PasspointNaiRealmEapAuthInnerNonEap> ELEMENTS_BY_NAME = new ConcurrentHashMap<>();

    public static final PasspointNaiRealmEapAuthInnerNonEap

    NAI_REALM_INNER_NON_EAP_PAP = new PasspointNaiRealmEapAuthInnerNonEap(1, "PAP"),
            NAI_REALM_INNER_NON_EAP_CHAP = new PasspointNaiRealmEapAuthInnerNonEap(2, "CHAP"),
            NAI_REALM_INNER_NON_EAP_MSCHAP = new PasspointNaiRealmEapAuthInnerNonEap(3, "MSCHAP"),
            NAI_REALM_INNER_NON_EAP_MSCHAPV2 = new PasspointNaiRealmEapAuthInnerNonEap(4, "MSCHAPV2"),
            UNSUPPORTED = new PasspointNaiRealmEapAuthInnerNonEap(-1, "UNSUPPORTED");
    static {
        // try to load all the subclasses explicitly - to avoid timing issues
        // when items coming from subclasses may be registered some time later,
        // after the parent class is loaded
        Set<Class<? extends PasspointNaiRealmEapAuthInnerNonEap>> subclasses = BaseJsonModel.getReflections()
                .getSubTypesOf(PasspointNaiRealmEapAuthInnerNonEap.class);
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

    protected PasspointNaiRealmEapAuthInnerNonEap(int id, String name) {
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
    public static PasspointNaiRealmEapAuthInnerNonEap[] values() {
        return new ArrayList<>(ELEMENTS.values()).toArray(new PasspointNaiRealmEapAuthInnerNonEap[0]);
    }

    public static PasspointNaiRealmEapAuthInnerNonEap getById(int enumId) {
        return ELEMENTS.get(enumId);
    }

    @JsonCreator
    public static PasspointNaiRealmEapAuthInnerNonEap getByName(String value) {
        PasspointNaiRealmEapAuthInnerNonEap ret = ELEMENTS_BY_NAME.get(value);
        if (ret == null) {
            ret = UNSUPPORTED;
        }

        return ret;
    }


    public static List<PasspointNaiRealmEapAuthInnerNonEap> getValues() {
        return new ArrayList<>(ELEMENTS.values());
    }

    public static boolean isUnsupported(PasspointNaiRealmEapAuthInnerNonEap value) {
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
        if (!(obj instanceof PasspointNaiRealmEapAuthInnerNonEap)) {
            return false;
        }
        PasspointNaiRealmEapAuthInnerNonEap other = (PasspointNaiRealmEapAuthInnerNonEap) obj;
        return id == other.id && Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return name;
    }


}
