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


public class PasspointEapMethods extends BaseJsonModel implements EnumWithId {


    /**
     * 
     */
    private static final long serialVersionUID = -1873755168561965743L;

    private static final Logger LOG = LoggerFactory.getLogger(PasspointEapMethods.class);

    private static Object lock = new Object();
    private static final Map<Integer, PasspointEapMethods> ELEMENTS = new ConcurrentHashMap<>();
    private static final Map<String, PasspointEapMethods> ELEMENTS_BY_NAME = new ConcurrentHashMap<>();

    public static final PasspointEapMethods

    eap_tls = new PasspointEapMethods(13, "EAP-TLS with certificate"),
    eap_ttls = new PasspointEapMethods(21, "EAP-TTLS with username/password"),
    eap_aka_authentication = new PasspointEapMethods(23,"EAP-AKA Authentication"),
    eap_mschap_v2 = new PasspointEapMethods(29, "EAP-MSCHAP-V2 with username/password"),
    eap_aka = new PasspointEapMethods(50, "EAP-AKA'"),

            UNSUPPORTED = new PasspointEapMethods(-1, "UNSUPPORTED");

    static {
        // try to load all the subclasses explicitly - to avoid timing issues
        // when items coming from subclasses may be registered some time later,
        // after the parent class is loaded
        Set<Class<? extends PasspointEapMethods>> subclasses = BaseJsonModel.getReflections().getSubTypesOf(PasspointEapMethods.class);
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

    protected PasspointEapMethods(int id, String name) {
        synchronized (lock) {

            LOG.debug("Registering PasspointEapMethods by {} : {}", this.getClass().getSimpleName(), name);

            this.id = id;
            this.name = name;

            ELEMENTS_BY_NAME.values().forEach(s -> {
                if (s.getName().equals(name)) {
                    throw new IllegalStateException(
                            "PasspointEapMethods item for " + name + " is already defined, cannot have more than one of them");
                }
            });

            if (ELEMENTS.containsKey(id)) {
                throw new IllegalStateException("PasspointEapMethods item " + name + "(" + id
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
    public static PasspointEapMethods[] values() {
        return new ArrayList<>(ELEMENTS.values()).toArray(new PasspointEapMethods[0]);
    }

    public static PasspointEapMethods getById(int enumId) {
        return ELEMENTS.get(enumId);
    }

    @JsonCreator
    public static PasspointEapMethods getByName(String value) {
        PasspointEapMethods ret = ELEMENTS_BY_NAME.get(value);
        if (ret == null) {
            ret = UNSUPPORTED;
        }

        return ret;
    }


    public static List<PasspointEapMethods> getValues() {
        return new ArrayList<>(ELEMENTS.values());
    }

    public static boolean isUnsupported(PasspointEapMethods value) {
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
        if (!(obj instanceof PasspointEapMethods)) {
            return false;
        }
        PasspointEapMethods other = (PasspointEapMethods) obj;
        return id == other.id && Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return name;
    }

}
