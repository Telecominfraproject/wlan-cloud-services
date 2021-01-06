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

public class WlanStatusCode implements EnumWithId {

    private static final Logger LOG = LoggerFactory.getLogger(WlanStatusCode.class);

    private static Object lock = new Object();
    private static final Map<Integer, WlanStatusCode> ELEMENTS = new ConcurrentHashMap<>();
    private static final Map<String, WlanStatusCode> ELEMENTS_BY_NAME = new ConcurrentHashMap<>();

    public static final WlanStatusCode 

    WLAN_STATUS_SUCCESS = new WlanStatusCode(0,"WLAN_STATUS_SUCCESS"),
    WLAN_STATUS_UNSPECIFIED_FAILURE = new WlanStatusCode(1,"WLAN_STATUS_UNSPECIFIED_FAILURE"),
    WLAN_STATUS_TDLS_WAKEUP_ALTERNATE = new WlanStatusCode(2,"WLAN_STATUS_TDLS_WAKEUP_ALTERNATE"),
    WLAN_STATUS_TDLS_WAKEUP_REJECT = new WlanStatusCode(3,"WLAN_STATUS_TDLS_WAKEUP_REJECT"),
    WLAN_STATUS_SECURITY_DISABLED = new WlanStatusCode(5,"WLAN_STATUS_SECURITY_DISABLED"),
    WLAN_STATUS_UNACCEPTABLE_LIFETIME = new WlanStatusCode(6,"WLAN_STATUS_UNACCEPTABLE_LIFETIME"),
    WLAN_STATUS_NOT_IN_SAME_BSS = new WlanStatusCode(7,"WLAN_STATUS_NOT_IN_SAME_BSS"),
    WLAN_STATUS_CAPS_UNSUPPORTED = new WlanStatusCode(10,"WLAN_STATUS_CAPS_UNSUPPORTED"),
    WLAN_STATUS_REASSOC_NO_ASSOC = new WlanStatusCode(11,"WLAN_STATUS_REASSOC_NO_ASSOC"),
    WLAN_STATUS_ASSOC_DENIED_UNSPEC = new WlanStatusCode(12,"WLAN_STATUS_ASSOC_DENIED_UNSPEC"),
    WLAN_STATUS_NOT_SUPPORTED_AUTH_ALG = new WlanStatusCode(13,"WLAN_STATUS_NOT_SUPPORTED_AUTH_ALG"),
    WLAN_STATUS_UNKNOWN_AUTH_TRANSACTION = new WlanStatusCode(14,"WLAN_STATUS_UNKNOWN_AUTH_TRANSACTION"),
    WLAN_STATUS_CHALLENGE_FAIL = new WlanStatusCode(15,"WLAN_STATUS_CHALLENGE_FAIL"),
    WLAN_STATUS_AUTH_TIMEOUT = new WlanStatusCode(16,"WLAN_STATUS_AUTH_TIMEOUT"),
    WLAN_STATUS_AP_UNABLE_TO_HANDLE_NEW_STA = new WlanStatusCode(17,"WLAN_STATUS_AP_UNABLE_TO_HANDLE_NEW_STA"),
    WLAN_STATUS_ASSOC_DENIED_RATES = new WlanStatusCode(18,"WLAN_STATUS_ASSOC_DENIED_RATES"),
    WLAN_STATUS_ASSOC_DENIED_NOSHORT = new WlanStatusCode(19,"WLAN_STATUS_ASSOC_DENIED_NOSHORT"),
    WLAN_STATUS_SPEC_MGMT_REQUIRED = new WlanStatusCode(22,"WLAN_STATUS_SPEC_MGMT_REQUIRED"),
    WLAN_STATUS_PWR_CAPABILITY_NOT_VALID = new WlanStatusCode(23,"WLAN_STATUS_PWR_CAPABILITY_NOT_VALID"),
    WLAN_STATUS_SUPPORTED_CHANNEL_NOT_VALID = new WlanStatusCode(24,"WLAN_STATUS_SUPPORTED_CHANNEL_NOT_VALID"),
    WLAN_STATUS_ASSOC_DENIED_NO_SHORT_SLOT_TIME = new WlanStatusCode(25,"WLAN_STATUS_ASSOC_DENIED_NO_SHORT_SLOT_TIME"),
    WLAN_STATUS_ASSOC_DENIED_NO_HT = new WlanStatusCode(27,"WLAN_STATUS_ASSOC_DENIED_NO_HT"),
    WLAN_STATUS_R0KH_UNREACHABLE = new WlanStatusCode(28,"WLAN_STATUS_R0KH_UNREACHABLE"),
    WLAN_STATUS_ASSOC_DENIED_NO_PCO = new WlanStatusCode(29,"WLAN_STATUS_ASSOC_DENIED_NO_PCO"),
    WLAN_STATUS_ASSOC_REJECTED_TEMPORARILY = new WlanStatusCode(30,"WLAN_STATUS_ASSOC_REJECTED_TEMPORARILY"),
    WLAN_STATUS_ROBUST_MGMT_FRAME_POLICY_VIOLATION = new WlanStatusCode(31,"WLAN_STATUS_ROBUST_MGMT_FRAME_POLICY_VIOLATION"),
    WLAN_STATUS_UNSPECIFIED_QOS_FAILURE = new WlanStatusCode(32,"WLAN_STATUS_UNSPECIFIED_QOS_FAILURE"),
    WLAN_STATUS_DENIED_INSUFFICIENT_BANDWIDTH = new WlanStatusCode(33,"WLAN_STATUS_DENIED_INSUFFICIENT_BANDWIDTH"),
    WLAN_STATUS_DENIED_POOR_CHANNEL_CONDITIONS = new WlanStatusCode(34,"WLAN_STATUS_DENIED_POOR_CHANNEL_CONDITIONS"),
    WLAN_STATUS_DENIED_QOS_NOT_SUPPORTED = new WlanStatusCode(35,"WLAN_STATUS_DENIED_QOS_NOT_SUPPORTED"),
    WLAN_STATUS_REQUEST_DECLINED = new WlanStatusCode(37,"WLAN_STATUS_REQUEST_DECLINED"),
    WLAN_STATUS_INVALID_PARAMETERS = new WlanStatusCode(38,"WLAN_STATUS_INVALID_PARAMETERS"),
    WLAN_STATUS_REJECTED_WITH_SUGGESTED_CHANGES = new WlanStatusCode(39,"WLAN_STATUS_REJECTED_WITH_SUGGESTED_CHANGES"),
    WLAN_STATUS_INVALID_IE = new WlanStatusCode(40,"WLAN_STATUS_INVALID_IE"),
    WLAN_STATUS_GROUP_CIPHER_NOT_VALID = new WlanStatusCode(41,"WLAN_STATUS_GROUP_CIPHER_NOT_VALID"),
    WLAN_STATUS_PAIRWISE_CIPHER_NOT_VALID = new WlanStatusCode(42,"WLAN_STATUS_PAIRWISE_CIPHER_NOT_VALID"),
    WLAN_STATUS_AKMP_NOT_VALID = new WlanStatusCode(43,"WLAN_STATUS_AKMP_NOT_VALID"),
    WLAN_STATUS_UNSUPPORTED_RSN_IE_VERSION = new WlanStatusCode(44,"WLAN_STATUS_UNSUPPORTED_RSN_IE_VERSION"),
    WLAN_STATUS_INVALID_RSN_IE_CAPAB = new WlanStatusCode(45,"WLAN_STATUS_INVALID_RSN_IE_CAPAB"),
    WLAN_STATUS_CIPHER_REJECTED_PER_POLICY = new WlanStatusCode(46,"WLAN_STATUS_CIPHER_REJECTED_PER_POLICY"),
    WLAN_STATUS_TS_NOT_CREATED = new WlanStatusCode(47,"WLAN_STATUS_TS_NOT_CREATED"),
    WLAN_STATUS_DIRECT_LINK_NOT_ALLOWED = new WlanStatusCode(48,"WLAN_STATUS_DIRECT_LINK_NOT_ALLOWED"),
    WLAN_STATUS_DEST_STA_NOT_PRESENT = new WlanStatusCode(49,"WLAN_STATUS_DEST_STA_NOT_PRESENT"),
    WLAN_STATUS_DEST_STA_NOT_QOS_STA = new WlanStatusCode(50,"WLAN_STATUS_DEST_STA_NOT_QOS_STA"),
    WLAN_STATUS_ASSOC_DENIED_LISTEN_INT_TOO_LARGE = new WlanStatusCode(51,"WLAN_STATUS_ASSOC_DENIED_LISTEN_INT_TOO_LARGE"),
    WLAN_STATUS_INVALID_FT_ACTION_FRAME_COUNT = new WlanStatusCode(52,"WLAN_STATUS_INVALID_FT_ACTION_FRAME_COUNT"),
    WLAN_STATUS_INVALID_PMKID = new WlanStatusCode(53,"WLAN_STATUS_INVALID_PMKID"),
    WLAN_STATUS_INVALID_MDIE = new WlanStatusCode(54,"WLAN_STATUS_INVALID_MDIE"),
    WLAN_STATUS_INVALID_FTIE = new WlanStatusCode(55,"WLAN_STATUS_INVALID_FTIE"),
    WLAN_STATUS_REQUESTED_TCLAS_NOT_SUPPORTED = new WlanStatusCode(56,"WLAN_STATUS_REQUESTED_TCLAS_NOT_SUPPORTED"),
    WLAN_STATUS_INSUFFICIENT_TCLAS_PROCESSING_RESOURCES = new WlanStatusCode(57,"WLAN_STATUS_INSUFFICIENT_TCLAS_PROCESSING_RESOURCES"),
    WLAN_STATUS_TRY_ANOTHER_BSS = new WlanStatusCode(58,"WLAN_STATUS_TRY_ANOTHER_BSS"),
    WLAN_STATUS_GAS_ADV_PROTO_NOT_SUPPORTED = new WlanStatusCode(59,"WLAN_STATUS_GAS_ADV_PROTO_NOT_SUPPORTED"),
    WLAN_STATUS_NO_OUTSTANDING_GAS_REQ = new WlanStatusCode(60,"WLAN_STATUS_NO_OUTSTANDING_GAS_REQ"),
    WLAN_STATUS_GAS_RESP_NOT_RECEIVED = new WlanStatusCode(61,"WLAN_STATUS_GAS_RESP_NOT_RECEIVED"),
    WLAN_STATUS_STA_TIMED_OUT_WAITING_FOR_GAS_RESP = new WlanStatusCode(62,"WLAN_STATUS_STA_TIMED_OUT_WAITING_FOR_GAS_RESP"),
    WLAN_STATUS_GAS_RESP_LARGER_THAN_LIMIT = new WlanStatusCode(63,"WLAN_STATUS_GAS_RESP_LARGER_THAN_LIMIT"),
    WLAN_STATUS_REQ_REFUSED_HOME = new WlanStatusCode(64,"WLAN_STATUS_REQ_REFUSED_HOME"),
    WLAN_STATUS_ADV_SRV_UNREACHABLE = new WlanStatusCode(65,"WLAN_STATUS_ADV_SRV_UNREACHABLE"),
    WLAN_STATUS_REQ_REFUSED_SSPN = new WlanStatusCode(67,"WLAN_STATUS_REQ_REFUSED_SSPN"),
    WLAN_STATUS_REQ_REFUSED_UNAUTH_ACCESS = new WlanStatusCode(68,"WLAN_STATUS_REQ_REFUSED_UNAUTH_ACCESS"),
    WLAN_STATUS_INVALID_RSNIE = new WlanStatusCode(72,"WLAN_STATUS_INVALID_RSNIE"),
    WLAN_STATUS_U_APSD_COEX_NOT_SUPPORTED = new WlanStatusCode(73,"WLAN_STATUS_U_APSD_COEX_NOT_SUPPORTED"),
    WLAN_STATUS_U_APSD_COEX_MODE_NOT_SUPPORTED = new WlanStatusCode(74,"WLAN_STATUS_U_APSD_COEX_MODE_NOT_SUPPORTED"),
    WLAN_STATUS_BAD_INTERVAL_WITH_U_APSD_COEX = new WlanStatusCode(75,"WLAN_STATUS_BAD_INTERVAL_WITH_U_APSD_COEX"),
    WLAN_STATUS_ANTI_CLOGGING_TOKEN_REQ = new WlanStatusCode(76,"WLAN_STATUS_ANTI_CLOGGING_TOKEN_REQ"),
    WLAN_STATUS_FINITE_CYCLIC_GROUP_NOT_SUPPORTED = new WlanStatusCode(77,"WLAN_STATUS_FINITE_CYCLIC_GROUP_NOT_SUPPORTED"),
    WLAN_STATUS_CANNOT_FIND_ALT_TBTT = new WlanStatusCode(78,"WLAN_STATUS_CANNOT_FIND_ALT_TBTT"),
    WLAN_STATUS_TRANSMISSION_FAILURE = new WlanStatusCode(79,"WLAN_STATUS_TRANSMISSION_FAILURE"),
    WLAN_STATUS_REQ_TCLAS_NOT_SUPPORTED = new WlanStatusCode(80,"WLAN_STATUS_REQ_TCLAS_NOT_SUPPORTED"),
    WLAN_STATUS_TCLAS_RESOURCES_EXCHAUSTED = new WlanStatusCode(81,"WLAN_STATUS_TCLAS_RESOURCES_EXCHAUSTED"),
    WLAN_STATUS_REJECTED_WITH_SUGGESTED_BSS_TRANSITION = new WlanStatusCode(82,"WLAN_STATUS_REJECTED_WITH_SUGGESTED_BSS_TRANSITION"),
    WLAN_STATUS_REJECT_WITH_SCHEDULE = new WlanStatusCode(83,"WLAN_STATUS_REJECT_WITH_SCHEDULE"),
    WLAN_STATUS_REJECT_NO_WAKEUP_SPECIFIED = new WlanStatusCode(84,"WLAN_STATUS_REJECT_NO_WAKEUP_SPECIFIED"),
    WLAN_STATUS_SUCCESS_POWER_SAVE_MODE = new WlanStatusCode(85,"WLAN_STATUS_SUCCESS_POWER_SAVE_MODE"),
    WLAN_STATUS_PENDING_ADMITTING_FST_SESSION = new WlanStatusCode(86,"WLAN_STATUS_PENDING_ADMITTING_FST_SESSION"),
    WLAN_STATUS_PERFORMING_FST_NOW = new WlanStatusCode(87,"WLAN_STATUS_PERFORMING_FST_NOW"),
    WLAN_STATUS_PENDING_GAP_IN_BA_WINDOW = new WlanStatusCode(88,"WLAN_STATUS_PENDING_GAP_IN_BA_WINDOW"),
    WLAN_STATUS_REJECT_U_PID_SETTING = new WlanStatusCode(89,"WLAN_STATUS_REJECT_U_PID_SETTING"),
    WLAN_STATUS_REFUSED_EXTERNAL_REASON = new WlanStatusCode(92,"WLAN_STATUS_REFUSED_EXTERNAL_REASON"),
    WLAN_STATUS_REFUSED_AP_OUT_OF_MEMORY = new WlanStatusCode(93,"WLAN_STATUS_REFUSED_AP_OUT_OF_MEMORY"),
    WLAN_STATUS_REJECTED_EMERGENCY_SERVICE_NOT_SUPPORTED = new WlanStatusCode(94,"WLAN_STATUS_REJECTED_EMERGENCY_SERVICE_NOT_SUPPORTED"),
    WLAN_STATUS_QUERY_RESP_OUTSTANDING = new WlanStatusCode(95,"WLAN_STATUS_QUERY_RESP_OUTSTANDING"),
    WLAN_STATUS_REJECT_DSE_BAND = new WlanStatusCode(96,"WLAN_STATUS_REJECT_DSE_BAND"),
    WLAN_STATUS_TCLAS_PROCESSING_TERMINATED = new WlanStatusCode(97,"WLAN_STATUS_TCLAS_PROCESSING_TERMINATED"),
    WLAN_STATUS_TS_SCHEDULE_CONFLICT = new WlanStatusCode(98,"WLAN_STATUS_TS_SCHEDULE_CONFLICT"),
    WLAN_STATUS_DENIED_WITH_SUGGESTED_BAND_AND_CHANNEL = new WlanStatusCode(99,"WLAN_STATUS_DENIED_WITH_SUGGESTED_BAND_AND_CHANNEL"),
    WLAN_STATUS_MCCAOP_RESERVATION_CONFLICT = new WlanStatusCode(100,"WLAN_STATUS_MCCAOP_RESERVATION_CONFLICT"),
    WLAN_STATUS_MAF_LIMIT_EXCEEDED = new WlanStatusCode(101,"WLAN_STATUS_MAF_LIMIT_EXCEEDED"),
    WLAN_STATUS_MCCA_TRACK_LIMIT_EXCEEDED = new WlanStatusCode(102,"WLAN_STATUS_MCCA_TRACK_LIMIT_EXCEEDED"),
    WLAN_STATUS_DENIED_DUE_TO_SPECTRUM_MANAGEMENT = new WlanStatusCode(103,"WLAN_STATUS_DENIED_DUE_TO_SPECTRUM_MANAGEMENT"),
    WLAN_STATUS_ASSOC_DENIED_NO_VHT = new WlanStatusCode(104,"WLAN_STATUS_ASSOC_DENIED_NO_VHT"),
    WLAN_STATUS_ENABLEMENT_DENIED = new WlanStatusCode(105,"WLAN_STATUS_ENABLEMENT_DENIED"),
    WLAN_STATUS_RESTRICTION_FROM_AUTHORIZED_GDB = new WlanStatusCode(106,"WLAN_STATUS_RESTRICTION_FROM_AUTHORIZED_GDB"),
    WLAN_STATUS_AUTHORIZATION_DEENABLED = new WlanStatusCode(107,"WLAN_STATUS_AUTHORIZATION_DEENABLED"),
    WLAN_STATUS_FILS_AUTHENTICATION_FAILURE = new WlanStatusCode(112,"WLAN_STATUS_FILS_AUTHENTICATION_FAILURE"),
    WLAN_STATUS_UNKNOWN_AUTHENTICATION_SERVER = new WlanStatusCode(113,"WLAN_STATUS_UNKNOWN_AUTHENTICATION_SERVER"),
    WLAN_STATUS_UNKNOWN_PASSWORD_IDENTIFIER = new WlanStatusCode(123,"WLAN_STATUS_UNKNOWN_PASSWORD_IDENTIFIER"),
    WLAN_STATUS_SAE_HASH_TO_ELEMENT = new WlanStatusCode(126,"WLAN_STATUS_SAE_HASH_TO_ELEMENT"),
    WLAN_STATUS_SAE_PK = new WlanStatusCode(127,"WLAN_STATUS_SAE_PK"),

