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


public class NaiRealmEncoding extends BaseJsonModel implements EnumWithId {

    private static final long serialVersionUID = -8656752874673129263L;

    private static final Logger LOG = LoggerFactory.getLogger(NaiRealmEncoding.class);

    private static Object lock = new Object();
    private static final Map<Integer, NaiRealmEncoding> ELEMENTS = new ConcurrentHashMap<>();
    private static final Map<String, NaiRealmEncoding> ELEMENTS_BY_NAME = new ConcurrentHashMap<>();

    public static final NaiRealmEncoding

    // # encoding:
    // # 0 = Realm formatted in accordance with IETF RFC 4282
    // # 1 = UTF-8 formatted character string that is not formatted in
    // accordance with IETF RFC 4282
    ietf_rfc_4282_encoding = new NaiRealmEncoding(0, "Realm formatted in accordance with IETF RFC 4282"),
            utf8_non_ietf_rfc_4282_encoding = new NaiRealmEncoding(1,
                    "UTF-8 formatted character string that is not formatted in accordance with IETF RFC 4282"),
            UNSUPPORTED = new NaiRealmEncoding(-1, "UNSUPPORTED");
    static {
        // try to load all the subclasses explicitly - to avoid timing issues
        // when items coming from subclasses may be registered some time later,
        // after the parent class is loaded
        Set<Class<? extends NaiRealmEncoding>> subclasses = BaseJsonModel.getReflections()
                .getSubTypesOf(NaiRealmEncoding.class);
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

    protected NaiRealmEncoding(int id, String name) {
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
    public static NaiRealmEncoding[] values() {
        return new ArrayList<>(ELEMENTS.values()).toArray(new NaiRealmEncoding[0]);
    }

    public static NaiRealmEncoding getById(int enumId) {
        return ELEMENTS.get(enumId);
    }

    @JsonCreator
    public static NaiRealmEncoding getByName(String value) {
        NaiRealmEncoding ret = ELEMENTS_BY_NAME.get(value);
        if (ret == null) {
            ret = UNSUPPORTED;
        }

        return ret;
    }


    public static List<NaiRealmEncoding> getValues() {
        return new ArrayList<>(ELEMENTS.values());
    }

    public static boolean isUnsupported(NaiRealmEncoding value) {
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
        if (!(obj instanceof NaiRealmEncoding)) {
            return false;
        }
        NaiRealmEncoding other = (NaiRealmEncoding) obj;
        return id == other.id && Objects.equals(name, other.name);
    }

    @Override
    public String toString() {
        return name;
    }


}
