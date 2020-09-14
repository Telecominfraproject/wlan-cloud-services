package com.telecominfraproject.wlan.alarm.models;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.telecominfraproject.wlan.core.model.extensibleenum.EnumWithId;

/**
 * All available Alarm codes that can be handled by the CloudSDK. 
 * <br>This enumeration-like class can be extended by vendors - new elements can be defined by extending this class like so:
 * <br>
 * <pre>
 * public class VendorExtendedAlarmCode extends AlarmCode {
 *    
 *    public static final AlarmCode 
 *    VENDOR_AC_A = new VendorExtendedAlarmCode(500, "VENDOR_AC_A", "description A") ,
 *    VENDOR_AC_B = new VendorExtendedAlarmCode(501, "VENDOR_AC_B", "description B")
 *    ;
 *
 *    private VendorExtendedAlarmCode(int id, String name, String description) {
 *        super(id, name, description);
 *    }
 *
 * }
 * </pre>
 * @see com.telecominfraproject.wlan.core.model.extensibleenum.EnumWithId
 * @see com.telecominfraproject.wlan.alarm.models.vendorextensions.TestVendorExtendedAlarmModel
 * <br>
 * @author dtop
 *
 */
public class AlarmCode implements EnumWithId {

    private static Object lock = new Object();
    private static final Map<Integer, AlarmCode> ELEMENTS = new ConcurrentHashMap<>();
    private static final Map<String, AlarmCode> ELEMENTS_BY_NAME = new ConcurrentHashMap<>();
    private static final Map<Integer, AlarmCode> VALID_VALUES = new ConcurrentHashMap<>();


    public static final AlarmCode 
    LimitedCloudConnectivity = new AlarmCode(3, "LimitedCloudConnectivity", "Equipment is connected, however it's not reporting status or metrics"),
    AccessPointIsUnreachable = new AlarmCode(4, "AccessPointIsUnreachable", "Equipment is not reachable from cloud"),
    NoMetricsReceived = new AlarmCode(6, "NoMetricsReceived", "Equipment is not report metrics"),
    NoiseFloor2G = new AlarmCode(7, "NoiseFloor2G", "Noise floor is too high on 2G radio"),
    ChannelUtilization2G = new AlarmCode(8, "ChannelUtilization2G", "Channel utilization is too high on 2G radio"),
    NoiseFloor5G = new AlarmCode(9, "NoiseFloor5G", "Noise floor is too high on 5G radio"),
    ChannelUtilization5G = new AlarmCode(10, "ChannelUtilization5G", "Channel utilization is too high on 5G radio"),
    DNS = new AlarmCode(11, "DNS", "Issue with Domain Name System (DNS)"),
    DNSLatency = new AlarmCode(12, "DNSLatency", "DNS query takes too long"),
    DHCP = new AlarmCode(13, "DHCP", "Issue with DHCP"),
    DHCPLatency = new AlarmCode(14, "DHCPLatency", "DHCP request takes too long"),
    Radius = new AlarmCode(15, "Radius", "Issue with RADIUS"),
    RadiusLatency = new AlarmCode(16, "RadiusLatency", "RADIUS request takes too long"),
    CloudLink = new AlarmCode(17, "CloudLink", "Issue reported by equipment with connection with Cloud"),
    CloudLinkLatency = new AlarmCode(18, "CloudLinkLatency", "Cloud request take too long"),
    CPUUtilization = new AlarmCode(19, "CPUUtilization", "CPU utilization is too high"),
    MemoryUtilization = new AlarmCode(20, "MemoryUtilization", "Memory utilization is too high"),
    Disconnected = new AlarmCode(22, "Disconnected", "Equipment is not connected to the cloud"),
    CPUTemperature = new AlarmCode(23, "CPUTemperature", "CPU Temperature is too high"),
    LowMemoryReboot = new AlarmCode(25, "LowMemoryReboot", "Equipment rebooted due to low memory"),
    CountryCodeMisMatch = new AlarmCode(26, "CountryCodeMisMatch", "Equipment country code does not match with location"),
    HardwareIssueDiagnostic = new AlarmCode(29, "HardwareIssueDiagnostic", "Hardware issue encountered on equipment"), 
    TooManyClients2g = new AlarmCode(30, "TooManyClients2g", "Too many client devices on 2G radio"),
    TooManyClients5g = new AlarmCode(31, "TooManyClients5g", "Too many client devices on 5G radio"),
    RebootRequestFailed = new AlarmCode(32, "RebootRequestFailed", "Failed to reboot equipment"),
    RadiusConfigurationFailed = new AlarmCode(33, "RadiusConfigurationFailed", "Failed to configure RADIUS on equipment"),
    FirmwareUpgradeStuck = new AlarmCode(34, "FirmwareUpgradeStuck", "Firmware upgrade failed"),
    MultipleAPCsOnSameSubnet = new AlarmCode(35, "MultipleAPCsOnSameSubnet", "Multiple APC reported on the same subnet"),
    RadioHung2G = new AlarmCode(36, "RadioHung2G", "Radio hung on 2G radio"),
    RadioHung5G = new AlarmCode(37, "RadioHung5G", "Radio hung on 5G radio"), 
    ConfigurationOutOfSync = new AlarmCode(38, "ConfigurationOutOfSync", "Equipment configuration is out-of-sync from cloud configuration"),
    FailedCPAuthentications = new AlarmCode(40, "FailedCPAuthentications", "Captive portal authenticaiton request failed"),
    DisabledSSID = new AlarmCode(41, "DisabledSSID", "SSID is disabled on equipment even though it is configured"),
    DeauthAttackDetected = new AlarmCode(42, "DeauthAttackDetected", "De-Auth attack detected"),
    TooManyBlockedDevices = new AlarmCode(45, "TooManyBlockedDevices", "Too many blocked client devices provisioned"),
    TooManyRogueAPs = new AlarmCode(46, "TooManyRogueAPs", "Too many rogue APs provisioned"),
    NeighbourScanStuckOn2g = new AlarmCode(47, "NeighbourScanStuckOn2g", "Neighbour scan is stuck on 2G Radio"),
    NeighbourScanStuckOn5g = new AlarmCode(48, "NeighbourScanStuckOn5g", "Neighbour scan is stuck on 5G Radio"),
    InTroubleshootMode = new AlarmCode(49, "InTroubleshootMode", "Equipment is in troubleshoot mode for too long"),
    ChannelsOutOfSync2g = new AlarmCode(50, "ChannelsOutOfSync2g", "Channel configuration is out-of-sync from cloud configuration on 2G radio"), // in case the channel selection is out of sync
    ChannelsOutOfSync5g = new AlarmCode(51, "ChannelsOutOfSync5g", "Channel configuration is out-of-sync from cloud configuration on 5G radio"), // in case the channel selection is out of sync
    InconsistentBasemacs = new AlarmCode(52, "InconsistentBasemacs", "Equipment base MAC address reported is different than the provisioned value."),
    GenericError = new AlarmCode(53, "GenericError", "Generic error encountered, detail in alarm text"),
    RadioHung = new AlarmCode(54, "RadioHung", "Equipment reported radio hung"),
    AssocFailure = new AlarmCode(55, "AssocFailure", "Association failed"),
    ClientAuthFailure = new AlarmCode(56, "ClientAuthFailure", "Client device authentication failed"),
    QoEIssues2g = new AlarmCode(57, "QoEIssues2g", "QoE issue detected on 2G radio"),
    QoEIssues5g = new AlarmCode(58, "QoEIssues5g", "QoE issue detected on 5G radio"),

