package com.telecominfraproject.wlan.alarm.models;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.telecominfraproject.wlan.core.model.json.JsonDeserializationUtils;

/**
 * 
 * @author dtop
 *
 */
public enum AlarmCode {
    LimitedCloudConnectivity(3, "Equipment is connected, however it's not reporting status or metrics"),
    AccessPointIsUnreachable(4, "Equipment it not reachable from cloud"),
    NoMetricsReceived(6, "Equipment is not report metrics"),
    NoiseFloor2G(7, "Noise floor is too high on 2G radio"),
    ChannelUtilization2G(8, "Channel utilization is too high on 2G radio"),
    NoiseFloor5G(9, "Noise floor is too high on 5G radio"),
    ChannelUtilization5G(10, "Channel utilization is too high on 5G radio"),
    DNS(11, "Issue with Domain Name System (DNS)"),
    DNSLatency(12, "DNS query takes too long"),
    DHCP(13, "Issue with DHCP"),
    DHCPLatency(14, "DHCP request takes too long"),
    Radius(15, "Issue with RADIUS"),
    RadiusLatency(16, "RADIUS request takes too long"),
    CloudLink(17, "Issue reported by equipment with connection with Cloud"),
    CloudLinkLatency(18, "Cloud request take too long"),
    CPUUtilization(19, "CPU utilization is too high"),
    MemoryUtilization(20, "Memory utilization is too high"),
    Disconnected(22, "Equipment is not connected to the cloud"),
    CPUTemperature(23, "CPU Temperature is too high"),
    LowMemoryReboot(25, "Equipment rebooted due to low memory"),
    CountryCodeMisMatch(26, "Equipment country code does not match with location"),
    HardwareIssueDiagnostic(29, "Hardware issue encountered on equipment"), 
    TooManyClients2g(30, "Too many client devices on 2G radio"),
    TooManyClients5g(31, "Too many client devices on 5G radio"),
    RebootRequestFailed(32, "Failed to reboot equipment"),
    RadiusConfigurationFailed(33, "Failed to configure RADIUS on equipment"),
    FirmwareUpgradeStuck(34, "Firmware upgrade failed"),
    MultipleAPCsOnSameSubnet(35, "Multiple APC reported on the same subnet"),
    RadioHung2G(36, "Radio hung on 2G radio"),
    RadioHung5G(37, "Radio hung on 5G radio"), 
    ConfigurationOutOfSync(38, "Equipment configuration is out-of-sync from cloud configuration"),
    FailedCPAuthentications(40, "Captive portal authenticaiton request failed"),
    DisabledSSID(41, "SSID is disabled on equipment even though it is configured"),
    DeauthAttackDetected(42, "De-Auth attack detected"),
    TooManyBlacklistedDevices(45, "Too many black listed client devices provisioned"),
    TooManyRogueAPs(46, "Too many rogue APs provisioned"),
    NeighbourScanStuckOn2g(47, "Neighbour scan is stuck on 2G Radio"),
    NeighbourScanStuckOn5g(48, "Neighbour scan is stuck on 5G Radio"),
    InTroubleshootMode(49, "Equipment is in troubleshoot mode for too long"),
    ChannelsOutOfSync2g(50, "Channel configuration is out-of-sync from cloud configuration on 2G radio"), // in case the channel selection is out of sync
    ChannelsOutOfSync5g(51, "Channel configuration is out-of-sync from cloud configuration on 5G radio"), // in case the channel selection is out of sync
    InconsistentBasemacs(52, "Equipment base MAC address reported is different than the provisioned value."),
    GenericError(53, "Generic error encountered, detail in alarm text"),
    RadioHung(54, "Equipment reported radio hung"),
    AssocFailure(55, "Association failed"),
    ClientAuthFailure(56, "Client device authentication failed"),
    QoEIssues2g(57, "QoE issue detected on 2G radio"),
    QoEIssues5g(58, "QoE issue detected on 5G radio"),

    // for multiple DNS servers
    DNSServerUnreachable(67, "DNS Server is unreachable"),
    DNSServerLatency(68, "DNS Server query takes too long"),

    
    UNSUPPORTED(-1, "Alarm code is not supported by this release")
    ;
    
    private static final Map<Integer, AlarmCode> ELEMENTS = new HashMap<>();
    private static final Map<Integer, AlarmCode> VALID_VALUES = new TreeMap<>();

    public static AlarmCode getById(int enumId) {
        if (ELEMENTS.isEmpty()) {
            synchronized (ELEMENTS) {
                if (ELEMENTS.isEmpty()) {
                    // initialize elements map
                    for (AlarmCode met : AlarmCode.values()) {
                        ELEMENTS.put(met.getId(), met);
                    }
                }
            }
        }

        AlarmCode result = ELEMENTS.get(enumId);
        return result == null ? UNSUPPORTED : result;
    }

    @JsonCreator
    public static AlarmCode getByName(String value) {
        return JsonDeserializationUtils.deserializEnum(value, AlarmCode.class, UNSUPPORTED);
    }

    public static boolean isUnsupported(AlarmCode value) {
        return UNSUPPORTED.equals(value);
    }
    
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

    private final String description;
    
    private AlarmCode(int id, String description){
        this.id = id;
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }

    public int getId(){
        return this.id;
    }
}
