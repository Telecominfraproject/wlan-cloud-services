package com.telecominfraproject.wlan.client.models.events.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.extensibleenum.EnumWithId;
import com.telecominfraproject.wlan.core.model.json.BaseJsonModel;

public class WlanReasonCode implements EnumWithId {

    private static final Logger LOG = LoggerFactory.getLogger(WlanReasonCode.class);

    private static Object lock = new Object();
    private static final Map<Integer, WlanReasonCode> ELEMENTS = new ConcurrentHashMap<>();
    private static final Map<String, WlanReasonCode> ELEMENTS_BY_NAME = new ConcurrentHashMap<>();

    public static final WlanReasonCode 

    /* Reason codes (IEEE Std 802.11-2016, 9.4.1.7, Table 9-45) */
    WLAN_REASON_UNSPECIFIED = new WlanReasonCode( 1, "WLAN_REASON_UNSPECIFIED"),
    WLAN_REASON_PREV_AUTH_NOT_VALID = new WlanReasonCode(  2,"WLAN_REASON_PREV_AUTH_NOT_VALID"),
    WLAN_REASON_DEAUTH_LEAVING = new WlanReasonCode(  3,"WLAN_REASON_DEAUTH_LEAVING"),
    WLAN_REASON_DISASSOC_DUE_TO_INACTIVITY = new WlanReasonCode(  4,"WLAN_REASON_DISASSOC_DUE_TO_INACTIVITY"),
    WLAN_REASON_DISASSOC_AP_BUSY = new WlanReasonCode(  5,"WLAN_REASON_DISASSOC_AP_BUSY"),
    WLAN_REASON_CLASS2_FRAME_FROM_NONAUTH_STA = new WlanReasonCode(  6,"WLAN_REASON_CLASS2_FRAME_FROM_NONAUTH_STA"),
    WLAN_REASON_CLASS3_FRAME_FROM_NONASSOC_STA = new WlanReasonCode(  7,"WLAN_REASON_CLASS3_FRAME_FROM_NONASSOC_STA"),
    WLAN_REASON_DISASSOC_STA_HAS_LEFT = new WlanReasonCode(  8,"WLAN_REASON_DISASSOC_STA_HAS_LEFT"),
    WLAN_REASON_STA_REQ_ASSOC_WITHOUT_AUTH = new WlanReasonCode(  9,"WLAN_REASON_STA_REQ_ASSOC_WITHOUT_AUTH"),
    WLAN_REASON_PWR_CAPABILITY_NOT_VALID = new WlanReasonCode(  10,"WLAN_REASON_PWR_CAPABILITY_NOT_VALID"),
    WLAN_REASON_SUPPORTED_CHANNEL_NOT_VALID = new WlanReasonCode(  11,"WLAN_REASON_SUPPORTED_CHANNEL_NOT_VALID"),
    WLAN_REASON_BSS_TRANSITION_DISASSOC = new WlanReasonCode(  12,"WLAN_REASON_BSS_TRANSITION_DISASSOC"),
    WLAN_REASON_INVALID_IE = new WlanReasonCode(  13,"WLAN_REASON_INVALID_IE"),
    WLAN_REASON_MICHAEL_MIC_FAILURE = new WlanReasonCode(  14,"WLAN_REASON_MICHAEL_MIC_FAILURE"),
    WLAN_REASON_4WAY_HANDSHAKE_TIMEOUT = new WlanReasonCode(  15,"WLAN_REASON_4WAY_HANDSHAKE_TIMEOUT"),
    WLAN_REASON_GROUP_KEY_UPDATE_TIMEOUT = new WlanReasonCode(  16,"WLAN_REASON_GROUP_KEY_UPDATE_TIMEOUT"),
    WLAN_REASON_IE_IN_4WAY_DIFFERS = new WlanReasonCode(  17,"WLAN_REASON_IE_IN_4WAY_DIFFERS"),
    WLAN_REASON_GROUP_CIPHER_NOT_VALID = new WlanReasonCode(  18,"WLAN_REASON_GROUP_CIPHER_NOT_VALID"),
    WLAN_REASON_PAIRWISE_CIPHER_NOT_VALID = new WlanReasonCode(  19,"WLAN_REASON_PAIRWISE_CIPHER_NOT_VALID"),
    WLAN_REASON_AKMP_NOT_VALID = new WlanReasonCode(  20,"WLAN_REASON_AKMP_NOT_VALID"),
    WLAN_REASON_UNSUPPORTED_RSN_IE_VERSION = new WlanReasonCode(  21,"WLAN_REASON_UNSUPPORTED_RSN_IE_VERSION"),
    WLAN_REASON_INVALID_RSN_IE_CAPAB = new WlanReasonCode(  22,"WLAN_REASON_INVALID_RSN_IE_CAPAB"),
    WLAN_REASON_IEEE_802_1X_AUTH_FAILED = new WlanReasonCode(  23,"WLAN_REASON_IEEE_802_1X_AUTH_FAILED"),
    WLAN_REASON_CIPHER_SUITE_REJECTED = new WlanReasonCode(  24,"WLAN_REASON_CIPHER_SUITE_REJECTED"),
    WLAN_REASON_TDLS_TEARDOWN_UNREACHABLE = new WlanReasonCode(  25,"WLAN_REASON_TDLS_TEARDOWN_UNREACHABLE"),
    WLAN_REASON_TDLS_TEARDOWN_UNSPECIFIED = new WlanReasonCode(  26,"WLAN_REASON_TDLS_TEARDOWN_UNSPECIFIED"),
    WLAN_REASON_SSP_REQUESTED_DISASSOC = new WlanReasonCode(  27,"WLAN_REASON_SSP_REQUESTED_DISASSOC"),
    WLAN_REASON_NO_SSP_ROAMING_AGREEMENT = new WlanReasonCode(  28,"WLAN_REASON_NO_SSP_ROAMING_AGREEMENT"),
    WLAN_REASON_BAD_CIPHER_OR_AKM = new WlanReasonCode(  29,"WLAN_REASON_BAD_CIPHER_OR_AKM"),
    WLAN_REASON_NOT_AUTHORIZED_THIS_LOCATION = new WlanReasonCode(  30,"WLAN_REASON_NOT_AUTHORIZED_THIS_LOCATION"),
    WLAN_REASON_SERVICE_CHANGE_PRECLUDES_TS = new WlanReasonCode(  31,"WLAN_REASON_SERVICE_CHANGE_PRECLUDES_TS"),
    WLAN_REASON_UNSPECIFIED_QOS_REASON = new WlanReasonCode(  32,"WLAN_REASON_UNSPECIFIED_QOS_REASON"),
    WLAN_REASON_NOT_ENOUGH_BANDWIDTH = new WlanReasonCode(  33,"WLAN_REASON_NOT_ENOUGH_BANDWIDTH"),
    WLAN_REASON_DISASSOC_LOW_ACK = new WlanReasonCode(  34,"WLAN_REASON_DISASSOC_LOW_ACK"),
    WLAN_REASON_EXCEEDED_TXOP = new WlanReasonCode(  35,"WLAN_REASON_EXCEEDED_TXOP"),
    WLAN_REASON_STA_LEAVING = new WlanReasonCode(  36,"WLAN_REASON_STA_LEAVING"),
    WLAN_REASON_END_TS_BA_DLS = new WlanReasonCode(  37,"WLAN_REASON_END_TS_BA_DLS"),
    WLAN_REASON_UNKNOWN_TS_BA = new WlanReasonCode(  38,"WLAN_REASON_UNKNOWN_TS_BA"),
    WLAN_REASON_TIMEOUT = new WlanReasonCode(  39,"WLAN_REASON_TIMEOUT"),
    WLAN_REASON_PEERKEY_MISMATCH = new WlanReasonCode(  45,"WLAN_REASON_PEERKEY_MISMATCH"),
    WLAN_REASON_AUTHORIZED_ACCESS_LIMIT_REACHED = new WlanReasonCode(  46,"WLAN_REASON_AUTHORIZED_ACCESS_LIMIT_REACHED"),
    WLAN_REASON_EXTERNAL_SERVICE_REQUIREMENTS = new WlanReasonCode(  47,"WLAN_REASON_EXTERNAL_SERVICE_REQUIREMENTS"),
    WLAN_REASON_INVALID_FT_ACTION_FRAME_COUNT = new WlanReasonCode(  48,"WLAN_REASON_INVALID_FT_ACTION_FRAME_COUNT"),
    WLAN_REASON_INVALID_PMKID = new WlanReasonCode(  49,"WLAN_REASON_INVALID_PMKID"),
    WLAN_REASON_INVALID_MDE = new WlanReasonCode(  50,"WLAN_REASON_INVALID_MDE"),
    WLAN_REASON_INVALID_FTE = new WlanReasonCode(  51,"WLAN_REASON_INVALID_FTE"),
    WLAN_REASON_MESH_PEERING_CANCELLED = new WlanReasonCode(  52,"WLAN_REASON_MESH_PEERING_CANCELLED"),
    WLAN_REASON_MESH_MAX_PEERS = new WlanReasonCode(  53,"WLAN_REASON_MESH_MAX_PEERS"),
    WLAN_REASON_MESH_CONFIG_POLICY_VIOLATION = new WlanReasonCode(  54,"WLAN_REASON_MESH_CONFIG_POLICY_VIOLATION"),
    WLAN_REASON_MESH_CLOSE_RCVD = new WlanReasonCode(  55,"WLAN_REASON_MESH_CLOSE_RCVD"),
    WLAN_REASON_MESH_MAX_RETRIES = new WlanReasonCode(  56,"WLAN_REASON_MESH_MAX_RETRIES"),
    WLAN_REASON_MESH_CONFIRM_TIMEOUT = new WlanReasonCode(  57,"WLAN_REASON_MESH_CONFIRM_TIMEOUT"),
    WLAN_REASON_MESH_INVALID_GTK  = new WlanReasonCode( 58,"WLAN_REASON_MESH_INVALID_GTK"),
    WLAN_REASON_MESH_INCONSISTENT_PARAMS = new WlanReasonCode(  59,"WLAN_REASON_MESH_INCONSISTENT_PARAMS"),
    WLAN_REASON_MESH_INVALID_SECURITY_CAP = new WlanReasonCode(  60,"WLAN_REASON_MESH_INVALID_SECURITY_CAP"),
    WLAN_REASON_MESH_PATH_ERROR_NO_PROXY_INFO = new WlanReasonCode(  61,"WLAN_REASON_MESH_PATH_ERROR_NO_PROXY_INFO"),
    WLAN_REASON_MESH_PATH_ERROR_NO_FORWARDING_INFO = new WlanReasonCode(  62,"WLAN_REASON_MESH_PATH_ERROR_NO_FORWARDING_INFO"),
    WLAN_REASON_MESH_PATH_ERROR_DEST_UNREACHABLE = new WlanReasonCode(  63,"WLAN_REASON_MESH_PATH_ERROR_DEST_UNREACHABLE"),
    WLAN_REASON_MAC_ADDRESS_ALREADY_EXISTS_IN_MBSS = new WlanReasonCode(  64,"WLAN_REASON_MAC_ADDRESS_ALREADY_EXISTS_IN_MBSS"),
    WLAN_REASON_MESH_CHANNEL_SWITCH_REGULATORY_REQ = new WlanReasonCode(  65,"WLAN_REASON_MESH_CHANNEL_SWITCH_REGULATORY_REQ"),
    WLAN_REASON_MESH_CHANNEL_SWITCH_UNSPECIFIED = new WlanReasonCode(  66,"WLAN_REASON_MESH_CHANNEL_SWITCH_UNSPECIFIED"),

