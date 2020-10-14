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


public class NaiRealmEapAuthParam extends BaseJsonModel implements EnumWithId {


    /**
     * 
     */
    private static final long serialVersionUID = -1873755168561965743L;

    private static final Logger LOG = LoggerFactory.getLogger(NaiRealmEapAuthParam.class);

    private static Object lock = new Object();
    private static final Map<Integer, NaiRealmEapAuthParam> ELEMENTS = new ConcurrentHashMap<>();
    private static final Map<String, NaiRealmEapAuthParam> ELEMENTS_BY_NAME = new ConcurrentHashMap<>();

    public static final NaiRealmEapAuthParam NAI_REALM_EAP_AUTH_EXPANDED_EAP_METHOD = new NaiRealmEapAuthParam(1,
            "Expanded EAP Method"),
            NAI_REALM_EAP_AUTH_NON_EAP_INNER_AUTH = new NaiRealmEapAuthParam(2, "Non-EAP Inner Authentication Type"),
            NAI_REALM_EAP_AUTH_INNER_AUTH_EAP_METHOD = new NaiRealmEapAuthParam(3,
                    "Inner Authentication EAP Method Type"),
            NAI_REALM_EAP_AUTH_EXPANDED_INNER_EAP_METHOD = new NaiRealmEapAuthParam(4, "Expanded Inner EAP Method"),
            NAI_REALM_EAP_AUTH_CRED_TYPE = new NaiRealmEapAuthParam(5, "Credential Type"),
            NAI_REALM_EAP_AUTH_TUNNELED_CRED_TYPE = new NaiRealmEapAuthParam(6, "Tunneled EAP Method Credential Type"),
            NAI_REALM_EAP_AUTH_VENDOR_SPECIFIC = new NaiRealmEapAuthParam(221, "Nai Realm EAP Auth Vendor Specific"),
            UNSUPPORTED = new NaiRealmEapAuthParam(-1, "UNSUPPORTED");

    static {
        // try to load all the subclasses explicitly - to avoid timing issues
        // when items coming from subclasses may be registered some time later,
        // after the parent class is loaded
        Set<Class<? extends NaiRealmEapAuthParam>> subclasses = BaseJsonModel.getReflections()
                .getSubTypesOf(NaiRealmEapAuthParam.class);
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

    protected NaiRealmEapAuthParam(int id, String name) {
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
    public static NaiRealmEapAuthParam[] values() {
        return new ArrayList<>(ELEMENTS.values()).toArray(new NaiRealmEapAuthParam[0]);
    }

    public static NaiRealmEapAuthParam getById(int enumId) {
        return ELEMENTS.get(enumId);
    }

    @JsonCreator
    public static NaiRealmEapAuthParam getByName(String value) {
        NaiRealmEapAuthParam ret = ELEMENTS_BY_NAME.get(value);
        if (ret == null) {
            ret = UNSUPPORTED;
        }

        return ret;
    }


    public static List<NaiRealmEapAuthParam> getValues() {
        return new ArrayList<>(ELEMENTS.values());
    }

    public static boolean isUnsupported(NaiRealmEapAuthParam value) {
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
        if (!(obj instanceof NaiRealmEapAuthParam)) {
            return false;
        }
        NaiRealmEapAuthParam other = (NaiRealmEapAuthParam) obj;
        return id == other.id && Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return name;
    }

}
