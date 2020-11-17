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


public class PasspointNaiRealmEapCredType extends BaseJsonModel implements EnumWithId {

    private static final long serialVersionUID = 3414126959454909905L;

    private static final Logger LOG = LoggerFactory.getLogger(PasspointNaiRealmEapCredType.class);

    private static Object lock = new Object();
    private static final Map<Integer, PasspointNaiRealmEapCredType> ELEMENTS = new ConcurrentHashMap<>();
    private static final Map<String, PasspointNaiRealmEapCredType> ELEMENTS_BY_NAME = new ConcurrentHashMap<>();

    public static final PasspointNaiRealmEapCredType NAI_REALM_CRED_TYPE_SIM = new PasspointNaiRealmEapCredType(1, "SIM"),
            NAI_REALM_CRED_TYPE_USIM = new PasspointNaiRealmEapCredType(2, "USIM"),
            NAI_REALM_CRED_TYPE_NFC_SECURE_ELEMENT = new PasspointNaiRealmEapCredType(3, "NFC Secure Element"),
            NAI_REALM_CRED_TYPE_HARDWARE_TOKEN = new PasspointNaiRealmEapCredType(4, "Hardware Token"),
            NAI_REALM_CRED_TYPE_SOFTOKEN = new PasspointNaiRealmEapCredType(5, "Softoken"),
            NAI_REALM_CRED_TYPE_CERTIFICATE = new PasspointNaiRealmEapCredType(6, "Certificate"),
            NAI_REALM_CRED_TYPE_USERNAME_PASSWORD = new PasspointNaiRealmEapCredType(7, "username/password"),
            NAI_REALM_CRED_TYPE_NONE = new PasspointNaiRealmEapCredType(8, "none (server-side authentication only)"),
            NAI_REALM_CRED_TYPE_ANONYMOUS = new PasspointNaiRealmEapCredType(9, "Anonymous"),
            NAI_REALM_CRED_TYPE_VENDOR_SPECIFIC = new PasspointNaiRealmEapCredType(10, "Vendor Specific"),
            UNSUPPORTED = new PasspointNaiRealmEapCredType(-1, "UNSUPPORTED");
    static {
        // try to load all the subclasses explicitly - to avoid timing issues
        // when items coming from subclasses may be registered some time later,
        // after the parent class is loaded
        Set<Class<? extends PasspointNaiRealmEapCredType>> subclasses = BaseJsonModel.getReflections()
                .getSubTypesOf(PasspointNaiRealmEapCredType.class);
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

    protected PasspointNaiRealmEapCredType(int id, String name) {
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
    public static PasspointNaiRealmEapCredType[] values() {
        return new ArrayList<>(ELEMENTS.values()).toArray(new PasspointNaiRealmEapCredType[0]);
    }

    public static PasspointNaiRealmEapCredType getById(int enumId) {
        return ELEMENTS.get(enumId);
    }

    @JsonCreator
    public static PasspointNaiRealmEapCredType getByName(String value) {
        PasspointNaiRealmEapCredType ret = ELEMENTS_BY_NAME.get(value);
        if (ret == null) {
            ret = UNSUPPORTED;
        }

        return ret;
    }


    public static List<PasspointNaiRealmEapCredType> getValues() {
        return new ArrayList<>(ELEMENTS.values());
    }

    public static boolean isUnsupported(PasspointNaiRealmEapCredType value) {
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
        if (!(obj instanceof PasspointNaiRealmEapCredType)) {
            return false;
        }
        PasspointNaiRealmEapCredType other = (PasspointNaiRealmEapCredType) obj;
        return id == other.id && Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return name;
    }


}
