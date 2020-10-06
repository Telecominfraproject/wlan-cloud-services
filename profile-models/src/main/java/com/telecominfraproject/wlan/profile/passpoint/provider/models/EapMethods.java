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


public class EapMethods extends BaseJsonModel implements EnumWithId {


    /**
     * 
     */
    private static final long serialVersionUID = -1873755168561965743L;

    private static final Logger LOG = LoggerFactory.getLogger(EapMethods.class);

    private static Object lock = new Object();
    private static final Map<Integer, EapMethods> ELEMENTS = new ConcurrentHashMap<>();
    private static final Map<String, EapMethods> ELEMENTS_BY_NAME = new ConcurrentHashMap<>();

    public static final EapMethods

    eap_tls = new EapMethods(13, "EAP-TLS with certificate"),
            eap_ttls = new EapMethods(21, "EAP-TTLS with username/password"),
            eap_mschap_v2 = new EapMethods(29, "EAP-MSCHAP-V2 with username/password"),

            UNSUPPORTED = new EapMethods(-1, "UNSUPPORTED");

    static {
        // try to load all the subclasses explicitly - to avoid timing issues
        // when items coming from subclasses may be registered some time later,
        // after the parent class is loaded
        Set<Class<? extends EapMethods>> subclasses = BaseJsonModel.getReflections().getSubTypesOf(EapMethods.class);
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

    protected EapMethods(int id, String name) {
        synchronized (lock) {

            LOG.debug("Registering EapMethods by {} : {}", this.getClass().getSimpleName(), name);

            this.id = id;
            this.name = name;

            ELEMENTS_BY_NAME.values().forEach(s -> {
                if (s.getName().equals(name)) {
                    throw new IllegalStateException(
                            "EapMethods item for " + name + " is already defined, cannot have more than one of them");
                }
            });

            if (ELEMENTS.containsKey(id)) {
                throw new IllegalStateException("EapMethods item " + name + "(" + id
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
    public static EapMethods[] values() {
        return new ArrayList<>(ELEMENTS.values()).toArray(new EapMethods[0]);
    }

    public static EapMethods getById(int enumId) {
        return ELEMENTS.get(enumId);
    }

    @JsonCreator
    public static EapMethods getByName(String value) {
        EapMethods ret = ELEMENTS_BY_NAME.get(value);
        if (ret == null) {
            ret = UNSUPPORTED;
        }

        return ret;
    }


    public static List<EapMethods> getValues() {
        return new ArrayList<>(ELEMENTS.values());
    }

    public static boolean isUnsupported(EapMethods value) {
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
        if (!(obj instanceof EapMethods)) {
            return false;
        }
        EapMethods other = (EapMethods) obj;
        return id == other.id && Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return name;
    }

}