    // for multiple DNS servers
    DNSServerUnreachable = new AlarmCode(67, "DNSServerUnreachable", "DNS Server is unreachable"),
    DNSServerLatency = new AlarmCode(68, "DNSServerLatency", "DNS Server query takes too long"),

    
    UNSUPPORTED = new AlarmCode(-1, "UNSUPPORTED", "Alarm code is not supported by this release")
    ;
    
    
    public static AlarmCode[] validValues() {
        if (VALID_VALUES.isEmpty()) {
            synchronized (VALID_VALUES) {
                if (VALID_VALUES.isEmpty()) {
                    for (AlarmCode met : AlarmCode.values()) {
                        // skip un-supported
                        if (isUnsupported(met)) {
                            continue;
                        }
                        // skip deprecated
                        try {
                            Field field = AlarmCode.class.getField(met.name());
                            if (field.isAnnotationPresent(Deprecated.class)) {
                                continue;
                            }
                        } catch (NoSuchFieldException e) {
                            continue;
                        } catch (SecurityException e) {
                            continue;
                        }

                        VALID_VALUES.put(met.getId(), met);
                    }
                }                
            }
        }
        return VALID_VALUES.values().toArray(new AlarmCode[VALID_VALUES.size()]);
    }
    
    private final int id;
    private final String name;
    private final String description;
    
    protected AlarmCode(int id, String name, String description){
        
        synchronized(lock) {
            this.id = id;
            this.name = name;
            this.description = description;

            ELEMENTS_BY_NAME.values().forEach(s -> {
                if(s.getName().equals(name)) {
                    throw new IllegalStateException("AlarmCode item for "+ name + " is already defined, cannot have more than one of them");
                }                
            });
    
            if(ELEMENTS.containsKey(id)) {
                throw new IllegalStateException("AlarmCode item "+ name + "("+id+") is already defined, cannot have more than one of them");
            }
    
            ELEMENTS.put(id, this);
            ELEMENTS_BY_NAME.put(name, this);
            
            //add the item to VALID_VALUES if it's not UNSUPPORTED and not @Deprecated
            if(!name.equals("UNSUPPORTED")){
                // skip deprecated
                try {
                    Field field = AlarmCode.class.getField(name);
                    if (!field.isAnnotationPresent(Deprecated.class)) {
                        VALID_VALUES.put(id, this);
                    }
                } catch (NoSuchFieldException e) {
                    //do nothing
                } catch (SecurityException e) {
                    //do nothing
                }
            }

        }

    }
    
    public String getDescription() {
        return description;
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
    public static AlarmCode[] values() {
        return new ArrayList<>(ELEMENTS.values()).toArray(new AlarmCode[0]);
    }

    public static AlarmCode getById(int enumId){
        return ELEMENTS.get(enumId);
    }
    
    @JsonCreator
    public static AlarmCode getByName(String value) {
        AlarmCode ret = ELEMENTS_BY_NAME.get(value);
        if (ret == null) {
            ret = UNSUPPORTED;
        }
        
        return ret;
    }


    public static List<AlarmCode> getValues() {
        return new ArrayList<>(ELEMENTS.values());
    }
    
    public static boolean isUnsupported(AlarmCode value) {
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
        if (!(obj instanceof AlarmCode)) {
            return false;
        }
        AlarmCode other = (AlarmCode) obj;
        return id == other.id;
    }   

    @Override
    public String toString() {
        return name;
    }
}
