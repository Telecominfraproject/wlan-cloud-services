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


public class PasspointConnectionCapabilitiesIpProtocol implements EnumWithId {

    // # Connection Capability
    // # This can be used to advertise what type of IP traffic can be sent
    // through the
    // # hotspot (e.g., due to firewall allowing/blocking protocols/ports).
    // # format: <IP Protocol>:<Port Number>:<Status>
    // # IP Protocol: 1 = ICMP, 6 = TCP, 17 = UDP
    // # Port Number: 0..65535
    // # Status: 0 = Closed, 1 = Open, 2 = Unknown
    // # Each hs20_conn_capab line is added to the list of advertised tuples.
    // #hs20_conn_capab=1:0:2
    // #hs20_conn_capab=6:22:1
    // #hs20_conn_capab=17:5060:0


    private static final Logger LOG = LoggerFactory.getLogger(PasspointConnectionCapabilitiesIpProtocol.class);

    private static Object lock = new Object();
    private static final Map<Integer, PasspointConnectionCapabilitiesIpProtocol> ELEMENTS = new ConcurrentHashMap<>();
    private static final Map<String, PasspointConnectionCapabilitiesIpProtocol> ELEMENTS_BY_NAME = new ConcurrentHashMap<>();

    public static final PasspointConnectionCapabilitiesIpProtocol

    ICMP = new PasspointConnectionCapabilitiesIpProtocol(1, "ICMP"), TCP = new PasspointConnectionCapabilitiesIpProtocol(6, "TCP"), UDP = new PasspointConnectionCapabilitiesIpProtocol(17, "UDP"),

            UNSUPPORTED = new PasspointConnectionCapabilitiesIpProtocol(-1, "UNSUPPORTED");

    static {
        // try to load all the subclasses explicitly - to avoid timing issues
        // when items coming from subclasses may be registered some time later,
        // after the parent class is loaded
        Set<Class<? extends PasspointConnectionCapabilitiesIpProtocol>> subclasses = BaseJsonModel.getReflections().getSubTypesOf(PasspointConnectionCapabilitiesIpProtocol.class);
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

    protected PasspointConnectionCapabilitiesIpProtocol(int id, String name) {
        synchronized (lock) {

            LOG.debug("Registering IpProtocol by {} : {}", this.getClass().getSimpleName(), name);

            this.id = id;
            this.name = name;
            ELEMENTS_BY_NAME.values().forEach(s -> {
                if (s.getName().equals(name)) {
                    throw new IllegalStateException(
                            "IpProtocol item for " + name + " is already defined, cannot have more than one of them");
                }
            });

            if (ELEMENTS.containsKey(id)) {
                throw new IllegalStateException("IpProtocol item " + name + "(" + id
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
    public static PasspointConnectionCapabilitiesIpProtocol[] values() {
        return new ArrayList<>(ELEMENTS.values()).toArray(new PasspointConnectionCapabilitiesIpProtocol[0]);
    }

    public static PasspointConnectionCapabilitiesIpProtocol getById(int enumId) {
        return ELEMENTS.get(enumId);
    }

    @JsonCreator
    public static PasspointConnectionCapabilitiesIpProtocol getByName(String value) {
        PasspointConnectionCapabilitiesIpProtocol ret = ELEMENTS_BY_NAME.get(value);
        if (ret == null) {
            ret = UNSUPPORTED;
        }

        return ret;
    }


    public static List<PasspointConnectionCapabilitiesIpProtocol> getValues() {
        return new ArrayList<>(ELEMENTS.values());
    }

    public static boolean isUnsupported(PasspointConnectionCapabilitiesIpProtocol value) {
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
        if (!(obj instanceof PasspointConnectionCapabilitiesIpProtocol)) {
            return false;
        }
        PasspointConnectionCapabilitiesIpProtocol other = (PasspointConnectionCapabilitiesIpProtocol) obj;
        return id == other.id;
    }

    @Override
    public String toString() {
        return name;
    }


}
