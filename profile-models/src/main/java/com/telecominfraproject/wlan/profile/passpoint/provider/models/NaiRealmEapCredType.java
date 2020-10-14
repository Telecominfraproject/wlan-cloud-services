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


public class NaiRealmEapCredType extends BaseJsonModel implements EnumWithId {

    private static final long serialVersionUID = 3414126959454909905L;

    private static final Logger LOG = LoggerFactory.getLogger(NaiRealmEapCredType.class);

    private static Object lock = new Object();
    private static final Map<Integer, NaiRealmEapCredType> ELEMENTS = new ConcurrentHashMap<>();
    private static final Map<String, NaiRealmEapCredType> ELEMENTS_BY_NAME = new ConcurrentHashMap<>();

    public static final NaiRealmEapCredType NAI_REALM_CRED_TYPE_SIM = new NaiRealmEapCredType(1, "SIM"),
            NAI_REALM_CRED_TYPE_USIM = new NaiRealmEapCredType(2, "USIM"),
            NAI_REALM_CRED_TYPE_NFC_SECURE_ELEMENT = new NaiRealmEapCredType(3, "NFC Secure Element"),
            NAI_REALM_CRED_TYPE_HARDWARE_TOKEN = new NaiRealmEapCredType(4, "Hardware Token"),
            NAI_REALM_CRED_TYPE_SOFTOKEN = new NaiRealmEapCredType(5, "Softoken"),
            NAI_REALM_CRED_TYPE_CERTIFICATE = new NaiRealmEapCredType(6, "Certificate"),
            NAI_REALM_CRED_TYPE_USERNAME_PASSWORD = new NaiRealmEapCredType(7, "username/password"),
            NAI_REALM_CRED_TYPE_NONE = new NaiRealmEapCredType(8, "none (server-side authentication only)"),
            NAI_REALM_CRED_TYPE_ANONYMOUS = new NaiRealmEapCredType(9, "Anonymous"),
            NAI_REALM_CRED_TYPE_VENDOR_SPECIFIC = new NaiRealmEapCredType(10, "Vendor Specific"),
            UNSUPPORTED = new NaiRealmEapCredType(-1, "UNSUPPORTED");
    static {
        // try to load all the subclasses explicitly - to avoid timing issues
        // when items coming from subclasses may be registered some time later,
        // after the parent class is loaded
        Set<Class<? extends NaiRealmEapCredType>> subclasses = BaseJsonModel.getReflections()
                .getSubTypesOf(NaiRealmEapCredType.class);
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

    protected NaiRealmEapCredType(int id, String name) {
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
    public static NaiRealmEapCredType[] values() {
        return new ArrayList<>(ELEMENTS.values()).toArray(new NaiRealmEapCredType[0]);
    }

    public static NaiRealmEapCredType getById(int enumId) {
        return ELEMENTS.get(enumId);
    }

    @JsonCreator
    public static NaiRealmEapCredType getByName(String value) {
        NaiRealmEapCredType ret = ELEMENTS_BY_NAME.get(value);
        if (ret == null) {
            ret = UNSUPPORTED;
        }

        return ret;
    }


    public static List<NaiRealmEapCredType> getValues() {
        return new ArrayList<>(ELEMENTS.values());
    }

    public static boolean isUnsupported(NaiRealmEapCredType value) {
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
        if (!(obj instanceof NaiRealmEapCredType)) {
            return false;
        }
        NaiRealmEapCredType other = (NaiRealmEapCredType) obj;
        return id == other.id && Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return name;
    }


}
