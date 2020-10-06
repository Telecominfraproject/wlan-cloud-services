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


public class AuthenticationParameterTypes extends BaseJsonModel implements EnumWithId {


    /**
     * 
     */
    private static final long serialVersionUID = -1873755168561965743L;

    private static final Logger LOG = LoggerFactory.getLogger(AuthenticationParameterTypes.class);

    private static Object lock = new Object();
    private static final Map<Integer, AuthenticationParameterTypes> ELEMENTS = new ConcurrentHashMap<>();
    private static final Map<String, AuthenticationParameterTypes> ELEMENTS_BY_NAME = new ConcurrentHashMap<>();

    public static final AuthenticationParameterTypes

    expanded_eap_method = new AuthenticationParameterTypes(1, "Expanded EAP Method"),
            non_eap_inner_authentication_type = new AuthenticationParameterTypes(2,
                    "Non-EAP Inner Authentication Type"),
            inner_authentication_eap_method_type = new AuthenticationParameterTypes(3,
                    "Inner Authentication EAP Method Type"),
            expanded_inner_eap_method = new AuthenticationParameterTypes(4, "Expanded Inner EAP Method"),
            credential_type = new AuthenticationParameterTypes(5, "Credential Type"),
            tunneled_eap_method_credential_type = new AuthenticationParameterTypes(6,
                    "Tunneled EAP Method Credential Type"),

            UNSUPPORTED = new AuthenticationParameterTypes(-1, "UNSUPPORTED");

    static {
        // try to load all the subclasses explicitly - to avoid timing issues
        // when items coming from subclasses may be registered some time later,
        // after the parent class is loaded
        Set<Class<? extends AuthenticationParameterTypes>> subclasses = BaseJsonModel.getReflections()
                .getSubTypesOf(AuthenticationParameterTypes.class);
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

    protected AuthenticationParameterTypes(int id, String name) {
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
    public static AuthenticationParameterTypes[] values() {
        return new ArrayList<>(ELEMENTS.values()).toArray(new AuthenticationParameterTypes[0]);
    }

    public static AuthenticationParameterTypes getById(int enumId) {
        return ELEMENTS.get(enumId);
    }

    @JsonCreator
    public static AuthenticationParameterTypes getByName(String value) {
        AuthenticationParameterTypes ret = ELEMENTS_BY_NAME.get(value);
        if (ret == null) {
            ret = UNSUPPORTED;
        }

        return ret;
    }


    public static List<AuthenticationParameterTypes> getValues() {
        return new ArrayList<>(ELEMENTS.values());
    }

    public static boolean isUnsupported(AuthenticationParameterTypes value) {
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
        if (!(obj instanceof AuthenticationParameterTypes)) {
            return false;
        }
        AuthenticationParameterTypes other = (AuthenticationParameterTypes) obj;
        return id == other.id && Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return name;
    }

}
