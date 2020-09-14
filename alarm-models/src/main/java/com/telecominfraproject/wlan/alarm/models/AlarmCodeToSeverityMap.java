package com.telecominfraproject.wlan.alarm.models;

import java.util.HashMap;
import java.util.Map;

import com.telecominfraproject.wlan.status.models.StatusCode;

/**
 * @author ekeddy
 *
 */
public class AlarmCodeToSeverityMap {
    private static Map<AlarmCode,StatusCode> mapping = new HashMap<>();

    static {

        // Start by initializing everything to an error.
        for(AlarmCode c: AlarmCode.values()) {
            mapping.put(c, StatusCode.error);
        }

        // Now adjust alarm codes that are not errors.
        mapping.put(AlarmCode.NoiseFloor2G, StatusCode.requiresAttention);
        mapping.put(AlarmCode.NoiseFloor5G, StatusCode.requiresAttention);

        mapping.put(AlarmCode.TooManyClients2g, StatusCode.requiresAttention);
        mapping.put(AlarmCode.TooManyClients5g, StatusCode.requiresAttention);

        mapping.put(AlarmCode.ChannelUtilization2G, StatusCode.requiresAttention);
        mapping.put(AlarmCode.ChannelUtilization5G, StatusCode.requiresAttention);

        mapping.put(AlarmCode.CPUUtilization, StatusCode.requiresAttention);
        mapping.put(AlarmCode.MemoryUtilization, StatusCode.requiresAttention);
        mapping.put(AlarmCode.CPUTemperature, StatusCode.requiresAttention);
        mapping.put(AlarmCode.CountryCodeMisMatch, StatusCode.requiresAttention);
        mapping.put(AlarmCode.FirmwareUpgradeStuck, StatusCode.requiresAttention);

        mapping.put(AlarmCode.DeauthAttackDetected, StatusCode.requiresAttention);
        mapping.put(AlarmCode.DHCP, StatusCode.requiresAttention);
        mapping.put(AlarmCode.DHCPLatency, StatusCode.requiresAttention);
        mapping.put(AlarmCode.DNS, StatusCode.requiresAttention);
        mapping.put(AlarmCode.DNSLatency, StatusCode.requiresAttention);
        mapping.put(AlarmCode.DNSServerUnreachable, StatusCode.requiresAttention);
        mapping.put(AlarmCode.DNSServerLatency, StatusCode.requiresAttention);


    }

    /**
     * Get the default severity for an alarm code.
     * @param alarmCode
     * @return
     */
    public static StatusCode get(AlarmCode alarmCode) {
        return mapping.get(alarmCode);
    }
}
