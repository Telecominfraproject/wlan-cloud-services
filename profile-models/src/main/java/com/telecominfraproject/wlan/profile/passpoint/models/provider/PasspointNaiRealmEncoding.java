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


public class PasspointNaiRealmEncoding extends BaseJsonModel implements EnumWithId {

    private static final long serialVersionUID = -8656752874673129263L;

    private static final Logger LOG = LoggerFactory.getLogger(PasspointNaiRealmEncoding.class);

    private static Object lock = new Object();
    private static final Map<Integer, PasspointNaiRealmEncoding> ELEMENTS = new ConcurrentHashMap<>();
    private static final Map<String, PasspointNaiRealmEncoding> ELEMENTS_BY_NAME = new ConcurrentHashMap<>();

    public static final PasspointNaiRealmEncoding

    // # encoding:
    // # 0 = Realm formatted in accordance with IETF RFC 4282
    // # 1 = UTF-8 formatted character string that is not formatted in
    // accordance with IETF RFC 4282
    ietf_rfc_4282_encoding = new PasspointNaiRealmEncoding(0, "Realm formatted in accordance with IETF RFC 4282"),
            utf8_non_ietf_rfc_4282_encoding = new PasspointNaiRealmEncoding(1,
                    "UTF-8 formatted character string that is not formatted in accordance with IETF RFC 4282"),
            UNSUPPORTED = new PasspointNaiRealmEncoding(-1, "UNSUPPORTED");
    static {
        // try to load all the subclasses explicitly - to avoid timing issues
        // when items coming from subclasses may be registered some time later,
        // after the parent class is loaded
        Set<Class<? extends PasspointNaiRealmEncoding>> subclasses = BaseJsonModel.getReflections()
                .getSubTypesOf(PasspointNaiRealmEncoding.class);
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

    protected PasspointNaiRealmEncoding(int id, String name) {
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
    public static PasspointNaiRealmEncoding[] values() {
        return new ArrayList<>(ELEMENTS.values()).toArray(new PasspointNaiRealmEncoding[0]);
    }

    public static PasspointNaiRealmEncoding getById(int enumId) {
        return ELEMENTS.get(enumId);
    }

    @JsonCreator
    public static PasspointNaiRealmEncoding getByName(String value) {
        PasspointNaiRealmEncoding ret = ELEMENTS_BY_NAME.get(value);
        if (ret == null) {
            ret = UNSUPPORTED;
        }

        return ret;
    }


    public static List<PasspointNaiRealmEncoding> getValues() {
        return new ArrayList<>(ELEMENTS.values());
    }

    public static boolean isUnsupported(PasspointNaiRealmEncoding value) {
        return (UNSUPPORTED.equals(value));
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PasspointNaiRealmEncoding)) {
            return false;
        }
        PasspointNaiRealmEncoding other = (PasspointNaiRealmEncoding) obj;
        return id == other.id && Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return name;
    }


}
