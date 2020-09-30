package com.telecominfraproject.wlan.profile.passpoint.models;

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


public class NetworkAuthenticationType implements EnumWithId {


    private static final Logger LOG = LoggerFactory.getLogger(NetworkAuthenticationType.class);

    private static Object lock = new Object();
    private static final Map<Integer, NetworkAuthenticationType> ELEMENTS = new ConcurrentHashMap<>();
    private static final Map<String, NetworkAuthenticationType> ELEMENTS_BY_NAME = new ConcurrentHashMap<>();

    public static final NetworkAuthenticationType

    acceptance_of_terms_and_conditions = new NetworkAuthenticationType(0, "acceptance_of_terms_and_conditions"),
            online_enrollment_supported = new NetworkAuthenticationType(1, "online_enrollment_supported"),
            http_https_redirection = new NetworkAuthenticationType(2, "http_https_redirection"),
            dns_redirection = new NetworkAuthenticationType(3, "dns_redirection"),

            UNSUPPORTED = new NetworkAuthenticationType(-1, "UNSUPPORTED");
    
    static {
        // try to load all the subclasses explicitly - to avoid timing issues
        // when items coming from subclasses may be registered some time later,
        // after the parent class is loaded
        Set<Class<? extends NetworkAuthenticationType>> subclasses = BaseJsonModel.getReflections()
                .getSubTypesOf(NetworkAuthenticationType.class);
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

    protected NetworkAuthenticationType(int id, String name) {
        synchronized (lock) {

            LOG.debug("Registering NetworkAuthenticationType by {} : {}", this.getClass().getSimpleName(), name);

            this.id = id;
            this.name = name;

            ELEMENTS_BY_NAME.values().forEach(s -> {
                if (s.getName().equals(name)) {
                    throw new IllegalStateException("NetworkAuthenticationType item for " + name
                            + " is already defined, cannot have more than one of them");
                }
            });

            if (ELEMENTS.containsKey(id)) {
                throw new IllegalStateException("NetworkAuthenticationType item " + name + "(" + id
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
    public static NetworkAuthenticationType[] values() {
        return new ArrayList<>(ELEMENTS.values()).toArray(new NetworkAuthenticationType[0]);
    }

    public static NetworkAuthenticationType getById(int enumId) {
        return ELEMENTS.get(enumId);
    }

    @JsonCreator
    public static NetworkAuthenticationType getByName(String value) {
        NetworkAuthenticationType ret = ELEMENTS_BY_NAME.get(value);
        if (ret == null) {
            ret = UNSUPPORTED;
        }

        return ret;
    }


    public static List<NetworkAuthenticationType> getValues() {
        return new ArrayList<>(ELEMENTS.values());
    }

    public static boolean isUnsupported(NetworkAuthenticationType value) {
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
        if (!(obj instanceof NetworkAuthenticationType)) {
            return false;
        }
        NetworkAuthenticationType other = (NetworkAuthenticationType) obj;
        return id == other.id;
    }

    @Override
    public String toString() {
        return name;
    }


}