    UNSUPPORTED  = new WlanStatusCode(-1, "UNSUPPORTED") ;

    static {
        //try to load all the subclasses explicitly - to avoid timing issues when items coming from subclasses may be registered some time later, after the parent class is loaded 
        Set<Class<? extends WlanStatusCode>> subclasses = BaseJsonModel.getReflections().getSubTypesOf(WlanStatusCode.class);
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

    protected WlanStatusCode(int id, String name) {
        synchronized(lock) {

            LOG.debug("Registering WlanStatusCode by {} : {}", this.getClass().getSimpleName(), name);

            this.id = id;
            this.name = name;

            ELEMENTS_BY_NAME.values().forEach(s -> {
                if(s.getName().equals(name)) {
                    throw new IllegalStateException("WlanStatusCode item for "+ name + " is already defined, cannot have more than one of them");
                }                
            });

            if(ELEMENTS.containsKey(id)) {
                throw new IllegalStateException("WlanStatusCode item "+ name + "("+id+") is already defined, cannot have more than one of them");
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

    public static WlanStatusCode getById(int enumId){
        return ELEMENTS.get(enumId);
    }

    @JsonCreator
    public static WlanStatusCode getByName(String value) {
        WlanStatusCode ret = ELEMENTS_BY_NAME.get(value);
        if (ret == null) {
            ret = UNSUPPORTED;
        }

        return ret;
    }


    public static List<WlanStatusCode> getValues() {
        return new ArrayList<>(ELEMENTS.values());
    }

    public static boolean isUnsupported(WlanStatusCode value) {
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
        if (!(obj instanceof WlanStatusCode)) {
            return false;
        }
        WlanStatusCode other = (WlanStatusCode) obj;
        return id == other.id;
    }   

    @Override
    public String toString() {
        return name;
    }

}