    UNSUPPORTED  = new WlanReasonCode(-1, "UNSUPPORTED") ;

    static {
        //try to load all the subclasses explicitly - to avoid timing issues when items coming from subclasses may be registered some time later, after the parent class is loaded 
        Set<Class<? extends WlanReasonCode>> subclasses = BaseJsonModel.getReflections().getSubTypesOf(WlanReasonCode.class);
        for(Class<?> cls: subclasses) {
            try {
                Class.forName(cls.getName());
            } catch (ClassNotFoundException e) {
                LOG.warn("Cannot load class {} : {}", cls.getName(), e);
            }
        }
    }  

    private final int id;
    private final String name;

    protected WlanReasonCode(int id, String name) {
        synchronized(lock) {

            LOG.debug("Registering WlanReasonCode by {} : {}", this.getClass().getSimpleName(), name);

            this.id = id;
            this.name = name;

            ELEMENTS_BY_NAME.values().forEach(s -> {
                if(s.getName().equals(name)) {
                    throw new IllegalStateException("WlanReasonCode item for "+ name + " is already defined, cannot have more than one of them");
                }                
            });

            if(ELEMENTS.containsKey(id)) {
                throw new IllegalStateException("WlanReasonCode item "+ name + "("+id+") is already defined, cannot have more than one of them");
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

    public static WlanReasonCode getById(int enumId){
        return ELEMENTS.get(enumId);
    }

    @JsonCreator
    public static WlanReasonCode getByName(String value) {
        WlanReasonCode ret = ELEMENTS_BY_NAME.get(value);
        if (ret == null) {
            ret = UNSUPPORTED;
        }

        return ret;
    }


    public static List<WlanReasonCode> getValues() {
        return new ArrayList<>(ELEMENTS.values());
    }

    public static boolean isUnsupported(WlanReasonCode value) {
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
        if (!(obj instanceof WlanReasonCode)) {
            return false;
        }
        WlanReasonCode other = (WlanReasonCode) obj;
        return id == other.id;
    }   

    @Override
    public String toString() {
        return name;
    }

}
