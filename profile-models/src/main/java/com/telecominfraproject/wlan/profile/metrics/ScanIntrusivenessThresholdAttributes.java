package com.telecominfraproject.wlan.profile.metrics;

/**
 * @author mikehansen
 *
 */
public enum ScanIntrusivenessThresholdAttributes {
    MAX_DELAY, //      max delay of measurement when the threshold is reached - each sampling interval the threshold delta is used.
    PERCENT_UTILIZATION //       utilization percentage that still allows measurements
}