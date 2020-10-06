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


public class CredentialType extends BaseJsonModel implements EnumWithId {

    private static final long serialVersionUID = 3414126959454909905L;

    private static final Logger LOG = LoggerFactory.getLogger(CredentialType.class);

    private static Object lock = new Object();
    private static final Map<Integer, CredentialType> ELEMENTS = new ConcurrentHashMap<>();
    private static final Map<String, CredentialType> ELEMENTS_BY_NAME = new ConcurrentHashMap<>();

    public static final CredentialType

    sim = new CredentialType(1, "SIM"), usim = new CredentialType(2, "USIM"),
            nfc_secure_element = new CredentialType(3, "NFC Secure Element"),
            hardware_token = new CredentialType(4, "Hardware Token"), softoken = new CredentialType(5, "Softoken"),
            certificate = new CredentialType(6, "Certificate"),
            username_password = new CredentialType(7, "username/password"),
            none = new CredentialType(8, "none (server-side authentication only)"),
            reserved = new CredentialType(9, "Reserved"), vendor_specific = new CredentialType(10, "Vendor Specific"),
            UNSUPPORTED = new CredentialType(-1, "UNSUPPORTED");
    static {
        // try to load all the subclasses explicitly - to avoid timing issues
        // when items coming from subclasses may be registered some time later,
        // after the parent class is loaded
        Set<Class<? extends CredentialType>> subclasses = BaseJsonModel.getReflections()
                .getSubTypesOf(CredentialType.class);
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

    protected CredentialType(int id, String name) {
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
    public static CredentialType[] values() {
        return new ArrayList<>(ELEMENTS.values()).toArray(new CredentialType[0]);
    }

    public static CredentialType getById(int enumId) {
        return ELEMENTS.get(enumId);
    }

    @JsonCreator
    public static CredentialType getByName(String value) {
        CredentialType ret = ELEMENTS_BY_NAME.get(value);
        if (ret == null) {
            ret = UNSUPPORTED;
        }

        return ret;
    }


    public static List<CredentialType> getValues() {
        return new ArrayList<>(ELEMENTS.values());
    }

    public static boolean isUnsupported(CredentialType value) {
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
        if (!(obj instanceof CredentialType)) {
            return false;
        }
        CredentialType other = (CredentialType) obj;
        return id == other.id && Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return name;
    }


}
