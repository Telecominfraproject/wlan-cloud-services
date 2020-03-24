package com.telecominfraproject.wlan.servicemetrics.models;

import java.util.Set;

import com.telecominfraproject.wlan.core.model.equipment.MacAddress;

/**
 * @author dtop
 */
public interface ClientMetricsInterface {
    Set<MacAddress> getDeviceMacAddresses();

    boolean containsClientMac(MacAddress clientMac);
}
