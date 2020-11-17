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


public class PasspointNaiRealmEapAuthParam extends BaseJsonModel implements EnumWithId {


    /**
     * 
     */
    private static final long serialVersionUID = -1873755168561965743L;

    private static final Logger LOG = LoggerFactory.getLogger(PasspointNaiRealmEapAuthParam.class);

    private static Object lock = new Object();
    private static final Map<Integer, PasspointNaiRealmEapAuthParam> ELEMENTS = new ConcurrentHashMap<>();
    private static final Map<String, PasspointNaiRealmEapAuthParam> ELEMENTS_BY_NAME = new ConcurrentHashMap<>();

    public static final PasspointNaiRealmEapAuthParam NAI_REALM_EAP_AUTH_EXPANDED_EAP_METHOD = new PasspointNaiRealmEapAuthParam(1,
            "Expanded EAP Method"),
            NAI_REALM_EAP_AUTH_NON_EAP_INNER_AUTH = new PasspointNaiRealmEapAuthParam(2, "Non-EAP Inner Authentication Type"),
            NAI_REALM_EAP_AUTH_INNER_AUTH_EAP_METHOD = new PasspointNaiRealmEapAuthParam(3,
                    "Inner Authentication EAP Method Type"),
            NAI_REALM_EAP_AUTH_EXPANDED_INNER_EAP_METHOD = new PasspointNaiRealmEapAuthParam(4, "Expanded Inner EAP Method"),
            NAI_REALM_EAP_AUTH_CRED_TYPE = new PasspointNaiRealmEapAuthParam(5, "Credential Type"),
            NAI_REALM_EAP_AUTH_TUNNELED_CRED_TYPE = new PasspointNaiRealmEapAuthParam(6, "Tunneled EAP Method Credential Type"),
            NAI_REALM_EAP_AUTH_VENDOR_SPECIFIC = new PasspointNaiRealmEapAuthParam(221, "Nai Realm EAP Auth Vendor Specific"),
            UNSUPPORTED = new PasspointNaiRealmEapAuthParam(-1, "UNSUPPORTED");

    static {
        // try to load all the subclasses explicitly - to avoid timing issues
        // when items coming from subclasses may be registered some time later,
        // after the parent class is loaded
        Set<Class<? extends PasspointNaiRealmEapAuthParam>> subclasses = BaseJsonModel.getReflections()
                .getSubTypesOf(PasspointNaiRealmEapAuthParam.class);
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

    protected PasspointNaiRealmEapAuthParam(int id, String name) {
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
    public static PasspointNaiRealmEapAuthParam[] values() {
        return new ArrayList<>(ELEMENTS.values()).toArray(new PasspointNaiRealmEapAuthParam[0]);
    }

    public static PasspointNaiRealmEapAuthParam getById(int enumId) {
        return ELEMENTS.get(enumId);
    }

    @JsonCreator
    public static PasspointNaiRealmEapAuthParam getByName(String value) {
        PasspointNaiRealmEapAuthParam ret = ELEMENTS_BY_NAME.get(value);
        if (ret == null) {
            ret = UNSUPPORTED;
        }

        return ret;
    }


    public static List<PasspointNaiRealmEapAuthParam> getValues() {
        return new ArrayList<>(ELEMENTS.values());
    }

    public static boolean isUnsupported(PasspointNaiRealmEapAuthParam value) {
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
        if (!(obj instanceof PasspointNaiRealmEapAuthParam)) {
            return false;
        }
        PasspointNaiRealmEapAuthParam other = (PasspointNaiRealmEapAuthParam) obj;
        return id == other.id && Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return name;
    }

}
